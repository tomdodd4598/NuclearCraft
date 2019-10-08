package nc.fluid;

public class FluidLiquid extends FluidBase {
	
	public FluidLiquid(String fluidName) {
		super(fluidName, true);
	}
	
	public FluidLiquid(String fluidName, Boolean opaque, Integer colour, Integer density, Integer temperature, Integer viscosity, Integer luminosity) {
		super(fluidName, true, opaque ? "liquid_opaque" : "liquid", colour);
		setDensity(density);
		setTemperature(temperature);
		setViscosity(viscosity);
		setLuminosity(luminosity);
	}
}
