package nc.radiation;

import java.util.List;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.ints.Int2DoubleMap;
import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.init.NCBlocks;
import nc.init.NCItems;
import nc.util.OreDictHelper;
import nc.util.RegistryHelper;
import nc.util.StringHelper;
import net.minecraft.block.Block;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class RadSources {
	
	public static final ObjectSet<String> ORE_BLACKLIST = new ObjectOpenHashSet<>();
	public static final IntSet STACK_BLACKLIST = new IntOpenHashSet();
	public static final ObjectSet<String> FLUID_BLACKLIST = new ObjectOpenHashSet<>();
	
	public static final Object2DoubleMap<String> ORE_MAP = new Object2DoubleOpenHashMap<>();
	public static final Int2DoubleMap STACK_MAP = new Int2DoubleOpenHashMap();
	public static final Object2DoubleMap<String> FLUID_MAP = new Object2DoubleOpenHashMap<>();
	
	public static final Int2DoubleMap FOOD_RAD_MAP = new Int2DoubleOpenHashMap();
	public static final Int2DoubleMap FOOD_RESISTANCE_MAP = new Int2DoubleOpenHashMap();
	
	public static void addToOreMap(String ore, double radiation) {
		if (ORE_BLACKLIST.contains(ore)) return;
		if (StringHelper.isGlob(ore)) {
			OreDictHelper.putWildcard(ORE_MAP, ore, radiation);
		}
		else {
			ORE_MAP.put(ore, radiation);
		}
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
	
	public static final List<String> ORE_PREFIXES = Lists.newArrayList("ore");
	
	public static void init() {
		for (String ore : NCConfig.radiation_ores_blacklist) {
			if (StringHelper.isGlob(ore)) {
				OreDictHelper.addWildcard(ORE_BLACKLIST, ore);
			}
			else {
				ORE_BLACKLIST.add(ore);
			}
		}
		for (String item : NCConfig.radiation_items_blacklist) {
			ItemStack stack = RegistryHelper.itemStackFromRegistry(item);
			if (stack != null) STACK_BLACKLIST.add(RecipeItemHelper.pack(stack));
		}
		for (String block : NCConfig.radiation_blocks_blacklist) {
			ItemStack stack = RegistryHelper.blockStackFromRegistry(block);
			if (stack != null) STACK_BLACKLIST.add(RecipeItemHelper.pack(stack));
		}
		for (String fluid : NCConfig.radiation_fluids_blacklist) {
			FLUID_BLACKLIST.add(fluid);
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
		
		putMaterial(BISMUTH, "Bismuth");
		putMaterial(RADIUM, "Radium");
		putMaterial(POLONIUM, "Polonium");
		putMaterial(TBP, "TBP");
		putMaterial(PROTACTINIUM_233, "Protactinium233");
		
		putMaterial(THORIUM, "Thorium");
		putMaterial(URANIUM, "Uranium", "Yellorium");
		putMaterial(PLUTONIUM, "Plutonium", "Blutonium");
		putMaterial(URANIUM_238, "Cyanite");
		
		putIsotope(URANIUM_233, "Uranium233", "uranium_233");
		putIsotope(URANIUM_235, "Uranium235", "uranium_235");
		putIsotope(URANIUM_238, "Uranium238", "uranium_238");
		
		putIsotope(NEPTUNIUM_236, "Neptunium236", "neptunium_236");
		putIsotope(NEPTUNIUM_237, "Neptunium237", "neptunium_237");
		
		putIsotope(PLUTONIUM_238, "Plutonium238", "plutonium_238");
		putIsotope(PLUTONIUM_239, "Plutonium239", "plutonium_239");
		putIsotope(PLUTONIUM_241, "Plutonium241", "plutonium_241");
		putIsotope(PLUTONIUM_242, "Plutonium242", "plutonium_242");
		
		putIsotope(AMERICIUM_241, "Americium241", "americium_241");
		putIsotope(AMERICIUM_242, "Americium242", "americium_242");
		putIsotope(AMERICIUM_243, "Americium243", "americium_243");
		
		putIsotope(CURIUM_243, "Curium243", "curium_243");
		putIsotope(CURIUM_245, "Curium245", "curium_245");
		putIsotope(CURIUM_246, "Curium246", "curium_246");
		putIsotope(CURIUM_247, "Curium247", "curium_247");
		
		putIsotope(BERKELIUM_247, "Berkelium247", "berkelium_247");
		putIsotope(BERKELIUM_248, "Berkelium248", "berkelium_248");
		
		putIsotope(CALIFORNIUM_249, "Californium249", "californium_249");
		putIsotope(CALIFORNIUM_250, "Californium250", "californium_250");
		putIsotope(CALIFORNIUM_251, "Californium251", "californium_251");
		putIsotope(CALIFORNIUM_252, "Californium252", "californium_252");
		
		putFuel(TBU, DEPLETED_TBU, "TBU", "tbu");
		
		putFuel(LEU_233, DEPLETED_LEU_233, "LEU233", "leu_233");
		putFuel(HEU_233, DEPLETED_HEU_233, "HEU233", "heu_233");
		putFuel(LEU_235, DEPLETED_LEU_235, "LEU235", "leu_235");
		putFuel(HEU_235, DEPLETED_HEU_235, "HEU235", "heu_235");
		
		putFuel(LEN_236, DEPLETED_LEN_236, "LEN236", "len_236");
		putFuel(HEN_236, DEPLETED_HEN_236, "HEN236", "hen_236");
		
		putFuel(LEP_239, DEPLETED_LEP_239, "LEP239", "lep_239");
		putFuel(HEP_239, DEPLETED_HEP_239, "HEP239", "hep_239");
		putFuel(LEP_241, DEPLETED_LEP_241, "LEP241", "lep_241");
		putFuel(HEP_241, DEPLETED_HEP_241, "HEP241", "hep_241");
		
		putFuel(MIX_239, DEPLETED_MIX_239, "MIX239", "mix_239");
		putFuel(MIX_241, DEPLETED_MIX_241, "MIX241", "mix_241");
		
		putFuel(LEA_242, DEPLETED_LEA_242, "LEA242", "lea_242");
		putFuel(HEA_242, DEPLETED_HEA_242, "HEA242", "hea_242");
		
		putFuel(LECm_243, DEPLETED_LECm_243, "LECm243", "lecm_243");
		putFuel(HECm_243, DEPLETED_HECm_243, "HECm243", "hecm_243");
		putFuel(LECm_245, DEPLETED_LECm_245, "LECm245", "lecm_245");
		putFuel(HECm_245, DEPLETED_HECm_245, "HECm245", "hecm_245");
		putFuel(LECm_247, DEPLETED_LECm_247, "LECm247", "lecm_247");
		putFuel(HECm_247, DEPLETED_HECm_247, "HECm247", "hecm_247");
		
		putFuel(LEB_248, DEPLETED_LEB_248, "LEB248", "leb_248");
		putFuel(HEB_248, DEPLETED_HEB_248, "HEB248", "heb_248");
		
		putFuel(LECf_249, DEPLETED_LECf_249, "LECf249", "lecf_249");
		putFuel(HECf_249, DEPLETED_HECf_249, "HECf249", "hecf_249");
		putFuel(LECf_251, DEPLETED_LECf_251, "LECf251", "lecf_251");
		putFuel(HECf_251, DEPLETED_HECf_251, "HECf251", "hecf_251");
		
		put(URANIUM_238*4D, "plateDU");
		put(URANIUM_238*16D, NCBlocks.solar_panel_du, NCBlocks.voltaic_pile_du, NCBlocks.lithium_ion_battery_du);
		put(URANIUM_238*12D, new ItemStack(NCItems.rad_shielding, 1, 2));
		
		put(URANIUM_238/4D, NCBlocks.rtg_uranium);
		put(PLUTONIUM_238/4D, NCBlocks.rtg_plutonium);
		put(AMERICIUM_241/4D, NCBlocks.rtg_americium);
		put(CALIFORNIUM_250/4D, NCBlocks.rtg_californium);
		
		put(RADIUM/4D, new ItemStack(NCBlocks.fission_source, 1, 0));
		put(POLONIUM/4D, new ItemStack(NCBlocks.fission_source, 1, 1));
		put(CALIFORNIUM_252/4D, new ItemStack(NCBlocks.fission_source, 1, 2));
		
		put(TRITIUM/256D, NCBlocks.tritium_lamp);
		
		put(CAESIUM_137/4D, "dustIrradiatedBorax");
		
		if (ModCheck.gregtechLoaded()) {
			for (String prefix : ORE_PREFIXES) {
				put(17D*THORIUM/4D, prefix + "Monazite");
			}
		}
		
		putFluid(FUSION, "plasma");
		putFluid(TRITIUM, "tritium");
		
		putFluid(CAESIUM_137, "caesium_137");
		putFluid(CORIUM, "corium");
		
		putFluid(THORIUM, "thorium");
		putFluid(URANIUM, "uranium", "yellorium");
		putFluid(PLUTONIUM, "plutonium", "blutonium");
		putFluid(URANIUM_238, "cyanite");
		
		putFluid(CAESIUM_137/4D, "irradiated_borax_solution");
		
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
		ORE_MAP.entrySet().forEach(ent -> OreDictionary.getOres(ent.getKey(), false).forEach(s -> addToStackMap(s, ent.getValue())));
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
		for (String ore : ores) for (String suffix : new String[] {"", "Carbide", "Oxide", "Nitride", "ZA"}) {
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
	
	public static void putIsotope(double radiation, String ore, String fluid) {
		for (String suffix : new String[] {"", "Carbide", "Oxide", "Nitride", "ZA"}) {
			putMaterial(radiation, ore + suffix);
		}
		for (String suffix : new String[] {"", "_za", "_fluoride", "_fluoride_flibe"}) {
			addToFluidMap(fluid + suffix, radiation*FLUID);
		}
	}
	
	public static void putFuel(double fuelRadiation, double depletedRadiation, String ore, String fluid) {
		for (String suffix : new String[] {"", "TRISO", "Carbide", "Oxide", "Nitride", "ZA"}) {
			addToOreMap("ingot" + ore + suffix, fuelRadiation);
			addToOreMap("ingotDepleted" + ore + suffix, depletedRadiation);
		}
		for (String suffix : new String[] {"", "_za", "_fluoride", "_fluoride_flibe"}) {
			addToFluidMap(fluid + suffix, fuelRadiation*FLUID);
			addToFluidMap("depleted_" + fluid + suffix, depletedRadiation*FLUID);
		}
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
	
	public static void putFluid(double radiation, String... fluids) {
		for (String fluid : fluids) {
			addToFluidMap(fluid, radiation*FLUID);
		}
	}
	
	// Materials
	
	public static final double FUSION = 0.000000315D;
	public static final double NEUTRON = 0.00505D;
	public static final double TRITIUM = 0.0115D;
	
	public static final double CAESIUM_137 = 0.033D;
	public static final double CORIUM = 0.0000165D;
	
	public static final double BISMUTH = 4.975E-20D;
	public static final double RADIUM = 0.000625;
	public static final double POLONIUM = 2.64D/9D;
	
	public static final double THORIUM = 0.0000000000715D;
	public static final double URANIUM = 0.000000000385D;
	public static final double PLUTONIUM = 0.000042D;
	
	// Isotopes
	
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
	
	// Fuels
	
	public static double getFuelRadiation(double rad1, double amount1, double rad2, double amount2) {
		return (rad1*amount1 + rad2*amount2)*INGOT/9D;
	}
	
	public static double getDepletedFuelRadiation(double rad1, int amount1, double rad2, int amount2, double rad3, int amount3, double rad4, int amount4) {
		return (rad1*amount1 + rad2*amount2 + rad3*amount3 + rad4*amount4)*INGOT/9D;
	}
	
	public static final double TBU = getFuelRadiation(THORIUM, 8.5D, URANIUM_233, 0.5D);
	
	public static final double LEU_233 = getFuelRadiation(URANIUM_238, 8, URANIUM_233, 1);
	public static final double HEU_233 = getFuelRadiation(URANIUM_238, 6, URANIUM_233, 3);
	public static final double LEU_235 = getFuelRadiation(URANIUM_238, 8, URANIUM_235, 1);
	public static final double HEU_235 = getFuelRadiation(URANIUM_238, 6, URANIUM_235, 3);
	
	public static final double LEN_236 = getFuelRadiation(NEPTUNIUM_237, 8, NEPTUNIUM_236, 1);
	public static final double HEN_236 = getFuelRadiation(NEPTUNIUM_237, 6, NEPTUNIUM_236, 3);
	
	public static final double LEP_239 = getFuelRadiation(PLUTONIUM_242, 8, PLUTONIUM_239, 1);
	public static final double HEP_239 = getFuelRadiation(PLUTONIUM_242, 6, PLUTONIUM_239, 3);
	public static final double LEP_241 = getFuelRadiation(PLUTONIUM_242, 8, PLUTONIUM_241, 1);
	public static final double HEP_241 = getFuelRadiation(PLUTONIUM_242, 6, PLUTONIUM_241, 3);
	
	public static final double MIX_239 = getFuelRadiation(URANIUM_238, 8, PLUTONIUM_239, 1);
	public static final double MIX_241 = getFuelRadiation(URANIUM_238, 8, PLUTONIUM_241, 1);
	
	public static final double LEA_242 = getFuelRadiation(AMERICIUM_243, 8, AMERICIUM_242, 1);
	public static final double HEA_242 = getFuelRadiation(AMERICIUM_243, 6, AMERICIUM_242, 3);
	
	public static final double LECm_243 = getFuelRadiation(CURIUM_246, 8, CURIUM_243, 1);
	public static final double HECm_243 = getFuelRadiation(CURIUM_246, 6, CURIUM_243, 3);
	public static final double LECm_245 = getFuelRadiation(CURIUM_246, 8, CURIUM_245, 1);
	public static final double HECm_245 = getFuelRadiation(CURIUM_246, 6, CURIUM_245, 3);
	public static final double LECm_247 = getFuelRadiation(CURIUM_246, 8, CURIUM_247, 1);
	public static final double HECm_247 = getFuelRadiation(CURIUM_246, 6, CURIUM_247, 3);
	
	public static final double LEB_248 = getFuelRadiation(BERKELIUM_247, 8, BERKELIUM_248, 1);
	public static final double HEB_248 = getFuelRadiation(BERKELIUM_247, 6, BERKELIUM_248, 3);
	
	public static final double LECf_249 = getFuelRadiation(CALIFORNIUM_252, 8, CALIFORNIUM_249, 1);
	public static final double HECf_249 = getFuelRadiation(CALIFORNIUM_252, 6, CALIFORNIUM_249, 3);
	public static final double LECf_251 = getFuelRadiation(CALIFORNIUM_252, 8, CALIFORNIUM_251, 1);
	public static final double HECf_251 = getFuelRadiation(CALIFORNIUM_252, 6, CALIFORNIUM_251, 3);
	
	public static final double DEPLETED_TBU = getDepletedFuelRadiation(URANIUM_233, 1, URANIUM_238, 5, NEPTUNIUM_236, 1, NEPTUNIUM_237, 1);
	
	public static final double DEPLETED_LEU_233 = getDepletedFuelRadiation(URANIUM_238, 5, PLUTONIUM_241, 1, PLUTONIUM_242, 1, AMERICIUM_243, 1);
	public static final double DEPLETED_HEU_233 = getDepletedFuelRadiation(URANIUM_235, 1, URANIUM_238, 2, PLUTONIUM_242, 3, AMERICIUM_243, 1);
	public static final double DEPLETED_LEU_235 = getDepletedFuelRadiation(URANIUM_238, 4, PLUTONIUM_239, 1, PLUTONIUM_242, 2, AMERICIUM_243, 1);
	public static final double DEPLETED_HEU_235 = getDepletedFuelRadiation(URANIUM_238, 3, NEPTUNIUM_236, 1, PLUTONIUM_242, 2, AMERICIUM_243, 1);
	
	public static final double DEPLETED_LEN_236 = getDepletedFuelRadiation(URANIUM_238, 4, NEPTUNIUM_237, 1, PLUTONIUM_241, 1, PLUTONIUM_242, 2);
	public static final double DEPLETED_HEN_236 = getDepletedFuelRadiation(URANIUM_238, 4, PLUTONIUM_238, 1, PLUTONIUM_241, 1, PLUTONIUM_242, 1);
	
	public static final double DEPLETED_LEP_239 = getDepletedFuelRadiation(PLUTONIUM_242, 5, AMERICIUM_242, 1, AMERICIUM_243, 1, CURIUM_246, 1);
	public static final double DEPLETED_HEP_239 = getDepletedFuelRadiation(PLUTONIUM_241, 1, AMERICIUM_242, 1, AMERICIUM_243, 4, CURIUM_243, 1);
	public static final double DEPLETED_LEP_241 = getDepletedFuelRadiation(PLUTONIUM_242, 5, AMERICIUM_243, 1, CURIUM_246, 1, BERKELIUM_247, 1);
	public static final double DEPLETED_HEP_241 = getDepletedFuelRadiation(PLUTONIUM_241, 1, AMERICIUM_242, 1, AMERICIUM_243, 3, CURIUM_246, 2);
	
	public static final double DEPLETED_MIX_239 = getDepletedFuelRadiation(URANIUM_238, 4, PLUTONIUM_241, 1, PLUTONIUM_242, 2, AMERICIUM_243, 1);
	public static final double DEPLETED_MIX_241 = getDepletedFuelRadiation(URANIUM_238, 3, PLUTONIUM_241, 1, PLUTONIUM_242, 3, AMERICIUM_243, 1);
	
	public static final double DEPLETED_LEA_242 = getDepletedFuelRadiation(AMERICIUM_243, 3, CURIUM_245, 1, CURIUM_246, 3, BERKELIUM_248, 1);
	public static final double DEPLETED_HEA_242 = getDepletedFuelRadiation(AMERICIUM_243, 3, CURIUM_243, 1, CURIUM_246, 2, BERKELIUM_247, 1);
	
	public static final double DEPLETED_LECm_243 = getDepletedFuelRadiation(CURIUM_246, 4, CURIUM_247, 1, BERKELIUM_247, 2, BERKELIUM_248, 1);
	public static final double DEPLETED_HECm_243 = getDepletedFuelRadiation(CURIUM_245, 1, CURIUM_246, 3, BERKELIUM_247, 2, BERKELIUM_248, 1);
	public static final double DEPLETED_LECm_245 = getDepletedFuelRadiation(CURIUM_246, 4, CURIUM_247, 1, BERKELIUM_247, 2, CALIFORNIUM_249, 1);
	public static final double DEPLETED_HECm_245 = getDepletedFuelRadiation(CURIUM_246, 3, CURIUM_247, 1, BERKELIUM_247, 2, CALIFORNIUM_249, 1);
	public static final double DEPLETED_LECm_247 = getDepletedFuelRadiation(CURIUM_246, 5, BERKELIUM_247, 1, BERKELIUM_248, 1, CALIFORNIUM_249, 1);
	public static final double DEPLETED_HECm_247 = getDepletedFuelRadiation(BERKELIUM_247, 4, BERKELIUM_248, 1, CALIFORNIUM_249, 1, CALIFORNIUM_251, 1);
	
	public static final double DEPLETED_LEB_248 = getDepletedFuelRadiation(BERKELIUM_247, 5, BERKELIUM_248, 1, CALIFORNIUM_249, 1, CALIFORNIUM_251, 1);
	public static final double DEPLETED_HEB_248 = getDepletedFuelRadiation(BERKELIUM_248, 1, CALIFORNIUM_249, 1, CALIFORNIUM_251, 2, CALIFORNIUM_252, 3);
	
	public static final double DEPLETED_LECf_249 = getDepletedFuelRadiation(CALIFORNIUM_252, 2, CALIFORNIUM_252, 2, CALIFORNIUM_252, 2, CALIFORNIUM_252, 2);
	public static final double DEPLETED_HECf_249 = getDepletedFuelRadiation(CALIFORNIUM_250, 1, CALIFORNIUM_252, 2, CALIFORNIUM_252, 2, CALIFORNIUM_252, 2);
	public static final double DEPLETED_LECf_251 = getDepletedFuelRadiation(CALIFORNIUM_252, 2, CALIFORNIUM_252, 2, CALIFORNIUM_252, 2, CALIFORNIUM_252, 2);
	public static final double DEPLETED_HECf_251 = getDepletedFuelRadiation(CALIFORNIUM_252, 2, CALIFORNIUM_252, 2, CALIFORNIUM_252, 2, CALIFORNIUM_252, 1);
	
	public static final double TBU_FISSION = (TBU + DEPLETED_TBU + CAESIUM_137)/64D;
	
	public static final double LEU_233_FISSION = (LEU_233 + DEPLETED_LEU_233 + CAESIUM_137)/64D;
	public static final double HEU_233_FISSION = (HEU_233 + DEPLETED_HEU_233 + CAESIUM_137)/64D;
	public static final double LEU_235_FISSION = (LEU_235 + DEPLETED_LEU_235 + CAESIUM_137)/64D;
	public static final double HEU_235_FISSION = (HEU_235 + DEPLETED_HEU_235 + CAESIUM_137)/64D;
	
	public static final double LEN_236_FISSION = (LEN_236 + DEPLETED_LEN_236 + CAESIUM_137)/64D;
	public static final double HEN_236_FISSION = (HEN_236 + DEPLETED_HEN_236 + CAESIUM_137)/64D;
	
	public static final double LEP_239_FISSION = (LEP_239 + DEPLETED_LEP_239 + CAESIUM_137)/64D;
	public static final double HEP_239_FISSION = (HEP_239 + DEPLETED_HEP_239 + CAESIUM_137)/64D;
	public static final double LEP_241_FISSION = (LEP_241 + DEPLETED_LEP_241 + CAESIUM_137)/64D;
	public static final double HEP_241_FISSION = (HEP_241 + DEPLETED_HEP_241 + CAESIUM_137)/64D;
	
	public static final double MIX_239_FISSION = (MIX_239 + DEPLETED_MIX_239 + CAESIUM_137)/64D;
	public static final double MIX_241_FISSION = (MIX_241 + DEPLETED_MIX_241 + CAESIUM_137)/64D;
	
	public static final double LEA_242_FISSION = (LEA_242 + DEPLETED_LEA_242 + CAESIUM_137)/64D;
	public static final double HEA_242_FISSION = (HEA_242 + DEPLETED_HEA_242 + CAESIUM_137)/64D;
	
	public static final double LECm_243_FISSION = (LECm_243 + DEPLETED_LECm_243 + CAESIUM_137)/64D;
	public static final double HECm_243_FISSION = (HECm_243 + DEPLETED_HECm_243 + CAESIUM_137)/64D;
	public static final double LECm_245_FISSION = (LECm_245 + DEPLETED_LECm_245 + CAESIUM_137)/64D;
	public static final double HECm_245_FISSION = (HECm_245 + DEPLETED_HECm_245 + CAESIUM_137)/64D;
	public static final double LECm_247_FISSION = (LECm_247 + DEPLETED_LECm_247 + CAESIUM_137)/64D;
	public static final double HECm_247_FISSION = (HECm_247 + DEPLETED_HECm_247 + CAESIUM_137)/64D;
	
	public static final double LEB_248_FISSION = (LEB_248 + DEPLETED_LEB_248 + CAESIUM_137)/64D;
	public static final double HEB_248_FISSION = (HEB_248 + DEPLETED_HEB_248 + CAESIUM_137)/64D;
	
	public static final double LECf_249_FISSION = (LECf_249 + DEPLETED_LECf_249 + CAESIUM_137)/64D;
	public static final double HECf_249_FISSION = (HECf_249 + DEPLETED_HECf_249 + CAESIUM_137)/64D;
	public static final double LECf_251_FISSION = (LECf_251 + DEPLETED_LECf_251 + CAESIUM_137)/64D;
	public static final double HECf_251_FISSION = (HECf_251 + DEPLETED_HECf_251 + CAESIUM_137)/64D;
	
	public static final double PROTACTINIUM_233 = 13.54/9D;
	public static final double TBP = getFuelRadiation(THORIUM, 8.5D, PROTACTINIUM_233, 0.5D);
}
