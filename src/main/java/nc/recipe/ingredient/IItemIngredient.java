package nc.recipe.ingredient;

import java.util.ArrayList;
import java.util.List;

import nc.util.ItemStackHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

public interface IItemIngredient extends IIngredient<ItemStack> {
	
	@Override
	public default ItemStack getNextStack(int ingredientNumber) {
		ItemStack nextStack = getStack();
		nextStack.setCount(getNextStackSize(ingredientNumber));
		return nextStack;
	}
	
	@Override
	public default List<ItemStack> getInputStackHashingList() {
		List<ItemStack> list = new ArrayList<>();
		for (ItemStack stack : getInputStackList()) {
			if (stack != null && !stack.isEmpty() && ItemStackHelper.getMetadata(stack) == OreDictionary.WILDCARD_VALUE) {
				NonNullList<ItemStack> subStacks = NonNullList.create();
				stack.getItem().getSubItems(CreativeTabs.SEARCH, subStacks);
				for (ItemStack subStack : subStacks) {
					list.add(subStack);
				}
			}
			else list.add(stack);
		}
		return list;
	}
}
