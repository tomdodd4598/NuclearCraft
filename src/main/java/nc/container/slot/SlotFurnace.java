package nc.container.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;

public class SlotFurnace extends SlotFurnaceOutput {
	
	public SlotFurnace(EntityPlayer player, IInventory tile, int index, int xPosition, int yPosition) {
		super(player, tile, index, xPosition, yPosition);
	}
}
