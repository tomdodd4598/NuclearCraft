package nc.fluid;

import net.minecraft.init.SoundEvents;

public class FluidGas extends NCFluid {
	
	public FluidGas(String fluidName) {
		super(fluidName, false);
		setDensity(-10);
		setGaseous(true);
		setViscosity(40);
		setEmptySound(SoundEvents.BLOCK_FIRE_EXTINGUISH);
		setFillSound(SoundEvents.BLOCK_FIRE_EXTINGUISH);
	}
	
	public FluidGas(String fluidName, Integer color) {
		super(fluidName, false, "gas", color);
		setDensity(-10);
		setGaseous(true);
		setViscosity(40);
		setEmptySound(SoundEvents.BLOCK_FIRE_EXTINGUISH);
		setFillSound(SoundEvents.BLOCK_FIRE_EXTINGUISH);
	}
}
