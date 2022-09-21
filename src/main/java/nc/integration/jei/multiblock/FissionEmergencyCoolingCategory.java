package nc.integration.jei.multiblock;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import nc.Global;
import nc.integration.jei.*;
import nc.integration.jei.JEIHelper.RecipeFluidMapper;
import nc.integration.jei.NCJEI.IJEIHandler;
import nc.recipe.IngredientSorption;
import nc.util.Lang;

public class FissionEmergencyCoolingCategory extends JEIMachineCategory<JEIRecipe.FissionEmergencyCooling> {
	
	public FissionEmergencyCoolingCategory(IGuiHelper guiHelper, IJEIHandler<JEIRecipe.FissionEmergencyCooling> handler) {
		super(guiHelper, handler, "fission_vent", 47, 30, 90, 26);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipe.FissionEmergencyCooling recipeWrapper, IIngredients ingredients) {
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);
		
		RecipeFluidMapper fluidMapper = new RecipeFluidMapper();
		fluidMapper.map(IngredientSorption.INPUT, 0, 0, 56 - backPosX, 35 - backPosY, 16, 16);
		fluidMapper.map(IngredientSorption.OUTPUT, 0, 1, 112 - backPosX, 31 - backPosY, 24, 24);
		fluidMapper.mapFluidsTo(recipeLayout.getFluidStacks(), ingredients);
	}
	
	@Override
	public String getTitle() {
		return Lang.localise(Global.MOD_ID + ".fission_emergency_cooling.jei_name");
	}
}
