package nc.fluid;

import net.minecraft.init.SoundEvents;

public class FluidHotGas extends FluidBase {
	
	public FluidHotGas(String fluidName) {
		super(fluidName, false);
		setDensity(-10);
		setGaseous(true);
		setViscosity(40);
		setTemperature(1000);
		setEmptySound(SoundEvents.BLOCK_FIRE_EXTINGUISH);
		setFillSound(SoundEvents.BLOCK_FIRE_EXTINGUISH);
	}
	
	public FluidHotGas(String fluidName, Integer colour) {
		super(fluidName, false, "gas", colour);
		setDensity(-10);
		setGaseous(true);
		setViscosity(40);
		setTemperature(1000);
		setEmptySound(SoundEvents.BLOCK_FIRE_EXTINGUISH);
		setFillSound(SoundEvents.BLOCK_FIRE_EXTINGUISH);
	}
}
