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
		
		// Gas -> Hot Gas
		
		for (int i = 0; i < 16; ++i) {
			String coolant = GasCoolerRecipes.COOLANTS[i];
			addRecipe(fluidStack(coolant, 1), fluidStack(coolant + "_hot", 2), (int) (fission_cooler_coolant_heat_per_mb * fission_heating_gas_coolant_heat_mult));
		}
		
		// NaK -> Hot NaK
		
		addRecipe(fluidStack("nak", 1), fluidStack("nak_hot", 1), (int) (fission_heater_coolant_heat_per_mb * fission_heating_nak_coolant_heat_mult));
		for (int i = 1; i < COOLANTS.size(); ++i) {
			addRecipe(fluidStack(COOLANTS.get(i) + "_nak", 1), fluidStack(COOLANTS.get(i) + "_nak_hot", 1), (int) (fission_heater_coolant_heat_per_mb * fission_heating_nak_coolant_heat_mult));
		}
	}
	
	@Override
	protected List<Object> fixedExtras(List<Object> extras) {
		ExtrasFixer fixer = new ExtrasFixer(extras);
		fixer.add(Integer.class, 64);
		return fixer.fixed;
	}
}
