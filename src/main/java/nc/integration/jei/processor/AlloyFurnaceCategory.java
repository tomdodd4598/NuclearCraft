package nc.integration.jei.processor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import nc.integration.jei.*;
import nc.integration.jei.JEIHelper.RecipeItemMapper;
import nc.integration.jei.NCJEI.IJEIHandler;
import nc.recipe.IngredientSorption;

public class AlloyFurnaceCategory extends JEIMachineCategory<JEIRecipe.AlloyFurnace> {
	
	public AlloyFurnaceCategory(IGuiHelper guiHelper, IJEIHandler<JEIRecipe.AlloyFurnace> handler) {
		super(guiHelper, handler, "alloy_furnace", 45, 30, 102, 26);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipe.AlloyFurnace recipeWrapper, IIngredients ingredients) {
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);
		
		RecipeItemMapper itemMapper = new RecipeItemMapper();
		itemMapper.map(IngredientSorption.INPUT, 0, 0, 46 - backPosX, 35 - backPosY);
		itemMapper.map(IngredientSorption.INPUT, 1, 1, 66 - backPosX, 35 - backPosY);
		itemMapper.map(IngredientSorption.OUTPUT, 0, 2, 126 - backPosX, 35 - backPosY);
		itemMapper.mapItemsTo(recipeLayout.getItemStacks(), ingredients);
	}
}
