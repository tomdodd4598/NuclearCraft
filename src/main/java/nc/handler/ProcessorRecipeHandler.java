package nc.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.oredict.OreDictionary;

public abstract class ProcessorRecipeHandler {

	public int itemInputSize, fluidInputSize, totalInputSize, itemOutputSize, fluidOutputSize, totalOutputSize;
	public boolean shapeless, hasExtras;
	public static final int[] INVALID_ORDER = new int[] {-1, -1, -1, -1};

	/** Add all recipes here */
	public abstract void addRecipes();

	public ProcessorRecipeHandler(int itemInputSize, int fluidInputSize, int itemOutputSize, int fluidOutputSize, boolean shapeless, boolean hasExtras) {
		this.itemInputSize = itemInputSize;
		this.fluidInputSize = fluidInputSize;
		this.totalInputSize = itemInputSize + fluidInputSize;
		this.itemOutputSize = itemOutputSize;
		this.fluidOutputSize = fluidOutputSize;
		this.totalOutputSize = itemOutputSize + fluidOutputSize;
		this.shapeless = itemInputSize == 1 ? false : shapeless;
		this.hasExtras = hasExtras;
		
		addRecipes();
	}

	private Map<Object[], Object[]> recipeList = new HashMap<Object[], Object[]>();

	/** Get the full list of recipes */
	public Map<Object[], Object[]> getRecipes() {
		return recipeList;
	}

	/** Make sure each ingredient entry is an ItemStack or FluidStack */
	public void addRecipe(Object... objects) {
		Object[] stack = new Object[objects.length];
		if(objects.length > totalInputSize + totalOutputSize + (hasExtras ? 1 : 0)) {
			FMLLog.warning(getClass().getName() + " - a recipe involving " + objects[0].toString() + " was removed because it was too long!");
			return;
		}
		for (int i = 0; i < totalInputSize + totalOutputSize; i++) {
			if (objects[i] == null) {
				FMLLog.warning(getClass().getName() + " - a recipe was removed because an entry was null!");
				return;
			}
			if (objects[i] instanceof String) {
				if (i < itemInputSize) {
					if (oreExists((String)objects[i])) {
						stack[i] = oreStack((String)objects[i], 1);
					} else {
						FMLLog.warning(getClass().getName() + " - a recipe was removed because an oreDict entry for " + (String)objects[i] + " didn't exist!");
						return;
					}
				} else if (i < totalInputSize) {
					if (fluidExists((String)objects[i])) {
						stack[i] = fluidStack((String)objects[i], 1000);
					} else {
						FMLLog.warning(getClass().getName() + " - a recipe was removed because a fluidReg entry for " + (String)objects[i] + " didn't exist!");
						return;
					}
				} else if (i < totalInputSize + itemOutputSize) {
					NonNullList<ItemStack> ores = OreDictionary.getOres((String)objects[i]);
					if (ores.size() > 0) {
					stack[i] = ores.get(0);
					} else {
						FMLLog.warning(getClass().getName() + " - a recipe was removed because an oreDict entry for " + (String)objects[i] + " didn't exist!");
						return;
					}
				} else if (i < totalInputSize + totalOutputSize) {
					if (fluidExists((String)objects[i])) {
						stack[i] = fluidStack((String)objects[i], 1000);
					} else {
						FMLLog.warning(getClass().getName() + " - a recipe was removed because a fluidReg entry for " + (String)objects[i] + " didn't exist!");
						return;
					}
				}
			} else if (objects[i] instanceof OreStack) {
				if (i < itemInputSize) {
					if (OreDictionary.getOres(((OreStack) objects[i]).oreString).size() > 0) {
						stack[i] = objects[i];
					} else {
						FMLLog.warning(getClass().getName() + " - a recipe was removed because an oreDict entry for " + ((OreStack) objects[i]).oreString + " didn't exist!");
						return;
					}
				} else if (i < totalInputSize) {
					FMLLog.warning(getClass().getName() + " - a recipe was removed because an there was an input type mismatch!");
					return;
				} else if (i < totalInputSize + itemOutputSize) {
					NonNullList<ItemStack> ores = OreDictionary.getOres(((OreStack) objects[i]).oreString);
					if (ores.size() > 0) {
						stack[i] = new ItemStack(ores.get(0).getItem(), ((OreStack) objects[i]).stackSize, ores.get(0).getItemDamage());
					} else {
						FMLLog.warning(getClass().getName() + " - a recipe was removed because an oreDict entry for " + ((OreStack) objects[i]).oreString + " didn't exist!");
						return;
					}
				} else if (i < totalInputSize + totalOutputSize) {
					FMLLog.warning(getClass().getName() + " - a recipe was removed because an there was an input type mismatch!");
					return;
				}
			} else if (objects[i] instanceof FluidStack) {
				if (i < itemInputSize) {
					FMLLog.warning(getClass().getName() + " - a recipe was removed because an there was an input type mismatch!");
					return;
				} else if (i < totalInputSize) {
					if (fluidExists(((FluidStack)objects[i]).getFluid())) {
						stack[i] = objects[i];
					} else {
						FMLLog.warning(getClass().getName() + " - a recipe was removed because a fluidReg entry didn't exist!");
						return;
					}
				} else if (i < totalInputSize + itemOutputSize) {
					FMLLog.warning(getClass().getName() + " - a recipe was removed because an there was an input type mismatch!");
					return;
				} else if (i < totalInputSize + totalOutputSize) {
					if (fluidExists(((FluidStack)objects[i]).getFluid())) {
						stack[i] = objects[i];
					} else {
						FMLLog.warning(getClass().getName() + " - a recipe was removed because a fluidReg entry didn't exist!");
						return;
					}
				}
			} else if (objects[i] instanceof Fluid) {
				if (i < itemInputSize) {
					FMLLog.warning(getClass().getName() + " - a recipe was removed because an there was an input type mismatch!");
					return;
				} else if (i < totalInputSize) {
					if (fluidExists(((Fluid)objects[i]))) {
						stack[i] = new FluidStack((Fluid)objects[i], 1000);
					} else {
						FMLLog.warning(getClass().getName() + " - a recipe was removed because a fluidReg entry didn't exist!");
						return;
					}
				} else if (i < totalInputSize + itemOutputSize) {
					FMLLog.warning(getClass().getName() + " - a recipe was removed because an there was an input type mismatch!");
					return;
				} else if (i < totalInputSize + totalOutputSize) {
					if (fluidExists(((Fluid)objects[i]))) {
						stack[i] = new FluidStack((Fluid)objects[i], 1000);
					} else {
						FMLLog.warning(getClass().getName() + " - a recipe was removed because a fluidReg entry didn't exist!");
						return;
					}
				}
			} else if (objects[i] instanceof ItemStack[]) {
				for (int s = 0; s < ((ItemStack[]) objects[i]).length; i++) {
					if (((ItemStack[]) objects[i])[s] == ItemStack.EMPTY) {
						FMLLog.warning(getClass().getName() + " - a recipe was removed because an ItemStack input was empty!");
						return;
					}
				}
				stack[i] = objects[i];
			} else if (objects[i] instanceof FluidStack[]) {
				for (int s = 0; s < ((FluidStack[]) objects[i]).length; i++) {
					if (((FluidStack[]) objects[i])[s] == null) {
						FMLLog.warning(getClass().getName() + " - a recipe was removed because an input was null!");
						return;
					}
				}
				stack[i] = objects[i];
			} else {
				stack[i] = fixedStack(objects[i]);
			}
		}
		if (hasExtras) stack[totalInputSize + totalOutputSize] = objects[totalInputSize + totalOutputSize];
		addFinal(stack);
	}
	
	public boolean oreExists(String name) {
		return OreDictionary.getOres(name).size() > 0;
	}
	
	public boolean fluidExists(String name) {
		//return FluidRegistry.getRegisteredFluids().keySet().contains(name);
		return FluidRegistry.getFluid(name) != null;
	}
	
	public boolean fluidExists(Fluid fluid) {
		//return FluidRegistry.getRegisteredFluids().keySet().contains(name);
		return FluidRegistry.getFluidName(fluid) != null;
	}

	/** Separates the recipe into an input and output list */
	private void addFinal(Object[] stacks) {
		Object[] input = new Object[totalInputSize], output = new Object[totalOutputSize + (hasExtras ? 1 : 0)];

		for (int i = 0; i < stacks.length; i++) {
			if (i < totalInputSize) {
				input[i] = stacks[i];
			} else if (i < totalInputSize + (hasExtras ? 1 : 0) + totalOutputSize) {
				output[i - totalInputSize] = stacks[i];
			} else {
				throw new RuntimeException("Recipe is too big!");
			}
		}
		addRecipe(input, output);
	}

	/** Turns blocks/items into ItemStacks */
	private ItemStack fixedStack(Object obj) {
		if (obj instanceof ItemStack) {
			return ((ItemStack) obj).copy();
		} else if (obj instanceof Item) {
			return new ItemStack((Item) obj, 1);
		} else {
			if (!(obj instanceof Block)) {
				throw new RuntimeException("Invalid Recipe!");
			}
			return new ItemStack((Block) obj, 1);
		}
	}

	/** Adds the input and output lists */
	private void addRecipe(Object[] input, Object[] output) {
		recipeList.put(convertToArrays(input), output);
	}

	/**
	 * 
	 * @param output
	 *            Output stack you wish to find
	 * @param input
	 *            Full list of inputs
	 * @return
	 */
	public Object getOutput(int output, Object... input) {
		return getOutput(input)[output];
	}

	/**
	 * 
	 * @param input
	 *            Full list of inputs
	 * @return Full list of output stacks
	 */
	public Object[] getOutput(Object... input) {
		if (input.length != totalInputSize) {
			Object[] defaultStacks = new Object[totalOutputSize];
			for (int i = 0; i < defaultStacks.length; i++) {
				defaultStacks[i] = null;
			}
			return defaultStacks;
		}
		for (int i = 0; i < input.length; i++) {
			if (input[i] == ItemStack.EMPTY || input[i] == null) {
				Object[] defaultStacks = new Object[totalOutputSize];
				for (int j = 0; j < defaultStacks.length; j++) {
					defaultStacks[j] = null;
				}
				return defaultStacks;
			}
		}
		Iterator<?> iterator = recipeList.entrySet().iterator();

		Map.Entry<?, ?> entry;
		do {
			if (!iterator.hasNext()) {
				Object[] defaultStacks = new Object[totalOutputSize];
				for (int j = 0; j < defaultStacks.length; j++) {
					defaultStacks[j] = null;
				}
				return defaultStacks;
			}

			entry = (Map.Entry<?, ?>) iterator.next();
		} while (!checkInput(input, (Object[]) entry.getKey()));

		return convertOutput((Object[]) entry.getValue());
	}

	public int getInputSize(int input, Object... output) {
		Object[] inputs = getInput(output);
		if (inputs == null) {
			return 1;
		}
		return getInputSize(inputs)[input];
	}

	/** Gets the full list of inputs from list of outputs */
	public Object[] getInput(Object... output) {

		if (output.length != totalOutputSize) {
			return new Object[totalInputSize];
		}
		for (int i = 0; i < output.length; i++) {
			if (output[i] == ItemStack.EMPTY || output[i] == null) {
				return new Object[totalInputSize];
			}
		}
		Iterator<?> iterator = recipeList.entrySet().iterator();

		Map.Entry<?, ?> entry;
		do {
			if (!iterator.hasNext()) {
				return new Object[totalInputSize];
			}

			entry = (Map.Entry<?, ?>) iterator.next();
		} while (!checkOutput(output, (Object[]) entry.getValue()));

		return (Object[]) entry.getKey();
	}
	
	/** Gets the recipe extras from list of inputs */
	public Object getExtras(Object... input) {
		if (input.length != totalInputSize) {
			return null;
		}
		for (int i = 0; i < input.length; i++) {
			if (input[i] == ItemStack.EMPTY || input[i] == null) {
				return null;
			}
		}
		Iterator<?> iterator = recipeList.entrySet().iterator();

		Map.Entry<?, ?> entry;
		do {
			if (!iterator.hasNext()) {
				return null;
			}

			entry = (Map.Entry<?, ?>) iterator.next();
		} while (!checkInput(input, (Object[]) entry.getKey()));
		
		Object[] output = (Object[]) entry.getValue();
		return output[totalOutputSize];
	}

	/** Check if the ingredient is used in any recipe ignoring its stack size */
	public boolean validInput(Object input) {
		if (input == ItemStack.EMPTY || input == null) {
			return false;
		}
		Iterator<?> iterator = recipeList.entrySet().iterator();

		Map.Entry<?, ?> entry;
		do {
			if (!iterator.hasNext()) {
				return false;
			}

			entry = (Map.Entry<?, ?>) iterator.next();
		} while (containsStack(input, (Object[]) entry.getKey(), false).isEmpty());

		return true;
	}

	/** Check if the product is produced in any recipe ignoring its stack size */
	public boolean validOutput(Object output) {
		if (output == ItemStack.EMPTY || output == null) {
			return false;
		}
		Iterator<?> iterator = recipeList.entrySet().iterator();

		Map.Entry<?, ?> entry;
		do {
			if (!iterator.hasNext()) {
				return false;
			}

			entry = (Map.Entry<?, ?>) iterator.next();
		} while (containsStack(output, (Object[]) entry.getKey(), false).isEmpty());

		return true;
	}

	private Object[] convertOutput(Object[] output) {
		Object[] defaultStacks = new Object[output.length - (hasExtras ? 1 : 0)];
		for (int j = 0; j < defaultStacks.length; j++) {
			defaultStacks[j] = null;
		}
		Object[] stack = defaultStacks;
		for (int i = 0; i < output.length - (hasExtras ? 1 : 0); i++) {
			if (output[i] instanceof ItemStack) {
				stack[i] = (ItemStack) output[i];
			} else if (output[i] instanceof OreStack) {
				NonNullList<ItemStack> ore = OreDictionary.getOres(((OreStack) output[i]).oreString);
				stack[i] = new ItemStack(ore.get(0).getItem(), ((OreStack) output[i]).stackSize, ore.get(0).getItemDamage());
			} else if (output[i] instanceof FluidStack) {
				stack[i] = (FluidStack) output[i];
			}
		}
		return stack;
	}

	private int[] getInputSize(Object[] input) {
		int[] sizes = new int[input.length];
		for (int i = 0; i < input.length; i++) {
			if (input[i] instanceof ItemStack) {
				sizes[i] = ((ItemStack) input[i]).getCount();
			} else if (input[i] instanceof ItemStack[]) {
				sizes[i] = ((ItemStack[]) input[i])[0].getCount();
			} else if (input[i] instanceof FluidStack) {
				sizes[i] = ((FluidStack) input[i]).amount;
			} else if (input[i] instanceof FluidStack[]) {
				sizes[i] = ((FluidStack[]) input[i])[0].amount;
			}
		}
		return sizes;
	}

	private boolean checkInput(Object[] input, Object[] key) {
		if (input.length != key.length && input.length == totalInputSize) {
			return false;
		}
		if (!shapeless) {
			for (int i = 0; i < key.length; i++) {
				if (key[i] instanceof ItemStack) {
					if (!equalStack(input[i], (ItemStack) key[i], true)) {
						return false;
					}
				} else if (key[i] instanceof ItemStack[]) {
					if (containsStack(input[i], (ItemStack[]) key[i], true).isEmpty()) {
						return false;
					}
				} else if (key[i] instanceof FluidStack) {
					if (!equalStack(input[i], (FluidStack) key[i], true)) {
						return false;
					}
				} else if (key[i] instanceof FluidStack[]) {
					if (containsStack(input[i], (FluidStack[]) key[i], true).isEmpty()) {
						return false;
					}
				}
			}
		} else {
			List<Integer> currentOrder = new ArrayList<Integer>();
			for (int i = 0; i < key.length; i++) {
				List<Integer> stackNumbers = containsStack(input[i], key, true);
				if (stackNumbers.isEmpty()) return false;
				boolean check = false;
				for (int stackNumber : stackNumbers) {
					if (!currentOrder.contains(stackNumber)) {
						currentOrder.add(stackNumber);
						check = true;
						break;
					}
				}
				if (!check) return false;
			}
		}
		return true;
	}
	
	public int[] getInputOrder(Object[] input, Object[] key) {
		if (input.length != key.length && input.length == totalInputSize) {
			return INVALID_ORDER;
		}
		int[] order = new int[input.length];
		if (!shapeless) {
			for (int i = 0; i < input.length; i++) {
				order[i] = i;
			}
			return order;
		} else {
			List<Integer> currentOrder = new ArrayList<Integer>();
			for (int i = 0; i < input.length; i++) {
				List<Integer> stackNumbers = containsStack(input[i], key, true);
				if (stackNumbers.isEmpty()) return INVALID_ORDER;
				boolean check = false;
				for (int stackNumber : stackNumbers) {
					if (!currentOrder.contains(stackNumber)) {
						order[i] = stackNumber;
						currentOrder.add(stackNumber);
						check = true;
						break;
					}
				}
				if (!check) return INVALID_ORDER;
			}
			return order;
		}
	}
	
	public int[] getItemInputOrder(Object[] input, Object[] key) {
		if (itemInputSize == 0) return new int[] {};
		if (input.length != key.length && input.length == totalInputSize) {
			return INVALID_ORDER;
		}
		int[] order = new int[input.length - fluidInputSize];
		if (!shapeless) {
			for (int i = 0; i < input.length - fluidInputSize; i++) {
				order[i] = i;
			}
			return order;
		} else {
			List<Integer> currentOrder = new ArrayList<Integer>();
			for (int i = 0; i < input.length; i++) {
				List<Integer> stackNumbers = containsStack(input[i], key, true);
				if (stackNumbers.isEmpty()) return INVALID_ORDER;
				boolean check = false;
				for (int stackNumber : stackNumbers) {
					if (!currentOrder.contains(stackNumber)) {
						order[i] = stackNumber;
						currentOrder.add(stackNumber);
						check = true;
						break;
					}
				}
				if (!check) return INVALID_ORDER;
			}
			return order;
		}
	}
	
	public int[] getFluidInputOrder(Object[] input, Object[] key) {
		if (fluidInputSize == 0) return new int[] {};
		if (input.length != key.length && input.length == totalInputSize) {
			return INVALID_ORDER;
		}
		int[] order = new int[input.length - itemInputSize];
		if (!shapeless) {
			for (int i = 0; i < input.length - itemInputSize; i++) {
				order[i] = i + itemInputSize;
			}
			return order;
		} else {
			List<Integer> currentOrder = new ArrayList<Integer>();
			for (int i = itemInputSize; i < input.length + itemInputSize; i++) {
				List<Integer> stackNumbers = containsStack(input[i], key, true);
				if (stackNumbers.isEmpty()) return INVALID_ORDER;
				boolean check = false;
				for (int stackNumber : stackNumbers) {
					if (!currentOrder.contains(stackNumber)) {
						order[i] = stackNumber - itemInputSize;
						currentOrder.add(stackNumber);
						check = true;
						break;
					}
				}
				if (!check) return INVALID_ORDER;
			}
			return order;
		}
	}
	
	private boolean checkOutput(Object[] output, Object[] key) {
		if (output.length + (hasExtras ? 1 : 0) != key.length && output.length == totalOutputSize) {
			return false;
		}
		for (int i = 0; i < output.length; i++) {
			if (key[i] instanceof ItemStack) {
				if (!equalStack(output[i], (ItemStack) key[i], true)) {
					return false;
				}
			} else if (key[i] instanceof ItemStack[]) {
				if (containsStack(output[i], (ItemStack[]) key[i], true).isEmpty()) {
					return false;
				}
			} else if (key[i] instanceof FluidStack) {
				if (!equalStack(output[i], (FluidStack) key[i], true)) {
					return false;
				}
			} else if (key[i] instanceof FluidStack[]) {
				if (containsStack(output[i], (FluidStack[]) key[i], true).isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}

	public List<Integer> containsStack(Object stack, Object[] key, boolean checkSize) {
		List<Integer> searchResults = new ArrayList<Integer>();
		for (int i = 0; i < key.length; i++) {
			if (key[i] != null) {
				if (key[i] instanceof ItemStack) {
					if (equalStack(stack, (ItemStack) key[i], checkSize)) {
						searchResults.add(i);
					}
				} else if (key[i] instanceof ItemStack[]) {
					for (int s = 0; s < ((ItemStack[]) key[i]).length; s++) {
						if (((ItemStack[]) key[i])[s] != ItemStack.EMPTY && ((ItemStack[]) key[i])[s] instanceof ItemStack) {
							if (equalStack(stack, ((ItemStack[]) key[i])[s], checkSize)) {
								searchResults.add(i);
							}
						}
					}
				} else if (key[i] instanceof FluidStack) {
					if (equalStack(stack, (FluidStack) key[i], checkSize)) {
						searchResults.add(i);
					}
				} else if (key[i] instanceof FluidStack[]) {
					for (int s = 0; s < ((FluidStack[]) key[i]).length; s++) {
						if (((FluidStack[]) key[i])[s] != null && ((FluidStack[]) key[i])[s] instanceof FluidStack) {
							if (equalStack(stack, ((FluidStack[]) key[i])[s], checkSize)) {
								searchResults.add(i);
							}
						}
					}
				}
			}
		}
		return searchResults;
	}

	private boolean equalStack(Object stack, Object key, boolean checkSize) {
		//System.out.print(stack.stackSize >= stack.stackSize);
		if (stack instanceof ItemStack && key instanceof ItemStack) return ((ItemStack)stack).getItem() == ((ItemStack)key).getItem() && (((ItemStack)stack).getItemDamage() == ((ItemStack)key).getItemDamage()) && (!checkSize || ((ItemStack)key).getCount() <= ((ItemStack)stack).getCount());
		else if (stack instanceof FluidStack && key instanceof FluidStack) return ((FluidStack)stack).isFluidEqual((FluidStack)key) && ((FluidStack)key).amount <= ((FluidStack)stack).amount;
		else return false;
	}

	private int findStackSize(Object stack, Object[] key, int pos) {
		if (key[pos] != null) {
			if (key[pos] instanceof ItemStack) {
				if (equalStack(stack, ((ItemStack) key[pos]), false)) {
					return ((ItemStack) key[pos]).getCount();
				}
			} else if (key[pos] instanceof FluidStack) {
				if (equalStack(stack, ((FluidStack) key[pos]), false)) {
					return ((FluidStack) key[pos]).amount;
				}
			} else if (key[pos] instanceof ItemStack[]) {
				return findStackSize(stack, (ItemStack[]) key[pos], pos);
			} else if (key[pos] instanceof FluidStack[]) {
				return findStackSize(stack, (FluidStack[]) key[pos], pos);
			}
		}
		return -1;
	}

	private Object[] convertToArrays(Object[] object) {
		Object[] stack = new Object[object.length];
		for (int i = 0; i < object.length; i++) {
			if (object[i] instanceof ItemStack) {
				stack[i] = (ItemStack)object[i];
			} else if (object[i] instanceof ItemStack[]) {
				stack[i] = (ItemStack[])object[i];
			} else if (object[i] instanceof OreStack) {
				NonNullList<ItemStack> ore = OreDictionary.getOres(((OreStack) object[i]).oreString);
				ItemStack[] ores = new ItemStack[ore.size()];
				for (int o = 0; o < ore.size(); o++) {
					ores[o] = new ItemStack(ore.get(o).getItem(), ((OreStack) object[i]).stackSize, ore.get(o).getItemDamage());
				}
				stack[i] = ores;
			} else if (object[i] instanceof FluidStack) {
				stack[i] = (FluidStack)object[i];
			} else if (object[i] instanceof FluidStack[]) {
				stack[i] = (FluidStack[])object[i];
			} 
		}
		return stack;
	}

	public OreStack oreStack(String oreString, int stackSize) {
		return new OreStack(oreString, stackSize);
	}

	public static class OreStack extends Object {
		public String oreString;
		public int stackSize;

		public OreStack(String oreString, int stackSize) {
			this.oreString = oreString;
			this.stackSize = stackSize;
		}
	}
	
	public FluidStack fluidStack(String fluidString, int stackSize) {
		List<String> fluidList = new ArrayList<String>(FluidRegistry.getRegisteredFluids().keySet());
		if (fluidList.contains(fluidString)) return new FluidStack(FluidRegistry.getFluid(fluidString), stackSize);
		else return null;
	}
}
