package nc.tab;

import nc.init.NCItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class TabBaseItemMaterials extends CreativeTabs {

	public TabBaseItemMaterials() {
		super("nuclearcraftBaseItemMaterials");
	}

	public Item getTabIconItem() {
		return NCItems.alloy;
	}
}
