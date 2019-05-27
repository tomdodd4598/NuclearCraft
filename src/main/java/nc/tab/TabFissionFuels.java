package nc.tab;

import nc.init.NCItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabFissionFuels extends CreativeTabs {

	public TabFissionFuels() {
		super("nuclearcraftFissionFuels");
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(NCItems.fuel_uranium);
	}
}
