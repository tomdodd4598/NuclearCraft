package nc.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import nc.util.StackHelper;
import nc.util.OreStackHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.FMLLog;

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

	public boolean addRecipe(T recipe) {
		return recipe != null ? recipes.add(recipe) : false;
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
		if (object instanceof IIngredient) {
			return (IIngredient) object;
		} else if (object instanceof ArrayList) {
			ArrayList list = (ArrayList) object;
			ArrayList<IIngredient> buildList = new ArrayList();
			if (!list.isEmpty()) {
				for (Object listObject : list) {
					if (listObject != null) {
						IIngredient recipeObject = buildRecipeObject(listObject);
						if (recipeObject != null) {
							buildList.add(recipeObject);
						} else {
							return null;
						}
					}
				}
				return new RecipeStackArray(buildList);
			} else {
				return null;
			}
		} else if (object instanceof String) {
			if (OreStackHelper.exists((String) object, StackType.ITEM)) return new RecipeOreStack((String) object, StackType.ITEM, 1);
			else if (OreStackHelper.exists((String) object, StackType.FLUID)) return new RecipeOreStack((String) object, StackType.FLUID, 1);
			else if (OreStackHelper.exists((String) object, StackType.UNSPECIFIED)) return new RecipeOreStack((String) object, StackType.UNSPECIFIED, 1);
			FMLLog.warning(getRecipeName() + " - a string ingredient '" + ((String) object) + "' is invalid!");
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
				if (obj != null) FMLLog.warning(getRecipeName() + " - a recipe was removed because the input " + obj.toString() + " is invalid!");
				else FMLLog.warning(getRecipeName() + " - a recipe was removed because an input was null!");
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
				if (obj != null) FMLLog.warning(getRecipeName() + " - a recipe was removed because the output " + obj.toString() + " is invalid!");
				else FMLLog.warning(getRecipeName() + " - a recipe was removed because an output was null!");
				return null;
			}
		}
		if (!isValidRecipe(recipeInputs, recipeOutputs)) {
			FMLLog.warning(getRecipeName() + " - a recipe was removed: " + recipeInputs.toString() + " -> " + recipeOutputs.toString());
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
	
	public static String[][] validFluids(BaseRecipeHandler recipes, String... exceptions) {
		int fluidInputSize = recipes.inputSizeFluid;
		int fluidOutputSize = recipes.outputSizeFluid;
		ArrayList<Fluid> fluidList = new ArrayList<Fluid>(FluidRegistry.getRegisteredFluids().values());
		ArrayList<FluidStack> fluidStackList = new ArrayList<FluidStack>();
		for (Fluid fluid : fluidList) {
			fluidStackList.add(new FluidStack(fluid, 1000));
		}
		ArrayList<String> exceptionsList = new ArrayList<String>();
		if (exceptions != null) for (int i = 0; i < exceptions.length; i++) {
			exceptionsList.add(exceptions[i]);
		}
		ArrayList<String> fluidNameList = new ArrayList<String>();
		for (FluidStack fluidStack : fluidStackList) {
			String fluidName = fluidStack.getFluid().getName();
			if (recipes.isValidInput(fluidStack) && !exceptionsList.contains(fluidName)) fluidNameList.add(fluidName);
		}
		String[] allowedFluidArray = new String[fluidNameList.size()];
		for (int i = 0; i < fluidNameList.size(); i++) {
			allowedFluidArray[i] = fluidNameList.get(i);
		}
		
		String[][] allowedFluidArrays = new String[fluidInputSize + fluidOutputSize][];
		for (int i = 0; i < fluidInputSize; i++) {
			allowedFluidArrays[i] = allowedFluidArray;
		}
		for (int i = fluidInputSize; i < fluidInputSize + fluidOutputSize; i++) {
			allowedFluidArrays[i] = new String[] {};
		}
		return allowedFluidArrays;
	}
	
	public static Object adjustObject(Object object) {
		return StackHelper.fixStack(object);
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

	public static ArrayList getValuesFromList(ArrayList<IIngredient> list) {
		ArrayList values = new ArrayList();
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
	
	public RecipeOreStack oreStack(String oreType, int stackSize) {
		if (!OreStackHelper.exists(oreType, StackType.ITEM)) {
			FMLLog.warning(getRecipeName() + " - an item ore dict stack of '" + oreType + "' is invalid!");
			return null;
		}
		return new RecipeOreStack(oreType, StackType.ITEM, stackSize);
	}
	
	public RecipeOreStack fluidStack(String oreType, int stackSize) {
		if (!OreStackHelper.exists(oreType, StackType.FLUID)) {
			FMLLog.warning(getRecipeName() + " - a fluid ore dict stack of '" + oreType + "' is invalid!");
			return null;
		}
		return new RecipeOreStack(oreType, StackType.FLUID, stackSize);
	}
}
