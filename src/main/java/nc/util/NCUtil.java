package nc.util;

import static nc.config.NCConfig.ctrl_info;

import org.apache.logging.log4j.*;
import org.lwjgl.input.Keyboard;

import it.unimi.dsi.fastutil.objects.*;
import nc.Global;

public class NCUtil {
	
	private static final Lazy<Logger> LOGGER = new Lazy<>(() -> LogManager.getFormatterLogger(Global.MOD_ID));
	
	public static Logger getLogger() {
		return LOGGER.get();
	}
	
	private static boolean isShiftKeyDown() {
		return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
	}
	
	private static boolean isCtrlKeyDown() {
		return Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
	}
	
	public static boolean isInfoKeyDown() {
		return ctrl_info ? NCUtil.isCtrlKeyDown() : NCUtil.isShiftKeyDown();
	}
	
	public static boolean isModifierKeyDown() {
		return NCUtil.isCtrlKeyDown() || NCUtil.isShiftKeyDown();
	}
	
	private static final Object2ObjectMap<String, String> SHORT_MOD_ID_MAP = new Object2ObjectOpenHashMap<>();
	
	static {
		putShortModId(Global.MOD_ID, "nc");
	}
	
	public static void putShortModId(String modId, String shortModId) {
		SHORT_MOD_ID_MAP.put(modId, shortModId);
	}
	
	public static String getShortModId(String modId) {
		String shortModId = SHORT_MOD_ID_MAP.get(modId);
		return shortModId == null ? modId : shortModId;
	}
}
