package nc.multiblock.fission;

import java.util.Iterator;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import nc.config.NCConfig;
import nc.init.NCSounds;
import nc.multiblock.Multiblock;
import nc.multiblock.MultiblockLogic;
import nc.multiblock.TileBeefBase.SyncReason;
import nc.multiblock.container.ContainerMultiblockController;
import nc.multiblock.container.ContainerSolidFissionController;
import nc.multiblock.fission.solid.tile.TileSolidFissionCell;
import nc.multiblock.fission.tile.IFissionComponent;
import nc.multiblock.fission.tile.IFissionController;
import nc.multiblock.fission.tile.IFissionFuelComponent;
import nc.multiblock.fission.tile.IFissionPart;
import nc.multiblock.fission.tile.IFissionSpecialComponent;
import nc.multiblock.fission.tile.TileFissionPort;
import nc.multiblock.fission.tile.TileFissionSource;
import nc.multiblock.fission.tile.TileFissionSource.PrimingTargetInfo;
import nc.multiblock.fission.tile.TileFissionVent;
import nc.multiblock.network.FissionUpdatePacket;
import nc.recipe.NCRecipes;
import nc.util.NCMath;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;

public class FissionReactorLogic extends MultiblockLogic<FissionReactor, IFissionPart, FissionUpdatePacket> {
	
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
	public void onMachineAssembled() {
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
		
		getReactor().heatBuffer.setHeatCapacity(FissionReactor.BASE_MAX_HEAT*getCapacityMultiplier());
		getReactor().ambientTemp = 273 + (int) (getWorld().getBiome(getReactor().getMiddleCoord()).getTemperature(getReactor().getMiddleCoord())*20F);
		
		if (!getWorld().isRemote) {
			linkPorts();
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
		if (getReactor().controller != null) {
			getReactor().controller.updateBlockState(false);
		}
		getReactor().isReactorOn = false;
	}
	
	@Override
	public boolean isMachineWhole(Multiblock multiblock) {
		multiblock.setLastError("zerocore.api.nc.multiblock.validation.invalid_logic", null);
		return false;
	}
	
	public void onAssimilate(Multiblock assimilated) {
		if (!(assimilated instanceof FissionReactor)) return;
		FissionReactor assimilatedReactor = (FissionReactor) assimilated;
		getReactor().heatBuffer.mergeHeatBuffers(assimilatedReactor.heatBuffer);
	}
	
	public void onAssimilated(Multiblock assimilator) {}
	
	//TODO - temporary ports
	public void linkPorts() {
		Long2ObjectMap<TileFissionPort> portMap = getPartMap(TileFissionPort.class);
		Long2ObjectMap<TileSolidFissionCell> cellMap = getPartMap(TileSolidFissionCell.class);
		
		if (portMap.isEmpty()) {
			for (TileSolidFissionCell cell : cellMap.values()) {
				cell.clearMasterPort();
			}
			return;
		}
		
		TileFissionPort masterPort = null;
		for (TileFissionPort port : portMap.values()) {
			masterPort = port.getMasterPort();
			break;
		}
		
		for (TileFissionPort port : portMap.values()) {
			if (port != masterPort) {
				port.clearMasterPort();
			}
		}
		
		if (masterPort == null) {
			for (TileFissionPort port : portMap.values()) {
				masterPort = port;
				break;
			}
		}
		
		if (masterPort == null) {
			return;
		}
		
		for (TileFissionPort port : portMap.values()) {
			if (port != masterPort) {
				port.shiftStacks(masterPort);
				port.setMasterPortPos(masterPort.getPos());
				port.refreshMasterPort();
			}
			
			port.inventoryStackLimit = Math.max(64, 2*cellMap.size());
			port.recipe_handler = NCRecipes.solid_fission;
		}
		
		for (TileSolidFissionCell cell : cellMap.values()) {
			cell.setMasterPortPos(masterPort.getPos());
			cell.refreshPort();
		}
	}
	
	public void refreshReactor() {
		refreshFlux();
		refreshClusters();
		refreshReactorStats();
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
			cluster.refreshClusterStats();
			cluster.recoverHeatFromComponents();
		}
		getReactor().sortClusters();
	}
	
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
	
	public boolean onUpdateServer() {
		if (!getReactor().isReactorOn) {
			getReactor().heatBuffer.changeHeatStored(-getHeatDissipation());
		}
		return false;
	}
	
	public boolean isReactorOn() {
		return getReactor().rawHeating > 0L;
	}
	
	public void updateRedstone() {
		/*getReactor().comparatorSignal = (int) MathHelper.clamp(1500D/NCConfig.fission_comparator_max_heat*getReactor().heatBuffer.heatStored/getReactor().heatBuffer.heatCapacity, 0D, 15D);
		for (TileSaltFissionRedstonePort redstonePort : getReactor().partSuperMap.get(TileSaltFissionRedstonePort.class).values) {
			if (redstonePort.comparatorSignal != getReactor().comparatorSignal) {
				redstonePort.comparatorSignal = getReactor().comparatorSignal;
				getWorld().updateComparatorOutputLevel(redstonePort.getPos(), null);
			}
		}*/
	}
	
	public void doMeltdown() {
		Iterator<IFissionController> controllerIterator = getPartMap(IFissionController.class).values().iterator();
		while (controllerIterator.hasNext()) {
			IFissionController controller = controllerIterator.next();
			controllerIterator.remove();
			controller.doMeltdown();
		}
		
		//TODO - graphite fires
		
		//TODO - explosions if vents are present, melt casing if not
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
	
	//TODO - config
	public long getHeatDissipation() {
		return Math.max(1L, getReactor().heatBuffer.getHeatStored()*getReactor().getExteriorSurfaceArea()/(NCMath.cube(6)*672000L));
	}
	
	// Client
	
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
		
	}
	
	@Override
	public void readFromLogicTag(NBTTagCompound logicTag, SyncReason syncReason) {
		
	}
	
	// Packets
	
	@Override
	public FissionUpdatePacket getUpdatePacket() {
		return null;
	}
	
	@Override
	public void onPacket(FissionUpdatePacket message) {
		
	}
	
	public ContainerMultiblockController<FissionReactor, IFissionController> getContainer(EntityPlayer player) {
		return new ContainerSolidFissionController(player, getReactor().controller);
	}
	
	public void clearAllMaterial() {}
}
