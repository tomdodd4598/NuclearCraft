package nc.integration.jei.processor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import nc.config.NCConfig;
import nc.integration.jei.BaseCategory;
import nc.integration.jei.IJEIHandler;
import nc.integration.jei.JEIMethods.RecipeFluidMapper;
import nc.recipe.SorptionType;

public class SaltMixerCategory extends BaseCategory {
	
	public SaltMixerCategory(IGuiHelper guiHelper, IJEIHandler handler) {
		super(guiHelper, handler, "salt_mixer_idle", NCConfig.processor_time[13], 45, 30, 102, 26, 176, 3, 37, 18, 84, 34);
	}
	
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		RecipeFluidMapper fluidMapper = new RecipeFluidMapper();
		fluidMapper.map(SorptionType.INPUT, 0, 0, 46 - backPosX, 35 - backPosY, 16, 16);
		fluidMapper.map(SorptionType.INPUT, 1, 1, 66 - backPosX, 35 - backPosY, 16, 16);
		fluidMapper.map(SorptionType.OUTPUT, 0, 2, 122 - backPosX, 31 - backPosY, 24, 24);
		fluidMapper.mapFluidsTo(recipeLayout.getFluidStacks(), ingredients);
	}
}
