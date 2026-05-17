package nc.recipe.multiblock;

import nc.recipe.BasicRecipeHandler;

import java.util.*;

public class CondenserRecipes extends BasicRecipeHandler {
	
	public CondenserRecipes() {
		super("condenser", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStack("exhaust_steam", 16), fluidStack("condensate_water", 1), 32D, 550, 350, -1, 1D);
		addRecipe(fluidStack("low_quality_steam", 32), fluidStack("condensate_water", 1), 2D, 350, 350, -1, 0.5D);
	}
	
	@Override
	protected List<Object> fixedExtras(List<Object> extras) {
		ExtrasFixer fixer = new ExtrasFixer(extras);
		fixer.add(Double.class, 1D);
		fixer.add(Integer.class, 300);
		fixer.add(Integer.class, 300);
		fixer.add(Integer.class, 0);
		fixer.add(Double.class, 0D);
		return fixer.fixed;
	}
	
	@Override
	public List<Object> getFactoredExtras(List<Object> extras, int factor) {
		List<Object> factored = new ArrayList<>(extras);
		factored.set(0, (double) extras.get(0) / factor);
		return factored;
	}
}
