package nc.recipe.multiblock;

import java.util.ArrayList;
import java.util.List;

import nc.recipe.ProcessorRecipeHandler;

public class FissionEmergencyCoolingRecipes extends ProcessorRecipeHandler {
	
	public FissionEmergencyCoolingRecipes() {
		super("emergency_cooling", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStack("emergency_coolant", 1), fluidStack("emergency_coolant_heated", 1));
	}
	
	@Override
	public List fixExtras(List extras) {
		return new ArrayList(0);
	}
}
