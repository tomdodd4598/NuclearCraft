package nc.radiation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import nc.config.NCConfig;
import nc.init.NCBlocks;
import nc.util.RegistryHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RadSources {
	
	public static final Map<String, Double> ORE_MAP = new HashMap<String, Double>();
	public static final Map<ItemStack, Double> STACK_MAP = new HashMap<ItemStack, Double>();
	
	private static final double INGOT = 1D;
	private static final double NUGGET = 1D/9D;
	private static final double HALF = 1D/2D;
	private static final double THIRD = 1D/2D;
	private static final double SMALL = 1D/4D;
	private static final double GEAR = 4D;
	private static final double BLOCK = 9D;
	private static final double SLAB = 9D/2D;
	
	private static final List<String> MATERIAL_INGOT_NAME_LIST = Lists.newArrayList("ingot", "dust", "dustDirty", "clump", "shard", "crystal", "crushed", "crushedPurified", "plate", "blockSheetmetal");
	private static final List<String> MATERIAL_NUGGET_NAME_LIST = Lists.newArrayList("tinyDust", "dustTiny", "nugget");
	private static final List<String> MATERIAL_HALF_NAME_LIST = Lists.newArrayList("rod, slabSheetmetal");
	private static final List<String> MATERIAL_THIRD_NAME_LIST = Lists.newArrayList("coin");
	private static final List<String> MATERIAL_SMALL_NAME_LIST = Lists.newArrayList("smallDust", "dustSmall", "ore");
	private static final List<String> MATERIAL_GEAR_NAME_LIST = Lists.newArrayList("gear");
	private static final List<String> MATERIAL_BLOCK_NAME_LIST = Lists.newArrayList("block", "plateDense");
	private static final List<String> MATERIAL_SLAB_NAME_LIST = Lists.newArrayList("slab");
	
	private static final List<String> ISOTOPE_INGOT_NAME_LIST = Lists.newArrayList("ingot");
	private static final List<String> ISOTOPE_NUGGET_NAME_LIST = Lists.newArrayList("nugget");
	private static final List<String> ISOTOPE_BLOCK_NAME_LIST = Lists.newArrayList("block");
	
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
	
	static {
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
		putIsotope(PLUTONIUM_239, "Plutonium239");
		putIsotope(PLUTONIUM_241, "Plutonium241");
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
		
		putFuel(THORIUM_232, 9, THORIUM_230, 0, "TBU");
		
		putFuel(URANIUM_238, URANIUM_233, "U233");
		putFuel(URANIUM_238, URANIUM_235, "U235");
		
		putFuel(NEPTUNIUM_237, NEPTUNIUM_236, "N236");
		
		putFuel(PLUTONIUM_242, PLUTONIUM_239, "P239");
		putFuel(PLUTONIUM_242, PLUTONIUM_241, "P241");
		
		putFuel(URANIUM_238, 8, PLUTONIUM_239, 1, "MOX239");
		putFuel(URANIUM_238, 8, PLUTONIUM_241, 1, "MOX241");
		
		putFuel(AMERICIUM_243, AMERICIUM_242, "A242");
		
		putFuel(CURIUM_246, CURIUM_243, "Cm243");
		putFuel(CURIUM_246, CURIUM_245, "Cm245");
		putFuel(CURIUM_246, CURIUM_247, "Cm247");
		
		putFuel(BERKELIUM_247, BERKELIUM_248, "B248");
		
		putFuel(CALIFORNIUM_252, CALIFORNIUM_249, "Cf249");
		putFuel(CALIFORNIUM_252, CALIFORNIUM_251, "Cf251");
		
		putDepletedFuel(URANIUM_233, 16, URANIUM_235, 8, NEPTUNIUM_236, 8, NEPTUNIUM_237, 32, "TBU");
		
		putDepletedFuel(URANIUM_238, 40, NEPTUNIUM_237, 8, PLUTONIUM_239, 8, PLUTONIUM_241, 8, "LEU235");
		putDepletedFuel(URANIUM_238, 20, NEPTUNIUM_237, 16, PLUTONIUM_239, 4, PLUTONIUM_242, 24, "HEU235");
		
		putDepletedFuel(PLUTONIUM_239, 4, PLUTONIUM_241, 4, PLUTONIUM_242, 32, AMERICIUM_243, 24, "LEU233");
		putDepletedFuel(NEPTUNIUM_236, 32, NEPTUNIUM_237, 8, PLUTONIUM_242, 16, AMERICIUM_243, 8, "HEU233");
		
		putDepletedFuel(NEPTUNIUM_237, 4, PLUTONIUM_242, 32, AMERICIUM_242, 8, AMERICIUM_243, 20, "LEN236");
		putDepletedFuel(URANIUM_238, 16, PLUTONIUM_238, 8, PLUTONIUM_239, 8, PLUTONIUM_242, 32, "HEN236");
		
		putDepletedFuel(URANIUM_238, 40, PLUTONIUM_242, 12, AMERICIUM_243, 8, CURIUM_243, 4, "MOX239");
		
		putDepletedFuel(PLUTONIUM_239, 8, PLUTONIUM_242, 24, CURIUM_243, 4, CURIUM_246, 28, "LEP239");
		putDepletedFuel(AMERICIUM_241, 8, AMERICIUM_242, 24, CURIUM_245, 8, CURIUM_246, 24, "HEP239");
		
		putDepletedFuel(PLUTONIUM_242, 4, AMERICIUM_242, 4, AMERICIUM_243, 8, CURIUM_246, 48, "LEP241");
		putDepletedFuel(AMERICIUM_241, 8, CURIUM_245, 8, CURIUM_246, 24, CURIUM_247, 24, "HEP241");
		
		putDepletedFuel(URANIUM_238, 40, PLUTONIUM_241, 8, PLUTONIUM_242, 8, CURIUM_246, 8, "MOX241");
		
		putDepletedFuel(CURIUM_243, 8, CURIUM_245, 8, CURIUM_246, 40, CURIUM_247, 8, "LEA242");
		putDepletedFuel(CURIUM_245, 16, CURIUM_246, 32, CURIUM_247, 8, BERKELIUM_247, 8, "HEA242");
		
		putDepletedFuel(CURIUM_246, 32, BERKELIUM_247, 16, BERKELIUM_248, 8, CALIFORNIUM_249, 8, "LECm243");
		putDepletedFuel(CURIUM_246, 24, BERKELIUM_247, 24, BERKELIUM_248, 8, CALIFORNIUM_249, 8, "HECm243");
		
		putDepletedFuel(BERKELIUM_247, 40, BERKELIUM_248, 8, CALIFORNIUM_249, 4, CALIFORNIUM_252, 12, "LECm245");
		putDepletedFuel(BERKELIUM_247, 48, BERKELIUM_248, 4, CALIFORNIUM_249, 4, CALIFORNIUM_251, 8, "HECm245");
		
		putDepletedFuel(BERKELIUM_247, 20, BERKELIUM_248, 4, CALIFORNIUM_251, 8, CALIFORNIUM_252, 32, "LECm247");
		putDepletedFuel(BERKELIUM_248, 8, CALIFORNIUM_249, 8, CALIFORNIUM_251, 24, CALIFORNIUM_252, 24, "HECm247");
		
		putDepletedFuel(CALIFORNIUM_249, 4, CALIFORNIUM_251, 4, CALIFORNIUM_252, 28, CALIFORNIUM_252, 28, "LEB248");
		putDepletedFuel(CALIFORNIUM_250, 8, CALIFORNIUM_251, 8, CALIFORNIUM_252, 24, CALIFORNIUM_252, 24, "HEB248");
		
		putDepletedFuel(CALIFORNIUM_250, 16, CALIFORNIUM_251, 8, CALIFORNIUM_252, 20, CALIFORNIUM_252, 20, "LECf249");
		putDepletedFuel(CALIFORNIUM_250, 32, CALIFORNIUM_251, 16, CALIFORNIUM_252, 8, CALIFORNIUM_252, 8, "HECf249");
		
		putDepletedFuel(CALIFORNIUM_251, 4, CALIFORNIUM_252, 20, CALIFORNIUM_252, 20, CALIFORNIUM_252, 20, "LECf251");
		putDepletedFuel(CALIFORNIUM_251, 16, CALIFORNIUM_252, 16, CALIFORNIUM_252, 16, CALIFORNIUM_252, 16, "HECf251");
		
		put(URANIUM_238*4, "plateDU");
	}
	
	private static void putMaterial(double radiation, String... ores) {
		for (String ore : ores) for (String suffix : new String[] {"", "Oxide"}) {
			for (String prefix : MATERIAL_INGOT_NAME_LIST) ORE_MAP.put(prefix + ore + suffix, radiation*INGOT);
			for (String prefix : MATERIAL_NUGGET_NAME_LIST) ORE_MAP.put(prefix + ore + suffix, radiation*NUGGET);
			for (String prefix : MATERIAL_HALF_NAME_LIST) ORE_MAP.put(prefix + ore + suffix, radiation*HALF);
			for (String prefix : MATERIAL_THIRD_NAME_LIST) ORE_MAP.put(prefix + ore + suffix, radiation*THIRD);
			for (String prefix : MATERIAL_SMALL_NAME_LIST) ORE_MAP.put(prefix + ore + suffix, radiation*SMALL);
			for (String prefix : MATERIAL_GEAR_NAME_LIST) ORE_MAP.put(prefix + ore + suffix, radiation*GEAR);
			for (String prefix : MATERIAL_BLOCK_NAME_LIST) ORE_MAP.put(prefix + ore + suffix, radiation*BLOCK);
			for (String prefix : MATERIAL_SLAB_NAME_LIST) ORE_MAP.put(prefix + ore + suffix, radiation*SLAB);
		}
	}
	
	private static void putIsotope(double radiation, String... ores) {
		for (String ore : ores) for (String suffix : new String[] {"", "Base", "Oxide"}) {
			for (String prefix : ISOTOPE_INGOT_NAME_LIST) ORE_MAP.put(prefix + ore + suffix, radiation*INGOT);
			for (String prefix : ISOTOPE_NUGGET_NAME_LIST) ORE_MAP.put(prefix + ore + suffix, radiation*NUGGET);
			for (String prefix : ISOTOPE_BLOCK_NAME_LIST) ORE_MAP.put(prefix + ore + suffix, radiation*BLOCK);
		}
	}
	
	public static double getFuelRadiation(double rad1, int amount1, double rad2, int amount2) {
		return (rad1*amount1 + rad2*amount2)*INGOT;
	}
	
	private static void putFuel(double rad1, int amount1, double rad2, int amount2, String ore) {
		double radiation = getFuelRadiation(rad1, amount1, rad2, amount2);
		ORE_MAP.put("fuel" + ore, radiation);
		ORE_MAP.put("fuel" + ore + "Oxide", radiation);
		ORE_MAP.put("fuelRod" + ore, radiation);
		ORE_MAP.put("fuelRod" + ore + "Oxide", radiation);
	}
	
	private static void putFuel(double fertile, double fissile, String ore) {
		putFuel(fertile, 8, fissile, 1, "LE" + ore);
		putFuel(fertile, 5, fissile, 4, "HE" + ore);
	}
	
	public static double getDepletedFuelRadiation(double rad1, int amount1, double rad2, int amount2, double rad3, int amount3, double rad4, int amount4) {
		return (rad1*amount1 + rad2*amount2 + rad3*amount3 + rad4*amount4)*NUGGET;
	}
	
	private static void putDepletedFuel(double rad1, int amount1, double rad2, int amount2, double rad3, int amount3, double rad4, int amount4, String ore) {
		double radiation = getDepletedFuelRadiation(rad1, amount1, rad2, amount2, rad3, amount3, rad4, amount4);
		ORE_MAP.put("depletedFuel" + ore, radiation);
		ORE_MAP.put("depletedFuel" + ore + "Oxide", radiation);
		ORE_MAP.put("depletedFuelRod" + ore, radiation);
		ORE_MAP.put("depletedFuelRod" + ore + "Oxide", radiation);
	}
	
	private static void put(double radiation, String... ores) {
		for (String ore : ores) ORE_MAP.put(ore, radiation);
	}
	
	private static void put(double radiation, ItemStack... stacks) {
		for (ItemStack stack : stacks) STACK_MAP.put(stack, radiation);
	}
	
	private static void put(double radiation, Item... items) {
		for (Item item : items) STACK_MAP.put(new ItemStack(item), radiation);
	}
	
	private static void put(double radiation, Block... blocks) {
		for (Block block : blocks) STACK_MAP.put(new ItemStack(block), radiation);
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
	
	// Custom and Stack Entries
	
	public static void init() {
		for (String oreInfo : NCConfig.radiation_ores) {
			int scorePos = oreInfo.lastIndexOf('_');
			if (scorePos == -1) continue;
			ORE_MAP.put(oreInfo.substring(0, scorePos), Double.parseDouble(oreInfo.substring(scorePos + 1)));
		}
		for (String itemInfo : NCConfig.radiation_items) {
			int scorePos = itemInfo.lastIndexOf('_');
			if (scorePos == -1) continue;
			ItemStack stack = RegistryHelper.itemStackFromRegistry(itemInfo.substring(0, scorePos));
			if (stack != null) STACK_MAP.put(stack, Double.parseDouble(itemInfo.substring(scorePos + 1)));
		}
		for (String blockInfo : NCConfig.radiation_blocks) {
			int scorePos = blockInfo.lastIndexOf('_');
			if (scorePos == -1) continue;
			ItemStack stack = RegistryHelper.blockStackFromRegistry(blockInfo.substring(0, scorePos));
			if (stack != null) STACK_MAP.put(stack, Double.parseDouble(blockInfo.substring(scorePos + 1)));
		}
		
		put(RadSources.URANIUM_238/4D, NCBlocks.rtg_uranium);
		put(RadSources.PLUTONIUM_238/4D, NCBlocks.rtg_plutonium);
		put(RadSources.AMERICIUM_241/4D, NCBlocks.rtg_americium);
		put(RadSources.CALIFORNIUM_250/4D, NCBlocks.rtg_californium);
		
		put(RadSources.THORIUM_230*9D/4D, NCBlocks.helium_collector);
		put(RadSources.THORIUM_230*8D*9D/4D, NCBlocks.helium_collector_compact);
		put(RadSources.THORIUM_230*64D*9D/4D, NCBlocks.helium_collector_dense);
	}
}
