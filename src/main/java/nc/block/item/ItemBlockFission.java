package nc.block.item;

import net.minecraft.block.Block;
import net.minecraft.util.text.translation.I18n;

public class ItemBlockFission extends ItemBlockMeta {
	
	public ItemBlockFission(Block block) {
		super(block, INFO());
	}
	
	public final static String[][] INFO() {
		String[] infoCasing = new String[17];
		for (int i = 0; i < 17; i++) {
			infoCasing[i] = I18n.translateToLocalFormatted("tile.fission_block.casing.des" + i);
		}
		String[] infoBlast = new String[2];
		for (int i = 0; i < 2; i++) {
			infoBlast[i] = I18n.translateToLocalFormatted("tile.fission_block.blast.des" + i);
		}
		return new String[][] {infoCasing, infoBlast};
	}
}
