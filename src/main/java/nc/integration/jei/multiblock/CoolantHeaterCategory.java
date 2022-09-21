package nc.integration.jei.multiblock;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import nc.Global;
import nc.integration.jei.*;
import nc.integration.jei.JEIHelper.*;
import nc.integration.jei.NCJEI.IJEIHandler;
import nc.recipe.IngredientSorption;
import nc.util.Lang;

public class CoolantHeaterCategory extends JEIMachineCategory<JEIRecipe.CoolantHeater> {
	
	public CoolantHeaterCategory(IGuiHelper guiHelper, IJEIHandler<JEIRecipe.CoolantHeater> handler) {
		super(guiHelper, handler, "salt_fission_heater", 45, 30, 102, 26);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipe.CoolantHeater recipeWrapper, IIngredients ingredients) {
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);
		
		RecipeItemMapper itemMapper = new RecipeItemMapper();
		RecipeFluidMapper fluidMapper = new RecipeFluidMapper();
		itemMapper.map(IngredientSorption.INPUT, 0, 0, 46 - backPosX, 35 - backPosY);
		fluidMapper.map(IngredientSorption.INPUT, 0, 0, 66 - backPosX, 35 - backPosY, 16, 16);
		fluidMapper.map(IngredientSorption.OUTPUT, 0, 1, 122 - backPosX, 31 - backPosY, 24, 24);
		itemMapper.mapItemsTo(recipeLayout.getItemStacks(), ingredients);
		fluidMapper.mapFluidsTo(recipeLayout.getFluidStacks(), ingredients);
	}
	
	@Override
	public String getTitle() {
		return Lang.localise(Global.MOD_ID + ".multiblock_gui.coolant_heater.jei_name");
	}
}
