package nc.recipe.processor;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;

public class IrradiatorRecipes extends BaseRecipeHandler {
	
	public IrradiatorRecipes() {
		super(0, 2, 0, 2, true);
	}

	@Override
	public void addRecipes() {
		//addRecipe(fluidStack("helium3", 1000), fluidStack("neutron", 10), fluidStack("hydrogen", 1000), fluidStack("tritium", 1000), NCConfig.processor_time[9]);
		addRecipe(fluidStack("lithium6", 144), fluidStack("neutron", 10), fluidStack("tritium", 2000), fluidStack("helium", 2000), NCConfig.processor_time[9]);
		addRecipe(fluidStack("boron10", 144), fluidStack("neutron", 10), fluidStack("tritium", 2000), fluidStack("helium", 4000), NCConfig.processor_time[9]);
	}

	@Override
	public String getRecipeName() {
		return "irradiator";
	}
}
