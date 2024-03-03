package nc.integration.jei.wrapper;

import java.util.List;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import nc.recipe.*;
import nc.util.Lazy;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public abstract class JEIRecipeWrapper implements IRecipeWrapper {
	
	public final BasicRecipeHandler recipeHandler;
	public final BasicRecipe recipe;
	
	protected final List<List<ItemStack>> itemInputs;
	protected final List<List<FluidStack>> fluidInputs;
	protected final List<List<ItemStack>> itemOutputs;
	protected final List<List<FluidStack>> fluidOutputs;
	
	protected final Lazy<IDrawable> arrow;
	protected final int arrowX, arrowY;
	
	protected JEIRecipeWrapper(IGuiHelper guiHelper, BasicRecipeHandler recipeHandler, BasicRecipe recipe, String textureLocation, int backgroundX, int backgroundY, int arrowX, int arrowY, int arrowW, int arrowH, int arrowU, int arrowV) {
		this.recipeHandler = recipeHandler;
		this.recipe = recipe;
		
		itemInputs = RecipeHelper.getItemInputLists(recipe.getItemIngredients());
		fluidInputs = RecipeHelper.getFluidInputLists(recipe.getFluidIngredients());
		itemOutputs = RecipeHelper.getItemOutputLists(recipe.getItemProducts());
		fluidOutputs = RecipeHelper.getFluidOutputLists(recipe.getFluidProducts());
		
		if (arrowW > 0 && arrowH > 0) {
			arrow = new Lazy<>(() -> {
				IDrawableStatic arrowDrawable = guiHelper.createDrawable(new ResourceLocation(textureLocation), arrowU, arrowV, arrowW, arrowH);
				int progressTime = getProgressArrowTime();
				return progressTime < 2 ? arrowDrawable : guiHelper.createAnimatedDrawable(arrowDrawable, progressTime, IDrawableAnimated.StartDirection.LEFT, false);
			});
		}
		else {
			arrow = null;
		}
		this.arrowX = arrowX - backgroundX;
		this.arrowY = arrowY - backgroundY;
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
		if (arrow != null) {
			arrow.get().draw(minecraft, arrowX, arrowY);
		}
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
