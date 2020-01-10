package nc.fluid;

public class FluidFlammable extends NCFluid {
	
	public FluidFlammable(String fluidName) {
		super(fluidName, true);
		setDensity(800);
		setViscosity(800);
	}
	
	public FluidFlammable(String fluidName, Integer color) {
		super(fluidName, true, "liquid", color);
		setDensity(800);
		setViscosity(800);
	}
}
