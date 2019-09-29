package nc.container;

import nc.tile.inventory.ITileInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotFurnaceOutput;

public class SlotFurnace extends SlotFurnaceOutput {
	
	public SlotFurnace(EntityPlayer player, ITileInventory tile, int slotIndex, int xPosition, int yPosition) {
		this(player, tile.getInventory(), slotIndex, xPosition, yPosition);
	}
	
	public SlotFurnace(EntityPlayer player, IInventory inv, int slotIndex, int xPosition, int yPosition) {
		super(player, inv, slotIndex, xPosition, yPosition);
	}
}
