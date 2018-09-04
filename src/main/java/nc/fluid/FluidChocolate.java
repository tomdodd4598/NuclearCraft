package nc.fluid;

import net.minecraft.init.SoundEvents;

public class FluidChocolate extends FluidBase {
	
	public FluidChocolate(String fluidName) {
		super(fluidName, true);
		setViscosity(5000);
		setTemperature(330);
		setDensity(1325);
		setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA);
		setFillSound(SoundEvents.ITEM_BUCKET_FILL_LAVA);
	}
	
	public FluidChocolate(String fluidName, Integer colour) {
		super(fluidName, true, "molten", colour);
		setViscosity(5000);
		setTemperature(330);
		setDensity(1325);
		setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA);
		setFillSound(SoundEvents.ITEM_BUCKET_FILL_LAVA);
	}
}
