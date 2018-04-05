package nc.integration.crafttweaker;

import java.util.List;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import nc.util.NCUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CTMethods {
	
	public static ItemStack getItemStack(IItemStack item) {
		if(item == null) return ItemStack.EMPTY;
		
		Object internal = item.getInternal();
		if(internal == null || !(internal instanceof ItemStack)) {
			CraftTweakerAPI.logError("Not a valid item stack: " + item);
		}
		return ((ItemStack) internal).copy();
	}
	
	public static ItemStack getItemStack(IIngredient ingredient) {
		if(ingredient == null) return ItemStack.EMPTY;
		
		List<IItemStack> items = ingredient.getItems();
		if(items.size() != 1) {
			NCUtil.getLogger().error("Not an ingredient with a single item: " + ingredient);
		}
		return getItemStack(items.get(0));
	}
	
	public static FluidStack getLiquidStack(ILiquidStack stack) {
		if(stack == null) return null;
		return (FluidStack) stack.getInternal();
	}
}
