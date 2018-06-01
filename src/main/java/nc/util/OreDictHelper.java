package nc.util;

import java.util.ArrayList;
import java.util.List;

import nc.config.NCConfig;
import nc.recipe.StackType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
		idList.addAll(ArrayHelper.asIntegerList(OreDictionary.getOreIDs(stackList.get(0))));
		
		for (ItemStack stack : stackList) {
			if (stack.isEmpty() || stack == null) return "Unknown";
			idList = ArrayHelper.intersect(idList, ArrayHelper.asIntegerList(OreDictionary.getOreIDs(stack)));
			if (idList.isEmpty()) return "Unknown";
		}
		return OreDictionary.getOreName(idList.get(0));
	}
	
	public static boolean getBlockMatchesOre(World world, BlockPos pos, String... names) {
		List<ItemStack> stackList = new ArrayList<ItemStack>();
		for (int i = 0; i < names.length; i++) {
			List<ItemStack> stacks = OreDictionary.getOres(names[i]);
			stackList.addAll(stacks);
		}
		ItemStack stack = ItemStackHelper.blockStateToStack(world.getBlockState(pos));
		for (ItemStack oreStack : stackList) if (oreStack.isItemEqual(stack)) return true;
		return false;
	}
	
	public static ArrayList<ItemStack> getPrioritisedStackList(String ore) {
		ArrayList<ItemStack> defaultStackList = new ArrayList<ItemStack>(OreDictionary.getOres(ore));
		if (!NCConfig.ore_dict_priority_bool || NCConfig.ore_dict_priority.length < 1) return defaultStackList;
		ArrayList<ItemStack> prioritisedStackList = new ArrayList<ItemStack>();
		for (int i = 0; i < NCConfig.ore_dict_priority.length; i++) {
			for (ItemStack stack : defaultStackList) {
				if (RegistryHelper.getModID(stack).equals(NCConfig.ore_dict_priority[i]) && !prioritisedStackList.contains(stack)) {
					prioritisedStackList.add(stack);
				}
			}
		}
		if (prioritisedStackList.isEmpty()) return defaultStackList;
		for (ItemStack stack : defaultStackList) {
			if (!prioritisedStackList.contains(stack)) {
				prioritisedStackList.add(stack);
			}
		}
		return prioritisedStackList;
	}
}
