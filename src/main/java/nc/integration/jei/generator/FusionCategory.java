package nc.integration.jei.generator;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import nc.config.NCConfig;
import nc.integration.jei.BaseCategory;
import nc.integration.jei.IJEIHandler;
import nc.integration.jei.JEIMethods.RecipeFluidMapper;
import nc.recipe.SorptionType;
import nc.util.Lang;

public class FusionCategory extends BaseCategory {
	
	public FusionCategory(IGuiHelper guiHelper, IJEIHandler handler) {
		super(guiHelper, handler, "fusion_core", "_jei", (int) (NCConfig.fusion_fuel_use*200), 55, 30, 94, 26, 176, 3, 37, 16, 74, 35);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		RecipeFluidMapper fluidMapper = new RecipeFluidMapper();
		fluidMapper.map(SorptionType.INPUT, 0, 0, 56 - backPosX, 31 - backPosY, 6, 24);
		fluidMapper.map(SorptionType.INPUT, 1, 1, 66 - backPosX, 31 - backPosY, 6, 24);
		fluidMapper.map(SorptionType.OUTPUT, 0, 2, 112 - backPosX, 31 - backPosY, 6, 24);
		fluidMapper.map(SorptionType.OUTPUT, 1, 3, 122 - backPosX, 31 - backPosY, 6, 24);
		fluidMapper.map(SorptionType.OUTPUT, 2, 4, 132 - backPosX, 31 - backPosY, 6, 24);
		fluidMapper.map(SorptionType.OUTPUT, 3, 5, 142 - backPosX, 31 - backPosY, 6, 24);
		fluidMapper.mapFluidsTo(recipeLayout.getFluidStacks(), ingredients);
	}
	
	@Override
	public String getTitle() {
		return Lang.localise("gui.container.fusion_core.reactor");
	}
}
