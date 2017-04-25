package nc.tab;

import nc.init.NCBlocks;
import nc.init.NCItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabBaseBlockMaterials extends CreativeTabs {

	public TabBaseBlockMaterials() {
		super("nuclearcraftBaseBlockMaterials");
	}

	public ItemStack getTabIconItem() {
		return new ItemStack(NCBlocks.ore, 1, 2);
	}
}
