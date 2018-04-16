package nc.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import nc.ModCheck;
import nc.util.ArrayHelper;
import nc.util.NCUtil;
import nc.util.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public abstract class RecipeMethods<T extends IRecipe> implements IRecipeGetter<T> {
	
	public ArrayList<T> recipes = new ArrayList();
	public ArrayList<Class<?>> validInputs = Lists.newArrayList(IIngredient.class, ArrayList.class, String.class, Item.class, Block.class, ItemStack.class, ItemStack[].class, Fluid.class, FluidStack.class);
	public ArrayList<Class<?>> validOutputs = Lists.newArrayList(IIngredient.class, String.class, Item.class, Block.class, ItemStack.class, Fluid.class, FluidStack.class);
	
	public ArrayList<Class<?>> needAltering = Lists.newArrayList(Item.class, Block.class, Fluid.class);
	
	public static final int[] INVALID = new int[] {-1};
	
	public RecipeMethods() {}
	
	public abstract void addRecipes();
	
	@Override
	public abstract String getRecipeName();

	@Override
	public ArrayList<T> getRecipes() {
		return recipes;
	}
	
	@Nullable
	public T getRecipeFromInputs(Object[] inputs) {
		for (T recipe : recipes) {
			if (recipe.matchingInputs(inputs)) return recipe;
		}
		return null;
	}

	@Nullable
	public T getRecipeFromOutputs(Object[] outputs) {
		for (T recipe : recipes) {
			if (recipe.matchingOutputs(outputs)) return recipe;
		}
		return null;
	}
	
	public ArrayList<IIngredient> getInputList(Object... outputs) {
		T recipe = getRecipeFromOutputs(outputs);
		ArrayList result = recipe != null ? recipe.inputs() : new ArrayList<IIngredient>();
		return result;
	}

	public ArrayList<IIngredient> getOutputList(Object... inputs) {
		T recipe = getRecipeFromInputs(inputs);
		ArrayList result = recipe != null ? recipe.outputs() : new ArrayList<IIngredient>();
		return result;
	}
	
	public static boolean containsIngredient(ArrayList<IIngredient> list, IIngredient ingredient) {
		for (IIngredient i : list) {
			if (i == null) continue;
			if (i.matches(ingredient, SorptionType.NEUTRAL)) return true;
		}
		return false;
	}

	public boolean addRecipe(T recipe) {
		return (recipe != null) ? recipes.add(recipe) : false;
	}

	public boolean removeRecipe(T recipe) {
		return recipe != null ? recipes.remove(recipe) : false;
	}

	public void addValidInput(Class inputTypes) {
		validInputs.add(inputTypes);
	}

	private boolean isValidInputType(Object input) {
		for (Class<?> inputType : validInputs) {
			if (input instanceof ArrayList && inputType == ArrayList.class) {
				ArrayList list = (ArrayList) input;
				if (!list.isEmpty() && isValidInputType(list.get(0))) {
					return true;
				}
			} else if (inputType.isInstance(input)) {
				return true;
			}
		}
		return false;
	}

	private boolean isValidOutputType(Object output) {
		for (Class<?> outputType : validOutputs) {
			if (outputType.isInstance(output)) {
				return true;
			}
		}
		return false;
	}

	private boolean requiresAdjustment(Object object) {
		for (Class<?> objectType : needAltering) {
			if (objectType.isInstance(object)) {
				return true;
			}
		}
		return false;
	}

	/**builds a recipe object, can be null, this can be overridden if needed*/
	@Nullable
	public IIngredient buildRecipeObject(Object object) {
		if (requiresAdjustment(object)) {
			object = adjustObject(object);
		}
		if (ModCheck.mekanismLoaded() && object instanceof RecipeOreStack) {
			if (((RecipeOreStack)object).isFluid) return buildRecipeObject(mekanismFluidStackList((RecipeOreStack)object));
		}
		if (object instanceof IIngredient) {
			return (IIngredient) object;
		} else if (object instanceof ArrayList) {
			ArrayList list = (ArrayList) object;
			ArrayList<IIngredient> buildList = new ArrayList();
			if (!list.isEmpty()) {
				for (Object listObject : list) {
					if (listObject instanceof IIngredient) {
						buildList.add((IIngredient)listObject);
					}
					else if (listObject != null) {
						IIngredient recipeObject = buildRecipeObject(listObject);
						if (recipeObject != null) buildList.add(recipeObject);
					}
				}
				if (buildList.isEmpty()) return null;
				return new RecipeStackArray(buildList);
			} else {
				return null;
			}
		} else if (object instanceof String) {
			if (OreDictHelper.exists((String) object, StackType.ITEM)) return new RecipeOreStack((String) object, StackType.ITEM, 1);
			else if (OreDictHelper.exists((String) object, StackType.FLUID)) return new RecipeOreStack((String) object, StackType.FLUID, 1);
			else if (OreDictHelper.exists((String) object, StackType.UNSPECIFIED)) return new RecipeOreStack((String) object, StackType.UNSPECIFIED, 1);
			return null;
		}
		if (object instanceof ItemStack) {
			return new RecipeStack((ItemStack) object);
		}
		if (object instanceof FluidStack) {
			return new RecipeStack((FluidStack) object);
		}
		return null;
	}
	
	public void addRecipe(ArrayList inputList, ArrayList outputList, ArrayList extrasList, boolean shapeless) {
		addRecipe(buildDefaultRecipe(inputList, outputList, extrasList, shapeless));
	}
	
	@Nullable
	public T buildDefaultRecipe(ArrayList inputs, ArrayList outputs, ArrayList additionals, boolean shapeless) {
		ArrayList<IIngredient> recipeInputs = Lists.<IIngredient>newArrayList();
		ArrayList<IIngredient> recipeOutputs = Lists.<IIngredient>newArrayList();
		for (Object obj : inputs) {
			if (obj != null && isValidInputType(obj)) {
				IIngredient input = buildRecipeObject(obj);
				if (input == null)
					return null;
				recipeInputs.add(input);
			} else {
				if (obj != null) NCUtil.getLogger().info(getRecipeName() + " - a recipe was removed because the input " + obj.toString() + " is invalid!");
				return null;
			}
		}
		for (Object obj : outputs) {
			if (obj != null && isValidOutputType(obj)) {
				IIngredient output = buildRecipeObject(obj);
				if (output == null)
					return null;
				recipeOutputs.add(output);
			} else {
				if (obj != null) NCUtil.getLogger().info(getRecipeName() + " - a recipe was removed because the output " + obj.toString() + " is invalid!");
				return null;
			}
		}
		if (!isValidRecipe(recipeInputs, recipeOutputs)) {
			NCUtil.getLogger().info(getRecipeName() + " - a recipe was removed: " + recipeInputs.toString() + " -> " + recipeOutputs.toString());
		}
		return buildRecipe(recipeInputs, recipeOutputs, additionals, shapeless);
	}

	public boolean isValidRecipe(ArrayList<IIngredient> recipeInputs, ArrayList<IIngredient> recipeOutputs) {
		return recipeInputs.size() > 0 && recipeOutputs.size() > 0;
	}

	public T buildRecipe(ArrayList<IIngredient> inputList, ArrayList<IIngredient> outputList, ArrayList extrasList, boolean shapeless) {
		return (T) new BaseRecipe(inputList, outputList, extrasList, shapeless);
	}

	public boolean isValidOutput(Object object) {
		for (T recipe : recipes) {
			for (IIngredient output : recipe.outputs()) {
				if (output.matches(object, SorptionType.OUTPUT)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isValidInput(Object object, Object... objects) {
		List others = new ArrayList(Arrays.asList(objects));
		others.removeAll(Collections.singleton(null));
		others.removeAll(Collections.singleton(ItemStack.EMPTY));
		if (others.size() <= 0) return isValidInput(object);
		ArrayList<ArrayList<T>> recipeLists = Lists.<ArrayList<T>>newArrayList();
		
		ArrayList<T> startingRecipeList = new ArrayList(recipes);
		recipeLoop: for (T recipe : recipes) {
			for (IIngredient input : recipe.inputs()) {
				if (input.matches(object, SorptionType.NEUTRAL)) continue recipeLoop;
			}
			startingRecipeList.remove(recipe);
		}
		
		recipeLists.add(new ArrayList(startingRecipeList));
		
		for (int i = 0; i < others.size(); i++) {
			if (recipeLists.get(i).isEmpty()) return false;
			
			recipeLists.add(new ArrayList(recipeLists.get(i)));
			recipeLoop: for (T recipe : recipeLists.get(i)) {
				for (IIngredient input : recipe.inputs()) {
					if (input.matches(others.get(i), SorptionType.NEUTRAL)) continue recipeLoop;
				}
				recipeLists.get(i + 1).remove(recipe);
			}
		}
		
		return !recipeLists.get(others.size()).isEmpty();
	}
	
	public boolean isValidInput(Object object) {
		for (T recipe : recipes) {
			for (IIngredient input : recipe.inputs()) {
				if (input.matches(object, SorptionType.NEUTRAL)) {
					return true;
				}
			}
		}
		return false;
	}
	
	protected static String[][] validFluids(BaseRecipeHandler recipes, String... exceptions) {
		int fluidInputSize = recipes.inputSizeFluid;
		int fluidOutputSize = recipes.outputSizeFluid;
		
		ArrayList<FluidStack> fluidStackList = new ArrayList<FluidStack>();
		for (Fluid fluid : FluidRegistry.getRegisteredFluids().values()) fluidStackList.add(new FluidStack(fluid, 1000));
		
		ArrayList<String> exceptionsList = Lists.newArrayList(exceptions);
		
		ArrayList<String> fluidNameList = new ArrayList<String>();
		for (FluidStack fluidStack : fluidStackList) {
			String fluidName = fluidStack.getFluid().getName();
			if (recipes.isValidInput(fluidStack) && !exceptionsList.contains(fluidName)) fluidNameList.add(fluidName);
		}
		
		String[] allowedFluidArray = ArrayHelper.asStringArray(fluidNameList);
		
		String[][] allowedFluidArrays = new String[fluidInputSize + fluidOutputSize][];
		for (int i = 0; i < fluidInputSize; i++) allowedFluidArrays[i] = allowedFluidArray;
		for (int i = fluidInputSize; i < fluidInputSize + fluidOutputSize; i++) allowedFluidArrays[i] = new String[] {};
		
		return allowedFluidArrays;
	}
	
	public static String[][] validFluids(NCRecipes.Type recipeType, String... exceptions) {
		return validFluids(recipeType.getRecipeHandler(), exceptions);
	}
	
	public static Object adjustObject(Object object) {
		return fixStack(object);
	}
	
	public static Object fixStack(Object object) {
		if (object instanceof FluidStack) {
			FluidStack fluidstack = ((FluidStack) object).copy();
			if (fluidstack.amount == 0) {
				fluidstack.amount = 1000;
			}
			return fluidstack;
		} else if (object instanceof Fluid) {
			return new FluidStack((Fluid) object, 1000);
		} else if (object instanceof ItemStack) {
			ItemStack stack = ((ItemStack) object).copy();
			if (stack.getCount() == 0) {
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

	public static ArrayList<List<Object>> getIngredientLists(ArrayList<IIngredient> ingredientList) {
		ArrayList<List<Object>> values = new ArrayList<List<Object>>();
		ingredientList.forEach(object -> values.add(object.getIngredientList()));
		return values;
	}
	
	public static List<Object> getIngredientList(List<IIngredient> list) {
		List<Object> values = new ArrayList<Object>();
		list.forEach(object -> values.add(object.getIngredient()));
		return values;
	}
	
	@Nullable
	public static Object getIngredientFromList(ArrayList<IIngredient> list, int pos) {
		if (!list.isEmpty() && pos < list.size()) {
			IIngredient object = list.get(pos);
			if (object instanceof IRecipeStack) {
				return ((IRecipeStack) object).getOutputStack();
			}
		}
		return null;
	}

	public static boolean matchingIngredients(SorptionType sorption, ArrayList<IIngredient> ingredients, boolean shapeless, Object[] objects) {
		ArrayList<IIngredient> matches = (ArrayList<IIngredient>) ingredients.clone();
		if (ingredients.size() != objects.length) {
			return false;
		}
		int pos = -1;
		inputs: for (Object obj : objects) {
			if (obj == null) {
				return false;
			}
			pos++;
			if (shapeless) {
				for (IIngredient ingredient : (ArrayList<IIngredient>) matches.clone()) {
					if (ingredient.matches(obj, sorption)) {
						matches.remove(ingredient);
						continue inputs;
					}
				}
			} else if (ingredients.get(pos).matches(obj, sorption)) {
				matches.remove(ingredients.get(pos));
				continue inputs;
			}
			return false;
		}
		return true;
	}
	
	public static ArrayList<String> getIngredientNames(ArrayList<IIngredient> ingredientList) {
		ArrayList<String> ingredientNames = new ArrayList<String>();
		for (IIngredient ingredient : ingredientList) {
			if (ingredient == null) ingredientNames.add("null");
			else ingredientNames.add(ingredient.getStackSize() + " x " + ingredient.getIngredientName());
		}
		return ingredientNames;
	}
	
	public static ArrayList<String> buildIngredientNames(ArrayList ingredientList, NCRecipes.Type recipeType) {
		ArrayList<String> ingredientNames = new ArrayList<String>();
		for (Object obj : ingredientList) {
			if (obj == null) ingredientNames.add("null");
			else {
				if (!(obj instanceof IIngredient)) obj = recipeType.getRecipeHandler().buildRecipeObject(obj);
				IIngredient ingredient = (IIngredient) obj;
				ingredientNames.add(ingredient.getStackSize() + " x " + ingredient.getIngredientName());
			}
		}
		return ingredientNames;
	}
	
	public static RecipeOreStack getOreStackFromItems(ArrayList<ItemStack> stackList, int stackSize) {
		if (stackList.isEmpty() || stackList == null) return null;
		String oreName = OreDictHelper.getOreNameFromStacks(stackList);
		if (oreName == "Unknown") return null;
		return new RecipeOreStack(oreName, StackType.ITEM, stackSize);
	}
	
	public RecipeOreStack oreStack(String oreType, int stackSize) {
		if (!OreDictHelper.exists(oreType, StackType.ITEM)) {
			//NCUtil.getLogger().info(getRecipeName() + " - an item ore dict stack of '" + oreType + "' is invalid!");
			return null;
		}
		return new RecipeOreStack(oreType, StackType.ITEM, stackSize);
	}
	
	public RecipeOreStack fluidStack(String oreType, int stackSize) {
		if (!OreDictHelper.exists(oreType, StackType.FLUID)) {
			//NCUtil.getLogger().info(getRecipeName() + " - a fluid ore dict stack of '" + oreType + "' is invalid!");
			return null;
		}
		return new RecipeOreStack(oreType, StackType.FLUID, stackSize);
	}
	
	public ArrayList<RecipeOreStack> oreStackList(ArrayList<String> oreTypes, int stackSize) {
		ArrayList<RecipeOreStack> oreStackList = new ArrayList<RecipeOreStack>();
		for (String oreType : oreTypes) if (oreStack(oreType, stackSize) != null) oreStackList.add(oreStack(oreType, stackSize));
		return oreStackList;
	}
	
	public ArrayList<RecipeOreStack> oreStackList(String[] oreTypes, int stackSize) {
		return oreStackList(Lists.newArrayList(oreTypes), stackSize);
	}
	
	public ArrayList<RecipeOreStack> fluidStackList(ArrayList<String> oreTypes, int stackSize) {
		ArrayList<RecipeOreStack> fluidStackList = new ArrayList<RecipeOreStack>();
		for (String oreType : oreTypes) if (fluidStack(oreType, stackSize) != null) fluidStackList.add(fluidStack(oreType, stackSize));
		return fluidStackList;
	}
	
	public ArrayList<RecipeOreStack> fluidStackList(String[] oreTypes, int stackSize) {
		return fluidStackList(Lists.newArrayList(oreTypes), stackSize);
	}
	
	public ArrayList<RecipeOreStack> mekanismFluidStackList(RecipeOreStack stack) {
		return stack.oreString.equals("helium") ? Lists.newArrayList(stack) : fluidStackList(Lists.newArrayList(stack.oreString, "liquid" + stack.oreString), stack.stackSize);
	}
}
