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

public class AlloyFurnaceCategory extends BaseCategory {
	
	public AlloyFurnaceCategory(IGuiHelper guiHelper, IJEIHandler handler) {
		super(guiHelper, handler, "alloy_furnace_idle", NCConfig.processor_time[4], 45, 30, 102, 26, 176, 3, 37, 16, 84, 35);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		RecipeItemMapper itemMapper = new RecipeItemMapper();
		itemMapper.map(SorptionType.INPUT, 0, 0, 46 - backPosX, 35 - backPosY);
		itemMapper.map(SorptionType.INPUT, 1, 1, 66 - backPosX, 35 - backPosY);
		itemMapper.map(SorptionType.OUTPUT, 0, 2, 126 - backPosX, 35 - backPosY);
		itemMapper.mapItemsTo(recipeLayout.getItemStacks(), ingredients);
	}
}
