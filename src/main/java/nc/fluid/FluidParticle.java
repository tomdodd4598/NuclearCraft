package nc.fluid;

import net.minecraft.init.SoundEvents;

public class FluidParticle extends NCFluid {
	
	public FluidParticle(String fluidName) {
		super(fluidName, false);
		setDensity(10000);
		setViscosity(40);
		setLuminosity(7);
		setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA);
		setFillSound(SoundEvents.ITEM_BUCKET_FILL_LAVA);
	}
}
