package nc.util;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.MathHelper;

public class GuiHelper {
	
	public static int getScreenEdgeFromAngleX(ScaledResolution res, double angle) {
		int width = res.getScaledWidth();
		if (angle >= 45D && angle <= 135D) {
			return width;
		}
		if (angle >= 225D && angle <= 315D) {
			return 0;
		}
		
		double radAngle = Math.toRadians(angle - 45D);
		double cos = Math.cos(radAngle);
		double sin = Math.sin(radAngle);
		
		return NCMath.toInt(Math.round(0.5D * width * (1 + cos * Math.abs(cos) + sin * Math.abs(sin))));
	}
	
	public static int getScreenEdgeFromAngleY(ScaledResolution res, double angle) {
		int height = res.getScaledHeight();
		if (angle >= 315D || angle <= 45D) {
			return 0;
		}
		if (angle >= 135D && angle <= 225D) {
			return height;
		}
		
		double radAngle = Math.toRadians(angle - 45D);
		double cos = Math.cos(radAngle);
		double sin = Math.sin(radAngle);
		
		return NCMath.toInt(Math.round(0.5D * height * (1 - cos * Math.abs(cos) + sin * Math.abs(sin))));
	}
	
	public static int getRenderPositionX(ScaledResolution res, int edgeX, int width, int cushion) {
		return MathHelper.clamp(edgeX, cushion, res.getScaledWidth() - width - cushion);
	}
	
	public static int getRenderPositionY(ScaledResolution res, int edgeY, int height, int cushion) {
		return MathHelper.clamp(edgeY, cushion, res.getScaledHeight() - height - cushion);
	}
	
	public static int getRenderPositionXFromAngle(ScaledResolution res, double angle, int width, int cushion) {
		return MathHelper.clamp(getScreenEdgeFromAngleX(res, angle), cushion, res.getScaledWidth() - width - cushion);
	}
	
	public static int getRenderPositionYFromAngle(ScaledResolution res, double angle, int height, int cushion) {
		return MathHelper.clamp(getScreenEdgeFromAngleY(res, angle), cushion, res.getScaledHeight() - height - cushion);
	}
}
