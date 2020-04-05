package nc.recipe.ingredient;

import java.util.ArrayList;
import java.util.List;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import nc.integration.crafttweaker.ingredient.CTChanceFluidIngredient;
import nc.recipe.IngredientMatchResult;
import nc.recipe.IngredientSorption;
import nc.util.NCMath;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Optional;

public class ChanceFluidIngredient implements IFluidIngredient {
	
	// ONLY USED AS AN OUTPUT, SO INGREDIENT NUMBER DOES NOT MATTER!
	
	public IFluidIngredient ingredient;
	public int chancePercent;
	public int stackDiff;
	public int minStackSize;
	public int sizeIncrSteps;
	public double meanStackSize;
	
	public ChanceFluidIngredient(IFluidIngredient ingredient, int chancePercent, int stackDiff) {
		this(ingredient, chancePercent, stackDiff, 0);
	}
	
	public ChanceFluidIngredient(IFluidIngredient ingredient, int chancePercent, int stackDiff, int minStackSize) {
		this.ingredient = ingredient;
		this.chancePercent = MathHelper.clamp(chancePercent, 0, 100);
		this.stackDiff = Math.max(1, stackDiff);
		this.minStackSize = MathHelper.clamp(minStackSize, 0, ingredient.getMaxStackSize(0));
		
		int sizeIncrSteps = (ingredient.getMaxStackSize(0) - this.minStackSize)/this.stackDiff;
		int sizeShift = ingredient.getMaxStackSize(0) - this.minStackSize - sizeIncrSteps*this.stackDiff;
		
		//this.ingredient.setMaxStackSize(this.ingredient.getMaxStackSize(0) + sizeShift);
		this.minStackSize += sizeShift;
		this.sizeIncrSteps = sizeIncrSteps;
		
		meanStackSize = this.minStackSize + (double)(this.ingredient.getMaxStackSize(0) - this.minStackSize)*(double)this.chancePercent/100D;
	}

	@Override
	public FluidStack getStack() {
		return ingredient.getStack();
	}
	
	@Override
	public List<FluidStack> getInputStackList() {
		List<FluidStack> stackList = new ArrayList<>();
		for (FluidStack stack : ingredient.getInputStackList()) {
			int runningStackSize = minStackSize;
			while (runningStackSize <= getMaxStackSize(0)) {
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
		List<FluidStack> stackList = new ArrayList<>();
		int runningStackSize = minStackSize;
		while (runningStackSize <= getMaxStackSize(0)) {
			FluidStack newStack = getStack().copy();
			newStack.amount = runningStackSize;
			stackList.add(newStack);
			runningStackSize += stackDiff;
		}
		return stackList;
	}
	
	@Override
	public int getMaxStackSize(int ingredientNumber) {
		return ingredient.getMaxStackSize(0);
	}
	
	@Override
	public void setMaxStackSize(int stackSize) {
		ingredient.setMaxStackSize(stackSize);
	}
	
	@Override
	public int getNextStackSize(int ingredientNumber) {
		return minStackSize + stackDiff*NCMath.getBinomial(sizeIncrSteps, chancePercent);
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
	public IntList getFactors() {
		IntList list = ingredient.getFactors();
		list.add(stackDiff);
		list.add(minStackSize);
		return new IntArrayList(list);
	}
	
	@Override
	public IFluidIngredient getFactoredIngredient(int factor) {
		return new ChanceFluidIngredient(ingredient.getFactoredIngredient(factor), chancePercent, stackDiff/factor, minStackSize/factor);
	}

	@Override
	public IngredientMatchResult match(Object object, IngredientSorption sorption) {
		return ingredient.match(object, sorption);
	}
	
	@Override
	public boolean isValid() {
		return ingredient.isValid();
	}
	
	// CraftTweaker
	
	@Override
	@Optional.Method(modid = "crafttweaker")
	public crafttweaker.api.item.IIngredient ct() {
		return CTChanceFluidIngredient.create(ingredient.ct(), chancePercent, stackDiff, minStackSize);
	}
}
