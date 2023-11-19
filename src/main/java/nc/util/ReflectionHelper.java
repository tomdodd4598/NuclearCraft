package nc.util;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Map;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.*;

public class ReflectionHelper {
	
	public static Class<?>[] getClasses(Object... objects) {
		Class<?>[] classes = new Class<?>[objects.length];
		for (int i = 0; i < objects.length; ++i) {
			classes[i] = objects[i].getClass();
		}
		return classes;
	}
	
	/** NOTE: The constructor parameter types must match the argument types EXACTLY - they can NOT be superclasses */
	public static <T> T newInstance(Class<T> clazz, Object... args) {
		Constructor<T> constructor;
		try {
			constructor = clazz.getConstructor(getClasses(args));
			return constructor.newInstance(args);
		}
		catch (Exception e) {
			throw new UnsupportedOperationException();
		}
	}
	
	public static class DynamicClassLoader extends ClassLoader {
		
		public Class<?> defineClass(String name, byte[] bytes) {
			return defineClass(name, bytes, 0, bytes.length);
		}
	}
	
	public static final DynamicClassLoader CLASS_LOADER = new DynamicClassLoader();
	
	@SuppressWarnings("unchecked")
	public static <T> Class<T> defineClass(String name, ClassWriter cw) {
		return (Class<T>) CLASS_LOADER.defineClass(name, cw.toByteArray());
	}
	
	public static <T> Class<T> cloneClass(Class<?> clazz, String cloneName, Map<String, String> map) {
		try {
			ClassReader cr = new ClassReader(clazz.getName());
			ClassWriter cw = new ClassWriter(cr, 0);
			cr.accept(getClassRemapper(cw, map), 0);
			return defineClass(clazz.getPackage().getName() + "." + cloneName, cw);
		}
		catch (IOException e) {
			throw new UnsupportedOperationException();
		}
	}
	
	public static ClassRemapper getClassRemapper(ClassVisitor cv, Map<String, String> map) {
		return new ClassRemapper(cv, new SimpleRemapper(map));
	}
}
