package nc.recipe.processor;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;

public class IrradiatorRecipes extends BaseRecipeHandler {
	
	private static final IrradiatorRecipes RECIPES = new IrradiatorRecipes();
	
	public IrradiatorRecipes() {
		super(0, 2, 0, 2, true);
	}

	public static final IrradiatorRecipes instance() {
		return RECIPES;
	}

	public void addRecipes() {
		addRecipe(fluidStack("helium3", 1000), fluidStack("neutron", 50), fluidStack("hydrogen", 1000), fluidStack("tritium", 1000), NCConfig.processor_time[9]);
		addRecipe(fluidStack("lithium6", 144), fluidStack("neutron", 50), fluidStack("tritium", 2000), fluidStack("helium", 2000), NCConfig.processor_time[9]);
		addRecipe(fluidStack("boron10", 144), fluidStack("neutron", 50), fluidStack("tritium", 2000), fluidStack("helium", 4000), NCConfig.processor_time[9]);
	}

	public String getRecipeName() {
		return "irradiator";
	}
}
