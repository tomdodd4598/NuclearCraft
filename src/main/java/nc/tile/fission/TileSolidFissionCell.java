package nc.tile.fission;

import static nc.config.NCConfig.*;
import static nc.util.PosHelper.DEFAULT_NON;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import javax.annotation.*;

import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.objects.*;
import nc.Global;
import nc.capability.radiation.source.IRadiationSource;
import nc.container.ContainerFunction;
import nc.container.processor.ContainerMachineConfig;
import nc.gui.*;
import nc.gui.processor.*;
import nc.handler.TileInfoHandler;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.*;
import nc.network.tile.multiblock.SolidFissionCellUpdatePacket;
import nc.radiation.RadiationHelper;
import nc.recipe.*;
import nc.tile.fission.TileSolidFissionCell.SolidFissionCellContainerInfo;
import nc.tile.fission.port.*;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.*;
import nc.tile.internal.inventory.*;
import nc.tile.inventory.*;
import nc.tile.processor.IProcessor;
import nc.tile.processor.info.ProcessorContainerInfo;
import nc.tile.processor.info.builder.ProcessorContainerInfoBuilder;
import nc.util.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.items.*;

public class TileSolidFissionCell extends TileFissionPart implements IProcessor<TileSolidFissionCell, SolidFissionCellUpdatePacket, SolidFissionCellContainerInfo>, ITileFilteredInventory, IFissionFuelComponent, IFissionPortTarget<TileFissionCellPort, TileSolidFissionCell> {
	
	public static class SolidFissionCellContainerInfo extends ProcessorContainerInfo<TileSolidFissionCell, SolidFissionCellUpdatePacket, SolidFissionCellContainerInfo> {
		
		public SolidFissionCellContainerInfo(String modId, String name, Class<? extends Container> containerClass, ContainerFunction<TileSolidFissionCell> containerFunction, Class<? extends GuiContainer> guiClass, GuiFunction<TileSolidFissionCell> guiFunction, ContainerFunction<TileSolidFissionCell> configContainerFunction, GuiFunction<TileSolidFissionCell> configGuiFunction, int inputTankCapacity, int outputTankCapacity, double defaultProcessTime, double defaultProcessPower, boolean isGenerator, boolean consumesInputs, boolean losesProgress, String ocComponentName, int[] guiWH, List<int[]> itemInputGuiXYWH, List<int[]> fluidInputGuiXYWH, List<int[]> itemOutputGuiXYWH, List<int[]> fluidOutputGuiXYWH, int[] playerGuiXY, int[] progressBarGuiXYWHUV, int[] energyBarGuiXYWHUV, int[] machineConfigGuiXY, int[] redstoneControlGuiXY, boolean jeiCategoryEnabled, String jeiCategoryUid, String jeiTitle, String jeiTexture, int[] jeiBackgroundXYWH, int[] jeiTooltipXYWH, int[] jeiClickAreaXYWH) {
			super(modId, name, containerClass, containerFunction, guiClass, guiFunction, configContainerFunction, configGuiFunction, inputTankCapacity, outputTankCapacity, defaultProcessTime, defaultProcessPower, isGenerator, consumesInputs, losesProgress, ocComponentName, guiWH, itemInputGuiXYWH, fluidInputGuiXYWH, itemOutputGuiXYWH, fluidOutputGuiXYWH, playerGuiXY, progressBarGuiXYWHUV, energyBarGuiXYWHUV, machineConfigGuiXY, redstoneControlGuiXY, jeiCategoryEnabled, jeiCategoryUid, jeiTitle, jeiTexture, jeiBackgroundXYWH, jeiTooltipXYWH, jeiClickAreaXYWH);
		}
	}
	
	public static class SolidFissionCellContainerInfoBuilder extends ProcessorContainerInfoBuilder<TileSolidFissionCell, SolidFissionCellUpdatePacket, SolidFissionCellContainerInfo, SolidFissionCellContainerInfoBuilder> {
		
		public SolidFissionCellContainerInfoBuilder(String modId, String name, Class<TileSolidFissionCell> tileClass, Supplier<TileSolidFissionCell> tileSupplier, Class<? extends Container> containerClass, ContainerFunction<TileSolidFissionCell> containerFunction, Class<? extends GuiContainer> guiClass, GuiInfoTileFunction<TileSolidFissionCell> guiFunction) {
			super(modId, name, tileClass, tileSupplier, containerClass, containerFunction, guiClass, GuiFunction.of(modId, name, containerFunction, guiFunction), ContainerMachineConfig::new, GuiFunction.of(modId, name, ContainerMachineConfig::new, GuiProcessor.SideConfig::new));
			infoFunction = SolidFissionCellContainerInfo::new;
		}
	}
	
	protected final SolidFissionCellContainerInfo info;
	
	protected final @Nonnull String inventoryName;
	
	protected final @Nonnull NonNullList<ItemStack> inventoryStacks;
	protected final @Nonnull NonNullList<ItemStack> consumedStacks;
	
	protected final @Nonnull NonNullList<ItemStack> filterStacks;
	
	protected @Nonnull InventoryConnection[] inventoryConnections;
	
	protected final @Nonnull List<Tank> tanks;
	protected final @Nonnull List<Tank> consumedTanks;
	
	protected @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(Arrays.asList());
	
	protected @Nonnull FluidTileWrapper[] fluidSides = ITileFluid.getDefaultFluidSides(this);
	protected @Nonnull GasTileWrapper gasWrapper = new GasTileWrapper(this);
	
	public double baseProcessTime = 1D, baseProcessEfficiency = 0D, baseProcessDecayFactor = 0D, baseProcessRadiation = 0D;
	public int baseProcessHeat = 0, baseProcessCriticality = 1;
	protected boolean selfPriming = false;
	
	public double time, resetTime;
	public boolean isProcessing, canProcessInputs, hasConsumed;
	
	public double decayProcessHeat = 0D, decayHeatFraction = 0D, iodineFraction = 0D, poisonFraction = 0D;
	
	protected RecipeInfo<BasicRecipe> recipeInfo = null;
	
	protected final Set<EntityPlayer> updatePacketListeners = new ObjectOpenHashSet<>();
	
	protected FissionCluster cluster = null;
	protected long heat = 0L;
	
	public long clusterHeatStored, clusterHeatCapacity;
	
	protected boolean primed = false, fluxSearched = false;
	protected long flux = 0;
	
	public int heatMult = 0;
	protected double undercoolingLifetimeFactor = 1D;
	protected Double sourceEfficiency = null;
	protected long[] moderatorLineFluxes = new long[] {0L, 0L, 0L, 0L, 0L, 0L};
	protected Double[] moderatorLineEfficiencies = new Double[] {null, null, null, null, null, null};
	protected IFissionFluxSink[] adjacentFluxSinks = new IFissionFluxSink[] {null, null, null, null, null, null};
	protected final LongSet[] passiveModeratorCaches = new LongSet[] {new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet()};
	protected final LongSet[] activeModeratorCaches = new LongSet[] {new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet()};
	protected final ModeratorLine[] moderatorLineCaches = new ModeratorLine[] {null, null, null, null, null, null};
	protected final LongSet[] passiveReflectorModeratorCaches = new LongSet[] {new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet()};
	protected final LongSet[] activeReflectorModeratorCaches = new LongSet[] {new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet()};
	protected final LongSet activeReflectorCache = new LongOpenHashSet();
	
	protected BlockPos masterPortPos = DEFAULT_NON;
	protected TileFissionCellPort masterPort = null;
	
	public TileSolidFissionCell() {
		super(CuboidalPartPositionType.INTERIOR);
		info = TileInfoHandler.getProcessorContainerInfo("solid_fission");
		
		inventoryName = Global.MOD_ID + ".container." + info.name;
		
		inventoryStacks = info.getInventoryStacks();
		consumedStacks = info.getConsumedStacks();
		
		filterStacks = info.getInventoryStacks();
		
		inventoryConnections = ITileInventory.inventoryConnectionAll(info.nonItemSorptions());
		
		tanks = Arrays.asList();
		consumedTanks = info.getConsumedTanks();
	}
	
	@Override
	public void onMachineAssembled(FissionReactor controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
	}
	
	// IFissionFuelComponent
	
	@Override
	public @Nullable FissionCluster getCluster() {
		return cluster;
	}
	
	@Override
	public void setClusterInternal(@Nullable FissionCluster cluster) {
		this.cluster = cluster;
	}
	
	@Override
	public boolean isValidHeatConductor(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
		return isProcessing || getDecayHeating() > 0;
	}
	
	@Override
	public boolean isFunctional() {
		return isProcessing || getDecayHeating() > 0;
	}
	
	@Override
	public void resetStats() {
		// primed = false;
		fluxSearched = false;
		flux = heatMult = 0;
		undercoolingLifetimeFactor = 1D;
		// sourceEfficiency = null;
		for (EnumFacing dir : EnumFacing.VALUES) {
			moderatorLineFluxes[dir.getIndex()] = 0;
			moderatorLineEfficiencies[dir.getIndex()] = null;
			adjacentFluxSinks[dir.getIndex()] = null;
			passiveModeratorCaches[dir.getIndex()].clear();
			activeModeratorCaches[dir.getIndex()].clear();
			moderatorLineCaches[dir.getIndex()] = null;
			passiveReflectorModeratorCaches[dir.getIndex()].clear();
			activeReflectorModeratorCaches[dir.getIndex()].clear();
		}
		activeReflectorCache.clear();
		
		refreshAll();
	}
	
	@Override
	public boolean isClusterRoot() {
		return true;
	}
	
	@Override
	public void clusterSearch(Integer id, final Object2IntMap<IFissionComponent> clusterSearchCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
		refreshDirty();
		refreshIsProcessing(false);
		
		IFissionFuelComponent.super.clusterSearch(id, clusterSearchCache, componentFailCache, assumedValidCache);
	}
	
	@Override
	public boolean isProducingFlux() {
		return isPrimed() || isProcessing;
	}
	
	@Override
	public void tryPriming(FissionReactor sourceReactor, boolean fromSource) {
		if (getMultiblock() != sourceReactor) {
			return;
		}
		
		if (canProcessInputs) {
			primed = true;
		}
	}
	
	@Override
	public boolean isPrimed() {
		return primed;
	}
	
	@Override
	public void addToPrimedCache(final ObjectSet<IFissionFuelComponent> primedCache) {
		primedCache.add(this);
	}
	
	@Override
	public void unprime() {
		primed = false;
	}
	
	@Override
	public boolean isAcceptingFlux(EnumFacing side) {
		return canProcessInputs;
	}
	
	@Override
	public void refreshIsProcessing(boolean checkCluster) {
		isProcessing = isProcessing(checkCluster);
		hasConsumed = hasConsumed();
	}
	
	@Override
	public boolean isFluxSearched() {
		return fluxSearched;
	}
	
	@Override
	public void setFluxSearched(boolean fluxSearched) {
		this.fluxSearched = fluxSearched;
	}
	
	@Override
	public void incrementHeatMultiplier() {
		++heatMult;
	}
	
	@Override
	public double getSourceEfficiency() {
		return sourceEfficiency == null ? 1D : sourceEfficiency.doubleValue();
	}
	
	@Override
	public void setSourceEfficiency(double sourceEfficiency, boolean maximize) {
		this.sourceEfficiency = this.sourceEfficiency != null && maximize ? Math.max(this.sourceEfficiency, sourceEfficiency) : sourceEfficiency;
	}
	
	@Override
	public long getFlux() {
		return flux;
	}
	
	@Override
	public void addFlux(long addedFlux) {
		flux += addedFlux;
	}
	
	@Override
	public long[] getModeratorLineFluxes() {
		return moderatorLineFluxes;
	}
	
	@Override
	public Double[] getModeratorLineEfficiencies() {
		return moderatorLineEfficiencies;
	}
	
	@Override
	public IFissionFluxSink[] getAdjacentFluxSinks() {
		return adjacentFluxSinks;
	}
	
	@Override
	public LongSet[] getPassiveModeratorCaches() {
		return passiveModeratorCaches;
	}
	
	@Override
	public LongSet[] getActiveModeratorCaches() {
		return activeModeratorCaches;
	}
	
	@Override
	public ModeratorLine[] getModeratorLineCaches() {
		return moderatorLineCaches;
	}
	
	@Override
	public LongSet[] getPassiveReflectorModeratorCaches() {
		return passiveReflectorModeratorCaches;
	}
	
	@Override
	public LongSet[] getActiveReflectorModeratorCaches() {
		return activeReflectorModeratorCaches;
	}
	
	@Override
	public LongSet getActiveReflectorCache() {
		return activeReflectorCache;
	}
	
	@Override
	public long getRawHeating() {
		return isProcessing ? baseProcessHeat * heatMult : 0L;
	}
	
	@Override
	public long getRawHeatingIgnoreCoolingPenalty() {
		return isProcessing ? 0L : getDecayHeating();
	}
	
	@Override
	public double getEffectiveHeating() {
		return isProcessing ? baseProcessHeat * getEfficiency() : 0D;
	}
	
	@Override
	public double getEffectiveHeatingIgnoreCoolingPenalty() {
		return isProcessing ? 0D : getFloatingPointDecayHeating();
	}
	
	@Override
	public long getHeatMultiplier() {
		return heatMult;
	}
	
	@Override
	public double getFluxEfficiencyFactor() {
		return (1D + Math.exp(-2D * getFloatingPointCriticality())) / (1D + Math.exp(2D * (flux - 2D * getFloatingPointCriticality())));
	}
	
	@Override
	public double getEfficiency() {
		return isProcessing ? heatMult * baseProcessEfficiency * getSourceEfficiency() * getModeratorEfficiencyFactor() * getFluxEfficiencyFactor() : 0D;
	}
	
	@Override
	public double getEfficiencyIgnoreCoolingPenalty() {
		return isProcessing ? 0D : 1D;
	}
	
	@Override
	public void setUndercoolingLifetimeFactor(double undercoolingLifetimeFactor) {
		this.undercoolingLifetimeFactor = undercoolingLifetimeFactor;
	}
	
	@Override
	public long getCriticality() {
		return fission_decay_mechanics ? NCMath.toInt(getFloatingPointCriticality()) : baseProcessCriticality;
	}
	
	@Override
	public double getFloatingPointCriticality() {
		return fission_decay_mechanics ? baseProcessCriticality * (1D - baseProcessDecayFactor + poisonFraction) : baseProcessCriticality;
	}
	
	@Override
	public boolean isSelfPriming() {
		return selfPriming;
	}
	
	/** Fix to force adjacent moderators to be active */
	@Override
	public void defaultRefreshModerators(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
		if (isProcessing) {
			defaultRefreshAdjacentActiveModerators(componentFailCache, assumedValidCache);
		}
		else if (getDecayHeating() > 0) {
			defaultForceAdjacentActiveModerators(componentFailCache, assumedValidCache);
		}
	}
	
	@Override
	public void onClusterMeltdown(Iterator<IFissionComponent> componentIterator) {
		IRadiationSource chunkSource = RadiationHelper.getRadiationSource(world.getChunk(pos));
		if (chunkSource != null) {
			RadiationHelper.addToSourceRadiation(chunkSource, 8D * baseProcessRadiation * getSpeedMultiplier() * fission_meltdown_radiation_multiplier);
		}
		
		componentIterator.remove();
		world.removeTileEntity(pos);
		
		IBlockState corium = FluidRegistry.getFluid("corium").getBlock().getDefaultState();
		world.setBlockState(pos, corium);
		
		if (getMultiblock() != null) {
			for (EnumFacing dir : EnumFacing.VALUES) {
				BlockPos offPos = pos.offset(dir);
				if (getMultiblock().rand.nextDouble() < 0.75D) {
					world.removeTileEntity(offPos);
					world.setBlockState(offPos, corium);
				}
			}
		}
	}
	
	@Override
	public long getHeatStored() {
		return heat;
	}
	
	@Override
	public void setHeatStored(long heat) {
		this.heat = heat;
	}
	
	// IFissionPortTarget
	
	@Override
	public BlockPos getMasterPortPos() {
		return masterPortPos;
	}
	
	@Override
	public void setMasterPortPos(BlockPos pos) {
		masterPortPos = pos;
	}
	
	@Override
	public void clearMasterPort() {
		masterPort = null;
		masterPortPos = DEFAULT_NON;
	}
	
	@Override
	public void refreshMasterPort() {
		masterPort = getMultiblock() == null ? null : getMultiblock().getPartMap(TileFissionCellPort.class).get(masterPortPos.toLong());
		if (masterPort == null) {
			masterPortPos = DEFAULT_NON;
		}
	}
	
	@Override
	public boolean onPortRefresh() {
		refreshAll();
		
		return isMultiblockAssembled() && getMultiblock().isReactorOn && !isProcessing && isProcessing(false);
	}
	
	// Ticking
	
	@Override
	public void onLoad() {
		super.onLoad();
		if (!world.isRemote) {
			refreshMasterPort();
			refreshAll();
		}
	}
	
	@Override
	public void update() {
		if (!world.isRemote) {
			boolean shouldRefresh = isMultiblockAssembled() && getMultiblock().isReactorOn && !isProcessing(true) && isProcessing(false);
			
			onTick();
			
			updateDecayFractions();
			
			if (shouldRefresh) {
				getMultiblock().refreshFlag = true;
			}
		}
	}
	
	public void updateDecayFractions() {
		if (!fission_decay_mechanics) {
			decayHeatFraction = iodineFraction = poisonFraction = 0D;
			return;
		}
		
		long oldCriticality = getCriticality();
		boolean oldHasEnoughFlux = hasEnoughFlux();
		int oldDecayHeating = getDecayHeating();
		
		boolean decayHeatReduce = true;
		boolean iodineReduce = true;
		boolean poisonReduce = true;
		
		double decayHeatEquilibrium = fission_decay_equilibrium_factors[0] * baseProcessDecayFactor;
		double iodineEquilibrium = fission_decay_equilibrium_factors[1] * baseProcessDecayFactor;
		double poisonEquilibrium = fission_decay_equilibrium_factors[2] * baseProcessDecayFactor;
		
		if (isProcessing) {
			if (decayHeatFraction <= decayHeatEquilibrium) {
				decayHeatFraction = MathHelper.clamp(decayHeatFraction + (fission_decay_term_multipliers[0] * (decayHeatEquilibrium - decayHeatFraction) + fission_decay_term_multipliers[1] * decayHeatEquilibrium) / fission_decay_build_up_times[0], 0D, decayHeatEquilibrium);
				decayHeatReduce = false;
			}
			
			if (iodineFraction <= iodineEquilibrium) {
				iodineFraction = MathHelper.clamp(iodineFraction + (fission_decay_term_multipliers[0] * (iodineEquilibrium - iodineFraction) + fission_decay_term_multipliers[1] * iodineEquilibrium) / fission_decay_build_up_times[1], 0D, iodineEquilibrium);
				iodineReduce = false;
			}
			
			if (poisonFraction <= poisonEquilibrium) {
				poisonFraction = MathHelper.clamp(poisonFraction + (fission_decay_term_multipliers[0] * (poisonEquilibrium - poisonFraction) + fission_decay_term_multipliers[1] * poisonEquilibrium) / fission_decay_build_up_times[2], 0D, poisonEquilibrium);
				poisonReduce = false;
			}
		}
		
		double decayHeatFractionReduction = 0D;
		if (decayHeatReduce) {
			decayHeatFractionReduction = Math.min(decayHeatFraction, (fission_decay_term_multipliers[0] * decayHeatFraction + fission_decay_term_multipliers[1] * decayHeatEquilibrium) / fission_decay_lifetimes[0]);
			decayHeatFraction = Math.max(0D, decayHeatFraction - decayHeatFractionReduction);
		}
		
		double poisonParentFractionReduction = 0D;
		if (iodineReduce) {
			poisonParentFractionReduction = Math.min(iodineFraction, (fission_decay_term_multipliers[0] * iodineFraction + fission_decay_term_multipliers[1] * iodineEquilibrium) / fission_decay_lifetimes[1]);
			iodineFraction = Math.max(0D, iodineFraction - poisonParentFractionReduction + fission_decay_daughter_multipliers[0] * decayHeatFractionReduction);
		}
		
		double poisonFractionReduction = 0D;
		if (poisonReduce) {
			poisonFractionReduction = Math.min(poisonFraction, (fission_decay_term_multipliers[0] * poisonFraction + fission_decay_term_multipliers[1] * poisonEquilibrium) / fission_decay_lifetimes[2]);
			poisonFraction = Math.max(0D, poisonFraction - poisonFractionReduction + fission_decay_daughter_multipliers[1] * poisonParentFractionReduction);
		}
		
		boolean refreshReactor = false, refreshCluster = false;
		
		if (oldCriticality != getCriticality()) {
			if (isProcessing) {
				if (oldHasEnoughFlux && !hasEnoughFlux()) {
					refreshReactor = true;
				}
				else {
					refreshCluster = true;
				}
			}
			else if (oldCriticality > baseProcessCriticality && getCriticality() <= baseProcessCriticality) {
				refreshReactor = true;
			}
		}
		
		if (!isProcessing && oldDecayHeating != getDecayHeating()) {
			if (getDecayHeating() == 0) {
				refreshReactor = true;
			}
			else {
				refreshCluster = true;
			}
		}
		
		if (refreshReactor) {
			getMultiblock().refreshFlag = true;
		}
		else if (refreshCluster) {
			getMultiblock().addClusterToRefresh(cluster);
		}
	}
	
	public int getDecayHeating() {
		return fission_decay_mechanics ? NCMath.toInt(getFloatingPointDecayHeating()) : 0;
	}
	
	public double getFloatingPointDecayHeating() {
		return fission_decay_mechanics ? decayProcessHeat * decayHeatFraction : 0D;
	}
	
	@Override
	public void refreshAll() {
		refreshDirty();
		refreshIsProcessing(true);
	}
	
	@Override
	public void refreshActivity() {
		boolean wasReady = readyToProcess(false);
		canProcessInputs = canProcessInputs();
		if (/* selfPriming && */ getMultiblock() != null && !wasReady && readyToProcess(false)) {
			getMultiblock().refreshFlag = true;
		}
	}
	
	// IProcessor
	
	@Override
	public SolidFissionCellContainerInfo getContainerInfo() {
		return info;
	}
	
	@Override
	public BasicRecipeHandler getRecipeHandler() {
		return NCRecipes.solid_fission;
	}
	
	@Override
	public RecipeInfo<BasicRecipe> getRecipeInfo() {
		return recipeInfo;
	}
	
	@Override
	public void setRecipeInfo(RecipeInfo<BasicRecipe> recipeInfo) {
		this.recipeInfo = recipeInfo;
	}
	
	@Override
	public void setRecipeStats(@Nullable BasicRecipe recipe) {
		// IProcessor.super.setRecipeStats(recipe);
		baseProcessTime = recipe == null ? 1D : recipe.getFissionFuelTime();
		baseProcessHeat = recipe == null ? 0 : recipe.getFissionFuelHeat();
		baseProcessEfficiency = recipe == null ? 0D : recipe.getFissionFuelEfficiency();
		baseProcessCriticality = recipe == null ? 1 : recipe.getFissionFuelCriticality();
		selfPriming = recipe == null ? false : recipe.getFissionFuelSelfPriming();
		baseProcessRadiation = recipe == null ? 0D : recipe.getFissionFuelRadiation();
		
		if (recipe != null) {
			decayProcessHeat = baseProcessHeat;
			baseProcessDecayFactor = recipe.getFissionFuelDecayFactor();
		}
	}
	
	@Override
	public @Nonnull NonNullList<ItemStack> getConsumedStacks() {
		return consumedStacks;
	}
	
	@Override
	public @Nonnull List<Tank> getConsumedTanks() {
		return consumedTanks;
	}
	
	@Override
	public double getBaseProcessTime() {
		return baseProcessTime;
	}
	
	@Override
	public void setBaseProcessTime(double baseProcessTime) {
		this.baseProcessTime = baseProcessTime;
	}
	
	@Override
	public double getBaseProcessPower() {
		return 0D;
	}
	
	@Override
	public void setBaseProcessPower(double baseProcessPower) {}
	
	@Override
	public double getCurrentTime() {
		return time;
	}
	
	@Override
	public void setCurrentTime(double time) {
		this.time = time;
	}
	
	@Override
	public double getResetTime() {
		return resetTime;
	}
	
	@Override
	public void setResetTime(double resetTime) {
		this.resetTime = resetTime;
	}
	
	@Override
	public boolean getIsProcessing() {
		return isProcessing;
	}
	
	@Override
	public void setIsProcessing(boolean isProcessing) {
		this.isProcessing = isProcessing;
	}
	
	@Override
	public boolean getCanProcessInputs() {
		return canProcessInputs;
	}
	
	@Override
	public void setCanProcessInputs(boolean canProcessInputs) {
		this.canProcessInputs = canProcessInputs;
	}
	
	@Override
	public boolean getHasConsumed() {
		return hasConsumed;
	}
	
	@Override
	public void setHasConsumed(boolean hasConsumed) {
		this.hasConsumed = hasConsumed;
	}
	
	@Override
	public double getSpeedMultiplier() {
		return 1D / undercoolingLifetimeFactor;
	}
	
	@Override
	public double getPowerMultiplier() {
		return 0D;
	}
	
	public boolean isProcessing(boolean checkCluster) {
		return readyToProcess(checkCluster) && hasEnoughFlux();
	}
	
	public boolean readyToProcess(boolean checkCluster) {
		return canProcessInputs && hasConsumed && isMultiblockAssembled() && !(checkCluster && cluster == null);
	}
	
	public boolean hasEnoughFlux() {
		return flux >= getCriticality();
	}
	
	@Override
	public void process() {
		getRadiationSource().setRadiationLevel(baseProcessRadiation * getSpeedMultiplier());
		IProcessor.super.process();
	}
	
	@Override
	public void finishProcess() {
		double oldProcessTime = baseProcessTime, oldProcessEfficiency = baseProcessEfficiency, oldProcessDecayFactor = baseProcessDecayFactor;
		int oldProcessHeat = baseProcessHeat;
		long oldCriticality = getCriticality();
		produceProducts();
		refreshRecipe();
		time = Math.max(0D, time - oldProcessTime);
		refreshActivityOnProduction();
		if (!canProcessInputs) {
			time = 0;
		}
		
		if (getMultiblock() != null) {
			if (canProcessInputs) {
				if (oldProcessHeat != baseProcessHeat || oldProcessEfficiency != baseProcessEfficiency || oldProcessDecayFactor != baseProcessDecayFactor || oldCriticality != getCriticality()) {
					if (!hasEnoughFlux()) {
						getMultiblock().refreshFlag = true;
					}
					else {
						getMultiblock().addClusterToRefresh(cluster);
					}
				}
			}
			else {
				sourceEfficiency = null;
				getMultiblock().refreshFlag = true;
			}
		}
	}
	
	// ITileInventory
	
	@Override
	public String getName() {
		return inventoryName;
	}
	
	@Override
	public @Nonnull NonNullList<ItemStack> getInventoryStacks() {
		return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getInventoryStacks() : inventoryStacks;
	}
	
	@Override
	public @Nonnull NonNullList<ItemStack> getInventoryStacksInternal() {
		return inventoryStacks;
	}
	
	@Override
	public void markDirty() {
		refreshDirty();
		super.markDirty();
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (stack.isEmpty() || (slot >= info.itemInputSize && slot < info.itemInputSize + info.itemOutputSize)) {
			return false;
		}
		ItemStack filter = getFilterStacks().get(slot);
		if (!filter.isEmpty() && !stack.isItemEqual(filter)) {
			return false;
		}
		return isItemValidForSlotInternal(slot, stack);
	}
	
	@Override
	public boolean isItemValidForSlotInternal(int slot, ItemStack stack) {
		return IProcessor.super.isItemValidForSlot(slot, stack);
	}
	
	@Override
	public void clearAllSlots() {
		for (int i = 0; i < inventoryStacks.size(); ++i) {
			inventoryStacks.set(i, ItemStack.EMPTY);
		}
		for (int i = 0; i < consumedStacks.size(); ++i) {
			consumedStacks.set(i, ItemStack.EMPTY);
		}
		refreshAll();
	}
	
	@Override
	public @Nonnull InventoryConnection[] getInventoryConnections() {
		return inventoryConnections;
	}
	
	@Override
	public void setInventoryConnections(@Nonnull InventoryConnection[] connections) {
		inventoryConnections = connections;
	}
	
	@Override
	public ItemOutputSetting getItemOutputSetting(int slot) {
		return ItemOutputSetting.DEFAULT;
	}
	
	@Override
	public void setItemOutputSetting(int slot, ItemOutputSetting setting) {}
	
	// ITileFilteredInventory
	
	@Override
	public @Nonnull NonNullList<ItemStack> getFilterStacks() {
		return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getFilterStacks() : filterStacks;
	}
	
	@Override
	public boolean canModifyFilter(int slot) {
		return getMultiblock() != null ? !getMultiblock().isAssembled() : true;
	}
	
	@Override
	public void onFilterChanged(int slot) {
		markDirty();
	}
	
	@Override
	public Object getFilterKey() {
		return getFilterStacks().get(0).isEmpty() ? 0 : RecipeItemHelper.pack(getFilterStacks().get(0));
	}
	
	// ITileFluid
	
	@Override
	public @Nonnull List<Tank> getTanks() {
		return tanks;
	}
	
	@Override
	public @Nonnull FluidConnection[] getFluidConnections() {
		return fluidConnections;
	}
	
	@Override
	public void setFluidConnections(@Nonnull FluidConnection[] connections) {
		fluidConnections = connections;
	}
	
	@Override
	public @Nonnull FluidTileWrapper[] getFluidSides() {
		return fluidSides;
	}
	
	@Override
	public @Nonnull GasTileWrapper getGasWrapper() {
		return gasWrapper;
	}
	
	@Override
	public boolean getInputTanksSeparated() {
		return false;
	}
	
	@Override
	public void setInputTanksSeparated(boolean separated) {}
	
	@Override
	public boolean getVoidUnusableFluidInput(int tankNumber) {
		return false;
	}
	
	@Override
	public void setVoidUnusableFluidInput(int tankNumber, boolean voidUnusableFluidInput) {}
	
	@Override
	public TankOutputSetting getTankOutputSetting(int tankNumber) {
		return TankOutputSetting.DEFAULT;
	}
	
	@Override
	public void setTankOutputSetting(int tankNumber, TankOutputSetting setting) {}
	
	@Override
	public boolean hasConfigurableFluidConnections() {
		return false;
	}
	
	// ITileGui
	
	@Override
	public Set<EntityPlayer> getTileUpdatePacketListeners() {
		return updatePacketListeners;
	}
	
	@Override
	public SolidFissionCellUpdatePacket getTileUpdatePacket() {
		return new SolidFissionCellUpdatePacket(pos, isProcessing, time, baseProcessTime, getTanks(), masterPortPos, getFilterStacks(), cluster);
	}
	
	@Override
	public void onTileUpdatePacket(SolidFissionCellUpdatePacket message) {
		IProcessor.super.onTileUpdatePacket(message);
		if (DEFAULT_NON.equals(masterPortPos = message.masterPortPos) ^ masterPort == null) {
			refreshMasterPort();
		}
		IntStream.range(0, filterStacks.size()).forEach(x -> filterStacks.set(x, message.filterStacks.get(x)));
		clusterHeatStored = message.clusterHeatStored;
		clusterHeatCapacity = message.clusterHeatCapacity;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		writeInventory(nbt);
		writeInventoryConnections(nbt);
		
		writeProcessorNBT(nbt);
		
		nbt.setDouble("baseProcessTime", baseProcessTime);
		nbt.setInteger("baseProcessHeat", baseProcessHeat);
		nbt.setDouble("baseProcessEfficiency", baseProcessEfficiency);
		nbt.setInteger("baseProcessCriticality", baseProcessCriticality);
		nbt.setDouble("baseProcessDecayFactor", baseProcessDecayFactor);
		nbt.setBoolean("selfPriming", selfPriming);
		
		nbt.setDouble("decayProcessHeat", decayProcessHeat);
		nbt.setDouble("decayHeatFraction", decayHeatFraction);
		nbt.setDouble("iodineFraction", iodineFraction);
		nbt.setDouble("poisonFraction", poisonFraction);
		
		nbt.setLong("flux", flux);
		nbt.setLong("clusterHeat", heat);
		
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readInventory(nbt);
		readInventoryConnections(nbt);
		
		readProcessorNBT(nbt);
		
		baseProcessTime = nbt.getDouble("baseProcessTime");
		baseProcessHeat = nbt.getInteger("baseProcessHeat");
		baseProcessEfficiency = nbt.getDouble("baseProcessEfficiency");
		baseProcessCriticality = nbt.getInteger("baseProcessCriticality");
		baseProcessDecayFactor = nbt.getDouble("baseProcessDecayFactor");
		selfPriming = nbt.getBoolean("selfPriming");
		
		decayProcessHeat = nbt.getDouble("decayProcessHeat");
		decayHeatFraction = nbt.getDouble("decayHeatFraction");
		iodineFraction = nbt.getDouble("iodineFraction");
		poisonFraction = nbt.getDouble("poisonFraction");
		
		flux = nbt.getLong("flux");
		heat = nbt.getLong("clusterHeat");
	}
	
	@Override
	public NBTTagCompound writeInventory(NBTTagCompound nbt) {
		NBTHelper.writeAllItems(nbt, inventoryStacks, filterStacks, consumedStacks);
		return nbt;
	}
	
	@Override
	public void readInventory(NBTTagCompound nbt) {
		NBTHelper.readAllItems(nbt, inventoryStacks, filterStacks, consumedStacks);
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return !getInventoryStacks().isEmpty() && hasInventorySideCapability(side);
		}
		return super.hasCapability(capability, side);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (!getInventoryStacks().isEmpty() && hasInventorySideCapability(side)) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(getItemHandler(side));
			}
			return null;
		}
		return super.getCapability(capability, side);
	}
	
	@Override
	public IItemHandler getItemHandler(@Nullable EnumFacing side) {
		// ITileInventory tile = !DEFAULT_NON.equals(masterPortPos) ? masterPort : this;
		// return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new PortItemHandler(tile, side));
		return ITileFilteredInventory.super.getItemHandler(side);
	}
}
