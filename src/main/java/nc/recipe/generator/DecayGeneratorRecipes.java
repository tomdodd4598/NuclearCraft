package nc.recipe.generator;

import java.util.ArrayList;
import java.util.List;

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
		addRecipe("blockThorium", "blockLead", NCConfig.decay_lifetime[0], NCConfig.decay_power[0], RadSources.THORIUM);
		addRecipe("blockUranium", "blockUranium238", NCConfig.decay_lifetime[1], NCConfig.decay_power[1], RadSources.URANIUM);
		
		addRecipe("blockUranium238", OreDictHelper.oreExists("blockRadium") ? "blockRadium" : "blockLead", NCConfig.decay_lifetime[3], NCConfig.decay_power[3], RadSources.URANIUM_238);
		addRecipe("blockNeptunium237", OreDictHelper.oreExists("blockBismuth") ? "blockBismuth" : "blockLead", NCConfig.decay_lifetime[4], NCConfig.decay_power[4], RadSources.NEPTUNIUM_237);
		addRecipe("blockPlutonium242", "blockUranium238", NCConfig.decay_lifetime[5], NCConfig.decay_power[5], RadSources.PLUTONIUM_242);
		addRecipe("blockAmericium243", "blockLead", NCConfig.decay_lifetime[6], NCConfig.decay_power[6], RadSources.AMERICIUM_243);
		addRecipe("blockCurium246", "blockPlutonium242", NCConfig.decay_lifetime[7], NCConfig.decay_power[7], RadSources.CURIUM_246);
		addRecipe("blockBerkelium247", "blockAmericium243", NCConfig.decay_lifetime[8], NCConfig.decay_power[8], RadSources.BERKELIUM_247);
		addRecipe("blockCalifornium252", "blockLead", NCConfig.decay_lifetime[9], NCConfig.decay_power[9], RadSources.CALIFORNIUM_252);
	}
	
	@Override
	public List fixExtras(List extras) {
		List fixed = new ArrayList(3);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Double ? (double) extras.get(0) : 1200D);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof Double ? (double) extras.get(1) : 0D);
		fixed.add(extras.size() > 2 && extras.get(2) instanceof Double ? (double) extras.get(2) : 0D);
		return fixed;
	}
}
