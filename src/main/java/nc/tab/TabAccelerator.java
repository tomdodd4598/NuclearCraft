package nc.tab;

import nc.init.NCBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class TabAccelerator extends CreativeTabs {

	public TabAccelerator() {
		super("nuclearcraftAccelerator");
	}

	public Item getTabIconItem() {
		return Item.getItemFromBlock(NCBlocks.accelerator_electromagnet_active);
	}
}
