package nc.integration.jei.processor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import nc.integration.jei.*;
import nc.integration.jei.JEIHelper.RecipeFluidMapper;
import nc.integration.jei.NCJEI.IJEIHandler;
import nc.recipe.IngredientSorption;

public class SupercoolerCategory extends JEIMachineCategory<JEIRecipe.Supercooler> {
	
	public SupercoolerCategory(IGuiHelper guiHelper, IJEIHandler<JEIRecipe.Supercooler> handler) {
		super(guiHelper, handler, "supercooler", 47, 30, 90, 26);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipe.Supercooler recipeWrapper, IIngredients ingredients) {
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);
		
		RecipeFluidMapper fluidMapper = new RecipeFluidMapper();
		fluidMapper.map(IngredientSorption.INPUT, 0, 0, 56 - backPosX, 35 - backPosY, 16, 16);
		fluidMapper.map(IngredientSorption.OUTPUT, 0, 1, 112 - backPosX, 31 - backPosY, 24, 24);
		fluidMapper.mapFluidsTo(recipeLayout.getFluidStacks(), ingredients);
	}
}
