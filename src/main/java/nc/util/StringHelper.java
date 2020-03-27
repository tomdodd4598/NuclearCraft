package nc.util;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class StringHelper {
	
	public static String capitalize(String s) {
		return s.substring(0, 1).toUpperCase(Locale.ROOT) + s.substring(1);
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
		//int fixedLength = Math.min(length, s.length());
		return s.substring(length);
	}
	
	public static String removeSuffix(String s, int length) {
		//int fixedLength = Math.min(length, s.length());
		return s.substring(0, s.length() - length);
	}
	
	public static boolean beginsWith(String s, String check) {
		return starting(s, check.length()).equals(check);
	}
	
	public static String stringListConcat(List<String> list) {
		return String.join(", ", list);
	}
	
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
			switch(c) {
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
				default: out.append(c);
			}
		}
		out.append('$');
		return out.toString();
	}
}
