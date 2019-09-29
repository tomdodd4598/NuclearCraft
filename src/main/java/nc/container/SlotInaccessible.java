package nc.container;

import nc.tile.inventory.ITileInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotInaccessible extends Slot {
	
	public SlotInaccessible(ITileInventory tile, int slotIndex, int xPosition, int yPosition) {
		this(tile.getInventory(), slotIndex, xPosition, yPosition);
	}
	
	public SlotInaccessible(IInventory inv, int slotIndex, int xPosition, int yPosition) {
		super(inv, slotIndex, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}
	
	@Override
	public boolean canTakeStack(EntityPlayer player) {
		return false;
	}
}
