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
import nc.recipe.ProcessorRecipe;
import nc.util.Lang;
import nc.util.UnitHelper;
import net.minecraft.util.text.TextFormatting;

public class TurbineCategory extends JEICategoryAbstract<JEIRecipeWrapper.Turbine> {
	
	public TurbineCategory(IGuiHelper guiHelper, IJEIHandler handler) {
		super(guiHelper, handler, "turbine", 47, 30, 90, 26);
		recipeTitle = Lang.localise(Global.MOD_ID + ".multiblock_gui.turbine.jei_name");
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipeWrapper.Turbine recipeWrapper, IIngredients ingredients) {
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);
		
		RecipeFluidMapper fluidMapper = new RecipeFluidMapper();
		fluidMapper.map(IngredientSorption.INPUT, 0, 0, 56 - backPosX, 35 - backPosY, 16, 16);
		fluidMapper.map(IngredientSorption.OUTPUT, 0, 1, 112 - backPosX, 31 - backPosY, 24, 24);
		fluidMapper.mapFluidsTo(recipeLayout.getFluidStacks(), ingredients);
		
		recipeLayout.getFluidStacks().addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
			tooltip.add(TextFormatting.LIGHT_PURPLE + ENERGY_DENSITY + " " + TextFormatting.WHITE + UnitHelper.prefix(recipeWrapper.recipe.getTurbinePowerPerMB(), 3, "RF/mB"));
			tooltip.add(TextFormatting.GRAY + EXPANSION + " " + TextFormatting.WHITE + Math.round(100D*expansion(recipeWrapper.recipe)) + "%");
		});
	}
	
	private double expansion(ProcessorRecipe recipe) {
		return (double)recipe.fluidProducts().get(0).getMaxStackSize()/(double)recipe.fluidIngredients().get(0).getMaxStackSize();
	}
	
	private static final String ENERGY_DENSITY = Lang.localise("jei.nuclearcraft.turbine_energy_density");
	private static final String EXPANSION = Lang.localise("jei.nuclearcraft.turbine_expansion");
}
