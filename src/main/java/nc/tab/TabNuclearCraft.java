package nc.tab;

import nc.init.NCBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabNuclearCraft extends CreativeTabs {

	public TabNuclearCraft() {
		super("nuclearcraft");
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(NCBlocks.fission_controller_new_active);
	}
}
