package nc.tab;

import nc.init.NCItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabFissionFuelRods extends CreativeTabs {

	public TabFissionFuelRods() {
		super("nuclearcraftFissionFuelRods");
	}

	public ItemStack getTabIconItem() {
		return new ItemStack(NCItems.fuel_rod_uranium);
	}
}
