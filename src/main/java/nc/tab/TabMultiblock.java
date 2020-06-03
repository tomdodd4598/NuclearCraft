package nc.tab;

import nc.init.NCBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabMultiblock extends CreativeTabs {
	
	public TabMultiblock() {
		super("nuclearcraft.multiblock");
	}
	
	@Override
	public ItemStack createIcon() {
		return new ItemStack(NCBlocks.solid_fission_controller);
	}
}
