package nc.block.fluid;

public class SuperFluid extends FluidBase {
	
	public SuperFluid(String fluidName) {
		super(fluidName, false);
		setViscosity(1);
		setTemperature(4);
		setDensity(150);
	}
}
