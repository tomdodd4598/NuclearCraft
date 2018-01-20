package nc.recipe.processor;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;
import nc.util.FluidHelper;

public class SupercoolerRecipes extends BaseRecipeHandler {
	
	public SupercoolerRecipes() {
		super("supercooler", 0, 1, 0, 1, false);
	}

	@Override
	public void addRecipes() {
		addRecipe(fluidStack("helium", FluidHelper.BUCKET_VOLUME*8), fluidStack("liquidhelium", FluidHelper.PARTICLE_VOLUME), NCConfig.processor_time[7]);
		addRecipe(fluidStack("water", FluidHelper.BUCKET_VOLUME), fluidStack("ice", FluidHelper.BUCKET_VOLUME), NCConfig.processor_time[7]/8);
	}
}
