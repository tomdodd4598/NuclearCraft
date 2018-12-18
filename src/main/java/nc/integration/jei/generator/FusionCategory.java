package nc.integration.jei.generator;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import nc.config.NCConfig;
import nc.integration.jei.IJEIHandler;
import nc.integration.jei.JEICategoryAbstract;
import nc.integration.jei.JEIMethods.RecipeFluidMapper;
import nc.integration.jei.JEIRecipeWrapper;
import nc.recipe.IngredientSorption;
import nc.util.Lang;
import nc.util.NCMath;
import nc.util.UnitHelper;
import net.minecraft.util.text.TextFormatting;

public class FusionCategory extends JEICategoryAbstract<JEIRecipeWrapper.Fusion> {
	
	public FusionCategory(IGuiHelper guiHelper, IJEIHandler handler) {
		super(guiHelper, handler, "fusion_core", "_jei", 55, 30, 94, 26);
		recipeTitle = Lang.localise("gui.container.fusion_core.reactor");
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipeWrapper.Fusion recipeWrapper, IIngredients ingredients) {
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);
		
		RecipeFluidMapper fluidMapper = new RecipeFluidMapper();
		fluidMapper.map(IngredientSorption.INPUT, 0, 0, 56 - backPosX, 31 - backPosY, 6, 24);
		fluidMapper.map(IngredientSorption.INPUT, 1, 1, 66 - backPosX, 31 - backPosY, 6, 24);
		fluidMapper.map(IngredientSorption.OUTPUT, 0, 2, 112 - backPosX, 31 - backPosY, 6, 24);
		fluidMapper.map(IngredientSorption.OUTPUT, 1, 3, 122 - backPosX, 31 - backPosY, 6, 24);
		fluidMapper.map(IngredientSorption.OUTPUT, 2, 4, 132 - backPosX, 31 - backPosY, 6, 24);
		fluidMapper.map(IngredientSorption.OUTPUT, 3, 5, 142 - backPosX, 31 - backPosY, 6, 24);
		fluidMapper.mapFluidsTo(recipeLayout.getFluidStacks(), ingredients);
		
		recipeLayout.getFluidStacks().addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
			tooltip.add(TextFormatting.GREEN + COMBO_TIME + " " + TextFormatting.WHITE + UnitHelper.applyTimeUnitShort(NCMath.round(recipeWrapper.recipe.getFusionComboTime()/NCConfig.fusion_fuel_use, 2), 2));
			tooltip.add(TextFormatting.LIGHT_PURPLE + COMBO_POWER + " " + TextFormatting.WHITE + UnitHelper.prefix(100D*recipeWrapper.recipe.getFusionComboPower()*NCConfig.fusion_base_power, 5, "RF/t"));
			tooltip.add(TextFormatting.YELLOW + COMBO_TEMP + " " + TextFormatting.WHITE + UnitHelper.prefix(NCMath.round(R*recipeWrapper.recipe.getFusionComboHeatVariable(), 2), 5, "K", 2));
		});
	}
	
	private static final double R = 1.21875567483D;
	
	private static final String COMBO_TIME = Lang.localise("jei.nuclearcraft.fusion_time");
	private static final String COMBO_POWER = Lang.localise("jei.nuclearcraft.fusion_power");
	private static final String COMBO_TEMP = Lang.localise("jei.nuclearcraft.fusion_temp");
	
	@Override
	public String getTitle() {
		return recipeTitle;
	}
}
