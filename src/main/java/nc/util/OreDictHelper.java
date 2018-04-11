package nc.util;

import java.util.ArrayList;
import java.util.List;

import nc.recipe.StackType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictHelper {
	
	public static boolean exists(String ore, StackType type) {
		if (!type.isFluid()) if (!OreDictionary.getOres(ore).isEmpty()) return true;
		if (!type.isItem()) if (FluidRegistry.getRegisteredFluids().keySet().contains(ore.toLowerCase())) return true;
		return false;
	}
	
	public static boolean oreExists(String ore) {
		return !OreDictionary.getOres(ore).isEmpty();
	}
	
	public static boolean fluidExists(String name) {
		return FluidRegistry.getRegisteredFluids().keySet().contains(name.toLowerCase());
	}
	
	public static String getOreNameFromStacks(ArrayList<ItemStack> stackList) {
		List<Integer> idList = new ArrayList<Integer>();
		if (stackList.isEmpty() || stackList == null) return "Unknown";
		idList.addAll(ArrayHelper.asList(OreDictionary.getOreIDs(stackList.get(0))));
		
		for (ItemStack stack : stackList) {
			if (stack.isEmpty() || stack == null) return "Unknown";
			idList = ArrayHelper.intersect(idList, ArrayHelper.asList(OreDictionary.getOreIDs(stack)));
			if (idList.isEmpty()) return "Unknown";
		}
		return OreDictionary.getOreName(idList.get(0));
	}
}
