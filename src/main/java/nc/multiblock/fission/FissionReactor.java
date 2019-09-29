package nc.multiblock.fission;

import static nc.block.property.BlockProperties.FACING_ALL;

import java.util.Iterator;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import nc.Global;
import nc.config.NCConfig;
import nc.init.NCSounds;
import nc.multiblock.IMultiblockPart;
import nc.multiblock.MultiblockBase;
import nc.multiblock.TileBeefBase.SyncReason;
import nc.multiblock.container.ContainerSaltFissionController;
import nc.multiblock.container.ContainerSolidFissionController;
import nc.multiblock.cuboidal.CuboidalMultiblockBase;
import nc.multiblock.fission.salt.FissionReactorType;
import nc.multiblock.fission.salt.tile.TileSaltFissionController;
import nc.multiblock.fission.salt.tile.TileSaltFissionHeater;
import nc.multiblock.fission.salt.tile.TileSaltFissionVessel;
import nc.multiblock.fission.solid.tile.TileSolidFissionCell;
import nc.multiblock.fission.solid.tile.TileSolidFissionController;
import nc.multiblock.fission.solid.tile.TileSolidFissionSink;
import nc.multiblock.fission.tile.IFissionComponent;
import nc.multiblock.fission.tile.IFissionController;
import nc.multiblock.fission.tile.IFissionFuelComponent;
import nc.multiblock.fission.tile.IFissionSpecialComponent;
import nc.multiblock.fission.tile.TileFissionConductor;
import nc.multiblock.fission.tile.TileFissionPort;
import nc.multiblock.fission.tile.TileFissionSource;
import nc.multiblock.fission.tile.TileFissionVent;
import nc.multiblock.network.FissionUpdatePacket;
import nc.multiblock.network.SaltFissionUpdatePacket;
import nc.multiblock.network.SolidFissionUpdatePacket;
import nc.multiblock.validation.IMultiblockValidator;
import nc.tile.internal.heat.HeatBuffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FissionReactor extends CuboidalMultiblockBase<FissionUpdatePacket> {
	
	public FissionReactorType type = FissionReactorType.SOLID_FUEL;
	
	protected final Int2ObjectMap<FissionCluster> clusterMap = new Int2ObjectOpenHashMap<>();
	public int clusterCount = 0;
	
	protected final Long2ObjectMap<IFissionComponent> componentMap = new Long2ObjectOpenHashMap<>();
	protected final Long2ObjectMap<IFissionSpecialComponent> specialComponentMap = new Long2ObjectOpenHashMap<>();
	
	protected final Long2ObjectMap<TileFissionConductor> conductorMap = new Long2ObjectOpenHashMap<>();
	protected final Long2ObjectMap<TileFissionPort> portMap = new Long2ObjectOpenHashMap<>();
	protected final Long2ObjectMap<TileFissionVent> ventMap = new Long2ObjectOpenHashMap<>();
	protected final Long2ObjectMap<TileFissionSource> sourceMap = new Long2ObjectOpenHashMap<>();
	
	protected final Long2ObjectMap<TileSolidFissionCell> cellMap = new Long2ObjectOpenHashMap<>();
	protected final Long2ObjectMap<TileSolidFissionSink> sinkMap = new Long2ObjectOpenHashMap<>();
	
	protected final Long2ObjectMap<TileSaltFissionVessel> vesselMap = new Long2ObjectOpenHashMap<>();
	protected final Long2ObjectMap<TileSaltFissionHeater> heaterMap = new Long2ObjectOpenHashMap<>();
	
	protected final Long2ObjectMap<IFissionController> controllerMap = new Long2ObjectOpenHashMap<>();
	protected IFissionController controller;
	
	public final LongSet passiveModeratorCache = new LongOpenHashSet();
	public final LongSet activeModeratorCache = new LongOpenHashSet();
	public final LongSet activeReflectorCache = new LongOpenHashSet();
	
	public static final int BASE_MAX_HEAT = 25000, MAX_TEMP = 2400;
	public final HeatBuffer heatBuffer = new HeatBuffer(BASE_MAX_HEAT);
	public int ambientTemp = 290;
	public int comparatorSignal = 0;
	
	public boolean refreshFlag = true;
	
	public boolean isReactorOn/*, computerActivated*/;
	public int fuelComponentCount = 0;
	public long cooling = 0L, heating = 0L, totalHeatMult = 0L, usefulPartCount = 0L;
	public double meanHeatMult = 0D, totalEfficiency = 0D, meanEfficiency = 0D, sparsityEfficiencyMult = 0D;
	
	public FissionReactor(World world) {
		super(world);
	}
	
	// Multiblock Part Getters
	
	public Int2ObjectMap<FissionCluster> getClusterMap() {
		return clusterMap;
	}
	
	public Long2ObjectMap<IFissionComponent> getComponentMap() {
		return componentMap;
	}
	
	public Long2ObjectMap<IFissionSpecialComponent> getSpecialComponentMap() {
		return specialComponentMap;
	}
	
	public Long2ObjectMap<TileFissionConductor> getConductorMap() {
		return conductorMap;
	}
	
	public Long2ObjectMap<TileFissionPort> getPortMap() {
		return portMap;
	}
	
	public Long2ObjectMap<TileFissionVent> getVentMap() {
		return ventMap;
	}
	
	public Long2ObjectMap<TileFissionSource> getSourceMap() {
		return sourceMap;
	}
	
	public Long2ObjectMap<TileSolidFissionCell> getCellMap() {
		return cellMap;
	}
	
	public Long2ObjectMap<TileSolidFissionSink> getSinkMap() {
		return sinkMap;
	}
	
	public Long2ObjectMap<TileSaltFissionVessel> getVesselMap() {
		return vesselMap;
	}
	
	public Long2ObjectMap<TileSaltFissionHeater> getHeaterMap() {
		return heaterMap;
	}
	
	// Multiblock Size Limits
	
	@Override
	protected int getMinimumInteriorLength() {
		return NCConfig.fission_min_size;
	}
	
	@Override
	protected int getMaximumInteriorLength() {
		return NCConfig.fission_max_size;
	}
	
	// Multiblock Methods
	
	@Override
	public void onAttachedPartWithMultiblockData(IMultiblockPart part, NBTTagCompound data) {
		syncDataFrom(data, SyncReason.FullSync);
	}
	
	@Override
	protected void onBlockAdded(IMultiblockPart newPart) {
		if (newPart instanceof IFissionComponent) {
			componentMap.put(newPart.getTilePos().toLong(), (IFissionComponent) newPart);
			if (newPart instanceof IFissionSpecialComponent) specialComponentMap.put(newPart.getTilePos().toLong(), (IFissionSpecialComponent) newPart);
		}
		
		if (newPart instanceof TileFissionConductor) conductorMap.put(newPart.getTilePos().toLong(), (TileFissionConductor) newPart);
		else if (newPart instanceof TileFissionPort) portMap.put(newPart.getTilePos().toLong(), (TileFissionPort) newPart);
		else if (newPart instanceof TileFissionVent) ventMap.put(newPart.getTilePos().toLong(), (TileFissionVent) newPart);
		else if (newPart instanceof TileFissionSource) sourceMap.put(newPart.getTilePos().toLong(), (TileFissionSource) newPart);
		
		else if (newPart instanceof TileSolidFissionCell) cellMap.put(newPart.getTilePos().toLong(), (TileSolidFissionCell) newPart);
		else if (newPart instanceof TileSolidFissionSink) sinkMap.put(newPart.getTilePos().toLong(), (TileSolidFissionSink) newPart);
		
		else if (newPart instanceof TileSaltFissionVessel) vesselMap.put(newPart.getTilePos().toLong(), (TileSaltFissionVessel) newPart);
		else if (newPart instanceof TileSaltFissionHeater) heaterMap.put(newPart.getTilePos().toLong(), (TileSaltFissionHeater) newPart);
		
		else if (newPart instanceof IFissionController) controllerMap.put(newPart.getTilePos().toLong(), (IFissionController) newPart);
	}
	
	@Override
	protected void onBlockRemoved(IMultiblockPart oldPart) {
		if (oldPart instanceof IFissionComponent) {
			componentMap.remove(oldPart.getTilePos().toLong());
			if (oldPart instanceof IFissionSpecialComponent) specialComponentMap.remove(oldPart.getTilePos().toLong());
		}
		
		if (oldPart instanceof TileFissionConductor) conductorMap.remove(oldPart.getTilePos().toLong());
		else if (oldPart instanceof TileFissionPort) portMap.remove(oldPart.getTilePos().toLong());
		else if (oldPart instanceof TileFissionVent) ventMap.remove(oldPart.getTilePos().toLong());
		else if (oldPart instanceof TileFissionSource) sourceMap.remove(oldPart.getTilePos().toLong());
		
		else if (oldPart instanceof TileSolidFissionCell) cellMap.remove(oldPart.getTilePos().toLong());
		else if (oldPart instanceof TileSolidFissionSink) sinkMap.remove(oldPart.getTilePos().toLong());
		
		else if (oldPart instanceof TileSaltFissionVessel) vesselMap.remove(oldPart.getTilePos().toLong());
		else if (oldPart instanceof TileSaltFissionHeater) heaterMap.remove(oldPart.getTilePos().toLong());
		
		else if (oldPart instanceof IFissionController) controllerMap.remove(oldPart.getTilePos().toLong());
	}
	
	@Override
	protected void onMachineAssembled() {
		for (IFissionController contr : controllerMap.values()) controller = contr;
		onReactorFormed();
	}
	
	@Override
	protected void onMachineRestored() {
		onReactorFormed();
	}
	
	protected void onReactorFormed() {
		heatBuffer.setHeatCapacity(BASE_MAX_HEAT*getNumConnectedBlocks());
		refreshReactor();
		ambientTemp = 273 + (int) (WORLD.getBiome(getMiddleCoord()).getTemperature(getMiddleCoord())*20F);
		updateActivity();
	}
	
	protected void refreshReactor() {
		if (!isAssembled()) return;
		
		final ObjectSet<IFissionFuelComponent> primedCache = new ObjectOpenHashSet<>();
		for (IFissionComponent component : componentMap.values()) {
			if (component instanceof IFissionFuelComponent) {
				IFissionFuelComponent fuelComponent = (IFissionFuelComponent) component;
				fuelComponent.refreshIsProcessing(false);
				if (fuelComponent.isFunctional()) {
					primedCache.add(fuelComponent);
				}
			}
			component.setCluster(null);
			component.resetStats();
		}
		clusterMap.clear();
		clusterCount = 0;
		passiveModeratorCache.clear();
		activeModeratorCache.clear();
		activeReflectorCache.clear();
		
		if (type == FissionReactorType.PEBBLE_BED) {
			
		}
		else {
			for (IFissionFuelComponent primedComponent : primedCache) {
				primedComponent.tryPriming(this);
				primedComponent.fluxSearch();
			}
			
			for (TileFissionSource source : sourceMap.values()) {
				EnumFacing facing = source.getPartPosition().getFacing();
				if (facing != null) {
					WORLD.setBlockState(source.getPos(), WORLD.getBlockState(source.getPos()).withProperty(FACING_ALL, facing), 3);
				}
				
				if (!source.getIsRedstonePowered()) continue;
				IFissionFuelComponent fuelComponent = source.getPrimingTarget();
				if (fuelComponent == null || fuelComponent.isFluxSearched()) continue;
				
				fuelComponent.tryPriming(this);
				if (fuelComponent.isPrimed()) {
					primedCache.add(fuelComponent);
					fuelComponent.fluxSearch();
				}
			}
			
			for (IFissionFuelComponent primedComponent : primedCache) {
				primedComponent.refreshIsProcessing(false);
				primedComponent.refreshPrimed();
			}
			
			if (type == FissionReactorType.SOLID_FUEL) {
				for (TileSolidFissionCell cell : cellMap.values()) {
					cell.clusterSearch(null);
				}
			}
			else {
				for (TileSaltFissionVessel vessel : vesselMap.values()) {
					vessel.clusterSearch(null);
				}
			}
			
			for (long posLong : activeModeratorCache) {
				for (EnumFacing dir : EnumFacing.VALUES) {
					IFissionComponent component = componentMap.get(BlockPos.fromLong(posLong).offset(dir).toLong());
					if (component != null) component.clusterSearch(null);
				}
			}
			
			for (long posLong : activeReflectorCache) {
				for (EnumFacing dir : EnumFacing.VALUES) {
					IFissionComponent component = componentMap.get(BlockPos.fromLong(posLong).offset(dir).toLong());
					if (component != null) component.clusterSearch(null);
				}
			}
		}
		
		for (IFissionSpecialComponent specialComponent : specialComponentMap.values()) {
			specialComponent.postClusterSearch();
		}
		
		for (FissionCluster cluster : clusterMap.values()) {
			cluster.refreshClusterStats();
		}
		sortClusters();
		
		refreshReactorStats();
	}
	
	public void onSourceUpdated(TileFissionSource source) {
		if (!source.getIsRedstonePowered()) return;
		IFissionFuelComponent fuelComponent = source.getPrimingTarget();
		if (!fuelComponent.isFunctional()) refreshReactor();
	}
	
	/** Only use when the cluster geometry isn't changed and there is no effect on other clusters! */
	public void refreshCluster(long id) {
		FissionCluster cluster = clusterMap.get(id);
		if (cluster != null) cluster.refreshClusterStats();
		refreshReactorStats();
	}
	
	protected void refreshReactorStats() {
		resetStats();
		for (FissionCluster cluster : clusterMap.values()) {
			if (cluster.connectedToWall) {
				usefulPartCount += cluster.componentCount;
				fuelComponentCount += cluster.fuelComponentCount;
				cooling += cluster.cooling;
				heating += cluster.heating;
				totalHeatMult += cluster.totalHeatMult;
				totalEfficiency += cluster.totalEfficiency;
			}
		}
		
		usefulPartCount += passiveModeratorCache.size() + activeModeratorCache.size() + activeReflectorCache.size();
		double usefulPartRatio = (double)usefulPartCount/(double)getInteriorVolume();
		sparsityEfficiencyMult = usefulPartRatio >= NCConfig.fission_sparsity_penalty_params[1] ? 1D : (1D - NCConfig.fission_sparsity_penalty_params[0])*Math.sin(usefulPartRatio*Math.PI/(2D*NCConfig.fission_sparsity_penalty_params[1])) + NCConfig.fission_sparsity_penalty_params[0];
		totalEfficiency *= sparsityEfficiencyMult;
		meanHeatMult = fuelComponentCount == 0 ? 0D : (double)totalHeatMult/(double)fuelComponentCount;
		meanEfficiency = fuelComponentCount == 0 ? 0D : totalEfficiency/fuelComponentCount;
	}
	
	protected void sortClusters() {
		final ObjectSet<FissionCluster> uniqueClusterCache = new ObjectOpenHashSet<>();
		for (FissionCluster cluster : clusterMap.values()) {
			uniqueClusterCache.add(cluster);
		}
		clusterMap.clear();
		int i = 0;
		for (FissionCluster cluster : uniqueClusterCache) {
			cluster.setId(i);
			clusterMap.put(i, cluster);
			i++;
		}
		clusterCount = clusterMap.size();
	}
	
	public void mergeClusters(long assimilatorId, FissionCluster targetCluster) {
		if (assimilatorId == targetCluster.getId()) return;
		FissionCluster assimilatorCluster = clusterMap.get(assimilatorId);
		for (IFissionComponent component : targetCluster.getComponentMap().values()) {
			component.setCluster(assimilatorCluster);
		}
		assimilatorCluster.heatBuffer.mergeHeatBuffers(targetCluster.heatBuffer);
		targetCluster.getComponentMap().clear();
		clusterMap.remove(targetCluster.getId());
	}
	
	@Override
	protected void onMachinePaused() {
		
	}
	
	@Override
	protected void onMachineDisassembled() {
		resetStats();
		if (controller != null) controller.updateBlockState(false);
	}
	
	protected void resetStats() {
		isReactorOn = false;
		fuelComponentCount = 0;
		cooling = heating = totalHeatMult = usefulPartCount = 0L;
		meanHeatMult = totalEfficiency = meanEfficiency = sparsityEfficiencyMult = 0D;
	}
	
	@Override
	protected boolean isMachineWhole(IMultiblockValidator validatorCallback) {
		
		// Only one controller
		
		if (controllerMap.size() == 0) {
			validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.no_controller", null);
			return false;
		}
		if (controllerMap.size() > 1) {
			validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.too_many_controllers", null);
			return false;
		}
		
		for (IFissionController contr : controllerMap.values()) {
			controller = contr;
		}
		
		if (controller instanceof TileSolidFissionController) {
			type = FissionReactorType.SOLID_FUEL;
		}
		else type = FissionReactorType.MOLTEN_SALT;
		
		
		if (type != FissionReactorType.PEBBLE_BED) {
			
		}
		else if (type != FissionReactorType.SOLID_FUEL) {
			if (cellMap.size() != 0) {
				validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.fission_reactor.prohibit_cells", null);
				return false;
			}
			if (sinkMap.size() != 0) {
				validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.fission_reactor.prohibit_sinks", null);
				return false;
			}
		}
		else if (type != FissionReactorType.MOLTEN_SALT) {
			if (vesselMap.size() != 0) {
				validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.fission_reactor.prohibit_vessels", null);
				return false;
			}
			if (heaterMap.size() != 0) {
				validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.fission_reactor.prohibit_heaters", null);
				return false;
			}
		}
		
		return super.isMachineWhole(validatorCallback);
	}
	
	@Override
	protected void onAssimilate(MultiblockBase assimilated) {
		if (!(assimilated instanceof FissionReactor)) return;
		FissionReactor assimilatedReactor = (FissionReactor) assimilated;
		heatBuffer.mergeHeatBuffers(assimilatedReactor.heatBuffer);
	}
	
	@Override
	protected void onAssimilated(MultiblockBase assimilator) {
		
	}
	
	// Server
	
	@Override
	protected boolean updateServer() {
		if (refreshFlag) {
			refreshReactor();
			refreshFlag = false;
		}
		updateActivity();
		heatBuffer.changeHeatStored(getNetHeating());
		if (heatBuffer.isFull() && NCConfig.fission_overheat) {
			heatBuffer.setHeatStored(0);
			doMeltdown();
			return true;
		}
		
		for (FissionCluster cluster : clusterMap.values()) {
			cluster.heatBuffer.changeHeatStored(cluster.getNetHeating());
			if (cluster.heatBuffer.isFull() && NCConfig.fission_overheat) {
				cluster.heatBuffer.setHeatStored(0);
				cluster.doMeltdown();
				return true;
			}
		}
		
		//updateRedstonePorts();
		sendUpdateToListeningPlayers();
		
		return true;
	}
	
	public void updateActivity() {
		boolean wasReactorOn = isReactorOn;
		isReactorOn = (heating > 0L /*|| computerActivated*/) && isAssembled();
		if (isReactorOn != wasReactorOn) {
			if (controller != null) controller.updateBlockState(isReactorOn);
			sendUpdateToAllPlayers();
		}
	}
	
	/*protected void updateRedstonePorts() {
		comparatorSignal = (int) MathHelper.clamp(1500D/NCConfig.fission_comparator_max_heat*heatBuffer.heatStored/heatBuffer.heatCapacity, 0D, 15D);
		for (TileSaltFissionRedstonePort redstonePort : redstonePorts) {
			if (redstonePort.comparatorSignal != comparatorSignal) {
				redstonePort.comparatorSignal = comparatorSignal;
				WORLD.updateComparatorOutputLevel(redstonePort.getPos(), null);
			}
		}
	}*/
	
	public long getNetHeating() {
		return heating - cooling;
	}
	
	public int getTemperature() {
		return Math.round(ambientTemp + (MAX_TEMP - ambientTemp)*(float)heatBuffer.getHeatStored()/heatBuffer.getHeatCapacity());
	}
	
	protected void doMeltdown() {
		//TODO
		
		Iterator<IFissionController> controllerIterator = controllerMap.values().iterator();
		while (controllerIterator.hasNext()) {
			IFissionController controller = controllerIterator.next();
			controllerIterator.remove();
			controller.doMeltdown();
		}
		
		/*IBlockState corium = RegistryHelper.getBlock(Global.MOD_ID + ":fluid_corium").getDefaultState();
		for (BlockPos blockPos : BlockPos.getAllInBoxMutable(getMinimumCoord(), getMaximumCoord())) {
			if (rand.nextDouble() < 0.18D) {
				WORLD.removeTileEntity(blockPos);
				WORLD.setBlockState(blockPos, corium);
			}
		}*/
		
		checkIfMachineIsWhole();
	}
	
	// Client
	
	@Override
	protected void updateClient() {
		if (isReactorOn) {
			if (type == FissionReactorType.PEBBLE_BED) {
				
			}
			else if (type == FissionReactorType.SOLID_FUEL) {
				for (TileSolidFissionCell cell : cellMap.values()) playFissionSound(cell.getPos());
			}
			else {
				for (TileSaltFissionVessel vessel : vesselMap.values()) playFissionSound(vessel.getPos());
			}
		}
	}
	
	protected void playFissionSound(BlockPos pos) {
		if (fuelComponentCount <= 0) return;
		double soundRate = Math.min(meanEfficiency/(14D*NCConfig.fission_max_size*Math.sqrt(fuelComponentCount)), 1D/fuelComponentCount);
		if (rand.nextDouble() < soundRate) {
			WORLD.playSound(pos.getX(), pos.getY(), pos.getZ(), NCSounds.geiger_tick, SoundCategory.BLOCKS, 1.6F, 1F + 0.12F*(rand.nextFloat() - 0.5F), false);
		}
	}
	
	// NBT
	
	@Override
	protected void syncDataTo(NBTTagCompound data, SyncReason syncReason) {
		heatBuffer.writeToNBT(data);
		data.setInteger("comparatorSignal", comparatorSignal);
		data.setBoolean("isReactorOn", isReactorOn);
		//data.setBoolean("computerActivated", computerActivated);
		data.setInteger("clusterCount", clusterCount);
		data.setLong("cooling", cooling);
		data.setLong("heating", heating);
		data.setLong("totalHeatMult", totalHeatMult);
		data.setDouble("meanHeatMult", meanHeatMult);
		data.setInteger("fuelComponentCount", fuelComponentCount);
		data.setLong("usefulPartCount", usefulPartCount);
		data.setDouble("totalEfficiency", totalEfficiency);
		data.setDouble("meanEfficiency", meanEfficiency);
		data.setDouble("sparsityEfficiencyMult", sparsityEfficiencyMult);
	}
	
	@Override
	protected void syncDataFrom(NBTTagCompound data, SyncReason syncReason) {
		heatBuffer.readFromNBT(data);
		comparatorSignal = data.getInteger("comparatorSignal");
		isReactorOn = data.getBoolean("isReactorOn");
		//computerActivated = data.getBoolean("computerActivated");
		clusterCount = data.getInteger("clusterCount");
		cooling = data.getLong("cooling");
		heating = data.getLong("heating");
		totalHeatMult = data.getLong("totalHeatMult");
		meanHeatMult = data.getDouble("meanHeatMult");
		fuelComponentCount = data.getInteger("fuelComponentCount");
		usefulPartCount = data.getLong("usefulPartCount");
		totalEfficiency = data.getDouble("totalEfficiency");
		meanEfficiency = data.getDouble("meanEfficiency");
		sparsityEfficiencyMult = data.getDouble("sparsityEfficiencyMult");
	}
	
	// Packets
	
	@Override
	protected FissionUpdatePacket getUpdatePacket() {
		/*if (type == FissionReactorType.PEBBLE_BED) {
			
		}
		else*/ if (type == FissionReactorType.SOLID_FUEL) {
			return new SolidFissionUpdatePacket(controller.getTilePos(), isReactorOn, clusterCount, cooling, heating, totalHeatMult, meanHeatMult, fuelComponentCount, usefulPartCount, totalEfficiency, meanEfficiency, sparsityEfficiencyMult, heatBuffer.getHeatCapacity(), heatBuffer.getHeatStored());
		}
		else {
			return new SaltFissionUpdatePacket(controller.getTilePos(), isReactorOn, clusterCount, cooling, heating, totalHeatMult, meanHeatMult, fuelComponentCount, usefulPartCount, totalEfficiency, meanEfficiency, sparsityEfficiencyMult, heatBuffer.getHeatCapacity(), heatBuffer.getHeatStored());
		}
	}
	
	@Override
	public void onPacket(FissionUpdatePacket message) {
		isReactorOn = message.isReactorOn;
		clusterCount = message.clusterCount;
		cooling = message.cooling;
		heating = message.heating;
		totalHeatMult = message.totalHeatMult;
		meanHeatMult = message.meanHeatMult;
		fuelComponentCount = message.fuelComponentCount;
		usefulPartCount = message.usefulPartCount;
		totalEfficiency = message.totalEfficiency;
		meanEfficiency = message.meanEfficiency;
		sparsityEfficiencyMult = message.sparsityEfficiencyMult;
		heatBuffer.setHeatCapacity(message.capacity);
		heatBuffer.setHeatStored(message.heat);
	}
	
	public Container getContainer(EntityPlayer player) {
		if (controller instanceof TileSolidFissionController) {
			return new ContainerSolidFissionController(player, (TileSolidFissionController) controller);
		}
		else {
			return new ContainerSaltFissionController(player, (TileSaltFissionController) controller);
		}
	}
	
	@Override
	public void clearAll() {
		for (TileFissionPort port : portMap.values()) {
			port.clearAllSlots();
			port.clearAllTanks();
		}
		for (TileFissionVent vent : ventMap.values()) vent.clearAllTanks();
		
		for (TileSolidFissionCell cell : cellMap.values()) cell.clearAllSlots();
		
		for (TileSaltFissionVessel vessel : vesselMap.values()) vessel.clearAllTanks();
		for (TileSaltFissionHeater heater : heaterMap.values()) heater.clearAllTanks();
	}
	
	// Multiblock Validators
	
	@Override
	protected boolean isBlockGoodForInterior(World world, int x, int y, int z, IMultiblockValidator validatorCallback) {
		return true;
	}
}
