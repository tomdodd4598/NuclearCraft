package nc.recipe.ingredient;

import java.util.ArrayList;
import java.util.List;

import nc.util.ItemStackHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
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
			int meta = ItemStackHelper.getMetadata(stack);
			if (stack != null && !stack.isEmpty() && meta == OreDictionary.WILDCARD_VALUE) {
				NonNullList<ItemStack> subStacks = NonNullList.create();
				Item item = stack.getItem();
				if (ItemBlock.class.isAssignableFrom(item.getClass())) {
					for (int i = 0; i < 16; i++) {
						subStacks.add(new ItemStack(item, stack.getCount(), i));
					}
				}
				else {
					stack.getItem().getSubItems(CreativeTabs.SEARCH, subStacks);
				}
				list.addAll(subStacks);
			}
			else list.add(stack);
		}
		return list;
	}
	
	@Override
	public IItemIngredient getFactoredIngredient(int factor);
}
