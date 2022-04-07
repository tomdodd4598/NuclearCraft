package nc.integration.crafttweaker;

import java.util.*;

import crafttweaker.*;
import crafttweaker.api.item.IIngredient;
import nc.recipe.*;
import nc.recipe.ingredient.*;

public class CTAddRecipe implements IAction {
	
	protected static boolean errored = false;
	
	protected final BasicRecipeHandler recipeHandler;
	protected BasicRecipe recipe;
	
	protected final List<IItemIngredient> itemIngredients = new ArrayList<>();
	protected final List<IFluidIngredient> fluidIngredients = new ArrayList<>();
	protected final List<IItemIngredient> itemProducts = new ArrayList<>();
	protected final List<IFluidIngredient> fluidProducts = new ArrayList<>();
	protected final List<Object> extras = new ArrayList<>();
	
	protected boolean nullInputs = true, nullIngredient, nullRecipe, wrongSize;
	
	public CTAddRecipe(BasicRecipeHandler recipeHandler, List<Object> objects) {
		this.recipeHandler = recipeHandler;
		
		int count = 0;
		while (count < objects.size()) {
			Object object = objects.get(count);
			if (count < recipeHandler.getItemInputSize()) {
				if (object != null) {
					if (!(object instanceof IIngredient)) {
						nullIngredient = true;
						return;
					}
					nullInputs = false;
				}
				IItemIngredient ingredient = CTHelper.buildAdditionItemIngredient((IIngredient) object);
				if (ingredient == null) {
					nullIngredient = true;
					return;
				}
				itemIngredients.add(ingredient);
			}
			else if (count < recipeHandler.getItemInputSize() + recipeHandler.getFluidInputSize()) {
				if (object != null) {
					if (!(object instanceof IIngredient)) {
						nullIngredient = true;
						return;
					}
					nullInputs = false;
				}
				IFluidIngredient ingredient = CTHelper.buildAdditionFluidIngredient((IIngredient) object);
				if (ingredient == null) {
					nullIngredient = true;
					return;
				}
				fluidIngredients.add(ingredient);
			}
			else if (count < recipeHandler.getItemInputSize() + recipeHandler.getFluidInputSize() + recipeHandler.getItemOutputSize()) {
				if (object != null) {
					if (!(object instanceof IIngredient)) {
						nullIngredient = true;
						return;
					}
				}
				IItemIngredient ingredient = CTHelper.buildAdditionItemIngredient((IIngredient) object);
				if (ingredient == null) {
					nullIngredient = true;
					return;
				}
				itemProducts.add(ingredient);
			}
			else if (count < recipeHandler.getItemInputSize() + recipeHandler.getFluidInputSize() + recipeHandler.getItemOutputSize() + recipeHandler.getFluidOutputSize()) {
				if (object != null) {
					if (!(object instanceof IIngredient)) {
						nullIngredient = true;
						return;
					}
				}
				IFluidIngredient ingredient = CTHelper.buildAdditionFluidIngredient((IIngredient) object);
				if (ingredient == null) {
					nullIngredient = true;
					return;
				}
				fluidProducts.add(ingredient);
			}
			else {
				extras.add(object);
			}
			++count;
		}
		
		if (itemIngredients.size() != recipeHandler.getItemInputSize() || fluidIngredients.size() != recipeHandler.getFluidInputSize() || itemProducts.size() != recipeHandler.getItemOutputSize() || fluidProducts.size() != recipeHandler.getFluidOutputSize()) {
			CraftTweakerAPI.logError("A " + recipeHandler.getName() + " recipe addition had the wrong size: " + RecipeHelper.getRecipeString(itemIngredients, fluidIngredients, itemProducts, fluidProducts));
			wrongSize = true;
			return;
		}
		
		recipe = recipeHandler.buildRecipe(itemIngredients, fluidIngredients, itemProducts, fluidProducts, extras, recipeHandler.isShapeless());
		if (recipe == null) {
			nullRecipe = true;
		}
	}
	
	@Override
	public void apply() {
		if (!isError()) {
			recipeHandler.addRecipe(recipe);
		}
	}
	
	@Override
	public String describe() {
		String recipeString = RecipeHelper.getRecipeString(itemIngredients, fluidIngredients, itemProducts, fluidProducts);
		if (!isError()) {
			return "Adding " + recipeHandler.getName() + " recipe: " + recipeString;
		}
		else {
			callError();
			
			String out = "Failed to add " + recipeHandler.getName() + " recipe " + recipeString;
			
			if (nullInputs) {
				return out + " as all ingredients were null";
			}
			if (nullIngredient) {
				return out + " as one or more ingredients had no match";
			}
			else {
				return out;
			}
		}
	}
	
	protected boolean isError() {
		return nullInputs || nullIngredient || nullRecipe || wrongSize;
	}
	
	protected static void callError() {
		if (!errored) {
			errored = true;
			CraftTweakerAPI.logError("At least one NuclearCraft recipe addition method has errored. Check the log for more details");
		}
	}
}
