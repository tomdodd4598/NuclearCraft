package nc.fluid;

import net.minecraft.init.SoundEvents;

public class FluidGlowstone extends NCFluid {
	
	public FluidGlowstone(String fluidName) {
		super(fluidName, true);
		setDensity(-500);
		setGaseous(true);
		setViscosity(8000);
		setTemperature(1200);
		setLuminosity(15);
		setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA);
		setFillSound(SoundEvents.ITEM_BUCKET_FILL_LAVA);
	}
	
	public FluidGlowstone(String fluidName, Integer color) {
		super(fluidName, true, "molten", color);
		setDensity(-500);
		setGaseous(true);
		setViscosity(8000);
		setTemperature(1200);
		setLuminosity(15);
		setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA);
		setFillSound(SoundEvents.ITEM_BUCKET_FILL_LAVA);
	}
}
