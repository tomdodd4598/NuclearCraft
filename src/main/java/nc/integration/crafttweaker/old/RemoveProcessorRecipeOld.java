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
import nc.recipe.RecipeFluidStack;
import nc.integration.crafttweaker.CTMethods;
import nc.integration.crafttweaker.RemoveProcessorRecipe;
import nc.recipe.IFluidIngredient;
import nc.recipe.IItemIngredient;
import nc.recipe.NCRecipes;
import nc.recipe.RecipeOreStack;
import nc.recipe.SorptionType;
import nc.util.ItemStackHelper;
import nc.util.RecipeHelper;
import net.minecraft.item.ItemStack;

public class RemoveProcessorRecipeOld implements IAction {
	
	public List<IItemIngredient> itemIngredients;
	public List<IFluidIngredient> fluidIngredients;
	public SorptionType type;
	public ProcessorRecipe recipe;
	public boolean wasNull, wrongSize;
	public final NCRecipes.Type recipeType;

	public RemoveProcessorRecipeOld(NCRecipes.Type recipeType, SorptionType type, ArrayList<Object> ingredients) {
		this.recipeType = recipeType;
		this.type = type;
		if (type == SorptionType.OUTPUT ? ingredients.size() != recipeType.getRecipeHandler().itemOutputSize + recipeType.getRecipeHandler().fluidOutputSize : ingredients.size() != recipeType.getRecipeHandler().itemInputSize + recipeType.getRecipeHandler().fluidInputSize) {
			CraftTweakerAPI.logError("A " + recipeType.getRecipeHandler().getRecipeName() + " recipe was the wrong size");
			wrongSize = true;
			return;
		}

		List<IItemIngredient> itemIngredients = new ArrayList<IItemIngredient>();
		List<IFluidIngredient> fluidIngredients = new ArrayList<IFluidIngredient>();
		for (Object output : ingredients) {
			if (output == null) {
				CraftTweakerAPI.logError(String.format("An ingredient of a %s was null", recipeType.getRecipeHandler().getRecipeName()));
				wasNull = true;
				return;
			}
			if (output instanceof IItemStack) {
				itemIngredients.add(recipeType.getRecipeHandler().buildItemIngredient(CTMethods.getItemStack((IItemStack) output)));
				continue;
			} else if (output instanceof IOreDictEntry) {
				itemIngredients.add(new RecipeOreStack(((IOreDictEntry) output).getName(), ((IOreDictEntry) output).getAmount()));
				continue;
			} else if (output instanceof IngredientStack) {
				ArrayList<ItemStack> stackList = new ArrayList<ItemStack>();
				((IngredientStack) output).getItems().forEach(ingredient -> stackList.add(ItemStackHelper.changeStackSize(CTMethods.getItemStack(ingredient), ((IngredientStack) output).getAmount())));
				itemIngredients.add(recipeType.getRecipeHandler().buildItemIngredient(stackList));
				continue;
			} else if (output instanceof ILiquidStack) {
				fluidIngredients.add(new RecipeFluidStack(((ILiquidStack) output).getName(), ((ILiquidStack) output).getAmount()));
				continue;
			} else if (!(output instanceof ItemStack)) {
				CraftTweakerAPI.logError(String.format("%s: Invalid ingredient: %s, %s", recipeType.getRecipeHandler().getRecipeName(), output.getClass().getName(), output));
				wasNull = true;
				return;
			} else {
				itemIngredients.add(recipeType.getRecipeHandler().buildItemIngredient(output));
				continue;
			}
		}

		this.itemIngredients = itemIngredients;
		this.fluidIngredients = fluidIngredients;
		this.recipe = type == SorptionType.INPUT ? recipeType.getRecipeHandler().getRecipeFromIngredients(itemIngredients, fluidIngredients) : recipeType.getRecipeHandler().getRecipeFromProducts(itemIngredients, fluidIngredients);
	}
	
	@Override
	public void apply() {
		if (recipe == null) {
			callError();
			return;
		}
		if (!wasNull && !wrongSize) {
			boolean removed = recipeType.getRecipeHandler().removeRecipe(recipe);
			if (!removed) callError();
		}
	}
	
	@Override
	public String describe() {
		if (recipe == null) {
			return String.format("Error: Failed to remove %s recipe (old) with %s as the " + (type == SorptionType.INPUT ? "input" : "output"), recipeType.getRecipeName(), RecipeHelper.getAllIngredientNamesConcat(itemIngredients, fluidIngredients));
		}
		return String.format("Removing %s recipe (old): %s", recipeType.getRecipeName(), RecipeHelper.getRecipeString(recipe));
	}
	
	public static void callError() {
		RemoveProcessorRecipe.callError();
	}
}
