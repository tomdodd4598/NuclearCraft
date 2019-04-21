package nc.recipe;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import nc.recipe.ingredient.ChanceFluidIngredient;
import nc.recipe.ingredient.ChanceItemIngredient;
import nc.recipe.ingredient.EmptyFluidIngredient;
import nc.recipe.ingredient.EmptyItemIngredient;
import nc.recipe.ingredient.FluidIngredient;
import nc.recipe.ingredient.IFluidIngredient;
import nc.recipe.ingredient.IItemIngredient;
import nc.recipe.ingredient.ItemIngredient;
import nc.recipe.ingredient.OreIngredient;
import nc.tile.internal.fluid.Tank;
import nc.util.FluidRegHelper;
import nc.util.ItemStackHelper;
import nc.util.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public abstract class AbstractRecipeHandler<T extends IRecipe> {
	
	private List<T> recipes = new ArrayList<T>();
	
	private static List<Class<?>> validItemInputs = Lists.newArrayList(IItemIngredient.class, ArrayList.class, String.class, Item.class, Block.class, ItemStack.class, ItemStack[].class);
	private static List<Class<?>> validFluidInputs = Lists.newArrayList(IFluidIngredient.class, ArrayList.class, String.class, Fluid.class, FluidStack.class, FluidStack[].class);
	private static List<Class<?>> validItemOutputs = Lists.newArrayList(IItemIngredient.class, String.class, Item.class, Block.class, ItemStack.class);
	private static List<Class<?>> validFluidOutputs = Lists.newArrayList(IFluidIngredient.class, String.class, Fluid.class, FluidStack.class);
	
	private static List<Class<?>> needItemAltering = Lists.newArrayList(Item.class, Block.class);
	private static List<Class<?>> needFluidAltering = Lists.newArrayList(Fluid.class);
	
	public static final List<Integer> INVALID = Lists.newArrayList(-1);
	
	public AbstractRecipeHandler() {}
	
	public abstract void addRecipes();
	
	public abstract String getRecipeName();
	
	public List<T> getRecipes() {
		return recipes;
	}
	
	public abstract void addRecipe(Object... objects);
	
	public @Nullable RecipeInfo<T> getRecipeInfoFromInputs(List<ItemStack> itemInputs, List<Tank> fluidInputs) {
		if (isFullNull(itemInputs, fluidInputs)) return null;
		
		for (T recipe : recipes) {
			RecipeMatchResult matchResult = recipe.matchInputs(itemInputs, fluidInputs);
			if (matchResult.matches()) return new RecipeInfo(recipe, matchResult);
		}
		return null;
	}
	
	public @Nullable RecipeInfo<T> getRecipeInfoFromOutputs(List<ItemStack> itemOutputs, List<Tank> fluidOutputs) {
		if (isFullNull(itemOutputs, fluidOutputs)) return null;
		
		for (T recipe : recipes) {
			RecipeMatchResult matchResult = recipe.matchOutputs(itemOutputs, fluidOutputs);
			if (matchResult.matches()) return new RecipeInfo(recipe, matchResult);
		}
		return null;
	}
	
	private static boolean isFullNull(List<ItemStack> items, List<Tank> tanks) {
		for (ItemStack item : items) {
			if (item != null && !item.isEmpty()) return false;
		}
		for (Tank tank : tanks) {
			if (tank.getFluid() != null) return false;
		}
		return true;
	}
	
	public @Nullable T getRecipeFromIngredients(List<IItemIngredient> itemIngredients, List<IFluidIngredient> fluidIngredients) {
		for (T recipe : recipes) {
			if (recipe.matchIngredients(itemIngredients, fluidIngredients).matches()) return recipe;
		}
		return null;
	}
	
	public @Nullable T getRecipeFromProducts(List<IItemIngredient> itemProducts, List<IFluidIngredient> fluidProducts) {
		for (T recipe : recipes) {
			if (recipe.matchProducts(itemProducts, fluidProducts).matches()) return recipe;
		}
		return null;
	}
	
	/*public List<IIngredient> getInputList(Object... outputs) {
		List outputList = ArrayHelper.asList(outputs);
		T recipe = getRecipeFromOutputs(outputList);
		List result = recipe != null ? recipe.inputs() : new ArrayList<IIngredient>();
		return result;
	}

	public List<IIngredient> getOutputList(Object... inputs) {
		List inputList = ArrayHelper.asList(inputs);
		T recipe = getRecipeFromInputs(inputList);
		List result = recipe != null ? recipe.outputs() : new ArrayList<IIngredient>();
		return result;
	}*/
	
	public boolean addRecipe(T recipe) {
		return (recipe != null) ? recipes.add(recipe) : false;
	}

	public boolean removeRecipe(T recipe) {
		return recipe != null ? recipes.remove(recipe) : false;
	}
	
	public void removeAllRecipes() {
		recipes.clear();
	}

	public static void addValidItemInput(Class itemInputType) {
		validItemInputs.add(itemInputType);
	}
	
	public static void addValidFluidInput(Class fluidInputType) {
		validFluidInputs.add(fluidInputType);
	}
	
	public static void addValidItemOutput(Class itemOutputType) {
		validItemOutputs.add(itemOutputType);
	}
	
	public static void addValidFluidOutput(Class fluidOutputType) {
		validFluidOutputs.add(fluidOutputType);
	}
	
	public static boolean isValidItemInputType(Object itemInput) {
		for (Class<?> itemInputType : validItemInputs) {
			if (itemInput instanceof ArrayList && itemInputType == ArrayList.class) {
				ArrayList list = (ArrayList) itemInput;
				for (Object obj : list) if (isValidItemInputType(obj)) return true;
			} else if (itemInputType.isInstance(itemInput)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isValidFluidInputType(Object fluidInput) {
		for (Class<?> fluidInputType : validFluidInputs) {
			if (fluidInput instanceof ArrayList && fluidInputType == ArrayList.class) {
				ArrayList list = (ArrayList) fluidInput;
				for (Object obj : list) if (isValidFluidInputType(obj)) return true;
			} else if (fluidInputType.isInstance(fluidInput)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isValidItemOutputType(Object itemOutput) {
		for (Class<?> itemOutputType : validItemOutputs) {
			if (itemOutputType.isInstance(itemOutput)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isValidFluidOutputType(Object fluidOutput) {
		for (Class<?> fluidOutputType : validFluidOutputs) {
			if (fluidOutputType.isInstance(fluidOutput)) {
				return true;
			}
		}
		return false;
	}

	public static boolean requiresItemFixing(Object object) {
		for (Class<?> objectType : needItemAltering) {
			if (objectType.isInstance(object)) return true;
		}
		return false;
	}
	
	public static boolean requiresFluidFixing(Object object) {
		for (Class<?> objectType : needFluidAltering) {
			if (objectType.isInstance(object)) return true;
		}
		return false;
	}
	
	public boolean isValidItemInput(ItemStack stack) {
		for (T recipe : recipes) {
			for (IItemIngredient input : recipe.itemIngredients()) {
				if (input.match(stack, IngredientSorption.NEUTRAL).matches()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isValidFluidInput(FluidStack stack) {
		for (T recipe : recipes) {
			for (IFluidIngredient input : recipe.fluidIngredients()) {
				if (input.match(stack, IngredientSorption.NEUTRAL).matches()) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isValidItemOutput(ItemStack stack) {
		for (T recipe : recipes) {
			for (IItemIngredient output : recipe.itemProducts()) {
				if (output.match(stack, IngredientSorption.OUTPUT).matches()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isValidFluidOutput(FluidStack stack) {
		for (T recipe : recipes) {
			for (IFluidIngredient output : recipe.fluidProducts()) {
				if (output.match(stack, IngredientSorption.OUTPUT).matches()) {
					return true;
				}
			}
		}
		return false;
	}
	
	/** Smart item insertion */
	public boolean isValidItemInput(ItemStack stack, ItemStack slotStack, List<ItemStack> otherInputs) {
		if (otherInputs.isEmpty() || (stack.isItemEqual(slotStack) && ItemStackHelper.areItemStackTagsEqual(stack, slotStack))) {
			return isValidItemInput(stack);
		}
		
		List<ItemStack> otherStacks = new ArrayList<ItemStack>();
		for (ItemStack otherInput : otherInputs) {
			if (!otherInput.isEmpty()) otherStacks.add(otherInput);
		}
		if (otherStacks.isEmpty()) return isValidItemInput(stack);
		
		List<ItemStack> allStacks = Lists.newArrayList(stack);
		allStacks.addAll(otherStacks);
		
		List<T> recipeList = new ArrayList(recipes);
		recipeLoop: for (T recipe : recipes) {
			objLoop: for (ItemStack obj : allStacks) {
				for (IItemIngredient input : recipe.itemIngredients()) {
					if (input.match(obj, IngredientSorption.NEUTRAL).matches()) continue objLoop;
				}
				recipeList.remove(recipe);
				continue recipeLoop;
			}
		}
		
		for (T recipe : recipeList) {
			for (IItemIngredient input : recipe.itemIngredients()) {
				if (input.match(stack, IngredientSorption.NEUTRAL).matches()) {
					for (ItemStack other : otherStacks) {
						if (input.match(other, IngredientSorption.NEUTRAL).matches()) return false;
					}
					return true;
				}
			}
		}
		
		return false;
	}
	
	// Stacks
	
	public static OreIngredient oreStack(String oreType, int stackSize) {
		if (!OreDictHelper.oreExists(oreType)) return null;
		return new OreIngredient(oreType, stackSize);
	}
	
	public static FluidIngredient fluidStack(String fluidName, int stackSize) {
		if (!FluidRegHelper.fluidExists(fluidName)) return null;
		return new FluidIngredient(fluidName, stackSize);
	}
	
	public static List<OreIngredient> oreStackList(List<String> oreTypes, int stackSize) {
		List<OreIngredient> oreStackList = new ArrayList<OreIngredient>();
		for (String oreType : oreTypes) if (oreStack(oreType, stackSize) != null) oreStackList.add(oreStack(oreType, stackSize));
		return oreStackList;
	}
	
	public static List<FluidIngredient> fluidStackList(List<String> fluidNames, int stackSize) {
		List<FluidIngredient> fluidStackList = new ArrayList<FluidIngredient>();
		for (String fluidName : fluidNames) if (fluidStack(fluidName, stackSize) != null) fluidStackList.add(fluidStack(fluidName, stackSize));
		return fluidStackList;
	}
	
	public static EmptyItemIngredient emptyItemStack() {
		return new EmptyItemIngredient();
	}
	
	public static EmptyFluidIngredient emptyFluidStack() {
		return new EmptyFluidIngredient();
	}
	
	public static ChanceItemIngredient chanceItemStack(ItemStack stack, int chancePercent) {
		if (stack == null) return null;
		return new ChanceItemIngredient(new ItemIngredient(stack), chancePercent);
	}
	
	public static ChanceItemIngredient chanceItemStack(ItemStack stack, int chancePercent, int minStackSize) {
		if (stack == null) return null;
		return new ChanceItemIngredient(new ItemIngredient(stack), chancePercent, minStackSize);
	}
	
	public static ChanceItemIngredient chanceOreStack(String oreType, int stackSize, int chancePercent) {
		if (!OreDictHelper.oreExists(oreType)) return null;
		return new ChanceItemIngredient(oreStack(oreType, stackSize), chancePercent);
	}
	
	public static ChanceItemIngredient chanceOreStack(String oreType, int stackSize, int chancePercent, int minStackSize) {
		if (!OreDictHelper.oreExists(oreType)) return null;
		return new ChanceItemIngredient(oreStack(oreType, stackSize), chancePercent, minStackSize);
	}
	
	public static ChanceFluidIngredient chanceFluidStack(String fluidName, int stackSize, int chancePercent, int stackDiff) {
		if (!FluidRegHelper.fluidExists(fluidName)) return null;
		return new ChanceFluidIngredient(fluidStack(fluidName, stackSize), chancePercent, stackDiff);
	}
	
	public static ChanceFluidIngredient chanceFluidStack(String fluidName, int stackSize, int chancePercent, int stackDiff, int minStackSize) {
		if (!FluidRegHelper.fluidExists(fluidName)) return null;
		return new ChanceFluidIngredient(fluidStack(fluidName, stackSize), chancePercent, stackDiff, minStackSize);
	}
	
	public static List<ChanceItemIngredient> chanceOreStackList(List<String> oreTypes, int stackSize, int chancePercent) {
		List<ChanceItemIngredient> oreStackList = new ArrayList<ChanceItemIngredient>();
		for (String oreType : oreTypes) if (chanceOreStack(oreType, stackSize, chancePercent) != null) oreStackList.add(chanceOreStack(oreType, stackSize, chancePercent));
		return oreStackList;
	}
	
	public static List<ChanceItemIngredient> chanceOreStackList(List<String> oreTypes, int stackSize, int chancePercent, int minStackSize) {
		List<ChanceItemIngredient> oreStackList = new ArrayList<ChanceItemIngredient>();
		for (String oreType : oreTypes) if (chanceOreStack(oreType, stackSize, chancePercent, minStackSize) != null) oreStackList.add(chanceOreStack(oreType, stackSize, chancePercent, minStackSize));
		return oreStackList;
	}
	
	public static List<ChanceFluidIngredient> chanceFluidStackList(List<String> fluidNames, int stackSize, int chancePercent, int stackDiff) {
		List<ChanceFluidIngredient> fluidStackList = new ArrayList<ChanceFluidIngredient>();
		for (String fluidName : fluidNames) if (chanceFluidStack(fluidName, stackSize, chancePercent, stackDiff) != null) fluidStackList.add(chanceFluidStack(fluidName, stackSize, chancePercent, stackDiff));
		return fluidStackList;
	}
	
	public static List<ChanceFluidIngredient> chanceFluidStackList(List<String> fluidNames, int stackSize, int chancePercent, int stackDiff, int minStackSize) {
		List<ChanceFluidIngredient> fluidStackList = new ArrayList<ChanceFluidIngredient>();
		for (String fluidName : fluidNames) if (chanceFluidStack(fluidName, stackSize, chancePercent, stackDiff, minStackSize) != null) fluidStackList.add(chanceFluidStack(fluidName, stackSize, chancePercent, stackDiff, minStackSize));
		return fluidStackList;
	}
}
