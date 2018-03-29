package nc.fluid;

import net.minecraft.init.SoundEvents;

public class FluidGlowstone extends FluidBase {
	
	public FluidGlowstone(String fluidName) {
		super(fluidName, true);
		setDensity(-10);
		setGaseous(true);
		setViscosity(8000);
		setTemperature(1200);
		setLuminosity(10);
		setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA);
		setFillSound(SoundEvents.ITEM_BUCKET_FILL_LAVA);
	}
	
	public FluidGlowstone(String fluidName, Integer colour) {
		super(fluidName, true, "molten", colour);
		setDensity(-10);
		setGaseous(true);
		setViscosity(8000);
		setTemperature(1200);
		setLuminosity(10);
		setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA);
		setFillSound(SoundEvents.ITEM_BUCKET_FILL_LAVA);
	}
}
