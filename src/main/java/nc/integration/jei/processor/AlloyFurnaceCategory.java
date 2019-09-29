package nc.integration.jei.processor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import nc.integration.jei.IJEIHandler;
import nc.integration.jei.JEICategoryProcessor;
import nc.integration.jei.JEIMethods.RecipeItemMapper;
import nc.integration.jei.JEIRecipeWrapper;
import nc.recipe.IngredientSorption;

public class AlloyFurnaceCategory extends JEICategoryProcessor<JEIRecipeWrapper.AlloyFurnace> {
	
	public AlloyFurnaceCategory(IGuiHelper guiHelper, IJEIHandler handler) {
		super(guiHelper, handler, "alloy_furnace", 45, 30, 102, 26);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipeWrapper.AlloyFurnace recipeWrapper, IIngredients ingredients) {
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);
		
		RecipeItemMapper itemMapper = new RecipeItemMapper();
		itemMapper.map(IngredientSorption.INPUT, 0, 0, 46 - backPosX, 35 - backPosY);
		itemMapper.map(IngredientSorption.INPUT, 1, 1, 66 - backPosX, 35 - backPosY);
		itemMapper.map(IngredientSorption.OUTPUT, 0, 2, 126 - backPosX, 35 - backPosY);
		itemMapper.mapItemsTo(recipeLayout.getItemStacks(), ingredients);
	}
}
