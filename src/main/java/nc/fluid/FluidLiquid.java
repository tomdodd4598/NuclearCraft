package nc.fluid;

public class FluidLiquid extends FluidBase {
	
	public FluidLiquid(String fluidName) {
		super(fluidName, true);
	}
	
	public FluidLiquid(String fluidName, Integer colour) {
		super(fluidName, true, "liquid", colour);
	}
	
	public FluidLiquid(String fluidName, Boolean opaque, Integer colour) {
		super(fluidName, true, opaque ? "liquid_opaque" : "liquid", colour);
	}
}
