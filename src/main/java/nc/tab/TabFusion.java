package nc.tab;

import nc.init.NCBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabFusion extends CreativeTabs {

	public TabFusion() {
		super("nuclearcraftFusion");
	}

	public ItemStack getTabIconItem() {
		return new ItemStack(NCBlocks.fusion_core);
	}
}
