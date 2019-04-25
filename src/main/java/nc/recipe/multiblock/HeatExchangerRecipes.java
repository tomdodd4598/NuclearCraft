package nc.recipe.multiblock;

import nc.config.NCConfig;
import nc.recipe.ProcessorRecipeHandler;

public class HeatExchangerRecipes extends ProcessorRecipeHandler {
	
	public HeatExchangerRecipes() {
		super("heat_exchanger", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		// Hot NaK -> NaK
		
		for (int i = 0; i < COOLANTS.length; i++) {
			addRecipe(fluidStack(COOLANTS[i] + "nak_hot", NCConfig.salt_fission_cooling_max_rate), fluidStack(COOLANTS[i] + "nak", NCConfig.salt_fission_cooling_max_rate), NCConfig.salt_fission_cooling_rate[i]*NCConfig.heat_exchanger_coolant_mult, 700, 300);
		}
		
		// Steam <-> Water
		
		addRecipe(fluidStack("water", 200), fluidStack("high_pressure_steam", 1000), 32000D, 300, 1200);
		addRecipe(fluidStack("preheated_water", 200), fluidStack("high_pressure_steam", 1000), 16000D, 400, 1200);
		
		if (!NCConfig.heat_exchanger_alternate_exhaust_recipe) {
			addRecipe(fluidStack("exhaust_steam", 1000), fluidStack("low_pressure_steam", 1000), 4000D, 500, 800);
		}
		else addRecipe(fluidStack("exhaust_steam", 1000), fluidStack("steam", 1000), 4000D, 500, 800);
		
		addRecipe(fluidStack("high_pressure_steam", 250), fluidStack("steam", 1000), 4000D, 1200, 800);
		
		addRecipe(fluidStack("condensate_water", 1000), fluidStack("preheated_water", 1000), 32000D, 300, 400);
	}
	
	private static final String[] COOLANTS = new String[] {"", "redstone_", "quartz_", "gold_", "glowstone_", "lapis_", "diamond_", "liquidhelium_", "ender_", "cryotheum_", "iron_", "emerald_", "copper_", "tin_", "magnesium_"};
}
