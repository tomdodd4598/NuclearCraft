package nc.recipe;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.*;
import nc.integration.crafttweaker.*;
import nc.integration.gtce.GTCERecipeHelper;
import nc.recipe.ingredient.*;
import nc.tile.internal.fluid.Tank;
import nc.util.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Optional;
import org.apache.commons.lang3.tuple.Pair;
import stanhebben.zenscript.annotations.*;

import javax.annotation.*;
import java.math.*;
import java.util.*;
import java.util.function.*;

import static nc.config.NCConfig.factor_recipes;

@ZenClass("mods.nuclearcraft.BasicRecipeHandler")
@ZenRegister
public abstract class BasicRecipeHandler extends AbstractRecipeHandler<BasicRecipe> {
	
	public final String name;
	public final int itemInputSize, fluidInputSize, itemOutputSize, fluidOutputSize;
	public final boolean isShapeless;
	
	public final int itemInputLastIndex, fluidInputLastIndex, itemOutputLastIndex, fluidOutputLastIndex;
	
	public final List<Set<String>> validFluids = new ArrayList<>();
	
	public BasicRecipeHandler(@Nonnull String name, int itemInputSize, int fluidInputSize, int itemOutputSize, int fluidOutputSize) {
		this(name, itemInputSize, fluidInputSize, itemOutputSize, fluidOutputSize, true);
	}
	
	public BasicRecipeHandler(@Nonnull String name, int itemInputSize, int fluidInputSize, int itemOutputSize, int fluidOutputSize, boolean isShapeless) {
		this.name = name;
		this.itemInputSize = itemInputSize;
		this.fluidInputSize = fluidInputSize;
		this.itemOutputSize = itemOutputSize;
		this.fluidOutputSize = fluidOutputSize;
		this.isShapeless = isShapeless;
		itemInputLastIndex = itemInputSize;
		fluidInputLastIndex = itemInputSize + fluidInputSize;
		itemOutputLastIndex = itemInputSize + fluidInputSize + itemOutputSize;
		fluidOutputLastIndex = itemInputSize + fluidInputSize + itemOutputSize + fluidOutputSize;
		addRecipes();
	}
	
	@Override
	public void addRecipe(Object... objects) {
		BasicRecipe recipe = buildRecipe(objects);
		addRecipe(factor_recipes ? factorRecipe(recipe) : recipe);
	}
	
	public BasicRecipe newRecipe(List<IItemIngredient> itemIngredients, List<IFluidIngredient> fluidIngredients, List<IItemIngredient> itemProducts, List<IFluidIngredient> fluidProducts, List<Object> extras, boolean shapeless) {
		return new BasicRecipe(itemIngredients, fluidIngredients, itemProducts, fluidProducts, extras, shapeless);
	}
	
	@Override
	public boolean isValidRecipe(BasicRecipe recipe) {
		if (itemOutputSize == 0 && fluidOutputSize == 0) {
			return true;
		}
		
		for (IIngredient<?> ingredient : recipe.getItemIngredients()) {
			if (!ingredient.isEmpty()) {
				return true;
			}
		}
		
		for (IIngredient<?> ingredient : recipe.getFluidIngredients()) {
			if (!ingredient.isEmpty()) {
				return true;
			}
		}
		
		for (IIngredient<?> ingredient : recipe.getItemProducts()) {
			if (!ingredient.isEmpty()) {
				return true;
			}
		}
		
		for (IIngredient<?> ingredient : recipe.getFluidProducts()) {
			if (!ingredient.isEmpty()) {
				return true;
			}
		}
		
		return false;
	}
	
	public void addGTCERecipes() {
		if (NCRecipes.hasGTCEIntegration(name)) {
			for (BasicRecipe recipe : recipeList) {
				GTCERecipeHelper.addGTCERecipe(name, recipe);
			}
		}
	}
	
	protected abstract List<Object> fixedExtras(List<Object> extras);
	
	public static class ExtrasFixer {
		
		protected final List<Object> extras;
		public final List<Object> fixed = new ArrayList<>();
		
		protected int currentIndex = 0;
		
		public ExtrasFixer(List<Object> extras) {
			this.extras = extras;
		}
		
		public <T> void add(Class<? extends T> clazz, T defaultValue) {
			int index = currentIndex++;
			Object extra;
			fixed.add(index < extras.size() && (extra = tryCast(clazz, extras.get(index))) != null ? extra : defaultValue);
		}
		
		public static Object tryCast(Class<?> targetClass, Object value) {
			if (value == null) {
				return null;
			}
			
			if (value instanceof Byte byteValue) {
				return castInt(targetClass, byteValue);
			}
			else if (value instanceof Short shortValue) {
				return castInt(targetClass, shortValue);
			}
			else if (value instanceof Integer intValue) {
				return castInt(targetClass, intValue);
			}
			else if (value instanceof Long longValue) {
				return castInt(targetClass, longValue);
			}
			else if (value instanceof BigInteger bigIntValue) {
				return castInt(targetClass, bigIntValue);
			}
			else if (value instanceof Float floatValue) {
				return castFloat(targetClass, floatValue);
			}
			else if (value instanceof Double doubleValue) {
				return castFloat(targetClass, doubleValue);
			}
			else if (value instanceof BigDecimal bigDecimalValue) {
				return castFloat(targetClass, bigDecimalValue);
			}
			
			return castDefault(targetClass, value);
		}
		
		private static <T extends Number> Number castInt(Class<?> targetClass, T value) {
			if (targetClass.equals(byte.class) || targetClass.equals(Byte.class)) {
				return value.byteValue();
			}
			else if (targetClass.equals(short.class) || targetClass.equals(Short.class)) {
				return value.shortValue();
			}
			else if (targetClass.equals(int.class) || targetClass.equals(Integer.class)) {
				return value.intValue();
			}
			else if (targetClass.equals(long.class) || targetClass.equals(Long.class)) {
				return value.longValue();
			}
			else {
				return castDefault(targetClass, value);
			}
		}
		
		private static <T extends Number> Number castFloat(Class<?> targetClass, T value) {
			if (targetClass.equals(float.class) || targetClass.equals(Float.class)) {
				return value.floatValue();
			}
			else if (targetClass.equals(double.class) || targetClass.equals(Double.class)) {
				return value.doubleValue();
			}
			else {
				return castDefault(targetClass, value);
			}
		}
		
		private static <T> T castDefault(Class<?> targetClass, T value) {
			return targetClass.isInstance(value) ? value : null;
		}
	}
	
	protected BasicRecipe factorRecipe(BasicRecipe recipe) {
		if (recipe == null) {
			return null;
		}
		if (!recipe.getItemIngredients().isEmpty() || !recipe.getItemProducts().isEmpty()) {
			return recipe;
		}
		
		IntList stackSizes = new IntArrayList();
		for (IFluidIngredient ingredient : recipe.getFluidIngredients()) {
			stackSizes.addAll(ingredient.getFactors());
		}
		for (IFluidIngredient ingredient : recipe.getFluidProducts()) {
			stackSizes.addAll(ingredient.getFactors());
		}
		stackSizes.addAll(getExtraFactors(recipe.getExtras()));
		
		int hcf = NCMath.hcf(stackSizes.toIntArray());
		if (hcf == 1) {
			return recipe;
		}
		
		UnaryOperator<List<IFluidIngredient>> factor = x -> StreamHelper.map(x, y -> y.getFactoredIngredient(hcf));
		
		return newRecipe(recipe.getItemIngredients(), factor.apply(recipe.getFluidIngredients()), recipe.getItemProducts(), factor.apply(recipe.getFluidProducts()), getFactoredExtras(recipe.getExtras(), hcf), recipe.isShapeless());
	}
	
	protected IntList getExtraFactors(List<Object> extras) {
		return new IntArrayList();
	}
	
	protected List<Object> getFactoredExtras(List<Object> extras, int factor) {
		return extras;
	}
	
	@Nullable
	public BasicRecipe buildRecipe(Object... objects) {
		List<Object> itemInputs = new ArrayList<>(), fluidInputs = new ArrayList<>(), itemOutputs = new ArrayList<>(), fluidOutputs = new ArrayList<>(), extras = new ArrayList<>();
		for (int i = 0; i < objects.length; ++i) {
			Object object = objects[i];
			if (i < itemInputLastIndex) {
				itemInputs.add(object);
			}
			else if (i < fluidInputLastIndex) {
				fluidInputs.add(object);
			}
			else if (i < itemOutputLastIndex) {
				itemOutputs.add(object);
			}
			else if (i < fluidOutputLastIndex) {
				fluidOutputs.add(object);
			}
			else {
				extras.add(object);
			}
		}
		return buildRecipe(itemInputs, fluidInputs, itemOutputs, fluidOutputs, extras, isShapeless);
	}
	
	protected static <T, V extends IIngredient<T>> V buildIngredientInternal(Object obj, Predicate<Object> isValidType, Function<Object, V> buildIngredient) {
		if (obj != null && isValidType.test(obj)) {
			V ingredient = buildIngredient.apply(obj);
			if (ingredient != null) {
				return ingredient;
			}
		}
		return null;
	}
	
	protected static IItemIngredient buildItemInputIngredientInternal(Object obj) {
		return buildIngredientInternal(obj, AbstractRecipeHandler::isValidItemInputType, RecipeHelper::buildItemIngredient);
	}
	
	protected static IFluidIngredient buildFluidInputIngredientInternal(Object obj) {
		return buildIngredientInternal(obj, AbstractRecipeHandler::isValidFluidInputType, RecipeHelper::buildFluidIngredient);
	}
	
	protected static IItemIngredient buildItemOutputIngredientInternal(Object obj) {
		return buildIngredientInternal(obj, AbstractRecipeHandler::isValidItemOutputType, RecipeHelper::buildItemIngredient);
	}
	
	protected static IFluidIngredient buildFluidOutputIngredientInternal(Object obj) {
		return buildIngredientInternal(obj, AbstractRecipeHandler::isValidFluidOutputType, RecipeHelper::buildFluidIngredient);
	}
	
	@Nullable
	public BasicRecipe buildRecipe(List<?> itemInputs, List<?> fluidInputs, List<?> itemOutputs, List<?> fluidOutputs, List<Object> extras, boolean shapeless) {
		List<IItemIngredient> itemIngredients = new ArrayList<>(), itemProducts = new ArrayList<>();
		List<IFluidIngredient> fluidIngredients = new ArrayList<>(), fluidProducts = new ArrayList<>();
		for (Object obj : itemInputs) {
			IItemIngredient input = buildItemInputIngredientInternal(obj);
			if (input == null) {
				return null;
			}
			itemIngredients.add(input);
		}
		for (Object obj : fluidInputs) {
			IFluidIngredient input = buildFluidInputIngredientInternal(obj);
			if (input == null) {
				return null;
			}
			fluidIngredients.add(input);
		}
		for (Object obj : itemOutputs) {
			IItemIngredient output = buildItemOutputIngredientInternal(obj);
			if (output == null) {
				return null;
			}
			itemProducts.add(output);
		}
		for (Object obj : fluidOutputs) {
			IFluidIngredient output = buildFluidOutputIngredientInternal(obj);
			if (output == null) {
				return null;
			}
			fluidProducts.add(output);
		}
		if (!isValidRecipe(itemIngredients, fluidIngredients, itemProducts, fluidProducts)) {
			NCUtil.getLogger().info("A " + name + " recipe failed to be registered: " + RecipeHelper.getRecipeString(itemIngredients, fluidIngredients, itemProducts, fluidProducts));
		}
		return newRecipe(itemIngredients, fluidIngredients, itemProducts, fluidProducts, fixedExtras(extras), shapeless);
	}
	
	public boolean isValidRecipe(List<IItemIngredient> itemIngredients, List<IFluidIngredient> fluidIngredients, List<IItemIngredient> itemProducts, List<IFluidIngredient> fluidProducts) {
		return itemIngredients.size() == itemInputSize && fluidIngredients.size() == fluidInputSize && itemProducts.size() == itemOutputSize && fluidProducts.size() == fluidOutputSize;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@ZenMethod
	public static BasicRecipeHandler get(String name) {
		return NCRecipes.getHandler(name);
	}
	
	@Override
	@ZenMethod
	public List<BasicRecipe> getRecipeList() {
		return recipeList;
	}
	
	@ZenMethod
	public int getItemInputSize() {
		return itemInputSize;
	}
	
	@ZenMethod
	public int getFluidInputSize() {
		return fluidInputSize;
	}
	
	@ZenMethod
	public int getItemOutputSize() {
		return itemOutputSize;
	}
	
	@ZenMethod
	public int getFluidOutputSize() {
		return fluidOutputSize;
	}
	
	@ZenMethod
	public boolean isShapeless() {
		return isShapeless;
	}
	
	@ZenMethod(value = "addRecipe")
	@Optional.Method(modid = "crafttweaker")
	public void ctAddRecipe(Object... objects) {
		CraftTweakerAPI.apply(new CTAddRecipe(this, Arrays.asList(objects)));
	}
	
	@ZenMethod(value = "removeRecipeWithInput")
	@Optional.Method(modid = "crafttweaker")
	public void ctRemoveRecipeWithInput(crafttweaker.api.item.IIngredient... ctIngredients) {
		CraftTweakerAPI.apply(new CTRemoveRecipe(this, IngredientSorption.INPUT, Arrays.asList(ctIngredients)));
	}
	
	@ZenMethod(value = "removeRecipeWithOutput")
	@Optional.Method(modid = "crafttweaker")
	public void ctRemoveRecipeWithOutput(crafttweaker.api.item.IIngredient... ctIngredients) {
		CraftTweakerAPI.apply(new CTRemoveRecipe(this, IngredientSorption.OUTPUT, Arrays.asList(ctIngredients)));
	}
	
	@ZenMethod(value = "removeAllRecipes")
	@Optional.Method(modid = "crafttweaker")
	public void ctRemoveAllRecipes() {
		CraftTweakerAPI.apply(new CTRemoveAllRecipes(this));
	}
	
	protected void setValidFluids() {
		validFluids.clear();
		validFluids.addAll(RecipeHelper.validFluids(this));
	}
	
	public void postInit() {
		super.postInit();
		setValidFluids();
	}
	
	@Override
	protected void fillHashCache() {
		for (BasicRecipe recipe : recipeList) {
			List<Pair<List<ItemStack>, List<FluidStack>>> materialListTuples = new ArrayList<>();
			
			if (!prepareMaterialListTuples(recipe, materialListTuples)) {
				continue;
			}
			
			for (Pair<List<ItemStack>, List<FluidStack>> materials : materialListTuples) {
				if (isShapeless) {
					for (List<ItemStack> items : PermutationHelper.permutations(materials.getLeft())) {
						for (List<FluidStack> fluids : PermutationHelper.permutations(materials.getRight())) {
							addToHashCache(recipe, items, fluids);
						}
					}
				}
				else {
					addToHashCache(recipe, materials.getLeft(), materials.getRight());
				}
			}
		}
	}
	
	protected void addToHashCache(BasicRecipe recipe, List<ItemStack> items, List<FluidStack> fluids) {
		long hash = RecipeHelper.hashMaterials(items, fluids);
		if (recipeCache.containsKey(hash)) {
			recipeCache.get(hash).add(recipe);
		}
		else {
			ObjectSet<BasicRecipe> set = new ObjectOpenHashSet<>();
			set.add(recipe);
			recipeCache.put(hash, set);
		}
	}
	
	protected <T, V extends IIngredient<T>> boolean isValidInput(T stack, int index, Function<BasicRecipe, List<V>> ingredientsFunction) {
		for (BasicRecipe recipe : recipeList) {
			if (isShapeless) {
				for (V input : ingredientsFunction.apply(recipe)) {
					if (input.match(stack, IngredientSorption.NEUTRAL).matches()) {
						return true;
					}
				}
			}
			else {
				if (ingredientsFunction.apply(recipe).get(index).match(stack, IngredientSorption.NEUTRAL).matches()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isValidItemInput(ItemStack stack, int slot) {
		return isValidInput(stack, slot, BasicRecipe::getItemIngredients);
	}
	
	public boolean isValidFluidInput(FluidStack stack, int tankNumber) {
		return isValidInput(stack, tankNumber, BasicRecipe::getFluidIngredients);
	}
	
	/**
	 * Smart insertion - don't insert if stack is not valid for any possible recipes
	 */
	public <T, U, V extends IIngredient<T>, W extends IIngredient<U>> boolean isValidInput(T stack, int index, List<T> inputs, List<U> associatedInputs, RecipeInfo<BasicRecipe> recipeInfo, int inputSize, int associatedInputSize, Predicate<T> isEmptyFunction, Predicate<U> associatedIsEmptyFunction, Predicate<T> isEqualFunction, Function<BasicRecipe, List<V>> ingredientsFunction, Function<BasicRecipe, List<W>> associatedIngredientsFunction) {
		List<T> otherInputs = inputsExcludingIndex(inputs, index);
		if ((otherInputs.stream().allMatch(isEmptyFunction) && associatedInputs.stream().allMatch(associatedIsEmptyFunction)) || isEqualFunction.test(inputs.get(index))) {
			return isValidInput(stack, index, ingredientsFunction);
		}
		
		if (recipeInfo == null) {
			ObjectSet<BasicRecipe> recipes = new ObjectOpenHashSet<>(recipeList);
			recipeLoop:
			for (BasicRecipe recipe : recipeList) {
				List<V> ingredients = ingredientsFunction.apply(recipe);
				List<W> associatedIngredients = associatedIngredientsFunction.apply(recipe);
				if (isShapeless) {
					stackLoop:
					for (T inputStack : inputs) {
						if (!isEmptyFunction.test(inputStack)) {
							for (V recipeInput : ingredients) {
								if (recipeInput.match(inputStack, IngredientSorption.NEUTRAL).matches()) {
									continue stackLoop;
								}
							}
							recipes.remove(recipe);
							continue recipeLoop;
						}
					}
					
					associatedStackLoop:
					for (U inputStack : associatedInputs) {
						if (!associatedIsEmptyFunction.test(inputStack)) {
							for (W recipeInput : associatedIngredients) {
								if (recipeInput.match(inputStack, IngredientSorption.NEUTRAL).matches()) {
									continue associatedStackLoop;
								}
							}
							recipes.remove(recipe);
							continue recipeLoop;
						}
					}
				}
				else {
					for (int i = 0; i < inputSize; ++i) {
						T inputStack = inputs.get(i);
						if (!isEmptyFunction.test(inputStack) && !ingredients.get(i).match(inputStack, IngredientSorption.NEUTRAL).matches()) {
							recipes.remove(recipe);
							continue recipeLoop;
						}
					}
					
					for (int i = 0; i < associatedInputSize; ++i) {
						U inputStack = associatedInputs.get(i);
						if (!associatedIsEmptyFunction.test(inputStack) && !associatedIngredients.get(i).match(inputStack, IngredientSorption.NEUTRAL).matches()) {
							recipes.remove(recipe);
							continue recipeLoop;
						}
					}
				}
			}
			
			for (BasicRecipe recipe : recipes) {
				if (isValidInputInternal(stack, index, otherInputs, ingredientsFunction.apply(recipe), isEmptyFunction)) {
					return true;
				}
			}
			return false;
		}
		else {
			return isValidInputInternal(stack, index, otherInputs, ingredientsFunction.apply(recipeInfo.recipe), isEmptyFunction);
		}
	}
	
	public boolean isValidItemInput(ItemStack stack, int slot, List<ItemStack> itemInputs, List<Tank> fluidInputs, RecipeInfo<BasicRecipe> recipeInfo) {
		return isValidInput(stack, slot, itemInputs, StreamHelper.map(fluidInputs, Tank::getFluid), recipeInfo, itemInputSize, fluidInputSize, ItemStack::isEmpty, x -> x == null || x.amount <= 0, x -> stack.isItemEqual(x) && StackHelper.areItemStackTagsEqual(stack, x), BasicRecipe::getItemIngredients, BasicRecipe::getFluidIngredients);
	}
	
	public boolean isValidFluidInput(FluidStack stack, int tankNumber, List<Tank> fluidInputs, List<ItemStack> itemInputs, RecipeInfo<BasicRecipe> recipeInfo) {
		return isValidInput(stack, tankNumber, StreamHelper.map(fluidInputs, Tank::getFluid), itemInputs, recipeInfo, fluidInputSize, itemInputSize, x -> x == null || x.amount <= 0, ItemStack::isEmpty, stack == null ? Objects::isNull : x -> (stack.isFluidEqual(x) && FluidStack.areFluidStackTagsEqual(stack, x)), BasicRecipe::getFluidIngredients, BasicRecipe::getItemIngredients);
	}
	
	protected <T, V extends IIngredient<T>> boolean isValidInputInternal(T stack, int index, List<T> otherInputs, List<V> ingredients, Predicate<T> isEmptyFunction) {
		if (isShapeless) {
			for (V input : ingredients) {
				if (input.match(stack, IngredientSorption.NEUTRAL).matches()) {
					for (T other : otherInputs) {
						if (!isEmptyFunction.test(other) && input.match(other, IngredientSorption.NEUTRAL).matches()) {
							return false;
						}
					}
					return true;
				}
			}
			return false;
		}
		else {
			return ingredients.get(index).match(stack, IngredientSorption.NEUTRAL).matches();
		}
	}
	
	protected static <T> List<T> inputsExcludingIndex(List<T> inputs, int index) {
		List<T> inputsExcludingIndex = new ArrayList<>(inputs);
		if (index >= 0 && index < inputs.size()) {
			inputsExcludingIndex.remove(index);
		}
		return inputsExcludingIndex;
	}
}
