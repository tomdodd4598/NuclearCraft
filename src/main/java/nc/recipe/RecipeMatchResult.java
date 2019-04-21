package nc.recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeMatchResult {
	
	public static final RecipeMatchResult FAIL = new RecipeMatchResult(false, new ArrayList<Integer>(), new ArrayList<Integer>(), AbstractRecipeHandler.INVALID, AbstractRecipeHandler.INVALID);
	
	private final boolean match;
	
	final List<Integer> itemIngredientNumbers, fluidIngredientNumbers, itemInputOrder, fluidInputOrder;
	
	public RecipeMatchResult(boolean match, List<Integer> itemIngredientNumbers, List<Integer> fluidIngredientNumbers, List<Integer> itemInputOrder, List<Integer> fluidInputOrder) {
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
