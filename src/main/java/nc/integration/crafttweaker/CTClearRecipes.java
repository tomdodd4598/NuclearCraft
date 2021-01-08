package nc.integration.crafttweaker;

import crafttweaker.IAction;
import nc.recipe.BasicRecipeHandler;

public class CTClearRecipes implements IAction {
	
	protected final BasicRecipeHandler recipeHandler;
	
	public CTClearRecipes(BasicRecipeHandler recipeHandler) {
		this.recipeHandler = recipeHandler;
	}
	
	@Override
	public void apply() {
		recipeHandler.removeAllRecipes();
	}
	
	@Override
	public String describe() {
		return String.format("Removed all %s recipes", recipeHandler.getName());
	}
}
