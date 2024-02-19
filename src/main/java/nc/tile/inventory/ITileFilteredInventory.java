package nc.tile.inventory;

import javax.annotation.Nonnull;

import nc.tile.ITileFiltered;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public interface ITileFilteredInventory extends ITileFiltered, ITileInventory {
	
	public @Nonnull NonNullList<ItemStack> getInventoryStacksInternal();
	
	public @Nonnull NonNullList<ItemStack> getFilterStacks();
	
	public boolean isItemValidForSlotInternal(int slot, ItemStack stack);
}
