package nc.recipe;

import static nc.util.PermutationHelper.permutations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import crafttweaker.annotations.ZenRegister;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
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
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.nuclearcraft.AbstractRecipeHandler")
@ZenRegister
public abstract class AbstractRecipeHandler<RECIPE extends IRecipe> {
	
	protected List<RECIPE> recipeList = new ArrayList<>();
	
	protected Long2ObjectMap<RECIPE> recipeCache = new Long2ObjectOpenHashMap<>();
	
	private static List<Class<?>> validItemInputs = Lists.newArrayList(IItemIngredient.class, ArrayList.class, String.class, Item.class, Block.class, ItemStack.class, ItemStack[].class);
	private static List<Class<?>> validFluidInputs = Lists.newArrayList(IFluidIngredient.class, ArrayList.class, String.class, Fluid.class, FluidStack.class, FluidStack[].class);
	private static List<Class<?>> validItemOutputs = Lists.newArrayList(IItemIngredient.class, String.class, Item.class, Block.class, ItemStack.class);
	private static List<Class<?>> validFluidOutputs = Lists.newArrayList(IFluidIngredient.class, String.class, Fluid.class, FluidStack.class);
	
	private static List<Class<?>> needItemAltering = Lists.newArrayList(Item.class, Block.class);
	private static List<Class<?>> needFluidAltering = Lists.newArrayList(Fluid.class);
	
	public static final IntList INVALID = new IntArrayList(new int[] {-1});
	
	public abstract void addRecipes();
	
	@ZenMethod
	public abstract String getRecipeName();
	
	@ZenMethod
	public abstract List<RECIPE> getRecipeList();
	
	public Long2ObjectMap<RECIPE> getRecipeCache() {
		return recipeCache;
	}
	
	public abstract void addRecipe(Object... objects);
	
	public @Nullable RecipeInfo<RECIPE> getRecipeInfoFromInputs(List<ItemStack> itemInputs, List<Tank> fluidInputs) {
		RECIPE recipe = recipeCache.get(RecipeHelper.hashMaterialsRaw(itemInputs, fluidInputs));
		if (recipe != null) {
			RecipeMatchResult matchResult = recipe.matchInputs(itemInputs, fluidInputs);
			if (matchResult.matches()) return new RecipeInfo(recipe, matchResult);
		}
		return null;
	}
	
	/*public @Nullable RecipeInfo<RECIPE> getRecipeInfoFromOutputs(List<ItemStack> itemOutputs, List<Tank> fluidOutputs) {
		if (isFullNull(itemOutputs, fluidOutputs)) return null;
		
		for (T recipe : recipeList) {
			RecipeMatchResult matchResult = recipe.matchOutputs(itemOutputs, fluidOutputs);
			if (matchResult.matches()) return new RecipeInfo(recipe, matchResult);
		}
		return null;
	}*/
	
	/*private static boolean isFullNull(List<ItemStack> items, List<Tank> tanks) {
		for (ItemStack item : items) if (item != null && !item.isEmpty()) return false;
		for (Tank tank : tanks) if (tank.getFluid() != null) return false;
		return true;
	}*/
	
	public @Nullable RECIPE getRecipeFromIngredients(List<IItemIngredient> itemIngredients, List<IFluidIngredient> fluidIngredients) {
		for (RECIPE recipe : recipeList) {
			if (recipe.matchIngredients(itemIngredients, fluidIngredients).matches()) return recipe;
		}
		return null;
	}
	
	public @Nullable RECIPE getRecipeFromProducts(List<IItemIngredient> itemProducts, List<IFluidIngredient> fluidProducts) {
		for (RECIPE recipe : recipeList) {
			if (recipe.matchProducts(itemProducts, fluidProducts).matches()) return recipe;
		}
		return null;
	}
	
	/*public List<IIngredient> getInputList(Object... outputs) {
		List outputList = ArrayHelper.asList(outputs);
		RECIPE recipe = getRecipeFromOutputs(outputList);
		List result = recipe != null ? recipe.inputs() : new ArrayList<>();
		return result;
	}

	public List<IIngredient> getOutputList(Object... inputs) {
		List inputList = ArrayHelper.asList(inputs);
		RECIPE recipe = getRecipeFromInputs(inputList);
		List result = recipe != null ? recipe.outputs() : new ArrayList<>();
		return result;
	}*/
	
	public boolean addRecipe(RECIPE recipe) {
		return recipe != null ? recipeList.add(recipe) : false;
	}

	public boolean removeRecipe(RECIPE recipe) {
		return recipe != null ? recipeList.remove(recipe) : false;
	}
	
	public void removeAllRecipes() {
		recipeList.clear();
		recipeCache.clear();
	}
	
	public void refreshCache() {
		recipeCache.clear();
		
		recipeLoop: for (RECIPE recipe : recipeList) {
			List<List<ItemStack>> itemInputLists = new ArrayList<>();
			List<List<FluidStack>> fluidInputLists = new ArrayList<>();
			
			for (IItemIngredient item : recipe.getItemIngredients()) itemInputLists.add(item.getInputStackHashingList());
			for (IFluidIngredient fluid : recipe.getFluidIngredients()) fluidInputLists.add(fluid.getInputStackHashingList());
			
			int arrSize = recipe.getItemIngredients().size() + recipe.getFluidIngredients().size();
			int[] inputNumbers = new int[arrSize];
			Arrays.fill(inputNumbers, 0);
			
			int[] maxNumbers  = new int[arrSize];
			for (int i = 0; i < itemInputLists.size(); i++) {
				int maxNumber = itemInputLists.get(i).size() - 1;
				if (maxNumber < 0) continue recipeLoop;
				maxNumbers[i] = maxNumber;
			}
			for (int i = 0; i < fluidInputLists.size(); i++) {
				int maxNumber = fluidInputLists.get(i).size() - 1;
				if (maxNumber < 0) continue recipeLoop;
				maxNumbers[i + itemInputLists.size()] = maxNumber;
			}
			
			List<Pair<List<ItemStack>, List<FluidStack>>> materialListTuples = new ArrayList<>();
			
			RecipeTupleGenerator.INSTANCE.generateMaterialListTuples(materialListTuples, maxNumbers, inputNumbers, itemInputLists, fluidInputLists);
			
			for (Pair<List<ItemStack>, List<FluidStack>> materials : materialListTuples) {
				for (List<ItemStack> items : permutations(materials.getLeft())) {
					for (List<FluidStack> fluids : permutations(materials.getRight())) {
						recipeCache.put(RecipeHelper.hashMaterials(items, fluids), recipe);
					}
				}
			}
		}
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
		for (RECIPE recipe : recipeList) {
			for (IItemIngredient input : recipe.getItemIngredients()) {
				if (input.match(stack, IngredientSorption.NEUTRAL).matches()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isValidFluidInput(FluidStack stack) {
		for (RECIPE recipe : recipeList) {
			for (IFluidIngredient input : recipe.getFluidIngredients()) {
				if (input.match(stack, IngredientSorption.NEUTRAL).matches()) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isValidItemOutput(ItemStack stack) {
		for (RECIPE recipe : recipeList) {
			for (IItemIngredient output : recipe.getItemProducts()) {
				if (output.match(stack, IngredientSorption.OUTPUT).matches()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isValidFluidOutput(FluidStack stack) {
		for (RECIPE recipe : recipeList) {
			for (IFluidIngredient output : recipe.getFluidProducts()) {
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
		
		List<ItemStack> otherStacks = new ArrayList<>();
		for (ItemStack otherInput : otherInputs) {
			if (!otherInput.isEmpty()) otherStacks.add(otherInput);
		}
		if (otherStacks.isEmpty()) return isValidItemInput(stack);
		
		List<ItemStack> allStacks = Lists.newArrayList(stack);
		allStacks.addAll(otherStacks);
		
		List<RECIPE> recipeList = new ArrayList(this.recipeList);
		recipeLoop: for (RECIPE recipe : this.recipeList) {
			objLoop: for (ItemStack obj : allStacks) {
				for (IItemIngredient input : recipe.getItemIngredients()) {
					if (input.match(obj, IngredientSorption.NEUTRAL).matches()) continue objLoop;
				}
				recipeList.remove(recipe);
				continue recipeLoop;
			}
		}
		
		for (RECIPE recipe : recipeList) {
			for (IItemIngredient input : recipe.getItemIngredients()) {
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
	
	/* ================================== Recipe Ingredients ===================================== */
	
	public static OreIngredient oreStack(String oreType, int stackSize) {
		if (!OreDictHelper.oreExists(oreType)) return null;
		return new OreIngredient(oreType, stackSize);
	}
	
	public static FluidIngredient fluidStack(String fluidName, int stackSize) {
		if (!FluidRegHelper.fluidExists(fluidName)) return null;
		return new FluidIngredient(fluidName, stackSize);
	}
	
	public static List<OreIngredient> oreStackList(List<String> oreTypes, int stackSize) {
		List<OreIngredient> oreStackList = new ArrayList<>();
		for (String oreType : oreTypes) if (oreStack(oreType, stackSize) != null) oreStackList.add(oreStack(oreType, stackSize));
		return oreStackList;
	}
	
	public static List<FluidIngredient> fluidStackList(List<String> fluidNames, int stackSize) {
		List<FluidIngredient> fluidStackList = new ArrayList<>();
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
		List<ChanceItemIngredient> oreStackList = new ArrayList<>();
		for (String oreType : oreTypes) if (chanceOreStack(oreType, stackSize, chancePercent) != null) oreStackList.add(chanceOreStack(oreType, stackSize, chancePercent));
		return oreStackList;
	}
	
	public static List<ChanceItemIngredient> chanceOreStackList(List<String> oreTypes, int stackSize, int chancePercent, int minStackSize) {
		List<ChanceItemIngredient> oreStackList = new ArrayList<>();
		for (String oreType : oreTypes) if (chanceOreStack(oreType, stackSize, chancePercent, minStackSize) != null) oreStackList.add(chanceOreStack(oreType, stackSize, chancePercent, minStackSize));
		return oreStackList;
	}
	
	public static List<ChanceFluidIngredient> chanceFluidStackList(List<String> fluidNames, int stackSize, int chancePercent, int stackDiff) {
		List<ChanceFluidIngredient> fluidStackList = new ArrayList<>();
		for (String fluidName : fluidNames) if (chanceFluidStack(fluidName, stackSize, chancePercent, stackDiff) != null) fluidStackList.add(chanceFluidStack(fluidName, stackSize, chancePercent, stackDiff));
		return fluidStackList;
	}
	
	public static List<ChanceFluidIngredient> chanceFluidStackList(List<String> fluidNames, int stackSize, int chancePercent, int stackDiff, int minStackSize) {
		List<ChanceFluidIngredient> fluidStackList = new ArrayList<>();
		for (String fluidName : fluidNames) if (chanceFluidStack(fluidName, stackSize, chancePercent, stackDiff, minStackSize) != null) fluidStackList.add(chanceFluidStack(fluidName, stackSize, chancePercent, stackDiff, minStackSize));
		return fluidStackList;
	}
}
