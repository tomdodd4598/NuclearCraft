package nc.util;

import java.util.Map;
import java.util.Map.Entry;

public class MapHelper {
	
	public static <K, V> Entry<K, V> getNextEntry(Map<K, V> map) {
		return map.entrySet().iterator().hasNext() ? map.entrySet().iterator().next() : null;
	}
}
