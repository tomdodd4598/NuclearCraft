package nc.tab;

import nc.init.NCItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabFissionBlocks extends CreativeTabs {

	public TabFissionBlocks() {
		super("nuclearcraftFissionBlocks");
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(NCItems.part, 1, 0);
	}
}
