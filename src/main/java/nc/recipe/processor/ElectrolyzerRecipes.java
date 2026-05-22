package nc.recipe.processor;

import java.util.*;

import static nc.config.NCConfig.*;
import static nc.util.FissionHelper.FISSION_FLUID;
import static nc.util.FluidStackHelper.*;

public class ElectrolyzerRecipes extends BasicProcessorRecipeHandler {
	
	public ElectrolyzerRecipes() {
		super("electrolyzer", 0, 1, 0, 4);
	}
	
	@Override
	public void addRecipes() {
		if (!default_processor_recipes_global || !default_processor_recipes[8]) {
			return;
		}
		
		addRecipe(fluidStack("water", BUCKET_VOLUME / 2), fluidStack("hydrogen", BUCKET_VOLUME / 2), fluidStack("oxygen", BUCKET_VOLUME / 4), emptyFluidStack(), emptyFluidStack(), 1D, 1D);
		addRecipe(fluidStack("le_water", BUCKET_VOLUME / 2), fluidStack("hydrogen", 3 * BUCKET_VOLUME / 8), fluidStack("deuterium", BUCKET_VOLUME / 8), fluidStack("oxygen", BUCKET_VOLUME / 4), emptyFluidStack(), 1D, 1D);
		addRecipe(fluidStack("he_water", BUCKET_VOLUME / 2), fluidStack("hydrogen", BUCKET_VOLUME / 4), fluidStack("deuterium", BUCKET_VOLUME / 4), fluidStack("oxygen", BUCKET_VOLUME / 4), emptyFluidStack(), 1D, 1D);
		addRecipe(fluidStack("heavy_water", BUCKET_VOLUME / 2), fluidStack("deuterium", BUCKET_VOLUME / 2), fluidStack("oxygen", BUCKET_VOLUME / 4), emptyFluidStack(), emptyFluidStack(), 1D, 1D);
		addRecipe(fluidStack("hydrofluoric_acid", BUCKET_VOLUME / 4), fluidStack("hydrogen", BUCKET_VOLUME / 4), fluidStack("fluorine", BUCKET_VOLUME / 4), emptyFluidStack(), emptyFluidStack(), 1D, 1D);
		
		addRecipe(fluidStack("naoh", GEM_VOLUME / 2), fluidStack("sodium", INGOT_VOLUME / 2), fluidStack("water", BUCKET_VOLUME / 4), fluidStack("oxygen", BUCKET_VOLUME / 8), emptyFluidStack(), 1D, 1D);
		addRecipe(fluidStack("koh", GEM_VOLUME / 2), fluidStack("potassium", INGOT_VOLUME / 2), fluidStack("water", BUCKET_VOLUME / 4), fluidStack("oxygen", BUCKET_VOLUME / 8), emptyFluidStack(), 1D, 1D);
		
		addRecipe(fluidStack("alumina", INGOT_VOLUME / 2), fluidStack("aluminum", INGOT_VOLUME), fluidStack("oxygen", BUCKET_VOLUME * 3 / 4), emptyFluidStack(), emptyFluidStack(), 2D, 1D);
		
		addRecipe(fluidStack("ammonium_bisulfate_solution", GEM_VOLUME), fluidStack("ammonium_persulfate_solution", GEM_VOLUME / 2), fluidStack("hydrogen", BUCKET_VOLUME / 2), emptyFluidStack(), emptyFluidStack(), 1D, 1D);
		
		addRecipe(fluidStack("hodybef_vapor", BUCKET_VOLUME / 4), fluidStack("holmium", INGOT_VOLUME / 4), fluidStack("dysprosium", INGOT_VOLUME / 4), fluidStack("beryllium", INGOT_VOLUME / 2), fluidStack("fluorine", BUCKET_VOLUME * 3 / 2), 2D, 1D);
		
		// Fission Materials
		addFissionFluorideRecipes();
	}
	
	public void addFissionFluorideRecipes() {
		for (String element : FISSION_FLUID) {
			addRecipe(fluidStack(element + "_fluoride", INGOT_VOLUME / 2), fluidStack(element, INGOT_VOLUME / 2), fluidStack("fluorine", BUCKET_VOLUME / 2), emptyFluidStack(), emptyFluidStack(), 0.5D, 1D);
			addRecipe(fluidStack("depleted_" + element + "_fluoride", INGOT_VOLUME / 2), fluidStack("depleted_" + element, INGOT_VOLUME / 2), fluidStack("fluorine", BUCKET_VOLUME / 2), emptyFluidStack(), emptyFluidStack(), 0.5D, 1D);
		}
	}
	
	@Override
	public List<Object> getFactoredExtras(List<Object> extras, int factor) {
		List<Object> factored = new ArrayList<>(extras);
		factored.set(0, (double) extras.get(0) / factor);
		return factored;
	}
}
