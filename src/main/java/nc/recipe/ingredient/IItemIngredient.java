package nc.recipe.ingredient;

import net.minecraft.item.ItemStack;

public interface IItemIngredient extends IIngredient<ItemStack> {
	
	@Override
	public default ItemStack getNextStack(int ingredientNumber) {
		ItemStack nextStack = getStack();
		nextStack.setCount(getNextStackSize(ingredientNumber));
		return nextStack;
	}
}
