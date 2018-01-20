package nc.fluid;

public class FluidLiquid extends FluidBase {
	
	public FluidLiquid(String fluidName) {
		super(fluidName, true);
	}
	
	public FluidLiquid(String fluidName, int colour) {
		super(fluidName, true, "liquid", colour);
	}
}
