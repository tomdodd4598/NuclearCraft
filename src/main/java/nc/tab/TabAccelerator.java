package nc.tab;

import nc.init.NCBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabAccelerator extends CreativeTabs {

	public TabAccelerator() {
		super("nuclearcraftAccelerator");
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(NCBlocks.accelerator_electromagnet_active);
	}
}
