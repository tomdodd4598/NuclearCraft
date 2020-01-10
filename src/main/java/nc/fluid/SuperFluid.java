package nc.fluid;

public class SuperFluid extends NCFluid {
	
	public SuperFluid(String fluidName) {
		super(fluidName, false);
		setViscosity(1);
		setTemperature(4);
		setDensity(150);
	}
}
