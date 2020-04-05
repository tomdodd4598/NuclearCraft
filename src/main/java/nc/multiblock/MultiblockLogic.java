package nc.multiblock;

import java.util.List;
import java.util.Random;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.FissionReactorLogic;
import nc.multiblock.fission.salt.MoltenSaltFissionLogic;
import nc.multiblock.fission.salt.tile.TileSaltFissionHeater;
import nc.multiblock.fission.salt.tile.TileSaltFissionVessel;
import nc.multiblock.fission.solid.SolidFuelFissionLogic;
import nc.multiblock.fission.solid.tile.TileSolidFissionCell;
import nc.multiblock.fission.solid.tile.TileSolidFissionSink;
import nc.multiblock.fission.tile.IFissionComponent;
import nc.multiblock.fission.tile.IFissionController;
import nc.multiblock.fission.tile.IFissionSpecialComponent;
import nc.multiblock.fission.tile.TileFissionConductor;
import nc.multiblock.fission.tile.TileFissionIrradiator;
import nc.multiblock.fission.tile.TileFissionMonitor;
import nc.multiblock.fission.tile.TileFissionShield;
import nc.multiblock.fission.tile.TileFissionSource;
import nc.multiblock.fission.tile.TileFissionVent;
import nc.multiblock.fission.tile.manager.TileFissionShieldManager;
import nc.multiblock.fission.tile.port.TileFissionCellPort;
import nc.multiblock.fission.tile.port.TileFissionIrradiatorPort;
import nc.multiblock.heatExchanger.CondenserLogic;
import nc.multiblock.heatExchanger.HeatExchanger;
import nc.multiblock.heatExchanger.HeatExchangerLogic;
import nc.multiblock.heatExchanger.tile.IHeatExchangerController;
import nc.multiblock.heatExchanger.tile.TileCondenserTube;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerTube;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerVent;
import nc.multiblock.network.MultiblockUpdatePacket;
import nc.multiblock.tile.ITileLogicMultiblockPart;
import nc.multiblock.tile.ITileMultiblockPart;
import nc.multiblock.tile.TileBeefAbstract.SyncReason;
import nc.multiblock.tile.port.ITilePort;
import nc.multiblock.tile.port.ITilePortTarget;
import nc.multiblock.turbine.Turbine;
import nc.multiblock.turbine.TurbineLogic;
import nc.multiblock.turbine.tile.ITurbineController;
import nc.multiblock.turbine.tile.TileTurbineDynamoCoil;
import nc.multiblock.turbine.tile.TileTurbineInlet;
import nc.multiblock.turbine.tile.TileTurbineOutlet;
import nc.multiblock.turbine.tile.TileTurbineRotorBearing;
import nc.multiblock.turbine.tile.TileTurbineRotorBlade;
import nc.multiblock.turbine.tile.TileTurbineRotorShaft;
import nc.multiblock.turbine.tile.TileTurbineRotorStator;
import nc.tile.internal.fluid.Tank;
import nc.tile.inventory.ITileFilteredInventory;
import nc.util.BlockPosHelper;
import nc.util.SuperMap;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public abstract class MultiblockLogic<MULTIBLOCK extends Multiblock<PACKET> & ILogicMultiblock<LOGIC, T>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC, T, PACKET>, T extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC, T>, PACKET extends MultiblockUpdatePacket> {
	
	protected final MULTIBLOCK multiblock;
	
	protected Random rand = new Random();
	
	public MultiblockLogic(MULTIBLOCK multiblock) {
		this.multiblock = multiblock;
	}
	
	public MultiblockLogic(MultiblockLogic<MULTIBLOCK, LOGIC, T, PACKET> oldLogic) {
		multiblock = oldLogic.multiblock;
	}
	
	public SuperMap<Long, T, Long2ObjectMap<? extends T>> getPartSuperMap() {
		return multiblock.getPartSuperMap();
	}
	
	public <TYPE extends T> Long2ObjectMap<TYPE> getPartMap(Class<TYPE> type) {
		return getPartSuperMap().get(type);
	}
	
	public abstract String getID();
	
	protected World getWorld() {
		return multiblock.WORLD;
	}
	
	// Multiblock Size Limits
	
	public abstract int getMinimumInteriorLength();
	
	public abstract int getMaximumInteriorLength();
	
	// Multiblock Methods
	
	public void onAttachedPartWithMultiblockData(ITileMultiblockPart part, NBTTagCompound data) {}
	
	public void onBlockAdded(ITileMultiblockPart newPart) {}
	
	public void onBlockRemoved(ITileMultiblockPart oldPart) {}
	
	public abstract void onMachineAssembled();
	
	public abstract void onMachineRestored();
	
	public abstract void onMachinePaused();
	
	public abstract void onMachineDisassembled();
	
	public abstract boolean isMachineWhole(Multiblock multiblock);
	
	// Utility Methods
	
	public <PORT extends ITilePort<MULTIBLOCK, LOGIC, T, PORT, TARGET> & ITileFilteredInventory, PRT extends T, TARGET extends ITilePortTarget<MULTIBLOCK, LOGIC, T, PORT, TARGET> & ITileFilteredInventory, TRGT extends T> void refreshFilteredItemPorts(Class<PORT> portClass, Class<TARGET> targetClass) {
		refreshFilteredItemPorts(portClass, (Class<PRT>) portClass, targetClass, (Class<TRGT>) targetClass);
	}
	
	private <PORT extends ITilePort<MULTIBLOCK, LOGIC, T, PORT, TARGET> & ITileFilteredInventory, PRT extends T, TARGET extends ITilePortTarget<MULTIBLOCK, LOGIC, T, PORT, TARGET> & ITileFilteredInventory, TRGT extends T> void refreshFilteredItemPorts(Class<PORT> portClass, Class<PRT> portClz, Class<TARGET> targetClass, Class<TRGT> targetClz) {
		Long2ObjectMap<PORT> portMap = (Long2ObjectMap<PORT>) getPartMap(portClz);
		Long2ObjectMap<TARGET> targetMap = (Long2ObjectMap<TARGET>) getPartMap(targetClz);
		
		for (TARGET target : targetMap.values()) {
			target.clearMasterPort();
		}
		
		Int2ObjectMap<PORT> masterPortMap = new Int2ObjectOpenHashMap<>();
		Int2IntMap targetCountMap = new Int2IntOpenHashMap();
		for (PORT port : portMap.values()) {
			int filter = port.getFilterID();
			if (BlockPosHelper.DEFAULT_NON.equals(port.getMasterPortPos()) && !masterPortMap.containsKey(filter)) {
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
			int filter = port.getFilterID();
			if (!masterPortMap.containsKey(filter)) {
				masterPortMap.put(filter, port);
				targetCountMap.put(filter, 0);
			}
		}
		
		for (PORT port : portMap.values()) {
			int filter = port.getFilterID();
			PORT master = masterPortMap.get(filter);
			if (port != master) {
				port.setMasterPortPos(master.getTilePos());
				port.refreshMasterPort();
				port.setInventoryStackLimit(64);
			}
		}
		
		for (TARGET target : targetMap.values()) {
			int filter = target.getFilterID();
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
		
		for (Int2ObjectMap.Entry<PORT> entry : masterPortMap.int2ObjectEntrySet()) {
			entry.getValue().setInventoryStackLimit(Math.max(64, 2*targetCountMap.get(entry.getIntKey())));
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
	
	public NBTTagCompound writeTanks(List<Tank> tanks, NBTTagCompound data) {
		for (int i = 0; i < tanks.size(); i++) {
			tanks.get(i).writeToNBT(data, i);
		}
		return data;
	}
	
	public void readTanks(List<Tank> tanks, NBTTagCompound data) {
		for (int i = 0; i < tanks.size(); i++) {
			tanks.get(i).readFromNBT(data, i);
		}
	}
	
	// Packets
	
	public abstract PACKET getUpdatePacket();
	
	public abstract void onPacket(PACKET message);
	
	// Multiblock Validators
	
	public boolean isBlockGoodForInterior(World world, int x, int y, int z, Multiblock multiblock) {
		return true;
	}
	
	// Init
	
	public static void init() {
		FissionReactor.PART_CLASSES.add(IFissionController.class);
		FissionReactor.PART_CLASSES.add(IFissionComponent.class);
		FissionReactor.PART_CLASSES.add(IFissionSpecialComponent.class);
		FissionReactor.PART_CLASSES.add(TileFissionConductor.class);
		FissionReactor.PART_CLASSES.add(TileFissionMonitor.class);
		FissionReactor.PART_CLASSES.add(TileFissionVent.class);
		FissionReactor.PART_CLASSES.add(TileFissionIrradiatorPort.class);
		FissionReactor.PART_CLASSES.add(TileFissionCellPort.class);
		FissionReactor.PART_CLASSES.add(TileFissionShieldManager.class);
		FissionReactor.PART_CLASSES.add(TileFissionIrradiator.class);
		FissionReactor.PART_CLASSES.add(TileFissionSource.class);
		FissionReactor.PART_CLASSES.add(TileFissionShield.class);
		FissionReactor.PART_CLASSES.add(TileSolidFissionCell.class);
		FissionReactor.PART_CLASSES.add(TileSolidFissionSink.class);
		FissionReactor.PART_CLASSES.add(TileSaltFissionVessel.class);
		FissionReactor.PART_CLASSES.add(TileSaltFissionHeater.class);
		
		HeatExchanger.PART_CLASSES.add(IHeatExchangerController.class);
		HeatExchanger.PART_CLASSES.add(TileHeatExchangerVent.class);
		HeatExchanger.PART_CLASSES.add(TileHeatExchangerTube.class);
		HeatExchanger.PART_CLASSES.add(TileCondenserTube.class);
		
		Turbine.PART_CLASSES.add(ITurbineController.class);
		Turbine.PART_CLASSES.add(TileTurbineRotorShaft.class);
		Turbine.PART_CLASSES.add(TileTurbineRotorBlade.class);
		Turbine.PART_CLASSES.add(TileTurbineRotorStator.class);
		Turbine.PART_CLASSES.add(TileTurbineRotorBearing.class);
		Turbine.PART_CLASSES.add(TileTurbineDynamoCoil.class);
		Turbine.PART_CLASSES.add(TileTurbineInlet.class);
		Turbine.PART_CLASSES.add(TileTurbineOutlet.class);
		
		try {
			FissionReactor.LOGIC_MAP.put("", FissionReactorLogic.class.getConstructor(FissionReactorLogic.class));
			//FissionReactor.LOGIC_MAP.put("pebble_bed", PebbleBedFissionLogic.class);
			FissionReactor.LOGIC_MAP.put("solid_fuel", SolidFuelFissionLogic.class.getConstructor(FissionReactorLogic.class));
			FissionReactor.LOGIC_MAP.put("molten_salt", MoltenSaltFissionLogic.class.getConstructor(FissionReactorLogic.class));
			
			HeatExchanger.LOGIC_MAP.put("heat_exchanger", HeatExchangerLogic.class.getConstructor(HeatExchangerLogic.class));
			HeatExchanger.LOGIC_MAP.put("condenser", CondenserLogic.class.getConstructor(HeatExchangerLogic.class));
			
			Turbine.LOGIC_MAP.put("turbine", TurbineLogic.class.getConstructor(TurbineLogic.class));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
