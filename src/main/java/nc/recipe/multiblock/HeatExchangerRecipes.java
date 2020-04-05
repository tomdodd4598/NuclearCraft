package nc.recipe.multiblock;

import static nc.init.NCCoolantFluids.COOLANTS;

import java.util.ArrayList;
import java.util.List;

import nc.config.NCConfig;
import nc.recipe.ProcessorRecipeHandler;

public class HeatExchangerRecipes extends ProcessorRecipeHandler {
	
	public HeatExchangerRecipes() {
		super("heat_exchanger", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		// Hot NaK -> NaK
		
		for (int i = 0; i < COOLANTS.size(); i++) {
			addRecipe(fluidStack(COOLANTS.get(i) + "nak_hot", 20), fluidStack(COOLANTS.get(i) + "nak", 20), NCConfig.fission_heater_cooling_rate[i]*NCConfig.heat_exchanger_coolant_mult, 700, 300);
		}
		
		// Steam <-> Water
		
		addRecipe(fluidStack("water", 250), fluidStack("high_pressure_steam", 1000), 32000D, 300, 1200);
		addRecipe(fluidStack("preheated_water", 250), fluidStack("high_pressure_steam", 1000), 16000D, 400, 1200);
		
		if (!NCConfig.heat_exchanger_alternate_exhaust_recipe) {
			addRecipe(fluidStack("exhaust_steam", 1000), fluidStack("low_pressure_steam", 1000), 4000D, 500, 800);
		}
		else addRecipe(fluidStack("exhaust_steam", 1000), fluidStack("steam", 1000), 4000D, 500, 800);
		
		addRecipe(fluidStack("high_pressure_steam", 250), fluidStack("steam", 1000), 4000D, 1200, 800);
		
		addRecipe(fluidStack("condensate_water", 1000), fluidStack("preheated_water", 1000), 32000D, 300, 400);
	}
	
	@Override
	public List fixExtras(List extras) {
		List fixed = new ArrayList(3);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Double ? (double) extras.get(0) : 16000D);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof Integer ? (int) extras.get(1) : 300);
		fixed.add(extras.size() > 2 && extras.get(2) instanceof Integer ? (int) extras.get(2) : 300);
		return fixed;
	}
	
	@Override
	public List getFactoredExtras(List extras, int factor) {
		List factored = new ArrayList(extras);
		factored.set(0, (double)extras.get(0)/factor);
		return factored;
	}
}
