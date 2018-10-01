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
import net.minecraft.util.text.TextFormatting;

public class HeatExchangerCategory extends JEICategoryAbstract<JEIRecipeWrapper.HeatExchanger> {
	
	public HeatExchangerCategory(IGuiHelper guiHelper, IJEIHandler handler) {
		super(guiHelper, handler, "heat_exchanger", 47, 30, 90, 26);
		recipeTitle = Lang.localise(Global.MOD_ID + ".multiblock_gui.heat_exchanger.jei_name");
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipeWrapper.HeatExchanger recipeWrapper, IIngredients ingredients) {
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);
		
		RecipeFluidMapper fluidMapper = new RecipeFluidMapper();
		fluidMapper.map(IngredientSorption.INPUT, 0, 0, 56 - backPosX, 35 - backPosY, 16, 16);
		fluidMapper.map(IngredientSorption.OUTPUT, 0, 1, 112 - backPosX, 31 - backPosY, 24, 24);
		fluidMapper.mapFluidsTo(recipeLayout.getFluidStacks(), ingredients);
		
		recipeLayout.getFluidStacks().addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
			if (ingredient != null && ingredient.getFluid() != null) {
				if (slotIndex == 0) {
					tooltip.add((recipeWrapper.recipe.getHeatExchangerIsHeating() ? TextFormatting.AQUA : TextFormatting.RED) + TEMPERATURE + " " + ingredient.getFluid().getTemperature() + "K");
					tooltip.add(TextFormatting.WHITE + HEAT_REQUIRED + " " + (int)recipeWrapper.recipe.getHeatExchangerRecipeHeat(16000) + "H");
				}
				else if (slotIndex == 1) {
					tooltip.add((recipeWrapper.recipe.getHeatExchangerIsHeating() ? TextFormatting.RED : TextFormatting.AQUA) + TEMPERATURE + " " + ingredient.getFluid().getTemperature() + "K");
					tooltip.add(TextFormatting.WHITE + HEAT_REQUIRED + " " + (int)recipeWrapper.recipe.getHeatExchangerRecipeHeat(16000) + "H");
				}
			}
		});
	}
	
	private static final String TEMPERATURE = Lang.localise("jei.nuclearcraft.fluid_temp");
	private static final String HEAT_REQUIRED = Lang.localise("jei.nuclearcraft.heat_req");
	
	@Override
	public String getTitle() {
		return recipeTitle;
	}
}
