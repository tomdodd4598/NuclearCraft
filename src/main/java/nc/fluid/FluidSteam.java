package nc.fluid;

import net.minecraft.init.SoundEvents;

public class FluidSteam extends NCFluid {
	
	public FluidSteam(String fluidName) {
		super(fluidName, false);
		setDensity(-10);
		setGaseous(true);
		setViscosity(40);
		setTemperature(450);
		setEmptySound(SoundEvents.BLOCK_FIRE_EXTINGUISH);
		setFillSound(SoundEvents.BLOCK_FIRE_EXTINGUISH);
	}
	
	public FluidSteam(String fluidName, Integer color, Integer temperature) {
		super(fluidName, false, "steam", color);
		setDensity(-10);
		setGaseous(true);
		setViscosity(40);
		setTemperature(temperature);
		setEmptySound(SoundEvents.BLOCK_FIRE_EXTINGUISH);
		setFillSound(SoundEvents.BLOCK_FIRE_EXTINGUISH);
	}
}
