package nc.fluid;

import nc.util.ColorHelper;
import net.minecraft.init.SoundEvents;

public class FluidHotCoolant extends NCFluid {
	
	private static final Integer NAK_COLOR = 0xFFE5BC;
	
	public FluidHotCoolant(String fluidName) {
		super(fluidName, true);
		setViscosity(10000);
		setTemperature(700);
		setLuminosity(7);
		setDensity(4000);
		setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA);
		setFillSound(SoundEvents.ITEM_BUCKET_FILL_LAVA);
	}
	
	public FluidHotCoolant(String fluidName, Integer color) {
		super(fluidName, true, "molten", color);
		setViscosity(10000);
		setTemperature(700);
		setLuminosity(7);
		setDensity(4000);
		setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA);
		setFillSound(SoundEvents.ITEM_BUCKET_FILL_LAVA);
	}
	
	public static int getNAKColor(Integer colour) {
		return ColorHelper.blend(colour.intValue(), NAK_COLOR, 0.2F);
	}
}
