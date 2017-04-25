package nc.tab;

import nc.init.NCBlocks;
import nc.init.NCItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabFissionBlocks extends CreativeTabs {

	public TabFissionBlocks() {
		super("nuclearcraftFissionBlocks");
	}

	public ItemStack getTabIconItem() {
		return new ItemStack(NCBlocks.fission_controller_active);
	}
}
