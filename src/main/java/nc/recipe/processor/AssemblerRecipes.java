package nc.recipe.processor;

import static nc.util.FissionHelper.FISSION_ORE_DICT;

import java.util.*;

import nc.recipe.BasicRecipeHandler;

public class AssemblerRecipes extends BasicRecipeHandler {
	
	public AssemblerRecipes() {
		super("assembler", 4, 0, 1, 0);
	}
	
	@Override
	public void addRecipes() {
		for (String element : FISSION_ORE_DICT) {
			addRecipe(oreStack("ingot" + element + "Carbide", 9), "dustGraphite", "ingotPyrolyticCarbon", "ingotSiliconCarbide", oreStack("ingot" + element + "TRISO", 9), 1D, 1D);
		}
	}
	
	@Override
	public List<Object> fixExtras(List<Object> extras) {
		List<Object> fixed = new ArrayList<>(3);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Double ? (double) extras.get(0) : 1D);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof Double ? (double) extras.get(1) : 1D);
		fixed.add(extras.size() > 2 && extras.get(2) instanceof Double ? (double) extras.get(2) : 0D);
		return fixed;
	}
}
