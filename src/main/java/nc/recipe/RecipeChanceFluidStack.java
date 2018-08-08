package nc.recipe;

import java.util.ArrayList;
import java.util.List;

import nc.util.NCMathHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidStack;

public class RecipeChanceFluidStack implements IFluidIngredient {
	
	public IFluidIngredient ingredient;
	public int chancePercent;
	public int stackDiff;
	public int minStackSize;
	
	public RecipeChanceFluidStack(IFluidIngredient ingredient, int chancePercent, int stackDiff) {
		this(ingredient, chancePercent, stackDiff, 0);
	}
	
	public RecipeChanceFluidStack(IFluidIngredient ingredient, int chancePercent, int stackDiff, int minStackSize) {
		this.ingredient = ingredient;
		this.chancePercent = MathHelper.clamp(chancePercent, 0, 100);
		this.stackDiff = Math.max(1, stackDiff);
		this.minStackSize = MathHelper.clamp(minStackSize, 0, ingredient.getMaxStackSize());
		
		int sizeIncrSteps = (ingredient.getMaxStackSize() - this.minStackSize)/this.stackDiff;
		int sizeShift = ingredient.getMaxStackSize() - this.minStackSize - sizeIncrSteps*this.stackDiff;
		
		this.ingredient.setMaxStackSize(this.ingredient.getMaxStackSize() + sizeShift);
		this.minStackSize += sizeShift;
	}

	@Override
	public FluidStack getStack() {
		return ingredient.getStack();
	}

	@Override
	public String getIngredientName() {
		return ingredient.getIngredientName() + " [ " + chancePercent + "%, diff: " + stackDiff + ", min: " + minStackSize + " ]";
	}

	@Override
	public String getIngredientNamesConcat() {
		return ingredient.getIngredientNamesConcat() + " [ " + chancePercent + "%, diff: " + stackDiff + ", min: " + minStackSize + " ]";
	}

	@Override
	public int getMaxStackSize() {
		return ingredient.getMaxStackSize();
	}
	
	@Override
	public void setMaxStackSize(int stackSize) {
		ingredient.setMaxStackSize(stackSize);
	}
	
	@Override
	public int getNextStackSize() {
		return minStackSize + NCMathHelper.getBinomial(getMaxStackSize() - minStackSize, chancePercent);
	}

	@Override
	public List<FluidStack> getInputStackList() {
		List<FluidStack> stackList = new ArrayList<FluidStack>();
		for (FluidStack stack : ingredient.getInputStackList()) {
			int runningStackSize = minStackSize;
			while (runningStackSize <= getMaxStackSize()) {
				FluidStack newStack = stack.copy();
				newStack.amount = runningStackSize;
				stackList.add(newStack);
				runningStackSize += stackDiff;
			}
		}
		return stackList;
	}
	
	@Override
	public List<FluidStack> getOutputStackList() {
		List<FluidStack> stackList = new ArrayList<FluidStack>();
		int runningStackSize = minStackSize;
		while (runningStackSize <= getMaxStackSize()) {
			FluidStack newStack = getStack().copy();
			newStack.amount = runningStackSize;
			stackList.add(newStack);
			runningStackSize += stackDiff;
		}
		return stackList;
	}

	@Override
	public boolean matches(Object object, SorptionType sorption) {
		return ingredient.matches(object, sorption);
	}
}
