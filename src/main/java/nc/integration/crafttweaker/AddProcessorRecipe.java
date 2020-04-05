package nc.integration.crafttweaker;

import java.util.ArrayList;
import java.util.List;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.recipe.RecipeHelper;
import nc.recipe.ingredient.IFluidIngredient;
import nc.recipe.ingredient.IItemIngredient;

public class AddProcessorRecipe implements IAction {
	
	public static boolean hasErrored = false;
	
	public List<IItemIngredient> itemIngredients;
	public List<IFluidIngredient> fluidIngredients;
	public List<IItemIngredient> itemProducts;
	public List<IFluidIngredient> fluidProducts;
	public List extras;
	public ProcessorRecipe recipe;
	public boolean inputsAllNull = true, ingredientError, wasNull, wrongSize;
	public final ProcessorRecipeHandler recipeHandler;

	public AddProcessorRecipe(ProcessorRecipeHandler recipeHandler, List objects) {
		this.recipeHandler = recipeHandler;
		
		int listCount = 0, ingredientCount = 0;
		List<IItemIngredient> itemIngredients = new ArrayList<>();
		List<IFluidIngredient> fluidIngredients = new ArrayList<>();
		List<IItemIngredient> itemProducts = new ArrayList<>();
		List<IFluidIngredient> fluidProducts = new ArrayList<>();
		List extras = new ArrayList();
		
		while (listCount < objects.size()) {
			Object object = objects.get(listCount);
			if (ingredientCount < recipeHandler.getItemInputSize()) {
				if (object != null) {
					if (!(object instanceof IIngredient)) {
						ingredientError = true;
						return;
					}
					inputsAllNull = false;
				}
				IItemIngredient ingredient = CTHelper.buildAdditionItemIngredient((IIngredient) object);
				if (ingredient == null) {
					ingredientError = true;
					return;
				}
				itemIngredients.add(ingredient);
			} else if (ingredientCount < recipeHandler.getItemInputSize() + recipeHandler.getFluidInputSize()) {
				if (object != null) {
					if (!(object instanceof IIngredient)) {
						ingredientError = true;
						return;
					}
					inputsAllNull = false;
				}
				IFluidIngredient ingredient = CTHelper.buildAdditionFluidIngredient((IIngredient) object);
				if (ingredient == null) {
					ingredientError = true;
					return;
				}
				fluidIngredients.add(ingredient);
			} else if (ingredientCount < recipeHandler.getItemInputSize() + recipeHandler.getFluidInputSize() + recipeHandler.getItemOutputSize()) {
				if (object != null) {
					if (!(object instanceof IIngredient)) {
						ingredientError = true;
						return;
					}
				}
				IItemIngredient ingredient = CTHelper.buildAdditionItemIngredient((IIngredient) object);
				if (ingredient == null) {
					ingredientError = true;
					return;
				}
				itemProducts.add(ingredient);
			} else if (ingredientCount < recipeHandler.getItemInputSize() + recipeHandler.getFluidInputSize() + recipeHandler.getItemOutputSize() + recipeHandler.getFluidOutputSize()) {
				if (object != null) {
					if (!(object instanceof IIngredient)) {
						ingredientError = true;
						return;
					}
				}
				IFluidIngredient ingredient = CTHelper.buildAdditionFluidIngredient((IIngredient) object);
				if (ingredient == null) {
					ingredientError = true;
					return;
				}
				fluidProducts.add(ingredient);
			} else {
				extras.add(object);
			}
			listCount++;
			ingredientCount++;
		}
		
		if (itemIngredients.size() != recipeHandler.getItemInputSize() || fluidIngredients.size() != recipeHandler.getFluidInputSize() || itemProducts.size() != recipeHandler.getItemOutputSize() || fluidProducts.size() != recipeHandler.getFluidOutputSize()) {
			CraftTweakerAPI.logError("A " + recipeHandler.getRecipeName() + " recipe was the wrong size");
			wrongSize = true;
			return;
		}
		
		this.itemIngredients = itemIngredients;
		this.fluidIngredients = fluidIngredients;
		this.itemProducts = itemProducts;
		this.fluidProducts = fluidProducts;
		this.extras = extras;
		
		recipe = recipeHandler.buildRecipe(itemIngredients, fluidIngredients, itemProducts, fluidProducts, extras, recipeHandler.isShapeless());
		if (recipe == null) wasNull = true;
	}
	
	@Override
	public void apply() {
		if (!inputsAllNull && !ingredientError && !wasNull && !wrongSize) {
			recipeHandler.addRecipe(recipe);
		}
	}
	
	@Override
	public String describe() {
		if (inputsAllNull || ingredientError || wasNull || wrongSize) {
			if (ingredientError || wrongSize) callError();
			return String.format("Error: Failed to add %s recipe: %s", recipeHandler.getRecipeName(), RecipeHelper.getRecipeString(itemIngredients, fluidIngredients, itemProducts, fluidProducts));
		}
		return String.format("Adding %s recipe: %s", recipeHandler.getRecipeName(), RecipeHelper.getRecipeString(itemIngredients, fluidIngredients, itemProducts, fluidProducts));
	}
	
	public static void callError() {
		if (!hasErrored) {
			CraftTweakerAPI.logError("At least one NuclearCraft CraftTweaker recipe addition method has errored - check the CraftTweaker log for more details");
		}
		hasErrored = true;
	}
}
