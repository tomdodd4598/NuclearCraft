package nc.util;

import static nc.config.NCConfig.ctrl_info;

import java.util.*;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;

public class InfoHelper {
	
	public static final int MAXIMUM_TEXT_WIDTH = 225;
	
	public static final String SHIFT_STRING = Lang.localise("gui.nc.inventory.shift_for_info");
	public static final String CTRL_STRING = Lang.localise("gui.nc.inventory.ctrl_for_info");
	
	public static final String[] EMPTY_ARRAY = {}, NULL_ARRAY = {null};
	public static final String[][] EMPTY_ARRAYS = {}, NULL_ARRAYS = {null};
	
	public static void infoLine(List<String> list, TextFormatting fixedColor, String line) {
		list.add(fixedColor + line);
	}
	
	public static void shiftInfo(List<String> list) {
		list.add(TextFormatting.ITALIC + (ctrl_info ? CTRL_STRING : SHIFT_STRING));
	}
	
	public static void fixedInfoList(List<String> list, boolean infoBelow, TextFormatting fixedColor, String... fixedLines) {
		for (String fixedLine : fixedLines) {
			infoLine(list, fixedColor, fixedLine);
		}
		if (infoBelow) {
			shiftInfo(list);
		}
	}
	
	public static void fixedInfoList(List<String> list, boolean infoBelow, TextFormatting[] fixedColors, String... fixedLines) {
		for (int i = 0; i < fixedLines.length; ++i) {
			infoLine(list, fixedColors[i], fixedLines[i]);
		}
		if (infoBelow) {
			shiftInfo(list);
		}
	}
	
	public static void infoList(List<String> list, TextFormatting infoColor, String... lines) {
		for (String line : lines) {
			infoLine(list, infoColor, line);
		}
	}
	
	public static void infoList(List<String> list, TextFormatting[] infoColors, String... lines) {
		for (int i = 0; i < lines.length; ++i) {
			infoLine(list, infoColors[i], lines[i]);
		}
	}
	
	public static void infoFull(List<String> list, TextFormatting fixedColor, String[] fixedLines, TextFormatting infoColor, String... lines) {
		if (showFixedInfo(fixedLines, lines)) {
			fixedInfoList(list, !Arrays.equals(lines, EMPTY_ARRAY) && !Arrays.equals(lines, NULL_ARRAY), fixedColor, fixedLines);
		}
		else if (showInfo(fixedLines, lines)) {
			infoList(list, infoColor, lines);
		}
		else if (showShiftInfo(fixedLines, lines)) {
			shiftInfo(list);
		}
	}
	
	public static void infoFull(List<String> list, TextFormatting fixedColor, String[] fixedLines, TextFormatting[] infoColors, String... lines) {
		if (showFixedInfo(fixedLines, lines)) {
			fixedInfoList(list, !Arrays.equals(lines, EMPTY_ARRAY) && !Arrays.equals(lines, NULL_ARRAY), fixedColor, fixedLines);
		}
		else if (showInfo(fixedLines, lines)) {
			infoList(list, infoColors, lines);
		}
		else if (showShiftInfo(fixedLines, lines)) {
			shiftInfo(list);
		}
	}
	
	public static void infoFull(List<String> list, TextFormatting[] fixedColors, String[] fixedLines, TextFormatting infoColor, String... lines) {
		if (showFixedInfo(fixedLines, lines)) {
			fixedInfoList(list, !Arrays.equals(lines, EMPTY_ARRAY) && !Arrays.equals(lines, NULL_ARRAY), fixedColors, fixedLines);
		}
		else if (showInfo(fixedLines, lines)) {
			infoList(list, infoColor, lines);
		}
		else if (showShiftInfo(fixedLines, lines)) {
			shiftInfo(list);
		}
	}
	
	public static void infoFull(List<String> list, TextFormatting[] fixedColors, String[] fixedLines, TextFormatting[] infoColors, String... lines) {
		if (showFixedInfo(fixedLines, lines)) {
			fixedInfoList(list, !Arrays.equals(lines, EMPTY_ARRAY) && !Arrays.equals(lines, NULL_ARRAY), fixedColors, fixedLines);
		}
		else if (showInfo(fixedLines, lines)) {
			infoList(list, infoColors, lines);
		}
		else if (showShiftInfo(fixedLines, lines)) {
			shiftInfo(list);
		}
	}
	
	public static boolean showFixedInfo(String[] fixedLines, String... lines) {
		return !Arrays.equals(fixedLines, EMPTY_ARRAY) && !Arrays.equals(fixedLines, NULL_ARRAY) && (!NCUtil.isInfoKeyDown() || Arrays.equals(lines, EMPTY_ARRAY));
	}
	
	public static boolean showInfo(String[] fixedLines, String... lines) {
		return (NCUtil.isInfoKeyDown() || lines.length == 1) && !Arrays.equals(lines, EMPTY_ARRAY) && !Arrays.equals(lines, NULL_ARRAY);
	}
	
	public static boolean showShiftInfo(String[] fixedLines, String... lines) {
		return !Arrays.equals(lines, EMPTY_ARRAY) && !Arrays.equals(lines, NULL_ARRAY);
	}
	
	public static void infoFull(List<String> list, TextFormatting infoColor, String... lines) {
		infoFull(list, TextFormatting.AQUA, EMPTY_ARRAY, infoColor, lines);
	}
	
	public static void infoFull(List<String> list, TextFormatting[] infoColors, String... lines) {
		infoFull(list, new TextFormatting[] {}, EMPTY_ARRAY, infoColors, lines);
	}
	
	public static String[] formattedInfo(String tooltip, Object... args) {
		return FontRenderHelper.wrapString(Lang.localise(tooltip, args), MAXIMUM_TEXT_WIDTH);
	}
	
	public static String[] buildFixedInfo(String unlocName, String... tooltip) {
		if (tooltip.length == 0) {
			return standardFixedInfo(unlocName, unlocName);
		}
		else {
			return tooltip;
		}
	}
	
	public static String[] buildInfo(String unlocName, String... tooltip) {
		if (tooltip.length == 0) {
			return standardInfo(unlocName, unlocName);
		}
		else {
			return tooltip;
		}
	}
	
	public static <T extends Enum<T> & IStringSerializable> String[] getEnumNames(Class<T> enumm) {
		T[] values = enumm.getEnumConstants();
		String[] names = new String[values.length];
		for (int i = 0; i < values.length; ++i) {
			names[i] = values[i].getName();
		}
		return names;
	}
	
	public static <T extends Enum<T> & IStringSerializable> String[][] buildFixedInfo(String unlocNameBase, Class<T> enumm, String[]... tooltips) {
		return buildGeneralInfo(unlocNameBase, getEnumNames(enumm), ".fixd", ".fix", tooltips);
	}
	
	public static String[][] buildFixedInfo(String unlocNameBase, String[] types, String[]... tooltips) {
		return buildGeneralInfo(unlocNameBase, types, ".fixd", ".fix", tooltips);
	}
	
	public static <T extends Enum<T> & IStringSerializable> String[][] buildInfo(String unlocNameBase, Class<T> enumm, String[]... tooltips) {
		return buildGeneralInfo(unlocNameBase, getEnumNames(enumm), ".desc", ".des", tooltips);
	}
	
	public static String[][] buildInfo(String unlocNameBase, String[] names, String[]... tooltips) {
		return buildGeneralInfo(unlocNameBase, names, ".desc", ".des", tooltips);
	}
	
	public static String[][] buildGeneralInfo(String unlocNameBase, String[] types, String desc, String des, String[]... tooltips) {
		String[][] strings = new String[types.length][];
		
		if (Arrays.equals(tooltips, NULL_ARRAYS)) {
			for (int i = 0; i < types.length; ++i) {
				strings[i] = NULL_ARRAY;
			}
			return strings;
		}
		
		for (int i = 0; i < types.length; ++i) {
			if (CollectionHelper.isNull(tooltips, i)) {
				strings[i] = EMPTY_ARRAY;
			}
			else if (CollectionHelper.isEmpty(tooltips, i)) {
				strings[i] = standardGeneralInfo(unlocNameBase + "." + types[i], unlocNameBase, desc, des);
			}
			else {
				strings[i] = tooltips[i];
			}
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
		if (!Lang.canLocalise(firstLine)) {
			return EMPTY_ARRAY;
		}
		String[] info = new String[] {Lang.localise(firstLine)};
		int line = 1;
		while (Lang.canLocalise(base + line)) {
			info = CollectionHelper.concatenate(info, Lang.localise(base + line));
			++line;
		}
		return info;
	}
}
