package nc.recipe;

import it.unimi.dsi.fastutil.ints.IntList;

public class RecipeMatchResult {
	
	public static final RecipeMatchResult FAIL = new RecipeMatchResult(false, AbstractRecipeHandler.INVALID, AbstractRecipeHandler.INVALID, AbstractRecipeHandler.INVALID, AbstractRecipeHandler.INVALID);
	
	private final boolean match;
	
	final IntList itemIngredientNumbers, fluidIngredientNumbers, itemInputOrder, fluidInputOrder;
	
	public RecipeMatchResult(boolean match, IntList itemIngredientNumbers, IntList fluidIngredientNumbers, IntList itemInputOrder, IntList fluidInputOrder) {
		this.match = match;
		this.itemIngredientNumbers = itemIngredientNumbers;
		this.fluidIngredientNumbers = fluidIngredientNumbers;
		this.itemInputOrder = itemInputOrder;
		this.fluidInputOrder = fluidInputOrder;
	}
	
	public boolean matches() {
		return match;
	}
}
