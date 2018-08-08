package nc.recipe.generator;

import nc.config.NCConfig;
import nc.recipe.ProcessorRecipeHandler;

public class DecayGeneratorRecipes extends ProcessorRecipeHandler {
	
	public DecayGeneratorRecipes() {
		super("decay_generator", 1, 0, 1, 0);
	}
	
	// Decay time (minutes) = Sum of natural logs of real decay times (years). If decays to lead, add 5 if past thorium.
	
	@Override
	public void addRecipes() {
		addRecipe("blockThorium", "blockDepletedThorium", 32.0D*1200D, 1D*NCConfig.decay_power[0]);
		addRecipe("blockUranium", "blockDepletedUranium", 49.2D*1200D, 1D*NCConfig.decay_power[1]);
		
		addRecipe("blockDepletedThorium", "blockLead", 16.2D*1200D, 1D*NCConfig.decay_power[2]);
		addRecipe("blockDepletedUranium", "blockLead", 50.9D*1200D, 1D*NCConfig.decay_power[3]);
		addRecipe("blockDepletedNeptunium", "blockLead", 40.4D*1200D, 1D*NCConfig.decay_power[4]);
		addRecipe("blockDepletedPlutonium", "blockDepletedUranium", 12.8D*1200D, 1D*NCConfig.decay_power[5]);
		addRecipe("blockDepletedAmericium", "blockLead", 54.8D*1200D, 1D*NCConfig.decay_power[6]);
		addRecipe("blockDepletedCurium", "blockDepletedPlutonium", 8.5D*1200D, 1D*NCConfig.decay_power[7]);
		addRecipe("blockDepletedBerkelium", "blockDepletedAmericium", 7.2D*1200D, 1D*NCConfig.decay_power[8]);
		addRecipe("blockDepletedCalifornium", "blockLead", 86.0D*1200D, 1D*NCConfig.decay_power[9]);
	}
}
