package nc.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class NCContainer extends Container {
	
	public NCContainer() {}

	public boolean canInteractWith(EntityPlayer playerIn) {
		return false;
	}
	
	protected int upcast(int input) {
		if (input < 0) input += 65536;
		return input;
	}
}
