package nc.util;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nc.Global;

public class NCUtil {

	private static Logger logger;
	private static Lang lang;
	
	public static Logger getLogger() {
		if (logger == null) {
			logger = LogManager.getFormatterLogger(Global.MOD_ID);
		}
		return logger;
	}
	
	public static final int[] INVALID = new int[] {-1};
	
	public static String capitalize(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}
}
