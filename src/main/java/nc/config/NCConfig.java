package nc.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nc.Global;
import nc.util.NCMath;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NCConfig {

	private static Configuration config = null;
	
	public static final String CATEGORY_ORES = "ores";
	public static final String CATEGORY_PROCESSORS = "processors";
	public static final String CATEGORY_GENERATORS = "generators";
	public static final String CATEGORY_FISSION = "fission";
	public static final String CATEGORY_FUSION = "fusion";
	public static final String CATEGORY_ACCELERATOR = "accelerator";
	public static final String CATEGORY_ENERGY_STORAGE = "energy_storage";
	public static final String CATEGORY_TOOLS = "tools";
	public static final String CATEGORY_ARMOR = "armor";
	public static final String CATEGORY_OTHER = "other";
	
	public static int[] ore_dims;
	public static boolean ore_dims_list_type;
	public static boolean[] ore_gen;
	public static int[] ore_size;
	public static int[] ore_rate;
	public static int[] ore_min_height;
	public static int[] ore_max_height;
	public static boolean[] ore_drops;
	
	public static int[] processor_time;
	public static int[] processor_power;
	public static int processor_rf_per_eu;
	public static int processor_update_rate;
	public static int[] processor_passive_rate;
	public static int cobble_gen_power;
	
	public static int[] rtg_power;
	public static int[] solar_power;
	public static int generator_rf_per_eu;
	public static int generator_update_rate;
	
	public static double fission_power; // Default: 1
	public static double fission_fuel_use; // Default: 1
	public static double fission_heat_generation; // Default: 1
	public static double[] fission_cooling_rate;
	public static double[] fission_active_cooling_rate;
	public static boolean fission_water_cooler_requirement;
	public static boolean fission_overheat;
	public static int fission_update_rate;
	public static int fission_min_size; // Default: 1
	public static int fission_max_size; // Default: 24
	public static int fission_active_cooler_max_rate;
	
	public static double[] fission_thorium_fuel_time;
	public static double[] fission_thorium_power;
	public static double[] fission_thorium_heat_generation;
	
	public static double[] fission_uranium_fuel_time;
	public static double[] fission_uranium_power;
	public static double[] fission_uranium_heat_generation;
	
	public static double[] fission_neptunium_fuel_time;
	public static double[] fission_neptunium_power;
	public static double[] fission_neptunium_heat_generation;
	
	public static double[] fission_plutonium_fuel_time;
	public static double[] fission_plutonium_power;
	public static double[] fission_plutonium_heat_generation;
	
	public static double[] fission_mox_fuel_time;
	public static double[] fission_mox_power;
	public static double[] fission_mox_heat_generation;
	
	public static double[] fission_americium_fuel_time;
	public static double[] fission_americium_power;
	public static double[] fission_americium_heat_generation;
	
	public static double[] fission_curium_fuel_time;
	public static double[] fission_curium_power;
	public static double[] fission_curium_heat_generation;
	
	public static double[] fission_berkelium_fuel_time;
	public static double[] fission_berkelium_power;
	public static double[] fission_berkelium_heat_generation;
	
	public static double[] fission_californium_fuel_time;
	public static double[] fission_californium_power;
	public static double[] fission_californium_heat_generation;
	
	public static double fusion_base_power; // Default: 1
	public static double fusion_fuel_use; // Default: 1
	public static double fusion_heat_generation; // Default: 1
	public static boolean fusion_overheat;
	public static boolean fusion_active_cooling;
	public static int fusion_update_rate;
	public static int fusion_min_size; // Default: 1
	public static int fusion_max_size; // Default: 24
	public static int fusion_electromagnet_power;
	public static boolean fusion_alternate_sound;
	
	public static double[] fusion_fuel_time;
	public static double[] fusion_power;
	public static double[] fusion_heat_variable;
	
	public static int accelerator_electromagnet_power;
	public static int accelerator_supercooler_coolant;
	
	public static int[] battery_capacity;
	
	public static int[] tool_mining_level;
	public static int[] tool_durability;
	public static double[] tool_speed;
	public static double[] tool_attack_damage;
	public static int[] tool_enchantability;
	
	public static int[] armor_durability;
	public static int[] armor_boron;
	public static int[] armor_tough;
	public static int[] armor_hard_carbon;
	public static int[] armor_boron_nitride;
	public static int[] armor_enchantability;
	public static double[] armor_toughness;
	
	public static boolean rare_drops;
	
	public static void preInit() {
		File configFile = new File(Loader.instance().getConfigDir(), "nuclearcraft.cfg");
		config = new Configuration(configFile);
		syncFromFiles();
	}
	
	public static Configuration getConfig() {
		return config;
	}
	
	public static void clientPreInit() {
		MinecraftForge.EVENT_BUS.register(new ConfigEventHandler());
	}
	
	public static void syncFromFiles() {
		syncConfig(true, true);
	}
	
	public static void syncFromGui() {
		syncConfig(false, true);
	}
	
	public static void syncFromFields() {
		syncConfig(false, false);
	}
	
	private static void syncConfig(boolean loadFromConfigFile, boolean readFieldFromConfig) {
		if (loadFromConfigFile) config.load();
		
		Property propertyOreDims = config.get(CATEGORY_ORES, "ore_dims", new int[] {0, 6, -11325, -9999}, I18n.translateToLocalFormatted("gui.config.ores.ore_dims.comment"), Integer.MIN_VALUE, Integer.MAX_VALUE);
		propertyOreDims.setLanguageKey("gui.config.ores.ore_dims");
		Property propertyOreDimsListType = config.get(CATEGORY_ORES, "ore_dims_list_type", false, I18n.translateToLocalFormatted("gui.config.ores.ore_dims_list_type.comment"));
		propertyOreDimsListType.setLanguageKey("gui.config.ores.ore_dims_list_type");
		Property propertyOreGen = config.get(CATEGORY_ORES, "ore_gen", new boolean[] {true, true, true, true, true, true, true, true}, I18n.translateToLocalFormatted("gui.config.ores.ore_gen.comment"));
		propertyOreGen.setLanguageKey("gui.config.ores.ore_gen");
		Property propertyOreSize = config.get(CATEGORY_ORES, "ore_size", new int[] {8, 8, 8, 8, 8, 8, 8, 8}, I18n.translateToLocalFormatted("gui.config.ores.ore_size.comment"), 1, Integer.MAX_VALUE);
		propertyOreSize.setLanguageKey("gui.config.ores.ore_size");
		Property propertyOreRate = config.get(CATEGORY_ORES, "ore_rate", new int[] {6, 6, 8, 4, 4, 8, 6, 4}, I18n.translateToLocalFormatted("gui.config.ores.ore_rate.comment"), 1, Integer.MAX_VALUE);
		propertyOreRate.setLanguageKey("gui.config.ores.ore_rate");
		Property propertyOreMinHeight = config.get(CATEGORY_ORES, "ore_min_height", new int[] {0, 0, 0, 0, 0, 0, 0, 0}, I18n.translateToLocalFormatted("gui.config.ores.ore_min_height.comment"), 1, 255);
		propertyOreMinHeight.setLanguageKey("gui.config.ores.ore_min_height");
		Property propertyOreMaxHeight = config.get(CATEGORY_ORES, "ore_max_height", new int[] {32, 32, 32, 32, 32, 32, 32, 32}, I18n.translateToLocalFormatted("gui.config.ores.ore_max_height.comment"), 1, 255);
		propertyOreMaxHeight.setLanguageKey("gui.config.ores.ore_max_height");
		Property propertyOreDrops = config.get(CATEGORY_ORES, "ore_drops", new boolean[] {true, true, true, true, true}, I18n.translateToLocalFormatted("gui.config.ores.ore_drops.comment"));
		propertyOreDrops.setLanguageKey("gui.config.ores.ore_drops");
		
		Property propertyProcessorTime = config.get(CATEGORY_PROCESSORS, "processor_time", new int[] {400, 800, 800, 400, 400, 600, 800, 1600, 1600, 800, 400, 600, 800, 600, 1600, 600}, I18n.translateToLocalFormatted("gui.config.processors.processor_time.comment"), 1, 128000);
		propertyProcessorTime.setLanguageKey("gui.config.processors.processor_time");
		Property propertyProcessorPower = config.get(CATEGORY_PROCESSORS, "processor_power", new int[] {20, 10, 10, 20, 10, 10, 40, 20, 40, 20, 10, 40, 10, 20, 10, 10}, I18n.translateToLocalFormatted("gui.config.processors.processor_power.comment"), 0, 16000);
		propertyProcessorPower.setLanguageKey("gui.config.processors.processor_power");
		Property propertyProcessorRFPerEU = config.get(CATEGORY_PROCESSORS, "processor_rf_per_eu", 4, I18n.translateToLocalFormatted("gui.config.processors.processor_rf_per_eu.comment"), 1, 255);
		propertyProcessorRFPerEU.setLanguageKey("gui.config.processors.processor_rf_per_eu");
		Property propertyProcessorUpdateRate = config.get(CATEGORY_PROCESSORS, "processor_update_rate", 20, I18n.translateToLocalFormatted("gui.config.processors.processor_update_rate.comment"), 1, 1200);
		propertyProcessorUpdateRate.setLanguageKey("gui.config.processors.processor_update_rate");
		Property propertyProcessorPassiveRate = config.get(CATEGORY_PROCESSORS, "processor_passive_rate", new int[] {200, 2, 400, 200}, I18n.translateToLocalFormatted("gui.config.processors.processor_passive_rate.comment"), 1, 4000);
		propertyProcessorPassiveRate.setLanguageKey("gui.config.processors.processor_passive_rate");
		Property propertyCobbleGenPower = config.get(CATEGORY_PROCESSORS, "cobble_gen_power", 0, I18n.translateToLocalFormatted("gui.config.processors.cobble_gen_power.comment"), 0, 255);
		propertyCobbleGenPower.setLanguageKey("gui.config.processors.cobble_gen_power");
		
		Property propertyRTGPower = config.get(CATEGORY_GENERATORS, "rtg_power", new int[] {4, 100, 50, 400}, I18n.translateToLocalFormatted("gui.config.generators.rtg_power.comment"), 1, Integer.MAX_VALUE);
		propertyRTGPower.setLanguageKey("gui.config.generators.rtg_power");
		Property propertySolarPower = config.get(CATEGORY_GENERATORS, "solar_power", new int[] {5}, I18n.translateToLocalFormatted("gui.config.generators.solar_power.comment"), 1, Integer.MAX_VALUE);
		propertySolarPower.setLanguageKey("gui.config.generators.solar_power");
		Property propertyGeneratorRFPerEU = config.get(CATEGORY_GENERATORS, "generator_rf_per_eu", 16, I18n.translateToLocalFormatted("gui.config.generators.generator_rf_per_eu.comment"), 1, 255);
		propertyGeneratorRFPerEU.setLanguageKey("gui.config.generators.generator_rf_per_eu");
		Property propertyGeneratorUpdateRate = config.get(CATEGORY_GENERATORS, "generator_update_rate", 20, I18n.translateToLocalFormatted("gui.config.generators.generator_update_rate.comment"), 1, 1200);
		propertyGeneratorUpdateRate.setLanguageKey("gui.config.generators.generator_update_rate");
		
		Property propertyFissionPower = config.get(CATEGORY_FISSION, "fission_power", 1D, I18n.translateToLocalFormatted("gui.config.fission.fission_power.comment"), 0D, 255D);
		propertyFissionPower.setLanguageKey("gui.config.fission.fission_power");
		Property propertyFissionFuelUse = config.get(CATEGORY_FISSION, "fission_fuel_use", 1D, I18n.translateToLocalFormatted("gui.config.fission.fission_fuel_use.comment"), 0D, 255D);
		propertyFissionFuelUse.setLanguageKey("gui.config.fission.fission_fuel_use");
		Property propertyFissionHeatGeneration = config.get(CATEGORY_FISSION, "fission_heat_generation", 1D, I18n.translateToLocalFormatted("gui.config.fission.fission_heat_generation.comment"), 0D, 255D);
		propertyFissionHeatGeneration.setLanguageKey("gui.config.fission.fission_heat_generation");
		Property propertyFissionCoolingRate = config.get(CATEGORY_FISSION, "fission_cooling_rate", new double[] {20D, 80D, 80D, 120D, 120D, 100D, 120D, 120D, 140D, 140D}, I18n.translateToLocalFormatted("gui.config.fission.fission_cooling_rate.comment"), 0D, 32767D);
		propertyFissionCoolingRate.setLanguageKey("gui.config.fission.fission_cooling_rate");
		Property propertyFissionActiveCoolingRate = config.get(CATEGORY_FISSION, "fission_active_cooling_rate", new double[] {200D, 6400D, 80000D, 16000D, 10800D, 12800D}, I18n.translateToLocalFormatted("gui.config.fission.fission_active_cooling_rate.comment"), 0.001D, 255D);
		propertyFissionActiveCoolingRate.setLanguageKey("gui.config.fission.fission_active_cooling_rate");
		Property propertyFissionWaterCoolerRequirement = config.get(CATEGORY_FISSION, "fission_water_cooler_requirement", true, I18n.translateToLocalFormatted("gui.config.fission.fission_water_cooler_requirement.comment"));
		propertyFissionWaterCoolerRequirement.setLanguageKey("gui.config.fission.fission_water_cooler_requirement");
		Property propertyFissionOverheat = config.get(CATEGORY_FISSION, "fission_overheat", true, I18n.translateToLocalFormatted("gui.config.fission.fission_overheat.comment"));
		propertyFissionOverheat.setLanguageKey("gui.config.fission.fission_overheat");
		Property propertyFissionUpdateRate = config.get(CATEGORY_FISSION, "fission_update_rate", 40, I18n.translateToLocalFormatted("gui.config.fission.fission_update_rate.comment"), 1, 1200);
		propertyFissionUpdateRate.setLanguageKey("gui.config.fission.fission_update_rate");
		Property propertyFissionMinSize = config.get(CATEGORY_FISSION, "fission_min_size", 1, I18n.translateToLocalFormatted("gui.config.fission.fission_min_size.comment"), 1, 255);
		propertyFissionMinSize.setLanguageKey("gui.config.fission.fission_min_size");
		Property propertyFissionMaxSize = config.get(CATEGORY_FISSION, "fission_max_size", 24, I18n.translateToLocalFormatted("gui.config.fission.fission_max_size.comment"), 1, 255);
		propertyFissionMaxSize.setLanguageKey("gui.config.fission.fission_max_size");
		Property propertyFissionActiveCoolerMaxRate = config.get(CATEGORY_FISSION, "fission_active_cooler_max_rate", 5, I18n.translateToLocalFormatted("gui.config.fission.fission_active_cooler_max_rate.comment"), 1, 8000);
		propertyFissionActiveCoolerMaxRate.setLanguageKey("gui.config.fission.fission_active_cooler_max_rate");
		
		Property propertyFissionThoriumFuelTime = config.get(CATEGORY_FISSION, "fission_thorium_fuel_time", new double[] {144000D, 144000D}, I18n.translateToLocalFormatted("gui.config.fission.fission_thorium_fuel_time.comment"), 1D, Double.MAX_VALUE);
		propertyFissionThoriumFuelTime.setLanguageKey("gui.config.fission.fission_thorium_fuel_time");
		Property propertyFissionThoriumPower = config.get(CATEGORY_FISSION, "fission_thorium_power", new double[] {60D, NCMath.Round(60D*1.4D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_thorium_power.comment"), 0D, 32767D);
		propertyFissionThoriumPower.setLanguageKey("gui.config.fission.fission_thorium_power");
		Property propertyFissionThoriumHeatGeneration = config.get(CATEGORY_FISSION, "fission_thorium_heat_generation", new double[] {18D, NCMath.Round(18D*1.25D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_thorium_heat_generation.comment"), 0D, 32767D);
		propertyFissionThoriumHeatGeneration.setLanguageKey("gui.config.fission.fission_thorium_heat_generation");
		
		Property propertyFissionUraniumFuelTime = config.get(CATEGORY_FISSION, "fission_uranium_fuel_time", new double[] {64000D, 64000D, 64000D, 64000D, 72000D, 72000D, 72000D, 72000D}, I18n.translateToLocalFormatted("gui.config.fission.fission_uranium_fuel_time.comment"), 1D, Double.MAX_VALUE);
		propertyFissionUraniumFuelTime.setLanguageKey("gui.config.fission.fission_uranium_fuel_time");
		Property propertyFissionUraniumPower = config.get(CATEGORY_FISSION, "fission_uranium_power", new double[] {144D, NCMath.Round(144D*1.4D, 1), 144D*4D, NCMath.Round(144D*4D*1.4D, 1), 120D, NCMath.Round(120D*1.4D, 1), 120D*4D, NCMath.Round(120D*4D*1.4D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_uranium_power.comment"), 0D, 32767D);
		propertyFissionUraniumPower.setLanguageKey("gui.config.fission.fission_uranium_power");
		Property propertyFissionUraniumHeatGeneration = config.get(CATEGORY_FISSION, "fission_uranium_heat_generation", new double[] {60D, NCMath.Round(60D*1.25D, 1), 60D*6D, NCMath.Round(60D*6D*1.25D, 1), 50D, NCMath.Round(50D*1.25D, 1), 50D*6D, NCMath.Round(50D*6D*1.25D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_uranium_heat_generation.comment"), 0D, 32767D);
		propertyFissionUraniumHeatGeneration.setLanguageKey("gui.config.fission.fission_uranium_heat_generation");
		
		Property propertyFissionNeptuniumFuelTime = config.get(CATEGORY_FISSION, "fission_neptunium_fuel_time", new double[] {102000D, 102000D, 102000D, 102000D}, I18n.translateToLocalFormatted("gui.config.fission.fission_neptunium_fuel_time.comment"), 1D, Double.MAX_VALUE);
		propertyFissionNeptuniumFuelTime.setLanguageKey("gui.config.fission.fission_neptunium_fuel_time");
		Property propertyFissionNeptuniumPower = config.get(CATEGORY_FISSION, "fission_neptunium_power", new double[] {90D, NCMath.Round(90D*1.4D, 1), 90D*4D, NCMath.Round(90D*4D*1.4D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_neptunium_power.comment"), 0D, 32767D);
		propertyFissionNeptuniumPower.setLanguageKey("gui.config.fission.fission_neptunium_power");
		Property propertyFissionNeptuniumHeatGeneration = config.get(CATEGORY_FISSION, "fission_neptunium_heat_generation", new double[] {36D, NCMath.Round(36D*1.25D, 1), 36D*6D, NCMath.Round(36D*6D*1.25D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_neptunium_heat_generation.comment"), 0D, 32767D);
		propertyFissionNeptuniumHeatGeneration.setLanguageKey("gui.config.fission.fission_neptunium_heat_generation");
		
		Property propertyFissionPlutoniumFuelTime = config.get(CATEGORY_FISSION, "fission_plutonium_fuel_time", new double[] {92000D, 92000D, 92000D, 92000D, 60000D, 60000D, 60000D, 60000D}, I18n.translateToLocalFormatted("gui.config.fission.fission_plutonium_fuel_time.comment"), 1D, Double.MAX_VALUE);
		propertyFissionPlutoniumFuelTime.setLanguageKey("gui.config.fission.fission_plutonium_fuel_time");
		Property propertyFissionPlutoniumPower = config.get(CATEGORY_FISSION, "fission_plutonium_power", new double[] {105D, NCMath.Round(105D*1.4D, 1), 105D*4D, NCMath.Round(105D*4D*1.4D, 1), 165D, NCMath.Round(165D*1.4D, 1), 165D*4D, NCMath.Round(165D*4D*1.4D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_plutonium_power.comment"), 0D, 32767D);
		propertyFissionPlutoniumPower.setLanguageKey("gui.config.fission.fission_plutonium_power");
		Property propertyFissionPlutoniumHeatGeneration = config.get(CATEGORY_FISSION, "fission_plutonium_heat_generation", new double[] {40D, NCMath.Round(40D*1.25D, 1), 40D*6D, NCMath.Round(40D*6D*1.25D, 1), 70D, NCMath.Round(70D*1.25D, 1), 70D*6D, NCMath.Round(70D*6D*1.25D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_plutonium_heat_generation.comment"), 0D, 32767D);
		propertyFissionPlutoniumHeatGeneration.setLanguageKey("gui.config.fission.fission_plutonium_heat_generation");
		
		Property propertyFissionMOXFuelTime = config.get(CATEGORY_FISSION, "fission_mox_fuel_time", new double[] {84000D, 56000D}, I18n.translateToLocalFormatted("gui.config.fission.fission_mox_fuel_time.comment"), 1D, Double.MAX_VALUE);
		propertyFissionMOXFuelTime.setLanguageKey("gui.config.fission.fission_mox_fuel_time");
		Property propertyFissionMOXPower = config.get(CATEGORY_FISSION, "fission_mox_power", new double[] {NCMath.Round(111D*1.4D, 1), NCMath.Round(174D*1.4D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_mox_power.comment"), 0D, 32767D);
		propertyFissionMOXPower.setLanguageKey("gui.config.fission.fission_mox_power");
		Property propertyFissionMOXHeatGeneration = config.get(CATEGORY_FISSION, "fission_mox_heat_generation", new double[] {NCMath.Round(46D*1.25D, 1), NCMath.Round(78D*1.25D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_mox_heat_generation.comment"), 0D, 32767D);
		propertyFissionMOXHeatGeneration.setLanguageKey("gui.config.fission.fission_mox_heat_generation");
		
		Property propertyFissionAmericiumFuelTime = config.get(CATEGORY_FISSION, "fission_americium_fuel_time", new double[] {54000D, 54000D, 54000D, 54000D}, I18n.translateToLocalFormatted("gui.config.fission.fission_americium_fuel_time.comment"), 1D, Double.MAX_VALUE);
		propertyFissionAmericiumFuelTime.setLanguageKey("gui.config.fission.fission_americium_fuel_time");
		Property propertyFissionAmericiumPower = config.get(CATEGORY_FISSION, "fission_americium_power", new double[] {192D, NCMath.Round(192D*1.4D, 1), 192D*4D, NCMath.Round(192D*4D*1.4D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_americium_power.comment"), 0D, 32767D);
		propertyFissionAmericiumPower.setLanguageKey("gui.config.fission.fission_americium_power");
		Property propertyFissionAmericiumHeatGeneration = config.get(CATEGORY_FISSION, "fission_americium_heat_generation", new double[] {94D, NCMath.Round(94D*1.25D, 1), 94D*6D, NCMath.Round(94D*6D*1.25D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_americium_heat_generation.comment"), 0D, 32767D);
		propertyFissionAmericiumHeatGeneration.setLanguageKey("gui.config.fission.fission_americium_heat_generation");
		
		Property propertyFissionCuriumFuelTime = config.get(CATEGORY_FISSION, "fission_curium_fuel_time", new double[] {52000D, 52000D, 52000D, 52000D, 68000D, 68000D, 68000D, 68000D, 78000D, 78000D, 78000D, 78000D}, I18n.translateToLocalFormatted("gui.config.fission.fission_curium_fuel_time.comment"), 1D, Double.MAX_VALUE);
		propertyFissionCuriumFuelTime.setLanguageKey("gui.config.fission.fission_curium_fuel_time");
		Property propertyFissionCuriumPower = config.get(CATEGORY_FISSION, "fission_curium_power", new double[] {210D, NCMath.Round(210D*1.4D, 1), 210D*4D, NCMath.Round(210D*4D*1.4D, 1), 162D, NCMath.Round(162D*1.4D, 1), 162D*4D, NCMath.Round(162D*4D*1.4D, 1), 138D, NCMath.Round(138D*1.4D, 1), 138D*4D, NCMath.Round(138D*4D*1.4D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_curium_power.comment"), 0D, 32767D);
		propertyFissionCuriumPower.setLanguageKey("gui.config.fission.fission_curium_power");
		Property propertyFissionCuriumHeatGeneration = config.get(CATEGORY_FISSION, "fission_curium_heat_generation", new double[] {112D, NCMath.Round(112D*1.25D, 1), 112D*6D, NCMath.Round(112D*6D*1.25D, 1), 68D, NCMath.Round(68D*1.25D, 1), 68D*6D, NCMath.Round(68D*6D*1.25D, 1), 54D, NCMath.Round(54D*1.25D, 1), 54D*6D, NCMath.Round(54D*6D*1.25D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_curium_heat_generation.comment"), 0D, 32767D);
		propertyFissionCuriumHeatGeneration.setLanguageKey("gui.config.fission.fission_curium_heat_generation");
		
		Property propertyFissionBerkeliumFuelTime = config.get(CATEGORY_FISSION, "fission_berkelium_fuel_time", new double[] {86000D, 86000D, 86000D, 86000D}, I18n.translateToLocalFormatted("gui.config.fission.fission_berkelium_fuel_time.comment"), 1D, Double.MAX_VALUE);
		propertyFissionBerkeliumFuelTime.setLanguageKey("gui.config.fission.fission_berkelium_fuel_time");
		Property propertyFissionBerkeliumPower = config.get(CATEGORY_FISSION, "fission_berkelium_power", new double[] {135D, NCMath.Round(135D*1.4D, 1), 135D*4D, NCMath.Round(135D*4D*1.4D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_berkelium_power.comment"), 0D, 32767D);
		propertyFissionBerkeliumPower.setLanguageKey("gui.config.fission.fission_berkelium_power");
		Property propertyFissionBerkeliumHeatGeneration = config.get(CATEGORY_FISSION, "fission_berkelium_heat_generation", new double[] {52D, NCMath.Round(52D*1.25D, 1), 52D*6D, NCMath.Round(52D*6D*1.25D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_berkelium_heat_generation.comment"), 0D, 32767D);
		propertyFissionBerkeliumHeatGeneration.setLanguageKey("gui.config.fission.fission_berkelium_heat_generation");
		
		Property propertyFissionCaliforniumFuelTime = config.get(CATEGORY_FISSION, "fission_californium_fuel_time", new double[] {60000D, 60000D, 60000D, 60000D, 58000D, 58000D, 58000D, 58000D}, I18n.translateToLocalFormatted("gui.config.fission.fission_californium_fuel_time.comment"), 1D, Double.MAX_VALUE);
		propertyFissionCaliforniumFuelTime.setLanguageKey("gui.config.fission.fission_californium_fuel_time");
		Property propertyFissionCaliforniumPower = config.get(CATEGORY_FISSION, "fission_californium_power", new double[] {216D, NCMath.Round(216D*1.4D, 1), 216D*4D, NCMath.Round(216D*4D*1.4D, 1), 225D, NCMath.Round(225D*1.4D, 1), 225D*4D, NCMath.Round(225D*4D*1.4D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_californium_power.comment"), 0D, 32767D);
		propertyFissionCaliforniumPower.setLanguageKey("gui.config.fission.fission_californium_power");
		Property propertyFissionCaliforniumHeatGeneration = config.get(CATEGORY_FISSION, "fission_californium_heat_generation", new double[] {116D, NCMath.Round(116D*1.25D, 1), 116D*6D, NCMath.Round(116D*6D*1.25D, 1), 120D, NCMath.Round(120D*1.25D, 1), 120D*6D, NCMath.Round(120D*6D*1.25D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_californium_heat_generation.comment"), 0D, 32767D);
		propertyFissionCaliforniumHeatGeneration.setLanguageKey("gui.config.fission.fission_californium_heat_generation");
		
		Property propertyFusionBasePower = config.get(CATEGORY_FUSION, "fusion_base_power", 1D, I18n.translateToLocalFormatted("gui.config.fusion.fusion_base_power.comment"), 0D, 255D);
		propertyFusionBasePower.setLanguageKey("gui.config.fusion.fusion_base_power");
		Property propertyFusionFuelUse = config.get(CATEGORY_FUSION, "fusion_fuel_use", 1D, I18n.translateToLocalFormatted("gui.config.fusion.fusion_fuel_use.comment"), 0.001D, 255D);
		propertyFusionFuelUse.setLanguageKey("gui.config.fusion.fusion_fuel_use");
		Property propertyFusionHeatGeneration = config.get(CATEGORY_FUSION, "fusion_heat_generation", 1D, I18n.translateToLocalFormatted("gui.config.fusion.fusion_heat_generation.comment"), 0D, 255D);
		propertyFusionHeatGeneration.setLanguageKey("gui.config.fusion.fusion_heat_generation");
		Property propertyFusionOverheat = config.get(CATEGORY_FUSION, "fusion_overheat", true, I18n.translateToLocalFormatted("gui.config.fusion.fusion_overheat.comment"));
		propertyFusionOverheat.setLanguageKey("gui.config.fusion.fusion_overheat");
		Property propertyFusionActiveCooling = config.get(CATEGORY_FUSION, "fusion_active_cooling", true, I18n.translateToLocalFormatted("gui.config.fusion.fusion_active_cooling.comment"));
		propertyFusionActiveCooling.setLanguageKey("gui.config.fusion.fusion_active_cooling");
		Property propertyFusionUpdateRate = config.get(CATEGORY_FUSION, "fusion_update_rate", 40, I18n.translateToLocalFormatted("gui.config.fusion.fusion_update_rate.comment"), 1, 1200);
		propertyFusionUpdateRate.setLanguageKey("gui.config.fusion.fusion_update_rate");
		Property propertyFusionMinSize = config.get(CATEGORY_FUSION, "fusion_min_size", 1, I18n.translateToLocalFormatted("gui.config.fusion.fusion_min_size.comment"), 1, 255);
		propertyFusionMinSize.setLanguageKey("gui.config.fusion.fusion_min_size");
		Property propertyFusionMaxSize = config.get(CATEGORY_FUSION, "fusion_max_size", 24, I18n.translateToLocalFormatted("gui.config.fusion.fusion_max_size.comment"), 1, 255);
		propertyFusionMaxSize.setLanguageKey("gui.config.fusion.fusion_max_size");
		Property propertyFusionElectromagnetPower = config.get(CATEGORY_FUSION, "fusion_electromagnet_power", 1000, I18n.translateToLocalFormatted("gui.config.fusion.fusion_electromagnet_power.comment"), 0, Integer.MAX_VALUE);
		propertyFusionElectromagnetPower.setLanguageKey("gui.config.fusion.fusion_electromagnet_power");
		Property propertyFusionAlternateSound = config.get(CATEGORY_FUSION, "fusion_alternate_sound", false, I18n.translateToLocalFormatted("gui.config.fusion.fusion_alternate_sound.comment"));
		propertyFusionAlternateSound.setLanguageKey("gui.config.fusion.fusion_alternate_sound");
		
		Property propertyFusionFuelTime = config.get(CATEGORY_FUSION, "fusion_fuel_time", new double[] {100D, 208.3D, 312.5D, 312.5D, 1250D, 1250D, 625D, 312.5D, 156.3D, 500D, 1250D, 500D, 2500D, 833.3D, 1250D, 1250D, 6250D, 3125D, 833.3D, 2500D, 625D, 1250D, 2500D, 2500D, 5000D, 5000D, 2500D, 5000D}, I18n.translateToLocalFormatted("gui.config.fusion.fusion_fuel_time.comment"), 1D, 32767D);
		propertyFusionFuelTime.setLanguageKey("gui.config.fusion.fusion_fuel_time");
		Property propertyFusionPower = config.get(CATEGORY_FUSION, "fusion_power", new double[] {640D, 480D, 160D, 160D, 640D, 240D, 960D, 1120D, 1600D, 1280D, 160D, 1200D, 80D, 480D, 320D, 80D, 40D, 80D, 960D, 40D, 1120D, 240D, 80D, 40D, 40D, 40D, 40D, 20D}, I18n.translateToLocalFormatted("gui.config.fusion.fusion_power.comment"), 0D, 32767D);
		propertyFusionPower.setLanguageKey("gui.config.fusion.fusion_power");
		Property propertyFusionHeatVariable = config.get(CATEGORY_FUSION, "fusion_heat_variable", new double[] {2140D, 1380D, 4700D, 4820D, 5660, 4550D, 4640D, 4780D, 670D, 2370D, 5955D, 5335D, 7345D, 3875D, 5070D, 7810D, 7510D, 8060D, 6800D, 8060D, 8800D, 12500D, 8500D, 9200D, 13000D, 12000D, 11000D, 14000D}, I18n.translateToLocalFormatted("gui.config.fusion.fusion_heat_variable.comment"), 500D, 20000D);
		propertyFusionHeatVariable.setLanguageKey("gui.config.fusion.fusion_heat_variable");
		
		Property propertyAcceleratorElectromagnetPower = config.get(CATEGORY_ACCELERATOR, "accelerator_electromagnet_power", 5000, I18n.translateToLocalFormatted("gui.config.accelerator.accelerator_electromagnet_power.comment"), 0, Integer.MAX_VALUE);
		propertyAcceleratorElectromagnetPower.setLanguageKey("gui.config.accelerator.accelerator_electromagnet_power");
		Property propertyAcceleratorSupercoolerCoolant = config.get(CATEGORY_ACCELERATOR, "accelerator_supercooler_coolant", 1, I18n.translateToLocalFormatted("gui.config.accelerator.accelerator_supercooler_coolant.comment"), 0, 32767);
		propertyAcceleratorSupercoolerCoolant.setLanguageKey("gui.config.accelerator.accelerator_supercooler_coolant");
		
		Property propertyBatteryCapacity = config.get(CATEGORY_ENERGY_STORAGE, "battery_capacity", new int[] {1600000, 64000000}, I18n.translateToLocalFormatted("gui.config.energy_storage.battery_capacity.comment"), 1, Integer.MAX_VALUE);
		propertyBatteryCapacity.setLanguageKey("gui.config.energy_storage.battery_capacity");
		
		Property propertyToolMiningLevel = config.get(CATEGORY_TOOLS, "tool_mining_level", new int[] {2, 2, 3, 3, 3, 3, 3, 3}, I18n.translateToLocalFormatted("gui.config.tools.tool_mining_level.comment"), 1, 15);
		propertyToolMiningLevel.setLanguageKey("gui.config.tools.tool_mining_level");
		Property propertyToolDurability = config.get(CATEGORY_TOOLS, "tool_durability", new int[] {547, 547*5, 929, 929*5, 1245, 1245*5, 1928, 1928*5}, I18n.translateToLocalFormatted("gui.config.tools.tool_durability.comment"), 1, 32767);
		propertyToolDurability.setLanguageKey("gui.config.tools.tool_durability");
		Property propertyToolSpeed = config.get(CATEGORY_TOOLS, "tool_speed", new double[] {8D, 8D, 10D, 10D, 11D, 11D, 12D, 12D}, I18n.translateToLocalFormatted("gui.config.tools.tool_speed.comment"), 1D, 255D);
		propertyToolSpeed.setLanguageKey("gui.config.tools.tool_speed");
		Property propertyToolAttackDamage = config.get(CATEGORY_TOOLS, "tool_attack_damage", new double[] {2.5D, 2.5D, 3D, 3D, 3D, 3D, 3.5D, 3.5D}, I18n.translateToLocalFormatted("gui.config.tools.tool_attack_damage.comment"), 0D, 255D);
		propertyToolAttackDamage.setLanguageKey("gui.config.tools.tool_attack_damage");
		Property propertyToolEnchantability = config.get(CATEGORY_TOOLS, "tool_enchantability", new int[] {6, 6, 15, 15, 12, 12, 20, 20}, I18n.translateToLocalFormatted("gui.config.tools.tool_enchantability.comment"), 1, 255);
		propertyToolEnchantability.setLanguageKey("gui.config.tools.tool_enchantability");
		
		Property propertyArmorDurability = config.get(CATEGORY_ARMOR, "armor_durability", new int[] {22, 30, 34, 42}, I18n.translateToLocalFormatted("gui.config.armor.armor_durability.comment"), 1, 127);
		propertyArmorDurability.setLanguageKey("gui.config.armor.armor_durability");
		Property propertyArmorBoron = config.get(CATEGORY_ARMOR, "armor_boron", new int[] {2, 5, 7, 3}, I18n.translateToLocalFormatted("gui.config.armor.armor_boron.comment"), 1, 25);
		propertyArmorBoron.setLanguageKey("gui.config.armor.armor_boron");
		Property propertyArmorTough = config.get(CATEGORY_ARMOR, "armor_tough", new int[] {3, 6, 7, 3}, I18n.translateToLocalFormatted("gui.config.armor.armor_tough.comment"), 1, 25);
		propertyArmorTough.setLanguageKey("gui.config.armor.armor_tough");
		Property propertyArmorHardCarbon = config.get(CATEGORY_ARMOR, "armor_hard_carbon", new int[] {3, 5, 7, 3}, I18n.translateToLocalFormatted("gui.config.armor.armor_hard_carbon.comment"), 1, 25);
		propertyArmorHardCarbon.setLanguageKey("gui.config.armor.armor_hard_carbon");
		Property propertyArmorBoronNitride = config.get(CATEGORY_ARMOR, "armor_boron_nitride", new int[] {3, 6, 8, 3}, I18n.translateToLocalFormatted("gui.config.armor.armor_boron_nitride.comment"), 1, 25);
		propertyArmorBoronNitride.setLanguageKey("gui.config.armor.armor_boron_nitride");
		Property propertyArmorEnchantability = config.get(CATEGORY_ARMOR, "armor_enchantability", new int[] {6, 15, 12, 20}, I18n.translateToLocalFormatted("gui.config.armor.armor_enchantability.comment"), 1, 255);
		propertyArmorEnchantability.setLanguageKey("gui.config.armor.armor_enchantability");
		Property propertyArmorToughness = config.get(CATEGORY_ARMOR, "armor_toughness", new double[] {1D, 2D, 1D, 2D}, I18n.translateToLocalFormatted("gui.config.armor.armor_toughness.comment"), 1, 8);
		propertyArmorToughness.setLanguageKey("gui.config.armor.armor_toughness");
		
		Property propertyRareDrops = config.get(CATEGORY_OTHER, "rare_drops", false, I18n.translateToLocalFormatted("gui.config.other.rare_drops.comment"));
		propertyRareDrops.setLanguageKey("gui.config.other.rare_drops");
		
		List<String> propertyOrderOres = new ArrayList<String>();
		propertyOrderOres.add(propertyOreDims.getName());
		propertyOrderOres.add(propertyOreDimsListType.getName());
		propertyOrderOres.add(propertyOreGen.getName());
		propertyOrderOres.add(propertyOreSize.getName());
		propertyOrderOres.add(propertyOreRate.getName());
		propertyOrderOres.add(propertyOreMinHeight.getName());
		propertyOrderOres.add(propertyOreMaxHeight.getName());
		propertyOrderOres.add(propertyOreDrops.getName());
		config.setCategoryPropertyOrder(CATEGORY_ORES, propertyOrderOres);
		
		List<String> propertyOrderProcessors = new ArrayList<String>();
		propertyOrderProcessors.add(propertyProcessorTime.getName());
		propertyOrderProcessors.add(propertyProcessorPower.getName());
		propertyOrderProcessors.add(propertyProcessorRFPerEU.getName());
		propertyOrderProcessors.add(propertyProcessorUpdateRate.getName());
		propertyOrderProcessors.add(propertyProcessorPassiveRate.getName());
		propertyOrderProcessors.add(propertyCobbleGenPower.getName());
		config.setCategoryPropertyOrder(CATEGORY_PROCESSORS, propertyOrderProcessors);
		
		List<String> propertyOrderGenerators = new ArrayList<String>();
		propertyOrderGenerators.add(propertyRTGPower.getName());
		propertyOrderGenerators.add(propertySolarPower.getName());
		propertyOrderGenerators.add(propertyGeneratorRFPerEU.getName());
		propertyOrderGenerators.add(propertyGeneratorUpdateRate.getName());
		config.setCategoryPropertyOrder(CATEGORY_GENERATORS, propertyOrderGenerators);
		
		List<String> propertyOrderFission = new ArrayList<String>();
		propertyOrderFission.add(propertyFissionPower.getName());
		propertyOrderFission.add(propertyFissionFuelUse.getName());
		propertyOrderFission.add(propertyFissionHeatGeneration.getName());
		propertyOrderFission.add(propertyFissionCoolingRate.getName());
		propertyOrderFission.add(propertyFissionActiveCoolingRate.getName());
		propertyOrderFission.add(propertyFissionWaterCoolerRequirement.getName());
		propertyOrderFission.add(propertyFissionOverheat.getName());
		propertyOrderFission.add(propertyFissionMinSize.getName());
		propertyOrderFission.add(propertyFissionMaxSize.getName());
		propertyOrderFission.add(propertyFissionActiveCoolerMaxRate.getName());
		
		propertyOrderFission.add(propertyFissionThoriumFuelTime.getName());
		propertyOrderFission.add(propertyFissionThoriumPower.getName());
		propertyOrderFission.add(propertyFissionThoriumHeatGeneration.getName());
		
		propertyOrderFission.add(propertyFissionUraniumFuelTime.getName());
		propertyOrderFission.add(propertyFissionUraniumPower.getName());
		propertyOrderFission.add(propertyFissionUraniumHeatGeneration.getName());
		
		propertyOrderFission.add(propertyFissionNeptuniumFuelTime.getName());
		propertyOrderFission.add(propertyFissionNeptuniumPower.getName());
		propertyOrderFission.add(propertyFissionNeptuniumHeatGeneration.getName());
		
		propertyOrderFission.add(propertyFissionPlutoniumFuelTime.getName());
		propertyOrderFission.add(propertyFissionPlutoniumPower.getName());
		propertyOrderFission.add(propertyFissionPlutoniumHeatGeneration.getName());
		
		propertyOrderFission.add(propertyFissionMOXFuelTime.getName());
		propertyOrderFission.add(propertyFissionMOXPower.getName());
		propertyOrderFission.add(propertyFissionMOXHeatGeneration.getName());
		
		propertyOrderFission.add(propertyFissionAmericiumFuelTime.getName());
		propertyOrderFission.add(propertyFissionAmericiumPower.getName());
		propertyOrderFission.add(propertyFissionAmericiumHeatGeneration.getName());
		
		propertyOrderFission.add(propertyFissionCuriumFuelTime.getName());
		propertyOrderFission.add(propertyFissionCuriumPower.getName());
		propertyOrderFission.add(propertyFissionCuriumHeatGeneration.getName());
		
		propertyOrderFission.add(propertyFissionBerkeliumFuelTime.getName());
		propertyOrderFission.add(propertyFissionBerkeliumPower.getName());
		propertyOrderFission.add(propertyFissionBerkeliumHeatGeneration.getName());
		
		propertyOrderFission.add(propertyFissionCaliforniumFuelTime.getName());
		propertyOrderFission.add(propertyFissionCaliforniumPower.getName());
		propertyOrderFission.add(propertyFissionCaliforniumHeatGeneration.getName());
		config.setCategoryPropertyOrder(CATEGORY_FISSION, propertyOrderFission);
		
		List<String> propertyOrderEnergyStorage = new ArrayList<String>();
		propertyOrderEnergyStorage.add(propertyBatteryCapacity.getName());
		config.setCategoryPropertyOrder(CATEGORY_ENERGY_STORAGE, propertyOrderEnergyStorage);
		
		List<String> propertyOrderFusion = new ArrayList<String>();
		propertyOrderFusion.add(propertyFusionBasePower.getName());
		propertyOrderFusion.add(propertyFusionFuelUse.getName());
		propertyOrderFusion.add(propertyFusionHeatGeneration.getName());
		propertyOrderFusion.add(propertyFusionOverheat.getName());
		propertyOrderFusion.add(propertyFusionActiveCooling.getName());
		propertyOrderFusion.add(propertyFusionMinSize.getName());
		propertyOrderFusion.add(propertyFusionMaxSize.getName());
		propertyOrderFusion.add(propertyFusionElectromagnetPower.getName());
		propertyOrderFusion.add(propertyFusionAlternateSound.getName());
		
		propertyOrderFusion.add(propertyFusionFuelTime.getName());
		propertyOrderFusion.add(propertyFusionPower.getName());
		propertyOrderFusion.add(propertyFusionHeatVariable.getName());
		config.setCategoryPropertyOrder(CATEGORY_FUSION, propertyOrderFusion);
		
		List<String> propertyOrderAccelerator = new ArrayList<String>();
		propertyOrderAccelerator.add(propertyAcceleratorElectromagnetPower.getName());
		propertyOrderAccelerator.add(propertyAcceleratorSupercoolerCoolant.getName());
		config.setCategoryPropertyOrder(CATEGORY_ACCELERATOR, propertyOrderAccelerator);
		
		List<String> propertyOrderTools = new ArrayList<String>();
		propertyOrderTools.add(propertyToolMiningLevel.getName());
		propertyOrderTools.add(propertyToolDurability.getName());
		propertyOrderTools.add(propertyToolSpeed.getName());
		propertyOrderTools.add(propertyToolAttackDamage.getName());
		propertyOrderTools.add(propertyToolEnchantability.getName());
		config.setCategoryPropertyOrder(CATEGORY_TOOLS, propertyOrderTools);
		
		List<String> propertyOrderArmor = new ArrayList<String>();
		propertyOrderArmor.add(propertyArmorDurability.getName());
		propertyOrderArmor.add(propertyArmorEnchantability.getName());
		propertyOrderArmor.add(propertyArmorBoron.getName());
		propertyOrderArmor.add(propertyArmorTough.getName());
		propertyOrderArmor.add(propertyArmorHardCarbon.getName());
		propertyOrderArmor.add(propertyArmorBoronNitride.getName());
		propertyOrderArmor.add(propertyArmorToughness.getName());
		config.setCategoryPropertyOrder(CATEGORY_ARMOR, propertyOrderArmor);
		
		List<String> propertyOrderOther = new ArrayList<String>();
		propertyOrderOther.add(propertyRareDrops.getName());
		config.setCategoryPropertyOrder(CATEGORY_OTHER, propertyOrderOther);
		
		if(readFieldFromConfig) {
			ore_dims = propertyOreDims.getIntList();
			ore_dims_list_type = propertyOreDimsListType.getBoolean();
			ore_gen = readBooleanArrayFromConfig(propertyOreGen);
			ore_size = readIntegerArrayFromConfig(propertyOreSize);
			ore_rate = readIntegerArrayFromConfig(propertyOreRate);
			ore_min_height = readIntegerArrayFromConfig(propertyOreMinHeight);
			ore_max_height = readIntegerArrayFromConfig(propertyOreMaxHeight);
			ore_drops = readBooleanArrayFromConfig(propertyOreDrops);
			
			processor_time = readIntegerArrayFromConfig(propertyProcessorTime);
			processor_power = readIntegerArrayFromConfig(propertyProcessorPower);
			processor_rf_per_eu = propertyProcessorRFPerEU.getInt();
			processor_update_rate = propertyProcessorUpdateRate.getInt();
			processor_passive_rate = readIntegerArrayFromConfig(propertyProcessorPassiveRate);
			cobble_gen_power = propertyCobbleGenPower.getInt();
			
			rtg_power = readIntegerArrayFromConfig(propertyRTGPower);
			solar_power = readIntegerArrayFromConfig(propertySolarPower);
			generator_rf_per_eu = propertyGeneratorRFPerEU.getInt();
			generator_update_rate = propertyGeneratorUpdateRate.getInt();
			
			fission_power = propertyFissionPower.getDouble();
			fission_fuel_use = propertyFissionFuelUse.getDouble();
			fission_heat_generation = propertyFissionHeatGeneration.getDouble();
			fission_cooling_rate = readDoubleArrayFromConfig(propertyFissionCoolingRate);
			fission_active_cooling_rate = readDoubleArrayFromConfig(propertyFissionActiveCoolingRate);
			fission_water_cooler_requirement = propertyFissionWaterCoolerRequirement.getBoolean();
			fission_overheat = propertyFissionOverheat.getBoolean();
			fission_update_rate = propertyFissionUpdateRate.getInt();
			fission_min_size = propertyFissionMinSize.getInt();
			fission_max_size = propertyFissionMaxSize.getInt();
			fission_active_cooler_max_rate = propertyFissionActiveCoolerMaxRate.getInt();
			
			fission_thorium_fuel_time = readDoubleArrayFromConfig(propertyFissionThoriumFuelTime);
			fission_thorium_power = readDoubleArrayFromConfig(propertyFissionThoriumPower);
			fission_thorium_heat_generation = readDoubleArrayFromConfig(propertyFissionThoriumHeatGeneration);
			
			fission_uranium_fuel_time = readDoubleArrayFromConfig(propertyFissionUraniumFuelTime);
			fission_uranium_power = readDoubleArrayFromConfig(propertyFissionUraniumPower);
			fission_uranium_heat_generation = readDoubleArrayFromConfig(propertyFissionUraniumHeatGeneration);
			
			fission_neptunium_fuel_time = readDoubleArrayFromConfig(propertyFissionNeptuniumFuelTime);
			fission_neptunium_power = readDoubleArrayFromConfig(propertyFissionNeptuniumPower);
			fission_neptunium_heat_generation = readDoubleArrayFromConfig(propertyFissionNeptuniumHeatGeneration);
			
			fission_plutonium_fuel_time = readDoubleArrayFromConfig(propertyFissionPlutoniumFuelTime);
			fission_plutonium_power = readDoubleArrayFromConfig(propertyFissionPlutoniumPower);
			fission_plutonium_heat_generation = readDoubleArrayFromConfig(propertyFissionPlutoniumHeatGeneration);
			
			fission_mox_fuel_time = readDoubleArrayFromConfig(propertyFissionMOXFuelTime);
			fission_mox_power = readDoubleArrayFromConfig(propertyFissionMOXPower);
			fission_mox_heat_generation = readDoubleArrayFromConfig(propertyFissionMOXHeatGeneration);
			
			fission_americium_fuel_time = readDoubleArrayFromConfig(propertyFissionAmericiumFuelTime);
			fission_americium_power = readDoubleArrayFromConfig(propertyFissionAmericiumPower);
			fission_americium_heat_generation = readDoubleArrayFromConfig(propertyFissionAmericiumHeatGeneration);
			
			fission_curium_fuel_time = readDoubleArrayFromConfig(propertyFissionCuriumFuelTime);
			fission_curium_power = readDoubleArrayFromConfig(propertyFissionCuriumPower);
			fission_curium_heat_generation = readDoubleArrayFromConfig(propertyFissionCuriumHeatGeneration);
			
			fission_berkelium_fuel_time = readDoubleArrayFromConfig(propertyFissionBerkeliumFuelTime);
			fission_berkelium_power = readDoubleArrayFromConfig(propertyFissionBerkeliumPower);
			fission_berkelium_heat_generation = readDoubleArrayFromConfig(propertyFissionBerkeliumHeatGeneration);
			
			fission_californium_fuel_time = readDoubleArrayFromConfig(propertyFissionCaliforniumFuelTime);
			fission_californium_power = readDoubleArrayFromConfig(propertyFissionCaliforniumPower);
			fission_californium_heat_generation = readDoubleArrayFromConfig(propertyFissionCaliforniumHeatGeneration);
			
			fusion_base_power = propertyFusionBasePower.getDouble();
			fusion_fuel_use = propertyFusionFuelUse.getDouble();
			fusion_heat_generation = propertyFusionHeatGeneration.getDouble();
			fusion_overheat = propertyFusionOverheat.getBoolean();
			fusion_active_cooling = propertyFusionActiveCooling.getBoolean();
			fusion_update_rate = propertyFusionUpdateRate.getInt();
			fusion_min_size = propertyFusionMinSize.getInt();
			fusion_max_size = propertyFusionMaxSize.getInt();
			fusion_electromagnet_power = propertyFusionElectromagnetPower.getInt();
			fusion_alternate_sound = propertyFusionAlternateSound.getBoolean();
			
			fusion_fuel_time = readDoubleArrayFromConfig(propertyFusionFuelTime);
			fusion_power = readDoubleArrayFromConfig(propertyFusionPower);
			fusion_heat_variable = readDoubleArrayFromConfig(propertyFusionHeatVariable);
			
			accelerator_electromagnet_power = propertyAcceleratorElectromagnetPower.getInt();
			accelerator_supercooler_coolant = propertyAcceleratorSupercoolerCoolant.getInt();
			
			battery_capacity = readIntegerArrayFromConfig(propertyBatteryCapacity);
			
			tool_mining_level = readIntegerArrayFromConfig(propertyToolMiningLevel);
			tool_durability = readIntegerArrayFromConfig(propertyToolDurability);
			tool_speed = readDoubleArrayFromConfig(propertyToolSpeed);
			tool_attack_damage = readDoubleArrayFromConfig(propertyToolAttackDamage);
			tool_enchantability = readIntegerArrayFromConfig(propertyToolEnchantability);
			
			armor_durability = readIntegerArrayFromConfig(propertyArmorDurability);
			armor_enchantability = readIntegerArrayFromConfig(propertyArmorEnchantability);
			armor_boron = readIntegerArrayFromConfig(propertyArmorBoron);
			armor_tough = readIntegerArrayFromConfig(propertyArmorTough);
			armor_hard_carbon = readIntegerArrayFromConfig(propertyArmorHardCarbon);
			armor_boron_nitride = readIntegerArrayFromConfig(propertyArmorBoronNitride);
			armor_toughness = readDoubleArrayFromConfig(propertyArmorToughness);
			
			rare_drops = propertyRareDrops.getBoolean();
		}
		
		propertyOreDims.set(ore_dims);
		propertyOreDimsListType.set(ore_dims_list_type);
		propertyOreGen.set(ore_gen);
		propertyOreSize.set(ore_size);
		propertyOreRate.set(ore_rate);
		propertyOreMinHeight.set(ore_min_height);
		propertyOreMaxHeight.set(ore_max_height);
		propertyOreDrops.set(ore_drops);
		
		propertyProcessorTime.set(processor_time);
		propertyProcessorPower.set(processor_power);
		propertyProcessorRFPerEU.set(processor_rf_per_eu);
		propertyProcessorUpdateRate.set(processor_update_rate);
		propertyProcessorPassiveRate.set(processor_passive_rate);
		propertyCobbleGenPower.set(cobble_gen_power);
		
		propertyRTGPower.set(rtg_power);
		propertySolarPower.set(solar_power);
		propertyGeneratorRFPerEU.set(generator_rf_per_eu);
		propertyGeneratorUpdateRate.set(generator_update_rate);
		
		propertyFissionPower.set(fission_power);
		propertyFissionFuelUse.set(fission_fuel_use);
		propertyFissionHeatGeneration.set(fission_heat_generation);
		propertyFissionCoolingRate.set(fission_cooling_rate);
		propertyFissionActiveCoolingRate.set(fission_active_cooling_rate);
		propertyFissionWaterCoolerRequirement.set(fission_water_cooler_requirement);
		propertyFissionOverheat.set(fission_overheat);
		propertyFissionUpdateRate.set(fission_update_rate);
		propertyFissionMinSize.set(fission_min_size);
		propertyFissionMaxSize.set(fission_max_size);
		propertyFissionActiveCoolerMaxRate.set(fission_active_cooler_max_rate);
		
		propertyFissionThoriumFuelTime.set(fission_thorium_fuel_time);
		propertyFissionThoriumPower.set(fission_thorium_power);
		propertyFissionThoriumHeatGeneration.set(fission_thorium_heat_generation);
		
		propertyFissionUraniumFuelTime.set(fission_uranium_fuel_time);
		propertyFissionUraniumPower.set(fission_uranium_power);
		propertyFissionUraniumHeatGeneration.set(fission_uranium_heat_generation);
		
		propertyFissionNeptuniumFuelTime.set(fission_neptunium_fuel_time);
		propertyFissionNeptuniumPower.set(fission_neptunium_power);
		propertyFissionNeptuniumHeatGeneration.set(fission_neptunium_heat_generation);
		
		propertyFissionPlutoniumFuelTime.set(fission_plutonium_fuel_time);
		propertyFissionPlutoniumPower.set(fission_plutonium_power);
		propertyFissionPlutoniumHeatGeneration.set(fission_plutonium_heat_generation);
		
		propertyFissionMOXFuelTime.set(fission_mox_fuel_time);
		propertyFissionMOXPower.set(fission_mox_power);
		propertyFissionMOXHeatGeneration.set(fission_mox_heat_generation);
		
		propertyFissionAmericiumFuelTime.set(fission_americium_fuel_time);
		propertyFissionAmericiumPower.set(fission_americium_power);
		propertyFissionAmericiumHeatGeneration.set(fission_americium_heat_generation);
		
		propertyFissionCuriumFuelTime.set(fission_curium_fuel_time);
		propertyFissionCuriumPower.set(fission_curium_power);
		propertyFissionCuriumHeatGeneration.set(fission_curium_heat_generation);
		
		propertyFissionBerkeliumFuelTime.set(fission_berkelium_fuel_time);
		propertyFissionBerkeliumPower.set(fission_berkelium_power);
		propertyFissionBerkeliumHeatGeneration.set(fission_berkelium_heat_generation);
		
		propertyFissionCaliforniumFuelTime.set(fission_californium_fuel_time);
		propertyFissionCaliforniumPower.set(fission_californium_power);
		propertyFissionCaliforniumHeatGeneration.set(fission_californium_heat_generation);
		
		propertyFusionBasePower.set(fusion_base_power);
		propertyFusionFuelUse.set(fusion_fuel_use);
		propertyFusionHeatGeneration.set(fusion_heat_generation);
		propertyFusionOverheat.set(fusion_overheat);
		propertyFusionActiveCooling.set(fusion_active_cooling);
		propertyFusionUpdateRate.set(fusion_update_rate);
		propertyFusionMinSize.set(fusion_min_size);
		propertyFusionMaxSize.set(fusion_max_size);
		propertyFusionElectromagnetPower.set(fusion_electromagnet_power);
		propertyFusionAlternateSound.set(fusion_alternate_sound);
		
		propertyFusionFuelTime.set(fusion_fuel_time);
		propertyFusionPower.set(fusion_power);
		propertyFusionHeatVariable.set(fusion_heat_variable);
		
		propertyAcceleratorElectromagnetPower.set(accelerator_electromagnet_power);
		propertyAcceleratorSupercoolerCoolant.set(accelerator_supercooler_coolant);
		
		propertyBatteryCapacity.set(battery_capacity);
		
		propertyToolMiningLevel.set(tool_mining_level);
		propertyToolDurability.set(tool_durability);
		propertyToolSpeed.set(tool_speed);
		propertyToolAttackDamage.set(tool_attack_damage);
		propertyToolEnchantability.set(tool_enchantability);
		
		propertyArmorDurability.set(armor_durability);
		propertyArmorEnchantability.set(armor_enchantability);
		propertyArmorBoron.set(armor_boron);
		propertyArmorTough.set(armor_tough);
		propertyArmorHardCarbon.set(armor_hard_carbon);
		propertyArmorBoronNitride.set(armor_boron_nitride);
		propertyArmorToughness.set(armor_toughness);
		
		propertyRareDrops.set(rare_drops);
		
		if (config.hasChanged()) config.save();
	}
	
	public static boolean[] readBooleanArrayFromConfig(Property property) {
		int currentLength = property.getBooleanList().length;
		int defaultLength = property.getDefaults().length;
		if (currentLength == defaultLength) {
			return property.getBooleanList();
		}
		boolean[] newArray = new boolean[defaultLength];
		if (currentLength > defaultLength) {
			for (int i = 0; i < defaultLength; i++) {
				newArray[i] = property.getBooleanList()[i];
			}
		} else {
			for (int i = 0; i < currentLength; i++) {
				newArray[i] = property.getBooleanList()[i];
			}
			for (int i = currentLength; i < defaultLength; i++) {
				newArray[i] = property.setToDefault().getBooleanList()[i];
			}
		}
		return newArray;
	}
	
	public static int[] readIntegerArrayFromConfig(Property property) {
		int currentLength = property.getIntList().length;
		int defaultLength = property.getDefaults().length;
		if (currentLength == defaultLength) {
			return property.getIntList();
		}
		int[] newArray = new int[defaultLength];
		if (currentLength > defaultLength) {
			for (int i = 0; i < defaultLength; i++) {
				newArray[i] = property.getIntList()[i];
			}
		} else {
			for (int i = 0; i < currentLength; i++) {
				newArray[i] = property.getIntList()[i];
			}
			for (int i = currentLength; i < defaultLength; i++) {
				newArray[i] = property.setToDefault().getIntList()[i];
			}
		}
		return newArray;
	}
	
	public static double[] readDoubleArrayFromConfig(Property property) {
		int currentLength = property.getDoubleList().length;
		int defaultLength = property.getDefaults().length;
		if (currentLength == defaultLength) {
			return property.getDoubleList();
		}
		double[] newArray = new double[defaultLength];
		if (currentLength > defaultLength) {
			for (int i = 0; i < defaultLength; i++) {
				newArray[i] = property.getDoubleList()[i];
			}
		} else {
			for (int i = 0; i < currentLength; i++) {
				newArray[i] = property.getDoubleList()[i];
			}
			for (int i = currentLength; i < defaultLength; i++) {
				newArray[i] = property.setToDefault().getDoubleList()[i];
			}
		}
		return newArray;
	}
	
	public static class ConfigEventHandler {
		
		@SubscribeEvent(priority = EventPriority.LOWEST)
		public void onEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(Global.MOD_ID)) syncFromGui();
		}
	}
}
