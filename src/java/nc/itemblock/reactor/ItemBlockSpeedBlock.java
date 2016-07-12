package nc.itemblock.reactor;

import nc.itemblock.ItemBlockNC;
import net.minecraft.block.Block;

public class ItemBlockSpeedBlock extends ItemBlockNC {

	public ItemBlockSpeedBlock(Block block) {
		super(block, "Causes fuel cells to deplete faster in", "Fission Reactors. The increase in the rate of", "depletion is proportional to the standard", "rate of the depletion of fuel in the reactor", "with no Speed Blocks installed.");
	}
}
