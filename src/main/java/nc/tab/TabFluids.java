package nc.tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

public class TabFluids extends CreativeTabs {

	public TabFluids() {
		super("nuclearcraftFluids");
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(FluidRegistry.getFluid("flibe").getBlock());
	}
}
