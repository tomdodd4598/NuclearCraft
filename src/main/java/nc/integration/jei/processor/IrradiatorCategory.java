package nc.integration.jei.processor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import nc.integration.jei.IJEIHandler;
import nc.integration.jei.JEIMethods.RecipeFluidMapper;
import nc.integration.jei.JEICategoryAbstract;
import nc.recipe.SorptionType;

public class IrradiatorCategory extends JEICategoryAbstract {
	
	public IrradiatorCategory(IGuiHelper guiHelper, IJEIHandler handler) {
		super(guiHelper, handler, "irradiator_idle", 31, 30, 130, 26);
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
