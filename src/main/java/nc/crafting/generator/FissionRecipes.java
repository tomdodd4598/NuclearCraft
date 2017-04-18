package nc.crafting.generator;

import nc.handler.ProcessorRecipeHandler;

public class FissionRecipes extends ProcessorRecipeHandler {
	
	private static final FissionRecipes RECIPES = new FissionRecipes();

	public FissionRecipes() {
		super(1, 0, 1, 0, false);
	}
	public static final ProcessorRecipeHandler instance() {
		return RECIPES;
	}
	
	public void addRecipes() {
		fuelRodDeplete("TBU");
		fuelRodDeplete("U", 233, 235);
		fuelRodDeplete("N", 236);
		fuelRodDeplete("P", 239, 241);
		addRecipe("fuelRodMOX239", "depletedFuelRodMOX239");
		addRecipe("fuelRodMOX241", "depletedFuelRodMOX241");
		fuelRodDeplete("A", 242);
		fuelRodDeplete("Cm", 243, 245, 247);
		fuelRodDeplete("B", 248);
		fuelRodDeplete("Cf", 249, 251);
	}
	
	public void fuelRodDeplete(String fuel) {
		addRecipe("fuelRod" + fuel, "depletedFuelRod" + fuel);
		addRecipe("fuelRod" + fuel + "Oxide", "depletedFuelRod" + fuel + "Oxide");
	}
	
	public void fuelRodDeplete(String fuel, int... types) {
		for (int type : types) {
			fuelRodDeplete("LE" + fuel + type);
			fuelRodDeplete("HE" + fuel + type);
		}
	}
}
