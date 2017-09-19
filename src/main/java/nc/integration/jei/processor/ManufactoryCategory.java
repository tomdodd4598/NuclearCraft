package nc.integration.jei.processor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import nc.config.NCConfig;
import nc.integration.jei.BaseCategory;
import nc.integration.jei.IJEIHandler;
import nc.integration.jei.JEIMethods.RecipeItemMapper;
import nc.recipe.SorptionType;

public class ManufactoryCategory extends BaseCategory {
	
	public ManufactoryCategory(IGuiHelper guiHelper, IJEIHandler handler) {
		super(guiHelper, handler, "manufactory_idle", NCConfig.processor_time[0], 47, 30, 90, 26, 176, 3, 37, 16, 74, 35);
	}
	
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		RecipeItemMapper itemMapper = new RecipeItemMapper();
		itemMapper.map(SorptionType.INPUT, 0, 0, 56 - backPosX, 35 - backPosY);
		itemMapper.map(SorptionType.OUTPUT, 0, 1, 116 - backPosX, 35 - backPosY);
		itemMapper.mapItemsTo(recipeLayout.getItemStacks(), ingredients);
	}
}
