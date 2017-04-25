package nc.block.fluid;

import nc.Global;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class SuperFluid extends FluidBase {
	
	public SuperFluid(String fluidName) {
		super(fluidName);
		setViscosity(1);
		setTemperature(4);
	}
}
