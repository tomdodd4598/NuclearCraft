package nc.tile.energyFluid;

import java.util.List;

import nc.Global;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.inventory.ITileInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public abstract class TileEnergyFluidInventory extends TileEnergyFluid implements IInventory, ITileInventory {
	
	public String inventoryName;
	public NonNullList<ItemStack> inventoryStacks;
	
	public TileEnergyFluidInventory(String name, int size, int capacity, EnergyConnection[] energyConnections, int fluidCapacity, FluidConnection fluidConnections, List<String> allowedFluids) {
		this(name, size, capacity, capacity, capacity, energyConnections, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, EnergyConnection[] energyConnections, List<Integer> fluidCapacity, List<FluidConnection> fluidConnections, List<List<String>> allowedFluids) {
		this(name, size, capacity, capacity, capacity, energyConnections, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, EnergyConnection[] energyConnections, int fluidCapacity, int maxFluidTransfer, FluidConnection fluidConnections, List<String> allowedFluids) {
		this(name, size, capacity, capacity, capacity, energyConnections, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, EnergyConnection[] energyConnections, List<Integer> fluidCapacity, List<Integer> maxFluidTransfer, List<FluidConnection> fluidConnections, List<List<String>> allowedFluids) {
		this(name, size, capacity, capacity, capacity, energyConnections, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, EnergyConnection[] energyConnections, int fluidCapacity, int maxFluidReceive, int maxFluidExtract, FluidConnection fluidConnections, List<String> allowedFluids) {
		this(name, size, capacity, capacity, capacity, energyConnections, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, EnergyConnection[] energyConnections, List<Integer> fluidCapacity, List<Integer> maxFluidReceive, List<Integer> maxFluidExtract, List<FluidConnection> fluidConnections, List<List<String>> allowedFluids) {
		this(name, size, capacity, capacity, capacity, energyConnections, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection[] energyConnections, int fluidCapacity, FluidConnection fluidConnections, List<String> allowedFluids) {
		this(name, size, capacity, maxTransfer, maxTransfer, energyConnections, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection[] energyConnections, List<Integer> fluidCapacity, List<FluidConnection> fluidConnections, List<List<String>> allowedFluids) {
		this(name, size, capacity, maxTransfer, maxTransfer, energyConnections, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection[] energyConnections, int fluidCapacity, int maxFluidTransfer, FluidConnection fluidConnections, List<String> allowedFluids) {
		this(name, size, capacity, maxTransfer, maxTransfer, energyConnections, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection[] energyConnections, List<Integer> fluidCapacity, List<Integer> maxFluidTransfer, List<FluidConnection> fluidConnections, List<List<String>> allowedFluids) {
		this(name, size, capacity, maxTransfer, maxTransfer, energyConnections, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection[] energyConnections, int fluidCapacity, int maxFluidReceive, int maxFluidExtract, FluidConnection fluidConnections, List<String> allowedFluids) {
		this(name, size, capacity, maxTransfer, maxTransfer, energyConnections, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection[] energyConnections, List<Integer> fluidCapacity, List<Integer> maxFluidReceive, List<Integer> maxFluidExtract, List<FluidConnection> fluidConnections, List<List<String>> allowedFluids) {
		this(name, size, capacity, maxTransfer, maxTransfer, energyConnections, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection[] energyConnections, int fluidCapacity, FluidConnection fluidConnections, List<String> allowedFluids) {
		this(name, size, capacity, maxReceive, maxExtract, energyConnections, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection[] energyConnections, List<Integer> fluidCapacity, List<FluidConnection> fluidConnections, List<List<String>> allowedFluids) {
		this(name, size, capacity, maxReceive, maxExtract, energyConnections, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection[] energyConnections, int fluidCapacity, int maxFluidTransfer, FluidConnection fluidConnections, List<String> allowedFluids) {
		this(name, size, capacity, maxReceive, maxExtract, energyConnections, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection[] energyConnections, List<Integer> fluidCapacity, List<Integer> maxFluidTransfer, List<FluidConnection> fluidConnections, List<List<String>> allowedFluids) {
		this(name, size, capacity, maxReceive, maxExtract, energyConnections, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection[] energyConnections, int fluidCapacity, int maxFluidReceive, int maxFluidExtract, FluidConnection fluidConnections, List<String> allowedFluids) {
		super(capacity, maxReceive, maxExtract, energyConnections, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnections, allowedFluids);
		inventoryName = Global.MOD_ID + ".container." + name;
		inventoryStacks = NonNullList.<ItemStack>withSize(size, ItemStack.EMPTY);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection[] energyConnections, List<Integer> fluidCapacity, List<Integer> maxFluidReceive, List<Integer> maxFluidExtract, List<FluidConnection> fluidConnections, List<List<String>> allowedFluids) {
		super(capacity, maxReceive, maxExtract, energyConnections, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnections, allowedFluids);
		inventoryName = Global.MOD_ID + ".container." + name;
		inventoryStacks = NonNullList.<ItemStack>withSize(size, ItemStack.EMPTY);
	}
	
	// Inventory Name
	
	@Override
	public String getName() {
		return inventoryName;
	}
	
	@Override
	public boolean hasCustomName() {
		return false;
	}
	
	// Inventory
	
	@Override
	public int getSizeInventory() {
		return inventoryStacks.size();
	}
	
	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : inventoryStacks) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventoryStacks.get(slot);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(inventoryStacks, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(inventoryStacks, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = inventoryStacks.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
		inventoryStacks.set(index, stack);

		if (stack.getCount() > getInventoryStackLimit()) {
			stack.setCount(getInventoryStackLimit());
		}

		if (index == 0 && !flag) {
			markDirty();
		}
	}
		
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return true;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
		
	@Override
	public void clear() {
		inventoryStacks.clear();
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return world.getTileEntity(pos) != this ? false : player.getDistanceSq((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}
	
	@Override
	public NonNullList<ItemStack> getInventoryStacks() {
		return inventoryStacks;
	}
	
	public void pushStacks() {
		if (isEmpty()) return;
		for (EnumFacing side : EnumFacing.VALUES) {
			IItemHandler inv = getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
			if (inv == null) continue;
			for (int i = 0; i < inventoryStacks.size(); i++) {
				if (inventoryStacks.get(i).isEmpty()) continue;
				TileEntity tile = world.getTileEntity(getPos().offset(side));
				IItemHandler adjInv = tile == null ? null : tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side.getOpposite());
				
				if (adjInv != null) {
					for (int j = 0; j < adjInv.getSlots(); j++) {
						if (!inv.extractItem(i, inventoryStacks.get(i).getCount() - adjInv.insertItem(j, inv.extractItem(i, getInventoryStackLimit(), true), false).getCount(), false).isEmpty()) {
							return;
						}
					}
				}
			}
		}
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		ItemStackHelper.saveAllItems(nbt, inventoryStacks);
		return nbt;
	}
		
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		inventoryStacks = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(nbt, inventoryStacks);
	}
	
	// Inventory Fields
	
	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {}
		
	@Override
	public int getFieldCount() {
		return 0;
	}
	
	// Capability
	
	IItemHandler handler = new InvWrapper(this);
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T) handler;
		return super.getCapability(capability, facing);
	}
}
