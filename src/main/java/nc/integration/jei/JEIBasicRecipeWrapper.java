package nc.integration.jei;

import java.util.List;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import nc.Global;
import nc.integration.jei.NCJEI.IJEIHandler;
import nc.recipe.*;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public abstract class JEIBasicRecipeWrapper implements IRecipeWrapper {
	
	public final BasicRecipeHandler recipeHandler;
	public final BasicRecipe recipe;
	
	public final List<List<ItemStack>> itemInputs;
	public final List<List<FluidStack>> fluidInputs;
	public final List<List<ItemStack>> itemOutputs;
	public final List<List<FluidStack>> fluidOutputs;
	
	public final boolean drawArrow;
	public final IDrawable arrow;
	public final int arrowDrawPosX, arrowDrawPosY;
	
	public JEIBasicRecipeWrapper(IGuiHelper guiHelper, IJEIHandler handler, BasicRecipeHandler recipeHandler, BasicRecipe recipe, int backX, int backY, int arrowX, int arrowY, int arrowWidth, int arrowHeight, int arrowPosX, int arrowPosY) {
		this.recipeHandler = recipeHandler;
		this.recipe = recipe;
		
		itemInputs = RecipeHelper.getItemInputLists(recipe.getItemIngredients());
		fluidInputs = RecipeHelper.getFluidInputLists(recipe.getFluidIngredients());
		itemOutputs = RecipeHelper.getItemOutputLists(recipe.getItemProducts());
		fluidOutputs = RecipeHelper.getFluidOutputLists(recipe.getFluidProducts());
		
		this.drawArrow = arrowWidth > 0 && arrowHeight > 0;
		ResourceLocation location = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + handler.getTextureName() + ".png");
		IDrawableStatic arrowDrawable = guiHelper.createDrawable(location, arrowX, arrowY, Math.max(arrowWidth, 1), Math.max(arrowHeight, 1));
		arrow = staticArrow() ? arrowDrawable : guiHelper.createAnimatedDrawable(arrowDrawable, getProgressArrowTime(), IDrawableAnimated.StartDirection.LEFT, false);
		arrowDrawPosX = arrowPosX - backX;
		arrowDrawPosY = arrowPosY - backY;
	}
	
	protected abstract int getProgressArrowTime();
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(ItemStack.class, itemInputs);
		ingredients.setInputLists(FluidStack.class, fluidInputs);
		ingredients.setOutputLists(ItemStack.class, itemOutputs);
		ingredients.setOutputLists(FluidStack.class, fluidOutputs);
	}
	
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		if (drawArrow) {
			arrow.draw(minecraft, arrowDrawPosX, arrowDrawPosY);
		}
	}
	
	protected boolean staticArrow() {
		return getProgressArrowTime() < 2 /* || (factor_recipes && itemInputs.isEmpty() && itemOutputs.isEmpty() && getProgressArrowTime() < 4) */;
	}
	
	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		return null;
	}
	
	@Override
	public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
		return false;
	}
}
