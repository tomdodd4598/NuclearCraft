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
	public static final String CATEGORY_ENERGY_STORAGE = "energy_storage";
	public static final String CATEGORY_TOOLS = "tools";
	public static final String CATEGORY_OTHER = "other";
	
	public static boolean[] ore_gen;
	public static int[] ore_size;
	public static int[] ore_rate;
	public static int[] ore_min_height;
	public static int[] ore_max_height;
	
	public static int[] processor_time;
	public static int[] processor_power;
	public static int processor_rf_per_eu;
	
	public static int[] rtg_power;
	public static int[] solar_power;
	public static int generator_rf_per_eu;
	
	public static double fission_power; // Default: 1
	public static double fission_fuel_use; // Default: 1
	public static double fission_heat_generation; // Default: 1
	public static double[] fission_cooling_rate;
	public static boolean fission_overheat;
	public static int fission_update_rate;
	public static int fission_min_size; // Default: 1
	public static int fission_max_size; // Default: 24
	
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
	
	public static int[] battery_capacity;
	
	public static int[] tool_mining_level;
	public static int[] tool_durability;
	public static double[] tool_speed;
	public static double[] tool_attack_damage;
	public static int[] tool_enchantability;
	
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
		
		Property propertyProcessorTime = config.get(CATEGORY_PROCESSORS, "processor_time", new int[] {400, 800, 800, 400, 400}, I18n.translateToLocalFormatted("gui.config.processors.processor_time.comment"), 1, 128000);
		propertyProcessorTime.setLanguageKey("gui.config.processors.processor_time");
		Property propertyProcessorPower = config.get(CATEGORY_PROCESSORS, "processor_power", new int[] {10, 10, 10, 20, 10}, I18n.translateToLocalFormatted("gui.config.processors.processor_power.comment"), 0, 16000);
		propertyProcessorPower.setLanguageKey("gui.config.processors.processor_power");
		Property propertyProcessorRFPerEU = config.get(CATEGORY_PROCESSORS, "processor_rf_per_eu", 4, I18n.translateToLocalFormatted("gui.config.processors.processor_rf_per_eu.comment"), 1, 255);
		propertyProcessorRFPerEU.setLanguageKey("gui.config.processors.processor_rf_per_eu");
		
		Property propertyRTGPower = config.get(CATEGORY_GENERATORS, "rtg_power", new int[] {4, 100, 50, 400}, I18n.translateToLocalFormatted("gui.config.generators.rtg_power.comment"), 1, Integer.MAX_VALUE);
		propertyRTGPower.setLanguageKey("gui.config.generators.rtg_power");
		Property propertySolarPower = config.get(CATEGORY_GENERATORS, "solar_power", new int[] {5}, I18n.translateToLocalFormatted("gui.config.generators.solar_power.comment"), 1, Integer.MAX_VALUE);
		propertySolarPower.setLanguageKey("gui.config.generators.solar_power");
		Property propertyGeneratorRFPerEU = config.get(CATEGORY_GENERATORS, "generator_rf_per_eu", 16, I18n.translateToLocalFormatted("gui.config.generators.generator_rf_per_eu.comment"), 1, 255);
		propertyGeneratorRFPerEU.setLanguageKey("gui.config.generators.generator_rf_per_eu");
		
		Property propertyFissionPower = config.get(CATEGORY_FISSION, "fission_power", 1.0D, I18n.translateToLocalFormatted("gui.config.fission.fission_power.comment"), 0.0D, 255.0D);
		propertyFissionPower.setLanguageKey("gui.config.fission.fission_power");
		Property propertyFissionFuelUse = config.get(CATEGORY_FISSION, "fission_fuel_use", 1.0D, I18n.translateToLocalFormatted("gui.config.fission.fission_fuel_use.comment"), 0.0D, 255.0D);
		propertyFissionFuelUse.setLanguageKey("gui.config.fission.fission_fuel_use");
		Property propertyFissionHeatGeneration = config.get(CATEGORY_FISSION, "fission_heat_generation", 1.0D, I18n.translateToLocalFormatted("gui.config.fission.fission_heat_generation.comment"), 0.0D, 255.0D);
		propertyFissionHeatGeneration.setLanguageKey("gui.config.fission.fission_heat_generation");
		Property propertyFissionCoolingRate = config.get(CATEGORY_FISSION, "fission_cooling_rate", new double[] {20.0D, 80.0D, 80.0D, 120.0D, 160.0D, 100.0D, 120.0D, 120.0D, 150.0D, 200.0D}, I18n.translateToLocalFormatted("gui.config.fission.fission_cooling_rate.comment"), 0.0D, 32767.0D);
		propertyFissionCoolingRate.setLanguageKey("gui.config.fission.fission_cooling_rate");
		Property propertyFissionOverheat = config.get(CATEGORY_FISSION, "fission_overheat", true, I18n.translateToLocalFormatted("gui.config.fission.fission_overheat.comment"));
		propertyFissionOverheat.setLanguageKey("gui.config.fission.fission_overheat");
		Property propertyFissionUpdateRate = config.get(CATEGORY_FISSION, "fission_update_rate", 40, I18n.translateToLocalFormatted("gui.config.fission.fission_update_rate.comment"), 1, 1200);
		propertyFissionUpdateRate.setLanguageKey("gui.config.fission.fission_update_rate");
		Property propertyFissionMinSize = config.get(CATEGORY_FISSION, "fission_min_size", 1, I18n.translateToLocalFormatted("gui.config.fission.fission_min_size.comment"), 1, 255);
		propertyFissionMinSize.setLanguageKey("gui.config.fission.fission_min_size");
		Property propertyFissionMaxSize = config.get(CATEGORY_FISSION, "fission_max_size", 24, I18n.translateToLocalFormatted("gui.config.fission.fission_max_size.comment"), 1, 255);
		propertyFissionMaxSize.setLanguageKey("gui.config.fission.fission_max_size");
		
		Property propertyFissionThoriumFuelTime = config.get(CATEGORY_FISSION, "fission_thorium_fuel_time", new double[] {144000.0D, 144000.0D}, I18n.translateToLocalFormatted("gui.config.fission.fission_thorium_fuel_time.comment"), 0.0D, (double) Integer.MAX_VALUE);
		propertyFissionThoriumFuelTime.setLanguageKey("gui.config.fission.fission_thorium_fuel_time");
		Property propertyFissionThoriumPower = config.get(CATEGORY_FISSION, "fission_thorium_power", new double[] {60.0D, NCMath.Round(60.0D*1.4D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_thorium_power.comment"), 0.0D, 32767.0D);
		propertyFissionThoriumPower.setLanguageKey("gui.config.fission.fission_thorium_power");
		Property propertyFissionThoriumHeatGeneration = config.get(CATEGORY_FISSION, "fission_thorium_heat_generation", new double[] {18.0D, NCMath.Round(18.0D*1.25D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_thorium_heat_generation.comment"), 0.0D, 32767.0D);
		propertyFissionThoriumHeatGeneration.setLanguageKey("gui.config.fission.fission_thorium_heat_generation");
		
		Property propertyFissionUraniumFuelTime = config.get(CATEGORY_FISSION, "fission_uranium_fuel_time", new double[] {64000.0D, 64000.0D, 64000.0D, 64000.0D, 72000.0D, 72000.0D, 72000.0D, 72000.0D}, I18n.translateToLocalFormatted("gui.config.fission.fission_uranium_fuel_time.comment"), 0.0D, (double) Integer.MAX_VALUE);
		propertyFissionUraniumFuelTime.setLanguageKey("gui.config.fission.fission_uranium_fuel_time");
		Property propertyFissionUraniumPower = config.get(CATEGORY_FISSION, "fission_uranium_power", new double[] {144.0D, NCMath.Round(144.0D*1.4D, 1), 144.0D*4.0D, NCMath.Round(144.0D*4.0D*1.4D, 1), 120.0D, NCMath.Round(120.0D*1.4D, 1), 120.0D*4.0D, NCMath.Round(120.0D*4.0D*1.4D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_uranium_power.comment"), 0.0D, 32767.0D);
		propertyFissionUraniumPower.setLanguageKey("gui.config.fission.fission_uranium_power");
		Property propertyFissionUraniumHeatGeneration = config.get(CATEGORY_FISSION, "fission_uranium_heat_generation", new double[] {60.0D, NCMath.Round(60.0D*1.25D, 1), 60.0D*6.0D, NCMath.Round(60.0D*6.0D*1.25D, 1), 50.0D, NCMath.Round(50.0D*1.25D, 1), 50.0D*6.0D, NCMath.Round(50.0D*6.0D*1.25D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_uranium_heat_generation.comment"), 0.0D, 32767.0D);
		propertyFissionUraniumHeatGeneration.setLanguageKey("gui.config.fission.fission_uranium_heat_generation");
		
		Property propertyFissionNeptuniumFuelTime = config.get(CATEGORY_FISSION, "fission_neptunium_fuel_time", new double[] {102000.0D, 102000.0D, 102000.0D, 102000.0D}, I18n.translateToLocalFormatted("gui.config.fission.fission_neptunium_fuel_time.comment"), 0.0D, (double) Integer.MAX_VALUE);
		propertyFissionNeptuniumFuelTime.setLanguageKey("gui.config.fission.fission_neptunium_fuel_time");
		Property propertyFissionNeptuniumPower = config.get(CATEGORY_FISSION, "fission_neptunium_power", new double[] {90.0D, NCMath.Round(90.0D*1.4D, 1), 90.0D*4.0D, NCMath.Round(90.0D*4.0D*1.4D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_neptunium_power.comment"), 0.0D, 32767.0D);
		propertyFissionNeptuniumPower.setLanguageKey("gui.config.fission.fission_neptunium_power");
		Property propertyFissionNeptuniumHeatGeneration = config.get(CATEGORY_FISSION, "fission_neptunium_heat_generation", new double[] {36.0D, NCMath.Round(36.0D*1.25D, 1), 36.0D*6.0D, NCMath.Round(36.0D*6.0D*1.25D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_neptunium_heat_generation.comment"), 0.0D, 32767.0D);
		propertyFissionNeptuniumHeatGeneration.setLanguageKey("gui.config.fission.fission_neptunium_heat_generation");
		
		Property propertyFissionPlutoniumFuelTime = config.get(CATEGORY_FISSION, "fission_plutonium_fuel_time", new double[] {92000.0D, 92000.0D, 92000.0D, 92000.0D, 60000.0D, 60000.0D, 60000.0D, 60000.0D}, I18n.translateToLocalFormatted("gui.config.fission.fission_plutonium_fuel_time.comment"), 0.0D, (double) Integer.MAX_VALUE);
		propertyFissionPlutoniumFuelTime.setLanguageKey("gui.config.fission.fission_plutonium_fuel_time");
		Property propertyFissionPlutoniumPower = config.get(CATEGORY_FISSION, "fission_plutonium_power", new double[] {105.0D, NCMath.Round(105.0D*1.4D, 1), 105.0D*4.0D, NCMath.Round(105.0D*4.0D*1.4D, 1), 165.0D, NCMath.Round(165.0D*1.4D, 1), 165.0D*4.0D, NCMath.Round(165.0D*4.0D*1.4D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_plutonium_power.comment"), 0.0D, 32767.0D);
		propertyFissionPlutoniumPower.setLanguageKey("gui.config.fission.fission_plutonium_power");
		Property propertyFissionPlutoniumHeatGeneration = config.get(CATEGORY_FISSION, "fission_plutonium_heat_generation", new double[] {40.0D, NCMath.Round(40.0D*1.25D, 1), 40.0D*6.0D, NCMath.Round(40.0D*6.0D*1.25D, 1), 70.0D, NCMath.Round(70.0D*1.25D, 1), 70.0D*6.0D, NCMath.Round(70.0D*6.0D*1.25D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_plutonium_heat_generation.comment"), 0.0D, 32767.0D);
		propertyFissionPlutoniumHeatGeneration.setLanguageKey("gui.config.fission.fission_plutonium_heat_generation");
		
		Property propertyFissionMOXFuelTime = config.get(CATEGORY_FISSION, "fission_mox_fuel_time", new double[] {84000.0D, 56000.0D}, I18n.translateToLocalFormatted("gui.config.fission.fission_mox_fuel_time.comment"), 0.0D, (double) Integer.MAX_VALUE);
		propertyFissionMOXFuelTime.setLanguageKey("gui.config.fission.fission_mox_fuel_time");
		Property propertyFissionMOXPower = config.get(CATEGORY_FISSION, "fission_mox_power", new double[] {NCMath.Round(111.0D*1.4D, 1), NCMath.Round(174.0D*1.4D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_mox_power.comment"), 0.0D, 32767.0D);
		propertyFissionMOXPower.setLanguageKey("gui.config.fission.fission_mox_power");
		Property propertyFissionMOXHeatGeneration = config.get(CATEGORY_FISSION, "fission_mox_heat_generation", new double[] {NCMath.Round(46.0D*1.25D, 1), NCMath.Round(78.0D*1.25D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_mox_heat_generation.comment"), 0.0D, 32767.0D);
		propertyFissionMOXHeatGeneration.setLanguageKey("gui.config.fission.fission_mox_heat_generation");
		
		Property propertyFissionAmericiumFuelTime = config.get(CATEGORY_FISSION, "fission_americium_fuel_time", new double[] {54000.0D, 54000.0D, 54000.0D, 54000.0D}, I18n.translateToLocalFormatted("gui.config.fission.fission_americium_fuel_time.comment"), 0.0D, (double) Integer.MAX_VALUE);
		propertyFissionAmericiumFuelTime.setLanguageKey("gui.config.fission.fission_americium_fuel_time");
		Property propertyFissionAmericiumPower = config.get(CATEGORY_FISSION, "fission_americium_power", new double[] {192.0D, NCMath.Round(192.0D*1.4D, 1), 192.0D*4.0D, NCMath.Round(192.0D*4.0D*1.4D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_americium_power.comment"), 0.0D, 32767.0D);
		propertyFissionAmericiumPower.setLanguageKey("gui.config.fission.fission_americium_power");
		Property propertyFissionAmericiumHeatGeneration = config.get(CATEGORY_FISSION, "fission_americium_heat_generation", new double[] {94.0D, NCMath.Round(94.0D*1.25D, 1), 94.0D*6.0D, NCMath.Round(94.0D*6.0D*1.25D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_americium_heat_generation.comment"), 0.0D, 32767.0D);
		propertyFissionAmericiumHeatGeneration.setLanguageKey("gui.config.fission.fission_americium_heat_generation");
		
		Property propertyFissionCuriumFuelTime = config.get(CATEGORY_FISSION, "fission_curium_fuel_time", new double[] {52000.0D, 52000.0D, 52000.0D, 52000.0D, 68000.0D, 68000.0D, 68000.0D, 68000.0D, 78000.0D, 78000.0D, 78000.0D, 78000.0D}, I18n.translateToLocalFormatted("gui.config.fission.fission_curium_fuel_time.comment"), 0.0D, (double) Integer.MAX_VALUE);
		propertyFissionCuriumFuelTime.setLanguageKey("gui.config.fission.fission_curium_fuel_time");
		Property propertyFissionCuriumPower = config.get(CATEGORY_FISSION, "fission_curium_power", new double[] {210.0D, NCMath.Round(210.0D*1.4D, 1), 210.0D*4.0D, NCMath.Round(210.0D*4.0D*1.4D, 1), 162.0D, NCMath.Round(162.0D*1.4D, 1), 162.0D*4.0D, NCMath.Round(162.0D*4.0D*1.4D, 1), 138.0D, NCMath.Round(138.0D*1.4D, 1), 138.0D*4.0D, NCMath.Round(138.0D*4.0D*1.4D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_curium_power.comment"), 0.0D, 32767.0D);
		propertyFissionCuriumPower.setLanguageKey("gui.config.fission.fission_curium_power");
		Property propertyFissionCuriumHeatGeneration = config.get(CATEGORY_FISSION, "fission_curium_heat_generation", new double[] {112.0D, NCMath.Round(112.0D*1.25D, 1), 112.0D*6.0D, NCMath.Round(112.0D*6.0D*1.25D, 1), 68.0D, NCMath.Round(68.0D*1.25D, 1), 68.0D*6.0D, NCMath.Round(68.0D*6.0D*1.25D, 1), 54.0D, NCMath.Round(54.0D*1.25D, 1), 54.0D*6.0D, NCMath.Round(54.0D*6.0D*1.25D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_curium_heat_generation.comment"), 0.0D, 32767.0D);
		propertyFissionCuriumHeatGeneration.setLanguageKey("gui.config.fission.fission_curium_heat_generation");
		
		Property propertyFissionBerkeliumFuelTime = config.get(CATEGORY_FISSION, "fission_berkelium_fuel_time", new double[] {86000.0D, 86000.0D, 86000.0D, 86000.0D}, I18n.translateToLocalFormatted("gui.config.fission.fission_berkelium_fuel_time.comment"), 0.0D, (double) Integer.MAX_VALUE);
		propertyFissionBerkeliumFuelTime.setLanguageKey("gui.config.fission.fission_berkelium_fuel_time");
		Property propertyFissionBerkeliumPower = config.get(CATEGORY_FISSION, "fission_berkelium_power", new double[] {135.0D, NCMath.Round(135.0D*1.4D, 1), 135.0D*4.0D, NCMath.Round(135.0D*4.0D*1.4D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_berkelium_power.comment"), 0.0D, 32767.0D);
		propertyFissionBerkeliumPower.setLanguageKey("gui.config.fission.fission_berkelium_power");
		Property propertyFissionBerkeliumHeatGeneration = config.get(CATEGORY_FISSION, "fission_berkelium_heat_generation", new double[] {52.0D, NCMath.Round(52.0D*1.25D, 1), 52.0D*6.0D, NCMath.Round(52.0D*6.0D*1.25D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_berkelium_heat_generation.comment"), 0.0D, 32767.0D);
		propertyFissionBerkeliumHeatGeneration.setLanguageKey("gui.config.fission.fission_berkelium_heat_generation");
		
		Property propertyFissionCaliforniumFuelTime = config.get(CATEGORY_FISSION, "fission_californium_fuel_time", new double[] {60000.0D, 60000.0D, 60000.0D, 60000.0D, 58000.0D, 58000.0D, 58000.0D, 58000.0D}, I18n.translateToLocalFormatted("gui.config.fission.fission_californium_fuel_time.comment"), 0.0D, (double) Integer.MAX_VALUE);
		propertyFissionCaliforniumFuelTime.setLanguageKey("gui.config.fission.fission_californium_fuel_time");
		Property propertyFissionCaliforniumPower = config.get(CATEGORY_FISSION, "fission_californium_power", new double[] {216.0D, NCMath.Round(216.0D*1.4D, 1), 216.0D*4.0D, NCMath.Round(216.0D*4.0D*1.4D, 1), 225.0D, NCMath.Round(225.0D*1.4D, 1), 225.0D*4.0D, NCMath.Round(225.0D*4.0D*1.4D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_californium_power.comment"), 0.0D, 32767.0D);
		propertyFissionCaliforniumPower.setLanguageKey("gui.config.fission.fission_californium_power");
		Property propertyFissionCaliforniumHeatGeneration = config.get(CATEGORY_FISSION, "fission_californium_heat_generation", new double[] {116.0D, NCMath.Round(116.0D*1.25D, 1), 116.0D*6.0D, NCMath.Round(116.0D*6.0D*1.25D, 1), 120.0D, NCMath.Round(120.0D*1.25D, 1), 120.0D*6.0D, NCMath.Round(120.0D*6.0D*1.25D, 1)}, I18n.translateToLocalFormatted("gui.config.fission.fission_californium_heat_generation.comment"), 0.0D, 32767.0D);
		propertyFissionCaliforniumHeatGeneration.setLanguageKey("gui.config.fission.fission_californium_heat_generation");
		
		Property propertyBatteryCapacity = config.get(CATEGORY_ENERGY_STORAGE, "battery_capacity", new int[] {1600000, 64000000}, I18n.translateToLocalFormatted("gui.config.energy_storage.battery_capacity.comment"), 1, Integer.MAX_VALUE);
		propertyBatteryCapacity.setLanguageKey("gui.config.energy_storage.battery_capacity");
		
		Property propertyToolMiningLevel = config.get(CATEGORY_TOOLS, "tool_mining_level", new int[] {2, 3, 3}, I18n.translateToLocalFormatted("gui.config.tools.tool_mining_level.comment"), 1, 15);
		propertyToolMiningLevel.setLanguageKey("gui.config.tools.tool_mining_level");
		Property propertyToolDurability = config.get(CATEGORY_TOOLS, "tool_durability", new int[] {547, 929, 929*5}, I18n.translateToLocalFormatted("gui.config.tools.tool_durability.comment"), 1, 32767);
		propertyToolDurability.setLanguageKey("gui.config.tools.tool_durability");
		Property propertyToolSpeed = config.get(CATEGORY_TOOLS, "tool_speed", new double[] {8.0D, 10.0D, 10.0D}, I18n.translateToLocalFormatted("gui.config.tools.tool_speed.comment"), 1.0D, 255.0D);
		propertyToolSpeed.setLanguageKey("gui.config.tools.tool_speed");
		Property propertyToolAttackDamage = config.get(CATEGORY_TOOLS, "tool_attack_damage", new double[] {2.5D, 3.0D, 3.0D}, I18n.translateToLocalFormatted("gui.config.tools.tool_attack_damage.comment"), 0.0D, 255.0D);
		propertyToolAttackDamage.setLanguageKey("gui.config.tools.tool_attack_damage");
		Property propertyToolEnchantability = config.get(CATEGORY_TOOLS, "tool_enchantability", new int[] {6, 15, 15}, I18n.translateToLocalFormatted("gui.config.tools.tool_enchantability.comment"), 1, 32767);
		propertyToolEnchantability.setLanguageKey("gui.config.tools.tool_enchantability");
		
		Property propertyRareDrops = config.get(CATEGORY_OTHER, "gui.config.other.rare_drops", false, I18n.translateToLocalFormatted("gui.config.other.rare_drops.comment"));
		propertyRareDrops.setLanguageKey("gui.config.other.rare_drops");
		
		List<String> propertyOrderOres = new ArrayList<String>();
		propertyOrderOres.add(propertyOreGen.getName());
		propertyOrderOres.add(propertyOreSize.getName());
		propertyOrderOres.add(propertyOreRate.getName());
		propertyOrderOres.add(propertyOreMinHeight.getName());
		propertyOrderOres.add(propertyOreMaxHeight.getName());
		config.setCategoryPropertyOrder(CATEGORY_ORES, propertyOrderOres);
		
		List<String> propertyOrderProcessors = new ArrayList<String>();
		propertyOrderProcessors.add(propertyProcessorTime.getName());
		propertyOrderProcessors.add(propertyProcessorPower.getName());
		propertyOrderProcessors.add(propertyProcessorRFPerEU.getName());
		config.setCategoryPropertyOrder(CATEGORY_PROCESSORS, propertyOrderProcessors);
		
		List<String> propertyOrderGenerators = new ArrayList<String>();
		propertyOrderGenerators.add(propertyRTGPower.getName());
		propertyOrderGenerators.add(propertySolarPower.getName());
		propertyOrderGenerators.add(propertyGeneratorRFPerEU.getName());
		config.setCategoryPropertyOrder(CATEGORY_GENERATORS, propertyOrderGenerators);
		
		List<String> propertyOrderFission = new ArrayList<String>();
		propertyOrderFission.add(propertyFissionPower.getName());
		propertyOrderFission.add(propertyFissionFuelUse.getName());
		propertyOrderFission.add(propertyFissionHeatGeneration.getName());
		propertyOrderFission.add(propertyFissionCoolingRate.getName());
		propertyOrderFission.add(propertyFissionMinSize.getName());
		propertyOrderFission.add(propertyFissionMaxSize.getName());
		
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
		
		List<String> propertyOrderTools = new ArrayList<String>();
		propertyOrderTools.add(propertyToolMiningLevel.getName());
		propertyOrderTools.add(propertyToolDurability.getName());
		propertyOrderTools.add(propertyToolSpeed.getName());
		propertyOrderTools.add(propertyToolAttackDamage.getName());
		propertyOrderTools.add(propertyToolEnchantability.getName());
		config.setCategoryPropertyOrder(CATEGORY_TOOLS, propertyOrderTools);
		
		List<String> propertyOrderOther = new ArrayList<String>();
		propertyOrderOther.add(propertyRareDrops.getName());
		config.setCategoryPropertyOrder(CATEGORY_OTHER, propertyOrderOther);
		
		if(readFieldFromConfig) {
			ore_gen = readBooleanArrayFromConfig(propertyOreGen);
			ore_size = readIntegerArrayFromConfig(propertyOreSize);
			ore_rate = readIntegerArrayFromConfig(propertyOreRate);
			ore_min_height = readIntegerArrayFromConfig(propertyOreMinHeight);
			ore_max_height = readIntegerArrayFromConfig(propertyOreMaxHeight);
			/*ore_gen = propertyOreGen.getBooleanList();
			ore_size = propertyOreSize.getIntList();
			ore_rate = propertyOreRate.getIntList();
			ore_min_height = propertyOreMinHeight.getIntList();
			ore_max_height = propertyOreMaxHeight.getIntList();*/
			
			processor_time = readIntegerArrayFromConfig(propertyProcessorTime);
			processor_power = readIntegerArrayFromConfig(propertyProcessorPower);
			/*processor_time = propertyProcessorTime.getIntList();
			processor_power = propertyProcessorPower.getIntList();*/
			processor_rf_per_eu = propertyProcessorRFPerEU.getInt();
			
			rtg_power = readIntegerArrayFromConfig(propertyRTGPower);
			solar_power = readIntegerArrayFromConfig(propertySolarPower);
			/*rtg_power = propertyRTGPower.getIntList();
			solar_power = propertySolarPower.getIntList();*/
			generator_rf_per_eu = propertyGeneratorRFPerEU.getInt();
			
			fission_power = propertyFissionPower.getDouble();
			fission_fuel_use = propertyFissionFuelUse.getDouble();
			fission_heat_generation = propertyFissionHeatGeneration.getDouble();
			fission_cooling_rate = readDoubleArrayFromConfig(propertyFissionCoolingRate);
			//fission_cooling_rate = propertyFissionCoolingRate.getDoubleList();
			fission_overheat = propertyFissionOverheat.getBoolean();
			fission_update_rate = propertyFissionUpdateRate.getInt();
			fission_min_size = propertyFissionMinSize.getInt();
			fission_max_size = propertyFissionMaxSize.getInt();
			
			fission_thorium_fuel_time = readDoubleArrayFromConfig(propertyFissionThoriumFuelTime);
			fission_thorium_power = readDoubleArrayFromConfig(propertyFissionThoriumPower);
			fission_thorium_heat_generation = readDoubleArrayFromConfig(propertyFissionThoriumHeatGeneration);
			/*fission_thorium_fuel_time = propertyFissionThoriumFuelTime.getDoubleList();
			fission_thorium_power = propertyFissionThoriumPower.getDoubleList();
			fission_thorium_heat_generation = propertyFissionThoriumHeatGeneration.getDoubleList();*/
			
			fission_uranium_fuel_time = readDoubleArrayFromConfig(propertyFissionUraniumFuelTime);
			fission_uranium_power = readDoubleArrayFromConfig(propertyFissionUraniumPower);
			fission_uranium_heat_generation = readDoubleArrayFromConfig(propertyFissionUraniumHeatGeneration);
			/*fission_uranium_fuel_time = propertyFissionUraniumFuelTime.getDoubleList();
			fission_uranium_power = propertyFissionUraniumPower.getDoubleList();
			fission_uranium_heat_generation = propertyFissionUraniumHeatGeneration.getDoubleList();*/
			
			fission_neptunium_fuel_time = readDoubleArrayFromConfig(propertyFissionNeptuniumFuelTime);
			fission_neptunium_power = readDoubleArrayFromConfig(propertyFissionNeptuniumPower);
			fission_neptunium_heat_generation = readDoubleArrayFromConfig(propertyFissionNeptuniumHeatGeneration);
			/*fission_neptunium_fuel_time = propertyFissionNeptuniumFuelTime.getDoubleList();
			fission_neptunium_power = propertyFissionNeptuniumPower.getDoubleList();
			fission_neptunium_heat_generation = propertyFissionNeptuniumHeatGeneration.getDoubleList();*/
			
			fission_plutonium_fuel_time = readDoubleArrayFromConfig(propertyFissionPlutoniumFuelTime);
			fission_plutonium_power = readDoubleArrayFromConfig(propertyFissionPlutoniumPower);
			fission_plutonium_heat_generation = readDoubleArrayFromConfig(propertyFissionPlutoniumHeatGeneration);
			/*fission_plutonium_fuel_time = propertyFissionPlutoniumFuelTime.getDoubleList();
			fission_plutonium_power = propertyFissionPlutoniumPower.getDoubleList();
			fission_plutonium_heat_generation = propertyFissionPlutoniumHeatGeneration.getDoubleList();*/
			
			fission_mox_fuel_time = readDoubleArrayFromConfig(propertyFissionMOXFuelTime);
			fission_mox_power = readDoubleArrayFromConfig(propertyFissionMOXPower);
			fission_mox_heat_generation = readDoubleArrayFromConfig(propertyFissionMOXHeatGeneration);
			/*fission_mox_fuel_time = propertyFissionMOXFuelTime.getDoubleList();
			fission_mox_power = propertyFissionMOXPower.getDoubleList();
			fission_mox_heat_generation = propertyFissionMOXHeatGeneration.getDoubleList();*/
			
			fission_americium_fuel_time = readDoubleArrayFromConfig(propertyFissionAmericiumFuelTime);
			fission_americium_power = readDoubleArrayFromConfig(propertyFissionAmericiumPower);
			fission_americium_heat_generation = readDoubleArrayFromConfig(propertyFissionAmericiumHeatGeneration);
			/*fission_americium_fuel_time = propertyFissionAmericiumFuelTime.getDoubleList();
			fission_americium_power = propertyFissionAmericiumPower.getDoubleList();
			fission_americium_heat_generation = propertyFissionAmericiumHeatGeneration.getDoubleList();*/
			
			fission_curium_fuel_time = readDoubleArrayFromConfig(propertyFissionCuriumFuelTime);
			fission_curium_power = readDoubleArrayFromConfig(propertyFissionCuriumPower);
			fission_curium_heat_generation = readDoubleArrayFromConfig(propertyFissionCuriumHeatGeneration);
			/*fission_curium_fuel_time = propertyFissionCuriumFuelTime.getDoubleList();
			fission_curium_power = propertyFissionCuriumPower.getDoubleList();
			fission_curium_heat_generation = propertyFissionCuriumHeatGeneration.getDoubleList();*/
			
			fission_berkelium_fuel_time = readDoubleArrayFromConfig(propertyFissionBerkeliumFuelTime);
			fission_berkelium_power = readDoubleArrayFromConfig(propertyFissionBerkeliumPower);
			fission_berkelium_heat_generation = readDoubleArrayFromConfig(propertyFissionBerkeliumHeatGeneration);
			/*fission_berkelium_fuel_time = propertyFissionBerkeliumFuelTime.getDoubleList();
			fission_berkelium_power = propertyFissionBerkeliumPower.getDoubleList();
			fission_berkelium_heat_generation = propertyFissionBerkeliumHeatGeneration.getDoubleList();*/
			
			fission_californium_fuel_time = readDoubleArrayFromConfig(propertyFissionCaliforniumFuelTime);
			fission_californium_power = readDoubleArrayFromConfig(propertyFissionCaliforniumPower);
			fission_californium_heat_generation = readDoubleArrayFromConfig(propertyFissionCaliforniumHeatGeneration);
			/*fission_californium_fuel_time = propertyFissionCaliforniumFuelTime.getDoubleList();
			fission_californium_power = propertyFissionCaliforniumPower.getDoubleList();
			fission_californium_heat_generation = propertyFissionCaliforniumHeatGeneration.getDoubleList();*/
			
			battery_capacity = readIntegerArrayFromConfig(propertyBatteryCapacity);
			//battery_capacity = propertyBatteryCapacity.getIntList();
			
			tool_mining_level = readIntegerArrayFromConfig(propertyToolMiningLevel);
			tool_durability = readIntegerArrayFromConfig(propertyToolDurability);
			tool_speed = readDoubleArrayFromConfig(propertyToolSpeed);
			tool_attack_damage = readDoubleArrayFromConfig(propertyToolAttackDamage);
			tool_enchantability = readIntegerArrayFromConfig(propertyToolEnchantability);
			/*tool_mining_level = propertyToolMiningLevel.getIntList();
			tool_durability = propertyToolDurability.getIntList();
			tool_speed = propertyToolSpeed.getDoubleList();
			tool_attack_damage = propertyToolAttackDamage.getDoubleList();
			tool_enchantability = propertyToolEnchantability.getIntList();*/
			
			rare_drops = propertyRareDrops.getBoolean();
		}
		
		propertyOreGen.set(ore_gen);
		propertyOreSize.set(ore_size);
		propertyOreRate.set(ore_rate);
		propertyOreMinHeight.set(ore_min_height);
		propertyOreMaxHeight.set(ore_max_height);
		
		propertyProcessorTime.set(processor_time);
		propertyProcessorPower.set(processor_power);
		propertyProcessorRFPerEU.set(processor_rf_per_eu);
		
		propertyRTGPower.set(rtg_power);
		propertySolarPower.set(solar_power);
		propertyGeneratorRFPerEU.set(generator_rf_per_eu);
		
		propertyFissionPower.set(fission_power);
		propertyFissionFuelUse.set(fission_fuel_use);
		propertyFissionHeatGeneration.set(fission_heat_generation);
		propertyFissionCoolingRate.set(fission_cooling_rate);
		propertyFissionOverheat.set(fission_overheat);
		propertyFissionUpdateRate.set(fission_update_rate);
		propertyFissionMinSize.set(fission_min_size);
		propertyFissionMaxSize.set(fission_max_size);
		
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
		
		propertyBatteryCapacity.set(battery_capacity);
		
		propertyToolMiningLevel.set(tool_mining_level);
		propertyToolDurability.set(tool_durability);
		propertyToolSpeed.set(tool_speed);
		propertyToolAttackDamage.set(tool_attack_damage);
		propertyToolEnchantability.set(tool_enchantability);
		
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
