package nc.fluid;

import net.minecraft.init.SoundEvents;

public class FluidSugar extends NCFluid {
	
	public FluidSugar(String fluidName) {
		super(fluidName, true);
		setViscosity(8000);
		setTemperature(350);
		setDensity(1150);
		setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA);
		setFillSound(SoundEvents.ITEM_BUCKET_FILL_LAVA);
	}
	
	public FluidSugar(String fluidName, Integer color) {
		super(fluidName, true, "molten", color);
		setViscosity(8000);
		setTemperature(350);
		setDensity(1150);
		setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA);
		setFillSound(SoundEvents.ITEM_BUCKET_FILL_LAVA);
	}
}
