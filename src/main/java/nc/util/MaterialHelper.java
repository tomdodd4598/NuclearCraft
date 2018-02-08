package nc.util;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;

public class MaterialHelper {
	
	public static boolean isReplaceable(Material mat) {
		return mat.getMobilityFlag() == EnumPushReaction.DESTROY || mat == Material.AIR;
	}
}
