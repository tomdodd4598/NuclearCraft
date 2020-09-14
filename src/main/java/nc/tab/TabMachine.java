package nc.tab;

import static nc.config.NCConfig.register_processor;

import nc.init.NCBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabMachine extends CreativeTabs {
	
	public TabMachine() {
		super("nuclearcraft.machine");
	}
	
	@Override
	public ItemStack createIcon() {
		return new ItemStack(register_processor[1] ? NCBlocks.manufactory : NCBlocks.machine_interface);
	}
}
