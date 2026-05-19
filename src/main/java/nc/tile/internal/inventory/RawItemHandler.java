package nc.tile.internal.inventory;

import nc.tile.inventory.ITileInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class RawItemHandler<T extends ITileInventory> extends ItemHandler<T> {
	
	public RawItemHandler(T tile, EnumFacing side) {
		super(tile, side);
	}
	
	@Override
	protected int getStackSplitSize(ItemStack stack, int slotStackCount, int slot) {
		return getSlotLimit(slot) - slotStackCount;
	}
	
	@Override
	protected int getSlotStackLimit(ItemStack stackInSlot, int slot) {
		return getSlotLimit(slot);
	}
}
