package nc.container.slot;

import nc.tile.inventory.ITileInventory;
import nc.tile.processor.TileNuclearFurnace;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotNuclearFuel extends Slot {
	
	public SlotNuclearFuel(ITileInventory tile, int index, int xPosition, int yPosition) {
		super(tile.getInventory(), index, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return TileNuclearFurnace.isItemFuel(stack);
	}
}
