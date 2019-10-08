package nc.tab;

import nc.config.NCConfig;
import nc.init.NCBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabMachine extends CreativeTabs {

	public TabMachine() {
		super("nuclearcraft.machine");
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(NCConfig.register_processor[1] ? NCBlocks.manufactory : NCBlocks.machine_interface);
	}
}
