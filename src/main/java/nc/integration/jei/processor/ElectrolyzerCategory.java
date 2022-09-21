package nc.integration.jei.processor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import nc.integration.jei.*;
import nc.integration.jei.JEIHelper.RecipeFluidMapper;
import nc.integration.jei.NCJEI.IJEIHandler;
import nc.recipe.IngredientSorption;

public class ElectrolyzerCategory extends JEIMachineCategory<JEIRecipe.Electrolyzer> {
	
	public ElectrolyzerCategory(IGuiHelper guiHelper, IJEIHandler<JEIRecipe.Electrolyzer> handler) {
		super(guiHelper, handler, "electrolyzer", 49, 30, 94, 38);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipe.Electrolyzer recipeWrapper, IIngredients ingredients) {
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);
		
		RecipeFluidMapper fluidMapper = new RecipeFluidMapper();
		fluidMapper.map(IngredientSorption.INPUT, 0, 0, 50 - backPosX, 41 - backPosY, 16, 16);
		fluidMapper.map(IngredientSorption.OUTPUT, 0, 1, 106 - backPosX, 31 - backPosY, 16, 16);
		fluidMapper.map(IngredientSorption.OUTPUT, 1, 2, 126 - backPosX, 31 - backPosY, 16, 16);
		fluidMapper.map(IngredientSorption.OUTPUT, 2, 3, 106 - backPosX, 51 - backPosY, 16, 16);
		fluidMapper.map(IngredientSorption.OUTPUT, 3, 4, 126 - backPosX, 51 - backPosY, 16, 16);
		fluidMapper.mapFluidsTo(recipeLayout.getFluidStacks(), ingredients);
	}
}
