package nc.radiation;

import java.util.HashMap;
import java.util.Map;

import nc.config.NCConfig;
import nc.util.RegistryHelper;
import net.minecraft.world.biome.Biome;

public class RadBiomes {
	
	public static final Map<Biome, Double> BIOME_MAP = new HashMap<Biome, Double>();
	
	public static void init() {
		for (String biomeInfo : NCConfig.radiation_biomes) {
			int scorePos = biomeInfo.lastIndexOf('_');
			if (scorePos == -1) continue;
			Biome biome = RegistryHelper.biomeFromRegistry(biomeInfo.substring(0, scorePos));
			if (biome != null) BIOME_MAP.put(biome, Double.parseDouble(biomeInfo.substring(scorePos + 1)));
		}
	}
}
