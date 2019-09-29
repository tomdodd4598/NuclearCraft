package nc.radiation;

import java.util.HashMap;
import java.util.Map;

import nc.config.NCConfig;

public class RadWorlds {
	
	public static final Map<Integer, Double> RAD_MAP = new HashMap<Integer, Double>();
	public static final Map<Integer, Double> LIMIT_MAP = new HashMap<Integer, Double>();
	
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
