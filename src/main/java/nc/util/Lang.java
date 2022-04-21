package nc.util;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.util.text.translation.I18n;

public class Lang {
	
	// private static final String REGEX = "\\|";
	public static final char CHAR = '|';
	
	public static String localise(String unloc, Object... args) {
		return I18n.translateToLocalFormatted(unloc, args);
	}
	
	public static String localise(String unloc) {
		return I18n.translateToLocal(unloc);
	}
	
	public static String[] localiseList(String unloc, String... args) {
		return splitList(localise(unloc, (Object[]) args));
	}
	
	public static String[] localiseList(String unloc) {
		return splitList(localise(unloc));
	}
	
	public static List<String> localiseAll(List<String> unloc) {
		List<String> ret = Lists.newArrayList();
		for (String s : unloc) {
			ret.add(localise(s));
		}
		return ret;
	}
	
	public static String[] localiseAll(Lang lang, String... unloc) {
		String[] ret = new String[unloc.length];
		for (int i = 0; i < ret.length; ++i) {
			ret[i] = Lang.localise(unloc[i]);
		}
		return ret;
	}
	
	public static String[] splitList(String list) {
		return list.split("\\|");
	}
	
	public static String[] splitList(String list, String split) {
		return list.split(split);
	}
	
	public static boolean canLocalise(String unloc) {
		return I18n.canTranslate(unloc);
	}
}
