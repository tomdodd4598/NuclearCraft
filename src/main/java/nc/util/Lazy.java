package nc.util;

import java.lang.reflect.*;
import java.util.function.*;

import nc.util.PrimitiveFunction.*;

public class Lazy<T> {
	
	protected boolean initialized = false;
	protected T value;
	
	protected Supplier<T> supplier;
	
	public Lazy(Supplier<T> supplier) {
		this.supplier = supplier;
	}
	
	public T get() {
		if (!initialized) {
			value = supplier.get();
		}
		return value;
	}
	
	public static class LazyByte {
		
		protected boolean initialized = false;
		protected byte value;
		
		protected ByteSupplier supplier;
		
		public LazyByte(ByteSupplier supplier) {
			this.supplier = supplier;
		}
		
		public byte get() {
			if (!initialized) {
				value = supplier.getAsByte();
			}
			return value;
		}
	}
	
	public static class LazyShort {
		
		protected boolean initialized = false;
		protected short value;
		
		protected ShortSupplier supplier;
		
		public LazyShort(ShortSupplier supplier) {
			this.supplier = supplier;
		}
		
		public short get() {
			if (!initialized) {
				value = supplier.getAsShort();
			}
			return value;
		}
	}
	
	public static class LazyInt {
		
		protected boolean initialized = false;
		protected int value;
		
		protected IntSupplier supplier;
		
		public LazyInt(IntSupplier supplier) {
			this.supplier = supplier;
		}
		
		public int get() {
			if (!initialized) {
				value = supplier.getAsInt();
			}
			return value;
		}
	}
	
	public static class LazyLong {
		
		protected boolean initialized = false;
		protected long value;
		
		protected LongSupplier supplier;
		
		public LazyLong(LongSupplier supplier) {
			this.supplier = supplier;
		}
		
		public long get() {
			if (!initialized) {
				value = supplier.getAsLong();
			}
			return value;
		}
	}
	
	public static class LazyFloat {
		
		protected boolean initialized = false;
		protected float value;
		
		protected FloatSupplier supplier;
		
		public LazyFloat(FloatSupplier supplier) {
			this.supplier = supplier;
		}
		
		public float get() {
			if (!initialized) {
				value = supplier.getAsFloat();
			}
			return value;
		}
	}
	
	public static class LazyDouble {
		
		protected boolean initialized = false;
		protected double value;
		
		protected DoubleSupplier supplier;
		
		public LazyDouble(DoubleSupplier supplier) {
			this.supplier = supplier;
		}
		
		public double get() {
			if (!initialized) {
				value = supplier.getAsDouble();
			}
			return value;
		}
	}
	
	public static class LazyBoolean {
		
		protected boolean initialized = false;
		protected boolean value;
		
		protected BooleanSupplier supplier;
		
		public LazyBoolean(BooleanSupplier supplier) {
			this.supplier = supplier;
		}
		
		public boolean get() {
			if (!initialized) {
				value = supplier.getAsBoolean();
			}
			return value;
		}
	}
	
	public static class LazyChar {
		
		protected boolean initialized = false;
		protected char value;
		
		protected CharSupplier supplier;
		
		public LazyChar(CharSupplier supplier) {
			this.supplier = supplier;
		}
		
		public char get() {
			if (!initialized) {
				value = supplier.getAsChar();
			}
			return value;
		}
	}
	
	public static class LazyMethod<T> {
		
		protected final Class<?> source;
		protected final String name;
		protected final Class<?>[] parameterTypes;
		
		protected boolean initialized = false;
		protected Method method;
		
		/** Can access parent methods, but not hidden methods. */
		public LazyMethod(Class<?> source, String name, Class<?>... parameterTypes) {
			this.source = source;
			this.name = name;
			this.parameterTypes = parameterTypes;
		}
		
		protected void initialize() throws NoSuchMethodException, SecurityException {
			method = source.getMethod(name, parameterTypes);
		}
		
		@SuppressWarnings("unchecked")
		public T invoke(Object obj, Object... args) {
			if (!initialized) {
				try {
					initialize();
				}
				catch (NoSuchMethodException | SecurityException e) {
					throw new UnsupportedOperationException();
				}
			}
			
			try {
				return (T) method.invoke(obj, args);
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new UnsupportedOperationException();
			}
		}
	}
	
	public static class LazyDeclaredMethod<T> extends LazyMethod<T> {
		
		/** Can access hidden methods, but not parent methods. */
		public LazyDeclaredMethod(Class<?> source, String name, Class<?>... parameterTypes) {
			super(source, name, parameterTypes);
		}
		
		@Override
		protected void initialize() throws NoSuchMethodException, SecurityException {
			method = source.getDeclaredMethod(name, parameterTypes);
			method.setAccessible(true);
		}
	}
}
