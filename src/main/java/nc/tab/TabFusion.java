package nc.tab;

import nc.init.NCBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class TabFusion extends CreativeTabs {

	public TabFusion() {
		super("nuclearcraftFusion");
	}

	public Item getTabIconItem() {
		return Item.getItemFromBlock(NCBlocks.fusion_core);
	}
}
