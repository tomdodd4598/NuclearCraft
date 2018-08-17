package nc.integration.jei;

import java.util.List;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import nc.Global;
import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.util.RecipeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public abstract class JEIProcessorRecipeWrapper<T extends JEIProcessorRecipeWrapper> implements IRecipeWrapper {

	protected final ProcessorRecipeHandler recipeHandler;
	protected final ProcessorRecipe recipe;
	
	protected final List<List<ItemStack>> itemInputs;
	protected final List<List<FluidStack>> fluidInputs;
	
	/*protected final List<ItemStack> itemOutputs;
	protected final List<FluidStack> fluidOutputs;*/
	
	protected List<List<ItemStack>> itemOutputs;
	protected List<List<FluidStack>> fluidOutputs;
	
	protected final IDrawableAnimated arrow;
	protected final int arrowDrawPosX, arrowDrawPosY;
	
	public JEIProcessorRecipeWrapper(IGuiHelper guiHelper, IJEIHandler handler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe, int backX, int backY, int arrowX, int arrowY, int arrowWidth, int arrowHeight, int arrowPosX, int arrowPosY) {
		this(guiHelper, handler, recipeHandler, recipe, "", backX, backY, arrowX, arrowY, arrowWidth, arrowHeight, arrowPosX, arrowPosY);
	}
	
	public JEIProcessorRecipeWrapper(IGuiHelper guiHelper, IJEIHandler handler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe, String guiExtra, int backX, int backY, int arrowX, int arrowY, int arrowWidth, int arrowHeight, int arrowPosX, int arrowPosY) {
		this.recipeHandler = recipeHandler;
		this.recipe = recipe;
		
		itemInputs = RecipeHelper.getItemInputLists(recipe.itemIngredients());
		fluidInputs = RecipeHelper.getFluidInputLists(recipe.fluidIngredients());
		
		/*itemOutputs = RecipeHelper.getItemOutputList(recipe.itemProducts());
		fluidOutputs = RecipeHelper.getFluidOutputList(recipe.fluidProducts());*/
		
		itemOutputs = RecipeHelper.getItemOutputLists(recipe.itemProducts());
		fluidOutputs = RecipeHelper.getFluidOutputLists(recipe.fluidProducts());
		
		ResourceLocation location = new ResourceLocation(Global.MOD_ID + ":textures/gui/container/" + handler.getTextureName() + guiExtra + ".png");
		IDrawableStatic arrowDrawable = guiHelper.createDrawable(location, arrowX, arrowY, arrowWidth, arrowHeight);
		arrow = guiHelper.createAnimatedDrawable(arrowDrawable, Math.max(1, (int)(getProcessTime()/6D)), IDrawableAnimated.StartDirection.LEFT, false);
		arrowDrawPosX = arrowPosX - backX;
		arrowDrawPosY = arrowPosY - backY;
	}
	
	protected abstract double getProcessTime();

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(ItemStack.class, itemInputs);
		ingredients.setInputLists(FluidStack.class, fluidInputs);
		/*ingredients.setOutputs(ItemStack.class, itemOutputs);
		ingredients.setOutputs(FluidStack.class, fluidOutputs);*/
		ingredients.setOutputLists(ItemStack.class, itemOutputs);
		ingredients.setOutputLists(FluidStack.class, fluidOutputs);
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		arrow.draw(minecraft, arrowDrawPosX, arrowDrawPosY);
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
