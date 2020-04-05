package nc.recipe.processor;

import static nc.util.FissionHelper.FISSION_ORE_DICT;

import java.util.ArrayList;
import java.util.List;

import nc.recipe.ProcessorRecipeHandler;

public class AssemblerRecipes extends ProcessorRecipeHandler {
	
	public AssemblerRecipes() {
		super("assembler", 4, 0, 1, 0);
	}

	@Override
	public void addRecipes() {
		for (int i = 0; i < FISSION_ORE_DICT.length; i++) {
			addRecipe(oreStack("ingot" + FISSION_ORE_DICT[i] + "Carbide", 9), "dustGraphite", "ingotPyrolyticCarbon", "ingotSiliconCarbide", oreStack("ingot" + FISSION_ORE_DICT[i] + "TRISO", 9), 1D, 1D);
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
