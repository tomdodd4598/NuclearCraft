package nc.integration.jei.processor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import nc.integration.jei.IJEIHandler;
import nc.integration.jei.JEICategoryProcessor;
import nc.integration.jei.JEIMethods.RecipeItemMapper;
import nc.integration.jei.JEIRecipeWrapper;
import nc.recipe.IngredientSorption;

public class FuelReprocessorCategory extends JEICategoryProcessor<JEIRecipeWrapper.FuelReprocessor> {
	
	public FuelReprocessorCategory(IGuiHelper guiHelper, IJEIHandler handler) {
		super(guiHelper, handler, "fuel_reprocessor", 39, 30, 114, 38);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipeWrapper.FuelReprocessor recipeWrapper, IIngredients ingredients) {
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);
		
		RecipeItemMapper itemMapper = new RecipeItemMapper();
		itemMapper.map(IngredientSorption.INPUT, 0, 0, 40 - backPosX, 41 - backPosY);
		itemMapper.map(IngredientSorption.OUTPUT, 0, 1, 96 - backPosX, 31 - backPosY);
		itemMapper.map(IngredientSorption.OUTPUT, 1, 2, 116 - backPosX, 31 - backPosY);
		itemMapper.map(IngredientSorption.OUTPUT, 2, 3, 136 - backPosX, 31 - backPosY);
		itemMapper.map(IngredientSorption.OUTPUT, 3, 4, 96 - backPosX, 51 - backPosY);
		itemMapper.map(IngredientSorption.OUTPUT, 4, 5, 116 - backPosX, 51 - backPosY);
		itemMapper.map(IngredientSorption.OUTPUT, 5, 6, 136 - backPosX, 51 - backPosY);
		itemMapper.mapItemsTo(recipeLayout.getItemStacks(), ingredients);
	}
}
