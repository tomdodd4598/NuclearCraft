package nc.tab;

import nc.init.NCFluids;
import nc.init.NCItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabFluids extends CreativeTabs {

	public TabFluids() {
		super("nuclearcraftFluids");
	}

	public ItemStack getTabIconItem() {
		return new ItemStack(NCFluids.block_oxygen);
	}
}
