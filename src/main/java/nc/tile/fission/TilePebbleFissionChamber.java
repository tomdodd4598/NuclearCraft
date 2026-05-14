package nc.tile.fission;

import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.objects.*;
import nc.Global;
import nc.capability.radiation.source.IRadiationSource;
import nc.handler.TileInfoHandler;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.*;
import nc.network.tile.multiblock.PebbleFissionChamberUpdatePacket;
import nc.radiation.RadiationHelper;
import nc.recipe.*;
import nc.tile.fission.port.*;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.*;
import nc.tile.internal.inventory.*;
import nc.tile.inventory.*;
import nc.tile.processor.IBasicProcessor;
import nc.tile.processor.info.ProcessorContainerInfoImpl;
import nc.util.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.items.*;

import javax.annotation.*;
import java.util.*;
import java.util.stream.IntStream;

import static nc.config.NCConfig.*;
import static nc.util.PosHelper.DEFAULT_NON;

public class TilePebbleFissionChamber extends TileFissionPart implements IBasicProcessor<TilePebbleFissionChamber, PebbleFissionChamberUpdatePacket>, ITileFilteredInventory, IFissionFuelBunchComponent, IFissionPortTarget<TileFissionChamberPort, TilePebbleFissionChamber> {
	
	protected final ProcessorContainerInfoImpl.BasicProcessorContainerInfo<TilePebbleFissionChamber, PebbleFissionChamberUpdatePacket> info;
	
	protected final @Nonnull String inventoryName;
	
	protected final @Nonnull NonNullList<ItemStack> inventoryStacks;
	protected final @Nonnull NonNullList<ItemStack> consumedStacks;
	
	protected final @Nonnull NonNullList<ItemStack> filterStacks;
	
	protected @Nonnull InventoryConnection[] inventoryConnections;
	
	protected @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(Collections.emptyList());
	
	protected @Nonnull FluidTileWrapper[] fluidSides = ITileFluid.getDefaultFluidSides(this);
	protected @Nonnull GasTileWrapper gasWrapper = new GasTileWrapper(this);
	
	public double baseProcessTime = 1D, baseProcessEfficiency = 0D, baseProcessDecayFactor = 0D, baseProcessRadiation = 0D;
	public int baseProcessHeat = 0, baseProcessCriticality = 1, intrinsicFlux = 0;
	protected boolean selfPriming = false;
	
	public double time, resetTime;
	public boolean isProcessing, canProcessInputs, hasConsumed;
	public boolean isRunningSimulated;
	
	public double decayProcessHeat = 0D, decayHeatFraction = 0D, iodineFraction = 0D, poisonFraction = 0D;
	
	protected RecipeInfo<BasicRecipe> recipeInfo = null;
	
	protected final Set<EntityPlayer> updatePacketListeners = new ObjectOpenHashSet<>();
	
	public long cachedFlux = 0L;
	
	protected FissionCluster cluster = null;
	protected long heat = 0L;
	
	public long clusterHeatStored, clusterHeatCapacity;
	
	protected boolean fluxSearched = false;
	
	public long heatMult = 0L;
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
	protected TileFissionChamberPort masterPort = null;
	
	protected FissionFuelBunch fuelBunch = null;
	
	public TilePebbleFissionChamber() {
		super(CuboidalPartPositionType.INTERIOR);
		info = TileInfoHandler.getProcessorContainerInfo("pebble_fission_chamber");
		
		inventoryName = Global.MOD_ID + ".container." + info.name;
		
		inventoryStacks = info.getInventoryStacks();
		consumedStacks = info.getConsumedStacks();
		
		filterStacks = info.getInventoryStacks();
		
		inventoryConnections = ITileInventory.inventoryConnectionAll(info.nonItemSorptions());
	}
	
	@Override
	public void onMachineAssembled(FissionReactor multiblock) {
		doStandardNullControllerResponse(multiblock);
		super.onMachineAssembled(multiblock);
	}
	
	// IFissionFuelComponent
	
	@Override
	public @Nullable FissionFuelBunch getFuelBunch() {
		return fuelBunch;
	}
	
	@Override
	public void setFuelBunch(@Nullable FissionFuelBunch fuelBunch) {
		this.fuelBunch = fuelBunch;
		if (fuelBunch != null) {
			fuelBunch.fuelComponentMap.put(pos.toLong(), this);
		}
	}
	
	@Override
	public @Nullable FissionCluster getCluster() {
		return cluster;
	}
	
	@Override
	public void setClusterInternal(@Nullable FissionCluster cluster) {
		this.cluster = cluster;
	}
	
	@Override
	public boolean isValidHeatConductor(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
		return isRunning(simulate) || getDecayHeating() > 0;
	}
	
	@Override
	public boolean isFunctional(boolean simulate) {
		return isRunning(simulate) || getDecayHeating() > 0;
	}
	
	@Override
	public void resetStats() {
		fuelBunch.sources = fuelBunch.flux = cachedFlux = 0L;
		fluxSearched = false;
		heatMult = 0L;
		undercoolingLifetimeFactor = 1D;
		// sourceEfficiency = null;
		for (int i = 0; i < 6; ++i) {
			moderatorLineFluxes[i] = 0;
			moderatorLineEfficiencies[i] = null;
			adjacentFluxSinks[i] = null;
			passiveModeratorCaches[i].clear();
			activeModeratorCaches[i].clear();
			moderatorLineCaches[i] = null;
			passiveReflectorModeratorCaches[i].clear();
			activeReflectorModeratorCaches[i].clear();
		}
		activeReflectorCache.clear();
		
		refreshAll();
	}
	
	@Override
	public boolean isClusterRoot() {
		return true;
	}
	
	@Override
	public void clusterSearch(Integer id, final Object2IntMap<IFissionComponent> clusterSearchCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
		refreshDirty();
		refreshIsProcessing(false, simulate);
		
		IFissionFuelBunchComponent.super.clusterSearch(id, clusterSearchCache, componentFailCache, assumedValidCache, simulate);
	}
	
	@Override
	public boolean isProducingFlux(boolean simulate) {
		return isPrimed(simulate) || isRunning(simulate);
	}
	
	@Override
	public void tryPriming(FissionReactor sourceReactor, boolean fromSource, boolean simulate) {
		if (getMultiblock() != sourceReactor) {
			return;
		}
		
		if (canProcessInputs) {
			fuelBunch.tryPriming(fromSource, simulate);
		}
	}
	
	@Override
	public boolean isPrimed(boolean simulate) {
		return fuelBunch.primed;
	}
	
	@Override
	public void addToPrimedCache(final ObjectSet<IFissionFuelComponent> primedCache) {
		primedCache.addAll(fuelBunch.fuelComponentMap.values());
	}
	
	@Override
	public void addToPrimedFailCache(final Long2ObjectMap<IFissionFuelComponent> primedFailCache) {
		primedFailCache.putAll(getFuelBunch().fuelComponentMap);
	}
	
	@Override
	public void unprime(boolean simulate) {
		fuelBunch.primed = false;
	}
	
	@Override
	public boolean isAcceptingFlux(EnumFacing side, boolean simulate) {
		return canProcessInputs;
	}
	
	@Override
	public void refreshIsProcessing(boolean checkCluster, boolean simulate) {
		if (simulate) {
			isProcessing = false;
			isRunningSimulated = isProcessing(checkCluster, simulate);
		}
		else {
			isProcessing = isProcessing(checkCluster, simulate);
			isRunningSimulated = false;
		}
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
	public void addToFluxSearchCache(final ObjectSet<IFissionFuelComponent> fluxSearchCache) {
		fluxSearchCache.addAll(fuelBunch.fuelComponentMap.values());
	}
	
	@Override
	public void onEndModeratorLine(boolean simulate) {
		for (IFissionFuelBunchComponent fuelComponent : fuelBunch.fuelComponentMap.values()) {
			fuelComponent.refreshIsProcessing(false, simulate);
		}
	}
	
	@Override
	public void incrementHeatMultiplier() {
		++heatMult;
	}
	
	@Override
	public double getSourceEfficiency() {
		return sourceEfficiency == null ? 1D : sourceEfficiency;
	}
	
	@Override
	public void setSourceEfficiency(double sourceEfficiency, boolean maximize) {
		this.sourceEfficiency = this.sourceEfficiency != null && maximize ? Math.max(this.sourceEfficiency, sourceEfficiency) : sourceEfficiency;
	}
	
	@Override
	public long getFlux() {
		return fuelBunch.flux;
	}
	
	@Override
	public void addFlux(long addedFlux) {
		cachedFlux += addedFlux;
		fuelBunch.flux += addedFlux;
	}
	
	@Override
	public long getIndividualFlux() {
		return cachedFlux;
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
	public long getBaseProcessHeat() {
		return baseProcessHeat;
	}
	
	@Override
	public double getBaseProcessEfficiency() {
		return baseProcessEfficiency;
	}
	
	/**
	 * DON'T USE IN REACTOR LOGIC!
	 */
	@Override
	public long getRawHeating(boolean simulate) {
		return isRunning(simulate) ? baseProcessHeat * fuelBunch.getHeatMultiplier(simulate) / getFuelBunchSize() : 0L;
	}
	
	@Override
	public long getRawHeatingIgnoreCoolingPenalty(boolean simulate) {
		return isRunning(simulate) ? 0L : getDecayHeating();
	}
	
	@Override
	public double getEffectiveHeating(boolean simulate) {
		return isRunning(simulate) ? baseProcessHeat * getEfficiency(simulate) : 0D;
	}
	
	@Override
	public double getEffectiveHeatingIgnoreCoolingPenalty(boolean simulate) {
		return isRunning(simulate) ? 0D : getFloatingPointDecayHeating();
	}
	
	/**
	 * DON'T USE IN REACTOR LOGIC!
	 */
	@Override
	public long getHeatMultiplier(boolean simulate) {
		return fuelBunch.getHeatMultiplier(simulate) / getFuelBunchSize();
	}
	
	@Override
	public long getIntrinsicFlux() {
		return intrinsicFlux;
	}
	
	@Override
	public double getIntrinsicFluxEfficiencyFactor() {
		return fission_chamber_intrinsic_flux_efficiency;
	}
	
	@Override
	public long getIndividualHeatMultiplier(boolean simulate) {
		return heatMult;
	}
	
	@Override
	public double getFluxEfficiencyFactor() {
		return fuelBunch.getFluxEfficiencyFactor(getFloatingPointCriticality());
	}
	
	@Override
	public double getEfficiency(boolean simulate) {
		return isRunning(simulate) ? fuelBunch.getHeatMultiplier(simulate) * baseProcessEfficiency * getSourceEfficiency() * getModeratorEfficiencyFactor() * getFluxEfficiencyFactor() / getFuelBunchSize() : 0D;
	}
	
	@Override
	public double getEfficiencyIgnoreCoolingPenalty(boolean simulate) {
		return isRunning(simulate) ? 0D : 1D;
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
	public boolean isInitiallyPrimed(boolean simulate) {
		return selfPriming || isFunctional(simulate);
	}
	
	/**
	 * Fix to force adjacent moderators to be active
	 */
	@Override
	public void defaultRefreshModerators(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
		if (isRunning(simulate)) {
			defaultRefreshAdjacentActiveModerators(componentFailCache, assumedValidCache, simulate);
		}
		else if (getDecayHeating() > 0) {
			defaultForceAdjacentActiveModerators(componentFailCache, assumedValidCache, simulate);
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
		
		FissionReactor multiblock = getMultiblock();
		if (multiblock != null) {
			for (EnumFacing dir : EnumFacing.VALUES) {
				BlockPos offPos = pos.offset(dir);
				if (multiblock.rand.nextDouble() < 0.75D) {
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
		FissionReactor multiblock = getMultiblock();
		masterPort = multiblock == null ? null : multiblock.getPartMap(TileFissionChamberPort.class).get(masterPortPos.toLong());
		if (masterPort == null) {
			masterPortPos = DEFAULT_NON;
		}
	}
	
	@Override
	public void onPortRefresh() {
		refreshAll();
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
			FissionReactor reactor = getMultiblock();
			boolean shouldRefresh = reactor != null && reactor.isReactorOn && cluster == null && isProcessing(false, false);
			
			if (onTick()) {
				markDirty();
			}
			
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
	
	@Override
	public int getDecayHeating() {
		return fission_decay_mechanics ? NCMath.toInt(getFloatingPointDecayHeating()) : 0;
	}
	
	@Override
	public double getFloatingPointDecayHeating() {
		return fission_decay_mechanics ? decayProcessHeat * decayHeatFraction : 0D;
	}
	
	@Override
	public void refreshAll() {
		refreshDirty();
		refreshIsProcessing(true, false);
	}
	
	@Override
	public void refreshActivity() {
		boolean wasReady = readyToProcess(false);
		canProcessInputs = canProcessInputs();
		FissionReactor multiblock = getMultiblock();
		if (multiblock != null && !wasReady && readyToProcess(false)) {
			multiblock.refreshFlag = true;
		}
	}
	
	@Override
	public boolean isRunning(boolean simulate) {
		return simulate ? isRunningSimulated : isProcessing;
	}
	
	// IProcessor
	
	@Override
	public ProcessorContainerInfoImpl.BasicProcessorContainerInfo<TilePebbleFissionChamber, PebbleFissionChamberUpdatePacket> getContainerInfo() {
		return info;
	}
	
	@Override
	public BasicRecipeHandler getRecipeHandler() {
		return NCRecipes.pebble_fission;
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
		if (recipe == null) {
			baseProcessTime = 1D;
			baseProcessHeat = 0;
			baseProcessEfficiency = 0D;
			baseProcessCriticality = 1;
			intrinsicFlux = 0;
			selfPriming = false;
			baseProcessRadiation = 0D;
		}
		else {
			baseProcessTime = recipe.getFissionFuelTime();
			baseProcessHeat = recipe.getFissionFuelHeat();
			baseProcessEfficiency = recipe.getFissionFuelEfficiency();
			baseProcessCriticality = recipe.getFissionFuelCriticality();
			intrinsicFlux = recipe.getFissionFuelIntrinsicFlux();
			selfPriming = recipe.getFissionFuelSelfPriming();
			baseProcessRadiation = recipe.getFissionFuelRadiation();
		}
		
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
		return Collections.emptyList();
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
	
	@Override
	public boolean isProcessing() {
		return !isSimulation() && isProcessing(true, false);
	}
	
	public boolean isProcessing(boolean checkCluster, boolean simulate) {
		return readyToProcess(checkCluster) && hasEnoughFlux();
	}
	
	@Override
	public boolean readyToProcess() {
		return readyToProcess(true);
	}
	
	public boolean readyToProcess(boolean checkCluster) {
		return canProcessInputs && hasConsumed && isMultiblockAssembled() && (!checkCluster || cluster != null);
	}
	
	public boolean hasEnoughFlux() {
		return fuelBunch != null && fuelBunch.flux >= fuelBunch.getCriticalityFactor(getCriticality());
	}
	
	@Override
	public void process() {
		getRadiationSource().setRadiationLevel(baseProcessRadiation * getSpeedMultiplier());
		IBasicProcessor.super.process();
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
		
		FissionReactor multiblock = getMultiblock();
		if (multiblock != null) {
			if (canProcessInputs) {
				if (oldProcessHeat != baseProcessHeat || oldProcessEfficiency != baseProcessEfficiency || oldProcessDecayFactor != baseProcessDecayFactor || oldCriticality != getCriticality()) {
					if (!hasEnoughFlux()) {
						multiblock.refreshFlag = true;
					}
					else {
						multiblock.addClusterToRefresh(cluster);
					}
				}
			}
			else {
				sourceEfficiency = null;
				multiblock.refreshFlag = true;
			}
		}
	}
	
	@Override
	public int getItemProductCapacity(int slot, ItemStack stack) {
		return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getInventoryStackLimit() : IBasicProcessor.super.getItemProductCapacity(slot, stack);
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
		return IBasicProcessor.super.isItemValidForSlot(slot, stack);
	}
	
	@Override
	public int getInventoryStackLimit() {
		return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getInventoryStackLimit() : IBasicProcessor.super.getInventoryStackLimit();
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
		return !isMultiblockAssembled();
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
		return Collections.emptyList();
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
	public PebbleFissionChamberUpdatePacket getTileUpdatePacket() {
		return new PebbleFissionChamberUpdatePacket(pos, isProcessing, time, baseProcessTime, getTanks(), masterPortPos, getFilterStacks(), cluster);
	}
	
	@Override
	public void onTileUpdatePacket(PebbleFissionChamberUpdatePacket message) {
		IBasicProcessor.super.onTileUpdatePacket(message);
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
		nbt.setInteger("intrinsicFlux", intrinsicFlux);
		nbt.setDouble("baseProcessDecayFactor", baseProcessDecayFactor);
		nbt.setBoolean("selfPriming", selfPriming);
		
		nbt.setDouble("decayProcessHeat", decayProcessHeat);
		nbt.setDouble("decayHeatFraction", decayHeatFraction);
		nbt.setDouble("iodineFraction", iodineFraction);
		nbt.setDouble("poisonFraction", poisonFraction);
		
		nbt.setLong("cachedFlux", cachedFlux);
		nbt.setLong("clusterHeat", heat);
		
		nbt.setBoolean("isRunningSimulated", isRunningSimulated);
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
		intrinsicFlux = nbt.getInteger("intrinsicFlux");
		baseProcessDecayFactor = nbt.getDouble("baseProcessDecayFactor");
		selfPriming = nbt.getBoolean("selfPriming");
		
		decayProcessHeat = nbt.getDouble("decayProcessHeat");
		decayHeatFraction = nbt.getDouble("decayHeatFraction");
		iodineFraction = nbt.getDouble("iodineFraction");
		poisonFraction = nbt.getDouble("poisonFraction");
		
		cachedFlux = nbt.getLong("cachedFlux");
		heat = nbt.getLong("clusterHeat");
		
		isRunningSimulated = nbt.getBoolean("isRunningSimulated");
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
	
	// OpenComputers
	
	@Override
	public String getOCKey() {
		return "chamber";
	}
	
	@Override
	public Object getOCInfo() {
		Object2ObjectMap<String, Object> entry = new Object2ObjectLinkedOpenHashMap<>();
		List<ItemStack> stacks = getInventoryStacks();
		entry.put("fuel", OCHelper.stackInfo(stacks.get(0)));
		entry.put("depleted_fuel", OCHelper.stackInfo(stacks.get(1)));
		entry.put("effective_heating", getEffectiveHeating(false));
		entry.put("heat_multiplier", getHeatMultiplier(false));
		entry.put("is_processing", getIsProcessing());
		entry.put("current_time", getCurrentTime());
		entry.put("base_process_time", getBaseProcessTime());
		entry.put("base_process_heat", getBaseProcessHeat());
		entry.put("base_process_efficiency", baseProcessEfficiency);
		entry.put("base_process_criticality", baseProcessCriticality);
		entry.put("intrinsic_flux", intrinsicFlux);
		entry.put("base_process_decay_factor", baseProcessDecayFactor);
		entry.put("is_self_priming", selfPriming);
		entry.put("base_process_radiation", baseProcessRadiation);
		entry.put("is_primed", isPrimed(false));
		entry.put("efficiency", getEfficiency(false));
		entry.put("flux", getFlux());
		return entry;
	}
}
