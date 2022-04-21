package nc.util;

import java.util.*;

import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.*;

public class StringHelper {
	
	public static String capitalize(String s) {
		return s.isEmpty() ? s : (s.substring(0, 1).toUpperCase(Locale.ROOT) + s.substring(1));
	}
	
	public static String starting(String s, int length) {
		int fixedLength = Math.min(length, s.length());
		return s.substring(0, fixedLength);
	}
	
	public static String ending(String s, int length) {
		int fixedLength = Math.min(length, s.length());
		return s.substring(s.length() - fixedLength);
	}
	
	public static String removePrefix(String s, int length) {
		return s.substring(length);
	}
	
	public static String removeSuffix(String s, int length) {
		return s.substring(0, s.length() - length);
	}
	
	public static boolean beginsWith(String s, String check) {
		return starting(s, check.length()).equals(check);
	}
	
	public static String stringListConcat(List<String> list) {
		return String.join(", ", list);
	}
	
	@SafeVarargs
	public static String stringListConcat(List<String> first, List<String>... others) {
		return String.join(", ", CollectionHelper.concatenate(first, others));
	}
	
	public static String charLine(char c, int length) {
		char[] charArray = new char[length];
		Arrays.fill(charArray, c);
		return new String(charArray);
	}
	
	public static boolean isGlob(String s) {
		return s.contains("*") || s.contains("?") || s.contains(".") || s.contains("\\");
	}
	
	/** Converts wildcards to a regular expressions */
	public static String regex(String glob) {
		StringBuilder out = new StringBuilder("^");
		for (int i = 0; i < glob.length(); ++i) {
			final char c = glob.charAt(i);
			switch (c) {
				case '*':
					out.append(".*");
					break;
				case '?':
					out.append('.');
					break;
				case '.':
					out.append("\\.");
					break;
				case '\\':
					out.append("\\\\");
					break;
				default:
					out.append(c);
			}
		}
		out.append('$');
		return out.toString();
	}
	
	public static final Object2IntMap<String> NUMBER_S2I_MAP = new Object2IntOpenHashMap<>();
	public static final Int2ObjectMap<String> NUMBER_I2S_MAP = new Int2ObjectOpenHashMap<>();
	
	static {
		NUMBER_S2I_MAP.put("zero", 0);
		NUMBER_S2I_MAP.put("one", 1);
		NUMBER_S2I_MAP.put("two", 2);
		NUMBER_S2I_MAP.put("three", 3);
		NUMBER_S2I_MAP.put("four", 4);
		NUMBER_S2I_MAP.put("five", 5);
		NUMBER_S2I_MAP.put("six", 6);
		NUMBER_S2I_MAP.put("seven", 7);
		NUMBER_S2I_MAP.put("eight", 8);
		NUMBER_S2I_MAP.put("nine", 9);
		NUMBER_S2I_MAP.put("ten", 10);
		
		for (Object2IntMap.Entry<String> entry : NUMBER_S2I_MAP.object2IntEntrySet()) {
			NUMBER_I2S_MAP.put(entry.getIntValue(), entry.getKey());
		}
		
		for (int i = 0; i <= 10; ++i) {
			NUMBER_S2I_MAP.put(Integer.toString(i), i);
		}
	}
}
