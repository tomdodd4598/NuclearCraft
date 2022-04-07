package nc.recipe.multiblock;

import static nc.config.NCConfig.fission_emergency_cooling_multiplier;

import java.util.*;

import nc.recipe.BasicRecipeHandler;

public class FissionEmergencyCoolingRecipes extends BasicRecipeHandler {
	
	public FissionEmergencyCoolingRecipes() {
		super("emergency_cooling", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStack("emergency_coolant", 1), fluidStack("emergency_coolant_heated", 1), fission_emergency_cooling_multiplier);
	}
	
	@Override
	public List<Object> fixExtras(List<Object> extras) {
		List<Object> fixed = new ArrayList<>(1);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Double ? (double) extras.get(0) : 1D);
		return fixed;
	}
}
