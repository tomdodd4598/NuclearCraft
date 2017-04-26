package nc.tab;

import nc.init.NCBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class TabFissionBlocks extends CreativeTabs {

	public TabFissionBlocks() {
		super("nuclearcraftFissionBlocks");
	}

	public Item getTabIconItem() {
		return Item.getItemFromBlock(NCBlocks.fission_controller_active);
	}
}
