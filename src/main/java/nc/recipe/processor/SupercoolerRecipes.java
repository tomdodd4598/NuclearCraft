package nc.recipe.processor;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;

public class SupercoolerRecipes extends BaseRecipeHandler {
	
	//private static final SupercoolerRecipes RECIPES = new SupercoolerRecipes();
	
	public SupercoolerRecipes() {
		super(0, 1, 0, 1, false);
	}

	/*public static final SupercoolerRecipes instance() {
		return RECIPES;
	}*/

	public void addRecipes() {
		addRecipe(fluidStack("helium", 4000), fluidStack("liquidhelium", 40), NCConfig.processor_time[7]);
		addRecipe(fluidStack("water", 1000), fluidStack("ice", 1000), NCConfig.processor_time[7]);
	}

	public String getRecipeName() {
		return "supercooler";
	}
}
