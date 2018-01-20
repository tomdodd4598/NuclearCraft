package nc.fluid;

import net.minecraft.init.SoundEvents;

public class FluidMolten extends FluidBase {
	
	public FluidMolten(String fluidName) {
		super(fluidName, true);
		setViscosity(8000);
		setTemperature(1000);
		setLuminosity(10);
		setDensity(5000);
		setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA);
		setFillSound(SoundEvents.ITEM_BUCKET_FILL_LAVA);
	}
	
	public FluidMolten(String fluidName, int colour) {
		super(fluidName, true, "molten", colour);
		setViscosity(8000);
		setTemperature(1000);
		setLuminosity(10);
		setDensity(5000);
		setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA);
		setFillSound(SoundEvents.ITEM_BUCKET_FILL_LAVA);
	}
}
