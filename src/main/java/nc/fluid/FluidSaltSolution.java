package nc.fluid;

public class FluidSaltSolution extends FluidBase {
	
	public FluidSaltSolution(String fluidName) {
		super(fluidName, true);
	}
	
	public FluidSaltSolution(String fluidName, Integer colour) {
		super(fluidName, true, "salt_solution", colour);
	}
}
