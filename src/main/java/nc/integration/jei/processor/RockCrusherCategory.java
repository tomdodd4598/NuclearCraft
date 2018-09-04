package nc.integration.jei.processor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import nc.integration.jei.IJEIHandler;
import nc.integration.jei.JEIMethods.RecipeItemMapper;
import nc.integration.jei.JEICategoryAbstract;
import nc.recipe.SorptionType;

public class RockCrusherCategory extends JEICategoryAbstract {
	
	public RockCrusherCategory(IGuiHelper guiHelper, IJEIHandler handler) {
		super(guiHelper, handler, "rock_crusher_idle", 29, 30, 122, 26);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		RecipeItemMapper itemMapper = new RecipeItemMapper();
		itemMapper.map(SorptionType.INPUT, 0, 0, 38 - backPosX, 35 - backPosY);
		itemMapper.map(SorptionType.OUTPUT, 0, 1, 94 - backPosX, 35 - backPosY);
		itemMapper.map(SorptionType.OUTPUT, 1, 2, 114 - backPosX, 35 - backPosY);
		itemMapper.map(SorptionType.OUTPUT, 2, 3, 134 - backPosX, 35 - backPosY);
		itemMapper.mapItemsTo(recipeLayout.getItemStacks(), ingredients);
	}
}
