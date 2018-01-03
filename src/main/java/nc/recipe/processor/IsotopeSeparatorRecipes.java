package nc.recipe.processor;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;

public class IsotopeSeparatorRecipes extends BaseRecipeHandler {
	
	public IsotopeSeparatorRecipes() {
		super(1, 0, 2, 0, false);
	}

	@Override
	public void addRecipes() {
		addRecipe("ingotThorium", "ingotThorium232", "tinyThorium230", NCConfig.processor_time[1]);
		addRecipe("ingotThoriumOxide", "ingotThorium232Oxide", "tinyThorium230Oxide", NCConfig.processor_time[1]);
		addRecipe("ingotUranium", "ingotUranium238Base", "tinyUranium235", NCConfig.processor_time[1]);
		addRecipe("ingotUraniumOxide", "ingotUranium238Oxide", "tinyUranium235Oxide", NCConfig.processor_time[1]);
		addRecipe("dustThorium", "ingotThorium232", "tinyThorium230", NCConfig.processor_time[1]);
		addRecipe("dustThoriumOxide", "ingotThorium232Oxide", "tinyThorium230Oxide", NCConfig.processor_time[1]);
		addRecipe("dustUranium", "ingotUranium238Base", "tinyUranium235", NCConfig.processor_time[1]);
		addRecipe("dustUraniumOxide", "ingotUranium238Oxide", "tinyUranium235Oxide", NCConfig.processor_time[1]);
		
		addRecipe("ingotYellorium", "ingotUranium238Base", "tinyUranium235", NCConfig.processor_time[1]);
		addRecipe("dustYellorium", "ingotUranium238Base", "tinyUranium235", NCConfig.processor_time[1]);
		addRecipe("ingotBlutonium", "ingotPlutonium242Base", "tinyPlutonium239", NCConfig.processor_time[1]);
		addRecipe("dustBlutonium", "ingotPlutonium242Base", "tinyPlutonium239", NCConfig.processor_time[1]);
		addRecipe("ingotPlutonium", "ingotPlutonium242Base", "tinyPlutonium239", NCConfig.processor_time[1]);
		addRecipe("dustPlutonium", "ingotPlutonium242Base", "tinyPlutonium239", NCConfig.processor_time[1]);
		
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

	@Override
	public String getRecipeName() {
		return "isotope_separator";
	}
}
