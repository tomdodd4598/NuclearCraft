package nc.fluid;

public class FluidAcid extends FluidBase {
	
	public FluidAcid(String fluidName) {
		super(fluidName, true);
	}
	
	public FluidAcid(String fluidName, Integer colour) {
		super(fluidName, true, "liquid", colour);
	}
}
