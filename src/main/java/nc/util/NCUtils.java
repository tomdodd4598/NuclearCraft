package nc.util;

import nc.Global;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NCUtils {

	private static Logger logger;
	private static Lang lang;
	
	public static Logger getLogger() {
		if (logger == null) {
			logger = LogManager.getFormatterLogger(Global.MOD_ID);
		}
		return logger;
	}
}
