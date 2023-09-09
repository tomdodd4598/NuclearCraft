package nc.recipe.processor;

import static nc.util.FissionHelper.FISSION_ORE_DICT;

public class AssemblerRecipes extends BasicProcessorRecipeHandler {
	
	public AssemblerRecipes() {
		super("assembler", 4, 0, 1, 0);
	}
	
	@Override
	public void addRecipes() {
		for (String element : FISSION_ORE_DICT) {
			addRecipe(oreStack("ingot" + element + "Carbide", 9), "dustGraphite", "ingotPyrolyticCarbon", "ingotSiliconCarbide", oreStack("ingot" + element + "TRISO", 9), 1D, 1D);
		}
	}
}
