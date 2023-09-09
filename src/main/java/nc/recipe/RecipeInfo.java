package nc.recipe;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.ints.IntList;

public class RecipeInfo<T extends IRecipe> {
	
	public final @Nonnull T recipe;
	
	public final RecipeMatchResult matchResult;
	
	public RecipeInfo(@Nonnull T recipe, RecipeMatchResult matchResult) {
		this.recipe = recipe;
		this.matchResult = matchResult;
	}
	
	/** Already takes item input order into account! */
	public IntList getItemIngredientNumbers() {
		return matchResult.itemIngredientNumbers;
	}
	
	/** Already takes fluid input order into account! */
	public IntList getFluidIngredientNumbers() {
		return matchResult.fluidIngredientNumbers;
	}
	
	public IntList getItemInputOrder() {
		return matchResult.itemInputOrder;
	}
	
	public IntList getFluidInputOrder() {
		return matchResult.fluidInputOrder;
	}
}
