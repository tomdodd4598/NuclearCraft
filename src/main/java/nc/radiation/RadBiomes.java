package nc.radiation;

import java.util.HashMap;
import java.util.Map;

import nc.config.NCConfig;
import nc.worldgen.biome.NCBiomes;
import net.minecraft.world.biome.Biome;

public class RadBiomes {
	
	public static final Map<Biome, Double> BIOME_MAP = new HashMap<Biome, Double>();
	
	public static void init() {
		BIOME_MAP.put(NCBiomes.NUCLEAR_WASTELAND, RadWorlds.BACKGROUND_MAP.get(NCConfig.wasteland_dimension)/8D);
	}
}
