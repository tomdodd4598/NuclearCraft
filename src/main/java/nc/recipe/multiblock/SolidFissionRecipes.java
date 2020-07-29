package nc.recipe.multiblock;

import static nc.config.NCConfig.*;

import java.util.*;

import it.unimi.dsi.fastutil.objects.*;
import nc.recipe.ProcessorRecipeHandler;
import nc.recipe.ingredient.*;
import nc.util.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class SolidFissionRecipes extends ProcessorRecipeHandler {
	
	public SolidFissionRecipes() {
		super("solid_fission", 1, 0, 1, 0);
	}
	
	@Override
	public void addRecipes() {
		addFuelDepleteRecipes(fission_thorium_fuel_time, fission_thorium_heat_generation, fission_thorium_efficiency, fission_thorium_criticality, fission_thorium_self_priming, fission_thorium_radiation, "TBU");
		addFuelDepleteRecipes(fission_uranium_fuel_time, fission_uranium_heat_generation, fission_uranium_efficiency, fission_uranium_criticality, fission_uranium_self_priming, fission_uranium_radiation, "LEU233", "HEU233", "LEU235", "HEU235");
		addFuelDepleteRecipes(fission_neptunium_fuel_time, fission_neptunium_heat_generation, fission_neptunium_efficiency, fission_neptunium_criticality, fission_neptunium_self_priming, fission_neptunium_radiation, "LEN236", "HEN236");
		addFuelDepleteRecipes(fission_plutonium_fuel_time, fission_plutonium_heat_generation, fission_plutonium_efficiency, fission_plutonium_criticality, fission_plutonium_self_priming, fission_plutonium_radiation, "LEP239", "HEP239", "LEP241", "HEP241");
		addFuelDepleteRecipes(fission_mixed_fuel_time, fission_mixed_heat_generation, fission_mixed_efficiency, fission_mixed_criticality, fission_mixed_self_priming, fission_mixed_radiation, "MIX239", "MIX241");
		addFuelDepleteRecipes(fission_americium_fuel_time, fission_americium_heat_generation, fission_americium_efficiency, fission_americium_criticality, fission_americium_self_priming, fission_americium_radiation, "LEA242", "HEA242");
		addFuelDepleteRecipes(fission_curium_fuel_time, fission_curium_heat_generation, fission_curium_efficiency, fission_curium_criticality, fission_curium_self_priming, fission_curium_radiation, "LECm243", "HECm243", "LECm245", "HECm245", "LECm247", "HECm247");
		addFuelDepleteRecipes(fission_berkelium_fuel_time, fission_berkelium_heat_generation, fission_berkelium_efficiency, fission_berkelium_criticality, fission_berkelium_self_priming, fission_berkelium_radiation, "LEB248", "HEB248");
		addFuelDepleteRecipes(fission_californium_fuel_time, fission_californium_heat_generation, fission_californium_efficiency, fission_californium_criticality, fission_californium_self_priming, fission_californium_radiation, "LECf249", "HECf249", "LECf251", "HECf251");
		
		addRecipe(RegistryHelper.itemStackFromRegistry("ic2:nuclear:0", 1), "depletedFuelIC2U", (int) (fission_uranium_fuel_time[9] * 19D / 6D), (int) (fission_uranium_heat_generation[9] * 18D / 19D), fission_uranium_efficiency[9], fission_uranium_criticality[9], false, fission_uranium_radiation[9] * 18D / 19D);
		addRecipe(RegistryHelper.itemStackFromRegistry("ic2:nuclear:4", 1), "depletedFuelIC2MOX", (int) (fission_mixed_fuel_time[1] * 7D * 3D), (int) (fission_mixed_heat_generation[1] * 9D / 7D), fission_mixed_efficiency[1], fission_mixed_criticality[1], false, fission_mixed_radiation[1] * 9D / 7D);
		
		addRecipe(getYelloriumIngredient(), "ingotCyanite", (int) (fission_uranium_fuel_time[9] * 0.5D * 9D / 8D), (int) (fission_uranium_heat_generation[9] * 8D / 9D), fission_uranium_efficiency[9], fission_uranium_criticality[9], false, fission_uranium_radiation[9] * 8D / 9D);
	}
	
	public void addFuelDepleteRecipes(int[] time, int[] heat, double[] efficiency, int[] criticality, boolean[] selfPriming, double[] radiation, String... fuelTypes) {
		int id = 0;
		for (String fuelType : fuelTypes) {
			addRecipe("ingot" + fuelType + "Oxide", "ingotDepleted" + fuelType + "Oxide", time[id + 1], heat[id + 1], efficiency[id + 1], criticality[id + 1], selfPriming[id + 1], radiation[id + 1]);
			addRecipe("ingot" + fuelType + "Nitride", "ingotDepleted" + fuelType + "Nitride", time[id + 2], heat[id + 2], efficiency[id + 2], criticality[id + 2], selfPriming[id + 2], radiation[id + 2]);
			addRecipe("ingot" + fuelType + "ZA", "ingotDepleted" + fuelType + "ZA", time[id + 3], heat[id + 3], efficiency[id + 3], criticality[id + 3], selfPriming[id + 3], radiation[id + 3]);
			id += 5;
		}
	}
	
	@Override
	public List fixExtras(List extras) {
		List fixed = new ArrayList(6);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Integer ? (int) extras.get(0) : 1);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof Integer ? (int) extras.get(1) : 0);
		fixed.add(extras.size() > 2 && extras.get(2) instanceof Double ? (double) extras.get(2) : 0D);
		fixed.add(extras.size() > 3 && extras.get(3) instanceof Integer ? (int) extras.get(3) : 1);
		fixed.add(extras.size() > 4 && extras.get(4) instanceof Boolean ? (boolean) extras.get(4) : false);
		fixed.add(extras.size() > 5 && extras.get(5) instanceof Double ? (double) extras.get(5) : 0D);
		return fixed;
	}
	
	// Explicitly exclude uranium and plutonium ingots
	private static ItemArrayIngredient getYelloriumIngredient() {
		ObjectSet<ItemStack> yellorium = new ObjectOpenHashSet(OreDictionary.getOres("ingotYellorium", false));
		yellorium.addAll(OreDictionary.getOres("ingotBlutonium", false));
		List<IItemIngredient> ingredients = new ArrayList<>();
		for (ItemStack stack : yellorium) {
			if (!OreDictHelper.isOreMember(stack, "ingotUranium") && !OreDictHelper.isOreMember(stack, "ingotPlutonium")) {
				ingredients.add(new ItemIngredient(stack));
			}
		}
		return new ItemArrayIngredient(ingredients);
	}
}
