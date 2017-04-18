package nc.tab;

import nc.init.NCItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class NCTab extends CreativeTabs {

	public NCTab() {
		super("nuclearcraft");
	}

	public Item getTabIconItem() {
		return NCItems.fuel_rod_uranium;
	}
}
