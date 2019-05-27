package nc.tab;

import nc.init.NCBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabHeatExchangerBlocks extends CreativeTabs {

	public TabHeatExchangerBlocks() {
		super("nuclearcraftHeatExchangerBlocks");
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(NCBlocks.salt_fission_controller, 1);
	}
}
