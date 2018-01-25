package nc.fluid;

import nc.util.ColorHelper;

public class FluidFission extends FluidMolten {
	
	private static final int FLUORINE_COLOR = 0xD3C85D;
	private static final int FLIBE_COLOR = 0xC1C8B0;
			
	public FluidFission(String fluidName) {
		super(fluidName);
	}
	
	public FluidFission(String fluidName, int colour) {
		super(fluidName, colour);
	}
	
	public static int getFluorideColor(int colour) {
		return ColorHelper.blend(colour, FLUORINE_COLOR, 0.125F);
	}
	
	public static int getFLIBEColor(int colour) {
		return ColorHelper.blend(colour, FLIBE_COLOR, 0.4F);
	}
}
