package nc.tab;

import nc.init.NCBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabTurbineBlocks extends CreativeTabs {

	public TabTurbineBlocks() {
		super("nuclearcraftTurbineBlocks");
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(NCBlocks.turbine_controller, 1);
	}
}
