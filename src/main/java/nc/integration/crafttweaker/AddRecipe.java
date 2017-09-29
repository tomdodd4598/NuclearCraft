package nc.integration.crafttweaker;

import java.util.ArrayList;
import java.util.List;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.oredict.IOreDictEntry;
import nc.integration.jei.JEIMethods;
import nc.recipe.BaseRecipeHandler;
import nc.recipe.IIngredient;
import nc.recipe.IRecipe;
import nc.recipe.IRecipeStack;
import nc.recipe.RecipeOreStack;
import nc.recipe.StackType;
import nc.util.NCUtil;
import net.minecraft.item.ItemStack;

public class AddRecipe<T extends BaseRecipeHandler> implements IUndoableAction {
	
	public ArrayList<IIngredient> inputs;
	public ArrayList<IIngredient> outputs;
	public ArrayList extras;
	public boolean wasNull, wrongSize;
	public T helper;

	public AddRecipe(T helper, ArrayList<Object> inputs, ArrayList<Object> outputs, ArrayList extras) {
		this.helper = helper;
		if (helper instanceof BaseRecipeHandler && (inputs.size() != ((BaseRecipeHandler) helper).inputSizeItem + ((BaseRecipeHandler) helper).inputSizeFluid || outputs.size() != ((BaseRecipeHandler) helper).outputSizeItem + ((BaseRecipeHandler) helper).outputSizeFluid)) {
			MineTweakerAPI.logError("A " + helper.getRecipeName() + " recipe was the wrong size");
			wrongSize = true;
			return;
		}
		ArrayList<IIngredient> adaptedInputs = new ArrayList<IIngredient>();
		ArrayList<IIngredient> adaptedOutputs = new ArrayList<IIngredient>();
		for (Object input : inputs) {
			if (input == null) {
				MineTweakerAPI.logError(String.format("An ingredient of a %s was null", helper.getRecipeName()));
				wasNull = true;
				return;
			}
			if (input instanceof IItemStack) {
				adaptedInputs.add(helper.buildRecipeObject(MineTweakerMC.getItemStack((IItemStack) input)));
				continue;
			} else if (input instanceof IOreDictEntry) {
				adaptedInputs.add(new RecipeOreStack(((IOreDictEntry) input).getName(), StackType.ITEM, ((IOreDictEntry) input).getAmount()));
				continue;
			} else if (input instanceof ILiquidStack) {
				adaptedInputs.add(helper.buildRecipeObject(MineTweakerMC.getLiquidStack((ILiquidStack) input)));
				continue;
			} else if (!(input instanceof ItemStack)) {
				MineTweakerAPI.logError(String.format("%s: Invalid ingredient: %s", helper.getRecipeName(), input));
				continue;
			} else {
				adaptedInputs.add(helper.buildRecipeObject(input));
				continue;
			}
		}
		for (Object output : outputs) {
			if (output == null) {
				MineTweakerAPI.logError(String.format("An ingredient of a %s was null", helper.getRecipeName()));
				wasNull = true;
				return;
			}
			if (output instanceof IItemStack) {
				adaptedOutputs.add(helper.buildRecipeObject(MineTweakerMC.getItemStack((IItemStack) output)));
				continue;
			} else if (output instanceof IOreDictEntry) {
				adaptedOutputs.add(new RecipeOreStack(((IOreDictEntry) output).getName(), StackType.ITEM, ((IOreDictEntry) output).getAmount()));
				continue;
			} else if (output instanceof ILiquidStack) {
				adaptedOutputs.add(helper.buildRecipeObject(MineTweakerMC.getLiquidStack((ILiquidStack) output)));
				continue;
			} else if (!(output instanceof ItemStack)) {
				MineTweakerAPI.logError(String.format("%s: Invalid ingredient: %s", helper.getRecipeName(), output));
				continue;
			} else {
				adaptedOutputs.add(helper.buildRecipeObject(output));
				continue;
			}
		}
		this.inputs = adaptedInputs;
		this.outputs = adaptedOutputs;
		this.extras = extras;
	}
	
	public void apply() {
		if (!wasNull && !wrongSize) {
			boolean isShapeless = helper instanceof BaseRecipeHandler ? ((BaseRecipeHandler) helper).shapeless : true;
			IRecipe recipe = helper.buildRecipe((ArrayList<IRecipeStack>) inputs.clone(), (ArrayList<IRecipeStack>) outputs.clone(), (ArrayList) extras.clone(), isShapeless);
			helper.addRecipe(recipe);	
			MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(JEIMethods.createJEIRecipe(recipe, helper));
		} else {
			NCUtil.getLogger().error(String.format("Failed to add %s recipe (%s = %s)", helper.getRecipeName(), inputs, outputs));
		}
	}
	
	@SuppressWarnings("static-access")
	public void undo() {
		if (!wasNull && !wrongSize) {
			List values = helper.getValuesFromList(inputs);
			IRecipe recipe = helper.getRecipeFromInputs(values.toArray());
			if (recipe == null) {
				MineTweakerAPI.logError(String.format("%s: Adding Recipe - Couldn't find matching recipe %s", helper.getRecipeName(), values));
				return;
			}
			boolean removed = helper.removeRecipe(recipe);			
			if (!removed) {
				MineTweakerAPI.logError(String.format("%s: Adding Recipe - Failed to remove recipe %s", helper.getRecipeName(), values));
			}else{
				MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(JEIMethods.createJEIRecipe(recipe, helper));
			}

		} else {
			NCUtil.getLogger().error(String.format("Adding Recipe - Failed to remove %s recipe (%s = %s)", helper.getRecipeName(), inputs, outputs));
		}
	}
	
	@SuppressWarnings("static-access")
	public String describe() {
		return String.format("Adding %s recipe (%s = %s)", helper.getRecipeName(), helper.getValuesFromList(inputs), helper.getValuesFromList(outputs));
	}
	
	public String describeUndo() {
		return String.format("Reverting /%s/", describe());
	}
	
	public boolean canUndo() {
		return true;
	}
	
	public Object getOverrideKey() {
		return null;
	}
}
