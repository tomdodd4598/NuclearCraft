package nc.recipe.ingredient;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import nc.recipe.IngredientMatchResult;
import nc.recipe.IngredientSorption;
import nc.tile.internal.fluid.Tank;
import net.minecraftforge.fluids.FluidStack;

public class EmptyFluidIngredient implements IFluidIngredient {

	public EmptyFluidIngredient() {}
	
	@Override
	public FluidStack getStack() {
		return null;
	}

	@Override
	public String getIngredientName() {
		return "null";
	}

	@Override
	public String getIngredientNamesConcat() {
		return "null";
	}

	@Override
	public int getMaxStackSize(int ingredientNumber) {
		return 0;
	}
	
	@Override
	public void setMaxStackSize(int stackSize) {
		
	}

	@Override
	public List<FluidStack> getInputStackList() {
		return new ArrayList<>();
	}
	
	@Override
	public List<FluidStack> getOutputStackList() {
		return new ArrayList<>();
	}
	
	@Override
	public List<FluidStack> getInputStackHashingList() {
		return Lists.newArrayList((FluidStack)null);
	}
	
	@Override
	public IngredientMatchResult match(Object object, IngredientSorption sorption) {
		if (object == null) return IngredientMatchResult.PASS_0;
		if (object instanceof Tank) {
			return new IngredientMatchResult(((Tank)object).getFluid() == null, 0);
		}
		return new IngredientMatchResult(object instanceof EmptyFluidIngredient, 0);
	}
	
	@Override
	public boolean isValid() {
		return true;
	}
}
