package nc.multiblock.fission;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.objects.*;
import nc.init.*;
import nc.multiblock.*;
import nc.network.multiblock.FissionUpdatePacket;
import nc.tile.fission.*;
import nc.tile.fission.IFissionFuelComponent.ModeratorBlockInfo;
import nc.tile.fission.TileFissionSource.PrimingTargetInfo;
import nc.tile.fission.manager.*;
import nc.tile.fission.port.TileFissionIrradiatorPort;
import nc.tile.internal.energy.EnergyStorage;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.heat.HeatBuffer;
import nc.tile.multiblock.TilePartAbstract.SyncReason;
import nc.util.NCMath;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.BiPredicate;

import static nc.config.NCConfig.*;

public class FissionReactorLogic extends MultiblockLogic<FissionReactor, FissionReactorLogic, IFissionPart> implements IPacketMultiblockLogic<FissionReactor, FissionReactorLogic, IFissionPart, FissionUpdatePacket> {
	
	public final HeatBuffer heatBuffer = new HeatBuffer(FissionReactor.BASE_MAX_HEAT);
	
	public final Long2ObjectMap<IFissionComponent> componentFailCache = new Long2ObjectOpenHashMap<>(), assumedValidCache = new Long2ObjectOpenHashMap<>();
	
	public final ObjectSet<FissionFuelBunch> fuelBunches = new ObjectOpenHashSet<>();
	
	public FissionReactorLogic(FissionReactor reactor) {
		super(reactor);
	}
	
	public FissionReactorLogic(FissionReactorLogic oldLogic) {
		super(oldLogic);
	}
	
	@Override
	public String getID() {
		return "";
	}
	
	protected Int2ObjectMap<FissionCluster> getClusterMap() {
		return multiblock.getClusterMap();
	}
	
	public void onResetStats() {}
	
	// Multiblock Size Limits
	
	@Override
	public int getMinimumInteriorLength() {
		return fission_min_size;
	}
	
	@Override
	public int getMaximumInteriorLength() {
		return fission_max_size;
	}
	
	// Multiblock Methods
	
	@Override
	public void onMachineAssembled() {
		onReactorFormed();
	}
	
	@Override
	public void onMachineRestored() {
		onReactorFormed();
	}
	
	public void onReactorFormed() {
		for (IFissionController<?> contr : getParts(IFissionController.class)) {
			multiblock.controller = contr;
			break;
		}
		
		heatBuffer.setHeatCapacity(FissionReactor.BASE_MAX_HEAT * getCapacityMultiplier());
		multiblock.ambientTemp = 273.15D + 20D * getWorld().getBiome(multiblock.getMiddleCoord()).getTemperature(multiblock.getMiddleCoord());
		
		if (!getWorld().isRemote) {
			refreshConnections();
			multiblock.refreshFlag = true;
			multiblock.checkRefresh();
		}
	}
	
	public int getCapacityMultiplier() {
		return Math.max(multiblock.getExteriorSurfaceArea(), multiblock.getInteriorVolume());
	}
	
	@Override
	public void onMachinePaused() {
		onReactorBroken();
	}
	
	@Override
	public void onMachineDisassembled() {
		onReactorBroken();
	}
	
	public void onReactorBroken() {
		if (!getWorld().isRemote) {
			refreshConnections();
			multiblock.updateActivity();
		}
	}
	
	@Override
	public boolean isMachineWhole() {
		multiblock.setLastError("zerocore.api.nc.multiblock.validation.invalid_logic", Collections.emptyList());
		return false;
	}
	
	public boolean isMissingSorption() {
		return isMissingSorption(TileFissionIrradiatorPort.class, TileFissionIrradiator.class, NCBlocks.fission_irradiator_port.getLocalizedName());
	}
	
	@Override
	public List<Pair<Class<? extends IFissionPart>, String>> getPartBlacklist() {
		return Collections.emptyList();
	}
	
	@Override
	public void onAssimilate(FissionReactor assimilated) {
		heatBuffer.mergeHeatBuffers(assimilated.getLogic().heatBuffer);
		
		/*if (multiblock.isAssembled()) {
			onReactorFormed();
		}
		else {
			onReactorBroken();
		}*/
	}
	
	@Override
	public void onAssimilated(FissionReactor assimilator) {}
	
	public void refreshConnections() {
		refreshManagers(TileFissionSourceManager.class);
		refreshManagers(TileFissionShieldManager.class);
		refreshFilteredPorts(TileFissionIrradiatorPort.class, TileFissionIrradiator.class);
	}
	
	public <T extends IFissionFuelBunchComponent> void formFuelBunches(Class<T> fuelBunchComponentClass, BiPredicate<T, T> bunchingPredicate) {
		fuelBunches.clear();
		
		Long2ObjectMap<T> fuelBunchComponentMap = getPartMap(fuelBunchComponentClass);
		
		for (T fuelBunchComponent : fuelBunchComponentMap.values()) {
			fuelBunchComponent.setFuelBunch(null);
		}
		
		for (T fuelBunchComponent : fuelBunchComponentMap.values()) {
			boolean setBunch = false;
			FissionFuelBunch fuelBunch = null;
			BlockPos pos = fuelBunchComponent.getTilePos();
			for (EnumFacing dir : EnumFacing.VALUES) {
				T otherBunchComponent = fuelBunchComponentMap.get(pos.offset(dir).toLong());
				if (otherBunchComponent != null && bunchingPredicate.test(fuelBunchComponent, otherBunchComponent)) {
					FissionFuelBunch otherFuelBunch = otherBunchComponent.getFuelBunch();
					if (fuelBunch == null) {
						fuelBunch = otherFuelBunch;
					}
					else if (fuelBunch != otherFuelBunch) {
						if (otherFuelBunch != null) {
							fuelBunches.remove(otherFuelBunch);
							for (IFissionFuelBunchComponent otherFuelBunchComponent : otherFuelBunch.fuelComponentMap.values()) {
								otherFuelBunchComponent.setFuelBunch(fuelBunch);
							}
							otherFuelBunch.fuelComponentMap.clear();
						}
						else {
							otherBunchComponent.setFuelBunch(fuelBunch);
						}
					}
					
					if (!setBunch && fuelBunch != null) {
						fuelBunchComponent.setFuelBunch(fuelBunch);
						setBunch = true;
					}
				}
			}
			
			if (fuelBunch == null) {
				fuelBunch = new FissionFuelBunch();
				fuelBunchComponent.setFuelBunch(fuelBunch);
				fuelBunches.add(fuelBunch);
			}
		}
		
		for (FissionFuelBunch fuelBunch : fuelBunches) {
			fuelBunch.init();
		}
	}
	
	public void refreshReactor(boolean simulate) {
		if (multiblock.isSimulation) {
			for (IFissionComponent component : getParts(IFissionComponent.class)) {
				component.setCluster(null);
				component.resetStats();
			}
		}
		
		componentFailCache.clear();
		do {
			assumedValidCache.clear();
			refreshFlux(simulate);
			refreshClusters(simulate);
		}
		while (multiblock.refreshFlag);
		
		refreshReactorStats(simulate);
	}
	
	public void refreshFlux(boolean simulate) {
		final Long2ObjectMap<IFissionFuelComponent> primedFailCache = new Long2ObjectOpenHashMap<>();
		do {
			multiblock.refreshFlag = false;
			if (!multiblock.isAssembled()) {
				return;
			}
			
			final ObjectSet<IFissionFuelComponent> primedCache = new ObjectOpenHashSet<>();
			for (IFissionComponent component : getParts(IFissionComponent.class)) {
				if (component instanceof IFissionFuelComponent fuelComponent) {
					fuelComponent.refreshIsProcessing(false, simulate);
					if (fuelComponent.isInitiallyPrimed(simulate) && !primedFailCache.containsKey(fuelComponent.getTilePos().toLong())) {
						fuelComponent.tryPriming(multiblock, false, simulate);
						if (fuelComponent.isPrimed(simulate)) {
							fuelComponent.addToPrimedCache(primedCache);
						}
					}
				}
				component.setCluster(null);
				component.resetStats();
			}
			
			for (FissionCluster cluster : multiblock.clusterMap.values()) {
				cluster.distributeHeatToComponents(simulate);
				cluster.getComponentMap().clear();
			}
			multiblock.clusterMap.clear();
			
			multiblock.clusterCount = 0;
			multiblock.passiveModeratorCache.clear();
			multiblock.activeModeratorCache.clear();
			multiblock.activeReflectorCache.clear();
			
			distributeFlux(primedCache, primedFailCache, simulate);
		}
		while (multiblock.refreshFlag);
	}
	
	public void distributeFlux(final ObjectSet<IFissionFuelComponent> primedCache, final Long2ObjectMap<IFissionFuelComponent> primedFailCache, boolean simulate) {
		for (TileFissionSource source : getParts(TileFissionSource.class)) {
			if (!simulate) {
				source.refreshIsRedstonePowered(getWorld(), source.getPos());
				source.setActivity(source.isActive = source.isSourceActive());
			}
			
			if (!simulate && !source.isActive) {
				continue;
			}
			
			PrimingTargetInfo targetInfo = source.getPrimingTarget(true, simulate);
			if (targetInfo == null) {
				continue;
			}
			
			IFissionFuelComponent fuelComponent = targetInfo.fuelComponent;
			if (fuelComponent == null || primedFailCache.containsKey(fuelComponent.getTilePos().toLong())) {
				continue;
			}
			
			fuelComponent.tryPriming(multiblock, true, simulate);
			if (fuelComponent.isPrimed(simulate)) {
				fuelComponent.addToPrimedCache(primedCache);
			}
		}
		
		for (IFissionFuelComponent primedComponent : primedCache) {
			iterateFluxSearch(primedComponent, simulate);
		}
		
		for (IFissionFuelComponent primedComponent : primedCache) {
			primedComponent.refreshIsProcessing(false, simulate);
			refreshFuelComponentLocal(primedComponent, simulate);
			primedComponent.unprime(simulate);
			
			if (!primedComponent.isFunctional(simulate)) {
				primedComponent.addToPrimedFailCache(primedFailCache);
				multiblock.refreshFlag = true;
			}
		}
	}
	
	public void refreshClusters(boolean simulate) {
		refreshAllFuelComponentModerators(simulate);
		
		multiblock.passiveModeratorCache.removeAll(multiblock.activeModeratorCache);
		
		for (IFissionComponent component : getParts(IFissionComponent.class)) {
			if (component != null && component.isClusterRoot()) {
				iterateClusterSearch(component, simulate);
			}
		}
		
		for (long posLong : multiblock.activeModeratorCache) {
			BlockPos pos = BlockPos.fromLong(posLong);
			for (EnumFacing dir : EnumFacing.VALUES) {
				IFissionComponent component = getPartMap(IFissionComponent.class).get(pos.offset(dir).toLong());
				if (component != null) {
					iterateClusterSearch(component, simulate);
				}
			}
		}
		
		for (long posLong : multiblock.activeReflectorCache) {
			BlockPos pos = BlockPos.fromLong(posLong);
			for (EnumFacing dir : EnumFacing.VALUES) {
				IFissionComponent component = getPartMap(IFissionComponent.class).get(pos.offset(dir).toLong());
				if (component != null) {
					iterateClusterSearch(component, simulate);
				}
			}
		}
		
		for (IFissionComponent component : assumedValidCache.values()) {
			if (!component.isFunctional(simulate)) {
				component.addToComponentFailCache(componentFailCache);
				multiblock.refreshFlag = true;
			}
		}
		
		if (multiblock.refreshFlag) {
			return;
		}
		
		for (IFissionSpecialPart part : getParts(IFissionSpecialPart.class)) {
			part.postClusterSearch(simulate);
		}
		
		for (FissionCluster cluster : multiblock.clusterMap.values()) {
			refreshClusterStats(cluster, simulate);
			cluster.recoverHeatFromComponents(simulate);
		}
		
		multiblock.sortClusters(simulate);
	}
	
	public void refreshAllFuelComponentModerators(boolean simulate) {}
	
	public void refreshClusterStats(FissionCluster cluster, boolean simulate) {
		cluster.componentCount = cluster.fuelComponentCount = 0;
		cluster.cooling = cluster.rawHeating = cluster.rawHeatingIgnoreCoolingPenalty = 0L;
		cluster.totalBaseFuelHeating = cluster.effectiveHeating = cluster.effectiveHeatingIgnoreCoolingPenalty = 0D;
		cluster.meanHeatMult = cluster.meanEfficiency = cluster.overcoolingEfficiencyFactor = cluster.undercoolingLifetimeFactor = 0D;
		
		if (!simulate) {
			cluster.heatBuffer.setHeatCapacity(FissionReactor.BASE_MAX_HEAT * cluster.getComponentMap().size());
		}
		
		incrementClusterStatsFromComponents(cluster, simulate);
		
		if (multiblock.refreshFlag) {
			return;
		}
		
		cluster.overcoolingEfficiencyFactor = cluster.cooling == 0L ? 1D : Math.min(1D, (double) (cluster.rawHeating + fission_cooling_efficiency_leniency) / (double) cluster.cooling);
		cluster.undercoolingLifetimeFactor = cluster.rawHeating == 0L ? 1D : Math.min(1D, (double) (cluster.cooling + fission_cooling_efficiency_leniency) / (double) cluster.rawHeating);
		cluster.effectiveHeating *= cluster.overcoolingEfficiencyFactor;
		
		cluster.rawHeating += cluster.rawHeatingIgnoreCoolingPenalty;
		cluster.effectiveHeating += cluster.effectiveHeatingIgnoreCoolingPenalty;
		
		cluster.meanHeatMult = cluster.totalBaseFuelHeating <= 0D ? 0D : (double) cluster.rawHeating / cluster.totalBaseFuelHeating;
		cluster.meanEfficiency = cluster.totalBaseFuelHeating <= 0D ? 0D : cluster.effectiveHeating / cluster.totalBaseFuelHeating;
		
		for (IFissionComponent component : cluster.getComponentMap().values()) {
			if (component instanceof IFissionFuelComponent fuelComponent) {
				fuelComponent.setUndercoolingLifetimeFactor(cluster.undercoolingLifetimeFactor);
			}
		}
	}
	
	public void incrementClusterStatsFromComponents(FissionCluster cluster, boolean simulate) {
		for (IFissionComponent component : cluster.getComponentMap().values()) {
			if (component.isFunctional(simulate)) {
				++cluster.componentCount;
				if (component instanceof IFissionHeatingComponent heatingComponent) {
					cluster.rawHeating += heatingComponent.getRawHeating(simulate);
					cluster.rawHeatingIgnoreCoolingPenalty += heatingComponent.getRawHeatingIgnoreCoolingPenalty(simulate);
					cluster.effectiveHeating += heatingComponent.getEffectiveHeating(simulate);
					cluster.effectiveHeatingIgnoreCoolingPenalty += heatingComponent.getEffectiveHeatingIgnoreCoolingPenalty(simulate);
					if (component instanceof IFissionFuelComponent fuelComponent) {
						++cluster.fuelComponentCount;
						cluster.totalBaseFuelHeating += fuelComponent.getBaseProcessHeat();
					}
				}
				if (component instanceof IFissionCoolingComponent coolingComponent) {
					cluster.cooling += coolingComponent.getCooling(simulate);
				}
			}
		}
	}
	
	public void iterateFluxSearch(IFissionFuelComponent rootFuelComponent, boolean simulate) {
		final ObjectSet<IFissionFuelComponent> fluxSearchCache = new ObjectOpenHashSet<>();
		rootFuelComponent.fluxSearch(fluxSearchCache, componentFailCache, assumedValidCache, simulate);
		
		do {
			final Iterator<IFissionFuelComponent> fluxSearchIterator = fluxSearchCache.iterator();
			final ObjectSet<IFissionFuelComponent> fluxSearchSubCache = new ObjectOpenHashSet<>();
			while (fluxSearchIterator.hasNext()) {
				IFissionFuelComponent fuelComponent = fluxSearchIterator.next();
				fluxSearchIterator.remove();
				fuelComponent.fluxSearch(fluxSearchSubCache, componentFailCache, assumedValidCache, simulate);
			}
			fluxSearchCache.addAll(fluxSearchSubCache);
		}
		while (!fluxSearchCache.isEmpty());
	}
	
	public void iterateClusterSearch(IFissionComponent rootComponent, boolean simulate) {
		final Object2IntMap<IFissionComponent> clusterSearchCache = new Object2IntOpenHashMap<>();
		rootComponent.clusterSearch(null, clusterSearchCache, componentFailCache, assumedValidCache, simulate);
		
		do {
			final Iterator<IFissionComponent> clusterSearchIterator = clusterSearchCache.keySet().iterator();
			final Object2IntMap<IFissionComponent> clusterSearchSubCache = new Object2IntOpenHashMap<>();
			while (clusterSearchIterator.hasNext()) {
				IFissionComponent component = clusterSearchIterator.next();
				Integer id = clusterSearchCache.get(component);
				clusterSearchIterator.remove();
				component.clusterSearch(id, clusterSearchSubCache, componentFailCache, assumedValidCache, simulate);
			}
			clusterSearchCache.putAll(clusterSearchSubCache);
		}
		while (!clusterSearchCache.isEmpty());
	}
	
	public void refreshReactorStats(boolean simulate) {
		multiblock.resetStats();
	}
	
	// Server
	
	@Override
	public boolean onUpdateServer() {
		if (fission_heat_dissipation[multiblock.isReactorOn ? 1 : 0]) {
			heatBuffer.changeHeatStored(-getHeatDissipation());
		}
		return false;
	}
	
	public boolean isReactorActive(boolean checkSimulation) {
		return (!checkSimulation || !multiblock.isSimulation) && multiblock.rawHeating > 0L;
	}
	
	public void playFuelComponentSounds(Class<? extends IFissionFuelComponent> clazz) {
		Collection<? extends IFissionFuelComponent> fuelComponents = getParts(clazz);
		int i = fuelComponents.size();
		for (IFissionFuelComponent fuelComponent : fuelComponents) {
			if ((i <= 0 || rand.nextDouble() < 1D / i) && playFissionSound(fuelComponent)) {
				return;
			}
			else if (fuelComponent.isFunctional(false)) {
				--i;
			}
		}
	}
	
	public boolean playFissionSound(IFissionFuelComponent fuelComponent) {
		if (multiblock.fuelComponentCount <= 0) {
			return true;
		}
		double soundRate = Math.min(1D, fuelComponent.getEfficiency(false) / (14D * fission_max_size));
		if (rand.nextDouble() < soundRate) {
			BlockPos pos = fuelComponent.getTilePos();
			getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(), NCSounds.geiger_tick, SoundCategory.BLOCKS, (float) (1.6D * Math.log1p(Math.sqrt(multiblock.fuelComponentCount)) * fission_sound_volume), 1F + 0.12F * (rand.nextFloat() - 0.5F));
			return true;
		}
		return false;
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void casingMeltdown() {
		Iterator<IFissionController> controllerIterator = getPartIterator(IFissionController.class);
		while (controllerIterator.hasNext()) {
			IFissionController controller = controllerIterator.next();
			controller.doMeltdown(controllerIterator);
		}
		
		// TODO - graphite fires
		
		// TODO - explosions if vents with water are present, melt casing if not
		if (getPartMap(TileFissionVent.class).isEmpty()) {
		
		}
		else {
		
		}
		
		MultiblockRegistry.INSTANCE.addDirtyMultiblock(getWorld(), multiblock);
	}
	
	public void clusterMeltdown(FissionCluster cluster) {
		MultiblockRegistry.INSTANCE.addDirtyMultiblock(getWorld(), multiblock);
	}
	
	public long getHeatDissipation() {
		long heatStored = heatBuffer.getHeatStored();
		return NCMath.clamp((long) (fission_heat_dissipation_rate * heatStored * multiblock.getExteriorSurfaceArea()), 1L, heatStored);
	}
	
	public double getTemperature() {
		return multiblock.ambientTemp + (FissionReactor.MAX_TEMP - multiblock.ambientTemp) * (double) heatBuffer.getHeatStored() / (double) heatBuffer.getHeatCapacity();
	}
	
	public float getBurnDamage() {
		double temp = getTemperature();
		return temp < 353D ? 0F : (float) (1D + (temp - 353D) / 200D);
	}
	
	// Component Logic
	
	public void onSourceUpdated(TileFissionSource source) {
		if (source.isActive) {
			PrimingTargetInfo targetInfo = source.getPrimingTarget(true, false);
			if (targetInfo != null) {
				if (!targetInfo.fuelComponent.isFunctional(false)) {
					multiblock.refreshFlag = true;
				}
				else if (targetInfo.newSourceEfficiency) {
					multiblock.refreshCluster(targetInfo.fuelComponent.getCluster(), false);
				}
			}
		}
	}
	
	public void onShieldUpdated(TileFissionShield shield) {
		if (shield.inCompleteModeratorLine) {
			multiblock.refreshFlag = true;
		}
	}
	
	public void distributeFluxFromFuelComponent(IFissionFuelComponent fuelComponent, final ObjectSet<IFissionFuelComponent> fluxSearchCache, final Long2ObjectMap<IFissionComponent> currentComponentFailCache, final Long2ObjectMap<IFissionComponent> currentAssumedValidCache, boolean simulate) {}
	
	public IFissionFuelComponent getNextFuelComponent(IFissionFuelComponent fuelComponent, BlockPos pos) {
		IFissionComponent component = getPartMap(IFissionComponent.class).get(pos.toLong());
		return component instanceof IFissionFuelComponent ? (IFissionFuelComponent) component : null;
	}
	
	public void refreshFuelComponentLocal(IFissionFuelComponent fuelComponent, boolean simulate) {}
	
	public void refreshFuelComponentModerators(IFissionFuelComponent fuelComponent, final Long2ObjectMap<IFissionComponent> currentComponentFailCache, final Long2ObjectMap<IFissionComponent> currentAssumedValidCache, boolean simulate) {}
	
	public boolean isShieldActiveModerator(TileFissionShield shield, boolean activeModeratorPos) {
		return false;
	}
	
	public ModeratorBlockInfo getShieldModeratorBlockInfo(TileFissionShield shield, boolean validActiveModerator) {
		return new ModeratorBlockInfo(shield.getPos(), shield, shield.isShielding, validActiveModerator, 0, shield.efficiency);
	}
	
	public @Nonnull EnergyStorage getPowerPortEnergyStorage(EnergyStorage backupStorage) {
		return backupStorage;
	}
	
	public int getPowerPortEUSinkTier() {
		return 10;
	}
	
	public int getPowerPortEUSourceTier() {
		return 1;
	}
	
	public @Nonnull List<Tank> getVentTanks(List<Tank> backupTanks) {
		return backupTanks;
	}
	
	// Client
	
	@Override
	public void onUpdateClient() {}
	
	// NBT
	
	@Override
	public void writeToLogicTag(NBTTagCompound logicTag, SyncReason syncReason) {
		heatBuffer.writeToNBT(logicTag, "heatBuffer");
	}
	
	@Override
	public void readFromLogicTag(NBTTagCompound logicTag, SyncReason syncReason) {
		heatBuffer.readFromNBT(logicTag, "heatBuffer");
	}
	
	// Packets
	
	@Override
	public FissionUpdatePacket getMultiblockUpdatePacket() {
		return null;
	}
	
	@Override
	public void onMultiblockUpdatePacket(FissionUpdatePacket message) {
		heatBuffer.setHeatStored(message.heatStored);
		heatBuffer.setHeatCapacity(message.heatCapacity);
	}
	
	// Clear Material
	
	@Override
	public void clearAllMaterial() {}
}
