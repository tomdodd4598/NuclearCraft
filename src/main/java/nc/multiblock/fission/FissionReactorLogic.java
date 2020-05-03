package nc.multiblock.fission;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import nc.config.NCConfig;
import nc.init.NCSounds;
import nc.multiblock.Multiblock;
import nc.multiblock.MultiblockLogic;
import nc.multiblock.container.ContainerMultiblockController;
import nc.multiblock.container.ContainerSolidFissionController;
import nc.multiblock.fission.tile.IFissionComponent;
import nc.multiblock.fission.tile.IFissionController;
import nc.multiblock.fission.tile.IFissionFuelComponent;
import nc.multiblock.fission.tile.IFissionFuelComponent.ModeratorBlockInfo;
import nc.multiblock.fission.tile.IFissionPart;
import nc.multiblock.fission.tile.IFissionSpecialComponent;
import nc.multiblock.fission.tile.TileFissionIrradiator;
import nc.multiblock.fission.tile.TileFissionShield;
import nc.multiblock.fission.tile.TileFissionSource;
import nc.multiblock.fission.tile.TileFissionSource.PrimingTargetInfo;
import nc.multiblock.fission.tile.manager.TileFissionShieldManager;
import nc.multiblock.fission.tile.TileFissionVent;
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
		return NCConfig.fission_min_size;
	}
	
	@Override
	public int getMaximumInteriorLength() {
		return NCConfig.fission_max_size;
	}
	
	// Multiblock Methods
	
	@Override
	public void onMachineAssembled(boolean wasAssembled) {
		onReactorFormed();
	}
	
	@Override
	public void onMachineRestored() {
		onReactorFormed();
	}
	
	public void onReactorFormed() {
		for (IFissionController contr : getPartMap(IFissionController.class).values()) {
			 getReactor().controller = contr;
		}
		
		heatBuffer.setHeatCapacity(FissionReactor.BASE_MAX_HEAT*getCapacityMultiplier());
		getReactor().ambientTemp = 273 + (int) (getWorld().getBiome(getReactor().getMiddleCoord()).getTemperature(getReactor().getMiddleCoord())*20F);
		
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
	public void onMachinePaused() {}
	
	@Override
	public void onMachineDisassembled() {
		getReactor().resetStats();
		
		if (!getWorld().isRemote) {
			refreshConnections();
			//refreshReactor();
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
		if (!(assimilated instanceof FissionReactor)) return;
		FissionReactor assimilatedReactor = (FissionReactor) assimilated;
		heatBuffer.mergeHeatBuffers(assimilatedReactor.getLogic().heatBuffer);
		onReactorFormed();
	}
	
	public void onAssimilated(Multiblock assimilator) {}
	
	public void refreshConnections() {
		refreshFilteredPorts(TileFissionIrradiatorPort.class, TileFissionIrradiator.class);
	}
	
	public void refreshReactor() {
		refreshManagers();
		refreshFlux();
		refreshClusters();
		refreshReactorStats();
	}
	
	public void refreshManagers() {
		refreshManagers(TileFissionShieldManager.class);
	}
	
	public void refreshFlux() {
		final ObjectSet<IFissionFuelComponent> primedFailureCache = new ObjectOpenHashSet<>();
		do {
			getReactor().refreshFlag = false;
			if (!getReactor().isAssembled()) return;
			
			final ObjectSet<IFissionFuelComponent> primedCache = new ObjectOpenHashSet<>();
			for (IFissionComponent component : getPartMap(IFissionComponent.class).values()) {
				if (component instanceof IFissionFuelComponent) {
					IFissionFuelComponent fuelComponent = (IFissionFuelComponent) component;
					fuelComponent.refreshIsProcessing(false);
					if ((fuelComponent.isFunctional() || fuelComponent.isSelfPriming()) && !primedFailureCache.contains(fuelComponent)) {
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
			
			distributeFlux(primedCache, primedFailureCache);
		}
		while (getReactor().refreshFlag);
	}
	
	public void distributeFlux(final ObjectSet<IFissionFuelComponent> primedCache, final ObjectSet<IFissionFuelComponent> primedFailureCache) {}
	
	public void refreshClusters() {
		for (IFissionSpecialComponent specialComponent : getPartMap(IFissionSpecialComponent.class).values()) {
			specialComponent.postClusterSearch();
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
		
		cluster.heatBuffer.setHeatCapacity(FissionReactor.BASE_MAX_HEAT*cluster.getComponentMap().size());
	}
	
	public void iterateFluxSearch(IFissionFuelComponent rootFuelComponent) {
		final ObjectSet<IFissionFuelComponent> fluxSearchCache = new ObjectOpenHashSet<>();
		rootFuelComponent.fluxSearch(fluxSearchCache);
		
		do {
			final Iterator<IFissionFuelComponent> fluxSearchIterator = fluxSearchCache.iterator();
			final ObjectSet<IFissionFuelComponent> fluxSearchSubCache = new ObjectOpenHashSet<>();
			while (fluxSearchIterator.hasNext()) {
				IFissionFuelComponent fuelComponent = fluxSearchIterator.next();
				fluxSearchIterator.remove();
				fuelComponent.fluxSearch(fluxSearchSubCache);
			}
			fluxSearchCache.addAll(fluxSearchSubCache);
		}
		while (!fluxSearchCache.isEmpty());
	}
	
	public void iterateClusterSearch(IFissionComponent rootComponent) {
		final Object2IntMap<IFissionComponent> clusterSearchCache = new Object2IntOpenHashMap<>();
		rootComponent.clusterSearch(null, clusterSearchCache);
		
		do {
			final Iterator<IFissionComponent> clusterSearchIterator = clusterSearchCache.keySet().iterator();
			final Object2IntMap<IFissionComponent> clusterSearchSubCache = new Object2IntOpenHashMap<>();
			while (clusterSearchIterator.hasNext()) {
				IFissionComponent component = clusterSearchIterator.next();
				Integer id = clusterSearchCache.get(component);
				clusterSearchIterator.remove();
				component.clusterSearch(id, clusterSearchSubCache);
			}
			clusterSearchCache.putAll(clusterSearchSubCache);
		}
		while (!clusterSearchCache.isEmpty());
	}
	
	public void refreshReactorStats() {
		
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
		/*getReactor().comparatorSignal = NCMath.getComparatorSignal(100D/NCConfig.fission_comparator_max_heat*getReactor().heatBuffer.heatStored, getReactor().heatBuffer.heatCapacity, 0D);
		for (TileSaltFissionRedstonePort redstonePort : getReactor().partSuperMap.get(TileSaltFissionRedstonePort.class).values) {
			if (redstonePort.comparatorSignal != getReactor().comparatorSignal) {
				redstonePort.comparatorSignal = getReactor().comparatorSignal;
				getWorld().updateComparatorOutputLevel(redstonePort.getPos(), null);
			}
		}*/
	}
	
	public void casingMeltdown() {
		Iterator<IFissionController> controllerIterator = getPartMap(IFissionController.class).values().iterator();
		while (controllerIterator.hasNext()) {
			IFissionController controller = controllerIterator.next();
			controllerIterator.remove();
			controller.doMeltdown();
		}
		
		//TODO - graphite fires
		
		//TODO - explosions if vents with water are present, melt casing if not
		if (getPartMap(TileFissionVent.class).isEmpty()) {
			
		}
		else {
			
		}
		
		/*IBlockState corium = RegistryHelper.getBlock(Global.MOD_ID + ":fluid_corium").getDefaultState();
		for (BlockPos blockPos : BlockPos.getAllInBoxMutable(getMinimumCoord(), getMaximumCoord())) {
			if (rand.nextDouble() < 0.18D) {
				WORLD.removeTileEntity(blockPos);
				WORLD.setBlockState(blockPos, corium);
			}
		}*/
		
		getReactor().checkIfMachineIsWhole();
	}
	
	public void clusterMeltdown(FissionCluster cluster) {
		getReactor().checkIfMachineIsWhole();
	}
	
	//TODO - config
	public long getHeatDissipation() {
		return Math.max(1L, heatBuffer.getHeatStored()*getReactor().getExteriorSurfaceArea()/(NCMath.cube(6)*672000L));
	}
	
	public int getTemperature() {
		return Math.round(getReactor().ambientTemp + (FissionReactor.MAX_TEMP - getReactor().ambientTemp)*(float)heatBuffer.getHeatStored()/heatBuffer.getHeatCapacity());
	}
	
	public float getBurnDamage() {
		return getTemperature() < 373 ? 0F : 1F + (getTemperature() - 373)/200F;
	}
	
	// Component Logic
	
	public void onSourceUpdated(TileFissionSource source) {
		if (source.getIsRedstonePowered()) {
			PrimingTargetInfo targetInfo = source.getPrimingTarget();
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
		if (shield.inActiveModeratorLine) {
			getReactor().refreshFlag = true;
		}
	}
	
	public void distributeFluxFromFuelComponent(IFissionFuelComponent fuelComponent, final ObjectSet<IFissionFuelComponent> fluxSearchCache) {}
	
	public IFissionFuelComponent getNextFuelComponent(IFissionFuelComponent fuelComponent, BlockPos pos) {
		IFissionComponent component = getPartMap(IFissionComponent.class).get(pos.toLong());
		return component instanceof IFissionFuelComponent ? (IFissionFuelComponent) component : null;
	}
	
	public void refreshFuelComponentLocal(IFissionFuelComponent fuelComponent) {}
	
	public void refreshFuelComponentModerators(IFissionFuelComponent fuelComponent) {}
	
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
		if (getReactor().fuelComponentCount <= 0) return true;
		double soundRate = Math.min(1D, getReactor().meanEfficiency/(14D*NCConfig.fission_max_size));
		if (rand.nextDouble() < soundRate) {
			getWorld().playSound(fuelComponent.getTilePos().getX(), fuelComponent.getTilePos().getY(), fuelComponent.getTilePos().getZ(), NCSounds.geiger_tick, SoundCategory.BLOCKS, (float) (1.6D*Math.log1p(Math.sqrt(getReactor().fuelComponentCount))*NCConfig.fission_sound_volume), 1F + 0.12F*(rand.nextFloat() - 0.5F), false);
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
