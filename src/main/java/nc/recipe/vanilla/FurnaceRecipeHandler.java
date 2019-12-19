package nc.recipe.vanilla;

import static nc.config.NCConfig.ore_dict_raw_material_recipes;

import nc.enumm.MetaEnums.IngotType;
import nc.enumm.MetaEnums.OreType;
import nc.init.NCBlocks;
import nc.init.NCItems;
import nc.util.OreDictHelper;
import nc.util.StringHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class FurnaceRecipeHandler {
	
	public static void registerFurnaceRecipes() {
		for (int i = 0; i < OreType.values().length; i++) {
			String type = StringHelper.capitalize(OreType.values()[i].getName());
			if (!ore_dict_raw_material_recipes) {
				GameRegistry.addSmelting(new ItemStack(NCBlocks.ore, 1, i), OreDictHelper.getPrioritisedCraftingStack(new ItemStack(NCItems.ingot, 1, i), "ingot" + type), 0.5F);
			}
			else for (ItemStack ore : OreDictionary.getOres("ore" + type)) {
				GameRegistry.addSmelting(ore, OreDictHelper.getPrioritisedCraftingStack(new ItemStack(NCItems.ingot, 1, i), "ingot" + type), 0.5F);
			}
		}
		for (int i = 0; i < IngotType.values().length; i++) {
			String type = StringHelper.capitalize(IngotType.values()[i].getName());
			if (!ore_dict_raw_material_recipes) {
				GameRegistry.addSmelting(new ItemStack(NCItems.dust, 1, i), OreDictHelper.getPrioritisedCraftingStack(new ItemStack(NCItems.ingot, 1, i), "ingot" + type), 0F);
			}
			else for (ItemStack dust : OreDictionary.getOres("dust" + type)) {
				GameRegistry.addSmelting(dust, OreDictHelper.getPrioritisedCraftingStack(new ItemStack(NCItems.ingot, 1, i), "ingot" + type), 0F);
			}
		}
		
		// Manganese Reduction
		GameRegistry.addSmelting(new ItemStack(NCItems.ingot, 1, 15), OreDictHelper.getPrioritisedCraftingStack(new ItemStack(NCItems.ingot, 1, 14), "ingotManganeseOxide"), 0F);
		if (OreDictHelper.oreExists("ingotManganese")) {
			GameRegistry.addSmelting(new ItemStack(NCItems.ingot, 1, 14), OreDictHelper.getPrioritisedCraftingStack(new ItemStack(NCItems.ingot, 1, 14), "ingotManganese"), 0F);
		}
		
		// Rhodochrosite
		GameRegistry.addSmelting(new ItemStack(NCItems.gem_dust, 1, 1), OreDictHelper.getPrioritisedCraftingStack(new ItemStack(NCItems.dust, 1, 14), "dustManganeseOxide"), 0F);
		
		GameRegistry.addSmelting(new ItemStack(NCItems.thorium, 1, 1), OreDictHelper.getPrioritisedCraftingStack(new ItemStack(NCItems.ingot, 1, 3), "ingotThorium"), 0F);
		GameRegistry.addSmelting(new ItemStack(NCItems.thorium, 1, 2), OreDictHelper.getPrioritisedCraftingStack(new ItemStack(NCItems.ingot, 1, 3), "ingotThorium"), 0F);
		reductionIsotopeRecipes(NCItems.uranium, 3);
		reductionIsotopeRecipes(NCItems.neptunium, 2);
		reductionIsotopeRecipes(NCItems.plutonium, 4);
		reductionIsotopeRecipes(NCItems.americium, 3);
		reductionIsotopeRecipes(NCItems.curium, 4);
		reductionIsotopeRecipes(NCItems.berkelium, 2);
		reductionIsotopeRecipes(NCItems.californium, 4);
		
		GameRegistry.addSmelting(new ItemStack(Items.DYE, 1, 3), new ItemStack(NCItems.roasted_cocoa_beans, 1), 0F);
	}
	
	public static void reductionIsotopeRecipes(Item isotope, int noTypes) {
		for (int i = 0; i < noTypes; i++) {
			GameRegistry.addSmelting(new ItemStack(isotope, 1, 5*i + 2), new ItemStack(isotope, 1, 5*i), 0F);
			GameRegistry.addSmelting(new ItemStack(isotope, 1, 5*i + 3), new ItemStack(isotope, 1, 5*i), 0F);
		}
	}
}
