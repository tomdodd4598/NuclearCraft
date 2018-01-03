package nc.util;

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
}
