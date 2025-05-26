package nc.recipe;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.*;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.recipe.ingredient.*;
import nc.tile.internal.fluid.Tank;
import nc.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.Map.Entry;

public class RecipeHelper {
	
	public static boolean containsItemIngredient(List<IItemIngredient> list, IItemIngredient ingredient) {
		for (IItemIngredient i : list) {
			if (i == null) {
				continue;
			}
			if (i.match(ingredient, IngredientSorption.NEUTRAL).matches()) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean containsFluidIngredient(List<IFluidIngredient> list, IFluidIngredient ingredient) {
		for (IFluidIngredient i : list) {
			if (i == null) {
				continue;
			}
			if (i.match(ingredient, IngredientSorption.NEUTRAL).matches()) {
				return true;
			}
		}
		return false;
	}
	
	public static ItemStack fixItemStack(Object object) {
		if (object == null) {
			return null;
		}
		else if (object instanceof ItemStack) {
			ItemStack stack = ((ItemStack) object).copy();
			if (stack.getCount() <= 0) {
				stack.setCount(1);
			}
			return stack;
		}
		else if (object instanceof Item) {
			return new ItemStack((Item) object, 1);
		}
		else {
			if (!(object instanceof Block)) {
				throw new RuntimeException(String.format("Invalid ItemStack: %s", object));
			}
			return new ItemStack((Block) object, 1);
		}
	}
	
	public static FluidStack fixFluidStack(Object object) {
		if (object == null) {
			return null;
		}
		else if (object instanceof FluidStack) {
			FluidStack fluidstack = ((FluidStack) object).copy();
			if (fluidstack.amount <= 0) {
				fluidstack.amount = 1000;
			}
			return fluidstack;
		}
		else {
			if (!(object instanceof Fluid)) {
				throw new RuntimeException(String.format("Invalid FluidStack: %s", object));
			}
			return new FluidStack((Fluid) object, 1000);
		}
	}
	
	public static OreIngredient oreStackFromString(String name) {
		if (OreDictHelper.oreExists(name)) {
			return new OreIngredient(name, 1);
		}
		return null;
	}
	
	public static FluidIngredient fluidStackFromString(String name) {
		if (FluidRegHelper.fluidExists(name)) {
			return new FluidIngredient(name, 1000);
		}
		return null;
	}
	
	public static List<List<ItemStack>> getItemInputLists(List<IItemIngredient> itemIngredientList) {
		return StreamHelper.map(itemIngredientList, IItemIngredient::getInputStackList);
	}
	
	public static List<List<FluidStack>> getFluidInputLists(List<IFluidIngredient> fluidIngredientList) {
		return StreamHelper.map(fluidIngredientList, IFluidIngredient::getInputStackList);
	}
	
	public static List<List<ItemStack>> getItemOutputLists(List<IItemIngredient> itemIngredientList) {
		return StreamHelper.map(itemIngredientList, IItemIngredient::getOutputStackList);
	}
	
	public static List<List<FluidStack>> getFluidOutputLists(List<IFluidIngredient> fluidIngredientList) {
		return StreamHelper.map(fluidIngredientList, IFluidIngredient::getOutputStackList);
	}
	
	@Nullable
	public static List<ItemStack> getItemOutputList(List<IItemIngredient> itemIngredientList) {
		List<ItemStack> stacks = new ArrayList<>();
		for (IItemIngredient itemIngredient : itemIngredientList) {
			if (itemIngredient == null) {
				return Collections.emptyList();
			}
			ItemStack stack = itemIngredient.getStack();
			if (stack == null) {
				return Collections.emptyList();
			}
			stacks.add(stack);
		}
		return stacks;
	}
	
	@Nullable
	public static List<FluidStack> getFluidOutputList(List<IFluidIngredient> fluidIngredientList) {
		List<FluidStack> stacks = new ArrayList<>();
		for (IFluidIngredient fluidIngredient : fluidIngredientList) {
			if (fluidIngredient == null) {
				return Collections.emptyList();
			}
			FluidStack stack = fluidIngredient.getStack();
			if (stack == null) {
				return Collections.emptyList();
			}
			stacks.add(stack);
		}
		return stacks;
	}
	
	@Nullable
	public static ItemStack getItemStackFromIngredientList(List<IItemIngredient> itemIngredientList, int pos) {
		if (pos < itemIngredientList.size()) {
			return itemIngredientList.get(pos).getStack();
		}
		return null;
	}
	
	@Nullable
	public static FluidStack getFluidStackFromIngredientList(List<IFluidIngredient> fluidIngredientList, int pos) {
		if (pos < fluidIngredientList.size()) {
			return fluidIngredientList.get(pos).getStack();
		}
		return null;
	}
	
	@Nullable
	public static IItemIngredient buildItemIngredient(Object object) {
		if (AbstractRecipeHandler.requiresItemFixing(object)) {
			object = RecipeHelper.fixItemStack(object);
		}
		if (object instanceof IItemIngredient) {
			return checkedItemIngredient((IItemIngredient) object);
		}
		else if (object instanceof List<?> list) {
			List<IItemIngredient> buildList = new ArrayList<>();
			if (!list.isEmpty()) {
				for (Object listObject : list) {
					if (listObject instanceof IItemIngredient) {
						buildList.add((IItemIngredient) listObject);
					}
					else if (listObject != null) {
						IItemIngredient recipeObject = checkedItemIngredient(buildItemIngredient(listObject));
						if (recipeObject != null) {
							buildList.add(recipeObject);
						}
					}
				}
				if (buildList.isEmpty()) {
					return null;
				}
				return checkedItemIngredient(new ItemArrayIngredient(buildList));
			}
			else {
				return null;
			}
		}
		else if (object instanceof String) {
			return checkedItemIngredient(RecipeHelper.oreStackFromString((String) object));
		}
		if (object instanceof ItemStack) {
			return checkedItemIngredient(new ItemIngredient((ItemStack) object));
		}
		return null;
	}
	
	@Nullable
	public static IItemIngredient checkedItemIngredient(IItemIngredient ingredient) {
		return ingredient == null || !ingredient.isValid() ? null : ingredient;
	}
	
	@Nullable
	public static IFluidIngredient buildFluidIngredient(Object object) {
		if (AbstractRecipeHandler.requiresFluidFixing(object)) {
			object = RecipeHelper.fixFluidStack(object);
		}
		
		boolean mekanismExpansion = fluidNeedsMekanismExpansion(), techRebornExpansion = fluidNeedsTechRebornExpansion();
		if ((mekanismExpansion || techRebornExpansion) && object instanceof FluidIngredient) {
			return checkedFluidIngredient(buildFluidIngredient(expandedFluidStackList((FluidIngredient) object, mekanismExpansion, techRebornExpansion)));
		}
		
		if (object instanceof IFluidIngredient) {
			return checkedFluidIngredient((IFluidIngredient) object);
		}
		else if (object instanceof List<?> list) {
			List<IFluidIngredient> buildList = new ArrayList<>();
			if (!list.isEmpty()) {
				for (Object listObject : list) {
					if (listObject instanceof IFluidIngredient) {
						buildList.add((IFluidIngredient) listObject);
					}
					else if (listObject != null) {
						IFluidIngredient recipeObject = checkedFluidIngredient(buildFluidIngredient(listObject));
						if (recipeObject != null) {
							buildList.add(recipeObject);
						}
					}
				}
				if (buildList.isEmpty()) {
					return null;
				}
				return checkedFluidIngredient(new FluidArrayIngredient(buildList));
			}
			else {
				return null;
			}
		}
		else if (object instanceof String) {
			return checkedFluidIngredient(RecipeHelper.fluidStackFromString((String) object));
		}
		if (object instanceof FluidStack) {
			return checkedFluidIngredient(new FluidIngredient((FluidStack) object));
		}
		return null;
	}
	
	@Nullable
	public static IFluidIngredient checkedFluidIngredient(IFluidIngredient ingredient) {
		return ingredient == null || !ingredient.isValid() ? null : ingredient;
	}
	
	private static boolean fluidNeedsMekanismExpansion() {
		return NCConfig.enable_fluid_recipe_expansion[0] && ModCheck.mekanismLoaded();
	}
	
	private static boolean fluidNeedsTechRebornExpansion() {
		return NCConfig.enable_fluid_recipe_expansion[1] && ModCheck.techRebornLoaded();
	}
	
	/**
	 * For Mekanism and Tech Reborn fluids
	 */
	public static List<FluidIngredient> expandedFluidStackList(FluidIngredient stack, boolean mekanismExpansion, boolean techRebornExpansion) {
		List<FluidIngredient> fluidStackList = Lists.newArrayList(stack);
		
		if (mekanismExpansion && !stack.fluidName.equals("helium")) {
			if (GasHelper.TRANSLATION_MAP.containsKey(stack.fluidName)) {
				fluidStackList.add(AbstractRecipeHandler.fluidStack(GasHelper.TRANSLATION_MAP.get(stack.fluidName), stack.stack.amount));
			}
			else {
				fluidStackList.add(AbstractRecipeHandler.fluidStack("liquid" + stack.fluidName, stack.stack.amount));
			}
		}
		
		if (techRebornExpansion) {
			fluidStackList.add(AbstractRecipeHandler.fluidStack("fluid" + stack.fluidName, stack.stack.amount));
		}
		
		return fluidStackList;
	}
	
	public static RecipeMatchResult matchIngredients(IngredientSorption sorption, List<IItemIngredient> itemIngredients, List<IFluidIngredient> fluidIngredients, List<?> items, List<?> fluids, boolean shapeless) {
		int itemCount = items.size(), fluidCount = fluids.size();
		if (itemIngredients.size() != itemCount || fluidIngredients.size() != fluidCount) {
			return RecipeMatchResult.FAIL;
		}
		
		IntList itemIngredientNumbers = new IntArrayList(new int[itemCount]);
		IntList fluidIngredientNumbers = new IntArrayList(new int[fluidCount]);
		IntList itemInputOrder = CollectionHelper.increasingList(itemCount);
		IntList fluidInputOrder = CollectionHelper.increasingList(fluidCount);
		
		if (!shapeless) {
			for (int i = 0; i < itemCount; ++i) {
				IngredientMatchResult matchResult = itemIngredients.get(i).match(items.get(i), sorption);
				if (matchResult.matches()) {
					itemIngredientNumbers.set(i, matchResult.getIngredientNumber());
					continue;
				}
				return RecipeMatchResult.FAIL;
			}
			for (int i = 0; i < fluidCount; ++i) {
				Object fluid = fluids.get(i);
				if (fluid instanceof Tank tank) {
					fluid = tank.getFluid();
				}
				IngredientMatchResult matchResult = fluidIngredients.get(i).match(fluid, sorption);
				if (matchResult.matches()) {
					fluidIngredientNumbers.set(i, matchResult.getIngredientNumber());
					continue;
				}
				return RecipeMatchResult.FAIL;
			}
		}
		else {
			List<IItemIngredient> itemIngredientsRemaining = new ArrayList<>(itemIngredients);
			itemInputs:
			for (int i = 0; i < itemCount; ++i) {
				Object item = items.get(i);
				for (int j = 0; j < itemCount; ++j) {
					IItemIngredient itemIngredient = itemIngredientsRemaining.get(j);
					if (itemIngredient == null) {
						continue;
					}
					IngredientMatchResult matchResult = itemIngredient.match(item, sorption);
					if (matchResult.matches()) {
						itemIngredientsRemaining.set(j, null);
						itemIngredientNumbers.set(i, matchResult.getIngredientNumber());
						itemInputOrder.set(i, j);
						continue itemInputs;
					}
				}
				return RecipeMatchResult.FAIL;
			}
			List<IFluidIngredient> fluidIngredientsRemaining = new ArrayList<>(fluidIngredients);
			fluidInputs:
			for (int i = 0; i < fluidCount; ++i) {
				Object fluid = fluids.get(i);
				if (fluid instanceof Tank tank) {
					fluid = tank.getFluid();
				}
				for (int j = 0; j < fluidCount; ++j) {
					IFluidIngredient fluidIngredient = fluidIngredientsRemaining.get(j);
					if (fluidIngredient == null) {
						continue;
					}
					IngredientMatchResult matchResult = fluidIngredient.match(fluid, sorption);
					if (matchResult.matches()) {
						fluidIngredientsRemaining.set(j, null);
						fluidIngredientNumbers.set(i, matchResult.getIngredientNumber());
						fluidInputOrder.set(i, j);
						continue fluidInputs;
					}
				}
				return RecipeMatchResult.FAIL;
			}
		}
		return new RecipeMatchResult(true, itemIngredientNumbers, fluidIngredientNumbers, itemInputOrder, fluidInputOrder);
	}
	
	public static List<String> getItemIngredientNames(List<IItemIngredient> ingredientList) {
		List<String> ingredientNames = new ArrayList<>();
		for (IItemIngredient ingredient : ingredientList) {
			if (ingredient == null || ingredient instanceof EmptyItemIngredient) {
				ingredientNames.add("null");
			}
			else if (ingredient instanceof ItemArrayIngredient) {
				ingredientNames.add(((ItemArrayIngredient) ingredient).getIngredientRecipeString());
			}
			else {
				ingredientNames.add(ingredient.getMaxStackSize(0) + " x " + ingredient.getIngredientName());
			}
		}
		return ingredientNames;
	}
	
	public static List<String> getFluidIngredientNames(List<IFluidIngredient> ingredientList) {
		List<String> ingredientNames = new ArrayList<>();
		for (IFluidIngredient ingredient : ingredientList) {
			if (ingredient == null || ingredient instanceof EmptyFluidIngredient) {
				ingredientNames.add("null");
			}
			else if (ingredient instanceof FluidArrayIngredient) {
				ingredientNames.add(((FluidArrayIngredient) ingredient).getIngredientRecipeString());
			}
			else {
				ingredientNames.add(ingredient.getMaxStackSize(0) + " x " + ingredient.getIngredientName());
			}
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
		if (recipe == null) {
			return "nullRecipe";
		}
		return getRecipeString(recipe.getItemIngredients(), recipe.getFluidIngredients(), recipe.getItemProducts(), recipe.getFluidProducts());
	}
	
	public static List<String> buildItemIngredientNames(List<Object> ingredientList) {
		List<String> ingredientNames = new ArrayList<>();
		for (Object obj : ingredientList) {
			if (obj == null) {
				ingredientNames.add("null");
			}
			else {
				if (!(obj instanceof IItemIngredient)) {
					obj = buildItemIngredient(obj);
				}
				IItemIngredient ingredient = (IItemIngredient) obj;
				if (ingredient instanceof ItemArrayIngredient) {
					ingredientNames.add(((ItemArrayIngredient) ingredient).getIngredientRecipeString());
				}
				else {
					ingredientNames.add(ingredient.getMaxStackSize(0) + " x " + ingredient.getIngredientName());
				}
			}
		}
		return ingredientNames;
	}
	
	public static List<String> buildFluidIngredientNames(List<Object> ingredientList) {
		List<String> ingredientNames = new ArrayList<>();
		for (Object obj : ingredientList) {
			if (obj == null) {
				ingredientNames.add("null");
			}
			else {
				if (!(obj instanceof IFluidIngredient)) {
					obj = buildFluidIngredient(obj);
				}
				IFluidIngredient ingredient = (IFluidIngredient) obj;
				if (ingredient instanceof FluidArrayIngredient) {
					ingredientNames.add(((FluidArrayIngredient) ingredient).getIngredientRecipeString());
				}
				else {
					ingredientNames.add(ingredient.getMaxStackSize(0) + " x " + ingredient.getIngredientName());
				}
			}
		}
		return ingredientNames;
	}
	
	public static List<Set<String>> validFluids(BasicRecipeHandler recipeHandler) {
		return validFluids(recipeHandler, Collections.emptySet());
	}
	
	public static List<Set<String>> validFluids(BasicRecipeHandler recipeHandler, Set<String> exceptions) {
		Set<String> fluidNameSet = new ObjectOpenHashSet<>();
		for (Entry<String, Fluid> entry : FluidRegistry.getRegisteredFluids().entrySet()) {
			String fluidName = entry.getKey();
			if (recipeHandler.isValidFluidInput(new FluidStack(entry.getValue(), 1000)) && !exceptions.contains(fluidName)) {
				fluidNameSet.add(fluidName);
			}
		}
		
		List<Set<String>> allowedFluidSets = new ArrayList<>();
		for (int i = 0; i < recipeHandler.fluidInputSize; ++i) {
			allowedFluidSets.add(fluidNameSet);
		}
		for (int i = recipeHandler.fluidInputSize; i < recipeHandler.fluidInputSize + recipeHandler.fluidOutputSize; ++i) {
			allowedFluidSets.add(null);
		}
		
		return allowedFluidSets;
	}
	
	public static OreIngredient getOreStackFromItems(List<ItemStack> stackList, int stackSize) {
		if (stackList == null || stackList.isEmpty()) {
			return null;
		}
		String oreName = OreDictHelper.getOreNameFromStacks(stackList);
		if (oreName.equals("Unknown")) {
			return null;
		}
		return new OreIngredient(oreName, stackSize);
	}
	
	public static long hashMaterialsRaw(List<ItemStack> items, List<Tank> fluids) {
		long hash = 1L;
		for (ItemStack stack : items) {
			hash = 31L * hash + (stack == null || stack.isEmpty() ? 0L : (StackHelper.isWildcard(stack) ? 1L : RecipeItemHelper.pack(stack)));
		}
		for (Tank tank : fluids) {
			FluidStack stack;
			hash = 31L * hash + (tank == null || (stack = tank.getFluid()) == null ? 0L : stack.getFluid().getName().hashCode());
		}
		return hash;
	}
	
	public static long hashMaterials(List<ItemStack> items, List<FluidStack> fluids) {
		long hash = 1L;
		for (ItemStack stack : items) {
			hash = 31L * hash + (stack == null || stack.isEmpty() ? 0L : (StackHelper.isWildcard(stack) ? 1L : RecipeItemHelper.pack(stack)));
		}
		for (FluidStack stack : fluids) {
			hash = 31L * hash + (stack == null ? 0L : stack.getFluid().getName().hashCode());
		}
		return hash;
	}
	
	public static BasicRecipe blockRecipe(BasicRecipeHandler recipeHandler, World world, BlockPos pos) {
		return blockRecipe(recipeHandler, world.getBlockState(pos));
	}
	
	public static BasicRecipe blockRecipe(BasicRecipeHandler recipeHandler, IBlockState blockState) {
		RecipeInfo<BasicRecipe> recipeInfo = recipeHandler.getRecipeInfoFromInputs(Lists.newArrayList(StackHelper.blockStateToStack(blockState)), Collections.emptyList());
		return recipeInfo == null ? null : recipeInfo.recipe;
	}
	
	public static double getDecayTimeMultiplier(double baseRads, double radiation, double scaleFactor) {
		return radiation > baseRads ? (Math.log1p(baseRads / scaleFactor) / Math.log1p(radiation / scaleFactor)) : (1D + (Math.log1p(scaleFactor / radiation) / Math.log1p(scaleFactor / baseRads) - 1D) * (baseRads / scaleFactor) * (Math.log1p(scaleFactor / baseRads) / Math.log1p(baseRads / scaleFactor)));
	}
	
	public static double getDecayPowerMultiplier(double baseRads, double radiation, double scaleFactor) {
		return getDecayTimeMultiplier(baseRads, 1D / radiation, scaleFactor);
	}
	
	public static InventoryCrafting fakeCrafter(int width, int height) {
		return new FakeCrafting(width, height);
	}
	
	protected static class FakeCrafting extends InventoryCrafting {
		
		protected static final FakeCraftingContainer FAKE_CONTAINER = new FakeCraftingContainer();
		
		protected static class FakeCraftingContainer extends Container {
			
			@Override
			public void onCraftMatrixChanged(IInventory inventory) {
			
			}
			
			@Override
			public boolean canInteractWith(EntityPlayer player) {
				return false;
			}
		}
		
		protected FakeCrafting(int width, int height) {
			super(FAKE_CONTAINER, width, height);
		}
	}
}
