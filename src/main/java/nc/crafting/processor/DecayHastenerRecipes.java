package nc.crafting.processor;

import nc.handler.ProcessorRecipeHandler;

public class DecayHastenerRecipes extends ProcessorRecipeHandler {
	
	private static final DecayHastenerRecipes RECIPES = new DecayHastenerRecipes();

	public DecayHastenerRecipes() {
		super(1, 0, 1, 0, false);
	}
	public static final ProcessorRecipeHandler instance() {
		return RECIPES;
	}
	
	public void addRecipes() {
		decay("Thorium230Base", "dustLead");
		decay("Thorium232", "dustLead");
		
		decay("Uranium233", "dustLead");
		decay("Uranium235", "dustLead");
		decay("Uranium238Base", "Thorium230Base");
		
		decay("Neptunium236", "Thorium232");
		decay("Neptunium237Base", "Uranium233");
		
		decay("Plutonium238Base", "Thorium230Base");
		decay("Plutonium239", "Uranium235");
		decay("Plutonium241", "Neptunium237Base");
		decay("Plutonium242Base", "Uranium238Base");
    	
		decay("Americium241Base", "Neptunium237Base");
		decay("Americium242", "Thorium230Base");
		decay("Americium243Base", "Plutonium239");
		
		decay("Curium243", "Plutonium239");
		decay("Curium245", "Plutonium241");
		decay("Curium246Base", "Plutonium242Base");
		decay("Curium247", "Americium243Base");
		
		decay("Berkelium247Base", "Americium243Base");
		decay("Berkelium248", "Thorium232");
		
		decay("Californium249", "Curium245");
		decay("Californium250Base", "Curium246Base");
		decay("Californium251", "Curium247");
		decay("Californium252Base", "Thorium232");
	}
	
	public void decay(String input, String output) {
		boolean isInputBase = input.substring(input.length() - 4, input.length()).equals("Base"), isOutputBase = output.substring(output.length() - 4, output.length()).equals("Base"), isLead = output.equals("dustLead");
		for (String size : new String[] {"ingot", "tiny"}) for (String oxide : new String[] {"", "Oxide"}) {
			addRecipe(size + ((oxide.equals("Oxide") && isInputBase) || (size.equals("tiny") && isInputBase) ? input.substring(0, input.length() - 4) : input) + oxide, isLead ? (size.equals("ingot") ? "dustLead" : "tinyDustLead") : (size + ((oxide.equals("Oxide") && isOutputBase) || (size.equals("tiny") && isOutputBase) ? output.substring(0, output.length() - 4) : output) + oxide));
		}
	}
}
