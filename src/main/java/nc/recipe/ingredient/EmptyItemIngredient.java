package nc.recipe.ingredient;

import java.util.ArrayList;
import java.util.List;

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
	public int getMaxStackSize() {
		return 0;
	}
	
	@Override
	public void setMaxStackSize(int stackSize) {
		
	}

	@Override
	public List<ItemStack> getInputStackList() {
		return new ArrayList();
	}
	
	@Override
	public List<ItemStack> getOutputStackList() {
		return new ArrayList();
	}

	@Override
	public boolean matches(Object object, IngredientSorption sorption) {
		if (object == null) return true;
		if (object instanceof ItemStack) {
			return ((ItemStack) object).isEmpty();
		}
		return object instanceof EmptyItemIngredient;
	}
}
