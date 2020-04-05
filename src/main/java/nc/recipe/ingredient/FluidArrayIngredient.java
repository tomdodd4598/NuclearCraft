package nc.recipe.ingredient;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import crafttweaker.api.item.IngredientOr;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import nc.recipe.IngredientMatchResult;
import nc.recipe.IngredientSorption;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Optional;

public class FluidArrayIngredient implements IFluidIngredient {
	
	public List<IFluidIngredient> ingredientList;
	public List<FluidStack> cachedStackList = new ArrayList<>();
	
	public FluidArrayIngredient(IFluidIngredient... ingredients) {
		this(Lists.newArrayList(ingredients));
	}
	
	public FluidArrayIngredient(List<IFluidIngredient> ingredientList) {
		this.ingredientList = ingredientList;
		ingredientList.forEach(input -> cachedStackList.add(input.getStack()));
	}
	
	@Override
	public FluidStack getStack() {
		return isValid() ? cachedStackList.get(0).copy() : null;
	}
	
	@Override
	public List<FluidStack> getInputStackList() {
		List<FluidStack> stacks = new ArrayList<>();
		ingredientList.forEach(ingredient -> ingredient.getInputStackList().forEach(obj -> stacks.add(obj)));
		return stacks;
	}
	
	@Override
	public List<FluidStack> getOutputStackList() {
		return isValid() ? Lists.newArrayList(getStack()) : new ArrayList<>();
	}
	
	@Override
	public int getMaxStackSize(int ingredientNumber) {
		return ingredientList.get(ingredientNumber).getMaxStackSize(0);
	}
	
	@Override
	public void setMaxStackSize(int stackSize) {
		for (IFluidIngredient ingredient : ingredientList) {
			ingredient.setMaxStackSize(stackSize);
		}
		for (FluidStack stack : cachedStackList) {
			stack.amount = stackSize;
		}
	}
	
	@Override
	public String getIngredientName() {
		//return ingredientList.get(0).getIngredientName();
		return getIngredientNamesConcat();
	}
	
	@Override
	public String getIngredientNamesConcat() {
		String names = "";
		for (IFluidIngredient ingredient : ingredientList) {
			names += (", " + ingredient.getIngredientName());
		}
		return "{ " + names.substring(2) + " }";
	}
	
	public String getIngredientRecipeString() {
		String names = "";
		for (IFluidIngredient ingredient : ingredientList) {
			names += (", " + ingredient.getMaxStackSize(0) + " x " + ingredient.getIngredientName());
		}
		return "{ " + names.substring(2) + " }";
	}
	
	@Override
	public IntList getFactors() {
		IntList list = new IntArrayList();
		for (IFluidIngredient ingredient : ingredientList) {
			list.addAll(ingredient.getFactors());
		}
		return new IntArrayList(list);
	}
	
	@Override
	public IFluidIngredient getFactoredIngredient(int factor) {
		List<IFluidIngredient> list = new ArrayList<>();
		for (IFluidIngredient ingredient : ingredientList) {
			list.add(ingredient.getFactoredIngredient(factor));
		}
		return new FluidArrayIngredient(list);
	}
	
	@Override
	public IngredientMatchResult match(Object object, IngredientSorption sorption) {
		for (int i = 0; i < ingredientList.size(); i++) {
			if (ingredientList.get(i).match(object, sorption).matches()) {
				return new IngredientMatchResult(true, i);
			}
		}
		return IngredientMatchResult.FAIL;
	}
	
	@Override
	public boolean isValid() {
		return cachedStackList != null && !cachedStackList.isEmpty();
	}
	
	// CraftTweaker
	
	@Override
	@Optional.Method(modid = "crafttweaker")
	public crafttweaker.api.item.IIngredient ct() {
		crafttweaker.api.item.IIngredient[] array = new crafttweaker.api.item.IIngredient[ingredientList.size()];
		for (int i = 0; i < ingredientList.size(); i++) {
			array[i] = ingredientList.get(i).ct();
		}
		return new IngredientOr(array);
	}
}
