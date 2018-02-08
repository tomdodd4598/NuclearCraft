package nc.recipe.vanilla;

import nc.config.NCConfig;
import nc.enumm.MetaEnums.IngotOxideType;
import nc.enumm.MetaEnums.IngotType;
import nc.enumm.MetaEnums.OreType;
import nc.init.NCBlocks;
import nc.init.NCItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class FurnaceRecipeHandler {
	
	public static void registerFurnaceRecipes() {
		for (int i = 0; i < OreType.values().length; i++) {
			GameRegistry.addSmelting(new ItemStack(NCBlocks.ore, 1, i), new ItemStack(NCItems.ingot, 1, i), 0.5F);
		}
		for (int i = 0; i < IngotType.values().length; i++) {
			GameRegistry.addSmelting(new ItemStack(NCItems.dust, 1, i), new ItemStack(NCItems.ingot, 1, i), 0.0F);
		}
		for (int i = 0; i < IngotOxideType.values().length; i++) {
			GameRegistry.addSmelting(new ItemStack(NCItems.dust_oxide, 1, i), new ItemStack(NCItems.ingot_oxide, 1, i), 0.0F);
		}
		
		GameRegistry.addSmelting(new ItemStack(NCItems.gem_dust, 1, 1), new ItemStack(NCItems.dust_oxide, 1, 2), 0.0F);
		
		oxideMaterialRecipes(NCItems.thorium, 2);
		oxideMaterialRecipes(NCItems.uranium, 3);
		oxideMaterialRecipes(NCItems.neptunium, 2);
		oxideMaterialRecipes(NCItems.plutonium, 4);
		oxideMaterialRecipes(NCItems.americium, 3);
		oxideMaterialRecipes(NCItems.curium, 4);
		oxideMaterialRecipes(NCItems.berkelium, 2);
		oxideMaterialRecipes(NCItems.californium, 4);
		
		oxideFuelRecipes(NCItems.fuel_thorium, NCItems.fuel_rod_thorium, NCItems.depleted_fuel_thorium, NCItems.depleted_fuel_rod_thorium, 1);
		oxideFuelRecipes(NCItems.fuel_uranium, NCItems.fuel_rod_uranium, NCItems.depleted_fuel_uranium, NCItems.depleted_fuel_rod_uranium, 4);
		oxideFuelRecipes(NCItems.fuel_neptunium, NCItems.fuel_rod_neptunium, NCItems.depleted_fuel_neptunium, NCItems.depleted_fuel_rod_neptunium, 2);
		oxideFuelRecipes(NCItems.fuel_plutonium, NCItems.fuel_rod_plutonium, NCItems.depleted_fuel_plutonium, NCItems.depleted_fuel_rod_plutonium, 4);
		oxideFuelRecipes(NCItems.fuel_americium, NCItems.fuel_rod_americium, NCItems.depleted_fuel_americium, NCItems.depleted_fuel_rod_americium, 2);
		oxideFuelRecipes(NCItems.fuel_curium, NCItems.fuel_rod_curium, NCItems.depleted_fuel_curium, NCItems.depleted_fuel_rod_curium, 6);
		oxideFuelRecipes(NCItems.fuel_berkelium, NCItems.fuel_rod_berkelium, NCItems.depleted_fuel_berkelium, NCItems.depleted_fuel_rod_berkelium, 2);
		oxideFuelRecipes(NCItems.fuel_californium, NCItems.fuel_rod_californium, NCItems.depleted_fuel_californium, NCItems.depleted_fuel_rod_californium, 4);
	}
	
	public static void oxideFuelRecipes(Item fuelType, Item rodType, Item depletedFuelType, Item depletedRodType, int noFuelTypes) {
		for (int i = 0; i < noFuelTypes; i++) {
			GameRegistry.addSmelting(new ItemStack(fuelType, 1, 1 + 2*i), new ItemStack(fuelType, 1, 2*i), 0.0F);
			GameRegistry.addSmelting(new ItemStack(rodType, 1, 1 + 2*i), new ItemStack(rodType, 1, 2*i), 0.0F);
			if (NCConfig.fission_experimental_mechanics) GameRegistry.addSmelting(new ItemStack(depletedFuelType, 1, 1 + 2*i), new ItemStack(depletedFuelType, 1, 2*i), 0.0F);
			GameRegistry.addSmelting(new ItemStack(depletedRodType, 1, 1 + 2*i), new ItemStack(depletedRodType, 1, 2*i), 0.0F);
		}
	}
	
	public static void oxideMaterialRecipes(Item material, int noTypes) {
		for (int i = 0; i < 2*noTypes; i++) {
			GameRegistry.addSmelting(new ItemStack(material, 1, 1 + 2*i), new ItemStack(material, 1, 2*i), 0.0F);
		}
	}
}
