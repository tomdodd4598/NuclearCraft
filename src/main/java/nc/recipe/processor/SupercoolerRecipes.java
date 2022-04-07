package nc.recipe.processor;

import static nc.util.FluidStackHelper.BUCKET_VOLUME;

import java.util.*;

import nc.recipe.BasicRecipeHandler;

public class SupercoolerRecipes extends BasicRecipeHandler {
	
	public SupercoolerRecipes() {
		super("supercooler", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStack("helium", BUCKET_VOLUME * 8), fluidStack("liquid_helium", 25), 1D, 1D);
		addRecipe(fluidStack("nitrogen", BUCKET_VOLUME * 8), fluidStack("liquid_nitrogen", 25), 0.5D, 0.5D);
		addRecipe(fluidStack("water", BUCKET_VOLUME / 4), fluidStack("ice", BUCKET_VOLUME / 4), 0.25D, 0.5D);
		addRecipe(fluidStack("emergency_coolant_heated", BUCKET_VOLUME / 4), fluidStack("emergency_coolant", BUCKET_VOLUME / 4), 0.25D, 1D);
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
