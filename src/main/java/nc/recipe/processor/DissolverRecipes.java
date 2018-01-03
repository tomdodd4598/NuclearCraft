package nc.recipe.processor;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;

public class DissolverRecipes extends BaseRecipeHandler {
	
	public DissolverRecipes() {
		super(1, 1, 0, 1, false);
	}

	@Override
	public void addRecipes() {
		addRecipe("dustBoronNitride", fluidStack("water", 1000), fluidStack("boron_nitride_solution", 666), NCConfig.processor_time[15]);
		addRecipe("dustFluorite", fluidStack("water", 1000), fluidStack("fluorite_water", 666), NCConfig.processor_time[15]);
		addRecipe("dustCalciumSulfate", fluidStack("water", 1000), fluidStack("calcium_sulfate_solution", 666), NCConfig.processor_time[15]);
	}

	@Override
	public String getRecipeName() {
		return "dissolver";
	}
}
