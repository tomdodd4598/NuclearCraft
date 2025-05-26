package nc.integration.jei.wrapper;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import nc.recipe.*;
import nc.recipe.ingredient.*;
import nc.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

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
		itemOutputs = StreamHelper.map(RecipeHelper.getItemOutputLists(recipe.getItemProducts()), x -> StreamHelper.filter(x, y -> y != null && !y.isEmpty()));
		fluidOutputs = StreamHelper.map(RecipeHelper.getFluidOutputLists(recipe.getFluidProducts()), x -> StreamHelper.filter(x, y -> y != null && y.amount > 0));
		
		if (arrowW > 0 && arrowH > 0) {
			arrow = new Lazy<>(() -> {
				int[] progressUVWH = getProgressArrowUVWH(arrowU, arrowV, arrowW, arrowH);
				IDrawableStatic arrowDrawable = guiHelper.createDrawable(new ResourceLocation(textureLocation), progressUVWH[0], progressUVWH[1], progressUVWH[2], progressUVWH[3]);
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
	
	protected int[] getProgressArrowUVWH(int arrowU, int arrowV, int arrowW, int arrowH) {
		return new int[] {arrowU, arrowV, arrowW, arrowH};
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
	
	protected <T> T getEnumerationElement(List<T> list, long millisPerElement) {
		return list.get((int) ((System.currentTimeMillis() / millisPerElement) % list.size()));
	}
	
	public void addItemIngredientTooltip(List<String> tooltip, IItemIngredient ingredient) {
	
	}
	
	public void addFluidIngredientTooltip(List<String> tooltip, IFluidIngredient ingredient) {
	
	}
	
	public void addItemProductTooltip(List<String> tooltip, IItemIngredient product) {
		if (product instanceof IChanceItemIngredient chanceProduct) {
			tooltip.add(TextFormatting.WHITE + Lang.localize("jei.nuclearcraft.chance_output", chanceProduct.getMinStackSize(), chanceProduct.getMaxStackSize(0), NCMath.decimalPlaces(chanceProduct.getMeanStackSize(), 2)));
		}
	}
	
	public void addFluidProductTooltip(List<String> tooltip, IFluidIngredient product) {
		if (product instanceof IChanceFluidIngredient chanceProduct) {
			tooltip.add(TextFormatting.WHITE + Lang.localize("jei.nuclearcraft.chance_output", chanceProduct.getMinStackSize(), chanceProduct.getMaxStackSize(0), NCMath.decimalPlaces(chanceProduct.getMeanStackSize(), 2)));
		}
	}
}
