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
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class NCConfig {
	
	private static Configuration config = null;
	
	public static final String CATEGORY_WORLD_GEN = "world_gen";
	public static final String CATEGORY_PROCESSOR = "processor";
	public static final String CATEGORY_GENERATOR = "generator";
	public static final String CATEGORY_ENERGY_STORAGE = "energy_storage";
	public static final String CATEGORY_FISSION = "fission";
	public static final String CATEGORY_FUSION = "fusion";
	public static final String CATEGORY_HEAT_EXCHANGER = "heat_exchanger";
	public static final String CATEGORY_TURBINE = "turbine";
	public static final String CATEGORY_ACCELERATOR = "accelerator";
	public static final String CATEGORY_QUANTUM = "quantum";
	public static final String CATEGORY_TOOL = "tool";
	public static final String CATEGORY_ARMOR = "armor";
	public static final String CATEGORY_ENTITY = "entity";
	public static final String CATEGORY_RADIATION = "radiation";
	public static final String CATEGORY_REGISTRATION = "registration";
	public static final String CATEGORY_MISC = "misc";
	
	public static int[] ore_dims;
	public static boolean ore_dims_list_type;
	public static boolean[] ore_gen;
	public static int[] ore_size;
	public static int[] ore_rate;
	public static int[] ore_min_height;
	public static int[] ore_max_height;
	public static boolean[] ore_drops;
	public static boolean ore_hide_disabled;
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
	public static double[] processor_passive_rate;
	public static boolean passive_push;
	public static double cobble_gen_power;
	public static boolean ore_processing;
	public static int[] manufactory_wood;
	public static boolean rock_crusher_alternate;
	public static boolean[] gtce_recipe_integration;
	public static boolean gtce_recipe_logging;
	public static boolean smart_processor_input;
	public static boolean passive_permeation;
	public static boolean factor_recipes;
	public static boolean processor_particles;
	
	public static int[] rtg_power;
	public static int[] solar_power;
	public static double[] decay_lifetime;
	public static double[] decay_power;
	
	public static int[] battery_capacity;
	
	public static double fission_fuel_time_multiplier; // Default: 1
	public static double[] fission_source_efficiency;
	public static int[] fission_sink_cooling_rate;
	public static int[] fission_heater_cooling_rate;
	public static int[] fission_moderator_flux_factor;
	public static double[] fission_moderator_efficiency;
	public static double[] fission_reflector_efficiency;
	public static double[] fission_reflector_reflectivity;
	public static double[] fission_shield_heat_per_flux;
	public static double[] fission_shield_efficiency;
	public static double[] fission_irradiator_heat_per_flux;
	public static double[] fission_irradiator_efficiency;
	public static int fission_cooling_efficiency_leniency;
	public static double[] fission_sparsity_penalty_params; // Multiplier and threshold
	public static boolean fission_overheat;
	public static boolean fission_explosions;
	public static double fission_meltdown_radiation_multiplier;
	public static int fission_min_size; // Default: 1
	public static int fission_max_size; // Default: 24
	public static int fission_comparator_max_temp;
	public static int fission_neutron_reach;
	public static double fission_sound_volume;
	
	public static int[] fission_thorium_fuel_time;
	public static int[] fission_thorium_heat_generation;
	public static double[] fission_thorium_efficiency;
	public static int[] fission_thorium_criticality;
	public static boolean[] fission_thorium_self_priming;
	public static double[] fission_thorium_radiation;
	
	public static int[] fission_uranium_fuel_time;
	public static int[] fission_uranium_heat_generation;
	public static double[] fission_uranium_efficiency;
	public static int[] fission_uranium_criticality;
	public static boolean[] fission_uranium_self_priming;
	public static double[] fission_uranium_radiation;
	
	public static int[] fission_neptunium_fuel_time;
	public static int[] fission_neptunium_heat_generation;
	public static double[] fission_neptunium_efficiency;
	public static int[] fission_neptunium_criticality;
	public static boolean[] fission_neptunium_self_priming;
	public static double[] fission_neptunium_radiation;
	
	public static int[] fission_plutonium_fuel_time;
	public static int[] fission_plutonium_heat_generation;
	public static double[] fission_plutonium_efficiency;
	public static int[] fission_plutonium_criticality;
	public static boolean[] fission_plutonium_self_priming;
	public static double[] fission_plutonium_radiation;
	
	public static int[] fission_mixed_fuel_time;
	public static int[] fission_mixed_heat_generation;
	public static double[] fission_mixed_efficiency;
	public static int[] fission_mixed_criticality;
	public static boolean[] fission_mixed_self_priming;
	public static double[] fission_mixed_radiation;
	
	public static int[] fission_americium_fuel_time;
	public static int[] fission_americium_heat_generation;
	public static double[] fission_americium_efficiency;
	public static int[] fission_americium_criticality;
	public static boolean[] fission_americium_self_priming;
	public static double[] fission_americium_radiation;
	
	public static int[] fission_curium_fuel_time;
	public static int[] fission_curium_heat_generation;
	public static double[] fission_curium_efficiency;
	public static int[] fission_curium_criticality;
	public static boolean[] fission_curium_self_priming;
	public static double[] fission_curium_radiation;
	
	public static int[] fission_berkelium_fuel_time;
	public static int[] fission_berkelium_heat_generation;
	public static double[] fission_berkelium_efficiency;
	public static int[] fission_berkelium_criticality;
	public static boolean[] fission_berkelium_self_priming;
	public static double[] fission_berkelium_radiation;
	
	public static int[] fission_californium_fuel_time;
	public static int[] fission_californium_heat_generation;
	public static double[] fission_californium_efficiency;
	public static int[] fission_californium_criticality;
	public static boolean[] fission_californium_self_priming;
	public static double[] fission_californium_radiation;
	
	public static double fusion_base_power; // Default: 1
	public static double fusion_fuel_use; // Default: 1
	public static double fusion_heat_generation; // Default: 1
	public static double fusion_heating_multiplier; // Default: 1
	public static boolean fusion_overheat;
	public static double fusion_meltdown_radiation_multiplier;
	public static int fusion_min_size; // Default: 1
	public static int fusion_max_size; // Default: 24
	public static int fusion_comparator_max_efficiency;
	public static double fusion_electromagnet_power;
	public static boolean fusion_plasma_craziness;
	public static double fusion_sound_volume;
	
	public static double[] fusion_fuel_time;
	public static double[] fusion_power;
	public static double[] fusion_heat_variable;
	public static double[] fusion_radiation;
	
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
	public static double turbine_throughput_efficiency_leniency;
	public static double turbine_tension_throughput_factor;
	public static double turbine_sound_volume;
	
	public static double accelerator_electromagnet_power;
	public static double accelerator_supercooler_coolant;
	
	public static int quantum_max_qubits;
	public static int quantum_angle_precision;
	
	public static int[] tool_mining_level;
	public static int[] tool_durability;
	public static double[] tool_speed;
	public static double[] tool_attack_damage;
	public static int[] tool_enchantability;
	public static double[] tool_handle_modifier;
	
	public static int[] armor_durability;
	public static int[] armor_boron;
	public static int[] armor_tough;
	public static int[] armor_hard_carbon;
	public static int[] armor_boron_nitride;
	public static int[] armor_hazmat;
	public static int[] armor_enchantability;
	public static double[] armor_toughness;
	
	public static int entity_tracking_range;
	
	private static boolean radiation_enabled;
	public static boolean radiation_enabled_public;
	
	public static int radiation_world_chunks_per_tick;
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
	public static String[] max_entity_rads;
	public static double radiation_entity_decay_rate;
	public static double radiation_spread_rate;
	public static double radiation_spread_gradient;
	public static double radiation_decay_rate;
	public static double radiation_lowest_rate;
	public static double radiation_chunk_limit;
	
	//public static String[] radiation_block_effects;
	//public static double radiation_block_effect_limit;
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
	public static boolean radiation_scrubber_non_linear;
	public static double[] radiation_scrubber_param;
	public static int[] radiation_scrubber_time;
	public static int[] radiation_scrubber_power;
	public static double[] radiation_scrubber_efficiency;
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
	
	public static String[] radiation_player_debuff_lists;
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
	
	public static boolean[] register_processor;
	public static boolean[] register_passive;
	public static boolean[] register_battery;
	public static boolean register_quantum;
	public static boolean[] register_tool;
	public static boolean[] register_tic_tool;
	public static boolean[] register_armor;
	public static boolean[] register_conarm_armor;
	public static boolean[] register_entity;
	public static boolean register_fluid_blocks;
	public static boolean register_cofh_fluids;
	public static boolean register_projecte_emc;
	
	public static boolean single_creative_tab;
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
	
	public static boolean ore_dict_raw_material_recipes;
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
		
		Property propertyOreDims = config.get(CATEGORY_WORLD_GEN, "ore_dims", new int[] {0, 2, -6, -100, 4598, -9999, -11325}, Lang.localise("gui.nc.config.ore_dims.comment"), Integer.MIN_VALUE, Integer.MAX_VALUE);
		propertyOreDims.setLanguageKey("gui.nc.config.ore_dims");
		Property propertyOreDimsListType = config.get(CATEGORY_WORLD_GEN, "ore_dims_list_type", false, Lang.localise("gui.nc.config.ore_dims_list_type.comment"));
		propertyOreDimsListType.setLanguageKey("gui.nc.config.ore_dims_list_type");
		Property propertyOreGen = config.get(CATEGORY_WORLD_GEN, "ore_gen", new boolean[] {true, true, true, true, true, true, true, true}, Lang.localise("gui.nc.config.ore_gen.comment"));
		propertyOreGen.setLanguageKey("gui.nc.config.ore_gen");
		Property propertyOreSize = config.get(CATEGORY_WORLD_GEN, "ore_size", new int[] {6, 6, 6, 3, 3, 4, 4, 5}, Lang.localise("gui.nc.config.ore_size.comment"), 1, Integer.MAX_VALUE);
		propertyOreSize.setLanguageKey("gui.nc.config.ore_size");
		Property propertyOreRate = config.get(CATEGORY_WORLD_GEN, "ore_rate", new int[] {5, 4, 6, 3, 6, 6, 6, 4}, Lang.localise("gui.nc.config.ore_rate.comment"), 1, Integer.MAX_VALUE);
		propertyOreRate.setLanguageKey("gui.nc.config.ore_rate");
		Property propertyOreMinHeight = config.get(CATEGORY_WORLD_GEN, "ore_min_height", new int[] {0, 0, 0, 0, 0, 0, 0, 0}, Lang.localise("gui.nc.config.ore_min_height.comment"), 1, 255);
		propertyOreMinHeight.setLanguageKey("gui.nc.config.ore_min_height");
		Property propertyOreMaxHeight = config.get(CATEGORY_WORLD_GEN, "ore_max_height", new int[] {48, 40, 36, 32, 32, 28, 28, 24}, Lang.localise("gui.nc.config.ore_max_height.comment"), 1, 255);
		propertyOreMaxHeight.setLanguageKey("gui.nc.config.ore_max_height");
		Property propertyOreDrops = config.get(CATEGORY_WORLD_GEN, "ore_drops", new boolean[] {false, false, false, false, false, false, false}, Lang.localise("gui.nc.config.ore_drops.comment"));
		propertyOreDrops.setLanguageKey("gui.nc.config.ore_drops");
		Property propertyOreHideDisabled = config.get(CATEGORY_WORLD_GEN, "ore_hide_disabled", false, Lang.localise("gui.nc.config.ore_hide_disabled.comment"));
		propertyOreHideDisabled.setLanguageKey("gui.nc.config.ore_hide_disabled");
		Property propertyOreHarvestLevels = config.get(CATEGORY_WORLD_GEN, "ore_harvest_levels", new int[] {1, 1, 1, 2, 2, 2, 2, 2}, Lang.localise("gui.nc.config.ore_harvest_levels.comment"), 0, 15);
		propertyOreHarvestLevels.setLanguageKey("gui.nc.config.ore_harvest_levels");
		
		Property propertyWastelandBiome = config.get(CATEGORY_WORLD_GEN, "wasteland_biome", true, Lang.localise("gui.nc.config.wasteland_biome.comment"));
		propertyWastelandBiome.setLanguageKey("gui.nc.config.wasteland_biome");
		Property propertyWastelandBiomeWeight = config.get(CATEGORY_WORLD_GEN, "wasteland_biome_weight", 5, Lang.localise("gui.nc.config.wasteland_biome_weight.comment"), 0, 255);
		propertyWastelandBiomeWeight.setLanguageKey("gui.nc.config.wasteland_biome_weight");
		
		Property propertyWastelandDimensionGen = config.get(CATEGORY_WORLD_GEN, "wasteland_dimension_gen", true, Lang.localise("gui.nc.config.wasteland_dimension_gen.comment"));
		propertyWastelandDimensionGen.setLanguageKey("gui.nc.config.wasteland_dimension_gen");
		Property propertyWastelandDimension = config.get(CATEGORY_WORLD_GEN, "wasteland_dimension", 4598, Lang.localise("gui.nc.config.wasteland_dimension.comment"), Integer.MIN_VALUE, Integer.MAX_VALUE);
		propertyWastelandDimension.setLanguageKey("gui.nc.config.wasteland_dimension");
		
		Property propertyMushroomSpreadRate = config.get(CATEGORY_WORLD_GEN, "mushroom_spread_rate", 16, Lang.localise("gui.nc.config.mushroom_spread_rate.comment"), 0, 511);
		propertyMushroomSpreadRate.setLanguageKey("gui.nc.config.mushroom_spread_rate");
		Property propertyMushroomGen = config.get(CATEGORY_WORLD_GEN, "mushroom_gen", true, Lang.localise("gui.nc.config.mushroom_gen.comment"));
		propertyMushroomGen.setLanguageKey("gui.nc.config.mushroom_gen");
		Property propertyMushroomGenSize = config.get(CATEGORY_WORLD_GEN, "mushroom_gen_size", 64, Lang.localise("gui.nc.config.mushroom_gen_size.comment"), 0, 511);
		propertyMushroomGenSize.setLanguageKey("gui.nc.config.mushroom_gen_size");
		Property propertyMushroomGenRate = config.get(CATEGORY_WORLD_GEN, "mushroom_gen_rate", 40, Lang.localise("gui.nc.config.mushroom_gen_rate.comment"), 0, 511);
		propertyMushroomGenRate.setLanguageKey("gui.nc.config.mushroom_gen_rate");
		
		Property propertyProcessorTime = config.get(CATEGORY_PROCESSOR, "processor_time", new int[] {400, 800, 800, 400, 400, 600, 800, 600, 3200, 600, 400, 600, 800, 600, 1600, 600, 2400, 1200, 400}, Lang.localise("gui.nc.config.processor_time.comment"), 1, 128000);
		propertyProcessorTime.setLanguageKey("gui.nc.config.processor_time");
		Property propertyProcessorPower = config.get(CATEGORY_PROCESSOR, "processor_power", new int[] {20, 10, 10, 20, 10, 10, 40, 20, 40, 10, 0, 40, 10, 20, 10, 10, 10, 10, 20}, Lang.localise("gui.nc.config.processor_power.comment"), 0, 16000);
		propertyProcessorPower.setLanguageKey("gui.nc.config.processor_power");
		Property propertySpeedUpgradePowerLaws = config.get(CATEGORY_PROCESSOR, "speed_upgrade_power_laws_fp", new double[] {1D, 2D}, Lang.localise("gui.nc.config.speed_upgrade_power_laws_fp.comment"), 1D, 15D);
		propertySpeedUpgradePowerLaws.setLanguageKey("gui.nc.config.speed_upgrade_power_laws_fp");
		Property propertySpeedUpgradeMultipliers = config.get(CATEGORY_PROCESSOR, "speed_upgrade_multipliers_fp", new double[] {1D, 1D}, Lang.localise("gui.nc.config.speed_upgrade_multipliers_fp.comment"), 0D, 15D);
		propertySpeedUpgradeMultipliers.setLanguageKey("gui.nc.config.speed_upgrade_multipliers_fp");
		Property propertyEnergyUpgradePowerLaws = config.get(CATEGORY_PROCESSOR, "energy_upgrade_power_laws_fp", new double[] {1D}, Lang.localise("gui.nc.config.energy_upgrade_power_laws_fp.comment"), 1D, 15D);
		propertyEnergyUpgradePowerLaws.setLanguageKey("gui.nc.config.energy_upgrade_power_laws_fp");
		Property propertyEnergyUpgradeMultipliers = config.get(CATEGORY_PROCESSOR, "energy_upgrade_multipliers_fp", new double[] {1D}, Lang.localise("gui.nc.config.energy_upgrade_multipliers_fp.comment"), 0D, 15D);
		propertyEnergyUpgradeMultipliers.setLanguageKey("gui.nc.config.energy_upgrade_multipliers_fp");
		Property propertyRFPerEU = config.get(CATEGORY_PROCESSOR, "rf_per_eu", 16, Lang.localise("gui.nc.config.rf_per_eu.comment"), 1, 2000);
		propertyRFPerEU.setLanguageKey("gui.nc.config.rf_per_eu");
		Property propertyEnableGTCEEU = config.get(CATEGORY_PROCESSOR, "enable_gtce_eu", true, Lang.localise("gui.nc.config.enable_gtce_eu.comment"));
		propertyEnableGTCEEU.setLanguageKey("gui.nc.config.enable_gtce_eu");
		Property propertyEnableMekGas = config.get(CATEGORY_PROCESSOR, "enable_mek_gas", true, Lang.localise("gui.nc.config.enable_mek_gas.comment"));
		propertyEnableMekGas.setLanguageKey("gui.nc.config.enable_mek_gas");
		Property propertyMachineUpdateRate = config.get(CATEGORY_PROCESSOR, "machine_update_rate", 20, Lang.localise("gui.nc.config.machine_update_rate.comment"), 1, 1200);
		propertyMachineUpdateRate.setLanguageKey("gui.nc.config.machine_update_rate");
		Property propertyProcessorPassiveRate = config.get(CATEGORY_PROCESSOR, "processor_passive_rate", new double[] {0.125, 10, 5}, Lang.localise("gui.nc.config.processor_passive_rate.comment"), 0D, 4000D);
		propertyProcessorPassiveRate.setLanguageKey("gui.nc.config.processor_passive_rate");
		Property propertyPassivePush = config.get(CATEGORY_PROCESSOR, "passive_push", true, Lang.localise("gui.nc.config.passive_push.comment"));
		propertyPassivePush.setLanguageKey("gui.nc.config.passive_push");
		Property propertyCobbleGenPower = config.get(CATEGORY_PROCESSOR, "cobble_gen_power", 0D, Lang.localise("gui.nc.config.cobble_gen_power.comment"), 0D, 255D);
		propertyCobbleGenPower.setLanguageKey("gui.nc.config.cobble_gen_power");
		Property propertyOreProcessing = config.get(CATEGORY_PROCESSOR, "ore_processing", true, Lang.localise("gui.nc.config.ore_processing.comment"));
		propertyOreProcessing.setLanguageKey("gui.nc.config.ore_processing");
		Property propertyManufactoryWood = config.get(CATEGORY_PROCESSOR, "manufactory_wood", new int[] {6, 4}, Lang.localise("gui.nc.config.manufactory_wood.comment"), 1, 64);
		propertyManufactoryWood.setLanguageKey("gui.nc.config.manufactory_wood");
		Property propertyRockCrusherAlternate = config.get(CATEGORY_PROCESSOR, "rock_crusher_alternate", false, Lang.localise("gui.nc.config.rock_crusher_alternate.comment"));
		propertyRockCrusherAlternate.setLanguageKey("gui.nc.config.rock_crusher_alternate");
		Property propertyGTCERecipes = config.get(CATEGORY_PROCESSOR, "gtce_recipe_integration", new boolean[] {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true}, Lang.localise("gui.nc.config.gtce_recipe_integration.comment"));
		propertyGTCERecipes.setLanguageKey("gui.nc.config.gtce_recipe_integration");
		Property propertyGTCERecipeLogging = config.get(CATEGORY_PROCESSOR, "gtce_recipe_logging", false, Lang.localise("gui.nc.config.gtce_recipe_logging.comment"));
		propertyGTCERecipeLogging.setLanguageKey("gui.nc.config.gtce_recipe_logging");
		Property propertySmartProcessorInput = config.get(CATEGORY_PROCESSOR, "smart_processor_input", true, Lang.localise("gui.nc.config.smart_processor_input.comment"));
		propertySmartProcessorInput.setLanguageKey("gui.nc.config.smart_processor_input");
		Property propertyPermeation = config.get(CATEGORY_PROCESSOR, "passive_permeation", true, Lang.localise("gui.nc.config.passive_permeation.comment"));
		propertyPermeation.setLanguageKey("gui.nc.config.passive_permeation");
		Property propertyFactorRecipes = config.get(CATEGORY_PROCESSOR, "factor_recipes", false, Lang.localise("gui.nc.config.factor_recipes.comment"));
		propertyFactorRecipes.setLanguageKey("gui.nc.config.factor_recipes");
		Property propertyProcessorParticles = config.get(CATEGORY_PROCESSOR, "processor_particles", true, Lang.localise("gui.nc.config.processor_particles.comment"));
		propertyProcessorParticles.setLanguageKey("gui.nc.config.processor_particles");
		
		Property propertyRTGPower = config.get(CATEGORY_GENERATOR, "rtg_power", new int[] {1, 50, 20, 400}, Lang.localise("gui.nc.config.rtg_power.comment"), 1, Integer.MAX_VALUE);
		propertyRTGPower.setLanguageKey("gui.nc.config.rtg_power");
		Property propertySolarPower = config.get(CATEGORY_GENERATOR, "solar_power", new int[] {5, 20, 80, 320}, Lang.localise("gui.nc.config.solar_power.comment"), 1, Integer.MAX_VALUE);
		propertySolarPower.setLanguageKey("gui.nc.config.solar_power");
		Property propertyDecayLifetime = config.get(CATEGORY_GENERATOR, "decay_lifetime", new double[] {62.4D*1200D, 20.4D*1200D, 36.6D*1200D, 39.6D*1200D, 35.5D*1200D, 12.8D*1200D, 52.8D*1200D, 8.5D*1200D, 7.2D*1200D, 65.2D*1200D}, Lang.localise("gui.nc.config.decay_lifetime.comment"), 1D, 16777215D);
		propertyDecayLifetime.setLanguageKey("gui.nc.config.decay_lifetime");
		Property propertyDecayPower = config.get(CATEGORY_GENERATOR, "decay_power", new double[] {4D, 4D, 0.75D, 0.25D, 0.5D, 0.75D, 1D, 1.25D, 1.5D, 2D}, Lang.localise("gui.nc.config.decay_power.comment"), 0D, 32767D);
		propertyDecayPower.setLanguageKey("gui.nc.config.decay_power");
		
		Property propertyBatteryCapacity = config.get(CATEGORY_ENERGY_STORAGE, "battery_capacity", new int[] {1600000, 6400000, 25600000, 102400000, 32000000, 128000000, 512000000, 2048000000}, Lang.localise("gui.nc.config.battery_capacity.comment"), 1, Integer.MAX_VALUE);
		propertyBatteryCapacity.setLanguageKey("gui.nc.config.battery_capacity");
		
		Property propertyFissionFuelTimeMultiplier = config.get(CATEGORY_FISSION, "fission_fuel_time_multiplier", 1D, Lang.localise("gui.nc.config.fission_fuel_time_multiplier.comment"), 0.001D, 255D);
		propertyFissionFuelTimeMultiplier.setLanguageKey("gui.nc.config.fission_fuel_time_multiplier");
		Property propertyFissionSourceEfficiency = config.get(CATEGORY_FISSION, "fission_source_efficiency", new double[] {0.9D, 0.95D, 1D}, Lang.localise("gui.nc.config.fission_source_efficiency.comment"), 0D, 255D);
		propertyFissionSourceEfficiency.setLanguageKey("gui.nc.config.fission_source_efficiency");
		Property propertyFissionSinkCoolingRate = config.get(CATEGORY_FISSION, "fission_sink_cooling_rate", new int[] {55, 50, 85, 75, 70, 105, 100, 95, 110, 115, 145, 65, 90, 195, 190, 80, 120, 60, 165, 130, 125, 150, 185, 170, 175, 160, 140, 135, 180, 200, 155, 205}, Lang.localise("gui.nc.config.fission_sink_cooling_rate.comment"), 0, 32767);
		propertyFissionSinkCoolingRate.setLanguageKey("gui.nc.config.fission_sink_cooling_rate");
		Property propertyFissionHeaterCoolingRate = config.get(CATEGORY_FISSION, "fission_heater_cooling_rate", new int[] {55, 50, 85, 75, 70, 105, 100, 95, 110, 115, 145, 65, 90, 195, 190, 80, 120, 60, 165, 130, 125, 150, 185, 170, 175, 160, 140, 135, 180, 200, 155, 205}, Lang.localise("gui.nc.config.fission_heater_cooling_rate.comment"), 0, 32767);
		propertyFissionHeaterCoolingRate.setLanguageKey("gui.nc.config.fission_heater_cooling_rate");
		Property propertyFissionModeratorFluxFactor = config.get(CATEGORY_FISSION, "fission_moderator_flux_factor", new int[] {10, 22, 36}, Lang.localise("gui.nc.config.fission_moderator_flux_factor.comment"), 0, 32767);
		propertyFissionModeratorFluxFactor.setLanguageKey("gui.nc.config.fission_moderator_flux_factor");
		Property propertyFissionModeratorEfficiency = config.get(CATEGORY_FISSION, "fission_moderator_efficiency", new double[] {1.1D, 1.05D, 1D}, Lang.localise("gui.nc.config.fission_moderator_efficiency.comment"), 0D, 255D);
		propertyFissionModeratorEfficiency.setLanguageKey("gui.nc.config.fission_moderator_efficiency");
		Property propertyFissionReflectorEfficiency = config.get(CATEGORY_FISSION, "fission_reflector_efficiency", new double[] {0.5D, 0.25D}, Lang.localise("gui.nc.config.fission_reflector_efficiency.comment"), 0D, 255D);
		propertyFissionReflectorEfficiency.setLanguageKey("gui.nc.config.fission_reflector_efficiency");
		Property propertyFissionReflectorReflectivity = config.get(CATEGORY_FISSION, "fission_reflector_reflectivity", new double[] {1D, 0.5D}, Lang.localise("gui.nc.config.fission_reflector_reflectivity.comment"), 0D, 1D);
		propertyFissionReflectorReflectivity.setLanguageKey("gui.nc.config.fission_reflector_reflectivity");
		Property propertyFissionShieldHeatPerFlux = config.get(CATEGORY_FISSION, "fission_shield_heat_per_flux", new double[] {5D}, Lang.localise("gui.nc.config.fission_shield_heat_per_flux.comment"), 0D, 32767D);
		propertyFissionShieldHeatPerFlux.setLanguageKey("gui.nc.config.fission_shield_heat_per_flux");
		Property propertyFissionShieldEfficiency = config.get(CATEGORY_FISSION, "fission_shield_efficiency", new double[] {0.5D}, Lang.localise("gui.nc.config.fission_shield_efficiency.comment"), 0D, 255D);
		propertyFissionShieldEfficiency.setLanguageKey("gui.nc.config.fission_shield_efficiency");
		Property propertyFissionIrradiatorHeatPerFlux = config.get(CATEGORY_FISSION, "fission_irradiator_heat_per_flux", new double[] {0D, 0D, 0D}, Lang.localise("gui.nc.config.fission_irradiator_heat_per_flux.comment"), 0D, 32767D);
		propertyFissionIrradiatorHeatPerFlux.setLanguageKey("gui.nc.config.fission_irradiator_heat_per_flux");
		Property propertyFissionIrradiatorEfficiency = config.get(CATEGORY_FISSION, "fission_irradiator_efficiency", new double[] {0D, 0D, 0.5D}, Lang.localise("gui.nc.config.fission_irradiator_efficiency.comment"), 0D, 32767D);
		propertyFissionIrradiatorEfficiency.setLanguageKey("gui.nc.config.fission_irradiator_efficiency");
		
		Property propertyFissionCoolingEfficiencyLeniency = config.get(CATEGORY_FISSION, "fission_cooling_efficiency_leniency", 10, Lang.localise("gui.nc.config.fission_cooling_efficiency_leniency.comment"), 0, 32767);
		propertyFissionCoolingEfficiencyLeniency.setLanguageKey("gui.nc.config.fission_cooling_efficiency_leniency");
		Property propertyFissionSparsityPenaltyParams = config.get(CATEGORY_FISSION, "fission_sparsity_penalty_params", new double[] {0.5D, 0.75D}, Lang.localise("gui.nc.config.fission_sparsity_penalty_params.comment"), 0D, 1D);
		propertyFissionSparsityPenaltyParams.setLanguageKey("gui.nc.config.fission_sparsity_penalty_params");
		Property propertyFissionOverheat = config.get(CATEGORY_FISSION, "fission_overheat", true, Lang.localise("gui.nc.config.fission_overheat.comment"));
		propertyFissionOverheat.setLanguageKey("gui.nc.config.fission_overheat");
		Property propertyFissionExplosions = config.get(CATEGORY_FISSION, "fission_explosions", false, Lang.localise("gui.nc.config.fission_explosions.comment"));
		propertyFissionExplosions.setLanguageKey("gui.nc.config.fission_explosions");
		Property propertyFissionMeltdownRadiationMultiplier = config.get(CATEGORY_FISSION, "fission_meltdown_radiation_multiplier", 1D, Lang.localise("gui.nc.config.fission_meltdown_radiation_multiplier.comment"), 0D, 255D);
		propertyFissionMeltdownRadiationMultiplier.setLanguageKey("gui.nc.config.fission_meltdown_radiation_multiplier");
		Property propertyFissionMinSize = config.get(CATEGORY_FISSION, "fission_min_size", 1, Lang.localise("gui.nc.config.fission_min_size.comment"), 1, 255);
		propertyFissionMinSize.setLanguageKey("gui.nc.config.fission_min_size");
		Property propertyFissionMaxSize = config.get(CATEGORY_FISSION, "fission_max_size", 24, Lang.localise("gui.nc.config.fission_max_size.comment"), 1, 255);
		propertyFissionMaxSize.setLanguageKey("gui.nc.config.fission_max_size");
		Property propertyFissionComparatorMaxTemp = config.get(CATEGORY_FISSION, "fission_comparator_max_temp", 1600, Lang.localise("gui.nc.config.fission_comparator_max_temp.comment"), 20, 2400);
		propertyFissionComparatorMaxTemp.setLanguageKey("gui.nc.config.fission_comparator_max_temp");
		Property propertyFissionNeutronReach = config.get(CATEGORY_FISSION, "fission_neutron_reach", 4, Lang.localise("gui.nc.config.fission_neutron_reach.comment"), 1, 255);
		propertyFissionNeutronReach.setLanguageKey("gui.nc.config.fission_neutron_reach");
		Property propertyFissionSoundVolume = config.get(CATEGORY_FISSION, "fission_sound_volume", 1D, Lang.localise("gui.nc.config.fission_sound_volume.comment"), 0D, 15D);
		propertyFissionSoundVolume.setLanguageKey("gui.nc.config.fission_sound_volume");
		
		Property propertyFissionThoriumFuelTime = config.get(CATEGORY_FISSION, "fission_thorium_fuel_time", new int[] {14400, 14400, 18000, 11520, 18000}, Lang.localise("gui.nc.config.fission_thorium_fuel_time.comment"), 1, Integer.MAX_VALUE);
		propertyFissionThoriumFuelTime.setLanguageKey("gui.nc.config.fission_thorium_fuel_time");
		Property propertyFissionThoriumHeatGeneration = config.get(CATEGORY_FISSION, "fission_thorium_heat_generation", new int[] {40, 40, 32, 50, 32}, Lang.localise("gui.nc.config.fission_thorium_heat_generation.comment"), 0, 32767);
		propertyFissionThoriumHeatGeneration.setLanguageKey("gui.nc.config.fission_thorium_heat_generation");
		Property propertyFissionThoriumEfficiency = config.get(CATEGORY_FISSION, "fission_thorium_efficiency", new double[] {1.25D, 1.25D, 1.25D, 1.25D, 1.25D}, Lang.localise("gui.nc.config.fission_thorium_efficiency.comment"), 0D, 32767D);
		propertyFissionThoriumEfficiency.setLanguageKey("gui.nc.config.fission_thorium_efficiency");
		Property propertyFissionThoriumCriticality = config.get(CATEGORY_FISSION, "fission_thorium_criticality", new int[] {199, 234, 293, 199, 234}, Lang.localise("gui.nc.config.fission_thorium_criticality.comment"), 0, 32767);
		propertyFissionThoriumCriticality.setLanguageKey("gui.nc.config.fission_thorium_criticality");
		Property propertyFissionThoriumSelfPriming = config.get(CATEGORY_FISSION, "fission_thorium_self_priming", new boolean[] {false, false, false, false, false}, Lang.localise("gui.nc.config.fission_thorium_self_priming.comment"));
		propertyFissionThoriumSelfPriming.setLanguageKey("gui.nc.config.fission_thorium_self_priming");
		Property propertyFissionThoriumRadiation = config.get(CATEGORY_FISSION, "fission_thorium_radiation", new double[] {RadSources.TBU_FISSION, RadSources.TBU_FISSION, RadSources.TBU_FISSION, RadSources.TBU_FISSION, RadSources.TBU_FISSION}, Lang.localise("gui.nc.config.fission_thorium_radiation.comment"), 0D, 1000D);
		propertyFissionThoriumRadiation.setLanguageKey("gui.nc.config.fission_thorium_radiation");
		
		Property propertyFissionUraniumFuelTime = config.get(CATEGORY_FISSION, "fission_uranium_fuel_time", new int[] {2666, 2666, 3348, 2134, 3348, 2666, 2666, 3348, 2134, 3348, 4800, 4800, 6000, 3840, 6000, 4800, 4800, 6000, 3840, 6000}, Lang.localise("gui.nc.config.fission_uranium_fuel_time.comment"), 1, Integer.MAX_VALUE);
		propertyFissionUraniumFuelTime.setLanguageKey("gui.nc.config.fission_uranium_fuel_time");
		Property propertyFissionUraniumHeatGeneration = config.get(CATEGORY_FISSION, "fission_uranium_heat_generation", new int[] {216, 216, 172, 270, 172, 216*3, 216*3, 172*3, 270*3, 172*3, 120, 120, 96, 150, 96, 120*3, 120*3, 96*3, 150*3, 96*3}, Lang.localise("gui.nc.config.fission_uranium_heat_generation.comment"), 0, 32767);
		propertyFissionUraniumHeatGeneration.setLanguageKey("gui.nc.config.fission_uranium_heat_generation");
		Property propertyFissionUraniumEfficiency = config.get(CATEGORY_FISSION, "fission_uranium_efficiency", new double[] {1.1D, 1.1D, 1.1D, 1.1D, 1.1D, 1.15D, 1.15D, 1.15D, 1.15D, 1.15D, 1D, 1D, 1D, 1D, 1D, 1.05D, 1.05D, 1.05D, 1.05D, 1.05D}, Lang.localise("gui.nc.config.fission_uranium_efficiency.comment"), 0D, 32767D);
		propertyFissionUraniumEfficiency.setLanguageKey("gui.nc.config.fission_uranium_efficiency");
		Property propertyFissionUraniumCriticality = config.get(CATEGORY_FISSION, "fission_uranium_criticality", new int[] {66, 78, 98, 66, 78, 66/2, 78/2, 98/2, 66/2, 78/2, 87, 102, 128, 87, 102, 87/2, 102/2, 128/2, 87/2, 102/2}, Lang.localise("gui.nc.config.fission_uranium_criticality.comment"), 0, 32767);
		propertyFissionUraniumCriticality.setLanguageKey("gui.nc.config.fission_uranium_criticality");
		Property propertyFissionUraniumSelfPriming = config.get(CATEGORY_FISSION, "fission_uranium_self_priming", new boolean[] {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false}, Lang.localise("gui.nc.config.fission_uranium_self_priming.comment"));
		propertyFissionUraniumSelfPriming.setLanguageKey("gui.nc.config.fission_uranium_self_priming");
		Property propertyFissionUraniumRadiation = config.get(CATEGORY_FISSION, "fission_uranium_radiation", new double[] {RadSources.LEU_233_FISSION, RadSources.LEU_233_FISSION, RadSources.LEU_233_FISSION, RadSources.LEU_233_FISSION, RadSources.LEU_233_FISSION, RadSources.HEU_233_FISSION, RadSources.HEU_233_FISSION, RadSources.HEU_233_FISSION, RadSources.HEU_233_FISSION, RadSources.HEU_233_FISSION, RadSources.LEU_235_FISSION, RadSources.LEU_235_FISSION, RadSources.LEU_235_FISSION, RadSources.LEU_235_FISSION, RadSources.LEU_235_FISSION, RadSources.HEU_235_FISSION, RadSources.HEU_235_FISSION, RadSources.HEU_235_FISSION, RadSources.HEU_235_FISSION, RadSources.HEU_235_FISSION}, Lang.localise("gui.nc.config.fission_uranium_radiation.comment"), 0D, 1000D);
		propertyFissionUraniumRadiation.setLanguageKey("gui.nc.config.fission_uranium_radiation");
		
		Property propertyFissionNeptuniumFuelTime = config.get(CATEGORY_FISSION, "fission_neptunium_fuel_time", new int[] {1972, 1972, 2462, 1574, 2462, 1972, 1972, 2462, 1574, 2462}, Lang.localise("gui.nc.config.fission_neptunium_fuel_time.comment"), 1, Integer.MAX_VALUE);
		propertyFissionNeptuniumFuelTime.setLanguageKey("gui.nc.config.fission_neptunium_fuel_time");
		Property propertyFissionNeptuniumHeatGeneration = config.get(CATEGORY_FISSION, "fission_neptunium_heat_generation", new int[] {292, 292, 234, 366, 234, 292*3, 292*3, 234*3, 366*3, 234*3}, Lang.localise("gui.nc.config.fission_neptunium_heat_generation.comment"), 0, 32767);
		propertyFissionNeptuniumHeatGeneration.setLanguageKey("gui.nc.config.fission_neptunium_heat_generation");
		Property propertyFissionNeptuniumEfficiency = config.get(CATEGORY_FISSION, "fission_neptunium_efficiency", new double[] {1.1D, 1.1D, 1.1D, 1.1D, 1.1D, 1.15D, 1.15D, 1.15D, 1.15D, 1.15D}, Lang.localise("gui.nc.config.fission_neptunium_efficiency.comment"), 0D, 32767D);
		propertyFissionNeptuniumEfficiency.setLanguageKey("gui.nc.config.fission_neptunium_efficiency");
		Property propertyFissionNeptuniumCriticality = config.get(CATEGORY_FISSION, "fission_neptunium_criticality", new int[] {60, 70, 88, 60, 70, 60/2, 70/2, 88/2, 60/2, 70/2}, Lang.localise("gui.nc.config.fission_neptunium_criticality.comment"), 0, 32767);
		propertyFissionNeptuniumCriticality.setLanguageKey("gui.nc.config.fission_neptunium_criticality");
		Property propertyFissionNeptuniumSelfPriming = config.get(CATEGORY_FISSION, "fission_neptunium_self_priming", new boolean[] {false, false, false, false, false, false, false, false, false, false}, Lang.localise("gui.nc.config.fission_neptunium_self_priming.comment"));
		propertyFissionNeptuniumSelfPriming.setLanguageKey("gui.nc.config.fission_neptunium_self_priming");
		Property propertyFissionNeptuniumRadiation = config.get(CATEGORY_FISSION, "fission_neptunium_radiation", new double[] {RadSources.LEN_236_FISSION, RadSources.LEN_236_FISSION, RadSources.LEN_236_FISSION, RadSources.LEN_236_FISSION, RadSources.LEN_236_FISSION, RadSources.HEN_236_FISSION, RadSources.HEN_236_FISSION, RadSources.HEN_236_FISSION, RadSources.HEN_236_FISSION, RadSources.HEN_236_FISSION}, Lang.localise("gui.nc.config.fission_neptunium_radiation.comment"), 0D, 1000D);
		propertyFissionNeptuniumRadiation.setLanguageKey("gui.nc.config.fission_neptunium_radiation");
		
		Property propertyFissionPlutoniumFuelTime = config.get(CATEGORY_FISSION, "fission_plutonium_fuel_time", new int[] {4572, 4572, 5760, 3646, 5760, 4572, 4572, 5760, 3646, 5760, 3164, 3164, 3946, 2526, 3946, 3164, 3164, 3946, 2526, 3946}, Lang.localise("gui.nc.config.fission_plutonium_fuel_time.comment"), 1, Integer.MAX_VALUE);
		propertyFissionPlutoniumFuelTime.setLanguageKey("gui.nc.config.fission_plutonium_fuel_time");
		Property propertyFissionPlutoniumHeatGeneration = config.get(CATEGORY_FISSION, "fission_plutonium_heat_generation", new int[] {126, 126, 100, 158, 100, 126*3, 126*3, 100*3, 158*3, 100*3, 182, 182, 146, 228, 146, 182*3, 182*3, 146*3, 228*3, 146*3}, Lang.localise("gui.nc.config.fission_plutonium_heat_generation.comment"), 0, 32767);
		propertyFissionPlutoniumHeatGeneration.setLanguageKey("gui.nc.config.fission_plutonium_heat_generation");
		Property propertyFissionPlutoniumEfficiency = config.get(CATEGORY_FISSION, "fission_plutonium_efficiency", new double[] {1.2D, 1.2D, 1.2D, 1.2D, 1.2D, 1.25D, 1.25D, 1.25D, 1.25D, 1.25D, 1.25D, 1.25D, 1.25D, 1.25D, 1.25D, 1.3D, 1.3D, 1.3D, 1.3D, 1.3D}, Lang.localise("gui.nc.config.fission_plutonium_efficiency.comment"), 0D, 32767D);
		propertyFissionPlutoniumEfficiency.setLanguageKey("gui.nc.config.fission_plutonium_efficiency");
		Property propertyFissionPlutoniumCriticality = config.get(CATEGORY_FISSION, "fission_plutonium_criticality", new int[] {84, 99, 124, 84, 99, 84/2, 99/2, 124/2, 84/2, 99/2, 71, 84, 105, 71, 84, 71/2, 84/2, 105/2, 71/2, 84/2}, Lang.localise("gui.nc.config.fission_plutonium_criticality.comment"), 0, 32767);
		propertyFissionPlutoniumCriticality.setLanguageKey("gui.nc.config.fission_plutonium_criticality");
		Property propertyFissionPlutoniumSelfPriming = config.get(CATEGORY_FISSION, "fission_plutonium_self_priming", new boolean[] {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false}, Lang.localise("gui.nc.config.fission_plutonium_self_priming.comment"));
		propertyFissionPlutoniumSelfPriming.setLanguageKey("gui.nc.config.fission_plutonium_self_priming");
		Property propertyFissionPlutoniumRadiation = config.get(CATEGORY_FISSION, "fission_plutonium_radiation", new double[] {RadSources.LEP_239_FISSION, RadSources.LEP_239_FISSION, RadSources.LEP_239_FISSION, RadSources.LEP_239_FISSION, RadSources.LEP_239_FISSION, RadSources.HEP_239_FISSION, RadSources.HEP_239_FISSION, RadSources.HEP_239_FISSION, RadSources.HEP_239_FISSION, RadSources.HEP_239_FISSION, RadSources.LEP_241_FISSION, RadSources.LEP_241_FISSION, RadSources.LEP_241_FISSION, RadSources.LEP_241_FISSION, RadSources.LEP_241_FISSION, RadSources.HEP_241_FISSION, RadSources.HEP_241_FISSION, RadSources.HEP_241_FISSION, RadSources.HEP_241_FISSION, RadSources.HEP_241_FISSION}, Lang.localise("gui.nc.config.fission_plutonium_radiation.comment"), 0D, 1000D);
		propertyFissionPlutoniumRadiation.setLanguageKey("gui.nc.config.fission_plutonium_radiation");
		
		Property propertyFissionMixedFuelTime = config.get(CATEGORY_FISSION, "fission_mixed_fuel_time", new int[] {4354, 4354, 5486, 3472, 5486, 3014, 3014, 3758, 2406, 3758}, Lang.localise("gui.nc.config.fission_mixed_fuel_time.comment"), 1, Integer.MAX_VALUE);
		propertyFissionMixedFuelTime.setLanguageKey("gui.nc.config.fission_mixed_fuel_time");
		Property propertyFissionMixedHeatGeneration = config.get(CATEGORY_FISSION, "fission_mixed_heat_generation", new int[] {132, 132, 106, 166, 106, 192, 192, 154, 240, 154}, Lang.localise("gui.nc.config.fission_mixed_heat_generation.comment"), 0, 32767);
		propertyFissionMixedHeatGeneration.setLanguageKey("gui.nc.config.fission_mixed_heat_generation");
		Property propertyFissionMixedEfficiency = config.get(CATEGORY_FISSION, "fission_mixed_efficiency", new double[] {1.05D, 1.05D, 1.05D, 1.05D, 1.05D, 1.15D, 1.15D, 1.15D, 1.15D, 1.15D}, Lang.localise("gui.nc.config.fission_mixed_efficiency.comment"), 0D, 32767D);
		propertyFissionMixedEfficiency.setLanguageKey("gui.nc.config.fission_mixed_efficiency");
		Property propertyFissionMixedCriticality = config.get(CATEGORY_FISSION, "fission_mixed_criticality", new int[] {80, 94, 118, 80, 94, 68, 80, 100, 68, 80}, Lang.localise("gui.nc.config.fission_mixed_criticality.comment"), 0, 32767);
		propertyFissionMixedCriticality.setLanguageKey("gui.nc.config.fission_mixed_criticality");
		Property propertyFissionMixedSelfPriming = config.get(CATEGORY_FISSION, "fission_mixed_self_priming", new boolean[] {false, false, false, false, false, false, false, false, false, false}, Lang.localise("gui.nc.config.fission_mixed_self_priming.comment"));
		propertyFissionMixedSelfPriming.setLanguageKey("gui.nc.config.fission_mixed_self_priming");
		Property propertyFissionMixedRadiation = config.get(CATEGORY_FISSION, "fission_mixed_radiation", new double[] {RadSources.MIX_239_FISSION, RadSources.MIX_239_FISSION, RadSources.MIX_239_FISSION, RadSources.MIX_239_FISSION, RadSources.MIX_239_FISSION, RadSources.MIX_241_FISSION, RadSources.MIX_241_FISSION, RadSources.MIX_241_FISSION, RadSources.MIX_241_FISSION, RadSources.MIX_241_FISSION}, Lang.localise("gui.nc.config.fission_mixed_radiation.comment"), 0D, 1000D);
		propertyFissionMixedRadiation.setLanguageKey("gui.nc.config.fission_mixed_radiation");
		
		Property propertyFissionAmericiumFuelTime = config.get(CATEGORY_FISSION, "fission_americium_fuel_time", new int[] {1476, 1476, 1846, 1180, 1846, 1476, 1476, 1846, 1180, 1846}, Lang.localise("gui.nc.config.fission_americium_fuel_time.comment"), 1, Integer.MAX_VALUE);
		propertyFissionAmericiumFuelTime.setLanguageKey("gui.nc.config.fission_americium_fuel_time");
		Property propertyFissionAmericiumHeatGeneration = config.get(CATEGORY_FISSION, "fission_americium_heat_generation", new int[] {390, 390, 312, 488, 312, 390*3, 390*3, 312*3, 488*3, 312*3}, Lang.localise("gui.nc.config.fission_americium_heat_generation.comment"), 0, 32767);
		propertyFissionAmericiumHeatGeneration.setLanguageKey("gui.nc.config.fission_americium_heat_generation");
		Property propertyFissionAmericiumEfficiency = config.get(CATEGORY_FISSION, "fission_americium_efficiency", new double[] {1.35D, 1.35D, 1.35D, 1.35D, 1.35D, 1.4D, 1.4D, 1.4D, 1.4D, 1.4D}, Lang.localise("gui.nc.config.fission_americium_efficiency.comment"), 0D, 32767D);
		propertyFissionAmericiumEfficiency.setLanguageKey("gui.nc.config.fission_americium_efficiency");
		Property propertyFissionAmericiumCriticality = config.get(CATEGORY_FISSION, "fission_americium_criticality", new int[] {55, 65, 81, 55, 65, 55/2, 65/2, 81/2, 55/2, 65/2}, Lang.localise("gui.nc.config.fission_americium_criticality.comment"), 0, 32767);
		propertyFissionAmericiumCriticality.setLanguageKey("gui.nc.config.fission_americium_criticality");
		Property propertyFissionAmericiumSelfPriming = config.get(CATEGORY_FISSION, "fission_americium_self_priming", new boolean[] {false, false, false, false, false, false, false, false, false, false}, Lang.localise("gui.nc.config.fission_americium_self_priming.comment"));
		propertyFissionAmericiumSelfPriming.setLanguageKey("gui.nc.config.fission_americium_self_priming");
		Property propertyFissionAmericiumRadiation = config.get(CATEGORY_FISSION, "fission_americium_radiation", new double[] {RadSources.LEA_242_FISSION, RadSources.LEA_242_FISSION, RadSources.LEA_242_FISSION, RadSources.LEA_242_FISSION, RadSources.LEA_242_FISSION, RadSources.HEA_242_FISSION, RadSources.HEA_242_FISSION, RadSources.HEA_242_FISSION, RadSources.HEA_242_FISSION, RadSources.HEA_242_FISSION}, Lang.localise("gui.nc.config.fission_americium_radiation.comment"), 0D, 1000D);
		propertyFissionAmericiumRadiation.setLanguageKey("gui.nc.config.fission_americium_radiation");
		
		Property propertyFissionCuriumFuelTime = config.get(CATEGORY_FISSION, "fission_curium_fuel_time", new int[] {1500, 1500, 1870, 1200, 1870, 1500, 1500, 1870, 1200, 1870, 2420, 2420, 3032, 1932, 3032, 2420, 2420, 3032, 1932, 3032, 2150, 2150, 2692, 1714, 2692, 2150, 2150, 2692, 1714, 2692}, Lang.localise("gui.nc.config.fission_curium_fuel_time.comment"), 1, Integer.MAX_VALUE);
		propertyFissionCuriumFuelTime.setLanguageKey("gui.nc.config.fission_curium_fuel_time");
		Property propertyFissionCuriumHeatGeneration = config.get(CATEGORY_FISSION, "fission_curium_heat_generation", new int[] {384, 384, 308, 480, 308, 384*3, 384*3, 308*3, 480*3, 308*3, 238, 238, 190, 298, 190, 238*3, 238*3, 190*3, 298*3, 190*3, 268, 268, 214, 336, 214, 268*3, 268*3, 214*3, 336*3, 214*3}, Lang.localise("gui.nc.config.fission_curium_heat_generation.comment"), 0, 32767);
		propertyFissionCuriumHeatGeneration.setLanguageKey("gui.nc.config.fission_curium_heat_generation");
		Property propertyFissionCuriumEfficiency = config.get(CATEGORY_FISSION, "fission_curium_efficiency", new double[] {1.45D, 1.45D, 1.45D, 1.45D, 1.45D, 1.5D, 1.5D, 1.5D, 1.5D, 1.5D, 1.5D, 1.5D, 1.5D, 1.5D, 1.5D, 1.55D, 1.55D, 1.55D, 1.55D, 1.55D, 1.55D, 1.55D, 1.55D, 1.55D, 1.55D, 1.6D, 1.6D, 1.6D, 1.6D, 1.6D}, Lang.localise("gui.nc.config.fission_curium_efficiency.comment"), 0D, 32767D);
		propertyFissionCuriumEfficiency.setLanguageKey("gui.nc.config.fission_curium_efficiency");
		Property propertyFissionCuriumCriticality = config.get(CATEGORY_FISSION, "fission_curium_criticality", new int[] {56, 66, 83, 56, 66, 56/2, 66/2, 83/2, 56/2, 66/2, 64, 75, 94, 64, 75, 64/2, 75/2, 94/2, 64/2, 75/2, 61, 72, 90, 61, 72, 61/2, 72/2, 90/2, 61/2, 72/2}, Lang.localise("gui.nc.config.fission_curium_criticality.comment"), 0, 32767);
		propertyFissionCuriumCriticality.setLanguageKey("gui.nc.config.fission_curium_criticality");
		Property propertyFissionCuriumSelfPriming = config.get(CATEGORY_FISSION, "fission_curium_self_priming", new boolean[] {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false}, Lang.localise("gui.nc.config.fission_curium_self_priming.comment"));
		propertyFissionCuriumSelfPriming.setLanguageKey("gui.nc.config.fission_curium_self_priming");
		Property propertyFissionCuriumRadiation = config.get(CATEGORY_FISSION, "fission_curium_radiation", new double[] {RadSources.LECm_243_FISSION, RadSources.LECm_243_FISSION, RadSources.LECm_243_FISSION, RadSources.LECm_243_FISSION, RadSources.LECm_243_FISSION, RadSources.HECm_243_FISSION, RadSources.HECm_243_FISSION, RadSources.HECm_243_FISSION, RadSources.HECm_243_FISSION, RadSources.HECm_243_FISSION, RadSources.LECm_245_FISSION, RadSources.LECm_245_FISSION, RadSources.LECm_245_FISSION, RadSources.LECm_245_FISSION, RadSources.LECm_245_FISSION, RadSources.HECm_245_FISSION, RadSources.HECm_245_FISSION, RadSources.HECm_245_FISSION, RadSources.HECm_245_FISSION, RadSources.HECm_245_FISSION, RadSources.LECm_247_FISSION, RadSources.LECm_247_FISSION, RadSources.LECm_247_FISSION, RadSources.LECm_247_FISSION, RadSources.LECm_247_FISSION, RadSources.HECm_247_FISSION, RadSources.HECm_247_FISSION, RadSources.HECm_247_FISSION, RadSources.HECm_247_FISSION, RadSources.HECm_247_FISSION}, Lang.localise("gui.nc.config.fission_curium_radiation.comment"), 0D, 1000D);
		propertyFissionCuriumRadiation.setLanguageKey("gui.nc.config.fission_curium_radiation");
		
		Property propertyFissionBerkeliumFuelTime = config.get(CATEGORY_FISSION, "fission_berkelium_fuel_time", new int[] {2166, 2166, 2716, 1734, 2716, 2166, 2166, 2716, 1734, 2716}, Lang.localise("gui.nc.config.fission_berkelium_fuel_time.comment"), 1, Integer.MAX_VALUE);
		propertyFissionBerkeliumFuelTime.setLanguageKey("gui.nc.config.fission_berkelium_fuel_time");
		Property propertyFissionBerkeliumHeatGeneration = config.get(CATEGORY_FISSION, "fission_berkelium_heat_generation", new int[] {266, 266, 212, 332, 212, 266*3, 266*3, 212*3, 332*3, 212*3}, Lang.localise("gui.nc.config.fission_berkelium_heat_generation.comment"), 0, 32767);
		propertyFissionBerkeliumHeatGeneration.setLanguageKey("gui.nc.config.fission_berkelium_heat_generation");
		Property propertyFissionBerkeliumEfficiency = config.get(CATEGORY_FISSION, "fission_berkelium_efficiency", new double[] {1.65D, 1.65D, 1.65D, 1.65D, 1.65D, 1.7D, 1.7D, 1.7D, 1.7D, 1.7D}, Lang.localise("gui.nc.config.fission_berkelium_efficiency.comment"), 0D, 32767D);
		propertyFissionBerkeliumEfficiency.setLanguageKey("gui.nc.config.fission_berkelium_efficiency");
		Property propertyFissionBerkeliumCriticality = config.get(CATEGORY_FISSION, "fission_berkelium_criticality", new int[] {62, 73, 91, 62, 73, 62/2, 73/2, 91/2, 62/2, 73/2}, Lang.localise("gui.nc.config.fission_berkelium_criticality.comment"), 0, 32767);
		propertyFissionBerkeliumCriticality.setLanguageKey("gui.nc.config.fission_berkelium_criticality");
		Property propertyFissionBerkeliumSelfPriming = config.get(CATEGORY_FISSION, "fission_berkelium_self_priming", new boolean[] {false, false, false, false, false, false, false, false, false, false}, Lang.localise("gui.nc.config.fission_berkelium_self_priming.comment"));
		propertyFissionBerkeliumSelfPriming.setLanguageKey("gui.nc.config.fission_berkelium_self_priming");
		Property propertyFissionBerkeliumRadiation = config.get(CATEGORY_FISSION, "fission_berkelium_radiation", new double[] {RadSources.LEB_248_FISSION, RadSources.LEB_248_FISSION, RadSources.LEB_248_FISSION, RadSources.LEB_248_FISSION, RadSources.LEB_248_FISSION, RadSources.HEB_248_FISSION, RadSources.HEB_248_FISSION, RadSources.HEB_248_FISSION, RadSources.HEB_248_FISSION, RadSources.HEB_248_FISSION}, Lang.localise("gui.nc.config.fission_berkelium_radiation.comment"), 0D, 1000D);
		propertyFissionBerkeliumRadiation.setLanguageKey("gui.nc.config.fission_berkelium_radiation");
		
		Property propertyFissionCaliforniumFuelTime = config.get(CATEGORY_FISSION, "fission_californium_fuel_time", new int[] {1066, 1066, 1334, 852, 1334, 1066, 1066, 1334, 852, 1334, 2000, 2000, 2504, 1600, 2504, 2000, 2000, 2504, 1600, 2504}, Lang.localise("gui.nc.config.fission_californium_fuel_time.comment"), 1, Integer.MAX_VALUE);
		propertyFissionCaliforniumFuelTime.setLanguageKey("gui.nc.config.fission_californium_fuel_time");
		Property propertyFissionCaliforniumHeatGeneration = config.get(CATEGORY_FISSION, "fission_californium_heat_generation", new int[] {540, 540, 432, 676, 432, 540*3, 540*3, 432*3, 676*3, 432*3, 288, 288, 230, 360, 230, 288*3, 288*3, 230*3, 360*3, 230*3}, Lang.localise("gui.nc.config.fission_californium_heat_generation.comment"), 0, 32767);
		propertyFissionCaliforniumHeatGeneration.setLanguageKey("gui.nc.config.fission_californium_heat_generation");
		Property propertyFissionCaliforniumEfficiency = config.get(CATEGORY_FISSION, "fission_californium_efficiency", new double[] {1.75D, 1.75D, 1.75D, 1.75D, 1.75D, 1.8D, 1.8D, 1.8D, 1.8D, 1.8D, 1.8D, 1.8D, 1.8D, 1.8D, 1.8D, 1.85D, 1.85D, 1.85D, 1.85D, 1.85D}, Lang.localise("gui.nc.config.fission_californium_efficiency.comment"), 0D, 32767D);
		propertyFissionCaliforniumEfficiency.setLanguageKey("gui.nc.config.fission_californium_efficiency");
		Property propertyFissionCaliforniumCriticality = config.get(CATEGORY_FISSION, "fission_californium_criticality", new int[] {51, 60, 75, 51, 60, 51/2, 60/2, 75/2, 51/2, 60/2, 60, 71, 89, 60, 71, 60/2, 71/2, 89/2, 60/2, 71/2}, Lang.localise("gui.nc.config.fission_californium_criticality.comment"), 0, 32767);
		propertyFissionCaliforniumCriticality.setLanguageKey("gui.nc.config.fission_californium_criticality");
		Property propertyFissionCaliforniumSelfPriming = config.get(CATEGORY_FISSION, "fission_californium_self_priming", new boolean[] {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true}, Lang.localise("gui.nc.config.fission_californium_self_priming.comment"));
		propertyFissionCaliforniumSelfPriming.setLanguageKey("gui.nc.config.fission_californium_self_priming");
		Property propertyFissionCaliforniumRadiation = config.get(CATEGORY_FISSION, "fission_californium_radiation", new double[] {RadSources.LECf_249_FISSION, RadSources.LECf_249_FISSION, RadSources.LECf_249_FISSION, RadSources.LECf_249_FISSION, RadSources.LECf_249_FISSION, RadSources.HECf_249_FISSION, RadSources.HECf_249_FISSION, RadSources.HECf_249_FISSION, RadSources.HECf_249_FISSION, RadSources.HECf_249_FISSION, RadSources.LECf_251_FISSION, RadSources.LECf_251_FISSION, RadSources.LECf_251_FISSION, RadSources.LECf_251_FISSION, RadSources.LECf_251_FISSION, RadSources.HECf_251_FISSION, RadSources.HECf_251_FISSION, RadSources.HECf_251_FISSION, RadSources.HECf_251_FISSION, RadSources.HECf_251_FISSION}, Lang.localise("gui.nc.config.fission_californium_radiation.comment"), 0D, 1000D);
		propertyFissionCaliforniumRadiation.setLanguageKey("gui.nc.config.fission_californium_radiation");
		
		Property propertyFusionBasePower = config.get(CATEGORY_FUSION, "fusion_base_power", 1D, Lang.localise("gui.nc.config.fusion_base_power.comment"), 0D, 255D);
		propertyFusionBasePower.setLanguageKey("gui.nc.config.fusion_base_power");
		Property propertyFusionFuelUse = config.get(CATEGORY_FUSION, "fusion_fuel_use", 1D, Lang.localise("gui.nc.config.fusion_fuel_use.comment"), 0.001D, 255D);
		propertyFusionFuelUse.setLanguageKey("gui.nc.config.fusion_fuel_use");
		Property propertyFusionHeatGeneration = config.get(CATEGORY_FUSION, "fusion_heat_generation", 1D, Lang.localise("gui.nc.config.fusion_heat_generation.comment"), 0D, 255D);
		propertyFusionHeatGeneration.setLanguageKey("gui.nc.config.fusion_heat_generation");
		Property propertyFusionHeatingMultiplier = config.get(CATEGORY_FUSION, "fusion_heating_multiplier", 1D, Lang.localise("gui.nc.config.fusion_heating_multiplier.comment"), 0D, 255D);
		propertyFusionHeatingMultiplier.setLanguageKey("gui.nc.config.fusion_heating_multiplier");
		Property propertyFusionOverheat = config.get(CATEGORY_FUSION, "fusion_overheat", true, Lang.localise("gui.nc.config.fusion_overheat.comment"));
		propertyFusionOverheat.setLanguageKey("gui.nc.config.fusion_overheat");
		Property propertyFusionMeltdownRadiationMultiplier = config.get(CATEGORY_FUSION, "fusion_meltdown_radiation_multiplier", 1D, Lang.localise("gui.nc.config.fusion_meltdown_radiation_multiplier.comment"), 0D, 255D);
		propertyFusionMeltdownRadiationMultiplier.setLanguageKey("gui.nc.config.fusion_meltdown_radiation_multiplier");
		Property propertyFusionMinSize = config.get(CATEGORY_FUSION, "fusion_min_size", 1, Lang.localise("gui.nc.config.fusion_min_size.comment"), 1, 255);
		propertyFusionMinSize.setLanguageKey("gui.nc.config.fusion_min_size");
		Property propertyFusionMaxSize = config.get(CATEGORY_FUSION, "fusion_max_size", 24, Lang.localise("gui.nc.config.fusion_max_size.comment"), 1, 255);
		propertyFusionMaxSize.setLanguageKey("gui.nc.config.fusion_max_size");
		Property propertyFusionComparatorMaxEfficiency = config.get(CATEGORY_FUSION, "fusion_comparator_max_efficiency", 90, Lang.localise("gui.nc.config.fusion_comparator_max_efficiency.comment"), 1, 100);
		propertyFusionComparatorMaxEfficiency.setLanguageKey("gui.nc.config.fusion_comparator_max_efficiency");
		Property propertyFusionElectromagnetPower = config.get(CATEGORY_FUSION, "fusion_electromagnet_power", 250D, Lang.localise("gui.nc.config.fusion_electromagnet_power.comment"), 0D, Integer.MAX_VALUE);
		propertyFusionElectromagnetPower.setLanguageKey("gui.nc.config.fusion_electromagnet_power");
		Property propertyFusionPlasmaCraziness = config.get(CATEGORY_FUSION, "fusion_plasma_craziness", true, Lang.localise("gui.nc.config.fusion_plasma_craziness.comment"));
		propertyFusionPlasmaCraziness.setLanguageKey("gui.nc.config.fusion_plasma_craziness");
		Property propertyFusionSoundVolume = config.get(CATEGORY_FUSION, "fusion_sound_volume", 1D, Lang.localise("gui.nc.config.fusion_sound_volume.comment"), 0D, 15D);
		propertyFusionSoundVolume.setLanguageKey("gui.nc.config.fusion_sound_volume");
		
		Property propertyFusionFuelTime = config.get(CATEGORY_FUSION, "fusion_fuel_time", new double[] {100D, 150D, 200D, 200D, 350D, 400D, 600D, 200D, 250D, 250D, 400D, 450D, 650D, 300D, 300D, 450D, 500D, 700D, 300D, 450D, 500D, 700D, 600D, 650D, 850D, 700D, 900D, 1100D}, Lang.localise("gui.nc.config.fusion_fuel_time.comment"), 1D, 32767D);
		propertyFusionFuelTime.setLanguageKey("gui.nc.config.fusion_fuel_time");
		Property propertyFusionPower = config.get(CATEGORY_FUSION, "fusion_power", new double[] {442D, 1123D, 0.3D, 3036D, 351D, 1330D, 444D, 507D, 1726D, 2252D, 1716D, 859D, 261D, 901D, 1099D, 915D, 435D, 7D, 1315D, 1151D, 727D, 140D, 1068D, 552D, 157D, 229D, 0.45D, 0.05D}, Lang.localise("gui.nc.config.fusion_power.comment"), 0D, 32767D);
		propertyFusionPower.setLanguageKey("gui.nc.config.fusion_power");
		Property propertyFusionHeatVariable = config.get(CATEGORY_FUSION, "fusion_heat_variable", new double[] {3635D, 1022D, 4964D, 2740D, 5972D, 4161D, 13432D, 949D, 670D, 2160D, 3954D, 4131D, 13853D, 736D, 2137D, 4079D, 4522D, 27254D, 5420D, 7800D, 7937D, 24266D, 11268D, 11927D, 30399D, 13630D, 166414D, 293984D}, Lang.localise("gui.nc.config.fusion_heat_variable.comment"), 500D, 20000D);
		propertyFusionHeatVariable.setLanguageKey("gui.nc.config.fusion_heat_variable");
		Property propertyFusionRadiation = config.get(CATEGORY_FUSION, "fusion_radiation", new double[] {RadSources.FUSION/64D, RadSources.FUSION/64D, (RadSources.FUSION + RadSources.TRITIUM + RadSources.NEUTRON)/64D, RadSources.FUSION/64D, (RadSources.FUSION + RadSources.TRITIUM)/64D, RadSources.FUSION/64D, RadSources.FUSION/64D, (RadSources.FUSION + RadSources.TRITIUM/2D + RadSources.NEUTRON/2D)/64D, (RadSources.FUSION + RadSources.TRITIUM + RadSources.NEUTRON)/64D, RadSources.FUSION/64D, RadSources.FUSION/64D, (RadSources.FUSION + RadSources.NEUTRON)/64D, (RadSources.FUSION + RadSources.NEUTRON)/64D, (RadSources.FUSION + 2*RadSources.TRITIUM + 2*RadSources.NEUTRON)/64D, (RadSources.FUSION + RadSources.NEUTRON)/64D, (RadSources.FUSION + RadSources.NEUTRON)/64D, (RadSources.FUSION + 2*RadSources.NEUTRON)/64D, (RadSources.FUSION + 2*RadSources.NEUTRON)/64D, RadSources.FUSION/64D, RadSources.FUSION/64D, RadSources.FUSION/64D, RadSources.FUSION/64D, RadSources.FUSION/64D, (RadSources.FUSION + RadSources.NEUTRON)/64D, (RadSources.FUSION + RadSources.NEUTRON)/64D, (RadSources.FUSION + 2*RadSources.NEUTRON)/64D, (RadSources.FUSION + 2*RadSources.NEUTRON)/64D, (RadSources.FUSION + 2*RadSources.NEUTRON)/64D}, Lang.localise("gui.nc.config.fusion_radiation.comment"), 0D, 1000D);
		propertyFusionRadiation.setLanguageKey("gui.nc.config.fusion_radiation");
		
		Property propertyHeatExchangerMinSize = config.get(CATEGORY_HEAT_EXCHANGER, "heat_exchanger_min_size", 1, Lang.localise("gui.nc.config.heat_exchanger.heat_exchanger_min_size.comment"), 1, 255);
		propertyHeatExchangerMinSize.setLanguageKey("gui.nc.config.heat_exchanger.heat_exchanger_min_size");
		Property propertyHeatExchangerMaxSize = config.get(CATEGORY_HEAT_EXCHANGER, "heat_exchanger_max_size", 24, Lang.localise("gui.nc.config.heat_exchanger.heat_exchanger_max_size.comment"), 1, 255);
		propertyHeatExchangerMaxSize.setLanguageKey("gui.nc.config.heat_exchanger.heat_exchanger_max_size");
		Property propertyHeatExchangerConductivity = config.get(CATEGORY_HEAT_EXCHANGER, "heat_exchanger_conductivity", new double[] {1D, 1.1D, 1.2D}, Lang.localise("gui.nc.config.heat_exchanger.heat_exchanger_conductivity.comment"), 0.01D, 15D);
		propertyHeatExchangerConductivity.setLanguageKey("gui.nc.config.heat_exchanger.heat_exchanger_conductivity");
		Property propertyHeatExchangerCoolantMult = config.get(CATEGORY_HEAT_EXCHANGER, "heat_exchanger_coolant_mult", 100D, Lang.localise("gui.nc.config.heat_exchanger.heat_exchanger_coolant_mult.comment"), 1D, 10000D);
		propertyHeatExchangerCoolantMult.setLanguageKey("gui.nc.config.heat_exchanger.heat_exchanger_coolant_mult");
		Property propertyHeatExchangerAlternateExhaustRecipe = config.get(CATEGORY_HEAT_EXCHANGER, "heat_exchanger_alternate_exhaust_recipe", false, Lang.localise("gui.nc.config.heat_exchanger.heat_exchanger_alternate_exhaust_recipe.comment"));
		propertyHeatExchangerAlternateExhaustRecipe.setLanguageKey("gui.nc.config.heat_exchanger.heat_exchanger_alternate_exhaust_recipe");
		
		Property propertyTurbineMinSize = config.get(CATEGORY_TURBINE, "turbine_min_size", 1, Lang.localise("gui.nc.config.turbine_min_size.comment"), 1, 255);
		propertyTurbineMinSize.setLanguageKey("gui.nc.config.turbine_min_size");
		Property propertyTurbineMaxSize = config.get(CATEGORY_TURBINE, "turbine_max_size", 24, Lang.localise("gui.nc.config.turbine_max_size.comment"), 1, 255);
		propertyTurbineMaxSize.setLanguageKey("gui.nc.config.turbine_max_size");
		Property propertyTurbineBladeEfficiency = config.get(CATEGORY_TURBINE, "turbine_blade_efficiency", new double[] {1D, 1.1D, 1.2D}, Lang.localise("gui.nc.config.turbine_blade_efficiency.comment"), 0.01D, 15D);
		propertyTurbineBladeEfficiency.setLanguageKey("gui.nc.config.turbine_blade_efficiency");
		Property propertyTurbineBladeExpansion = config.get(CATEGORY_TURBINE, "turbine_blade_expansion", new double[] {1.4D, 1.6D, 1.8D}, Lang.localise("gui.nc.config.turbine_blade_expansion.comment"), 1D, 15D);
		propertyTurbineBladeExpansion.setLanguageKey("gui.nc.config.turbine_blade_expansion");
		Property propertyTurbineStatorExpansion = config.get(CATEGORY_TURBINE, "turbine_stator_expansion", 0.75D, Lang.localise("gui.nc.config.turbine_stator_expansion.comment"), 0.01D, 1D);
		propertyTurbineStatorExpansion.setLanguageKey("gui.nc.config.turbine_stator_expansion");
		Property propertyTurbineCoilConductivity = config.get(CATEGORY_TURBINE, "turbine_coil_conductivity", new double[] {0.86D, 0.9D, 0.98D, 1.04D, 1.1D, 1.12D}, Lang.localise("gui.nc.config.turbine_coil_conductivity.comment"), 0.01D, 15D);
		propertyTurbineCoilConductivity.setLanguageKey("gui.nc.config.turbine_coil_conductivity");
		Property propertyTurbinePowerPerMB = config.get(CATEGORY_TURBINE, "turbine_power_per_mb", new double[] {16D, 4D, 4D}, Lang.localise("gui.nc.config.turbine_power_per_mb.comment"), 0D, 255D);
		propertyTurbinePowerPerMB.setLanguageKey("gui.nc.config.turbine_power_per_mb");
		Property propertyTurbineExpansionLevel = config.get(CATEGORY_TURBINE, "turbine_expansion_level", new double[] {4D, 2D, 2D}, Lang.localise("gui.nc.config.turbine_expansion_level.comment"), 1D, 255D);
		propertyTurbineExpansionLevel.setLanguageKey("gui.nc.config.turbine_expansion_level");
		Property propertyTurbineMBPerBlade = config.get(CATEGORY_TURBINE, "turbine_mb_per_blade", 100, Lang.localise("gui.nc.config.turbine_mb_per_blade.comment"), 1, 32767);
		propertyTurbineMBPerBlade.setLanguageKey("gui.nc.config.turbine_mb_per_blade");
		Property propertyTurbineThroughputEfficiencyLeniency = config.get(CATEGORY_TURBINE, "turbine_throughput_efficiency_leniency", 1D, Lang.localise("gui.nc.config.turbine_throughput_efficiency_leniency.comment"), 0D, 255D);
		propertyTurbineThroughputEfficiencyLeniency.setLanguageKey("gui.nc.config.turbine_throughput_efficiency_leniency");
		Property propertyTurbineTensionThroughputFactor = config.get(CATEGORY_TURBINE, "turbine_tension_throughput_factor", 2D, Lang.localise("gui.nc.config.turbine_tension_throughput_factor.comment"), 1D, 255D);
		propertyTurbineTensionThroughputFactor.setLanguageKey("gui.nc.config.turbine_tension_throughput_factor");
		Property propertyTurbineSoundVolume = config.get(CATEGORY_TURBINE, "turbine_sound_volume", 1D, Lang.localise("gui.nc.config.turbine_sound_volume.comment"), 0D, 15D);
		propertyTurbineSoundVolume.setLanguageKey("gui.nc.config.turbine_sound_volume");
		
		Property propertyAcceleratorElectromagnetPower = config.get(CATEGORY_ACCELERATOR, "accelerator_electromagnet_power", 1000D, Lang.localise("gui.nc.config.accelerator_electromagnet_power.comment"), 0D, Integer.MAX_VALUE);
		propertyAcceleratorElectromagnetPower.setLanguageKey("gui.nc.config.accelerator_electromagnet_power");
		Property propertyAcceleratorSupercoolerCoolant = config.get(CATEGORY_ACCELERATOR, "accelerator_supercooler_coolant", 0.125D, Lang.localise("gui.nc.config.accelerator_supercooler_coolant.comment"), 0D, Integer.MAX_VALUE);
		propertyAcceleratorSupercoolerCoolant.setLanguageKey("gui.nc.config.accelerator_supercooler_coolant");
		
		Property propertyQuantumMaxQubits = config.get(CATEGORY_QUANTUM, "quantum_max_qubits", 7, Lang.localise("gui.nc.config.quantum_max_qubits.comment"), 1, 10);
		propertyQuantumMaxQubits.setLanguageKey("gui.nc.config.quantum_max_qubits");
		Property propertyQuantumAnglePrecision = config.get(CATEGORY_QUANTUM, "quantum_angle_precision", 16, Lang.localise("gui.nc.config.quantum_angle_precision.comment"), 2, 5760);
		propertyQuantumAnglePrecision.setLanguageKey("gui.nc.config.quantum_angle_precision");
		
		Property propertyToolMiningLevel = config.get(CATEGORY_TOOL, "tool_mining_level", new int[] {2, 2, 3, 3, 3, 3, 4, 4}, Lang.localise("gui.nc.config.tool_mining_level.comment"), 0, 15);
		propertyToolMiningLevel.setLanguageKey("gui.nc.config.tool_mining_level");
		Property propertyToolDurability = config.get(CATEGORY_TOOL, "tool_durability", new int[] {547, 547*5, 929, 929*5, 1245, 1245*5, 1928, 1928*5}, Lang.localise("gui.nc.config.tool_durability.comment"), 1, 32767);
		propertyToolDurability.setLanguageKey("gui.nc.config.tool_durability");
		Property propertyToolSpeed = config.get(CATEGORY_TOOL, "tool_speed", new double[] {8D, 8D, 10D, 10D, 11D, 11D, 12D, 12D}, Lang.localise("gui.nc.config.tool_speed.comment"), 1D, 255D);
		propertyToolSpeed.setLanguageKey("gui.nc.config.tool_speed");
		Property propertyToolAttackDamage = config.get(CATEGORY_TOOL, "tool_attack_damage", new double[] {2.5D, 2.5D, 3D, 3D, 3D, 3D, 3.5D, 3.5D}, Lang.localise("gui.nc.config.tool_attack_damage.comment"), 0D, 255D);
		propertyToolAttackDamage.setLanguageKey("gui.nc.config.tool_attack_damage");
		Property propertyToolEnchantability = config.get(CATEGORY_TOOL, "tool_enchantability", new int[] {6, 6, 15, 15, 12, 12, 20, 20}, Lang.localise("gui.nc.config.tool_enchantability.comment"), 1, 255);
		propertyToolEnchantability.setLanguageKey("gui.nc.config.tool_enchantability");
		Property propertyToolHandleModifier = config.get(CATEGORY_TOOL, "tool_handle_modifier", new double[] {0.85D, 1.1D, 1D, 0.75D}, Lang.localise("gui.nc.config.tool_handle_modifier.comment"), 0.01D, 10D);
		propertyToolHandleModifier.setLanguageKey("gui.nc.config.tool_handle_modifier");
		
		Property propertyArmorDurability = config.get(CATEGORY_ARMOR, "armor_durability", new int[] {22, 30, 34, 42, 0}, Lang.localise("gui.nc.config.armor_durability.comment"), 1, 127);
		propertyArmorDurability.setLanguageKey("gui.nc.config.armor_durability");
		Property propertyArmorBoron = config.get(CATEGORY_ARMOR, "armor_boron", new int[] {2, 5, 7, 3}, Lang.localise("gui.nc.config.armor_boron.comment"), 1, 25);
		propertyArmorBoron.setLanguageKey("gui.nc.config.armor_boron");
		Property propertyArmorTough = config.get(CATEGORY_ARMOR, "armor_tough", new int[] {3, 6, 7, 3}, Lang.localise("gui.nc.config.armor_tough.comment"), 1, 25);
		propertyArmorTough.setLanguageKey("gui.nc.config.armor_tough");
		Property propertyArmorHardCarbon = config.get(CATEGORY_ARMOR, "armor_hard_carbon", new int[] {3, 5, 7, 3}, Lang.localise("gui.nc.config.armor_hard_carbon.comment"), 1, 25);
		propertyArmorHardCarbon.setLanguageKey("gui.nc.config.armor_hard_carbon");
		Property propertyArmorBoronNitride = config.get(CATEGORY_ARMOR, "armor_boron_nitride", new int[] {3, 6, 8, 3}, Lang.localise("gui.nc.config.armor_boron_nitride.comment"), 1, 25);
		propertyArmorBoronNitride.setLanguageKey("gui.nc.config.armor_boron_nitride");
		Property propertyArmorHazmat = config.get(CATEGORY_ARMOR, "armor_hazmat", new int[] {3, 6, 7, 3}, Lang.localise("gui.nc.config.armor_hazmat.comment"), 1, 25);
		propertyArmorHazmat.setLanguageKey("gui.nc.config.armor_hazmat");
		Property propertyArmorEnchantability = config.get(CATEGORY_ARMOR, "armor_enchantability", new int[] {6, 15, 12, 20, 5}, Lang.localise("gui.nc.config.armor_enchantability.comment"), 1, 255);
		propertyArmorEnchantability.setLanguageKey("gui.nc.config.armor_enchantability");
		Property propertyArmorToughness = config.get(CATEGORY_ARMOR, "armor_toughness", new double[] {1D, 2D, 1D, 2D, 0D}, Lang.localise("gui.nc.config.armor_toughness.comment"), 0D, 8D);
		propertyArmorToughness.setLanguageKey("gui.nc.config.armor_toughness");
		
		Property propertyEntityTrackingRange = config.get(CATEGORY_ENTITY, "entity_tracking_range", 64, Lang.localise("gui.nc.config.entity_tracking_range.comment"), 1, 255);
		propertyEntityTrackingRange.setLanguageKey("gui.nc.config.entity_tracking_range");
		
		Property propertyRadiationEnabled = config.get(CATEGORY_RADIATION, "radiation_enabled", true, Lang.localise("gui.nc.config.radiation_enabled.comment"));
		propertyRadiationEnabled.setLanguageKey("gui.nc.config.radiation_enabled");
		
		Property propertyRadiationWorldChunksPerTick = config.get(CATEGORY_RADIATION, "radiation_world_chunks_per_tick", 5, Lang.localise("gui.nc.config.radiation_world_chunks_per_tick.comment"), 1, 400);
		propertyRadiationWorldChunksPerTick.setLanguageKey("gui.nc.config.radiation_world_chunks_per_tick");
		Property propertyRadiationPlayerTickRate = config.get(CATEGORY_RADIATION, "radiation_player_tick_rate", 5, Lang.localise("gui.nc.config.radiation_player_tick_rate.comment"), 1, 400);
		propertyRadiationPlayerTickRate.setLanguageKey("gui.nc.config.radiation_player_tick_rate");
		
		Property propertyRadiationWorlds = config.get(CATEGORY_RADIATION, "radiation_worlds", new String[] {"4598_2.25"}, Lang.localise("gui.nc.config.radiation_worlds.comment"));
		propertyRadiationWorlds.setLanguageKey("gui.nc.config.radiation_worlds");
		Property propertyRadiationBiomes = config.get(CATEGORY_RADIATION, "radiation_biomes", new String[] {"nuclearcraft:nuclear_wasteland_0.25"}, Lang.localise("gui.nc.config.radiation_biomes.comment"));
		propertyRadiationBiomes.setLanguageKey("gui.nc.config.radiation_biomes");
		Property propertyRadiationStructures = config.get(CATEGORY_RADIATION, "radiation_structures", new String[] {"Fortress_0.0025", "Mineshaft_0.00005", "Stronghold_0.001"}, Lang.localise("gui.nc.config.radiation_structures.comment"));
		propertyRadiationStructures.setLanguageKey("gui.nc.config.radiation_structures");
		Property propertyRadiationWorldLimit = config.get(CATEGORY_RADIATION, "radiation_world_limits", new String[] {}, Lang.localise("gui.nc.config.radiation_world_limits.comment"));
		propertyRadiationWorldLimit.setLanguageKey("gui.nc.config.radiation_world_limits");
		Property propertyRadiationBiomeLimit = config.get(CATEGORY_RADIATION, "radiation_biome_limits", new String[] {}, Lang.localise("gui.nc.config.radiation_biome_limits.comment"));
		propertyRadiationBiomeLimit.setLanguageKey("gui.nc.config.radiation_biome_limits");
		Property propertyRadiationFromBiomesDimsBlacklist = config.get(CATEGORY_RADIATION, "radiation_from_biomes_dims_blacklist", new int[] {144}, Lang.localise("gui.nc.config.radiation_from_biomes_dims_blacklist.comment"));
		propertyRadiationFromBiomesDimsBlacklist.setLanguageKey("gui.nc.config.radiation_from_biomes_dims_blacklist");
		
		Property propertyRadiationOres = config.get(CATEGORY_RADIATION, "radiation_ores", new String[] {"depletedFuelIC2U_" + (RadSources.URANIUM_238*4D + RadSources.PLUTONIUM_239/9D), "depletedFuelIC2MOX_" + (RadSources.PLUTONIUM_239*28D/9D)}, Lang.localise("gui.nc.config.radiation_ores.comment"));
		propertyRadiationOres.setLanguageKey("gui.nc.config.radiation_ores");
		Property propertyRadiationItems = config.get(CATEGORY_RADIATION, "radiation_items", new String[] {"ic2:nuclear:0_0.000000000048108553", "ic2:nuclear:1_" + RadSources.URANIUM_235, "ic2:nuclear:2_" + RadSources.URANIUM_238, "ic2:nuclear:3_" + RadSources.PLUTONIUM_239, "ic2:nuclear:4_0.000000833741517857143", "ic2:nuclear:5_" + (RadSources.URANIUM_235/9D), "ic2:nuclear:6_" + (RadSources.URANIUM_238/9D), "ic2:nuclear:7_" + (RadSources.PLUTONIUM_239/9D), "ic2:nuclear:8_0.000000000048108553", "ic2:nuclear:9_0.000000833741517857143", "ic2:nuclear:10_" + (RadSources.PLUTONIUM_238*3D), "ic2:nuclear:11_" + (RadSources.URANIUM_238*4D + RadSources.PLUTONIUM_239/9D), "ic2:nuclear:12_" + ((RadSources.URANIUM_238*4D + RadSources.PLUTONIUM_239/9D)*2D), "ic2:nuclear:13_" + ((RadSources.URANIUM_238*4D + RadSources.PLUTONIUM_239/9D)*4D), "ic2:nuclear:14_" + (RadSources.PLUTONIUM_239*28D/9D), "ic2:nuclear:15_" + (RadSources.PLUTONIUM_239*2D*28D/9D), "ic2:nuclear:16_" + (RadSources.PLUTONIUM_239*4D*28D/9D)}, Lang.localise("gui.nc.config.radiation_items.comment"));
		propertyRadiationItems.setLanguageKey("gui.nc.config.radiation_items");
		Property propertyRadiationBlocks = config.get(CATEGORY_RADIATION, "radiation_blocks", new String[] {}, Lang.localise("gui.nc.config.radiation_blocks.comment"));
		propertyRadiationBlocks.setLanguageKey("gui.nc.config.radiation_blocks");
		Property propertyRadiationFluids = config.get(CATEGORY_RADIATION, "radiation_fluids", new String[] {}, Lang.localise("gui.nc.config.radiation_fluids.comment"));
		propertyRadiationFluids.setLanguageKey("gui.nc.config.radiation_fluids");
		Property propertyRadiationFoods = config.get(CATEGORY_RADIATION, "radiation_foods", new String[] {"minecraft:golden_apple:0_-20_0.1", "minecraft:golden_apple:1_-100_0.5", "minecraft:golden_carrot:0_-4_0", "minecraft:spider_eye:0_0_0.5", "minecraft:poisonous_potato:0_0_0.5", "minecraft:fish:3_0_2", "minecraft:rabbit_stew:0_0_0.1", "minecraft:chorus_fruit:0_0_-0.25", "minecraft:beetroot:0_0_0.25", "minecraft:beetroot_soup:0_0_1.5", "quark:golden_frog_leg:0_-4_0"}, Lang.localise("gui.nc.config.radiation_foods.comment"));
		propertyRadiationFoods.setLanguageKey("gui.nc.config.radiation_foods");
		Property propertyRadiationOresBlacklist = config.get(CATEGORY_RADIATION, "radiation_ores_blacklist", new String[] {}, Lang.localise("gui.nc.config.radiation_ores_blacklist.comment"));
		propertyRadiationOresBlacklist.setLanguageKey("gui.nc.config.radiation_ores_blacklist");
		Property propertyRadiationItemsBlacklist = config.get(CATEGORY_RADIATION, "radiation_items_blacklist", new String[] {}, Lang.localise("gui.nc.config.radiation_items_blacklist.comment"));
		propertyRadiationItemsBlacklist.setLanguageKey("gui.nc.config.radiation_items_blacklist");
		Property propertyRadiationBlocksBlacklist = config.get(CATEGORY_RADIATION, "radiation_blocks_blacklist", new String[] {}, Lang.localise("gui.nc.config.radiation_blocks_blacklist.comment"));
		propertyRadiationBlocksBlacklist.setLanguageKey("gui.nc.config.radiation_blocks_blacklist");
		Property propertyRadiationFluidsBlacklist = config.get(CATEGORY_RADIATION, "radiation_fluids_blacklist", new String[] {}, Lang.localise("gui.nc.config.radiation_fluids_blacklist.comment"));
		propertyRadiationFluidsBlacklist.setLanguageKey("gui.nc.config.radiation_fluids_blacklist");
		
		Property propertyRadiationMaxPlayerRads = config.get(CATEGORY_RADIATION, "max_player_rads", 1000D, Lang.localise("gui.nc.config.max_player_rads.comment"), 1D, 1000000000D);
		propertyRadiationMaxPlayerRads.setLanguageKey("gui.nc.config.max_player_rads");
		Property propertyRadiationPlayerDecayRate = config.get(CATEGORY_RADIATION, "radiation_player_decay_rate", 0.0000005D, Lang.localise("gui.nc.config.radiation_player_decay_rate.comment"), 0D, 1D);
		propertyRadiationPlayerDecayRate.setLanguageKey("gui.nc.config.radiation_player_decay_rate");
		Property propertyRadiationMaxEntityRads = config.get(CATEGORY_RADIATION, "max_entity_rads", new String[] {}, Lang.localise("gui.nc.config.max_entity_rads.comment"));
		propertyRadiationMaxEntityRads.setLanguageKey("gui.nc.config.max_entity_rads");
		Property propertyRadiationEntityDecayRate = config.get(CATEGORY_RADIATION, "radiation_entity_decay_rate", 0.001D, Lang.localise("gui.nc.config.radiation_entity_decay_rate.comment"), 0D, 1D);
		propertyRadiationEntityDecayRate.setLanguageKey("gui.nc.config.radiation_entity_decay_rate");
		Property propertyRadiationSpreadRate = config.get(CATEGORY_RADIATION, "radiation_spread_rate", 0.1D, Lang.localise("gui.nc.config.radiation_spread_rate.comment"), 0D, 1D);
		propertyRadiationSpreadRate.setLanguageKey("gui.nc.config.radiation_spread_rate");
		Property propertyRadiationSpreadGradient = config.get(CATEGORY_RADIATION, "radiation_spread_gradient", 0.5D, Lang.localise("gui.nc.config.radiation_spread_gradient.comment"), 0D, 1000000000D);
		propertyRadiationSpreadGradient.setLanguageKey("gui.nc.config.radiation_spread_gradient");
		Property propertyRadiationDecayRate = config.get(CATEGORY_RADIATION, "radiation_decay_rate", 0.001D, Lang.localise("gui.nc.config.radiation_decay_rate.comment"), 0D, 1D);
		propertyRadiationDecayRate.setLanguageKey("gui.nc.config.radiation_decay_rate");
		Property propertyRadiationLowestRate = config.get(CATEGORY_RADIATION, "radiation_lowest_rate", 0.000000000000001D, Lang.localise("gui.nc.config.radiation_lowest_rate.comment"), 0.000000000000000001D, 1D);
		propertyRadiationLowestRate.setLanguageKey("gui.nc.config.radiation_lowest_rate");
		Property propertyRadiationChunkLimit = config.get(CATEGORY_RADIATION, "radiation_chunk_limit", -1D, Lang.localise("gui.nc.config.radiation_chunk_limit.comment"), -1D, Double.MAX_VALUE);
		propertyRadiationChunkLimit.setLanguageKey("gui.nc.config.radiation_chunk_limit");
		
		//Property propertyRadiationBlockEffects = config.get(CATEGORY_RADIATION, "radiation_block_effects", new String[] {"nuclearcraft:wasteland_earth:0_20_materialMapColor@8368696", "nuclearcraft:dry_earth:0_20_materialMapColor@9923917", "minecraft:air:0_10_materialMapColor@31744"}, Lang.localise("gui.nc.config.radiation_block_effects.comment"));
		//propertyRadiationBlockEffects.setLanguageKey("gui.nc.config.radiation_block_effects");
		//Property propertyRadiationBlockEffectLimit = config.get(CATEGORY_RADIATION, "radiation_block_effect_limit", 10D, Lang.localise("gui.nc.config.radiation_block_effect_limit.comment"), 0.000000000000000001D, Double.MAX_VALUE);
		//propertyRadiationBlockEffectLimit.setLanguageKey("gui.nc.config.radiation_block_effect_limit");
		Property propertyRadiationBlockEffectMaxRate = config.get(CATEGORY_RADIATION, "radiation_block_effect_max_rate", 0, Lang.localise("gui.nc.config.radiation_block_effect_max_rate.comment"), 0, 15);
		propertyRadiationBlockEffectMaxRate.setLanguageKey("gui.nc.config.radiation_block_effect_max_rate");
		Property propertyRadiationRainMult = config.get(CATEGORY_RADIATION, "radiation_rain_mult", 1D, Lang.localise("gui.nc.config.radiation_rain_mult.comment"), 0.000001D, 1000000D);
		propertyRadiationRainMult.setLanguageKey("gui.nc.config.radiation_rain_mult");
		Property propertyRadiationSwimMult = config.get(CATEGORY_RADIATION, "radiation_swim_mult", 2D, Lang.localise("gui.nc.config.radiation_swim_mult.comment"), 0.000001D, 1000000D);
		propertyRadiationSwimMult.setLanguageKey("gui.nc.config.radiation_swim_mult");
		
		Property propertyRadiationRadawayAmount = config.get(CATEGORY_RADIATION, "radiation_radaway_amount", 300D, Lang.localise("gui.nc.config.radiation_radaway_amount.comment"), 0.001D, 1000000000D);
		propertyRadiationRadawayAmount.setLanguageKey("gui.nc.config.radiation_radaway_amount");
		Property propertyRadiationRadawaySlowAmount = config.get(CATEGORY_RADIATION, "radiation_radaway_slow_amount", 300D, Lang.localise("gui.nc.config.radiation_radaway_slow_amount.comment"), 0.001D, 1000000000D);
		propertyRadiationRadawaySlowAmount.setLanguageKey("gui.nc.config.radiation_radaway_slow_amount");
		Property propertyRadiationRadawayRate = config.get(CATEGORY_RADIATION, "radiation_radaway_rate", 5D, Lang.localise("gui.nc.config.radiation_radaway_rate.comment"), 0.001D, 1000000000D);
		propertyRadiationRadawayRate.setLanguageKey("gui.nc.config.radiation_radaway_rate");
		Property propertyRadiationRadawaySlowRate = config.get(CATEGORY_RADIATION, "radiation_radaway_slow_rate", 0.025D, Lang.localise("gui.nc.config.radiation_radaway_slow_rate.comment"), 0.00001D, 10000000D);
		propertyRadiationRadawaySlowRate.setLanguageKey("gui.nc.config.radiation_radaway_slow_rate");
		Property propertyRadiationPoisonTime = config.get(CATEGORY_RADIATION, "radiation_poison_time", 60D, Lang.localise("gui.nc.config.radiation_poison_time.comment"), 1D, 1000000D);
		propertyRadiationPoisonTime.setLanguageKey("gui.nc.config.radiation_poison_time");
		Property propertyRadiationRadawayCooldown = config.get(CATEGORY_RADIATION, "radiation_radaway_cooldown", 0D, Lang.localise("gui.nc.config.radiation_radaway_cooldown.comment"), 0D, 100000D);
		propertyRadiationRadawayCooldown.setLanguageKey("gui.nc.config.radiation_radaway_cooldown");
		Property propertyRadiationRadXAmount = config.get(CATEGORY_RADIATION, "radiation_rad_x_amount", 25D, Lang.localise("gui.nc.config.radiation_rad_x_amount.comment"), 0.001D, 1000000000D);
		propertyRadiationRadXAmount.setLanguageKey("gui.nc.config.radiation_rad_x_amount");
		Property propertyRadiationRadXLifetime = config.get(CATEGORY_RADIATION, "radiation_rad_x_lifetime", 12000D, Lang.localise("gui.nc.config.radiation_rad_x_lifetime.comment"), 20D, 1000000000D);
		propertyRadiationRadXLifetime.setLanguageKey("gui.nc.config.radiation_rad_x_lifetime");
		Property propertyRadiationRadXCooldown = config.get(CATEGORY_RADIATION, "radiation_rad_x_cooldown", 0D, Lang.localise("gui.nc.config.radiation_rad_x_cooldown.comment"), 0D, 100000D);
		propertyRadiationRadXCooldown.setLanguageKey("gui.nc.config.radiation_rad_x_cooldown");
		Property propertyRadiationShieldingLevel = config.get(CATEGORY_RADIATION, "radiation_shielding_level", new double[] {0.01D, 0.1D, 1D}, Lang.localise("gui.nc.config.radiation_shielding_level.comment"), 0.000000000000000001D, 1000D);
		propertyRadiationShieldingLevel.setLanguageKey("gui.nc.config.radiation_shielding_level");
		Property propertyRadiationTileShielding = config.get(CATEGORY_RADIATION, "radiation_tile_shielding", true, Lang.localise("gui.nc.config.radiation_tile_shielding.comment"));
		propertyRadiationTileShielding.setLanguageKey("gui.nc.config.radiation_tile_shielding");
		Property propertyRadiationScrubberRate = config.get(CATEGORY_RADIATION, "radiation_scrubber_fraction", 0.125D, Lang.localise("gui.nc.config.radiation_scrubber_fraction.comment"), 0.001D, 1D);
		propertyRadiationScrubberRate.setLanguageKey("gui.nc.config.radiation_scrubber_fraction");
		Property propertyRadiationScrubberRadius = config.get(CATEGORY_RADIATION, "radiation_scrubber_radius", 4, Lang.localise("gui.nc.config.radiation_scrubber_radius.comment"), 1, 10);
		propertyRadiationScrubberRadius.setLanguageKey("gui.nc.config.radiation_scrubber_radius");
		Property propertyRadiationScrubberNonLinear = config.get(CATEGORY_RADIATION, "radiation_scrubber_non_linear", true, Lang.localise("gui.nc.config.radiation_scrubber_non_linear.comment"));
		propertyRadiationScrubberNonLinear.setLanguageKey("gui.nc.config.radiation_scrubber_non_linear");
		Property propertyRadiationScrubberParam = config.get(CATEGORY_RADIATION, "radiation_scrubber_param", new double[] {2.14280951676725D, 3D, 4D, 2D}, Lang.localise("gui.nc.config.radiation_scrubber_param.comment"), 1D, 15D);
		propertyRadiationScrubberParam.setLanguageKey("gui.nc.config.radiation_scrubber_param");
		Property propertyRadiationScrubberTime = config.get(CATEGORY_RADIATION, "radiation_scrubber_time", new int[] {12000, 2400, 96000}, Lang.localise("gui.nc.config.radiation_scrubber_time.comment"), 1, Integer.MAX_VALUE);
		propertyRadiationScrubberTime.setLanguageKey("gui.nc.config.radiation_scrubber_time");
		Property propertyRadiationScrubberPower = config.get(CATEGORY_RADIATION, "radiation_scrubber_power", new int[] {200, 40, 20}, Lang.localise("gui.nc.config.radiation_scrubber_power.comment"), 0, Integer.MAX_VALUE);
		propertyRadiationScrubberPower.setLanguageKey("gui.nc.config.radiation_scrubber_power");
		Property propertyRadiationScrubberEfficiency = config.get(CATEGORY_RADIATION, "radiation_scrubber_efficiency", new double[] {1D, 5D, 0.25D}, Lang.localise("gui.nc.config.radiation_scrubber_efficiency.comment"), 0D, 255D);
		propertyRadiationScrubberEfficiency.setLanguageKey("gui.nc.config.radiation_scrubber_efficiency");
		Property propertyRadiationGeigerBlockRedstone = config.get(CATEGORY_RADIATION, "radiation_geiger_block_redstone", 3D, Lang.localise("gui.nc.config.radiation_geiger_block_redstone.comment"), -100D, 100D);
		propertyRadiationGeigerBlockRedstone.setLanguageKey("gui.nc.config.radiation_geiger_block_redstone");
		
		Property propertyRadiationShieldingDefaultRecipes = config.get(CATEGORY_RADIATION, "radiation_shielding_default_recipes", true, Lang.localise("gui.nc.config.radiation_shielding_default_recipes.comment"));
		propertyRadiationShieldingDefaultRecipes.setLanguageKey("gui.nc.config.radiation_shielding_default_recipes");
		Property propertyRadiationShieldingItemBlacklist = config.get(CATEGORY_RADIATION, "radiation_shielding_item_blacklist", new String[] {"nuclearcraft:helm_hazmat", "nuclearcraft:chest_hazmat", "nuclearcraft:legs_hazmat", "nuclearcraft:boots_hazmat", "ic2:hazmat_helmet", "ic2:hazmat_chestplate", "ic2:hazmat_leggings", "ic2:rubber_boots", "extraplanets:tier1_space_suit_helmet", "extraplanets:tier1_space_suit_chest", "extraplanets:tier1_space_suit_jetpack_chest", "extraplanets:tier1_space_suit_leggings", "extraplanets:tier1_space_suit_boots", "extraplanets:tier1_space_suit_gravity_boots", "extraplanets:tier2_space_suit_helmet", "extraplanets:tier2_space_suit_chest", "extraplanets:tier2_space_suit_jetpack_chest", "extraplanets:tier2_space_suit_leggings", "extraplanets:tier2_space_suit_boots", "extraplanets:tier2_space_suit_gravity_boots", "extraplanets:tier3_space_suit_helmet", "extraplanets:tier3_space_suit_chest", "extraplanets:tier3_space_suit_jetpack_chest", "extraplanets:tier3_space_suit_leggings", "extraplanets:tier3_space_suit_boots", "extraplanets:tier3_space_suit_gravity_boots", "extraplanets:tier4_space_suit_helmet", "extraplanets:tier4_space_suit_chest", "extraplanets:tier4_space_suit_jetpack_chest", "extraplanets:tier4_space_suit_leggings", "extraplanets:tier4_space_suit_boots", "extraplanets:tier4_space_suit_gravity_boots"}, Lang.localise("gui.nc.config.radiation_shielding_item_blacklist.comment"));
		propertyRadiationShieldingItemBlacklist.setLanguageKey("gui.nc.config.radiation_shielding_item_blacklist");
		Property propertyRadiationShieldingCustomStacks = config.get(CATEGORY_RADIATION, "radiation_shielding_custom_stacks", new String[] {}, Lang.localise("gui.nc.config.radiation_shielding_custom_stacks.comment"));
		propertyRadiationShieldingCustomStacks.setLanguageKey("gui.nc.config.radiation_shielding_custom_stacks");
		Property propertyRadiationShieldingDefaultLevels = config.get(CATEGORY_RADIATION, "radiation_shielding_default_levels", new String[] {"nuclearcraft:helm_hazmat_2.0", "nuclearcraft:chest_hazmat_3.0", "nuclearcraft:legs_hazmat_2.0", "nuclearcraft:boots_hazmat_2.0", "ic2:hazmat_helmet_2.0", "ic2:hazmat_chestplate_3.0", "ic2:hazmat_leggings_2.0", "ic2:rubber_boots_2.0", "ic2:quantum_helmet_2.0", "ic2:quantum_chestplate_3.0", "ic2:quantum_leggings_2.0", "ic2:quantum_boots_2.0", "gravisuite:gravichestplate_3.0", "ic2:itemarmorquantumhelmet_2.0", "ic2:itemarmorquantumchestplate_3.0", "ic2:itemarmorquantumlegs_2.0", "ic2:itemarmorquantumboots_2.0", "gravisuit:gravisuit_3.0", "gravisuit:nucleargravisuit_3.0", "extraplanets:tier1_space_suit_helmet_1.0", "extraplanets:tier1_space_suit_chest_1.5", "extraplanets:tier1_space_suit_jetpack_chest_1.5", "extraplanets:tier1_space_suit_leggings_1.0", "extraplanets:tier1_space_suit_boots_1.0", "extraplanets:tier1_space_suit_gravity_boots_1.0", "extraplanets:tier2_space_suit_helmet_1.3", "extraplanets:tier2_space_suit_chest_1.95", "extraplanets:tier2_space_suit_jetpack_chest_1.95", "extraplanets:tier2_space_suit_leggings_1.3", "extraplanets:tier2_space_suit_boots_1.3", "extraplanets:tier2_space_suit_gravity_boots_1.3", "extraplanets:tier3_space_suit_helmet_1.6", "extraplanets:tier3_space_suit_chest_2.4", "extraplanets:tier3_space_suit_jetpack_chest_2.4", "extraplanets:tier3_space_suit_leggings_1.6", "extraplanets:tier3_space_suit_boots_1.6", "extraplanets:tier3_space_suit_gravity_boots_1.6", "extraplanets:tier4_space_suit_helmet_2.0", "extraplanets:tier4_space_suit_chest_3.0", "extraplanets:tier4_space_suit_jetpack_chest_3.0", "extraplanets:tier4_space_suit_leggings_2.0", "extraplanets:tier4_space_suit_boots_2.0", "extraplanets:tier4_space_suit_gravity_boots_2.0"}, Lang.localise("gui.nc.config.radiation_shielding_default_levels.comment"));
		propertyRadiationShieldingDefaultLevels.setLanguageKey("gui.nc.config.radiation_shielding_default_levels");
		
		Property propertyRadiationHardcoreStacks = config.get(CATEGORY_RADIATION, "radiation_hardcore_stacks", true, Lang.localise("gui.nc.config.radiation_hardcore_stacks.comment"));
		propertyRadiationHardcoreStacks.setLanguageKey("gui.nc.config.radiation_hardcore_stacks");
		Property propertyRadiationHardcoreContainers = config.get(CATEGORY_RADIATION, "radiation_hardcore_containers", 0D, Lang.localise("gui.nc.config.radiation_hardcore_containers.comment"), 0D, 1D);
		propertyRadiationHardcoreContainers.setLanguageKey("gui.nc.config.radiation_hardcore_containers");
		Property propertyRadiationDroppedItems = config.get(CATEGORY_RADIATION, "radiation_dropped_items", true, Lang.localise("gui.nc.config.radiation_dropped_items.comment"));
		propertyRadiationDroppedItems.setLanguageKey("gui.nc.config.radiation_dropped_items");
		Property propertyRadiationDeathPersist = config.get(CATEGORY_RADIATION, "radiation_death_persist", true, Lang.localise("gui.nc.config.radiation_death_persist.comment"));
		propertyRadiationDeathPersist.setLanguageKey("gui.nc.config.radiation_death_persist");
		Property propertyRadiationDeathPersistFraction = config.get(CATEGORY_RADIATION, "radiation_death_persist_fraction", 0.75D, Lang.localise("gui.nc.config.radiation_death_persist_fraction.comment"), 0D, 1D);
		propertyRadiationDeathPersistFraction.setLanguageKey("gui.nc.config.radiation_death_persist_fraction");
		Property propertyRadiationDeathImmunityTime = config.get(CATEGORY_RADIATION, "radiation_death_immunity_time", 90D, Lang.localise("gui.nc.config.radiation_death_immunity_time.comment"), 0D, 3600D);
		propertyRadiationDeathImmunityTime.setLanguageKey("gui.nc.config.radiation_death_immunity_time");
		
		Property propertyRadiationPlayerDebuffLists = config.get(CATEGORY_RADIATION, "radiation_player_debuff_lists", new String[] {"40.0_minecraft:weakness@1", "55.0_minecraft:weakness@1,minecraft:mining_fatigue@1", "70.0_minecraft:weakness@2,minecraft:mining_fatigue@1,minecraft:hunger@1", "80.0_minecraft:weakness@2,minecraft:mining_fatigue@2,minecraft:hunger@1,minecraft:poison@1", "90.0_minecraft:weakness@3,minecraft:mining_fatigue@3,minecraft:hunger@2,minecraft:poison@1,minecraft:wither@1"}, Lang.localise("gui.nc.config.radiation_player_debuff_lists.comment"));
		propertyRadiationPlayerDebuffLists.setLanguageKey("gui.nc.config.radiation_player_debuff_lists");
		Property propertyRadiationPassiveDebuffLists = config.get(CATEGORY_RADIATION, "radiation_passive_debuff_lists", new String[] {"40.0_minecraft:weakness@1", "55.0_minecraft:weakness@1,minecraft:mining_fatigue@1", "70.0_minecraft:weakness@2,minecraft:mining_fatigue@1,minecraft:hunger@1", "80.0_minecraft:weakness@2,minecraft:mining_fatigue@2,minecraft:hunger@1,minecraft:poison@1", "90.0_minecraft:weakness@3,minecraft:mining_fatigue@3,minecraft:hunger@2,minecraft:poison@1,minecraft:wither@1"}, Lang.localise("gui.nc.config.radiation_passive_debuff_lists.comment"));
		propertyRadiationPassiveDebuffLists.setLanguageKey("gui.nc.config.radiation_passive_debuff_lists");
		Property propertyRadiationMobBuffLists = config.get(CATEGORY_RADIATION, "radiation_mob_buff_lists", new String[] {"40.0_minecraft:speed@1", "55.0_minecraft:speed@1,minecraft:strength@1", "70.0_minecraft:speed@1,minecraft:strength@1,minecraft:resistance@1", "80.0_minecraft:speed@1,minecraft:strength@1,minecraft:resistance@1,minecraft:absorption@1", "90.0_minecraft:speed@1,minecraft:strength@1,minecraft:resistance@1,minecraft:absorption@1,minecraft:regeneration@1"}, Lang.localise("gui.nc.config.radiation_mob_buff_lists.comment"));
		propertyRadiationMobBuffLists.setLanguageKey("gui.nc.config.radiation_mob_buff_lists");
		
		Property propertyRadiationHorseArmor = config.get(CATEGORY_RADIATION, "radiation_horse_armor", false, Lang.localise("gui.nc.config.radiation_horse_armor.comment"));
		propertyRadiationHorseArmor.setLanguageKey("gui.nc.config.radiation_horse_armor");
		
		Property propertyRadiationHUDSize = config.get(CATEGORY_RADIATION, "radiation_hud_size", 1D, Lang.localise("gui.nc.config.radiation_hud_size.comment"), 0.1D, 10D);
		propertyRadiationHUDSize.setLanguageKey("gui.nc.config.radiation_hud_size");
		Property propertyRadiationHUDPosition = config.get(CATEGORY_RADIATION, "radiation_hud_position", 225D, Lang.localise("gui.nc.config.radiation_hud_position.comment"), 0D, 360D);
		propertyRadiationHUDPosition.setLanguageKey("gui.nc.config.radiation_hud_position");
		Property propertyRadiationHUDPositionCartesian = config.get(CATEGORY_RADIATION, "radiation_hud_position_cartesian", new double[] {}, Lang.localise("gui.nc.config.radiation_hud_position_cartesian.comment"), 0D, 1D);
		propertyRadiationHUDPositionCartesian.setLanguageKey("gui.nc.config.radiation_hud_position_cartesian");
		Property propertyRadiationHUDTextOutline = config.get(CATEGORY_RADIATION, "radiation_hud_text_outline", false, Lang.localise("gui.nc.config.radiation_hud_text_outline.comment"));
		propertyRadiationHUDTextOutline.setLanguageKey("gui.nc.config.radiation_hud_text_outline");
		Property propertyRadiationRequireCounter = config.get(CATEGORY_RADIATION, "radiation_require_counter", true, Lang.localise("gui.nc.config.radiation_require_counter.comment"));
		propertyRadiationRequireCounter.setLanguageKey("gui.nc.config.radiation_require_counter");
		Property propertyRadiationChunkBoundaries = config.get(CATEGORY_RADIATION, "radiation_chunk_boundaries", true, Lang.localise("gui.nc.config.radiation_chunk_boundaries.comment"));
		propertyRadiationChunkBoundaries.setLanguageKey("gui.nc.config.radiation_chunk_boundaries");
		Property propertyRadiationUnitPrefixes = config.get(CATEGORY_RADIATION, "radiation_unit_prefixes", 0, Lang.localise("gui.nc.config.radiation_unit_prefixes.comment"), 0, 15);
		propertyRadiationUnitPrefixes.setLanguageKey("gui.nc.config.radiation_unit_prefixes");
		
		Property propertyRadiationBadgeDurability = config.get(CATEGORY_RADIATION, "radiation_badge_durability", 250D, Lang.localise("gui.nc.config.radiation_badge_durability.comment"), 0.000000000000000001D, 1000D);
		propertyRadiationBadgeDurability.setLanguageKey("gui.nc.config.radiation_badge_durability");
		Property propertyRadiationBadgeInfoRate = config.get(CATEGORY_RADIATION, "radiation_badge_info_rate", 0.1D, Lang.localise("gui.nc.config.radiation_badge_info_rate.comment"), 0.000000000000000001D, 1D);
		propertyRadiationBadgeInfoRate.setLanguageKey("gui.nc.config.radiation_badge_info_rate");
		
		Property propertyRegisterProcessor = config.get(CATEGORY_REGISTRATION, "register_processor", new boolean[] {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true}, Lang.localise("gui.nc.config.register_processor.comment"));
		propertyRegisterProcessor.setLanguageKey("gui.nc.config.register_processor");
		Property propertyRegisterPassive = config.get(CATEGORY_REGISTRATION, "register_passive", new boolean[] {true, true, true}, Lang.localise("gui.nc.config.register_passive.comment"));
		propertyRegisterPassive.setLanguageKey("gui.nc.config.register_passive");
		Property propertyRegisterBattery = config.get(CATEGORY_REGISTRATION, "register_battery", new boolean[] {true, true}, Lang.localise("gui.nc.config.register_battery.comment"));
		propertyRegisterBattery.setLanguageKey("gui.nc.config.register_battery");
		Property propertyRegisterQuantum = config.get(CATEGORY_REGISTRATION, "register_quantum", true, Lang.localise("gui.nc.config.register_quantum.comment"));
		propertyRegisterQuantum.setLanguageKey("gui.nc.config.register_quantum");
		Property propertyRegisterTool = config.get(CATEGORY_REGISTRATION, "register_tool", new boolean[] {true, true, true, true}, Lang.localise("gui.nc.config.register_tool.comment"));
		propertyRegisterTool.setLanguageKey("gui.nc.config.register_tool");
		Property propertyRegisterTiCTool = config.get(CATEGORY_REGISTRATION, "register_tic_tool", new boolean[] {true, true, true, true, true, true, true, true}, Lang.localise("gui.nc.config.register_tic_tool.comment"));
		propertyRegisterTiCTool.setLanguageKey("gui.nc.config.register_tic_tool");
		Property propertyRegisterArmor = config.get(CATEGORY_REGISTRATION, "register_armor", new boolean[] {true, true, true, true}, Lang.localise("gui.nc.config.register_armor.comment"));
		propertyRegisterArmor.setLanguageKey("gui.nc.config.register_armor");
		Property propertyRegisterConarmArmor = config.get(CATEGORY_REGISTRATION, "register_conarm_armor", new boolean[] {true, true, true, true, true, true, true, true}, Lang.localise("gui.nc.config.register_conarm_armor.comment"));
		propertyRegisterConarmArmor.setLanguageKey("gui.nc.config.register_conarm_armor");
		Property propertyRegisterEntity = config.get(CATEGORY_REGISTRATION, "register_entity", new boolean[] {true}, Lang.localise("gui.nc.config.register_entity.comment"));
		propertyRegisterEntity.setLanguageKey("gui.nc.config.register_entity");
		Property propertyRegisterFluidBlocks = config.get(CATEGORY_REGISTRATION, "register_fluid_blocks", false, Lang.localise("gui.nc.config.register_fluid_blocks.comment"));
		propertyRegisterFluidBlocks.setLanguageKey("gui.nc.config.register_fluid_blocks");
		Property propertyRegisterCoFHFluids = config.get(CATEGORY_REGISTRATION, "register_cofh_fluids", false, Lang.localise("gui.nc.config.register_cofh_fluids.comment"));
		propertyRegisterCoFHFluids.setLanguageKey("gui.nc.config.register_cofh_fluids");
		Property propertyRegisterProjectEEMC = config.get(CATEGORY_REGISTRATION, "register_projecte_emc", true, Lang.localise("gui.nc.config.register_projecte_emc.comment"));
		propertyRegisterProjectEEMC.setLanguageKey("gui.nc.config.register_projecte_emc");
		
		Property propertySingleCreativeTab = config.get(CATEGORY_MISC, "single_creative_tab", false, Lang.localise("gui.nc.config.single_creative_tab.comment"));
		propertySingleCreativeTab.setLanguageKey("gui.nc.config.single_creative_tab");
		Property propertyCtrlInfo = config.get(CATEGORY_MISC, "ctrl_info", false, Lang.localise("gui.nc.config.ctrl_info.comment"));
		propertyCtrlInfo.setLanguageKey("gui.nc.config.ctrl_info");
		Property propertyJEIChanceItemsIncludeNull = config.get(CATEGORY_MISC, "jei_chance_items_include_null", false, Lang.localise("gui.nc.config.jei_chance_items_include_null.comment"));
		propertyJEIChanceItemsIncludeNull.setLanguageKey("gui.nc.config.jei_chance_items_include_null");
		
		Property propertyRareDrops = config.get(CATEGORY_MISC, "rare_drops", false, Lang.localise("gui.nc.config.rare_drops.comment"));
		propertyRareDrops.setLanguageKey("gui.nc.config.rare_drops");
		Property propertyDungeonLoot = config.get(CATEGORY_MISC, "dungeon_loot", true, Lang.localise("gui.nc.config.dungeon_loot.comment"));
		propertyDungeonLoot.setLanguageKey("gui.nc.config.dungeon_loot");
		
		Property propertyOreDictRawMaterialRecipes = config.get(CATEGORY_MISC, "ore_dict_raw_material_recipes", false, Lang.localise("gui.nc.config.ore_dict_raw_material_recipes.comment"));
		propertyOreDictRawMaterialRecipes.setLanguageKey("gui.nc.config.ore_dict_raw_material_recipes");
		Property propertyOreDictPriorityBool = config.get(CATEGORY_MISC, "ore_dict_priority_bool", true, Lang.localise("gui.nc.config.ore_dict_priority_bool.comment"));
		propertyOreDictPriorityBool.setLanguageKey("gui.nc.config.ore_dict_priority_bool");
		Property propertyOreDictPriority = config.get(CATEGORY_MISC, "ore_dict_priority", new String[] {"minecraft", "thermalfoundation", "techreborn", "nuclearcraft", "immersiveengineering", "mekanism", "ic2", "appliedenergistics2", "refinedstorage", "actuallyadditions", "advancedRocketry", "thaumcraft", "biomesoplenty"}, Lang.localise("gui.nc.config.ore_dict_priority.comment"));
		propertyOreDictPriority.setLanguageKey("gui.nc.config.ore_dict_priority");
		
		List<String> propertyOrderWorldGen = new ArrayList<>();
		propertyOrderWorldGen.add(propertyOreDims.getName());
		propertyOrderWorldGen.add(propertyOreDimsListType.getName());
		propertyOrderWorldGen.add(propertyOreGen.getName());
		propertyOrderWorldGen.add(propertyOreSize.getName());
		propertyOrderWorldGen.add(propertyOreRate.getName());
		propertyOrderWorldGen.add(propertyOreMinHeight.getName());
		propertyOrderWorldGen.add(propertyOreMaxHeight.getName());
		propertyOrderWorldGen.add(propertyOreDrops.getName());
		propertyOrderWorldGen.add(propertyOreHideDisabled.getName());
		propertyOrderWorldGen.add(propertyOreHarvestLevels.getName());
		
		propertyOrderWorldGen.add(propertyWastelandBiome.getName());
		propertyOrderWorldGen.add(propertyWastelandBiomeWeight.getName());
		propertyOrderWorldGen.add(propertyWastelandDimensionGen.getName());
		propertyOrderWorldGen.add(propertyWastelandDimension.getName());
		propertyOrderWorldGen.add(propertyMushroomSpreadRate.getName());
		propertyOrderWorldGen.add(propertyMushroomGen.getName());
		propertyOrderWorldGen.add(propertyMushroomGenSize.getName());
		propertyOrderWorldGen.add(propertyMushroomGenRate.getName());
		config.setCategoryPropertyOrder(CATEGORY_WORLD_GEN, propertyOrderWorldGen);
		
		List<String> propertyOrderProcessor = new ArrayList<>();
		propertyOrderProcessor.add(propertyProcessorTime.getName());
		propertyOrderProcessor.add(propertyProcessorPower.getName());
		propertyOrderProcessor.add(propertySpeedUpgradePowerLaws.getName());
		propertyOrderProcessor.add(propertySpeedUpgradeMultipliers.getName());
		propertyOrderProcessor.add(propertyEnergyUpgradePowerLaws.getName());
		propertyOrderProcessor.add(propertyEnergyUpgradeMultipliers.getName());
		propertyOrderProcessor.add(propertyRFPerEU.getName());
		propertyOrderProcessor.add(propertyEnableGTCEEU.getName());
		propertyOrderProcessor.add(propertyEnableMekGas.getName());
		propertyOrderProcessor.add(propertyMachineUpdateRate.getName());
		propertyOrderProcessor.add(propertyProcessorPassiveRate.getName());
		propertyOrderProcessor.add(propertyPassivePush.getName());
		propertyOrderProcessor.add(propertyCobbleGenPower.getName());
		propertyOrderProcessor.add(propertyOreProcessing.getName());
		propertyOrderProcessor.add(propertyManufactoryWood.getName());
		propertyOrderProcessor.add(propertyRockCrusherAlternate.getName());
		propertyOrderProcessor.add(propertyGTCERecipes.getName());
		propertyOrderProcessor.add(propertyGTCERecipeLogging.getName());
		propertyOrderProcessor.add(propertySmartProcessorInput.getName());
		propertyOrderProcessor.add(propertyPermeation.getName());
		propertyOrderProcessor.add(propertyFactorRecipes.getName());
		propertyOrderProcessor.add(propertyProcessorParticles.getName());
		config.setCategoryPropertyOrder(CATEGORY_PROCESSOR, propertyOrderProcessor);
		
		List<String> propertyOrderGenerator = new ArrayList<>();
		propertyOrderGenerator.add(propertyRTGPower.getName());
		propertyOrderGenerator.add(propertySolarPower.getName());
		propertyOrderGenerator.add(propertyDecayLifetime.getName());
		propertyOrderGenerator.add(propertyDecayPower.getName());
		config.setCategoryPropertyOrder(CATEGORY_GENERATOR, propertyOrderGenerator);
		
		List<String> propertyOrderEnergyStorage = new ArrayList<>();
		propertyOrderEnergyStorage.add(propertyBatteryCapacity.getName());
		config.setCategoryPropertyOrder(CATEGORY_ENERGY_STORAGE, propertyOrderEnergyStorage);
		
		List<String> propertyOrderFission = new ArrayList<>();
		propertyOrderFission.add(propertyFissionFuelTimeMultiplier.getName());
		propertyOrderFission.add(propertyFissionSourceEfficiency.getName());
		propertyOrderFission.add(propertyFissionSinkCoolingRate.getName());
		propertyOrderFission.add(propertyFissionHeaterCoolingRate.getName());
		propertyOrderFission.add(propertyFissionModeratorFluxFactor.getName());
		propertyOrderFission.add(propertyFissionModeratorEfficiency.getName());
		propertyOrderFission.add(propertyFissionReflectorEfficiency.getName());
		propertyOrderFission.add(propertyFissionReflectorReflectivity.getName());
		propertyOrderFission.add(propertyFissionShieldHeatPerFlux.getName());
		propertyOrderFission.add(propertyFissionShieldEfficiency.getName());
		propertyOrderFission.add(propertyFissionIrradiatorHeatPerFlux.getName());
		propertyOrderFission.add(propertyFissionIrradiatorEfficiency.getName());
		propertyOrderFission.add(propertyFissionCoolingEfficiencyLeniency.getName());
		propertyOrderFission.add(propertyFissionSparsityPenaltyParams.getName());
		propertyOrderFission.add(propertyFissionOverheat.getName());
		propertyOrderFission.add(propertyFissionExplosions.getName());
		propertyOrderFission.add(propertyFissionMeltdownRadiationMultiplier.getName());
		propertyOrderFission.add(propertyFissionMinSize.getName());
		propertyOrderFission.add(propertyFissionMaxSize.getName());
		propertyOrderFission.add(propertyFissionComparatorMaxTemp.getName());
		propertyOrderFission.add(propertyFissionNeutronReach.getName());
		propertyOrderFission.add(propertyFissionSoundVolume.getName());
		
		propertyOrderFission.add(propertyFissionThoriumFuelTime.getName());
		propertyOrderFission.add(propertyFissionThoriumHeatGeneration.getName());
		propertyOrderFission.add(propertyFissionThoriumEfficiency.getName());
		propertyOrderFission.add(propertyFissionThoriumCriticality.getName());
		propertyOrderFission.add(propertyFissionThoriumSelfPriming.getName());
		propertyOrderFission.add(propertyFissionThoriumRadiation.getName());
		
		propertyOrderFission.add(propertyFissionUraniumFuelTime.getName());
		propertyOrderFission.add(propertyFissionUraniumHeatGeneration.getName());
		propertyOrderFission.add(propertyFissionUraniumEfficiency.getName());
		propertyOrderFission.add(propertyFissionUraniumCriticality.getName());
		propertyOrderFission.add(propertyFissionUraniumSelfPriming.getName());
		propertyOrderFission.add(propertyFissionUraniumRadiation.getName());
		
		propertyOrderFission.add(propertyFissionNeptuniumFuelTime.getName());
		propertyOrderFission.add(propertyFissionNeptuniumHeatGeneration.getName());
		propertyOrderFission.add(propertyFissionNeptuniumEfficiency.getName());
		propertyOrderFission.add(propertyFissionNeptuniumCriticality.getName());
		propertyOrderFission.add(propertyFissionNeptuniumSelfPriming.getName());
		propertyOrderFission.add(propertyFissionNeptuniumRadiation.getName());
		
		propertyOrderFission.add(propertyFissionPlutoniumFuelTime.getName());
		propertyOrderFission.add(propertyFissionPlutoniumHeatGeneration.getName());
		propertyOrderFission.add(propertyFissionPlutoniumEfficiency.getName());
		propertyOrderFission.add(propertyFissionPlutoniumCriticality.getName());
		propertyOrderFission.add(propertyFissionPlutoniumSelfPriming.getName());
		propertyOrderFission.add(propertyFissionPlutoniumRadiation.getName());
		
		propertyOrderFission.add(propertyFissionMixedFuelTime.getName());
		propertyOrderFission.add(propertyFissionMixedHeatGeneration.getName());
		propertyOrderFission.add(propertyFissionMixedEfficiency.getName());
		propertyOrderFission.add(propertyFissionMixedCriticality.getName());
		propertyOrderFission.add(propertyFissionMixedSelfPriming.getName());
		propertyOrderFission.add(propertyFissionMixedRadiation.getName());
		
		propertyOrderFission.add(propertyFissionAmericiumFuelTime.getName());
		propertyOrderFission.add(propertyFissionAmericiumHeatGeneration.getName());
		propertyOrderFission.add(propertyFissionAmericiumEfficiency.getName());
		propertyOrderFission.add(propertyFissionAmericiumCriticality.getName());
		propertyOrderFission.add(propertyFissionAmericiumSelfPriming.getName());
		propertyOrderFission.add(propertyFissionAmericiumRadiation.getName());
		
		propertyOrderFission.add(propertyFissionCuriumFuelTime.getName());
		propertyOrderFission.add(propertyFissionCuriumHeatGeneration.getName());
		propertyOrderFission.add(propertyFissionCuriumEfficiency.getName());
		propertyOrderFission.add(propertyFissionCuriumCriticality.getName());
		propertyOrderFission.add(propertyFissionCuriumSelfPriming.getName());
		propertyOrderFission.add(propertyFissionCuriumRadiation.getName());
		
		propertyOrderFission.add(propertyFissionBerkeliumFuelTime.getName());
		propertyOrderFission.add(propertyFissionBerkeliumHeatGeneration.getName());
		propertyOrderFission.add(propertyFissionBerkeliumEfficiency.getName());
		propertyOrderFission.add(propertyFissionBerkeliumCriticality.getName());
		propertyOrderFission.add(propertyFissionBerkeliumSelfPriming.getName());
		propertyOrderFission.add(propertyFissionBerkeliumRadiation.getName());
		
		propertyOrderFission.add(propertyFissionCaliforniumFuelTime.getName());
		propertyOrderFission.add(propertyFissionCaliforniumHeatGeneration.getName());
		propertyOrderFission.add(propertyFissionCaliforniumEfficiency.getName());
		propertyOrderFission.add(propertyFissionCaliforniumCriticality.getName());
		propertyOrderFission.add(propertyFissionCaliforniumSelfPriming.getName());
		propertyOrderFission.add(propertyFissionCaliforniumRadiation.getName());
		config.setCategoryPropertyOrder(CATEGORY_FISSION, propertyOrderFission);
		
		List<String> propertyOrderFusion = new ArrayList<>();
		propertyOrderFusion.add(propertyFusionBasePower.getName());
		propertyOrderFusion.add(propertyFusionFuelUse.getName());
		propertyOrderFusion.add(propertyFusionHeatGeneration.getName());
		propertyOrderFusion.add(propertyFusionHeatingMultiplier.getName());
		propertyOrderFusion.add(propertyFusionOverheat.getName());
		propertyOrderFusion.add(propertyFusionMeltdownRadiationMultiplier.getName());
		propertyOrderFusion.add(propertyFusionMinSize.getName());
		propertyOrderFusion.add(propertyFusionMaxSize.getName());
		propertyOrderFusion.add(propertyFusionComparatorMaxEfficiency.getName());
		propertyOrderFusion.add(propertyFusionElectromagnetPower.getName());
		propertyOrderFusion.add(propertyFusionPlasmaCraziness.getName());
		propertyOrderFusion.add(propertyFusionSoundVolume.getName());
		
		propertyOrderFusion.add(propertyFusionFuelTime.getName());
		propertyOrderFusion.add(propertyFusionPower.getName());
		propertyOrderFusion.add(propertyFusionHeatVariable.getName());
		propertyOrderFusion.add(propertyFusionRadiation.getName());
		config.setCategoryPropertyOrder(CATEGORY_FUSION, propertyOrderFusion);
		
		List<String> propertyOrderHeatExchanger = new ArrayList<>();
		propertyOrderHeatExchanger.add(propertyHeatExchangerMinSize.getName());
		propertyOrderHeatExchanger.add(propertyHeatExchangerMaxSize.getName());
		propertyOrderHeatExchanger.add(propertyHeatExchangerConductivity.getName());
		propertyOrderHeatExchanger.add(propertyHeatExchangerCoolantMult.getName());
		propertyOrderHeatExchanger.add(propertyHeatExchangerAlternateExhaustRecipe.getName());
		config.setCategoryPropertyOrder(CATEGORY_HEAT_EXCHANGER, propertyOrderHeatExchanger);
		
		List<String> propertyOrderTurbine = new ArrayList<>();
		propertyOrderTurbine.add(propertyTurbineMinSize.getName());
		propertyOrderTurbine.add(propertyTurbineMaxSize.getName());
		propertyOrderTurbine.add(propertyTurbineBladeEfficiency.getName());
		propertyOrderTurbine.add(propertyTurbineBladeExpansion.getName());
		propertyOrderTurbine.add(propertyTurbineStatorExpansion.getName());
		propertyOrderTurbine.add(propertyTurbineCoilConductivity.getName());
		propertyOrderTurbine.add(propertyTurbinePowerPerMB.getName());
		propertyOrderTurbine.add(propertyTurbineExpansionLevel.getName());
		propertyOrderTurbine.add(propertyTurbineMBPerBlade.getName());
		propertyOrderTurbine.add(propertyTurbineThroughputEfficiencyLeniency.getName());
		propertyOrderTurbine.add(propertyTurbineTensionThroughputFactor.getName());
		propertyOrderTurbine.add(propertyTurbineSoundVolume.getName());
		config.setCategoryPropertyOrder(CATEGORY_TURBINE, propertyOrderTurbine);
		
		List<String> propertyOrderAccelerator = new ArrayList<>();
		propertyOrderAccelerator.add(propertyAcceleratorElectromagnetPower.getName());
		propertyOrderAccelerator.add(propertyAcceleratorSupercoolerCoolant.getName());
		config.setCategoryPropertyOrder(CATEGORY_ACCELERATOR, propertyOrderAccelerator);
		
		List<String> propertyOrderQuantum = new ArrayList<>();
		propertyOrderQuantum.add(propertyQuantumMaxQubits.getName());
		propertyOrderQuantum.add(propertyQuantumAnglePrecision.getName());
		config.setCategoryPropertyOrder(CATEGORY_QUANTUM, propertyOrderQuantum);
		
		List<String> propertyOrderTool = new ArrayList<>();
		propertyOrderTool.add(propertyToolMiningLevel.getName());
		propertyOrderTool.add(propertyToolDurability.getName());
		propertyOrderTool.add(propertyToolSpeed.getName());
		propertyOrderTool.add(propertyToolAttackDamage.getName());
		propertyOrderTool.add(propertyToolEnchantability.getName());
		propertyOrderTool.add(propertyToolHandleModifier.getName());
		config.setCategoryPropertyOrder(CATEGORY_TOOL, propertyOrderTool);
		
		List<String> propertyOrderArmor = new ArrayList<>();
		propertyOrderArmor.add(propertyArmorDurability.getName());
		propertyOrderArmor.add(propertyArmorEnchantability.getName());
		propertyOrderArmor.add(propertyArmorBoron.getName());
		propertyOrderArmor.add(propertyArmorTough.getName());
		propertyOrderArmor.add(propertyArmorHardCarbon.getName());
		propertyOrderArmor.add(propertyArmorBoronNitride.getName());
		propertyOrderArmor.add(propertyArmorHazmat.getName());
		propertyOrderArmor.add(propertyArmorToughness.getName());
		config.setCategoryPropertyOrder(CATEGORY_ARMOR, propertyOrderArmor);
		
		List<String> propertyOrderEntity = new ArrayList<>();
		propertyOrderEntity.add(propertyEntityTrackingRange.getName());
		config.setCategoryPropertyOrder(CATEGORY_ENTITY, propertyOrderEntity);
		
		List<String> propertyOrderRadiation = new ArrayList<>();
		propertyOrderRadiation.add(propertyRadiationEnabled.getName());
		propertyOrderRadiation.add(propertyRadiationWorldChunksPerTick.getName());
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
		propertyOrderRadiation.add(propertyRadiationMaxEntityRads.getName());
		propertyOrderRadiation.add(propertyRadiationEntityDecayRate.getName());
		propertyOrderRadiation.add(propertyRadiationSpreadRate.getName());
		propertyOrderRadiation.add(propertyRadiationSpreadGradient.getName());
		propertyOrderRadiation.add(propertyRadiationDecayRate.getName());
		propertyOrderRadiation.add(propertyRadiationLowestRate.getName());
		propertyOrderRadiation.add(propertyRadiationChunkLimit.getName());
		//propertyOrderRadiation.add(propertyRadiationBlockEffects.getName());
		//propertyOrderRadiation.add(propertyRadiationBlockEffectLimit.getName());
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
		propertyOrderRadiation.add(propertyRadiationScrubberNonLinear.getName());
		propertyOrderRadiation.add(propertyRadiationScrubberParam.getName());
		propertyOrderRadiation.add(propertyRadiationScrubberTime.getName());
		propertyOrderRadiation.add(propertyRadiationScrubberPower.getName());
		propertyOrderRadiation.add(propertyRadiationScrubberEfficiency.getName());
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
		propertyOrderRadiation.add(propertyRadiationPlayerDebuffLists.getName());
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
		
		List<String> propertyOrderRegistration = new ArrayList<>();
		propertyOrderRegistration.add(propertyRegisterProcessor.getName());
		propertyOrderRegistration.add(propertyRegisterPassive.getName());
		propertyOrderRegistration.add(propertyRegisterBattery.getName());
		propertyOrderRegistration.add(propertyRegisterQuantum.getName());
		propertyOrderRegistration.add(propertyRegisterTool.getName());
		propertyOrderRegistration.add(propertyRegisterTiCTool.getName());
		propertyOrderRegistration.add(propertyRegisterArmor.getName());
		propertyOrderRegistration.add(propertyRegisterConarmArmor.getName());
		propertyOrderRegistration.add(propertyRegisterEntity.getName());
		propertyOrderRegistration.add(propertyRegisterFluidBlocks.getName());
		propertyOrderRegistration.add(propertyRegisterCoFHFluids.getName());
		propertyOrderRegistration.add(propertyRegisterProjectEEMC.getName());
		config.setCategoryPropertyOrder(CATEGORY_REGISTRATION, propertyOrderRegistration);
		
		List<String> propertyOrderMisc = new ArrayList<>();
		propertyOrderMisc.add(propertySingleCreativeTab.getName());
		propertyOrderMisc.add(propertyCtrlInfo.getName());
		propertyOrderMisc.add(propertyJEIChanceItemsIncludeNull.getName());
		propertyOrderMisc.add(propertyRareDrops.getName());
		propertyOrderMisc.add(propertyDungeonLoot.getName());
		propertyOrderMisc.add(propertyOreDictRawMaterialRecipes.getName());
		propertyOrderMisc.add(propertyOreDictPriorityBool.getName());
		propertyOrderMisc.add(propertyOreDictPriority.getName());
		config.setCategoryPropertyOrder(CATEGORY_MISC, propertyOrderMisc);
		
		if (setFromConfig) {
			ore_dims = propertyOreDims.getIntList();
			ore_dims_list_type = propertyOreDimsListType.getBoolean();
			ore_gen = readBooleanArrayFromConfig(propertyOreGen);
			ore_size = readIntegerArrayFromConfig(propertyOreSize);
			ore_rate = readIntegerArrayFromConfig(propertyOreRate);
			ore_min_height = readIntegerArrayFromConfig(propertyOreMinHeight);
			ore_max_height = readIntegerArrayFromConfig(propertyOreMaxHeight);
			ore_drops = readBooleanArrayFromConfig(propertyOreDrops);
			ore_hide_disabled = propertyOreHideDisabled.getBoolean();
			ore_harvest_levels = readIntegerArrayFromConfig(propertyOreHarvestLevels);
			
			wasteland_biome = propertyWastelandBiome.getBoolean();
			wasteland_biome_weight = propertyWastelandBiomeWeight.getInt();
			wasteland_dimension_gen = propertyWastelandDimensionGen.getBoolean();
			wasteland_dimension = propertyWastelandDimension.getInt();
			mushroom_spread_rate = propertyMushroomSpreadRate.getInt();
			mushroom_gen = propertyMushroomGen.getBoolean();
			mushroom_gen_size = propertyMushroomGenSize.getInt();
			mushroom_gen_rate = propertyMushroomGenRate.getInt();
			
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
			processor_passive_rate = readDoubleArrayFromConfig(propertyProcessorPassiveRate);
			passive_push = propertyPassivePush.getBoolean();
			cobble_gen_power = propertyCobbleGenPower.getDouble();
			ore_processing = propertyOreProcessing.getBoolean();
			manufactory_wood = readIntegerArrayFromConfig(propertyManufactoryWood);
			rock_crusher_alternate = propertyRockCrusherAlternate.getBoolean();
			gtce_recipe_integration = readBooleanArrayFromConfig(propertyGTCERecipes);
			gtce_recipe_logging = propertyGTCERecipeLogging.getBoolean();
			smart_processor_input = propertySmartProcessorInput.getBoolean();
			passive_permeation = propertyPermeation.getBoolean();
			factor_recipes = propertyFactorRecipes.getBoolean();
			processor_particles = propertyProcessorParticles.getBoolean();
			
			rtg_power = readIntegerArrayFromConfig(propertyRTGPower);
			solar_power = readIntegerArrayFromConfig(propertySolarPower);
			decay_lifetime = readDoubleArrayFromConfig(propertyDecayLifetime);
			decay_power = readDoubleArrayFromConfig(propertyDecayPower);
			
			battery_capacity = readIntegerArrayFromConfig(propertyBatteryCapacity);
			
			fission_fuel_time_multiplier = propertyFissionFuelTimeMultiplier.getDouble();
			fission_source_efficiency = readDoubleArrayFromConfig(propertyFissionSourceEfficiency);
			fission_sink_cooling_rate = readIntegerArrayFromConfig(propertyFissionSinkCoolingRate);
			fission_heater_cooling_rate = readIntegerArrayFromConfig(propertyFissionHeaterCoolingRate);
			fission_moderator_flux_factor = readIntegerArrayFromConfig(propertyFissionModeratorFluxFactor);
			fission_moderator_efficiency = readDoubleArrayFromConfig(propertyFissionModeratorEfficiency);
			fission_reflector_efficiency = readDoubleArrayFromConfig(propertyFissionReflectorEfficiency);
			fission_reflector_reflectivity = readDoubleArrayFromConfig(propertyFissionReflectorReflectivity);
			fission_shield_heat_per_flux = readDoubleArrayFromConfig(propertyFissionShieldHeatPerFlux);
			fission_shield_efficiency = readDoubleArrayFromConfig(propertyFissionShieldEfficiency);
			fission_irradiator_heat_per_flux = readDoubleArrayFromConfig(propertyFissionIrradiatorHeatPerFlux);
			fission_irradiator_efficiency = readDoubleArrayFromConfig(propertyFissionIrradiatorEfficiency);
			fission_cooling_efficiency_leniency = propertyFissionCoolingEfficiencyLeniency.getInt();
			fission_sparsity_penalty_params = readDoubleArrayFromConfig(propertyFissionSparsityPenaltyParams);
			fission_overheat = propertyFissionOverheat.getBoolean();
			fission_explosions = propertyFissionExplosions.getBoolean();
			fission_meltdown_radiation_multiplier = propertyFissionMeltdownRadiationMultiplier.getDouble();
			fission_min_size = propertyFissionMinSize.getInt();
			fission_max_size = propertyFissionMaxSize.getInt();
			fission_comparator_max_temp = propertyFissionComparatorMaxTemp.getInt();
			fission_neutron_reach = propertyFissionNeutronReach.getInt();
			fission_sound_volume = propertyFissionSoundVolume.getDouble();
			
			fission_thorium_fuel_time = readIntegerArrayFromConfig(propertyFissionThoriumFuelTime);
			fission_thorium_heat_generation = readIntegerArrayFromConfig(propertyFissionThoriumHeatGeneration);
			fission_thorium_efficiency = readDoubleArrayFromConfig(propertyFissionThoriumEfficiency);
			fission_thorium_criticality = readIntegerArrayFromConfig(propertyFissionThoriumCriticality);
			fission_thorium_self_priming = readBooleanArrayFromConfig(propertyFissionThoriumSelfPriming);
			fission_thorium_radiation = readDoubleArrayFromConfig(propertyFissionThoriumRadiation);
			
			fission_uranium_fuel_time = readIntegerArrayFromConfig(propertyFissionUraniumFuelTime);
			fission_uranium_heat_generation = readIntegerArrayFromConfig(propertyFissionUraniumHeatGeneration);
			fission_uranium_efficiency = readDoubleArrayFromConfig(propertyFissionUraniumEfficiency);
			fission_uranium_criticality = readIntegerArrayFromConfig(propertyFissionUraniumCriticality);
			fission_uranium_self_priming = readBooleanArrayFromConfig(propertyFissionUraniumSelfPriming);
			fission_uranium_radiation = readDoubleArrayFromConfig(propertyFissionUraniumRadiation);
			
			fission_neptunium_fuel_time = readIntegerArrayFromConfig(propertyFissionNeptuniumFuelTime);
			fission_neptunium_heat_generation = readIntegerArrayFromConfig(propertyFissionNeptuniumHeatGeneration);
			fission_neptunium_efficiency = readDoubleArrayFromConfig(propertyFissionNeptuniumEfficiency);
			fission_neptunium_criticality = readIntegerArrayFromConfig(propertyFissionNeptuniumCriticality);
			fission_neptunium_self_priming = readBooleanArrayFromConfig(propertyFissionNeptuniumSelfPriming);
			fission_neptunium_radiation = readDoubleArrayFromConfig(propertyFissionNeptuniumRadiation);
			
			fission_plutonium_fuel_time = readIntegerArrayFromConfig(propertyFissionPlutoniumFuelTime);
			fission_plutonium_heat_generation = readIntegerArrayFromConfig(propertyFissionPlutoniumHeatGeneration);
			fission_plutonium_efficiency = readDoubleArrayFromConfig(propertyFissionPlutoniumEfficiency);
			fission_plutonium_criticality = readIntegerArrayFromConfig(propertyFissionPlutoniumCriticality);
			fission_plutonium_self_priming = readBooleanArrayFromConfig(propertyFissionPlutoniumSelfPriming);
			fission_plutonium_radiation = readDoubleArrayFromConfig(propertyFissionPlutoniumRadiation);
			
			fission_mixed_fuel_time = readIntegerArrayFromConfig(propertyFissionMixedFuelTime);
			fission_mixed_heat_generation = readIntegerArrayFromConfig(propertyFissionMixedHeatGeneration);
			fission_mixed_efficiency = readDoubleArrayFromConfig(propertyFissionMixedEfficiency);
			fission_mixed_criticality = readIntegerArrayFromConfig(propertyFissionMixedCriticality);
			fission_mixed_self_priming = readBooleanArrayFromConfig(propertyFissionMixedSelfPriming);
			fission_mixed_radiation = readDoubleArrayFromConfig(propertyFissionMixedRadiation);
			
			fission_americium_fuel_time = readIntegerArrayFromConfig(propertyFissionAmericiumFuelTime);
			fission_americium_heat_generation = readIntegerArrayFromConfig(propertyFissionAmericiumHeatGeneration);
			fission_americium_efficiency = readDoubleArrayFromConfig(propertyFissionAmericiumEfficiency);
			fission_americium_criticality = readIntegerArrayFromConfig(propertyFissionAmericiumCriticality);
			fission_americium_self_priming = readBooleanArrayFromConfig(propertyFissionAmericiumSelfPriming);
			fission_americium_radiation = readDoubleArrayFromConfig(propertyFissionAmericiumRadiation);
			
			fission_curium_fuel_time = readIntegerArrayFromConfig(propertyFissionCuriumFuelTime);
			fission_curium_heat_generation = readIntegerArrayFromConfig(propertyFissionCuriumHeatGeneration);
			fission_curium_efficiency = readDoubleArrayFromConfig(propertyFissionCuriumEfficiency);
			fission_curium_criticality = readIntegerArrayFromConfig(propertyFissionCuriumCriticality);
			fission_curium_self_priming = readBooleanArrayFromConfig(propertyFissionCuriumSelfPriming);
			fission_curium_radiation = readDoubleArrayFromConfig(propertyFissionCuriumRadiation);
			
			fission_berkelium_fuel_time = readIntegerArrayFromConfig(propertyFissionBerkeliumFuelTime);
			fission_berkelium_heat_generation = readIntegerArrayFromConfig(propertyFissionBerkeliumHeatGeneration);
			fission_berkelium_efficiency = readDoubleArrayFromConfig(propertyFissionBerkeliumEfficiency);
			fission_berkelium_criticality = readIntegerArrayFromConfig(propertyFissionBerkeliumCriticality);
			fission_berkelium_self_priming = readBooleanArrayFromConfig(propertyFissionBerkeliumSelfPriming);
			fission_berkelium_radiation = readDoubleArrayFromConfig(propertyFissionBerkeliumRadiation);
			
			fission_californium_fuel_time = readIntegerArrayFromConfig(propertyFissionCaliforniumFuelTime);
			fission_californium_heat_generation = readIntegerArrayFromConfig(propertyFissionCaliforniumHeatGeneration);
			fission_californium_efficiency = readDoubleArrayFromConfig(propertyFissionCaliforniumEfficiency);
			fission_californium_criticality = readIntegerArrayFromConfig(propertyFissionCaliforniumCriticality);
			fission_californium_self_priming = readBooleanArrayFromConfig(propertyFissionCaliforniumSelfPriming);
			fission_californium_radiation = readDoubleArrayFromConfig(propertyFissionCaliforniumRadiation);
			
			fusion_base_power = propertyFusionBasePower.getDouble();
			fusion_fuel_use = propertyFusionFuelUse.getDouble();
			fusion_heat_generation = propertyFusionHeatGeneration.getDouble();
			fusion_heating_multiplier = propertyFusionHeatingMultiplier.getDouble();
			fusion_overheat = propertyFusionOverheat.getBoolean();
			fusion_meltdown_radiation_multiplier = propertyFusionMeltdownRadiationMultiplier.getDouble();
			fusion_min_size = propertyFusionMinSize.getInt();
			fusion_max_size = propertyFusionMaxSize.getInt();
			fusion_comparator_max_efficiency = propertyFusionComparatorMaxEfficiency.getInt();
			fusion_electromagnet_power = propertyFusionElectromagnetPower.getDouble();
			fusion_plasma_craziness = propertyFusionPlasmaCraziness.getBoolean();
			fusion_sound_volume = propertyFusionSoundVolume.getDouble();
			
			fusion_fuel_time = readDoubleArrayFromConfig(propertyFusionFuelTime);
			fusion_power = readDoubleArrayFromConfig(propertyFusionPower);
			fusion_heat_variable = readDoubleArrayFromConfig(propertyFusionHeatVariable);
			fusion_radiation = readDoubleArrayFromConfig(propertyFusionRadiation);
			
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
			turbine_throughput_efficiency_leniency = propertyTurbineThroughputEfficiencyLeniency.getDouble();
			turbine_tension_throughput_factor = propertyTurbineTensionThroughputFactor.getDouble();
			turbine_sound_volume = propertyTurbineSoundVolume.getDouble();
			
			accelerator_electromagnet_power = propertyAcceleratorElectromagnetPower.getDouble();
			accelerator_supercooler_coolant = propertyAcceleratorSupercoolerCoolant.getDouble();
			
			quantum_max_qubits = propertyQuantumMaxQubits.getInt();
			quantum_angle_precision = propertyQuantumAnglePrecision.getInt();
					
			tool_mining_level = readIntegerArrayFromConfig(propertyToolMiningLevel);
			tool_durability = readIntegerArrayFromConfig(propertyToolDurability);
			tool_speed = readDoubleArrayFromConfig(propertyToolSpeed);
			tool_attack_damage = readDoubleArrayFromConfig(propertyToolAttackDamage);
			tool_enchantability = readIntegerArrayFromConfig(propertyToolEnchantability);
			tool_handle_modifier = readDoubleArrayFromConfig(propertyToolHandleModifier);
			
			armor_durability = readIntegerArrayFromConfig(propertyArmorDurability);
			armor_enchantability = readIntegerArrayFromConfig(propertyArmorEnchantability);
			armor_boron = readIntegerArrayFromConfig(propertyArmorBoron);
			armor_tough = readIntegerArrayFromConfig(propertyArmorTough);
			armor_hard_carbon = readIntegerArrayFromConfig(propertyArmorHardCarbon);
			armor_boron_nitride = readIntegerArrayFromConfig(propertyArmorBoronNitride);
			armor_hazmat = readIntegerArrayFromConfig(propertyArmorHazmat);
			armor_toughness = readDoubleArrayFromConfig(propertyArmorToughness);
			
			entity_tracking_range = propertyEntityTrackingRange.getInt();
			
			radiation_enabled = propertyRadiationEnabled.getBoolean();
			
			radiation_world_chunks_per_tick = propertyRadiationWorldChunksPerTick.getInt();
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
			max_entity_rads = propertyRadiationMaxEntityRads.getStringList();
			radiation_entity_decay_rate = propertyRadiationEntityDecayRate.getDouble();
			radiation_spread_rate = propertyRadiationSpreadRate.getDouble();
			radiation_spread_gradient = propertyRadiationSpreadGradient.getDouble();
			radiation_decay_rate = propertyRadiationDecayRate.getDouble();
			radiation_lowest_rate = propertyRadiationLowestRate.getDouble();
			radiation_chunk_limit = propertyRadiationChunkLimit.getDouble();
			
			//radiation_block_effects = propertyRadiationBlockEffects.getStringList();
			//radiation_block_effect_limit = propertyRadiationBlockEffectLimit.getDouble();
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
			radiation_scrubber_non_linear = propertyRadiationScrubberNonLinear.getBoolean();
			radiation_scrubber_param = readDoubleArrayFromConfig(propertyRadiationScrubberParam);
			radiation_scrubber_time = readIntegerArrayFromConfig(propertyRadiationScrubberTime);
			radiation_scrubber_power = readIntegerArrayFromConfig(propertyRadiationScrubberPower);
			radiation_scrubber_efficiency = readDoubleArrayFromConfig(propertyRadiationScrubberEfficiency);
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
			
			radiation_player_debuff_lists = propertyRadiationPlayerDebuffLists.getStringList();
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
			
			register_processor = readBooleanArrayFromConfig(propertyRegisterProcessor);
			register_passive = readBooleanArrayFromConfig(propertyRegisterPassive);
			register_battery = readBooleanArrayFromConfig(propertyRegisterBattery);
			register_quantum = propertyRegisterQuantum.getBoolean();
			register_tool = readBooleanArrayFromConfig(propertyRegisterTool);
			register_tic_tool = readBooleanArrayFromConfig(propertyRegisterTiCTool);
			register_armor = readBooleanArrayFromConfig(propertyRegisterArmor);
			register_conarm_armor = readBooleanArrayFromConfig(propertyRegisterConarmArmor);
			register_entity = readBooleanArrayFromConfig(propertyRegisterEntity);
			register_fluid_blocks = propertyRegisterFluidBlocks.getBoolean();
			register_cofh_fluids = propertyRegisterCoFHFluids.getBoolean();
			register_projecte_emc = propertyRegisterProjectEEMC.getBoolean();
			
			single_creative_tab = propertySingleCreativeTab.getBoolean();
			ctrl_info = propertyCtrlInfo.getBoolean();
			jei_chance_items_include_null = propertyJEIChanceItemsIncludeNull.getBoolean();
			rare_drops = propertyRareDrops.getBoolean();
			dungeon_loot = propertyDungeonLoot.getBoolean();
			ore_dict_raw_material_recipes = propertyOreDictRawMaterialRecipes.getBoolean();
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
		propertyOreHideDisabled.set(ore_hide_disabled);
		propertyOreHarvestLevels.set(ore_harvest_levels);
		
		propertyWastelandBiome.set(wasteland_biome);
		propertyWastelandBiomeWeight.set(wasteland_biome_weight);
		propertyWastelandDimensionGen.set(wasteland_dimension_gen);
		propertyWastelandDimension.set(wasteland_dimension);
		propertyMushroomSpreadRate.set(mushroom_spread_rate);
		propertyMushroomGen.set(mushroom_gen);
		propertyMushroomGenSize.set(mushroom_gen_size);
		propertyMushroomGenRate.set(mushroom_gen_rate);
		
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
		propertyPassivePush.set(passive_push);
		propertyCobbleGenPower.set(cobble_gen_power);
		propertyOreProcessing.set(ore_processing);
		propertyManufactoryWood.set(manufactory_wood);
		propertyRockCrusherAlternate.set(rock_crusher_alternate);
		propertyGTCERecipes.set(gtce_recipe_integration);
		propertyGTCERecipeLogging.set(gtce_recipe_logging);
		propertySmartProcessorInput.set(smart_processor_input);
		propertyPermeation.set(passive_permeation);
		propertyFactorRecipes.set(factor_recipes);
		propertyProcessorParticles.set(processor_particles);
		
		propertyRTGPower.set(rtg_power);
		propertySolarPower.set(solar_power);
		propertyDecayLifetime.set(decay_lifetime);
		propertyDecayPower.set(decay_power);
		
		propertyBatteryCapacity.set(battery_capacity);
		
		propertyFissionFuelTimeMultiplier.set(fission_fuel_time_multiplier);
		propertyFissionSourceEfficiency.set(fission_source_efficiency);
		propertyFissionSinkCoolingRate.set(fission_sink_cooling_rate);
		propertyFissionHeaterCoolingRate.set(fission_heater_cooling_rate);
		propertyFissionModeratorFluxFactor.set(fission_moderator_flux_factor);
		propertyFissionModeratorEfficiency.set(fission_moderator_efficiency);
		propertyFissionReflectorEfficiency.set(fission_reflector_efficiency);
		propertyFissionReflectorReflectivity.set(fission_reflector_reflectivity);
		propertyFissionShieldHeatPerFlux.set(fission_shield_heat_per_flux);
		propertyFissionShieldEfficiency.set(fission_shield_efficiency);
		propertyFissionIrradiatorHeatPerFlux.set(fission_irradiator_heat_per_flux);
		propertyFissionIrradiatorEfficiency.set(fission_irradiator_efficiency);
		propertyFissionCoolingEfficiencyLeniency.set(fission_cooling_efficiency_leniency);
		propertyFissionSparsityPenaltyParams.set(fission_sparsity_penalty_params);
		propertyFissionOverheat.set(fission_overheat);
		propertyFissionExplosions.set(fission_explosions);
		propertyFissionMeltdownRadiationMultiplier.set(fission_meltdown_radiation_multiplier);
		propertyFissionMinSize.set(fission_min_size);
		propertyFissionMaxSize.set(fission_max_size);
		propertyFissionComparatorMaxTemp.set(fission_comparator_max_temp);
		propertyFissionNeutronReach.set(fission_neutron_reach);
		propertyFissionSoundVolume.set(fission_sound_volume);
		
		propertyFissionThoriumFuelTime.set(fission_thorium_fuel_time);
		propertyFissionThoriumHeatGeneration.set(fission_thorium_heat_generation);
		propertyFissionThoriumEfficiency.set(fission_thorium_efficiency);
		propertyFissionThoriumCriticality.set(fission_thorium_criticality);
		propertyFissionThoriumSelfPriming.set(fission_thorium_self_priming);
		propertyFissionThoriumRadiation.set(fission_thorium_radiation);
		
		propertyFissionUraniumFuelTime.set(fission_uranium_fuel_time);
		propertyFissionUraniumHeatGeneration.set(fission_uranium_heat_generation);
		propertyFissionUraniumEfficiency.set(fission_uranium_efficiency);
		propertyFissionUraniumCriticality.set(fission_uranium_criticality);
		propertyFissionUraniumSelfPriming.set(fission_uranium_self_priming);
		propertyFissionUraniumRadiation.set(fission_uranium_radiation);
		
		propertyFissionNeptuniumFuelTime.set(fission_neptunium_fuel_time);
		propertyFissionNeptuniumHeatGeneration.set(fission_neptunium_heat_generation);
		propertyFissionNeptuniumEfficiency.set(fission_neptunium_efficiency);
		propertyFissionNeptuniumCriticality.set(fission_neptunium_criticality);
		propertyFissionNeptuniumSelfPriming.set(fission_neptunium_self_priming);
		propertyFissionNeptuniumRadiation.set(fission_neptunium_radiation);
		
		propertyFissionPlutoniumFuelTime.set(fission_plutonium_fuel_time);
		propertyFissionPlutoniumHeatGeneration.set(fission_plutonium_heat_generation);
		propertyFissionPlutoniumEfficiency.set(fission_plutonium_efficiency);
		propertyFissionPlutoniumCriticality.set(fission_plutonium_criticality);
		propertyFissionPlutoniumSelfPriming.set(fission_plutonium_self_priming);
		propertyFissionPlutoniumRadiation.set(fission_plutonium_radiation);
		
		propertyFissionMixedFuelTime.set(fission_mixed_fuel_time);
		propertyFissionMixedHeatGeneration.set(fission_mixed_heat_generation);
		propertyFissionMixedEfficiency.set(fission_mixed_efficiency);
		propertyFissionMixedCriticality.set(fission_mixed_criticality);
		propertyFissionMixedSelfPriming.set(fission_mixed_self_priming);
		propertyFissionMixedRadiation.set(fission_mixed_radiation);
		
		propertyFissionAmericiumFuelTime.set(fission_americium_fuel_time);
		propertyFissionAmericiumHeatGeneration.set(fission_americium_heat_generation);
		propertyFissionAmericiumEfficiency.set(fission_americium_efficiency);
		propertyFissionAmericiumCriticality.set(fission_americium_criticality);
		propertyFissionAmericiumSelfPriming.set(fission_americium_self_priming);
		propertyFissionAmericiumRadiation.set(fission_americium_radiation);
		
		propertyFissionCuriumFuelTime.set(fission_curium_fuel_time);
		propertyFissionCuriumHeatGeneration.set(fission_curium_heat_generation);
		propertyFissionCuriumEfficiency.set(fission_curium_efficiency);
		propertyFissionCuriumCriticality.set(fission_curium_criticality);
		propertyFissionCuriumSelfPriming.set(fission_curium_self_priming);
		propertyFissionCuriumRadiation.set(fission_curium_radiation);
		
		propertyFissionBerkeliumFuelTime.set(fission_berkelium_fuel_time);
		propertyFissionBerkeliumHeatGeneration.set(fission_berkelium_heat_generation);
		propertyFissionBerkeliumEfficiency.set(fission_berkelium_efficiency);
		propertyFissionBerkeliumCriticality.set(fission_berkelium_criticality);
		propertyFissionBerkeliumSelfPriming.set(fission_berkelium_self_priming);
		propertyFissionBerkeliumRadiation.set(fission_berkelium_radiation);
		
		propertyFissionCaliforniumFuelTime.set(fission_californium_fuel_time);
		propertyFissionCaliforniumHeatGeneration.set(fission_californium_heat_generation);
		propertyFissionCaliforniumEfficiency.set(fission_californium_efficiency);
		propertyFissionCaliforniumCriticality.set(fission_californium_criticality);
		propertyFissionCaliforniumSelfPriming.set(fission_californium_self_priming);
		propertyFissionCaliforniumRadiation.set(fission_californium_radiation);
		
		propertyFusionBasePower.set(fusion_base_power);
		propertyFusionFuelUse.set(fusion_fuel_use);
		propertyFusionHeatGeneration.set(fusion_heat_generation);
		propertyFusionHeatingMultiplier.set(fusion_heating_multiplier);
		propertyFusionOverheat.set(fusion_overheat);
		propertyFusionMeltdownRadiationMultiplier.set(fusion_meltdown_radiation_multiplier);
		propertyFusionMinSize.set(fusion_min_size);
		propertyFusionMaxSize.set(fusion_max_size);
		propertyFusionComparatorMaxEfficiency.set(fusion_comparator_max_efficiency);
		propertyFusionElectromagnetPower.set(fusion_electromagnet_power);
		propertyFusionPlasmaCraziness.set(fusion_plasma_craziness);
		propertyFusionSoundVolume.set(fusion_sound_volume);
		
		propertyFusionFuelTime.set(fusion_fuel_time);
		propertyFusionPower.set(fusion_power);
		propertyFusionHeatVariable.set(fusion_heat_variable);
		propertyFusionRadiation.set(fusion_radiation);
		
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
		propertyTurbineThroughputEfficiencyLeniency.set(turbine_throughput_efficiency_leniency);
		propertyTurbineTensionThroughputFactor.set(turbine_tension_throughput_factor);
		propertyTurbineSoundVolume.set(turbine_sound_volume);
		
		propertyAcceleratorElectromagnetPower.set(accelerator_electromagnet_power);
		propertyAcceleratorSupercoolerCoolant.set(accelerator_supercooler_coolant);
		
		propertyQuantumMaxQubits.set(quantum_max_qubits);
		propertyQuantumAnglePrecision.set(quantum_angle_precision);
		
		propertyToolMiningLevel.set(tool_mining_level);
		propertyToolDurability.set(tool_durability);
		propertyToolSpeed.set(tool_speed);
		propertyToolAttackDamage.set(tool_attack_damage);
		propertyToolEnchantability.set(tool_enchantability);
		propertyToolHandleModifier.set(tool_handle_modifier);
		
		propertyArmorDurability.set(armor_durability);
		propertyArmorEnchantability.set(armor_enchantability);
		propertyArmorBoron.set(armor_boron);
		propertyArmorTough.set(armor_tough);
		propertyArmorHardCarbon.set(armor_hard_carbon);
		propertyArmorBoronNitride.set(armor_boron_nitride);
		propertyArmorHazmat.set(armor_hazmat);
		propertyArmorToughness.set(armor_toughness);
		
		propertyEntityTrackingRange.set(entity_tracking_range);
		
		propertyRadiationEnabled.set(radiation_enabled);
		
		propertyRadiationWorldChunksPerTick.set(radiation_world_chunks_per_tick);
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
		propertyRadiationMaxEntityRads.set(max_entity_rads);
		propertyRadiationEntityDecayRate.set(radiation_entity_decay_rate);
		propertyRadiationSpreadRate.set(radiation_spread_rate);
		propertyRadiationSpreadGradient.set(radiation_spread_gradient);
		propertyRadiationDecayRate.set(radiation_decay_rate);
		propertyRadiationLowestRate.set(radiation_lowest_rate);
		propertyRadiationChunkLimit.set(radiation_chunk_limit);
		
		//propertyRadiationBlockEffects.set(radiation_block_effects);
		//propertyRadiationBlockEffectLimit.set(radiation_block_effect_limit);
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
		propertyRadiationScrubberNonLinear.set(radiation_scrubber_non_linear);
		propertyRadiationScrubberParam.set(radiation_scrubber_param);
		propertyRadiationScrubberTime.set(radiation_scrubber_time);
		propertyRadiationScrubberPower.set(radiation_scrubber_power);
		propertyRadiationScrubberEfficiency.set(radiation_scrubber_efficiency);
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
		
		propertyRadiationPlayerDebuffLists.set(radiation_player_debuff_lists);
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
		
		propertyRegisterProcessor.set(register_processor);
		propertyRegisterPassive.set(register_passive);
		propertyRegisterBattery.set(register_battery);
		propertyRegisterQuantum.set(register_quantum);
		propertyRegisterTool.set(register_tool);
		propertyRegisterTiCTool.set(register_tic_tool);
		propertyRegisterArmor.set(register_armor);
		propertyRegisterConarmArmor.set(register_conarm_armor);
		propertyRegisterEntity.set(register_entity);
		propertyRegisterFluidBlocks.set(register_fluid_blocks);
		propertyRegisterCoFHFluids.set(register_cofh_fluids);
		propertyRegisterProjectEEMC.set(register_projecte_emc);
		
		propertySingleCreativeTab.set(single_creative_tab);
		propertyCtrlInfo.set(ctrl_info);
		propertyJEIChanceItemsIncludeNull.set(jei_chance_items_include_null);
		propertyRareDrops.set(rare_drops);
		propertyOreDictRawMaterialRecipes.set(ore_dict_raw_material_recipes);
		propertyOreDictPriorityBool.set(ore_dict_priority_bool);
		propertyOreDictPriority.set(ore_dict_priority);
		
		if (setFromConfig) {
			ProcessorRecipeHandler.initGTCEIntegration();
			
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
		public void onEvent(OnConfigChangedEvent event) {
			if (event.getModID().equals(Global.MOD_ID)) {
				syncFromGui();
			}
		}
	}
}
