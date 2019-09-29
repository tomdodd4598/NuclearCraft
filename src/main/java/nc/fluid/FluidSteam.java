package nc.fluid;

import net.minecraft.init.SoundEvents;

public class FluidSteam extends FluidBase {
	
	public FluidSteam(String fluidName) {
		super(fluidName, false);
		setDensity(-10);
		setGaseous(true);
		setViscosity(40);
		setTemperature(450);
		setEmptySound(SoundEvents.BLOCK_FIRE_EXTINGUISH);
		setFillSound(SoundEvents.BLOCK_FIRE_EXTINGUISH);
	}
	
	public FluidSteam(String fluidName, Integer colour, Integer temperature) {
		super(fluidName, false, "steam", colour);
		setDensity(-10);
		setGaseous(true);
		setViscosity(40);
		setTemperature(temperature);
		setEmptySound(SoundEvents.BLOCK_FIRE_EXTINGUISH);
		setFillSound(SoundEvents.BLOCK_FIRE_EXTINGUISH);
	}
}
