package nc.tab;

import nc.config.NCConfig;
import nc.init.NCBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabMachines extends CreativeTabs {

	public TabMachines() {
		super("nuclearcraftMachines");
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(NCConfig.register_processor[1] ? NCBlocks.manufactory_active : NCBlocks.machine_interface);
	}
}
