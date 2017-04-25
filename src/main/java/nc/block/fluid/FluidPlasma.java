package nc.block.fluid;

import nc.Global;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidPlasma extends FluidBase {
	
	public FluidPlasma(String fluidName) {
		super(fluidName);
		setGaseous(false);
		setViscosity(100);
		setTemperature(10000);
	}
}
