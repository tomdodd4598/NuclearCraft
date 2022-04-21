package nc.integration.crafttweaker;

import static nc.recipe.IngredientSorption.INPUT;

import java.util.*;

import crafttweaker.*;
import crafttweaker.api.item.IIngredient;
import nc.recipe.*;
import nc.recipe.ingredient.*;

public class CTRemoveRecipe implements IAction {
	
	protected static boolean errored = false;
	
	protected final BasicRecipeHandler recipeHandler;
	protected final IngredientSorption type;
	protected BasicRecipe recipe;
	
	protected final List<IItemIngredient> itemIngredients = new ArrayList<>();
	protected final List<IFluidIngredient> fluidIngredients = new ArrayList<>();
	
	protected boolean nullIngredient, nullRecipe, wrongSize;
	
	public CTRemoveRecipe(BasicRecipeHandler recipeHandler, IngredientSorption type, List<IIngredient> ctIngredients) {
		this.recipeHandler = recipeHandler;
		this.type = type;
		
		int itemSize = type == INPUT ? recipeHandler.getItemInputSize() : recipeHandler.getItemOutputSize();
		int fluidSize = type == INPUT ? recipeHandler.getFluidInputSize() : recipeHandler.getFluidOutputSize();
		
		for (int i = 0; i < itemSize; ++i) {
			IItemIngredient ingredient = CTHelper.buildRemovalItemIngredient(ctIngredients.get(i));
			if (ingredient == null) {
				nullIngredient = true;
				return;
			}
			itemIngredients.add(ingredient);
		}
		for (int i = itemSize; i < itemSize + fluidSize; ++i) {
			IFluidIngredient ingredient = CTHelper.buildRemovalFluidIngredient(ctIngredients.get(i));
			if (ingredient == null) {
				nullIngredient = true;
				return;
			}
			fluidIngredients.add(ingredient);
		}
		
		if (ctIngredients.size() != itemSize + fluidSize) {
			CraftTweakerAPI.logError("A " + recipeHandler.getName() + " recipe removal had the wrong number of " + (type == INPUT ? "inputs" : "outputs") + ": " + RecipeHelper.getAllIngredientNamesConcat(itemIngredients, fluidIngredients));
			wrongSize = true;
			return;
		}
		
		recipe = type == INPUT ? recipeHandler.getRecipeFromIngredients(itemIngredients, fluidIngredients) : recipeHandler.getRecipeFromProducts(itemIngredients, fluidIngredients);
		if (recipe == null) {
			nullRecipe = true;
		}
	}
	
	@Override
	public void apply() {
		if (!isError()) {
			while (recipeHandler.removeRecipe(recipe)) {
				recipe = type == INPUT ? recipeHandler.getRecipeFromIngredients(itemIngredients, fluidIngredients) : recipeHandler.getRecipeFromProducts(itemIngredients, fluidIngredients);
			}
		}
	}
	
	@Override
	public String describe() {
		if (!isError()) {
			if (type == INPUT) {
				return "Removing " + recipeHandler.getName() + " recipe: " + RecipeHelper.getRecipeString(recipe);
			}
			else {
				return "Removing " + recipeHandler.getName() + " recipes for: " + RecipeHelper.getAllIngredientNamesConcat(itemIngredients, fluidIngredients);
			}
		}
		else {
			callError();
			
			String out = "Failed to remove " + recipeHandler.getName() + " recipe with " + RecipeHelper.getAllIngredientNamesConcat(itemIngredients, fluidIngredients) + " as the " + (type == INPUT ? "input" : "output");
			
			if (nullIngredient) {
				return out + " as one or more " + (type == INPUT ? "ingredients" : "products") + " had no match";
			}
			else if (nullRecipe) {
				return out + " as no matching recipe could be found";
			}
			else {
				return out;
			}
		}
	}
	
	protected boolean isError() {
		return nullIngredient || nullRecipe || wrongSize;
	}
	
	protected static void callError() {
		if (!errored) {
			errored = true;
			CraftTweakerAPI.logError("At least one NuclearCraft recipe removal method has errored. Check the log for more details");
		}
	}
}
