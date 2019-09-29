package nc.integration.jei.multiblock;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import nc.Global;
import nc.integration.jei.IJEIHandler;
import nc.integration.jei.JEICategoryProcessor;
import nc.integration.jei.JEIMethods.RecipeItemMapper;
import nc.integration.jei.JEIRecipeWrapper;
import nc.recipe.IngredientSorption;
import nc.util.Lang;

public class FissionReflectorCategory extends JEICategoryProcessor<JEIRecipeWrapper.FissionReflector> {
	
	public FissionReflectorCategory(IGuiHelper guiHelper, IJEIHandler handler) {
		super(guiHelper, handler, "fission_reflector", 47, 30, 90, 26);
		recipeTitle = Lang.localise(Global.MOD_ID + ".multiblock_gui.fission_reflector.jei_name");
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipeWrapper.FissionReflector recipeWrapper, IIngredients ingredients) {
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);
		
		RecipeItemMapper itemMapper = new RecipeItemMapper();
		itemMapper.map(IngredientSorption.INPUT, 0, 0, 86 - backPosX, 35 - backPosY);
		itemMapper.mapItemsTo(recipeLayout.getItemStacks(), ingredients);
	}
}
