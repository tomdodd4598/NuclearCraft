package nc.recipe;

import it.unimi.dsi.fastutil.ints.IntList;
import nc.recipe.ingredient.*;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Predicate;

public class RecipeInfo<T extends IRecipe> {
	
	public final @Nonnull T recipe;
	
	public final RecipeMatchResult matchResult;
	
	public RecipeInfo(@Nonnull T recipe, RecipeMatchResult matchResult) {
		this.recipe = recipe;
		this.matchResult = matchResult;
	}
	
	/**
	 * Already takes item input order into account!
	 */
	public IntList getItemIngredientNumbers() {
		return matchResult.itemIngredientNumbers;
	}
	
	/**
	 * Already takes fluid input order into account!
	 */
	public IntList getFluidIngredientNumbers() {
		return matchResult.fluidIngredientNumbers;
	}
	
	public IntList getItemInputOrder() {
		return matchResult.itemInputOrder;
	}
	
	public IntList getFluidInputOrder() {
		return matchResult.fluidInputOrder;
	}
	
	// Recipe Unit Info
	
	public RecipeUnitInfo getRecipeUnitInfo(double baseRateMultiplier) {
		if (matchResult == null || !matchResult.isMatch) {
			return RecipeUnitInfo.DEFAULT;
		}
		
		List<IItemIngredient> itemIngredients = recipe.getItemIngredients();
		List<IFluidIngredient> fluidIngredients = recipe.getFluidIngredients();
		
		Predicate<IIngredient<?>> notEmpty = x -> !x.isEmpty();
		long itemInputCount = itemIngredients.stream().filter(notEmpty).count();
		long fluidInputCount = fluidIngredients.stream().filter(notEmpty).count();
		
		if (itemInputCount == 1 && fluidInputCount == 0) {
			IntList itemInputOrder = getItemInputOrder(), itemIngredientNumbers = getItemIngredientNumbers();
			for (int i = 0, len = itemIngredients.size(); i < len; ++i) {
				IItemIngredient itemIngredient = itemIngredients.get(itemInputOrder.get(i));
				if (!itemIngredient.isEmpty()) {
					return new RecipeUnitInfo("I/t", 0, baseRateMultiplier * itemIngredient.getMaxStackSize(itemIngredientNumbers.get(i)));
				}
			}
		}
		else if (itemInputCount == 0 && fluidInputCount == 1) {
			IntList fluidInputOrder = getFluidInputOrder(), fluidIngredientNumbers = getFluidIngredientNumbers();
			for (int i = 0, len = fluidIngredients.size(); i < len; ++i) {
				IFluidIngredient fluidIngredient = fluidIngredients.get(fluidInputOrder.get(i));
				if (!fluidIngredient.isEmpty()) {
					return new RecipeUnitInfo("B/t", -1, baseRateMultiplier * fluidIngredient.getMaxStackSize(fluidIngredientNumbers.get(i)));
				}
			}
		}
		
		return RecipeUnitInfo.DEFAULT.withRateMultiplier(baseRateMultiplier);
	}
}
