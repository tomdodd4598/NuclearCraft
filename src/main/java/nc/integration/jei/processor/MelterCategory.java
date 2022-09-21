package nc.integration.jei.processor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import nc.integration.jei.*;
import nc.integration.jei.JEIHelper.*;
import nc.integration.jei.NCJEI.IJEIHandler;
import nc.recipe.IngredientSorption;

public class MelterCategory extends JEIMachineCategory<JEIRecipe.Melter> {
	
	public MelterCategory(IGuiHelper guiHelper, IJEIHandler<JEIRecipe.Melter> handler) {
		super(guiHelper, handler, "melter", 47, 30, 90, 26);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipe.Melter recipeWrapper, IIngredients ingredients) {
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);
		
		RecipeItemMapper itemMapper = new RecipeItemMapper();
		RecipeFluidMapper fluidMapper = new RecipeFluidMapper();
		itemMapper.map(IngredientSorption.INPUT, 0, 0, 56 - backPosX, 35 - backPosY);
		fluidMapper.map(IngredientSorption.OUTPUT, 0, 0, 112 - backPosX, 31 - backPosY, 24, 24);
		itemMapper.mapItemsTo(recipeLayout.getItemStacks(), ingredients);
		fluidMapper.mapFluidsTo(recipeLayout.getFluidStacks(), ingredients);
	}
}
