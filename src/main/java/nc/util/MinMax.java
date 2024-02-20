package nc.util;

import java.util.Comparator;

public class MinMax<T> {
	
	protected T min = null, max = null;
	protected final Comparator<T> comparator;
	
	public MinMax(Comparator<T> comparator) {
		this.comparator = comparator;
	}
	
	public T getMin() {
		return min;
	}
	
	public T getMax() {
		return max;
	}
	
	public void update(T value) {
		if (min == null || (value != null && comparator.compare(min, value) > 0)) {
			min = value;
		}
		if (max == null || (value != null && comparator.compare(max, value) < 0)) {
			max = value;
		}
	}
	
	public static <V extends Comparable<V>> MinMax<V> comparable() {
		return new MinMax<>(Comparable::compareTo);
	}
	
	public static class MinMaxByte {
		
		protected byte min = Byte.MAX_VALUE, max = Byte.MIN_VALUE;
		
		protected MinMaxByte() {}
		
		public byte getMin() {
			return min;
		}
		
		public byte getMax() {
			return max;
		}
		
		public void update(byte value) {
			if (min > value) {
				min = value;
			}
			if (max < value) {
				max = value;
			}
		}
	}
	
	public static class MinMaxShort {
		
		protected short min = Short.MAX_VALUE, max = Short.MIN_VALUE;
		
		public MinMaxShort() {}
		
		public short getMin() {
			return min;
		}
		
		public short getMax() {
			return max;
		}
		
		public void update(short value) {
			if (min > value) {
				min = value;
			}
			if (max < value) {
				max = value;
			}
		}
	}
	
	public static class MinMaxInt {
		
		protected int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
		
		public MinMaxInt() {}
		
		public int getMin() {
			return min;
		}
		
		public int getMax() {
			return max;
		}
		
		public void update(int value) {
			if (min > value) {
				min = value;
			}
			if (max < value) {
				max = value;
			}
		}
	}
	
	public static class MinMaxLong {
		
		protected long min = Long.MAX_VALUE, max = Long.MIN_VALUE;
		
		public MinMaxLong() {}
		
		public long getMin() {
			return min;
		}
		
		public long getMax() {
			return max;
		}
		
		public void update(long value) {
			if (min > value) {
				min = value;
			}
			if (max < value) {
				max = value;
			}
		}
	}
	
	public static class MinMaxFloat {
		
		protected float min = Float.POSITIVE_INFINITY, max = Float.NEGATIVE_INFINITY;
		
		public MinMaxFloat() {}
		
		public float getMin() {
			return min;
		}
		
		public float getMax() {
			return max;
		}
		
		public void update(float value) {
			if (min > value) {
				min = value;
			}
			if (max < value) {
				max = value;
			}
		}
	}
	
	public static class MinMaxDouble {
		
		protected double min = Double.POSITIVE_INFINITY, max = Double.NEGATIVE_INFINITY;
		
		public MinMaxDouble() {}
		
		public double getMin() {
			return min;
		}
		
		public double getMax() {
			return max;
		}
		
		public void update(double value) {
			if (min > value) {
				min = value;
			}
			if (max < value) {
				max = value;
			}
		}
	}
	
	public static class MinMaxBoolean {
		
		protected boolean min = true, max = false;
		
		public MinMaxBoolean() {}
		
		public boolean getMin() {
			return min;
		}
		
		public boolean getMax() {
			return max;
		}
		
		public void update(boolean value) {
			if (Boolean.compare(min, value) > 0) {
				min = value;
			}
			if (Boolean.compare(max, value) < 0) {
				max = value;
			}
		}
	}
	
	public static class MinMaxChar {
		
		protected char min = Character.MAX_VALUE, max = Character.MIN_VALUE;
		
		public MinMaxChar() {}
		
		public char getMin() {
			return min;
		}
		
		public char getMax() {
			return max;
		}
		
		public void update(char value) {
			if (min > value) {
				min = value;
			}
			if (max < value) {
				max = value;
			}
		}
	}
}
