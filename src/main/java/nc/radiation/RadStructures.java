package nc.radiation;

import static nc.config.NCConfig.radiation_structures;

import java.util.*;

import it.unimi.dsi.fastutil.objects.*;

public class RadStructures {
	
	public static final Object2DoubleMap<String> RAD_MAP = new Object2DoubleOpenHashMap<>();
	public static final List<String> STRUCTURE_LIST = new ArrayList<>();
	
	public static void init() {
		for (String structure : radiation_structures) {
			int scorePos = structure.lastIndexOf('_');
			if (scorePos == -1) {
				continue;
			}
			RAD_MAP.put(structure.substring(0, scorePos), Double.parseDouble(structure.substring(scorePos + 1)));
			STRUCTURE_LIST.add(structure.substring(0, scorePos));
		}
	}
}
