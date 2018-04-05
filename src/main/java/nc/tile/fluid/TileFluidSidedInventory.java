package nc.tile.fluid;

import javax.annotation.Nullable;

import nc.tile.internal.EnumTank.FluidConnection;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

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
	
	IItemHandler handlerTop = new SidedInvWrapper(this, EnumFacing.UP);
	IItemHandler handlerBottom = new SidedInvWrapper(this, EnumFacing.DOWN);
	IItemHandler handlerSide = new SidedInvWrapper(this, EnumFacing.WEST);
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this);
		}
		if (facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
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
