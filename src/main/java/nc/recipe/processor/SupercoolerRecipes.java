package nc.recipe.processor;

import static nc.util.FluidStackHelper.BUCKET_VOLUME;

import java.util.*;

public class SupercoolerRecipes extends BasicProcessorRecipeHandler {
	
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
	public List<Object> getFactoredExtras(List<Object> extras, int factor) {
		List<Object> factored = new ArrayList<>(extras);
		factored.set(0, (double) extras.get(0) / factor);
		return factored;
	}
}
