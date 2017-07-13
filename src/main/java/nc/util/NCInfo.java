package nc.util;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.util.text.TextFormatting;

public class NCInfo {
	public static final String shiftString = "Press Shift for more info";
	
	public static final String[] nul = {""};
	
	public static void shiftInfo(List list) {
		list.add(TextFormatting.ITALIC + NCInfo.shiftString);
	}

	public static void info(List list, String line) {
		list.add(TextFormatting.AQUA + line);
	}
	
	public static boolean shift() {
		return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
	}
	
	public static void infoList(List list, String... lines) {
		for (int i = 0; i < lines.length; i++) {
			list.add(TextFormatting.AQUA + lines[i]);
		}
	}
	
	public static void infoFull(List list, String... lines) {
		if (lines == nul) return; else {
			if (NCInfo.shift() || lines.length == 1) {
				NCInfo.infoList(list, lines);
			} else {
				NCInfo.shiftInfo(list);
			}
        }
	}
}
