package nc.recipe.multiblock;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import nc.config.NCConfig;
import nc.recipe.ProcessorRecipeHandler;
import nc.util.RegistryHelper;

public class SolidFissionRecipes extends ProcessorRecipeHandler {
	
	public SolidFissionRecipes() {
		super("solid_fission", 1, 0, 1, 0);
	}
	
	@Override
	public void addRecipes() {
		addFuelDepleteRecipes(NCConfig.fission_thorium_fuel_time, NCConfig.fission_thorium_heat_generation, NCConfig.fission_thorium_efficiency, NCConfig.fission_thorium_criticality, NCConfig.fission_thorium_self_priming, NCConfig.fission_thorium_radiation, "TBU");
		addFuelDepleteRecipes(NCConfig.fission_uranium_fuel_time, NCConfig.fission_uranium_heat_generation, NCConfig.fission_uranium_efficiency, NCConfig.fission_uranium_criticality, NCConfig.fission_uranium_self_priming, NCConfig.fission_uranium_radiation, "LEU233", "HEU233", "LEU235", "HEU235");
		addFuelDepleteRecipes(NCConfig.fission_neptunium_fuel_time, NCConfig.fission_neptunium_heat_generation, NCConfig.fission_neptunium_efficiency, NCConfig.fission_neptunium_criticality, NCConfig.fission_neptunium_self_priming, NCConfig.fission_neptunium_radiation, "LEN236", "HEN236");
		addFuelDepleteRecipes(NCConfig.fission_plutonium_fuel_time, NCConfig.fission_plutonium_heat_generation, NCConfig.fission_plutonium_efficiency, NCConfig.fission_plutonium_criticality, NCConfig.fission_plutonium_self_priming, NCConfig.fission_plutonium_radiation, "LEP239", "HEP239", "LEP241", "HEP241");
		addFuelDepleteRecipes(NCConfig.fission_mixed_fuel_time, NCConfig.fission_mixed_heat_generation, NCConfig.fission_mixed_efficiency, NCConfig.fission_mixed_criticality, NCConfig.fission_mixed_self_priming, NCConfig.fission_mixed_radiation, "MIX239", "MIX241");
		addFuelDepleteRecipes(NCConfig.fission_americium_fuel_time, NCConfig.fission_americium_heat_generation, NCConfig.fission_americium_efficiency, NCConfig.fission_americium_criticality, NCConfig.fission_americium_self_priming, NCConfig.fission_americium_radiation, "LEA242", "HEA242");
		addFuelDepleteRecipes(NCConfig.fission_curium_fuel_time, NCConfig.fission_curium_heat_generation, NCConfig.fission_curium_efficiency, NCConfig.fission_curium_criticality, NCConfig.fission_curium_self_priming, NCConfig.fission_curium_radiation, "LECm243", "HECm243", "LECm245", "HECm245", "LECm247", "HECm247");
		addFuelDepleteRecipes(NCConfig.fission_berkelium_fuel_time, NCConfig.fission_berkelium_heat_generation, NCConfig.fission_berkelium_efficiency, NCConfig.fission_berkelium_criticality, NCConfig.fission_berkelium_self_priming, NCConfig.fission_berkelium_radiation, "LEB248", "HEB248");
		addFuelDepleteRecipes(NCConfig.fission_californium_fuel_time, NCConfig.fission_californium_heat_generation, NCConfig.fission_californium_efficiency, NCConfig.fission_californium_criticality, NCConfig.fission_californium_self_priming, NCConfig.fission_californium_radiation, "LECf249", "HECf249", "LECf251", "HECf251");
		
		addRecipe(RegistryHelper.itemStackFromRegistry("ic2:nuclear:0", 1), "depletedFuelIC2U", NCConfig.fission_uranium_fuel_time[9]*19D/6D, NCConfig.fission_uranium_heat_generation[9]*18D/19D, NCConfig.fission_uranium_efficiency[9], NCConfig.fission_uranium_criticality[9], false, NCConfig.fission_uranium_radiation[9]*18D/19D);
		addRecipe(RegistryHelper.itemStackFromRegistry("ic2:nuclear:4", 1), "depletedFuelIC2MOX", NCConfig.fission_mixed_fuel_time[1]*7D*3D, NCConfig.fission_mixed_heat_generation[1]*9D/7D, NCConfig.fission_mixed_efficiency[1], NCConfig.fission_mixed_criticality[1], false, NCConfig.fission_mixed_radiation[1]*9D/7D);
		
		addRecipe(Lists.newArrayList("ingotYellorium", "ingotBlutonium"), "ingotCyanite", NCConfig.fission_uranium_fuel_time[9]*0.5D*9D/8D, NCConfig.fission_uranium_heat_generation[9]*8D/9D, NCConfig.fission_uranium_efficiency[9], NCConfig.fission_uranium_criticality[9], false, NCConfig.fission_uranium_radiation[9]*8D/9D);
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
}
