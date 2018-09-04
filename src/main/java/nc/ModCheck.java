package nc;

import net.minecraftforge.fml.common.Loader;

public class ModCheck {
	
	private static boolean initialized = false;
	
	private static boolean ic2Loaded = false;
	private static boolean mekanismLoaded = false;
	private static boolean craftTweakerLoaded = false;
	private static boolean tinkersLoaded = false;
	private static boolean gregtechLoaded = false;
	private static boolean openComputersLoaded = false;
	
	public static void init() {
		if (initialized) return;
		
		ic2Loaded = Loader.isModLoaded("ic2") || Loader.isModLoaded("IC2");
		mekanismLoaded = Loader.isModLoaded("mekanism") || Loader.isModLoaded("Mekanism");
		craftTweakerLoaded = Loader.isModLoaded("minetweaker3") || Loader.isModLoaded("MineTweaker3") || Loader.isModLoaded("crafttweaker") || Loader.isModLoaded("CraftTweaker2");
		tinkersLoaded = (Loader.isModLoaded("tconstruct") || Loader.isModLoaded("Tinkers' Construct")) && (Loader.isModLoaded("mantle") || Loader.isModLoaded("Mantle"));
		gregtechLoaded = Loader.isModLoaded("gregtech") || Loader.isModLoaded("GregTech Community Edition");
		openComputersLoaded = Loader.isModLoaded("OpenComputers") || Loader.isModLoaded("opencomputers");
		
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
	
	public static boolean tinkersLoaded() {
		return tinkersLoaded;
	}
	
	public static boolean gregtechLoaded() {
		return gregtechLoaded;
	}
	
	public static boolean openComputersLoaded() {
		return openComputersLoaded;
	}
}
