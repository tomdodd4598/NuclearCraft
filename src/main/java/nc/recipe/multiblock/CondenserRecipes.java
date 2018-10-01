package nc.recipe.multiblock;

import nc.recipe.ProcessorRecipeHandler;

public class CondenserRecipes extends ProcessorRecipeHandler {
	
	public CondenserRecipes() {
		super("condenser", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStack("low_quality_steam", 4000), fluidStack("condensate_water", 100), 1D);
		addRecipe(fluidStack("high_pressure_steam", 500), fluidStack("condensate_water", 100), 1D);
	}
}
