package nc.radiation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.config.NCConfig;

public class RadStructures {
	
	public static final Map<String, Double> RAD_MAP = new HashMap<String, Double>();
	public static final List<String> STRUCTURE_LIST = new ArrayList<String>();
	
	public static void init() {
		for (String structure : NCConfig.radiation_structures) {
			int scorePos = structure.lastIndexOf('_');
			if (scorePos == -1) continue;
			RAD_MAP.put(structure.substring(0, scorePos), Double.parseDouble(structure.substring(scorePos + 1)));
			STRUCTURE_LIST.add(structure.substring(0, scorePos));
		}
	}
}
