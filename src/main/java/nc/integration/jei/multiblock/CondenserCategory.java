package nc.integration.jei.multiblock;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import nc.Global;
import nc.integration.jei.IJEIHandler;
import nc.integration.jei.JEICategoryProcessor;
import nc.integration.jei.JEIMethods.RecipeFluidMapper;
import nc.integration.jei.JEIRecipeWrapper;
import nc.recipe.IngredientSorption;
import nc.util.Lang;

public class CondenserCategory extends JEICategoryProcessor<JEIRecipeWrapper.Condenser> {
	
	public CondenserCategory(IGuiHelper guiHelper, IJEIHandler handler) {
		super(guiHelper, handler, "condenser", 47, 30, 90, 26);
		recipeTitle = Lang.localise(Global.MOD_ID + ".multiblock_gui.condenser.jei_name");
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipeWrapper.Condenser recipeWrapper, IIngredients ingredients) {
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);
		
		RecipeFluidMapper fluidMapper = new RecipeFluidMapper();
		fluidMapper.map(IngredientSorption.INPUT, 0, 0, 56 - backPosX, 35 - backPosY, 16, 16);
		fluidMapper.map(IngredientSorption.OUTPUT, 0, 1, 112 - backPosX, 31 - backPosY, 24, 24);
		fluidMapper.mapFluidsTo(recipeLayout.getFluidStacks(), ingredients);
	}
}
