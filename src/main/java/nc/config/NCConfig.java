package nc.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nc.Global;
import nc.ModCheck;
import nc.network.PacketHandler;
import nc.network.config.ConfigUpdatePacket;
import nc.radiation.RadSources;
import nc.recipe.ProcessorRecipeHandler;
import nc.util.Lang;
import nc.util.NCMath;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class NCConfig {

	private static Configuration config = null;
	
	public static final String CATEGORY_ORES = "ores";
	public static final String CATEGORY_PROCESSORS = "processors";
	public static final String CATEGORY_GENERATORS = "generators";
	public static final String CATEGORY_FISSION = "fission";
	public static final String CATEGORY_FUSION = "fusion";
	public static final String CATEGORY_SALT_FISSION = "salt_fission";
	public static final String CATEGORY_HEAT_EXCHANGER = "heat_exchanger";
	public static final String CATEGORY_TURBINE = "turbine";
	public static final String CATEGORY_ACCELERATOR = "accelerator";
	public static final String CATEGORY_ENERGY_STORAGE = "energy_storage";
	public static final String CATEGORY_TOOLS = "tools";
	public static final String CATEGORY_ARMOR = "armor";
	public static final String CATEGORY_RADIATION = "radiation";
	public static final String CATEGORY_ENTITIES = "entities";
	public static final String CATEGORY_OTHER = "other";
	
	public static int[] ore_dims;
	public static boolean ore_dims_list_type;
	public static boolean[] ore_gen;
	public static int[] ore_size;
	public static int[] ore_rate;
	public static int[] ore_min_height;
	public static int[] ore_max_height;
	public static boolean[] ore_drops;
	public static boolean hide_disabled_ores;
	public static int[] ore_harvest_levels;
	
	public static int[] processor_time;
	public static int[] processor_power;
	public static double[] speed_upgrade_power_laws;
	public static double[] speed_upgrade_multipliers;
	public static double[] energy_upgrade_power_laws;
	public static double[] energy_upgrade_multipliers;
	public static int rf_per_eu;
	public static boolean enable_gtce_eu;
	public static boolean enable_mek_gas;
	public static int machine_update_rate;
	public static int[] processor_passive_rate;
	public static int cobble_gen_power;
	public static boolean ore_processing;
	public static int[] manufactory_wood;
	public static boolean rock_crusher_alternate;
	public static boolean[] gtce_recipe_integration;
	public static boolean gtce_recipe_logging;
	public static boolean smart_processor_input;
	public static boolean passive_permeation;
	public static boolean processor_particles;
	
	public static int[] rtg_power;
	public static int[] solar_power;
	public static double[] decay_lifetime;
	public static int[] decay_power;
	
	public static double fission_power; // Default: 1
	public static double fission_fuel_use; // Default: 1
	public static double fission_heat_generation; // Default: 1
	public static double[] fission_cooling_rate;
	public static double[] fission_active_cooling_rate;
	public static boolean fission_water_cooler_requirement;
	public static boolean fission_overheat;
	public static boolean fission_explosions;
	public static double fission_meltdown_radiation_multiplier;
	public static int fission_min_size; // Default: 1
	public static int fission_max_size; // Default: 24
	public static int fission_comparator_max_heat;
	public static boolean fission_force_heat_comparator;
	public static int active_cooler_max_rate;
	
	public static double fission_moderator_extra_power;
	public static double fission_moderator_extra_heat;
	public static int fission_neutron_reach;
	
	public static double[] fission_thorium_fuel_time;
	public static double[] fission_thorium_power;
	public static double[] fission_thorium_heat_generation;
	public static double[] fission_thorium_radiation;
	
	public static double[] fission_uranium_fuel_time;
	public static double[] fission_uranium_power;
	public static double[] fission_uranium_heat_generation;
	public static double[] fission_uranium_radiation;
	
	public static double[] fission_neptunium_fuel_time;
	public static double[] fission_neptunium_power;
	public static double[] fission_neptunium_heat_generation;
	public static double[] fission_neptunium_radiation;
	
	public static double[] fission_plutonium_fuel_time;
	public static double[] fission_plutonium_power;
	public static double[] fission_plutonium_heat_generation;
	public static double[] fission_plutonium_radiation;
	
	public static double[] fission_mox_fuel_time;
	public static double[] fission_mox_power;
	public static double[] fission_mox_heat_generation;
	public static double[] fission_mox_radiation;
	
	public static double[] fission_americium_fuel_time;
	public static double[] fission_americium_power;
	public static double[] fission_americium_heat_generation;
	public static double[] fission_americium_radiation;
	
	public static double[] fission_curium_fuel_time;
	public static double[] fission_curium_power;
	public static double[] fission_curium_heat_generation;
	public static double[] fission_curium_radiation;
	
	public static double[] fission_berkelium_fuel_time;
	public static double[] fission_berkelium_power;
	public static double[] fission_berkelium_heat_generation;
	public static double[] fission_berkelium_radiation;
	
	public static double[] fission_californium_fuel_time;
	public static double[] fission_californium_power;
	public static double[] fission_californium_heat_generation;
	public static double[] fission_californium_radiation;
	
	public static double fusion_base_power; // Default: 1
	public static double fusion_fuel_use; // Default: 1
	public static double fusion_heat_generation; // Default: 1
	public static double fusion_heating_multiplier; // Default: 1
	public static boolean fusion_overheat;
	public static double fusion_meltdown_radiation_multiplier;
	public static boolean fusion_active_cooling;
	public static double[] fusion_active_cooling_rate;
	public static int fusion_min_size; // Default: 1
	public static int fusion_max_size; // Default: 24
	public static int fusion_comparator_max_efficiency;
	public static int fusion_electromagnet_power;
	public static boolean fusion_enable_sound;
	public static boolean fusion_plasma_craziness;
	
	public static double[] fusion_fuel_time;
	public static double[] fusion_power;
	public static double[] fusion_heat_variable;
	public static double[] fusion_radiation;
	
	public static double salt_fission_power; // Default: 1
	public static double salt_fission_fuel_use; // Default: 1
	public static double salt_fission_heat_generation; // Default: 1
	public static boolean salt_fission_overheat;
	public static double salt_fission_meltdown_radiation_multiplier;
	public static int salt_fission_min_size; // Default: 1
	public static int salt_fission_max_size; // Default: 24
	public static double[] salt_fission_cooling_rate;
	public static int salt_fission_redstone_max_heat;
	public static int salt_fission_max_distribution_rate;
	
	public static int heat_exchanger_min_size; // Default: 1
	public static int heat_exchanger_max_size; // Default: 24
	public static double[] heat_exchanger_conductivity;
	public static double heat_exchanger_coolant_mult;
	public static boolean heat_exchanger_alternate_exhaust_recipe;
	
	public static int turbine_min_size; // Default: 1
	public static int turbine_max_size; // Default: 24
	public static double[] turbine_blade_efficiency;
	public static double[] turbine_blade_expansion;
	public static double turbine_stator_expansion;
	public static double[] turbine_coil_conductivity;
	public static double[] turbine_power_per_mb;
	public static double[] turbine_expansion_level;
	public static int turbine_mb_per_blade;
	
	public static int accelerator_electromagnet_power;
	public static int accelerator_supercooler_coolant;
	
	public static int[] battery_capacity;
	
	public static int[] tool_mining_level;
	public static int[] tool_durability;
	public static double[] tool_speed;
	public static double[] tool_attack_damage;
	public static int[] tool_enchantability;
	public static double[] tool_handle_modifier;
	public static boolean[] tool_tic_register;
	
	public static int[] armor_durability;
	public static int[] armor_boron;
	public static int[] armor_tough;
	public static int[] armor_hard_carbon;
	public static int[] armor_boron_nitride;
	public static int[] armor_hazmat;
	public static int[] armor_enchantability;
	public static double[] armor_toughness;
	public static boolean[] armor_conarm_register;
	
	public static boolean[] entity_register;
	public static int entity_tracking_range;
	
	private static boolean radiation_enabled;
	public static boolean radiation_enabled_public;
	
	public static int radiation_world_tick_rate;
	public static int radiation_player_tick_rate;
	
	public static String[] radiation_worlds;
	public static String[] radiation_biomes;
	public static String[] radiation_structures;
	public static String[] radiation_world_limits;
	public static String[] radiation_biome_limits;
	public static int[] radiation_from_biomes_dims_blacklist;
	
	public static String[] radiation_ores;
	public static String[] radiation_items;
	public static String[] radiation_blocks;
	public static String[] radiation_fluids;
	public static String[] radiation_foods;
	public static String[] radiation_ores_blacklist;
	public static String[] radiation_items_blacklist;
	public static String[] radiation_blocks_blacklist;
	public static String[] radiation_fluids_blacklist;
	
	public static double max_player_rads;
	public static double radiation_player_decay_rate;
	public static double radiation_entity_decay_rate;
	public static double radiation_spread_rate;
	public static double radiation_spread_gradient;
	public static double radiation_decay_rate;
	public static double radiation_lowest_rate;
	public static double radiation_chunk_limit;
	
	public static String[] radiation_block_effects;
	public static double radiation_block_effect_limit;
	public static int radiation_block_effect_max_rate;
	public static double radiation_rain_mult;
	public static double radiation_swim_mult;
	
	public static double radiation_radaway_amount;
	public static double radiation_radaway_slow_amount;
	public static double radiation_radaway_rate;
	public static double radiation_radaway_slow_rate;
	public static double radiation_poison_time;
	public static double radiation_radaway_cooldown;
	public static double radiation_rad_x_amount;
	public static double radiation_rad_x_lifetime;
	public static double radiation_rad_x_cooldown;
	public static double[] radiation_shielding_level;
	public static boolean radiation_tile_shielding;
	public static double radiation_scrubber_fraction;
	public static int radiation_scrubber_radius;
	public static boolean radiation_scrubber_alt;
	public static double[] radiation_scrubber_param;
	public static int radiation_scrubber_power;
	public static int radiation_scrubber_borax_rate;
	public static double radiation_geiger_block_redstone;
	
	public static boolean radiation_shielding_default_recipes;
	public static String[] radiation_shielding_item_blacklist;
	public static String[] radiation_shielding_custom_stacks;
	public static String[] radiation_shielding_default_levels;
	
	public static boolean radiation_hardcore_stacks;
	public static double radiation_hardcore_containers;
	public static boolean radiation_dropped_items;
	public static boolean radiation_death_persist;
	public static double radiation_death_persist_fraction;
	public static double radiation_death_immunity_time;
	
	public static String[] radiation_passive_debuff_lists;
	public static String[] radiation_mob_buff_lists;
	
	private static boolean radiation_horse_armor;
	public static boolean radiation_horse_armor_public;
	
	public static double radiation_hud_size;
	public static double radiation_hud_position;
	public static double[] radiation_hud_position_cartesian;
	public static boolean radiation_hud_text_outline;
	public static boolean radiation_require_counter;
	public static boolean radiation_chunk_boundaries;
	public static int radiation_unit_prefixes;
	
	public static double radiation_badge_durability;
	public static double radiation_badge_info_rate;
	
	public static boolean single_creative_tab;
	
	public static boolean[] register_processor;
	public static boolean[] register_passive;
	public static boolean[] register_tool;
	public static boolean[] register_armor;
	
	public static boolean ctrl_info;
	
	public static boolean jei_chance_items_include_null;
	
	public static boolean rare_drops;
	public static boolean dungeon_loot;
	
	public static boolean wasteland_biome;
	public static int wasteland_biome_weight;
	
	public static boolean wasteland_dimension_gen;
	public static int wasteland_dimension;
	
	public static int mushroom_spread_rate;
	public static boolean mushroom_gen;
	public static int mushroom_gen_size;
	public static int mushroom_gen_rate;
	
	public static boolean register_fission_fluid_blocks;
	public static boolean register_cofh_fluids;
	
	public static boolean register_projecte_emc;
	
	public static boolean ore_dict_priority_bool;
	public static String[] ore_dict_priority;
	
	public static Configuration getConfig() {
		return config;
	}
	
	public static void preInit() {
		File configFile = new File(Loader.instance().getConfigDir(), "nuclearcraft.cfg");
		config = new Configuration(configFile);
		syncFromFiles();
		
		MinecraftForge.EVENT_BUS.register(new ServerConfigEventHandler());
	}
	
	public static void clientPreInit() {
		MinecraftForge.EVENT_BUS.register(new ClientConfigEventHandler());
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
	
	private static void syncConfig(boolean loadFromFile, boolean setFromConfig) {
		if (loadFromFile) config.load();
		
		Property propertyOreDims = config.get(CATEGORY_ORES, "ore_dims", new int[] {0, 2, -6, -100, 4598, -9999, -11325}, Lang.localise("gui.config.ores.ore_dims.comment"), Integer.MIN_VALUE, Integer.MAX_VALUE);
		propertyOreDims.setLanguageKey("gui.config.ores.ore_dims");
		Property propertyOreDimsListType = config.get(CATEGORY_ORES, "ore_dims_list_type", false, Lang.localise("gui.config.ores.ore_dims_list_type.comment"));
		propertyOreDimsListType.setLanguageKey("gui.config.ores.ore_dims_list_type");
		Property propertyOreGen = config.get(CATEGORY_ORES, "ore_gen", new boolean[] {true, true, true, true, true, true, true, true}, Lang.localise("gui.config.ores.ore_gen.comment"));
		propertyOreGen.setLanguageKey("gui.config.ores.ore_gen");
		Property propertyOreSize = config.get(CATEGORY_ORES, "ore_size", new int[] {6, 6, 6, 4, 4, 5, 5, 5}, Lang.localise("gui.config.ores.ore_size.comment"), 1, Integer.MAX_VALUE);
		propertyOreSize.setLanguageKey("gui.config.ores.ore_size");
		Property propertyOreRate = config.get(CATEGORY_ORES, "ore_rate", new int[] {5, 4, 6, 4, 4, 6, 6, 4}, Lang.localise("gui.config.ores.ore_rate.comment"), 1, Integer.MAX_VALUE);
		propertyOreRate.setLanguageKey("gui.config.ores.ore_rate");
		Property propertyOreMinHeight = config.get(CATEGORY_ORES, "ore_min_height", new int[] {0, 0, 0, 0, 0, 0, 0, 0}, Lang.localise("gui.config.ores.ore_min_height.comment"), 1, 255);
		propertyOreMinHeight.setLanguageKey("gui.config.ores.ore_min_height");
		Property propertyOreMaxHeight = config.get(CATEGORY_ORES, "ore_max_height", new int[] {48, 40, 36, 32, 32, 28, 28, 24}, Lang.localise("gui.config.ores.ore_max_height.comment"), 1, 255);
		propertyOreMaxHeight.setLanguageKey("gui.config.ores.ore_max_height");
		Property propertyOreDrops = config.get(CATEGORY_ORES, "ore_drops", new boolean[] {false, false, false, false, false, false, false}, Lang.localise("gui.config.ores.ore_drops.comment"));
		propertyOreDrops.setLanguageKey("gui.config.ores.ore_drops");
		Property propertyHideDisabledOres = config.get(CATEGORY_ORES, "hide_disabled_ores", false, Lang.localise("gui.config.ores.hide_disabled_ores.comment"));
		propertyHideDisabledOres.setLanguageKey("gui.config.ores.hide_disabled_ores");
		Property propertyOreHarvestLevels = config.get(CATEGORY_ORES, "ore_harvest_levels", new int[] {1, 1, 1, 2, 2, 2, 2, 2}, Lang.localise("gui.config.ores.ore_harvest_levels.comment"), 0, 15);
		propertyOreHarvestLevels.setLanguageKey("gui.config.ores.ore_harvest_levels");
		
		Property propertyProcessorTime = config.get(CATEGORY_PROCESSORS, "processor_time", new int[] {400, 800, 800, 400, 400, 600, 800, 600, 3200, 800, 400, 600, 800, 600, 1600, 600, 2400, 1200, 400}, Lang.localise("gui.config.processors.processor_time.comment"), 1, 128000);
		propertyProcessorTime.setLanguageKey("gui.config.processors.processor_time");
		Property propertyProcessorPower = config.get(CATEGORY_PROCESSORS, "processor_power", new int[] {20, 10, 10, 20, 10, 10, 40, 20, 40, 20, 0, 40, 10, 20, 10, 10, 10, 10, 20}, Lang.localise("gui.config.processors.processor_power.comment"), 0, 16000);
		propertyProcessorPower.setLanguageKey("gui.config.processors.processor_power");
		Property propertySpeedUpgradePowerLaws = config.get(CATEGORY_PROCESSORS, "speed_upgrade_power_laws_fp", new double[] {1D, 2D}, Lang.localise("gui.config.processors.speed_upgrade_power_laws_fp.comment"), 1D, 15D);
		propertySpeedUpgradePowerLaws.setLanguageKey("gui.config.processors.speed_upgrade_power_laws_fp");
		Property propertySpeedUpgradeMultipliers = config.get(CATEGORY_PROCESSORS, "speed_upgrade_multipliers_fp", new double[] {1D, 1D}, Lang.localise("gui.config.processors.speed_upgrade_multipliers_fp.comment"), 0D, 15D);
		propertySpeedUpgradeMultipliers.setLanguageKey("gui.config.processors.speed_upgrade_multipliers_fp");
		Property propertyEnergyUpgradePowerLaws = config.get(CATEGORY_PROCESSORS, "energy_upgrade_power_laws_fp", new double[] {1D}, Lang.localise("gui.config.processors.energy_upgrade_power_laws_fp.comment"), 1D, 15D);
		propertyEnergyUpgradePowerLaws.setLanguageKey("gui.config.processors.energy_upgrade_power_laws_fp");
		Property propertyEnergyUpgradeMultipliers = config.get(CATEGORY_PROCESSORS, "energy_upgrade_multipliers_fp", new double[] {1D}, Lang.localise("gui.config.processors.energy_upgrade_multipliers_fp.comment"), 0D, 15D);
		propertyEnergyUpgradeMultipliers.setLanguageKey("gui.config.processors.energy_upgrade_multipliers_fp");
		Property propertyRFPerEU = config.get(CATEGORY_PROCESSORS, "rf_per_eu", 16, Lang.localise("gui.config.processors.rf_per_eu.comment"), 1, 2000);
		propertyRFPerEU.setLanguageKey("gui.config.processors.rf_per_eu");
		Property propertyEnableGTCEEU = config.get(CATEGORY_PROCESSORS, "enable_gtce_eu", true, Lang.localise("gui.config.processors.enable_gtce_eu.comment"));
		propertyEnableGTCEEU.setLanguageKey("gui.config.processors.enable_gtce_eu");
		Property propertyEnableMekGas = config.get(CATEGORY_PROCESSORS, "enable_mek_gas", true, Lang.localise("gui.config.processors.enable_mek_gas.comment"));
		propertyEnableMekGas.setLanguageKey("gui.config.processors.enable_mek_gas");
		Property propertyMachineUpdateRate = config.get(CATEGORY_PROCESSORS, "machine_update_rate", 20, Lang.localise("gui.config.processors.machine_update_rate.comment"), 1, 1200);
		propertyMachineUpdateRate.setLanguageKey("gui.config.processors.machine_update_rate");
		Property propertyProcessorPassiveRate = config.get(CATEGORY_PROCESSORS, "processor_passive_rate", new int[] {100, 2, 200, 50}, Lang.localise("gui.config.processors.processor_passive_rate.comment"), 1, 4000);
		propertyProcessorPassiveRate.setLanguageKey("gui.config.processors.processor_passive_rate");
		Property propertyCobbleGenPower = config.get(CATEGORY_PROCESSORS, "cobble_gen_power", 0, Lang.localise("gui.config.processors.cobble_gen_power.comment"), 0, 255);
		propertyCobbleGenPower.setLanguageKey("gui.config.processors.cobble_gen_power");
		Property propertyOreProcessing = config.get(CATEGORY_PROCESSORS, "ore_processing", true, Lang.localise("gui.config.processors.ore_processing.comment"));
		propertyOreProcessing.setLanguageKey("gui.config.processors.ore_processing");
		Property propertyManufactoryWood = config.get(CATEGORY_PROCESSORS, "manufactory_wood", new int[] {6, 4}, Lang.localise("gui.config.processors.manufactory_wood.comment"), 1, 64);
		propertyManufactoryWood.setLanguageKey("gui.config.processors.manufactory_wood");
		Property propertyRockCrusherAlternate = config.get(CATEGORY_PROCESSORS, "rock_crusher_alternate", false, Lang.localise("gui.config.processors.rock_crusher_alternate.comment"));
		propertyRockCrusherAlternate.setLanguageKey("gui.config.processors.rock_crusher_alternate");
		Property propertyGTCERecipes = config.get(CATEGORY_PROCESSORS, "gtce_recipe_integration", new boolean[] {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true}, Lang.localise("gui.config.processors.gtce_recipe_integration.comment"));
		propertyGTCERecipes.setLanguageKey("gui.config.processors.gtce_recipe_integration");
		Property propertyGTCERecipeLogging = config.get(CATEGORY_PROCESSORS, "gtce_recipe_logging", false, Lang.localise("gui.config.processors.gtce_recipe_logging.comment"));
		propertyGTCERecipeLogging.setLanguageKey("gui.config.processors.gtce_recipe_logging");
		Property propertySmartProcessorInput = config.get(CATEGORY_PROCESSORS, "smart_processor_input", true, Lang.localise("gui.config.processors.smart_processor_input.comment"));
		propertySmartProcessorInput.setLanguageKey("gui.config.processors.smart_processor_input");
		Property propertyPermeation = config.get(CATEGORY_PROCESSORS, "passive_permeation", true, Lang.localise("gui.config.processors.passive_permeation.comment"));
		propertyPermeation.setLanguageKey("gui.config.processors.passive_permeation");
		Property propertyProcessorParticles = config.get(CATEGORY_PROCESSORS, "processor_particles", true, Lang.localise("gui.config.processors.processor_particles.comment"));
		propertyProcessorParticles.setLanguageKey("gui.config.processors.processor_particles");
		
		Property propertyRTGPower = config.get(CATEGORY_GENERATORS, "rtg_power", new int[] {4, 100, 50, 400}, Lang.localise("gui.config.generators.rtg_power.comment"), 1, Integer.MAX_VALUE);
		propertyRTGPower.setLanguageKey("gui.config.generators.rtg_power");
		Property propertySolarPower = config.get(CATEGORY_GENERATORS, "solar_power", new int[] {5, 20, 80, 320}, Lang.localise("gui.config.generators.solar_power.comment"), 1, Integer.MAX_VALUE);
		propertySolarPower.setLanguageKey("gui.config.generators.solar_power");
		Property propertyDecayLifetime = config.get(CATEGORY_GENERATORS, "decay_lifetime", new double[] {62.4D*1200D, 20.4D*1200D, 36.6D*1200D, 39.6D*1200D, 35.5D*1200D, 12.8D*1200D, 52.8D*1200D, 8.5D*1200D, 7.2D*1200D, 65.2D*1200D}, Lang.localise("gui.config.generators.decay_lifetime.comment"), 1D, 16777215D);
		propertyDecayLifetime.setLanguageKey("gui.config.generators.decay_lifetime");
		Property propertyDecayPower = config.get(CATEGORY_GENERATORS, "decay_power", new int[] {80, 80, 15, 5, 10, 15, 20, 25, 30, 40}, Lang.localise("gui.config.generators.decay_power.comment"), 0, 32767);
		propertyDecayPower.setLanguageKey("gui.config.generators.decay_power");
		
		Property propertyFissionPower = config.get(CATEGORY_FISSION, "fission_power", 1D, Lang.localise("gui.config.fission.fission_power.comment"), 0D, 255D);
		propertyFissionPower.setLanguageKey("gui.config.fission.fission_power");
		Property propertyFissionFuelUse = config.get(CATEGORY_FISSION, "fission_fuel_use", 1D, Lang.localise("gui.config.fission.fission_fuel_use.comment"), 0.001D, 255D);
		propertyFissionFuelUse.setLanguageKey("gui.config.fission.fission_fuel_use");
		Property propertyFissionHeatGeneration = config.get(CATEGORY_FISSION, "fission_heat_generation", 1D, Lang.localise("gui.config.fission.fission_heat_generation.comment"), 0D, 255D);
		propertyFissionHeatGeneration.setLanguageKey("gui.config.fission.fission_heat_generation");
		Property propertyFissionCoolingRate = config.get(CATEGORY_FISSION, "fission_cooling_rate", new double[] {60D, 90D, 90D, 120D, 130D, 120D, 150D, 140D, 120D, 160D, 80D, 160D, 80D, 120D, 110D}, Lang.localise("gui.config.fission.fission_cooling_rate.comment"), 0D, 32767D);
		propertyFissionCoolingRate.setLanguageKey("gui.config.fission.fission_cooling_rate");
		Property propertyFissionActiveCoolingRate = config.get(CATEGORY_FISSION, "fission_active_cooling_rate", new double[] {300D, 6400D, 6000D, 9600D, 8000D, 5600D, 14000D, 13200D, 10800D, 12800D, 4800D, 7200D, 5200D, 6000D, 7200D}, Lang.localise("gui.config.fission.fission_active_cooling_rate.comment"), 1D, 16777215D);
		propertyFissionActiveCoolingRate.setLanguageKey("gui.config.fission.fission_active_cooling_rate");
		Property propertyFissionWaterCoolerRequirement = config.get(CATEGORY_FISSION, "fission_water_cooler_requirement", true, Lang.localise("gui.config.fission.fission_water_cooler_requirement.comment"));
		propertyFissionWaterCoolerRequirement.setLanguageKey("gui.config.fission.fission_water_cooler_requirement");
		Property propertyFissionOverheat = config.get(CATEGORY_FISSION, "fission_overheat", true, Lang.localise("gui.config.fission.fission_overheat.comment"));
		propertyFissionOverheat.setLanguageKey("gui.config.fission.fission_overheat");
		Property propertyFissionExplosions = config.get(CATEGORY_FISSION, "fission_explosions", false, Lang.localise("gui.config.fission.fission_explosions.comment"));
		propertyFissionExplosions.setLanguageKey("gui.config.fission.fission_explosions");
		Property propertyFissionMeltdownRadiationMultiplier = config.get(CATEGORY_FISSION, "fission_meltdown_radiation_multiplier", 1D, Lang.localise("gui.config.fission.fission_meltdown_radiation_multiplier.comment"), 0D, 255D);
		propertyFissionMeltdownRadiationMultiplier.setLanguageKey("gui.config.fission.fission_meltdown_radiation_multiplier");
		Property propertyFissionMinSize = config.get(CATEGORY_FISSION, "fission_min_size", 1, Lang.localise("gui.config.fission.fission_min_size.comment"), 1, 255);
		propertyFissionMinSize.setLanguageKey("gui.config.fission.fission_min_size");
		Property propertyFissionMaxSize = config.get(CATEGORY_FISSION, "fission_max_size", 24, Lang.localise("gui.config.fission.fission_max_size.comment"), 1, 255);
		propertyFissionMaxSize.setLanguageKey("gui.config.fission.fission_max_size");
		Property propertyFissionComparatorMaxHeat = config.get(CATEGORY_FISSION, "fission_comparator_max_heat", 50, Lang.localise("gui.config.fission.fission_comparator_max_heat.comment"), 1, 100);
		propertyFissionComparatorMaxHeat.setLanguageKey("gui.config.fission.fission_comparator_max_heat");
		Property propertyFissionForceHeatComparator = config.get(CATEGORY_FISSION, "fission_force_heat_comparator", false, Lang.localise("gui.config.fission.fission_force_heat_comparator.comment"));
		propertyFissionForceHeatComparator.setLanguageKey("gui.config.fission.fission_force_heat_comparator");
		Property propertyFissionActiveCoolerMaxRate = config.get(CATEGORY_FISSION, "fission_active_cooler_max_rate", 10, Lang.localise("gui.config.fission.fission_active_cooler_max_rate.comment"), 1, 8000);
		propertyFissionActiveCoolerMaxRate.setLanguageKey("gui.config.fission.fission_active_cooler_max_rate");
		
		Property propertyFissionModeratorExtraPower = config.get(CATEGORY_FISSION, "fission_moderator_extra_power", 1D, Lang.localise("gui.config.fission.fission_moderator_extra_power.comment"), 0D, 15D);
		propertyFissionModeratorExtraPower.setLanguageKey("gui.config.fission.fission_moderator_extra_power");
		Property propertyFissionModeratorExtraHeat = config.get(CATEGORY_FISSION, "fission_moderator_extra_heat", 2D, Lang.localise("gui.config.fission.fission_moderator_extra_heat.comment"), 0D, 15D);
		propertyFissionModeratorExtraHeat.setLanguageKey("gui.config.fission.fission_moderator_extra_heat");
		Property propertyFissionNeutronReach = config.get(CATEGORY_FISSION, "fission_neutron_reach", 4, Lang.localise("gui.config.fission.fission_neutron_reach.comment"), 0, 255);
		propertyFissionNeutronReach.setLanguageKey("gui.config.fission.fission_neutron_reach");
		
		Property propertyFissionThoriumFuelTime = config.get(CATEGORY_FISSION, "fission_thorium_fuel_time", new double[] {144000D, 144000D}, Lang.localise("gui.config.fission.fission_thorium_fuel_time.comment"), 1D, Double.MAX_VALUE);
		propertyFissionThoriumFuelTime.setLanguageKey("gui.config.fission.fission_thorium_fuel_time");
		Property propertyFissionThoriumPower = config.get(CATEGORY_FISSION, "fission_thorium_power", new double[] {60D, NCMath.round(60D*1.4D, 1)}, Lang.localise("gui.config.fission.fission_thorium_power.comment"), 0D, 32767D);
		propertyFissionThoriumPower.setLanguageKey("gui.config.fission.fission_thorium_power");
		Property propertyFissionThoriumHeatGeneration = config.get(CATEGORY_FISSION, "fission_thorium_heat_generation", new double[] {18D, NCMath.round(18D*1.25D, 1)}, Lang.localise("gui.config.fission.fission_thorium_heat_generation.comment"), 0D, 32767D);
		propertyFissionThoriumHeatGeneration.setLanguageKey("gui.config.fission.fission_thorium_heat_generation");
		Property propertyFissionThoriumRadiation = config.get(CATEGORY_FISSION, "fission_thorium_radiation", new double[] {(RadSources.TBU + RadSources.CAESIUM_137)/64D, (RadSources.TBU + RadSources.CAESIUM_137)/64D}, Lang.localise("gui.config.fission.fission_thorium_radiation.comment"), 0D, 1000D);
		propertyFissionThoriumRadiation.setLanguageKey("gui.config.fission.fission_thorium_radiation");
		
		Property propertyFissionUraniumFuelTime = config.get(CATEGORY_FISSION, "fission_uranium_fuel_time", new double[] {64000D, 64000D, 64000D, 64000D, 72000D, 72000D, 72000D, 72000D}, Lang.localise("gui.config.fission.fission_uranium_fuel_time.comment"), 1D, Double.MAX_VALUE);
		propertyFissionUraniumFuelTime.setLanguageKey("gui.config.fission.fission_uranium_fuel_time");
		Property propertyFissionUraniumPower = config.get(CATEGORY_FISSION, "fission_uranium_power", new double[] {144D, NCMath.round(144D*1.4D, 1), 144D*4D, NCMath.round(144D*4D*1.4D, 1), 120D, NCMath.round(120D*1.4D, 1), 120D*4D, NCMath.round(120D*4D*1.4D, 1)}, Lang.localise("gui.config.fission.fission_uranium_power.comment"), 0D, 32767D);
		propertyFissionUraniumPower.setLanguageKey("gui.config.fission.fission_uranium_power");
		Property propertyFissionUraniumHeatGeneration = config.get(CATEGORY_FISSION, "fission_uranium_heat_generation", new double[] {60D, NCMath.round(60D*1.25D, 1), 60D*6D, NCMath.round(60D*6D*1.25D, 1), 50D, NCMath.round(50D*1.25D, 1), 50D*6D, NCMath.round(50D*6D*1.25D, 1)}, Lang.localise("gui.config.fission.fission_uranium_heat_generation.comment"), 0D, 32767D);
		propertyFissionUraniumHeatGeneration.setLanguageKey("gui.config.fission.fission_uranium_heat_generation");
		Property propertyFissionUraniumRadiation = config.get(CATEGORY_FISSION, "fission_uranium_radiation", new double[] {(RadSources.LEU_233 + RadSources.CAESIUM_137)/64D, (RadSources.LEU_233 + RadSources.CAESIUM_137)/64D, (RadSources.HEU_233 + RadSources.CAESIUM_137)/64D, (RadSources.HEU_233 + RadSources.CAESIUM_137)/64D, (RadSources.LEU_235 + RadSources.CAESIUM_137)/64D, (RadSources.LEU_235 + RadSources.CAESIUM_137)/64D, (RadSources.HEU_235 + RadSources.CAESIUM_137)/64D, (RadSources.HEU_235 + RadSources.CAESIUM_137)/64D}, Lang.localise("gui.config.fission.fission_uranium_radiation.comment"), 0D, 1000D);
		propertyFissionUraniumRadiation.setLanguageKey("gui.config.fission.fission_uranium_radiation");
		
		Property propertyFissionNeptuniumFuelTime = config.get(CATEGORY_FISSION, "fission_neptunium_fuel_time", new double[] {102000D, 102000D, 102000D, 102000D}, Lang.localise("gui.config.fission.fission_neptunium_fuel_time.comment"), 1D, Double.MAX_VALUE);
		propertyFissionNeptuniumFuelTime.setLanguageKey("gui.config.fission.fission_neptunium_fuel_time");
		Property propertyFissionNeptuniumPower = config.get(CATEGORY_FISSION, "fission_neptunium_power", new double[] {90D, NCMath.round(90D*1.4D, 1), 90D*4D, NCMath.round(90D*4D*1.4D, 1)}, Lang.localise("gui.config.fission.fission_neptunium_power.comment"), 0D, 32767D);
		propertyFissionNeptuniumPower.setLanguageKey("gui.config.fission.fission_neptunium_power");
		Property propertyFissionNeptuniumHeatGeneration = config.get(CATEGORY_FISSION, "fission_neptunium_heat_generation", new double[] {36D, NCMath.round(36D*1.25D, 1), 36D*6D, NCMath.round(36D*6D*1.25D, 1)}, Lang.localise("gui.config.fission.fission_neptunium_heat_generation.comment"), 0D, 32767D);
		propertyFissionNeptuniumHeatGeneration.setLanguageKey("gui.config.fission.fission_neptunium_heat_generation");
		Property propertyFissionNeptuniumRadiation = config.get(CATEGORY_FISSION, "fission_neptunium_radiation", new double[] {(RadSources.LEN_236 + RadSources.CAESIUM_137)/64D, (RadSources.LEN_236 + RadSources.CAESIUM_137)/64D, (RadSources.HEN_236 + RadSources.CAESIUM_137)/64D, (RadSources.HEN_236 + RadSources.CAESIUM_137)/64D}, Lang.localise("gui.config.fission.fission_neptunium_radiation.comment"), 0D, 1000D);
		propertyFissionNeptuniumRadiation.setLanguageKey("gui.config.fission.fission_neptunium_radiation");
		
		Property propertyFissionPlutoniumFuelTime = config.get(CATEGORY_FISSION, "fission_plutonium_fuel_time", new double[] {92000D, 92000D, 92000D, 92000D, 60000D, 60000D, 60000D, 60000D}, Lang.localise("gui.config.fission.fission_plutonium_fuel_time.comment"), 1D, Double.MAX_VALUE);
		propertyFissionPlutoniumFuelTime.setLanguageKey("gui.config.fission.fission_plutonium_fuel_time");
		Property propertyFissionPlutoniumPower = config.get(CATEGORY_FISSION, "fission_plutonium_power", new double[] {105D, NCMath.round(105D*1.4D, 1), 105D*4D, NCMath.round(105D*4D*1.4D, 1), 165D, NCMath.round(165D*1.4D, 1), 165D*4D, NCMath.round(165D*4D*1.4D, 1)}, Lang.localise("gui.config.fission.fission_plutonium_power.comment"), 0D, 32767D);
		propertyFissionPlutoniumPower.setLanguageKey("gui.config.fission.fission_plutonium_power");
		Property propertyFissionPlutoniumHeatGeneration = config.get(CATEGORY_FISSION, "fission_plutonium_heat_generation", new double[] {40D, NCMath.round(40D*1.25D, 1), 40D*6D, NCMath.round(40D*6D*1.25D, 1), 70D, NCMath.round(70D*1.25D, 1), 70D*6D, NCMath.round(70D*6D*1.25D, 1)}, Lang.localise("gui.config.fission.fission_plutonium_heat_generation.comment"), 0D, 32767D);
		propertyFissionPlutoniumHeatGeneration.setLanguageKey("gui.config.fission.fission_plutonium_heat_generation");
		Property propertyFissionPlutoniumRadiation = config.get(CATEGORY_FISSION, "fission_plutonium_radiation", new double[] {(RadSources.LEP_239 + RadSources.CAESIUM_137)/64D, (RadSources.LEP_239 + RadSources.CAESIUM_137)/64D, (RadSources.HEP_239 + RadSources.CAESIUM_137)/64D, (RadSources.HEP_239 + RadSources.CAESIUM_137)/64D, (RadSources.LEP_241 + RadSources.CAESIUM_137)/64D, (RadSources.LEP_241 + RadSources.CAESIUM_137)/64D, (RadSources.HEP_241 + RadSources.CAESIUM_137)/64D, (RadSources.HEP_241 + RadSources.CAESIUM_137)/64D}, Lang.localise("gui.config.fission.fission_plutonium_radiation.comment"), 0D, 1000D);
		propertyFissionPlutoniumRadiation.setLanguageKey("gui.config.fission.fission_plutonium_radiation");
		
		Property propertyFissionMOXFuelTime = config.get(CATEGORY_FISSION, "fission_mox_fuel_time", new double[] {84000D, 56000D}, Lang.localise("gui.config.fission.fission_mox_fuel_time.comment"), 1D, Double.MAX_VALUE);
		propertyFissionMOXFuelTime.setLanguageKey("gui.config.fission.fission_mox_fuel_time");
		Property propertyFissionMOXPower = config.get(CATEGORY_FISSION, "fission_mox_power", new double[] {NCMath.round(111D*1.4D, 1), NCMath.round(174D*1.4D, 1)}, Lang.localise("gui.config.fission.fission_mox_power.comment"), 0D, 32767D);
		propertyFissionMOXPower.setLanguageKey("gui.config.fission.fission_mox_power");
		Property propertyFissionMOXHeatGeneration = config.get(CATEGORY_FISSION, "fission_mox_heat_generation", new double[] {NCMath.round(46D*1.25D, 1), NCMath.round(78D*1.25D, 1)}, Lang.localise("gui.config.fission.fission_mox_heat_generation.comment"), 0D, 32767D);
		propertyFissionMOXHeatGeneration.setLanguageKey("gui.config.fission.fission_mox_heat_generation");
		Property propertyFissionMOXRadiation = config.get(CATEGORY_FISSION, "fission_mox_radiation", new double[] {(RadSources.MOX_239 + RadSources.CAESIUM_137)/64D, (RadSources.MOX_241 + RadSources.CAESIUM_137)/64D}, Lang.localise("gui.config.fission.fission_mox_radiation.comment"), 0D, 1000D);
		propertyFissionMOXRadiation.setLanguageKey("gui.config.fission.fission_mox_radiation");
		
		Property propertyFissionAmericiumFuelTime = config.get(CATEGORY_FISSION, "fission_americium_fuel_time", new double[] {54000D, 54000D, 54000D, 54000D}, Lang.localise("gui.config.fission.fission_americium_fuel_time.comment"), 1D, Double.MAX_VALUE);
		propertyFissionAmericiumFuelTime.setLanguageKey("gui.config.fission.fission_americium_fuel_time");
		Property propertyFissionAmericiumPower = config.get(CATEGORY_FISSION, "fission_americium_power", new double[] {192D, NCMath.round(192D*1.4D, 1), 192D*4D, NCMath.round(192D*4D*1.4D, 1)}, Lang.localise("gui.config.fission.fission_americium_power.comment"), 0D, 32767D);
		propertyFissionAmericiumPower.setLanguageKey("gui.config.fission.fission_americium_power");
		Property propertyFissionAmericiumHeatGeneration = config.get(CATEGORY_FISSION, "fission_americium_heat_generation", new double[] {94D, NCMath.round(94D*1.25D, 1), 94D*6D, NCMath.round(94D*6D*1.25D, 1)}, Lang.localise("gui.config.fission.fission_americium_heat_generation.comment"), 0D, 32767D);
		propertyFissionAmericiumHeatGeneration.setLanguageKey("gui.config.fission.fission_americium_heat_generation");
		Property propertyFissionAmericiumRadiation = config.get(CATEGORY_FISSION, "fission_americium_radiation", new double[] {(RadSources.LEA_242 + RadSources.CAESIUM_137)/64D, (RadSources.LEA_242 + RadSources.CAESIUM_137)/64D, (RadSources.HEA_242 + RadSources.CAESIUM_137)/64D, (RadSources.HEA_242 + RadSources.CAESIUM_137)/64D}, Lang.localise("gui.config.fission.fission_americium_radiation.comment"), 0D, 1000D);
		propertyFissionAmericiumRadiation.setLanguageKey("gui.config.fission.fission_americium_radiation");
		
		Property propertyFissionCuriumFuelTime = config.get(CATEGORY_FISSION, "fission_curium_fuel_time", new double[] {52000D, 52000D, 52000D, 52000D, 68000D, 68000D, 68000D, 68000D, 78000D, 78000D, 78000D, 78000D}, Lang.localise("gui.config.fission.fission_curium_fuel_time.comment"), 1D, Double.MAX_VALUE);
		propertyFissionCuriumFuelTime.setLanguageKey("gui.config.fission.fission_curium_fuel_time");
		Property propertyFissionCuriumPower = config.get(CATEGORY_FISSION, "fission_curium_power", new double[] {210D, NCMath.round(210D*1.4D, 1), 210D*4D, NCMath.round(210D*4D*1.4D, 1), 162D, NCMath.round(162D*1.4D, 1), 162D*4D, NCMath.round(162D*4D*1.4D, 1), 138D, NCMath.round(138D*1.4D, 1), 138D*4D, NCMath.round(138D*4D*1.4D, 1)}, Lang.localise("gui.config.fission.fission_curium_power.comment"), 0D, 32767D);
		propertyFissionCuriumPower.setLanguageKey("gui.config.fission.fission_curium_power");
		Property propertyFissionCuriumHeatGeneration = config.get(CATEGORY_FISSION, "fission_curium_heat_generation", new double[] {112D, NCMath.round(112D*1.25D, 1), 112D*6D, NCMath.round(112D*6D*1.25D, 1), 68D, NCMath.round(68D*1.25D, 1), 68D*6D, NCMath.round(68D*6D*1.25D, 1), 54D, NCMath.round(54D*1.25D, 1), 54D*6D, NCMath.round(54D*6D*1.25D, 1)}, Lang.localise("gui.config.fission.fission_curium_heat_generation.comment"), 0D, 32767D);
		propertyFissionCuriumHeatGeneration.setLanguageKey("gui.config.fission.fission_curium_heat_generation");
		Property propertyFissionCuriumRadiation = config.get(CATEGORY_FISSION, "fission_curium_radiation", new double[] {(RadSources.LECm_243 + RadSources.CAESIUM_137)/64D, (RadSources.LECm_243 + RadSources.CAESIUM_137)/64D, (RadSources.HECm_243 + RadSources.CAESIUM_137)/64D, (RadSources.HECm_243 + RadSources.CAESIUM_137)/64D, (RadSources.LECm_245 + RadSources.CAESIUM_137)/64D, (RadSources.LECm_245 + RadSources.CAESIUM_137)/64D, (RadSources.HECm_245 + RadSources.CAESIUM_137)/64D, (RadSources.HECm_245 + RadSources.CAESIUM_137)/64D, (RadSources.LECm_247 + RadSources.CAESIUM_137)/64D, (RadSources.LECm_247 + RadSources.CAESIUM_137)/64D, (RadSources.HECm_247 + RadSources.CAESIUM_137)/64D, (RadSources.HECm_247 + RadSources.CAESIUM_137)/64D}, Lang.localise("gui.config.fission.fission_curium_radiation.comment"), 0D, 1000D);
		propertyFissionCuriumRadiation.setLanguageKey("gui.config.fission.fission_curium_radiation");
		
		Property propertyFissionBerkeliumFuelTime = config.get(CATEGORY_FISSION, "fission_berkelium_fuel_time", new double[] {86000D, 86000D, 86000D, 86000D}, Lang.localise("gui.config.fission.fission_berkelium_fuel_time.comment"), 1D, Double.MAX_VALUE);
		propertyFissionBerkeliumFuelTime.setLanguageKey("gui.config.fission.fission_berkelium_fuel_time");
		Property propertyFissionBerkeliumPower = config.get(CATEGORY_FISSION, "fission_berkelium_power", new double[] {135D, NCMath.round(135D*1.4D, 1), 135D*4D, NCMath.round(135D*4D*1.4D, 1)}, Lang.localise("gui.config.fission.fission_berkelium_power.comment"), 0D, 32767D);
		propertyFissionBerkeliumPower.setLanguageKey("gui.config.fission.fission_berkelium_power");
		Property propertyFissionBerkeliumHeatGeneration = config.get(CATEGORY_FISSION, "fission_berkelium_heat_generation", new double[] {52D, NCMath.round(52D*1.25D, 1), 52D*6D, NCMath.round(52D*6D*1.25D, 1)}, Lang.localise("gui.config.fission.fission_berkelium_heat_generation.comment"), 0D, 32767D);
		propertyFissionBerkeliumHeatGeneration.setLanguageKey("gui.config.fission.fission_berkelium_heat_generation");
		Property propertyFissionBerkeliumRadiation = config.get(CATEGORY_FISSION, "fission_berkelium_radiation", new double[] {(RadSources.LEB_248 + RadSources.CAESIUM_137)/64D, (RadSources.LEB_248 + RadSources.CAESIUM_137)/64D, (RadSources.HEB_248 + RadSources.CAESIUM_137)/64D, (RadSources.HEB_248 + RadSources.CAESIUM_137)/64D}, Lang.localise("gui.config.fission.fission_berkelium_radiation.comment"), 0D, 1000D);
		propertyFissionBerkeliumRadiation.setLanguageKey("gui.config.fission.fission_berkelium_radiation");
		
		Property propertyFissionCaliforniumFuelTime = config.get(CATEGORY_FISSION, "fission_californium_fuel_time", new double[] {60000D, 60000D, 60000D, 60000D, 58000D, 58000D, 58000D, 58000D}, Lang.localise("gui.config.fission.fission_californium_fuel_time.comment"), 1D, Double.MAX_VALUE);
		propertyFissionCaliforniumFuelTime.setLanguageKey("gui.config.fission.fission_californium_fuel_time");
		Property propertyFissionCaliforniumPower = config.get(CATEGORY_FISSION, "fission_californium_power", new double[] {216D, NCMath.round(216D*1.4D, 1), 216D*4D, NCMath.round(216D*4D*1.4D, 1), 225D, NCMath.round(225D*1.4D, 1), 225D*4D, NCMath.round(225D*4D*1.4D, 1)}, Lang.localise("gui.config.fission.fission_californium_power.comment"), 0D, 32767D);
		propertyFissionCaliforniumPower.setLanguageKey("gui.config.fission.fission_californium_power");
		Property propertyFissionCaliforniumHeatGeneration = config.get(CATEGORY_FISSION, "fission_californium_heat_generation", new double[] {116D, NCMath.round(116D*1.25D, 1), 116D*6D, NCMath.round(116D*6D*1.25D, 1), 120D, NCMath.round(120D*1.25D, 1), 120D*6D, NCMath.round(120D*6D*1.25D, 1)}, Lang.localise("gui.config.fission.fission_californium_heat_generation.comment"), 0D, 32767D);
		propertyFissionCaliforniumHeatGeneration.setLanguageKey("gui.config.fission.fission_californium_heat_generation");
		Property propertyFissionCaliforniumRadiation = config.get(CATEGORY_FISSION, "fission_californium_radiation", new double[] {(RadSources.LECf_249 + RadSources.CAESIUM_137)/64D, (RadSources.LECf_249 + RadSources.CAESIUM_137)/64D, (RadSources.HECf_249 + RadSources.CAESIUM_137)/64D, (RadSources.HECf_249 + RadSources.CAESIUM_137)/64D, (RadSources.LECf_251 + RadSources.CAESIUM_137)/64D, (RadSources.LECf_251 + RadSources.CAESIUM_137)/64D, (RadSources.HECf_251 + RadSources.CAESIUM_137)/64D, (RadSources.HECf_251 + RadSources.CAESIUM_137)/64D}, Lang.localise("gui.config.fission.fission_californium_radiation.comment"), 0D, 1000D);
		propertyFissionCaliforniumRadiation.setLanguageKey("gui.config.fission.fission_californium_radiation");
		
		Property propertyFusionBasePower = config.get(CATEGORY_FUSION, "fusion_base_power", 1D, Lang.localise("gui.config.fusion.fusion_base_power.comment"), 0D, 255D);
		propertyFusionBasePower.setLanguageKey("gui.config.fusion.fusion_base_power");
		Property propertyFusionFuelUse = config.get(CATEGORY_FUSION, "fusion_fuel_use", 1D, Lang.localise("gui.config.fusion.fusion_fuel_use.comment"), 0.001D, 255D);
		propertyFusionFuelUse.setLanguageKey("gui.config.fusion.fusion_fuel_use");
		Property propertyFusionHeatGeneration = config.get(CATEGORY_FUSION, "fusion_heat_generation", 1D, Lang.localise("gui.config.fusion.fusion_heat_generation.comment"), 0D, 255D);
		propertyFusionHeatGeneration.setLanguageKey("gui.config.fusion.fusion_heat_generation");
		Property propertyFusionHeatingMultiplier = config.get(CATEGORY_FUSION, "fusion_heating_multiplier", 1D, Lang.localise("gui.config.fusion.fusion_heating_multiplier.comment"), 0D, 255D);
		propertyFusionHeatingMultiplier.setLanguageKey("gui.config.fusion.fusion_heating_multiplier");
		Property propertyFusionOverheat = config.get(CATEGORY_FUSION, "fusion_overheat", true, Lang.localise("gui.config.fusion.fusion_overheat.comment"));
		propertyFusionOverheat.setLanguageKey("gui.config.fusion.fusion_overheat");
		Property propertyFusionMeltdownRadiationMultiplier = config.get(CATEGORY_FUSION, "fusion_meltdown_radiation_multiplier", 1D, Lang.localise("gui.config.fusion.fusion_meltdown_radiation_multiplier.comment"), 0D, 255D);
		propertyFusionMeltdownRadiationMultiplier.setLanguageKey("gui.config.fusion.fusion_meltdown_radiation_multiplier");
		Property propertyFusionActiveCooling = config.get(CATEGORY_FUSION, "fusion_active_cooling", true, Lang.localise("gui.config.fusion.fusion_active_cooling.comment"));
		propertyFusionActiveCooling.setLanguageKey("gui.config.fusion.fusion_active_cooling");
		Property propertyFusionActiveCoolingRate = config.get(CATEGORY_FUSION, "fusion_active_cooling_rate", new double[] {400D, 25600D, 24000D, 38400D, 32000D, 22400D, 56000D, 52800D, 43200D, 51200D, 19200D, 28800D, 20800D, 24000D, 28800D}, Lang.localise("gui.config.fusion.fusion_active_cooling_rate.comment"), 1D, 16777215D);
		propertyFusionActiveCoolingRate.setLanguageKey("gui.config.fusion.fusion_active_cooling_rate");
		Property propertyFusionMinSize = config.get(CATEGORY_FUSION, "fusion_min_size", 1, Lang.localise("gui.config.fusion.fusion_min_size.comment"), 1, 255);
		propertyFusionMinSize.setLanguageKey("gui.config.fusion.fusion_min_size");
		Property propertyFusionMaxSize = config.get(CATEGORY_FUSION, "fusion_max_size", 24, Lang.localise("gui.config.fusion.fusion_max_size.comment"), 1, 255);
		propertyFusionMaxSize.setLanguageKey("gui.config.fusion.fusion_max_size");
		Property propertyFusionComparatorMaxEfficiency = config.get(CATEGORY_FUSION, "fusion_comparator_max_efficiency", 90, Lang.localise("gui.config.fusion.fusion_comparator_max_efficiency.comment"), 1, 100);
		propertyFusionComparatorMaxEfficiency.setLanguageKey("gui.config.fusion.fusion_comparator_max_efficiency");
		Property propertyFusionElectromagnetPower = config.get(CATEGORY_FUSION, "fusion_electromagnet_power", 4000, Lang.localise("gui.config.fusion.fusion_electromagnet_power.comment"), 0, Integer.MAX_VALUE);
		propertyFusionElectromagnetPower.setLanguageKey("gui.config.fusion.fusion_electromagnet_power");
		Property propertyFusionEnableSound = config.get(CATEGORY_FUSION, "fusion_enable_sound", true, Lang.localise("gui.config.fusion.fusion_enable_sound.comment"));
		propertyFusionEnableSound.setLanguageKey("gui.config.fusion.fusion_enable_sound");
		Property propertyFusionPlasmaCraziness = config.get(CATEGORY_FUSION, "fusion_plasma_craziness", true, Lang.localise("gui.config.fusion.fusion_plasma_craziness.comment"));
		propertyFusionPlasmaCraziness.setLanguageKey("gui.config.fusion.fusion_plasma_craziness");
		
		Property propertyFusionFuelTime = config.get(CATEGORY_FUSION, "fusion_fuel_time", new double[] {100D, 150D, 200D, 200D, 350D, 400D, 600D, 200D, 250D, 250D, 400D, 450D, 650D, 300D, 300D, 450D, 500D, 700D, 300D, 450D, 500D, 700D, 600D, 650D, 850D, 700D, 900D, 1100D}, Lang.localise("gui.config.fusion.fusion_fuel_time.comment"), 1D, 32767D);
		propertyFusionFuelTime.setLanguageKey("gui.config.fusion.fusion_fuel_time");
		Property propertyFusionPower = config.get(CATEGORY_FUSION, "fusion_power", new double[] {442D, 1123D, 0.3D, 3036D, 351D, 1330D, 444D, 507D, 1726D, 2252D, 1716D, 859D, 261D, 901D, 1099D, 915D, 435D, 7D, 1315D, 1151D, 727D, 140D, 1068D, 552D, 157D, 229D, 0.45D, 0.05D}, Lang.localise("gui.config.fusion.fusion_power.comment"), 0D, 32767D);
		propertyFusionPower.setLanguageKey("gui.config.fusion.fusion_power");
		Property propertyFusionHeatVariable = config.get(CATEGORY_FUSION, "fusion_heat_variable", new double[] {3635D, 1022D, 4964D, 2740D, 5972D, 4161D, 13432D, 949D, 670D, 2160D, 3954D, 4131D, 13853D, 736D, 2137D, 4079D, 4522D, 27254D, 5420D, 7800D, 7937D, 24266D, 11268D, 11927D, 30399D, 13630D, 166414D, 293984D}, Lang.localise("gui.config.fusion.fusion_heat_variable.comment"), 500D, 20000D);
		propertyFusionHeatVariable.setLanguageKey("gui.config.fusion.fusion_heat_variable");
		Property propertyFusionRadiation = config.get(CATEGORY_FUSION, "fusion_radiation", new double[] {RadSources.FUSION/64D, RadSources.FUSION/64D, (RadSources.FUSION + RadSources.TRITIUM + RadSources.NEUTRON)/64D, RadSources.FUSION/64D, (RadSources.FUSION + RadSources.TRITIUM)/64D, RadSources.FUSION/64D, RadSources.FUSION/64D, (RadSources.FUSION + RadSources.TRITIUM/2D + RadSources.NEUTRON/2D)/64D, (RadSources.FUSION + RadSources.TRITIUM + RadSources.NEUTRON)/64D, RadSources.FUSION/64D, RadSources.FUSION/64D, (RadSources.FUSION + RadSources.NEUTRON)/64D, (RadSources.FUSION + RadSources.NEUTRON)/64D, (RadSources.FUSION + 2*RadSources.TRITIUM + 2*RadSources.NEUTRON)/64D, (RadSources.FUSION + RadSources.NEUTRON)/64D, (RadSources.FUSION + RadSources.NEUTRON)/64D, (RadSources.FUSION + 2*RadSources.NEUTRON)/64D, (RadSources.FUSION + 2*RadSources.NEUTRON)/64D, RadSources.FUSION/64D, RadSources.FUSION/64D, RadSources.FUSION/64D, RadSources.FUSION/64D, RadSources.FUSION/64D, (RadSources.FUSION + RadSources.NEUTRON)/64D, (RadSources.FUSION + RadSources.NEUTRON)/64D, (RadSources.FUSION + 2*RadSources.NEUTRON)/64D, (RadSources.FUSION + 2*RadSources.NEUTRON)/64D, (RadSources.FUSION + 2*RadSources.NEUTRON)/64D}, Lang.localise("gui.config.fusion.fusion_radiation.comment"), 0D, 1000D);
		propertyFusionRadiation.setLanguageKey("gui.config.fusion.fusion_radiation");
		
		Property propertySaltFissionPower = config.get(CATEGORY_SALT_FISSION, "salt_fission_power", 1D, Lang.localise("gui.config.salt_fission.salt_fission_power.comment"), 0D, 255D);
		propertySaltFissionPower.setLanguageKey("gui.config.salt_fission.salt_fission_power");
		Property propertySaltFissionFuelUse = config.get(CATEGORY_SALT_FISSION, "salt_fission_fuel_use", 1D, Lang.localise("gui.config.salt_fission.salt_fission_fuel_use.comment"), 0.001D, 255D);
		propertySaltFissionFuelUse.setLanguageKey("gui.config.salt_fission.salt_fission_fuel_use");
		Property propertySaltFissionHeatGeneration = config.get(CATEGORY_SALT_FISSION, "salt_fission_heat_generation", 1D, Lang.localise("gui.config.salt_fission.salt_fission_heat_generation.comment"), 0D, 255D);
		propertySaltFissionHeatGeneration.setLanguageKey("gui.config.salt_fission.salt_fission_heat_generation");
		Property propertySaltFissionOverheat = config.get(CATEGORY_SALT_FISSION, "salt_fission_overheat", true, Lang.localise("gui.config.salt_fission.salt_fission_overheat.comment"));
		propertySaltFissionOverheat.setLanguageKey("gui.config.salt_fission.salt_fission_overheat");
		Property propertySaltFissionMeltdownRadiationMultiplier = config.get(CATEGORY_SALT_FISSION, "salt_fission_meltdown_radiation_multiplier", 1D, Lang.localise("gui.config.salt_fission.salt_fission_meltdown_radiation_multiplier.comment"), 0D, 255D);
		propertySaltFissionMeltdownRadiationMultiplier.setLanguageKey("gui.config.salt_fission.salt_fission_meltdown_radiation_multiplier");
		Property propertySaltFissionMinSize = config.get(CATEGORY_SALT_FISSION, "salt_fission_min_size", 1, Lang.localise("gui.config.salt_fission.salt_fission_min_size.comment"), 1, 255);
		propertySaltFissionMinSize.setLanguageKey("gui.config.salt_fission.salt_fission_min_size");
		Property propertySaltFissionMaxSize = config.get(CATEGORY_SALT_FISSION, "salt_fission_max_size", 24, Lang.localise("gui.config.salt_fission.salt_fission_max_size.comment"), 1, 255);
		propertySaltFissionMaxSize.setLanguageKey("gui.config.salt_fission.salt_fission_max_size");
		Property propertySaltFissionCoolingRate = config.get(CATEGORY_SALT_FISSION, "salt_fission_cooling_rate", new double[] {240D, 360D, 360D, 480D, 520D, 480D, 600D, 560D, 480D, 640D, 320D, 640D, 320D, 480D, 440D}, Lang.localise("gui.config.salt_fission.salt_fission_cooling_rate.comment"), 1D, 16777215D);
		propertySaltFissionCoolingRate.setLanguageKey("gui.config.salt_fission.salt_fission_cooling_rate");
		Property propertySaltFissionRedstoneMaxHeat = config.get(CATEGORY_SALT_FISSION, "salt_fission_redstone_max_heat", 50, Lang.localise("gui.config.salt_fission.salt_fission_redstone_max_heat.comment"), 1, 100);
		propertySaltFissionRedstoneMaxHeat.setLanguageKey("gui.config.salt_fission.salt_fission_redstone_max_heat");
		Property propertySaltFissionMaxDistributionRate = config.get(CATEGORY_SALT_FISSION, "salt_fission_max_distribution_rate", 4, Lang.localise("gui.config.salt_fission.salt_fission_max_distribution_rate.comment"), 1, 1000);
		propertySaltFissionMaxDistributionRate.setLanguageKey("gui.config.salt_fission.salt_fission_max_distribution_rate");
		
		Property propertyHeatExchangerMinSize = config.get(CATEGORY_HEAT_EXCHANGER, "heat_exchanger_min_size", 1, Lang.localise("gui.config.heat_exchanger.heat_exchanger_min_size.comment"), 1, 255);
		propertyHeatExchangerMinSize.setLanguageKey("gui.config.heat_exchanger.heat_exchanger_min_size");
		Property propertyHeatExchangerMaxSize = config.get(CATEGORY_HEAT_EXCHANGER, "heat_exchanger_max_size", 24, Lang.localise("gui.config.heat_exchanger.heat_exchanger_max_size.comment"), 1, 255);
		propertyHeatExchangerMaxSize.setLanguageKey("gui.config.heat_exchanger.heat_exchanger_max_size");
		Property propertyHeatExchangerConductivity = config.get(CATEGORY_HEAT_EXCHANGER, "heat_exchanger_conductivity", new double[] {1D, 1.1D, 1.25D}, Lang.localise("gui.config.heat_exchanger.heat_exchanger_conductivity.comment"), 0.01D, 15D);
		propertyHeatExchangerConductivity.setLanguageKey("gui.config.heat_exchanger.heat_exchanger_conductivity");
		Property propertyHeatExchangerCoolantMult = config.get(CATEGORY_HEAT_EXCHANGER, "heat_exchanger_coolant_mult", 125D, Lang.localise("gui.config.heat_exchanger.heat_exchanger_coolant_mult.comment"), 1D, 10000D);
		propertyHeatExchangerCoolantMult.setLanguageKey("gui.config.heat_exchanger.heat_exchanger_coolant_mult");
		Property propertyHeatExchangerAlternateExhaustRecipe = config.get(CATEGORY_HEAT_EXCHANGER, "heat_exchanger_alternate_exhaust_recipe", false, Lang.localise("gui.config.heat_exchanger.heat_exchanger_alternate_exhaust_recipe.comment"));
		propertyHeatExchangerAlternateExhaustRecipe.setLanguageKey("gui.config.heat_exchanger.heat_exchanger_alternate_exhaust_recipe");
		
		Property propertyTurbineMinSize = config.get(CATEGORY_TURBINE, "turbine_min_size", 1, Lang.localise("gui.config.turbine.turbine_min_size.comment"), 1, 255);
		propertyTurbineMinSize.setLanguageKey("gui.config.turbine.turbine_min_size");
		Property propertyTurbineMaxSize = config.get(CATEGORY_TURBINE, "turbine_max_size", 24, Lang.localise("gui.config.turbine.turbine_max_size.comment"), 1, 255);
		propertyTurbineMaxSize.setLanguageKey("gui.config.turbine.turbine_max_size");
		Property propertyTurbineBladeEfficiency = config.get(CATEGORY_TURBINE, "turbine_blade_efficiency", new double[] {1D, 1.1D, 1.25D}, Lang.localise("gui.config.turbine.turbine_blade_efficiency.comment"), 0.01D, 15D);
		propertyTurbineBladeEfficiency.setLanguageKey("gui.config.turbine.turbine_blade_efficiency");
		Property propertyTurbineBladeExpansion = config.get(CATEGORY_TURBINE, "turbine_blade_expansion", new double[] {1.4D, 1.6D, 1.8D}, Lang.localise("gui.config.turbine.turbine_blade_expansion.comment"), 1D, 15D);
		propertyTurbineBladeExpansion.setLanguageKey("gui.config.turbine.turbine_blade_expansion");
		Property propertyTurbineStatorExpansion = config.get(CATEGORY_TURBINE, "turbine_stator_expansion", 0.75D, Lang.localise("gui.config.turbine.turbine_stator_expansion.comment"), 0.01D, 1D);
		propertyTurbineStatorExpansion.setLanguageKey("gui.config.turbine.turbine_stator_expansion");
		Property propertyTurbineCoilConductivity = config.get(CATEGORY_TURBINE, "turbine_coil_conductivity", new double[] {0.86D, 0.9D, 0.98D, 1.04D, 1.1D, 1.12D}, Lang.localise("gui.config.turbine.turbine_coil_conductivity.comment"), 0.01D, 15D);
		propertyTurbineCoilConductivity.setLanguageKey("gui.config.turbine.turbine_coil_conductivity");
		Property propertyTurbinePowerPerMB = config.get(CATEGORY_TURBINE, "turbine_power_per_mb", new double[] {16D, 4D, 4D}, Lang.localise("gui.config.turbine.turbine_power_per_mb.comment"), 0D, 255D);
		propertyTurbinePowerPerMB.setLanguageKey("gui.config.turbine.turbine_power_per_mb");
		Property propertyTurbineExpansionLevel = config.get(CATEGORY_TURBINE, "turbine_expansion_level", new double[] {4D, 2D, 2D}, Lang.localise("gui.config.turbine.turbine_expansion_level.comment"), 1D, 255D);
		propertyTurbineExpansionLevel.setLanguageKey("gui.config.turbine.turbine_expansion_level");
		Property propertyTurbineMBPerBlade = config.get(CATEGORY_TURBINE, "turbine_mb_per_blade", 100, Lang.localise("gui.config.turbine.turbine_mb_per_blade.comment"), 1, 32767);
		propertyTurbineMBPerBlade.setLanguageKey("gui.config.turbine.turbine_mb_per_blade");
		
		Property propertyAcceleratorElectromagnetPower = config.get(CATEGORY_ACCELERATOR, "accelerator_electromagnet_power", 20000, Lang.localise("gui.config.accelerator.accelerator_electromagnet_power.comment"), 0, Integer.MAX_VALUE);
		propertyAcceleratorElectromagnetPower.setLanguageKey("gui.config.accelerator.accelerator_electromagnet_power");
		Property propertyAcceleratorSupercoolerCoolant = config.get(CATEGORY_ACCELERATOR, "accelerator_supercooler_coolant", 4, Lang.localise("gui.config.accelerator.accelerator_supercooler_coolant.comment"), 0, 32767);
		propertyAcceleratorSupercoolerCoolant.setLanguageKey("gui.config.accelerator.accelerator_supercooler_coolant");
		
		Property propertyBatteryCapacity = config.get(CATEGORY_ENERGY_STORAGE, "battery_capacity", new int[] {1600000, 6400000, 25600000, 102400000, 32000000, 128000000, 512000000, 2048000000}, Lang.localise("gui.config.energy_storage.battery_capacity.comment"), 1, Integer.MAX_VALUE);
		propertyBatteryCapacity.setLanguageKey("gui.config.energy_storage.battery_capacity");
		
		Property propertyToolMiningLevel = config.get(CATEGORY_TOOLS, "tool_mining_level", new int[] {2, 2, 3, 3, 3, 3, 4, 4}, Lang.localise("gui.config.tools.tool_mining_level.comment"), 0, 15);
		propertyToolMiningLevel.setLanguageKey("gui.config.tools.tool_mining_level");
		Property propertyToolDurability = config.get(CATEGORY_TOOLS, "tool_durability", new int[] {547, 547*5, 929, 929*5, 1245, 1245*5, 1928, 1928*5}, Lang.localise("gui.config.tools.tool_durability.comment"), 1, 32767);
		propertyToolDurability.setLanguageKey("gui.config.tools.tool_durability");
		Property propertyToolSpeed = config.get(CATEGORY_TOOLS, "tool_speed", new double[] {8D, 8D, 10D, 10D, 11D, 11D, 12D, 12D}, Lang.localise("gui.config.tools.tool_speed.comment"), 1D, 255D);
		propertyToolSpeed.setLanguageKey("gui.config.tools.tool_speed");
		Property propertyToolAttackDamage = config.get(CATEGORY_TOOLS, "tool_attack_damage", new double[] {2.5D, 2.5D, 3D, 3D, 3D, 3D, 3.5D, 3.5D}, Lang.localise("gui.config.tools.tool_attack_damage.comment"), 0D, 255D);
		propertyToolAttackDamage.setLanguageKey("gui.config.tools.tool_attack_damage");
		Property propertyToolEnchantability = config.get(CATEGORY_TOOLS, "tool_enchantability", new int[] {6, 6, 15, 15, 12, 12, 20, 20}, Lang.localise("gui.config.tools.tool_enchantability.comment"), 1, 255);
		propertyToolEnchantability.setLanguageKey("gui.config.tools.tool_enchantability");
		Property propertyToolHandleModifier = config.get(CATEGORY_TOOLS, "tool_handle_modifier", new double[] {0.85D, 1.1D, 1D, 0.75D}, Lang.localise("gui.config.tools.tool_handle_modifier.comment"), 0.01D, 10D);
		propertyToolHandleModifier.setLanguageKey("gui.config.tools.tool_handle_modifier");
		Property propertyToolTiCRegister = config.get(CATEGORY_TOOLS, "tool_tic_register", new boolean[] {true, true, true, true, true, true, true, true}, Lang.localise("gui.config.tools.tool_tic_register.comment"));
		propertyToolTiCRegister.setLanguageKey("gui.config.tools.tool_tic_register");
		
		Property propertyArmorDurability = config.get(CATEGORY_ARMOR, "armor_durability", new int[] {22, 30, 34, 42, 0}, Lang.localise("gui.config.armor.armor_durability.comment"), 1, 127);
		propertyArmorDurability.setLanguageKey("gui.config.armor.armor_durability");
		Property propertyArmorBoron = config.get(CATEGORY_ARMOR, "armor_boron", new int[] {2, 5, 7, 3}, Lang.localise("gui.config.armor.armor_boron.comment"), 1, 25);
		propertyArmorBoron.setLanguageKey("gui.config.armor.armor_boron");
		Property propertyArmorTough = config.get(CATEGORY_ARMOR, "armor_tough", new int[] {3, 6, 7, 3}, Lang.localise("gui.config.armor.armor_tough.comment"), 1, 25);
		propertyArmorTough.setLanguageKey("gui.config.armor.armor_tough");
		Property propertyArmorHardCarbon = config.get(CATEGORY_ARMOR, "armor_hard_carbon", new int[] {3, 5, 7, 3}, Lang.localise("gui.config.armor.armor_hard_carbon.comment"), 1, 25);
		propertyArmorHardCarbon.setLanguageKey("gui.config.armor.armor_hard_carbon");
		Property propertyArmorBoronNitride = config.get(CATEGORY_ARMOR, "armor_boron_nitride", new int[] {3, 6, 8, 3}, Lang.localise("gui.config.armor.armor_boron_nitride.comment"), 1, 25);
		propertyArmorBoronNitride.setLanguageKey("gui.config.armor.armor_boron_nitride");
		Property propertyArmorHazmat = config.get(CATEGORY_ARMOR, "armor_hazmat", new int[] {3, 6, 7, 3}, Lang.localise("gui.config.armor.armor_hazmat.comment"), 1, 25);
		propertyArmorHazmat.setLanguageKey("gui.config.armor.armor_hazmat");
		Property propertyArmorEnchantability = config.get(CATEGORY_ARMOR, "armor_enchantability", new int[] {6, 15, 12, 20, 5}, Lang.localise("gui.config.armor.armor_enchantability.comment"), 1, 255);
		propertyArmorEnchantability.setLanguageKey("gui.config.armor.armor_enchantability");
		Property propertyArmorToughness = config.get(CATEGORY_ARMOR, "armor_toughness", new double[] {1D, 2D, 1D, 2D, 0D}, Lang.localise("gui.config.armor.armor_toughness.comment"), 0D, 8D);
		propertyArmorToughness.setLanguageKey("gui.config.armor.armor_toughness");
		Property propertyArmorConarmRegister = config.get(CATEGORY_ARMOR, "armor_conarm_register", new boolean[] {true, true, true, true, true, true, true, true}, Lang.localise("gui.config.armor.armor_conarm_register.comment"));
		propertyArmorConarmRegister.setLanguageKey("gui.config.armor.armor_conarm_register");
		
		Property propertyEntityRegister = config.get(CATEGORY_ENTITIES, "entity_register", new boolean[] {true}, Lang.localise("gui.config.entities.entity_register.comment"));
		propertyEntityRegister.setLanguageKey("gui.config.entities.entity_register");
		Property propertyEntityTrackingRange = config.get(CATEGORY_ENTITIES, "entity_tracking_range", 64, Lang.localise("gui.config.entities.entity_tracking_range.comment"), 1, 255);
		propertyEntityTrackingRange.setLanguageKey("gui.config.entities.entity_tracking_range");
		
		Property propertyRadiationEnabled = config.get(CATEGORY_RADIATION, "radiation_enabled", true, Lang.localise("gui.config.radiation.radiation_enabled.comment"));
		propertyRadiationEnabled.setLanguageKey("gui.config.radiation.radiation_enabled");
		
		Property propertyRadiationWorldTickRate = config.get(CATEGORY_RADIATION, "radiation_world_tick_rate", 20, Lang.localise("gui.config.radiation.radiation_world_tick_rate.comment"), 1, 400);
		propertyRadiationWorldTickRate.setLanguageKey("gui.config.radiation.radiation_world_tick_rate");
		Property propertyRadiationPlayerTickRate = config.get(CATEGORY_RADIATION, "radiation_player_tick_rate", 5, Lang.localise("gui.config.radiation.radiation_player_tick_rate.comment"), 1, 400);
		propertyRadiationPlayerTickRate.setLanguageKey("gui.config.radiation.radiation_player_tick_rate");
		
		Property propertyRadiationWorlds = config.get(CATEGORY_RADIATION, "radiation_worlds", new String[] {"4598_2.25"}, Lang.localise("gui.config.radiation.radiation_worlds.comment"));
		propertyRadiationWorlds.setLanguageKey("gui.config.radiation.radiation_worlds");
		Property propertyRadiationBiomes = config.get(CATEGORY_RADIATION, "radiation_biomes", new String[] {"nuclearcraft:nuclear_wasteland_0.25"}, Lang.localise("gui.config.radiation.radiation_biomes.comment"));
		propertyRadiationBiomes.setLanguageKey("gui.config.radiation.radiation_biomes");
		Property propertyRadiationStructures = config.get(CATEGORY_RADIATION, "radiation_structures", new String[] {"Fortress_0.0025", "Mineshaft_0.00005", "Stronghold_0.001"}, Lang.localise("gui.config.radiation.radiation_structures.comment"));
		propertyRadiationStructures.setLanguageKey("gui.config.radiation.radiation_structures");
		Property propertyRadiationWorldLimit = config.get(CATEGORY_RADIATION, "radiation_world_limits", new String[] {}, Lang.localise("gui.config.radiation.radiation_world_limits.comment"));
		propertyRadiationWorldLimit.setLanguageKey("gui.config.radiation.radiation_world_limits");
		Property propertyRadiationBiomeLimit = config.get(CATEGORY_RADIATION, "radiation_biome_limits", new String[] {}, Lang.localise("gui.config.radiation.radiation_biome_limits.comment"));
		propertyRadiationBiomeLimit.setLanguageKey("gui.config.radiation.radiation_biome_limits");
		Property propertyRadiationFromBiomesDimsBlacklist = config.get(CATEGORY_RADIATION, "radiation_from_biomes_dims_blacklist", new int[] {144}, Lang.localise("gui.config.radiation.radiation_from_biomes_dims_blacklist.comment"));
		propertyRadiationFromBiomesDimsBlacklist.setLanguageKey("gui.config.radiation.radiation_from_biomes_dims_blacklist");
		
		Property propertyRadiationOres = config.get(CATEGORY_RADIATION, "radiation_ores", new String[] {"depletedFuelIC2U_" + (RadSources.URANIUM_238*4D + RadSources.PLUTONIUM_239/9D), "depletedFuelIC2MOX_" + (RadSources.PLUTONIUM_239*28D/9D)}, Lang.localise("gui.config.radiation.radiation_ores.comment"));
		propertyRadiationOres.setLanguageKey("gui.config.radiation.radiation_ores");
		Property propertyRadiationItems = config.get(CATEGORY_RADIATION, "radiation_items", new String[] {"ic2:nuclear:0_0.000000000048108553", "ic2:nuclear:1_" + RadSources.URANIUM_235, "ic2:nuclear:2_" + RadSources.URANIUM_238, "ic2:nuclear:3_" + RadSources.PLUTONIUM_239, "ic2:nuclear:4_0.000000833741517857143", "ic2:nuclear:5_" + (RadSources.URANIUM_235/9D), "ic2:nuclear:6_" + (RadSources.URANIUM_238/9D), "ic2:nuclear:7_" + (RadSources.PLUTONIUM_239/9D), "ic2:nuclear:8_0.000000000048108553", "ic2:nuclear:9_0.000000833741517857143", "ic2:nuclear:10_" + (RadSources.PLUTONIUM_238*3D), "ic2:nuclear:11_" + (RadSources.URANIUM_238*4D + RadSources.PLUTONIUM_239/9D), "ic2:nuclear:12_" + ((RadSources.URANIUM_238*4D + RadSources.PLUTONIUM_239/9D)*2D), "ic2:nuclear:13_" + ((RadSources.URANIUM_238*4D + RadSources.PLUTONIUM_239/9D)*4D), "ic2:nuclear:14_" + (RadSources.PLUTONIUM_239*28D/9D), "ic2:nuclear:15_" + (RadSources.PLUTONIUM_239*2D*28D/9D), "ic2:nuclear:16_" + (RadSources.PLUTONIUM_239*4D*28D/9D)}, Lang.localise("gui.config.radiation.radiation_items.comment"));
		propertyRadiationItems.setLanguageKey("gui.config.radiation.radiation_items");
		Property propertyRadiationBlocks = config.get(CATEGORY_RADIATION, "radiation_blocks", new String[] {}, Lang.localise("gui.config.radiation.radiation_blocks.comment"));
		propertyRadiationBlocks.setLanguageKey("gui.config.radiation.radiation_blocks");
		Property propertyRadiationFluids = config.get(CATEGORY_RADIATION, "radiation_fluids", new String[] {}, Lang.localise("gui.config.radiation.radiation_fluids.comment"));
		propertyRadiationFluids.setLanguageKey("gui.config.radiation.radiation_fluids");
		Property propertyRadiationFoods = config.get(CATEGORY_RADIATION, "radiation_foods", new String[] {"minecraft:golden_apple:0_-20_0.1", "minecraft:golden_apple:1_-100_0.5", "minecraft:golden_carrot:0_-4_0", "minecraft:spider_eye:0_0_0.5", "minecraft:poisonous_potato:0_0_0.5", "minecraft:fish:3_0_2", "minecraft:rabbit_stew:0_0_0.1", "minecraft:chorus_fruit:0_0_-0.25", "minecraft:beetroot:0_0_0.25", "minecraft:beetroot_soup:0_0_1.5", "quark:golden_frog_leg:0_-4_0"}, Lang.localise("gui.config.radiation.radiation_foods.comment"));
		propertyRadiationFoods.setLanguageKey("gui.config.radiation.radiation_foods");
		Property propertyRadiationOresBlacklist = config.get(CATEGORY_RADIATION, "radiation_ores_blacklist", new String[] {}, Lang.localise("gui.config.radiation.radiation_ores_blacklist.comment"));
		propertyRadiationOresBlacklist.setLanguageKey("gui.config.radiation.radiation_ores_blacklist");
		Property propertyRadiationItemsBlacklist = config.get(CATEGORY_RADIATION, "radiation_items_blacklist", new String[] {}, Lang.localise("gui.config.radiation.radiation_items_blacklist.comment"));
		propertyRadiationItemsBlacklist.setLanguageKey("gui.config.radiation.radiation_items_blacklist");
		Property propertyRadiationBlocksBlacklist = config.get(CATEGORY_RADIATION, "radiation_blocks_blacklist", new String[] {}, Lang.localise("gui.config.radiation.radiation_blocks_blacklist.comment"));
		propertyRadiationBlocksBlacklist.setLanguageKey("gui.config.radiation.radiation_blocks_blacklist");
		Property propertyRadiationFluidsBlacklist = config.get(CATEGORY_RADIATION, "radiation_fluids_blacklist", new String[] {}, Lang.localise("gui.config.radiation.radiation_fluids_blacklist.comment"));
		propertyRadiationFluidsBlacklist.setLanguageKey("gui.config.radiation.radiation_fluids_blacklist");
		
		Property propertyRadiationMaxPlayerRads = config.get(CATEGORY_RADIATION, "max_player_rads", 1000D, Lang.localise("gui.config.radiation.max_player_rads.comment"), 1D, 1000000000D);
		propertyRadiationMaxPlayerRads.setLanguageKey("gui.config.radiation.max_player_rads");
		Property propertyRadiationPlayerDecayRate = config.get(CATEGORY_RADIATION, "radiation_player_decay_rate", 0.0000005D, Lang.localise("gui.config.radiation.radiation_player_decay_rate.comment"), 0D, 1D);
		propertyRadiationPlayerDecayRate.setLanguageKey("gui.config.radiation.radiation_player_decay_rate");
		Property propertyRadiationEntityDecayRate = config.get(CATEGORY_RADIATION, "radiation_entity_decay_rate", 0.001D, Lang.localise("gui.config.radiation.radiation_entity_decay_rate.comment"), 0D, 1D);
		propertyRadiationEntityDecayRate.setLanguageKey("gui.config.radiation.radiation_entity_decay_rate");
		Property propertyRadiationSpreadRate = config.get(CATEGORY_RADIATION, "radiation_spread_rate", 0.1D, Lang.localise("gui.config.radiation.radiation_spread_rate.comment"), 0D, 1D);
		propertyRadiationSpreadRate.setLanguageKey("gui.config.radiation.radiation_spread_rate");
		Property propertyRadiationSpreadGradient = config.get(CATEGORY_RADIATION, "radiation_spread_gradient", 0.5D, Lang.localise("gui.config.radiation.radiation_spread_gradient.comment"), 0D, 1000000000D);
		propertyRadiationSpreadGradient.setLanguageKey("gui.config.radiation.radiation_spread_gradient");
		Property propertyRadiationDecayRate = config.get(CATEGORY_RADIATION, "radiation_decay_rate", 0.001D, Lang.localise("gui.config.radiation.radiation_decay_rate.comment"), 0D, 1D);
		propertyRadiationDecayRate.setLanguageKey("gui.config.radiation.radiation_decay_rate");
		Property propertyRadiationLowestRate = config.get(CATEGORY_RADIATION, "radiation_lowest_rate", 0.000000000000001D, Lang.localise("gui.config.radiation.radiation_lowest_rate.comment"), 0.000000000000000001D, 1D);
		propertyRadiationLowestRate.setLanguageKey("gui.config.radiation.radiation_lowest_rate");
		Property propertyRadiationChunkLimit = config.get(CATEGORY_RADIATION, "radiation_chunk_limit", -1D, Lang.localise("gui.config.radiation.radiation_chunk_limit.comment"), -1D, Double.MAX_VALUE);
		propertyRadiationChunkLimit.setLanguageKey("gui.config.radiation.radiation_chunk_limit");
		
		Property propertyRadiationBlockEffects = config.get(CATEGORY_RADIATION, "radiation_block_effects", new String[] {"nuclearcraft:dry_earth:0_20_materialMapColor@8368696", "nuclearcraft:dry_earth:0_20_materialMapColor@9923917", "minecraft:air:0_10_materialMapColor@31744"}, Lang.localise("gui.config.radiation.radiation_block_effects.comment"));
		propertyRadiationBlockEffects.setLanguageKey("gui.config.radiation.radiation_block_effects");
		Property propertyRadiationBlockEffectLimit = config.get(CATEGORY_RADIATION, "radiation_block_effect_limit", 10D, Lang.localise("gui.config.radiation.radiation_block_effect_limit.comment"), 0.000000000000000001D, Double.MAX_VALUE);
		propertyRadiationBlockEffectLimit.setLanguageKey("gui.config.radiation.radiation_block_effect_limit");
		Property propertyRadiationBlockEffectMaxRate = config.get(CATEGORY_RADIATION, "radiation_block_effect_max_rate", 0, Lang.localise("gui.config.radiation.radiation_block_effect_max_rate.comment"), 0, 15);
		propertyRadiationBlockEffectMaxRate.setLanguageKey("gui.config.radiation.radiation_block_effect_max_rate");
		Property propertyRadiationRainMult = config.get(CATEGORY_RADIATION, "radiation_rain_mult", 1D, Lang.localise("gui.config.radiation.radiation_rain_mult.comment"), 0.000001D, 1000000D);
		propertyRadiationRainMult.setLanguageKey("gui.config.radiation.radiation_rain_mult");
		Property propertyRadiationSwimMult = config.get(CATEGORY_RADIATION, "radiation_swim_mult", 2D, Lang.localise("gui.config.radiation.radiation_swim_mult.comment"), 0.000001D, 1000000D);
		propertyRadiationSwimMult.setLanguageKey("gui.config.radiation.radiation_swim_mult");
		
		Property propertyRadiationRadawayAmount = config.get(CATEGORY_RADIATION, "radiation_radaway_amount", 300D, Lang.localise("gui.config.radiation.radiation_radaway_amount.comment"), 0.001D, 1000000000D);
		propertyRadiationRadawayAmount.setLanguageKey("gui.config.radiation.radiation_radaway_amount");
		Property propertyRadiationRadawaySlowAmount = config.get(CATEGORY_RADIATION, "radiation_radaway_slow_amount", 300D, Lang.localise("gui.config.radiation.radiation_radaway_slow_amount.comment"), 0.001D, 1000000000D);
		propertyRadiationRadawaySlowAmount.setLanguageKey("gui.config.radiation.radiation_radaway_slow_amount");
		Property propertyRadiationRadawayRate = config.get(CATEGORY_RADIATION, "radiation_radaway_rate", 5D, Lang.localise("gui.config.radiation.radiation_radaway_rate.comment"), 0.001D, 1000000000D);
		propertyRadiationRadawayRate.setLanguageKey("gui.config.radiation.radiation_radaway_rate");
		Property propertyRadiationRadawaySlowRate = config.get(CATEGORY_RADIATION, "radiation_radaway_slow_rate", 0.025D, Lang.localise("gui.config.radiation.radiation_radaway_slow_rate.comment"), 0.00001D, 10000000D);
		propertyRadiationRadawaySlowRate.setLanguageKey("gui.config.radiation.radiation_radaway_slow_rate");
		Property propertyRadiationPoisonTime = config.get(CATEGORY_RADIATION, "radiation_poison_time", 60D, Lang.localise("gui.config.radiation.radiation_poison_time.comment"), 1D, 1000000D);
		propertyRadiationPoisonTime.setLanguageKey("gui.config.radiation.radiation_poison_time");
		Property propertyRadiationRadawayCooldown = config.get(CATEGORY_RADIATION, "radiation_radaway_cooldown", 0D, Lang.localise("gui.config.radiation.radiation_radaway_cooldown.comment"), 0D, 100000D);
		propertyRadiationRadawayCooldown.setLanguageKey("gui.config.radiation.radiation_radaway_cooldown");
		Property propertyRadiationRadXAmount = config.get(CATEGORY_RADIATION, "radiation_rad_x_amount", 25D, Lang.localise("gui.config.radiation.radiation_rad_x_amount.comment"), 0.001D, 1000000000D);
		propertyRadiationRadXAmount.setLanguageKey("gui.config.radiation.radiation_rad_x_amount");
		Property propertyRadiationRadXLifetime = config.get(CATEGORY_RADIATION, "radiation_rad_x_lifetime", 12000D, Lang.localise("gui.config.radiation.radiation_rad_x_lifetime.comment"), 20D, 1000000000D);
		propertyRadiationRadXLifetime.setLanguageKey("gui.config.radiation.radiation_rad_x_lifetime");
		Property propertyRadiationRadXCooldown = config.get(CATEGORY_RADIATION, "radiation_rad_x_cooldown", 0D, Lang.localise("gui.config.radiation.radiation_rad_x_cooldown.comment"), 0D, 100000D);
		propertyRadiationRadXCooldown.setLanguageKey("gui.config.radiation.radiation_rad_x_cooldown");
		Property propertyRadiationShieldingLevel = config.get(CATEGORY_RADIATION, "radiation_shielding_level", new double[] {0.01D, 0.1D, 1D}, Lang.localise("gui.config.radiation.radiation_shielding_level.comment"), 0.000000000000000001D, 1000D);
		propertyRadiationShieldingLevel.setLanguageKey("gui.config.radiation.radiation_shielding_level");
		Property propertyRadiationTileShielding = config.get(CATEGORY_RADIATION, "radiation_tile_shielding", true, Lang.localise("gui.config.radiation.radiation_tile_shielding.comment"));
		propertyRadiationTileShielding.setLanguageKey("gui.config.radiation.radiation_tile_shielding");
		Property propertyRadiationScrubberRate = config.get(CATEGORY_RADIATION, "radiation_scrubber_fraction", 0.125D, Lang.localise("gui.config.radiation.radiation_scrubber_fraction.comment"), 0.001D, 1D);
		propertyRadiationScrubberRate.setLanguageKey("gui.config.radiation.radiation_scrubber_fraction");
		Property propertyRadiationScrubberRadius = config.get(CATEGORY_RADIATION, "radiation_scrubber_radius", 4, Lang.localise("gui.config.radiation.radiation_scrubber_radius.comment"), 1, 10);
		propertyRadiationScrubberRadius.setLanguageKey("gui.config.radiation.radiation_scrubber_radius");
		Property propertyRadiationScrubberAlt = config.get(CATEGORY_RADIATION, "radiation_scrubber_alt", false, Lang.localise("gui.config.radiation.radiation_scrubber_alt.comment"));
		propertyRadiationScrubberAlt.setLanguageKey("gui.config.radiation.radiation_scrubber_alt");
		Property propertyRadiationScrubberParam = config.get(CATEGORY_RADIATION, "radiation_scrubber_param", new double[] {2.14280951676725D, 3D, 4D, 2D}, Lang.localise("gui.config.radiation.radiation_scrubber_param.comment"), 1D, 15D);
		propertyRadiationScrubberParam.setLanguageKey("gui.config.radiation.radiation_scrubber_param");
		Property propertyRadiationScrubberPower = config.get(CATEGORY_RADIATION, "radiation_scrubber_power", 500, Lang.localise("gui.config.radiation.radiation_scrubber_power.comment"), 0, Integer.MAX_VALUE);
		propertyRadiationScrubberPower.setLanguageKey("gui.config.radiation.radiation_scrubber_power");
		Property propertyRadiationScrubberBoraxRate = config.get(CATEGORY_RADIATION, "radiation_scrubber_borax_rate", 0, Lang.localise("gui.config.radiation.radiation_scrubber_borax_rate.comment"), 0, 100);
		propertyRadiationScrubberBoraxRate.setLanguageKey("gui.config.radiation.radiation_scrubber_borax_rate");
		Property propertyRadiationGeigerBlockRedstone = config.get(CATEGORY_RADIATION, "radiation_geiger_block_redstone", 3D, Lang.localise("gui.config.radiation.radiation_geiger_block_redstone.comment"), -100D, 100D);
		propertyRadiationGeigerBlockRedstone.setLanguageKey("gui.config.radiation.radiation_geiger_block_redstone");
		
		Property propertyRadiationShieldingDefaultRecipes = config.get(CATEGORY_RADIATION, "radiation_shielding_default_recipes", true, Lang.localise("gui.config.radiation.radiation_shielding_default_recipes.comment"));
		propertyRadiationShieldingDefaultRecipes.setLanguageKey("gui.config.radiation.radiation_shielding_default_recipes");
		Property propertyRadiationShieldingItemBlacklist = config.get(CATEGORY_RADIATION, "radiation_shielding_item_blacklist", new String[] {"nuclearcraft:helm_hazmat", "nuclearcraft:chest_hazmat", "nuclearcraft:legs_hazmat", "nuclearcraft:boots_hazmat", "ic2:hazmat_helmet", "ic2:hazmat_chestplate", "ic2:hazmat_leggings", "ic2:rubber_boots", "extraplanets:tier1_space_suit_helmet", "extraplanets:tier1_space_suit_chest", "extraplanets:tier1_space_suit_jetpack_chest", "extraplanets:tier1_space_suit_leggings", "extraplanets:tier1_space_suit_boots", "extraplanets:tier1_space_suit_gravity_boots", "extraplanets:tier2_space_suit_helmet", "extraplanets:tier2_space_suit_chest", "extraplanets:tier2_space_suit_jetpack_chest", "extraplanets:tier2_space_suit_leggings", "extraplanets:tier2_space_suit_boots", "extraplanets:tier2_space_suit_gravity_boots", "extraplanets:tier3_space_suit_helmet", "extraplanets:tier3_space_suit_chest", "extraplanets:tier3_space_suit_jetpack_chest", "extraplanets:tier3_space_suit_leggings", "extraplanets:tier3_space_suit_boots", "extraplanets:tier3_space_suit_gravity_boots", "extraplanets:tier4_space_suit_helmet", "extraplanets:tier4_space_suit_chest", "extraplanets:tier4_space_suit_jetpack_chest", "extraplanets:tier4_space_suit_leggings", "extraplanets:tier4_space_suit_boots", "extraplanets:tier4_space_suit_gravity_boots"}, Lang.localise("gui.config.radiation.radiation_shielding_item_blacklist.comment"));
		propertyRadiationShieldingItemBlacklist.setLanguageKey("gui.config.radiation.radiation_shielding_item_blacklist");
		Property propertyRadiationShieldingCustomStacks = config.get(CATEGORY_RADIATION, "radiation_shielding_custom_stacks", new String[] {}, Lang.localise("gui.config.radiation.radiation_shielding_custom_stacks.comment"));
		propertyRadiationShieldingCustomStacks.setLanguageKey("gui.config.radiation.radiation_shielding_custom_stacks");
		Property propertyRadiationShieldingDefaultLevels = config.get(CATEGORY_RADIATION, "radiation_shielding_default_levels", new String[] {"nuclearcraft:helm_hazmat_2.0", "nuclearcraft:chest_hazmat_3.0", "nuclearcraft:legs_hazmat_2.0", "nuclearcraft:boots_hazmat_2.0", "ic2:hazmat_helmet_2.0", "ic2:hazmat_chestplate_3.0", "ic2:hazmat_leggings_2.0", "ic2:rubber_boots_2.0", "extraplanets:tier1_space_suit_helmet_1.0", "extraplanets:tier1_space_suit_chest_1.5", "extraplanets:tier1_space_suit_jetpack_chest_1.5", "extraplanets:tier1_space_suit_leggings_1.0", "extraplanets:tier1_space_suit_boots_1.0", "extraplanets:tier1_space_suit_gravity_boots_1.0", "extraplanets:tier2_space_suit_helmet_1.3", "extraplanets:tier2_space_suit_chest_1.95", "extraplanets:tier2_space_suit_jetpack_chest_1.95", "extraplanets:tier2_space_suit_leggings_1.3", "extraplanets:tier2_space_suit_boots_1.3", "extraplanets:tier2_space_suit_gravity_boots_1.3", "extraplanets:tier3_space_suit_helmet_1.6", "extraplanets:tier3_space_suit_chest_2.4", "extraplanets:tier3_space_suit_jetpack_chest_2.4", "extraplanets:tier3_space_suit_leggings_1.6", "extraplanets:tier3_space_suit_boots_1.6", "extraplanets:tier3_space_suit_gravity_boots_1.6", "extraplanets:tier4_space_suit_helmet_2.0", "extraplanets:tier4_space_suit_chest_3.0", "extraplanets:tier4_space_suit_jetpack_chest_3.0", "extraplanets:tier4_space_suit_leggings_2.0", "extraplanets:tier4_space_suit_boots_2.0", "extraplanets:tier4_space_suit_gravity_boots_2.0"}, Lang.localise("gui.config.radiation.radiation_shielding_default_levels.comment"));
		propertyRadiationShieldingDefaultLevels.setLanguageKey("gui.config.radiation.radiation_shielding_default_levels");
		
		Property propertyRadiationHardcoreStacks = config.get(CATEGORY_RADIATION, "radiation_hardcore_stacks", true, Lang.localise("gui.config.radiation.radiation_hardcore_stacks.comment"));
		propertyRadiationHardcoreStacks.setLanguageKey("gui.config.radiation.radiation_hardcore_stacks");
		Property propertyRadiationHardcoreContainers = config.get(CATEGORY_RADIATION, "radiation_hardcore_containers", 0D, Lang.localise("gui.config.radiation.radiation_hardcore_containers.comment"), 0D, 1D);
		propertyRadiationHardcoreContainers.setLanguageKey("gui.config.radiation.radiation_hardcore_containers");
		Property propertyRadiationDroppedItems = config.get(CATEGORY_RADIATION, "radiation_dropped_items", true, Lang.localise("gui.config.radiation.radiation_dropped_items.comment"));
		propertyRadiationDroppedItems.setLanguageKey("gui.config.radiation.radiation_dropped_items");
		Property propertyRadiationDeathPersist = config.get(CATEGORY_RADIATION, "radiation_death_persist", true, Lang.localise("gui.config.radiation.radiation_death_persist.comment"));
		propertyRadiationDeathPersist.setLanguageKey("gui.config.radiation.radiation_death_persist");
		Property propertyRadiationDeathPersistFraction = config.get(CATEGORY_RADIATION, "radiation_death_persist_fraction", 0.75D, Lang.localise("gui.config.radiation.radiation_death_persist_fraction.comment"), 0D, 1D);
		propertyRadiationDeathPersistFraction.setLanguageKey("gui.config.radiation.radiation_death_persist_fraction");
		Property propertyRadiationDeathImmunityTime = config.get(CATEGORY_RADIATION, "radiation_death_immunity_time", 90D, Lang.localise("gui.config.radiation.radiation_death_immunity_time.comment"), 0D, 3600D);
		propertyRadiationDeathImmunityTime.setLanguageKey("gui.config.radiation.radiation_death_immunity_time");
		
		Property propertyRadiationPassiveDebuffLists = config.get(CATEGORY_RADIATION, "radiation_passive_debuff_lists", new String[] {"40.0_minecraft:weakness@1", "55.0_minecraft:weakness@1,minecraft:mining_fatigue@1", "70.0_minecraft:weakness@2,minecraft:mining_fatigue@1,minecraft:hunger@1", "80.0_minecraft:weakness@2,minecraft:mining_fatigue@2,minecraft:hunger@1,minecraft:poison@1", "90.0_minecraft:weakness@3,minecraft:mining_fatigue@3,minecraft:hunger@2,minecraft:poison@1,minecraft:wither@1"}, Lang.localise("gui.config.radiation.radiation_passive_debuff_lists.comment"));
		propertyRadiationPassiveDebuffLists.setLanguageKey("gui.config.radiation.radiation_passive_debuff_lists");
		Property propertyRadiationMobBuffLists = config.get(CATEGORY_RADIATION, "radiation_mob_buff_lists", new String[] {"40.0_minecraft:speed@1", "55.0_minecraft:speed@1,minecraft:strength@1", "70.0_minecraft:speed@1,minecraft:strength@1,minecraft:resistance@1", "80.0_minecraft:speed@1,minecraft:strength@1,minecraft:resistance@1,minecraft:absorption@1", "90.0_minecraft:speed@1,minecraft:strength@1,minecraft:resistance@1,minecraft:absorption@1,minecraft:regeneration@1"}, Lang.localise("gui.config.radiation.radiation_mob_buff_lists.comment"));
		propertyRadiationMobBuffLists.setLanguageKey("gui.config.radiation.radiation_mob_buff_lists");
		
		Property propertyRadiationHorseArmor = config.get(CATEGORY_RADIATION, "radiation_horse_armor", false, Lang.localise("gui.config.radiation.radiation_horse_armor.comment"));
		propertyRadiationHorseArmor.setLanguageKey("gui.config.radiation.radiation_horse_armor");
		
		Property propertyRadiationHUDSize = config.get(CATEGORY_RADIATION, "radiation_hud_size", 1D, Lang.localise("gui.config.radiation.radiation_hud_size.comment"), 0.1D, 10D);
		propertyRadiationHUDSize.setLanguageKey("gui.config.radiation.radiation_hud_size");
		Property propertyRadiationHUDPosition = config.get(CATEGORY_RADIATION, "radiation_hud_position", 225D, Lang.localise("gui.config.radiation.radiation_hud_position.comment"), 0D, 360D);
		propertyRadiationHUDPosition.setLanguageKey("gui.config.radiation.radiation_hud_position");
		Property propertyRadiationHUDPositionCartesian = config.get(CATEGORY_RADIATION, "radiation_hud_position_cartesian", new double[] {}, Lang.localise("gui.config.radiation.radiation_hud_position_cartesian.comment"), 0D, 1D);
		propertyRadiationHUDPositionCartesian.setLanguageKey("gui.config.radiation.radiation_hud_position_cartesian");
		Property propertyRadiationHUDTextOutline = config.get(CATEGORY_RADIATION, "radiation_hud_text_outline", false, Lang.localise("gui.config.radiation.radiation_hud_text_outline.comment"));
		propertyRadiationHUDTextOutline.setLanguageKey("gui.config.radiation.radiation_hud_text_outline");
		Property propertyRadiationRequireCounter = config.get(CATEGORY_RADIATION, "radiation_require_counter", true, Lang.localise("gui.config.radiation.radiation_require_counter.comment"));
		propertyRadiationRequireCounter.setLanguageKey("gui.config.radiation.radiation_require_counter");
		Property propertyRadiationChunkBoundaries = config.get(CATEGORY_RADIATION, "radiation_chunk_boundaries", true, Lang.localise("gui.config.radiation.radiation_chunk_boundaries.comment"));
		propertyRadiationChunkBoundaries.setLanguageKey("gui.config.radiation.radiation_chunk_boundaries");
		Property propertyRadiationUnitPrefixes = config.get(CATEGORY_RADIATION, "radiation_unit_prefixes", 0, Lang.localise("gui.config.radiation.radiation_unit_prefixes.comment"), 0, 15);
		propertyRadiationUnitPrefixes.setLanguageKey("gui.config.radiation.radiation_unit_prefixes");
		
		Property propertyRadiationBadgeDurability = config.get(CATEGORY_RADIATION, "radiation_badge_durability", 250D, Lang.localise("gui.config.radiation.radiation_badge_durability.comment"), 0.000000000000000001D, 1000D);
		propertyRadiationBadgeDurability.setLanguageKey("gui.config.radiation.radiation_badge_durability");
		Property propertyRadiationBadgeInfoRate = config.get(CATEGORY_RADIATION, "radiation_badge_info_rate", 0.1D, Lang.localise("gui.config.radiation.radiation_badge_info_rate.comment"), 0.000000000000000001D, 1D);
		propertyRadiationBadgeInfoRate.setLanguageKey("gui.config.radiation.radiation_badge_info_rate");
		
		Property propertySingleCreativeTab = config.get(CATEGORY_OTHER, "single_creative_tab", false, Lang.localise("gui.config.other.single_creative_tab.comment"));
		propertySingleCreativeTab.setLanguageKey("gui.config.other.single_creative_tab");
		
		Property propertyRegisterProcessor = config.get(CATEGORY_OTHER, "register_processor", new boolean[] {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true}, Lang.localise("gui.config.other.register_processor.comment"));
		propertyRegisterProcessor.setLanguageKey("gui.config.other.register_processor");
		Property propertyRegisterPassive = config.get(CATEGORY_OTHER, "register_passive", new boolean[] {true, true, true, true}, Lang.localise("gui.config.other.register_passive.comment"));
		propertyRegisterPassive.setLanguageKey("gui.config.other.register_passive");
		Property propertyRegisterTool = config.get(CATEGORY_OTHER, "register_tool", new boolean[] {true, true, true, true}, Lang.localise("gui.config.other.register_tool.comment"));
		propertyRegisterTool.setLanguageKey("gui.config.other.register_tool");
		Property propertyRegisterArmor = config.get(CATEGORY_OTHER, "register_armor", new boolean[] {true, true, true, true}, Lang.localise("gui.config.other.register_armor.comment"));
		propertyRegisterArmor.setLanguageKey("gui.config.other.register_armor");
		
		Property propertyCtrlInfo = config.get(CATEGORY_OTHER, "ctrl_info", false, Lang.localise("gui.config.other.ctrl_info.comment"));
		propertyCtrlInfo.setLanguageKey("gui.config.other.ctrl_info");
		
		Property propertyJEIChanceItemsIncludeNull = config.get(CATEGORY_OTHER, "jei_chance_items_include_null", false, Lang.localise("gui.config.other.jei_chance_items_include_null.comment"));
		propertyJEIChanceItemsIncludeNull.setLanguageKey("gui.config.other.jei_chance_items_include_null");
		
		Property propertyRareDrops = config.get(CATEGORY_OTHER, "rare_drops", false, Lang.localise("gui.config.other.rare_drops.comment"));
		propertyRareDrops.setLanguageKey("gui.config.other.rare_drops");
		Property propertyDungeonLoot = config.get(CATEGORY_OTHER, "dungeon_loot", true, Lang.localise("gui.config.other.dungeon_loot.comment"));
		propertyDungeonLoot.setLanguageKey("gui.config.other.dungeon_loot");
		
		Property propertyWastelandBiome = config.get(CATEGORY_OTHER, "wasteland_biome", true, Lang.localise("gui.config.other.wasteland_biome.comment"));
		propertyWastelandBiome.setLanguageKey("gui.config.other.wasteland_biome");
		Property propertyWastelandBiomeWeight = config.get(CATEGORY_OTHER, "wasteland_biome_weight", 5, Lang.localise("gui.config.other.wasteland_biome_weight.comment"), 0, 255);
		propertyWastelandBiomeWeight.setLanguageKey("gui.config.other.wasteland_biome_weight");
		
		Property propertyWastelandDimensionGen = config.get(CATEGORY_OTHER, "wasteland_dimension_gen", true, Lang.localise("gui.config.other.wasteland_dimension_gen.comment"));
		propertyWastelandDimensionGen.setLanguageKey("gui.config.other.wasteland_dimension_gen");
		Property propertyWastelandDimension = config.get(CATEGORY_OTHER, "wasteland_dimension", 4598, Lang.localise("gui.config.other.wasteland_dimension.comment"), Integer.MIN_VALUE, Integer.MAX_VALUE);
		propertyWastelandDimension.setLanguageKey("gui.config.other.wasteland_dimension");
		
		Property propertyMushroomSpreadRate = config.get(CATEGORY_OTHER, "mushroom_spread_rate", 16, Lang.localise("gui.config.other.mushroom_spread_rate.comment"), 0, 511);
		propertyMushroomSpreadRate.setLanguageKey("gui.config.other.mushroom_spread_rate");
		Property propertyMushroomGen = config.get(CATEGORY_OTHER, "mushroom_gen", true, Lang.localise("gui.config.other.mushroom_gen.comment"));
		propertyMushroomGen.setLanguageKey("gui.config.other.mushroom_gen");
		Property propertyMushroomGenSize = config.get(CATEGORY_OTHER, "mushroom_gen_size", 64, Lang.localise("gui.config.other.mushroom_gen_size.comment"), 0, 511);
		propertyMushroomGenSize.setLanguageKey("gui.config.other.mushroom_gen_size");
		Property propertyMushroomGenRate = config.get(CATEGORY_OTHER, "mushroom_gen_rate", 40, Lang.localise("gui.config.other.mushroom_gen_rate.comment"), 0, 511);
		propertyMushroomGenRate.setLanguageKey("gui.config.other.mushroom_gen_rate");
		
		Property propertyRegisterFluidBlocks = config.get(CATEGORY_OTHER, "register_fluid_blocks", false, Lang.localise("gui.config.other.register_fluid_blocks.comment"));
		propertyRegisterFluidBlocks.setLanguageKey("gui.config.other.register_fluid_blocks");
		Property propertyRegisterCoFHFluids = config.get(CATEGORY_OTHER, "register_cofh_fluids", false, Lang.localise("gui.config.other.register_cofh_fluids.comment"));
		propertyRegisterCoFHFluids.setLanguageKey("gui.config.other.register_cofh_fluids");
		
		Property propertyRegisterProjectEEMC = config.get(CATEGORY_OTHER, "register_projecte_emc", true, Lang.localise("gui.config.other.register_projecte_emc.comment"));
		propertyRegisterProjectEEMC.setLanguageKey("gui.config.other.register_projecte_emc");
		
		Property propertyOreDictPriorityBool = config.get(CATEGORY_OTHER, "ore_dict_priority_bool", true, Lang.localise("gui.config.other.ore_dict_priority_bool.comment"));
		propertyOreDictPriorityBool.setLanguageKey("gui.config.other.ore_dict_priority_bool");
		Property propertyOreDictPriority = config.get(CATEGORY_OTHER, "ore_dict_priority", new String[] {"minecraft", "thermalfoundation", "techreborn", "nuclearcraft", "immersiveengineering", "mekanism", "ic2", "appliedenergistics2", "refinedstorage", "actuallyadditions", "advancedRocketry", "thaumcraft", "biomesoplenty"}, Lang.localise("gui.config.other.ore_dict_priority.comment"));
		propertyOreDictPriority.setLanguageKey("gui.config.other.ore_dict_priority");
		
		List<String> propertyOrderOres = new ArrayList<String>();
		propertyOrderOres.add(propertyOreDims.getName());
		propertyOrderOres.add(propertyOreDimsListType.getName());
		propertyOrderOres.add(propertyOreGen.getName());
		propertyOrderOres.add(propertyOreSize.getName());
		propertyOrderOres.add(propertyOreRate.getName());
		propertyOrderOres.add(propertyOreMinHeight.getName());
		propertyOrderOres.add(propertyOreMaxHeight.getName());
		propertyOrderOres.add(propertyOreDrops.getName());
		propertyOrderOres.add(propertyHideDisabledOres.getName());
		propertyOrderOres.add(propertyOreHarvestLevels.getName());
		config.setCategoryPropertyOrder(CATEGORY_ORES, propertyOrderOres);
		
		List<String> propertyOrderProcessors = new ArrayList<String>();
		propertyOrderProcessors.add(propertyProcessorTime.getName());
		propertyOrderProcessors.add(propertyProcessorPower.getName());
		propertyOrderProcessors.add(propertySpeedUpgradePowerLaws.getName());
		propertyOrderProcessors.add(propertySpeedUpgradeMultipliers.getName());
		propertyOrderProcessors.add(propertyEnergyUpgradePowerLaws.getName());
		propertyOrderProcessors.add(propertyEnergyUpgradeMultipliers.getName());
		propertyOrderProcessors.add(propertyRFPerEU.getName());
		propertyOrderProcessors.add(propertyEnableGTCEEU.getName());
		propertyOrderProcessors.add(propertyEnableMekGas.getName());
		propertyOrderProcessors.add(propertyMachineUpdateRate.getName());
		propertyOrderProcessors.add(propertyProcessorPassiveRate.getName());
		propertyOrderProcessors.add(propertyCobbleGenPower.getName());
		propertyOrderProcessors.add(propertyOreProcessing.getName());
		propertyOrderProcessors.add(propertyManufactoryWood.getName());
		propertyOrderProcessors.add(propertyRockCrusherAlternate.getName());
		propertyOrderProcessors.add(propertyGTCERecipes.getName());
		propertyOrderProcessors.add(propertyGTCERecipeLogging.getName());
		propertyOrderProcessors.add(propertySmartProcessorInput.getName());
		propertyOrderProcessors.add(propertyPermeation.getName());
		propertyOrderProcessors.add(propertyProcessorParticles.getName());
		config.setCategoryPropertyOrder(CATEGORY_PROCESSORS, propertyOrderProcessors);
		
		List<String> propertyOrderGenerators = new ArrayList<String>();
		propertyOrderGenerators.add(propertyRTGPower.getName());
		propertyOrderGenerators.add(propertySolarPower.getName());
		propertyOrderGenerators.add(propertyDecayLifetime.getName());
		propertyOrderGenerators.add(propertyDecayPower.getName());
		config.setCategoryPropertyOrder(CATEGORY_GENERATORS, propertyOrderGenerators);
		
		List<String> propertyOrderFission = new ArrayList<String>();
		propertyOrderFission.add(propertyFissionPower.getName());
		propertyOrderFission.add(propertyFissionFuelUse.getName());
		propertyOrderFission.add(propertyFissionHeatGeneration.getName());
		propertyOrderFission.add(propertyFissionCoolingRate.getName());
		propertyOrderFission.add(propertyFissionActiveCoolingRate.getName());
		propertyOrderFission.add(propertyFissionWaterCoolerRequirement.getName());
		propertyOrderFission.add(propertyFissionOverheat.getName());
		propertyOrderFission.add(propertyFissionExplosions.getName());
		propertyOrderFission.add(propertyFissionMeltdownRadiationMultiplier.getName());
		propertyOrderFission.add(propertyFissionMinSize.getName());
		propertyOrderFission.add(propertyFissionMaxSize.getName());
		propertyOrderFission.add(propertyFissionComparatorMaxHeat.getName());
		propertyOrderFission.add(propertyFissionForceHeatComparator.getName());
		propertyOrderFission.add(propertyFissionActiveCoolerMaxRate.getName());
		
		propertyOrderFission.add(propertyFissionModeratorExtraPower.getName());
		propertyOrderFission.add(propertyFissionModeratorExtraHeat.getName());
		propertyOrderFission.add(propertyFissionNeutronReach.getName());
		
		propertyOrderFission.add(propertyFissionThoriumFuelTime.getName());
		propertyOrderFission.add(propertyFissionThoriumPower.getName());
		propertyOrderFission.add(propertyFissionThoriumHeatGeneration.getName());
		propertyOrderFission.add(propertyFissionThoriumRadiation.getName());
		
		propertyOrderFission.add(propertyFissionUraniumFuelTime.getName());
		propertyOrderFission.add(propertyFissionUraniumPower.getName());
		propertyOrderFission.add(propertyFissionUraniumHeatGeneration.getName());
		propertyOrderFission.add(propertyFissionUraniumRadiation.getName());
		
		propertyOrderFission.add(propertyFissionNeptuniumFuelTime.getName());
		propertyOrderFission.add(propertyFissionNeptuniumPower.getName());
		propertyOrderFission.add(propertyFissionNeptuniumHeatGeneration.getName());
		propertyOrderFission.add(propertyFissionNeptuniumRadiation.getName());
		
		propertyOrderFission.add(propertyFissionPlutoniumFuelTime.getName());
		propertyOrderFission.add(propertyFissionPlutoniumPower.getName());
		propertyOrderFission.add(propertyFissionPlutoniumHeatGeneration.getName());
		propertyOrderFission.add(propertyFissionPlutoniumRadiation.getName());
		
		propertyOrderFission.add(propertyFissionMOXFuelTime.getName());
		propertyOrderFission.add(propertyFissionMOXPower.getName());
		propertyOrderFission.add(propertyFissionMOXHeatGeneration.getName());
		propertyOrderFission.add(propertyFissionMOXRadiation.getName());
		
		propertyOrderFission.add(propertyFissionAmericiumFuelTime.getName());
		propertyOrderFission.add(propertyFissionAmericiumPower.getName());
		propertyOrderFission.add(propertyFissionAmericiumHeatGeneration.getName());
		propertyOrderFission.add(propertyFissionAmericiumRadiation.getName());
		
		propertyOrderFission.add(propertyFissionCuriumFuelTime.getName());
		propertyOrderFission.add(propertyFissionCuriumPower.getName());
		propertyOrderFission.add(propertyFissionCuriumHeatGeneration.getName());
		propertyOrderFission.add(propertyFissionCuriumRadiation.getName());
		
		propertyOrderFission.add(propertyFissionBerkeliumFuelTime.getName());
		propertyOrderFission.add(propertyFissionBerkeliumPower.getName());
		propertyOrderFission.add(propertyFissionBerkeliumHeatGeneration.getName());
		propertyOrderFission.add(propertyFissionBerkeliumRadiation.getName());
		
		propertyOrderFission.add(propertyFissionCaliforniumFuelTime.getName());
		propertyOrderFission.add(propertyFissionCaliforniumPower.getName());
		propertyOrderFission.add(propertyFissionCaliforniumHeatGeneration.getName());
		propertyOrderFission.add(propertyFissionCaliforniumRadiation.getName());
		config.setCategoryPropertyOrder(CATEGORY_FISSION, propertyOrderFission);
		
		List<String> propertyOrderFusion = new ArrayList<String>();
		propertyOrderFusion.add(propertyFusionBasePower.getName());
		propertyOrderFusion.add(propertyFusionFuelUse.getName());
		propertyOrderFusion.add(propertyFusionHeatGeneration.getName());
		propertyOrderFusion.add(propertyFusionHeatingMultiplier.getName());
		propertyOrderFusion.add(propertyFusionOverheat.getName());
		propertyOrderFusion.add(propertyFusionMeltdownRadiationMultiplier.getName());
		propertyOrderFusion.add(propertyFusionActiveCooling.getName());
		propertyOrderFusion.add(propertyFusionActiveCoolingRate.getName());
		propertyOrderFusion.add(propertyFusionMinSize.getName());
		propertyOrderFusion.add(propertyFusionMaxSize.getName());
		propertyOrderFusion.add(propertyFusionComparatorMaxEfficiency.getName());
		propertyOrderFusion.add(propertyFusionElectromagnetPower.getName());
		propertyOrderFusion.add(propertyFusionEnableSound.getName());
		propertyOrderFusion.add(propertyFusionPlasmaCraziness.getName());
		
		propertyOrderFusion.add(propertyFusionFuelTime.getName());
		propertyOrderFusion.add(propertyFusionPower.getName());
		propertyOrderFusion.add(propertyFusionHeatVariable.getName());
		propertyOrderFusion.add(propertyFusionRadiation.getName());
		config.setCategoryPropertyOrder(CATEGORY_FUSION, propertyOrderFusion);
		
		List<String> propertyOrderSaltFission = new ArrayList<String>();
		propertyOrderSaltFission.add(propertySaltFissionPower.getName());
		propertyOrderSaltFission.add(propertySaltFissionFuelUse.getName());
		propertyOrderSaltFission.add(propertySaltFissionHeatGeneration.getName());
		propertyOrderSaltFission.add(propertySaltFissionOverheat.getName());
		propertyOrderSaltFission.add(propertySaltFissionMeltdownRadiationMultiplier.getName());
		propertyOrderSaltFission.add(propertySaltFissionMinSize.getName());
		propertyOrderSaltFission.add(propertySaltFissionMaxSize.getName());
		propertyOrderSaltFission.add(propertySaltFissionCoolingRate.getName());
		propertyOrderSaltFission.add(propertySaltFissionRedstoneMaxHeat.getName());
		propertyOrderSaltFission.add(propertySaltFissionMaxDistributionRate.getName());
		config.setCategoryPropertyOrder(CATEGORY_SALT_FISSION, propertyOrderSaltFission);
		
		List<String> propertyOrderHeatExchanger = new ArrayList<String>();
		propertyOrderHeatExchanger.add(propertyHeatExchangerMinSize.getName());
		propertyOrderHeatExchanger.add(propertyHeatExchangerMaxSize.getName());
		propertyOrderHeatExchanger.add(propertyHeatExchangerConductivity.getName());
		propertyOrderHeatExchanger.add(propertyHeatExchangerCoolantMult.getName());
		propertyOrderHeatExchanger.add(propertyHeatExchangerAlternateExhaustRecipe.getName());
		config.setCategoryPropertyOrder(CATEGORY_HEAT_EXCHANGER, propertyOrderHeatExchanger);
		
		List<String> propertyOrderTurbine = new ArrayList<String>();
		propertyOrderTurbine.add(propertyTurbineMinSize.getName());
		propertyOrderTurbine.add(propertyTurbineMaxSize.getName());
		propertyOrderTurbine.add(propertyTurbineBladeEfficiency.getName());
		propertyOrderTurbine.add(propertyTurbineBladeExpansion.getName());
		propertyOrderTurbine.add(propertyTurbineStatorExpansion.getName());
		propertyOrderTurbine.add(propertyTurbineCoilConductivity.getName());
		propertyOrderTurbine.add(propertyTurbinePowerPerMB.getName());
		propertyOrderTurbine.add(propertyTurbineExpansionLevel.getName());
		propertyOrderTurbine.add(propertyTurbineMBPerBlade.getName());
		config.setCategoryPropertyOrder(CATEGORY_TURBINE, propertyOrderTurbine);
		
		List<String> propertyOrderAccelerator = new ArrayList<String>();
		propertyOrderAccelerator.add(propertyAcceleratorElectromagnetPower.getName());
		propertyOrderAccelerator.add(propertyAcceleratorSupercoolerCoolant.getName());
		config.setCategoryPropertyOrder(CATEGORY_ACCELERATOR, propertyOrderAccelerator);
		
		List<String> propertyOrderEnergyStorage = new ArrayList<String>();
		propertyOrderEnergyStorage.add(propertyBatteryCapacity.getName());
		config.setCategoryPropertyOrder(CATEGORY_ENERGY_STORAGE, propertyOrderEnergyStorage);
		
		List<String> propertyOrderTools = new ArrayList<String>();
		propertyOrderTools.add(propertyToolMiningLevel.getName());
		propertyOrderTools.add(propertyToolDurability.getName());
		propertyOrderTools.add(propertyToolSpeed.getName());
		propertyOrderTools.add(propertyToolAttackDamage.getName());
		propertyOrderTools.add(propertyToolEnchantability.getName());
		propertyOrderTools.add(propertyToolHandleModifier.getName());
		propertyOrderTools.add(propertyToolTiCRegister.getName());
		config.setCategoryPropertyOrder(CATEGORY_TOOLS, propertyOrderTools);
		
		List<String> propertyOrderArmor = new ArrayList<String>();
		propertyOrderArmor.add(propertyArmorDurability.getName());
		propertyOrderArmor.add(propertyArmorEnchantability.getName());
		propertyOrderArmor.add(propertyArmorBoron.getName());
		propertyOrderArmor.add(propertyArmorTough.getName());
		propertyOrderArmor.add(propertyArmorHardCarbon.getName());
		propertyOrderArmor.add(propertyArmorBoronNitride.getName());
		propertyOrderArmor.add(propertyArmorHazmat.getName());
		propertyOrderArmor.add(propertyArmorToughness.getName());
		propertyOrderArmor.add(propertyArmorConarmRegister.getName());
		config.setCategoryPropertyOrder(CATEGORY_ARMOR, propertyOrderArmor);
		
		List<String> propertyOrderEntities = new ArrayList<String>();
		propertyOrderEntities.add(propertyEntityTrackingRange.getName());
		propertyOrderEntities.add(propertyEntityRegister.getName());
		config.setCategoryPropertyOrder(CATEGORY_ENTITIES, propertyOrderEntities);
		
		List<String> propertyOrderRadiation = new ArrayList<String>();
		propertyOrderRadiation.add(propertyRadiationEnabled.getName());
		propertyOrderRadiation.add(propertyRadiationWorldTickRate.getName());
		propertyOrderRadiation.add(propertyRadiationPlayerTickRate.getName());
		propertyOrderRadiation.add(propertyRadiationWorlds.getName());
		propertyOrderRadiation.add(propertyRadiationBiomes.getName());
		propertyOrderRadiation.add(propertyRadiationStructures.getName());
		propertyOrderRadiation.add(propertyRadiationWorldLimit.getName());
		propertyOrderRadiation.add(propertyRadiationBiomeLimit.getName());
		propertyOrderRadiation.add(propertyRadiationFromBiomesDimsBlacklist.getName());
		propertyOrderRadiation.add(propertyRadiationOres.getName());
		propertyOrderRadiation.add(propertyRadiationItems.getName());
		propertyOrderRadiation.add(propertyRadiationBlocks.getName());
		propertyOrderRadiation.add(propertyRadiationFluids.getName());
		propertyOrderRadiation.add(propertyRadiationFoods.getName());
		propertyOrderRadiation.add(propertyRadiationOresBlacklist.getName());
		propertyOrderRadiation.add(propertyRadiationItemsBlacklist.getName());
		propertyOrderRadiation.add(propertyRadiationBlocksBlacklist.getName());
		propertyOrderRadiation.add(propertyRadiationFluidsBlacklist.getName());
		propertyOrderRadiation.add(propertyRadiationMaxPlayerRads.getName());
		propertyOrderRadiation.add(propertyRadiationPlayerDecayRate.getName());
		propertyOrderRadiation.add(propertyRadiationEntityDecayRate.getName());
		propertyOrderRadiation.add(propertyRadiationSpreadRate.getName());
		propertyOrderRadiation.add(propertyRadiationSpreadGradient.getName());
		propertyOrderRadiation.add(propertyRadiationDecayRate.getName());
		propertyOrderRadiation.add(propertyRadiationLowestRate.getName());
		propertyOrderRadiation.add(propertyRadiationChunkLimit.getName());
		propertyOrderRadiation.add(propertyRadiationBlockEffects.getName());
		propertyOrderRadiation.add(propertyRadiationBlockEffectLimit.getName());
		propertyOrderRadiation.add(propertyRadiationBlockEffectMaxRate.getName());
		propertyOrderRadiation.add(propertyRadiationRainMult.getName());
		propertyOrderRadiation.add(propertyRadiationSwimMult.getName());
		propertyOrderRadiation.add(propertyRadiationRadawayAmount.getName());
		propertyOrderRadiation.add(propertyRadiationRadawaySlowAmount.getName());
		propertyOrderRadiation.add(propertyRadiationRadawayRate.getName());
		propertyOrderRadiation.add(propertyRadiationRadawaySlowRate.getName());
		propertyOrderRadiation.add(propertyRadiationPoisonTime.getName());
		propertyOrderRadiation.add(propertyRadiationRadawayCooldown.getName());
		propertyOrderRadiation.add(propertyRadiationRadXAmount.getName());
		propertyOrderRadiation.add(propertyRadiationRadXLifetime.getName());
		propertyOrderRadiation.add(propertyRadiationRadXCooldown.getName());
		propertyOrderRadiation.add(propertyRadiationShieldingLevel.getName());
		propertyOrderRadiation.add(propertyRadiationTileShielding.getName());
		propertyOrderRadiation.add(propertyRadiationScrubberRate.getName());
		propertyOrderRadiation.add(propertyRadiationScrubberRadius.getName());
		propertyOrderRadiation.add(propertyRadiationScrubberAlt.getName());
		propertyOrderRadiation.add(propertyRadiationScrubberParam.getName());
		propertyOrderRadiation.add(propertyRadiationScrubberPower.getName());
		propertyOrderRadiation.add(propertyRadiationScrubberBoraxRate.getName());
		propertyOrderRadiation.add(propertyRadiationGeigerBlockRedstone.getName());
		propertyOrderRadiation.add(propertyRadiationShieldingDefaultRecipes.getName());
		propertyOrderRadiation.add(propertyRadiationShieldingItemBlacklist.getName());
		propertyOrderRadiation.add(propertyRadiationShieldingCustomStacks.getName());
		propertyOrderRadiation.add(propertyRadiationShieldingDefaultLevels.getName());
		propertyOrderRadiation.add(propertyRadiationHardcoreStacks.getName());
		propertyOrderRadiation.add(propertyRadiationHardcoreContainers.getName());
		propertyOrderRadiation.add(propertyRadiationDroppedItems.getName());
		propertyOrderRadiation.add(propertyRadiationDeathPersist.getName());
		propertyOrderRadiation.add(propertyRadiationDeathPersistFraction.getName());
		propertyOrderRadiation.add(propertyRadiationDeathImmunityTime.getName());
		propertyOrderRadiation.add(propertyRadiationPassiveDebuffLists.getName());
		propertyOrderRadiation.add(propertyRadiationMobBuffLists.getName());
		propertyOrderRadiation.add(propertyRadiationHorseArmor.getName());
		propertyOrderRadiation.add(propertyRadiationHUDSize.getName());
		propertyOrderRadiation.add(propertyRadiationHUDPosition.getName());
		propertyOrderRadiation.add(propertyRadiationHUDPositionCartesian.getName());
		propertyOrderRadiation.add(propertyRadiationHUDTextOutline.getName());
		propertyOrderRadiation.add(propertyRadiationRequireCounter.getName());
		propertyOrderRadiation.add(propertyRadiationChunkBoundaries.getName());
		propertyOrderRadiation.add(propertyRadiationUnitPrefixes.getName());
		propertyOrderRadiation.add(propertyRadiationBadgeDurability.getName());
		propertyOrderRadiation.add(propertyRadiationBadgeInfoRate.getName());
		config.setCategoryPropertyOrder(CATEGORY_RADIATION, propertyOrderRadiation);
		
		List<String> propertyOrderOther = new ArrayList<String>();
		propertyOrderOther.add(propertySingleCreativeTab.getName());
		propertyOrderOther.add(propertyRegisterProcessor.getName());
		propertyOrderOther.add(propertyRegisterPassive.getName());
		propertyOrderOther.add(propertyRegisterTool.getName());
		propertyOrderOther.add(propertyRegisterArmor.getName());
		propertyOrderOther.add(propertyCtrlInfo.getName());
		propertyOrderOther.add(propertyJEIChanceItemsIncludeNull.getName());
		propertyOrderOther.add(propertyRareDrops.getName());
		propertyOrderOther.add(propertyDungeonLoot.getName());
		propertyOrderOther.add(propertyWastelandBiome.getName());
		propertyOrderOther.add(propertyWastelandBiomeWeight.getName());
		propertyOrderOther.add(propertyWastelandDimensionGen.getName());
		propertyOrderOther.add(propertyWastelandDimension.getName());
		propertyOrderOther.add(propertyMushroomSpreadRate.getName());
		propertyOrderOther.add(propertyMushroomGen.getName());
		propertyOrderOther.add(propertyMushroomGenSize.getName());
		propertyOrderOther.add(propertyMushroomGenRate.getName());
		propertyOrderOther.add(propertyRegisterFluidBlocks.getName());
		propertyOrderOther.add(propertyRegisterCoFHFluids.getName());
		propertyOrderOther.add(propertyRegisterProjectEEMC.getName());
		propertyOrderOther.add(propertyOreDictPriorityBool.getName());
		propertyOrderOther.add(propertyOreDictPriority.getName());
		config.setCategoryPropertyOrder(CATEGORY_OTHER, propertyOrderOther);
		
		if (setFromConfig) {
			ore_dims = propertyOreDims.getIntList();
			ore_dims_list_type = propertyOreDimsListType.getBoolean();
			ore_gen = readBooleanArrayFromConfig(propertyOreGen);
			ore_size = readIntegerArrayFromConfig(propertyOreSize);
			ore_rate = readIntegerArrayFromConfig(propertyOreRate);
			ore_min_height = readIntegerArrayFromConfig(propertyOreMinHeight);
			ore_max_height = readIntegerArrayFromConfig(propertyOreMaxHeight);
			ore_drops = readBooleanArrayFromConfig(propertyOreDrops);
			hide_disabled_ores = propertyHideDisabledOres.getBoolean();
			ore_harvest_levels = readIntegerArrayFromConfig(propertyOreHarvestLevels);
			
			processor_time = readIntegerArrayFromConfig(propertyProcessorTime);
			processor_power = readIntegerArrayFromConfig(propertyProcessorPower);
			speed_upgrade_power_laws = readDoubleArrayFromConfig(propertySpeedUpgradePowerLaws);
			speed_upgrade_multipliers = readDoubleArrayFromConfig(propertySpeedUpgradeMultipliers);
			energy_upgrade_power_laws = readDoubleArrayFromConfig(propertyEnergyUpgradePowerLaws);
			energy_upgrade_multipliers = readDoubleArrayFromConfig(propertyEnergyUpgradeMultipliers);
			rf_per_eu = propertyRFPerEU.getInt();
			enable_gtce_eu = propertyEnableGTCEEU.getBoolean();
			enable_mek_gas = propertyEnableMekGas.getBoolean();
			machine_update_rate = propertyMachineUpdateRate.getInt();
			processor_passive_rate = readIntegerArrayFromConfig(propertyProcessorPassiveRate);
			cobble_gen_power = propertyCobbleGenPower.getInt();
			ore_processing = propertyOreProcessing.getBoolean();
			manufactory_wood = readIntegerArrayFromConfig(propertyManufactoryWood);
			rock_crusher_alternate = propertyRockCrusherAlternate.getBoolean();
			gtce_recipe_integration = readBooleanArrayFromConfig(propertyGTCERecipes);
			ProcessorRecipeHandler.initGTCEIntegration();
			gtce_recipe_logging = propertyGTCERecipeLogging.getBoolean();
			smart_processor_input = propertySmartProcessorInput.getBoolean();
			passive_permeation = propertyPermeation.getBoolean();
			processor_particles = propertyProcessorParticles.getBoolean();
			
			rtg_power = readIntegerArrayFromConfig(propertyRTGPower);
			solar_power = readIntegerArrayFromConfig(propertySolarPower);
			decay_lifetime = readDoubleArrayFromConfig(propertyDecayLifetime);
			decay_power = readIntegerArrayFromConfig(propertyDecayPower);
			
			fission_power = propertyFissionPower.getDouble();
			fission_fuel_use = propertyFissionFuelUse.getDouble();
			fission_heat_generation = propertyFissionHeatGeneration.getDouble();
			fission_cooling_rate = readDoubleArrayFromConfig(propertyFissionCoolingRate);
			fission_active_cooling_rate = readDoubleArrayFromConfig(propertyFissionActiveCoolingRate);
			fission_water_cooler_requirement = propertyFissionWaterCoolerRequirement.getBoolean();
			fission_overheat = propertyFissionOverheat.getBoolean();
			fission_explosions = propertyFissionExplosions.getBoolean();
			fission_meltdown_radiation_multiplier = propertyFissionMeltdownRadiationMultiplier.getDouble();
			fission_min_size = propertyFissionMinSize.getInt();
			fission_max_size = propertyFissionMaxSize.getInt();
			fission_comparator_max_heat = propertyFissionComparatorMaxHeat.getInt();
			fission_force_heat_comparator = propertyFissionForceHeatComparator.getBoolean();
			active_cooler_max_rate = propertyFissionActiveCoolerMaxRate.getInt();
			
			fission_moderator_extra_power = propertyFissionModeratorExtraPower.getDouble();
			fission_moderator_extra_heat = propertyFissionModeratorExtraHeat.getDouble();
			fission_neutron_reach = propertyFissionNeutronReach.getInt();
			
			fission_thorium_fuel_time = readDoubleArrayFromConfig(propertyFissionThoriumFuelTime);
			fission_thorium_power = readDoubleArrayFromConfig(propertyFissionThoriumPower);
			fission_thorium_heat_generation = readDoubleArrayFromConfig(propertyFissionThoriumHeatGeneration);
			fission_thorium_radiation = readDoubleArrayFromConfig(propertyFissionThoriumRadiation);
			
			fission_uranium_fuel_time = readDoubleArrayFromConfig(propertyFissionUraniumFuelTime);
			fission_uranium_power = readDoubleArrayFromConfig(propertyFissionUraniumPower);
			fission_uranium_heat_generation = readDoubleArrayFromConfig(propertyFissionUraniumHeatGeneration);
			fission_uranium_radiation = readDoubleArrayFromConfig(propertyFissionUraniumRadiation);
			
			fission_neptunium_fuel_time = readDoubleArrayFromConfig(propertyFissionNeptuniumFuelTime);
			fission_neptunium_power = readDoubleArrayFromConfig(propertyFissionNeptuniumPower);
			fission_neptunium_heat_generation = readDoubleArrayFromConfig(propertyFissionNeptuniumHeatGeneration);
			fission_neptunium_radiation = readDoubleArrayFromConfig(propertyFissionNeptuniumRadiation);
			
			fission_plutonium_fuel_time = readDoubleArrayFromConfig(propertyFissionPlutoniumFuelTime);
			fission_plutonium_power = readDoubleArrayFromConfig(propertyFissionPlutoniumPower);
			fission_plutonium_heat_generation = readDoubleArrayFromConfig(propertyFissionPlutoniumHeatGeneration);
			fission_plutonium_radiation = readDoubleArrayFromConfig(propertyFissionPlutoniumRadiation);
			
			fission_mox_fuel_time = readDoubleArrayFromConfig(propertyFissionMOXFuelTime);
			fission_mox_power = readDoubleArrayFromConfig(propertyFissionMOXPower);
			fission_mox_heat_generation = readDoubleArrayFromConfig(propertyFissionMOXHeatGeneration);
			fission_mox_radiation = readDoubleArrayFromConfig(propertyFissionMOXRadiation);
			
			fission_americium_fuel_time = readDoubleArrayFromConfig(propertyFissionAmericiumFuelTime);
			fission_americium_power = readDoubleArrayFromConfig(propertyFissionAmericiumPower);
			fission_americium_heat_generation = readDoubleArrayFromConfig(propertyFissionAmericiumHeatGeneration);
			fission_americium_radiation = readDoubleArrayFromConfig(propertyFissionAmericiumRadiation);
			
			fission_curium_fuel_time = readDoubleArrayFromConfig(propertyFissionCuriumFuelTime);
			fission_curium_power = readDoubleArrayFromConfig(propertyFissionCuriumPower);
			fission_curium_heat_generation = readDoubleArrayFromConfig(propertyFissionCuriumHeatGeneration);
			fission_curium_radiation = readDoubleArrayFromConfig(propertyFissionCuriumRadiation);
			
			fission_berkelium_fuel_time = readDoubleArrayFromConfig(propertyFissionBerkeliumFuelTime);
			fission_berkelium_power = readDoubleArrayFromConfig(propertyFissionBerkeliumPower);
			fission_berkelium_heat_generation = readDoubleArrayFromConfig(propertyFissionBerkeliumHeatGeneration);
			fission_berkelium_radiation = readDoubleArrayFromConfig(propertyFissionBerkeliumRadiation);
			
			fission_californium_fuel_time = readDoubleArrayFromConfig(propertyFissionCaliforniumFuelTime);
			fission_californium_power = readDoubleArrayFromConfig(propertyFissionCaliforniumPower);
			fission_californium_heat_generation = readDoubleArrayFromConfig(propertyFissionCaliforniumHeatGeneration);
			fission_californium_radiation = readDoubleArrayFromConfig(propertyFissionCaliforniumRadiation);
			
			fusion_base_power = propertyFusionBasePower.getDouble();
			fusion_fuel_use = propertyFusionFuelUse.getDouble();
			fusion_heat_generation = propertyFusionHeatGeneration.getDouble();
			fusion_heating_multiplier = propertyFusionHeatingMultiplier.getDouble();
			fusion_overheat = propertyFusionOverheat.getBoolean();
			fusion_meltdown_radiation_multiplier = propertyFusionMeltdownRadiationMultiplier.getDouble();
			fusion_active_cooling = propertyFusionActiveCooling.getBoolean();
			fusion_active_cooling_rate = readDoubleArrayFromConfig(propertyFusionActiveCoolingRate);
			fusion_min_size = propertyFusionMinSize.getInt();
			fusion_max_size = propertyFusionMaxSize.getInt();
			fusion_comparator_max_efficiency = propertyFusionComparatorMaxEfficiency.getInt();
			fusion_electromagnet_power = propertyFusionElectromagnetPower.getInt();
			fusion_enable_sound = propertyFusionEnableSound.getBoolean();
			fusion_plasma_craziness = propertyFusionPlasmaCraziness.getBoolean();
			
			fusion_fuel_time = readDoubleArrayFromConfig(propertyFusionFuelTime);
			fusion_power = readDoubleArrayFromConfig(propertyFusionPower);
			fusion_heat_variable = readDoubleArrayFromConfig(propertyFusionHeatVariable);
			fusion_radiation = readDoubleArrayFromConfig(propertyFusionRadiation);
			
			salt_fission_power = propertySaltFissionPower.getDouble();
			salt_fission_fuel_use = propertySaltFissionFuelUse.getDouble();
			salt_fission_heat_generation = propertySaltFissionHeatGeneration.getDouble();
			salt_fission_overheat = propertySaltFissionOverheat.getBoolean();
			salt_fission_meltdown_radiation_multiplier = propertySaltFissionMeltdownRadiationMultiplier.getDouble();
			salt_fission_min_size = propertySaltFissionMinSize.getInt();
			salt_fission_max_size = propertySaltFissionMaxSize.getInt();
			salt_fission_cooling_rate = readDoubleArrayFromConfig(propertySaltFissionCoolingRate);
			salt_fission_redstone_max_heat = propertySaltFissionRedstoneMaxHeat.getInt();
			salt_fission_max_distribution_rate = propertySaltFissionMaxDistributionRate.getInt();
			
			heat_exchanger_min_size = propertyHeatExchangerMinSize.getInt();
			heat_exchanger_max_size = propertyHeatExchangerMaxSize.getInt();
			heat_exchanger_conductivity = readDoubleArrayFromConfig(propertyHeatExchangerConductivity);
			heat_exchanger_coolant_mult = propertyHeatExchangerCoolantMult.getDouble();
			heat_exchanger_alternate_exhaust_recipe = propertyHeatExchangerAlternateExhaustRecipe.getBoolean();
			
			turbine_min_size = propertyTurbineMinSize.getInt();
			turbine_max_size = propertyTurbineMaxSize.getInt();
			turbine_blade_efficiency = readDoubleArrayFromConfig(propertyTurbineBladeEfficiency);
			turbine_blade_expansion = readDoubleArrayFromConfig(propertyTurbineBladeExpansion);
			turbine_stator_expansion = propertyTurbineStatorExpansion.getDouble();
			turbine_coil_conductivity = readDoubleArrayFromConfig(propertyTurbineCoilConductivity);
			turbine_power_per_mb = readDoubleArrayFromConfig(propertyTurbinePowerPerMB);
			turbine_expansion_level = readDoubleArrayFromConfig(propertyTurbineExpansionLevel);
			turbine_mb_per_blade = propertyTurbineMBPerBlade.getInt();
			
			accelerator_electromagnet_power = propertyAcceleratorElectromagnetPower.getInt();
			accelerator_supercooler_coolant = propertyAcceleratorSupercoolerCoolant.getInt();
			
			battery_capacity = readIntegerArrayFromConfig(propertyBatteryCapacity);
			
			tool_mining_level = readIntegerArrayFromConfig(propertyToolMiningLevel);
			tool_durability = readIntegerArrayFromConfig(propertyToolDurability);
			tool_speed = readDoubleArrayFromConfig(propertyToolSpeed);
			tool_attack_damage = readDoubleArrayFromConfig(propertyToolAttackDamage);
			tool_enchantability = readIntegerArrayFromConfig(propertyToolEnchantability);
			tool_handle_modifier = readDoubleArrayFromConfig(propertyToolHandleModifier);
			tool_tic_register = readBooleanArrayFromConfig(propertyToolTiCRegister);
			
			armor_durability = readIntegerArrayFromConfig(propertyArmorDurability);
			armor_enchantability = readIntegerArrayFromConfig(propertyArmorEnchantability);
			armor_boron = readIntegerArrayFromConfig(propertyArmorBoron);
			armor_tough = readIntegerArrayFromConfig(propertyArmorTough);
			armor_hard_carbon = readIntegerArrayFromConfig(propertyArmorHardCarbon);
			armor_boron_nitride = readIntegerArrayFromConfig(propertyArmorBoronNitride);
			armor_hazmat = readIntegerArrayFromConfig(propertyArmorHazmat);
			armor_toughness = readDoubleArrayFromConfig(propertyArmorToughness);
			armor_conarm_register = readBooleanArrayFromConfig(propertyArmorConarmRegister);
			
			entity_register = readBooleanArrayFromConfig(propertyEntityRegister);
			entity_tracking_range = propertyEntityTrackingRange.getInt();
			
			radiation_enabled = propertyRadiationEnabled.getBoolean();
			
			radiation_world_tick_rate = propertyRadiationWorldTickRate.getInt();
			radiation_player_tick_rate = propertyRadiationPlayerTickRate.getInt();
			
			radiation_worlds = propertyRadiationWorlds.getStringList();
			radiation_biomes = propertyRadiationBiomes.getStringList();
			radiation_structures = propertyRadiationStructures.getStringList();
			radiation_world_limits = propertyRadiationWorldLimit.getStringList();
			radiation_biome_limits = propertyRadiationBiomeLimit.getStringList();
			radiation_from_biomes_dims_blacklist = propertyRadiationFromBiomesDimsBlacklist.getIntList();
			
			radiation_ores = propertyRadiationOres.getStringList();
			radiation_items = propertyRadiationItems.getStringList();
			radiation_blocks = propertyRadiationBlocks.getStringList();
			radiation_fluids = propertyRadiationFluids.getStringList();
			radiation_foods = propertyRadiationFoods.getStringList();
			radiation_ores_blacklist = propertyRadiationOresBlacklist.getStringList();
			radiation_items_blacklist = propertyRadiationItemsBlacklist.getStringList();
			radiation_blocks_blacklist = propertyRadiationBlocksBlacklist.getStringList();
			radiation_fluids_blacklist = propertyRadiationFluidsBlacklist.getStringList();
			
			max_player_rads = propertyRadiationMaxPlayerRads.getDouble();
			radiation_player_decay_rate = propertyRadiationPlayerDecayRate.getDouble();
			radiation_entity_decay_rate = propertyRadiationEntityDecayRate.getDouble();
			radiation_spread_rate = propertyRadiationSpreadRate.getDouble();
			radiation_spread_gradient = propertyRadiationSpreadGradient.getDouble();
			radiation_decay_rate = propertyRadiationDecayRate.getDouble();
			radiation_lowest_rate = propertyRadiationLowestRate.getDouble();
			radiation_chunk_limit = propertyRadiationChunkLimit.getDouble();
			
			radiation_block_effects = propertyRadiationBlockEffects.getStringList();
			radiation_block_effect_limit = propertyRadiationBlockEffectLimit.getDouble();
			radiation_block_effect_max_rate = propertyRadiationBlockEffectMaxRate.getInt();
			radiation_rain_mult = propertyRadiationRainMult.getDouble();
			radiation_swim_mult = propertyRadiationSwimMult.getDouble();
			
			radiation_radaway_amount = propertyRadiationRadawayAmount.getDouble();
			radiation_radaway_slow_amount = propertyRadiationRadawaySlowAmount.getDouble();
			radiation_radaway_rate = propertyRadiationRadawayRate.getDouble();
			radiation_radaway_slow_rate = propertyRadiationRadawaySlowRate.getDouble();
			radiation_poison_time = propertyRadiationPoisonTime.getDouble();
			radiation_radaway_cooldown = propertyRadiationRadawayCooldown.getDouble();
			radiation_rad_x_amount = propertyRadiationRadXAmount.getDouble();
			radiation_rad_x_lifetime = propertyRadiationRadXLifetime.getDouble();
			radiation_rad_x_cooldown = propertyRadiationRadXCooldown.getDouble();
			radiation_shielding_level = readDoubleArrayFromConfig(propertyRadiationShieldingLevel);
			radiation_tile_shielding = propertyRadiationTileShielding.getBoolean();
			radiation_scrubber_fraction = propertyRadiationScrubberRate.getDouble();
			radiation_scrubber_radius = propertyRadiationScrubberRadius.getInt();
			radiation_scrubber_alt = propertyRadiationScrubberAlt.getBoolean();
			radiation_scrubber_param = readDoubleArrayFromConfig(propertyRadiationScrubberParam);
			radiation_scrubber_power = propertyRadiationScrubberPower.getInt();
			radiation_scrubber_borax_rate = propertyRadiationScrubberBoraxRate.getInt();
			radiation_geiger_block_redstone = propertyRadiationGeigerBlockRedstone.getDouble();
			
			radiation_shielding_default_recipes = propertyRadiationShieldingDefaultRecipes.getBoolean();
			radiation_shielding_item_blacklist = propertyRadiationShieldingItemBlacklist.getStringList();
			radiation_shielding_custom_stacks = propertyRadiationShieldingCustomStacks.getStringList();
			radiation_shielding_default_levels = propertyRadiationShieldingDefaultLevels.getStringList();
			
			radiation_hardcore_stacks = propertyRadiationHardcoreStacks.getBoolean();
			radiation_hardcore_containers = propertyRadiationHardcoreContainers.getDouble();
			radiation_dropped_items = propertyRadiationDroppedItems.getBoolean();
			radiation_death_persist = propertyRadiationDeathPersist.getBoolean();
			radiation_death_persist_fraction = propertyRadiationDeathPersistFraction.getDouble();
			radiation_death_immunity_time = propertyRadiationDeathImmunityTime.getDouble();
			
			radiation_passive_debuff_lists = propertyRadiationPassiveDebuffLists.getStringList();
			radiation_mob_buff_lists = propertyRadiationMobBuffLists.getStringList();
			
			radiation_horse_armor = propertyRadiationHorseArmor.getBoolean();
			
			radiation_hud_size = propertyRadiationHUDSize.getDouble();
			radiation_hud_position = propertyRadiationHUDPosition.getDouble();
			radiation_hud_position_cartesian = propertyRadiationHUDPositionCartesian.getDoubleList();
			radiation_hud_text_outline = propertyRadiationHUDTextOutline.getBoolean();
			radiation_require_counter = propertyRadiationRequireCounter.getBoolean();
			radiation_chunk_boundaries = propertyRadiationChunkBoundaries.getBoolean();
			radiation_unit_prefixes = propertyRadiationUnitPrefixes.getInt();
			
			radiation_badge_durability = propertyRadiationBadgeDurability.getDouble();
			radiation_badge_info_rate = propertyRadiationBadgeInfoRate.getDouble();
			
			single_creative_tab = propertySingleCreativeTab.getBoolean();
			register_processor = readBooleanArrayFromConfig(propertyRegisterProcessor);
			register_passive = readBooleanArrayFromConfig(propertyRegisterPassive);
			register_tool = readBooleanArrayFromConfig(propertyRegisterTool);
			register_armor = readBooleanArrayFromConfig(propertyRegisterArmor);
			ctrl_info = propertyCtrlInfo.getBoolean();
			jei_chance_items_include_null = propertyJEIChanceItemsIncludeNull.getBoolean();
			rare_drops = propertyRareDrops.getBoolean();
			dungeon_loot = propertyDungeonLoot.getBoolean();
			wasteland_biome = propertyWastelandBiome.getBoolean();
			wasteland_biome_weight = propertyWastelandBiomeWeight.getInt();
			wasteland_dimension_gen = propertyWastelandDimensionGen.getBoolean();
			wasteland_dimension = propertyWastelandDimension.getInt();
			mushroom_spread_rate = propertyMushroomSpreadRate.getInt();
			mushroom_gen = propertyMushroomGen.getBoolean();
			mushroom_gen_size = propertyMushroomGenSize.getInt();
			mushroom_gen_rate = propertyMushroomGenRate.getInt();
			register_fission_fluid_blocks = propertyRegisterFluidBlocks.getBoolean();
			register_cofh_fluids = propertyRegisterCoFHFluids.getBoolean();
			register_projecte_emc = propertyRegisterProjectEEMC.getBoolean();
			ore_dict_priority_bool = propertyOreDictPriorityBool.getBoolean();
			ore_dict_priority = propertyOreDictPriority.getStringList();
		}
		
		propertyOreDims.set(ore_dims);
		propertyOreDimsListType.set(ore_dims_list_type);
		propertyOreGen.set(ore_gen);
		propertyOreSize.set(ore_size);
		propertyOreRate.set(ore_rate);
		propertyOreMinHeight.set(ore_min_height);
		propertyOreMaxHeight.set(ore_max_height);
		propertyOreDrops.set(ore_drops);
		propertyHideDisabledOres.set(hide_disabled_ores);
		propertyOreHarvestLevels.set(ore_harvest_levels);
		
		propertyProcessorTime.set(processor_time);
		propertyProcessorPower.set(processor_power);
		propertySpeedUpgradePowerLaws.set(speed_upgrade_power_laws);
		propertySpeedUpgradeMultipliers.set(speed_upgrade_multipliers);
		propertyEnergyUpgradePowerLaws.set(energy_upgrade_power_laws);
		propertyEnergyUpgradeMultipliers.set(energy_upgrade_multipliers);
		propertyRFPerEU.set(rf_per_eu);
		propertyEnableGTCEEU.set(enable_gtce_eu);
		propertyEnableMekGas.set(enable_mek_gas);
		propertyMachineUpdateRate.set(machine_update_rate);
		propertyProcessorPassiveRate.set(processor_passive_rate);
		propertyCobbleGenPower.set(cobble_gen_power);
		propertyOreProcessing.set(ore_processing);
		propertyManufactoryWood.set(manufactory_wood);
		propertyRockCrusherAlternate.set(rock_crusher_alternate);
		propertyGTCERecipes.set(gtce_recipe_integration);
		propertyGTCERecipeLogging.set(gtce_recipe_logging);
		propertySmartProcessorInput.set(smart_processor_input);
		propertyPermeation.set(passive_permeation);
		propertyProcessorParticles.set(processor_particles);
		
		propertyRTGPower.set(rtg_power);
		propertySolarPower.set(solar_power);
		propertyDecayLifetime.set(decay_lifetime);
		propertyDecayPower.set(decay_power);
		
		propertyFissionPower.set(fission_power);
		propertyFissionFuelUse.set(fission_fuel_use);
		propertyFissionHeatGeneration.set(fission_heat_generation);
		propertyFissionCoolingRate.set(fission_cooling_rate);
		propertyFissionActiveCoolingRate.set(fission_active_cooling_rate);
		propertyFissionWaterCoolerRequirement.set(fission_water_cooler_requirement);
		propertyFissionOverheat.set(fission_overheat);
		propertyFissionExplosions.set(fission_explosions);
		propertyFissionMeltdownRadiationMultiplier.set(fission_meltdown_radiation_multiplier);
		propertyFissionMinSize.set(fission_min_size);
		propertyFissionMaxSize.set(fission_max_size);
		propertyFissionComparatorMaxHeat.set(fission_comparator_max_heat);
		propertyFissionForceHeatComparator.set(fission_force_heat_comparator);
		propertyFissionActiveCoolerMaxRate.set(active_cooler_max_rate);
		
		propertyFissionModeratorExtraPower.set(fission_moderator_extra_power);
		propertyFissionModeratorExtraHeat.set(fission_moderator_extra_heat);
		propertyFissionNeutronReach.set(fission_neutron_reach);
		
		propertyFissionThoriumFuelTime.set(fission_thorium_fuel_time);
		propertyFissionThoriumPower.set(fission_thorium_power);
		propertyFissionThoriumHeatGeneration.set(fission_thorium_heat_generation);
		propertyFissionThoriumRadiation.set(fission_thorium_radiation);
		
		propertyFissionUraniumFuelTime.set(fission_uranium_fuel_time);
		propertyFissionUraniumPower.set(fission_uranium_power);
		propertyFissionUraniumHeatGeneration.set(fission_uranium_heat_generation);
		propertyFissionUraniumRadiation.set(fission_uranium_radiation);
		
		propertyFissionNeptuniumFuelTime.set(fission_neptunium_fuel_time);
		propertyFissionNeptuniumPower.set(fission_neptunium_power);
		propertyFissionNeptuniumHeatGeneration.set(fission_neptunium_heat_generation);
		propertyFissionNeptuniumRadiation.set(fission_neptunium_radiation);
		
		propertyFissionPlutoniumFuelTime.set(fission_plutonium_fuel_time);
		propertyFissionPlutoniumPower.set(fission_plutonium_power);
		propertyFissionPlutoniumHeatGeneration.set(fission_plutonium_heat_generation);
		propertyFissionPlutoniumRadiation.set(fission_plutonium_radiation);
		
		propertyFissionMOXFuelTime.set(fission_mox_fuel_time);
		propertyFissionMOXPower.set(fission_mox_power);
		propertyFissionMOXHeatGeneration.set(fission_mox_heat_generation);
		propertyFissionMOXRadiation.set(fission_mox_radiation);
		
		propertyFissionAmericiumFuelTime.set(fission_americium_fuel_time);
		propertyFissionAmericiumPower.set(fission_americium_power);
		propertyFissionAmericiumHeatGeneration.set(fission_americium_heat_generation);
		propertyFissionAmericiumRadiation.set(fission_americium_radiation);
		
		propertyFissionCuriumFuelTime.set(fission_curium_fuel_time);
		propertyFissionCuriumPower.set(fission_curium_power);
		propertyFissionCuriumHeatGeneration.set(fission_curium_heat_generation);
		propertyFissionCuriumRadiation.set(fission_curium_radiation);
		
		propertyFissionBerkeliumFuelTime.set(fission_berkelium_fuel_time);
		propertyFissionBerkeliumPower.set(fission_berkelium_power);
		propertyFissionBerkeliumHeatGeneration.set(fission_berkelium_heat_generation);
		propertyFissionBerkeliumRadiation.set(fission_berkelium_radiation);
		
		propertyFissionCaliforniumFuelTime.set(fission_californium_fuel_time);
		propertyFissionCaliforniumPower.set(fission_californium_power);
		propertyFissionCaliforniumHeatGeneration.set(fission_californium_heat_generation);
		propertyFissionCaliforniumRadiation.set(fission_californium_radiation);
		
		propertyFusionBasePower.set(fusion_base_power);
		propertyFusionFuelUse.set(fusion_fuel_use);
		propertyFusionHeatGeneration.set(fusion_heat_generation);
		propertyFusionHeatingMultiplier.set(fusion_heating_multiplier);
		propertyFusionOverheat.set(fusion_overheat);
		propertyFusionMeltdownRadiationMultiplier.set(fusion_meltdown_radiation_multiplier);
		propertyFusionActiveCooling.set(fusion_active_cooling);
		propertyFusionActiveCoolingRate.set(fusion_active_cooling_rate);
		propertyFusionMinSize.set(fusion_min_size);
		propertyFusionMaxSize.set(fusion_max_size);
		propertyFusionComparatorMaxEfficiency.set(fusion_comparator_max_efficiency);
		propertyFusionElectromagnetPower.set(fusion_electromagnet_power);
		propertyFusionEnableSound.set(fusion_enable_sound);
		propertyFusionPlasmaCraziness.set(fusion_plasma_craziness);
		
		propertyFusionFuelTime.set(fusion_fuel_time);
		propertyFusionPower.set(fusion_power);
		propertyFusionHeatVariable.set(fusion_heat_variable);
		propertyFusionRadiation.set(fusion_radiation);
		
		propertySaltFissionPower.set(salt_fission_power);
		propertySaltFissionFuelUse.set(salt_fission_fuel_use);
		propertySaltFissionHeatGeneration.set(salt_fission_heat_generation);
		propertySaltFissionOverheat.set(salt_fission_overheat);
		propertySaltFissionMeltdownRadiationMultiplier.set(salt_fission_meltdown_radiation_multiplier);
		propertySaltFissionMinSize.set(salt_fission_min_size);
		propertySaltFissionMaxSize.set(salt_fission_max_size);
		propertySaltFissionCoolingRate.set(salt_fission_cooling_rate);
		propertySaltFissionRedstoneMaxHeat.set(salt_fission_redstone_max_heat);
		propertySaltFissionMaxDistributionRate.set(salt_fission_max_distribution_rate);
		
		propertyHeatExchangerMinSize.set(heat_exchanger_min_size);
		propertyHeatExchangerMaxSize.set(heat_exchanger_max_size);
		propertyHeatExchangerConductivity.set(heat_exchanger_conductivity);
		propertyHeatExchangerCoolantMult.set(heat_exchanger_coolant_mult);
		propertyHeatExchangerAlternateExhaustRecipe.set(heat_exchanger_alternate_exhaust_recipe);
		
		propertyTurbineMinSize.set(turbine_min_size);
		propertyTurbineMaxSize.set(turbine_max_size);
		propertyTurbineBladeEfficiency.set(turbine_blade_efficiency);
		propertyTurbineBladeExpansion.set(turbine_blade_expansion);
		propertyTurbineStatorExpansion.set(turbine_stator_expansion);
		propertyTurbineCoilConductivity.set(turbine_coil_conductivity);
		propertyTurbinePowerPerMB.set(turbine_power_per_mb);
		propertyTurbineExpansionLevel.set(turbine_expansion_level);
		propertyTurbineMBPerBlade.set(turbine_mb_per_blade);
		
		propertyAcceleratorElectromagnetPower.set(accelerator_electromagnet_power);
		propertyAcceleratorSupercoolerCoolant.set(accelerator_supercooler_coolant);
		
		propertyBatteryCapacity.set(battery_capacity);
		
		propertyToolMiningLevel.set(tool_mining_level);
		propertyToolDurability.set(tool_durability);
		propertyToolSpeed.set(tool_speed);
		propertyToolAttackDamage.set(tool_attack_damage);
		propertyToolEnchantability.set(tool_enchantability);
		propertyToolHandleModifier.set(tool_handle_modifier);
		propertyToolTiCRegister.set(tool_tic_register);
		
		propertyArmorDurability.set(armor_durability);
		propertyArmorEnchantability.set(armor_enchantability);
		propertyArmorBoron.set(armor_boron);
		propertyArmorTough.set(armor_tough);
		propertyArmorHardCarbon.set(armor_hard_carbon);
		propertyArmorBoronNitride.set(armor_boron_nitride);
		propertyArmorHazmat.set(armor_hazmat);
		propertyArmorToughness.set(armor_toughness);
		propertyArmorConarmRegister.set(armor_conarm_register);
		
		propertyEntityRegister.set(entity_register);
		propertyEntityTrackingRange.set(entity_tracking_range);
		
		propertyRadiationEnabled.set(radiation_enabled);
		
		propertyRadiationWorldTickRate.set(radiation_world_tick_rate);
		propertyRadiationPlayerTickRate.set(radiation_player_tick_rate);
		
		propertyRadiationWorlds.set(radiation_worlds);
		propertyRadiationBiomes.set(radiation_biomes);
		propertyRadiationStructures.set(radiation_structures);
		propertyRadiationWorldLimit.set(radiation_world_limits);
		propertyRadiationBiomeLimit.set(radiation_biome_limits);
		propertyRadiationFromBiomesDimsBlacklist.set(radiation_from_biomes_dims_blacklist);
		
		propertyRadiationOres.set(radiation_ores);
		propertyRadiationItems.set(radiation_items);
		propertyRadiationBlocks.set(radiation_blocks);
		propertyRadiationFluids.set(radiation_fluids);
		propertyRadiationFoods.set(radiation_foods);
		propertyRadiationOresBlacklist.set(radiation_ores_blacklist);
		propertyRadiationItemsBlacklist.set(radiation_items_blacklist);
		propertyRadiationBlocksBlacklist.set(radiation_blocks_blacklist);
		propertyRadiationFluidsBlacklist.set(radiation_fluids_blacklist);
		
		propertyRadiationMaxPlayerRads.set(max_player_rads);
		propertyRadiationPlayerDecayRate.set(radiation_player_decay_rate);
		propertyRadiationEntityDecayRate.set(radiation_entity_decay_rate);
		propertyRadiationSpreadRate.set(radiation_spread_rate);
		propertyRadiationSpreadGradient.set(radiation_spread_gradient);
		propertyRadiationDecayRate.set(radiation_decay_rate);
		propertyRadiationLowestRate.set(radiation_lowest_rate);
		propertyRadiationChunkLimit.set(radiation_chunk_limit);
		
		propertyRadiationBlockEffects.set(radiation_block_effects);
		propertyRadiationBlockEffectLimit.set(radiation_block_effect_limit);
		propertyRadiationBlockEffectMaxRate.set(radiation_block_effect_max_rate);
		propertyRadiationRainMult.set(radiation_rain_mult);
		propertyRadiationSwimMult.set(radiation_swim_mult);
		
		propertyRadiationRadawayAmount.set(radiation_radaway_amount);
		propertyRadiationRadawaySlowAmount.set(radiation_radaway_slow_amount);
		propertyRadiationRadawayRate.set(radiation_radaway_rate);
		propertyRadiationRadawaySlowRate.set(radiation_radaway_slow_rate);
		propertyRadiationPoisonTime.set(radiation_poison_time);
		propertyRadiationRadawayCooldown.set(radiation_radaway_cooldown);
		propertyRadiationRadXAmount.set(radiation_rad_x_amount);
		propertyRadiationRadXLifetime.set(radiation_rad_x_lifetime);
		propertyRadiationRadXCooldown.set(radiation_rad_x_cooldown);
		propertyRadiationShieldingLevel.set(radiation_shielding_level);
		propertyRadiationTileShielding.set(radiation_tile_shielding);
		propertyRadiationScrubberRate.set(radiation_scrubber_fraction);
		propertyRadiationScrubberRadius.set(radiation_scrubber_radius);
		propertyRadiationScrubberAlt.set(radiation_scrubber_alt);
		propertyRadiationScrubberParam.set(radiation_scrubber_param);
		propertyRadiationScrubberPower.set(radiation_scrubber_power);
		propertyRadiationScrubberBoraxRate.set(radiation_scrubber_borax_rate);
		propertyRadiationGeigerBlockRedstone.set(radiation_geiger_block_redstone);
		
		propertyRadiationShieldingDefaultRecipes.set(radiation_shielding_default_recipes);
		propertyRadiationShieldingItemBlacklist.set(radiation_shielding_item_blacklist);
		propertyRadiationShieldingCustomStacks.set(radiation_shielding_custom_stacks);
		propertyRadiationShieldingDefaultLevels.set(radiation_shielding_default_levels);
		
		propertyRadiationHardcoreStacks.set(radiation_hardcore_stacks);
		propertyRadiationHardcoreContainers.set(radiation_hardcore_containers);
		propertyRadiationDroppedItems.set(radiation_dropped_items);
		propertyRadiationDeathPersist.set(radiation_death_persist);
		propertyRadiationDeathPersistFraction.set(radiation_death_persist_fraction);
		propertyRadiationDeathImmunityTime.set(radiation_death_immunity_time);
		
		propertyRadiationPassiveDebuffLists.set(radiation_passive_debuff_lists);
		propertyRadiationMobBuffLists.set(radiation_mob_buff_lists);
		
		propertyRadiationHorseArmor.set(radiation_horse_armor);
		
		propertyRadiationHUDSize.set(radiation_hud_size);
		propertyRadiationHUDPosition.set(radiation_hud_position);
		propertyRadiationHUDPositionCartesian.set(radiation_hud_position_cartesian);
		propertyRadiationHUDTextOutline.set(radiation_hud_text_outline);
		propertyRadiationRequireCounter.set(radiation_require_counter);
		propertyRadiationChunkBoundaries.set(radiation_chunk_boundaries);
		propertyRadiationUnitPrefixes.set(radiation_unit_prefixes);
		
		propertyRadiationBadgeDurability.set(radiation_badge_durability);
		propertyRadiationBadgeInfoRate.set(radiation_badge_info_rate);
		
		propertySingleCreativeTab.set(single_creative_tab);
		propertyRegisterProcessor.set(register_processor);
		propertyRegisterPassive.set(register_passive);
		propertyRegisterTool.set(register_tool);
		propertyRegisterArmor.set(register_armor);
		propertyCtrlInfo.set(ctrl_info);
		propertyJEIChanceItemsIncludeNull.set(jei_chance_items_include_null);
		propertyRareDrops.set(rare_drops);
		propertyWastelandBiome.set(wasteland_biome);
		propertyWastelandBiomeWeight.set(wasteland_biome_weight);
		propertyWastelandDimensionGen.set(wasteland_dimension_gen);
		propertyWastelandDimension.set(wasteland_dimension);
		propertyMushroomSpreadRate.set(mushroom_spread_rate);
		propertyMushroomGen.set(mushroom_gen);
		propertyMushroomGenSize.set(mushroom_gen_size);
		propertyMushroomGenRate.set(mushroom_gen_rate);
		propertyRegisterFluidBlocks.set(register_fission_fluid_blocks);
		propertyRegisterCoFHFluids.set(register_cofh_fluids);
		propertyRegisterProjectEEMC.set(register_projecte_emc);
		propertyOreDictPriorityBool.set(ore_dict_priority_bool);
		propertyOreDictPriority.set(ore_dict_priority);
		
		if (setFromConfig) {
			radiation_enabled_public = radiation_enabled;
			radiation_horse_armor_public = radiation_horse_armor;
		}
		
		if (config.hasChanged()) config.save();
	}
	
	private static boolean[] readBooleanArrayFromConfig(Property property) {
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
	
	private static int[] readIntegerArrayFromConfig(Property property) {
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
	
	private static double[] readDoubleArrayFromConfig(Property property) {
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
	
	private static class ServerConfigEventHandler {
		
		@SubscribeEvent
		public void configOnWorldLoad(PlayerLoggedInEvent event) {
			if (event.player instanceof EntityPlayerMP) {
				PacketHandler.instance.sendTo(getConfigUpdatePacket(), (EntityPlayerMP)event.player);
			}
		}
	}
	
	public static ConfigUpdatePacket getConfigUpdatePacket() {
		return new ConfigUpdatePacket(radiation_enabled, radiation_horse_armor);
	}
	
	public static void onConfigPacket(ConfigUpdatePacket message) {
		if (!radiation_enabled_public && message.radiation_enabled) {
			String unloc = "message.nuclearcraft.radiation_config_info" + (ModCheck.jeiLoaded() ? "_jei" : "");
			Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.GOLD + Lang.localise(unloc)));
		}
		radiation_enabled_public = message.radiation_enabled;
		radiation_horse_armor_public = message.radiation_horse_armor;
	}
	
	private static class ClientConfigEventHandler {
		
		@SubscribeEvent(priority = EventPriority.LOWEST)
		public void onEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(Global.MOD_ID)) syncFromGui();
		}
	}
}
