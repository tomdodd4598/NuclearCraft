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
import nc.recipe.ProcessorRecipeHandler;
import nc.recipe.RecipeChanceFluidStack;
import nc.recipe.RecipeChanceItemStack;
import nc.recipe.RecipeEmptyFluidStack;
import nc.recipe.RecipeEmptyItemStack;
import nc.util.RecipeHelper;

public class AddProcessorRecipe implements IAction {
	
	public static boolean hasErrored = false;
	
	public List<IItemIngredient> itemIngredients;
	public List<IFluidIngredient> fluidIngredients;
	public List<IItemIngredient> itemProducts;
	public List<IFluidIngredient> fluidProducts;
	public List extras;
	public ProcessorRecipe recipe;
	public boolean wasNull, wrongSize;
	public final NCRecipes.Type recipeType;

	public AddProcessorRecipe(NCRecipes.Type recipeType, List<Object> objects) {
		this.recipeType = recipeType;
		ProcessorRecipeHandler recipeHandler = recipeType.getRecipeHandler();
		
		int listCount = 0, ingredientCount = 0;
		List<IItemIngredient> itemIngredients = new ArrayList<IItemIngredient>();
		List<IFluidIngredient> fluidIngredients = new ArrayList<IFluidIngredient>();
		List<IItemIngredient> itemProducts = new ArrayList<IItemIngredient>();
		List<IFluidIngredient> fluidProducts = new ArrayList<IFluidIngredient>();
		List extras = new ArrayList();
		
		while (listCount < objects.size()) {
			Object object = objects.get(listCount);
			Object nextObject = listCount + 1 < objects.size() ? objects.get(listCount + 1) : null;
			Object nextNextObject = listCount + 2 < objects.size() ? objects.get(listCount + 2) : null;
			Object nextNextNextObject = listCount + 3 < objects.size() ? objects.get(listCount + 3) : null;
			if (ingredientCount < recipeHandler.itemInputSize) {
				if (object != null && !(object instanceof IIngredient)) {
					wasNull = true;
					return;
				}
				IItemIngredient ingredient = CTMethods.buildAdditionItemIngredient(object, recipeType);
				if (ingredient == null) {
					wasNull = true;
					return;
				}
				itemIngredients.add(ingredient);
			} else if (ingredientCount < recipeHandler.itemInputSize + recipeHandler.fluidInputSize) {
				if (object != null && !(object instanceof IIngredient)) {
					wasNull = true;
					return;
				}
				IFluidIngredient ingredient = CTMethods.buildAdditionFluidIngredient(object, recipeType);
				if (ingredient == null) {
					wasNull = true;
					return;
				}
				fluidIngredients.add(ingredient);
			} else if (ingredientCount < recipeHandler.itemInputSize + recipeHandler.fluidInputSize + recipeHandler.itemOutputSize) {
				if (object != null && !(object instanceof IIngredient)) {
					wasNull = true;
					return;
				}
				IItemIngredient ingredient = CTMethods.buildAdditionItemIngredient(object, recipeType);
				if (ingredient == null) {
					wasNull = true;
					return;
				}
				if (nextObject instanceof Integer && nextNextObject instanceof Integer) {
					int chancePercent = (Integer) nextObject;
					int minStackSize = (Integer) nextNextObject;
					if (chancePercent <= 0) ingredient = new RecipeEmptyItemStack();
					else if (chancePercent < 100) ingredient = new RecipeChanceItemStack(ingredient, chancePercent, minStackSize);
					listCount += 2;
				}
				else if (nextObject instanceof Integer) {
					int chancePercent = (Integer) nextObject;
					if (chancePercent <= 0) ingredient = new RecipeEmptyItemStack();
					else if (chancePercent < 100) ingredient = new RecipeChanceItemStack(ingredient, chancePercent);
					listCount++;
				}
				itemProducts.add(ingredient);
			} else if (ingredientCount < recipeHandler.itemInputSize + recipeHandler.fluidInputSize + recipeHandler.itemOutputSize + recipeHandler.fluidOutputSize) {
				if (object != null && !(object instanceof IIngredient)) {
					wasNull = true;
					return;
				}
				IFluidIngredient ingredient = CTMethods.buildAdditionFluidIngredient(object, recipeType);
				if (ingredient == null) {
					wasNull = true;
					return;
				}
				if (nextObject instanceof Integer && nextNextObject instanceof Integer && nextNextNextObject instanceof Integer) {
					int chancePercent = (Integer) nextObject;
					int stackDiff = (Integer) nextNextObject;
					int minStackSize = (Integer) nextNextNextObject;
					if (chancePercent <= 0) ingredient = new RecipeEmptyFluidStack();
					else if (chancePercent < 100) ingredient = new RecipeChanceFluidStack(ingredient, chancePercent, stackDiff, minStackSize);
					listCount += 2;
				}
				else if (nextObject instanceof Integer && nextNextObject instanceof Integer) {
					int chancePercent = (Integer) nextObject;
					int stackDiff = (Integer) nextNextObject;
					if (chancePercent <= 0) ingredient = new RecipeEmptyFluidStack();
					else if (chancePercent < 100) ingredient = new RecipeChanceFluidStack(ingredient, chancePercent, stackDiff);
					listCount++;
				}
				fluidProducts.add(ingredient);
			} else {
				extras.add(object);
			}
			listCount++;
			ingredientCount++;
		}
		
		if (itemIngredients.size() != recipeHandler.itemInputSize || fluidIngredients.size() != recipeHandler.fluidInputSize || itemProducts.size() != recipeHandler.itemOutputSize || fluidProducts.size() != recipeHandler.fluidOutputSize) {
			CraftTweakerAPI.logError("A " + recipeType.getRecipeName() + " recipe was the wrong size");
			wrongSize = true;
			return;
		}
		
		this.itemIngredients = itemIngredients;
		this.fluidIngredients = fluidIngredients;
		this.itemProducts = itemProducts;
		this.fluidProducts = fluidProducts;
		this.extras = extras;
		
		recipe = recipeType.getRecipeHandler().buildRecipe(itemIngredients, fluidIngredients, itemProducts, fluidProducts, extras, recipeType.getRecipeHandler().shapeless);
		if (recipe == null) wasNull = true;
	}
	
	@Override
	public void apply() {
		if (!wasNull && !wrongSize) {
			boolean added = recipeType.getRecipeHandler().addRecipe(recipe);
			if (added) return;
		}
		callError();
	}
	
	@Override
	public String describe() {
		if (wasNull || wrongSize) {
			return String.format("Error: Failed to add %s recipe: %s", recipeType.getRecipeName(), RecipeHelper.getRecipeString(itemIngredients, fluidIngredients, itemProducts, fluidProducts));
		}
		return String.format("Adding %s recipe: %s", recipeType.getRecipeName(), RecipeHelper.getRecipeString(itemIngredients, fluidIngredients, itemProducts, fluidProducts));
	}
	
	public static void callError() {
		if (!hasErrored) CraftTweakerAPI.logError("At least one NuclearCraft CraftTweaker recipe addition method has errored - check the CraftTweaker log for more details");
		hasErrored = true;
	}
}
