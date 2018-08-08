package nc.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class RecipeEmptyItemStack implements IItemIngredient {

	public RecipeEmptyItemStack() {}
	
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
	public boolean matches(Object object, SorptionType sorption) {
		if (object == null) return true;
		if (object instanceof ItemStack) {
			return ((ItemStack) object).isEmpty();
		}
		return object instanceof RecipeEmptyItemStack;
	}
}
