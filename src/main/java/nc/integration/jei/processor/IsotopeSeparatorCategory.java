package nc.integration.jei.processor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import nc.integration.jei.IJEIHandler;
import nc.integration.jei.JEIMethods.RecipeItemMapper;
import nc.integration.jei.JEICategoryAbstract;
import nc.recipe.SorptionType;

public class IsotopeSeparatorCategory extends JEICategoryAbstract {
	
	public IsotopeSeparatorCategory(IGuiHelper guiHelper, IJEIHandler handler) {
		super(guiHelper, handler, "isotope_separator_idle", 33, 30, 118, 26);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		RecipeItemMapper itemMapper = new RecipeItemMapper();
		itemMapper.map(SorptionType.INPUT, 0, 0, 42 - backPosX, 35 - backPosY);
		itemMapper.map(SorptionType.OUTPUT, 0, 1, 102 - backPosX, 35 - backPosY);
		itemMapper.map(SorptionType.OUTPUT, 1, 2, 130 - backPosX, 35 - backPosY);
		itemMapper.mapItemsTo(recipeLayout.getItemStacks(), ingredients);
	}
}
