package nc.recipe.processor;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;
import nc.util.FluidHelper;

public class CrystallizerRecipes extends BaseRecipeHandler {
	
	public CrystallizerRecipes() {
		super("crystallizer", 0, 1, 1, 0);
	}

	@Override
	public void addRecipes() {
		addRecipe(fluidStack("boron_nitride_solution", FluidHelper.GEM_VOLUME), "dustBoronNitride", NCConfig.processor_time[14]);
		addRecipe(fluidStack("fluorite_water", FluidHelper.GEM_VOLUME), "dustFluorite", NCConfig.processor_time[14]);
		addRecipe(fluidStack("calcium_sulfate_solution", FluidHelper.GEM_VOLUME), "dustCalciumSulfate", NCConfig.processor_time[14]);
		addRecipe(fluidStack("sodium_fluoride_solution", FluidHelper.GEM_VOLUME), "dustSodiumFluoride", NCConfig.processor_time[14]);
		addRecipe(fluidStack("potassium_fluoride_solution", FluidHelper.GEM_VOLUME), "dustPotassiumFluoride", NCConfig.processor_time[14]);
		addRecipe(fluidStack("sodium_hydroxide_solution", FluidHelper.GEM_VOLUME), "dustSodiumHydroxide", NCConfig.processor_time[14]);
		addRecipe(fluidStack("potassium_hydroxide_solution", FluidHelper.GEM_VOLUME), "dustPotassiumHydroxide", NCConfig.processor_time[14]);
	}
}
