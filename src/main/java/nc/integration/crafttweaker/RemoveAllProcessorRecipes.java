package nc.integration.crafttweaker;

import crafttweaker.IAction;
import nc.recipe.NCRecipes;

public class RemoveAllProcessorRecipes implements IAction {
	
	public static boolean hasErrored = false;
	public final NCRecipes.Type recipeType;
	
	public RemoveAllProcessorRecipes(NCRecipes.Type recipeType) {
		this.recipeType = recipeType;
	}
	
	@Override
	public void apply() {
		recipeType.getRecipeHandler().removeAllRecipes();
	}
	
	@Override
	public String describe() {
		return String.format("Removing all %s recipes", recipeType.getRecipeName());
	}
}
