package nc.radiation;

import static nc.config.NCConfig.*;

import it.unimi.dsi.fastutil.ints.*;

public class RadWorlds {
	
	public static final Int2DoubleMap RAD_MAP = new Int2DoubleOpenHashMap();
	public static final Int2DoubleMap LIMIT_MAP = new Int2DoubleOpenHashMap();
	
	public static void init() {
		for (String world : radiation_worlds) {
			int scorePos = world.indexOf('_');
			if (scorePos == -1) {
				continue;
			}
			RAD_MAP.put(Integer.parseInt(world.substring(0, scorePos)), Double.parseDouble(world.substring(scorePos + 1)));
		}
		
		for (String world : radiation_world_limits) {
			int scorePos = world.indexOf('_');
			if (scorePos == -1) {
				continue;
			}
			LIMIT_MAP.put(Integer.parseInt(world.substring(0, scorePos)), Double.parseDouble(world.substring(scorePos + 1)));
		}
	}
}
