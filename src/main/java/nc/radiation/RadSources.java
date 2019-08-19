package nc.radiation;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.ints.Int2DoubleMap;
import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.init.NCBlocks;
import nc.util.RegistryHelper;
import net.minecraft.block.Block;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class RadSources {
	
	public static final Set<String> ORE_BLACKLIST = new ObjectOpenHashSet<>();
	public static final IntSet STACK_BLACKLIST = new IntOpenHashSet();
	public static final Set<String> FLUID_BLACKLIST = new ObjectOpenHashSet<>();
	
	public static final Object2DoubleMap<String> ORE_MAP = new Object2DoubleOpenHashMap<>();
	public static final Int2DoubleMap STACK_MAP = new Int2DoubleOpenHashMap();
	public static final Object2DoubleMap<String> FLUID_MAP = new Object2DoubleOpenHashMap<>();
	
	public static final Int2DoubleMap FOOD_RAD_MAP = new Int2DoubleOpenHashMap();
	public static final Int2DoubleMap FOOD_RESISTANCE_MAP = new Int2DoubleOpenHashMap();
	
	public static void addToOreMap(String ore, double radiation) {
		if (ORE_BLACKLIST.contains(ore)) return;
		ORE_MAP.put(ore, radiation);
	}
	
	public static void addToStackMap(ItemStack stack, double radiation) {
		int packed = RecipeItemHelper.pack(stack);
		if (packed == 0 || STACK_BLACKLIST.contains(packed)) return;
		STACK_MAP.put(packed, radiation);
	}
	
	public static void addToFluidMap(String fluidName, double radiation) {
		if (FLUID_BLACKLIST.contains(fluidName)) return;
		FLUID_MAP.put(fluidName, radiation);
		
		Fluid fluid = FluidRegistry.getFluid(fluidName);
		if (fluid != null) {
			Block fluidBlock = fluid.getBlock();
			if (fluidBlock != null) {
				addToStackMap(new ItemStack(fluidBlock), radiation);
			}
		}
	}
	
	public static void addToFoodMaps(ItemStack stack, double radiation, double resistance) {
		int packed = RecipeItemHelper.pack(stack);
		if (packed == 0) return;
		FOOD_RAD_MAP.put(packed, radiation);
		FOOD_RESISTANCE_MAP.put(packed, resistance);
	}
	
	public static final double INGOT = 1D;
	public static final double NUGGET = 1D/9D;
	public static final double HALF = 1D/2D;
	public static final double THIRD = 1D/3D;
	public static final double SMALL = 1D/4D;
	public static final double DOUBLE = 2D;
	public static final double TRIPLE = 3D;
	public static final double QUAD = 4D;
	public static final double FIVE = 5D;
	public static final double SIX = 6D;
	public static final double BLOCK = 9D;
	public static final double SLAB = 9D/2D;
	public static final double FLUID = 125D/18D;
	
	public static final List<String> MATERIAL_INGOT_NAME_LIST = Lists.newArrayList("ingot", "dust", "dustDirty", "clump", "shard", "crystal", "crushed", "dustImpure", "dustPure", "plate", "blockSheetmetal");
	public static final List<String> MATERIAL_NUGGET_NAME_LIST = Lists.newArrayList("tinyDust", "dustTiny", "nugget");
	public static final List<String> MATERIAL_HALF_NAME_LIST = Lists.newArrayList("rod", "stick", "slabSheetmetal");
	public static final List<String> MATERIAL_THIRD_NAME_LIST = Lists.newArrayList("coin");
	public static final List<String> MATERIAL_SMALL_NAME_LIST = Lists.newArrayList("smallDust", "dustSmall", "ore", "oreGravel");
	public static final List<String> MATERIAL_DOUBLE_NAME_LIST = Lists.newArrayList();
	public static final List<String> MATERIAL_TRIPLE_NAME_LIST = Lists.newArrayList();
	public static final List<String> MATERIAL_QUAD_NAME_LIST = Lists.newArrayList("gear");
	public static final List<String> MATERIAL_FIVE_NAME_LIST = Lists.newArrayList();
	public static final List<String> MATERIAL_SIX_NAME_LIST = Lists.newArrayList();
	public static final List<String> MATERIAL_BLOCK_NAME_LIST = Lists.newArrayList("block", "plateDense");
	public static final List<String> MATERIAL_SLAB_NAME_LIST = Lists.newArrayList("slab");
	
	public static final List<String> ISOTOPE_INGOT_NAME_LIST = Lists.newArrayList("ingot");
	public static final List<String> ISOTOPE_NUGGET_NAME_LIST = Lists.newArrayList("nugget");
	public static final List<String> ISOTOPE_BLOCK_NAME_LIST = Lists.newArrayList("block");
	
	public static final List<String> ORE_PREFIXES = Lists.newArrayList("ore");
	
	public static final double FUSION = 0.000000315D;
	public static final double NEUTRON = 0.00505D;
	public static final double TRITIUM = 0.0115D;
	
	public static final double CAESIUM_137 = 0.033D;
	public static final double CORIUM = 0.0000165D;
	
	public static final double THORIUM = 0.0000015D;
	public static final double URANIUM = 0.000000000385D;
	public static final double PLUTONIUM = 0.000042D;
	
	public static final double THORIUM_230 = 0.0000135D;
	public static final double THORIUM_232 = 0.0000000000715D;
	
	public static final double URANIUM_233 = 0.0000063D;
	public static final double URANIUM_235 = 0.00000000145D;
	public static final double URANIUM_238 = 0.000000000225D;
	
	public static final double NEPTUNIUM_236 = 0.0000065D;
	public static final double NEPTUNIUM_237 = 0.00000047D;
	
	public static final double PLUTONIUM_238 = 0.0115D;
	public static final double PLUTONIUM_239 = 0.0000415D;
	public static final double PLUTONIUM_241 = 0.0715D;
	public static final double PLUTONIUM_242 = 0.0000027D;
	
	public static final double AMERICIUM_241 = 0.00235D;
	public static final double AMERICIUM_242 = 0.0071D;
	public static final double AMERICIUM_243 = 0.00014D;
	
	public static final double CURIUM_243 = 0.0345D;
	public static final double CURIUM_245 = 0.00012D;
	public static final double CURIUM_246 = 0.000215D;
	public static final double CURIUM_247 = 0.0000000645D;
	
	public static final double BERKELIUM_247 = 0.000725D;
	public static final double BERKELIUM_248 = 0.00325D;
	
	public static final double CALIFORNIUM_249 = 0.00285D;
	public static final double CALIFORNIUM_250 = 0.0765D;
	public static final double CALIFORNIUM_251 = 0.00115D;
	public static final double CALIFORNIUM_252 = 0.38D;
	
	public static void init() {
		for (String oreInfo : NCConfig.radiation_ores_blacklist) {
			ORE_BLACKLIST.add(oreInfo);
		}
		for (String itemInfo : NCConfig.radiation_items_blacklist) {
			ItemStack stack = RegistryHelper.itemStackFromRegistry(itemInfo);
			if (stack != null) STACK_BLACKLIST.add(RecipeItemHelper.pack(stack));
		}
		for (String blockInfo : NCConfig.radiation_blocks_blacklist) {
			ItemStack stack = RegistryHelper.blockStackFromRegistry(blockInfo);
			if (stack != null) STACK_BLACKLIST.add(RecipeItemHelper.pack(stack));
		}
		for (String fluidInfo : NCConfig.radiation_fluids_blacklist) {
			FLUID_BLACKLIST.add(fluidInfo);
		}
		
		if (ModCheck.gregtechLoaded()) {
			MATERIAL_INGOT_NAME_LIST.addAll(Lists.newArrayList("crushedPurified", "crushedCentrifuged", "toolHeadShovel"));
			MATERIAL_SMALL_NAME_LIST.addAll(Lists.newArrayList("bolt", "screw", "oreNetherrack", "oreEndstone", "oreSand", "oreGravel", "oreBlackgranite", "oreRedgranite", "oreMarble", "oreBasalt"));
			MATERIAL_DOUBLE_NAME_LIST.addAll(Lists.newArrayList("toolHeadSword", "toolHeadHoe", "toolHeadFile", "toolHeadSaw", "toolHeadChainsaw"));
			MATERIAL_TRIPLE_NAME_LIST.addAll(Lists.newArrayList("toolHeadPickaxe", "toolHeadAxe", "toolHeadSense"));
			MATERIAL_QUAD_NAME_LIST.addAll(Lists.newArrayList("toolHeadDrill", "toolHeadWrench", "toolHeadPlow", "toolHeadBuzzSaw"));
			MATERIAL_FIVE_NAME_LIST.add("turbineBlade");
			MATERIAL_SIX_NAME_LIST.addAll(Lists.newArrayList("toolHeadHammer", "toolHeadUniversalSpade"));
			
			ORE_PREFIXES.addAll(Lists.newArrayList("oreNetherrack", "oreEndstone", "oreSand", "oreNetherrack", "oreBlackgranite", "oreRedgranite", "oreMarble", "oreBasalt"));
		}
		
		putMaterial(THORIUM, "Thorium");
		putMaterial(URANIUM, "Uranium", "Yellorium");
		putMaterial(PLUTONIUM, "Plutonium", "Blutonium");
		
		putIsotope(THORIUM_230, "Thorium230");
		putIsotope(THORIUM_232, "Thorium232");
		
		putIsotope(URANIUM_233, "Uranium233");
		putMaterial(URANIUM_235, "Uranium235");
		putMaterial(URANIUM_238, "Uranium238", "Cyanite");
		
		putIsotope(NEPTUNIUM_236, "Neptunium236");
		putIsotope(NEPTUNIUM_237, "Neptunium237");
		
		putIsotope(PLUTONIUM_238, "Plutonium238");
		putMaterial(PLUTONIUM_239, "Plutonium239");
		putMaterial(PLUTONIUM_241, "Plutonium241");
		putIsotope(PLUTONIUM_242, "Plutonium242");
		
		putIsotope(AMERICIUM_241, "Americium241");
		putIsotope(AMERICIUM_242, "Americium242");
		putIsotope(AMERICIUM_243, "Americium243");
		
		putIsotope(CURIUM_243, "Curium243");
		putIsotope(CURIUM_245, "Curium245");
		putIsotope(CURIUM_246, "Curium246");
		putIsotope(CURIUM_247, "Curium247");
		
		putIsotope(BERKELIUM_247, "Berkelium247");
		putIsotope(BERKELIUM_248, "Berkelium248");
		
		putIsotope(CALIFORNIUM_249, "Californium249");
		putIsotope(CALIFORNIUM_250, "Californium250");
		putIsotope(CALIFORNIUM_251, "Californium251");
		putIsotope(CALIFORNIUM_252, "Californium252");
		
		putFuel(THORIUM_232, 9, THORIUM_230, 0, "TBU", "tbu");
		
		putFuel(URANIUM_238, URANIUM_233, "U233", "u_233");
		putFuel(URANIUM_238, URANIUM_235, "U235", "u_235");
		
		putFuel(NEPTUNIUM_237, NEPTUNIUM_236, "N236", "n_236");
		
		putFuel(PLUTONIUM_242, PLUTONIUM_239, "P239", "p_239");
		putFuel(PLUTONIUM_242, PLUTONIUM_241, "P241", "p_241");
		
		putFuel(URANIUM_238, 8, PLUTONIUM_239, 1, "MOX239", "mox_239");
		putFuel(URANIUM_238, 8, PLUTONIUM_241, 1, "MOX241", "mox_241");
		
		putFuel(AMERICIUM_243, AMERICIUM_242, "A242", "a_242");
		
		putFuel(CURIUM_246, CURIUM_243, "Cm243", "cm_243");
		putFuel(CURIUM_246, CURIUM_245, "Cm245", "cm_245");
		putFuel(CURIUM_246, CURIUM_247, "Cm247", "cm_247");
		
		putFuel(BERKELIUM_247, BERKELIUM_248, "B248", "b_248");
		
		putFuel(CALIFORNIUM_252, CALIFORNIUM_249, "Cf249", "cf_249");
		putFuel(CALIFORNIUM_252, CALIFORNIUM_251, "Cf251", "cf_251");
		
		putDepletedFuel(URANIUM_233, 16, URANIUM_235, 8, NEPTUNIUM_236, 8, NEPTUNIUM_237, 32, "TBU", "tbu");
		
		putDepletedFuel(URANIUM_238, 40, NEPTUNIUM_237, 8, PLUTONIUM_239, 8, PLUTONIUM_241, 8, "LEU235", "leu_235");
		putDepletedFuel(URANIUM_238, 20, NEPTUNIUM_237, 16, PLUTONIUM_239, 4, PLUTONIUM_242, 24, "HEU235", "heu_235");
		
		putDepletedFuel(PLUTONIUM_239, 4, PLUTONIUM_241, 4, PLUTONIUM_242, 32, AMERICIUM_243, 24, "LEU233", "leu_233");
		putDepletedFuel(NEPTUNIUM_236, 32, NEPTUNIUM_237, 8, PLUTONIUM_242, 16, AMERICIUM_243, 8, "HEU233", "heu_233");
		
		putDepletedFuel(NEPTUNIUM_237, 4, PLUTONIUM_242, 32, AMERICIUM_242, 8, AMERICIUM_243, 20, "LEN236", "len_236");
		putDepletedFuel(URANIUM_238, 16, PLUTONIUM_238, 8, PLUTONIUM_239, 8, PLUTONIUM_242, 32, "HEN236", "hen_236");
		
		putDepletedFuel(URANIUM_238, 40, PLUTONIUM_242, 12, AMERICIUM_243, 8, CURIUM_243, 4, "MOX239", "mox_239");
		putDepletedFuel(URANIUM_238, 40, PLUTONIUM_241, 8, PLUTONIUM_242, 8, CURIUM_246, 8, "MOX241", "mox_241");
		
		putDepletedFuel(PLUTONIUM_239, 8, PLUTONIUM_242, 24, CURIUM_243, 4, CURIUM_246, 28, "LEP239", "lep_239");
		putDepletedFuel(AMERICIUM_241, 8, AMERICIUM_242, 24, CURIUM_245, 8, CURIUM_246, 24, "HEP239", "hep_239");
		
		putDepletedFuel(PLUTONIUM_242, 4, AMERICIUM_242, 4, AMERICIUM_243, 8, CURIUM_246, 48, "LEP241", "lep_241");
		putDepletedFuel(AMERICIUM_241, 8, CURIUM_245, 8, CURIUM_246, 24, CURIUM_247, 24, "HEP241", "hep_241");
		
		putDepletedFuel(CURIUM_243, 8, CURIUM_245, 8, CURIUM_246, 40, CURIUM_247, 8, "LEA242", "lea_242");
		putDepletedFuel(CURIUM_245, 16, CURIUM_246, 32, CURIUM_247, 8, BERKELIUM_247, 8, "HEA242", "hea_242");
		
		putDepletedFuel(CURIUM_246, 32, BERKELIUM_247, 16, BERKELIUM_248, 8, CALIFORNIUM_249, 8, "LECm243", "lecm_243");
		putDepletedFuel(CURIUM_246, 24, BERKELIUM_247, 24, BERKELIUM_248, 8, CALIFORNIUM_249, 8, "HECm243", "hecm_243");
		
		putDepletedFuel(BERKELIUM_247, 40, BERKELIUM_248, 8, CALIFORNIUM_249, 4, CALIFORNIUM_252, 12, "LECm245", "lecm_245");
		putDepletedFuel(BERKELIUM_247, 48, BERKELIUM_248, 4, CALIFORNIUM_249, 4, CALIFORNIUM_251, 8, "HECm245", "hecm_245");
		
		putDepletedFuel(BERKELIUM_247, 20, BERKELIUM_248, 4, CALIFORNIUM_251, 8, CALIFORNIUM_252, 32, "LECm247", "lecm_247");
		putDepletedFuel(BERKELIUM_248, 8, CALIFORNIUM_249, 8, CALIFORNIUM_251, 24, CALIFORNIUM_252, 24, "HECm247", "hecm_247");
		
		putDepletedFuel(CALIFORNIUM_249, 4, CALIFORNIUM_251, 4, CALIFORNIUM_252, 28, CALIFORNIUM_252, 28, "LEB248", "leb_248");
		putDepletedFuel(CALIFORNIUM_250, 8, CALIFORNIUM_251, 8, CALIFORNIUM_252, 24, CALIFORNIUM_252, 24, "HEB248", "heb_248");
		
		putDepletedFuel(CALIFORNIUM_250, 16, CALIFORNIUM_251, 8, CALIFORNIUM_252, 20, CALIFORNIUM_252, 20, "LECf249", "lecf_249");
		putDepletedFuel(CALIFORNIUM_250, 32, CALIFORNIUM_251, 16, CALIFORNIUM_252, 8, CALIFORNIUM_252, 8, "HECf249", "hecf_249");
		
		putDepletedFuel(CALIFORNIUM_251, 4, CALIFORNIUM_252, 20, CALIFORNIUM_252, 20, CALIFORNIUM_252, 20, "LECf251", "lecf_251");
		putDepletedFuel(CALIFORNIUM_251, 16, CALIFORNIUM_252, 16, CALIFORNIUM_252, 16, CALIFORNIUM_252, 16, "HECf251", "hecf_251");
		
		put(URANIUM_238*4, "plateDU");
		
		put(URANIUM_238/4D, NCBlocks.rtg_uranium);
		put(PLUTONIUM_238/4D, NCBlocks.rtg_plutonium);
		put(AMERICIUM_241/4D, NCBlocks.rtg_americium);
		put(CALIFORNIUM_250/4D, NCBlocks.rtg_californium);
		
		put(THORIUM_230*9D/4D, NCBlocks.helium_collector);
		put(THORIUM_230*8D*9D/4D, NCBlocks.helium_collector_compact);
		put(THORIUM_230*64D*9D/4D, NCBlocks.helium_collector_dense);
		
		put(TRITIUM/512D, NCBlocks.tritium_lamp);
		
		if (ModCheck.gregtechLoaded()) {
			for (String prefix : ORE_PREFIXES) {
				put(17D*THORIUM/4D, prefix + "Monazite");
			}
		}
		
		putFluid(FUSION, "plasma");
		putFluid(NEUTRON, "neutron");
		putFluid(TRITIUM, "tritium");
		
		putFluid(CAESIUM_137, "caesium_137");
		putFluid(CORIUM, "corium");
		
		putFissionFluid(THORIUM, "thorium");
		putFissionFluid(URANIUM, "uranium", "yellorium");
		putFissionFluid(PLUTONIUM, "plutonium", "blutonium");
		
		putFissionFluid(THORIUM_230, "thorium_230");
		//putFissionFluid(THORIUM_232, "thorium_232");
		
		putFissionFluid(URANIUM_233, "uranium_233");
		putFissionFluid(URANIUM_235, "uranium_235");
		putFissionFluid(URANIUM_238, "uranium_238", "cyanite");
		
		putFissionFluid(NEPTUNIUM_236, "neptunium_236");
		putFissionFluid(NEPTUNIUM_237, "neptunium_237");
		
		putFissionFluid(PLUTONIUM_238, "plutonium_238");
		putFissionFluid(PLUTONIUM_239, "plutonium_239");
		putFissionFluid(PLUTONIUM_241, "plutonium_241");
		putFissionFluid(PLUTONIUM_242, "plutonium_242");
		
		putFissionFluid(AMERICIUM_241, "americium_241");
		putFissionFluid(AMERICIUM_242, "americium_242");
		putFissionFluid(AMERICIUM_243, "americium_243");
		
		putFissionFluid(CURIUM_243, "curium_243");
		putFissionFluid(CURIUM_245, "curium_245");
		putFissionFluid(CURIUM_246, "curium_246");
		putFissionFluid(CURIUM_247, "curium_247");
		
		putFissionFluid(BERKELIUM_247, "berkelium_247");
		putFissionFluid(BERKELIUM_248, "berkelium_248");
		
		putFissionFluid(CALIFORNIUM_249, "californium_249");
		putFissionFluid(CALIFORNIUM_250, "californium_250");
		putFissionFluid(CALIFORNIUM_251, "californium_251");
		putFissionFluid(CALIFORNIUM_252, "californium_252");
		
		// Custom and Stack Entries
		
		for (String oreInfo : NCConfig.radiation_ores) {
			int scorePos = oreInfo.lastIndexOf('_');
			if (scorePos == -1) continue;
			addToOreMap(oreInfo.substring(0, scorePos), Double.parseDouble(oreInfo.substring(scorePos + 1)));
		}
		for (String itemInfo : NCConfig.radiation_items) {
			int scorePos = itemInfo.lastIndexOf('_');
			if (scorePos == -1) continue;
			ItemStack stack = RegistryHelper.itemStackFromRegistry(itemInfo.substring(0, scorePos));
			if (stack != null) addToStackMap(stack, Double.parseDouble(itemInfo.substring(scorePos + 1)));
		}
		for (String blockInfo : NCConfig.radiation_blocks) {
			int scorePos = blockInfo.lastIndexOf('_');
			if (scorePos == -1) continue;
			ItemStack stack = RegistryHelper.blockStackFromRegistry(blockInfo.substring(0, scorePos));
			if (stack != null) addToStackMap(stack, Double.parseDouble(blockInfo.substring(scorePos + 1)));
		}
		for (String fluidInfo : NCConfig.radiation_fluids) {
			int scorePos = fluidInfo.lastIndexOf('_');
			if (scorePos == -1) continue;
			addToFluidMap(fluidInfo.substring(0, scorePos), Double.parseDouble(fluidInfo.substring(scorePos + 1)));
		}
		
		// Food Entries
		
		for (String itemInfo : NCConfig.radiation_foods) {
			int scorePos = itemInfo.lastIndexOf('_');
			if (scorePos == -1) continue;
			double resistance = Double.parseDouble(itemInfo.substring(scorePos + 1));
			itemInfo = itemInfo.substring(0, scorePos);
			scorePos = itemInfo.lastIndexOf('_');
			if (scorePos == -1) continue;
			ItemStack stack = RegistryHelper.itemStackFromRegistry(itemInfo.substring(0, scorePos));
			double rads = Double.parseDouble(itemInfo.substring(scorePos + 1));
			if (stack != null && (rads != 0D || resistance != 0D) && stack.getItem() instanceof ItemFood) addToFoodMaps(stack, rads, resistance);
		}
	}
	
	public static void postInit() {
		ORE_MAP.entrySet().forEach(ent -> OreDictionary.getOres(ent.getKey()).forEach(s -> addToStackMap(s, ent.getValue())));
	}
	
	public static void refreshRadSources() {
		STACK_BLACKLIST.clear();
		STACK_MAP.clear();
		FOOD_RAD_MAP.clear();
		FOOD_RESISTANCE_MAP.clear();
		
		init();
		postInit();
	}
	
	public static void putMaterial(double radiation, String... ores) {
		for (String ore : ores) for (String suffix : new String[] {"", "Oxide"}) {
			for (String prefix : MATERIAL_INGOT_NAME_LIST) addToOreMap(prefix + ore + suffix, radiation*INGOT);
			for (String prefix : MATERIAL_NUGGET_NAME_LIST) addToOreMap(prefix + ore + suffix, radiation*NUGGET);
			for (String prefix : MATERIAL_HALF_NAME_LIST) addToOreMap(prefix + ore + suffix, radiation*HALF);
			for (String prefix : MATERIAL_THIRD_NAME_LIST) addToOreMap(prefix + ore + suffix, radiation*THIRD);
			for (String prefix : MATERIAL_SMALL_NAME_LIST) addToOreMap(prefix + ore + suffix, radiation*SMALL);
			for (String prefix : MATERIAL_DOUBLE_NAME_LIST) addToOreMap(prefix + ore + suffix, radiation*DOUBLE);
			for (String prefix : MATERIAL_TRIPLE_NAME_LIST) addToOreMap(prefix + ore + suffix, radiation*TRIPLE);
			for (String prefix : MATERIAL_QUAD_NAME_LIST) addToOreMap(prefix + ore + suffix, radiation*QUAD);
			for (String prefix : MATERIAL_FIVE_NAME_LIST) addToOreMap(prefix + ore + suffix, radiation*FIVE);
			for (String prefix : MATERIAL_SIX_NAME_LIST) addToOreMap(prefix + ore + suffix, radiation*SIX);
			for (String prefix : MATERIAL_BLOCK_NAME_LIST) addToOreMap(prefix + ore + suffix, radiation*BLOCK);
			for (String prefix : MATERIAL_SLAB_NAME_LIST) addToOreMap(prefix + ore + suffix, radiation*SLAB);
		}
	}
	
	public static void putIsotope(double radiation, String... ores) {
		for (String ore : ores) for (String suffix : new String[] {"", "Base", "Oxide"}) {
			for (String prefix : ISOTOPE_INGOT_NAME_LIST) addToOreMap(prefix + ore + suffix, radiation*INGOT);
			for (String prefix : ISOTOPE_NUGGET_NAME_LIST) addToOreMap(prefix + ore + suffix, radiation*NUGGET);
			for (String prefix : ISOTOPE_BLOCK_NAME_LIST) addToOreMap(prefix + ore + suffix, radiation*BLOCK);
		}
	}
	
	public static void putFluid(double radiation, String... fluids) {
		for (String fluid : fluids) {
			addToFluidMap(fluid, radiation*FLUID);
		}
	}
	
	public static void putFissionFluid(double radiation, String... fluids) {
		for (String fluid : fluids) {
			addToFluidMap(fluid, radiation*FLUID);
			addToFluidMap(fluid + "_fluoride", radiation*FLUID);
			addToFluidMap(fluid + "_fluoride_flibe", radiation*FLUID/2D);
		}
	}
	
	public static double getFuelRadiation(double rad1, int amount1, double rad2, int amount2) {
		return (rad1*amount1 + rad2*amount2)*INGOT;
	}
	
	public static void putFuel(double rad1, int amount1, double rad2, int amount2, String ore, String fluid) {
		double radiation = getFuelRadiation(rad1, amount1, rad2, amount2);
		addToOreMap("fuel" + ore, radiation);
		addToOreMap("fuel" + ore + "Oxide", radiation);
		addToOreMap("fuelRod" + ore, radiation);
		addToOreMap("fuelRod" + ore + "Oxide", radiation);
		addToFluidMap("fuel_" + fluid, radiation*FLUID/9D);
		addToFluidMap("fuel_" + fluid + "_fluoride", radiation*FLUID/9D);
		addToFluidMap("fuel_" + fluid + "_fluoride_flibe", radiation*FLUID/18D);
	}
	
	public static void putFuel(double fertile, double fissile, String ore, String fluid) {
		putFuel(fertile, 8, fissile, 1, "LE" + ore, "le" + fluid);
		putFuel(fertile, 5, fissile, 4, "HE" + ore, "he" + fluid);
	}
	
	public static double getDepletedFuelRadiation(double rad1, int amount1, double rad2, int amount2, double rad3, int amount3, double rad4, int amount4) {
		return (rad1*amount1 + rad2*amount2 + rad3*amount3 + rad4*amount4)*NUGGET;
	}
	
	public static void putDepletedFuel(double rad1, int amount1, double rad2, int amount2, double rad3, int amount3, double rad4, int amount4, String ore, String fluid) {
		double radiation = getDepletedFuelRadiation(rad1, amount1, rad2, amount2, rad3, amount3, rad4, amount4);
		addToOreMap("depletedFuel" + ore, radiation);
		addToOreMap("depletedFuel" + ore + "Oxide", radiation);
		addToOreMap("depletedFuelRod" + ore, radiation);
		addToOreMap("depletedFuelRod" + ore + "Oxide", radiation);
		addToFluidMap("depleted_fuel_" + fluid, radiation*FLUID/9D);
		addToFluidMap("depleted_fuel_" + fluid + "_fluoride", radiation*FLUID/9D);
		addToFluidMap("depleted_fuel_" + fluid + "_fluoride_flibe", radiation*FLUID/18D);
	}
	
	public static void put(double radiation, String... ores) {
		for (String ore : ores) addToOreMap(ore, radiation);
	}
	
	public static void put(double radiation, ItemStack... stacks) {
		for (ItemStack stack : stacks) addToStackMap(stack, radiation);
	}
	
	public static void put(double radiation, Item... items) {
		for (Item item : items) addToStackMap(new ItemStack(item), radiation);
	}
	
	public static void put(double radiation, Block... blocks) {
		for (Block block : blocks) addToStackMap(new ItemStack(block), radiation);
	}
	
	// Fuels
	
	public static final double TBU = getFuelRadiation(THORIUM_232, 9, THORIUM_230, 0);
	
	public static final double LEU_233 = getFuelRadiation(URANIUM_238, 8, URANIUM_233, 1);
	public static final double HEU_233 = getFuelRadiation(URANIUM_238, 5, URANIUM_233, 4);
	public static final double LEU_235 = getFuelRadiation(URANIUM_238, 8, URANIUM_235, 1);
	public static final double HEU_235 = getFuelRadiation(URANIUM_238, 5, URANIUM_235, 4);
	
	public static final double LEN_236 = getFuelRadiation(NEPTUNIUM_237, 8, NEPTUNIUM_236, 1);
	public static final double HEN_236 = getFuelRadiation(NEPTUNIUM_237, 5, NEPTUNIUM_236, 4);
	
	public static final double LEP_239 = getFuelRadiation(PLUTONIUM_242, 8, PLUTONIUM_239, 1);
	public static final double HEP_239 = getFuelRadiation(PLUTONIUM_242, 5, PLUTONIUM_239, 4);
	public static final double LEP_241 = getFuelRadiation(PLUTONIUM_242, 8, PLUTONIUM_241, 1);
	public static final double HEP_241 = getFuelRadiation(PLUTONIUM_242, 5, PLUTONIUM_241, 4);
	
	public static final double MOX_239 = getFuelRadiation(URANIUM_238, 8, PLUTONIUM_239, 1);
	public static final double MOX_241 = getFuelRadiation(URANIUM_238, 8, PLUTONIUM_241, 1);
	
	public static final double LEA_242 = getFuelRadiation(AMERICIUM_243, 8, AMERICIUM_242, 1);
	public static final double HEA_242 = getFuelRadiation(AMERICIUM_243, 5, AMERICIUM_242, 4);
	
	public static final double LECm_243 = getFuelRadiation(CURIUM_246, 8, CURIUM_243, 1);
	public static final double HECm_243 = getFuelRadiation(CURIUM_246, 5, CURIUM_243, 4);
	public static final double LECm_245 = getFuelRadiation(CURIUM_246, 8, CURIUM_245, 1);
	public static final double HECm_245 = getFuelRadiation(CURIUM_246, 5, CURIUM_245, 4);
	public static final double LECm_247 = getFuelRadiation(CURIUM_246, 8, CURIUM_247, 1);
	public static final double HECm_247 = getFuelRadiation(CURIUM_246, 5, CURIUM_247, 4);
	
	public static final double LEB_248 = getFuelRadiation(BERKELIUM_247, 8, BERKELIUM_248, 1);
	public static final double HEB_248 = getFuelRadiation(BERKELIUM_247, 5, BERKELIUM_248, 4);
	
	public static final double LECf_249 = getFuelRadiation(CALIFORNIUM_252, 8, CALIFORNIUM_249, 1);
	public static final double HECf_249 = getFuelRadiation(CALIFORNIUM_252, 5, CALIFORNIUM_249, 4);
	public static final double LECf_251 = getFuelRadiation(CALIFORNIUM_252, 8, CALIFORNIUM_251, 1);
	public static final double HECf_251 = getFuelRadiation(CALIFORNIUM_252, 5, CALIFORNIUM_251, 4);
}
