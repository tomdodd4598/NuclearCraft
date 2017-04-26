package nc.tab;

import nc.init.NCBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class TabMachines extends CreativeTabs {

	public TabMachines() {
		super("nuclearcraftMachines");
	}

	public Item getTabIconItem() {
		return Item.getItemFromBlock(NCBlocks.manufactory_active);
	}
}
