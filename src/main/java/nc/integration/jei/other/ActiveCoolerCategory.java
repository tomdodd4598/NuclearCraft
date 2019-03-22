package nc.integration.jei.other;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import nc.integration.jei.IJEIHandler;
import nc.integration.jei.JEICategoryAbstract;
import nc.integration.jei.JEIMethods.RecipeFluidMapper;
import nc.integration.jei.JEIRecipeWrapper;
import nc.recipe.IngredientSorption;
import nc.util.Lang;
import nc.util.UnitHelper;
import net.minecraft.util.text.TextFormatting;

public class ActiveCoolerCategory extends JEICategoryAbstract<JEIRecipeWrapper.ActiveCooler> {
	
	public ActiveCoolerCategory(IGuiHelper guiHelper, IJEIHandler handler) {
		super(guiHelper, handler, "active_cooler", 47, 30, 90, 26);
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JEIRecipeWrapper.ActiveCooler recipeWrapper, IIngredients ingredients) {
		super.setRecipe(recipeLayout, recipeWrapper, ingredients);
		
		RecipeFluidMapper fluidMapper = new RecipeFluidMapper();
		fluidMapper.map(IngredientSorption.INPUT, 0, 0, 86 - backPosX, 35 - backPosY, 16, 16);
		fluidMapper.mapFluidsTo(recipeLayout.getFluidStacks(), ingredients);
		
		recipeLayout.getFluidStacks().addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
			if (slotIndex == 0) {
				tooltip.add(TextFormatting.AQUA + FISSION_COOLING + TextFormatting.WHITE + " " + UnitHelper.prefix(recipeWrapper.recipe.getActiveFissionCoolingRate(), 5, "H/t"));
				tooltip.add(TextFormatting.AQUA + FUSION_COOLING + TextFormatting.WHITE + " " + UnitHelper.prefix(recipeWrapper.recipe.getActiveFusionCoolingRate(), 5, "K/t"));
			}
		});
	}
	
	private static final String FISSION_COOLING = Lang.localise("jei.nuclearcraft.active_fission_cooling");
	private static final String FUSION_COOLING = Lang.localise("jei.nuclearcraft.active_fusion_cooling");
}
