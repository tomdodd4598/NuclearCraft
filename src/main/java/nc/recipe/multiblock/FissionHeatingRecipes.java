package nc.recipe.multiblock;

import nc.recipe.ProcessorRecipeHandler;

public class FissionHeatingRecipes extends ProcessorRecipeHandler {
	
	public FissionHeatingRecipes() {
		super("fission_heating", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStack("water", 1), fluidStack("high_pressure_steam", 4), 32);
		addRecipe(fluidStack("preheated_water", 1), fluidStack("high_pressure_steam", 4), 16);
	}	
}
