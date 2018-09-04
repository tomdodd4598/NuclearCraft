package nc.recipe.ingredient;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import nc.recipe.SorptionType;
import nc.util.ItemStackHelper;
import net.minecraft.item.ItemStack;

public class ItemIngredient implements IItemIngredient {
	
	public ItemStack stack;

	public ItemIngredient(@Nonnull ItemStack stack) {
		this.stack = stack;
	}
	
	@Override
	public ItemStack getStack() {
		return stack.copy();
	}
	
	@Override
	public String getIngredientName() {
		return ItemStackHelper.stackName(stack);
	}
	
	@Override
	public String getIngredientNamesConcat() {
		return ItemStackHelper.stackName(stack);
	}

	@Override
	public boolean matches(Object object, SorptionType type) {
		if (object instanceof ItemStack) {
			ItemStack itemstack = (ItemStack) object;
			if (!itemstack.isItemEqual(stack) || !ItemStack.areItemStackTagsEqual(itemstack, stack)) {
				return false;
			}
			return type.checkStackSize(stack.getCount(), itemstack.getCount());
		}
		else if (object instanceof OreIngredient) {
			OreIngredient oreStack = (OreIngredient) object;
			//return (oreStack.matches(this, type));
			
			for (ItemStack itemStack : oreStack.cachedStackList) {
				if (matches(itemStack, type)) return type.checkStackSize(stack.getCount(), oreStack.stackSize);
			}
		}
		else if (object instanceof ItemIngredient) {
			if (matches(((ItemIngredient) object).stack, type)) return type.checkStackSize(getMaxStackSize(), ((ItemIngredient) object).getMaxStackSize());
		}
		return false;
	}

	@Override
	public List<ItemStack> getInputStackList() {
		return Lists.newArrayList(stack);
	}
	
	@Override
	public List<ItemStack> getOutputStackList() {
		return Lists.newArrayList(stack);
	}

	@Override
	public int getMaxStackSize() {
		return stack.getCount();
	}
	
	@Override
	public void setMaxStackSize(int stackSize) {
		stack.setCount(stackSize);
	}
}
