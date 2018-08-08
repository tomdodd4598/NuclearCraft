package nc.integration.crafttweaker.old;

import java.util.ArrayList;
import java.util.List;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.item.IngredientStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.oredict.IOreDictEntry;
import nc.recipe.ProcessorRecipe;
import nc.integration.crafttweaker.AddProcessorRecipe;
import nc.integration.crafttweaker.CTMethods;
import nc.recipe.IFluidIngredient;
import nc.recipe.IItemIngredient;
import nc.recipe.NCRecipes;
import nc.recipe.RecipeOreStack;
import nc.util.ItemStackHelper;
import nc.util.RecipeHelper;
import net.minecraft.item.ItemStack;

public class AddProcessorRecipeOld implements IAction {
	
	public List<IItemIngredient> itemIngredients;
	public List<IFluidIngredient> fluidIngredients;
	public List<IItemIngredient> itemProducts;
	public List<IFluidIngredient> fluidProducts;
	public ArrayList extras;
	public boolean wasNull, wrongSize;
	public final NCRecipes.Type recipeType;

	public AddProcessorRecipeOld(NCRecipes.Type recipeType, ArrayList<Object> inputs, ArrayList<Object> outputs, ArrayList extras) {
		this.recipeType = recipeType;
		if (inputs.size() != recipeType.getRecipeHandler().itemInputSize + recipeType.getRecipeHandler().fluidInputSize || outputs.size() != recipeType.getRecipeHandler().itemOutputSize + recipeType.getRecipeHandler().fluidOutputSize) {
			CraftTweakerAPI.logError("A " + recipeType.getRecipeHandler().getRecipeName() + " recipe was the wrong size");
			wrongSize = true;
			return;
		}
		List<IItemIngredient> itemIngredients = new ArrayList<IItemIngredient>();
		List<IFluidIngredient> fluidIngredients = new ArrayList<IFluidIngredient>();
		List<IItemIngredient> itemProducts = new ArrayList<IItemIngredient>();
		List<IFluidIngredient> fluidProducts = new ArrayList<IFluidIngredient>();
		for (Object input : inputs) {
			if (input == null) {
				CraftTweakerAPI.logError(String.format("An ingredient of a %s was null", recipeType.getRecipeHandler().getRecipeName()));
				wasNull = true;
				return;
			}
			if (input instanceof IItemStack) {
				itemIngredients.add(recipeType.getRecipeHandler().buildItemIngredient(CTMethods.getItemStack((IItemStack) input)));
				continue;
			} else if (input instanceof IOreDictEntry) {
				itemIngredients.add(new RecipeOreStack(((IOreDictEntry) input).getName(), ((IOreDictEntry) input).getAmount()));
				continue;
			} else if (input instanceof IngredientStack) {
				ArrayList<ItemStack> stackList = new ArrayList<ItemStack>();
				int stackSize = ((IngredientStack) input).getAmount();
				((IngredientStack) input).getItems().forEach(ingredient -> stackList.add(ItemStackHelper.changeStackSize(CTMethods.getItemStack(ingredient), stackSize)));
				RecipeOreStack oreStack = RecipeHelper.getOreStackFromItems(stackList, stackSize);
				if (oreStack != null) itemIngredients.add(oreStack);
				else itemIngredients.add(recipeType.getRecipeHandler().buildItemIngredient(stackList));
				continue;
			} else if (input instanceof ILiquidStack) {
				fluidIngredients.add(recipeType.getRecipeHandler().buildFluidIngredient(CTMethods.getLiquidStack((ILiquidStack) input)));
				continue;
			} else if (!(input instanceof ItemStack)) {
				CraftTweakerAPI.logError(String.format("%s: Invalid ingredient: %s, %s", recipeType.getRecipeHandler().getRecipeName(), input.getClass().getName(), input));
				continue;
			} else {
				itemIngredients.add(recipeType.getRecipeHandler().buildItemIngredient(input));
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
				itemProducts.add(recipeType.getRecipeHandler().buildItemIngredient(CTMethods.getItemStack((IItemStack) output)));
				continue;
			} else if (output instanceof IOreDictEntry) {
				itemProducts.add(new RecipeOreStack(((IOreDictEntry) output).getName(), ((IOreDictEntry) output).getAmount()));
				continue;
			} else if (output instanceof IngredientStack) {
				ArrayList<ItemStack> stackList = new ArrayList<ItemStack>();
				int stackSize = ((IngredientStack) output).getAmount();
				((IngredientStack) output).getItems().forEach(ingredient -> stackList.add(ItemStackHelper.changeStackSize(CTMethods.getItemStack(ingredient), stackSize)));
				RecipeOreStack oreStack = RecipeHelper.getOreStackFromItems(stackList, stackSize);
				if (oreStack != null) itemProducts.add(oreStack);
				else itemProducts.add(recipeType.getRecipeHandler().buildItemIngredient(stackList));
				continue;
			} else if (output instanceof ILiquidStack) {
				fluidProducts.add(recipeType.getRecipeHandler().buildFluidIngredient(CTMethods.getLiquidStack((ILiquidStack) output)));
				continue;
			} else if (!(output instanceof ItemStack)) {
				CraftTweakerAPI.logError(String.format("%s: Invalid ingredient: %s, %s", recipeType.getRecipeHandler().getRecipeName(), output.getClass().getName(), output));
				wasNull = true;
				return;
			} else {
				itemProducts.add(recipeType.getRecipeHandler().buildItemIngredient(output));
				continue;
			}
		}
		this.itemIngredients = itemIngredients;
		this.fluidIngredients = fluidIngredients;
		this.itemProducts = itemProducts;
		this.fluidProducts = fluidProducts;
		this.extras = extras;
	}
	
	@Override
	public void apply() {
		if (!wasNull && !wrongSize) {
			boolean isShapeless = recipeType.getRecipeHandler().shapeless;
			ProcessorRecipe recipe = recipeType.getRecipeHandler().buildRecipe(itemIngredients, fluidIngredients, itemProducts, fluidProducts, extras, isShapeless);
			boolean added = recipeType.getRecipeHandler().addRecipe(recipe);
			if (!added) {
				callError();
				wasNull = true;
			}
		} else callError();
	}
	
	@Override
	public String describe() {
		if (wasNull || wrongSize) {
			return String.format("Error: Failed to add %s recipe (old): %s", recipeType.getRecipeName(), RecipeHelper.getRecipeString(itemIngredients, fluidIngredients, itemProducts, fluidProducts));
		}
		return String.format("Adding %s recipe (old): %s", recipeType.getRecipeName(), RecipeHelper.getRecipeString(itemIngredients, fluidIngredients, itemProducts, fluidProducts));
	}
	
	public static void callError() {
		AddProcessorRecipe.callError();
	}
}
