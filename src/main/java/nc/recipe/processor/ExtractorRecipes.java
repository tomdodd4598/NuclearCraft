package nc.recipe.processor;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;
import net.minecraft.init.Blocks;

public class ExtractorRecipes extends BaseRecipeHandler {
	
	public ExtractorRecipes() {
		super("extractor", 1, 0, 1, 1);
	}

	@Override
	public void addRecipes() {
		addRecipe("turfMoon", Blocks.GRAVEL, fluidStack("helium3", 125), NCConfig.processor_time[16]);
	}
}
