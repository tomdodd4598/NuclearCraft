package nc.fluid;

import net.minecraft.init.SoundEvents;

public class FluidMolten extends NCFluid {
	
	public FluidMolten(String fluidName) {
		super(fluidName, true);
		setViscosity(8000);
		setTemperature(1200);
		setLuminosity(10);
		setDensity(5000);
		setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA);
		setFillSound(SoundEvents.ITEM_BUCKET_FILL_LAVA);
	}
	
	public FluidMolten(String fluidName, Integer color) {
		super(fluidName, true, "molten", color);
		setViscosity(8000);
		setTemperature(1200);
		setLuminosity(10);
		setDensity(5000);
		setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA);
		setFillSound(SoundEvents.ITEM_BUCKET_FILL_LAVA);
	}
}
