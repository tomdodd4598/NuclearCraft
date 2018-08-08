package nc.recipe;

import java.util.List;

import com.google.common.collect.Lists;

import nc.tile.internal.fluid.Tank;
import nc.util.FluidStackHelper;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class RecipeFluidStack implements IFluidIngredient {
	
	public FluidStack stack;
	public String fluidName;
	public int amount;

	public RecipeFluidStack(FluidStack stack) {
		this.stack = stack;
		fluidName = FluidStackHelper.stackName(stack);
		amount = stack.amount;
	}
	
	public RecipeFluidStack(String fluidName, int amount) {
		stack = FluidRegistry.getFluidStack(fluidName, amount);
		this.fluidName = fluidName;
		this.amount = amount;
	}
	
	@Override
	public FluidStack getStack() {
		return stack.copy();
	}
	
	@Override
	public String getIngredientName() {
		return fluidName;
	}
	
	@Override
	public String getIngredientNamesConcat() {
		return fluidName;
	}

	@Override
	public boolean matches(Object object, SorptionType type) {
		if (object instanceof Tank) object = (FluidStack)((Tank)object).getFluid();
		if (object instanceof FluidStack) {
			FluidStack fluidstack = (FluidStack) object;
			if (!fluidstack.isFluidEqual(stack) || !FluidStack.areFluidStackTagsEqual(fluidstack, stack)) {
				return false;
			}
			return type.checkStackSize(stack.amount, fluidstack.amount);
		}
		else if (object instanceof RecipeFluidStack) {
			if (matches(((RecipeFluidStack) object).stack, type)) return type.checkStackSize(getMaxStackSize(), ((RecipeFluidStack) object).getMaxStackSize());
		}
		return false;
	}

	@Override
	public List<FluidStack> getInputStackList() {
		return Lists.newArrayList(stack);
	}
	
	@Override
	public List<FluidStack> getOutputStackList() {
		return Lists.newArrayList(stack);
	}

	@Override
	public int getMaxStackSize() {
		return amount;
	}
	
	@Override
	public void setMaxStackSize(int stackSize) {
		amount = stackSize;
		stack.amount = stackSize;
	}
}
