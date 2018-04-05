package nc.recipe.saltFission;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;

public class CoolantHeaterRecipes extends BaseRecipeHandler {
	
	public CoolantHeaterRecipes() {
		super("coolant_heater", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addRecipe(fluidStack("nak", NCConfig.salt_fission_cooling_max_rate), fluidStack("nak_hot", NCConfig.salt_fission_cooling_max_rate));
		for (String coolant : new String[] {"redstone", "quartz", "gold", "glowstone", "lapis", "diamond", "liquidhelium", "ender", "cryotheum", "iron", "emerald", "copper", "tin", "magnesium"}) {
			addRecipe(fluidStack(coolant + "_nak", NCConfig.salt_fission_cooling_max_rate), fluidStack(coolant + "_nak_hot", NCConfig.salt_fission_cooling_max_rate));
		}
	}
}
