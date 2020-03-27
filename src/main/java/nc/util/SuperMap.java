package nc.util;

import java.util.Map;
import java.util.Map.Entry;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;

public abstract class SuperMap<KEY, T, M extends Map<KEY, ? extends T>> {
	
	protected final Object2ObjectMap<Class<? extends T>, M> internalMap;
	
	protected SuperMap() {
		internalMap = new Object2ObjectOpenHashMap<>();
	}
	
	public <TYPE extends T, MAP extends Map<KEY, TYPE>> MAP put(Class<TYPE> clazz, M map) {
		return (MAP) internalMap.put(clazz, map);
	}
	
	public <TYPE extends T, MAP extends Map<KEY, TYPE>> MAP get(Class<TYPE> clazz) {
		if (!internalMap.containsKey(clazz)) {
			internalMap.put(clazz, backup(clazz));
		}
		return (MAP) internalMap.get(clazz);
	}
	
	public abstract <TYPE extends T> M backup(Class<TYPE> clazz);
	
	public <TYPE extends T, MAP extends Map<KEY, TYPE>> MAP equip(Class<TYPE> clazz) {
		return put(clazz, backup(clazz));
	}
	
	public ObjectSet<Map.Entry<Class<? extends T>, M>> entrySet() {
		return internalMap.entrySet();
	}
	
	public static class SuperMapEntry<KEY, T> {
		private final Class<T> key;
		private final Map<KEY, T> value;
		
		public SuperMapEntry(Entry<Class<?>, Map<KEY, ?>> superMapEntry) {
			key = (Class<T>) superMapEntry.getKey();
			value = (Map<KEY, T>) superMapEntry.getValue();
		}
		
		public Class<T> getKey() {
			return key;
		}
		
		public Map<KEY, T> getValue() {
			return value;
		}
	}
}
