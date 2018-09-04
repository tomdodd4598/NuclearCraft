package nc.recipe.generator;

import nc.config.NCConfig;
import nc.radiation.RadSources;
import nc.recipe.ProcessorRecipeHandler;

public class DecayGeneratorRecipes extends ProcessorRecipeHandler {
	
	public DecayGeneratorRecipes() {
		super("decay_generator", 1, 0, 1, 0);
	}
	
	// Decay time (minutes) = Sum of natural logs of real decay times (years). If decays to lead, add 5 if past thorium.
	
	@Override
	public void addRecipes() {
		addRecipe("blockThorium", "blockThorium230", 32.0D*1200D, 1D*NCConfig.decay_power[0], RadSources.THORIUM*9D);
		addRecipe("blockUranium", "blockUranium238", 49.2D*1200D, 1D*NCConfig.decay_power[1], RadSources.URANIUM*9D);
		
		addRecipe("blockThorium230", "blockLead", 16.2D*1200D, 1D*NCConfig.decay_power[2], RadSources.THORIUM_230*9D);
		addRecipe("blockUranium238", "blockLead", 50.9D*1200D, 1D*NCConfig.decay_power[3], RadSources.URANIUM_238*9D);
		addRecipe("blockNeptunium237", "blockLead", 40.4D*1200D, 1D*NCConfig.decay_power[4], RadSources.NEPTUNIUM_237*9D);
		addRecipe("blockPlutonium242", "blockUranium238", 12.8D*1200D, 1D*NCConfig.decay_power[5], RadSources.PLUTONIUM_242*9D);
		addRecipe("blockAmericium243", "blockLead", 54.8D*1200D, 1D*NCConfig.decay_power[6], RadSources.AMERICIUM_243*9D);
		addRecipe("blockCurium246", "blockPlutonium242", 8.5D*1200D, 1D*NCConfig.decay_power[7], RadSources.CURIUM_246*9D);
		addRecipe("blockBerkelium247", "blockAmericium243", 7.2D*1200D, 1D*NCConfig.decay_power[8], RadSources.BERKELIUM_247*9D);
		addRecipe("blockCalifornium252", "blockLead", 86.0D*1200D, 1D*NCConfig.decay_power[9], RadSources.CALIFORNIUM_252*9D);
	}
}
