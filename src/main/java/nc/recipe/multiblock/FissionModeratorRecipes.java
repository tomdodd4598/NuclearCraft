package nc.recipe.multiblock;

import static nc.config.NCConfig.*;

import java.util.*;

import nc.init.NCBlocks;
import nc.recipe.BasicRecipeHandler;

public class FissionModeratorRecipes extends BasicRecipeHandler {
	
	public FissionModeratorRecipes() {
		super("fission_moderator", 1, 0, 0, 0);
	}
	
	@Override
	public void addRecipes() {
		addRecipe("blockGraphite", fission_moderator_flux_factor[0], fission_moderator_efficiency[0]);
		addRecipe("blockBeryllium", fission_moderator_flux_factor[1], fission_moderator_efficiency[1]);
		addRecipe(NCBlocks.heavy_water_moderator, fission_moderator_flux_factor[2], fission_moderator_efficiency[2]);
	}
	
	@Override
	public List<Object> fixExtras(List<Object> extras) {
		List<Object> fixed = new ArrayList<>(2);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Integer ? (int) extras.get(0) : 0);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof Double ? (double) extras.get(1) : 1D);
		return fixed;
	}
}
