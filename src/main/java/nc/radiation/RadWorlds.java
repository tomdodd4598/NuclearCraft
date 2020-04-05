package nc.radiation;

import it.unimi.dsi.fastutil.ints.Int2DoubleMap;
import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;
import nc.config.NCConfig;

public class RadWorlds {
	
	public static final Int2DoubleMap RAD_MAP = new Int2DoubleOpenHashMap();
	public static final Int2DoubleMap LIMIT_MAP = new Int2DoubleOpenHashMap();
	
	public static void init() {
		for (String world : NCConfig.radiation_worlds) {
			int scorePos = world.indexOf('_');
			if (scorePos == -1) continue;
			RAD_MAP.put(Integer.parseInt(world.substring(0, scorePos)), Double.parseDouble(world.substring(scorePos + 1)));
		}
		
		for (String world : NCConfig.radiation_world_limits) {
			int scorePos = world.indexOf('_');
			if (scorePos == -1) continue;
			LIMIT_MAP.put(Integer.parseInt(world.substring(0, scorePos)), Double.parseDouble(world.substring(scorePos + 1)));
		}
	}
}
