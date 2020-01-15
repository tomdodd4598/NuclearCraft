package nc.tile.inventory;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public interface ITileFilteredInventory extends ITileInventory {
	
	public @Nonnull NonNullList<ItemStack> getFilterStacks();
	
	public void onFilterChanged(int slot);
}
