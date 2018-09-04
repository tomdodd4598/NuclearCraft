package nc.recipe.multiblock;

import nc.config.NCConfig;
import nc.recipe.ProcessorRecipeHandler;

public class HeatExchangerRecipes extends ProcessorRecipeHandler {
	
	public HeatExchangerRecipes() {
		super("heat_exchanger", 0, 2, 0, 2);
	}
	
	@Override
	public void addRecipes() {
		for (int i = 0; i < COOLANTS.length; i++) {
			addRecipe(fluidStack(COOLANTS[i] + "nak_hot", NCConfig.salt_fission_cooling_max_rate), fluidStack("water", (int)NCConfig.salt_fission_cooling_rate[i]), fluidStack(COOLANTS[i] + "nak", NCConfig.salt_fission_cooling_max_rate), fluidStack("high_pressure_steam", (int)NCConfig.salt_fission_cooling_rate[i]), 1D);
			addRecipe(fluidStack(COOLANTS[i] + "nak_hot", NCConfig.salt_fission_cooling_max_rate), fluidStack("preheated_water", 2*(int)NCConfig.salt_fission_cooling_rate[i]), fluidStack(COOLANTS[i] + "nak", NCConfig.salt_fission_cooling_max_rate), fluidStack("high_pressure_steam", 2*(int)NCConfig.salt_fission_cooling_rate[i]), 0.75D);
		}
		
		addRecipe(fluidStack("exhaust_steam", 1000), fluidStack("condensate_water", 1000), fluidStack("low_pressure_steam", 1000), fluidStack("preheated_water", 1000), 1D);
		addRecipe(fluidStack("high_pressure_steam", 1000), fluidStack("condensate_water", 1000), fluidStack("steam", 1000), fluidStack("preheated_water", 1000), 1D);
	}
	
	static final String[] COOLANTS = new String[] {"", "redstone_", "quartz_", "gold_", "glowstone_", "lapis_", "diamond_", "liquidhelium_", "ender_", "cryotheum_", "iron_", "emerald_", "copper_", "tin_", "magnesium_"};
}
