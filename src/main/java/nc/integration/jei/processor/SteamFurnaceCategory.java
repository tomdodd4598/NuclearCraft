package nc.integration.jei.processor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import nc.integration.jei.JEIHelper.RecipeFluidMapper;
import nc.integration.jei.JEIHelper.RecipeItemMapper;
import nc.integration.jei.JEIMachineCategory;
import nc.integration.jei.JEIRecipeWrapper;
import nc.integration.jei.NCJEI.IJEIHandler;
import nc.recipe.IngredientSorption;

public class SteamFurnaceCategory extends JEIMachineCategory<JEIRecipeWrapper.SteamFurnace> {

	public SteamFurnaceCategory(IGuiHelper guiHelper, IJEIHandler handler) {
		super(guiHelper, handler, "steam_furnace", 45, 30, 102, 26);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipeWrapper.SteamFurnace recipeWrapper, IIngredients ingredients) {
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);
		
		RecipeItemMapper itemMapper = new RecipeItemMapper();
		RecipeFluidMapper fluidMapper = new RecipeFluidMapper();
		itemMapper.map(IngredientSorption.INPUT, 0, 0, 46 - backPosX, 35 - backPosY);
		fluidMapper.map(IngredientSorption.INPUT, 0, 0, 66 - backPosX, 35 - backPosY, 16, 16);
		itemMapper.map(IngredientSorption.OUTPUT, 0, 1, 126 - backPosX, 35 - backPosY);
		itemMapper.mapItemsTo(recipeLayout.getItemStacks(), ingredients);
		fluidMapper.mapFluidsTo(recipeLayout.getFluidStacks(), ingredients);
	}
}
