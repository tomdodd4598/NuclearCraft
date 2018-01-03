package nc.recipe.processor;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;

public class CrystallizerRecipes extends BaseRecipeHandler {
	
	public CrystallizerRecipes() {
		super(0, 1, 1, 0, false);
	}

	@Override
	public void addRecipes() {
		addRecipe(fluidStack("boron_nitride_solution", 666), "dustBoronNitride", NCConfig.processor_time[14]);
		addRecipe(fluidStack("fluorite_water", 666), "dustFluorite", NCConfig.processor_time[14]);
		addRecipe(fluidStack("calcium_sulfate_solution", 666), "dustCalciumSulfate", NCConfig.processor_time[14]);
	}

	@Override
	public String getRecipeName() {
		return "crystallizer";
	}
}
