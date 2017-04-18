package nc.itemblock;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;

public class ItemBlockMeta extends ItemBlockWithMetadata {

	public ItemBlockMeta(Block block) {
		super(block, block);
	}
}
