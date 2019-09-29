package nc.recipe.multiblock;

import nc.config.NCConfig;
import nc.recipe.ProcessorRecipeHandler;

public class SaltFissionRecipes extends ProcessorRecipeHandler {
	
	public SaltFissionRecipes() {
		super("salt_fission", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addFuelDepleteRecipe("tbu", NCConfig.fission_thorium_fuel_time[0], NCConfig.fission_thorium_heat_generation[0], NCConfig.fission_thorium_radiation[0]);
		addFuelDepleteRecipes("u", NCConfig.fission_uranium_fuel_time, NCConfig.fission_uranium_heat_generation, NCConfig.fission_uranium_radiation, 233, 235);
		addFuelDepleteRecipes("n", NCConfig.fission_neptunium_fuel_time, NCConfig.fission_neptunium_heat_generation, NCConfig.fission_neptunium_radiation, 236);
		addFuelDepleteRecipes("p", NCConfig.fission_plutonium_fuel_time, NCConfig.fission_plutonium_heat_generation, NCConfig.fission_plutonium_radiation, 239, 241);
		
		addFuelDepleteRecipes("a", NCConfig.fission_americium_fuel_time, NCConfig.fission_americium_heat_generation, NCConfig.fission_americium_radiation, 242);
		addFuelDepleteRecipes("cm", NCConfig.fission_curium_fuel_time, NCConfig.fission_curium_heat_generation, NCConfig.fission_curium_radiation, 243, 245, 247);
		addFuelDepleteRecipes("b", NCConfig.fission_berkelium_fuel_time, NCConfig.fission_berkelium_heat_generation, NCConfig.fission_berkelium_radiation, 248);
		addFuelDepleteRecipes("cf", NCConfig.fission_californium_fuel_time, NCConfig.fission_californium_heat_generation, NCConfig.fission_californium_radiation, 249, 251);
	}
	
	public void addFuelDepleteRecipe(String fuel, double time, double heat, double radiationLevel) {
		addRecipe(fluidStack("fuel_" + fuel + "_fluoride_flibe", 4), fluidStack("depleted_fuel_" + fuel + "_fluoride_flibe", 4), time/648D, heat, radiationLevel);
	}
	
	public void addFuelDepleteRecipes(String fuel, int[] times, int[] heats, double[] radiationLevels, int... types) {
		int count = 0;
		for (int type : types) {
			addFuelDepleteRecipe("le" + fuel + "_" + type, times[count], heats[count], radiationLevels[count]);
			addFuelDepleteRecipe("he" + fuel + "_" + type, times[count + 2], heats[count + 2], radiationLevels[count + 2]);
			count += 4;
		}
	}
}
