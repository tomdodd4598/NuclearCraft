package nc.recipe.vanilla;

import nc.init.NCBlocks;
import nc.init.NCItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;

public class FurnaceFuelHandler implements IFuelHandler {
	
	public int getBurnTime(ItemStack fuel) {
		if (fuel.getItem() == NCItems.ingot && fuel.getItemDamage() == 8) return 1600;
		if (fuel.getItem() == NCItems.dust && fuel.getItemDamage() == 8) return 1600;
		if (fuel.getItem() == Item.getItemFromBlock(NCBlocks.ingot_block) && fuel.getItemDamage() == 8) return 16000;
		
		return 0;
	}
	
}
