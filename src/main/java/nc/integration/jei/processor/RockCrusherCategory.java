package nc.integration.jei.processor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import nc.integration.jei.*;
import nc.integration.jei.JEIHelper.RecipeItemMapper;
import nc.integration.jei.NCJEI.IJEIHandler;
import nc.recipe.IngredientSorption;

public class RockCrusherCategory extends JEIMachineCategory<JEIRecipe.RockCrusher> {
	
	public RockCrusherCategory(IGuiHelper guiHelper, IJEIHandler<JEIRecipe.RockCrusher> handler) {
		super(guiHelper, handler, "rock_crusher", 29, 30, 122, 26);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipe.RockCrusher recipeWrapper, IIngredients ingredients) {
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);
		
		RecipeItemMapper itemMapper = new RecipeItemMapper();
		itemMapper.map(IngredientSorption.INPUT, 0, 0, 38 - backPosX, 35 - backPosY);
		itemMapper.map(IngredientSorption.OUTPUT, 0, 1, 94 - backPosX, 35 - backPosY);
		itemMapper.map(IngredientSorption.OUTPUT, 1, 2, 114 - backPosX, 35 - backPosY);
		itemMapper.map(IngredientSorption.OUTPUT, 2, 3, 134 - backPosX, 35 - backPosY);
		itemMapper.mapItemsTo(recipeLayout.getItemStacks(), ingredients);
	}
}
