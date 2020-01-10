package nc.recipe.processor;

import java.util.List;

import com.google.common.collect.Lists;

import nc.radiation.RadSources;
import nc.recipe.ProcessorRecipeHandler;
import nc.util.OreDictHelper;

public class DecayHastenerRecipes extends ProcessorRecipeHandler {
	
	public DecayHastenerRecipes() {
		super("decay_hastener", 1, 0, 1, 0);
	}

	@Override
	public void addRecipes() {
		addDecayRecipes("Radium", "Lead");
		addDecayRecipes("Polonium", "Lead");
		
		addDecayRecipes("Thorium", "Lead");
		
		if (OreDictHelper.oreExists("dustBismuth")) {
			addDecayRecipes("Uranium233", "Bismuth");
		}
		else {
			addDecayRecipes("TBU", "Lead");
			addDecayRecipes("Uranium233", "Lead");
		}
		addDecayRecipes("Uranium235", "Lead");
		addDecayRecipes("Uranium238", "Radium");
		
		addDecayRecipes("Neptunium236", "Thorium");
		addDecayRecipes("Neptunium237", "Uranium233");
		
		addDecayRecipes("Plutonium238", "Lead");
		addDecayRecipes("Plutonium239", "Uranium235");
		addDecayRecipes("Plutonium241", "Neptunium237");
		addDecayRecipes("Plutonium242", "Uranium238");
		
		addDecayRecipes("Americium241", "Neptunium237");
		addDecayRecipes("Americium242", "Lead");
		addDecayRecipes("Americium243", "Plutonium239");
		
		addDecayRecipes("Curium243", "Plutonium239");
		addDecayRecipes("Curium245", "Plutonium241");
		addDecayRecipes("Curium246", "Plutonium242");
		addDecayRecipes("Curium247", "Americium243");
		
		addDecayRecipes("Berkelium247", "Americium243");
		addDecayRecipes("Berkelium248", "Thorium");
		
		addDecayRecipes("Californium249", "Curium245");
		addDecayRecipes("Californium250", "Curium246");
		addDecayRecipes("Californium251", "Curium247");
		addDecayRecipes("Californium252", "Thorium");
	}
	
	private static final List<String> DUSTS = Lists.newArrayList("Lead", "Bismuth", "Radium", "Polonium", "Thorium");
	
	public void addDecayRecipes(String input, String output) {
		String inputName = "ingot" + input;
		double radiationLevel = RadSources.ORE_MAP.getDouble(inputName);
		if (DUSTS.contains(output)) {
			addRecipe(Lists.newArrayList(inputName, inputName + "Oxide", inputName + "Nitride"), "dust" + output, 1D, 1D, radiationLevel);
		}
		else for (String type : new String[] {"", "Carbide", "Oxide", "Nitride", "ZA"}) {
			addRecipe(inputName + type, "ingot" + output + type, 1D, 1D, radiationLevel);
		}
	}
}
