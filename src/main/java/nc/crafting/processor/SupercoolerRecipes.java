package nc.crafting.processor;

import nc.config.NCConfig;
import nc.handler.ProcessorRecipeHandler;

public class SupercoolerRecipes extends ProcessorRecipeHandler {
	
	private static final SupercoolerRecipes RECIPES = new SupercoolerRecipes();

	public SupercoolerRecipes() {
		super(0, 1, 0, 1, false, true);
	}
	
	public static final ProcessorRecipeHandler instance() {
		return RECIPES;
	}
	
	public void addRecipes() {
		addRecipe(fluidStack("helium", 4000), fluidStack("liquidhelium", 25), NCConfig.processor_time[7]);
		addRecipe(fluidStack("water", 1000), fluidStack("ice", 1000), NCConfig.processor_time[7]);
	}
}
