package nc.tab;

import nc.init.NCItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabMaterial extends CreativeTabs {
	
	public TabMaterial() {
		super("nuclearcraft.material");
	}
	
	@Override
	public ItemStack createIcon() {
		return new ItemStack(NCItems.alloy, 1, 10);
	}
}
