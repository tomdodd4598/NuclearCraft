package nc.recipe.processor;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;

public class CrystallizerRecipes extends BaseRecipeHandler {
	
	private static final CrystallizerRecipes RECIPES = new CrystallizerRecipes();
	
	public CrystallizerRecipes() {
		super(0, 1, 1, 0, false);
	}

	public static final CrystallizerRecipes instance() {
		return RECIPES;
	}

	public void addRecipes() {
		addRecipe(fluidStack("boron_nitride_solution", 666), "dustBoronNitride", NCConfig.processor_time[14]);
	}

	public String getRecipeName() {
		return "crystallizer";
	}
}
