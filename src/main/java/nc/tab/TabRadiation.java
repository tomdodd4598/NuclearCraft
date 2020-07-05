package nc.tab;

import nc.init.NCItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabRadiation extends CreativeTabs {
	
	public TabRadiation() {
		super("nuclearcraft.radiation");
	}
	
	@Override
	public ItemStack createIcon() {
		return new ItemStack(NCItems.geiger_counter);
	}
}
