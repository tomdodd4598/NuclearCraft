package nc.tab;

import nc.init.NCItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabFissionMaterials extends CreativeTabs {

	public TabFissionMaterials() {
		super("nuclearcraftFissionMaterials");
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(NCItems.uranium, 1, 4);
	}
}
