package nc.recipe.multiblock;

import static nc.config.NCConfig.*;
import static nc.init.NCCoolantFluids.COOLANTS;

import java.util.*;

import nc.recipe.BasicRecipeHandler;

public class HeatExchangerRecipes extends BasicRecipeHandler {
	
	public HeatExchangerRecipes() {
		super("heat_exchanger", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		// Hot NaK -> NaK
		
		addRecipe(fluidStack("nak_hot", 1), fluidStack("nak", 1), fission_heater_cooling_rate[0] * heat_exchanger_coolant_mult, 700, 300);
		for (int i = 1; i < COOLANTS.size(); ++i) {
			addRecipe(fluidStack(COOLANTS.get(i) + "_nak_hot", 1), fluidStack(COOLANTS.get(i) + "_nak", 1), fission_heater_cooling_rate[i] * heat_exchanger_coolant_mult, 700, 300);
		}
		
		// Steam <-> Water
		
		addRecipe(fluidStack("water", 1), fluidStack("high_pressure_steam", 4), 128D, 300, 1200);
		addRecipe(fluidStack("preheated_water", 1), fluidStack("high_pressure_steam", 4), 64D, 400, 1200);
		
		if (!heat_exchanger_alternate_exhaust_recipe) {
			addRecipe(fluidStack("exhaust_steam", 1), fluidStack("low_pressure_steam", 1), 4D, 500, 800);
		}
		else {
			addRecipe(fluidStack("exhaust_steam", 1), fluidStack("steam", 1), 4D, 500, 800);
		}
		
		addRecipe(fluidStack("high_pressure_steam", 1), fluidStack("steam", 4), 16D, 1200, 800);
		
		addRecipe(fluidStack("condensate_water", 1), fluidStack("preheated_water", 1), 32D, 300, 400);
	}
	
	@Override
	public List<Object> fixExtras(List<Object> extras) {
		List<Object> fixed = new ArrayList<>(3);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Double ? (double) extras.get(0) : 16000D);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof Integer ? (int) extras.get(1) : 300);
		fixed.add(extras.size() > 2 && extras.get(2) instanceof Integer ? (int) extras.get(2) : 300);
		return fixed;
	}
	
	@Override
	public List<Object> getFactoredExtras(List<Object> extras, int factor) {
		List<Object> factored = new ArrayList<>(extras);
		factored.set(0, (double) extras.get(0) / factor);
		return factored;
	}
}
