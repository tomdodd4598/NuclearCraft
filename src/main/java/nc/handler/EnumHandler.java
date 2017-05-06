package nc.handler;

import nc.config.NCConfig;
import net.minecraft.util.IStringSerializable;

public class EnumHandler {
	
	public static enum OreTypes implements IStringSerializable {
		COPPER("copper", 0),
		TIN("tin", 1),
		LEAD("lead", 2),
		THORIUM("thorium", 3),
		URANIUM("uranium", 4),
		BORON("boron", 5),
		LITHIUM("lithium", 6),
		MAGNESIUM("magnesium", 7);
		
		private int id;
		private String name;
		
		private OreTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}

		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum FissionBlockTypes implements IStringSerializable {
		CASING("casing", 0, 15.0F),
		BLAST("blast", 1, 3000.0F);
		
		private int id;
		private String name;
		private float resistance;
		
		private FissionBlockTypes(String name, int id, float resistance) {
			this.id = id;
			this.name = name;
			this.resistance = resistance;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public float getResistance() {	
			return resistance;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum CoolerTypes implements IStringSerializable {
		EMPTY("empty", 0, 0, 0.0D),
		WATER("water", 1, 0, NCConfig.fission_cooling_rate[0]),
		REDSTONE("redstone", 2, 7, NCConfig.fission_cooling_rate[1]),
		QUARTZ("quartz", 3, 0, NCConfig.fission_cooling_rate[2]),
		GOLD("gold", 4, 0, NCConfig.fission_cooling_rate[3]),
		GLOWSTONE("glowstone", 5, 15, NCConfig.fission_cooling_rate[4]),
		LAPIS("lapis", 6, 0, NCConfig.fission_cooling_rate[5]),
		DIAMOND("diamond", 7, 0, NCConfig.fission_cooling_rate[6]),
		HELIUM("helium", 8, 0, NCConfig.fission_cooling_rate[7]),
		ENDERIUM("enderium", 9, 7, NCConfig.fission_cooling_rate[8]),
		CRYOTHEUM("cryotheum", 10, 7, NCConfig.fission_cooling_rate[9]);
		
		private int id;
		private String name;
		private int lightLevel;
		private double coolingRate;
		
		private CoolerTypes(String name, int id, int lightLevel, double coolingRate) {
			this.id = id;
			this.name = name;
			this.lightLevel = lightLevel;
			this.coolingRate = coolingRate;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public int getLightLevel() {	
			return lightLevel;
		}
		
		public double getCooling() {	
			return coolingRate;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum IngotTypes implements IStringSerializable {
		COPPER("copper", 0),
		TIN("tin", 1),
		LEAD("lead", 2),
		THORIUM("thorium", 3),
		URANIUM("uranium", 4),
		BORON("boron", 5),
		LITHIUM("lithium", 6),
		MAGNESIUM("magnesium", 7),
		GRAPHITE("graphite", 8);
		
		private int id;
		private String name;
		
		private IngotTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum IngotOxideTypes implements IStringSerializable {
		THORIUM("thorium", 0),
		URANIUM("uranium", 1),
		MANGANESE("manganese", 2),
		MANGANESE2("manganese2", 3);
		
		private int id;
		private String name;
		
		private IngotOxideTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum DustTypes implements IStringSerializable {
		COPPER("copper", 0),
		TIN("tin", 1),
		LEAD("lead", 2),
		THORIUM("thorium", 3),
		URANIUM("uranium", 4),
		BORON("boron", 5),
		LITHIUM("lithium", 6),
		MAGNESIUM("magnesium", 7),
		GRAPHITE("graphite", 8);
		
		private int id;
		private String name;
		
		private DustTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum DustOxideTypes implements IStringSerializable {
		THORIUM("thorium", 0),
		URANIUM("uranium", 1),
		MANGANESE("manganese", 2),
		MANGANESE2("manganese2", 3);
		
		private int id;
		private String name;
		
		private DustOxideTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum GemTypes implements IStringSerializable {
		RHODOCHROSITE("rhodochrosite", 0);
		
		private int id;
		private String name;
		
		private GemTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum GemDustTypes implements IStringSerializable {
		DIAMOND("diamond", 0),
		RHODOCHROSITE("rhodochrosite", 1),
		QUARTZ("quartz", 2),
		OBSIDIAN("obsidian", 3);
		
		private int id;
		private String name;
		
		private GemDustTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum AlloyTypes implements IStringSerializable {
		BRONZE("bronze", 0),
		TOUGH("tough", 1),
		HARD_CARBON("hard_carbon", 2),
		MAGNESIUM_DIBORIDE("magnesium_diboride", 3),
		LITHIUM_MANGANESE_DIOXIDE("lithium_manganese_dioxide", 4),
		STEEL("steel", 5),
		FERROBORON("ferroboron", 6);
		
		private int id;
		private String name;
		
		private AlloyTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum PartTypes implements IStringSerializable {
		PLATE_BASIC("plate_basic", 0),
		PLATE_ADVANCED("plate_advanced", 1),
		PLATE_DU("plate_du", 2),
		PLATE_ELITE("plate_elite", 3),
		WIRE_COPPER("wire_copper", 4),
		WIRE_MAGNESIUM_DIBORIDE("wire_magnesium_diboride", 5);
		
		private int id;
		private String name;
		
		private PartTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum UpgradeTypes implements IStringSerializable {
		SPEED("speed", 0);
		
		private int id;
		private String name;
		
		private UpgradeTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum ThoriumTypes implements IStringSerializable {
		_230("_230", 0),
		_230_OXIDE("_230_oxide", 1),
		_230_TINY("_230_tiny", 2),
		_230_OXIDE_TINY("_230_oxide_tiny", 3),
		_232("_232", 4),
		_232_OXIDE("_232_oxide", 5),
		_232_TINY("_232_tiny", 6),
		_232_OXIDE_TINY("_232_oxide_tiny", 7);
		
		private int id;
		private String name;
		
		private ThoriumTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum UraniumTypes implements IStringSerializable {
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
		
		private int id;
		private String name;
		
		private UraniumTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum NeptuniumTypes implements IStringSerializable {
		_236("_236", 0),
		_236_OXIDE("_236_oxide", 1),
		_236_TINY("_236_tiny", 2),
		_236_OXIDE_TINY("_236_oxide_tiny", 3),
		_237("_237", 4),
		_237_OXIDE("_237_oxide", 5),
		_237_TINY("_237_tiny", 6),
		_237_OXIDE_TINY("_237_oxide_tiny", 7);
		
		private int id;
		private String name;
		
		private NeptuniumTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum PlutoniumTypes implements IStringSerializable {
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
		
		private int id;
		private String name;
		
		private PlutoniumTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum AmericiumTypes implements IStringSerializable {
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
		
		private int id;
		private String name;
		
		private AmericiumTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum CuriumTypes implements IStringSerializable {
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
		
		private int id;
		private String name;
		
		private CuriumTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum BerkeliumTypes implements IStringSerializable {
		_247("_247", 0),
		_247_OXIDE("_247_oxide", 1),
		_247_TINY("_247_tiny", 2),
		_247_OXIDE_TINY("_247_oxide_tiny", 3),
		_248("_248", 4),
		_248_OXIDE("_248_oxide", 5),
		_248_TINY("_248_tiny", 6),
		_248_OXIDE_TINY("_248_oxide_tiny", 7);
		
		private int id;
		private String name;
		
		private BerkeliumTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum CaliforniumTypes implements IStringSerializable {
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
		
		private int id;
		private String name;
		
		private CaliforniumTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum ThoriumFuelTypes implements IStringSerializable {
		TBU("tbu", 0),
		TBU_OXIDE("tbu_oxide", 1);
		
		private int id;
		private String name;
		
		private ThoriumFuelTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum UraniumFuelTypes implements IStringSerializable {
		LEU_233("leu_233", 0),
		LEU_233_OXIDE("leu_233_oxide", 1),
		HEU_233("heu_233", 2),
		HEU_233_OXIDE("heu_233_oxide", 3),
		LEU_235("leu_235", 4),
		LEU_235_OXIDE("leu_235_oxide", 5),
		HEU_235("heu_235", 6),
		HEU_235_OXIDE("heu_235_oxide", 7);
		
		private int id;
		private String name;
		
		private UraniumFuelTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum NeptuniumFuelTypes implements IStringSerializable {
		LEN_236("len_236", 0),
		LEN_236_OXIDE("len_236_oxide", 1),
		HEN_236("hen_236", 2),
		HEN_236_OXIDE("hen_236_oxide", 3);
		
		private int id;
		private String name;
		
		private NeptuniumFuelTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum PlutoniumFuelTypes implements IStringSerializable {
		LEP_239("lep_239", 0),
		LEP_239_OXIDE("lep_239_oxide", 1),
		HEP_239("hep_239", 2),
		HEP_239_OXIDE("hep_239_oxide", 3),
		LEP_241("lep_241", 4),
		LEP_241_OXIDE("lep_241_oxide", 5),
		HEP_241("hep_241", 6),
		HEP_241_OXIDE("hep_241_oxide", 7);
		
		private int id;
		private String name;
		
		private PlutoniumFuelTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum MixedOxideFuelTypes implements IStringSerializable {
		MOX_239("mox_239", 0),
		MOX_241("mox_241", 1);
		
		private int id;
		private String name;
		
		private MixedOxideFuelTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum AmericiumFuelTypes implements IStringSerializable {
		LEA_242("lea_242", 0),
		LEA_242_OXIDE("lea_242_oxide", 1),
		HEA_242("hea_242", 2),
		HEA_242_OXIDE("hea_242_oxide", 3);
		
		private int id;
		private String name;
		
		private AmericiumFuelTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum CuriumFuelTypes implements IStringSerializable {
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
		
		private int id;
		private String name;
		
		private CuriumFuelTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum BerkeliumFuelTypes implements IStringSerializable {
		LEB_248("leb_248", 0),
		LEB_248_OXIDE("leb_248_oxide", 1),
		HEB_248("heb_248", 2),
		HEB_248_OXIDE("heb_248_oxide", 3);
		
		private int id;
		private String name;
		
		private BerkeliumFuelTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum CaliforniumFuelTypes implements IStringSerializable {
		LEC_249("lec_249", 0),
		LEC_249_OXIDE("lec_249_oxide", 1),
		HEC_249("hec_249", 2),
		HEC_249_OXIDE("hec_249_oxide", 3),
		LEC_251("lec_251", 4),
		LEC_251_OXIDE("lec_251_oxide", 5),
		HEC_251("hec_251", 6),
		HEC_251_OXIDE("hec_251_oxide", 7);
		
		private int id;
		private String name;
		
		private CaliforniumFuelTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum ThoriumFuelRodTypes implements IStringSerializable, IFissionable {
		TBU("tbu", 0, NCConfig.fission_thorium_fuel_time[0], NCConfig.fission_thorium_power[0], NCConfig.fission_thorium_heat_generation[0]),
		TBU_OXIDE("tbu_oxide", 1, NCConfig.fission_thorium_fuel_time[1], NCConfig.fission_thorium_power[1], NCConfig.fission_thorium_heat_generation[1]);
		
		private int id;
		private String name;
		private double fuelTime;
		private double power;
		private double heatGen;
		
		private ThoriumFuelRodTypes(String name, int id, double fuelTime, double power, double heatGen) {
			this.id = id;
			this.name = name;
			this.fuelTime = fuelTime;
			this.power = power;
			this.heatGen = heatGen;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public double getBaseTime() {
			return fuelTime;
		}

		public double getBasePower() {
			return power;
		}

		public double getBaseHeat() {
			return heatGen;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum UraniumFuelRodTypes implements IStringSerializable, IFissionable {
		LEU_233("leu_233", 0, NCConfig.fission_uranium_fuel_time[0], NCConfig.fission_uranium_power[0], NCConfig.fission_uranium_heat_generation[0]),
		LEU_233_OXIDE("leu_233_oxide", 1, NCConfig.fission_uranium_fuel_time[1], NCConfig.fission_uranium_power[1], NCConfig.fission_uranium_heat_generation[1]),
		HEU_233("heu_233", 2, NCConfig.fission_uranium_fuel_time[2], NCConfig.fission_uranium_power[2], NCConfig.fission_uranium_heat_generation[2]),
		HEU_233_OXIDE("heu_233_oxide", 3, NCConfig.fission_uranium_fuel_time[3], NCConfig.fission_uranium_power[3], NCConfig.fission_uranium_heat_generation[3]),
		LEU_235("leu_235", 4, NCConfig.fission_uranium_fuel_time[4], NCConfig.fission_uranium_power[4], NCConfig.fission_uranium_heat_generation[4]),
		LEU_235_OXIDE("leu_235_oxide", 5, NCConfig.fission_uranium_fuel_time[5], NCConfig.fission_uranium_power[5], NCConfig.fission_uranium_heat_generation[5]),
		HEU_235("heu_235", 6, NCConfig.fission_uranium_fuel_time[6], NCConfig.fission_uranium_power[6], NCConfig.fission_uranium_heat_generation[6]),
		HEU_235_OXIDE("heu_235_oxide", 7, NCConfig.fission_uranium_fuel_time[7], NCConfig.fission_uranium_power[7], NCConfig.fission_uranium_heat_generation[7]);
		
		private int id;
		private String name;
		private double fuelTime;
		private double power;
		private double heatGen;
		
		private UraniumFuelRodTypes(String name, int id, double fuelTime, double power, double heatGen) {
			this.id = id;
			this.name = name;
			this.fuelTime = fuelTime;
			this.power = power;
			this.heatGen = heatGen;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public double getBaseTime() {
			return fuelTime;
		}

		public double getBasePower() {
			return power;
		}

		public double getBaseHeat() {
			return heatGen;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum NeptuniumFuelRodTypes implements IStringSerializable, IFissionable {
		LEN_236("len_236", 0, NCConfig.fission_neptunium_fuel_time[0], NCConfig.fission_neptunium_power[0], NCConfig.fission_neptunium_heat_generation[0]),
		LEN_236_OXIDE("len_236_oxide", 1, NCConfig.fission_neptunium_fuel_time[1], NCConfig.fission_neptunium_power[1], NCConfig.fission_neptunium_heat_generation[1]),
		HEN_236("hen_236", 2, NCConfig.fission_neptunium_fuel_time[2], NCConfig.fission_neptunium_power[2], NCConfig.fission_neptunium_heat_generation[2]),
		HEN_236_OXIDE("hen_236_oxide", 3, NCConfig.fission_neptunium_fuel_time[3], NCConfig.fission_neptunium_power[3], NCConfig.fission_neptunium_heat_generation[3]);
		
		private int id;
		private String name;
		private double fuelTime;
		private double power;
		private double heatGen;
		
		private NeptuniumFuelRodTypes(String name, int id, double fuelTime, double power, double heatGen) {
			this.id = id;
			this.name = name;
			this.fuelTime = fuelTime;
			this.power = power;
			this.heatGen = heatGen;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public double getBaseTime() {
			return fuelTime;
		}

		public double getBasePower() {
			return power;
		}

		public double getBaseHeat() {
			return heatGen;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum PlutoniumFuelRodTypes implements IStringSerializable, IFissionable {
		LEP_239("lep_239", 0, NCConfig.fission_plutonium_fuel_time[0], NCConfig.fission_plutonium_power[0], NCConfig.fission_plutonium_heat_generation[0]),
		LEP_239_OXIDE("lep_239_oxide", 1, NCConfig.fission_plutonium_fuel_time[1], NCConfig.fission_plutonium_power[1], NCConfig.fission_plutonium_heat_generation[1]),
		HEP_239("hep_239", 2, NCConfig.fission_plutonium_fuel_time[2], NCConfig.fission_plutonium_power[2], NCConfig.fission_plutonium_heat_generation[2]),
		HEP_239_OXIDE("hep_239_oxide", 3, NCConfig.fission_plutonium_fuel_time[3], NCConfig.fission_plutonium_power[3], NCConfig.fission_plutonium_heat_generation[3]),
		LEP_241("lep_241", 4, NCConfig.fission_plutonium_fuel_time[4], NCConfig.fission_plutonium_power[4], NCConfig.fission_plutonium_heat_generation[4]),
		LEP_241_OXIDE("lep_241_oxide", 5, NCConfig.fission_plutonium_fuel_time[5], NCConfig.fission_plutonium_power[5], NCConfig.fission_plutonium_heat_generation[5]),
		HEP_241("hep_241", 6, NCConfig.fission_plutonium_fuel_time[6], NCConfig.fission_plutonium_power[6], NCConfig.fission_plutonium_heat_generation[6]),
		HEP_241_OXIDE("hep_241_oxide", 7, NCConfig.fission_plutonium_fuel_time[7], NCConfig.fission_plutonium_power[7], NCConfig.fission_plutonium_heat_generation[7]);
		
		private int id;
		private String name;
		private double fuelTime;
		private double power;
		private double heatGen;
		
		private PlutoniumFuelRodTypes(String name, int id, double fuelTime, double power, double heatGen) {
			this.id = id;
			this.name = name;
			this.fuelTime = fuelTime;
			this.power = power;
			this.heatGen = heatGen;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public double getBaseTime() {
			return fuelTime;
		}

		public double getBasePower() {
			return power;
		}

		public double getBaseHeat() {
			return heatGen;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum MixedOxideFuelRodTypes implements IStringSerializable, IFissionable {
		MOX_239("mox_239", 0, NCConfig.fission_mox_fuel_time[0], NCConfig.fission_mox_power[0], NCConfig.fission_mox_heat_generation[0]),
		MOX_241("mox_241", 1, NCConfig.fission_mox_fuel_time[1], NCConfig.fission_mox_power[1], NCConfig.fission_mox_heat_generation[1]);
		
		private int id;
		private String name;
		private double fuelTime;
		private double power;
		private double heatGen;
		
		private MixedOxideFuelRodTypes(String name, int id, double fuelTime, double power, double heatGen) {
			this.id = id;
			this.name = name;
			this.fuelTime = fuelTime;
			this.power = power;
			this.heatGen = heatGen;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public double getBaseTime() {
			return fuelTime;
		}

		public double getBasePower() {
			return power;
		}

		public double getBaseHeat() {
			return heatGen;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum AmericiumFuelRodTypes implements IStringSerializable, IFissionable {
		LEA_242("lea_242", 0, NCConfig.fission_americium_fuel_time[0], NCConfig.fission_americium_power[0], NCConfig.fission_americium_heat_generation[0]),
		LEA_242_OXIDE("lea_242_oxide", 1, NCConfig.fission_americium_fuel_time[1], NCConfig.fission_americium_power[1], NCConfig.fission_americium_heat_generation[1]),
		HEA_242("hea_242", 2, NCConfig.fission_americium_fuel_time[2], NCConfig.fission_americium_power[2], NCConfig.fission_americium_heat_generation[2]),
		HEA_242_OXIDE("hea_242_oxide", 3, NCConfig.fission_americium_fuel_time[3], NCConfig.fission_americium_power[3], NCConfig.fission_americium_heat_generation[3]);
		
		private int id;
		private String name;
		private double fuelTime;
		private double power;
		private double heatGen;
		
		private AmericiumFuelRodTypes(String name, int id, double fuelTime, double power, double heatGen) {
			this.id = id;
			this.name = name;
			this.fuelTime = fuelTime;
			this.power = power;
			this.heatGen = heatGen;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public double getBaseTime() {
			return fuelTime;
		}

		public double getBasePower() {
			return power;
		}

		public double getBaseHeat() {
			return heatGen;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum CuriumFuelRodTypes implements IStringSerializable, IFissionable {
		LEC_243("lec_243", 0, NCConfig.fission_curium_fuel_time[0], NCConfig.fission_curium_power[0], NCConfig.fission_curium_heat_generation[0]),
		LEC_243_OXIDE("lec_243_oxide", 1, NCConfig.fission_curium_fuel_time[1], NCConfig.fission_curium_power[1], NCConfig.fission_curium_heat_generation[1]),
		HEC_243("hec_243", 2, NCConfig.fission_curium_fuel_time[2], NCConfig.fission_curium_power[2], NCConfig.fission_curium_heat_generation[2]),
		HEC_243_OXIDE("hec_243_oxide", 3, NCConfig.fission_curium_fuel_time[3], NCConfig.fission_curium_power[3], NCConfig.fission_curium_heat_generation[3]),
		LEC_245("lec_245", 4, NCConfig.fission_curium_fuel_time[4], NCConfig.fission_curium_power[4], NCConfig.fission_curium_heat_generation[4]),
		LEC_245_OXIDE("lec_245_oxide", 5, NCConfig.fission_curium_fuel_time[5], NCConfig.fission_curium_power[5], NCConfig.fission_curium_heat_generation[5]),
		HEC_245("hec_245", 6, NCConfig.fission_curium_fuel_time[6], NCConfig.fission_curium_power[6], NCConfig.fission_curium_heat_generation[6]),
		HEC_245_OXIDE("hec_245_oxide", 7, NCConfig.fission_curium_fuel_time[7], NCConfig.fission_curium_power[7], NCConfig.fission_curium_heat_generation[7]),
		LEC_247("lec_247", 8, NCConfig.fission_curium_fuel_time[8], NCConfig.fission_curium_power[8], NCConfig.fission_curium_heat_generation[8]),
		LEC_247_OXIDE("lec_247_oxide", 9, NCConfig.fission_curium_fuel_time[9], NCConfig.fission_curium_power[9], NCConfig.fission_curium_heat_generation[9]),
		HEC_247("hec_247", 10, NCConfig.fission_curium_fuel_time[10], NCConfig.fission_curium_power[10], NCConfig.fission_curium_heat_generation[10]),
		HEC_247_OXIDE("hec_247_oxide", 11, NCConfig.fission_curium_fuel_time[11], NCConfig.fission_curium_power[11], NCConfig.fission_curium_heat_generation[11]);
		
		private int id;
		private String name;
		private double fuelTime;
		private double power;
		private double heatGen;
		
		private CuriumFuelRodTypes(String name, int id, double fuelTime, double power, double heatGen) {
			this.id = id;
			this.name = name;
			this.fuelTime = fuelTime;
			this.power = power;
			this.heatGen = heatGen;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public double getBaseTime() {
			return fuelTime;
		}

		public double getBasePower() {
			return power;
		}

		public double getBaseHeat() {
			return heatGen;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum BerkeliumFuelRodTypes implements IStringSerializable, IFissionable {
		LEB_248("leb_248", 0, NCConfig.fission_berkelium_fuel_time[0], NCConfig.fission_berkelium_power[0], NCConfig.fission_berkelium_heat_generation[0]),
		LEB_248_OXIDE("leb_248_oxide", 1, NCConfig.fission_berkelium_fuel_time[1], NCConfig.fission_berkelium_power[1], NCConfig.fission_berkelium_heat_generation[1]),
		HEB_248("heb_248", 2, NCConfig.fission_berkelium_fuel_time[2], NCConfig.fission_berkelium_power[2], NCConfig.fission_berkelium_heat_generation[2]),
		HEB_248_OXIDE("heb_248_oxide", 3, NCConfig.fission_berkelium_fuel_time[3], NCConfig.fission_berkelium_power[3], NCConfig.fission_berkelium_heat_generation[3]);
		
		private int id;
		private String name;
		private double fuelTime;
		private double power;
		private double heatGen;
		
		private BerkeliumFuelRodTypes(String name, int id, double fuelTime, double power, double heatGen) {
			this.id = id;
			this.name = name;
			this.fuelTime = fuelTime;
			this.power = power;
			this.heatGen = heatGen;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public double getBaseTime() {
			return fuelTime;
		}

		public double getBasePower() {
			return power;
		}

		public double getBaseHeat() {
			return heatGen;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum CaliforniumFuelRodTypes implements IStringSerializable, IFissionable {
		LEC_249("lec_249", 0, NCConfig.fission_californium_fuel_time[0], NCConfig.fission_californium_power[0], NCConfig.fission_californium_heat_generation[0]),
		LEC_249_OXIDE("lec_249_oxide", 1, NCConfig.fission_californium_fuel_time[1], NCConfig.fission_californium_power[1], NCConfig.fission_californium_heat_generation[1]),
		HEC_249("hec_249", 2, NCConfig.fission_californium_fuel_time[2], NCConfig.fission_californium_power[2], NCConfig.fission_californium_heat_generation[2]),
		HEC_249_OXIDE("hec_249_oxide", 3, NCConfig.fission_californium_fuel_time[3], NCConfig.fission_californium_power[3], NCConfig.fission_californium_heat_generation[3]),
		LEC_251("lec_251", 4, NCConfig.fission_californium_fuel_time[4], NCConfig.fission_californium_power[4], NCConfig.fission_californium_heat_generation[4]),
		LEC_251_OXIDE("lec_251_oxide", 5, NCConfig.fission_californium_fuel_time[5], NCConfig.fission_californium_power[5], NCConfig.fission_californium_heat_generation[5]),
		HEC_251("hec_251", 6, NCConfig.fission_californium_fuel_time[6], NCConfig.fission_californium_power[6], NCConfig.fission_californium_heat_generation[6]),
		HEC_251_OXIDE("hec_251_oxide", 7, NCConfig.fission_californium_fuel_time[7], NCConfig.fission_californium_power[7], NCConfig.fission_californium_heat_generation[7]);
		
		private int id;
		private String name;
		private double fuelTime;
		private double power;
		private double heatGen;
		
		private CaliforniumFuelRodTypes(String name, int id, double fuelTime, double power, double heatGen) {
			this.id = id;
			this.name = name;
			this.fuelTime = fuelTime;
			this.power = power;
			this.heatGen = heatGen;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public double getBaseTime() {
			return fuelTime;
		}

		public double getBasePower() {
			return power;
		}

		public double getBaseHeat() {
			return heatGen;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum ThoriumDepletedFuelRodTypes implements IStringSerializable {
		TBU("tbu", 0),
		TBU_OXIDE("tbu_oxide", 1);
		
		private int id;
		private String name;
		
		private ThoriumDepletedFuelRodTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum UraniumDepletedFuelRodTypes implements IStringSerializable {
		LEU_233("leu_233", 0),
		LEU_233_OXIDE("leu_233_oxide", 1),
		HEU_233("heu_233", 2),
		HEU_233_OXIDE("heu_233_oxide", 3),
		LEU_235("leu_235", 4),
		LEU_235_OXIDE("leu_235_oxide", 5),
		HEU_235("heu_235", 6),
		HEU_235_OXIDE("heu_235_oxide", 7);
		
		private int id;
		private String name;
		
		private UraniumDepletedFuelRodTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum NeptuniumDepletedFuelRodTypes implements IStringSerializable {
		LEN_236("len_236", 0),
		LEN_236_OXIDE("len_236_oxide", 1),
		HEN_236("hen_236", 2),
		HEN_236_OXIDE("hen_236_oxide", 3);
		
		private int id;
		private String name;
		
		private NeptuniumDepletedFuelRodTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum PlutoniumDepletedFuelRodTypes implements IStringSerializable {
		LEP_239("lep_239", 0),
		LEP_239_OXIDE("lep_239_oxide", 1),
		HEP_239("hep_239", 2),
		HEP_239_OXIDE("hep_239_oxide", 3),
		LEP_241("lep_241", 4),
		LEP_241_OXIDE("lep_241_oxide", 5),
		HEP_241("hep_241", 6),
		HEP_241_OXIDE("hep_241_oxide", 7);
		
		private int id;
		private String name;
		
		private PlutoniumDepletedFuelRodTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum MixedOxideDepletedFuelRodTypes implements IStringSerializable {
		MOX_239("mox_239", 0),
		MOX_241("mox_241", 1);
		
		private int id;
		private String name;
		
		private MixedOxideDepletedFuelRodTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum AmericiumDepletedFuelRodTypes implements IStringSerializable {
		LEA_242("lea_242", 0),
		LEA_242_OXIDE("lea_242_oxide", 1),
		HEA_242("hea_242", 2),
		HEA_242_OXIDE("hea_242_oxide", 3);
		
		private int id;
		private String name;
		
		private AmericiumDepletedFuelRodTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum CuriumDepletedFuelRodTypes implements IStringSerializable {
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
		
		private int id;
		private String name;
		
		private CuriumDepletedFuelRodTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum BerkeliumDepletedFuelRodTypes implements IStringSerializable {
		LEB_248("leb_248", 0),
		LEB_248_OXIDE("leb_248_oxide", 1),
		HEB_248("heb_248", 2),
		HEB_248_OXIDE("heb_248_oxide", 3);
		
		private int id;
		private String name;
		
		private BerkeliumDepletedFuelRodTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum CaliforniumDepletedFuelRodTypes implements IStringSerializable {
		LEC_249("lec_249", 0),
		LEC_249_OXIDE("lec_249_oxide", 1),
		HEC_249("hec_249", 2),
		HEC_249_OXIDE("hec_249_oxide", 3),
		LEC_251("lec_251", 4),
		LEC_251_OXIDE("lec_251_oxide", 5),
		HEC_251("hec_251", 6),
		HEC_251_OXIDE("hec_251_oxide", 7);
		
		private int id;
		private String name;
		
		private CaliforniumDepletedFuelRodTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum BoronTypes implements IStringSerializable {
		_10("_10", 0),
		_10_TINY("_10_tiny", 1),
		_11("_11", 2),
		_11_TINY("_11_tiny", 3);
		
		private int id;
		private String name;
		
		private BoronTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
	
	public static enum LithiumTypes implements IStringSerializable {
		_6("_6", 0),
		_6_TINY("_6_tiny", 1),
		_7("_7", 2),
		_7_TINY("_7_tiny", 3);
		
		private int id;
		private String name;
		
		private LithiumTypes(String name, int id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {	
			return name;
		}
		
		public int getID() {	
			return id;
		}
		
		public String toString() {	
			return getName();
		}
		
		public Object[] getValues() {
			return values();
		}
	}
}
