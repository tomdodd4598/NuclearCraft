package nc.recipe.processor;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;

public class IsotopeSeparatorRecipes extends BaseRecipeHandler {
	
	//private static final IsotopeSeparatorRecipes RECIPES = new IsotopeSeparatorRecipes();
	
	public IsotopeSeparatorRecipes() {
		super(1, 0, 2, 0, false);
	}

	/*public static final IsotopeSeparatorRecipes instance() {
		return RECIPES;
	}*/

	public void addRecipes() {
		addRecipe("ingotThorium", oreStack("ingotThorium232", 2), oreStack("tinyThorium230", 2), NCConfig.processor_time[1]);
		addRecipe("ingotThoriumOxide", oreStack("ingotThorium232Oxide", 2), oreStack("tinyThorium230Oxide", 2), NCConfig.processor_time[1]);
		addRecipe("ingotUranium", oreStack("ingotUranium238Base", 2), oreStack("tinyUranium235", 2), NCConfig.processor_time[1]);
		addRecipe("ingotUraniumOxide", oreStack("ingotUranium238Oxide", 2), oreStack("tinyUranium235Oxide", 2), NCConfig.processor_time[1]);
		addRecipe("dustThorium", oreStack("ingotThorium232", 2), oreStack("tinyThorium230", 2), NCConfig.processor_time[1]);
		addRecipe("dustThoriumOxide", oreStack("ingotThorium232Oxide", 2), oreStack("tinyThorium230Oxide", 2), NCConfig.processor_time[1]);
		addRecipe("dustUranium", oreStack("ingotUranium238Base", 2), oreStack("tinyUranium235", 2), NCConfig.processor_time[1]);
		addRecipe("dustUraniumOxide", oreStack("ingotUranium238Oxide", 2), oreStack("tinyUranium235Oxide", 2), NCConfig.processor_time[1]);
		
		addRecipe("ingotYellorium", "ingotUranium238Base", "tinyUranium235", NCConfig.processor_time[1]);
		addRecipe("dustYellorium", "ingotUranium238Base", "tinyUranium235", NCConfig.processor_time[1]);
		addRecipe("ingotBlutonium", "ingotPlutonium242Base", "tinyPlutonium239", NCConfig.processor_time[1]);
		addRecipe("dustBlutonium", "ingotPlutonium242Base", "tinyPlutonium239", NCConfig.processor_time[1]);
		addRecipe("ingotPlutonium", oreStack("ingotPlutonium242Base", 2), oreStack("tinyPlutonium239", 2), NCConfig.processor_time[1]);
		addRecipe("dustPlutonium", oreStack("ingotPlutonium242Base", 2), oreStack("tinyPlutonium239", 2), NCConfig.processor_time[1]);
		
		addRecipe("ingotBoron", "ingotBoron11", oreStack("tinyBoron10", 3), NCConfig.processor_time[1]);
		addRecipe("ingotLithium", "ingotLithium7", oreStack("tinyLithium6", 3), NCConfig.processor_time[1]);
		addRecipe("dustBoron", "ingotBoron11", oreStack("tinyBoron10", 3), NCConfig.processor_time[1]);
		addRecipe("dustLithium", "ingotLithium7", oreStack("tinyLithium6", 3), NCConfig.processor_time[1]);
		
		addRecipe("fuelTBU", oreStack("ingotThorium232", 5), oreStack("ingotThorium232", 4), NCConfig.processor_time[1]);
		addRecipe("fuelTBUOxide", oreStack("ingotThorium232Oxide", 5), oreStack("ingotThorium232Oxide", 4), NCConfig.processor_time[1]);
		addRecipe("fuelRodTBU", oreStack("ingotThorium232", 5), oreStack("ingotThorium232", 4), NCConfig.processor_time[1]);
		addRecipe("fuelRodTBUOxide", oreStack("ingotThorium232Oxide", 5), oreStack("ingotThorium232Oxide", 4), NCConfig.processor_time[1]);
		addRecipe("fuelMOX239", oreStack("ingotUranium238Base", 8), oreStack("ingotPlutonium239Oxide", 1), NCConfig.processor_time[1]);
		addRecipe("fuelMOX241", oreStack("ingotUranium238Base", 8), oreStack("ingotPlutonium241Oxide", 1), NCConfig.processor_time[1]);
		addRecipe("fuelRodMOX239", oreStack("ingotUranium238Base", 8), oreStack("ingotPlutonium239Oxide", 1), NCConfig.processor_time[1]);
		addRecipe("fuelRodMOX241", oreStack("ingotUranium238Base", 8), oreStack("ingotPlutonium241Oxide", 1), NCConfig.processor_time[1]);
		
		fuelSeparate("U", "Uranium", 238, 233, 235);
		fuelSeparate("N", "Neptunium", 237, 236);
		fuelSeparate("P", "Plutonium", 242, 239, 241);
		fuelSeparate("A", "Americium", 243, 242);
		fuelSeparate("Cm", "Curium", 246, 243, 245, 247);
		fuelSeparate("B", "Berkelium", 247, 248);
		fuelSeparate("Cf", "Californium", 252, 249, 251);
	}
	
	public void fuelSeparate(String fuel, String element, int fertile, int... fissiles) {
		for (String form : new String[] {"fuel", "fuelRod"}) for (int fissile : fissiles) for (String type : new String[] {"LE", "HE"}) for (String oxide : new String[] {"", "Oxide"}) {
			addRecipe(form + type + fuel + fissile + oxide, oreStack("ingot" + element + fertile + (oxide == "" ? "Base" : ""), type == "LE" ? 8 : 5), oreStack("ingot" + element + fissile + oxide, type == "LE" ? 1 : 4), NCConfig.processor_time[1]);
		}
	}

	public String getRecipeName() {
		return "isotope_separator";
	}
}
