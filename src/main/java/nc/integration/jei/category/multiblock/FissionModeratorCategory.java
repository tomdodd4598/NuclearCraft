package nc.integration.jei.category.multiblock;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import nc.Global;
import nc.integration.jei.JEIHelper.RecipeItemMapper;
import nc.integration.jei.NCJEI.IJEIHandler;
import nc.integration.jei.category.JEIMachineCategory;
import nc.integration.jei.wrapper.JEIRecipeWrapperImpl;
import nc.recipe.IngredientSorption;
import nc.util.Lang;

public class FissionModeratorCategory extends JEIMachineCategory<JEIRecipeWrapperImpl.FissionModerator> {
	
	public FissionModeratorCategory(IGuiHelper guiHelper, IJEIHandler<JEIRecipeWrapperImpl.FissionModerator> handler) {
		super(guiHelper, handler, "fission_moderator", 47, 30, 90, 26);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipeWrapperImpl.FissionModerator recipeWrapper, IIngredients ingredients) {
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);
		
		RecipeItemMapper itemMapper = new RecipeItemMapper();
		itemMapper.map(IngredientSorption.INPUT, 0, 0, 86 - backPosX, 35 - backPosY);
		itemMapper.mapItemsTo(recipeLayout.getItemStacks(), ingredients);
	}
	
	@Override
	public String getTitle() {
		return Lang.localize(Global.MOD_ID + ".fission_moderator.jei_name");
	}
}
