package nc.integration.jei.processor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import nc.integration.jei.IJEIHandler;
import nc.integration.jei.JEIMethods.RecipeItemMapper;
import nc.integration.jei.JEIProcessorCategory;
import nc.recipe.SorptionType;

public class PressurizerCategory extends JEIProcessorCategory {
	
	public PressurizerCategory(IGuiHelper guiHelper, IJEIHandler handler) {
		super(guiHelper, handler, "pressurizer_idle", 47, 30, 90, 26);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		RecipeItemMapper itemMapper = new RecipeItemMapper();
		itemMapper.map(SorptionType.INPUT, 0, 0, 56 - backPosX, 35 - backPosY);
		itemMapper.map(SorptionType.OUTPUT, 0, 1, 116 - backPosX, 35 - backPosY);
		itemMapper.mapItemsTo(recipeLayout.getItemStacks(), ingredients);
	}
}
