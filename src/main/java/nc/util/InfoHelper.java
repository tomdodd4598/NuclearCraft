package nc.util;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.util.text.TextFormatting;

public class InfoHelper {
	private static final int TEXT_WIDTH = 225;
	
	public static final String SHIFT_STRING = Lang.localise("gui.inventory.shift_for_info");
	
	private static final String[] NUL = {};
	private static final String[][] NUL_ARRAY = {};
	
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
		if (lines == NUL) return; else {
			if (InfoHelper.shift() || lines.length == 1) InfoHelper.infoList(list, lines);
			else InfoHelper.shiftInfo(list);
        }
	}
	
	public static String[] formattedInfo(String tooltip, Object... args) {
		return FontRenderHelper.formattedString(Lang.localise(tooltip, args), TEXT_WIDTH);
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
		if (Lang.canLocalise(generalName + ".desc")) return formattedInfo(generalName + ".desc");
		if (Lang.canLocalise(unlocName + ".desc")) return formattedInfo(unlocName + ".desc");
		String firstLine = unlocName + ".des" + 0;
		if (!Lang.canLocalise(firstLine)) return NUL;
		String[] info = new String[] {Lang.localise(firstLine)};
		int line = 1;
		while (Lang.canLocalise(unlocName + ".des" + line)) {
			info = ArrayHelper.concatenate(info, Lang.localise(unlocName + ".des" + line));
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
