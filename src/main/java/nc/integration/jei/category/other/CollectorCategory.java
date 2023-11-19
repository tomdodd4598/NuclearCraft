package nc.integration.jei.category.other;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import nc.Global;
import nc.integration.jei.JEIHelper.*;
import nc.integration.jei.NCJEI.IJEIHandler;
import nc.integration.jei.category.JEIMachineCategory;
import nc.integration.jei.wrapper.JEIRecipeWrapperImpl;
import nc.recipe.IngredientSorption;
import nc.util.Lang;

public class CollectorCategory extends JEIMachineCategory<JEIRecipeWrapperImpl.Collector> {
	
	public CollectorCategory(IGuiHelper guiHelper, IJEIHandler<JEIRecipeWrapperImpl.Collector> handler) {
		super(guiHelper, handler, "collector", 33, 30, 118, 26);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipeWrapperImpl.Collector recipeWrapper, IIngredients ingredients) {
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);
		
		RecipeItemMapper itemMapper = new RecipeItemMapper();
		RecipeFluidMapper fluidMapper = new RecipeFluidMapper();
		itemMapper.map(IngredientSorption.INPUT, 0, 0, 42 - backPosX, 35 - backPosY);
		itemMapper.map(IngredientSorption.OUTPUT, 0, 1, 102 - backPosX, 35 - backPosY);
		fluidMapper.map(IngredientSorption.OUTPUT, 0, 0, 126 - backPosX, 31 - backPosY, 24, 24);
		itemMapper.mapItemsTo(recipeLayout.getItemStacks(), ingredients);
		fluidMapper.mapFluidsTo(recipeLayout.getFluidStacks(), ingredients);
	}
	
	@Override
	public String getTitle() {
		return Lang.localize(Global.MOD_ID + ".collector.jei_name");
	}
}
