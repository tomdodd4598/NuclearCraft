package nc.recipe.ingredient;

import net.minecraftforge.fluids.FluidStack;

public interface IFluidIngredient extends IIngredient<FluidStack> {
	
	@Override
	public default FluidStack getNextStack(int ingredientNumber) {
		FluidStack nextStack = getStack();
		nextStack.amount = getNextStackSize(ingredientNumber);
		return nextStack;
	}
}
