package nc.recipe.processor;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;
import net.minecraft.init.Blocks;

public class PressurizerRecipes extends BaseRecipeHandler {
	
	private static final PressurizerRecipes RECIPES = new PressurizerRecipes();
	
	public PressurizerRecipes() {
		super(1, 0, 1, 0, false);
	}

	public static final PressurizerRecipes instance() {
		return RECIPES;
	}

	public void addRecipes() {
		addRecipe("dustGraphite", "gemCoal", NCConfig.processor_time[11]);
		addRecipe(oreStack("ingotGraphite", 64), "gemDiamond", NCConfig.processor_time[11]*8);
		addRecipe("dustDiamond", "gemDiamond", NCConfig.processor_time[11]*2);
		addRecipe("dustRhodochrosite", "gemRhodochrosite", NCConfig.processor_time[11]*2);
		addRecipe("dustQuartz", "gemQuartz", NCConfig.processor_time[11]);
		addRecipe(oreStack("dustObsidian", 4), Blocks.OBSIDIAN, NCConfig.processor_time[11]*2);
		addRecipe("dustBoronNitride", "gemBoronNitride", NCConfig.processor_time[11]*4);
		addRecipe("dustFluorite", "gemFluorite", NCConfig.processor_time[11]*4);
	}

	public String getRecipeName() {
		return "pressurizer";
	}
}
