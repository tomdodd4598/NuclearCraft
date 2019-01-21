package nc.recipe.ingredient;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import nc.recipe.IngredientSorption;
import nc.util.OreDictHelper;
import net.minecraft.item.ItemStack;

public class OreIngredient implements IItemIngredient {
	
	public String oreName;
	public final List<ItemStack> cachedStackList;
	public int stackSize;
	
	public OreIngredient(String oreName, int stackSize) {
		this.oreName = oreName;
		cachedStackList = OreDictHelper.getPrioritisedStackList(oreName);
		this.stackSize = stackSize;
	}
	
	@Override
	public ItemStack getStack() {
		if (cachedStackList == null || cachedStackList.isEmpty() || cachedStackList.get(0) == null) return null;
		ItemStack item = cachedStackList.get(0).copy();
		item.setCount(stackSize);
		return item;
	}
	
	@Override
	public String getIngredientName() {
		return "ore:" + oreName;
	}
	
	@Override
	public String getIngredientNamesConcat() {
		return getIngredientName();
	}
	
	@Override
	public boolean matches(Object object, IngredientSorption type) {
		if (object instanceof OreIngredient) {
			OreIngredient oreStack = (OreIngredient)object;
			if (oreStack.oreName.equals(oreName) && type.checkStackSize(stackSize, oreStack.stackSize)) {
				return true;
			}
		}
		else if (object instanceof String) {
			return oreName.equals(object);
		}
		else if (object instanceof ItemStack && type.checkStackSize(stackSize, ((ItemStack) object).getCount())) {
			ItemStack itemstack = (ItemStack)object;
			if (itemstack.isEmpty()) return false;
			if (OreDictHelper.getOreNames(itemstack).contains(oreName)) return true;
		}
		else if (object instanceof ItemIngredient) {
			if (matches(((ItemIngredient) object).stack, type)) return true;
		}
		else if (object instanceof ItemArrayIngredient) {
			for (IItemIngredient ingredient : ((ItemArrayIngredient) object).ingredientList) if (!matches(ingredient, type)) return false;
			return true;
		}
		return false;
	}
	
	@Override
	public int getMaxStackSize() {
		return stackSize;
	}
	
	@Override
	public void setMaxStackSize(int stackSize) {
		this.stackSize = stackSize;
		for (ItemStack stack : cachedStackList) stack.setCount(stackSize);
	}
	
	@Override
	public List<ItemStack> getInputStackList() {
		List<ItemStack> stackList = new ArrayList<ItemStack>();
		for (ItemStack item : cachedStackList) {
			ItemStack itemStack = item.copy();
			itemStack.setCount(stackSize);
			stackList.add(itemStack);
		}
		return stackList;
	}
	
	@Override
	public List<ItemStack> getOutputStackList() {
		if (cachedStackList == null || cachedStackList.isEmpty()) return new ArrayList<ItemStack>();
		return Lists.newArrayList(getStack());
	}
	
	@Override
	public boolean isValid() {
		return cachedStackList != null && !cachedStackList.isEmpty() && cachedStackList.get(0) != null;
	}
}
