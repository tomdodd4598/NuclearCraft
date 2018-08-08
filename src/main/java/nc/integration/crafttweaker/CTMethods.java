package nc.integration.crafttweaker;

import java.util.ArrayList;
import java.util.List;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.item.IngredientStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.oredict.IOreDictEntry;
import nc.recipe.IFluidIngredient;
import nc.recipe.IItemIngredient;
import nc.recipe.NCRecipes;
import nc.recipe.RecipeEmptyFluidStack;
import nc.recipe.RecipeEmptyItemStack;
import nc.recipe.RecipeFluidStack;
import nc.recipe.RecipeOreStack;
import nc.util.ItemStackHelper;
import nc.util.NCUtil;
import nc.util.RecipeHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CTMethods {
	
	public static ItemStack getItemStack(IItemStack item) {
		if(item == null) return ItemStack.EMPTY;
		
		Object internal = item.getInternal();
		if(internal == null || !(internal instanceof ItemStack)) {
			CraftTweakerAPI.logError("Not a valid item stack: " + item);
		}
		return ((ItemStack) internal).copy();
	}
	
	public static ItemStack getItemStack(IIngredient ingredient) {
		if(ingredient == null) return ItemStack.EMPTY;
		
		List<IItemStack> items = ingredient.getItems();
		if(items.size() != 1) {
			NCUtil.getLogger().error("Not an ingredient with a single item: " + ingredient);
		}
		return getItemStack(items.get(0));
	}
	
	public static FluidStack getLiquidStack(ILiquidStack stack) {
		if(stack == null) return null;
		return (FluidStack) stack.getInternal();
	}
	
	public static IItemIngredient buildAdditionItemIngredient(Object ingredient, NCRecipes.Type recipeType) {
		if (ingredient == null) {
			return new RecipeEmptyItemStack();
		} else if (ingredient instanceof IItemStack) {
			return recipeType.getRecipeHandler().buildItemIngredient(getItemStack((IItemStack) ingredient));
		} else if (ingredient instanceof IOreDictEntry) {
			return new RecipeOreStack(((IOreDictEntry) ingredient).getName(), ((IOreDictEntry) ingredient).getAmount());
		} else if (ingredient instanceof IngredientStack) {
			return buildAdditionItemIngredientArray((IngredientStack) ingredient, recipeType);
		} else {
			CraftTweakerAPI.logError(String.format("%s: Invalid ingredient: %s, %s", recipeType.getRecipeName(), ingredient.getClass().getName(), ingredient));
			return null;
		}
	}
	
	public static IFluidIngredient buildAdditionFluidIngredient(Object ingredient, NCRecipes.Type recipeType) {
		if (ingredient == null) {
			return new RecipeEmptyFluidStack();
		} else if (ingredient instanceof ILiquidStack) {
			return recipeType.getRecipeHandler().buildFluidIngredient(getLiquidStack((ILiquidStack) ingredient));
		} else {
			CraftTweakerAPI.logError(String.format("%s: Invalid ingredient: %s, %s", recipeType.getRecipeName(), ingredient.getClass().getName(), ingredient));
			return null;
		}
	}
	
	public static IItemIngredient buildAdditionItemIngredientArray(IngredientStack stack, NCRecipes.Type recipeType) {
		List<ItemStack> stackList = new ArrayList<ItemStack>();
		stack.getItems().forEach(item -> stackList.add(ItemStackHelper.changeStackSize(getItemStack(item), stack.getAmount())));
		RecipeOreStack oreStack = RecipeHelper.getOreStackFromItems(stackList, stack.getAmount());
		if (oreStack != null) return oreStack;
		return recipeType.getRecipeHandler().buildItemIngredient(stackList);
	}
	
	public static IItemIngredient buildRemovalItemIngredient(IIngredient ingredient, NCRecipes.Type recipeType) {
		if (ingredient == null) {
			return new RecipeEmptyItemStack();
		} else if (ingredient instanceof IItemStack) {
			return recipeType.getRecipeHandler().buildItemIngredient(CTMethods.getItemStack((IItemStack) ingredient));
		} else if (ingredient instanceof IOreDictEntry) {
			return new RecipeOreStack(((IOreDictEntry) ingredient).getName(), ((IOreDictEntry) ingredient).getAmount());
		} else if (ingredient instanceof IngredientStack) {
			return buildRemovalItemIngredientArray((IngredientStack) ingredient, recipeType);
		} else {
			CraftTweakerAPI.logError(String.format("%s: Invalid ingredient: %s, %s", recipeType.getRecipeName(), ingredient.getClass().getName(), ingredient));
			return null;
		}
	}
	
	public static IFluidIngredient buildRemovalFluidIngredient(IIngredient ingredient, NCRecipes.Type recipeType) {
		if (ingredient == null) {
			return new RecipeEmptyFluidStack();
		} else if (ingredient instanceof ILiquidStack) {
			return new RecipeFluidStack(((ILiquidStack) ingredient).getName(), ((ILiquidStack) ingredient).getAmount());
		} else {
			CraftTweakerAPI.logError(String.format("%s: Invalid ingredient: %s, %s", recipeType.getRecipeName(), ingredient.getClass().getName(), ingredient));
			return null;
		}
	}
	
	public static IItemIngredient buildRemovalItemIngredientArray(IngredientStack stack, NCRecipes.Type recipeType) {
		List<ItemStack> stackList = new ArrayList<ItemStack>();
		stack.getItems().forEach(ingredient -> stackList.add(ItemStackHelper.changeStackSize(CTMethods.getItemStack(ingredient), stack.getAmount())));
		return recipeType.getRecipeHandler().buildItemIngredient(stackList);
	}
}
