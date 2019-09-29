package nc.recipe.processor;

import java.util.Arrays;
import java.util.List;

import nc.radiation.RadSources;
import nc.recipe.ProcessorRecipeHandler;
import nc.util.OreDictHelper;

public class DecayHastenerRecipes extends ProcessorRecipeHandler {
	
	public DecayHastenerRecipes() {
		super("decay_hastener", 1, 0, 1, 0);
	}

	@Override
	public void addRecipes() {
		addDecayRecipes("Thorium230Base", "Lead");
		addDecayRecipes("Thorium232", "Lead");
		
		addDecayRecipes("Uranium233", OreDictHelper.oreExists("dustBismuth") ? "Bismuth" : "Lead");
		addDecayRecipes("Uranium235", "Lead");
		addDecayRecipes("Uranium238Base", "Thorium230Base");
		
		addDecayRecipes("Neptunium236", "Thorium232");
		addDecayRecipes("Neptunium237Base", "Uranium233");
		
		addDecayRecipes("Plutonium238Base", "Thorium230Base");
		addDecayRecipes("Plutonium239", "Uranium235");
		addDecayRecipes("Plutonium241", "Neptunium237Base");
		addDecayRecipes("Plutonium242Base", "Uranium238Base");
		
		addDecayRecipes("Americium241Base", "Neptunium237Base");
		addDecayRecipes("Americium242", "Thorium230Base");
		addDecayRecipes("Americium243Base", "Plutonium239");
		
		addDecayRecipes("Curium243", "Plutonium239");
		addDecayRecipes("Curium245", "Plutonium241");
		addDecayRecipes("Curium246Base", "Plutonium242Base");
		addDecayRecipes("Curium247", "Americium243Base");
		
		addDecayRecipes("Berkelium247Base", "Americium243Base");
		addDecayRecipes("Berkelium248", "Thorium232");
		
		addDecayRecipes("Californium249", "Curium245");
		addDecayRecipes("Californium250Base", "Curium246Base");
		addDecayRecipes("Californium251", "Curium247");
		addDecayRecipes("Californium252Base", "Thorium232");
	}
	
	private static final List<String> CHAIN_ENDS = Arrays.asList("Lead", "Bismuth");
	
	public void addDecayRecipes(String input, String output) {
		boolean isInputBase = input.substring(input.length() - 4, input.length()).equals("Base"), isOutputBase = output.substring(output.length() - 4, output.length()).equals("Base"), isChainEnd = CHAIN_ENDS.contains(output);
		for (String size : new String[] {"ingot", "nugget"}) for (String oxide : new String[] {"", "Oxide"}) {
			String inputName = size + ((oxide.equals("Oxide") && isInputBase) || (size.equals("nugget") && isInputBase) ? input.substring(0, input.length() - 4) : input) + oxide;
			double radiationLevel = RadSources.ORE_MAP.getDouble(inputName);
			addRecipe(inputName, isChainEnd ? (size.equals("ingot") ? "dust" : "tinyDust") + output : size + ((oxide.equals("Oxide") && isOutputBase) || (size.equals("nugget") && isOutputBase) ? output.substring(0, output.length() - 4) : output) + oxide, size.equals("nugget") ? 1D/9D : 1D, 1D, radiationLevel/8D);
		}
	}
}
