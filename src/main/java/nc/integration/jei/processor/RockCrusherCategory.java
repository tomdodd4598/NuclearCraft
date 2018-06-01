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

public class RockCrusherCategory extends BaseCategory {
	
	public RockCrusherCategory(IGuiHelper guiHelper, IJEIHandler handler) {
		super(guiHelper, handler, "rock_crusher_idle", NCConfig.processor_time[18], 29, 30, 122, 26, 176, 3, 37, 16, 56, 35);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		RecipeItemMapper itemMapper = new RecipeItemMapper();
		itemMapper.map(SorptionType.INPUT, 0, 0, 38 - backPosX, 35 - backPosY);
		itemMapper.map(SorptionType.OUTPUT, 0, 1, 94 - backPosX, 35 - backPosY);
		itemMapper.map(SorptionType.OUTPUT, 1, 2, 114 - backPosX, 35 - backPosY);
		itemMapper.map(SorptionType.OUTPUT, 2, 3, 134 - backPosX, 35 - backPosY);
		itemMapper.mapItemsTo(recipeLayout.getItemStacks(), ingredients);
	}
}
