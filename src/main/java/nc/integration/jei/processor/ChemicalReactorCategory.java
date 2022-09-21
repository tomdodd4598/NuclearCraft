package nc.integration.jei.processor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import nc.integration.jei.*;
import nc.integration.jei.JEIHelper.RecipeFluidMapper;
import nc.integration.jei.NCJEI.IJEIHandler;
import nc.recipe.IngredientSorption;

public class ChemicalReactorCategory extends JEIMachineCategory<JEIRecipe.ChemicalReactor> {
	
	public ChemicalReactorCategory(IGuiHelper guiHelper, IJEIHandler<JEIRecipe.ChemicalReactor> handler) {
		super(guiHelper, handler, "chemical_reactor", 31, 30, 130, 26);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipe.ChemicalReactor recipeWrapper, IIngredients ingredients) {
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);
		
		RecipeFluidMapper fluidMapper = new RecipeFluidMapper();
		fluidMapper.map(IngredientSorption.INPUT, 0, 0, 32 - backPosX, 35 - backPosY, 16, 16);
		fluidMapper.map(IngredientSorption.INPUT, 1, 1, 52 - backPosX, 35 - backPosY, 16, 16);
		fluidMapper.map(IngredientSorption.OUTPUT, 0, 2, 108 - backPosX, 31 - backPosY, 24, 24);
		fluidMapper.map(IngredientSorption.OUTPUT, 1, 3, 136 - backPosX, 31 - backPosY, 24, 24);
		fluidMapper.mapFluidsTo(recipeLayout.getFluidStacks(), ingredients);
	}
}
