package nc.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nc.Global;

public class NCUtil {

	private static Logger logger;
	
	public static Logger getLogger() {
		if (logger == null) logger = LogManager.getFormatterLogger(Global.MOD_ID);
		return logger;
	}
	
	public static boolean isSubclassOf(Class<?> clazz, Class<?> superClass) {
	    if (superClass.equals(Object.class)) return true;
	    if (clazz.equals(superClass)) return true; else {
	        clazz = clazz.getSuperclass();
	        if (clazz.equals(Object.class)) return false;
	        return isSubclassOf(clazz, superClass);
	    }
	}
	
	/** NOTE: The constructor parameter types must match the argument types EXACTLY - they can NOT be superclasses */
	public static <T> T newInstance(Class<T> clazz, Object... args) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Constructor<T> constructor = clazz.getConstructor(getClasses(args));
		return constructor.newInstance(args);
	}
	
	public static Class<?>[] getClasses(Object... objects) {
		Class<?>[] classes = new Class<?>[objects.length];
		for (int i = 0; i < objects.length; i++) classes[i] = objects[i].getClass();
		return classes;
	}
}
