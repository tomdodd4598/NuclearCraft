package nc.recipe;

import java.util.List;

import net.minecraft.item.ItemStack;

public interface IItemIngredient {
	
	public ItemStack getStack();
	
	public default ItemStack getNextStack() {
		ItemStack nextStack = getStack().copy();
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
	
	public boolean matches(Object object, SorptionType sorption);
}
