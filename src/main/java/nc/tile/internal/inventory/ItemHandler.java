package nc.tile.internal.inventory;

import javax.annotation.Nonnull;

import nc.tile.inventory.ITileInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.*;

public class ItemHandler<T extends ITileInventory> implements IItemHandlerModifiable {
	
	protected final T tile;
	protected final EnumFacing side;
	
	public ItemHandler(T tile, EnumFacing side) {
		this.tile = tile;
		this.side = side;
	}
	
	@Override
	public int getSlots() {
		return side == null ? tile.getSizeInventory() : tile.getSlotsForFace(side).length;
	}
	
	@Override
	public @Nonnull ItemStack getStackInSlot(int slot) {
		int i = getSlot(slot);
		return i == -1 ? ItemStack.EMPTY : tile.getStackInSlot(i);
	}
	
	@Override
	public @Nonnull ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		if (stack.isEmpty()) {
			return ItemStack.EMPTY;
		}
		
		int slot1 = getSlot(slot);
		if (slot1 == -1) {
			return stack;
		}
		
		ItemStack stackInSlot = tile.getStackInSlot(slot1);
		
		int m;
		if (!stackInSlot.isEmpty()) {
			if (stackInSlot.getCount() >= getSlotStackLimit(stackInSlot, slot)) {
				return stack;
			}
			
			if (!ItemHandlerHelper.canItemStacksStack(stack, stackInSlot)) {
				return stack;
			}
			
			if (side != null && !tile.canInsertItem(slot1, stack, side) || !tile.isItemValidForSlot(slot1, stack)) {
				return stack;
			}
			
			m = getStackSplitSize(stack, stackInSlot.getCount(), slot);
			
			if (stack.getCount() <= m) {
				if (!simulate) {
					ItemStack copy = stack.copy();
					copy.grow(stackInSlot.getCount());
					setInventorySlotContents(slot1, copy);
				}
				return ItemStack.EMPTY;
			}
			else {
				// copy the stack to not modify the original one
				stack = stack.copy();
				if (!simulate) {
					ItemStack copy = stack.splitStack(m);
					copy.grow(stackInSlot.getCount());
					setInventorySlotContents(slot1, copy);
					return stack;
				}
				else {
					stack.shrink(m);
					return stack;
				}
			}
		}
		else {
			if (side != null && !tile.canInsertItem(slot1, stack, side) || !tile.isItemValidForSlot(slot1, stack)) {
				return stack;
			}
			
			m = getStackSplitSize(stack, 0, slot);
			
			if (m < stack.getCount()) {
				// copy the stack to not modify the original one
				stack = stack.copy();
				if (!simulate) {
					setInventorySlotContents(slot1, stack.splitStack(m));
					return stack;
				}
				else {
					stack.shrink(m);
					return stack;
				}
			}
			else {
				if (!simulate) {
					setInventorySlotContents(slot1, stack);
				}
				return ItemStack.EMPTY;
			}
		}
	}
	
	protected int getStackSplitSize(ItemStack stack, int slotStackCount, int slot) {
		return Math.min(stack.getMaxStackSize(), getSlotLimit(slot)) - slotStackCount;
	}
	
	protected int getSlotStackLimit(ItemStack stackInSlot, int slot) {
		return Math.min(stackInSlot.getMaxStackSize(), getSlotLimit(slot));
	}
	
	@Override
	public @Nonnull ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (amount == 0) {
			return ItemStack.EMPTY;
		}
		
		int slot1 = getSlot(slot);
		if (slot1 == -1) {
			return ItemStack.EMPTY;
		}
		
		ItemStack stackInSlot = tile.getStackInSlot(slot1);
		
		if (stackInSlot.isEmpty()) {
			return ItemStack.EMPTY;
		}
		
		if (side != null && !tile.canExtractItem(slot1, stackInSlot, side)) {
			return ItemStack.EMPTY;
		}
		
		if (simulate) {
			if (stackInSlot.getCount() < amount) {
				return stackInSlot.copy();
			}
			else {
				ItemStack copy = stackInSlot.copy();
				copy.setCount(amount);
				return copy;
			}
		}
		else {
			int m = Math.min(stackInSlot.getCount(), amount);
			ItemStack ret = tile.decrStackSize(slot1, m);
			tile.markDirty();
			return ret;
		}
	}
	
	@Override
	public int getSlotLimit(int slot) {
		return tile.getInventoryStackLimit();
	}
	
	@Override
	public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
		int i = getSlot(slot);
		if (i != -1) {
			setInventorySlotContents(i, stack);
		}
	}
	
	protected void setInventorySlotContents(int slot, ItemStack stack) {
		// Notify vanilla of updates, We change the handler to be responsible for this instead of the caller (mimic vanilla behavior)
		tile.markDirty();
		tile.setInventorySlotContents(slot, stack);
	}
	
	/** Returns -1 if slot number is out of bounds */
	public int getSlot(int slot) {
		return side == null ? slot < getSlots() ? slot : -1 : slot < getSlots() ? tile.getSlotsForFace(side)[slot] : -1;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof ItemHandler) {
			ItemHandler<?> handler = (ItemHandler<?>) obj;
			return tile.equals(handler.tile) && side == handler.side;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return 31 * tile.hashCode() + (side == null ? 0 : side.hashCode());
	}
}
