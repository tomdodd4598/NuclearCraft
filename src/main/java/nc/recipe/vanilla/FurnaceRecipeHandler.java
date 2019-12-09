package nc.recipe.vanilla;

import static nc.config.NCConfig.ore_dict_raw_material_recipes;

import nc.enumm.MetaEnums.IngotOxideType;
import nc.enumm.MetaEnums.IngotType;
import nc.enumm.MetaEnums.OreType;
import nc.init.NCBlocks;
import nc.init.NCItems;
import nc.util.OreDictHelper;
import nc.util.StringHelper;
import nc.worldgen.ore.OreGenerator;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class FurnaceRecipeHandler {
	
	public static void registerFurnaceRecipes() {
		for (int i = 0; i < OreType.values().length; i++) {
			if (OreGenerator.showOre(i)) {
				String type = StringHelper.capitalize(OreType.values()[i].getName());
				if (!ore_dict_raw_material_recipes) {
					GameRegistry.addSmelting(new ItemStack(NCBlocks.ore, 1, i), OreDictHelper.getPrioritisedCraftingStack(new ItemStack(NCItems.ingot, 1, i), "ingot" + type), 0.5F);
				}
				else for (ItemStack ore : OreDictionary.getOres("ore" + type)) {
					GameRegistry.addSmelting(ore, OreDictHelper.getPrioritisedCraftingStack(new ItemStack(NCItems.ingot, 1, i), "ingot" + type), 0.5F);
				}
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
		for (int i = 0; i < IngotOxideType.values().length; i++) {
			String type = StringHelper.capitalize(IngotOxideType.values()[i].getName()) + "Oxide";
			if (!ore_dict_raw_material_recipes) {
				GameRegistry.addSmelting(new ItemStack(NCItems.dust_oxide, 1, i), OreDictHelper.getPrioritisedCraftingStack(new ItemStack(NCItems.ingot_oxide, 1, i), "ingot" + type), 0F);
			}
			else for (ItemStack dust : OreDictionary.getOres("dust" + type)) {
				GameRegistry.addSmelting(dust, OreDictHelper.getPrioritisedCraftingStack(new ItemStack(NCItems.ingot_oxide, 1, i), "ingot" + type), 0F);
			}
		}
		// MnO2 -> MnO
		if (!ore_dict_raw_material_recipes) {
			GameRegistry.addSmelting(new ItemStack(NCItems.ingot_oxide, 1, 3), OreDictHelper.getPrioritisedCraftingStack(new ItemStack(NCItems.ingot_oxide, 1, 2), "ingotManganeseOxide"), 0F);
		}
		else for (ItemStack ingot : OreDictionary.getOres("ingotManganeseDioxide")) {
			GameRegistry.addSmelting(ingot, OreDictHelper.getPrioritisedCraftingStack(new ItemStack(NCItems.ingot_oxide, 1, 2), "ingotManganeseOxide"), 0F);
		}
		for (int i = 0; i < IngotOxideType.values().length; i++) {
			if (i == 3) continue;
			String type = StringHelper.capitalize(IngotOxideType.values()[i].getName());
			if (!ore_dict_raw_material_recipes) {
				GameRegistry.addSmelting(new ItemStack(NCItems.ingot_oxide, 1, i), OreDictHelper.getPrioritisedCraftingStack(new ItemStack(NCItems.ingot_oxide, 1, i), "ingot" + type), 0F);
			}
			else for (ItemStack ingot : OreDictionary.getOres("ingot" + type + "Oxide")) {
				GameRegistry.addSmelting(ingot, OreDictHelper.getPrioritisedCraftingStack(new ItemStack(NCItems.ingot_oxide, 1, i), "ingot" + type), 0F);
			}
		}
		
		GameRegistry.addSmelting(new ItemStack(NCItems.gem_dust, 1, 1), OreDictHelper.getPrioritisedCraftingStack(new ItemStack(NCItems.dust_oxide, 1, 2), "dustManganeseOxide"), 0F);
		
		oxideMaterialRecipes(NCItems.thorium, 2);
		oxideMaterialRecipes(NCItems.uranium, 3);
		oxideMaterialRecipes(NCItems.neptunium, 2);
		oxideMaterialRecipes(NCItems.plutonium, 4);
		oxideMaterialRecipes(NCItems.americium, 3);
		oxideMaterialRecipes(NCItems.curium, 4);
		oxideMaterialRecipes(NCItems.berkelium, 2);
		oxideMaterialRecipes(NCItems.californium, 4);
		
		oxideFuelRecipes(NCItems.fuel_thorium, NCItems.depleted_fuel_thorium, 1);
		oxideFuelRecipes(NCItems.fuel_uranium, NCItems.depleted_fuel_uranium, 4);
		oxideFuelRecipes(NCItems.fuel_neptunium, NCItems.depleted_fuel_neptunium, 2);
		oxideFuelRecipes(NCItems.fuel_plutonium, NCItems.depleted_fuel_plutonium, 4);
		oxideFuelRecipes(NCItems.fuel_americium, NCItems.depleted_fuel_americium, 2);
		oxideFuelRecipes(NCItems.fuel_curium, NCItems.depleted_fuel_curium, 6);
		oxideFuelRecipes(NCItems.fuel_berkelium, NCItems.depleted_fuel_berkelium, 2);
		oxideFuelRecipes(NCItems.fuel_californium, NCItems.depleted_fuel_californium, 4);
		
		GameRegistry.addSmelting(new ItemStack(Items.DYE, 1, 3), new ItemStack(NCItems.roasted_cocoa_beans, 1), 0F);
	}
	
	public static void oxideFuelRecipes(Item fuelType, Item depletedFuelType, int noFuelTypes) {
		for (int i = 0; i < noFuelTypes; i++) {
			GameRegistry.addSmelting(new ItemStack(fuelType, 1, 1 + 2*i), new ItemStack(fuelType, 1, 2*i), 0F);
			GameRegistry.addSmelting(new ItemStack(depletedFuelType, 1, 1 + 2*i), new ItemStack(depletedFuelType, 1, 2*i), 0F);
		}
	}
	
	public static void oxideMaterialRecipes(Item material, int noTypes) {
		for (int i = 0; i < 2*noTypes; i++) {
			GameRegistry.addSmelting(new ItemStack(material, 1, 1 + 2*i), new ItemStack(material, 1, 2*i), 0F);
		}
	}
}
