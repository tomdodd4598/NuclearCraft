package nc.recipe.multiblock;

import static nc.config.NCConfig.rf_per_eu;

import java.util.*;

import com.google.common.collect.Lists;

import nc.recipe.BasicRecipeHandler;

public class FissionHeatingRecipes extends BasicRecipeHandler {
	
	public FissionHeatingRecipes() {
		super("fission_heating", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStackList(Lists.newArrayList("water", "condensate_water"), 1), fluidStack("high_pressure_steam", 4), 64);
		addRecipe(fluidStack("preheated_water", 1), fluidStack("high_pressure_steam", 4), 32);
		addRecipe(fluidStack("ic2coolant", 1), fluidStack("ic2hot_coolant", 1), 10 * rf_per_eu);
	}
	
	@Override
	public List<Object> fixExtras(List<Object> extras) {
		List<Object> fixed = new ArrayList<>(1);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Integer ? (int) extras.get(0) : 64);
		return fixed;
	}
}
