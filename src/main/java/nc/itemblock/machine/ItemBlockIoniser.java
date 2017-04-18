package nc.itemblock.machine;

import nc.itemblock.ItemBlockNC;
import net.minecraft.block.Block;

public class ItemBlockIoniser extends ItemBlockNC {

	public ItemBlockIoniser(Block block) {
		super(block, "Uses RF to ionise atoms, producing ions,", "and can accept speed and efficiency upgrades.");
	}
}
