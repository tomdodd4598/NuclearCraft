package nc.integration.crafttweaker;

import crafttweaker.IAction;
import nc.recipe.ProcessorRecipeHandler;

public class RemoveAllProcessorRecipes implements IAction {
	
	protected final ProcessorRecipeHandler recipeHandler;
	
	public RemoveAllProcessorRecipes(ProcessorRecipeHandler recipeHandler) {
		this.recipeHandler = recipeHandler;
	}
	
	@Override
	public void apply() {
		recipeHandler.removeAllRecipes();
	}
	
	@Override
	public String describe() {
		return String.format("Removed all %s recipes", recipeHandler.getRecipeName());
	}
}
