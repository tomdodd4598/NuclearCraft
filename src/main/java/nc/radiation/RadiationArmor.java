package nc.radiation;

import java.util.HashSet;
import java.util.Set;

import it.unimi.dsi.fastutil.ints.Int2DoubleMap;
import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;
import nc.config.NCConfig;
import nc.init.NCArmor;
import nc.init.NCItems;
import nc.recipe.vanilla.CraftingRecipeHandler;
import nc.util.ArmorHelper;
import nc.util.RegistryHelper;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class RadiationArmor {
	
	public static final Set<Item> ARMOR_ITEM_SHIELDING_BLACKLIST = new HashSet<>();
	public static final Set<ItemStack> ARMOR_STACK_SHIELDING_LIST = new HashSet<>();
	
	public static final Int2DoubleMap ARMOR_RAD_RESISTANCE_MAP = new Int2DoubleOpenHashMap();
	
	public static void init() {
		ARMOR_ITEM_SHIELDING_BLACKLIST.add(NCArmor.helm_hazmat);
		ARMOR_ITEM_SHIELDING_BLACKLIST.add(NCArmor.chest_hazmat);
		ARMOR_ITEM_SHIELDING_BLACKLIST.add(NCArmor.legs_hazmat);
		ARMOR_ITEM_SHIELDING_BLACKLIST.add(NCArmor.boots_hazmat);
		
		for (String itemInfo : NCConfig.radiation_shielding_item_blacklist) {
			Item item = RegistryHelper.getItem(itemInfo);
			if (item != null) ARMOR_ITEM_SHIELDING_BLACKLIST.add(item);
		}
		
		for (String stackInfo : NCConfig.radiation_shielding_custom_stacks) {
			ItemStack stack = RegistryHelper.itemStackFromRegistry(stackInfo);
			ARMOR_STACK_SHIELDING_LIST.add(stack);
		}
	}
	
	public static void postInit() {
		for (String stackInfo : NCConfig.radiation_shielding_default_levels) {
			int scorePos = stackInfo.lastIndexOf('_');
			if (scorePos == -1) continue;
			ItemStack stack = RegistryHelper.itemStackFromRegistry(stackInfo.substring(0, scorePos));
			if (stack == null || stack.isEmpty() || !ArmorHelper.isArmor(stack.getItem(), NCConfig.radiation_horse_armor_public)) return;
			ARMOR_RAD_RESISTANCE_MAP.put(RecipeItemHelper.pack(stack), Double.parseDouble(stackInfo.substring(scorePos + 1)));
		}
	}
	
	public static ItemStack armorWithRadResistance(Item armor, double resistance) {
		return armorWithRadResistance(new ItemStack(armor), resistance);
	}
	
	public static ItemStack armorWithRadResistance(ItemStack armor, double resistance) {
		ItemStack stack = armor.copy();
		if (!ArmorHelper.isArmor(armor.getItem(), true)) return stack;
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setDouble("ncRadiationResistance", resistance);
		return stack;
	}
	
	public static void addArmorShieldingRecipes(Item item) {
		CraftingRecipeHandler.addShapelessArmorUpgradeRecipe(armorWithRadResistance(item, NCConfig.radiation_shielding_level[0]), new Object[] {item, new ItemStack(NCItems.rad_shielding, 1, 0)});
		CraftingRecipeHandler.addShapelessArmorUpgradeRecipe(armorWithRadResistance(item, NCConfig.radiation_shielding_level[1]), new Object[] {item, new ItemStack(NCItems.rad_shielding, 1, 1)});
		CraftingRecipeHandler.addShapelessArmorUpgradeRecipe(armorWithRadResistance(item, NCConfig.radiation_shielding_level[2]), new Object[] {item, new ItemStack(NCItems.rad_shielding, 1, 2)});
	}
	
	public static void addArmorShieldingRecipes(ItemStack stack) {
		CraftingRecipeHandler.addShapelessArmorUpgradeRecipe(armorWithRadResistance(stack, NCConfig.radiation_shielding_level[0]), new Object[] {stack, new ItemStack(NCItems.rad_shielding, 1, 0)});
		CraftingRecipeHandler.addShapelessArmorUpgradeRecipe(armorWithRadResistance(stack, NCConfig.radiation_shielding_level[1]), new Object[] {stack, new ItemStack(NCItems.rad_shielding, 1, 1)});
		CraftingRecipeHandler.addShapelessArmorUpgradeRecipe(armorWithRadResistance(stack, NCConfig.radiation_shielding_level[2]), new Object[] {stack, new ItemStack(NCItems.rad_shielding, 1, 2)});
	}
}
