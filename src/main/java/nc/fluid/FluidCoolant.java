package nc.fluid;

import nc.util.ColorHelper;
import net.minecraft.init.SoundEvents;

public class FluidCoolant extends FluidBase {
	
	private static final Integer NAK_COLOR = 0xFFE5BC;
	
	public FluidCoolant(String fluidName) {
		super(fluidName, true);
		setViscosity(15000);
		setTemperature(300);
		setDensity(5000);
		setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA);
		setFillSound(SoundEvents.ITEM_BUCKET_FILL_LAVA);
	}
	
	public FluidCoolant(String fluidName, Integer colour) {
		super(fluidName, true, "molten", colour);
		setViscosity(15000);
		setTemperature(300);
		setDensity(5000);
		setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY_LAVA);
		setFillSound(SoundEvents.ITEM_BUCKET_FILL_LAVA);
	}
	
	public static int getNAKColor(Integer colour) {
		return ColorHelper.blend(colour.intValue(), NAK_COLOR, 0.4F);
	}
}
