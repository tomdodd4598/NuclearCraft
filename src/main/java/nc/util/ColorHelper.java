package nc.util;

import crafttweaker.annotations.ZenRegister;
import net.minecraft.util.math.MathHelper;
import stanhebben.zenscript.annotations.*;

import java.awt.*;

@ZenClass("mods.nuclearcraft.ColorHelper")
@ZenRegister
public class ColorHelper {
	
	public static Color getColor(int color) {
		return new Color(color);
	}
	
	@ZenMethod
	public static int getAlpha(int color) {
		return color >> 24 & 0xFF;
	}
	
	@ZenMethod
	public static int getRed(int color) {
		return (color & 0xFF0000) >> 16;
	}
	
	@ZenMethod
	public static int getGreen(int color) {
		return (color & 0xFF00) >> 8;
	}
	
	@ZenMethod
	public static int getBlue(int color) {
		return color & 0xFF;
	}
	
	@ZenMethod
	public static int blend(int color1, int color2, float blendRatio) {
		blendRatio = MathHelper.clamp(blendRatio, 0F, 1F);
		
		int alpha1 = getAlpha(color1);
		int red1 = getRed(color1);
		int green1 = getGreen(color1);
		int blue1 = getBlue(color1);
		
		int alpha2 = getAlpha(color2);
		int red2 = getRed(color2);
		int green2 = getGreen(color2);
		int blue2 = getBlue(color2);
		
		int alpha = Math.max(alpha1, alpha2);
		int red = NCMath.toInt(red1 + (red2 - red1) * blendRatio);
		int green = NCMath.toInt(green1 + (green2 - green1) * blendRatio);
		int blue = NCMath.toInt(blue1 + (blue2 - blue1) * blendRatio);
		
		return alpha << 24 | red << 16 | green << 8 | blue;
	}
	
	@ZenMethod
	public static int glow(int color1, int color2, float glowFactor) {
		int alpha1 = getAlpha(color1);
		int red1 = getRed(color1);
		int green1 = getGreen(color1);
		int blue1 = getBlue(color1);
		
		int alpha2 = getAlpha(color2);
		int red2 = getRed(color2);
		int green2 = getGreen(color2);
		int blue2 = getBlue(color2);
		
		glowFactor = MathHelper.clamp(glowFactor, 0F, 1F) * 255F / Math.max(red1, Math.max(green1, blue1));
		
		int alpha = Math.max(alpha1, alpha2);
		int red = NCMath.toInt(red1 + (red2 - red1) * glowFactor * red1 / 255F);
		int green = NCMath.toInt(green1 + (green2 - green1) * glowFactor * green1 / 255F);
		int blue = NCMath.toInt(blue1 + (blue2 - blue1) * glowFactor * blue1 / 255F);
		
		return alpha << 24 | red << 16 | green << 8 | blue;
	}
	
	// Specific color methods
	
	@ZenMethod
	public static int waterBlend(int soluteColor, float blendRatio) {
		return ColorHelper.blend(0x2F43F4, soluteColor, blendRatio);
	}
	
	@ZenMethod
	public static int getNAKColor(int color) {
		return ColorHelper.blend(color, 0xFFE5BC, 0.375F);
	}
	
	@ZenMethod
	public static int getFluorideColor(int color) {
		return ColorHelper.blend(color, 0xD3C85D, 0.125F);
	}
	
	@ZenMethod
	public static int getFLIBEColor(int color) {
		return ColorHelper.blend(color, 0xC1C8B0, 0.4F);
	}
	
	@ZenMethod
	public static int getZAColor(int color) {
		return ColorHelper.glow(color, 0xDBDBAD, 0.4F);
	}
}
