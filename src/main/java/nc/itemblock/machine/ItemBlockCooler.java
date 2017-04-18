package nc.itemblock.machine;

import nc.itemblock.ItemBlockNC;
import net.minecraft.block.Block;

public class ItemBlockCooler extends ItemBlockNC {

	public ItemBlockCooler(Block block) {
		super(block, "Uses RF to cool Helium-4 drastically to temperatures of", "around 3 Kelvin above absolute zero, liquifying it.", "Can accept speed and efficiency upgrades.");
	}
}
