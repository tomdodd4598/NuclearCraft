package nc.tile.fluid;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import nc.tile.internal.fluid.FluidConnection;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public abstract class TileFluidSidedInventory extends TileFluidInventory implements ISidedInventory {
	
	public int[] topSlots;
	public int[] sideSlots;
	public int[] bottomSlots;
	
	public TileFluidSidedInventory(String name, int size, int capacity, List<String> allowedFluidsList, @Nonnull FluidConnection[] fluidConnections) {
		super(name, size, capacity, allowedFluidsList, fluidConnections);
	}
	
	public TileFluidSidedInventory(String name, int size, @Nonnull List<Integer> capacity, List<List<String>> allowedFluidsLists, @Nonnull FluidConnection[] fluidConnections) {
		super(name, size, capacity, allowedFluidsLists, fluidConnections);
	}
	
	public TileFluidSidedInventory(String name, int size, int capacity, int maxTransfer, List<String> allowedFluidsList, @Nonnull FluidConnection[] fluidConnections) {
		super(name, size, capacity, maxTransfer, allowedFluidsList, fluidConnections);
	}
	
	public TileFluidSidedInventory(String name, int size, @Nonnull List<Integer> capacity, @Nonnull List<Integer> maxTransfer, List<List<String>> allowedFluidsLists, @Nonnull FluidConnection[] fluidConnections) {
		super(name, size, capacity, maxTransfer, allowedFluidsLists, fluidConnections);
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
