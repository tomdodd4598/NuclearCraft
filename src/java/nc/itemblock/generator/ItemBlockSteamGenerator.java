package nc.itemblock.generator;

import nc.itemblock.ItemBlockNC;
import net.minecraft.block.Block;

public class ItemBlockSteamGenerator extends ItemBlockNC {

	public ItemBlockSteamGenerator(Block block) {
		super(block, "Generates RF from steam at a maximum", "rate of 2000 mB/t to 4000 RF/t", "(2 RF per 1 mB of Steam)");
	}
}