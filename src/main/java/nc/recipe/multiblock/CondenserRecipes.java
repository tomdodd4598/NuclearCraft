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
	protected List<Object> fixedExtras(List<Object> extras) {
		ExtrasFixer fixer = new ExtrasFixer(extras);
		fixer.add(Double.class, 40D);
		fixer.add(Integer.class, 300);
		return fixer.fixed;
	}
	
	@Override
	public List<Object> getFactoredExtras(List<Object> extras, int factor) {
		List<Object> factored = new ArrayList<>(extras);
		factored.set(0, (double) extras.get(0) / factor);
		return factored;
	}
}
