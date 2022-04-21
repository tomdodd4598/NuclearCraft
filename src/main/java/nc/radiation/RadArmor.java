package nc.radiation;

import static nc.config.NCConfig.*;

import it.unimi.dsi.fastutil.ints.*;
import nc.init.NCItems;
import nc.recipe.vanilla.CraftingRecipeHandler;
import nc.util.*;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.ItemStack;

public class RadArmor {
	
	public static final IntSet ARMOR_STACK_SHIELDING_BLACKLIST = new IntOpenHashSet();
	public static final IntSet ARMOR_STACK_SHIELDING_LIST = new IntOpenHashSet();
	
	public static final Int2DoubleMap ARMOR_RAD_RESISTANCE_MAP = new Int2DoubleOpenHashMap();
	
	public static void init() {
		for (String stackInfo : radiation_shielding_item_blacklist) {
			ItemStack stack = RegistryHelper.itemStackFromRegistry(stackInfo);
			if (stack != null) {
				int packed = RecipeItemHelper.pack(stack);
				if (packed != 0) {
					ARMOR_STACK_SHIELDING_BLACKLIST.add(packed);
				}
			}
		}
		
		for (String stackInfo : radiation_shielding_custom_stacks) {
			ItemStack stack = RegistryHelper.itemStackFromRegistry(stackInfo);
			if (stack != null) {
				int packed = RecipeItemHelper.pack(stack);
				if (packed != 0) {
					ARMOR_STACK_SHIELDING_LIST.add(packed);
				}
			}
		}
	}
	
	public static void postInit() {
		for (String stackInfo : radiation_shielding_default_levels) {
			int scorePos = stackInfo.lastIndexOf('_');
			if (scorePos == -1) {
				continue;
			}
			ItemStack stack = RegistryHelper.itemStackFromRegistry(stackInfo.substring(0, scorePos));
			if (stack == null || stack.isEmpty() || !ArmorHelper.isArmor(stack.getItem(), radiation_horse_armor_public)) {
				continue;
			}
			int packed = RecipeItemHelper.pack(stack);
			if (packed == 0) {
				continue;
			}
			ARMOR_RAD_RESISTANCE_MAP.put(packed, Double.parseDouble(stackInfo.substring(scorePos + 1)));
		}
	}
	
	public static void refreshRadiationArmor() {
		ARMOR_RAD_RESISTANCE_MAP.clear();
		
		postInit();
	}
	
	public static ItemStack armorWithRadResistance(ItemStack armor, double resistance) {
		ItemStack stack = armor.copy();
		if (!ArmorHelper.isArmor(armor.getItem(), radiation_horse_armor_public)) {
			return stack;
		}
		NBTHelper.getStackNBT(stack).setDouble("ncRadiationResistance", resistance);
		return stack;
	}
	
	public static void addArmorShieldingRecipes(ItemStack stack) {
		for (int i : new int[] {0, 1, 2}) {
			CraftingRecipeHandler.addShapelessArmorUpgradeRecipe(armorWithRadResistance(stack, radiation_shielding_level[i]), new Object[] {stack, new ItemStack(NCItems.rad_shielding, 1, i)});
		}
	}
}
