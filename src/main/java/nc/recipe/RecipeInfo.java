package nc.recipe;

import java.util.List;

public class RecipeInfo<T extends IRecipe> {
	
	private final T recipe;
	
	private final RecipeMatchResult matchResult;
	
	public RecipeInfo(T recipe, RecipeMatchResult matchResult) {
		this.recipe = recipe;
		this.matchResult = matchResult;
	}
	
	public T getRecipe() {
		return recipe;
	}
	
	/** Already takes item input order into account! */
	public List<Integer> getItemIngredientNumbers() {
		return matchResult.itemIngredientNumbers;
	}
	
	/** Already takes fluid input order into account! */
	public List<Integer> getFluidIngredientNumbers() {
		return matchResult.fluidIngredientNumbers;
	}
	
	public List<Integer> getItemInputOrder() {
		return matchResult.itemInputOrder;
	}
	
	public List<Integer> getFluidInputOrder() {
		return matchResult.fluidInputOrder;
	}
}
