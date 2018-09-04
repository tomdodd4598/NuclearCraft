package nc.recipe.multiblock;

import nc.recipe.ProcessorRecipeHandler;

public class LowTurbineRecipes extends ProcessorRecipeHandler {
	
	public LowTurbineRecipes() {
		super("low_turbine", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStack("low_pressure_steam", 1000), fluidStack("low_quality_steam", 1000), 1D, 1D);
	}
}
