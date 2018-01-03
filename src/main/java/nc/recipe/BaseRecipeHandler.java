package nc.recipe;

import java.util.ArrayList;

public abstract class BaseRecipeHandler<T extends IRecipe> extends RecipeMethods<T> {
	
	public int inputSizeItem, inputSizeFluid, outputSizeItem, outputSizeFluid;
	public boolean shapeless;
	
	public BaseRecipeHandler(int inputSizeItem, int inputSizeFluid, int outputSizeItem, int outputSizeFluid, boolean shapeless) {
		this.inputSizeItem = inputSizeItem;
		this.inputSizeFluid = inputSizeFluid;
		this.outputSizeItem = outputSizeItem;
		this.outputSizeFluid = outputSizeFluid;
		this.shapeless = shapeless;
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
}
