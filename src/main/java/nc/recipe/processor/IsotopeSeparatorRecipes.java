package nc.recipe.processor;

import com.google.common.collect.Lists;

import nc.recipe.ProcessorRecipeHandler;

public class IsotopeSeparatorRecipes extends ProcessorRecipeHandler {
	
	public IsotopeSeparatorRecipes() {
		super("isotope_separator", 1, 0, 2, 0);
	}
	
	@Override
	public void addRecipes() {
		for (String type : new String[] {"", "Carbide", "Oxide", "Nitride", "ZA"}) {
			addRecipe(oreStackList(Lists.newArrayList("ingotUranium" + type, "dustUranium" + type), 10), oreStack("ingotUranium238" + type, 9), oreStack("ingotUranium235" + type, 1), 10D, 1D);
		}
		
		addRecipe(oreStackList(Lists.newArrayList("ingotBoron", "dustBoron"), 9), oreStack("ingotBoron11", 9), oreStack("ingotBoron10", 3), 1D, 1D);
		addRecipe(oreStackList(Lists.newArrayList("ingotLithium", "dustLithium"), 9), oreStack("ingotLithium7", 9), oreStack("ingotLithium6", 3), 1D, 1D);
		
		addFuelSeparationRecipes("U", "Uranium", 238, 233, 235);
		addFuelSeparationRecipes("N", "Neptunium", 237, 236);
		addFuelSeparationRecipes("P", "Plutonium", 242, 239, 241);
		for (int fissile : new int[] {239, 241}) for (String type : new String[] {"Oxide", "Nitride", "ZA"}) {
			addRecipe(oreStack("fuelMIX" + fissile + type, 9), oreStack("ingotUranium238" + type, 8), oreStack("ingotPlutonium" + fissile + type, 1), 1D, 1D);
		}
		addFuelSeparationRecipes("A", "Americium", 243, 242);
		addFuelSeparationRecipes("Cm", "Curium", 246, 243, 245, 247);
		addFuelSeparationRecipes("B", "Berkelium", 247, 248);
		addFuelSeparationRecipes("Cf", "Californium", 252, 249, 251);
	}
	
	public void addFuelSeparationRecipes(String fuel, String element, int fertile, int... fissiles) {
		for (int fissile : fissiles) for (String type : new String[] {"Oxide", "Nitride", "ZA"}) {
			addRecipe(oreStack("fuelLE" + fuel + fissile + type, 9), oreStack("ingot" + element + fertile + type, 8), oreStack("ingot" + element + fissile + type, 1), 1D, 1D);
			addRecipe(oreStack("fuelHE" + fuel + fissile + type, 9), oreStack("ingot" + element + fertile + type, 6), oreStack("ingot" + element + fissile + type, 3), 1D, 1D);
		}
	}
}
