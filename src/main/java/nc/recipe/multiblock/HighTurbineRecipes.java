package nc.recipe.multiblock;

import nc.recipe.ProcessorRecipeHandler;

public class HighTurbineRecipes extends ProcessorRecipeHandler {
	
	public HighTurbineRecipes() {
		super("high_turbine", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStack("high_pressure_steam", 1000), fluidStack("exhaust_steam", 1000), 1D, 1D);
	}
}
