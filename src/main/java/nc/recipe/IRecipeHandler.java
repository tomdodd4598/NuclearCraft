package nc.recipe;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import nc.ModCheck;
import nc.tile.internal.fluid.Tank;
import nc.util.FluidRegHelper;
import nc.util.OreDictHelper;
import nc.util.RecipeHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public abstract class IRecipeHandler<T extends IRecipe> {
	
	public List<T> recipes = new ArrayList<T>();
	public List<Class<?>> validItemInputs = Lists.newArrayList(IItemIngredient.class, ArrayList.class, String.class, Item.class, Block.class, ItemStack.class, ItemStack[].class);
	public List<Class<?>> validFluidInputs = Lists.newArrayList(IFluidIngredient.class, ArrayList.class, String.class, Fluid.class, FluidStack.class, FluidStack[].class);
	public List<Class<?>> validItemOutputs = Lists.newArrayList(IItemIngredient.class, String.class, Item.class, Block.class, ItemStack.class);
	public List<Class<?>> validFluidOutputs = Lists.newArrayList(IFluidIngredient.class, String.class, Fluid.class, FluidStack.class);
	
	public List<Class<?>> needItemAltering = Lists.newArrayList(Item.class, Block.class);
	public List<Class<?>> needFluidAltering = Lists.newArrayList(Fluid.class);
	
	public static final List<Integer> INVALID = Lists.newArrayList(-1);
	
	public IRecipeHandler() {}
	
	public abstract void addRecipes();
	
	public abstract String getRecipeName();
	
	public List<T> getRecipes() {
		return recipes;
	}
	
	public abstract void addRecipe(Object... objects);
	
	@Nullable
	public T getRecipeFromInputs(List<ItemStack> itemInputs, List<Tank> fluidInputs) {
		for (T recipe : recipes) {
			if (recipe.matchingInputs(itemInputs, fluidInputs)) return recipe;
		}
		return null;
	}

	@Nullable
	public T getRecipeFromOutputs(List<ItemStack> itemOutputs, List<Tank> fluidOutputs) {
		for (T recipe : recipes) {
			if (recipe.matchingOutputs(itemOutputs, fluidOutputs)) return recipe;
		}
		return null;
	}
	
	@Nullable
	public T getRecipeFromIngredients(List<IItemIngredient> itemIngredients, List<IFluidIngredient> fluidIngredients) {
		for (T recipe : recipes) {
			if (recipe.matchingIngredients(itemIngredients, fluidIngredients)) return recipe;
		}
		return null;
	}
	
	@Nullable
	public T getRecipeFromProducts(List<IItemIngredient> itemProducts, List<IFluidIngredient> fluidProducts) {
		for (T recipe : recipes) {
			if (recipe.matchingProducts(itemProducts, fluidProducts)) return recipe;
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

	public void addValidItemInput(Class itemInputType) {
		validItemInputs.add(itemInputType);
	}
	
	public void addValidFluidInput(Class fluidInputType) {
		validFluidInputs.add(fluidInputType);
	}
	
	public void addValidItemOutput(Class itemOutputType) {
		validItemOutputs.add(itemOutputType);
	}
	
	public void addValidFluidOutput(Class fluidOutputType) {
		validFluidOutputs.add(fluidOutputType);
	}

	protected boolean isValidItemInputType(Object itemInput) {
		for (Class<?> itemInputType : validItemInputs) {
			if (itemInput instanceof ArrayList && itemInputType == ArrayList.class) {
				ArrayList list = (ArrayList) itemInput;
				if (!list.isEmpty() && isValidItemInputType(list.get(0))) {
					return true;
				}
			} else if (itemInputType.isInstance(itemInput)) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean isValidFluidInputType(Object fluidInput) {
		for (Class<?> fluidInputType : validFluidInputs) {
			if (fluidInput instanceof ArrayList && fluidInputType == ArrayList.class) {
				ArrayList list = (ArrayList) fluidInput;
				if (!list.isEmpty() && isValidFluidInputType(list.get(0))) {
					return true;
				}
			} else if (fluidInputType.isInstance(fluidInput)) {
				return true;
			}
		}
		return false;
	}

	protected boolean isValidItemOutputType(Object itemOutput) {
		for (Class<?> itemOutputType : validItemOutputs) {
			if (itemOutputType.isInstance(itemOutput)) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean isValidFluidOutputType(Object fluidOutput) {
		for (Class<?> fluidOutputType : validFluidOutputs) {
			if (fluidOutputType.isInstance(fluidOutput)) {
				return true;
			}
		}
		return false;
	}

	protected boolean requiresItemFixing(Object object) {
		for (Class<?> objectType : needItemAltering) {
			if (objectType.isInstance(object)) return true;
		}
		return false;
	}
	
	protected boolean requiresFluidFixing(Object object) {
		for (Class<?> objectType : needFluidAltering) {
			if (objectType.isInstance(object)) return true;
		}
		return false;
	}

	@Nullable
	public IItemIngredient buildItemIngredient(Object object) {
		if (requiresItemFixing(object)) {
			object = RecipeHelper.fixItemStack(object);
		}
		if (object instanceof IItemIngredient) {
			return (IItemIngredient) object;
		} else if (object instanceof ArrayList) {
			ArrayList list = (ArrayList) object;
			List<IItemIngredient> buildList = new ArrayList<IItemIngredient>();
			if (!list.isEmpty()) {
				for (Object listObject : list) {
					if (listObject instanceof IItemIngredient) {
						buildList.add((IItemIngredient)listObject);
					}
					else if (listObject != null) {
						IItemIngredient recipeObject = buildItemIngredient(listObject);
						if (recipeObject != null) buildList.add(recipeObject);
					}
				}
				if (buildList.isEmpty()) return null;
				return new RecipeItemStackArray(buildList);
			} else {
				return null;
			}
		} else if (object instanceof String) {
			return RecipeHelper.oreStackFromString((String) object);
		}
		if (object instanceof ItemStack) {
			return new RecipeItemStack((ItemStack) object);
		}
		return null;
	}
	
	@Nullable
	public IFluidIngredient buildFluidIngredient(Object object) {
		if (requiresFluidFixing(object)) {
			object = RecipeHelper.fixFluidStack(object);
		}
		if (ModCheck.mekanismLoaded() && object instanceof RecipeFluidStack) {
			return buildFluidIngredient(mekanismFluidStackList((RecipeFluidStack)object));
		}
		if (object instanceof IFluidIngredient) {
			return (IFluidIngredient) object;
		} else if (object instanceof ArrayList) {
			ArrayList list = (ArrayList) object;
			List<IFluidIngredient> buildList = new ArrayList<IFluidIngredient>();
			if (!list.isEmpty()) {
				for (Object listObject : list) {
					if (listObject instanceof IFluidIngredient) {
						buildList.add((IFluidIngredient)listObject);
					}
					else if (listObject != null) {
						IFluidIngredient recipeObject = buildFluidIngredient(listObject);
						if (recipeObject != null) buildList.add(recipeObject);
					}
				}
				if (buildList.isEmpty()) return null;
				return new RecipeFluidStackArray(buildList);
			} else {
				return null;
			}
		} else if (object instanceof String) {
			return RecipeHelper.fluidStackFromString((String) object);
		}
		if (object instanceof FluidStack) {
			return new RecipeFluidStack((FluidStack) object);
		}
		return null;
	}
	
	public boolean isValidItemInput(ItemStack stack) {
		for (T recipe : recipes) {
			for (IItemIngredient input : recipe.itemIngredients()) {
				if (input.matches(stack, SorptionType.NEUTRAL)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isValidFluidInput(FluidStack stack) {
		for (T recipe : recipes) {
			for (IFluidIngredient input : recipe.fluidIngredients()) {
				if (input.matches(stack, SorptionType.NEUTRAL)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isValidItemOutput(ItemStack stack) {
		for (T recipe : recipes) {
			for (IItemIngredient output : recipe.itemProducts()) {
				if (output.matches(stack, SorptionType.OUTPUT)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isValidItemOutput(FluidStack stack) {
		for (T recipe : recipes) {
			for (IFluidIngredient output : recipe.fluidProducts()) {
				if (output.matches(stack, SorptionType.OUTPUT)) {
					return true;
				}
			}
		}
		return false;
	}
	
	// Smart item insertion
	public boolean isValidItemInput(ItemStack stack, ItemStack slotStack, List<ItemStack> otherInputs) {
		if (otherInputs.isEmpty() || (stack.isItemEqual(slotStack) && ItemStack.areItemStackTagsEqual(stack, slotStack))) {
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
					if (input.matches(obj, SorptionType.NEUTRAL)) continue objLoop;
				}
				recipeList.remove(recipe);
				continue recipeLoop;
			}
		}
		
		for (T recipe : recipeList) {
			for (IItemIngredient input : recipe.itemIngredients()) {
				if (input.matches(stack, SorptionType.NEUTRAL)) {
					for (ItemStack other : otherStacks) {
						if (input.matches(other, SorptionType.NEUTRAL)) return false;
					}
					return true;
				}
			}
		}
		
		return false;
	}
	
	// Stacks
	
	public RecipeOreStack oreStack(String oreType, int stackSize) {
		if (!OreDictHelper.oreExists(oreType)) return null;
		return new RecipeOreStack(oreType, stackSize);
	}
	
	public RecipeFluidStack fluidStack(String fluidName, int stackSize) {
		if (!FluidRegHelper.fluidExists(fluidName)) return null;
		return new RecipeFluidStack(fluidName, stackSize);
	}
	
	public List<RecipeOreStack> oreStackList(List<String> oreTypes, int stackSize) {
		List<RecipeOreStack> oreStackList = new ArrayList<RecipeOreStack>();
		for (String oreType : oreTypes) if (oreStack(oreType, stackSize) != null) oreStackList.add(oreStack(oreType, stackSize));
		return oreStackList;
	}
	
	public List<RecipeFluidStack> fluidStackList(List<String> fluidNames, int stackSize) {
		List<RecipeFluidStack> fluidStackList = new ArrayList<RecipeFluidStack>();
		for (String fluidName : fluidNames) if (fluidStack(fluidName, stackSize) != null) fluidStackList.add(fluidStack(fluidName, stackSize));
		return fluidStackList;
	}
	
	public RecipeEmptyItemStack emptyItemStack() {
		return new RecipeEmptyItemStack();
	}
	
	public RecipeEmptyFluidStack emptyFluidStack() {
		return new RecipeEmptyFluidStack();
	}
	
	public RecipeChanceItemStack chanceItemStack(ItemStack stack, int chancePercent) {
		if (stack == null) return null;
		return new RecipeChanceItemStack(new RecipeItemStack(stack), chancePercent);
	}
	
	public RecipeChanceItemStack chanceItemStack(ItemStack stack, int chancePercent, int minStackSize) {
		if (stack == null) return null;
		return new RecipeChanceItemStack(new RecipeItemStack(stack), chancePercent, minStackSize);
	}
	
	public RecipeChanceItemStack chanceOreStack(String oreType, int stackSize, int chancePercent) {
		if (!OreDictHelper.oreExists(oreType)) return null;
		return new RecipeChanceItemStack(oreStack(oreType, stackSize), chancePercent);
	}
	
	public RecipeChanceItemStack chanceOreStack(String oreType, int stackSize, int chancePercent, int minStackSize) {
		if (!OreDictHelper.oreExists(oreType)) return null;
		return new RecipeChanceItemStack(oreStack(oreType, stackSize), chancePercent, minStackSize);
	}
	
	public RecipeChanceFluidStack chanceFluidStack(String fluidName, int stackSize, int chancePercent, int stackDiff) {
		if (!FluidRegHelper.fluidExists(fluidName)) return null;
		return new RecipeChanceFluidStack(fluidStack(fluidName, stackSize), chancePercent, stackDiff);
	}
	
	public RecipeChanceFluidStack chanceFluidStack(String fluidName, int stackSize, int chancePercent, int stackDiff, int minStackSize) {
		if (!FluidRegHelper.fluidExists(fluidName)) return null;
		return new RecipeChanceFluidStack(fluidStack(fluidName, stackSize), chancePercent, stackDiff, minStackSize);
	}
	
	public List<RecipeChanceItemStack> chanceOreStackList(List<String> oreTypes, int stackSize, int chancePercent) {
		List<RecipeChanceItemStack> oreStackList = new ArrayList<RecipeChanceItemStack>();
		for (String oreType : oreTypes) if (chanceOreStack(oreType, stackSize, chancePercent) != null) oreStackList.add(chanceOreStack(oreType, stackSize, chancePercent));
		return oreStackList;
	}
	
	public List<RecipeChanceItemStack> chanceOreStackList(List<String> oreTypes, int stackSize, int chancePercent, int minStackSize) {
		List<RecipeChanceItemStack> oreStackList = new ArrayList<RecipeChanceItemStack>();
		for (String oreType : oreTypes) if (chanceOreStack(oreType, stackSize, chancePercent, minStackSize) != null) oreStackList.add(chanceOreStack(oreType, stackSize, chancePercent, minStackSize));
		return oreStackList;
	}
	
	public List<RecipeChanceFluidStack> chanceFluidStackList(List<String> fluidNames, int stackSize, int chancePercent, int stackDiff) {
		List<RecipeChanceFluidStack> fluidStackList = new ArrayList<RecipeChanceFluidStack>();
		for (String fluidName : fluidNames) if (chanceFluidStack(fluidName, stackSize, chancePercent, stackDiff) != null) fluidStackList.add(chanceFluidStack(fluidName, stackSize, chancePercent, stackDiff));
		return fluidStackList;
	}
	
	public List<RecipeChanceFluidStack> chanceFluidStackList(List<String> fluidNames, int stackSize, int chancePercent, int stackDiff, int minStackSize) {
		List<RecipeChanceFluidStack> fluidStackList = new ArrayList<RecipeChanceFluidStack>();
		for (String fluidName : fluidNames) if (chanceFluidStack(fluidName, stackSize, chancePercent, stackDiff, minStackSize) != null) fluidStackList.add(chanceFluidStack(fluidName, stackSize, chancePercent, stackDiff, minStackSize));
		return fluidStackList;
	}
	
	public List<RecipeFluidStack> mekanismFluidStackList(RecipeFluidStack stack) {
		return stack.fluidName.equals("helium") ? Lists.newArrayList(stack) : fluidStackList(Lists.newArrayList(stack.fluidName, "liquid" + stack.fluidName), stack.amount);
	}
}
