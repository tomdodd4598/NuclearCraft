package nc.util;

import java.lang.reflect.Field;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.util.text.TextFormatting;

public class InfoHelper {
	private static final int MINIMUM_TEXT_WIDTH = 225;
	
	public static final String SHIFT_STRING = Lang.localise("gui.inventory.shift_for_info");
	
	private static final String[] EMPTY_ARRAY = {};
	
	public static void shiftInfo(List list) {
		list.add(TextFormatting.ITALIC + SHIFT_STRING);
	}

	public static void info(List list, String line) {
		list.add(TextFormatting.AQUA + line);
	}
	
	public static boolean shift() {
		return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
	}
	
	public static void infoList(List list, String... lines) {
		for (int i = 0; i < lines.length; i++) list.add(TextFormatting.AQUA + lines[i]);
	}
	
	public static void infoFull(List list, String... lines) {
		if (lines == EMPTY_ARRAY) return; else {
			if (InfoHelper.shift() || lines.length == 1) InfoHelper.infoList(list, lines);
			else InfoHelper.shiftInfo(list);
        }
	}
	
	public static String[] formattedInfo(String tooltip, Object... args) {
		return FontRenderHelper.formattedString(Lang.localise(tooltip, args), MINIMUM_TEXT_WIDTH /*Math.max(MINIMUM_TEXT_WIDTH, FontRenderHelper.maxSize(tooltip))*/);
	}
	
	public static String[] buildInfo(String unlocName, String... tooltip) {
		if (tooltip.length == 0) return standardInfo(unlocName);
		else return tooltip;
	}
	
	public static String[] standardInfo(String unlocName) {
		return standardInfo(unlocName, unlocName);
	}
	
	public static <T extends Enum<T>> String[][] buildInfo(String unlocNameBase, Class<T> enumm, String[]... tooltips) {
		String[] names = getEnumNames(enumm);
		String[][] strings = new String[names.length][];
		for (int i = 0; i < names.length; i++) {
			if (ArrayHelper.isEmpty(tooltips, i)) strings[i] = standardInfo(unlocNameBase + "." + names[i], unlocNameBase);
			else strings[i] = tooltips[i];
		}
		return strings;
	}
	
	public static String[] standardInfo(String unlocName, String generalName) {
		for (String name : new String[] {unlocName, generalName}) {
			if (Lang.canLocalise(name + ".desc")) {
				return formattedInfo(name + ".desc");
			}
		}
		return getNumberedInfo(unlocName + ".des");
	}
	
	public static String[] getNumberedInfo(String base) {
		String firstLine = base + 0;
		if (!Lang.canLocalise(firstLine)) return EMPTY_ARRAY;
		String[] info = new String[] {Lang.localise(firstLine)};
		int line = 1;
		while (Lang.canLocalise(base + line)) {
			info = ArrayHelper.concatenate(info, Lang.localise(base + line));
			line++;
		}
		return info;
	}
	
	public static <T extends Enum<T>> String[] getEnumNames(Class<T> enumm) {
		T[] values = enumm.getEnumConstants();
		String[] names = new String[values.length];
		for (int i = 0; i < values.length; i++) names[i] = values[i].toString();
		return names;
	}
}
