package nc.recipe.multiblock;

import java.util.*;

import nc.recipe.BasicRecipeHandler;

public class CondenserRecipes extends BasicRecipeHandler {
	
	public CondenserRecipes() {
		super("condenser", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStack("exhaust_steam", 16), fluidStack("condensate_water", 1), 1D, 500);
		addRecipe(fluidStack("low_quality_steam", 32), fluidStack("condensate_water", 1), 1D, 350);
	}
	
	@Override
	public List<Object> fixExtras(List<Object> extras) {
		List<Object> fixed = new ArrayList<>(2);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Double ? (double) extras.get(0) : 40D);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof Integer ? (int) extras.get(1) : 300);
		return fixed;
	}
	
	@Override
	public List<Object> getFactoredExtras(List<Object> extras, int factor) {
		List<Object> factored = new ArrayList<>(extras);
		factored.set(0, (double) extras.get(0) / factor);
		return factored;
	}
}
