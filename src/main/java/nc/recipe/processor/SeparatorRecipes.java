package nc.recipe.processor;

import static nc.util.FissionHelper.FISSION_ORE_DICT;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import nc.recipe.ProcessorRecipeHandler;

public class SeparatorRecipes extends ProcessorRecipeHandler {
	
	public SeparatorRecipes() {
		super("separator", 1, 0, 2, 0);
	}
	
	@Override
	public void addRecipes() {
		for (String type : new String[] {"", "Carbide", "Oxide", "Nitride", "ZA"}) {
			addRecipe(oreStackList(Lists.newArrayList("ingotUranium" + type, "dustUranium" + type), 10), oreStack("ingotUranium238" + type, 9), oreStack("ingotUranium235" + type, 1), 5D, 1D);
		}
		
		addRecipe(oreStackList(Lists.newArrayList("ingotBoron", "dustBoron"), 12), oreStack("ingotBoron11", 9), oreStack("ingotBoron10", 3), 6D, 1D);
		addRecipe(oreStackList(Lists.newArrayList("ingotLithium", "dustLithium"), 10), oreStack("ingotLithium7", 9), oreStack("ingotLithium6", 1), 5D, 1D);
		
		addFuelSeparationRecipes("U", "Uranium", 238, 233, 235);
		addFuelSeparationRecipes("N", "Neptunium", 237, 236);
		addFuelSeparationRecipes("P", "Plutonium", 242, 239, 241);
		for (int fissile : new int[] {239, 241}) for (String type : new String[] {"", "Carbide", "Oxide", "Nitride", "ZA"}) {
			addRecipe(oreStack("ingotMIX" + fissile + type, 9), oreStack("ingotUranium238" + type, 8), oreStack("ingotPlutonium" + fissile + type, 1), 1D, 1D);
		}
		addFuelSeparationRecipes("A", "Americium", 243, 242);
		addFuelSeparationRecipes("Cm", "Curium", 246, 243, 245, 247);
		addFuelSeparationRecipes("B", "Berkelium", 247, 248);
		addFuelSeparationRecipes("Cf", "Californium", 252, 249, 251);
		
		addFissionAlloyRecipes();
	}
	
	public void addFuelSeparationRecipes(String fuel, String element, int fertile, int... fissiles) {
		for (int fissile : fissiles) for (String type : new String[] {"", "Oxide", "Nitride"}) {
			addRecipe(oreStack("ingotLE" + fuel + fissile + type, 9), oreStack("ingot" + element + fertile + type, 8), oreStack("ingot" + element + fissile + type, 1), 1D, 1D);
			addRecipe(oreStack("ingotHE" + fuel + fissile + type, 9), oreStack("ingot" + element + fertile + type, 6), oreStack("ingot" + element + fissile + type, 3), 1D, 1D);
		}
	}
	
	public void addFissionAlloyRecipes() {
		for (int i = 0; i < FISSION_ORE_DICT.length; i++) {
			addRecipe("ingot" + FISSION_ORE_DICT[i] + "ZA", "ingot" + FISSION_ORE_DICT[i], "dustZirconium", 1D, 1D);
			addRecipe("ingot" + FISSION_ORE_DICT[i] + "Carbide", "ingot" + FISSION_ORE_DICT[i], "dustGraphite", 1D, 1D);
		}
	}
	
	@Override
	public List fixExtras(List extras) {
		List fixed = new ArrayList(3);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Double ? (double) extras.get(0) : 1D);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof Double ? (double) extras.get(1) : 1D);
		fixed.add(extras.size() > 2 && extras.get(2) instanceof Double ? (double) extras.get(2) : 0D);
		return fixed;
	}
}
