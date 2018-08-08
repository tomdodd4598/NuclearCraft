package nc.recipe.processor;

import nc.recipe.ProcessorRecipeHandler;
import nc.util.FluidStackHelper;

public class IrradiatorRecipes extends ProcessorRecipeHandler {
	
	public IrradiatorRecipes() {
		super("irradiator", 0, 2, 0, 2);
	}

	@Override
	public void addRecipes() {
		//addRecipe(fluidStack("helium3", FluidStackHelper.BUCKET_VOLUME), fluidStack("neutron", FluidStackHelper.PARTICLE_VOLUME), fluidStack("hydrogen", FluidStackHelper.BUCKET_VOLUME), fluidStack("tritium", FluidStackHelper.BUCKET_VOLUME), 1D, 1D);
		addRecipe(fluidStack("lithium6", FluidStackHelper.INGOT_VOLUME), fluidStack("neutron", FluidStackHelper.PARTICLE_VOLUME), fluidStack("tritium", FluidStackHelper.BUCKET_VOLUME), fluidStack("helium", FluidStackHelper.BUCKET_VOLUME), 1D, 1D);
		addRecipe(fluidStack("boron10", FluidStackHelper.INGOT_VOLUME), fluidStack("neutron", FluidStackHelper.PARTICLE_VOLUME), fluidStack("tritium", FluidStackHelper.BUCKET_VOLUME), fluidStack("helium", FluidStackHelper.BUCKET_VOLUME*2), 1D, 1D);
	}
}
