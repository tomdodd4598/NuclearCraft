package nc.tab;

import nc.init.NCBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class TabBaseBlockMaterials extends CreativeTabs {

	public TabBaseBlockMaterials() {
		super("nuclearcraftBaseBlockMaterials");
	}

	public Item getTabIconItem() {
		return Item.getItemFromBlock(NCBlocks.ore);
	}
}
