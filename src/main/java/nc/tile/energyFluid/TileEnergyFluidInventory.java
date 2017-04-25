package nc.tile.energyFluid;

import nc.Global;
import nc.ModCheck;
import nc.energy.EnumStorage.EnergyConnection;
import nc.fluid.EnumTank.FluidConnection;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public abstract class TileEnergyFluidInventory extends TileEnergyFluid implements IInventory {
	
	public String inventoryName;
	public NonNullList<ItemStack> inventoryStacks;
	
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
		inventoryName = Global.MOD_ID + ".container." + name;
		inventoryStacks = NonNullList.<ItemStack>withSize(size, ItemStack.EMPTY);
	}
	
	public TileEnergyFluidInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection connection, int[] fluidCapacity, int[] maxFluidReceive, int[] maxFluidExtract, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		super(capacity, maxReceive, maxExtract, connection, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnection, allowedFluids);
		inventoryName = Global.MOD_ID + ".container." + name;
		inventoryStacks = NonNullList.<ItemStack>withSize(size, ItemStack.EMPTY);
	}
	
	// Inventory Name
	
	public String getName() {
		return inventoryName;
	}
	
	public ITextComponent getDisplayName() {
		return new TextComponentTranslation(blockType.getLocalizedName());
	}
	
	public boolean hasCustomName() {
		return false;
	}
	
	// Inventory
	
	public int getSizeInventory() {
		return inventoryStacks.size();
	}

	public boolean isEmpty() {
		for (ItemStack itemstack : inventoryStacks) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public ItemStack getStackInSlot(int slot) {
		return inventoryStacks.get(slot);
	}

	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(inventoryStacks, index, count);
	}

	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(inventoryStacks, index);
	}

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
		
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return true;
	}

	public int getInventoryStackLimit() {
		return 64;
	}
		
	public void clear() {
		inventoryStacks.clear();
	}

	public boolean isUsableByPlayer(EntityPlayer player) {
		return world.getTileEntity(pos) != this ? false : player.getDistanceSq((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D) <= 64.0D;
	}

	public void openInventory(EntityPlayer player) {}

	public void closeInventory(EntityPlayer player) {}
	
	// NBT
	
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		ItemStackHelper.saveAllItems(nbt, inventoryStacks);
		return nbt;
	}
		
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		inventoryStacks = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(nbt, inventoryStacks);
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
		if (CapabilityEnergy.ENERGY == capability) {
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
		if (CapabilityEnergy.ENERGY == capability) {
			return (T) storage;
		}
		if (connection != null && ModCheck.teslaLoaded && connection.canConnect()) {
			if ((capability == TeslaCapabilities.CAPABILITY_CONSUMER && connection.canReceive()) || (capability == TeslaCapabilities.CAPABILITY_PRODUCER && connection.canExtract()) || capability == TeslaCapabilities.CAPABILITY_HOLDER)
				return (T) storage;
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (T) tanks;
		}
		if (capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) handler;
		}
		return super.getCapability(capability, facing);
	}
}
