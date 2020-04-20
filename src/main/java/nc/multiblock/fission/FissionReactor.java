package nc.multiblock.fission;

import java.lang.reflect.Constructor;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import nc.Global;
import nc.multiblock.ILogicMultiblock;
import nc.multiblock.Multiblock;
import nc.multiblock.container.ContainerMultiblockController;
import nc.multiblock.cuboidal.CuboidalMultiblock;
import nc.multiblock.fission.tile.IFissionComponent;
import nc.multiblock.fission.tile.IFissionController;
import nc.multiblock.fission.tile.IFissionPart;
import nc.multiblock.fission.tile.TileFissionMonitor;
import nc.multiblock.network.FissionUpdatePacket;
import nc.multiblock.tile.ITileMultiblockPart;
import nc.multiblock.tile.TileBeefAbstract.SyncReason;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class FissionReactor extends CuboidalMultiblock<IFissionPart, FissionUpdatePacket> implements ILogicMultiblock<FissionReactorLogic, IFissionPart> {
	
	public static final ObjectSet<Class<? extends IFissionPart>> PART_CLASSES = new ObjectOpenHashSet<>();
	public static final Object2ObjectMap<String, Constructor<? extends FissionReactorLogic>> LOGIC_MAP = new Object2ObjectOpenHashMap<>();
	
	protected @Nonnull FissionReactorLogic logic = new FissionReactorLogic(this);
	
	protected final PartSuperMap<IFissionPart> partSuperMap = new PartSuperMap<>();
	protected final Int2ObjectMap<FissionCluster> clusterMap = new Int2ObjectOpenHashMap<>();
	public int clusterCount = 0;
	
	public IFissionController controller;
	
	public final LongSet passiveModeratorCache = new LongOpenHashSet();
	public final LongSet activeModeratorCache = new LongOpenHashSet();
	public final LongSet activeReflectorCache = new LongOpenHashSet();
	
	public static final int BASE_MAX_HEAT = 25000, MAX_TEMP = 2400, BASE_TANK_CAPACITY = 4000;
	
	public boolean refreshFlag = true, isReactorOn = false;
	public int ambientTemp = 290, fuelComponentCount = 0;
	public long cooling = 0L, rawHeating = 0L, totalHeatMult = 0L, usefulPartCount = 0L;
	public double meanHeatMult = 0D, totalEfficiency = 0D, meanEfficiency = 0D, sparsityEfficiencyMult = 0D;
	
	public FissionReactor(World world) {
		super(world);
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
		if (logicID.equals(logic.getID())) return;
		logic = getNewLogic(LOGIC_MAP.get(logicID));
	}
	
	@Override
	public PartSuperMap<IFissionPart> getPartSuperMap() {
		return partSuperMap;
	}
	
	public Int2ObjectMap<FissionCluster> getClusterMap() {
		return clusterMap;
	}
	
	public void resetStats() {
		logic.onResetStats();
		//isReactorOn = false;
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
	public void onAttachedPartWithMultiblockData(ITileMultiblockPart part, NBTTagCompound data) {
		logic.onAttachedPartWithMultiblockData(part, data);
		syncDataFrom(data, SyncReason.FullSync);
	}
	
	@Override
	protected void onBlockAdded(ITileMultiblockPart newPart) {
		onPartAdded(newPart);
		logic.onBlockAdded(newPart);
	}
	
	@Override
	protected void onBlockRemoved(ITileMultiblockPart oldPart) {
		onPartRemoved(oldPart);
		logic.onBlockRemoved(oldPart);
	}
	
	@Override
	protected void onMachineAssembled(boolean wasAssembled) {
		logic.onMachineAssembled(wasAssembled);
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
	protected boolean isMachineWhole(Multiblock multiblock) {
		return setLogic(multiblock) && super.isMachineWhole(multiblock) && logic.isMachineWhole(multiblock);
	}
	
	public boolean setLogic(Multiblock multiblock) {
		if (getPartMap(IFissionController.class).isEmpty()) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.no_controller", null);
			return false;
		}
		if (getPartMap(IFissionController.class).size() > 1) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.too_many_controllers", null);
			return false;
		}
		
		for (IFissionController contr : getPartMap(IFissionController.class).values()) {
			controller = contr;
		}
		
		setLogic(controller.getLogicID());
		
		return true;
	}
	
	@Override
	protected void onAssimilate(Multiblock assimilated) {
		logic.onAssimilate(assimilated);
	}
	
	@Override
	protected void onAssimilated(Multiblock assimilator) {
		logic.onAssimilated(assimilator);
	}
	
	// Cluster Management
	
	/** Only use when the cluster geometry isn't changed and there is no effect on other clusters! */
	public void refreshCluster(FissionCluster cluster) {
		if (cluster != null && clusterMap.containsKey(cluster.getId())) {
			logic.refreshClusterStats(cluster);
		}
		logic.refreshReactorStats();
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
	
	public void mergeClusters(int assimilatorId, FissionCluster targetCluster) {
		if (assimilatorId == targetCluster.getId()) return;
		FissionCluster assimilatorCluster = clusterMap.get(assimilatorId);
		
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
		
		if (refreshFlag) {
			logic.refreshReactor();
		}
		updateActivity();
		
		if (logic.onUpdateServer()) {
			flag = true;
		}
		
		logic.updateRedstone();
		if (controller != null) {
			sendUpdateToListeningPlayers();
		}
		
		return flag;
	}
	
	public void updateActivity() {
		boolean wasReactorOn = isReactorOn;
		isReactorOn = isAssembled() && logic.isReactorOn();
		if (isReactorOn != wasReactorOn) {
			if (controller != null) {
				controller.updateBlockState(isReactorOn);
				sendUpdateToAllPlayers();
			}
			for (TileFissionMonitor monitor : getPartMap(TileFissionMonitor.class).values()) {
				monitor.updateBlockState(isReactorOn);
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
	protected FissionUpdatePacket getUpdatePacket() {
		return logic.getUpdatePacket();
	}
	
	@Override
	public void onPacket(FissionUpdatePacket message) {
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
		
		logic.onPacket(message);
	}
	
	public ContainerMultiblockController<FissionReactor, IFissionController> getContainer(EntityPlayer player) {
		return logic.getContainer(player);
	}
	
	@Override
	public void clearAllMaterial() {
		logic.clearAllMaterial();
		
		super.clearAllMaterial();
		
		if (!WORLD.isRemote) {
			logic.refreshReactor();
			updateActivity();
		}
	}
	
	// Multiblock Validators
	
	@Override
	protected boolean isBlockGoodForInterior(World world, int x, int y, int z, Multiblock multiblock) {
		return logic.isBlockGoodForInterior(world, x, y, z, multiblock);
	}
}
