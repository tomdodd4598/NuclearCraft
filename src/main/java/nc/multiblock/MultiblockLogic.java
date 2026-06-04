package nc.multiblock;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.*;
import nc.Global;
import nc.multiblock.fission.*;
import nc.multiblock.hx.*;
import nc.multiblock.machine.*;
import nc.multiblock.turbine.*;
import nc.tile.ITileFiltered;
import nc.tile.internal.energy.EnergyStorage;
import nc.tile.internal.fluid.Tank;
import nc.tile.multiblock.*;
import nc.tile.multiblock.ITileSorptionPart.SorptionKey;
import nc.tile.multiblock.TilePartAbstract.SyncReason;
import nc.tile.multiblock.manager.*;
import nc.tile.multiblock.port.*;
import nc.util.*;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public abstract class MultiblockLogic<MULTIBLOCK extends Multiblock<MULTIBLOCK, T> & ILogicMultiblock<MULTIBLOCK, LOGIC, T>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T>, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T>> implements IMultiblockLogic<MULTIBLOCK, LOGIC, T> {
	
	public final MULTIBLOCK multiblock;
	
	public final Random rand = new Random();
	
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
	
	public abstract List<Pair<Class<? extends T>, String>> getPartBlacklist();
	
	// Utility Methods
	
	public boolean containsBlacklistedPart() {
		for (Pair<Class<? extends T>, String> pair : getPartBlacklist()) {
			for (long posLong : getPartMap(pair.getLeft()).keySet()) {
				multiblock.setLastError(pair.getRight(), BlockPos.fromLong(posLong));
				return true;
			}
		}
		return false;
	}
	
	public static class SorptionInfo {
		public int inputCount = 0;
		public int outputCount = 0;
	}
	
	@SuppressWarnings("unchecked")
	public <PART extends ITileSorptionPart<MULTIBLOCK, T> & ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T>> Object2ObjectMap<SorptionKey, SorptionInfo> getSorptionDataMap(Class<PART> partClass) {
		Long2ObjectMap<PART> partMap = (Long2ObjectMap<PART>) getPartMap(partClass.asSubclass(multiblock.tClass));
		Object2ObjectMap<SorptionKey, SorptionInfo> dataMap = new Object2ObjectOpenHashMap<>();
		
		for (PART part : partMap.values()) {
			SorptionKey key = part.getSorptionKey();
			SorptionInfo info = dataMap.get(key);
			if (info == null) {
				info = new SorptionInfo();
				dataMap.put(key, info);
			}
			if (part.canReceive()) {
				++info.inputCount;
			}
			if (part.canExtract()) {
				++info.outputCount;
			}
		}
		
		return dataMap;
	}
	
	@SuppressWarnings("unchecked")
	public <PART extends ITileSorptionPart<MULTIBLOCK, T> & ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T>> boolean isMissingSorption(Class<PART> partClass, String partName) {
		Object2ObjectMap<SorptionKey, SorptionInfo> dataMap = this.getSorptionDataMap(partClass);
		for (Object2ObjectMap.Entry<SorptionKey, SorptionInfo> entry : dataMap.object2ObjectEntrySet()) {
			SorptionInfo info = entry.getValue();
			if (info.inputCount <= 0 || info.outputCount <= 0) {
				Long2ObjectMap<PART> partMap = (Long2ObjectMap<PART>) getPartMap(partClass.asSubclass(multiblock.tClass));
				String suffix = info.inputCount > 0 ? "output" : (info.outputCount > 0 ? "input" : "both");
				multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.sorption.missing_" + suffix, partMap.keySet(), partName);
				return true;
			}
		}
		return false;
	}
	
	public <PART extends ITileSorptionPart<MULTIBLOCK, T> & ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T>, TARGET extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T>> boolean isMissingSorption(Class<PART> partClass, Class<TARGET> targetClass, String partName) {
		return !getPartMap(targetClass.asSubclass(multiblock.tClass)).isEmpty() && isMissingSorption(partClass, partName);
	}
	
	@SuppressWarnings("unchecked")
	public <PORT extends ITilePort<MULTIBLOCK, LOGIC, T, PORT, TARGET> & ITileFiltered, TARGET extends ITilePortTarget<MULTIBLOCK, LOGIC, T, PORT, TARGET> & ITileFiltered> void refreshFilteredPorts(Class<PORT> portClass, Class<TARGET> targetClass) {
		Long2ObjectMap<PORT> portMap = (Long2ObjectMap<PORT>) getPartMap(portClass.asSubclass(multiblock.tClass));
		Long2ObjectMap<TARGET> targetMap = (Long2ObjectMap<TARGET>) getPartMap(targetClass.asSubclass(multiblock.tClass));
		
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
	public <MANAGER extends ITileManager<MULTIBLOCK, LOGIC, T, MANAGER, LISTENER>, LISTENER extends ITileManagerListener<MULTIBLOCK, LOGIC, T, MANAGER, LISTENER>> void refreshManagers(Class<MANAGER> managerClass) {
		for (MANAGER manager : ((Long2ObjectMap<MANAGER>) getPartMap(managerClass.asSubclass(multiblock.tClass))).values()) {
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
		Machine.LOGIC_MAP.put("", MachineLogic::new);
		Machine.LOGIC_MAP.put("electrolyzer", ElectrolyzerLogic::new);
		Machine.LOGIC_MAP.put("distiller", DistillerLogic::new);
		Machine.LOGIC_MAP.put("infiltrator", InfiltratorLogic::new);
		Machine.LOGIC_MAP.put("decay_pool", DecayPoolLogic::new);
		
		FissionReactor.LOGIC_MAP.put("", FissionReactorLogic::new);
		FissionReactor.LOGIC_MAP.put("pebble_bed", PebbleBedFissionLogic::new);
		FissionReactor.LOGIC_MAP.put("solid_fuel", SolidFuelFissionLogic::new);
		FissionReactor.LOGIC_MAP.put("molten_salt", MoltenSaltFissionLogic::new);
		
		HeatExchanger.LOGIC_MAP.put("heat_exchanger", HeatExchangerLogic::new);
		HeatExchanger.LOGIC_MAP.put("condenser", CondenserLogic::new);
		
		Turbine.LOGIC_MAP.put("turbine", TurbineLogic::new);
	}
}
