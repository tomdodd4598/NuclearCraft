package nc.recipe.multiblock;

import com.google.common.collect.Lists;

import nc.config.NCConfig;
import nc.recipe.ProcessorRecipeHandler;

public class FissionHeatingRecipes extends ProcessorRecipeHandler {
	
	public FissionHeatingRecipes() {
		super("fission_heating", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStackList(Lists.newArrayList("water", "condensate_water"), 1), fluidStack("high_pressure_steam", 4), 128);
		addRecipe(fluidStack("preheated_water", 1), fluidStack("high_pressure_steam", 4), 64);
		addRecipe(fluidStack("ic2coolant", 1), fluidStack("ic2hot_coolant", 1), 10*NCConfig.rf_per_eu);
	}	
}
