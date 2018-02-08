package nc.util;

public class StringHelper {
	
	public static String capitalize(String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
	}
	
	public static String starting(String string, int length) {
		int fixedLength = Math.min(length, string.length());
		return string.substring(0, fixedLength);
	}
	
	public static String ending(String string, int length) {
		int fixedLength = Math.min(length, string.length());
		return string.substring(string.length() - fixedLength);
	}
	
	public static String removePrefix(String string, int length) {
		int fixedLength = Math.min(length, string.length());
		return string.substring(length);
	}
	
	public static String removeSuffix(String string, int length) {
		int fixedLength = Math.min(length, string.length());
		return string.substring(0, string.length() - length);
	}
	
	public static boolean beginsWith(String stringIn, String stringCheck) {
		return starting(stringIn, stringCheck.length()) == stringCheck;
	}
}
