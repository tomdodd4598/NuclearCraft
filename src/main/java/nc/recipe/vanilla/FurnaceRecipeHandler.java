package nc.recipe.vanilla;

import nc.enumm.IMetaEnum;
import nc.enumm.MetaEnums.*;
import nc.init.*;
import nc.util.*;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import static nc.config.NCConfig.ore_dict_raw_material_recipes;

public class FurnaceRecipeHandler {
	
	public static void registerFurnaceRecipes() {
		OreType[] ores = OreType.values();
		for (int i = 0, len = ores.length; i < len; ++i) {
			String suffix = StringHelper.capitalize(ores[i].getName());
			ItemStack ingotStack = OreDictHelper.getPrioritisedCraftingStack(new ItemStack(NCItems.ingot, 1, i), "ingot" + suffix);
			if (!ore_dict_raw_material_recipes) {
				GameRegistry.addSmelting(new ItemStack(NCBlocks.ore, 1, i), ingotStack, 0.5F);
			}
			else {
				for (ItemStack ore : OreDictionary.getOres("ore" + suffix, false)) {
					GameRegistry.addSmelting(ore, ingotStack, 0.5F);
				}
			}
		}
		
		ingotSmeltingRecipes(IngotType.class, NCItems.ingot, NCItems.dust);
		ingotSmeltingRecipes(IngotType2.class, NCItems.ingot2, NCItems.dust2);
		
		// Oxide Reduction
		GameRegistry.addSmelting(new ItemStack(NCItems.ingot, 1, 15), OreDictHelper.getPrioritisedCraftingStack(new ItemStack(NCItems.ingot, 1, 14), "ingotManganeseOxide"), 0F);
		GameRegistry.addSmelting(new ItemStack(NCItems.ingot, 1, 14), OreDictHelper.getPrioritisedCraftingStack(new ItemStack(NCItems.ingot, 1, 11), "ingotManganese"), 0F);
		GameRegistry.addSmelting(new ItemStack(NCItems.ingot2, 1, 0), OreDictHelper.getPrioritisedCraftingStack(new ItemStack(NCItems.ingot, 1, 10), "ingotZirconium"), 0F);
		GameRegistry.addSmelting(new ItemStack(NCItems.ingot2, 1, 2), OreDictHelper.getPrioritisedCraftingStack(new ItemStack(NCItems.ingot, 1, 1), "ingotTin"), 0F);
		tryAddSmelting(new ItemStack(NCItems.ingot2, 1, 3), "ingotNickel", 0F);
		tryAddSmelting(new ItemStack(NCItems.ingot2, 1, 4), "ingotCobalt", 0F);
		tryAddSmelting(new ItemStack(NCItems.ingot2, 1, 5), "ingotRuthenium", 0F);
		tryAddSmelting(new ItemStack(NCItems.ingot2, 1, 6), "ingotIridium", 0F);
		
		// Rhodochrosite Refining
		GameRegistry.addSmelting(new ItemStack(NCItems.gem_dust, 1, 1), OreDictHelper.getPrioritisedCraftingStack(new ItemStack(NCItems.dust, 1, 14), "dustManganeseOxide"), 0F);
		
		// Barite Refining
		GameRegistry.addSmelting(new ItemStack(NCItems.gem_dust, 1, 12), OreDictHelper.getPrioritisedCraftingStack(new ItemStack(NCItems.compound, 1, 18), "dustBariumSulfide"), 0F);
		
		// Ammonium Sulfate Decomposition
		GameRegistry.addSmelting(new ItemStack(NCItems.compound, 1, 12), OreDictHelper.getPrioritisedCraftingStack(new ItemStack(NCItems.compound, 1, 13), "dustAmmoniumBisulfate"), 0F);
		
		// Silica Reduction
		if (OreDictHelper.oreExists("ingotSilicon")) {
			tryAddSmelting(new ItemStack(NCItems.compound, 1, 19), "ingotSilicon", 0F);
		}
		else {
			GameRegistry.addSmelting(new ItemStack(NCItems.compound, 1, 19), OreDictHelper.getPrioritisedCraftingStack(new ItemStack(NCItems.gem, 1, 6), "itemSilicon"), 0F);
		}
		
		// Polydimethylsilylene Pyrolysis
		GameRegistry.addSmelting(new ItemStack(NCItems.part, 1, 21), OreDictHelper.getPrioritisedCraftingStack(new ItemStack(NCItems.part, 1, 22), "ingotPolymethylsilyleneMethylene"), 0F);
		
		// Cocoa Bean Roasting
		GameRegistry.addSmelting(new ItemStack(Items.DYE, 1, 3), new ItemStack(NCItems.roasted_cocoa_beans, 1), 0F);
		
		// Isotopes
		reductionIsotopeRecipes(NCItems.uranium, 3);
		reductionIsotopeRecipes(NCItems.neptunium, 2);
		reductionIsotopeRecipes(NCItems.plutonium, 4);
		reductionIsotopeRecipes(NCItems.americium, 3);
		reductionIsotopeRecipes(NCItems.curium, 4);
		reductionIsotopeRecipes(NCItems.berkelium, 2);
		reductionIsotopeRecipes(NCItems.californium, 4);
		
		// Fission Fuels
		reductionFissionFuelRecipes(NCItems.pellet_thorium, NCItems.fuel_thorium, 1);
		reductionFissionFuelRecipes(NCItems.pellet_uranium, NCItems.fuel_uranium, 4);
		reductionFissionFuelRecipes(NCItems.pellet_neptunium, NCItems.fuel_neptunium, 2);
		reductionFissionFuelRecipes(NCItems.pellet_plutonium, NCItems.fuel_plutonium, 4);
		reductionFissionFuelRecipes(NCItems.pellet_mixed, NCItems.fuel_mixed, 2);
		reductionFissionFuelRecipes(NCItems.pellet_americium, NCItems.fuel_americium, 2);
		reductionFissionFuelRecipes(NCItems.pellet_curium, NCItems.fuel_curium, 6);
		reductionFissionFuelRecipes(NCItems.pellet_berkelium, NCItems.fuel_berkelium, 2);
		reductionFissionFuelRecipes(NCItems.pellet_californium, NCItems.fuel_californium, 4);
	}
	
	public static <T extends Enum<T> & IStringSerializable & IMetaEnum> void ingotSmeltingRecipes(Class<T> enumm, Item ingot, Item dust) {
		T[] values = enumm.getEnumConstants();
		for (int i = 0, len = values.length; i < len; ++i) {
			String suffix = StringHelper.capitalize(values[i].getName());
			ItemStack ingotStack = OreDictHelper.getPrioritisedCraftingStack(new ItemStack(ingot, 1, i), "ingot" + suffix);
			if (!ore_dict_raw_material_recipes) {
				GameRegistry.addSmelting(new ItemStack(dust, 1, i), ingotStack, 0F);
			}
			else {
				for (ItemStack dustStack : OreDictionary.getOres("dust" + suffix, false)) {
					GameRegistry.addSmelting(dustStack, ingotStack, 0F);
				}
			}
		}
	}
	
	public static void tryAddSmelting(ItemStack input, String output, float xp) {
		ItemStack outputStack = OreDictHelper.getPrioritisedCraftingStack(ItemStack.EMPTY, output);
		if (outputStack != null && !outputStack.isEmpty()) {
			GameRegistry.addSmelting(input, outputStack, xp);
		}
	}
	
	public static void reductionIsotopeRecipes(Item isotope, int noTypes) {
		for (int i = 0; i < noTypes; ++i) {
			GameRegistry.addSmelting(new ItemStack(isotope, 1, 5 * i + 2), new ItemStack(isotope, 1, 5 * i), 0F);
			GameRegistry.addSmelting(new ItemStack(isotope, 1, 5 * i + 3), new ItemStack(isotope, 1, 5 * i), 0F);
		}
	}
	
	public static void reductionFissionFuelRecipes(Item pellet, Item fuel, int noTypes) {
		for (int i = 0; i < noTypes; ++i) {
			GameRegistry.addSmelting(new ItemStack(fuel, 1, 4 * i + 1), new ItemStack(pellet, 1, 2 * i), 0F);
			GameRegistry.addSmelting(new ItemStack(fuel, 1, 4 * i + 2), new ItemStack(pellet, 1, 2 * i), 0F);
		}
	}
}
