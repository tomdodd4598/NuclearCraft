package nc.integration.jei.processor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import nc.integration.jei.IJEIHandler;
import nc.integration.jei.JEICategoryProcessor;
import nc.integration.jei.JEIMethods.RecipeItemMapper;
import nc.integration.jei.JEIRecipeWrapper;
import nc.recipe.IngredientSorption;

public class AssemblerCategory extends JEICategoryProcessor<JEIRecipeWrapper.Assembler> {
	
	public AssemblerCategory(IGuiHelper guiHelper, IJEIHandler handler) {
		super(guiHelper, handler, "assembler", 45, 30, 102, 38);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipeWrapper.Assembler recipeWrapper, IIngredients ingredients) {
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);
		
		RecipeItemMapper itemMapper = new RecipeItemMapper();
		itemMapper.map(IngredientSorption.INPUT, 0, 0, 46 - backPosX, 31 - backPosY);
		itemMapper.map(IngredientSorption.INPUT, 1, 1, 66 - backPosX, 31 - backPosY);
		itemMapper.map(IngredientSorption.INPUT, 2, 2, 46 - backPosX, 51 - backPosY);
		itemMapper.map(IngredientSorption.INPUT, 3, 3, 66 - backPosX, 51 - backPosY);
		itemMapper.map(IngredientSorption.OUTPUT, 0, 4, 126 - backPosX, 41 - backPosY);
		itemMapper.mapItemsTo(recipeLayout.getItemStacks(), ingredients);
	}
}
