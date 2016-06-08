package nc.util;

import cpw.mods.fml.common.Loader;

public class ModHelperBase {

	public static boolean useCofh;
	
	public void register() {}
	
	public static void detectMods() {

		useCofh = Loader.isModLoaded("CoFHCore");
		
	}
}