package nc.util;

import java.awt.Color;

import net.minecraft.util.math.MathHelper;

public class ColorHelper {
	
	public static int fixColor(int rgb) {
		int color = rgb;
		if(((color >> 24) & 0xFF) == 0) color |= 0xFF << 24;
		return color;
	}
	
	public static Color getColor(int rgb) {
		return new Color(fixColor(rgb));
	}
	
	public static int getRed(int rgb) {
		return getColor(rgb).getRed();
	}
	
	public static int getGreen(int rgb) {
		return getColor(rgb).getGreen();
	}
	
	public static int getBlue(int rgb) {
		return getColor(rgb).getBlue();
	}
	
	public static int blend(int color1, int color2, float blendRatio) {
		blendRatio = MathHelper.clamp(blendRatio, 0F, 1F);
		
		int alpha1 = (color1 >> 24 & 0xFF);
		int red1 = ((color1 & 0xFF0000) >> 16);
		int green1 = ((color1 & 0xFF00) >> 8);
		int blue1 = (color1 & 0xFF);
		
		int alpha2 = (color2 >> 24 & 0xFF);
		int red2 = ((color2 & 0xFF0000) >> 16);
		int green2 = ((color2 & 0xFF00) >> 8);
		int blue2 = (color2 & 0xFF);
		
		int alpha = Math.max(alpha1, alpha2);
		int red = (int) (red1 + (red2 - red1)*blendRatio);
		int green = (int) (green1 + (green2 - green1)*blendRatio);
		int blue = (int) (blue1 + (blue2 - blue1)*blendRatio);
		
		return alpha << 24 | red << 16 | green << 8 | blue;
	}
	
	public static int glow(int color1, int color2, float glowFactor) {
		int alpha1 = (color1 >> 24 & 0xFF);
		int red1 = ((color1 & 0xFF0000) >> 16);
		int green1 = ((color1 & 0xFF00) >> 8);
		int blue1 = (color1 & 0xFF);
		
		glowFactor = MathHelper.clamp(glowFactor, 0F, 1F)*255F/Math.max(red1, Math.max(green1, blue1));
		
		int alpha2 = (color2 >> 24 & 0xFF);
		int red2 = ((color2 & 0xFF0000) >> 16);
		int green2 = ((color2 & 0xFF00) >> 8);
		int blue2 = (color2 & 0xFF);
		
		int alpha = Math.max(alpha1, alpha2);
		int red = (int) (red1 + (red2 - red1)*glowFactor*red1/255F);
		int green = (int) (green1 + (green2 - green1)*glowFactor*green1/255F);
		int blue = (int) (blue1 + (blue2 - blue1)*glowFactor*blue1/255F);
		
		return alpha << 24 | red << 16 | green << 8 | blue;
	}
}
