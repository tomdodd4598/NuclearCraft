package nc.tile.energyFluid;

import nc.ModCheck;
import nc.energy.EnumStorage.EnergyConnection;
import nc.fluid.EnumTank.FluidConnection;
import nc.tile.ITileInventory;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public abstract class TileEnergyFluidInventory extends TileEnergyFluid implements IInventory, ITileInventory {
	
	public String inventoryName;
	public ItemStack[] inventoryStacks;
	
	public TileEnergyFluidInventory(String name, int size, int capacity, EnergyConnection connection, int fluidCapacity, FluidConnection fluidConnection, String[]... allowedFluids) {
		this(name, size, capacity, capacity, capacity, connection, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, EnergyConnection connection, int[] fluidCapacity, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		this(name, size, capacity, capacity, capacity, connection, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, EnergyConnection connection, int fluidCapacity, int maxFluidTransfer, FluidConnection fluidConnection, String[]... allowedFluids) {
		this(name, size, capacity, capacity, capacity, connection, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, EnergyConnection connection, int[] fluidCapacity, int[] maxFluidTransfer, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		this(name, size, capacity, capacity, capacity, connection, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, EnergyConnection connection, int fluidCapacity, int maxFluidReceive, int maxFluidExtract, FluidConnection fluidConnection, String[]... allowedFluids) {
		this(name, size, capacity, capacity, capacity, connection, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, EnergyConnection connection, int[] fluidCapacity, int[] maxFluidReceive, int[] maxFluidExtract, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		this(name, size, capacity, capacity, capacity, connection, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection connection, int fluidCapacity, FluidConnection fluidConnection, String[]... allowedFluids) {
		this(name, size, capacity, maxTransfer, maxTransfer, connection, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection connection, int[] fluidCapacity, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		this(name, size, capacity, maxTransfer, maxTransfer, connection, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection connection, int fluidCapacity, int maxFluidTransfer, FluidConnection fluidConnection, String[]... allowedFluids) {
		this(name, size, capacity, maxTransfer, maxTransfer, connection, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection connection, int[] fluidCapacity, int[] maxFluidTransfer, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		this(name, size, capacity, maxTransfer, maxTransfer, connection, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection connection, int fluidCapacity, int maxFluidReceive, int maxFluidExtract, FluidConnection fluidConnection, String[]... allowedFluids) {
		this(name, size, capacity, maxTransfer, maxTransfer, connection, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection connection, int[] fluidCapacity, int[] maxFluidReceive, int[] maxFluidExtract, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		this(name, size, capacity, maxTransfer, maxTransfer, connection, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection connection, int fluidCapacity, FluidConnection fluidConnection, String[]... allowedFluids) {
		this(name, size, capacity, maxReceive, maxExtract, connection, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection connection, int[] fluidCapacity, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		this(name, size, capacity, maxReceive, maxExtract, connection, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection connection, int fluidCapacity, int maxFluidTransfer, FluidConnection fluidConnection, String[]... allowedFluids) {
		this(name, size, capacity, maxReceive, maxExtract, connection, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection connection, int[] fluidCapacity, int[] maxFluidTransfer, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		this(name, size, capacity, maxReceive, maxExtract, connection, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection connection, int fluidCapacity, int maxFluidReceive, int maxFluidExtract, FluidConnection fluidConnection, String[]... allowedFluids) {
		super(capacity, maxReceive, maxExtract, connection, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnection, allowedFluids);
		inventoryName = name;
		inventoryStacks = new ItemStack[size];
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection connection, int[] fluidCapacity, int[] maxFluidReceive, int[] maxFluidExtract, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		super(capacity, maxReceive, maxExtract, connection, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnection, allowedFluids);
		inventoryName = name;
		inventoryStacks = new ItemStack[size];
	}
	
	// Inventory Name
	
	public String getName() {
		return inventoryName;
	}
	
	public boolean hasCustomName() {
		return false;
	}
	
	// Inventory
	
	public int getSizeInventory() {
		return inventoryStacks.length;
	}

	public ItemStack getStackInSlot(int slot) {
		return inventoryStacks[slot];
	}

	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(inventoryStacks, index, count);
	}

	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(inventoryStacks, index);
	}

	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = inventoryStacks[index];
		boolean flag = !(stack == null) && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
		inventoryStacks[index] = stack;

		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}

		if (index == 0 && !flag) {
			markDirty();
		}
	}
			
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return true;
	}

	public int getInventoryStackLimit() {
		return 64;
	}
			
	public void clear() {
		for (int i = 0; i < inventoryStacks.length; ++i) {
			inventoryStacks[i] = null;
		}
	}

	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(pos) != this ? false : player.getDistanceSq((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D) <= 64.0D;
	}

	public void openInventory(EntityPlayer player) {}

	public void closeInventory(EntityPlayer player) {}
	
	public ItemStack[] getInventoryStacks() {
		return inventoryStacks;
	}
	
	// NBT
	
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		
		NBTTagList nbttaglist = new NBTTagList();
		
		for (int i = 0; i < inventoryStacks.length; ++i) {
			if (inventoryStacks[i] != null) {
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setByte("Slot", (byte)i);
				inventoryStacks[i].writeToNBT(nbttagcompound);
				nbttaglist.appendTag(nbttagcompound);
			}
		}
		
		nbt.setTag("Items", nbttaglist);
		return nbt;
	}
		
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		
		NBTTagList nbttaglist = nbt.getTagList("Items", 10);
		
		inventoryStacks = new ItemStack[getSizeInventory()];
		
		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound.getByte("Slot");
			
			if (j >= 0 && j < inventoryStacks.length) {
				inventoryStacks[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
			}
		}
	}
	
	// Inventory Fields
	
	public int getField(int id) {
		return 0;
	}

	public void setField(int id, int value) {}
		
	public int getFieldCount() {
		return 0;
	}
	
	// Capability
	
	net.minecraftforge.items.IItemHandler handler = new net.minecraftforge.items.wrapper.InvWrapper(this);
	
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (CapabilityEnergy.ENERGY == capability && connection.canConnect()) {
			return true;
		}
		if (connection != null && ModCheck.teslaLoaded && connection.canConnect()) {
			if ((capability == TeslaCapabilities.CAPABILITY_CONSUMER && connection.canReceive()) || (capability == TeslaCapabilities.CAPABILITY_PRODUCER && connection.canExtract()) || capability == TeslaCapabilities.CAPABILITY_HOLDER)
				return true;
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return true;
		}
		if (capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (CapabilityEnergy.ENERGY == capability && connection.canConnect()) {
			return (T) storage;
		}
		if (connection != null && ModCheck.teslaLoaded && connection.canConnect()) {
			if ((capability == TeslaCapabilities.CAPABILITY_CONSUMER && connection.canReceive()) || (capability == TeslaCapabilities.CAPABILITY_PRODUCER && connection.canExtract()) || capability == TeslaCapabilities.CAPABILITY_HOLDER)
				return (T) storage;
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this);
		}
		if (capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) handler;
		}
		return super.getCapability(capability, facing);
	}
}
