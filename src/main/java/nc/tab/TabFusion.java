package nc.tab;

import nc.init.NCItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabFusion extends CreativeTabs {

	public TabFusion() {
		super("nuclearcraftFusion");
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(NCItems.part, 1, 4);
	}
}
