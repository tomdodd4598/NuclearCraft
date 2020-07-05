package nc.recipe.generator;

import static nc.config.NCConfig.*;
import static nc.radiation.RadSources.*;

import java.util.*;

import nc.recipe.ProcessorRecipeHandler;
import nc.util.OreDictHelper;

public class DecayGeneratorRecipes extends ProcessorRecipeHandler {
	
	public DecayGeneratorRecipes() {
		super("decay_generator", 1, 0, 1, 0);
	}
	
	// Decay time (minutes) = Sum of natural logs of real decay times (years).
	// Ignore if < 1 yr. If decays to lead, add 5 if past thorium.
	
	@Override
	public void addRecipes() {
		addRecipe("blockThorium", "blockLead", decay_lifetime[0], decay_power[0], THORIUM);
		addRecipe("blockUranium", "blockUranium238", decay_lifetime[1], decay_power[1], URANIUM);
		
		addRecipe("blockUranium238", OreDictHelper.oreExists("blockRadium") ? "blockRadium" : "blockLead", decay_lifetime[3], decay_power[3], URANIUM_238);
		addRecipe("blockNeptunium237", OreDictHelper.oreExists("blockBismuth") ? "blockBismuth" : "blockLead", decay_lifetime[4], decay_power[4], NEPTUNIUM_237);
		addRecipe("blockPlutonium242", "blockUranium238", decay_lifetime[5], decay_power[5], PLUTONIUM_242);
		addRecipe("blockAmericium243", "blockLead", decay_lifetime[6], decay_power[6], AMERICIUM_243);
		addRecipe("blockCurium246", "blockPlutonium242", decay_lifetime[7], decay_power[7], CURIUM_246);
		addRecipe("blockBerkelium247", "blockAmericium243", decay_lifetime[8], decay_power[8], BERKELIUM_247);
		addRecipe("blockCalifornium252", "blockLead", decay_lifetime[9], decay_power[9], CALIFORNIUM_252);
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
