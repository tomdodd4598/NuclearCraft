package nc.block.fluid;

public class FluidFlammable extends FluidBase {
	
	public FluidFlammable(String fluidName) {
		super(fluidName, true);
		setDensity(800);
		setViscosity(800);
	}
}
