package nc.container;

import nc.tile.processor.TileNuclearFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotNuclearFuel extends Slot {
	
	public SlotNuclearFuel(IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
		super(inventoryIn, slotIndex, xPosition, yPosition);
	}
	
	public boolean isItemValid(ItemStack stack) {
		return TileNuclearFurnace.isItemFuel(stack);
	}
}
