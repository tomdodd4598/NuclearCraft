package nc.recipe.ingredient;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import nc.recipe.IngredientSorption;
import net.minecraft.item.ItemStack;

public class ItemArrayIngredient implements IItemIngredient {
	
	public List<IItemIngredient> ingredientList;
	public List<ItemStack> cachedStackList = new ArrayList<ItemStack>();
	
	public ItemArrayIngredient(IItemIngredient... ingredients) {
		this(Lists.newArrayList(ingredients));
	}

	public ItemArrayIngredient(List<IItemIngredient> ingredientList) {
		this.ingredientList = ingredientList;
		ingredientList.forEach(input -> cachedStackList.add(input.getStack()));
	}

	@Override
	public ItemStack getStack() {
		if (cachedStackList == null || cachedStackList.isEmpty()) return null;
		return cachedStackList.get(0);
	}
	
	@Override
	public String getIngredientName() {
		return ingredientList.get(0).getIngredientName();
	}
	
	@Override
	public String getIngredientNamesConcat() {
		String names = "";
		for (IItemIngredient ingredient : ingredientList) names += (", " + ingredient.getIngredientName());
		return names.substring(2);
	}

	@Override
	public int getMaxStackSize() {
		return ingredientList.get(0).getMaxStackSize();
	}
	
	@Override
	public void setMaxStackSize(int stackSize) {
		for (IItemIngredient ingredient : ingredientList) ingredient.setMaxStackSize(stackSize);
		for (ItemStack stack : cachedStackList) stack.setCount(stackSize);
	}

	@Override
	public List<ItemStack> getInputStackList() {
		List<ItemStack> stacks = new ArrayList<ItemStack>();
		ingredientList.forEach(ingredient -> ingredient.getInputStackList().forEach(obj -> stacks.add(obj)));
		return stacks;
	}
	
	@Override
	public List<ItemStack> getOutputStackList() {
		if (cachedStackList == null || cachedStackList.isEmpty()) return new ArrayList<ItemStack>();
		return Lists.newArrayList(getStack());
	}

	@Override
	public boolean matches(Object object, IngredientSorption sorption) {
		for (IItemIngredient ingredient : ingredientList) {
			if (ingredient.matches(object, sorption)) {
				return true;
			}
		}
		return false;
	}
}
