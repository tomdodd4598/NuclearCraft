package nc.util;

import java.util.List;

import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.input.Keyboard;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class InfoNC {
	public static final String shiftString = "Press Shift for more info";
	
	public static final String[] nul = {""};
	
	public static void shiftInfo(List list) {
		list.add(EnumChatFormatting.ITALIC + InfoNC.shiftString);
	}

	public static void info(List list, String line) {
		list.add(EnumChatFormatting.AQUA + line);
	}
	
	public static boolean shift() {
		return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
	}
	
	public static void infoList(List list, String... lines) {
		for (int i = 0; i < lines.length; i++) {
			list.add(EnumChatFormatting.AQUA + lines[i]);
		}
	}
	
	public static void infoFull(List list, String... lines) {
		if (lines == nul) return; else {
			if (InfoNC.shift()) {
				InfoNC.infoList(list, lines);
			} else {
				InfoNC.shiftInfo(list);
			}
        }
	}
}
