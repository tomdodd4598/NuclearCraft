package nc.tile.fluid;

import java.util.List;

import javax.annotation.Nullable;

import nc.tile.internal.fluid.FluidConnection;
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
	
	public TileFluidSidedInventory(String name, int size, int capacity, FluidConnection fluidConnections, List<String> allowedFluids) {
		super(name, size, capacity, fluidConnections, allowedFluids);
	}
	
	public TileFluidSidedInventory(String name, int size, List<Integer> capacity, List<FluidConnection> fluidConnections, List<List<String>> allowedFluids) {
		super(name, size, capacity, fluidConnections, allowedFluids);
	}
	
	public TileFluidSidedInventory(String name, int size, int capacity, int maxTransfer, FluidConnection fluidConnections, List<String> allowedFluids) {
		super(name, size, capacity, maxTransfer, fluidConnections, allowedFluids);
	}
	
	public TileFluidSidedInventory(String name, int size, List<Integer> capacity, List<Integer> maxTransfer, List<FluidConnection> fluidConnections, List<List<String>> allowedFluids) {
		super(name, size, capacity, maxTransfer, fluidConnections, allowedFluids);
	}
	
	public TileFluidSidedInventory(String name, int size, int capacity, int maxReceive, int maxExtract, FluidConnection fluidConnections, List<String> allowedFluids) {
		super(name, size, capacity, maxReceive, maxExtract, fluidConnections, allowedFluids);
	}
	
	public TileFluidSidedInventory(String name, int size, List<Integer> capacity, List<Integer> maxReceive, List<Integer> maxExtract, List<FluidConnection> fluidConnections, List<List<String>> allowedFluids) {
		super(name, size, capacity, maxReceive, maxExtract, fluidConnections, allowedFluids);
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
