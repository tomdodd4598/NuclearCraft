package nc.recipe.ingredient;

import java.util.ArrayList;
import java.util.List;

import nc.recipe.SorptionType;
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
	public int getMaxStackSize() {
		return 0;
	}
	
	@Override
	public void setMaxStackSize(int stackSize) {
		
	}

	@Override
	public List<FluidStack> getInputStackList() {
		return new ArrayList();
	}
	
	@Override
	public List<FluidStack> getOutputStackList() {
		return new ArrayList();
	}

	@Override
	public boolean matches(Object object, SorptionType sorption) {
		if (object == null) return true;
		if (object instanceof Tank) {
			return ((Tank)object).getFluid() == null;
		}
		return object instanceof EmptyFluidIngredient;
	}
}
