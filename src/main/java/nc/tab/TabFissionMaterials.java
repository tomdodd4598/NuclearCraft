package nc.tab;

import nc.init.NCItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class TabFissionMaterials extends CreativeTabs {

	public TabFissionMaterials() {
		super("nuclearcraftFissionMaterials");
	}

	public Item getTabIconItem() {
		return NCItems.uranium;
	}
}
