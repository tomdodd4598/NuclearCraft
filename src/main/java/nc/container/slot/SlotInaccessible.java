package nc.container.slot;

import nc.tile.inventory.ITileInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotInaccessible extends Slot {
	
	public SlotInaccessible(ITileInventory tile, int index, int xPosition, int yPosition) {
		this(tile.getInventory(), index, xPosition, yPosition);
	}
	
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
