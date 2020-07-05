package nc.util;

import java.util.Map;

import it.unimi.dsi.fastutil.objects.*;

public abstract class Cache<K, V, M extends Map<K, V>> {
	
	protected final M internalMap;
	
	protected Cache() {
		internalMap = initInternalMap();
	}
	
	protected abstract M initInternalMap();
	
	public static class BooleanCache<K> extends Cache<K, Boolean, Object2BooleanMap<K>> {
		
		@Override
		public Object2BooleanMap<K> initInternalMap() {
			return new Object2BooleanOpenHashMap<>();
		}
		
		public boolean get(K key) {
			if (!internalMap.containsKey(key)) {
				put(key, internalMap.defaultReturnValue());
			}
			return internalMap.getBoolean(key);
		}
		
		public boolean put(K key, boolean value) {
			return internalMap.put(key, value);
		}
	}
	
	public static class IntCache<K> extends Cache<K, Integer, Object2IntMap<K>> {
		
		@Override
		public Object2IntMap<K> initInternalMap() {
			return new Object2IntOpenHashMap<>();
		}
		
		public int get(K key) {
			if (!internalMap.containsKey(key)) {
				put(key, internalMap.defaultReturnValue());
			}
			return internalMap.getInt(key);
		}
		
		public int put(K key, int value) {
			return internalMap.put(key, value);
		}
	}
	
	public static class LongCache<K> extends Cache<K, Long, Object2LongMap<K>> {
		
		@Override
		public Object2LongMap<K> initInternalMap() {
			return new Object2LongOpenHashMap<>();
		}
		
		public long get(K key) {
			if (!internalMap.containsKey(key)) {
				put(key, internalMap.defaultReturnValue());
			}
			return internalMap.getLong(key);
		}
		
		public long put(K key, long value) {
			return internalMap.put(key, value);
		}
	}
	
	public static class FloatCache<K> extends Cache<K, Float, Object2FloatMap<K>> {
		
		@Override
		public Object2FloatMap<K> initInternalMap() {
			return new Object2FloatOpenHashMap<>();
		}
		
		public float get(K key) {
			if (!internalMap.containsKey(key)) {
				put(key, internalMap.defaultReturnValue());
			}
			return internalMap.getFloat(key);
		}
		
		public float put(K key, float value) {
			return internalMap.put(key, value);
		}
	}
	
	public static class DoubleCache<K> extends Cache<K, Double, Object2DoubleMap<K>> {
		
		@Override
		public Object2DoubleMap<K> initInternalMap() {
			return new Object2DoubleOpenHashMap<>();
		}
		
		public double get(K key) {
			if (!internalMap.containsKey(key)) {
				put(key, internalMap.defaultReturnValue());
			}
			return internalMap.getDouble(key);
		}
		
		public double put(K key, double value) {
			return internalMap.put(key, value);
		}
	}
}
