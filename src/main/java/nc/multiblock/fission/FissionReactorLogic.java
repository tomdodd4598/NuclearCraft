package nc.multiblock.fission;

import static nc.block.property.BlockProperties.*;
import static nc.config.NCConfig.*;

import java.util.*;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.objects.*;
import nc.init.NCSounds;
import nc.multiblock.*;
import nc.multiblock.fission.tile.*;
import nc.multiblock.fission.tile.IFissionFuelComponent.ModeratorBlockInfo;
import nc.multiblock.fission.tile.TileFissionSource.PrimingTargetInfo;
import nc.multiblock.fission.tile.manager.TileFissionShieldManager;
import nc.multiblock.fission.tile.port.TileFissionIrradiatorPort;
import nc.multiblock.tile.TileBeefAbstract.SyncReason;
import nc.network.multiblock.FissionUpdatePacket;
import nc.tile.internal.energy.EnergyStorage;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.heat.HeatBuffer;
import nc.util.NCMath;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;

public class FissionReactorLogic extends MultiblockLogic<FissionReactor, FissionReactorLogic, IFissionPart> implements IPacketMultiblockLogic<FissionReactor, FissionReactorLogic, IFissionPart, FissionUpdatePacket> {
	
	public final HeatBuffer heatBuffer = new HeatBuffer(FissionReactor.BASE_MAX_HEAT);
	
	public final Long2ObjectMap<IFissionComponent> componentFailCache = new Long2ObjectOpenHashMap<>(), assumedValidCache = new Long2ObjectOpenHashMap<>();
	
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
	
	protected FissionReactor getReactor() {
		return multiblock;
	}
	
	protected Int2ObjectMap<FissionCluster> getClusterMap() {
		return getReactor().getClusterMap();
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
			getReactor().controller = contr;
		}
		
		heatBuffer.setHeatCapacity(FissionReactor.BASE_MAX_HEAT * getCapacityMultiplier());
		getReactor().ambientTemp = 273 + (int) (getWorld().getBiome(getReactor().getMiddleCoord()).getTemperature(getReactor().getMiddleCoord()) * 20F);
		
		if (!getWorld().isRemote) {
			refreshConnections();
			refreshReactor();
			getReactor().updateActivity();
		}
	}
	
	public int getCapacityMultiplier() {
		return Math.max(getReactor().getExteriorSurfaceArea(), getReactor().getInteriorVolume());
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
			getReactor().updateActivity();
		}
	}
	
	@Override
	public boolean isMachineWhole() {
		multiblock.setLastError("zerocore.api.nc.multiblock.validation.invalid_logic", null);
		return false;
	}
	
	@Override
	public List<Pair<Class<? extends IFissionPart>, String>> getPartBlacklist() {
		return new ArrayList<>();
	}
	
	@Override
	public void onAssimilate(FissionReactor assimilated) {
		heatBuffer.mergeHeatBuffers(assimilated.getLogic().heatBuffer);
		
		/*if (getReactor().isAssembled()) {
			onReactorFormed();
		}
		else {
			onReactorBroken();
		}*/
	}
	
	@Override
	public void onAssimilated(FissionReactor assimilator) {}
	
	public void refreshConnections() {
		refreshFilteredPorts(TileFissionIrradiatorPort.class, TileFissionIrradiator.class);
	}
	
	public void refreshReactor() {
		componentFailCache.clear();
		do {
			assumedValidCache.clear();
			refreshFlux();
			refreshClusters();
		}
		while (getReactor().refreshFlag);
		
		refreshReactorStats();
	}
	
	public void refreshManagers() {
		refreshManagers(TileFissionShieldManager.class);
	}
	
	public void refreshFlux() {
		final Long2ObjectMap<IFissionFuelComponent> primedFailCache = new Long2ObjectOpenHashMap<>();
		do {
			getReactor().refreshFlag = false;
			if (!getReactor().isAssembled()) {
				return;
			}
			
			final ObjectSet<IFissionFuelComponent> primedCache = new ObjectOpenHashSet<>();
			for (IFissionComponent component : getParts(IFissionComponent.class)) {
				if (component instanceof IFissionFuelComponent) {
					IFissionFuelComponent fuelComponent = (IFissionFuelComponent) component;
					fuelComponent.refreshIsProcessing(false);
					if ((fuelComponent.isFunctional() || fuelComponent.isSelfPriming()) && !primedFailCache.containsKey(fuelComponent.getTilePos().toLong())) {
						fuelComponent.tryPriming(getReactor(), false);
						if (fuelComponent.isPrimed()) {
							fuelComponent.addToPrimedCache(primedCache);
						}
					}
				}
				component.setCluster(null);
				component.resetStats();
			}
			
			for (FissionCluster cluster : getReactor().clusterMap.values()) {
				cluster.distributeHeatToComponents();
				cluster.getComponentMap().clear();
			}
			getReactor().clusterMap.clear();
			
			getReactor().clusterCount = 0;
			getReactor().passiveModeratorCache.clear();
			getReactor().activeModeratorCache.clear();
			getReactor().activeReflectorCache.clear();
			
			distributeFlux(primedCache, primedFailCache);
		}
		while (getReactor().refreshFlag);
	}
	
	public void distributeFlux(final ObjectSet<IFissionFuelComponent> primedCache, final Long2ObjectMap<IFissionFuelComponent> primedFailCache) {
		for (TileFissionSource source : getParts(TileFissionSource.class)) {
			IBlockState state = getWorld().getBlockState(source.getPos());
			EnumFacing facing = source.getPartPosition().getFacing();
			source.refreshIsRedstonePowered(getWorld(), source.getPos());
			getWorld().setBlockState(source.getPos(), state.withProperty(FACING_ALL, facing != null ? facing : state.getValue(FACING_ALL)).withProperty(ACTIVE, source.getIsRedstonePowered()), 3);
			
			if (!source.getIsRedstonePowered()) {
				continue;
			}
			PrimingTargetInfo targetInfo = source.getPrimingTarget(false);
			if (targetInfo == null) {
				continue;
			}
			IFissionFuelComponent fuelComponent = targetInfo.fuelComponent;
			if (fuelComponent == null || primedFailCache.containsKey(fuelComponent.getTilePos().toLong())) {
				continue;
			}
			
			fuelComponent.tryPriming(getReactor(), true);
			if (fuelComponent.isPrimed()) {
				fuelComponent.addToPrimedCache(primedCache);
			}
		}
		
		for (IFissionFuelComponent primedComponent : primedCache) {
			iterateFluxSearch(primedComponent);
		}
		
		for (IFissionFuelComponent primedComponent : primedCache) {
			primedComponent.refreshIsProcessing(false);
			refreshFuelComponentLocal(primedComponent);
			primedComponent.unprime();
			
			if (!primedComponent.isFunctional()) {
				primedFailCache.put(primedComponent.getTilePos().toLong(), primedComponent);
				getReactor().refreshFlag = true;
			}
		}
	}
	
	public void refreshClusters() {
		refreshAllFuelComponentModerators();
		
		getReactor().passiveModeratorCache.removeAll(getReactor().activeModeratorCache);
		
		for (IFissionComponent component : getParts(IFissionComponent.class)) {
			if (component != null && component.isClusterRoot()) {
				iterateClusterSearch(component);
			}
		}
		
		for (long posLong : getReactor().activeModeratorCache) {
			for (EnumFacing dir : EnumFacing.VALUES) {
				IFissionComponent component = getPartMap(IFissionComponent.class).get(BlockPos.fromLong(posLong).offset(dir).toLong());
				if (component != null) {
					iterateClusterSearch(component);
				}
			}
		}
		
		for (long posLong : getReactor().activeReflectorCache) {
			for (EnumFacing dir : EnumFacing.VALUES) {
				IFissionComponent component = getPartMap(IFissionComponent.class).get(BlockPos.fromLong(posLong).offset(dir).toLong());
				if (component != null) {
					iterateClusterSearch(component);
				}
			}
		}
		
		for (IFissionComponent component : assumedValidCache.values()) {
			if (!component.isFunctional()) {
				componentFailCache.put(component.getTilePos().toLong(), component);
				getReactor().refreshFlag = true;
			}
		}
		
		if (getReactor().refreshFlag) {
			return;
		}
		
		for (IFissionSpecialComponent component : getParts(IFissionSpecialComponent.class)) {
			component.postClusterSearch();
		}
		
		for (FissionCluster cluster : getReactor().clusterMap.values()) {
			refreshClusterStats(cluster);
			cluster.recoverHeatFromComponents();
		}
		
		getReactor().sortClusters();
	}
	
	public void refreshAllFuelComponentModerators() {}
	
	public void refreshClusterStats(FissionCluster cluster) {
		cluster.componentCount = cluster.fuelComponentCount = 0;
		cluster.cooling = cluster.rawHeating = cluster.rawHeatingIgnoreCoolingPenalty = cluster.totalHeatMult = 0L;
		cluster.effectiveHeating = cluster.effectiveHeatingIgnoreCoolingPenalty = cluster.meanHeatMult = cluster.totalEfficiency = cluster.totalEfficiencyIgnoreCoolingPenalty = cluster.meanEfficiency = cluster.overcoolingEfficiencyFactor = cluster.undercoolingLifetimeFactor = cluster.meanHeatingSpeedMultiplier = 0D;
		
		cluster.heatBuffer.setHeatCapacity(FissionReactor.BASE_MAX_HEAT * cluster.getComponentMap().size());
		
		incrementClusterStatsFromComponents(cluster);
		
		if (getReactor().refreshFlag) {
			return;
		}
		
		cluster.overcoolingEfficiencyFactor = cluster.cooling == 0L ? 1D : Math.min(1D, (double) (cluster.rawHeating + fission_cooling_efficiency_leniency) / (double) cluster.cooling);
		cluster.undercoolingLifetimeFactor = cluster.rawHeating == 0L ? 1D : Math.min(1D, (double) (cluster.cooling + fission_cooling_efficiency_leniency) / (double) cluster.rawHeating);
		cluster.effectiveHeating *= cluster.overcoolingEfficiencyFactor;
		cluster.totalEfficiency *= cluster.overcoolingEfficiencyFactor;
		
		cluster.rawHeating += cluster.rawHeatingIgnoreCoolingPenalty;
		cluster.effectiveHeating += cluster.effectiveHeatingIgnoreCoolingPenalty;
		cluster.totalEfficiency += cluster.totalEfficiencyIgnoreCoolingPenalty;
		
		cluster.meanHeatMult = cluster.fuelComponentCount == 0 ? 0D : (double) cluster.totalHeatMult / (double) cluster.fuelComponentCount;
		cluster.meanEfficiency = cluster.fuelComponentCount == 0 ? 0D : cluster.totalEfficiency / cluster.fuelComponentCount;
		
		for (IFissionComponent component : cluster.getComponentMap().values()) {
			if (component instanceof IFissionFuelComponent) {
				IFissionFuelComponent fuelComponent = (IFissionFuelComponent) component;
				fuelComponent.setUndercoolingLifetimeFactor(cluster.undercoolingLifetimeFactor);
			}
		}
	}
	
	public void incrementClusterStatsFromComponents(FissionCluster cluster) {
		for (IFissionComponent component : cluster.getComponentMap().values()) {
			if (component.isFunctional()) {
				++cluster.componentCount;
				if (component instanceof IFissionHeatingComponent) {
					cluster.rawHeating += ((IFissionHeatingComponent) component).getRawHeating();
					cluster.rawHeatingIgnoreCoolingPenalty += ((IFissionHeatingComponent) component).getRawHeatingIgnoreCoolingPenalty();
					cluster.effectiveHeating += ((IFissionHeatingComponent) component).getEffectiveHeating();
					cluster.effectiveHeatingIgnoreCoolingPenalty += ((IFissionHeatingComponent) component).getEffectiveHeatingIgnoreCoolingPenalty();
					if (component instanceof IFissionFuelComponent) {
						++cluster.fuelComponentCount;
						cluster.totalHeatMult += ((IFissionFuelComponent) component).getHeatMultiplier();
						cluster.totalEfficiency += ((IFissionFuelComponent) component).getEfficiency();
						cluster.totalEfficiencyIgnoreCoolingPenalty += ((IFissionFuelComponent) component).getEfficiencyIgnoreCoolingPenalty();
					}
				}
				if (component instanceof IFissionCoolingComponent) {
					cluster.cooling += ((IFissionCoolingComponent) component).getCooling();
				}
			}
		}
	}
	
	public void iterateFluxSearch(IFissionFuelComponent rootFuelComponent) {
		final ObjectSet<IFissionFuelComponent> fluxSearchCache = new ObjectOpenHashSet<>();
		rootFuelComponent.fluxSearch(fluxSearchCache, componentFailCache, assumedValidCache);
		
		do {
			final Iterator<IFissionFuelComponent> fluxSearchIterator = fluxSearchCache.iterator();
			final ObjectSet<IFissionFuelComponent> fluxSearchSubCache = new ObjectOpenHashSet<>();
			while (fluxSearchIterator.hasNext()) {
				IFissionFuelComponent fuelComponent = fluxSearchIterator.next();
				fluxSearchIterator.remove();
				fuelComponent.fluxSearch(fluxSearchSubCache, componentFailCache, assumedValidCache);
			}
			fluxSearchCache.addAll(fluxSearchSubCache);
		}
		while (!fluxSearchCache.isEmpty());
	}
	
	public void iterateClusterSearch(IFissionComponent rootComponent) {
		final Object2IntMap<IFissionComponent> clusterSearchCache = new Object2IntOpenHashMap<>();
		rootComponent.clusterSearch(null, clusterSearchCache, componentFailCache, assumedValidCache);
		do {
			final Iterator<IFissionComponent> clusterSearchIterator = clusterSearchCache.keySet().iterator();
			final Object2IntMap<IFissionComponent> clusterSearchSubCache = new Object2IntOpenHashMap<>();
			while (clusterSearchIterator.hasNext()) {
				IFissionComponent component = clusterSearchIterator.next();
				Integer id = clusterSearchCache.get(component);
				clusterSearchIterator.remove();
				component.clusterSearch(id, clusterSearchSubCache, componentFailCache, assumedValidCache);
			}
			clusterSearchCache.putAll(clusterSearchSubCache);
		}
		while (!clusterSearchCache.isEmpty());
	}
	
	public void refreshReactorStats() {
		getReactor().resetStats();
	}
	
	// Server
	
	@Override
	public boolean onUpdateServer() {
		if (fission_heat_dissipation[getReactor().isReactorOn ? 1 : 0]) {
			heatBuffer.changeHeatStored(-getHeatDissipation());
		}
		return false;
	}
	
	public boolean isReactorOn() {
		return getReactor().rawHeating > 0L;
	}
	
	public void updateRedstone() {
		
	}
	
	public void playFuelComponentSounds(Class<? extends IFissionFuelComponent> clazz) {
		Collection<? extends IFissionFuelComponent> fuelComponents = getParts(clazz);
		int i = fuelComponents.size();
		for (IFissionFuelComponent fuelComponent : fuelComponents) {
			if ((i <= 0 || rand.nextDouble() < 1D / i) && playFissionSound(fuelComponent)) {
				return;
			}
			else if (fuelComponent.isFunctional()) {
				--i;
			}
		}
	}
	
	public boolean playFissionSound(IFissionFuelComponent fuelComponent) {
		if (getReactor().fuelComponentCount <= 0) {
			return true;
		}
		double soundRate = Math.min(1D, fuelComponent.getEfficiency() / (14D * fission_max_size));
		if (rand.nextDouble() < soundRate) {
			getWorld().playSound(null, fuelComponent.getTilePos().getX(), fuelComponent.getTilePos().getY(), fuelComponent.getTilePos().getZ(), NCSounds.geiger_tick, SoundCategory.BLOCKS, (float) (1.6D * Math.log1p(Math.sqrt(getReactor().fuelComponentCount)) * fission_sound_volume), 1F + 0.12F * (rand.nextFloat() - 0.5F));
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
		
		MultiblockRegistry.INSTANCE.addDirtyMultiblock(getWorld(), getReactor());
	}
	
	public void clusterMeltdown(FissionCluster cluster) {
		MultiblockRegistry.INSTANCE.addDirtyMultiblock(getWorld(), getReactor());
	}
	
	// TODO - config
	public long getHeatDissipation() {
		return Math.max(1L, heatBuffer.getHeatStored() * getReactor().getExteriorSurfaceArea() / (NCMath.cube(6) * 672000L));
	}
	
	public int getTemperature() {
		return Math.round(getReactor().ambientTemp + (FissionReactor.MAX_TEMP - getReactor().ambientTemp) * (float) heatBuffer.getHeatStored() / heatBuffer.getHeatCapacity());
	}
	
	public float getBurnDamage() {
		return getTemperature() < 373 ? 0F : 1F + (getTemperature() - 373) / 200F;
	}
	
	// Component Logic
	
	public void onSourceUpdated(TileFissionSource source) {
		if (source.getIsRedstonePowered()) {
			PrimingTargetInfo targetInfo = source.getPrimingTarget(false);
			if (targetInfo != null) {
				if (!targetInfo.fuelComponent.isFunctional()) {
					getReactor().refreshFlag = true;
				}
				else if (targetInfo.newSourceEfficiency) {
					getReactor().refreshCluster(targetInfo.fuelComponent.getCluster());
				}
			}
		}
	}
	
	public void onShieldUpdated(TileFissionShield shield) {
		if (shield.inCompleteModeratorLine) {
			getReactor().refreshFlag = true;
		}
	}
	
	public void distributeFluxFromFuelComponent(IFissionFuelComponent fuelComponent, final ObjectSet<IFissionFuelComponent> fluxSearchCache, final Long2ObjectMap<IFissionComponent> currentComponentFailCache, final Long2ObjectMap<IFissionComponent> currentAssumedValidCache) {}
	
	public IFissionFuelComponent getNextFuelComponent(IFissionFuelComponent fuelComponent, BlockPos pos) {
		IFissionComponent component = getPartMap(IFissionComponent.class).get(pos.toLong());
		return component instanceof IFissionFuelComponent ? (IFissionFuelComponent) component : null;
	}
	
	public void refreshFuelComponentLocal(IFissionFuelComponent fuelComponent) {}
	
	public void refreshFuelComponentModerators(IFissionFuelComponent fuelComponent, final Long2ObjectMap<IFissionComponent> currentComponentFailCache, final Long2ObjectMap<IFissionComponent> currentAssumedValidCache) {}
	
	public boolean isShieldActiveModerator(TileFissionShield shield, boolean activeModeratorPos) {
		return false;
	}
	
	public ModeratorBlockInfo getShieldModeratorBlockInfo(TileFissionShield shield, boolean validActiveModerator) {
		return new ModeratorBlockInfo(shield.getPos(), shield, shield.isShielding, validActiveModerator, 0, shield.efficiency);
	}
	
	public @Nonnull EnergyStorage getPowerPortEnergyStorage(EnergyStorage backupStorage) {
		return backupStorage;
	}
	
	public int getPowerPortEUSourceTier() {
		return 1;
	}
	
	public int getPowerPortEUSinkTier() {
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
