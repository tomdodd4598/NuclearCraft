package nc.crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.FMLLog;

public abstract class NCRecipeHelper {

	public int outputSize, inputSize;

	/** add all your recipes here */
	public abstract void addRecipes();

	public NCRecipeHelper(int inputSize, int outputSize) {
		this.inputSize = inputSize;
		this.outputSize = outputSize;

		this.addRecipes();
	}

	private Map<Object[], Object[]> recipeList = new HashMap<Object[], Object[]>();

	/** get the full list of recipes */
	public Map<Object[], Object[]> getRecipes() {
		return this.recipeList;
	}

	/** makes sure each item/block is an itemstack */
	public void addRecipe(Object... objects) {
		Object[] stack = new Object[objects.length];
		if(objects.length>this.inputSize+this.outputSize){
			FMLLog.warning("RecipeHelper - A recipe was removed because it was too long!");
			return;
		}
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] == null) {
				return;
			}
			if (objects[i] instanceof String) {
				if (i < inputSize) {
					if (OreDictionary.getOres(((String) objects[i])).size() > 0) {
						stack[i] = new OreStack((String)objects[i],1);
					} else {
						return;
					}
				} else if (!(i - inputSize > outputSize)) {
					ArrayList<ItemStack> ores = OreDictionary.getOres((String) objects[i]);
					if(ores.size()>0){
					stack[i] = ores.get(0);
					}else{
						return;
					}
				}
			} else if (objects[i] instanceof OreStack) {
				if (i < inputSize) {
					if (OreDictionary.getOres(((OreStack) objects[i]).oreString).size() > 0) {
						stack[i] = objects[i];
					} else {
						return;
					}
				} else if (!(i - inputSize > outputSize)) {
					ArrayList<ItemStack> ores = OreDictionary.getOres(((OreStack) objects[i]).oreString);
					if (ores.size() > 0) {
						stack[i] = new ItemStack(ores.get(0).getItem(),((OreStack) objects[i]).stackSize, ores.get(0).getItemDamage());
					} else {
						return;
					}
				}
			} else if (objects[i] instanceof ItemStack[]) {
				for (int s = 0; s < ((ItemStack[]) objects[i]).length; i++) {
					if (((ItemStack[]) objects[i])[s] == null) {
						return;
					}
				}
				stack[i] = objects[i];
			} else {
				stack[i] = fixedStack(objects[i]);
			}
		}
		addFinal(stack);
	}

	/** separates the recipe into an input and output list */
	private void addFinal(Object[] stacks) {
		Object[] input = new Object[inputSize], output = new Object[outputSize];

		for (int i = 0; i < stacks.length; i++) {
			if (i < inputSize) {
				input[i] = stacks[i];
			} else if (!(i - inputSize > outputSize)) {
				output[i - inputSize] = stacks[i];
			} else {
				throw new RuntimeException("Recipe is too big!");
			}
		}
		addRecipe(input, output);
	}

	/** turns blocks/items into ItemStacks */
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

	/** adds the two input and output lists */
	private void addRecipe(Object[] input, Object[] output) {
		recipeList.put(convertToArrays(input), output);
	}

	/**
	 * 
	 * @param output
	 *            output stack you wish to find
	 * @param input
	 *            full list of inputs
	 * @return
	 */
	public ItemStack getOutput(int output, ItemStack... input) {
		return getOutput(input)[output];
	}

	/**
	 * 
	 * @param input
	 *            full list of inputs
	 * @return full list of output stacks
	 */
	public ItemStack[] getOutput(ItemStack... input) {
		if (input.length != inputSize) {
			return new ItemStack[outputSize];
		}
		for (int i = 0; i < input.length; i++) {
			if (input[i] == null) {
				return new ItemStack[outputSize];
			}
		}
		Iterator<?> iterator = this.recipeList.entrySet().iterator();

		Map.Entry<?, ?> entry;
		do {
			if (!iterator.hasNext()) {
				return new ItemStack[outputSize];
			}

			entry = (Map.Entry<?, ?>) iterator.next();
		} while (!checkInput(input, (Object[]) entry.getKey()));

		return convertOutput((Object[]) entry.getValue());
	}

	public int getInputSize(int input, ItemStack... output) {
		Object[] inputs = this.getInput(output);
		if (inputs == null) {
			return 1;
		}
		return this.getInputSize(inputs)[input];
	}

	/**
	 * gets the full list of inputs from list of outputs
	 * 
	 * @param input
	 *            stack to check
	 * @return
	 */
	public Object[] getInput(ItemStack... output) {

		if (output.length != outputSize) {
			return new Object[inputSize];
		}
		for (int i = 0; i < output.length; i++) {
			if (output[i] == null) {
				return new Object[inputSize];
			}
		}
		Iterator<?> iterator = this.recipeList.entrySet().iterator();

		Map.Entry<?, ?> entry;
		do {
			if (!iterator.hasNext()) {
				return new Object[inputSize];
			}

			entry = (Map.Entry<?, ?>) iterator.next();
		} while (!checkOutput(output, (Object[]) entry.getValue()));

		return (Object[]) entry.getKey();
	}

	/**
	 * fixed check if the stack is used in any recipe ignoring its stack size
	 * 
	 * @param input
	 *            stack to check
	 * @return validity
	 */
	public boolean validInput(ItemStack input) {
		if (input == null) {
			return false;
		}
		Iterator<?> iterator = this.recipeList.entrySet().iterator();

		Map.Entry<?, ?> entry;
		do {
			if (!iterator.hasNext()) {
				return false;
			}

			entry = (Map.Entry<?, ?>) iterator.next();
		} while (containsStack(input, (Object[]) entry.getKey(), false) == -1);

		return true;
	}

	/**
	 * fixed check if the stack is used in any recipe ignoring its stack size
	 * 
	 * @param output
	 *            stack to check
	 * @return validity
	 */
	public boolean validOutput(ItemStack output) {
		if (output == null) {
			return false;
		}
		Iterator<?> iterator = this.recipeList.entrySet().iterator();

		Map.Entry<?, ?> entry;
		do {
			if (!iterator.hasNext()) {
				return false;
			}

			entry = (Map.Entry<?, ?>) iterator.next();
		} while (containsStack(output, (Object[]) entry.getKey(), false) == -1);

		return true;
	}

	/** fixed */
	private ItemStack[] convertOutput(Object[] output) {
		ItemStack[] stack = new ItemStack[output.length];
		for (int i = 0; i < output.length; i++) {
			if (output[i] instanceof ItemStack) {
				stack[i] = (ItemStack) output[i];
			}else if (output[i] instanceof OreStack) {
				ArrayList<ItemStack> ore = OreDictionary
						.getOres(((OreStack) output[i]).oreString);
				stack[i] = new ItemStack(ore.get(0).getItem(),
						((OreStack) output[i]).stackSize, ore.get(0)
								.getItemDamage());
			}
		}
		return stack;
	}

	private int[] getInputSize(Object[] input) {
		int[] sizes = new int[input.length];
		for (int i = 0; i < input.length; i++) {
			if (input[i] instanceof ItemStack) {
				sizes[i] = ((ItemStack) input[i]).stackSize;
			} else if (input[i] instanceof ItemStack[]) {
				sizes[i] = ((ItemStack[]) input[i])[0].stackSize;
			}
		}
		return sizes;
	}

	private boolean checkInput(ItemStack[] input, Object[] key) {

		if (input.length != key.length && input.length == inputSize) {
			return false;
		}
		for (int i = 0; i < key.length; i++) {
			if (key[i] instanceof ItemStack) {
				if (!equalStack(input[i], (ItemStack) key[i], true)) {
					return false;
				}
			} else if (key[i] instanceof ItemStack[]) {
				if (containsStack(input[i], (ItemStack[]) key[i], true) == -1) {
					return false;
				}
			}
		}
		return true;
	}

	/** fixed */
	private boolean checkOutput(ItemStack[] output, Object[] key) {
		if (output.length != key.length && output.length == inputSize) {
			return false;
		}
		for (int i = 0; i < output.length; i++) {
			if (key[i] instanceof ItemStack) {
				if (!equalStack(output[i], ((ItemStack) key[i]), true)) {
					return false;
				}
			} else if (key[i] instanceof ItemStack[]) {
				if (containsStack(output[i], (ItemStack[]) key[i], true) == -1) {
					return false;
				}
			}
		}
		return true;
	}

	public int containsStack(ItemStack stack, Object[] key, boolean checkSize) {
		for (int i = 0; i < key.length; i++) {
			if (key[i] != null) {
				if (key[i] instanceof ItemStack) {
					if (equalStack(stack, ((ItemStack) key[i]), checkSize)) {
						return i;
					}
				} else if (key[i] instanceof ItemStack[]) {
					for (int s = 0; s < ((ItemStack[]) key[i]).length; s++) {
						if (((ItemStack[]) key[i])[s] != null
								&& ((ItemStack[]) key[i])[s] instanceof ItemStack) {
							if (equalStack(stack, ((ItemStack[]) key[i])[s],
									checkSize)) {
								return i;
							}
						}
					}
				}
			}
		}
		return -1;
	}

	private boolean equalStack(ItemStack stack, ItemStack key, boolean checkSize) {
		//System.out.print(stack.stackSize >= stack.stackSize);
		return stack.getItem() == key.getItem()
				&& (stack.getItemDamage() == key.getItemDamage())
				&& (!checkSize || key.stackSize <= stack.stackSize);
	}

	@SuppressWarnings("unused")
	private int findStackSize(ItemStack stack, Object[] key, int pos) {
		if (key[pos] != null) {
			if (key[pos] instanceof ItemStack) {
				if (equalStack(stack, ((ItemStack) key[pos]), false)) {
					return ((ItemStack) key[pos]).stackSize;
				}
			} else if (key[pos] instanceof ItemStack[]) {
				return findStackSize(stack, (ItemStack[]) key[pos], pos);

			}
		}
		return -1;
	}

	private Object[] convertToArrays(Object[] object) {
		Object[] stack = new Object[object.length];
		for (int i = 0; i < object.length; i++) {
			if (object[i] instanceof ItemStack) {
				stack[i] = (ItemStack) object[i];
			} else if (object[i] instanceof ItemStack[]) {
				stack[i] = (ItemStack[])object[i];
			}else if (object[i] instanceof OreStack) {
				ArrayList<ItemStack> ore = OreDictionary.getOres(((OreStack) object[i]).oreString);
				ItemStack[] ores = new ItemStack[ore.size()];
				for (int o = 0; o < ore.size(); o++) {
					ores[o] = new ItemStack(ore.get(o).getItem(),
							((OreStack) object[i]).stackSize, ore.get(o).getItemDamage());
				}
				stack[i] = ores;
			}
		}
		return stack;
	}

	public OreStack oreStack(String oreString, int stackSize) {
		return new OreStack(oreString, stackSize);
	}

	private static class OreStack extends Object {
		public String oreString;
		public int stackSize;

		public OreStack(String oreString, int stackSize) {
			this.oreString = oreString;
			this.stackSize = stackSize;
		}
	}
}
