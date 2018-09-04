package nc.integration.jei.processor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import nc.integration.jei.IJEIHandler;
import nc.integration.jei.JEIMethods.RecipeFluidMapper;
import nc.integration.jei.JEICategoryAbstract;
import nc.recipe.SorptionType;

public class ElectrolyserCategory extends JEICategoryAbstract {
	
	public ElectrolyserCategory(IGuiHelper guiHelper, IJEIHandler handler) {
		super(guiHelper, handler, "electrolyser_idle", 49, 30, 94, 38);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		RecipeFluidMapper fluidMapper = new RecipeFluidMapper();
		fluidMapper.map(SorptionType.INPUT, 0, 0, 50 - backPosX, 41 - backPosY, 16, 16);
		fluidMapper.map(SorptionType.OUTPUT, 0, 1, 106 - backPosX, 31 - backPosY, 16, 16);
		fluidMapper.map(SorptionType.OUTPUT, 1, 2, 126 - backPosX, 31 - backPosY, 16, 16);
		fluidMapper.map(SorptionType.OUTPUT, 2, 3, 106 - backPosX, 51 - backPosY, 16, 16);
		fluidMapper.map(SorptionType.OUTPUT, 3, 4, 126 - backPosX, 51 - backPosY, 16, 16);
		fluidMapper.mapFluidsTo(recipeLayout.getFluidStacks(), ingredients);
	}
}
