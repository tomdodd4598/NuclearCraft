package nc.container.slot;

import nc.tile.processor.TileNuclearFurnace;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class SlotNuclearFuel extends Slot {
	
	public SlotNuclearFuel(IInventory tile, int index, int xPosition, int yPosition) {
		super(tile, index, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return TileNuclearFurnace.isItemFuel(stack);
	}
}
