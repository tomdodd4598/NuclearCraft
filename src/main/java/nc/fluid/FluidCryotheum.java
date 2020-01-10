package nc.fluid;

import net.minecraft.init.SoundEvents;

public class FluidCryotheum extends NCFluid {
	
	public FluidCryotheum(String fluidName) {
		super(fluidName, true);
		setViscosity(8000);
		setTemperature(50);
		setLuminosity(7);
		setDensity(5000);
		setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA);
		setFillSound(SoundEvents.ITEM_BUCKET_FILL_LAVA);
	}
	
	public FluidCryotheum(String fluidName, Integer color) {
		super(fluidName, true, "molten", color);
		setViscosity(8000);
		setTemperature(50);
		setLuminosity(7);
		setDensity(5000);
		setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA);
		setFillSound(SoundEvents.ITEM_BUCKET_FILL_LAVA);
	}
}
