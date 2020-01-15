package nc.container.slot;

import nc.tile.inventory.ITileInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.SlotFurnaceOutput;

public class SlotFurnace extends SlotFurnaceOutput {
	
	public SlotFurnace(EntityPlayer player, ITileInventory tile, int index, int xPosition, int yPosition) {
		super(player, tile.getInventory(), index, xPosition, yPosition);
	}
}
