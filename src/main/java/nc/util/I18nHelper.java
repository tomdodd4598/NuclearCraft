package nc.util;

import javax.script.*;

public class I18nHelper {
	
	public static final ScriptEngine JS_ENGINE = new ScriptEngineManager(null).getEngineByName("JavaScript");
	
	public static int getPluralRule(int count) throws ScriptException {
		return (int) JS_ENGINE.eval(Lang.localise("nc.sf.plural_rule", count));
	}
	
	public static String getPluralForm(String unlocBase, int count, Object... args) {
		try {
			return Lang.localise(unlocBase + getPluralRule(count), args);
		}
		catch (ScriptException e) {
			e.printStackTrace();
			return unlocBase;
		}
	}
}
