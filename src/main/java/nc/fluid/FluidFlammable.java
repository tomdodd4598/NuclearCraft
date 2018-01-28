package nc.fluid;

public class FluidFlammable extends FluidBase {
	
	public FluidFlammable(String fluidName) {
		super(fluidName, true);
		setDensity(800);
		setViscosity(800);
	}
	
	public FluidFlammable(String fluidName, Integer colour) {
		super(fluidName, true, "liquid", colour);
		setDensity(800);
		setViscosity(800);
	}
}
