package nc.container;

import nc.tile.inventory.ITileInventory;
import nc.tile.processor.TileNuclearFurnace;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotNuclearFuel extends Slot {
	
	public SlotNuclearFuel(ITileInventory tile, int slotIndex, int xPosition, int yPosition) {
		super(tile.getInventory(), slotIndex, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return TileNuclearFurnace.isItemFuel(stack);
	}
}
