package nc.recipe.multiblock;

import nc.recipe.ProcessorRecipeHandler;

public class TurbineRecipes extends ProcessorRecipeHandler {
	
	public TurbineRecipes() {
		super("high_turbine", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStack("high_pressure_steam", 1000), fluidStack("exhaust_steam", 4000), 1D, 1D);
		addRecipe(fluidStack("low_pressure_steam", 1000), fluidStack("low_quality_steam", 2000), 1D, 1D);
	}
}
