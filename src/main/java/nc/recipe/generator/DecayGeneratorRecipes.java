package nc.recipe.generator;

import nc.config.NCConfig;
import nc.radiation.RadSources;
import nc.recipe.ProcessorRecipeHandler;
import nc.util.OreDictHelper;

public class DecayGeneratorRecipes extends ProcessorRecipeHandler {
	
	public DecayGeneratorRecipes() {
		super("decay_generator", 1, 0, 1, 0);
	}
	
	// Decay time (minutes) = Sum of natural logs of real decay times (years). Ignore if < 1 yr. If decays to lead, add 5 if past thorium.
	
	@Override
	public void addRecipes() {
		addRecipe("blockThorium", "blockLead", NCConfig.decay_lifetime[0], 1D*NCConfig.decay_power[0], RadSources.THORIUM*9D);
		addRecipe("blockUranium", "blockUranium238", NCConfig.decay_lifetime[1], 1D*NCConfig.decay_power[1], RadSources.URANIUM*9D);
		
		addRecipe("blockUranium238", "blockThorium230", NCConfig.decay_lifetime[3], 1D*NCConfig.decay_power[3], RadSources.URANIUM_238*9D);
		addRecipe("blockNeptunium237", OreDictHelper.oreExists("blockBismuth") ? "blockBismuth" : "blockLead", NCConfig.decay_lifetime[4], 1D*NCConfig.decay_power[4], RadSources.NEPTUNIUM_237*9D);
		addRecipe("blockPlutonium242", "blockUranium238", NCConfig.decay_lifetime[5], 1D*NCConfig.decay_power[5], RadSources.PLUTONIUM_242*9D);
		addRecipe("blockAmericium243", "blockLead", NCConfig.decay_lifetime[6], 1D*NCConfig.decay_power[6], RadSources.AMERICIUM_243*9D);
		addRecipe("blockCurium246", "blockPlutonium242", NCConfig.decay_lifetime[7], 1D*NCConfig.decay_power[7], RadSources.CURIUM_246*9D);
		addRecipe("blockBerkelium247", "blockAmericium243", NCConfig.decay_lifetime[8], 1D*NCConfig.decay_power[8], RadSources.BERKELIUM_247*9D);
		addRecipe("blockCalifornium252", "blockLead", NCConfig.decay_lifetime[9], 1D*NCConfig.decay_power[9], RadSources.CALIFORNIUM_252*9D);
	}
}
