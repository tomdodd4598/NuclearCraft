package nc.integration.jei.processor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import nc.integration.jei.IJEIHandler;
import nc.integration.jei.JEICategoryProcessor;
import nc.integration.jei.JEIMethods.RecipeFluidMapper;
import nc.integration.jei.JEIMethods.RecipeItemMapper;
import nc.integration.jei.JEIRecipeWrapper;
import nc.recipe.IngredientSorption;

public class InfuserCategory extends JEICategoryProcessor<JEIRecipeWrapper.Infuser> {
	
	public InfuserCategory(IGuiHelper guiHelper, IJEIHandler handler) {
		super(guiHelper, handler, "infuser", 45, 30, 102, 26);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipeWrapper.Infuser recipeWrapper, IIngredients ingredients) {
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);
		
		RecipeItemMapper itemMapper = new RecipeItemMapper();
		RecipeFluidMapper fluidMapper = new RecipeFluidMapper();
		itemMapper.map(IngredientSorption.INPUT, 0, 0, 46 - backPosX, 35 - backPosY);
		fluidMapper.map(IngredientSorption.INPUT, 0, 0, 66 - backPosX, 35 - backPosY, 16, 16);
		itemMapper.map(IngredientSorption.OUTPUT, 0, 1, 126 - backPosX, 35 - backPosY);
		itemMapper.mapItemsTo(recipeLayout.getItemStacks(), ingredients);
		fluidMapper.mapFluidsTo(recipeLayout.getFluidStacks(), ingredients);
	}
}
