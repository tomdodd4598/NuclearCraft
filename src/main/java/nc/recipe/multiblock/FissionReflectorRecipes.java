package nc.recipe.multiblock;

import nc.config.NCConfig;
import nc.recipe.ProcessorRecipeHandler;

public class FissionReflectorRecipes extends ProcessorRecipeHandler {
	
	public FissionReflectorRecipes() {
		super("fission_reflector", 1, 0, 0, 0);
	}
	
	@Override
	public void addRecipes() {
		addRecipe("blockNeutronReflector", NCConfig.fission_reflector_stats[0], NCConfig.fission_reflector_stats[1]);
	}
}
