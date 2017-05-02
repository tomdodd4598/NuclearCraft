package nc.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public interface ITileInventory {
	public NonNullList<ItemStack> getInventoryStacks();
}
