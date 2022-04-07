package nc.util;

public class FontRenderHelper {
	
	protected final static int[] CHAR_WIDTHS = new int[] {6, 6, 6, 6, 6, 6, 4, 6, 6, 6, 6, 6, 6, 6, 6, 4, 4, 6, 7, 6, 6, 6, 6, 6, 6, 1, 1, 1, 1, 1, 1, 1, 1, 2, 5, 6, 6, 6, 6, 3, 5, 5, 5, 6, 2, 6, 2, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 2, 2, 5, 6, 5, 6, 7, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 4, 6, 6, 3, 6, 6, 6, 6, 6, 5, 6, 6, 2, 6, 5, 3, 6, 6, 6, 6, 6, 6, 6, 4, 6, 6, 6, 6, 6, 6, 5, 2, 5, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 3, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 6, 3, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 6, 2, 6, 6, 8, 9, 9, 6, 6, 6, 8, 8, 6, 8, 8, 8, 8, 8, 6, 6, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 6, 9, 9, 9, 5, 9, 9, 8, 7, 7, 8, 7, 8, 7, 8, 7, 8, 8, 7, 9, 9, 6, 7, 7, 7, 7, 7, 9, 6, 7, 8, 6, 6, 6, 9, 7, 6, 7, 1};
	
	public static String[] wrapString(String string, int textWidth) {
		return wrapStringToWidthRaw(string, textWidth).split("\n");
	}
	
	private static String wrapStringToWidthRaw(String string, int textWidth) {
		int i = sizeStringToWidth(string, textWidth);
		
		if (string.length() <= i) {
			return string;
		}
		else {
			String s = string.substring(0, i);
			char c0 = string.charAt(i);
			boolean flag = c0 == ' ' || c0 == '\n';
			String s1 = getFormatFromString(s) + string.substring(i + (flag ? 1 : 0));
			
			return s + "\n" + wrapStringToWidthRaw(s1, textWidth);
		}
	}
	
	public static int maxSize(String string) {
		int use;
		String longestWord = "";
		while (!string.isEmpty()) {
			string = string.trim();
			use = string.indexOf(" ");
			if (use < 0) {
				break;
			}
			String cut = string.substring(0, use);
			if (sizeString(cut) > sizeString(longestWord)) {
				longestWord = cut;
			}
			string = string.substring(use + 1);
		}
		return sizeString(longestWord);
	}
	
	private static int sizeString(String string) {
		int size = 0;
		for (int i = 0; i < string.length(); ++i) {
			char c = string.charAt(i);
			size += getCharWidth(c);
		}
		return size;
	}
	
	private static int sizeStringToWidth(String string, int textWidth) {
		int i = string.length();
		int j = 0;
		int k = 0;
		int l = -1;
		
		for (boolean flag = false; k < i; ++k) {
			char c0 = string.charAt(k);
			
			switch (c0) {
				case '\n':
					--k;
					break;
				case ' ':
					l = k;
					//$FALL-THROUGH$
				default:
					j += getCharWidth(c0);
					
					if (flag) {
						++j;
					}
					
					break;
				case '\u00a7':
					if (k < i - 1) {
						++k;
						char c1 = string.charAt(k);
						
						if (c1 != 'l' && c1 != 'L') {
							if (c1 == 'r' || c1 == 'R' || isFormatColor(c1)) {
								flag = false;
							}
						}
						else {
							flag = true;
						}
					}
			}
			
			if (c0 == '\n') {
				++k;
				l = k;
				break;
			}
			
			if (j > textWidth) {
				break;
			}
		}
		
		return k != i && l != -1 && l < k ? l : k;
	}
	
	public static int getCharWidth(char character) {
		if (character == 160) {
			return 4;
		}
		if (character == 167) {
			return -1;
		}
		else if (character == ' ') {
			return 4;
		}
		else {
			int i = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(character);
			
			if (character > 0 && i != -1) {
				return CHAR_WIDTHS[i];
			}
			else {
				return 0;
			}
		}
	}
	
	public static String getFormatFromString(String text) {
		String s = "";
		int i = -1;
		int j = text.length();
		
		while ((i = text.indexOf(167, i + 1)) != -1) {
			if (i < j - 1) {
				char c0 = text.charAt(i + 1);
				
				if (isFormatColor(c0)) {
					s = "\u00a7" + c0;
				}
				else if (isFormatSpecial(c0)) {
					s = s + "\u00a7" + c0;
				}
			}
		}
		
		return s;
	}
	
	private static boolean isFormatColor(char colorChar) {
		return colorChar >= '0' && colorChar <= '9' || colorChar >= 'a' && colorChar <= 'f' || colorChar >= 'A' && colorChar <= 'F';
	}
	
	private static boolean isFormatSpecial(char formatChar) {
		return formatChar >= 'k' && formatChar <= 'o' || formatChar >= 'K' && formatChar <= 'O' || formatChar == 'r' || formatChar == 'R';
	}
}
