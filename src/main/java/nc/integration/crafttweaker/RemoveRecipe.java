package nc.integration.crafttweaker;

import java.util.ArrayList;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.oredict.IOreDictEntry;
import nc.recipe.BaseRecipeHandler;
import nc.recipe.IIngredient;
import nc.recipe.IRecipe;
import nc.recipe.RecipeOreStack;
import nc.recipe.SorptionType;
import nc.recipe.StackType;
import net.minecraft.item.ItemStack;

public class RemoveRecipe<T extends BaseRecipeHandler> implements IAction {
	
	public ArrayList<IIngredient> ingredients;
	public SorptionType type;
	public IRecipe recipe;
	public boolean wasNull, wrongSize;
	public T helper;

	public RemoveRecipe(T helper, SorptionType type, ArrayList<Object> ingredients) {
		this.helper = helper;
		this.type = type;
		if (helper instanceof BaseRecipeHandler && (type == SorptionType.OUTPUT ? ingredients.size() != ((BaseRecipeHandler) helper).outputSizeItem + ((BaseRecipeHandler) helper).outputSizeFluid : ingredients.size() != ((BaseRecipeHandler) helper).inputSizeItem + ((BaseRecipeHandler) helper).inputSizeFluid)) {
			CraftTweakerAPI.logError("A " + helper.getRecipeName() + " recipe was the wrong size");
			wrongSize = true;
			return;
		}

		ArrayList adaptedIngredients = new ArrayList();
		for (Object output : ingredients) {
			if (output == null) {
				CraftTweakerAPI.logError(String.format("An ingredient of a %s was null", helper.getRecipeName()));
				wasNull = true;
				return;
			}
			if (output instanceof IItemStack) {
				adaptedIngredients.add(CraftTweakerMC.getItemStack((IItemStack) output));
				continue;
			} else if (output instanceof IOreDictEntry) {
				adaptedIngredients.add(new RecipeOreStack(((IOreDictEntry) output).getName(), StackType.ITEM, ((IOreDictEntry) output).getAmount()));
				continue;
			} else if (output instanceof ILiquidStack) {
				adaptedIngredients.add(helper.buildRecipeObject(CraftTweakerMC.getLiquidStack((ILiquidStack) output)));
				continue;
			} else if (!(output instanceof ItemStack)) {
				CraftTweakerAPI.logError(String.format("%s: Invalid ingredient: %s", helper.getRecipeName(), output));
			} else {
				adaptedIngredients.add(output);
				continue;
			}
		}

		this.ingredients = adaptedIngredients;
		this.recipe = type == SorptionType.OUTPUT ? helper.getRecipeFromOutputs(adaptedIngredients.toArray()) : helper.getRecipeFromInputs(adaptedIngredients.toArray());
	}
	
	@Override
	public void apply() {
		if (recipe == null) {
			CraftTweakerAPI.logError(String.format("%s: Removing Recipe - Couldn't find matching recipe %s", helper.getRecipeName(), ingredients));
			return;
		}
		if (!wasNull && !wrongSize) {
			boolean removed = helper.removeRecipe(recipe);
			if (!removed){
				CraftTweakerAPI.logError(String.format("%s: Removing Recipe - Failed to remove recipe %s", helper.getRecipeName(), ingredients));
			}else{
				//CraftTweakerAPI.getIjeiRecipeRegistry().removeRecipe(JEIMethods.createJEIRecipe(recipe, helper));
			}
		}
	}
	
	public boolean canUndo() {
		return true;
	}
	
	public void undo() {
		if (recipe != null && !wasNull && !wrongSize) {
			helper.addRecipe(recipe);
			//CraftTweakerAPI.getIjeiRecipeRegistry().addRecipe(JEIMethods.createJEIRecipe(recipe, helper));
		}
	}
	
	@Override
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
