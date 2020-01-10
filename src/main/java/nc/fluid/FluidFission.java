package nc.fluid;

import nc.util.ColorHelper;

public class FluidFission extends FluidMolten {
	
	private static final Integer FLUORINE_COLOR = 0xD3C85D;
	private static final Integer FLIBE_COLOR = 0xC1C8B0;
	private static final Integer ZA_COLOR = 0xDBDBAD;
			
	public FluidFission(String fluidName) {
		super(fluidName);
	}
	
	public FluidFission(String fluidName, Integer color) {
		super(fluidName, color);
	}
	
	public static int getFluorideColor(Integer color) {
		return ColorHelper.blend(color.intValue(), FLUORINE_COLOR, 0.125F);
	}
	
	public static int getFLIBEColor(Integer color) {
		return ColorHelper.blend(color.intValue(), FLIBE_COLOR, 0.4F);
	}
	
	public static int getZAColor(Integer color) {
		return ColorHelper.glow(color.intValue(), ZA_COLOR, 0.4F);
	}
}
