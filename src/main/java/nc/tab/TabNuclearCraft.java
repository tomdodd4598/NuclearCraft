package nc.tab;

import nc.init.NCItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabNuclearCraft extends CreativeTabs {
	
	public TabNuclearCraft() {
		super("nuclearcraft.main");
	}
	
	@Override
	public ItemStack createIcon() {
		return new ItemStack(NCItems.fuel_uranium, 1, 10);
	}
}
