package nc.util;

import net.minecraft.block.material.*;

public class MaterialHelper {
	
	public static boolean isEmpty(Material material) {
		return material.isReplaceable() || material.equals(Material.AIR);
	}
	
	public static boolean isReplaceable(Material material) {
		return isEmpty(material) || material.getPushReaction().equals(EnumPushReaction.DESTROY);
	}
	
	public static boolean isFoliage(Material material) {
		return isFoliage(material.getMaterialMapColor());
	}
	
	public static boolean isFoliage(MapColor mapColor) {
		return mapColor.equals(MapColor.FOLIAGE);
	}
	
	public static boolean isGrass(Material material) {
		return isGrass(material.getMaterialMapColor());
	}
	
	public static boolean isGrass(MapColor mapColor) {
		return mapColor.equals(MapColor.GRASS);
	}
	
	public static boolean isDirt(Material material) {
		return isDirt(material.getMaterialMapColor());
	}
	
	public static boolean isDirt(MapColor mapColor) {
		return mapColor.equals(MapColor.DIRT) || isGrass(mapColor);
	}
	
	public static boolean isSnow(Material material) {
		return isSnow(material.getMaterialMapColor());
	}
	
	public static boolean isSnow(MapColor mapColor) {
		return mapColor.equals(MapColor.SNOW);
	}
	
	public static boolean isCloth(Material material) {
		return isCloth(material.getMaterialMapColor());
	}
	
	public static boolean isCloth(MapColor mapColor) {
		return mapColor.equals(MapColor.CLOTH);
	}
}
