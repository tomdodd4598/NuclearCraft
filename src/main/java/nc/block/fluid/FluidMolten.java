package nc.block.fluid;

import net.minecraft.init.SoundEvents;

public class FluidMolten extends FluidBase {
	
	public FluidMolten(String fluidName) {
		super(fluidName, true);
		setViscosity(5000);
		setTemperature(1000);
		setLuminosity(10);
		setDensity(5000);
		setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA);
		setFillSound(SoundEvents.ITEM_BUCKET_FILL_LAVA);
	}
}
