package nc.recipe.ingredient;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import nc.recipe.IngredientMatchResult;
import nc.recipe.IngredientSorption;
import net.minecraft.item.ItemStack;

public class EmptyItemIngredient implements IItemIngredient {

	public EmptyItemIngredient() {}
	
	@Override
	public ItemStack getStack() {
		return null;
	}

	@Override
	public String getIngredientName() {
		return "null";
	}

	@Override
	public String getIngredientNamesConcat() {
		return "null";
	}

	@Override
	public int getMaxStackSize(int ingredientNumber) {
		return 0;
	}
	
	@Override
	public void setMaxStackSize(int stackSize) {
		
	}

	@Override
	public List<ItemStack> getInputStackList() {
		return new ArrayList<>();
	}
	
	@Override
	public List<ItemStack> getOutputStackList() {
		return new ArrayList<>();
	}
	
	@Override
	public List<ItemStack> getInputStackHashingList() {
		return Lists.newArrayList(ItemStack.EMPTY);
	}
	
	@Override
	public IngredientMatchResult match(Object object, IngredientSorption sorption) {
		if (object == null) return IngredientMatchResult.PASS_0;
		if (object instanceof ItemStack) {
			return new IngredientMatchResult(((ItemStack) object).isEmpty(), 0);
		}
		return new IngredientMatchResult(object instanceof EmptyItemIngredient, 0);
	}
	
	@Override
	public boolean isValid() {
		return true;
	}
}
