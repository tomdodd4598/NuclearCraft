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

public class DissolverCategory extends JEIProcessorCategory {
	
	public DissolverCategory(IGuiHelper guiHelper, IJEIHandler handler) {
		super(guiHelper, handler, "dissolver_idle", 45, 30, 102, 26);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		RecipeItemMapper itemMapper = new RecipeItemMapper();
		RecipeFluidMapper fluidMapper = new RecipeFluidMapper();
		itemMapper.map(SorptionType.INPUT, 0, 0, 46 - backPosX, 35 - backPosY);
		fluidMapper.map(SorptionType.INPUT, 0, 0, 66 - backPosX, 35 - backPosY, 16, 16);
		fluidMapper.map(SorptionType.OUTPUT, 0, 1, 122 - backPosX, 31 - backPosY, 24, 24);
		itemMapper.mapItemsTo(recipeLayout.getItemStacks(), ingredients);
		fluidMapper.mapFluidsTo(recipeLayout.getFluidStacks(), ingredients);
	}
}
