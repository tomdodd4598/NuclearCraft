package nc.recipe.processor;

import static nc.util.FissionHelper.FISSION_ORE_DICT;

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
}
