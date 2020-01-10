package nc.integration.jei.processor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import nc.integration.jei.IJEIHandler;
import nc.integration.jei.JEICategoryProcessor;
import nc.integration.jei.JEIMethods.RecipeItemMapper;
import nc.integration.jei.JEIRecipeWrapper;
import nc.recipe.IngredientSorption;

public class SeparatorCategory extends JEICategoryProcessor<JEIRecipeWrapper.Separator> {
	
	public SeparatorCategory(IGuiHelper guiHelper, IJEIHandler handler) {
		super(guiHelper, handler, "separator", 33, 30, 118, 26);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipeWrapper.Separator recipeWrapper, IIngredients ingredients) {
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);
		
		RecipeItemMapper itemMapper = new RecipeItemMapper();
		itemMapper.map(IngredientSorption.INPUT, 0, 0, 42 - backPosX, 35 - backPosY);
		itemMapper.map(IngredientSorption.OUTPUT, 0, 1, 102 - backPosX, 35 - backPosY);
		itemMapper.map(IngredientSorption.OUTPUT, 1, 2, 130 - backPosX, 35 - backPosY);
		itemMapper.mapItemsTo(recipeLayout.getItemStacks(), ingredients);
	}
}
