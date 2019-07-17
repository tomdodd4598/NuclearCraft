package nc.recipe.processor;

import nc.recipe.ProcessorRecipeHandler;
import nc.util.FluidStackHelper;

public class SupercoolerRecipes extends ProcessorRecipeHandler {
	
	public SupercoolerRecipes() {
		super("supercooler", 0, 1, 0, 1);
	}

	@Override
	public void addRecipes() {
		addRecipe(fluidStack("helium", FluidStackHelper.BUCKET_VOLUME*8), fluidStack("liquidhelium", 25), 1D, 1D);
		addRecipe(fluidStack("nitrogen", FluidStackHelper.BUCKET_VOLUME*8), fluidStack("liquid_nitrogen", 25), 0.5D, 0.5D);
		addRecipe(fluidStack("water", FluidStackHelper.BUCKET_VOLUME), fluidStack("ice", FluidStackHelper.BUCKET_VOLUME), 0.25D, 0.5D);
	}
}
