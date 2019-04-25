package nc.recipe.multiblock;

import nc.recipe.ProcessorRecipeHandler;

public class CondenserRecipes extends ProcessorRecipeHandler {
	
	public CondenserRecipes() {
		super("condenser", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStack("exhaust_steam", 1000), fluidStack("condensate_water", 50), 40D, 500);
		addRecipe(fluidStack("low_quality_steam", 1000), fluidStack("condensate_water", 25), 20D, 350);
	}
}
