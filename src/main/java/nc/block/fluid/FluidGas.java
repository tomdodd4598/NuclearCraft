package nc.block.fluid;

import nc.Global;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidGas extends FluidBase {
	
	public FluidGas(String fluidName) {
		super(fluidName);
		setDensity(-10);
		setGaseous(true);
		setViscosity(40);
	}
}
