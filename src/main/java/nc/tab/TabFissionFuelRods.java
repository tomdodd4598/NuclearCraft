package nc.tab;

import nc.init.NCItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class TabFissionFuelRods extends CreativeTabs {

	public TabFissionFuelRods() {
		super("nuclearcraftFissionFuelRods");
	}

	public Item getTabIconItem() {
		return NCItems.fuel_rod_uranium;
	}
}
