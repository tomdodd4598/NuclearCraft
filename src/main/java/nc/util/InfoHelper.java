package nc.util;

import java.util.List;

import nc.config.NCConfig;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;

public class InfoHelper {
	
	private static final int MINIMUM_TEXT_WIDTH = 225;
	
	public static final String SHIFT_STRING = Lang.localise("gui.nc.inventory.shift_for_info");
	public static final String CTRL_STRING = Lang.localise("gui.nc.inventory.ctrl_for_info");
	
	public static final String[] EMPTY_ARRAY = {};
	public static final String[][] EMPTY_ARRAYS = {};
	
	public static void infoLine(List list, TextFormatting fixedColor, String line) {
		list.add(fixedColor + line);
	}
	
	public static void shiftInfo(List list) {
		list.add(TextFormatting.ITALIC + (NCConfig.ctrl_info ? CTRL_STRING : SHIFT_STRING));
	}
	
	public static void fixedInfoList(List list, boolean infoBelow, TextFormatting fixedColor, String... fixedLines) {
		for (int i = 0; i < fixedLines.length; i++) infoLine(list, fixedColor, fixedLines[i]);
		if (infoBelow) shiftInfo(list);
	}
	
	public static void fixedInfoList(List list, boolean infoBelow, TextFormatting[] fixedColors, String... fixedLines) {
		for (int i = 0; i < fixedLines.length; i++) infoLine(list, fixedColors[i], fixedLines[i]);
		if (infoBelow) shiftInfo(list);
	}
	
	public static void infoList(List list, TextFormatting infoColor, String... lines) {
		for (int i = 0; i < lines.length; i++) infoLine(list, infoColor, lines[i]);
	}
	
	public static void infoList(List list, TextFormatting[] infoColors, String... lines) {
		for (int i = 0; i < lines.length; i++) infoLine(list, infoColors[i], lines[i]);
	}
	
	public static void infoFull(List list, TextFormatting fixedColor, String[] fixedLines, TextFormatting infoColor, String... lines) {
		if (fixedLines != EMPTY_ARRAY && (!NCUtil.isInfoKeyDown() || lines == EMPTY_ARRAY)) fixedInfoList(list, lines != EMPTY_ARRAY, fixedColor, fixedLines);
		else if ((NCUtil.isInfoKeyDown() && lines != EMPTY_ARRAY) || lines.length == 1) infoList(list, infoColor, lines);
		else if (lines != EMPTY_ARRAY) shiftInfo(list);
	}
	
	public static void infoFull(List list, TextFormatting fixedColor, String[] fixedLines, TextFormatting[] infoColors, String... lines) {
		if (fixedLines != EMPTY_ARRAY && (!NCUtil.isInfoKeyDown() || lines == EMPTY_ARRAY)) fixedInfoList(list, lines != EMPTY_ARRAY, fixedColor, fixedLines);
		else if ((NCUtil.isInfoKeyDown() && lines != EMPTY_ARRAY) || lines.length == 1) infoList(list, infoColors, lines);
		else if (lines != EMPTY_ARRAY) shiftInfo(list);
	}
	
	public static void infoFull(List list, TextFormatting[] fixedColors, String[] fixedLines, TextFormatting infoColor, String... lines) {
		if (fixedLines != EMPTY_ARRAY && (!NCUtil.isInfoKeyDown() || lines == EMPTY_ARRAY)) fixedInfoList(list, lines != EMPTY_ARRAY, fixedColors, fixedLines);
		else if ((NCUtil.isInfoKeyDown() && lines != EMPTY_ARRAY) || lines.length == 1) infoList(list, infoColor, lines);
		else if (lines != EMPTY_ARRAY) shiftInfo(list);
	}
	
	public static void infoFull(List list, TextFormatting[] fixedColors, String[] fixedLines, TextFormatting[] infoColors, String... lines) {
		if (fixedLines != EMPTY_ARRAY && (!NCUtil.isInfoKeyDown() || lines == EMPTY_ARRAY)) fixedInfoList(list, lines != EMPTY_ARRAY, fixedColors, fixedLines);
		else if ((NCUtil.isInfoKeyDown() && lines != EMPTY_ARRAY) || lines.length == 1) infoList(list, infoColors, lines);
		else if (lines != EMPTY_ARRAY) shiftInfo(list);
	}
	
	public static void infoFull(List list, TextFormatting infoColor, String... lines) {
		infoFull(list, TextFormatting.AQUA, EMPTY_ARRAY, infoColor, lines);
	}
	
	public static void infoFull(List list, TextFormatting[] infoColors, String... lines) {
		infoFull(list, new TextFormatting[] {}, EMPTY_ARRAY, infoColors, lines);
	}
	
	public static String[] formattedInfo(String tooltip, Object... args) {
		return FontRenderHelper.formattedString(Lang.localise(tooltip, args), MINIMUM_TEXT_WIDTH /*Math.max(MINIMUM_TEXT_WIDTH, FontRenderHelper.maxSize(tooltip))*/);
	}
	
	public static String[] buildFixedInfo(String unlocName, String... tooltip) {
		if (tooltip.length == 0) return standardFixedInfo(unlocName);
		else return tooltip;
	}
	
	public static String[] buildInfo(String unlocName, String... tooltip) {
		if (tooltip.length == 0) return standardInfo(unlocName);
		else return tooltip;
	}
	
	public static String[] standardFixedInfo(String unlocName) {
		return standardFixedInfo(unlocName, unlocName);
	}
	
	public static String[] standardInfo(String unlocName) {
		return standardInfo(unlocName, unlocName);
	}
	
	public static <T extends Enum<T> & IStringSerializable> String[][] buildFixedInfo(String unlocNameBase, Class<T> enumm, String[]... tooltips) {
		return buildGeneralInfo(unlocNameBase, enumm, ".fixd", ".fix", tooltips);
	}
	
	public static <T extends Enum<T> & IStringSerializable> String[][] buildInfo(String unlocNameBase, Class<T> enumm, String[]... tooltips) {
		return buildGeneralInfo(unlocNameBase, enumm, ".desc", ".des", tooltips);
	}
	
	public static <T extends Enum<T> & IStringSerializable> String[][] buildGeneralInfo(String unlocNameBase, Class<T> enumm, String desc, String des, String[]... tooltips) {
		T[] values = enumm.getEnumConstants();
		String[][] strings = new String[values.length][];
		for (int i = 0; i < values.length; i++) {
			if (CollectionHelper.isNull(tooltips, i)) strings[i] = EMPTY_ARRAY;
			else if (CollectionHelper.isEmpty(tooltips, i)) strings[i] = standardGeneralInfo(unlocNameBase + "." + values[i].getName(), unlocNameBase, desc, des);
			else strings[i] = tooltips[i];
		}
		return strings;
	}
	
	public static String[] standardFixedInfo(String unlocName, String generalName) {
		return standardGeneralInfo(unlocName, generalName, ".fixd", ".fix");
	}
	
	public static String[] standardInfo(String unlocName, String generalName) {
		return standardGeneralInfo(unlocName, generalName, ".desc", ".des");
	}
	
	public static String[] standardGeneralInfo(String unlocName, String generalName, String desc, String des) {
		for (String name : new String[] {unlocName, generalName}) {
			if (Lang.canLocalise(name + desc)) {
				return formattedInfo(name + desc);
			}
		}
		return getNumberedInfo(unlocName + des);
	}
	
	public static String[] getNumberedInfo(String base) {
		String firstLine = base + 0;
		if (!Lang.canLocalise(firstLine)) return EMPTY_ARRAY;
		String[] info = new String[] {Lang.localise(firstLine)};
		int line = 1;
		while (Lang.canLocalise(base + line)) {
			info = CollectionHelper.concatenate(info, Lang.localise(base + line));
			line++;
		}
		return info;
	}
}
