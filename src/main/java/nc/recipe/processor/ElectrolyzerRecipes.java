package nc.recipe.processor;

import static nc.util.FissionHelper.FISSION_FLUID;
import static nc.util.FluidStackHelper.*;

import java.util.*;

import nc.recipe.BasicRecipeHandler;

public class ElectrolyzerRecipes extends BasicRecipeHandler {
	
	public ElectrolyzerRecipes() {
		super("electrolyzer", 0, 1, 0, 4);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStack("water", BUCKET_VOLUME / 2), fluidStack("hydrogen", BUCKET_VOLUME / 2), fluidStack("oxygen", BUCKET_VOLUME / 4), emptyFluidStack(), emptyFluidStack(), 0.5D, 1D);
		addRecipe(fluidStack("heavy_water", BUCKET_VOLUME / 2), fluidStack("deuterium", BUCKET_VOLUME / 2), fluidStack("oxygen", BUCKET_VOLUME / 4), emptyFluidStack(), emptyFluidStack(), 0.5D, 1D);
		addRecipe(fluidStack("hydrofluoric_acid", BUCKET_VOLUME / 4), fluidStack("hydrogen", BUCKET_VOLUME / 4), fluidStack("fluorine", BUCKET_VOLUME / 4), emptyFluidStack(), emptyFluidStack(), 0.5D, 1D);
		
		addRecipe(fluidStack("naoh", GEM_VOLUME / 2), fluidStack("sodium", INGOT_VOLUME / 2), fluidStack("water", BUCKET_VOLUME / 4), fluidStack("oxygen", BUCKET_VOLUME / 8), emptyFluidStack(), 0.5D, 1D);
		addRecipe(fluidStack("koh", GEM_VOLUME / 2), fluidStack("potassium", INGOT_VOLUME / 2), fluidStack("water", BUCKET_VOLUME / 4), fluidStack("oxygen", BUCKET_VOLUME / 8), emptyFluidStack(), 0.5D, 1D);
		
		addRecipe(fluidStack("alumina", INGOT_VOLUME / 2), fluidStack("aluminum", INGOT_VOLUME), fluidStack("oxygen", BUCKET_VOLUME * 3 / 4), emptyFluidStack(), emptyFluidStack(), 1D, 1D);
		
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
	public List<Object> fixExtras(List<Object> extras) {
		List<Object> fixed = new ArrayList<>(3);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Double ? (double) extras.get(0) : 1D);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof Double ? (double) extras.get(1) : 1D);
		fixed.add(extras.size() > 2 && extras.get(2) instanceof Double ? (double) extras.get(2) : 0D);
		return fixed;
	}
	
	@Override
	public List<Object> getFactoredExtras(List<Object> extras, int factor) {
		List<Object> factored = new ArrayList<>(extras);
		factored.set(0, (double) extras.get(0) / factor);
		return factored;
	}
}
