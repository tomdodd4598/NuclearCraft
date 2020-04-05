package nc.recipe.processor;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import nc.config.NCConfig;
import nc.radiation.RadSources;
import nc.recipe.ProcessorRecipeHandler;
import nc.util.NCMath;
import nc.util.OreDictHelper;

public class DecayHastenerRecipes extends ProcessorRecipeHandler {
	
	public DecayHastenerRecipes() {
		super("decay_hastener", 1, 0, 1, 0);
	}
	
	@Override
	public void addRecipes() {
		addDecayRecipes("Thorium", "Lead", RadSources.THORIUM);
		addDecayRecipes("Bismuth", "Thallium", RadSources.BISMUTH);
		
		addDecayRecipes("Radium", "Lead", RadSources.RADIUM);
		addDecayRecipes("Polonium", "Lead", RadSources.POLONIUM);
		
		addDecayRecipes("TBP", "TBU", RadSources.TBP);
		addDecayRecipes("Protactinium233", "Uranium233", RadSources.PROTACTINIUM_233);
		
		addDecayRecipes("Uranium233", "Bismuth", RadSources.URANIUM_233);
		addDecayRecipes("Uranium235", "Lead", RadSources.URANIUM_235);
		addDecayRecipes("Uranium238", "Radium", RadSources.URANIUM_238);
		
		addDecayRecipes("Neptunium236", "Thorium", RadSources.NEPTUNIUM_236);
		addDecayRecipes("Neptunium237", "Uranium233", RadSources.NEPTUNIUM_237);
		
		addDecayRecipes("Plutonium238", "Lead", RadSources.PLUTONIUM_238);
		addDecayRecipes("Plutonium239", "Uranium235", RadSources.PLUTONIUM_239);
		addDecayRecipes("Plutonium241", "Neptunium237", RadSources.PLUTONIUM_241);
		addDecayRecipes("Plutonium242", "Uranium238", RadSources.PLUTONIUM_242);
		
		addDecayRecipes("Americium241", "Neptunium237", RadSources.AMERICIUM_241);
		addDecayRecipes("Americium242", "Lead", RadSources.AMERICIUM_242);
		addDecayRecipes("Americium243", "Plutonium239", RadSources.AMERICIUM_243);
		
		addDecayRecipes("Curium243", "Plutonium239", RadSources.CURIUM_243);
		addDecayRecipes("Curium245", "Plutonium241", RadSources.CURIUM_245);
		addDecayRecipes("Curium246", "Plutonium242", RadSources.CURIUM_246);
		addDecayRecipes("Curium247", "Americium243", RadSources.CURIUM_247);
		
		addDecayRecipes("Berkelium247", "Americium243", RadSources.BERKELIUM_247);
		addDecayRecipes("Berkelium248", "Thorium", RadSources.BERKELIUM_248);
		
		addDecayRecipes("Californium249", "Curium245", RadSources.CALIFORNIUM_249);
		addDecayRecipes("Californium250", "Curium246", RadSources.CALIFORNIUM_250);
		addDecayRecipes("Californium251", "Curium247", RadSources.CALIFORNIUM_251);
		addDecayRecipes("Californium252", "Thorium", RadSources.CALIFORNIUM_252);
	}
	
	private static final List<String> DUSTS = Lists.newArrayList("Lead", "Bismuth", "Radium", "Polonium", "Thorium", "TBP");
	
	public void addDecayRecipes(String input, String output, double radiation) {
		String inputName = (OreDictHelper.oreExists("ingot" + input) ? "ingot" : "dust") + input;
		double timeMult = NCMath.roundTo(Z*(radiation >= 1D ? F/Math.log1p(Math.log1p(radiation)) : Math.log1p(Math.log1p(1D/radiation))/F), 5D/NCConfig.processor_time[2]);
		if (DUSTS.contains(output)) {
			addRecipe(Lists.newArrayList(inputName, inputName + "Oxide", inputName + "Nitride"), "dust" + output, timeMult, 1D, radiation);
		}
		else for (String type : new String[] {"", "Carbide", "Oxide", "Nitride", "ZA"}) {
			addRecipe(inputName + type, "ingot" + output + type, timeMult, 1D, radiation);
		}
	}
	
	private static final double F = Math.log1p(Math.log(2D)), Z = 0.1674477985420331D;
	
	@Override
	public List fixExtras(List extras) {
		List fixed = new ArrayList(3);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Double ? (double) extras.get(0) : 1D);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof Double ? (double) extras.get(1) : 1D);
		fixed.add(extras.size() > 2 && extras.get(2) instanceof Double ? (double) extras.get(2) : 0D);
		return fixed;
	}
}
