package nc.recipe.multiblock;

import static nc.config.NCConfig.*;

import java.util.List;

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
	protected void setStats() {}
	
	@Override
	protected List<Object> fixedExtras(List<Object> extras) {
		ExtrasFixer fixer = new ExtrasFixer(extras);
		fixer.add(Integer.class, 0);
		fixer.add(Double.class, 1D);
		return fixer.fixed;
	}
}
