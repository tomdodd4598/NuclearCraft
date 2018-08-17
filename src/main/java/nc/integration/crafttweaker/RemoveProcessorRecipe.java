package nc.integration.crafttweaker;

import java.util.ArrayList;
import java.util.List;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import nc.recipe.IFluidIngredient;
import nc.recipe.IItemIngredient;
import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipe;
import nc.recipe.SorptionType;
import nc.util.RecipeHelper;

public class RemoveProcessorRecipe implements IAction {
	
	public static boolean hasErrored = false;
	
	public List<IItemIngredient> itemIngredients;
	public List<IFluidIngredient> fluidIngredients;
	public SorptionType type;
	public ProcessorRecipe recipe;
	public boolean wasNull, wrongSize;
	public final NCRecipes.Type recipeType;

	public RemoveProcessorRecipe(NCRecipes.Type recipeType, SorptionType type, List<IIngredient> ctIngredients) {
		this.recipeType = recipeType;
		this.type = type;
		int itemSize = type == SorptionType.INPUT ? recipeType.getRecipeHandler().itemInputSize : recipeType.getRecipeHandler().itemOutputSize;
		int fluidSize = type == SorptionType.INPUT ? recipeType.getRecipeHandler().fluidInputSize : recipeType.getRecipeHandler().fluidOutputSize;
		if (ctIngredients.size() != itemSize + fluidSize) {
			CraftTweakerAPI.logError("A " + recipeType.getRecipeName() + " recipe was the wrong size");
			wrongSize = true;
			return;
		}
		List<IItemIngredient> itemIngredients = new ArrayList<IItemIngredient>();
		List<IFluidIngredient> fluidIngredients = new ArrayList<IFluidIngredient>();
		for (int i = 0; i < itemSize; i++) {
			IItemIngredient ingredient = CTMethods.buildRemovalItemIngredient(ctIngredients.get(i), recipeType);
			if (ingredient == null) {
				wasNull = true;
				return;
			}
			itemIngredients.add(ingredient);
		}
		for (int i = itemSize; i < fluidSize; i++) {
			IFluidIngredient ingredient = CTMethods.buildRemovalFluidIngredient(ctIngredients.get(i), recipeType);
			if (ingredient == null) {
				wasNull = true;
				return;
			}
			fluidIngredients.add(ingredient);
		}

		this.itemIngredients = itemIngredients;
		this.fluidIngredients = fluidIngredients;
		this.recipe = type == SorptionType.INPUT ? recipeType.getRecipeHandler().getRecipeFromIngredients(itemIngredients, fluidIngredients) : recipeType.getRecipeHandler().getRecipeFromProducts(itemIngredients, fluidIngredients);
		if (recipe == null) wasNull = true;
	}
	
	@Override
	public void apply() {
		if (!wasNull && !wrongSize) {
			boolean removed = recipeType.getRecipeHandler().removeRecipe(recipe);
			if (removed) return;
		}
		callError();
	}
	
	@Override
	public String describe() {
		if (wasNull || wrongSize) {
			return String.format("Error: Failed to remove %s recipe with %s as the " + (type == SorptionType.INPUT ? "input" : "output"), recipeType.getRecipeName(), RecipeHelper.getAllIngredientNamesConcat(itemIngredients, fluidIngredients));
		}
		return String.format("Removing %s recipe: %s", recipeType.getRecipeName(), RecipeHelper.getRecipeString(recipe));
	}
	
	public static void callError() {
		if (!hasErrored) CraftTweakerAPI.logError("At least one NuclearCraft CraftTweaker recipe removal method has errored - check the CraftTweaker log for more details");
		hasErrored = true;
	}
}
