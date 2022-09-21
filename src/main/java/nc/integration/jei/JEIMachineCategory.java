package nc.integration.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import nc.Global;
import nc.integration.jei.NCJEI.IJEIHandler;
import nc.recipe.ingredient.*;
import nc.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public abstract class JEIMachineCategory<WRAPPER extends JEIBasicRecipe<WRAPPER>> extends JEIBasicCategory<WRAPPER> {
	
	protected final IDrawable background;
	protected final String title;
	protected final int backPosX, backPosY;
	
	public JEIMachineCategory(IGuiHelper guiHelper, IJEIHandler<WRAPPER> handler, String blockName, int backX, int backY, int backWidth, int backHeight) {
		super(handler);
		ResourceLocation location = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + handler.getTextureName() + ".png");
		background = guiHelper.createDrawable(location, backX, backY, backWidth, backHeight);
		title = Lang.localise("tile." + Global.MOD_ID + "." + blockName + ".name");
		backPosX = backX + 1;
		backPosY = backY + 1;
	}
	
	@Override
	public void drawExtras(Minecraft minecraft) {
		
	}
	
	@Override
	public IDrawable getBackground() {
		return background;
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, WRAPPER recipeWrapper, IIngredients ingredients) {
		recipeLayout.getItemStacks().addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
			int outputIndex = slotIndex - recipeWrapper.recipeHandler.getItemInputSize();
			if (outputIndex >= 0 && outputIndex <= recipeWrapper.recipeHandler.getItemOutputSize() && recipeWrapper.recipe.getItemProducts().get(outputIndex) instanceof IChanceItemIngredient) {
				IChanceItemIngredient chanceIngredient = (IChanceItemIngredient) recipeWrapper.recipe.getItemProducts().get(outputIndex);
				tooltip.add(TextFormatting.WHITE + Lang.localise("jei.nuclearcraft.chance_output", chanceIngredient.getMinStackSize(), chanceIngredient.getMaxStackSize(0), NCMath.decimalPlaces(chanceIngredient.getMeanStackSize(), 2)));
			}
		});
		
		recipeLayout.getFluidStacks().addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
			int outputIndex = slotIndex - recipeWrapper.recipeHandler.getFluidInputSize();
			if (outputIndex >= 0 && outputIndex <= recipeWrapper.recipeHandler.getFluidOutputSize() && recipeWrapper.recipe.getFluidProducts().get(outputIndex) instanceof IChanceFluidIngredient) {
				IChanceFluidIngredient chanceIngredient = (IChanceFluidIngredient) recipeWrapper.recipe.getFluidProducts().get(outputIndex);
				tooltip.add(TextFormatting.WHITE + Lang.localise("jei.nuclearcraft.chance_output", chanceIngredient.getMinStackSize(), chanceIngredient.getMaxStackSize(0), NCMath.decimalPlaces(chanceIngredient.getMeanStackSize(), 2)));
			}
		});
	}
	
	@Override
	public String getTitle() {
		return title;
	}
}
