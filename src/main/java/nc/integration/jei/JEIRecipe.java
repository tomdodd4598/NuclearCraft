package nc.integration.jei;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import nc.recipe.IRecipe;
import nc.recipe.BaseRecipeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public abstract class JEIRecipe<T extends JEIRecipe> implements IRecipeWrapper {

	public BaseRecipeHandler recipeHandler;
	public IRecipe recipe;
	
	public List<List<Object>> inputs;
	public List<List<ItemStack>> itemInputs;
	public List<List<FluidStack>> fluidInputs;
	
	public List<Object> outputs;
	public List<ItemStack> itemOutputs;
	public List<FluidStack> fluidOutputs;
	
	public int inputSizeItem, inputSizeFluid;
	public int outputSizeItem, outputSizeFluid;
	
	public JEIRecipe(BaseRecipeHandler recipeHandler, IRecipe recipe) {
		this.recipeHandler = recipeHandler;
		this.recipe = recipe;
		
		inputs = recipeHandler.getIngredientLists(recipe.inputs());
		outputs = recipeHandler.getIngredientList(recipe.outputs());
		
		inputSizeItem = recipeHandler.inputSizeItem;
		inputSizeFluid = recipeHandler.inputSizeFluid;
		outputSizeItem = recipeHandler.outputSizeItem;
		outputSizeFluid = recipeHandler.outputSizeFluid;
		
		List<List<ItemStack>> itemInputLists = new ArrayList<List<ItemStack>>();
		for(int i = 0; i < inputSizeItem; i++) {
			List<ItemStack> inputList = new ArrayList<ItemStack>();
			for(Object input : inputs.get(i)) {
				if (input instanceof ItemStack) inputList.add((ItemStack) input);
			}
			itemInputLists.add(inputList);
		}
		itemInputs = itemInputLists;
		
		List<List<FluidStack>> fluidInputLists = new ArrayList<List<FluidStack>>();
		for(int i = inputSizeItem; i < inputSizeItem + inputSizeFluid; i++) {
			List<FluidStack> inputList = new ArrayList<FluidStack>();
			for(Object input : inputs.get(i)) {
				if (input instanceof FluidStack) inputList.add((FluidStack) input);
			}
			fluidInputLists.add(inputList);
		}
		fluidInputs = fluidInputLists;
		
		List<ItemStack> itemOutputList = new ArrayList<ItemStack>();
		for(Object output : outputs) {
			if (output instanceof ItemStack) itemOutputList.add((ItemStack) output);
		}
		itemOutputs = itemOutputList;
		
		List<FluidStack> fluidOutputList = new ArrayList<FluidStack>();
		for(Object output : outputs) {
			if (output instanceof FluidStack) fluidOutputList.add((FluidStack) output);
		}
		fluidOutputs = fluidOutputList;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(ItemStack.class, itemInputs);
		ingredients.setInputLists(FluidStack.class, fluidInputs);
		ingredients.setOutputs(ItemStack.class, itemOutputs);
		ingredients.setOutputs(FluidStack.class, fluidOutputs);
	}

	public List getInputs() {
		List<ItemStack> inputs = new ArrayList<ItemStack>();
		for (List<ItemStack> input : itemInputs) inputs.add(input.get(0));
		return inputs;
	}

	public List getOutputs() {
		return itemOutputs;
	}

	public List<FluidStack> getFluidInputs() {
		List<FluidStack> inputs = new ArrayList<FluidStack>();
		for (List<FluidStack> input : fluidInputs) inputs.add(input.get(0));
		return inputs;
	}

	public List<FluidStack> getFluidOutputs() {
		return fluidOutputs;
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {}

	public void drawAnimations(Minecraft minecraft, int recipeWidth, int recipeHeight) {}

	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		return null;
	}

	@Override
	public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
		return false;
	}

}
