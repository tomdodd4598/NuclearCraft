package nc.tab;

import nc.init.NCItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class NCTab extends CreativeTabs {

	public NCTab() {
		super("nuclearcraft");
	}

	public ItemStack getTabIconItem() {
		return new ItemStack(NCItems.fuel_rod_uranium);
	}
}
