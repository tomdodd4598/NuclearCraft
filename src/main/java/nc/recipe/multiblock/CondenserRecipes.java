package nc.recipe.multiblock;

import java.util.ArrayList;
import java.util.List;

import nc.recipe.ProcessorRecipeHandler;

public class CondenserRecipes extends ProcessorRecipeHandler {
	
	public CondenserRecipes() {
		super("condenser", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStack("exhaust_steam", 800), fluidStack("condensate_water", 50), 40D, 500);
		addRecipe(fluidStack("low_quality_steam", 800), fluidStack("condensate_water", 25), 20D, 350);
	}
	
	@Override
	public List fixExtras(List extras) {
		List fixed = new ArrayList(2);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Double ? (double) extras.get(0) : 40D);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof Integer ? (int) extras.get(1) : 300);
		return fixed;
	}
	
	@Override
	public List getFactoredExtras(List extras, int factor) {
		List factored = new ArrayList(extras);
		factored.set(0, (double)extras.get(0)/factor);
		return factored;
	}
}
