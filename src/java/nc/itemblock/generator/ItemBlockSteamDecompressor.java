package nc.itemblock.generator;

import nc.itemblock.ItemBlockNC;
import net.minecraft.block.Block;

public class ItemBlockSteamDecompressor extends ItemBlockNC {

	public ItemBlockSteamDecompressor(Block block) {
		super(block, "Takes in dense steam and decompresses it to", "form steam at a maximum rate of 2 mB/t of", "dense steam to 2000 mB/t of steam.");
	}
}