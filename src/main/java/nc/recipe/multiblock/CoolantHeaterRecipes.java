package nc.recipe.multiblock;

import nc.config.NCConfig;
import nc.recipe.ProcessorRecipeHandler;

public class CoolantHeaterRecipes extends ProcessorRecipeHandler {
	
	public CoolantHeaterRecipes() {
		super("coolant_heater", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		for (int i = 0; i < COOLANTS.length; i++) {
			addRecipe(fluidStack(COOLANTS[i] + "nak", NCConfig.salt_fission_cooling_max_rate), fluidStack(COOLANTS[i] + "nak_hot", NCConfig.salt_fission_cooling_max_rate), NCConfig.salt_fission_cooling_rate[i]);
		}
	}
	
	static final String[] COOLANTS = new String[] {"", "redstone_", "quartz_", "gold_", "glowstone_", "lapis_", "diamond_", "liquidhelium_", "ender_", "cryotheum_", "iron_", "emerald_", "copper_", "tin_", "magnesium_"};
}
