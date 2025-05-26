package nc.recipe.multiblock;

import nc.recipe.BasicRecipeHandler;

import java.util.*;

import static nc.config.NCConfig.*;
import static nc.init.NCCoolantFluids.COOLANTS;

public class HeatExchangerRecipes extends BasicRecipeHandler {
	
	public HeatExchangerRecipes() {
		super("heat_exchanger", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		// Water <-> Steam
		
		addRecipe(fluidStack("water", 1), fluidStack("high_pressure_steam", 4), 64D, 300, 600, false, 1, 0.5D);
		addRecipe(fluidStack("preheated_water", 1), fluidStack("high_pressure_steam", 4), 32D, 400, 600, false, 1, 0.5D);
		
		if (heat_exchanger_alternate_hps_recipe) {
			addRecipe(fluidStack("high_pressure_steam", 1), fluidStack("steam", 4), 4D, 600, 550);
		}
		else {
			addRecipe(fluidStack("high_pressure_steam", 1), fluidStack("low_pressure_steam", 4), 4D, 600, 550);
		}
		
		if (heat_exchanger_alternate_exhaust_recipe) {
			addRecipe(fluidStack("exhaust_steam", 1), fluidStack("steam", 1), 1D, 400, 550);
		}
		else {
			addRecipe(fluidStack("exhaust_steam", 1), fluidStack("low_pressure_steam", 1), 1D, 400, 550);
		}
		
		addRecipe(fluidStack("low_pressure_steam", 16), fluidStack("condensate_water", 1), 32D, 550, 350, false, -1, 0.5D);
		
		addRecipe(fluidStack("low_quality_steam", 32), fluidStack("condensate_water", 1), 2D, 350, 350, false, -1, 0.5D);
		
		addRecipe(fluidStack("condensate_water", 1), fluidStack("preheated_water", 1), 8D, 350, 400, false, 0, 0.5D);
		
		// Hot NaK -> NaK
		
		addRecipe(fluidStack("nak_hot", 1), fluidStack("nak", 1), fission_heater_cooling_rate[0] * heat_exchanger_coolant_heat_mult, 800, 400);
		for (int i = 1; i < COOLANTS.size(); ++i) {
			addRecipe(fluidStack(COOLANTS.get(i) + "_nak_hot", 1), fluidStack(COOLANTS.get(i) + "_nak", 1), fission_heater_cooling_rate[i] * heat_exchanger_coolant_heat_mult, 800, 400);
		}
	}
	
	@Override
	protected List<Object> fixedExtras(List<Object> extras) {
		ExtrasFixer fixer = new ExtrasFixer(extras);
		fixer.add(Double.class, 1D);
		fixer.add(Integer.class, 300);
		fixer.add(Integer.class, 300);
		fixer.add(Boolean.class, false);
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
