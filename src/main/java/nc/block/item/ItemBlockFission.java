package nc.block.item;

import net.minecraft.block.Block;
import net.minecraft.util.text.translation.I18n;

public class ItemBlockFission extends ItemBlockMeta {
	
	public final static String[][] INFO = {{I18n.translateToLocalFormatted("tile.fission_block.casing.des0"), I18n.translateToLocalFormatted("tile.fission_block.casing.des1")}, {I18n.translateToLocalFormatted("tile.fission_block.blast.des0"), I18n.translateToLocalFormatted("tile.fission_block.blast.des1")}};
	
	public ItemBlockFission(Block block) {
		super(block, INFO);
	}
}
