package nc.tab;

import nc.init.NCBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabMachines extends CreativeTabs {

	public TabMachines() {
		super("nuclearcraftMachines");
	}

	public ItemStack getTabIconItem() {
		return new ItemStack(NCBlocks.manufactory_active);
	}
}
