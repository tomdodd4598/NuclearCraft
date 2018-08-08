package nc.recipe.saltFission;

import nc.config.NCConfig;
import nc.recipe.ProcessorRecipeHandler;

public class SaltFissionRecipes extends ProcessorRecipeHandler {
	
	public SaltFissionRecipes() {
		super("salt_fission", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addFuelDepleteRecipe("tbu", NCConfig.fission_thorium_fuel_time[0], NCConfig.fission_thorium_power[0]);
		addFuelDepleteRecipes("u", NCConfig.fission_uranium_fuel_time, NCConfig.fission_uranium_power, 233, 235);
		addFuelDepleteRecipes("n", NCConfig.fission_neptunium_fuel_time, NCConfig.fission_neptunium_power, 236);
		addFuelDepleteRecipes("p", NCConfig.fission_plutonium_fuel_time, NCConfig.fission_plutonium_power, 239, 241);
		
		addFuelDepleteRecipes("a", NCConfig.fission_americium_fuel_time, NCConfig.fission_americium_power, 242);
		addFuelDepleteRecipes("cm", NCConfig.fission_curium_fuel_time, NCConfig.fission_curium_power, 243, 245, 247);
		addFuelDepleteRecipes("b", NCConfig.fission_berkelium_fuel_time, NCConfig.fission_berkelium_power, 248);
		addFuelDepleteRecipes("cf", NCConfig.fission_californium_fuel_time, NCConfig.fission_californium_power, 249, 251);
	}
	
	public void addFuelDepleteRecipe(String fuel, double time, double heat) {
		addRecipe(fluidStack("fuel_" + fuel + "_fluoride_flibe", 1), fluidStack("depleted_fuel_" + fuel + "_fluoride_flibe", 1), time/1296D, heat);
	}
	
	public void addFuelDepleteRecipes(String fuel, double[] times, double[] heats, int... types) {
		int count = 0;
		for (int type : types) {
			addFuelDepleteRecipe("le" + fuel + "_" + type, times[count], heats[count]);
			addFuelDepleteRecipe("he" + fuel + "_" + type, times[count + 2], heats[count + 2]);
			count += 4;
		}
	}
}
