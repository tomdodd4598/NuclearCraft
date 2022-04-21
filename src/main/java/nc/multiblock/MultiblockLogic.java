package nc.multiblock;

import java.util.*;

import org.apache.commons.lang3.tuple.Pair;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.*;
import nc.multiblock.fission.*;
import nc.multiblock.fission.salt.MoltenSaltFissionLogic;
import nc.multiblock.fission.solid.SolidFuelFissionLogic;
import nc.multiblock.heatExchanger.*;
import nc.multiblock.tile.ITileLogicMultiblockPart;
import nc.multiblock.tile.TileBeefAbstract.SyncReason;
import nc.multiblock.tile.manager.*;
import nc.multiblock.tile.port.*;
import nc.multiblock.turbine.*;
import nc.tile.ITileFiltered;
import nc.tile.internal.energy.EnergyStorage;
import nc.tile.internal.fluid.Tank;
import nc.util.*;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class MultiblockLogic<MULTIBLOCK extends Multiblock<MULTIBLOCK, T> & ILogicMultiblock<MULTIBLOCK, LOGIC, T>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T>, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T>> implements IMultiblockLogic<MULTIBLOCK, LOGIC, T> {
	
	protected final MULTIBLOCK multiblock;
	
	protected Random rand = new Random();
	
	public MultiblockLogic(MULTIBLOCK multiblock) {
		this.multiblock = multiblock;
	}
	
	public MultiblockLogic(MultiblockLogic<MULTIBLOCK, LOGIC, T> oldLogic) {
		multiblock = oldLogic.multiblock;
	}
	
	@Override
	public abstract String getID();
	
	@Override
	public World getWorld() {
		return multiblock.WORLD;
	}
	
	// Multiblock Parts
	
	public SuperMap<Long, T, Long2ObjectMap<? extends T>> getPartSuperMap() {
		return multiblock.getPartSuperMap();
	}
	
	public <TYPE extends T> Long2ObjectMap<TYPE> getPartMap(Class<TYPE> type) {
		return getPartSuperMap().get(type);
	}
	
	// Multiblock Part Helpers
	
	public <TYPE extends T> int getPartCount(Class<TYPE> type) {
		return getPartMap(type).size();
	}
	
	public <TYPE extends T> Collection<TYPE> getParts(Class<TYPE> type) {
		return getPartMap(type).values();
	}
	
	public <TYPE extends T> Iterator<TYPE> getPartIterator(Class<TYPE> type) {
		return getParts(type).iterator();
	}
	
	// Multiblock Size Limits
	
	public abstract int getMinimumInteriorLength();
	
	public abstract int getMaximumInteriorLength();
	
	// Multiblock Methods
	
	public void onAttachedPartWithMultiblockData(T part, NBTTagCompound data) {}
	
	public void onBlockAdded(T newPart) {}
	
	public void onBlockRemoved(T oldPart) {}
	
	public abstract void onMachineAssembled();
	
	public abstract void onMachineRestored();
	
	public abstract void onMachinePaused();
	
	public abstract void onMachineDisassembled();
	
	public abstract void onAssimilate(MULTIBLOCK assimilated);
	
	public abstract void onAssimilated(MULTIBLOCK assimilator);
	
	public abstract boolean isMachineWhole();
	
	public abstract boolean onUpdateServer();
	
	public abstract void onUpdateClient();
	
	public boolean containsBlacklistedPart() {
		for (Pair<Class<? extends T>, String> pair : getPartBlacklist()) {
			for (long posLong : getPartMap(pair.getLeft()).keySet()) {
				multiblock.setLastError(pair.getRight(), BlockPos.fromLong(posLong));
				return true;
			}
		}
		return false;
	}
	
	public abstract List<Pair<Class<? extends T>, String>> getPartBlacklist();
	
	// Utility Methods
	
	@SuppressWarnings("unchecked")
	public <PORT extends ITilePort<MULTIBLOCK, LOGIC, T, PORT, TARGET> & ITileFiltered, PRT extends T, TARGET extends ITilePortTarget<MULTIBLOCK, LOGIC, T, PORT, TARGET> & ITileFiltered, TRGT extends T> void refreshFilteredPorts(Class<PORT> portClass, Class<TARGET> targetClass) {
		refreshFilteredPorts(portClass, (Class<PRT>) portClass, targetClass, (Class<TRGT>) targetClass);
	}
	
	@SuppressWarnings("unchecked")
	private <PORT extends ITilePort<MULTIBLOCK, LOGIC, T, PORT, TARGET> & ITileFiltered, PRT extends T, TARGET extends ITilePortTarget<MULTIBLOCK, LOGIC, T, PORT, TARGET> & ITileFiltered, TRGT extends T> void refreshFilteredPorts(Class<PORT> portClass, Class<PRT> portClz, Class<TARGET> targetClass, Class<TRGT> targetClz) {
		Long2ObjectMap<PORT> portMap = (Long2ObjectMap<PORT>) getPartMap(portClz);
		Long2ObjectMap<TARGET> targetMap = (Long2ObjectMap<TARGET>) getPartMap(targetClz);
		
		for (TARGET target : targetMap.values()) {
			target.clearMasterPort();
		}
		
		Object2ObjectMap<Object, PORT> masterPortMap = new Object2ObjectOpenHashMap<>();
		Object2IntMap<Object> targetCountMap = new Object2IntOpenHashMap<>();
		for (PORT port : portMap.values()) {
			Object filter = port.getFilterKey();
			if (PosHelper.DEFAULT_NON.equals(port.getMasterPortPos()) && !masterPortMap.containsKey(filter)) {
				masterPortMap.put(filter, port);
				targetCountMap.put(filter, 0);
			}
			port.clearMasterPort();
			port.getTargets().clear();
		}
		
		if (!multiblock.isAssembled() || portMap.isEmpty()) {
			return;
		}
		
		for (PORT port : portMap.values()) {
			Object filter = port.getFilterKey();
			if (!masterPortMap.containsKey(filter)) {
				masterPortMap.put(filter, port);
				targetCountMap.put(filter, 0);
			}
		}
		
		for (PORT port : portMap.values()) {
			Object filter = port.getFilterKey();
			PORT master = masterPortMap.get(filter);
			if (port != master) {
				port.setMasterPortPos(master.getTilePos());
				port.refreshMasterPort();
				port.setInventoryStackLimit(64);
				port.setTankCapacity(port.getTankBaseCapacity());
			}
		}
		
		for (TARGET target : targetMap.values()) {
			Object filter = target.getFilterKey();
			if (masterPortMap.containsKey(filter)) {
				PORT master = masterPortMap.get(filter);
				if (master != null) {
					master.getTargets().add(target);
					target.setMasterPortPos(master.getTilePos());
					target.refreshMasterPort();
					targetCountMap.put(filter, targetCountMap.get(filter) + 1);
				}
			}
		}
		
		for (Object2ObjectMap.Entry<Object, PORT> entry : masterPortMap.object2ObjectEntrySet()) {
			entry.getValue().setInventoryStackLimit(Math.max(64, entry.getValue().getInventoryStackLimitPerConnection() * targetCountMap.get(entry.getKey())));
			entry.getValue().setTankCapacity(Math.max(entry.getValue().getTankBaseCapacity(), entry.getValue().getTankCapacityPerConnection() * targetCountMap.get(entry.getKey())));
		}
	}
	
	@SuppressWarnings("unchecked")
	public <MANAGER extends ITileManager<MULTIBLOCK, LOGIC, T, MANAGER, LISTENER>, MNGR extends T, LISTENER extends ITileManagerListener<MULTIBLOCK, LOGIC, T, MANAGER, LISTENER>> void refreshManagers(Class<MANAGER> managerClass) {
		refreshManagers(managerClass, (Class<MNGR>) managerClass);
	}
	
	@SuppressWarnings("unchecked")
	private <MANAGER extends ITileManager<MULTIBLOCK, LOGIC, T, MANAGER, LISTENER>, MNGR extends T, LISTENER extends ITileManagerListener<MULTIBLOCK, LOGIC, T, MANAGER, LISTENER>> void refreshManagers(Class<MANAGER> managerClass, Class<MNGR> managerClz) {
		for (MANAGER manager : ((Long2ObjectMap<MANAGER>) getPartMap(managerClz)).values()) {
			manager.refreshManager();
		}
	}
	
	// NBT
	
	public abstract void writeToLogicTag(NBTTagCompound logicTag, SyncReason syncReason);
	
	public abstract void readFromLogicTag(NBTTagCompound logicTag, SyncReason syncReason);
	
	public NBTTagCompound writeStacks(NonNullList<ItemStack> stacks, NBTTagCompound data) {
		ItemStackHelper.saveAllItems(data, stacks);
		return data;
	}
	
	public void readStacks(NonNullList<ItemStack> stacks, NBTTagCompound data) {
		ItemStackHelper.loadAllItems(data, stacks);
	}
	
	public NBTTagCompound writeTanks(List<Tank> tanks, NBTTagCompound data, String name) {
		for (int i = 0; i < tanks.size(); ++i) {
			tanks.get(i).writeToNBT(data, name + i);
		}
		return data;
	}
	
	public void readTanks(List<Tank> tanks, NBTTagCompound data, String name) {
		for (int i = 0; i < tanks.size(); ++i) {
			tanks.get(i).readFromNBT(data, name + i);
		}
	}
	
	public NBTTagCompound writeEnergy(EnergyStorage storage, NBTTagCompound data, String name) {
		storage.writeToNBT(data, name);
		return data;
	}
	
	public void readEnergy(EnergyStorage storage, NBTTagCompound data, String name) {
		storage.readFromNBT(data, name);
	}
	
	// Multiblock Validators
	
	public boolean isBlockGoodForInterior(World world, BlockPos pos) {
		return true;
	}
	
	// Clear Material
	
	public abstract void clearAllMaterial();
	
	// Init
	
	public static void init() {
		try {
			FissionReactor.LOGIC_MAP.put("", FissionReactorLogic.class.getConstructor(FissionReactorLogic.class));
			// FissionReactor.LOGIC_MAP.put("pebble_bed", PebbleBedFissionLogic.class);
			FissionReactor.LOGIC_MAP.put("solid_fuel", SolidFuelFissionLogic.class.getConstructor(FissionReactorLogic.class));
			FissionReactor.LOGIC_MAP.put("molten_salt", MoltenSaltFissionLogic.class.getConstructor(FissionReactorLogic.class));
			
			HeatExchanger.LOGIC_MAP.put("heat_exchanger", HeatExchangerLogic.class.getConstructor(HeatExchangerLogic.class));
			HeatExchanger.LOGIC_MAP.put("condenser", CondenserLogic.class.getConstructor(HeatExchangerLogic.class));
			
			Turbine.LOGIC_MAP.put("turbine", TurbineLogic.class.getConstructor(TurbineLogic.class));
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
