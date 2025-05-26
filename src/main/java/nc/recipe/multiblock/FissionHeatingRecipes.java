package nc.recipe.multiblock;

import nc.recipe.BasicRecipeHandler;

import java.util.List;

import static nc.config.NCConfig.*;
import static nc.init.NCCoolantFluids.COOLANTS;

public class FissionHeatingRecipes extends BasicRecipeHandler {
	
	public FissionHeatingRecipes() {
		super("fission_heating", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStack("water", 1), fluidStack("high_pressure_steam", 4), 64);
		addRecipe(fluidStack("preheated_water", 1), fluidStack("high_pressure_steam", 4), 32);
		addRecipe(fluidStack("ic2coolant", 1), fluidStack("ic2hot_coolant", 1), 10 * rf_per_eu);
		
		// Hot NaK -> NaK
		
		addRecipe(fluidStack("nak", 1), fluidStack("nak_hot", 1), fission_heater_cooling_rate[0]);
		for (int i = 1; i < COOLANTS.size(); ++i) {
			addRecipe(fluidStack(COOLANTS.get(i) + "_nak", 1), fluidStack(COOLANTS.get(i) + "_nak_hot", 1), fission_heater_cooling_rate[i]);
		}
	}
	
	@Override
	protected List<Object> fixedExtras(List<Object> extras) {
		ExtrasFixer fixer = new ExtrasFixer(extras);
		fixer.add(Integer.class, 64);
		return fixer.fixed;
	}
}
