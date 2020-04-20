package nc.tile.inventory;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public interface ITileFilteredInventory extends ITileInventory {
	
	public @Nonnull NonNullList<ItemStack> getInventoryStacksInternal();
	
	public boolean isItemValidForSlotInternal(int slot, ItemStack stack);
	
	public @Nonnull NonNullList<ItemStack> getFilterStacks();
	
	//public ItemStack getFilterStack(int slot);
	
	//public ItemStack setFilterStack(int slot, ItemStack stack);
	
	public boolean canModifyFilter(int slot);
	
	public void onFilterChanged(int slot);
	
	public int getFilterID();
}
