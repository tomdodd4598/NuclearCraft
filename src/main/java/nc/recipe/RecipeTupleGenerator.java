package nc.recipe;

import java.util.*;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipeTupleGenerator {
	
	public static final RecipeTupleGenerator INSTANCE = new RecipeTupleGenerator();
	
	private RecipeTupleGenerator() {}
	
	private boolean itemEnd, fluidEnd;
	
	public void generateMaterialListTuples(List<Pair<List<ItemStack>, List<FluidStack>>> tuples, int[] maxNumbers, int[] inputNumbers, List<List<ItemStack>> itemInputLists, List<List<FluidStack>> fluidInputLists, boolean removeEmptyStacks) {
		do {
			generateNextMaterialListTuple(tuples, maxNumbers, inputNumbers, itemInputLists, fluidInputLists, removeEmptyStacks);
		}
		while (!itemEnd || !fluidEnd);
	}
	
	private void generateNextMaterialListTuple(List<Pair<List<ItemStack>, List<FluidStack>>> tuples, int[] maxNumbers, int[] inputNumbers, List<List<ItemStack>> itemInputLists, List<List<FluidStack>> fluidInputLists, boolean removeEmptyStacks) {
		int itemInputSize = itemInputLists.size(), fluidInputSize = fluidInputLists.size();
		
		List<ItemStack> itemInputs = new ArrayList<>();
		List<FluidStack> fluidInputs = new ArrayList<>();
		
		for (int i = 0; i < itemInputSize; ++i) {
			ItemStack stack = itemInputLists.get(i).get(inputNumbers[i]);
			if (!removeEmptyStacks || (stack != null && !stack.isEmpty())) {
				itemInputs.add(stack);
			}
		}
		
		for (int i = 0; i < fluidInputSize; ++i) {
			FluidStack stack = fluidInputLists.get(i).get(inputNumbers[i + itemInputSize]);
			if (!removeEmptyStacks || stack != null) {
				fluidInputs.add(stack);
			}
		}
		
		tuples.add(Pair.of(itemInputs, fluidInputs));
		
		itemEnd = false;
		if (itemInputSize == 0) {
			itemEnd = true;
		}
		else {
			for (int i = 0; i < itemInputSize; ++i) {
				if (inputNumbers[i] < maxNumbers[i]) {
					++inputNumbers[i];
					break;
				}
				else {
					inputNumbers[i] = 0;
					if (i == itemInputSize - 1) {
						itemEnd = true;
					}
				}
			}
		}
		
		fluidEnd = false;
		if (fluidInputSize == 0) {
			fluidEnd = true;
		}
		else if (itemEnd) {
			for (int i = 0; i < fluidInputSize; ++i) {
				if (inputNumbers[i + itemInputSize] < maxNumbers[i + itemInputSize]) {
					++inputNumbers[i + itemInputSize];
					break;
				}
				else {
					inputNumbers[i + itemInputSize] = 0;
					if (i == fluidInputSize - 1) {
						fluidEnd = true;
					}
				}
			}
		}
	}
}
