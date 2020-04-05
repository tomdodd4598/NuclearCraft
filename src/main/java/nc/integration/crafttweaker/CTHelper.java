package nc.integration.crafttweaker;

import java.util.ArrayList;
import java.util.List;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.item.IngredientOr;
import crafttweaker.api.item.IngredientStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.oredict.IOreDictEntry;
import nc.integration.crafttweaker.ingredient.CTChanceFluidIngredient;
import nc.integration.crafttweaker.ingredient.CTChanceItemIngredient;
import nc.recipe.RecipeHelper;
import nc.recipe.ingredient.ChanceFluidIngredient;
import nc.recipe.ingredient.ChanceItemIngredient;
import nc.recipe.ingredient.EmptyFluidIngredient;
import nc.recipe.ingredient.EmptyItemIngredient;
import nc.recipe.ingredient.FluidIngredient;
import nc.recipe.ingredient.IFluidIngredient;
import nc.recipe.ingredient.IItemIngredient;
import nc.recipe.ingredient.OreIngredient;
import nc.util.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CTHelper {
	
	public static ItemStack getItemStack(IItemStack item) {
		if(item == null) return ItemStack.EMPTY;
		
		Object internal = item.getInternal();
		if (!(internal instanceof ItemStack)) {
			CraftTweakerAPI.logError("Not a valid item stack: " + item);
		}
		return ((ItemStack) internal).copy();
	}
	
	public static FluidStack getFluidStack(ILiquidStack stack) {
		if(stack == null) return null;
		return (FluidStack) stack.getInternal();
	}
	
	public static IItemIngredient buildAdditionItemIngredient(IIngredient ingredient) {
		if (ingredient == null) {
			return new EmptyItemIngredient();
		} else if (ingredient instanceof CTChanceItemIngredient) {
			CTChanceItemIngredient chanceIngredient = (CTChanceItemIngredient) ingredient;
			return new ChanceItemIngredient(buildAdditionItemIngredient(chanceIngredient.getInternalIngredient()), chanceIngredient.getChancePercent(), chanceIngredient.getMinStackSize());
		} else if (ingredient instanceof IItemStack) {
			return RecipeHelper.buildItemIngredient(getItemStack((IItemStack) ingredient));
		} else if (ingredient instanceof IOreDictEntry) {
			return new OreIngredient(((IOreDictEntry)ingredient).getName(), ((IOreDictEntry)ingredient).getAmount());
		} else if (ingredient instanceof IngredientStack) {
			return buildOreIngredientArray(ingredient, true);
		} else if (ingredient instanceof IngredientOr) {
			return buildAdditionItemIngredientArray((IngredientOr) ingredient);
		} else {
			CraftTweakerAPI.logError(String.format("NuclearCraft: Invalid ingredient: %s, %s", ingredient.getClass().getName(), ingredient));
			return null;
		}
	}
	
	public static IFluidIngredient buildAdditionFluidIngredient(IIngredient ingredient) {
		if (ingredient == null) {
			return new EmptyFluidIngredient();
		} else if (ingredient instanceof CTChanceFluidIngredient) {
			CTChanceFluidIngredient chanceIngredient = (CTChanceFluidIngredient) ingredient;
			return new ChanceFluidIngredient(buildAdditionFluidIngredient(chanceIngredient.getInternalIngredient()), chanceIngredient.getChancePercent(), chanceIngredient.getStackDiff(), chanceIngredient.getMinStackSize());
		} else if (ingredient instanceof ILiquidStack) {
			return RecipeHelper.buildFluidIngredient(getFluidStack((ILiquidStack) ingredient));
		} else if (ingredient instanceof IngredientOr) {
			return buildAdditionFluidIngredientArray((IngredientOr) ingredient);
		} else {
			CraftTweakerAPI.logError(String.format("NuclearCraft: Invalid ingredient: %s, %s", ingredient.getClass().getName(), ingredient));
			return null;
		}
	}
	
	public static IItemIngredient buildRemovalItemIngredient(IIngredient ingredient) {
		if (ingredient == null) {
			return new EmptyItemIngredient();
		} else if (ingredient instanceof IItemStack) {
			return RecipeHelper.buildItemIngredient(CTHelper.getItemStack((IItemStack) ingredient));
		} else if (ingredient instanceof IOreDictEntry) {
			return new OreIngredient(((IOreDictEntry)ingredient).getName(), ((IOreDictEntry)ingredient).getAmount());
		} else if (ingredient instanceof IngredientStack) {
			return buildOreIngredientArray(ingredient, false);
		} else if (ingredient instanceof IngredientOr) {
			return buildRemovalItemIngredientArray((IngredientOr) ingredient);
		} else {
			CraftTweakerAPI.logError(String.format("NuclearCraft: Invalid ingredient: %s, %s", ingredient.getClass().getName(), ingredient));
			return null;
		}
	}
	
	public static IFluidIngredient buildRemovalFluidIngredient(IIngredient ingredient) {
		if (ingredient == null) {
			return new EmptyFluidIngredient();
		} else if (ingredient instanceof ILiquidStack) {
			return new FluidIngredient(((ILiquidStack)ingredient).getName(), ((ILiquidStack)ingredient).getAmount());
		} else if (ingredient instanceof IngredientOr) {
			return buildRemovalFluidIngredientArray((IngredientOr) ingredient);
		} else {
			CraftTweakerAPI.logError(String.format("NuclearCraft: Invalid ingredient: %s, %s", ingredient.getClass().getName(), ingredient));
			return null;
		}
	}
	
	// Array Ingredients
	
	public static IItemIngredient buildAdditionItemIngredientArray(IngredientOr ingredient) {
		if (!(ingredient.getInternal() instanceof IIngredient[])) {
			CraftTweakerAPI.logError(String.format("NuclearCraft: Invalid ingredient: %s, %s", ingredient.getClass().getName(), ingredient));
			return null;
		}
		List<IItemIngredient> ingredientList = new ArrayList<>();
		for (IIngredient ctIngredient : (IIngredient[])ingredient.getInternal()) {
			ingredientList.add(buildAdditionItemIngredient(ctIngredient));
		}
		return RecipeHelper.buildItemIngredient(ingredientList);
	}
	
	public static IItemIngredient buildRemovalItemIngredientArray(IngredientOr ingredient) {
		if (!(ingredient.getInternal() instanceof IIngredient[])) {
			CraftTweakerAPI.logError(String.format("NuclearCraft: Invalid ingredient: %s, %s", ingredient.getClass().getName(), ingredient));
			return null;
		}
		List<IItemIngredient> ingredientList = new ArrayList<>();
		for (IIngredient ctIngredient : (IIngredient[])ingredient.getInternal()) {
			ingredientList.add(buildRemovalItemIngredient(ctIngredient));
		}
		return RecipeHelper.buildItemIngredient(ingredientList);
	}
	
	public static IItemIngredient buildOreIngredientArray(IIngredient stack, boolean addition) {
		List<ItemStack> stackList = new ArrayList<>();
		stack.getItems().forEach(item -> stackList.add(ItemStackHelper.changeStackSize(getItemStack(item), stack.getAmount())));
		if (addition) {
			OreIngredient oreStack = RecipeHelper.getOreStackFromItems(stackList, stack.getAmount());
			if (oreStack != null) return oreStack;
		}
		return RecipeHelper.buildItemIngredient(stackList);
	}
	
	public static IFluidIngredient buildAdditionFluidIngredientArray(IngredientOr ingredient) {
		if (!(ingredient.getInternal() instanceof IIngredient[])) {
			CraftTweakerAPI.logError(String.format("NuclearCraft: Invalid ingredient: %s, %s", ingredient.getClass().getName(), ingredient));
			return null;
		}
		List<IFluidIngredient> ingredientList = new ArrayList<>();
		for (IIngredient ctIngredient : (IIngredient[])ingredient.getInternal()) {
			ingredientList.add(buildAdditionFluidIngredient(ctIngredient));
		}
		return RecipeHelper.buildFluidIngredient(ingredientList);
	}
	
	public static IFluidIngredient buildRemovalFluidIngredientArray(IngredientOr ingredient) {
		if (!(ingredient.getInternal() instanceof IIngredient[])) {
			CraftTweakerAPI.logError(String.format("NuclearCraft: Invalid ingredient: %s, %s", ingredient.getClass().getName(), ingredient));
			return null;
		}
		List<IFluidIngredient> ingredientList = new ArrayList<>();
		for (IIngredient ctIngredient : (IIngredient[])ingredient.getInternal()) {
			ingredientList.add(buildRemovalFluidIngredient(ctIngredient));
		}
		return RecipeHelper.buildFluidIngredient(ingredientList);
	}
}
