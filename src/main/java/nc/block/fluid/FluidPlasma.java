package nc.block.fluid;

import net.minecraft.init.SoundEvents;

public class FluidPlasma extends FluidBase {
	
	public FluidPlasma(String fluidName) {
		super(fluidName, false);
		setViscosity(500);
		setTemperature(10000);
		setLuminosity(15);
		setDensity(500);
		setEmptySound(SoundEvents.BLOCK_FIRE_EXTINGUISH);
		setFillSound(SoundEvents.BLOCK_FIRE_EXTINGUISH);
	}
}
