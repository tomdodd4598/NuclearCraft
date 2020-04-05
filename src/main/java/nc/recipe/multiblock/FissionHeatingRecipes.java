package nc.recipe.multiblock;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import nc.config.NCConfig;
import nc.recipe.ProcessorRecipeHandler;

public class FissionHeatingRecipes extends ProcessorRecipeHandler {
	
	public FissionHeatingRecipes() {
		super("fission_heating", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStackList(Lists.newArrayList("water", "condensate_water"), 1), fluidStack("high_pressure_steam", 4), 64);
		addRecipe(fluidStack("preheated_water", 1), fluidStack("high_pressure_steam", 4), 32);
		addRecipe(fluidStack("ic2coolant", 1), fluidStack("ic2hot_coolant", 1), 10*NCConfig.rf_per_eu);
	}
	
	@Override
	public List fixExtras(List extras) {
		List fixed = new ArrayList(1);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Integer ? (int) extras.get(0) : 64);
		return fixed;
	}
}
