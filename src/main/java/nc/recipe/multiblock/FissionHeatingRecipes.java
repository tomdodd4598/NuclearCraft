package nc.recipe.multiblock;

import static nc.config.NCConfig.rf_per_eu;

import java.util.List;

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
	protected void setStats() {}
	
	@Override
	protected List<Object> fixedExtras(List<Object> extras) {
		ExtrasFixer fixer = new ExtrasFixer(extras);
		fixer.add(Integer.class, 64);
		return fixer.fixed;
	}
}
