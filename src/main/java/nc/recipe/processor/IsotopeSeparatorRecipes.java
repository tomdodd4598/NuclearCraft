package nc.recipe.processor;

import com.google.common.collect.Lists;

import nc.recipe.ProcessorRecipeHandler;

public class IsotopeSeparatorRecipes extends ProcessorRecipeHandler {
	
	public IsotopeSeparatorRecipes() {
		super("isotope_separator", 1, 0, 2, 0);
	}

	@Override
	public void addRecipes() {
		addRecipe(Lists.newArrayList("ingotThorium", "dustThorium"), "ingotThorium232", "nuggetThorium230", 1D, 1D);
		addRecipe(Lists.newArrayList("ingotThoriumOxide", "dustThoriumOxide"), "ingotThorium232Oxide", "nuggetThorium230Oxide", 1D, 1D);
		
		addRecipe(Lists.newArrayList("ingotUranium", "dustUranium", "ingotYellorium", "dustYellorium"), "ingotUranium238Base", "nuggetUranium235", 1D, 1D);
		addRecipe(Lists.newArrayList("ingotUraniumOxide", "dustUraniumOxide"), "ingotUranium238Oxide", "nuggetUranium235Oxide", 1D, 1D);
		
		addRecipe(Lists.newArrayList("ingotBlutonium", "dustBlutonium", "ingotPlutonium", "dustPlutonium"), "ingotPlutonium239Base", "nuggetPlutonium242", 1D, 1D);
		addRecipe(Lists.newArrayList("ingotPlutoniumOxide", "dustPlutoniumOxide"), "ingotPlutonium239Oxide", "nuggetPlutonium242Oxide", 1D, 1D);
		
		addRecipe(Lists.newArrayList("ingotBoron", "dustBoron"), "ingotBoron11", oreStack("nuggetBoron10", 3), 1D, 1D);
		addRecipe(Lists.newArrayList("ingotLithium", "dustLithium"), "ingotLithium7", oreStack("nuggetLithium6", 3), 1D, 1D);
		
		addRecipe("fuelTBU", oreStack("ingotThorium232", 9), emptyItemStack(), 1D, 1D);
		addRecipe("fuelTBUOxide", oreStack("ingotThorium232Oxide", 9), emptyItemStack(), 1D, 1D);
		addRecipe("fuelRodTBU", oreStack("ingotThorium232", 9), emptyItemStack(), 1D, 1D);
		addRecipe("fuelRodTBUOxide", oreStack("ingotThorium232Oxide", 9), emptyItemStack(), 1D, 1D);
		addRecipe("fuelMOX239", oreStack("ingotUranium238Base", 8), oreStack("ingotPlutonium239Oxide", 1), 1D, 1D);
		addRecipe("fuelMOX241", oreStack("ingotUranium238Base", 8), oreStack("ingotPlutonium241Oxide", 1), 1D, 1D);
		addRecipe("fuelRodMOX239", oreStack("ingotUranium238Base", 8), oreStack("ingotPlutonium239Oxide", 1), 1D, 1D);
		addRecipe("fuelRodMOX241", oreStack("ingotUranium238Base", 8), oreStack("ingotPlutonium241Oxide", 1), 1D, 1D);
		
		addFuelSeparationRecipes("U", "Uranium", 238, 233, 235);
		addFuelSeparationRecipes("N", "Neptunium", 237, 236);
		addFuelSeparationRecipes("P", "Plutonium", 242, 239, 241);
		addFuelSeparationRecipes("A", "Americium", 243, 242);
		addFuelSeparationRecipes("Cm", "Curium", 246, 243, 245, 247);
		addFuelSeparationRecipes("B", "Berkelium", 247, 248);
		addFuelSeparationRecipes("Cf", "Californium", 252, 249, 251);
	}
	
	public void addFuelSeparationRecipes(String fuel, String element, int fertile, int... fissiles) {
		for (String form : new String[] {"fuel", "fuelRod"}) for (int fissile : fissiles) for (String type : new String[] {"LE", "HE"}) for (String oxide : new String[] {"", "Oxide"}) {
			addRecipe(form + type + fuel + fissile + oxide, oreStack("ingot" + element + fertile + (oxide == "" ? "Base" : ""), type == "LE" ? 8 : 5), oreStack("ingot" + element + fissile + oxide, type == "LE" ? 1 : 4), 1D, 1D);
		}
	}
}
