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
import net.minecraft.util.text.TextFormatting;

public class HeatExchangerCategory extends JEIMachineCategory<JEIRecipe.HeatExchanger> {
	
	public HeatExchangerCategory(IGuiHelper guiHelper, IJEIHandler<JEIRecipe.HeatExchanger> handler) {
		super(guiHelper, handler, "heat_exchanger_controller", 47, 30, 90, 26);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipe.HeatExchanger recipeWrapper, IIngredients ingredients) {
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);
		
		RecipeFluidMapper fluidMapper = new RecipeFluidMapper();
		fluidMapper.map(IngredientSorption.INPUT, 0, 0, 56 - backPosX, 35 - backPosY, 16, 16);
		fluidMapper.map(IngredientSorption.OUTPUT, 0, 1, 112 - backPosX, 31 - backPosY, 24, 24);
		fluidMapper.mapFluidsTo(recipeLayout.getFluidStacks(), ingredients);
		
		recipeLayout.getFluidStacks().addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
			boolean heating = recipeWrapper.recipe.getHeatExchangerIsHeating();
			
			if (slotIndex == 0) {
				tooltip.add((heating ? TextFormatting.AQUA : TextFormatting.RED) + TEMPERATURE + TextFormatting.WHITE + " " + recipeWrapper.recipe.getHeatExchangerInputTemperature() + "K");
			}
			else if (slotIndex == 1) {
				tooltip.add((heating ? TextFormatting.RED : TextFormatting.AQUA) + TEMPERATURE + TextFormatting.WHITE + " " + recipeWrapper.recipe.getHeatExchangerOutputTemperature() + "K");
			}
		});
	}
	
	private static final String TEMPERATURE = Lang.localise("jei.nuclearcraft.exchanger_fluid_temp");
	
	@Override
	public String getTitle() {
		return Lang.localise(Global.MOD_ID + ".multiblock_gui.heat_exchanger.jei_name");
	}
}
