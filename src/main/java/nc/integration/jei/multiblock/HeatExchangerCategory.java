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
				boolean heating = recipeWrapper.recipe.getHeatExchangerIsHeating();
				int inputTemp = recipeWrapper.recipe.getHeatExchangerInputTemperature();
				int outputTemp = recipeWrapper.recipe.getHeatExchangerOutputTemperature();
				int rate = Math.abs(inputTemp - outputTemp);
				int processTime = (int)recipeWrapper.recipe.getHeatExchangerProcessTime(16000);
				
				if (slotIndex == 0) {
					tooltip.add((heating ? TextFormatting.AQUA : TextFormatting.RED) + TEMPERATURE + TextFormatting.WHITE + " " + inputTemp + "K");
					tooltip.add((heating ? TextFormatting.AQUA + COOLING_PROVIDED : TextFormatting.RED + HEATING_PROVIDED) + TextFormatting.WHITE + " " + rate + "/t");
					tooltip.add((heating ? TextFormatting.RED + HEATING_REQUIRED : TextFormatting.AQUA + COOLING_REQUIRED) + TextFormatting.WHITE + " " + processTime);
				}
				else if (slotIndex == 1) {
					tooltip.add((heating ? TextFormatting.RED : TextFormatting.AQUA) + TEMPERATURE + TextFormatting.WHITE + " " + outputTemp + "K");
					tooltip.add((heating ? TextFormatting.AQUA + COOLING_PROVIDED : TextFormatting.RED + HEATING_PROVIDED) + TextFormatting.WHITE + " " + rate + "/t");
					tooltip.add((heating ? TextFormatting.RED + HEATING_REQUIRED : TextFormatting.AQUA + COOLING_REQUIRED) + TextFormatting.WHITE + " " + processTime);
				}
			}
		});
	}
	
	private static final String TEMPERATURE = Lang.localise("jei.nuclearcraft.exchanger_fluid_temp");
	private static final String HEATING_PROVIDED = Lang.localise("jei.nuclearcraft.exchanger_heating_provided");
	private static final String COOLING_PROVIDED = Lang.localise("jei.nuclearcraft.exchanger_cooling_provided");
	private static final String HEATING_REQUIRED = Lang.localise("jei.nuclearcraft.exchanger_heating_required");
	private static final String COOLING_REQUIRED = Lang.localise("jei.nuclearcraft.exchanger_cooling_required");
	
	@Override
	public String getTitle() {
		return recipeTitle;
	}
}
