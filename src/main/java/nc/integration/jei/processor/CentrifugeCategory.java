package nc.integration.jei.processor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import nc.integration.jei.*;
import nc.integration.jei.JEIHelper.RecipeFluidMapper;
import nc.integration.jei.NCJEI.IJEIHandler;
import nc.recipe.IngredientSorption;

public class CentrifugeCategory extends JEIMachineCategory<JEIRecipe.Centrifuge> {
	
	public CentrifugeCategory(IGuiHelper guiHelper, IJEIHandler<JEIRecipe.Centrifuge> handler) {
		super(guiHelper, handler, "centrifuge", 39, 30, 114, 38);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipe.Centrifuge recipeWrapper, IIngredients ingredients) {
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);
		
		RecipeFluidMapper fluidMapper = new RecipeFluidMapper();
		fluidMapper.map(IngredientSorption.INPUT, 0, 0, 40 - backPosX, 41 - backPosY, 16, 16);
		fluidMapper.map(IngredientSorption.OUTPUT, 0, 1, 96 - backPosX, 31 - backPosY, 16, 16);
		fluidMapper.map(IngredientSorption.OUTPUT, 1, 2, 116 - backPosX, 31 - backPosY, 16, 16);
		fluidMapper.map(IngredientSorption.OUTPUT, 2, 3, 136 - backPosX, 31 - backPosY, 16, 16);
		fluidMapper.map(IngredientSorption.OUTPUT, 3, 4, 96 - backPosX, 51 - backPosY, 16, 16);
		fluidMapper.map(IngredientSorption.OUTPUT, 4, 5, 116 - backPosX, 51 - backPosY, 16, 16);
		fluidMapper.map(IngredientSorption.OUTPUT, 5, 6, 136 - backPosX, 51 - backPosY, 16, 16);
		fluidMapper.mapFluidsTo(recipeLayout.getFluidStacks(), ingredients);
	}
}
