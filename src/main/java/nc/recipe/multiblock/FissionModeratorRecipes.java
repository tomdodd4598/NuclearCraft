package nc.recipe.multiblock;

import java.util.ArrayList;
import java.util.List;

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
	
	@Override
	public List fixExtras(List extras) {
		List fixed = new ArrayList(2);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Integer ? (int) extras.get(0) : 0);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof Double ? (double) extras.get(1) : 1D);
		return fixed;
	}
}
