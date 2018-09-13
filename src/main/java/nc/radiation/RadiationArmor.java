package nc.radiation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.config.NCConfig;
import nc.init.NCArmor;
import nc.init.NCItems;
import nc.recipe.vanilla.CraftingRecipeHandler;
import nc.util.RegistryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class RadiationArmor {
	
	public static final List<Item> ARMOR_ITEM_SHIELDING_BLACKLIST = new ArrayList<Item>();
	public static final List<ItemStack> ARMOR_STACK_SHIELDING_LIST = new ArrayList<ItemStack>();
	
	public static final Map<ItemStack, Double> ARMOR_STACK_RESISTANCE_MAP = new HashMap<ItemStack, Double>();
	
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
			if (stack != null) ARMOR_STACK_SHIELDING_LIST.add(stack);
		}
	}
	
	public static void postInit() {
		for (String stackInfo : NCConfig.radiation_shielding_default_levels) {
			int scorePos = stackInfo.lastIndexOf('_');
			if (scorePos == -1) continue;
			ItemStack stack = RegistryHelper.itemStackFromRegistry(stackInfo.substring(0, scorePos));
			if (stack != null) ARMOR_STACK_RESISTANCE_MAP.put(stack, Double.parseDouble(stackInfo.substring(scorePos + 1)));
		}
	}
	
	public static ItemStack armorWithRadResistance(Item armor, double resistance) {
		return armorWithRadResistance(new ItemStack(armor), resistance);
	}
	
	public static ItemStack armorWithRadResistance(ItemStack armor, double resistance) {
		ItemStack stack = armor.copy();
		if (!(armor.getItem() instanceof ItemArmor)) return stack;
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setDouble("ncRadiationResistance", resistance);
		return stack;
	}
	
	public static void addArmorShieldingRecipes(Item item) {
		CraftingRecipeHandler.addShapelessArmorUpgradeOreRecipe(RadiationArmor.armorWithRadResistance(item, NCConfig.radiation_shielding_level[0]), new Object[] {item, new ItemStack(NCItems.rad_shielding, 1, 0)});
		CraftingRecipeHandler.addShapelessArmorUpgradeOreRecipe(RadiationArmor.armorWithRadResistance(item, NCConfig.radiation_shielding_level[1]), new Object[] {item, new ItemStack(NCItems.rad_shielding, 1, 1)});
		CraftingRecipeHandler.addShapelessArmorUpgradeOreRecipe(RadiationArmor.armorWithRadResistance(item, NCConfig.radiation_shielding_level[2]), new Object[] {item, new ItemStack(NCItems.rad_shielding, 1, 2)});
	}
	
	public static void addArmorShieldingRecipes(ItemStack stack) {
		CraftingRecipeHandler.addShapelessArmorUpgradeOreRecipe(RadiationArmor.armorWithRadResistance(stack, NCConfig.radiation_shielding_level[0]), new Object[] {stack, new ItemStack(NCItems.rad_shielding, 1, 0)});
		CraftingRecipeHandler.addShapelessArmorUpgradeOreRecipe(RadiationArmor.armorWithRadResistance(stack, NCConfig.radiation_shielding_level[1]), new Object[] {stack, new ItemStack(NCItems.rad_shielding, 1, 1)});
		CraftingRecipeHandler.addShapelessArmorUpgradeOreRecipe(RadiationArmor.armorWithRadResistance(stack, NCConfig.radiation_shielding_level[2]), new Object[] {stack, new ItemStack(NCItems.rad_shielding, 1, 2)});
	}
}
