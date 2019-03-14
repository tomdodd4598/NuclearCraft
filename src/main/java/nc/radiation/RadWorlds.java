package nc.radiation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import nc.config.NCConfig;

public class RadWorlds {
	
	public static final Map<Integer, Double> BACKGROUND_MAP = new HashMap<Integer, Double>();
	public static final HashSet<Integer> BIOME_BACKGROUND_EXEMPT_SET = new HashSet<Integer>();
	
	public static void init() {
		for (String world : NCConfig.radiation_worlds) {
			int scorePos = world.indexOf('_');
			if (scorePos == -1) continue;
			BACKGROUND_MAP.put(Integer.parseInt(world.substring(0, scorePos)), Double.parseDouble(world.substring(scorePos + 1)));
		}

		for (String world : NCConfig.radiation_biome_exempt_worlds) {
			BIOME_BACKGROUND_EXEMPT_SET.add(Integer.parseInt(world));
		}
	}
}
