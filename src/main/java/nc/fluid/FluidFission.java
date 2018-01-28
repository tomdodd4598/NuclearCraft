package nc.fluid;

import nc.util.ColorHelper;

public class FluidFission extends FluidMolten {
	
	private static final Integer FLUORINE_COLOR = 0xD3C85D;
	private static final Integer FLIBE_COLOR = 0xC1C8B0;
			
	public FluidFission(String fluidName) {
		super(fluidName);
	}
	
	public FluidFission(String fluidName, Integer colour) {
		super(fluidName, colour);
	}
	
	public static int getFluorideColor(Integer colour) {
		return ColorHelper.blend(colour.intValue(), FLUORINE_COLOR, 0.125F);
	}
	
	public static int getFLIBEColor(Integer colour) {
		return ColorHelper.blend(colour.intValue(), FLIBE_COLOR, 0.4F);
	}
}
