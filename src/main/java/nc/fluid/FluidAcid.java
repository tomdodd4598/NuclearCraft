package nc.fluid;

public class FluidAcid extends NCFluid {
	
	public FluidAcid(String fluidName) {
		super(fluidName, true);
	}
	
	public FluidAcid(String fluidName, Integer color) {
		super(fluidName, true, "liquid", color);
	}
}
