package nc.recipe.ingredient;

import java.util.List;

import net.minecraftforge.fluids.FluidStack;

public interface IFluidIngredient extends IIngredient<FluidStack> {
	
	@Override
    default FluidStack getNextStack(int ingredientNumber) {
		FluidStack nextStack = getStack();
		nextStack.amount = getNextStackSize(ingredientNumber);
		return nextStack;
	}
	
	@Override
    default List<FluidStack> getInputStackHashingList() {
		return getInputStackList();
	}
	
	@Override
    IFluidIngredient getFactoredIngredient(int factor);
}
