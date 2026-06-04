package nc.recipe.vanilla;

import nc.init.*;
import nc.util.Lazy;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;

import java.util.*;

public class FurnaceFuelHandler implements IFuelHandler {
	
	static class FuelPair {
		
		final ItemStack stack;
		final int burnTime;
		
		FuelPair(ItemStack stack, int burnTime) {
			this.stack = stack;
			this.burnTime = burnTime;
		}
	}
	
	Lazy<List<FuelPair>> fuelPairs = new Lazy<>(() -> Arrays.asList(new FuelPair(new ItemStack(NCItems.ingot, 1, 8), 1600), new FuelPair(new ItemStack(NCItems.dust, 1, 8), 1600), new FuelPair(new ItemStack(NCBlocks.ingot_block, 1, 8), 16000), new FuelPair(new ItemStack(NCItems.gem_dust, 1, 7), 1600), new FuelPair(new ItemStack(Items.REEDS), 200), new FuelPair(new ItemStack(Items.SUGAR), 200)));
	
	@Override
	public int getBurnTime(ItemStack fuel) {
		for (FuelPair pair : fuelPairs.get()) {
			if (fuel.isItemEqual(pair.stack)) {
				return pair.burnTime;
			}
		}
		return 0;
	}
}
