package nc.tile.inventory;

import javax.annotation.Nonnull;

import nc.tile.ITileFiltered;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public interface ITileFilteredInventory extends ITileFiltered, ITileInventory {
	
	public @Nonnull NonNullList<ItemStack> getInventoryStacksInternal();
	
	public boolean isItemValidForSlotInternal(int slot, ItemStack stack);
	
	public @Nonnull NonNullList<ItemStack> getFilterStacks();
	
	//public ItemStack getFilterStack(int slot);
	
	//public ItemStack setFilterStack(int slot, ItemStack stack);
}
