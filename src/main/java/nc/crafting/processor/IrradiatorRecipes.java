package nc.crafting.processor;

import nc.config.NCConfig;
import nc.handler.ProcessorRecipeHandler;

public class IrradiatorRecipes extends ProcessorRecipeHandler {
	
	private static final IrradiatorRecipes RECIPES = new IrradiatorRecipes();

	public IrradiatorRecipes() {
		super(0, 2, 0, 2, true, true);
	}
	
	public static final ProcessorRecipeHandler instance() {
		return RECIPES;
	}
	
	public void addRecipes() {
		addRecipe(fluidStack("lithium6", 108), fluidStack("neutron", 50), fluidStack("tritium", 2000), fluidStack("helium", 2000), NCConfig.processor_time[9]);
		addRecipe(fluidStack("boron10", 108), fluidStack("neutron", 50), fluidStack("tritium", 2000), fluidStack("helium", 4000), NCConfig.processor_time[9]);
	}
}
