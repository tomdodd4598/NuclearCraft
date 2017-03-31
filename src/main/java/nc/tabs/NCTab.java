package nc.tabs;

import nc.init.NCItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class NCTab extends CreativeTabs {

	public NCTab() {
		super("nuclearcraft");
	}

	public Item getTabIconItem() {
		return NCItems.fuel_rod_uranium;
	}
}
