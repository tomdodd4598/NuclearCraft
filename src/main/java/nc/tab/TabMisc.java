package nc.tab;

import nc.config.NCConfig;
import nc.init.NCItems;
import nc.init.NCTools;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabMisc extends CreativeTabs {

	public TabMisc() {
		super("nuclearcraftMisc");
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(NCConfig.register_tool[1] ? NCTools.spaxelhoe_tough : NCItems.portable_ender_chest);
	}
}
