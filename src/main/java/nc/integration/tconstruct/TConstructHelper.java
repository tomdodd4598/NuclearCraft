package nc.integration.tconstruct;

import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.*;

public class TConstructHelper {
	
	public static void addMaterialStats(Material material, IMaterialStats stats) {
		if (material == null || material.getStats(stats.getIdentifier()) != null) {
			return;
		}
		TinkerRegistry.addMaterialStats(material, stats);
	}
	
	public static void addMaterialStats(Material material, IMaterialStats... statsList) {
		for (IMaterialStats stat : statsList) {
			addMaterialStats(material, stat);
		}
	}
}
