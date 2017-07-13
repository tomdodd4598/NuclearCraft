package nc;

import net.minecraftforge.fml.common.Loader;

public class ModCheck {
	
	public static boolean ic2Loaded = false;
	public static boolean teslaLoaded = false;

	public static void init() {
		ic2Loaded = Loader.isModLoaded("ic2") || Loader.isModLoaded("IC2");
		teslaLoaded = Loader.isModLoaded("tesla") || Loader.isModLoaded("TESLA");
	}

	public static boolean ic2Loaded() {
		return ic2Loaded;
	}
	
	public static boolean teslaLoaded() {
		return teslaLoaded;
	}
}
