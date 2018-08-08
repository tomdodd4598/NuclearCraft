package nc.integration.jei;

import java.util.List;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import nc.recipe.IRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.util.RecipeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public abstract class JEIProcessorRecipe<T extends JEIProcessorRecipe> implements IRecipeWrapper {

	public ProcessorRecipeHandler recipeHandler;
	public IRecipe recipe;
	
	public List<List<ItemStack>> itemInputs;
	public List<List<FluidStack>> fluidInputs;
	
	/*public List<ItemStack> itemOutputs;
	public List<FluidStack> fluidOutputs;*/
	
	public List<List<ItemStack>> itemOutputs;
	public List<List<FluidStack>> fluidOutputs;
	
	public JEIProcessorRecipe(ProcessorRecipeHandler recipeHandler, IRecipe recipe) {
		this.recipeHandler = recipeHandler;
		this.recipe = recipe;
		
		itemInputs = RecipeHelper.getItemInputLists(recipe.itemIngredients());
		fluidInputs = RecipeHelper.getFluidInputLists(recipe.fluidIngredients());
		
		/*itemOutputs = RecipeHelper.getItemOutputList(recipe.itemProducts());
		fluidOutputs = RecipeHelper.getFluidOutputList(recipe.fluidProducts());*/
		
		itemOutputs = RecipeHelper.getItemOutputLists(recipe.itemProducts());
		fluidOutputs = RecipeHelper.getFluidOutputLists(recipe.fluidProducts());
	}

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
		
	}

	public void drawAnimations(Minecraft minecraft, int recipeWidth, int recipeHeight) {
		
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
