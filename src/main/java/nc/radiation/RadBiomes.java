package nc.radiation;

import static nc.config.NCConfig.*;

import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.*;
import nc.util.RegistryHelper;
import net.minecraft.world.biome.Biome;

public class RadBiomes {
	
	public static final Object2DoubleMap<Biome> RAD_MAP = new Object2DoubleOpenHashMap<>();
	public static final Object2DoubleMap<Biome> LIMIT_MAP = new Object2DoubleOpenHashMap<>();
	public static final IntSet DIM_BLACKLIST = new IntOpenHashSet();
	
	public static void init() {
		for (String biomeInfo : radiation_biomes) {
			int scorePos = biomeInfo.lastIndexOf('_');
			if (scorePos == -1) {
				continue;
			}
			Biome biome = RegistryHelper.biomeFromRegistry(biomeInfo.substring(0, scorePos));
			if (biome != null) {
				RAD_MAP.put(biome, Double.parseDouble(biomeInfo.substring(scorePos + 1)));
			}
		}
		
		for (String biomeInfo : radiation_biome_limits) {
			int scorePos = biomeInfo.lastIndexOf('_');
			if (scorePos == -1) {
				continue;
			}
			Biome biome = RegistryHelper.biomeFromRegistry(biomeInfo.substring(0, scorePos));
			if (biome != null) {
				LIMIT_MAP.put(biome, Double.parseDouble(biomeInfo.substring(scorePos + 1)));
			}
		}
		
		for (int dim : radiation_from_biomes_dims_blacklist) {
			DIM_BLACKLIST.add(dim);
		}
	}
}
