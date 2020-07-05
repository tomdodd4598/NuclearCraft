package nc.container.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class SlotInaccessible extends Slot {
	
	public SlotInaccessible(IInventory inv, int index, int xPosition, int yPosition) {
		super(inv, index, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}
	
	@Override
	public boolean canTakeStack(EntityPlayer player) {
		return false;
	}
	
	@Override
	public void putStack(ItemStack stack) {
		return;
	}
}
