package nc.integration.jei.multiblock;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import nc.Global;
import nc.integration.jei.IJEIHandler;
import nc.integration.jei.JEICategoryAbstract;
import nc.integration.jei.JEIMethods.RecipeFluidMapper;
import nc.integration.jei.JEIRecipeWrapper;
import nc.recipe.IngredientSorption;
import nc.util.Lang;
import nc.util.UnitHelper;
import net.minecraft.util.text.TextFormatting;

public class CoolantHeaterCategory extends JEICategoryAbstract<JEIRecipeWrapper.CoolantHeater> {
	
	public CoolantHeaterCategory(IGuiHelper guiHelper, IJEIHandler handler) {
		super(guiHelper, handler, "salt_fission_heater", 47, 30, 90, 26);
		recipeTitle = Lang.localise(Global.MOD_ID + ".multiblock_gui.coolant_heater.jei_name");
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipeWrapper.CoolantHeater recipeWrapper, IIngredients ingredients) {
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);
		
		RecipeFluidMapper fluidMapper = new RecipeFluidMapper();
		fluidMapper.map(IngredientSorption.INPUT, 0, 0, 56 - backPosX, 35 - backPosY, 16, 16);
		fluidMapper.map(IngredientSorption.OUTPUT, 0, 1, 112 - backPosX, 31 - backPosY, 24, 24);
		fluidMapper.mapFluidsTo(recipeLayout.getFluidStacks(), ingredients);
		
		recipeLayout.getFluidStacks().addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
			tooltip.add(TextFormatting.BLUE + COOLING + " " + TextFormatting.WHITE + UnitHelper.prefix(recipeWrapper.recipe.getCoolantHeaterCoolingRate(), 5, "H/t"));
			if (recipeWrapper.recipe.getCoolantHeaterJEIInfo() != null) {
				for (String posInfo : recipeWrapper.recipe.getCoolantHeaterJEIInfo()) tooltip.add(TextFormatting.AQUA + posInfo);
			}
		});
	}
	
	private static final String COOLING = Lang.localise("jei.nuclearcraft.coolant_heater_rate");
}
