package nc;

import net.minecraftforge.fml.common.Loader;

public class ModCheck {
	
	public static boolean initialized = false;
	
	static boolean ic2Loaded = false;
	static boolean mekanismLoaded = false;
	static boolean craftTweakerLoaded = false;
	
	public static void init() {
		if (initialized) return;
		
		ic2Loaded = Loader.isModLoaded("ic2") || Loader.isModLoaded("IC2");
		mekanismLoaded = Loader.isModLoaded("mekanism") || Loader.isModLoaded("Mekanism");
		craftTweakerLoaded = Loader.isModLoaded("minetweaker3") || Loader.isModLoaded("MineTweaker3") || Loader.isModLoaded("crafttweaker") || Loader.isModLoaded("CraftTweaker2");
		
		initialized = true;
	}

	public static boolean ic2Loaded() {
		return ic2Loaded;
	}
	
	public static boolean mekanismLoaded() {
		return mekanismLoaded;
	}
	
	public static boolean craftTweakerLoaded() {
		return craftTweakerLoaded;
	}
}
