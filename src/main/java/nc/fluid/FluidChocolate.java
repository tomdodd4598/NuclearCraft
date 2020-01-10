package nc.fluid;

import net.minecraft.init.SoundEvents;

public class FluidChocolate extends NCFluid {
	
	public FluidChocolate(String fluidName) {
		super(fluidName, true);
		setViscosity(5000);
		setTemperature(330);
		setDensity(1325);
		setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA);
		setFillSound(SoundEvents.ITEM_BUCKET_FILL_LAVA);
	}
	
	public FluidChocolate(String fluidName, Integer color) {
		super(fluidName, true, "molten", color);
		setViscosity(5000);
		setTemperature(330);
		setDensity(1325);
		setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA);
		setFillSound(SoundEvents.ITEM_BUCKET_FILL_LAVA);
	}
}
