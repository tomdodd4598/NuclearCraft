package nc.util;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialHelper {
	
	public static boolean isReplaceable(Material mat) {
		return mat.isReplaceable() || mat.getPushReaction() == EnumPushReaction.DESTROY || mat == Material.AIR;
	}
	
	public static boolean isEmpty(Material mat) {
		return mat.isReplaceable() || isFoliage(mat);
	}
	
	public static boolean isFoliage(Material mat) {
		return mat.getMaterialMapColor() == MapColor.FOLIAGE;
	}
	
	public static boolean isGrass(Material mat) {
		return mat.getMaterialMapColor() == MapColor.GRASS;
	}
	
	public static boolean isDirt(Material mat) {
		return mat.getMaterialMapColor() == MapColor.DIRT || isGrass(mat);
	}
}
