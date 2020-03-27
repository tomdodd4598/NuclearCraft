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
	private static boolean jeiLoaded = false;
	private static boolean projectELoaded = false;
	private static boolean commonCapabilitiesLoaded = false;
	private static boolean gameStagesLoaded = false;
	private static boolean cubicChunksLoaded = false;
	
	public static void init() {
		if (initialized) return;
		
		cofhCoreLoaded = Loader.isModLoaded("cofhcore");
		ic2Loaded = Loader.isModLoaded("ic2");
		mekanismLoaded = Loader.isModLoaded("mekanism");
		craftTweakerLoaded = Loader.isModLoaded("crafttweaker");
		mantleLoaded = Loader.isModLoaded("mantle");
		tinkersLoaded = mantleLoaded && Loader.isModLoaded("tconstruct");
		constructsArmoryLoaded = tinkersLoaded && Loader.isModLoaded("conarm");
		gregtechLoaded = Loader.isModLoaded("gregtech");
		openComputersLoaded = Loader.isModLoaded("opencomputers");
		galacticraftLoaded = Loader.isModLoaded("galacticraftcore");
		techRebornLoaded = Loader.isModLoaded("techreborn");
		baublesLoaded = Loader.isModLoaded("baubles");
		thermalFoundationLoaded = Loader.isModLoaded("thermalfoundation");
		jeiLoaded = Loader.isModLoaded("jei");
		projectELoaded = Loader.isModLoaded("projecte");
		commonCapabilitiesLoaded = Loader.isModLoaded("commoncapabilities");
		gameStagesLoaded = Loader.isModLoaded("gamestages");
		cubicChunksLoaded = Loader.isModLoaded("cubicchunks");
		
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
	
	public static boolean jeiLoaded() {
		return jeiLoaded;
	}
	
	public static boolean projectELoaded() {
		return projectELoaded;
	}
	
	public static boolean commonCapabilitiesLoaded() {
		return commonCapabilitiesLoaded;
	}
	
	public static boolean gameStagesLoaded() {
		return gameStagesLoaded;
	}
	
	public static boolean cubicChunksLoaded() {
		return cubicChunksLoaded;
	}
}
