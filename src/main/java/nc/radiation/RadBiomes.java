package nc.radiation;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import nc.config.NCConfig;
import nc.util.RegistryHelper;
import net.minecraft.world.biome.Biome;

public class RadBiomes {
	
	public static final Object2DoubleMap<Biome> RAD_MAP = new Object2DoubleOpenHashMap<>();
	public static final Object2DoubleMap<Biome> LIMIT_MAP = new Object2DoubleOpenHashMap<>();
	public static final IntSet DIM_BLACKLIST = new IntOpenHashSet();
	
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
		
		for (int dim : NCConfig.radiation_from_biomes_dims_blacklist) {
			DIM_BLACKLIST.add(dim);
		}
	}
}
