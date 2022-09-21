package nc.integration.jei.processor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import nc.integration.jei.*;
import nc.integration.jei.JEIHelper.RecipeItemMapper;
import nc.integration.jei.NCJEI.IJEIHandler;
import nc.recipe.IngredientSorption;

public class AssemblerCategory extends JEIMachineCategory<JEIRecipe.Assembler> {
	
	public AssemblerCategory(IGuiHelper guiHelper, IJEIHandler<JEIRecipe.Assembler> handler) {
		super(guiHelper, handler, "assembler", 45, 30, 102, 38);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipe.Assembler recipeWrapper, IIngredients ingredients) {
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
