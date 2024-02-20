package nc.tile.inventory;

import javax.annotation.Nonnull;

import nc.tile.ITileFiltered;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public interface ITileFilteredInventory extends ITileFiltered, ITileInventory {
	
	@Nonnull NonNullList<ItemStack> getInventoryStacksInternal();
	
	@Nonnull NonNullList<ItemStack> getFilterStacks();
	
	boolean isItemValidForSlotInternal(int slot, ItemStack stack);
}
