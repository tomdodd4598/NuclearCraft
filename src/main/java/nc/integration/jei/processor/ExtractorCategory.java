package nc.integration.jei.processor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import nc.integration.jei.IJEIHandler;
import nc.integration.jei.JEIMethods.RecipeFluidMapper;
import nc.integration.jei.JEIMethods.RecipeItemMapper;
import nc.integration.jei.JEIProcessorCategory;
import nc.recipe.SorptionType;

public class ExtractorCategory extends JEIProcessorCategory {
	
	public ExtractorCategory(IGuiHelper guiHelper, IJEIHandler handler) {
		super(guiHelper, handler, "extractor_idle", 33, 30, 118, 26);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		RecipeItemMapper itemMapper = new RecipeItemMapper();
		RecipeFluidMapper fluidMapper = new RecipeFluidMapper();
		itemMapper.map(SorptionType.INPUT, 0, 0, 42 - backPosX, 35 - backPosY);
		itemMapper.map(SorptionType.OUTPUT, 0, 1, 102 - backPosX, 35 - backPosY);
		fluidMapper.map(SorptionType.OUTPUT, 0, 0, 126 - backPosX, 31 - backPosY, 24, 24);
		itemMapper.mapItemsTo(recipeLayout.getItemStacks(), ingredients);
		fluidMapper.mapFluidsTo(recipeLayout.getFluidStacks(), ingredients);
	}
}
