package nc.tab;

import nc.init.NCFluids;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class TabFluids extends CreativeTabs {

	public TabFluids() {
		super("nuclearcraftFluids");
	}

	public Item getTabIconItem() {
		return Item.getItemFromBlock(NCFluids.block_liquidhelium);
	}
}
