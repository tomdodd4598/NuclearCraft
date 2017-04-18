package nc.itemblock.machine;

import nc.itemblock.ItemBlockNC;
import net.minecraft.block.Block;

public class ItemBlockFactory extends ItemBlockNC {

	public ItemBlockFactory(Block block) {
		super(block, "A useful machine that uses RF to create machine", "parts and efficiently process ores.", "Can accept speed and efficiency upgrades.");
	}
}
