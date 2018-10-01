package nc.util;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import nc.recipe.IRecipe;
import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipeHandler;
import nc.recipe.IngredientSorption;
import nc.recipe.ingredient.IFluidIngredient;
import nc.recipe.ingredient.IItemIngredient;
import nc.recipe.ingredient.ChanceFluidIngredient;
import nc.recipe.ingredient.ChanceItemIngredient;
import nc.recipe.ingredient.EmptyFluidIngredient;
import nc.recipe.ingredient.EmptyItemIngredient;
import nc.recipe.ingredient.FluidIngredient;
import nc.recipe.ingredient.OreIngredient;
import nc.tile.internal.fluid.Tank;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class RecipeHelper {
	
	public static boolean containsItemIngredient(List<IItemIngredient> list, IItemIngredient ingredient) {
		for (IItemIngredient i : list) {
			if (i == null) continue;
			if (i.matches(ingredient, IngredientSorption.NEUTRAL)) return true;
		}
		return false;
	}
	
	public static boolean containsFluidIngredient(List<IFluidIngredient> list, IFluidIngredient ingredient) {
		for (IFluidIngredient i : list) {
			if (i == null) continue;
			if (i.matches(ingredient, IngredientSorption.NEUTRAL)) return true;
		}
		return false;
	}
	
	public static ItemStack fixItemStack(Object object) {
		if (object instanceof ItemStack) {
			ItemStack stack = ((ItemStack) object).copy();
			if (stack.getCount() <= 0) {
				stack.setCount(1);
			}
			return stack;
		} else if (object instanceof Item) {
			return new ItemStack((Item) object, 1);
		} else {
			if (!(object instanceof Block)) {
				throw new RuntimeException(String.format("Invalid ItemStack: %s", object));
			}
			return new ItemStack((Block) object, 1);
		}
	}
	
	public static FluidStack fixFluidStack(Object object) {
		if (object instanceof FluidStack) {
			FluidStack fluidstack = ((FluidStack) object).copy();
			if (fluidstack.amount <= 0) {
				fluidstack.amount = 1000;
			}
			return fluidstack;
		} else {
			if (!(object instanceof Fluid)) {
				throw new RuntimeException(String.format("Invalid FluidStack: %s", object));
			}
			return new FluidStack((Fluid) object, 1000);
		}
	}
	
	public static OreIngredient oreStackFromString(String name) {
		if (OreDictHelper.oreExists(name)) return new OreIngredient(name, 1);
		return null;
	}
	
	public static FluidIngredient fluidStackFromString(String name) {
		if (FluidRegHelper.fluidExists(name)) return new FluidIngredient(name, 1000);
		return null;
	}

	public static List<List<ItemStack>> getItemInputLists(List<IItemIngredient> ingredientList) {
		List<List<ItemStack>> values = new ArrayList<List<ItemStack>>();
		ingredientList.forEach(ingredient -> values.add(ingredient.getInputStackList()));
		return values;
	}
	
	public static List<List<FluidStack>> getFluidInputLists(List<IFluidIngredient> ingredientList) {
		List<List<FluidStack>> values = new ArrayList<List<FluidStack>>();
		ingredientList.forEach(ingredient -> values.add(ingredient.getInputStackList()));
		return values;
	}
	
	public static List<List<ItemStack>> getItemOutputLists(List<IItemIngredient> ingredientList) {
		List<List<ItemStack>> values = new ArrayList<List<ItemStack>>();
		ingredientList.forEach(ingredient -> values.add(getItemOutputStackList(ingredient)));
		return values;
	}
	
	public static List<List<FluidStack>> getFluidOutputLists(List<IFluidIngredient> ingredientList) {
		List<List<FluidStack>> values = new ArrayList<List<FluidStack>>();
		ingredientList.forEach(ingredient -> values.add(getFluidOutputStackList(ingredient)));
		return values;
	}
	
	public static List<ItemStack> getItemOutputStackList(IItemIngredient ingredient) {
		if (ingredient instanceof ChanceItemIngredient) return ingredient.getOutputStackList();
		else return Lists.newArrayList(ingredient.getStack());
	}
	
	public static List<FluidStack> getFluidOutputStackList(IFluidIngredient ingredient) {
		if (ingredient instanceof ChanceFluidIngredient) return ingredient.getOutputStackList();
		else return Lists.newArrayList(ingredient.getStack());
	}
	
	@Nullable
	public static List<ItemStack> getItemOutputList(List<IItemIngredient> list) {
		if (list.contains(null)) return new ArrayList<ItemStack>();
		List stacks = new ArrayList<ItemStack>();
		list.forEach(ingredient -> stacks.add(ingredient.getStack()));
		if (stacks.contains(null)) return new ArrayList<ItemStack>();
		return stacks;
	}
	
	@Nullable
	public static List<FluidStack> getFluidOutputList(List<IFluidIngredient> list) {
		if (list.contains(null)) return new ArrayList<FluidStack>();
		List stacks = new ArrayList<FluidStack>();
		list.forEach(ingredient -> stacks.add(ingredient.getStack()));
		if (stacks.contains(null)) return new ArrayList<FluidStack>();
		return stacks;
	}
	
	@Nullable
	public static ItemStack getItemStackFromIngredientList(List<IItemIngredient> list, int pos) {
		if (!list.isEmpty() && pos < list.size()) {
			IItemIngredient object = list.get(pos);
			return object.getStack();
		}
		return null;
	}
	
	@Nullable
	public static FluidStack getFluidStackFromIngredientList(List<IFluidIngredient> list, int pos) {
		if (!list.isEmpty() && pos < list.size()) {
			IFluidIngredient object = list.get(pos);
			return object.getStack();
		}
		return null;
	}

	public static boolean matchingIngredients(IngredientSorption sorption, List<IItemIngredient> itemIngredients, List<IFluidIngredient> fluidIngredients, List items, List fluids, boolean shapeless) {
		List<IItemIngredient> itemIngredientsMatch = new ArrayList<IItemIngredient>(itemIngredients);
		List<IFluidIngredient> fluidIngredientsMatch = new ArrayList<IFluidIngredient>(fluidIngredients);
		if (itemIngredients.size() != items.size() || fluidIngredients.size() != fluids.size()) {
			return false;
		}
		int pos = -1;
		if (!items.isEmpty()) itemInputs: for (Object item : items) {
			/*if (stack.isEmpty()) {
				return false;
			}*/
			pos++;
			if (shapeless) {
				for (IItemIngredient itemIngredient : itemIngredients) {
					if (itemIngredient.matches(item, sorption)) {
						itemIngredientsMatch.remove(itemIngredient);
						continue itemInputs;
					}
				}
			} else if (itemIngredients.get(pos).matches(item, sorption)) {
				itemIngredientsMatch.remove(itemIngredients.get(pos));
				continue itemInputs;
			}
			return false;
		}
		pos = -1;
		if (!fluids.isEmpty()) fluidInputs: for (Object fluid : fluids) {
			/*if (tank.isEmpty()) {
				return false;
			}*/
			pos++;
			if (fluid instanceof Tank) fluid = (FluidStack)((Tank)fluid).getFluid();
			if (shapeless) {
				for (IFluidIngredient fluidIngredient : fluidIngredients) {
					if (fluidIngredient.matches(fluid, sorption)) {
						fluidIngredientsMatch.remove(fluidIngredient);
						continue fluidInputs;
					}
				}
			} else if (fluidIngredients.get(pos).matches(fluid, sorption)) {
				fluidIngredientsMatch.remove(fluidIngredients.get(pos));
				continue fluidInputs;
			}
			return false;
		}
		return true;
	}
	
	public static List<String> getItemIngredientNames(List<IItemIngredient> ingredientList) {
		List<String> ingredientNames = new ArrayList<String>();
		for (IItemIngredient ingredient : ingredientList) {
			if (ingredient == null || ingredient instanceof EmptyItemIngredient) ingredientNames.add("null");
			else ingredientNames.add(ingredient.getMaxStackSize() + " x " + ingredient.getIngredientName());
		}
		return ingredientNames;
	}
	
	public static List<String> getFluidIngredientNames(List<IFluidIngredient> ingredientList) {
		List<String> ingredientNames = new ArrayList<String>();
		for (IFluidIngredient ingredient : ingredientList) {
			if (ingredient == null || ingredient instanceof EmptyFluidIngredient) ingredientNames.add("null");
			else ingredientNames.add(ingredient.getMaxStackSize() + " x " + ingredient.getIngredientName());
		}
		return ingredientNames;
	}
	
	public static String getAllIngredientNamesConcat(List<IItemIngredient> itemIngredientList, List<IFluidIngredient> fluidIngredientList) {
		return StringHelper.stringListConcat(getItemIngredientNames(itemIngredientList), getFluidIngredientNames(fluidIngredientList));
	}
	
	public static String getRecipeString(List<IItemIngredient> itemIngredientList, List<IFluidIngredient> fluidIngredientList, List<IItemIngredient> itemProductList, List<IFluidIngredient> fluidProductList) {
		return getAllIngredientNamesConcat(itemIngredientList, fluidIngredientList) + " -> " + getAllIngredientNamesConcat(itemProductList, fluidProductList);
	}
	
	public static String getRecipeString(IRecipe recipe) {
		if (recipe == null) return "nullRecipe";
		return getRecipeString(recipe.itemIngredients(), recipe.fluidIngredients(), recipe.itemProducts(), recipe.fluidProducts());
	}
	
	public static List<String> buildItemIngredientNames(List ingredientList, NCRecipes.Type recipeType) {
		List<String> ingredientNames = new ArrayList<String>();
		for (Object obj : ingredientList) {
			if (obj == null) ingredientNames.add("null");
			else {
				if (!(obj instanceof IItemIngredient)) obj = recipeType.getRecipeHandler().buildItemIngredient(obj);
				IItemIngredient ingredient = (IItemIngredient) obj;
				ingredientNames.add(ingredient.getMaxStackSize() + " x " + ingredient.getIngredientName());
			}
		}
		return ingredientNames;
	}
	
	public static List<String> buildFluidIngredientNames(List ingredientList, NCRecipes.Type recipeType) {
		List<String> ingredientNames = new ArrayList<String>();
		for (Object obj : ingredientList) {
			if (obj == null) ingredientNames.add("null");
			else {
				if (!(obj instanceof IFluidIngredient)) obj = recipeType.getRecipeHandler().buildFluidIngredient(obj);
				IFluidIngredient ingredient = (IFluidIngredient) obj;
				ingredientNames.add(ingredient.getMaxStackSize() + " x " + ingredient.getIngredientName());
			}
		}
		return ingredientNames;
	}
	
	public static List<List<String>> validFluids(NCRecipes.Type recipeType) {
		return validFluids(recipeType, new ArrayList<String>());
	}
	
	public static List<List<String>> validFluids(NCRecipes.Type recipeType, List<String> exceptions) {
		return validFluids(recipeType.getRecipeHandler(), exceptions);
	}
	
	private static List<List<String>> validFluids(ProcessorRecipeHandler recipes, List<String> exceptions) {
		int fluidInputSize = recipes.fluidInputSize;
		int fluidOutputSize = recipes.fluidOutputSize;
		
		List<FluidStack> fluidStackList = new ArrayList<FluidStack>();
		for (Fluid fluid : FluidRegistry.getRegisteredFluids().values()) fluidStackList.add(new FluidStack(fluid, 1000));
		
		List<String> fluidNameList = new ArrayList<String>();
		for (FluidStack fluidStack : fluidStackList) {
			String fluidName = fluidStack.getFluid().getName();
			if (recipes.isValidFluidInput(fluidStack) && !exceptions.contains(fluidName)) fluidNameList.add(fluidName);
		}
		
		List<List<String>> allowedFluidLists = new ArrayList<List<String>>();
		for (int i = 0; i < fluidInputSize; i++) allowedFluidLists.add(fluidNameList);
		for (int i = fluidInputSize; i < fluidInputSize + fluidOutputSize; i++) allowedFluidLists.add(null);
		
		return allowedFluidLists;
	}
	
	public static OreIngredient getOreStackFromItems(List<ItemStack> stackList, int stackSize) {
		if (stackList.isEmpty() || stackList == null) return null;
		String oreName = OreDictHelper.getOreNameFromStacks(stackList);
		if (oreName == "Unknown") return null;
		return new OreIngredient(oreName, stackSize);
	}
}
