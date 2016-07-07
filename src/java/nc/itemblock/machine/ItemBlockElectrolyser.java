package nc.itemblock.machine;

import nc.itemblock.ItemBlockNC;
import net.minecraft.block.Block;

public class ItemBlockElectrolyser extends ItemBlockNC {

	public ItemBlockElectrolyser(Block block) {
		super(block, "Uses RF to separate water into Oxygen, Hydrogen and Deuterium,", "and can accept speed and efficiency upgrades.");
	}
}
