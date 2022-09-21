package nc.integration.jei.processor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import nc.integration.jei.*;
import nc.integration.jei.JEIHelper.RecipeItemMapper;
import nc.integration.jei.NCJEI.IJEIHandler;
import nc.recipe.IngredientSorption;

public class PressurizerCategory extends JEIMachineCategory<JEIRecipe.Pressurizer> {
	
	public PressurizerCategory(IGuiHelper guiHelper, IJEIHandler<JEIRecipe.Pressurizer> handler) {
		super(guiHelper, handler, "pressurizer", 47, 30, 90, 26);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipe.Pressurizer recipeWrapper, IIngredients ingredients) {
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);
		
		RecipeItemMapper itemMapper = new RecipeItemMapper();
		itemMapper.map(IngredientSorption.INPUT, 0, 0, 56 - backPosX, 35 - backPosY);
		itemMapper.map(IngredientSorption.OUTPUT, 0, 1, 116 - backPosX, 35 - backPosY);
		itemMapper.mapItemsTo(recipeLayout.getItemStacks(), ingredients);
	}
}
