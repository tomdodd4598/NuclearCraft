package nc.radiation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nc.config.NCConfig;
import nc.util.RegistryHelper;
import net.minecraft.world.biome.Biome;

public class RadBiomes {
	
	public static final Map<Biome, Double> RAD_MAP = new HashMap<Biome, Double>();
	public static final Map<Biome, Double> LIMIT_MAP = new HashMap<Biome, Double>();
	public static final Set<Integer> DIM_BLACKLIST = new HashSet<Integer>();
	
	public static void init() {
		for (String biomeInfo : NCConfig.radiation_biomes) {
			int scorePos = biomeInfo.lastIndexOf('_');
			if (scorePos == -1) continue;
			Biome biome = RegistryHelper.biomeFromRegistry(biomeInfo.substring(0, scorePos));
			if (biome != null) RAD_MAP.put(biome, Double.parseDouble(biomeInfo.substring(scorePos + 1)));
		}
		
		for (String biomeInfo : NCConfig.radiation_biome_limits) {
			int scorePos = biomeInfo.lastIndexOf('_');
			if (scorePos == -1) continue;
			Biome biome = RegistryHelper.biomeFromRegistry(biomeInfo.substring(0, scorePos));
			if (biome != null) LIMIT_MAP.put(biome, Double.parseDouble(biomeInfo.substring(scorePos + 1)));
		}
		
		for (int dim : NCConfig.radiation_from_biomes_dims_blacklist) DIM_BLACKLIST.add(dim);
	}
}
