package nc.tab;

import nc.init.NCItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabBaseItemMaterials extends CreativeTabs {

	public TabBaseItemMaterials() {
		super("nuclearcraftBaseItemMaterials");
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(NCItems.alloy, 1, 1);
	}
}
