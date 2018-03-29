package nc.recipe;

import java.util.ArrayList;

import nc.Global;

public abstract class BaseRecipeHandler<T extends IRecipe> extends RecipeMethods<T> {
	
	public int inputSizeItem, inputSizeFluid, outputSizeItem, outputSizeFluid;
	public final boolean shapeless;
	private final String recipeName;
	
	public BaseRecipeHandler(String recipeName, int inputSizeItem, int inputSizeFluid, int outputSizeItem, int outputSizeFluid) {
		this(recipeName, inputSizeItem, inputSizeFluid, outputSizeItem, outputSizeFluid, true);
	}
	
	public BaseRecipeHandler(String recipeName, int inputSizeItem, int inputSizeFluid, int outputSizeItem, int outputSizeFluid, boolean shapeless) {
		this.inputSizeItem = inputSizeItem;
		this.inputSizeFluid = inputSizeFluid;
		this.outputSizeItem = outputSizeItem;
		this.outputSizeFluid = outputSizeFluid;
		this.shapeless = shapeless;
		this.recipeName = recipeName;
		addRecipes();
	}
	
	public void addRecipe(Object... objects) {
		ArrayList inputs = new ArrayList();
		ArrayList outputs = new ArrayList();
		ArrayList additionals = new ArrayList();
		for (int i = 0; i < objects.length; i++) {
			Object object = objects[i];
			if (i < inputSizeItem + inputSizeFluid) {
				inputs.add(object);
			} else if (i < inputSizeItem + inputSizeFluid + outputSizeItem + outputSizeFluid) {
				outputs.add(object);
			} else {
				additionals.add(object);
			}
		}
		addRecipe(buildDefaultRecipe(inputs, outputs, additionals, shapeless));
	}
	
	@Override
	public boolean isValidRecipe(ArrayList<IIngredient> recipeInputList, ArrayList<IIngredient> recipeOutputList) {
		return recipeInputList.size() == inputSizeItem + inputSizeFluid && recipeOutputList.size() == outputSizeItem + outputSizeFluid;
	}
	
	@Override
	public String getRecipeName() {
		return Global.MOD_ID + "_" + recipeName;
	}
}
