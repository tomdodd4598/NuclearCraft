package nc.fluid;

import net.minecraft.util.SoundEvent;

public class FluidSoul extends NCFluid {
	
	public FluidSoul(String fluidName, SoundEvent emptySound, SoundEvent fillSound) {
		super(fluidName, false);
		setDensity(-10);
		setGaseous(true);
		setViscosity(40);
		setEmptySound(emptySound);
		setFillSound(fillSound);
	}
	
	public FluidSoul(String fluidName, Integer color, SoundEvent emptySound, SoundEvent fillSound) {
		super(fluidName, false, "soul", color);
		setDensity(-10);
		setGaseous(true);
		setViscosity(40);
		setEmptySound(emptySound);
		setFillSound(fillSound);
	}
}
