package nc.fluid;

import net.minecraft.init.SoundEvents;

public class FluidPlasma extends NCFluid {
	
	public FluidPlasma(String fluidName) {
		super(fluidName, false);
		setDensity(50);
		setViscosity(100);
		setTemperature(10000);
		setLuminosity(15);
		setEmptySound(SoundEvents.BLOCK_FIRE_EXTINGUISH);
		setFillSound(SoundEvents.BLOCK_FIRE_EXTINGUISH);
	}
}
