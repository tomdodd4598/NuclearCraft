package nc.multiblock.fission;

import static nc.config.NCConfig.*;

import java.util.*;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.objects.*;
import nc.init.NCSounds;
import nc.multiblock.*;
import nc.multiblock.container.*;
import nc.multiblock.fission.tile.*;
import nc.multiblock.fission.tile.IFissionFuelComponent.ModeratorBlockInfo;
import nc.multiblock.fission.tile.TileFissionSource.PrimingTargetInfo;
import nc.multiblock.fission.tile.manager.TileFissionShieldManager;
import nc.multiblock.fission.tile.port.TileFissionIrradiatorPort;
import nc.multiblock.network.FissionUpdatePacket;
import nc.multiblock.tile.TileBeefAbstract.SyncReason;
import nc.tile.internal.energy.EnergyStorage;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.heat.HeatBuffer;
import nc.util.NCMath;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

public class FissionReactorLogic extends MultiblockLogic<FissionReactor, FissionReactorLogic, IFissionPart, FissionUpdatePacket> {
	
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
		for (IFissionController contr : getParts(IFissionController.class)) {
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
	public boolean isMachineWhole(Multiblock multiblock) {
		multiblock.setLastError("zerocore.api.nc.multiblock.validation.invalid_logic", null);
		return false;
	}
	
	@Override
	public List<Pair<Class<? extends IFissionPart>, String>> getPartBlacklist() {
		return new ArrayList<>();
	}
	
	public void onAssimilate(Multiblock assimilated) {
		if (assimilated instanceof FissionReactor) {
			FissionReactor assimilatedReactor = (FissionReactor) assimilated;
			heatBuffer.mergeHeatBuffers(assimilatedReactor.getLogic().heatBuffer);
		}
		
		if (getReactor().isAssembled()) {
			onReactorFormed();
		}
		else {
			onReactorBroken();
		}
	}
	
	public void onAssimilated(Multiblock assimilator) {}
	
	public void refreshConnections() {
		refreshFilteredPorts(TileFissionIrradiatorPort.class, TileFissionIrradiator.class);
	}
	
	public void refreshReactor() {
		componentFailCache.clear();
		do {
			assumedValidCache.clear();
			refreshFlux();
			refreshClusters();
		} while (getReactor().refreshFlag);
		
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
						fuelComponent.tryPriming(getReactor());
						if (fuelComponent.isPrimed()) {
							primedCache.add(fuelComponent);
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
		} while (getReactor().refreshFlag);
	}
	
	public void distributeFlux(final ObjectSet<IFissionFuelComponent> primedCache, final Long2ObjectMap<IFissionFuelComponent> primedFailCache) {}
	
	public void refreshClusters() {
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
	
	public void refreshClusterStats(FissionCluster cluster) {
		cluster.componentCount = cluster.fuelComponentCount = 0;
		cluster.cooling = cluster.rawHeating = cluster.totalHeatMult = 0L;
		cluster.effectiveHeating = cluster.meanHeatMult = cluster.totalEfficiency = cluster.meanEfficiency = cluster.overcoolingEfficiencyFactor = cluster.undercoolingLifetimeFactor = cluster.meanHeatingSpeedMultiplier = 0D;
		
		cluster.heatBuffer.setHeatCapacity(FissionReactor.BASE_MAX_HEAT * cluster.getComponentMap().size());
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
		} while (!fluxSearchCache.isEmpty());
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
		} while (!clusterSearchCache.isEmpty());
	}
	
	public void refreshReactorStats() {
		getReactor().resetStats();
	}
	
	// Server
	
	@Override
	public boolean onUpdateServer() {
		if (!getReactor().isReactorOn) {
			heatBuffer.changeHeatStored(-getHeatDissipation());
		}
		return false;
	}
	
	public boolean isReactorOn() {
		return getReactor().rawHeating > 0L;
	}
	
	public void updateRedstone() {
		/* getReactor().comparatorSignal = NCMath.getComparatorSignal(100D/fission_comparator_max_heat* getReactor().heatBuffer.heatStored, getReactor().heatBuffer.heatCapacity, 0D); for (TileSaltFissionRedstonePort redstonePort : getParts(TileSaltFissionRedstonePort.class)) { if (redstonePort.comparatorSignal != getReactor().comparatorSignal) { redstonePort.comparatorSignal = getReactor().comparatorSignal; getWorld().updateComparatorOutputLevel(redstonePort.getPos(), null); } } */
	}
	
	public void casingMeltdown() {
		Iterator<IFissionController> controllerIterator = getPartIterator(IFissionController.class);
		while (controllerIterator.hasNext()) {
			IFissionController controller = controllerIterator.next();
			controllerIterator.remove();
			controller.doMeltdown();
		}
		
		// TODO - graphite fires
		
		// TODO - explosions if vents with water are present, melt casing if not
		if (getPartMap(TileFissionVent.class).isEmpty()) {
			
		}
		else {
			
		}
		
		/* IBlockState corium = RegistryHelper.getBlock(Global.MOD_ID + ":fluid_corium").getDefaultState(); for (BlockPos blockPos : BlockPos.getAllInBoxMutable(getMinimumCoord(), getMaximumCoord())) { if (rand.nextDouble() < 0.18D) { WORLD.removeTileEntity(blockPos); WORLD.setBlockState(blockPos, corium); } } */
		
		getReactor().checkIfMachineIsWhole();
	}
	
	public void clusterMeltdown(FissionCluster cluster) {
		getReactor().checkIfMachineIsWhole();
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
	
	public void distributeFluxFromFuelComponent(IFissionFuelComponent fuelComponent, final ObjectSet<IFissionFuelComponent> fluxSearchCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {}
	
	public IFissionFuelComponent getNextFuelComponent(IFissionFuelComponent fuelComponent, BlockPos pos) {
		IFissionComponent component = getPartMap(IFissionComponent.class).get(pos.toLong());
		return component instanceof IFissionFuelComponent ? (IFissionFuelComponent) component : null;
	}
	
	public void refreshFuelComponentLocal(IFissionFuelComponent fuelComponent) {}
	
	public void refreshFuelComponentModerators(IFissionFuelComponent fuelComponent, final Long2ObjectMap<IFissionComponent> assumedValidCache) {}
	
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
	
	public @Nonnull List<Tank> getVentTanks(List<Tank> backupTanks) {
		return backupTanks;
	}
	
	// Client
	
	@Override
	public void onUpdateClient() {}
	
	public boolean playFissionSound(IFissionFuelComponent fuelComponent) {
		if (getReactor().fuelComponentCount <= 0) {
			return true;
		}
		double soundRate = Math.min(1D, getReactor().meanEfficiency / (14D * fission_max_size));
		if (rand.nextDouble() < soundRate) {
			getWorld().playSound(fuelComponent.getTilePos().getX(), fuelComponent.getTilePos().getY(), fuelComponent.getTilePos().getZ(), NCSounds.geiger_tick, SoundCategory.BLOCKS, (float) (1.6D * Math.log1p(Math.sqrt(getReactor().fuelComponentCount)) * fission_sound_volume), 1F + 0.12F * (rand.nextFloat() - 0.5F), false);
			return true;
		}
		return false;
	}
	
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
	public FissionUpdatePacket getUpdatePacket() {
		return null;
	}
	
	@Override
	public void onPacket(FissionUpdatePacket message) {
		heatBuffer.setHeatStored(message.heatStored);
		heatBuffer.setHeatCapacity(message.heatCapacity);
	}
	
	public ContainerMultiblockController<FissionReactor, IFissionController> getContainer(EntityPlayer player) {
		return new ContainerSolidFissionController(player, getReactor().controller);
	}
	
	public void clearAllMaterial() {}
}
