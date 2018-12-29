package nc.recipe.multiblock;

import nc.recipe.ProcessorRecipeHandler;

public class CondenserRecipes extends ProcessorRecipeHandler {
	
	public CondenserRecipes() {
		super("condenser", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStack("low_quality_steam", 1000), fluidStack("condensate_water", 25), 1D);
		addRecipe(fluidStack("high_pressure_steam", 1000), fluidStack("condensate_water", 200), 1D);
	}
}
