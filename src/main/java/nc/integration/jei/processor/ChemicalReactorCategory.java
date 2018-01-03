package nc.integration.jei.processor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import nc.config.NCConfig;
import nc.integration.jei.BaseCategory;
import nc.integration.jei.IJEIHandler;
import nc.integration.jei.JEIMethods.RecipeFluidMapper;
import nc.recipe.SorptionType;

public class ChemicalReactorCategory extends BaseCategory {
	
	public ChemicalReactorCategory(IGuiHelper guiHelper, IJEIHandler handler) {
		super(guiHelper, handler, "chemical_reactor_idle", NCConfig.processor_time[12], 31, 30, 130, 26, 176, 3, 37, 18, 70, 34);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		RecipeFluidMapper fluidMapper = new RecipeFluidMapper();
		fluidMapper.map(SorptionType.INPUT, 0, 0, 32 - backPosX, 35 - backPosY, 16, 16);
		fluidMapper.map(SorptionType.INPUT, 1, 1, 52 - backPosX, 35 - backPosY, 16, 16);
		fluidMapper.map(SorptionType.OUTPUT, 0, 2, 108 - backPosX, 31 - backPosY, 24, 24);
		fluidMapper.map(SorptionType.OUTPUT, 1, 3, 136 - backPosX, 31 - backPosY, 24, 24);
		fluidMapper.mapFluidsTo(recipeLayout.getFluidStacks(), ingredients);
	}
}
