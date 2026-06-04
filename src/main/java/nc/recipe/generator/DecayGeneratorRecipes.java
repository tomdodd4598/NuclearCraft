package nc.recipe.generator;

import com.google.common.collect.*;
import nc.radiation.RadSources;
import nc.recipe.BasicRecipeHandler;
import nc.util.OreDictHelper;

import java.util.*;

import static nc.config.NCConfig.*;

public class DecayGeneratorRecipes extends BasicRecipeHandler {
	
	public static final double[] DEFAULT_BASE_RADIATION = {RadSources.THORIUM, RadSources.URANIUM, RadSources.URANIUM_238, RadSources.NEPTUNIUM_237, RadSources.PLUTONIUM_242, RadSources.AMERICIUM_243, RadSources.CURIUM_246, RadSources.BERKELIUM_247, RadSources.CALIFORNIUM_252};
	
	public DecayGeneratorRecipes() {
		super("decay_generator", 1, 0, 1, 0);
	}
	
	@Override
	public void addRecipes() {
		addDecayRecipes("Thorium", "Lead", decay_lifetime[0], decay_power[0], DEFAULT_BASE_RADIATION[0]);
		addDecayRecipes("Uranium", "Uranium238", decay_lifetime[1], decay_power[1], DEFAULT_BASE_RADIATION[1]);
		addDecayRecipes("Uranium238", OreDictHelper.oreExists("blockRadium") ? "Radium" : "Lead", decay_lifetime[2], decay_power[2], DEFAULT_BASE_RADIATION[2]);
		addDecayRecipes("Neptunium237", OreDictHelper.oreExists("blockBismuth") ? "Bismuth" : "Lead", decay_lifetime[3], decay_power[3], DEFAULT_BASE_RADIATION[3]);
		addDecayRecipes("Plutonium242", "Uranium238", decay_lifetime[4], decay_power[4], DEFAULT_BASE_RADIATION[4]);
		addDecayRecipes("Americium243", "Lead", decay_lifetime[5], decay_power[5], DEFAULT_BASE_RADIATION[5]);
		addDecayRecipes("Curium246", "Plutonium242", decay_lifetime[6], decay_power[6], DEFAULT_BASE_RADIATION[6]);
		addDecayRecipes("Berkelium247", "Americium243", decay_lifetime[7], decay_power[7], DEFAULT_BASE_RADIATION[7]);
		addDecayRecipes("Californium252", "Lead", decay_lifetime[8], decay_power[8], DEFAULT_BASE_RADIATION[8]);
	}
	
	private static final Set<String> NON_FISSION = Sets.newHashSet("Lead", "Bismuth", "Radium", "Thorium");
	
	public void addDecayRecipes(String input, String output, double lifetime, double power, double radiation) {
		String inputName = "block" + input;
		if (NON_FISSION.contains(output)) {
			addRecipe(Lists.newArrayList(inputName, inputName + "Oxide", inputName + "Nitride"), "block" + output, lifetime, power, radiation);
		}
		else {
			for (String type : new String[] {"", "Carbide", "Oxide", "Nitride", "ZA"}) {
				addRecipe(inputName + type, "block" + output + type, lifetime, power, radiation);
			}
		}
	}
	
	@Override
	public List<Object> fixedExtras(List<Object> extras) {
		ExtrasFixer fixer = new ExtrasFixer(extras);
		fixer.add(Double.class, 12000D);
		fixer.add(Double.class, 0D);
		fixer.add(Double.class, 0D);
		return fixer.fixed;
	}
}
