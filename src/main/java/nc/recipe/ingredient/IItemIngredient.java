package nc.recipe.ingredient;

import java.util.List;

import nc.recipe.IngredientSorption;
import net.minecraft.item.ItemStack;

public interface IItemIngredient {
	
	public ItemStack getStack();
	
	public default ItemStack getNextStack() {
		ItemStack nextStack = getStack();
		nextStack.setCount(getNextStackSize());
		return nextStack;
	}
	
	public List<ItemStack> getInputStackList();
	
	public List<ItemStack> getOutputStackList();

	public String getIngredientName();
	
	public String getIngredientNamesConcat();
	
	public default int getNextStackSize() {
		return getMaxStackSize();
	}
	
	public int getMaxStackSize();
	
	public void setMaxStackSize(int stackSize);
	
	public boolean matches(Object object, IngredientSorption sorption);
	
	public boolean isValid();
}
