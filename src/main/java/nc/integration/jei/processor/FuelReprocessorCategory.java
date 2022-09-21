package nc.integration.jei.processor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import nc.integration.jei.*;
import nc.integration.jei.JEIHelper.RecipeItemMapper;
import nc.integration.jei.NCJEI.IJEIHandler;
import nc.recipe.IngredientSorption;

public class FuelReprocessorCategory extends JEIMachineCategory<JEIRecipe.FuelReprocessor> {
	
	public FuelReprocessorCategory(IGuiHelper guiHelper, IJEIHandler<JEIRecipe.FuelReprocessor> handler) {
		super(guiHelper, handler, "fuel_reprocessor", 29, 30, 134, 38);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipe.FuelReprocessor recipeWrapper, IIngredients ingredients) {
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);
		
		RecipeItemMapper itemMapper = new RecipeItemMapper();
		itemMapper.map(IngredientSorption.INPUT, 0, 0, 30 - backPosX, 41 - backPosY);
		itemMapper.map(IngredientSorption.OUTPUT, 0, 1, 86 - backPosX, 31 - backPosY);
		itemMapper.map(IngredientSorption.OUTPUT, 1, 2, 106 - backPosX, 31 - backPosY);
		itemMapper.map(IngredientSorption.OUTPUT, 2, 3, 126 - backPosX, 31 - backPosY);
		itemMapper.map(IngredientSorption.OUTPUT, 3, 4, 146 - backPosX, 31 - backPosY);
		itemMapper.map(IngredientSorption.OUTPUT, 4, 5, 86 - backPosX, 51 - backPosY);
		itemMapper.map(IngredientSorption.OUTPUT, 5, 6, 106 - backPosX, 51 - backPosY);
		itemMapper.map(IngredientSorption.OUTPUT, 6, 7, 126 - backPosX, 51 - backPosY);
		itemMapper.map(IngredientSorption.OUTPUT, 7, 8, 146 - backPosX, 51 - backPosY);
		itemMapper.mapItemsTo(recipeLayout.getItemStacks(), ingredients);
	}
}
