package nc.integration.jei.generator;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import nc.config.NCConfig;
import nc.integration.jei.IJEIHandler;
import nc.integration.jei.JEICategoryAbstract;
import nc.integration.jei.JEIMethods.RecipeItemMapper;
import nc.integration.jei.JEIRecipeWrapper;
import nc.recipe.IngredientSorption;
import nc.util.Lang;
import nc.util.NCMath;
import nc.util.UnitHelper;
import net.minecraft.util.text.TextFormatting;

public class FissionCategory extends JEICategoryAbstract<JEIRecipeWrapper.Fission> {
	
	public FissionCategory(IGuiHelper guiHelper, IJEIHandler handler) {
		super(guiHelper, handler, "fission_controller_idle", "_jei", 47, 30, 90, 26);
		recipeTitle = Lang.localise("gui.container.fission_controller.reactor");
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipeWrapper.Fission recipeWrapper, IIngredients ingredients) {
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);
		
		RecipeItemMapper itemMapper = new RecipeItemMapper();
		itemMapper.map(IngredientSorption.INPUT, 0, 0, 56 - backPosX, 35 - backPosY);
		itemMapper.map(IngredientSorption.OUTPUT, 0, 1, 116 - backPosX, 35 - backPosY);
		itemMapper.mapItemsTo(recipeLayout.getItemStacks(), ingredients);
		
		recipeLayout.getItemStacks().addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
			tooltip.add(TextFormatting.GREEN + FUEL_TIME + " " + TextFormatting.WHITE + UnitHelper.applyTimeUnitShort(NCMath.round(recipeWrapper.recipe.getFissionFuelTime()/NCConfig.fission_fuel_use), 2));
			tooltip.add(TextFormatting.LIGHT_PURPLE + FUEL_POWER + " " + TextFormatting.WHITE + UnitHelper.prefix(recipeWrapper.recipe.getFissionFuelPower()*NCConfig.fission_heat_generation, 5, "RF/t"));
			tooltip.add(TextFormatting.YELLOW + FUEL_HEAT + " " + TextFormatting.WHITE + UnitHelper.prefix(recipeWrapper.recipe.getFissionFuelHeat()*NCConfig.fission_heat_generation, 5, "H/t"));
		});
	}
	
	private static final String FUEL_TIME = Lang.localise("jei.nuclearcraft.solid_fuel_time");
	private static final String FUEL_POWER = Lang.localise("jei.nuclearcraft.solid_fuel_power");
	private static final String FUEL_HEAT = Lang.localise("jei.nuclearcraft.solid_fuel_heat");
	
	@Override
	public String getTitle() {
		return recipeTitle;
	}
}
