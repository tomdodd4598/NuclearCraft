package nc.recipe.processor;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;

public class SaltMixerRecipes extends BaseRecipeHandler {
	
	private static final SaltMixerRecipes RECIPES = new SaltMixerRecipes();
	
	public SaltMixerRecipes() {
		super(0, 2, 0, 1, true);
	}

	public static final SaltMixerRecipes instance() {
		return RECIPES;
	}

	public void addRecipes() {
		addRecipe(fluidStack("lif", 500), fluidStack("bef2", 500), fluidStack("flibe", 1000), NCConfig.processor_time[13]);
	}

	public String getRecipeName() {
		return "salt_mixer";
	}
}
