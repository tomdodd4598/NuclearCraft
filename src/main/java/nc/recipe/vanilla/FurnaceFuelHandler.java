package nc.recipe.vanilla;

import nc.init.*;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;

public class FurnaceFuelHandler implements IFuelHandler {
	
	@Override
	public int getBurnTime(ItemStack fuel) {
		if (fuel.isItemEqual(new ItemStack(NCItems.ingot, 1, 8))) {
			return 1600;
		}
		else if (fuel.isItemEqual(new ItemStack(NCItems.dust, 1, 8))) {
			return 1600;
		}
		else if (fuel.isItemEqual(new ItemStack(NCBlocks.ingot_block, 1, 8))) {
			return 16000;
		}
		else if (fuel.isItemEqual(new ItemStack(NCItems.gem_dust, 1, 7))) {
			return 1600;
		}
		else if (fuel.isItemEqual(new ItemStack(Items.REEDS))) {
			return 200;
		}
		else if (fuel.isItemEqual(new ItemStack(Items.SUGAR))) {
			return 200;
		}
		return 0;
	}
}
