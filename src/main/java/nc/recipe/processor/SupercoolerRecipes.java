package nc.recipe.processor;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;

public class SupercoolerRecipes extends BaseRecipeHandler {
	
	public SupercoolerRecipes() {
		super(0, 1, 0, 1, false);
	}

	@Override
	public void addRecipes() {
		addRecipe(fluidStack("helium", 8000), fluidStack("liquidhelium", 10), NCConfig.processor_time[7]);
		addRecipe(fluidStack("water", 1000), fluidStack("ice", 1000), NCConfig.processor_time[7]/8);
	}

	@Override
	public String getRecipeName() {
		return "supercooler";
	}
}
