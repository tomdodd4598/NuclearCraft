package nc.tile.fluid;

import nc.fluid.EnumTank.FluidConnection;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public abstract class TileFluidSidedInventory extends TileFluidInventory implements ISidedInventory {
	
	public int[] topSlots;
	public int[] sideSlots;
	public int[] bottomSlots;
	
	public TileFluidSidedInventory(String name, int size, int capacity, FluidConnection connection, String[]... allowedFluids) {
		super(name, size, capacity, connection, allowedFluids);
	}
	
	public TileFluidSidedInventory(String name, int size, int[] capacity, FluidConnection[] connection, String[]... allowedFluids) {
		super(name, size, capacity, connection, allowedFluids);
	}
	
	public TileFluidSidedInventory(String name, int size, int capacity, int maxTransfer, FluidConnection connection, String[]... allowedFluids) {
		super(name, size, capacity, maxTransfer, connection, allowedFluids);
	}
	
	public TileFluidSidedInventory(String name, int size, int[] capacity, int[] maxTransfer, FluidConnection[] connection, String[]... allowedFluids) {
		super(name, size, capacity, maxTransfer, connection, allowedFluids);
	}
	
	public TileFluidSidedInventory(String name, int size, int capacity, int maxReceive, int maxExtract, FluidConnection connection, String[]... allowedFluids) {
		super(name, size, capacity, maxReceive, maxExtract, connection, allowedFluids);
	}
	
	public TileFluidSidedInventory(String name, int size, int[] capacity, int[] maxReceive, int[] maxExtract, FluidConnection[] connection, String[]... allowedFluids) {
		super(name, size, capacity, maxReceive, maxExtract, connection, allowedFluids);
	}
	
	// SidedInventory
	
	@Override
	public abstract int[] getSlotsForFace(EnumFacing side);

	@Override
	public abstract boolean canInsertItem(int slot, ItemStack stack, EnumFacing direction);

	@Override
	public abstract boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction);
	
	// Capability
	
	net.minecraftforge.items.IItemHandler handlerTop = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.UP);
	net.minecraftforge.items.IItemHandler handlerBottom = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.DOWN);
	net.minecraftforge.items.IItemHandler handlerSide = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.WEST);
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this);
		}
		if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (facing == EnumFacing.DOWN) {
				return (T) handlerBottom;
			} else if (facing == EnumFacing.UP) {
				return (T) handlerTop;
			} else {
				return (T) handlerSide;
			}
		}
		return super.getCapability(capability, facing);
	}
}
