package nc.recipe.ingredient;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import crafttweaker.api.minecraft.CraftTweakerMC;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import nc.recipe.IngredientMatchResult;
import nc.recipe.IngredientSorption;
import nc.util.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

public class ItemIngredient implements IItemIngredient {
	
	public ItemStack stack;

	public ItemIngredient(@Nonnull ItemStack stack) {
		this.stack = stack;
	}
	
	@Override
	public ItemStack getStack() {
		return stack == null ? null : stack.copy();
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
	public int getMaxStackSize(int ingredientNumber) {
		return stack.getCount();
	}
	
	@Override
	public void setMaxStackSize(int stackSize) {
		stack.setCount(stackSize);
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
	public IntList getFactors() {
		return new IntArrayList(Lists.newArrayList(stack.getCount()));
	}
	
	@Override
	public IItemIngredient getFactoredIngredient(int factor) {
		ItemStack newStack = stack.copy();
		newStack.setCount(stack.getCount()/factor);
		return new ItemIngredient(newStack);
	}
	
	@Override
	public IngredientMatchResult match(Object object, IngredientSorption type) {
		if (object instanceof ItemStack) {
			ItemStack itemstack = (ItemStack) object;
			if (!itemstack.isItemEqual(stack) || !ItemStackHelper.areItemStackTagsEqual(itemstack, stack)) {
				return IngredientMatchResult.FAIL;
			}
			return new IngredientMatchResult(type.checkStackSize(stack.getCount(), itemstack.getCount()), 0);
		}
		else if (object instanceof OreIngredient) {
			OreIngredient oreStack = (OreIngredient) object;
			//return (oreStack.matches(this, type));
			
			for (ItemStack itemStack : oreStack.cachedStackList) {
				if (match(itemStack, type).matches()) {
					return new IngredientMatchResult(type.checkStackSize(stack.getCount(), oreStack.stackSize), 0);
				}
			}
		}
		else if (object instanceof ItemIngredient && match(((ItemIngredient) object).stack, type).matches()) {
			return new IngredientMatchResult(type.checkStackSize(getMaxStackSize(0), ((ItemIngredient) object).getMaxStackSize(0)), 0);
		}
		return IngredientMatchResult.FAIL;
	}
	
	@Override
	public boolean isValid() {
		return stack != null;
	}
	
	// CraftTweaker
	
	@Override
	@Optional.Method(modid = "crafttweaker")
	public crafttweaker.api.item.IIngredient ct() {
		return CraftTweakerMC.getIItemStack(stack);
	}
}
