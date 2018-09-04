package nc.tab;

import nc.init.NCBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabCondenserBlocks extends CreativeTabs {

	public TabCondenserBlocks() {
		super("nuclearcraftCondenserBlocks");
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(NCBlocks.salt_fission_controller, 1);
	}
}
