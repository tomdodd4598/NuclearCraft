package nc.recipe.processor;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;

public class SaltMixerRecipes extends BaseRecipeHandler {
	
	public SaltMixerRecipes() {
		super(0, 2, 0, 1, true);
	}

	@Override
	public void addRecipes() {
		addRecipe(fluidStack("lif", 72), fluidStack("bef2", 72), fluidStack("flibe", 144), NCConfig.processor_time[13]);
	}

	@Override
	public String getRecipeName() {
		return "salt_mixer";
	}
}
