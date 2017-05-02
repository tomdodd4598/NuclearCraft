package nc.crafting.processor;

import nc.config.NCConfig;
import nc.handler.ProcessorRecipeHandler;

public class ManufactoryRecipes extends ProcessorRecipeHandler {
	
	private static final ManufactoryRecipes RECIPES = new ManufactoryRecipes();

	public ManufactoryRecipes() {
		super(1, 0, 1, 0, false, true);
	}
	
	public static final ProcessorRecipeHandler instance() {
		return RECIPES;
	}
	
	public void addRecipes() {
		addRecipe("gemCoal", "dustGraphite", NCConfig.processor_time[0]);
		addRecipe("dustCoal", "dustGraphite", NCConfig.processor_time[0]);
		addRecipe("gemDiamond", "dustDiamond", NCConfig.processor_time[0]*2);
		addRecipe("gemRhodochrosite", "dustRhodochrosite", NCConfig.processor_time[0]*2);
		addRecipe("gemQuartz", "dustQuartz", NCConfig.processor_time[0]);
		addRecipe("blockObsidian", oreStack("dustObsidian", 4), NCConfig.processor_time[0]*2);
		addRecipe(oreStack("blockSand", 4), "itemSilicon", NCConfig.processor_time[0]);
	}
}
