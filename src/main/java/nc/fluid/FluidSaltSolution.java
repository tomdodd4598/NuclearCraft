package nc.fluid;

public class FluidSaltSolution extends NCFluid {
	
	public FluidSaltSolution(String fluidName) {
		super(fluidName, true);
	}
	
	public FluidSaltSolution(String fluidName, Integer color) {
		super(fluidName, true, "salt_solution", color);
	}
}
