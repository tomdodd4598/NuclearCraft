package nc.tab;

import nc.init.NCItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabMisc extends CreativeTabs {
	
	public TabMisc() {
		super("nuclearcraft.misc");
	}
	
	@Override
	public ItemStack createIcon() {
		return new ItemStack(NCItems.smore);
	}
}
