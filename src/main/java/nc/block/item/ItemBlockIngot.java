package nc.block.item;

import net.minecraft.block.Block;
import net.minecraft.util.text.translation.I18n;

public class ItemBlockIngot extends ItemBlockMeta {
	
	public final static String[][] INFO = {{}, {}, {}, {}, {}, {}, {}, {}, {I18n.translateToLocalFormatted("tile.ingot_block.graphite.des0"), I18n.translateToLocalFormatted("tile.ingot_block.graphite.des1")}};

	public ItemBlockIngot(Block block) {
		super(block, INFO);
	}
}
