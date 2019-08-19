package nc.enumm;

import nc.config.NCConfig;
import net.minecraft.util.IStringSerializable;

public class MetaEnums {
	
	public static enum OreType implements IStringSerializable, IBlockMeta {
		COPPER("copper", 0, NCConfig.ore_harvest_levels[0], "pickaxe", 3, 15, 0),
		TIN("tin", 1, NCConfig.ore_harvest_levels[1], "pickaxe", 3, 15, 0),
		LEAD("lead", 2, NCConfig.ore_harvest_levels[2], "pickaxe", 3, 15, 0),
		THORIUM("thorium", 3, NCConfig.ore_harvest_levels[3], "pickaxe", 3, 15, 0),
		URANIUM("uranium", 4, NCConfig.ore_harvest_levels[4], "pickaxe", 3, 15, 0),
		BORON("boron", 5, NCConfig.ore_harvest_levels[5], "pickaxe", 3, 15, 0),
		LITHIUM("lithium", 6, NCConfig.ore_harvest_levels[6], "pickaxe", 3, 15, 0),
		MAGNESIUM("magnesium", 7, NCConfig.ore_harvest_levels[7], "pickaxe", 3, 15, 0);
		
		private String name;
		private int id;
		private int harvestLevel;
		private String harvestTool;
		private float hardness;
		private float resistance;
		private int lightValue;
		
		private OreType(String name, int id, int harvestLevel, String harvestTool, float hardness, float resistance, int lightValue) {
			this.name = name;
			this.id = id;
			this.harvestLevel = harvestLevel;
			this.harvestTool = harvestTool;
			this.hardness = hardness;
			this.resistance = resistance;
			this.lightValue = lightValue;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
		
		@Override
		public int getHarvestLevel() {
			return harvestLevel;
		}
		
		@Override
		public String getHarvestTool() {
			return harvestTool;
		}
		
		@Override
		public float getHardness() {
			return hardness;
		}
		
		@Override
		public float getResistance() {
			return resistance;
		}
		
		@Override
		public int getLightValue() {
			return lightValue;
		}
	}
	
	public static enum FissionBlockType implements IStringSerializable, IBlockMeta {
		CASING("casing", 0, 0, "pickaxe", 2, 15, 0),
		BLAST("blast", 1, 0, "pickaxe", 3, 3000, 0);
		
		private String name;
		private int id;
		private int harvestLevel;
		private String harvestTool;
		private float hardness;
		private float resistance;
		private int lightValue;
		
		private FissionBlockType(String name, int id, int harvestLevel, String harvestTool, float hardness, float resistance, int lightValue) {
			this.name = name;
			this.id = id;
			this.harvestLevel = harvestLevel;
			this.harvestTool = harvestTool;
			this.hardness = hardness;
			this.resistance = resistance;
			this.lightValue = lightValue;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
		
		@Override
		public int getHarvestLevel() {
			return harvestLevel;
		}
		
		@Override
		public String getHarvestTool() {
			return harvestTool;
		}
		
		@Override
		public float getHardness() {
			return hardness;
		}
		
		@Override
		public float getResistance() {
			return resistance;
		}
		
		@Override
		public int getLightValue() {
			return lightValue;
		}
	}
	
	public static enum CoolerType implements IStringSerializable, IBlockMeta {
		EMPTY("empty", 0, 0, "_", 0, "pickaxe", 2, 15, 0),
		WATER("water", 1, NCConfig.fission_cooling_rate[0], "water", 0, "pickaxe", 2, 15, 0),
		REDSTONE("redstone", 2, NCConfig.fission_cooling_rate[1], "redstone", 0, "pickaxe", 2, 15, 7),
		QUARTZ("quartz", 3, NCConfig.fission_cooling_rate[2], "quartz", 0, "pickaxe", 2, 15, 0),
		GOLD("gold", 4, NCConfig.fission_cooling_rate[3], "gold", 0, "pickaxe", 2, 15, 0),
		GLOWSTONE("glowstone", 5, NCConfig.fission_cooling_rate[4], "glowstone", 0, "pickaxe", 2, 15, 15),
		LAPIS("lapis", 6, NCConfig.fission_cooling_rate[5], "lapis", 0, "pickaxe", 2, 15, 0),
		DIAMOND("diamond", 7, NCConfig.fission_cooling_rate[6], "diamond", 0, "pickaxe", 2, 15, 0),
		HELIUM("helium", 8, NCConfig.fission_cooling_rate[7], "liquidhelium", 0, "pickaxe", 2, 15, 0),
		ENDERIUM("enderium", 9, NCConfig.fission_cooling_rate[8], "ender", 0, "pickaxe", 2, 15, 0),
		CRYOTHEUM("cryotheum", 10, NCConfig.fission_cooling_rate[9], "cryotheum", 0, "pickaxe", 2, 15, 0),
		IRON("iron", 11, NCConfig.fission_cooling_rate[10], "iron", 0, "pickaxe", 2, 15, 0),
		EMERALD("emerald", 12, NCConfig.fission_cooling_rate[11], "emerald", 0, "pickaxe", 2, 15, 0),
		COPPER("copper", 13, NCConfig.fission_cooling_rate[12], "copper", 0, "pickaxe", 2, 15, 0),
		TIN("tin", 14, NCConfig.fission_cooling_rate[13], "tin", 0, "pickaxe", 2, 15, 0),
		MAGNESIUM("magnesium", 15, NCConfig.fission_cooling_rate[14], "magnesium", 0, "pickaxe", 2, 15, 0);
		
		private String name;
		private int id;
		private double coolingRate;
		private String fluidName;
		private int harvestLevel;
		private String harvestTool;
		private float hardness;
		private float resistance;
		private int lightValue;
		
		private CoolerType(String name, int id, double coolingRate, String fluidName, int harvestLevel, String harvestTool, float hardness, float resistance, int lightValue) {
			this.name = name;
			this.id = id;
			this.coolingRate = coolingRate;
			this.fluidName = fluidName;
			this.harvestLevel = harvestLevel;
			this.harvestTool = harvestTool;
			this.hardness = hardness;
			this.resistance = resistance;
			this.lightValue = lightValue;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
		
		public double getCooling() {
			return coolingRate;
		}
		
		public String getFluidName() {
			return fluidName;
		}
		
		@Override
		public int getHarvestLevel() {
			return harvestLevel;
		}
		
		@Override
		public String getHarvestTool() {
			return harvestTool;
		}
		
		@Override
		public float getHardness() {
			return hardness;
		}
		
		@Override
		public float getResistance() {
			return resistance;
		}
		
		@Override
		public int getLightValue() {
			return lightValue;
		}
	}
	
	public static enum IngotType implements IStringSerializable, IBlockMeta, IItemMeta {
		COPPER("copper", 0, 0, "pickaxe", 4, 30, 0, 0, 0, false),
		TIN("tin", 1, 0, "pickaxe", 4, 30, 0, 0, 0, false),
		LEAD("lead", 2, 0, "pickaxe", 4, 30, 0, 0, 0, false),
		THORIUM("thorium", 3, 0, "pickaxe", 4, 30, 0, 0, 0, false),
		URANIUM("uranium", 4, 0, "pickaxe", 4, 30, 0, 0, 0, false),
		BORON("boron", 5, 0, "pickaxe", 4, 30, 0, 0, 0, false),
		LITHIUM("lithium", 6, 0, "pickaxe", 4, 30, 0, 5, 5, false),
		MAGNESIUM("magnesium", 7, 0, "pickaxe", 4, 30, 0, 5, 5, false),
		GRAPHITE("graphite", 8, 0, "pickaxe", 4, 30, 0, 5, 5, true),
		BERYLLIUM("beryllium", 9, 0, "pickaxe", 4, 30, 0, 0, 0, false),
		ZIRCONIUM("zirconium", 10, 0, "pickaxe", 4, 30, 0, 0, 0, false),
		MANGANESE("manganese", 11, 0, "pickaxe", 4, 30, 0, 0, 0, false),
		ALUMINUM("aluminum", 12, 0, "pickaxe", 4, 30, 0, 0, 0, false),
		SILVER("silver", 13, 0, "pickaxe", 4, 30, 0, 0, 0, false);
		
		private String name;
		private int id;
		private int harvestLevel;
		private String harvestTool;
		private float hardness;
		private float resistance;
		private int lightValue;
		private int fireSpreadSpeed;
		private int flammability;
		private boolean isFireSource;
		
		private IngotType(String name, int id, int harvestLevel, String harvestTool, float hardness, float resistance, int lightValue, int fireSpreadSpeed, int flammability, boolean isFireSource) {
			this.name = name;
			this.id = id;
			this.harvestLevel = harvestLevel;
			this.harvestTool = harvestTool;
			this.hardness = hardness;
			this.resistance = resistance;
			this.lightValue = lightValue;
			this.fireSpreadSpeed = fireSpreadSpeed;
			this.flammability = flammability;
			this.isFireSource = isFireSource;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
		
		@Override
		public int getHarvestLevel() {
			return harvestLevel;
		}
		
		@Override
		public String getHarvestTool() {
			return harvestTool;
		}
		
		@Override
		public float getHardness() {
			return hardness;
		}
		
		@Override
		public float getResistance() {
			return resistance;
		}
		
		@Override
		public int getLightValue() {
			return lightValue;
		}
		
		public int getFireSpreadSpeed() {
			return fireSpreadSpeed;
		}
		
		public int getFlammability() {
			return flammability;
		}
		
		public boolean isFireSource() {
			return isFireSource;
		}
	}
	
	public static enum IngotOxideType implements IStringSerializable, IItemMeta {
		THORIUM("thorium", 0),
		URANIUM("uranium", 1),
		MANGANESE("manganese", 2),
		MANGANESE2("manganese2", 3);
		
		private String name;
		private int id;
		
		private IngotOxideType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
	}
	
	public static enum DustType implements IStringSerializable, IItemMeta {
		COPPER("copper", 0),
		TIN("tin", 1),
		LEAD("lead", 2),
		THORIUM("thorium", 3),
		URANIUM("uranium", 4),
		BORON("boron", 5),
		LITHIUM("lithium", 6),
		MAGNESIUM("magnesium", 7),
		GRAPHITE("graphite", 8),
		BERYLLIUM("beryllium", 9),
		ZIRCONIUM("zirconium", 10),
		MANGANESE("manganese", 11),
		ALUMINUM("aluminum", 12),
		SILVER("silver", 13);
		
		private String name;
		private int id;
		
		private DustType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
	}
	
	public static enum DustOxideType implements IStringSerializable, IItemMeta {
		THORIUM("thorium", 0),
		URANIUM("uranium", 1),
		MANGANESE("manganese", 2),
		MANGANESE2("manganese2", 3);
		
		private String name;
		private int id;
		
		private DustOxideType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
	}
	
	public static enum GemType implements IStringSerializable, IItemMeta {
		RHODOCHROSITE("rhodochrosite", 0),
		BORON_NITRIDE("boron_nitride", 1),
		FLUORITE("fluorite", 2),
		VILLIAUMITE("villiaumite", 3),
		CAROBBIITE("carobbiite", 4),
		BORON_ARSENIDE("boron_arsenide", 5),
		SILICON("silicon", 6);
		
		private String name;
		private int id;
		
		private GemType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
	}
	
	public static enum GemDustType implements IStringSerializable, IItemMeta {
		DIAMOND("diamond", 0),
		RHODOCHROSITE("rhodochrosite", 1),
		QUARTZ("quartz", 2),
		OBSIDIAN("obsidian", 3),
		BORON_NITRIDE("boron_nitride", 4),
		FLUORITE("fluorite", 5),
		SULFUR("sulfur", 6),
		COAL("coal", 7),
		VILLIAUMITE("villiaumite", 8),
		CAROBBIITE("carobbiite", 9),
		ARSENIC("arsenic", 10),
		END_STONE("end_stone", 11);
		
		private String name;
		private int id;
		
		private GemDustType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
	}
	
	public static enum AlloyType implements IStringSerializable, IItemMeta {
		BRONZE("bronze", 0),
		TOUGH("tough", 1),
		HARD_CARBON("hard_carbon", 2),
		MAGNESIUM_DIBORIDE("magnesium_diboride", 3),
		LITHIUM_MANGANESE_DIOXIDE("lithium_manganese_dioxide", 4),
		STEEL("steel", 5),
		FERROBORON("ferroboron", 6),
		SHIBUICHI("shibuichi", 7),
		TIN_SILVER("tin_silver", 8),
		LEAD_PLATINUM("lead_platinum", 9),
		EXTREME("extreme", 10),
		THERMOCONDUCTING("thermoconducting", 11),
		ZIRCALOY("zircaloy", 12),
		SILICON_CARBIDE("silicon_carbide", 13),
		SIC_SIC_CMC("sic_sic_cmc", 14),
		HSLA_STEEL("hsla_steel", 15);
		
		private String name;
		private int id;
		
		private AlloyType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
	}
	
	public static enum CompoundType implements IStringSerializable, IItemMeta {
		CALCIUM_SULFATE("calcium_sulfate", 0),
		CRYSTAL_BINDER("crystal_binder", 1),
		ENERGETIC_BLEND("energetic_blend", 2),
		SODIUM_FLUORIDE("sodium_fluoride", 3),
		POTASSIUM_FLUORIDE("potassium_fluoride", 4),
		SODIUM_HYDROXIDE("sodium_hydroxide", 5),
		POTASSIUM_HYDROXIDE("potassium_hydroxide", 6),
		BORAX("borax", 7),
		DIMENSIONAL_BLEND("dimensional_blend", 8),
		C_MN_BLEND("c_mn_blend", 9),
		ALUGENTUM("alugentum", 10);
		
		private String name;
		private int id;
		
		private CompoundType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
	}
	
	public static enum PartType implements IStringSerializable, IItemMeta {
		PLATE_BASIC("plate_basic", 0),
		PLATE_ADVANCED("plate_advanced", 1),
		PLATE_DU("plate_du", 2),
		PLATE_ELITE("plate_elite", 3),
		WIRE_COPPER("wire_copper", 4),
		WIRE_MAGNESIUM_DIBORIDE("wire_magnesium_diboride", 5),
		BIOPLASTIC("bioplastic", 6),
		SERVO("servo", 7),
		MOTOR("motor", 8),
		ACTUATOR("actuator", 9),
		CHASSIS("chassis", 10),
		EMPTY_FRAME("empty_frame", 11),
		STEEL_FRAME("steel_frame", 12),
		SIC_FIBER("sic_fiber", 13);
		
		private String name;
		private int id;
		
		private PartType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
	}
	
	public static enum UpgradeType implements IStringSerializable, IItemMeta {
		SPEED("speed", 0),
		ENERGY("energy", 1);
		
		private String name;
		private int id;
		
		private UpgradeType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
	}
	
	public static enum ThoriumType implements IStringSerializable, IItemMeta {
		_230("_230", 0),
		_230_OXIDE("_230_oxide", 1),
		_230_TINY("_230_tiny", 2),
		_230_OXIDE_TINY("_230_oxide_tiny", 3),
		_232("_232", 4),
		_232_OXIDE("_232_oxide", 5),
		_232_TINY("_232_tiny", 6),
		_232_OXIDE_TINY("_232_oxide_tiny", 7);
		
		private String name;
		private int id;
		
		private ThoriumType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
	}
	
	public static enum UraniumType implements IStringSerializable, IItemMeta {
		_233("_233", 0),
		_233_OXIDE("_233_oxide", 1),
		_233_TINY("_233_tiny", 2),
		_233_OXIDE_TINY("_233_oxide_tiny", 3),
		_235("_235", 4),
		_235_OXIDE("_235_oxide", 5),
		_235_TINY("_235_tiny", 6),
		_235_OXIDE_TINY("_235_oxide_tiny", 7),
		_238("_238", 8),
		_238_OXIDE("_238_oxide", 9),
		_238_TINY("_238_tiny", 10),
		_238_OXIDE_TINY("_238_oxide_tiny", 11);
		
		private String name;
		private int id;
		
		private UraniumType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
	}
	
	public static enum NeptuniumType implements IStringSerializable, IItemMeta {
		_236("_236", 0),
		_236_OXIDE("_236_oxide", 1),
		_236_TINY("_236_tiny", 2),
		_236_OXIDE_TINY("_236_oxide_tiny", 3),
		_237("_237", 4),
		_237_OXIDE("_237_oxide", 5),
		_237_TINY("_237_tiny", 6),
		_237_OXIDE_TINY("_237_oxide_tiny", 7);
		
		private String name;
		private int id;
		
		private NeptuniumType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
	}
	
	public static enum PlutoniumType implements IStringSerializable, IItemMeta {
		_238("_238", 0),
		_238_OXIDE("_238_oxide", 1),
		_238_TINY("_238_tiny", 2),
		_238_OXIDE_TINY("_238_oxide_tiny", 3),
		_239("_239", 4),
		_239_OXIDE("_239_oxide", 5),
		_239_TINY("_239_tiny", 6),
		_239_OXIDE_TINY("_239_oxide_tiny", 7),
		_241("_241", 8),
		_241_OXIDE("_241_oxide", 9),
		_241_TINY("_241_tiny", 10),
		_241_OXIDE_TINY("_241_oxide_tiny", 11),
		_242("_242", 12),
		_242_OXIDE("_242_oxide", 13),
		_242_TINY("_242_tiny", 14),
		_242_OXIDE_TINY("_242_oxide_tiny", 15);
		
		private String name;
		private int id;
		
		private PlutoniumType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
	}
	
	public static enum AmericiumType implements IStringSerializable, IItemMeta {
		_241("_241", 0),
		_241_OXIDE("_241_oxide", 1),
		_241_TINY("_241_tiny", 2),
		_241_OXIDE_TINY("_241_oxide_tiny", 3),
		_242("_242", 4),
		_242_OXIDE("_242_oxide", 5),
		_242_TINY("_242_tiny", 6),
		_242_OXIDE_TINY("_242_oxide_tiny", 7),
		_243("_243", 8),
		_243_OXIDE("_243_oxide", 9),
		_243_TINY("_243_tiny", 10),
		_243_OXIDE_TINY("_243_oxide_tiny", 11);
		
		private String name;
		private int id;
		
		private AmericiumType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
	}
	
	public static enum CuriumType implements IStringSerializable, IItemMeta {
		_243("_243", 0),
		_243_OXIDE("_243_oxide", 1),
		_243_TINY("_243_tiny", 2),
		_243_OXIDE_TINY("_243_oxide_tiny", 3),
		_245("_245", 4),
		_245_OXIDE("_245_oxide", 5),
		_245_TINY("_245_tiny", 6),
		_245_OXIDE_TINY("_245_oxide_tiny", 7),
		_246("_246", 8),
		_246_OXIDE("_246_oxide", 9),
		_246_TINY("_246_tiny", 10),
		_246_OXIDE_TINY("_246_oxide_tiny", 11),
		_247("_247", 12),
		_247_OXIDE("_247_oxide", 13),
		_247_TINY("_247_tiny", 14),
		_247_OXIDE_TINY("_247_oxide_tiny", 15);
		
		private String name;
		private int id;
		
		private CuriumType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
	}
	
	public static enum BerkeliumType implements IStringSerializable, IItemMeta {
		_247("_247", 0),
		_247_OXIDE("_247_oxide", 1),
		_247_TINY("_247_tiny", 2),
		_247_OXIDE_TINY("_247_oxide_tiny", 3),
		_248("_248", 4),
		_248_OXIDE("_248_oxide", 5),
		_248_TINY("_248_tiny", 6),
		_248_OXIDE_TINY("_248_oxide_tiny", 7);
		
		private String name;
		private int id;
		
		private BerkeliumType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
	}
	
	public static enum CaliforniumType implements IStringSerializable, IItemMeta {
		_249("_249", 0),
		_249_OXIDE("_249_oxide", 1),
		_249_TINY("_249_tiny", 2),
		_249_OXIDE_TINY("_249_oxide_tiny", 3),
		_250("_250", 4),
		_250_OXIDE("_250_oxide", 5),
		_250_TINY("_250_tiny", 6),
		_250_OXIDE_TINY("_250_oxide_tiny", 7),
		_251("_251", 8),
		_251_OXIDE("_251_oxide", 9),
		_251_TINY("_251_tiny", 10),
		_251_OXIDE_TINY("_251_oxide_tiny", 11),
		_252("_252", 12),
		_252_OXIDE("_252_oxide", 13),
		_252_TINY("_252_tiny", 14),
		_252_OXIDE_TINY("_252_oxide_tiny", 15);
		
		private String name;
		private int id;
		
		private CaliforniumType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
	}
	
	public static enum ThoriumFuelType implements IStringSerializable, IItemMeta, IFissionStats {
		TBU("tbu", 0),
		TBU_OXIDE("tbu_oxide", 1);
		
		private String name;
		private int id;
		private double fuelTime;
		private double power;
		private double heatGen;
		
		private ThoriumFuelType(String name, int id) {
			this.name = name;
			this.id = id;
			fuelTime = NCConfig.fission_thorium_fuel_time[id];
			power = NCConfig.fission_thorium_power[id];
			heatGen = NCConfig.fission_thorium_heat_generation[id];
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
		
		@Override
		public double getBaseTime() {
			return fuelTime;
		}

		@Override
		public double getBasePower() {
			return power;
		}

		@Override
		public double getBaseHeat() {
			return heatGen;
		}
	}
	
	public static enum UraniumFuelType implements IStringSerializable, IItemMeta, IFissionStats {
		LEU_233("leu_233", 0),
		LEU_233_OXIDE("leu_233_oxide", 1),
		HEU_233("heu_233", 2),
		HEU_233_OXIDE("heu_233_oxide", 3),
		LEU_235("leu_235", 4),
		LEU_235_OXIDE("leu_235_oxide", 5),
		HEU_235("heu_235", 6),
		HEU_235_OXIDE("heu_235_oxide", 7);
		
		private String name;
		private int id;
		private double fuelTime;
		private double power;
		private double heatGen;
		
		private UraniumFuelType(String name, int id) {
			this.name = name;
			this.id = id;
			fuelTime = NCConfig.fission_uranium_fuel_time[id];
			power = NCConfig.fission_uranium_power[id];
			heatGen = NCConfig.fission_uranium_heat_generation[id];
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
		
		@Override
		public double getBaseTime() {
			return fuelTime;
		}

		@Override
		public double getBasePower() {
			return power;
		}

		@Override
		public double getBaseHeat() {
			return heatGen;
		}
	}
	
	public static enum NeptuniumFuelType implements IStringSerializable, IItemMeta, IFissionStats {
		LEN_236("len_236", 0),
		LEN_236_OXIDE("len_236_oxide", 1),
		HEN_236("hen_236", 2),
		HEN_236_OXIDE("hen_236_oxide", 3);
		
		private String name;
		private int id;
		private double fuelTime;
		private double power;
		private double heatGen;
		
		private NeptuniumFuelType(String name, int id) {
			this.name = name;
			this.id = id;
			fuelTime = NCConfig.fission_neptunium_fuel_time[id];
			power = NCConfig.fission_neptunium_power[id];
			heatGen = NCConfig.fission_neptunium_heat_generation[id];
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
		
		@Override
		public double getBaseTime() {
			return fuelTime;
		}

		@Override
		public double getBasePower() {
			return power;
		}

		@Override
		public double getBaseHeat() {
			return heatGen;
		}
	}
	
	public static enum PlutoniumFuelType implements IStringSerializable, IItemMeta, IFissionStats {
		LEP_239("lep_239", 0),
		LEP_239_OXIDE("lep_239_oxide", 1),
		HEP_239("hep_239", 2),
		HEP_239_OXIDE("hep_239_oxide", 3),
		LEP_241("lep_241", 4),
		LEP_241_OXIDE("lep_241_oxide", 5),
		HEP_241("hep_241", 6),
		HEP_241_OXIDE("hep_241_oxide", 7);
		
		private String name;
		private int id;
		private double fuelTime;
		private double power;
		private double heatGen;
		
		private PlutoniumFuelType(String name, int id) {
			this.name = name;
			this.id = id;
			fuelTime = NCConfig.fission_plutonium_fuel_time[id];
			power = NCConfig.fission_plutonium_power[id];
			heatGen = NCConfig.fission_plutonium_heat_generation[id];
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
		
		@Override
		public double getBaseTime() {
			return fuelTime;
		}

		@Override
		public double getBasePower() {
			return power;
		}

		@Override
		public double getBaseHeat() {
			return heatGen;
		}
	}
	
	public static enum MixedOxideFuelType implements IStringSerializable, IItemMeta, IFissionStats {
		MOX_239("mox_239", 0),
		MOX_241("mox_241", 1);
		
		private String name;
		private int id;
		private double fuelTime;
		private double power;
		private double heatGen;
		
		private MixedOxideFuelType(String name, int id) {
			this.name = name;
			this.id = id;
			fuelTime = NCConfig.fission_mox_fuel_time[id];
			power = NCConfig.fission_mox_power[id];
			heatGen = NCConfig.fission_mox_heat_generation[id];
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
		
		@Override
		public double getBaseTime() {
			return fuelTime;
		}

		@Override
		public double getBasePower() {
			return power;
		}

		@Override
		public double getBaseHeat() {
			return heatGen;
		}
	}
	
	public static enum AmericiumFuelType implements IStringSerializable, IItemMeta, IFissionStats {
		LEA_242("lea_242", 0),
		LEA_242_OXIDE("lea_242_oxide", 1),
		HEA_242("hea_242", 2),
		HEA_242_OXIDE("hea_242_oxide", 3);
		
		private String name;
		private int id;
		private double fuelTime;
		private double power;
		private double heatGen;
		
		private AmericiumFuelType(String name, int id) {
			this.name = name;
			this.id = id;
			fuelTime = NCConfig.fission_americium_fuel_time[id];
			power = NCConfig.fission_americium_power[id];
			heatGen = NCConfig.fission_americium_heat_generation[id];
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
		
		@Override
		public double getBaseTime() {
			return fuelTime;
		}

		@Override
		public double getBasePower() {
			return power;
		}

		@Override
		public double getBaseHeat() {
			return heatGen;
		}
	}
	
	public static enum CuriumFuelType implements IStringSerializable, IItemMeta, IFissionStats {
		LEC_243("lec_243", 0),
		LEC_243_OXIDE("lec_243_oxide", 1),
		HEC_243("hec_243", 2),
		HEC_243_OXIDE("hec_243_oxide", 3),
		LEC_245("lec_245", 4),
		LEC_245_OXIDE("lec_245_oxide", 5),
		HEC_245("hec_245", 6),
		HEC_245_OXIDE("hec_245_oxide", 7),
		LEC_247("lec_247", 8),
		LEC_247_OXIDE("lec_247_oxide", 9),
		HEC_247("hec_247", 10),
		HEC_247_OXIDE("hec_247_oxide", 11);
		
		private String name;
		private int id;
		private double fuelTime;
		private double power;
		private double heatGen;
		
		private CuriumFuelType(String name, int id) {
			this.name = name;
			this.id = id;
			fuelTime = NCConfig.fission_curium_fuel_time[id];
			power = NCConfig.fission_curium_power[id];
			heatGen = NCConfig.fission_curium_heat_generation[id];
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
		
		@Override
		public double getBaseTime() {
			return fuelTime;
		}

		@Override
		public double getBasePower() {
			return power;
		}

		@Override
		public double getBaseHeat() {
			return heatGen;
		}
	}
	
	public static enum BerkeliumFuelType implements IStringSerializable, IItemMeta, IFissionStats {
		LEB_248("leb_248", 0),
		LEB_248_OXIDE("leb_248_oxide", 1),
		HEB_248("heb_248", 2),
		HEB_248_OXIDE("heb_248_oxide", 3);
		
		private String name;
		private int id;
		private double fuelTime;
		private double power;
		private double heatGen;
		
		private BerkeliumFuelType(String name, int id) {
			this.name = name;
			this.id = id;
			fuelTime = NCConfig.fission_berkelium_fuel_time[id];
			power = NCConfig.fission_berkelium_power[id];
			heatGen = NCConfig.fission_berkelium_heat_generation[id];
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
		
		@Override
		public double getBaseTime() {
			return fuelTime;
		}

		@Override
		public double getBasePower() {
			return power;
		}

		@Override
		public double getBaseHeat() {
			return heatGen;
		}
	}
	
	public static enum CaliforniumFuelType implements IStringSerializable, IItemMeta, IFissionStats {
		LEC_249("lec_249", 0),
		LEC_249_OXIDE("lec_249_oxide", 1),
		HEC_249("hec_249", 2),
		HEC_249_OXIDE("hec_249_oxide", 3),
		LEC_251("lec_251", 4),
		LEC_251_OXIDE("lec_251_oxide", 5),
		HEC_251("hec_251", 6),
		HEC_251_OXIDE("hec_251_oxide", 7);
		
		private String name;
		private int id;
		private double fuelTime;
		private double power;
		private double heatGen;
		
		private CaliforniumFuelType(String name, int id) {
			this.name = name;
			this.id = id;
			fuelTime = NCConfig.fission_californium_fuel_time[id];
			power = NCConfig.fission_californium_power[id];
			heatGen = NCConfig.fission_californium_heat_generation[id];
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
		
		@Override
		public double getBaseTime() {
			return fuelTime;
		}

		@Override
		public double getBasePower() {
			return power;
		}

		@Override
		public double getBaseHeat() {
			return heatGen;
		}
	}
	
	public static enum ThoriumDepletedFuelType implements IStringSerializable, IItemMeta {
		TBU("tbu", 0),
		TBU_OXIDE("tbu_oxide", 1);
		
		private String name;
		private int id;
		
		private ThoriumDepletedFuelType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
	}
	
	public static enum UraniumDepletedFuelType implements IStringSerializable, IItemMeta {
		LEU_233("leu_233", 0),
		LEU_233_OXIDE("leu_233_oxide", 1),
		HEU_233("heu_233", 2),
		HEU_233_OXIDE("heu_233_oxide", 3),
		LEU_235("leu_235", 4),
		LEU_235_OXIDE("leu_235_oxide", 5),
		HEU_235("heu_235", 6),
		HEU_235_OXIDE("heu_235_oxide", 7);
		
		private String name;
		private int id;
		
		private UraniumDepletedFuelType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
	}
	
	public static enum NeptuniumDepletedFuelType implements IStringSerializable, IItemMeta {
		LEN_236("len_236", 0),
		LEN_236_OXIDE("len_236_oxide", 1),
		HEN_236("hen_236", 2),
		HEN_236_OXIDE("hen_236_oxide", 3);
		
		private String name;
		private int id;
		
		private NeptuniumDepletedFuelType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
	}
	
	public static enum PlutoniumDepletedFuelType implements IStringSerializable, IItemMeta {
		LEP_239("lep_239", 0),
		LEP_239_OXIDE("lep_239_oxide", 1),
		HEP_239("hep_239", 2),
		HEP_239_OXIDE("hep_239_oxide", 3),
		LEP_241("lep_241", 4),
		LEP_241_OXIDE("lep_241_oxide", 5),
		HEP_241("hep_241", 6),
		HEP_241_OXIDE("hep_241_oxide", 7);
		
		private String name;
		private int id;
		
		private PlutoniumDepletedFuelType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
	}
	
	public static enum MixedOxideDepletedFuelType implements IStringSerializable, IItemMeta {
		MOX_239("mox_239", 0),
		MOX_241("mox_241", 1);
		
		private String name;
		private int id;
		
		private MixedOxideDepletedFuelType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
	}
	
	public static enum AmericiumDepletedFuelType implements IStringSerializable, IItemMeta {
		LEA_242("lea_242", 0),
		LEA_242_OXIDE("lea_242_oxide", 1),
		HEA_242("hea_242", 2),
		HEA_242_OXIDE("hea_242_oxide", 3);
		
		private String name;
		private int id;
		
		private AmericiumDepletedFuelType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
	}
	
	public static enum CuriumDepletedFuelType implements IStringSerializable, IItemMeta {
		LEC_243("lec_243", 0),
		LEC_243_OXIDE("lec_243_oxide", 1),
		HEC_243("hec_243", 2),
		HEC_243_OXIDE("hec_243_oxide", 3),
		LEC_245("lec_245", 4),
		LEC_245_OXIDE("lec_245_oxide", 5),
		HEC_245("hec_245", 6),
		HEC_245_OXIDE("hec_245_oxide", 7),
		LEC_247("lec_247", 8),
		LEC_247_OXIDE("lec_247_oxide", 9),
		HEC_247("hec_247", 10),
		HEC_247_OXIDE("hec_247_oxide", 11);
		
		private String name;
		private int id;
		
		private CuriumDepletedFuelType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
	}
	
	public static enum BerkeliumDepletedFuelType implements IStringSerializable, IItemMeta {
		LEB_248("leb_248", 0),
		LEB_248_OXIDE("leb_248_oxide", 1),
		HEB_248("heb_248", 2),
		HEB_248_OXIDE("heb_248_oxide", 3);
		
		private String name;
		private int id;
		
		private BerkeliumDepletedFuelType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
	}
	
	public static enum CaliforniumDepletedFuelType implements IStringSerializable, IItemMeta {
		LEC_249("lec_249", 0),
		LEC_249_OXIDE("lec_249_oxide", 1),
		HEC_249("hec_249", 2),
		HEC_249_OXIDE("hec_249_oxide", 3),
		LEC_251("lec_251", 4),
		LEC_251_OXIDE("lec_251_oxide", 5),
		HEC_251("hec_251", 6),
		HEC_251_OXIDE("hec_251_oxide", 7);
		
		private String name;
		private int id;
		
		private CaliforniumDepletedFuelType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
	}
	
	public static enum IC2DepletedFuelType implements IStringSerializable, IItemMeta {
		U("u", 0),
		MOX("mox", 1);
		
		private String name;
		private int id;
		
		private IC2DepletedFuelType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
	}
	
	public static enum BoronType implements IStringSerializable, IItemMeta {
		_10("_10", 0),
		_10_TINY("_10_tiny", 1),
		_11("_11", 2),
		_11_TINY("_11_tiny", 3);
		
		private String name;
		private int id;
		
		private BoronType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
	}
	
	public static enum LithiumType implements IStringSerializable, IItemMeta {
		_6("_6", 0),
		_6_TINY("_6_tiny", 1),
		_7("_7", 2),
		_7_TINY("_7_tiny", 3);
		
		private String name;
		private int id;
		
		private LithiumType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
	}
	
	public static enum RadShieldingType implements IStringSerializable, IItemMeta {
		LIGHT("light", 0),
		MEDIUM("medium", 1),
		HEAVY("heavy", 2);
		
		private String name;
		private int id;
		
		private RadShieldingType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		@Override
		public int getID() {
			return id;
		}
	}
}
