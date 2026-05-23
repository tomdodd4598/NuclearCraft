package nc.util;

import net.minecraft.block.material.*;

public class MaterialHelper {
	
	public static boolean isEmpty(Material mat) {
		return mat.isReplaceable() || mat.equals(Material.AIR);
	}
	
	public static boolean isReplaceable(Material mat) {
		return isEmpty(mat) || mat.getPushReaction().equals(EnumPushReaction.DESTROY);
	}
	
	public static boolean isFoliage(Material mat) {
		return mat.getMaterialMapColor().equals(MapColor.FOLIAGE);
	}
	
	public static boolean isGrass(Material mat) {
		return mat.getMaterialMapColor().equals(MapColor.GRASS);
	}
	
	public static boolean isDirt(Material mat) {
		return mat.getMaterialMapColor().equals(MapColor.DIRT) || isGrass(mat);
	}
	
	public static boolean isSnow(Material mat) {
		return mat.getMaterialMapColor().equals(MapColor.SNOW);
	}
	
	public static boolean isCloth(Material mat) {
		return mat.getMaterialMapColor().equals(MapColor.CLOTH);
	}
}
