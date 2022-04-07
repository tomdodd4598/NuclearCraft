package nc.multiblock.fission.tile.port.internal;

import nc.tile.internal.inventory.ItemHandler;
import nc.tile.inventory.ITileInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class PortItemHandler<T extends ITileInventory> extends ItemHandler<T> {
	
	public PortItemHandler(T tile, EnumFacing side) {
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
