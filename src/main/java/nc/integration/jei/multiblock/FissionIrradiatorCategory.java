package nc.integration.jei.multiblock;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import nc.Global;
import nc.integration.jei.*;
import nc.integration.jei.JEIHelper.RecipeItemMapper;
import nc.integration.jei.NCJEI.IJEIHandler;
import nc.recipe.IngredientSorption;
import nc.util.Lang;

public class FissionIrradiatorCategory extends JEIMachineCategory<JEIRecipe.FissionIrradiator> {
	
	public FissionIrradiatorCategory(IGuiHelper guiHelper, IJEIHandler<JEIRecipe.FissionIrradiator> handler) {
		super(guiHelper, handler, "fission_irradiator", 47, 30, 90, 26);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipe.FissionIrradiator recipeWrapper, IIngredients ingredients) {
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);
		
		RecipeItemMapper itemMapper = new RecipeItemMapper();
		itemMapper.map(IngredientSorption.INPUT, 0, 0, 56 - backPosX, 35 - backPosY);
		itemMapper.map(IngredientSorption.OUTPUT, 0, 1, 116 - backPosX, 35 - backPosY);
		itemMapper.mapItemsTo(recipeLayout.getItemStacks(), ingredients);
	}
	
	@Override
	public String getTitle() {
		return Lang.localise(Global.MOD_ID + ".multiblock_gui.fission_irradiator.jei_name");
	}
}
