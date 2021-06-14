package nc.util;

import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.util.text.TextFormatting;

public class TextHelper {
	
	public static final Object2IntMap<TextFormatting> T2I_MAP = new Object2IntOpenHashMap<>();
	public static final Object2ObjectMap<String, TextFormatting> S2T_MAP = new Object2ObjectOpenHashMap<>();
	
	static {
		T2I_MAP.defaultReturnValue(0xFFFFFF);
		S2T_MAP.defaultReturnValue(TextFormatting.WHITE);
		
		T2I_MAP.put(TextFormatting.BLACK, 0x000000);
		T2I_MAP.put(TextFormatting.DARK_BLUE, 0x0000AA);
		T2I_MAP.put(TextFormatting.DARK_GREEN, 0x00AA00);
		T2I_MAP.put(TextFormatting.DARK_AQUA, 0x00AAAA);
		T2I_MAP.put(TextFormatting.DARK_RED, 0xAA0000);
		T2I_MAP.put(TextFormatting.DARK_PURPLE, 0xAA00AA);
		T2I_MAP.put(TextFormatting.GOLD, 0xFFAA00);
		T2I_MAP.put(TextFormatting.GRAY, 0xAAAAAA);
		T2I_MAP.put(TextFormatting.DARK_GRAY, 0x555555);
		T2I_MAP.put(TextFormatting.BLUE, 0x5555FF);
		T2I_MAP.put(TextFormatting.GREEN, 0x55FF55);
		T2I_MAP.put(TextFormatting.AQUA, 0x55FFFF);
		T2I_MAP.put(TextFormatting.RED, 0xFF5555);
		T2I_MAP.put(TextFormatting.LIGHT_PURPLE, 0xFF55FF);
		T2I_MAP.put(TextFormatting.YELLOW, 0xFFFF55);
		T2I_MAP.put(TextFormatting.WHITE, 0xFFFFFF);
		
		for (TextFormatting formatting : TextFormatting.values()) {
			S2T_MAP.put(formatting.toString(), formatting);
		}
	}
}
