package nc.util;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.util.text.translation.I18n;

public class Lang {
	
	public static String localize(String unloc, Object... args) {
		return I18n.translateToLocalFormatted(unloc, args);
	}
	
	public static String localize(String unloc) {
		return I18n.translateToLocal(unloc);
	}
	
	public static String[] localizeList(String unloc, String... args) {
		return splitList(localize(unloc, (Object[]) args));
	}
	
	public static String[] localizeList(String unloc) {
		return splitList(localize(unloc));
	}
	
	public static List<String> localizeAll(List<String> unloc) {
		List<String> ret = Lists.newArrayList();
		for (String s : unloc) {
			ret.add(localize(s));
		}
		return ret;
	}
	
	public static String[] localizeAll(Lang lang, String... unloc) {
		String[] ret = new String[unloc.length];
		for (int i = 0; i < ret.length; ++i) {
			ret[i] = Lang.localize(unloc[i]);
		}
		return ret;
	}
	
	public static String[] splitList(String list) {
		return list.split("\\|");
	}
	
	public static String[] splitList(String list, String split) {
		return list.split(split);
	}
	
	public static boolean canLocalize(String unloc) {
		return I18n.canTranslate(unloc);
	}
}
