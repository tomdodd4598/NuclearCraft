package nc.recipe.multiblock;

import nc.config.NCConfig;
import nc.init.NCBlocks;
import nc.recipe.ProcessorRecipeHandler;

public class FissionModeratorRecipes extends ProcessorRecipeHandler {
	
	public FissionModeratorRecipes() {
		super("fission_moderator", 1, 0, 0, 0);
	}
	
	@Override
	public void addRecipes() {
		addRecipe("blockGraphite", NCConfig.fission_moderator_flux_factor[0], NCConfig.fission_moderator_efficiency[0]);
		addRecipe("blockBeryllium", NCConfig.fission_moderator_flux_factor[1], NCConfig.fission_moderator_efficiency[1]);
		addRecipe(NCBlocks.heavy_water_moderator, NCConfig.fission_moderator_flux_factor[2], NCConfig.fission_moderator_efficiency[2]);
	}
}
