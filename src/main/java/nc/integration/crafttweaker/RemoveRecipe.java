package nc.integration.crafttweaker;

import java.util.ArrayList;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.oredict.IOreDictEntry;
import nc.integration.jei.JEIMethods;
import nc.recipe.BaseRecipeHandler;
import nc.recipe.IRecipe;
import nc.recipe.RecipeOreStack;
import nc.recipe.SorptionType;
import nc.recipe.StackType;
import net.minecraft.item.ItemStack;

public class RemoveRecipe<T extends BaseRecipeHandler> implements IUndoableAction {
	
	public ArrayList ingredients;
	public SorptionType type;
	public IRecipe recipe;
	public boolean wasNull, wrongSize;
	public T helper;

	public RemoveRecipe(T helper, SorptionType type, ArrayList ingredients) {
		this.helper = helper;
		this.type = type;
		if (helper instanceof BaseRecipeHandler && (type == SorptionType.OUTPUT ? ingredients.size() != ((BaseRecipeHandler) helper).outputSizeItem + ((BaseRecipeHandler) helper).outputSizeFluid : ingredients.size() != ((BaseRecipeHandler) helper).inputSizeItem + ((BaseRecipeHandler) helper).inputSizeFluid)) {
			MineTweakerAPI.logError("A " + helper.getRecipeName() + " recipe was the wrong size");
			wrongSize = true;
			return;
		}

		ArrayList adaptedIngredients = new ArrayList();
		for (Object output : ingredients) {
			if (output == null) {
				MineTweakerAPI.logError(String.format("An ingredient of a %s was null", helper.getRecipeName()));
				wasNull = true;
				return;
			}
			if (output instanceof IItemStack) {
				adaptedIngredients.add(MineTweakerMC.getItemStack((IItemStack) output));
				continue;
			} else if (output instanceof IOreDictEntry) {
				adaptedIngredients.add(new RecipeOreStack(((IOreDictEntry) output).getName(), StackType.ITEM, 1));
				continue;
			} else if (output instanceof ILiquidStack) {
				adaptedIngredients.add(new RecipeOreStack(((ILiquidStack) output).getName(), StackType.FLUID, 1));
				continue;
			} else if (!(output instanceof ItemStack)) {
				MineTweakerAPI.logError(String.format("%s: Invalid ingredient: %s", helper.getRecipeName(), output));
			} else {
				adaptedIngredients.add(output);
				continue;
			}
		}

		this.ingredients = adaptedIngredients;
		this.recipe = type == SorptionType.OUTPUT ? helper.getRecipeFromOutputs(adaptedIngredients.toArray()) : helper.getRecipeFromInputs(adaptedIngredients.toArray());
	}
	
	public void apply() {
		if (recipe == null) {
			MineTweakerAPI.logError(String.format("%s: Removing Recipe - Couldn't find matching recipe %s", helper.getRecipeName(), ingredients));
			return;
		}
		if (!wasNull && !wrongSize) {
			boolean removed = helper.removeRecipe(recipe);
			if (!removed){
				MineTweakerAPI.logError(String.format("%s: Removing Recipe - Failed to remove recipe %s", helper.getRecipeName(), ingredients));
			}else{
				MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(JEIMethods.createJEIRecipe(recipe, helper));
			}
		}
	}
	
	public boolean canUndo() {
		return true;
	}
	
	public void undo() {
		if (recipe != null && !wasNull && !wrongSize) {
			helper.addRecipe(recipe);
			MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(JEIMethods.createJEIRecipe(recipe, helper));
		}
	}
	
	public String describe() {
		if (recipe == null) {
			return "ERROR: RECIPE IS NULL";
		}
		return String.format("Removing %s recipe (%s = %s)", helper.getRecipeName(), recipe.inputs(), recipe.outputs());
	}
	
	public String describeUndo() {
		return String.format("Reverting /%s/", describe());
	}
	
	public Object getOverrideKey() {
		return null;
	}
}
