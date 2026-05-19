package nc.multiblock.distributor;

import it.unimi.dsi.fastutil.objects.*;
import nc.multiblock.Multiblock;
import nc.tile.distributor.*;
import nc.tile.internal.energy.EnergyStorage;
import nc.tile.internal.fluid.Tank;
import nc.tile.multiblock.TilePartAbstract.SyncReason;
import nc.tile.passive.ITilePassive;
import nc.util.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.*;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.*;
import net.minecraftforge.items.*;

import java.util.*;
import java.util.stream.Collectors;

public class Distributor extends Multiblock<Distributor, IDistributorPart> {
	
	public static final long BASE_ENERGY_CAPACITY = 16000L;
	public static final int BASE_ITEM_STACK_LIMIT = 64;
	public static final int BASE_FLUID_CAPACITY = 4000;
	
	public static final ObjectSet<Class<? extends IDistributorPart>> PART_CLASSES = new ObjectOpenHashSet<>();
	
	protected final PartSuperMap<Distributor, IDistributorPart> partSuperMap = new PartSuperMap<>();
	
	public final EnergyStorage energyStorage = new EnergyStorage(BASE_ENERGY_CAPACITY);
	public InventoryStackList inventoryStacks = InventoryStackList.withSize(1);
	public int itemStackLimit = BASE_ITEM_STACK_LIMIT;
	public final Tank tank = new Tank(BASE_FLUID_CAPACITY, null);
	
	protected boolean refreshStorage = false;
	public boolean storageUpdated = false;
	
	protected int energyOutputOffset = 0, itemOutputOffset = 0, fluidOutputOffset = 0;
	
	public Distributor(World world) {
		super(world, Distributor.class, IDistributorPart.class);
		for (Class<? extends IDistributorPart> clazz : PART_CLASSES) {
			partSuperMap.equip(clazz);
		}
	}
	
	@Override
	public PartSuperMap<Distributor, IDistributorPart> getPartSuperMap() {
		return partSuperMap;
	}
	
	@Override
	public void onAttachedPartWithMultiblockData(IDistributorPart part, NBTTagCompound data) {
		syncDataFrom(data, SyncReason.FullSync);
	}
	
	@Override
	protected void onBlockAdded(IDistributorPart newPart) {
		onPartAdded(newPart);
		refreshStorage = true;
	}
	
	@Override
	protected void onBlockRemoved(IDistributorPart oldPart) {
		onPartRemoved(oldPart);
		refreshStorage = true;
	}
	
	@Override
	protected void onMachineAssembled() {
		onMultiblockFormed();
	}
	
	@Override
	protected void onMachineRestored() {
		onMultiblockFormed();
	}
	
	protected void onMultiblockFormed() {
		if (!WORLD.isRemote) {
			refreshStorage = true;
		}
	}
	
	@Override
	protected void onMachinePaused() {}
	
	@Override
	protected void onMachineDisassembled() {}
	
	@Override
	protected int getMinimumNumberOfBlocksForAssembledMachine() {
		return 1;
	}
	
	@Override
	protected int getMaximumXSize() {
		return Integer.MAX_VALUE;
	}
	
	@Override
	protected int getMaximumZSize() {
		return Integer.MAX_VALUE;
	}
	
	@Override
	protected int getMaximumYSize() {
		return Integer.MAX_VALUE;
	}
	
	@Override
	protected boolean isMachineWhole() {
		return true;
	}
	
	@Override
	protected void onAssimilate(Distributor assimilated) {
		energyStorage.setStorageCapacity(energyStorage.getMaxEnergyStoredLong() + assimilated.energyStorage.getMaxEnergyStoredLong());
		energyStorage.setEnergyStored(energyStorage.getEnergyStoredLong() + assimilated.energyStorage.getEnergyStoredLong());
		assimilated.energyStorage.setEnergyStored(0L);
		
		ItemStack assimilatedStack = assimilated.inventoryStacks.get(0);
		if (!assimilatedStack.isEmpty()) {
			ItemStack remaining = insertIntoInventory(assimilatedStack.copy());
			if (!remaining.isEmpty()) {
				dropOverflow(Collections.singletonList(remaining));
			}
		}
		
		FluidStack otherFluid = assimilated.tank.getFluid();
		if (otherFluid != null && (tank.getFluid() == null || tank.getFluid().isFluidEqual(otherFluid))) {
			tank.setTankCapacity(tank.getCapacity() + assimilated.tank.getCapacity());
			int filled = tank.fill(otherFluid.copy(), true);
			assimilated.tank.drain(filled, true);
		}
		
		energyOutputOffset = Math.max(energyOutputOffset, assimilated.energyOutputOffset);
		itemOutputOffset = Math.max(itemOutputOffset, assimilated.itemOutputOffset);
		fluidOutputOffset = Math.max(fluidOutputOffset, assimilated.fluidOutputOffset);
		
		refreshStorage = true;
	}
	
	@Override
	protected void onAssimilated(Distributor assimilator) {}
	
	@Override
	protected boolean updateServer() {
		boolean shouldUpdate = storageUpdated;
		storageUpdated = false;
		
		if (refreshStorage) {
			shouldUpdate |= refreshCapacity();
			refreshStorage = false;
		}
		
		List<TileDistributorOutlet> outlets = getParts(TileDistributorOutlet.class).stream().sorted(Comparator.comparingLong(x -> x.getPos().toLong())).collect(Collectors.toList());
		
		if (!outlets.isEmpty()) {
			shouldUpdate |= distributeEnergy(outlets);
			shouldUpdate |= distributeItems(outlets);
			shouldUpdate |= distributeFluid(outlets);
		}
		
		return shouldUpdate;
	}
	
	protected boolean refreshCapacity() {
		boolean changed = false;
		
		long mult = Math.max(1L, (long) getPartCount(TileDistributorBuffer.class) + (long) getPartCount(TileDistributorInlet.class) + (long) getPartCount(TileDistributorOutlet.class));
		
		long newEnergyCapacity = BASE_ENERGY_CAPACITY * mult;
		if (energyStorage.getMaxEnergyStoredLong() != newEnergyCapacity) {
			energyStorage.setStorageCapacity(newEnergyCapacity);
			energyStorage.setMaxTransfer(newEnergyCapacity);
			energyStorage.cullEnergyStored();
			changed = true;
		}
		
		int newItemStackLimit = Math.max(64, NCMath.toInt(BASE_ITEM_STACK_LIMIT * mult));
		if (itemStackLimit != newItemStackLimit) {
			itemStackLimit = newItemStackLimit;
			changed = true;
		}
		
		changed |= cullInventory();
		
		int newTankCapacity = NCMath.toInt(BASE_FLUID_CAPACITY * mult);
		if (tank.getCapacity() != newTankCapacity) {
			tank.setTankCapacity(newTankCapacity);
			tank.clampTankAmount();
			changed = true;
		}
		
		return changed;
	}
	
	protected boolean cullInventory() {
		ItemStack stack = inventoryStacks.get(0);
		if (stack.isEmpty() || stack.getCount() <= itemStackLimit) {
			return false;
		}
		
		int overflowCount = stack.getCount() - itemStackLimit;
		stack.setCount(itemStackLimit);
		ItemStack overflow = stack.copy();
		overflow.setCount(overflowCount);
		dropOverflow(Collections.singletonList(overflow));
		return true;
	}
	
	protected void dropOverflow(List<ItemStack> overflow) {
		if (WORLD.isRemote || overflow.isEmpty()) {
			return;
		}
		BlockPos referencePos = getReferenceCoord();
		if (referencePos != null) {
			NCInventoryHelper.dropInventoryItems(WORLD, referencePos, overflow);
		}
	}
	
	protected boolean distributeEnergy(List<TileDistributorOutlet> outlets) {
		int storedEnergy = NCMath.toInt(energyStorage.getEnergyStoredLong());
		if (storedEnergy <= 0) {
			return false;
		}
		
		List<IEnergyStorage> availableHandlers = new ArrayList<>();
		List<Integer> capacityList = new ArrayList<>();
		long capacitySum = 0L;
		
		for (TileDistributorOutlet outlet : outlets) {
			for (EnumFacing side : EnumFacing.VALUES) {
				TileEntity tile = WORLD.getTileEntity(outlet.getPos().offset(side));
				if (tile == null || tile instanceof TileDistributorPart || (tile instanceof ITilePassive tilePassive && !tilePassive.canPushEnergyTo())) {
					continue;
				}
				
				IEnergyStorage handler = tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
				if (handler == null) {
					continue;
				}
				
				int capacity = handler.receiveEnergy(storedEnergy, true);
				if (capacity > 0) {
					availableHandlers.add(handler);
					capacityList.add(capacity);
					capacitySum += capacity;
				}
			}
		}
		
		if (availableHandlers.isEmpty()) {
			return false;
		}
		
		int[] capacities = capacityList.stream().mapToInt(Integer::intValue).toArray();
		int distributable = Math.min(storedEnergy, NCMath.toInt(capacitySum));
		int[] allocations = getAllocations(distributable, capacities, energyOutputOffset);
		
		long totalSent = 0L;
		for (int i = 0; i < availableHandlers.size(); ++i) {
			int allocation = allocations[i];
			if (allocation <= 0) {
				continue;
			}
			
			int toSend = Math.min(allocation, NCMath.toInt(energyStorage.getEnergyStoredLong()));
			if (toSend <= 0) {
				continue;
			}
			int sent = availableHandlers.get(i).receiveEnergy(toSend, false);
			if (sent > 0) {
				energyStorage.extractEnergy(sent, false);
				totalSent += sent;
			}
		}
		
		energyOutputOffset = availableHandlers.isEmpty() ? 0 : (energyOutputOffset + 1) % availableHandlers.size();
		return totalSent > 0L;
	}
	
	protected boolean distributeItems(List<TileDistributorOutlet> outlets) {
		if (inventoryStacks.get(0).isEmpty()) {
			return false;
		}
		
		ItemStack storedStack = inventoryStacks.get(0);
		List<IItemHandler> availableHandlers = new ArrayList<>();
		List<Integer> capacityList = new ArrayList<>();
		long capacitySum = 0L;
		
		for (TileDistributorOutlet outlet : outlets) {
			for (EnumFacing side : EnumFacing.VALUES) {
				TileEntity tile = WORLD.getTileEntity(outlet.getPos().offset(side));
				if (tile == null || tile instanceof TileDistributorPart || (tile instanceof ITilePassive tilePassive && !tilePassive.canPushItemsTo())) {
					continue;
				}
				
				IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side.getOpposite());
				if (handler == null || handler.getSlots() <= 0) {
					continue;
				}
				
				ItemStack offered = storedStack.copy();
				offered.setCount(storedStack.getCount());
				int capacity = offered.getCount() - ItemHandlerHelper.insertItemStacked(handler, offered, true).getCount();
				if (capacity > 0) {
					availableHandlers.add(handler);
					capacityList.add(capacity);
					capacitySum += capacity;
				}
			}
		}
		
		if (availableHandlers.isEmpty()) {
			return false;
		}
		
		int[] capacities = capacityList.stream().mapToInt(Integer::intValue).toArray();
		int distributable = Math.min(storedStack.getCount(), NCMath.toInt(capacitySum));
		int[] allocations = getAllocations(distributable, capacities, itemOutputOffset);
		
		boolean changed = false;
		for (int i = 0; i < availableHandlers.size(); ++i) {
			int allocation = allocations[i];
			if (allocation <= 0 || storedStack.isEmpty()) {
				continue;
			}
			
			int toSend = Math.min(allocation, storedStack.getCount());
			ItemStack offered = storedStack.copy();
			offered.setCount(toSend);
			int sent = toSend - ItemHandlerHelper.insertItemStacked(availableHandlers.get(i), offered, false).getCount();
			if (sent > 0) {
				storedStack.shrink(sent);
				if (storedStack.getCount() <= 0) {
					inventoryStacks.set(0, ItemStack.EMPTY);
				}
				changed = true;
			}
		}
		
		itemOutputOffset = availableHandlers.isEmpty() ? 0 : (itemOutputOffset + 1) % availableHandlers.size();
		return changed;
	}
	
	protected boolean distributeFluid(List<TileDistributorOutlet> outlets) {
		FluidStack fluid = tank.getFluid();
		if (fluid == null) {
			return false;
		}
		
		List<IFluidHandler> availableHandlers = new ArrayList<>();
		List<Integer> capacityList = new ArrayList<>();
		long capacitySum = 0L;
		
		for (TileDistributorOutlet outlet : outlets) {
			for (EnumFacing side : EnumFacing.VALUES) {
				TileEntity tile = WORLD.getTileEntity(outlet.getPos().offset(side));
				if (tile == null || tile instanceof TileDistributorPart || (tile instanceof ITilePassive tilePassive && !tilePassive.canPushFluidsTo())) {
					continue;
				}
				
				IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
				if (handler == null) {
					continue;
				}
				
				int capacity = handler.fill(new FluidStack(fluid, tank.getFluidAmount()), false);
				if (capacity > 0) {
					availableHandlers.add(handler);
					capacityList.add(capacity);
					capacitySum += capacity;
				}
			}
		}
		
		if (availableHandlers.isEmpty()) {
			return false;
		}
		
		int[] capacities = capacityList.stream().mapToInt(Integer::intValue).toArray();
		int distributable = Math.min(tank.getFluidAmount(), NCMath.toInt(capacitySum));
		int[] allocations = getAllocations(distributable, capacities, fluidOutputOffset);
		
		int totalSent = 0;
		for (int i = 0; i < availableHandlers.size(); ++i) {
			int allocation = allocations[i];
			if (allocation <= 0 || tank.getFluid() == null) {
				continue;
			}
			
			int toSend = Math.min(allocation, tank.getFluidAmount());
			if (toSend <= 0) {
				continue;
			}
			int sent = availableHandlers.get(i).fill(new FluidStack(fluid, toSend), true);
			if (sent > 0) {
				tank.drain(sent, true);
				totalSent += sent;
			}
		}
		
		fluidOutputOffset = availableHandlers.isEmpty() ? 0 : (fluidOutputOffset + 1) % availableHandlers.size();
		return totalSent > 0;
	}
	
	protected int[] getAllocations(int total, int[] capacities, int offset) {
		int size = capacities.length;
		int[] allocations = new int[size];
		if (total <= 0 || size == 0) {
			return allocations;
		}
		
		List<Integer> active = new ArrayList<>();
		for (int i = 0; i < size; ++i) {
			if (capacities[i] > 0) {
				active.add(i);
			}
		}
		
		if (active.isEmpty()) {
			return allocations;
		}
		
		int activeOffset = Math.floorMod(offset, active.size());
		if (activeOffset != 0) {
			Collections.rotate(active, -activeOffset);
		}
		
		int[] remainingCapacity = Arrays.copyOf(capacities, size);
		int remaining = total;
		
		while (remaining > 0 && !active.isEmpty()) {
			int share = remaining / active.size();
			if (share == 0) {
				share = 1;
			}
			
			boolean progressed = false;
			List<Integer> nextActive = new ArrayList<>();
			
			for (int i : active) {
				if (remaining <= 0) {
					break;
				}
				
				int give = Math.min(share, Math.min(remaining, remainingCapacity[i]));
				if (give > 0) {
					allocations[i] += give;
					remainingCapacity[i] -= give;
					remaining -= give;
					progressed = true;
				}
				
				if (remainingCapacity[i] > 0) {
					nextActive.add(i);
				}
			}
			
			if (!progressed) {
				break;
			}
			
			active = nextActive;
		}
		
		return allocations;
	}
	
	protected ItemStack insertIntoInventory(ItemStack stack) {
		if (stack.isEmpty()) {
			return ItemStack.EMPTY;
		}
		
		ItemStack slotStack = inventoryStacks.get(0);
		if (slotStack.isEmpty()) {
			int toMove = Math.min(itemStackLimit, stack.getCount());
			ItemStack inserted = stack.copy();
			inserted.setCount(toMove);
			inventoryStacks.set(0, inserted);
			stack.shrink(toMove);
			return stack.isEmpty() ? ItemStack.EMPTY : stack;
		}
		
		if (!ItemHandlerHelper.canItemStacksStack(slotStack, stack)) {
			return stack;
		}
		
		int room = itemStackLimit - slotStack.getCount();
		if (room <= 0) {
			return stack;
		}
		
		int toMove = Math.min(room, stack.getCount());
		slotStack.grow(toMove);
		stack.shrink(toMove);
		return stack.isEmpty() ? ItemStack.EMPTY : stack;
	}
	
	public int getPowerPortEUSinkTier() {
		return 10;
	}
	
	public int getPowerPortEUSourceTier() {
		return EnergyHelper.getEUTier(energyStorage.getEnergyStoredLong());
	}
	
	@Override
	protected void updateClient() {}
	
	@Override
	protected boolean isBlockGoodForInterior(World world, BlockPos pos) {
		return true;
	}
	
	@Override
	public void syncDataFrom(NBTTagCompound data, SyncReason syncReason) {
		readEnergy(energyStorage, data, "energyStorage");
		
		itemStackLimit = data.hasKey("itemStackLimit") ? data.getInteger("itemStackLimit") : BASE_ITEM_STACK_LIMIT;
		
		inventoryStacks = InventoryStackList.withSize(1);
		NBTHelper.readAllItems(data, "distributorInventory", inventoryStacks);
		cullInventory();
		
		tank.readFromNBT(data, "distributorTank");
		
		itemOutputOffset = data.getInteger("itemOutputOffset");
		fluidOutputOffset = data.getInteger("fluidOutputOffset");
		energyOutputOffset = data.getInteger("energyOutputOffset");
		
		refreshStorage = true;
	}
	
	@Override
	public void syncDataTo(NBTTagCompound data, SyncReason syncReason) {
		writeEnergy(energyStorage, data, "energyStorage");
		data.setInteger("itemStackLimit", itemStackLimit);
		NBTHelper.writeAllItems(data, "distributorInventory", inventoryStacks);
		tank.writeToNBT(data, "distributorTank");
		
		data.setInteger("itemOutputOffset", itemOutputOffset);
		data.setInteger("fluidOutputOffset", fluidOutputOffset);
		data.setInteger("energyOutputOffset", energyOutputOffset);
	}
}
