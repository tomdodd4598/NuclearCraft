package nc.multiblock.fission;

import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.objects.*;
import nc.Global;
import nc.multiblock.*;
import nc.multiblock.cuboidal.CuboidalMultiblock;
import nc.network.multiblock.FissionUpdatePacket;
import nc.tile.fission.*;
import nc.tile.multiblock.TilePartAbstract.SyncReason;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.UnaryOperator;

public class FissionReactor extends CuboidalMultiblock<FissionReactor, IFissionPart> implements ILogicMultiblock<FissionReactor, FissionReactorLogic, IFissionPart>, IPacketMultiblock<FissionReactor, IFissionPart, FissionUpdatePacket> {
	
	public static final ObjectSet<Class<? extends IFissionPart>> PART_CLASSES = new ObjectOpenHashSet<>();
	public static final Object2ObjectMap<String, UnaryOperator<FissionReactorLogic>> LOGIC_MAP = new Object2ObjectOpenHashMap<>();
	
	protected @Nonnull FissionReactorLogic logic = new FissionReactorLogic(this);
	
	protected final PartSuperMap<FissionReactor, IFissionPart> partSuperMap = new PartSuperMap<>();
	protected final Int2ObjectMap<FissionCluster> clusterMap = new Int2ObjectOpenHashMap<>();
	public int clusterCount = 0;
	
	protected final ObjectSet<FissionCluster> clustersToRefresh = new ObjectOpenHashSet<>();
	
	public IFissionController<?> controller;
	
	public final LongSet passiveModeratorCache = new LongOpenHashSet();
	public final LongSet activeModeratorCache = new LongOpenHashSet();
	public final LongSet activeReflectorCache = new LongOpenHashSet();
	
	public static final long BASE_MAX_HEAT = 32000;
	public static final int BASE_TANK_CAPACITY = 4000;
	public static final double MAX_TEMP = 2400D;
	
	public boolean refreshFlag = true, isSimulation = false, isReactorOn = false;
	public double ambientTemp = 290D;
	public int fuelComponentCount = 0;
	public long cooling = 0L, rawHeating = 0L, totalHeatMult = 0L, usefulPartCount = 0L;
	public double meanHeatMult = 0D, totalEfficiency = 0D, meanEfficiency = 0D, sparsityEfficiencyMult = 0D;
	
	protected final Set<EntityPlayer> updatePacketListeners = new ObjectOpenHashSet<>();
	
	public FissionReactor(World world) {
		super(world, FissionReactor.class, IFissionPart.class);
		for (Class<? extends IFissionPart> clazz : PART_CLASSES) {
			partSuperMap.equip(clazz);
		}
	}
	
	@Override
	public @Nonnull FissionReactorLogic getLogic() {
		return logic;
	}
	
	@Override
	public void setLogic(String logicID) {
		if (logicID.equals(logic.getID())) {
			return;
		}
		logic = getNewLogic(LOGIC_MAP.get(logicID));
	}
	
	@Override
	public PartSuperMap<FissionReactor, IFissionPart> getPartSuperMap() {
		return partSuperMap;
	}
	
	public Int2ObjectMap<FissionCluster> getClusterMap() {
		return clusterMap;
	}
	
	public void resetStats() {
		logic.onResetStats();
		fuelComponentCount = 0;
		cooling = rawHeating = totalHeatMult = usefulPartCount = 0L;
		meanHeatMult = totalEfficiency = meanEfficiency = sparsityEfficiencyMult = 0D;
	}
	
	// Multiblock Size Limits
	
	@Override
	protected int getMinimumInteriorLength() {
		return logic.getMinimumInteriorLength();
	}
	
	@Override
	protected int getMaximumInteriorLength() {
		return logic.getMaximumInteriorLength();
	}
	
	// Multiblock Methods
	
	@Override
	public void onAttachedPartWithMultiblockData(IFissionPart part, NBTTagCompound data) {
		logic.onAttachedPartWithMultiblockData(part, data);
		syncDataFrom(data, SyncReason.FullSync);
	}
	
	@Override
	protected void onBlockAdded(IFissionPart newPart) {
		onPartAdded(newPart);
		logic.onBlockAdded(newPart);
	}
	
	@Override
	protected void onBlockRemoved(IFissionPart oldPart) {
		onPartRemoved(oldPart);
		logic.onBlockRemoved(oldPart);
	}
	
	@Override
	protected void onMachineAssembled() {
		logic.onMachineAssembled();
	}
	
	@Override
	protected void onMachineRestored() {
		logic.onMachineRestored();
	}
	
	@Override
	protected void onMachinePaused() {
		logic.onMachinePaused();
	}
	
	@Override
	protected void onMachineDisassembled() {
		logic.onMachineDisassembled();
	}
	
	@Override
	protected boolean isMachineWhole() {
		return setLogic(this) && super.isMachineWhole() && logic.isMachineWhole();
	}
	
	public boolean setLogic(FissionReactor multiblock) {
		@SuppressWarnings("rawtypes") Long2ObjectMap<IFissionController> controllerMap = getPartMap(IFissionController.class);
		
		if (controllerMap.isEmpty()) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.no_controller", Collections.emptyList());
			return false;
		}
		if (controllerMap.size() > 1) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.too_many_controllers", controllerMap.keySet());
			return false;
		}
		
		for (IFissionController<?> contr : controllerMap.values()) {
			controller = contr;
			break;
		}
		
		setLogic(controller.getLogicID());
		
		return true;
	}
	
	@Override
	protected void onAssimilate(FissionReactor assimilated) {
		logic.onAssimilate(assimilated);
	}
	
	@Override
	protected void onAssimilated(FissionReactor assimilator) {
		logic.onAssimilated(assimilator);
	}
	
	// Cluster Management
	
	/**
	 * Only use when the cluster geometry isn't changed and there is no effect on other clusters!
	 */
	public void addClusterToRefresh(FissionCluster cluster) {
		if (cluster != null) {
			clustersToRefresh.add(cluster);
		}
	}
	
	protected void refreshCluster(FissionCluster cluster, boolean simulate) {
		if (cluster != null && clusterMap.containsKey(cluster.getId())) {
			logic.refreshClusterStats(cluster, simulate);
		}
	}
	
	protected void sortClusters(boolean simulate) {
		final ObjectSet<FissionCluster> uniqueClusterCache = new ObjectOpenHashSet<>();
		uniqueClusterCache.addAll(clusterMap.values());
		clusterMap.clear();
		int i = 0;
		for (FissionCluster cluster : uniqueClusterCache) {
			cluster.setId(i);
			clusterMap.put(i, cluster);
			++i;
		}
		clusterCount = clusterMap.size();
	}
	
	public void mergeClusters(int assimilatorId, FissionCluster targetCluster) {
		if (assimilatorId == targetCluster.getId()) {
			return;
		}
		FissionCluster assimilatorCluster = clusterMap.get(assimilatorId);
		
		if (targetCluster.connectedToWall) {
			assimilatorCluster.connectedToWall = true;
		}
		
		for (IFissionComponent component : targetCluster.getComponentMap().values()) {
			component.setCluster(assimilatorCluster);
		}
		
		assimilatorCluster.heatBuffer.mergeHeatBuffers(targetCluster.heatBuffer);
		targetCluster.getComponentMap().clear();
		clusterMap.remove(targetCluster.getId());
	}
	
	// Server
	
	@Override
	protected boolean updateServer() {
		boolean flag = refreshFlag;
		
		checkRefresh();
		
		if (logic.onUpdateServer()) {
			flag = true;
		}
		
		if (controller != null) {
			sendMultiblockUpdatePacketToListeners();
		}
		
		return flag;
	}
	
	public void checkRefresh() {
		boolean refreshSimulation = false;
		
		if (refreshFlag) {
			refreshSimulation = true;
			logic.refreshReactor(false);
			clustersToRefresh.clear();
		}
		else if (!clustersToRefresh.isEmpty()) {
			refreshSimulation = true;
			for (FissionCluster cluster : clustersToRefresh) {
				refreshCluster(cluster, false);
			}
			logic.refreshReactorStats(false);
			clustersToRefresh.clear();
		}
		
		if (refreshSimulation) {
			if (isAssembled() && !logic.isReactorActive(false)) {
				logic.refreshReactor(true);
				isSimulation = true;
				clustersToRefresh.clear();
			}
			else {
				isSimulation = false;
			}
		}
		
		updateActivity();
	}
	
	public void updateActivity() {
		boolean wasReactorOn = isReactorOn;
		isReactorOn = isAssembled() && logic.isReactorActive(true);
		if (isReactorOn != wasReactorOn) {
			if (controller != null) {
				controller.setActivity(isReactorOn);
				sendMultiblockUpdatePacketToAll();
			}
			for (TileFissionMonitor monitor : getParts(TileFissionMonitor.class)) {
				monitor.setActivity(isReactorOn);
			}
		}
	}
	
	// Client
	
	@Override
	protected void updateClient() {
		logic.onUpdateClient();
	}
	
	// NBT
	
	@Override
	public void syncDataTo(NBTTagCompound data, SyncReason syncReason) {
		data.setBoolean("isSimulation", isSimulation);
		data.setBoolean("isReactorOn", isReactorOn);
		data.setInteger("clusterCount", clusterCount);
		data.setLong("cooling", cooling);
		data.setLong("rawHeating", rawHeating);
		data.setLong("totalHeatMult", totalHeatMult);
		data.setDouble("meanHeatMult", meanHeatMult);
		data.setInteger("fuelComponentCount", fuelComponentCount);
		data.setLong("usefulPartCount", usefulPartCount);
		data.setDouble("totalEfficiency", totalEfficiency);
		data.setDouble("meanEfficiency", meanEfficiency);
		data.setDouble("sparsityEfficiencyMult", sparsityEfficiencyMult);
		
		writeLogicNBT(data, syncReason);
	}
	
	@Override
	public void syncDataFrom(NBTTagCompound data, SyncReason syncReason) {
		isSimulation = data.getBoolean("isSimulation");
		isReactorOn = data.getBoolean("isReactorOn");
		clusterCount = data.getInteger("clusterCount");
		cooling = data.getLong("cooling");
		rawHeating = data.getLong("rawHeating");
		totalHeatMult = data.getLong("totalHeatMult");
		meanHeatMult = data.getDouble("meanHeatMult");
		fuelComponentCount = data.getInteger("fuelComponentCount");
		usefulPartCount = data.getLong("usefulPartCount");
		totalEfficiency = data.getDouble("totalEfficiency");
		meanEfficiency = data.getDouble("meanEfficiency");
		sparsityEfficiencyMult = data.getDouble("sparsityEfficiencyMult");
		
		readLogicNBT(data, syncReason);
	}
	
	// Packets
	
	@Override
	public Set<EntityPlayer> getMultiblockUpdatePacketListeners() {
		return updatePacketListeners;
	}
	
	@Override
	public FissionUpdatePacket getMultiblockUpdatePacket() {
		return logic.getMultiblockUpdatePacket();
	}
	
	@Override
	public void onMultiblockUpdatePacket(FissionUpdatePacket message) {
		isReactorOn = message.isReactorOn;
		clusterCount = message.clusterCount;
		cooling = message.cooling;
		rawHeating = message.rawHeating;
		totalHeatMult = message.totalHeatMult;
		meanHeatMult = message.meanHeatMult;
		fuelComponentCount = message.fuelComponentCount;
		usefulPartCount = message.usefulPartCount;
		totalEfficiency = message.totalEfficiency;
		meanEfficiency = message.meanEfficiency;
		sparsityEfficiencyMult = message.sparsityEfficiencyMult;
		
		logic.onMultiblockUpdatePacket(message);
	}
	
	// Multiblock Validators
	
	@Override
	protected boolean isBlockGoodForInterior(World world, BlockPos pos) {
		return logic.isBlockGoodForInterior(world, pos);
	}
	
	// Clear Material
	
	@Override
	public void clearAllMaterial() {
		logic.clearAllMaterial();
		super.clearAllMaterial();
		
		if (!WORLD.isRemote) {
			refreshFlag = true;
			checkRefresh();
		}
	}
}
