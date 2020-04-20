package nc.fluid;

public class FluidLiquid extends NCFluid {
	
	public FluidLiquid(String fluidName) {
		super(fluidName, true);
	}
	
	public FluidLiquid(String fluidName, Boolean opaque, Integer color, Integer density, Integer temperature, Integer viscosity, Integer luminosity) {
		super(fluidName, true, opaque == null ? fluidName : (opaque ? "liquid_opaque" : "liquid"), color);
		setDensity(density);
		setTemperature(temperature);
		setViscosity(viscosity);
		setLuminosity(luminosity);
	}
}
