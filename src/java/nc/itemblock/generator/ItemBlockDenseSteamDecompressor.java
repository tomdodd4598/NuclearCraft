package nc.itemblock.generator;

import nc.itemblock.ItemBlockNC;
import net.minecraft.block.Block;

public class ItemBlockDenseSteamDecompressor extends ItemBlockNC {

	public ItemBlockDenseSteamDecompressor(Block block) {
		super(block, "Takes in superdense steam and decompresses it to", "form dense steam at a maximum rate of 2 mB/t of", "superdense steam to 2000 mB/t of dense steam.");
	}
}