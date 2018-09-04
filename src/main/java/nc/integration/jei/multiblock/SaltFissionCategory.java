package nc.integration.jei.multiblock;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import nc.Global;
import nc.integration.jei.IJEIHandler;
import nc.integration.jei.JEIMethods.RecipeFluidMapper;
import nc.integration.jei.JEICategoryAbstract;
import nc.recipe.SorptionType;
import nc.util.Lang;

public class SaltFissionCategory extends JEICategoryAbstract {
	
	public SaltFissionCategory(IGuiHelper guiHelper, IJEIHandler handler) {
		super(guiHelper, handler, "salt_fission_vessel", 47, 30, 90, 26);
		recipeTitle = Lang.localise(Global.MOD_ID + ".multiblock_gui.salt_fission.jei_name");
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		RecipeFluidMapper fluidMapper = new RecipeFluidMapper();
		fluidMapper.map(SorptionType.INPUT, 0, 0, 56 - backPosX, 35 - backPosY, 16, 16);
		fluidMapper.map(SorptionType.OUTPUT, 0, 1, 112 - backPosX, 31 - backPosY, 24, 24);
		fluidMapper.mapFluidsTo(recipeLayout.getFluidStacks(), ingredients);
	}
	
	@Override
	public String getTitle() {
		return recipeTitle;
	}
}
