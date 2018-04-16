package nc.util;

import java.util.List;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FluidStackHelper {
	
	public static FluidStack fixFluidStack(Object object) {
		if (object instanceof FluidStack) {
			FluidStack fluidstack = ((FluidStack) object).copy();
			if (fluidstack.amount == 0) {
				fluidstack.amount = 1000;
			}
			return fluidstack;
		} else {
			if (!(object instanceof Fluid)) {
				throw new RuntimeException(String.format("Invalid FluidStack: %s", object));
			}
			return new FluidStack((Fluid) object, 1000);
		}
	}
	
	public static String stackName(FluidStack stack) {
		return stack.getFluid().getName();
	}
	
	public static String stackListNames(List<FluidStack> list) {
		String names = "";
		for (FluidStack stack : list) names += (", " + stackName(stack));
		return names.substring(2);
	}
}
