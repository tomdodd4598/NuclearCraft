package nc.tab;

import nc.init.NCTools;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabMisc extends CreativeTabs {

	public TabMisc() {
		super("nuclearcraftMisc");
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(NCTools.spaxelhoe_tough);
	}
}
