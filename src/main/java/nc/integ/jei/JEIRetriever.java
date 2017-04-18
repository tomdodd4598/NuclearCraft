package nc.integ.jei;

import javax.annotation.Nonnull;

public class JEIRetriever {
	static boolean jeiRuntimeAvailable = false;
	
	public static boolean isJeiRuntimeAvailable() {
		return jeiRuntimeAvailable;
	}
	
	public static void setFilterText(@Nonnull String filterText) {
		if (jeiRuntimeAvailable) {
			JEIPeripheral.setFilterText(filterText);
		}
	}
	
	public static @Nonnull String getFilterText() {
		if (jeiRuntimeAvailable) {
			return JEIPeripheral.getFilterText();
		}
		return "";
	}
}
