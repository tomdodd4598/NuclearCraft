package nc.recipe.processor;

import static nc.util.FissionHelper.FUEL_FLUID;
import static nc.util.FissionHelper.ISOTOPE_FLUID;

import nc.recipe.ProcessorRecipeHandler;
import nc.util.FluidStackHelper;

public class ElectrolyzerRecipes extends ProcessorRecipeHandler {
	
	public ElectrolyzerRecipes() {
		super("electrolyzer", 0, 1, 0, 4);
	}

	@Override
	public void addRecipes() {
		addRecipe(fluidStack("water", FluidStackHelper.BUCKET_VOLUME), fluidStack("hydrogen", FluidStackHelper.BUCKET_VOLUME*2), fluidStack("oxygen", FluidStackHelper.BUCKET_VOLUME), emptyFluidStack(), emptyFluidStack(), 1D, 1D);
		addRecipe(fluidStack("heavy_water", FluidStackHelper.BUCKET_VOLUME), fluidStack("deuterium", FluidStackHelper.BUCKET_VOLUME*2), fluidStack("oxygen", FluidStackHelper.BUCKET_VOLUME), emptyFluidStack(), emptyFluidStack(), 1D, 1D);
		addRecipe(fluidStack("hydrofluoric_acid", FluidStackHelper.BUCKET_VOLUME), fluidStack("hydrogen", FluidStackHelper.BUCKET_VOLUME), fluidStack("fluorine", FluidStackHelper.BUCKET_VOLUME), emptyFluidStack(), emptyFluidStack(), 1D, 0.5D);
		
		addRecipe(fluidStack("naoh", FluidStackHelper.GEM_VOLUME), fluidStack("sodium", FluidStackHelper.INGOT_VOLUME), fluidStack("water", FluidStackHelper.BUCKET_VOLUME), fluidStack("oxygen", FluidStackHelper.BUCKET_VOLUME/2), emptyFluidStack(), 1.5D, 1.5D);
		addRecipe(fluidStack("koh", FluidStackHelper.GEM_VOLUME), fluidStack("potassium", FluidStackHelper.INGOT_VOLUME), fluidStack("water", FluidStackHelper.BUCKET_VOLUME), fluidStack("oxygen", FluidStackHelper.BUCKET_VOLUME/2), emptyFluidStack(), 1.5D, 1.5D);
		
		addRecipe(fluidStack("alumina", FluidStackHelper.INGOT_VOLUME), fluidStack("aluminum", 2*FluidStackHelper.INGOT_VOLUME), fluidStack("oxygen", 3*FluidStackHelper.BUCKET_VOLUME), emptyFluidStack(), emptyFluidStack(), 2D, 1D);
		
		addFissionFuelFluorideRecipes();
		
		addIsotopeZARecipes();
	}
	
	public void addFissionFuelFluorideRecipes() {
		for (int i = 0; i < FUEL_FLUID.length; i++) {
			for (String form : new String[] {"fuel_", "depleted_fuel_"}) {
				addRecipe(fluidStack(form + FUEL_FLUID[i] + "_fluoride", FluidStackHelper.INGOT_VOLUME), fluidStack(form + FUEL_FLUID[i], FluidStackHelper.INGOT_VOLUME), fluidStack("fluorine", FluidStackHelper.BUCKET_VOLUME), emptyFluidStack(), emptyFluidStack(), 0.5D, 1D);
			}
		}
	}
	
	public void addIsotopeZARecipes() {
		for (int i = 0; i < ISOTOPE_FLUID.length; i++) {
			addRecipe(fluidStack(ISOTOPE_FLUID[i] + "_za", FluidStackHelper.INGOT_VOLUME), fluidStack(ISOTOPE_FLUID[i], FluidStackHelper.INGOT_VOLUME), fluidStack("zirconium", FluidStackHelper.INGOT_VOLUME), emptyFluidStack(), emptyFluidStack(), 0.5D, 1D);
		}
	}
}
