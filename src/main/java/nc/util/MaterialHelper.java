package nc.util;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialHelper {
	
	public static boolean isReplaceable(Material mat) {
		return mat.getMobilityFlag() == EnumPushReaction.DESTROY || mat == Material.AIR;
	}
	
	public static boolean isEmpty(Material mat) {
		return mat.isReplaceable() || mat.getMaterialMapColor() == MapColor.FOLIAGE;
	}
}
