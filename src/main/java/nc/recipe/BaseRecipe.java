package nc.recipe;

import java.util.ArrayList;

public class BaseRecipe implements IRecipe {
	
	public ArrayList<IIngredient> recipeInputList;
	public ArrayList<IIngredient> recipeOutputList;
	public ArrayList recipeExtrasList;
	public boolean isShapeless;
	
	public BaseRecipe(ArrayList<IIngredient> inputList, ArrayList<IIngredient> outputList, ArrayList extrasList, boolean shapeless) {
		recipeInputList = inputList;
		recipeOutputList = outputList;
		recipeExtrasList = extrasList;
		isShapeless = shapeless;
	}
	
	@Override
	public ArrayList<IIngredient> inputs() {
		return recipeInputList;
	}

	@Override
	public ArrayList<IIngredient> outputs() {
		return recipeOutputList;
	}
	
	@Override
	public ArrayList extras() {
		return recipeExtrasList;
	}

	@Override
	public boolean matchingInputs(Object[] inputs) {
		return RecipeMethods.matchingIngredients(SorptionType.INPUT, recipeInputList, isShapeless, inputs);
	}

	@Override
	public boolean matchingOutputs(Object[] outputs) {
		return RecipeMethods.matchingIngredients(SorptionType.OUTPUT, recipeOutputList, isShapeless, outputs);
	}
}
