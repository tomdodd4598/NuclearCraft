package nc.multiblock.fission.salt.tile;

import static nc.config.NCConfig.*;
import static nc.util.FluidStackHelper.INGOT_BLOCK_VOLUME;
import static nc.util.PosHelper.DEFAULT_NON;

import java.util.*;

import javax.annotation.*;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.objects.*;
import nc.ModCheck;
import nc.capability.radiation.source.IRadiationSource;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.*;
import nc.multiblock.fission.salt.SaltFissionVesselBunch;
import nc.multiblock.fission.tile.*;
import nc.multiblock.fission.tile.port.*;
import nc.network.multiblock.SaltFissionVesselUpdatePacket;
import nc.radiation.RadiationHelper;
import nc.recipe.*;
import nc.recipe.ingredient.IFluidIngredient;
import nc.tile.ITileGui;
import nc.tile.fluid.*;
import nc.tile.generator.IFluidGenerator;
import nc.tile.internal.fluid.*;
import nc.util.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class TileSaltFissionVessel extends TileFissionPart implements ITileFilteredFluid, ITileGui<SaltFissionVesselUpdatePacket>, IFluidGenerator, IFissionFuelComponent, IFissionPortTarget<TileFissionVesselPort, TileSaltFissionVessel> {
	
	protected final @Nonnull List<Tank> tanks = Lists.newArrayList(new Tank(INGOT_BLOCK_VOLUME, NCRecipes.salt_fission_valid_fluids.get(0)), new Tank(INGOT_BLOCK_VOLUME, new ArrayList<>()));
	protected final @Nonnull List<Tank> filterTanks = Lists.newArrayList(new Tank(1000, NCRecipes.salt_fission_valid_fluids.get(0)), new Tank(1000, new ArrayList<>()));
	protected final @Nonnull List<Tank> consumedTanks = Lists.newArrayList(new Tank(INGOT_BLOCK_VOLUME, new ArrayList<>()));
	
	protected @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(Lists.newArrayList(TankSorption.NON, TankSorption.NON));
	
	protected @Nonnull FluidTileWrapper[] fluidSides;
	
	protected @Nonnull GasTileWrapper gasWrapper;
	
	protected final int fluidInputSize = 1, fluidOutputSize = 1;
	
	public double baseProcessTime = 1D, baseProcessEfficiency = 0D, baseProcessDecayFactor = 0D, baseProcessRadiation = 0D;
	public int baseProcessHeat = 0, baseProcessCriticality = 1;
	protected boolean selfPriming = false;
	
	public double time;
	public boolean isProcessing, hasConsumed, canProcessInputs;
	
	public double decayProcessHeat = 0D, decayHeatFraction = 0D, iodineFraction = 0D, poisonFraction = 0D;
	
	protected RecipeInfo<BasicRecipe> recipeInfo;
	
	protected Set<EntityPlayer> updatePacketListeners;
	
	protected FissionCluster cluster = null;
	protected long heat = 0L;
	
	public long clusterHeatStored, clusterHeatCapacity;
	
	protected boolean fluxSearched = false;
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
	protected TileFissionVesselPort masterPort = null;
	
	protected SaltFissionVesselBunch vesselBunch = null;
	
	public TileSaltFissionVessel() {
		super(CuboidalPartPositionType.INTERIOR);
		fluidSides = ITileFluid.getDefaultFluidSides(this);
		gasWrapper = new GasTileWrapper(this);
		
		updatePacketListeners = new ObjectOpenHashSet<>();
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
	
	public @Nullable SaltFissionVesselBunch getVesselBunch() {
		return vesselBunch;
	}
	
	public void setVesselBunch(@Nullable SaltFissionVesselBunch vesselBunch) {
		this.vesselBunch = vesselBunch;
		if (vesselBunch != null) {
			vesselBunch.getPartMap().put(pos.toLong(), this);
		}
	}
	
	public int getVesselBunchSize() {
		return vesselBunch.getPartMap().size();
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
	public boolean isValidHeatConductor(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
		return isProcessing || getDecayHeating() > 0;
	}
	
	@Override
	public boolean isFunctional() {
		return isProcessing || getDecayHeating() > 0;
	}
	
	@Override
	public void resetStats() {
		vesselBunch.sources = vesselBunch.flux = 0L;
		/* primed = */ fluxSearched = false;
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
		
		refreshRecipe();
		refreshActivity();
		refreshIsProcessing(true);
	}
	
	@Override
	public boolean isClusterRoot() {
		return true;
	}
	
	@Override
	public void clusterSearch(Integer id, final Object2IntMap<IFissionComponent> clusterSearchCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
		refreshRecipe();
		refreshActivity();
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
			if (fromSource) {
				++vesselBunch.sources;
				if (vesselBunch.sources >= vesselBunch.getSurfaceFactor()) {
					vesselBunch.primed = true;
				}
			}
			else {
				vesselBunch.primed = true;
			}
		}
	}
	
	@Override
	public boolean isPrimed() {
		return vesselBunch.primed;
	}
	
	@Override
	public void addToPrimedCache(final ObjectSet<IFissionFuelComponent> primedCache) {
		for (TileSaltFissionVessel vessel : vesselBunch.getPartMap().values()) {
			primedCache.add(vessel);
		}
	}
	
	@Override
	public void unprime() {
		vesselBunch.primed = false;
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
		return vesselBunch.flux;
	}
	
	@Override
	public void addFlux(long addedFlux) {
		flux += addedFlux;
		vesselBunch.flux += addedFlux;
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
	
	/** DON'T USE IN REACTOR LOGIC! */
	@Override
	public long getRawHeating() {
		return isProcessing ? baseProcessHeat * vesselBunch.getHeatMultiplier() / getVesselBunchSize() : 0L;
	}
	
	/** DON'T USE IN REACTOR LOGIC! */
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
	
	/** DON'T USE IN REACTOR LOGIC! */
	@Override
	public long getHeatMultiplier() {
		return vesselBunch.getHeatMultiplier() / getVesselBunchSize();
	}
	
	@Override
	public double getFluxEfficiencyFactor() {
		return vesselBunch.getFluxEfficiencyFactor(getFloatingPointCriticality());
	}
	
	@Override
	public double getEfficiency() {
		return isProcessing ? vesselBunch.getHeatMultiplier() * baseProcessEfficiency * getSourceEfficiency() * getModeratorEfficiencyFactor() * getFluxEfficiencyFactor() / getVesselBunchSize() : 0D;
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
		masterPort = getMultiblock() == null ? null : getMultiblock().getPartMap(TileFissionVesselPort.class).get(masterPortPos.toLong());
		if (masterPort == null) {
			masterPortPos = DEFAULT_NON;
		}
	}
	
	@Override
	public boolean onPortRefresh() {
		refreshRecipe();
		refreshActivity();
		refreshIsProcessing(true);
		
		return isMultiblockAssembled() && getMultiblock().isReactorOn && !isProcessing && isProcessing(false);
	}
	
	// Processing
	
	@Override
	public void onLoad() {
		super.onLoad();
		if (!world.isRemote) {
			refreshMasterPort();
			refreshRecipe();
			refreshActivity();
			refreshIsProcessing(true);
		}
	}
	
	@Override
	public void update() {
		if (!world.isRemote) {
			boolean wasProcessing = isProcessing;
			isProcessing = isProcessing(true);
			boolean shouldRefresh = isMultiblockAssembled() && getMultiblock().isReactorOn && !isProcessing && isProcessing(false);
			boolean shouldUpdate = wasProcessing != isProcessing;
			
			if (isProcessing) {
				process();
			}
			else {
				getRadiationSource().setRadiationLevel(0D);
			}
			
			updateDecayFractions();
			
			if (shouldRefresh) {
				getMultiblock().refreshFlag = true;
			}
			
			sendTileUpdatePacketToListeners();
			if (shouldUpdate) {
				markDirty();
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
	public void refreshRecipe() {
		recipeInfo = NCRecipes.salt_fission.getRecipeInfoFromInputs(new ArrayList<ItemStack>(), getFluidInputs(hasConsumed));
		consumeInputs();
	}
	
	@Override
	public void refreshActivity() {
		boolean wasReady = readyToProcess(false);
		canProcessInputs = canProcessInputs();
		if (getMultiblock() != null && !wasReady && readyToProcess(false)) {
			getMultiblock().refreshFlag = true;
		}
	}
	
	@Override
	public void refreshActivityOnProduction() {
		canProcessInputs = canProcessInputs();
	}
	
	// Processor Stats
	
	public double getSpeedMultiplier() {
		return 1D / undercoolingLifetimeFactor;
	}
	
	public boolean setRecipeStats() {
		if (recipeInfo == null) {
			baseProcessTime = 1D;
			baseProcessHeat = 0;
			baseProcessEfficiency = 0D;
			baseProcessCriticality = 1;
			// baseProcessDecayFactor = 0D;
			selfPriming = false;
			baseProcessRadiation = 0D;
			return false;
		}
		BasicRecipe recipe = recipeInfo.getRecipe();
		baseProcessTime = recipe.getSaltFissionFuelTime();
		baseProcessHeat = recipe.getFissionFuelHeat();
		decayProcessHeat = baseProcessHeat;
		baseProcessEfficiency = recipe.getFissionFuelEfficiency();
		baseProcessCriticality = recipe.getFissionFuelCriticality();
		baseProcessDecayFactor = recipe.getFissionFuelDecayFactor();
		selfPriming = recipe.getFissionFuelSelfPriming();
		baseProcessRadiation = recipe.getFissionFuelRadiation();
		return true;
	}
	
	// Processing
	
	public boolean isProcessing(boolean checkCluster) {
		return readyToProcess(checkCluster) && hasEnoughFlux();
	}
	
	public boolean readyToProcess(boolean checkCluster) {
		return canProcessInputs && hasConsumed && isMultiblockAssembled() && !(checkCluster && cluster == null);
	}
	
	public boolean hasEnoughFlux() {
		return vesselBunch != null && vesselBunch.flux >= vesselBunch.getCriticalityFactor(getCriticality());
	}
	
	public boolean hasConsumed() {
		if (world.isRemote) {
			return hasConsumed;
		}
		for (int i = 0; i < fluidInputSize; ++i) {
			if (!consumedTanks.get(i).isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean canProcessInputs() {
		boolean validRecipe = setRecipeStats(), canProcess = validRecipe && canProduceProducts();
		if (hasConsumed && !validRecipe) {
			for (Tank tank : getFluidInputs(true)) {
				tank.setFluidStored(null);
			}
			hasConsumed = false;
		}
		if (!canProcess) {
			time = MathHelper.clamp(time, 0D, baseProcessTime - 1D);
		}
		return canProcess;
	}
	
	public boolean canProduceProducts() {
		for (int j = 0; j < fluidOutputSize; ++j) {
			IFluidIngredient fluidProduct = getFluidProducts().get(j);
			if (fluidProduct.getMaxStackSize(0) <= 0) {
				continue;
			}
			if (fluidProduct.getStack() == null) {
				return false;
			}
			else if (!getTanks().get(j + fluidInputSize).isEmpty()) {
				if (!getTanks().get(j + fluidInputSize).getFluid().isFluidEqual(fluidProduct.getStack())) {
					return false;
				}
				else if (getTanks().get(j + fluidInputSize).getFluidAmount() + fluidProduct.getMaxStackSize(0) > getTanks().get(j + fluidInputSize).getCapacity()) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void consumeInputs() {
		if (hasConsumed || recipeInfo == null) {
			return;
		}
		IntList fluidInputOrder = recipeInfo.getFluidInputOrder();
		if (fluidInputOrder == AbstractRecipeHandler.INVALID) {
			return;
		}
		
		for (int i = 0; i < fluidInputSize; ++i) {
			if (!consumedTanks.get(i).isEmpty()) {
				consumedTanks.get(i).setFluidStored(null);
			}
		}
		for (int i = 0; i < fluidInputSize; ++i) {
			Tank tank = getTanks().get(i);
			int maxStackSize = getFluidIngredients().get(fluidInputOrder.get(i)).getMaxStackSize(recipeInfo.getFluidIngredientNumbers().get(i));
			if (maxStackSize > 0) {
				consumedTanks.get(i).setFluidStored(new FluidStack(tank.getFluid(), maxStackSize));
				tank.changeFluidAmount(-maxStackSize);
			}
			if (tank.isEmpty()) {
				tank.setFluidStored(null);
			}
		}
		hasConsumed = true;
	}
	
	public void process() {
		time += getSpeedMultiplier();
		getRadiationSource().setRadiationLevel(baseProcessRadiation * getSpeedMultiplier());
		while (time >= baseProcessTime) {
			finishProcess();
		}
	}
	
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
	
	public void produceProducts() {
		for (int i = 0; i < fluidInputSize; ++i) {
			consumedTanks.get(i).setFluidStored(null);
		}
		
		if (!hasConsumed || recipeInfo == null) {
			return;
		}
		
		for (int j = 0; j < fluidOutputSize; ++j) {
			IFluidIngredient fluidProduct = getFluidProducts().get(j);
			if (fluidProduct.getNextStackSize(0) <= 0) {
				continue;
			}
			if (getTanks().get(j + fluidInputSize).isEmpty()) {
				getTanks().get(j + fluidInputSize).setFluidStored(fluidProduct.getNextStack(0));
			}
			else if (getTanks().get(j + fluidInputSize).getFluid().isFluidEqual(fluidProduct.getStack())) {
				getTanks().get(j + fluidInputSize).changeFluidAmount(fluidProduct.getNextStackSize(0));
			}
		}
		hasConsumed = false;
	}
	
	// IProcessor
	
	@Override
	public int getFluidInputSize() {
		return fluidInputSize;
	}
	
	@Override
	public int getFluidOutputputSize() {
		return fluidOutputSize;
	}
	
	@Override
	public int getOtherSlotsSize() {
		return 0;
	}
	
	@Override
	public List<Tank> getFluidInputs(boolean consumed) {
		return consumed ? consumedTanks : getTanks().subList(0, fluidInputSize);
	}
	
	@Override
	public List<IFluidIngredient> getFluidIngredients() {
		return recipeInfo.getRecipe().getFluidIngredients();
	}
	
	@Override
	public List<IFluidIngredient> getFluidProducts() {
		return recipeInfo.getRecipe().getFluidProducts();
	}
	
	// Fluids
	
	@Override
	public @Nonnull List<Tank> getTanks() {
		return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getTanks() : tanks;
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
		return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getFluidSides() : fluidSides;
	}
	
	@Override
	public @Nonnull GasTileWrapper getGasWrapper() {
		return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getGasWrapper() : gasWrapper;
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
	
	@Override
	public void clearAllTanks() {
		IFluidGenerator.super.clearAllTanks();
		for (Tank tank : consumedTanks) {
			tank.setFluidStored(null);
		}
		
		hasConsumed = false;
		refreshRecipe();
		refreshActivity();
		refreshIsProcessing(true);
	}
	
	// ITileFilteredFluid
	
	@Override
	public @Nonnull List<Tank> getTanksInternal() {
		return tanks;
	}
	
	@Override
	public @Nonnull List<Tank> getFilterTanks() {
		return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getFilterTanks() : filterTanks;
	}
	
	@Override
	public boolean canModifyFilter(int tank) {
		return getMultiblock() != null ? !getMultiblock().isAssembled() : true;
	}
	
	@Override
	public void onFilterChanged(int slot) {
		markDirty();
	}
	
	@Override
	public Object getFilterKey() {
		return getFilterTanks().get(0).getFluidName();
	}
	
	// ITileGui
	
	@Override
	public int getGuiID() {
		return 202;
	}
	
	@Override
	public Set<EntityPlayer> getTileUpdatePacketListeners() {
		return updatePacketListeners;
	}
	
	@Override
	public SaltFissionVesselUpdatePacket getTileUpdatePacket() {
		return new SaltFissionVesselUpdatePacket(pos, masterPortPos, getTanks(), getFilterTanks(), cluster, isProcessing, time, baseProcessTime);
	}
	
	@Override
	public void onTileUpdatePacket(SaltFissionVesselUpdatePacket message) {
		masterPortPos = message.masterPortPos;
		if (DEFAULT_NON.equals(masterPortPos) ^ masterPort == null) {
			refreshMasterPort();
		}
		for (int i = 0; i < getTanks().size(); ++i) {
			getTanks().get(i).readInfo(message.tanksInfo.get(i));
		}
		for (int i = 0; i < getFilterTanks().size(); ++i) {
			getFilterTanks().get(i).readInfo(message.filterTanksInfo.get(i));
		}
		clusterHeatStored = message.clusterHeatStored;
		clusterHeatCapacity = message.clusterHeatCapacity;
		isProcessing = message.isProcessing;
		time = message.time;
		baseProcessTime = message.baseProcessTime;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		writeTanks(nbt);
		
		nbt.setDouble("baseProcessTime", baseProcessTime);
		nbt.setInteger("baseProcessHeat", baseProcessHeat);
		nbt.setDouble("baseProcessEfficiency", baseProcessEfficiency);
		nbt.setInteger("baseProcessCriticality", baseProcessCriticality);
		nbt.setDouble("baseProcessDecayFactor", baseProcessDecayFactor);
		nbt.setBoolean("selfPriming", selfPriming);
		
		nbt.setDouble("time", time);
		nbt.setBoolean("isProcessing", isProcessing);
		nbt.setBoolean("hasConsumed", hasConsumed);
		nbt.setBoolean("canProcessInputs", canProcessInputs);
		
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
		readTanks(nbt);
		
		baseProcessTime = nbt.getDouble("baseProcessTime");
		baseProcessHeat = nbt.getInteger("baseProcessHeat");
		baseProcessEfficiency = nbt.getDouble("baseProcessEfficiency");
		baseProcessCriticality = nbt.getInteger("baseProcessCriticality");
		baseProcessDecayFactor = nbt.getDouble("baseProcessDecayFactor");
		selfPriming = nbt.getBoolean("selfPriming");
		
		time = nbt.getDouble("time");
		isProcessing = nbt.getBoolean("isProcessing");
		hasConsumed = nbt.getBoolean("hasConsumed");
		canProcessInputs = nbt.getBoolean("canProcessInputs");
		
		decayProcessHeat = nbt.getDouble("decayProcessHeat");
		decayHeatFraction = nbt.getDouble("decayHeatFraction");
		iodineFraction = nbt.getDouble("iodineFraction");
		poisonFraction = nbt.getDouble("poisonFraction");
		
		flux = nbt.getLong("flux");
		heat = nbt.getLong("clusterHeat");
	}
	
	@Override
	public NBTTagCompound writeTanks(NBTTagCompound nbt) {
		for (int i = 0; i < tanks.size(); ++i) {
			tanks.get(i).writeToNBT(nbt, "tanks" + i);
		}
		for (int i = 0; i < filterTanks.size(); ++i) {
			filterTanks.get(i).writeToNBT(nbt, "filterTanks" + i);
		}
		for (int i = 0; i < consumedTanks.size(); ++i) {
			consumedTanks.get(i).writeToNBT(nbt, "consumedTanks" + i);
		}
		return nbt;
	}
	
	@Override
	public void readTanks(NBTTagCompound nbt) {
		for (int i = 0; i < tanks.size(); ++i) {
			tanks.get(i).readFromNBT(nbt, "tanks" + i);
		}
		for (int i = 0; i < filterTanks.size(); ++i) {
			filterTanks.get(i).readFromNBT(nbt, "filterTanks" + i);
		}
		for (int i = 0; i < consumedTanks.size(); ++i) {
			consumedTanks.get(i).readFromNBT(nbt, "consumedTanks" + i);
		}
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || ModCheck.mekanismLoaded() && enable_mek_gas && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY) {
			return !getTanks().isEmpty() && hasFluidSideCapability(side);
		}
		return super.hasCapability(capability, side);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			if (!getTanks().isEmpty() && hasFluidSideCapability(side)) {
				return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(getFluidSide(nonNullSide(side)));
			}
			return null;
		}
		else if (ModCheck.mekanismLoaded() && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY) {
			if (enable_mek_gas && !getTanks().isEmpty() && hasFluidSideCapability(side)) {
				return CapabilityHelper.GAS_HANDLER_CAPABILITY.cast(getGasWrapper());
			}
			return null;
		}
		return super.getCapability(capability, side);
	}
}
