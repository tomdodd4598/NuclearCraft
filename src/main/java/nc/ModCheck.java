package nc;

import net.minecraftforge.fml.common.Loader;

public class ModCheck {
	
	private static boolean initialized = false;
	
	private static boolean cofhCoreLoaded = false;
	private static boolean ic2Loaded = false;
	private static boolean mekanismLoaded = false;
	private static boolean craftTweakerLoaded = false;
	private static boolean mantleLoaded = false;
	private static boolean tinkersLoaded = false;
	private static boolean constructsArmoryLoaded = false;
	private static boolean gregtechLoaded = false;
	private static boolean openComputersLoaded = false;
	private static boolean galacticraftLoaded = false;
	private static boolean techRebornLoaded = false;
	private static boolean baublesLoaded = false;
	private static boolean thermalFoundationLoaded = false;
	
	public static void init() {
		if (initialized) return;
		
		cofhCoreLoaded = Loader.isModLoaded("cofhcore") || Loader.isModLoaded("CoFH Core");
		ic2Loaded = Loader.isModLoaded("ic2") || Loader.isModLoaded("IC2");
		mekanismLoaded = Loader.isModLoaded("mekanism") || Loader.isModLoaded("Mekanism");
		craftTweakerLoaded = Loader.isModLoaded("minetweaker3") || Loader.isModLoaded("MineTweaker3") || Loader.isModLoaded("crafttweaker") || Loader.isModLoaded("CraftTweaker2");
		mantleLoaded = Loader.isModLoaded("mantle") || Loader.isModLoaded("Mantle");
		tinkersLoaded = mantleLoaded && (Loader.isModLoaded("tconstruct") || Loader.isModLoaded("Tinkers' Construct"));
		constructsArmoryLoaded = tinkersLoaded && (Loader.isModLoaded("conarm") || Loader.isModLoaded("Construct's Armory"));
		gregtechLoaded = Loader.isModLoaded("gregtech") || Loader.isModLoaded("GregTech Community Edition");
		openComputersLoaded = Loader.isModLoaded("OpenComputers") || Loader.isModLoaded("opencomputers");
		galacticraftLoaded = Loader.isModLoaded("GalacticraftCore") || Loader.isModLoaded("galacticraftcore") || Loader.isModLoaded("Galacticraft Core");
		techRebornLoaded = Loader.isModLoaded("techreborn") || Loader.isModLoaded("Tech Reborn");
		baublesLoaded = Loader.isModLoaded("baubles") || Loader.isModLoaded("Baubles");
		thermalFoundationLoaded = Loader.isModLoaded("thermalfoundation") || Loader.isModLoaded("Thermal Foundation");
		
		initialized = true;
	}
	
	public static boolean cofhCoreLoaded() {
		return cofhCoreLoaded;
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
	
	public static boolean mantleLoaded() {
		return mantleLoaded;
	}
	
	public static boolean tinkersLoaded() {
		return tinkersLoaded;
	}
	
	public static boolean constructsArmoryLoaded() {
		return constructsArmoryLoaded;
	}
	
	public static boolean gregtechLoaded() {
		return gregtechLoaded;
	}
	
	public static boolean openComputersLoaded() {
		return openComputersLoaded;
	}
	
	public static boolean galacticraftLoaded() {
		return galacticraftLoaded;
	}
	
	public static boolean techRebornLoaded() {
		return techRebornLoaded;
	}
	
	public static boolean baublesLoaded() {
		return baublesLoaded;
	}
	
	public static boolean thermalFoundationLoaded() {
		return thermalFoundationLoaded;
	}
}
