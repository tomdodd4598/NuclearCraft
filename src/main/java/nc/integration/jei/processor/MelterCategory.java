package nc.integration.jei.processor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import nc.config.NCConfig;
import nc.integration.jei.BaseCategory;
import nc.integration.jei.IJEIHandler;
import nc.integration.jei.JEIMethods.RecipeFluidMapper;
import nc.integration.jei.JEIMethods.RecipeItemMapper;
import nc.recipe.SorptionType;

public class MelterCategory extends BaseCategory {
	
	public MelterCategory(IGuiHelper guiHelper, IJEIHandler handler) {
		super(guiHelper, handler, "melter_idle", NCConfig.processor_time[6], 47, 30, 90, 26, 176, 3, 37, 16, 74, 35);
	}
	
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		RecipeItemMapper itemMapper = new RecipeItemMapper();
		RecipeFluidMapper fluidMapper = new RecipeFluidMapper();
		itemMapper.map(SorptionType.INPUT, 0, 0, 56 - backPosX, 35 - backPosY);
		fluidMapper.map(SorptionType.OUTPUT, 0, 0, 112 - backPosX, 31 - backPosY, 24, 24);
		itemMapper.mapItemsTo(recipeLayout.getItemStacks(), ingredients);
		fluidMapper.mapFluidsTo(recipeLayout.getFluidStacks(), ingredients);
	}
}
