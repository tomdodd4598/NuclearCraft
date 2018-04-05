package nc.integration.crafttweaker;

import java.util.ArrayList;
import java.util.List;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.item.IngredientStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.oredict.IOreDictEntry;
import nc.recipe.IIngredient;
import nc.recipe.IRecipe;
import nc.recipe.NCRecipes;
import nc.recipe.RecipeMethods;
import nc.recipe.RecipeOreStack;
import nc.recipe.StackType;
import nc.util.StackHelper;
import net.minecraft.item.ItemStack;

public class AddRecipe implements IAction {
	
	public static boolean hasErrored = false;
	
	public ArrayList<IIngredient> inputs;
	public ArrayList<IIngredient> outputs;
	public ArrayList extras;
	public boolean wasNull, wrongSize;
	public final NCRecipes.Type recipeType;

	public AddRecipe(NCRecipes.Type recipeType, ArrayList<Object> inputs, ArrayList<Object> outputs, ArrayList extras) {
		this.recipeType = recipeType;
		if (inputs.size() != recipeType.getRecipeHandler().inputSizeItem + recipeType.getRecipeHandler().inputSizeFluid || outputs.size() != recipeType.getRecipeHandler().outputSizeItem + recipeType.getRecipeHandler().outputSizeFluid) {
			CraftTweakerAPI.logError("A " + recipeType.getRecipeHandler().getRecipeName() + " recipe was the wrong size");
			wrongSize = true;
			return;
		}
		ArrayList<IIngredient> adaptedInputs = new ArrayList<IIngredient>();
		ArrayList<IIngredient> adaptedOutputs = new ArrayList<IIngredient>();
		for (Object input : inputs) {
			if (input == null) {
				CraftTweakerAPI.logError(String.format("An ingredient of a %s was null", recipeType.getRecipeHandler().getRecipeName()));
				wasNull = true;
				return;
			}
			if (input instanceof IItemStack) {
				adaptedInputs.add(recipeType.getRecipeHandler().buildRecipeObject(CTMethods.getItemStack((IItemStack) input)));
				continue;
			} else if (input instanceof IOreDictEntry) {
				adaptedInputs.add(new RecipeOreStack(((IOreDictEntry) input).getName(), StackType.ITEM, ((IOreDictEntry) input).getAmount()));
				continue;
			} else if (input instanceof IngredientStack) {
				ArrayList<ItemStack> stackList = new ArrayList<ItemStack>();
				int stackSize = ((IngredientStack) input).getAmount();
				((IngredientStack) input).getItems().forEach(ingredient -> stackList.add(StackHelper.changeStackSize(CTMethods.getItemStack(ingredient), stackSize)));
				RecipeOreStack oreStack = RecipeMethods.getOreStackFromItems(stackList, stackSize);
				if (oreStack != null) adaptedInputs.add(oreStack);
				else adaptedInputs.add(recipeType.getRecipeHandler().buildRecipeObject(stackList));
				continue;
			} else if (input instanceof ILiquidStack) {
				adaptedInputs.add(recipeType.getRecipeHandler().buildRecipeObject(CTMethods.getLiquidStack((ILiquidStack) input)));
				continue;
			} else if (!(input instanceof ItemStack)) {
				CraftTweakerAPI.logError(String.format("%s: Invalid ingredient: %s, %s", recipeType.getRecipeHandler().getRecipeName(), input.getClass().getName(), input));
				continue;
			} else {
				adaptedInputs.add(recipeType.getRecipeHandler().buildRecipeObject(input));
				continue;
			}
		}
		for (Object output : outputs) {
			if (output == null) {
				CraftTweakerAPI.logError(String.format("An ingredient of a %s was null", recipeType.getRecipeHandler().getRecipeName()));
				wasNull = true;
				return;
			}
			if (output instanceof IItemStack) {
				adaptedOutputs.add(recipeType.getRecipeHandler().buildRecipeObject(CTMethods.getItemStack((IItemStack) output)));
				continue;
			} else if (output instanceof IOreDictEntry) {
				adaptedOutputs.add(new RecipeOreStack(((IOreDictEntry) output).getName(), StackType.ITEM, ((IOreDictEntry) output).getAmount()));
				continue;
			} else if (output instanceof IngredientStack) {
				ArrayList<ItemStack> stackList = new ArrayList<ItemStack>();
				int stackSize = ((IngredientStack) output).getAmount();
				((IngredientStack) output).getItems().forEach(ingredient -> stackList.add(StackHelper.changeStackSize(CTMethods.getItemStack(ingredient), stackSize)));
				RecipeOreStack oreStack = RecipeMethods.getOreStackFromItems(stackList, stackSize);
				if (oreStack != null) adaptedOutputs.add(oreStack);
				else adaptedOutputs.add(recipeType.getRecipeHandler().buildRecipeObject(stackList));
				continue;
			} else if (output instanceof ILiquidStack) {
				adaptedOutputs.add(recipeType.getRecipeHandler().buildRecipeObject(CTMethods.getLiquidStack((ILiquidStack) output)));
				continue;
			} else if (!(output instanceof ItemStack)) {
				CraftTweakerAPI.logError(String.format("%s: Invalid ingredient: %s, %s", recipeType.getRecipeHandler().getRecipeName(), output.getClass().getName(), output));
				wasNull = true;
				return;
			} else {
				adaptedOutputs.add(recipeType.getRecipeHandler().buildRecipeObject(output));
				continue;
			}
		}
		this.inputs = adaptedInputs;
		this.outputs = adaptedOutputs;
		this.extras = extras;
	}
	
	@Override
	public void apply() {
		if (!wasNull && !wrongSize) {
			boolean isShapeless = recipeType.getRecipeHandler().shapeless;
			IRecipe recipe = recipeType.getRecipeHandler().buildDefaultRecipe(inputs, outputs, extras, isShapeless);
			recipeType.getRecipeHandler().addRecipe(recipe);
			//CraftTweakerAPI.getIjeiRecipeRegistry().addRecipe(JEIMethods.createJEIRecipe(recipe, recipeType.getRecipeHandler()));
		} else {
			callError();
			//CraftTweakerAPI.logError(String.format("Failed to add %s recipe (%s -> %s)", recipeType.getRecipeHandler().getRecipeName(), RecipeMethods.getIngredientNames(inputs), RecipeMethods.getIngredientNames(outputs)));
		}
	}
	
	public void undo() {
		if (!wasNull && !wrongSize) {
			List values = recipeType.getRecipeHandler().getValuesFromList(inputs);
			IRecipe recipe = recipeType.getRecipeHandler().getRecipeFromInputs(values.toArray());
			if (recipe == null) {
				CraftTweakerAPI.logError(String.format("%s: Adding Recipe - Couldn't find matching recipe %s", recipeType.getRecipeHandler().getRecipeName(), values));
				return;
			}
			boolean removed = recipeType.getRecipeHandler().removeRecipe(recipe);			
			if (!removed) {
				CraftTweakerAPI.logError(String.format("%s: Adding Recipe - Failed to remove recipe %s", recipeType.getRecipeHandler().getRecipeName(), values));
			} else {
				//CraftTweakerAPI.getIjeiRecipeRegistry().removeRecipe(JEIMethods.createJEIRecipe(recipe, recipeType.getRecipeHandler()));
			}

		} else {
			CraftTweakerAPI.logError(String.format("Adding Recipe - Failed to remove %s recipe (%s -> %s)", recipeType.getRecipeHandler().getRecipeName(), RecipeMethods.getIngredientNames(inputs), RecipeMethods.getIngredientNames(outputs)));
		}
	}
	
	@Override
	public String describe() {
		if (wasNull || wrongSize) {
			return String.format("Error: Failed to add %s recipe (%s -> %s)", recipeType.getRecipeHandler().getRecipeName(), RecipeMethods.getIngredientNames(inputs), RecipeMethods.getIngredientNames(outputs));
		}
		return String.format("Adding %s recipe (%s -> %s)", recipeType.getRecipeHandler().getRecipeName(), RecipeMethods.getIngredientNames(inputs), RecipeMethods.getIngredientNames(outputs));
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
	
	public static void callError() {
		if (!hasErrored) CraftTweakerAPI.logError("Some NuclearCraft CraftTweaker recipe addition methods have errored - check the CraftTweaker log for more details");
		hasErrored = true;
	}
}
