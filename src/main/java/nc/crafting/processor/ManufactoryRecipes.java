package nc.crafting.processor;

import nc.handler.ProcessorRecipeHandler;

public class ManufactoryRecipes extends ProcessorRecipeHandler {
	
	private static final ManufactoryRecipes RECIPES = new ManufactoryRecipes();

	public ManufactoryRecipes() {
		super(1, 0, 1, 0, false);
	}
	
	public static final ProcessorRecipeHandler instance() {
		return RECIPES;
	}
	
	public void addRecipes() {
		addRecipe("gemCoal", "dustGraphite");
		addRecipe("dustCoal", "dustGraphite");
		addRecipe("gemDiamond", "dustDiamond");
		addRecipe("gemRhodochrosite", "dustRhodochrosite");
		addRecipe("gemQuartz", "dustQuartz");
		addRecipe("blockObsidian", oreStack("dustObsidian", 4));
		addRecipe(oreStack("blockSand", 4), "itemSilicon");
	}
}
