package nc.config;

import static nc.util.CollectionHelper.arrayCopies;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import nc.*;
import nc.multiblock.fission.FissionPlacement;
import nc.multiblock.turbine.TurbinePlacement;
import nc.network.PacketHandler;
import nc.network.config.ConfigUpdatePacket;
import nc.radiation.RadSources;
import nc.recipe.BasicRecipeHandler;
import nc.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.*;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.*;
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
	public static final String CATEGORY_QUANTUM = "quantum";
	public static final String CATEGORY_TOOL = "tool";
	public static final String CATEGORY_ARMOR = "armor";
	public static final String CATEGORY_ENTITY = "entity";
	public static final String CATEGORY_RADIATION = "radiation";
	public static final String CATEGORY_REGISTRATION = "registration";
	public static final String CATEGORY_MISC = "misc";
	
	public static final String CATEGORY_OUTPUT = "output";
	
	protected static final Map<String, List<String>> PROPERTY_ORDER_MAP = new HashMap<>();
	
	protected static final boolean LIST = false, ARRAY = true;
	
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
	
	public static double processor_time_multiplier; // Default: 1
	public static double processor_power_multiplier; // Default: 1
	public static int[] processor_time;
	public static int[] processor_power;
	public static double[] speed_upgrade_power_laws_fp;
	public static double[] speed_upgrade_multipliers_fp;
	public static double[] energy_upgrade_power_laws_fp;
	public static double[] energy_upgrade_multipliers_fp;
	public static int[] upgrade_stack_sizes;
	public static int rf_per_eu;
	public static boolean enable_ic2_eu;
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
	
	public static int[] battery_block_capacity;
	public static int[] battery_block_max_transfer;
	public static int[] battery_block_energy_tier;
	public static int[] battery_item_capacity;
	public static int[] battery_item_max_transfer;
	public static int[] battery_item_energy_tier;
	
	public static double fission_fuel_time_multiplier; // Default: 1
	public static double fission_fuel_heat_multiplier; // Default: 1
	public static double fission_fuel_efficiency_multiplier; // Default: 1
	public static double fission_fuel_radiation_multiplier; // Default: 1
	public static double[] fission_source_efficiency;
	public static int[] fission_sink_cooling_rate;
	public static String[] fission_sink_rule;
	public static int[] fission_heater_cooling_rate;
	public static String[] fission_heater_rule;
	public static int[] fission_moderator_flux_factor;
	public static double[] fission_moderator_efficiency;
	public static double[] fission_reflector_efficiency;
	public static double[] fission_reflector_reflectivity;
	public static double[] fission_shield_heat_per_flux;
	public static double[] fission_shield_efficiency;
	public static double[] fission_irradiator_heat_per_flux;
	public static double[] fission_irradiator_efficiency;
	public static int fission_cooling_efficiency_leniency;
	public static double[] fission_sparsity_penalty_params; // Multiplier, threshold
	
	public static boolean fission_decay_mechanics;
	public static double[] fission_decay_build_up_times; // Decay heat, iodine, poison
	public static double[] fission_decay_lifetimes; // Decay heat, iodine, poison
	public static double[] fission_decay_equilibrium_factors; // Decay heat, iodine, poison
	public static double[] fission_decay_daughter_multipliers; // Iodine, poison
	public static double[] fission_decay_term_multipliers; // Exponential, linear
	
	public static boolean fission_overheat;
	public static boolean fission_explosions;
	public static double fission_meltdown_radiation_multiplier;
	public static boolean fission_heat_damage;
	public static int fission_min_size; // Default: 1
	public static int fission_max_size; // Default: 24
	public static int fission_comparator_max_temp;
	public static int fission_neutron_reach;
	public static boolean[] fission_heat_dissipation;
	public static double fission_emergency_cooling_multiplier;
	public static double fission_sound_volume;
	
	public static int[] fission_thorium_fuel_time;
	public static int[] fission_thorium_heat_generation;
	public static double[] fission_thorium_efficiency;
	public static int[] fission_thorium_criticality;
	public static double[] fission_thorium_decay_factor;
	public static boolean[] fission_thorium_self_priming;
	public static double[] fission_thorium_radiation;
	
	public static int[] fission_uranium_fuel_time;
	public static int[] fission_uranium_heat_generation;
	public static double[] fission_uranium_efficiency;
	public static int[] fission_uranium_criticality;
	public static double[] fission_uranium_decay_factor;
	public static boolean[] fission_uranium_self_priming;
	public static double[] fission_uranium_radiation;
	
	public static int[] fission_neptunium_fuel_time;
	public static int[] fission_neptunium_heat_generation;
	public static double[] fission_neptunium_efficiency;
	public static int[] fission_neptunium_criticality;
	public static double[] fission_neptunium_decay_factor;
	public static boolean[] fission_neptunium_self_priming;
	public static double[] fission_neptunium_radiation;
	
	public static int[] fission_plutonium_fuel_time;
	public static int[] fission_plutonium_heat_generation;
	public static double[] fission_plutonium_efficiency;
	public static int[] fission_plutonium_criticality;
	public static double[] fission_plutonium_decay_factor;
	public static boolean[] fission_plutonium_self_priming;
	public static double[] fission_plutonium_radiation;
	
	public static int[] fission_mixed_fuel_time;
	public static int[] fission_mixed_heat_generation;
	public static double[] fission_mixed_efficiency;
	public static int[] fission_mixed_criticality;
	public static double[] fission_mixed_decay_factor;
	public static boolean[] fission_mixed_self_priming;
	public static double[] fission_mixed_radiation;
	
	public static int[] fission_americium_fuel_time;
	public static int[] fission_americium_heat_generation;
	public static double[] fission_americium_efficiency;
	public static int[] fission_americium_criticality;
	public static double[] fission_americium_decay_factor;
	public static boolean[] fission_americium_self_priming;
	public static double[] fission_americium_radiation;
	
	public static int[] fission_curium_fuel_time;
	public static int[] fission_curium_heat_generation;
	public static double[] fission_curium_efficiency;
	public static int[] fission_curium_criticality;
	public static double[] fission_curium_decay_factor;
	public static boolean[] fission_curium_self_priming;
	public static double[] fission_curium_radiation;
	
	public static int[] fission_berkelium_fuel_time;
	public static int[] fission_berkelium_heat_generation;
	public static double[] fission_berkelium_efficiency;
	public static int[] fission_berkelium_criticality;
	public static double[] fission_berkelium_decay_factor;
	public static boolean[] fission_berkelium_self_priming;
	public static double[] fission_berkelium_radiation;
	
	public static int[] fission_californium_fuel_time;
	public static int[] fission_californium_heat_generation;
	public static double[] fission_californium_efficiency;
	public static int[] fission_californium_criticality;
	public static double[] fission_californium_decay_factor;
	public static boolean[] fission_californium_self_priming;
	public static double[] fission_californium_radiation;
	
	public static double fusion_fuel_time_multiplier; // Default: 1
	public static double fusion_fuel_heat_multiplier; // Default: 1
	public static double fusion_fuel_efficiency_multiplier; // Default: 1
	public static double fusion_fuel_radiation_multiplier; // Default: 1
	public static boolean fusion_overheat;
	public static double fusion_meltdown_radiation_multiplier;
	public static int fusion_min_size; // Default: 1
	public static int fusion_max_size; // Default: 24
	public static int fusion_comparator_max_efficiency;
	public static double fusion_electromagnet_power;
	public static boolean fusion_plasma_craziness;
	public static double fusion_sound_volume;
	
	public static double[] fusion_fuel_time;
	public static double[] fusion_fuel_heat_generation;
	public static double[] fusion_fuel_optimal_temperature;
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
	public static String[] turbine_coil_rule;
	public static String[] turbine_connector_rule;
	public static double[] turbine_power_per_mb;
	public static double[] turbine_expansion_level;
	public static double[] turbine_spin_up_multiplier;
	public static int turbine_mb_per_blade;
	public static double[] turbine_throughput_leniency_params;
	public static double turbine_tension_throughput_factor;
	public static double turbine_tension_leniency;
	public static double turbine_power_bonus_multiplier;
	public static double turbine_sound_volume;
	public static double turbine_particles;
	public static double turbine_render_blade_width;
	public static double turbine_render_rotor_expansion;
	public static double turbine_render_rotor_speed;
	
	public static boolean quantum_dedicated_server;
	public static int quantum_max_qubits_live;
	public static int quantum_max_qubits_code;
	public static int quantum_angle_precision;
	
	public static int[] tool_mining_level;
	public static int[] tool_durability;
	public static double[] tool_speed;
	public static double[] tool_attack_damage;
	public static int[] tool_enchantability;
	public static double[] tool_handle_modifier;
	
	public static int[] armor_durability;
	public static double[] armor_toughness;
	public static int[] armor_enchantability;
	public static int[] armor_boron;
	public static int[] armor_tough;
	public static int[] armor_hard_carbon;
	public static int[] armor_boron_nitride;
	public static int[] armor_hazmat;
	
	public static int entity_tracking_range;
	
	private static boolean radiation_enabled;
	public static boolean radiation_enabled_public;
	
	public static String[] radiation_immune_players;
	public static int radiation_world_chunks_per_tick;
	public static int radiation_player_tick_rate;
	public static String[] radiation_worlds;
	public static String[] radiation_biomes;
	public static String[] radiation_structures; // Mineshaft, Village, Fortress, Stronghold, Temple, Monument, EndCity, Mansion
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
	
	public static double[] radiation_sound_volumes;
	public static boolean radiation_check_blocks;
	public static int radiation_block_effect_max_rate;
	public static double radiation_rain_mult;
	public static double radiation_swim_mult;
	
	public static double radiation_feral_ghoul_attack;
	
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
	
	public static String[] radiation_rads_text_color;
	public static String[] radiation_rate_text_color;
	public static String[] radiation_positive_food_rads_text_color;
	public static String[] radiation_negative_food_rads_text_color;
	public static String[] radiation_positive_food_resistance_text_color;
	public static String[] radiation_negative_food_resistance_text_color;
	
	public static String[] radiation_player_debuff_lists;
	public static String[] radiation_passive_debuff_lists;
	public static String[] radiation_mob_buff_lists;
	public static boolean radiation_player_rads_fatal;
	public static boolean radiation_passive_rads_fatal;
	public static boolean radiation_mob_rads_fatal;
	
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
	
	public static boolean give_guidebook;
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
	
	public static int[] corium_solidification;
	public static boolean corium_solidification_list_type;
	
	public static boolean ore_dict_raw_material_recipes;
	public static boolean ore_dict_priority_bool;
	public static String[] ore_dict_priority;
	public static boolean hwyla_enabled;
	
	public static Configuration getConfig() {
		return config;
	}
	
	public static void preInit() {
		config = new Configuration(new File(Loader.instance().getConfigDir(), "nuclearcraft.cfg"));
		syncConfig(true);
		
		MinecraftForge.EVENT_BUS.register(new ServerConfigEventHandler());
	}
	
	public static void postInit() {
		outputInfo();
	}
	
	public static void clientPreInit() {
		MinecraftForge.EVENT_BUS.register(new ClientConfigEventHandler());
	}
	
	protected static void syncConfig(boolean loadFromFile) {
		if (loadFromFile) {
			config.load();
		}
		
		PROPERTY_ORDER_MAP.clear();
		
		PROPERTY_ORDER_MAP.put(CATEGORY_WORLD_GEN, new ArrayList<>());
		PROPERTY_ORDER_MAP.put(CATEGORY_PROCESSOR, new ArrayList<>());
		PROPERTY_ORDER_MAP.put(CATEGORY_GENERATOR, new ArrayList<>());
		PROPERTY_ORDER_MAP.put(CATEGORY_ENERGY_STORAGE, new ArrayList<>());
		PROPERTY_ORDER_MAP.put(CATEGORY_FISSION, new ArrayList<>());
		PROPERTY_ORDER_MAP.put(CATEGORY_FUSION, new ArrayList<>());
		PROPERTY_ORDER_MAP.put(CATEGORY_HEAT_EXCHANGER, new ArrayList<>());
		PROPERTY_ORDER_MAP.put(CATEGORY_TURBINE, new ArrayList<>());
		PROPERTY_ORDER_MAP.put(CATEGORY_QUANTUM, new ArrayList<>());
		PROPERTY_ORDER_MAP.put(CATEGORY_TOOL, new ArrayList<>());
		PROPERTY_ORDER_MAP.put(CATEGORY_ARMOR, new ArrayList<>());
		PROPERTY_ORDER_MAP.put(CATEGORY_ENTITY, new ArrayList<>());
		PROPERTY_ORDER_MAP.put(CATEGORY_RADIATION, new ArrayList<>());
		PROPERTY_ORDER_MAP.put(CATEGORY_REGISTRATION, new ArrayList<>());
		PROPERTY_ORDER_MAP.put(CATEGORY_MISC, new ArrayList<>());
		
		ore_dims = sync(CATEGORY_WORLD_GEN, "ore_dims", new int[] {0, 2, -6, -100, 4598, -9999, -11325}, Integer.MIN_VALUE, Integer.MAX_VALUE, LIST);
		ore_dims_list_type = sync(CATEGORY_WORLD_GEN, "ore_dims_list_type", false);
		ore_gen = sync(CATEGORY_WORLD_GEN, "ore_gen", new boolean[] {true, true, true, true, true, true, true, true}, ARRAY);
		ore_size = sync(CATEGORY_WORLD_GEN, "ore_size", new int[] {6, 6, 6, 3, 3, 4, 4, 5}, 1, Integer.MAX_VALUE, ARRAY);
		ore_rate = sync(CATEGORY_WORLD_GEN, "ore_rate", new int[] {5, 4, 6, 3, 6, 6, 6, 4}, 1, Integer.MAX_VALUE, ARRAY);
		ore_min_height = sync(CATEGORY_WORLD_GEN, "ore_min_height", new int[] {0, 0, 0, 0, 0, 0, 0, 0}, 1, 255, ARRAY);
		ore_max_height = sync(CATEGORY_WORLD_GEN, "ore_max_height", new int[] {48, 40, 36, 32, 32, 28, 28, 24}, 1, 255, ARRAY);
		ore_drops = sync(CATEGORY_WORLD_GEN, "ore_drops", new boolean[] {false, false, false, false, false, false, false}, ARRAY);
		ore_hide_disabled = sync(CATEGORY_WORLD_GEN, "ore_hide_disabled", false);
		ore_harvest_levels = sync(CATEGORY_WORLD_GEN, "ore_harvest_levels", new int[] {1, 1, 1, 2, 2, 2, 2, 2}, 0, 15, ARRAY);
		
		wasteland_biome = sync(CATEGORY_WORLD_GEN, "wasteland_biome", true);
		wasteland_biome_weight = sync(CATEGORY_WORLD_GEN, "wasteland_biome_weight", 5, 0, 255);
		wasteland_dimension_gen = sync(CATEGORY_WORLD_GEN, "wasteland_dimension_gen", true);
		wasteland_dimension = sync(CATEGORY_WORLD_GEN, "wasteland_dimension", 4598, Integer.MIN_VALUE, Integer.MAX_VALUE);
		mushroom_spread_rate = sync(CATEGORY_WORLD_GEN, "mushroom_spread_rate", 16, 0, 511);
		mushroom_gen = sync(CATEGORY_WORLD_GEN, "mushroom_gen", true);
		mushroom_gen_size = sync(CATEGORY_WORLD_GEN, "mushroom_gen_size", 32, 0, 511);
		mushroom_gen_rate = sync(CATEGORY_WORLD_GEN, "mushroom_gen_rate", 64, 0, 511);
		
		processor_time_multiplier = sync(CATEGORY_PROCESSOR, "processor_time_multiplier", 1D, 0.001D, 255D);
		processor_power_multiplier = sync(CATEGORY_PROCESSOR, "processor_power_multiplier", 1D, 0D, 255D);
		processor_time = sync(CATEGORY_PROCESSOR, "processor_time", new int[] {400, 800, 800, 400, 400, 600, 800, 600, 3200, 600, 400, 600, 800, 600, 1600, 600, 2400, 1200, 400}, 1, 128000, ARRAY);
		processor_power = sync(CATEGORY_PROCESSOR, "processor_power", new int[] {20, 10, 10, 20, 10, 10, 40, 20, 40, 10, 0, 40, 10, 20, 10, 10, 10, 10, 20}, 0, 16000, ARRAY);
		speed_upgrade_power_laws_fp = sync(CATEGORY_PROCESSOR, "speed_upgrade_power_laws_fp", new double[] {1D, 2D}, 1D, 15D, ARRAY);
		speed_upgrade_multipliers_fp = sync(CATEGORY_PROCESSOR, "speed_upgrade_multipliers_fp", new double[] {1D, 1D}, 0D, 15D, ARRAY);
		energy_upgrade_power_laws_fp = sync(CATEGORY_PROCESSOR, "energy_upgrade_power_laws_fp", new double[] {1D}, 1D, 15D, ARRAY);
		energy_upgrade_multipliers_fp = sync(CATEGORY_PROCESSOR, "energy_upgrade_multipliers_fp", new double[] {1D}, 0D, 15D, ARRAY);
		upgrade_stack_sizes = sync(CATEGORY_PROCESSOR, "upgrade_stack_sizes", new int[] {64, 64}, 1, 64, ARRAY);
		rf_per_eu = sync(CATEGORY_PROCESSOR, "rf_per_eu", 16, 1, 65536);
		enable_ic2_eu = sync(CATEGORY_PROCESSOR, "enable_ic2_eu", true);
		enable_gtce_eu = sync(CATEGORY_PROCESSOR, "enable_gtce_eu", true);
		enable_mek_gas = sync(CATEGORY_PROCESSOR, "enable_mek_gas", true);
		machine_update_rate = sync(CATEGORY_PROCESSOR, "machine_update_rate", 20, 1, 1200);
		processor_passive_rate = sync(CATEGORY_PROCESSOR, "processor_passive_rate", new double[] {0.125, 10, 5}, 0D, 4000D, ARRAY);
		passive_push = sync(CATEGORY_PROCESSOR, "passive_push", true);
		cobble_gen_power = sync(CATEGORY_PROCESSOR, "cobble_gen_power", 0D, 0D, 255D);
		ore_processing = sync(CATEGORY_PROCESSOR, "ore_processing", true);
		manufactory_wood = sync(CATEGORY_PROCESSOR, "manufactory_wood", new int[] {6, 4}, 1, 64, ARRAY);
		rock_crusher_alternate = sync(CATEGORY_PROCESSOR, "rock_crusher_alternate", false);
		gtce_recipe_integration = sync(CATEGORY_PROCESSOR, "gtce_recipe_integration", new boolean[] {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true}, ARRAY);
		gtce_recipe_logging = sync(CATEGORY_PROCESSOR, "gtce_recipe_logging", false);
		smart_processor_input = sync(CATEGORY_PROCESSOR, "smart_processor_input", true);
		passive_permeation = sync(CATEGORY_PROCESSOR, "passive_permeation", true);
		factor_recipes = sync(CATEGORY_PROCESSOR, "factor_recipes", false);
		processor_particles = sync(CATEGORY_PROCESSOR, "processor_particles", true);
		
		rtg_power = sync(CATEGORY_GENERATOR, "rtg_power", new int[] {1, 40, 10, 200}, 1, Integer.MAX_VALUE, ARRAY);
		solar_power = sync(CATEGORY_GENERATOR, "solar_power", new int[] {5, 20, 80, 320}, 1, Integer.MAX_VALUE, ARRAY);
		decay_lifetime = sync(CATEGORY_GENERATOR, "decay_lifetime", new double[] {12000D / 0.75D, 12000D / 1.2D, 1200D, 12000D / 2.2D, 12000D / 3D, 12000D / 18D, 12000D / 28D, 12000D / 80D, 12000D / 1000D}, 1D, 16777215D, ARRAY);
		decay_power = sync(CATEGORY_GENERATOR, "decay_power", new double[] {0.75D, 1.2D, 1D, 2.2D, 3D, 18D, 28D, 80D, 1000D}, 0D, 32767D, ARRAY);
		
		battery_block_capacity = sync(CATEGORY_ENERGY_STORAGE, "battery_block_capacity", new int[] {1600000, 6400000, 25600000, 102400000, 32000000, 128000000, 512000000, 2048000000}, 1, Integer.MAX_VALUE, ARRAY);
		battery_block_max_transfer = sync(CATEGORY_ENERGY_STORAGE, "battery_block_max_transfer", new int[] {16000, 64000, 256000, 1024000, 320000, 1280000, 5120000, 20480000}, 1, Integer.MAX_VALUE, ARRAY);
		battery_block_energy_tier = sync(CATEGORY_ENERGY_STORAGE, "battery_block_energy_tier", new int[] {1, 2, 3, 4, 3, 4, 5, 6}, 1, 10, ARRAY);
		battery_item_capacity = sync(CATEGORY_ENERGY_STORAGE, "battery_item_capacity", new int[] {8000000}, 1, Integer.MAX_VALUE, ARRAY);
		battery_item_max_transfer = sync(CATEGORY_ENERGY_STORAGE, "battery_item_max_transfer", new int[] {80000}, 1, Integer.MAX_VALUE, ARRAY);
		battery_item_energy_tier = sync(CATEGORY_ENERGY_STORAGE, "battery_item_energy_tier", new int[] {3}, 1, 10, ARRAY);
		
		fission_fuel_time_multiplier = sync(CATEGORY_FISSION, "fission_fuel_time_multiplier", 1D, 0.001D, 255D);
		fission_fuel_heat_multiplier = sync(CATEGORY_FISSION, "fission_fuel_heat_multiplier", 1D, 0D, 255D);
		fission_fuel_efficiency_multiplier = sync(CATEGORY_FISSION, "fission_fuel_efficiency_multiplier", 1D, 0D, 255D);
		fission_fuel_radiation_multiplier = sync(CATEGORY_FISSION, "fission_fuel_radiation_multiplier", 1D, 0D, 255D);
		fission_source_efficiency = sync(CATEGORY_FISSION, "fission_source_efficiency", new double[] {0.9D, 0.95D, 1D}, 0D, 255D, ARRAY);
		fission_sink_cooling_rate = sync(CATEGORY_FISSION, "fission_sink_cooling_rate", new int[] {55, 50, 85, 80, 70, 105, 90, 100, 110, 115, 145, 65, 95, 200, 195, 75, 120, 60, 160, 130, 125, 150, 175, 170, 165, 180, 140, 135, 185, 190, 155, 205}, 0, 32767, ARRAY);
		fission_sink_rule = sync(CATEGORY_FISSION, "fission_sink_rule", new String[] {"one cell", "one moderator", "one cell && one moderator", "one redstone sink", "two axial glowstone sinks", "one obsidian sink", "two moderators", "one cell && one casing", "exactly two iron sinks", "two water sinks", "exactly one water sink && two lead sinks", "one reflector", "one reflector && one iron sink", "one cell && one gold sink", "one moderator && one prismarine sink", "one water sink", "two axial lapis sinks", "one iron sink", "exactly one quartz sink && one casing", "exactly two axial lead sinks && one casing", "exactly one moderator && one casing", "two cells", "one quartz sink && one lapis sink", "two glowstone sinks && one tin sink", "one gold sink && one prismarine sink", "one redstone sink && one end_stone sink", "one end_stone sink && one copper sink", "two axial reflectors", "two copper sinks && one purpur sink", "exactly two redstone sinks", "three moderators", "three cells"}, ARRAY);
		fission_heater_cooling_rate = sync(CATEGORY_FISSION, "fission_heater_cooling_rate", new int[] {55, 50, 85, 80, 70, 105, 90, 100, 110, 115, 145, 65, 95, 200, 195, 75, 120, 60, 160, 130, 125, 150, 175, 170, 165, 180, 140, 135, 185, 190, 155, 205}, 0, 32767, ARRAY);
		fission_heater_rule = sync(CATEGORY_FISSION, "fission_heater_rule", new String[] {"one vessel", "one moderator", "one vessel && one moderator", "one redstone heater", "two axial glowstone heaters", "one obsidian heater", "two moderators", "one vessel && one casing", "exactly two iron heaters", "two standard heaters", "exactly one standard heater && two lead heaters", "one reflector", "one reflector && one iron heater", "one vessel && one gold heater", "one moderator && one prismarine heater", "one standard heater", "two axial lapis heaters", "one iron heater", "exactly one quartz heater && one casing", "exactly two axial lead heaters && one casing", "exactly one moderator && one casing", "two vessels", "one quartz heater && one lapis heater", "two glowstone heaters && one tin heater", "one gold heater && one prismarine heater", "one redstone heater && one end_stone heater", "one end_stone heater && one copper heater", "two axial reflectors", "two copper heaters && one purpur heater", "exactly two redstone heaters", "three moderators", "three vessels"}, ARRAY);
		fission_moderator_flux_factor = sync(CATEGORY_FISSION, "fission_moderator_flux_factor", new int[] {10, 22, 36}, 0, 32767, ARRAY);
		fission_moderator_efficiency = sync(CATEGORY_FISSION, "fission_moderator_efficiency", new double[] {1.1D, 1.05D, 1D}, 0D, 255D, ARRAY);
		fission_reflector_efficiency = sync(CATEGORY_FISSION, "fission_reflector_efficiency", new double[] {0.5D, 0.25D}, 0D, 255D, ARRAY);
		fission_reflector_reflectivity = sync(CATEGORY_FISSION, "fission_reflector_reflectivity", new double[] {1D, 0.5D}, 0D, 1D, ARRAY);
		fission_shield_heat_per_flux = sync(CATEGORY_FISSION, "fission_shield_heat_per_flux", new double[] {5D}, 0D, 32767D, ARRAY);
		fission_shield_efficiency = sync(CATEGORY_FISSION, "fission_shield_efficiency", new double[] {0.5D}, 0D, 255D, ARRAY);
		fission_irradiator_heat_per_flux = sync(CATEGORY_FISSION, "fission_irradiator_heat_per_flux", new double[] {0D, 0D, 0D}, 0D, 32767D, ARRAY);
		fission_irradiator_efficiency = sync(CATEGORY_FISSION, "fission_irradiator_efficiency", new double[] {0D, 0D, 0.5D}, 0D, 32767D, ARRAY);
		fission_cooling_efficiency_leniency = sync(CATEGORY_FISSION, "fission_cooling_efficiency_leniency", 10, 0, 32767);
		fission_sparsity_penalty_params = sync(CATEGORY_FISSION, "fission_sparsity_penalty_params", new double[] {0.5D, 0.75D}, 0D, 1D, ARRAY);
		fission_decay_mechanics = sync(CATEGORY_FISSION, "fission_decay_mechanics", false);
		fission_decay_build_up_times = sync(CATEGORY_FISSION, "fission_decay_build_up_times", new double[] {24000D, 24000D, 24000D}, 0D, Integer.MAX_VALUE, ARRAY);
		fission_decay_lifetimes = sync(CATEGORY_FISSION, "fission_decay_lifetimes", new double[] {6000D, 8000D, 12000D}, 0D, Integer.MAX_VALUE, ARRAY);
		fission_decay_equilibrium_factors = sync(CATEGORY_FISSION, "fission_decay_equilibrium_factors", new double[] {1D, 5D, 1D}, 0D, 255D, ARRAY);
		fission_decay_daughter_multipliers = sync(CATEGORY_FISSION, "fission_decay_daughter_multipliers", new double[] {5D, 5D}, 0D, 255D, ARRAY);
		fission_decay_term_multipliers = sync(CATEGORY_FISSION, "fission_decay_term_multipliers", new double[] {0.95D, 0.05D}, 0D, 1D, ARRAY);
		fission_overheat = sync(CATEGORY_FISSION, "fission_overheat", true);
		fission_explosions = sync(CATEGORY_FISSION, "fission_explosions", false);
		fission_meltdown_radiation_multiplier = sync(CATEGORY_FISSION, "fission_meltdown_radiation_multiplier", 1D, 0D, 255D);
		fission_heat_damage = sync(CATEGORY_FISSION, "fission_heat_damage", false);
		fission_min_size = sync(CATEGORY_FISSION, "fission_min_size", 1, 1, 255);
		fission_max_size = sync(CATEGORY_FISSION, "fission_max_size", 24, 3, 255);
		fission_comparator_max_temp = sync(CATEGORY_FISSION, "fission_comparator_max_temp", 1600, 20, 2400);
		fission_neutron_reach = sync(CATEGORY_FISSION, "fission_neutron_reach", 4, 1, 255);
		fission_heat_dissipation = sync(CATEGORY_FISSION, "fission_heat_dissipation", new boolean[] {true, false}, ARRAY);
		fission_emergency_cooling_multiplier = sync(CATEGORY_FISSION, "fission_emergency_cooling_multiplier", 1D, 0.001D, 255D);
		fission_sound_volume = sync(CATEGORY_FISSION, "fission_sound_volume", 1D, 0D, 15D);
		
		fission_thorium_fuel_time = sync(CATEGORY_FISSION, "fission_thorium_fuel_time", new int[] {14400, 14400, 18000, 11520, 18000}, 1, Integer.MAX_VALUE, ARRAY);
		fission_thorium_heat_generation = sync(CATEGORY_FISSION, "fission_thorium_heat_generation", new int[] {40, 40, 32, 50, 32}, 0, 32767, ARRAY);
		fission_thorium_efficiency = sync(CATEGORY_FISSION, "fission_thorium_efficiency", new double[] {1.25D, 1.25D, 1.25D, 1.25D, 1.25D}, 0D, 32767D, ARRAY);
		fission_thorium_criticality = sync(CATEGORY_FISSION, "fission_thorium_criticality", new int[] {199, 234, 293, 199, 234}, 0, 32767, ARRAY);
		fission_thorium_decay_factor = sync(CATEGORY_FISSION, "fission_thorium_decay_factor", arrayCopies(5, 0.04D), 0D, 1D, ARRAY);
		fission_thorium_self_priming = sync(CATEGORY_FISSION, "fission_thorium_self_priming", new boolean[] {false, false, false, false, false}, ARRAY);
		fission_thorium_radiation = sync(CATEGORY_FISSION, "fission_thorium_radiation", new double[] {RadSources.TBU_FISSION, RadSources.TBU_FISSION, RadSources.TBU_FISSION, RadSources.TBU_FISSION, RadSources.TBU_FISSION}, 0D, 1000D, ARRAY);
		
		fission_uranium_fuel_time = sync(CATEGORY_FISSION, "fission_uranium_fuel_time", new int[] {2666, 2666, 3348, 2134, 3348, 2666, 2666, 3348, 2134, 3348, 4800, 4800, 6000, 3840, 6000, 4800, 4800, 6000, 3840, 6000}, 1, Integer.MAX_VALUE, ARRAY);
		fission_uranium_heat_generation = sync(CATEGORY_FISSION, "fission_uranium_heat_generation", new int[] {216, 216, 172, 270, 172, 216 * 3, 216 * 3, 172 * 3, 270 * 3, 172 * 3, 120, 120, 96, 150, 96, 120 * 3, 120 * 3, 96 * 3, 150 * 3, 96 * 3}, 0, 32767, ARRAY);
		fission_uranium_efficiency = sync(CATEGORY_FISSION, "fission_uranium_efficiency", new double[] {1.1D, 1.1D, 1.1D, 1.1D, 1.1D, 1.15D, 1.15D, 1.15D, 1.15D, 1.15D, 1D, 1D, 1D, 1D, 1D, 1.05D, 1.05D, 1.05D, 1.05D, 1.05D}, 0D, 32767D, ARRAY);
		fission_uranium_criticality = sync(CATEGORY_FISSION, "fission_uranium_criticality", new int[] {66, 78, 98, 66, 78, 66 / 2, 78 / 2, 98 / 2, 66 / 2, 78 / 2, 87, 102, 128, 87, 102, 87 / 2, 102 / 2, 128 / 2, 87 / 2, 102 / 2}, 0, 32767, ARRAY);
		fission_uranium_decay_factor = sync(CATEGORY_FISSION, "fission_uranium_decay_factor", arrayCopies(20, 0.065D), 0D, 1D, ARRAY);
		fission_uranium_self_priming = sync(CATEGORY_FISSION, "fission_uranium_self_priming", new boolean[] {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false}, ARRAY);
		fission_uranium_radiation = sync(CATEGORY_FISSION, "fission_uranium_radiation", new double[] {RadSources.LEU_233_FISSION, RadSources.LEU_233_FISSION, RadSources.LEU_233_FISSION, RadSources.LEU_233_FISSION, RadSources.LEU_233_FISSION, RadSources.HEU_233_FISSION, RadSources.HEU_233_FISSION, RadSources.HEU_233_FISSION, RadSources.HEU_233_FISSION, RadSources.HEU_233_FISSION, RadSources.LEU_235_FISSION, RadSources.LEU_235_FISSION, RadSources.LEU_235_FISSION, RadSources.LEU_235_FISSION, RadSources.LEU_235_FISSION, RadSources.HEU_235_FISSION, RadSources.HEU_235_FISSION, RadSources.HEU_235_FISSION, RadSources.HEU_235_FISSION, RadSources.HEU_235_FISSION}, 0D, 1000D, ARRAY);
		
		fission_neptunium_fuel_time = sync(CATEGORY_FISSION, "fission_neptunium_fuel_time", new int[] {1972, 1972, 2462, 1574, 2462, 1972, 1972, 2462, 1574, 2462}, 1, Integer.MAX_VALUE, ARRAY);
		fission_neptunium_heat_generation = sync(CATEGORY_FISSION, "fission_neptunium_heat_generation", new int[] {292, 292, 234, 366, 234, 292 * 3, 292 * 3, 234 * 3, 366 * 3, 234 * 3}, 0, 32767, ARRAY);
		fission_neptunium_efficiency = sync(CATEGORY_FISSION, "fission_neptunium_efficiency", new double[] {1.1D, 1.1D, 1.1D, 1.1D, 1.1D, 1.15D, 1.15D, 1.15D, 1.15D, 1.15D}, 0D, 32767D, ARRAY);
		fission_neptunium_criticality = sync(CATEGORY_FISSION, "fission_neptunium_criticality", new int[] {60, 70, 88, 60, 70, 60 / 2, 70 / 2, 88 / 2, 60 / 2, 70 / 2}, 0, 32767, ARRAY);
		fission_neptunium_decay_factor = sync(CATEGORY_FISSION, "fission_neptunium_decay_factor", arrayCopies(10, 0.07D), 0D, 1D, ARRAY);
		fission_neptunium_self_priming = sync(CATEGORY_FISSION, "fission_neptunium_self_priming", new boolean[] {false, false, false, false, false, false, false, false, false, false}, ARRAY);
		fission_neptunium_radiation = sync(CATEGORY_FISSION, "fission_neptunium_radiation", new double[] {RadSources.LEN_236_FISSION, RadSources.LEN_236_FISSION, RadSources.LEN_236_FISSION, RadSources.LEN_236_FISSION, RadSources.LEN_236_FISSION, RadSources.HEN_236_FISSION, RadSources.HEN_236_FISSION, RadSources.HEN_236_FISSION, RadSources.HEN_236_FISSION, RadSources.HEN_236_FISSION}, 0D, 1000D, ARRAY);
		
		fission_plutonium_fuel_time = sync(CATEGORY_FISSION, "fission_plutonium_fuel_time", new int[] {4572, 4572, 5760, 3646, 5760, 4572, 4572, 5760, 3646, 5760, 3164, 3164, 3946, 2526, 3946, 3164, 3164, 3946, 2526, 3946}, 1, Integer.MAX_VALUE, ARRAY);
		fission_plutonium_heat_generation = sync(CATEGORY_FISSION, "fission_plutonium_heat_generation", new int[] {126, 126, 100, 158, 100, 126 * 3, 126 * 3, 100 * 3, 158 * 3, 100 * 3, 182, 182, 146, 228, 146, 182 * 3, 182 * 3, 146 * 3, 228 * 3, 146 * 3}, 0, 32767, ARRAY);
		fission_plutonium_efficiency = sync(CATEGORY_FISSION, "fission_plutonium_efficiency", new double[] {1.2D, 1.2D, 1.2D, 1.2D, 1.2D, 1.25D, 1.25D, 1.25D, 1.25D, 1.25D, 1.25D, 1.25D, 1.25D, 1.25D, 1.25D, 1.3D, 1.3D, 1.3D, 1.3D, 1.3D}, 0D, 32767D, ARRAY);
		fission_plutonium_criticality = sync(CATEGORY_FISSION, "fission_plutonium_criticality", new int[] {84, 99, 124, 84, 99, 84 / 2, 99 / 2, 124 / 2, 84 / 2, 99 / 2, 71, 84, 105, 71, 84, 71 / 2, 84 / 2, 105 / 2, 71 / 2, 84 / 2}, 0, 32767, ARRAY);
		fission_plutonium_decay_factor = sync(CATEGORY_FISSION, "fission_plutonium_decay_factor", arrayCopies(20, 0.075D), 0D, 1D, ARRAY);
		fission_plutonium_self_priming = sync(CATEGORY_FISSION, "fission_plutonium_self_priming", new boolean[] {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false}, ARRAY);
		fission_plutonium_radiation = sync(CATEGORY_FISSION, "fission_plutonium_radiation", new double[] {RadSources.LEP_239_FISSION, RadSources.LEP_239_FISSION, RadSources.LEP_239_FISSION, RadSources.LEP_239_FISSION, RadSources.LEP_239_FISSION, RadSources.HEP_239_FISSION, RadSources.HEP_239_FISSION, RadSources.HEP_239_FISSION, RadSources.HEP_239_FISSION, RadSources.HEP_239_FISSION, RadSources.LEP_241_FISSION, RadSources.LEP_241_FISSION, RadSources.LEP_241_FISSION, RadSources.LEP_241_FISSION, RadSources.LEP_241_FISSION, RadSources.HEP_241_FISSION, RadSources.HEP_241_FISSION, RadSources.HEP_241_FISSION, RadSources.HEP_241_FISSION, RadSources.HEP_241_FISSION}, 0D, 1000D, ARRAY);
		
		fission_mixed_fuel_time = sync(CATEGORY_FISSION, "fission_mixed_fuel_time", new int[] {4354, 4354, 5486, 3472, 5486, 3014, 3014, 3758, 2406, 3758}, 1, Integer.MAX_VALUE, ARRAY);
		fission_mixed_heat_generation = sync(CATEGORY_FISSION, "fission_mixed_heat_generation", new int[] {132, 132, 106, 166, 106, 192, 192, 154, 240, 154}, 0, 32767, ARRAY);
		fission_mixed_efficiency = sync(CATEGORY_FISSION, "fission_mixed_efficiency", new double[] {1.05D, 1.05D, 1.05D, 1.05D, 1.05D, 1.15D, 1.15D, 1.15D, 1.15D, 1.15D}, 0D, 32767D, ARRAY);
		fission_mixed_criticality = sync(CATEGORY_FISSION, "fission_mixed_criticality", new int[] {80, 94, 118, 80, 94, 68, 80, 100, 68, 80}, 0, 32767, ARRAY);
		fission_mixed_decay_factor = sync(CATEGORY_FISSION, "fission_mixed_decay_factor", arrayCopies(10, 0.075D), 0D, 1D, ARRAY);
		fission_mixed_self_priming = sync(CATEGORY_FISSION, "fission_mixed_self_priming", new boolean[] {false, false, false, false, false, false, false, false, false, false}, ARRAY);
		fission_mixed_radiation = sync(CATEGORY_FISSION, "fission_mixed_radiation", new double[] {RadSources.MIX_239_FISSION, RadSources.MIX_239_FISSION, RadSources.MIX_239_FISSION, RadSources.MIX_239_FISSION, RadSources.MIX_239_FISSION, RadSources.MIX_241_FISSION, RadSources.MIX_241_FISSION, RadSources.MIX_241_FISSION, RadSources.MIX_241_FISSION, RadSources.MIX_241_FISSION}, 0D, 1000D, ARRAY);
		
		fission_americium_fuel_time = sync(CATEGORY_FISSION, "fission_americium_fuel_time", new int[] {1476, 1476, 1846, 1180, 1846, 1476, 1476, 1846, 1180, 1846}, 1, Integer.MAX_VALUE, ARRAY);
		fission_americium_heat_generation = sync(CATEGORY_FISSION, "fission_americium_heat_generation", new int[] {390, 390, 312, 488, 312, 390 * 3, 390 * 3, 312 * 3, 488 * 3, 312 * 3}, 0, 32767, ARRAY);
		fission_americium_efficiency = sync(CATEGORY_FISSION, "fission_americium_efficiency", new double[] {1.35D, 1.35D, 1.35D, 1.35D, 1.35D, 1.4D, 1.4D, 1.4D, 1.4D, 1.4D}, 0D, 32767D, ARRAY);
		fission_americium_criticality = sync(CATEGORY_FISSION, "fission_americium_criticality", new int[] {55, 65, 81, 55, 65, 55 / 2, 65 / 2, 81 / 2, 55 / 2, 65 / 2}, 0, 32767, ARRAY);
		fission_americium_decay_factor = sync(CATEGORY_FISSION, "fission_americium_decay_factor", arrayCopies(10, 0.08D), 0D, 1D, ARRAY);
		fission_americium_self_priming = sync(CATEGORY_FISSION, "fission_americium_self_priming", new boolean[] {false, false, false, false, false, false, false, false, false, false}, ARRAY);
		fission_americium_radiation = sync(CATEGORY_FISSION, "fission_americium_radiation", new double[] {RadSources.LEA_242_FISSION, RadSources.LEA_242_FISSION, RadSources.LEA_242_FISSION, RadSources.LEA_242_FISSION, RadSources.LEA_242_FISSION, RadSources.HEA_242_FISSION, RadSources.HEA_242_FISSION, RadSources.HEA_242_FISSION, RadSources.HEA_242_FISSION, RadSources.HEA_242_FISSION}, 0D, 1000D, ARRAY);
		
		fission_curium_fuel_time = sync(CATEGORY_FISSION, "fission_curium_fuel_time", new int[] {1500, 1500, 1870, 1200, 1870, 1500, 1500, 1870, 1200, 1870, 2420, 2420, 3032, 1932, 3032, 2420, 2420, 3032, 1932, 3032, 2150, 2150, 2692, 1714, 2692, 2150, 2150, 2692, 1714, 2692}, 1, Integer.MAX_VALUE, ARRAY);
		fission_curium_heat_generation = sync(CATEGORY_FISSION, "fission_curium_heat_generation", new int[] {384, 384, 308, 480, 308, 384 * 3, 384 * 3, 308 * 3, 480 * 3, 308 * 3, 238, 238, 190, 298, 190, 238 * 3, 238 * 3, 190 * 3, 298 * 3, 190 * 3, 268, 268, 214, 336, 214, 268 * 3, 268 * 3, 214 * 3, 336 * 3, 214 * 3}, 0, 32767, ARRAY);
		fission_curium_efficiency = sync(CATEGORY_FISSION, "fission_curium_efficiency", new double[] {1.45D, 1.45D, 1.45D, 1.45D, 1.45D, 1.5D, 1.5D, 1.5D, 1.5D, 1.5D, 1.5D, 1.5D, 1.5D, 1.5D, 1.5D, 1.55D, 1.55D, 1.55D, 1.55D, 1.55D, 1.55D, 1.55D, 1.55D, 1.55D, 1.55D, 1.6D, 1.6D, 1.6D, 1.6D, 1.6D}, 0D, 32767D, ARRAY);
		fission_curium_criticality = sync(CATEGORY_FISSION, "fission_curium_criticality", new int[] {56, 66, 83, 56, 66, 56 / 2, 66 / 2, 83 / 2, 56 / 2, 66 / 2, 64, 75, 94, 64, 75, 64 / 2, 75 / 2, 94 / 2, 64 / 2, 75 / 2, 61, 72, 90, 61, 72, 61 / 2, 72 / 2, 90 / 2, 61 / 2, 72 / 2}, 0, 32767, ARRAY);
		fission_curium_decay_factor = sync(CATEGORY_FISSION, "fission_curium_decay_factor", arrayCopies(30, 0.085D), 0D, 1D, ARRAY);
		fission_curium_self_priming = sync(CATEGORY_FISSION, "fission_curium_self_priming", new boolean[] {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false}, ARRAY);
		fission_curium_radiation = sync(CATEGORY_FISSION, "fission_curium_radiation", new double[] {RadSources.LECm_243_FISSION, RadSources.LECm_243_FISSION, RadSources.LECm_243_FISSION, RadSources.LECm_243_FISSION, RadSources.LECm_243_FISSION, RadSources.HECm_243_FISSION, RadSources.HECm_243_FISSION, RadSources.HECm_243_FISSION, RadSources.HECm_243_FISSION, RadSources.HECm_243_FISSION, RadSources.LECm_245_FISSION, RadSources.LECm_245_FISSION, RadSources.LECm_245_FISSION, RadSources.LECm_245_FISSION, RadSources.LECm_245_FISSION, RadSources.HECm_245_FISSION, RadSources.HECm_245_FISSION, RadSources.HECm_245_FISSION, RadSources.HECm_245_FISSION, RadSources.HECm_245_FISSION, RadSources.LECm_247_FISSION, RadSources.LECm_247_FISSION, RadSources.LECm_247_FISSION, RadSources.LECm_247_FISSION, RadSources.LECm_247_FISSION, RadSources.HECm_247_FISSION, RadSources.HECm_247_FISSION, RadSources.HECm_247_FISSION, RadSources.HECm_247_FISSION, RadSources.HECm_247_FISSION}, 0D, 1000D, ARRAY);
		
		fission_berkelium_fuel_time = sync(CATEGORY_FISSION, "fission_berkelium_fuel_time", new int[] {2166, 2166, 2716, 1734, 2716, 2166, 2166, 2716, 1734, 2716}, 1, Integer.MAX_VALUE, ARRAY);
		fission_berkelium_heat_generation = sync(CATEGORY_FISSION, "fission_berkelium_heat_generation", new int[] {266, 266, 212, 332, 212, 266 * 3, 266 * 3, 212 * 3, 332 * 3, 212 * 3}, 0, 32767, ARRAY);
		fission_berkelium_efficiency = sync(CATEGORY_FISSION, "fission_berkelium_efficiency", new double[] {1.65D, 1.65D, 1.65D, 1.65D, 1.65D, 1.7D, 1.7D, 1.7D, 1.7D, 1.7D}, 0D, 32767D, ARRAY);
		fission_berkelium_criticality = sync(CATEGORY_FISSION, "fission_berkelium_criticality", new int[] {62, 73, 91, 62, 73, 62 / 2, 73 / 2, 91 / 2, 62 / 2, 73 / 2}, 0, 32767, ARRAY);
		fission_berkelium_decay_factor = sync(CATEGORY_FISSION, "fission_berkelium_decay_factor", arrayCopies(10, 0.09D), 0D, 1D, ARRAY);
		fission_berkelium_self_priming = sync(CATEGORY_FISSION, "fission_berkelium_self_priming", new boolean[] {false, false, false, false, false, false, false, false, false, false}, ARRAY);
		fission_berkelium_radiation = sync(CATEGORY_FISSION, "fission_berkelium_radiation", new double[] {RadSources.LEB_248_FISSION, RadSources.LEB_248_FISSION, RadSources.LEB_248_FISSION, RadSources.LEB_248_FISSION, RadSources.LEB_248_FISSION, RadSources.HEB_248_FISSION, RadSources.HEB_248_FISSION, RadSources.HEB_248_FISSION, RadSources.HEB_248_FISSION, RadSources.HEB_248_FISSION}, 0D, 1000D, ARRAY);
		
		fission_californium_fuel_time = sync(CATEGORY_FISSION, "fission_californium_fuel_time", new int[] {1066, 1066, 1334, 852, 1334, 1066, 1066, 1334, 852, 1334, 2000, 2000, 2504, 1600, 2504, 2000, 2000, 2504, 1600, 2504}, 1, Integer.MAX_VALUE, ARRAY);
		fission_californium_heat_generation = sync(CATEGORY_FISSION, "fission_californium_heat_generation", new int[] {540, 540, 432, 676, 432, 540 * 3, 540 * 3, 432 * 3, 676 * 3, 432 * 3, 288, 288, 230, 360, 230, 288 * 3, 288 * 3, 230 * 3, 360 * 3, 230 * 3}, 0, 32767, ARRAY);
		fission_californium_efficiency = sync(CATEGORY_FISSION, "fission_californium_efficiency", new double[] {1.75D, 1.75D, 1.75D, 1.75D, 1.75D, 1.8D, 1.8D, 1.8D, 1.8D, 1.8D, 1.8D, 1.8D, 1.8D, 1.8D, 1.8D, 1.85D, 1.85D, 1.85D, 1.85D, 1.85D}, 0D, 32767D, ARRAY);
		fission_californium_criticality = sync(CATEGORY_FISSION, "fission_californium_criticality", new int[] {51, 60, 75, 51, 60, 51 / 2, 60 / 2, 75 / 2, 51 / 2, 60 / 2, 60, 71, 89, 60, 71, 60 / 2, 71 / 2, 89 / 2, 60 / 2, 71 / 2}, 0, 32767, ARRAY);
		fission_californium_decay_factor = sync(CATEGORY_FISSION, "fission_californium_decay_factor", arrayCopies(20, 0.1D), 0D, 1D, ARRAY);
		fission_californium_self_priming = sync(CATEGORY_FISSION, "fission_californium_self_priming", new boolean[] {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true}, ARRAY);
		fission_californium_radiation = sync(CATEGORY_FISSION, "fission_californium_radiation", new double[] {RadSources.LECf_249_FISSION, RadSources.LECf_249_FISSION, RadSources.LECf_249_FISSION, RadSources.LECf_249_FISSION, RadSources.LECf_249_FISSION, RadSources.HECf_249_FISSION, RadSources.HECf_249_FISSION, RadSources.HECf_249_FISSION, RadSources.HECf_249_FISSION, RadSources.HECf_249_FISSION, RadSources.LECf_251_FISSION, RadSources.LECf_251_FISSION, RadSources.LECf_251_FISSION, RadSources.LECf_251_FISSION, RadSources.LECf_251_FISSION, RadSources.HECf_251_FISSION, RadSources.HECf_251_FISSION, RadSources.HECf_251_FISSION, RadSources.HECf_251_FISSION, RadSources.HECf_251_FISSION}, 0D, 1000D, ARRAY);
		
		fusion_fuel_time_multiplier = sync(CATEGORY_FUSION, "fusion_fuel_time_multiplier", 1D, 0.001D, 255D);
		fusion_fuel_heat_multiplier = sync(CATEGORY_FUSION, "fusion_fuel_heat_multiplier", 1D, 0D, 255D);
		fusion_fuel_efficiency_multiplier = sync(CATEGORY_FUSION, "fusion_fuel_efficiency_multiplier", 1D, 0D, 255D);
		fusion_fuel_radiation_multiplier = sync(CATEGORY_FUSION, "fusion_fuel_radiation_multiplier", 1D, 0D, 255D);
		fusion_overheat = sync(CATEGORY_FUSION, "fusion_overheat", true);
		fusion_meltdown_radiation_multiplier = sync(CATEGORY_FUSION, "fusion_meltdown_radiation_multiplier", 1D, 0D, 255D);
		fusion_min_size = sync(CATEGORY_FUSION, "fusion_min_size", 1, 1, 255);
		fusion_max_size = sync(CATEGORY_FUSION, "fusion_max_size", 24, 1, 255);
		fusion_comparator_max_efficiency = sync(CATEGORY_FUSION, "fusion_comparator_max_efficiency", 90, 1, 100);
		fusion_electromagnet_power = sync(CATEGORY_FUSION, "fusion_electromagnet_power", 250D, 0D, Integer.MAX_VALUE);
		fusion_plasma_craziness = sync(CATEGORY_FUSION, "fusion_plasma_craziness", true);
		fusion_sound_volume = sync(CATEGORY_FUSION, "fusion_sound_volume", 1D, 0D, 15D);
		
		fusion_fuel_time = sync(CATEGORY_FUSION, "fusion_fuel_time", new double[] {100D, 150D, 200D, 200D, 350D, 400D, 600D, 200D, 250D, 250D, 400D, 450D, 650D, 300D, 300D, 450D, 500D, 700D, 300D, 450D, 500D, 700D, 600D, 650D, 850D, 700D, 900D, 1100D}, 1D, 32767D, ARRAY);
		fusion_fuel_heat_generation = sync(CATEGORY_FUSION, "fusion_fuel_heat_generation", new double[] {44200D, 112300D, 30D, 303600D, 35100D, 133000D, 44400D, 50700D, 172600D, 225200D, 171600D, 85900D, 26100D, 90100D, 109900D, 91500D, 43500D, 700D, 131500D, 115100D, 72700D, 14000D, 106800D, 55200D, 15700D, 22900D, 45D, 5D}, 0D, Integer.MAX_VALUE, ARRAY);
		// TODO: multiply by R
		fusion_fuel_optimal_temperature = sync(CATEGORY_FUSION, "fusion_fuel_optimal_temperature", new double[] {3635D, 1022D, 4964D, 2740D, 5972D, 4161D, 13432D, 949D, 670D, 2160D, 3954D, 4131D, 13853D, 736D, 2137D, 4079D, 4522D, 27254D, 5420D, 7800D, 7937D, 24266D, 11268D, 11927D, 30399D, 13630D, 166414D, 293984D}, 500D, 20000D, ARRAY);
		fusion_radiation = sync(CATEGORY_FUSION, "fusion_radiation", new double[] {RadSources.FUSION / 64D, RadSources.FUSION / 64D, (RadSources.FUSION + RadSources.TRITIUM + RadSources.NEUTRON) / 64D, RadSources.FUSION / 64D, (RadSources.FUSION + RadSources.TRITIUM) / 64D, RadSources.FUSION / 64D, RadSources.FUSION / 64D, (RadSources.FUSION + RadSources.TRITIUM / 2D + RadSources.NEUTRON / 2D) / 64D, (RadSources.FUSION + RadSources.TRITIUM + RadSources.NEUTRON) / 64D, RadSources.FUSION / 64D, RadSources.FUSION / 64D, (RadSources.FUSION + RadSources.NEUTRON) / 64D, (RadSources.FUSION + RadSources.NEUTRON) / 64D, (RadSources.FUSION + 2 * RadSources.TRITIUM + 2 * RadSources.NEUTRON) / 64D, (RadSources.FUSION + RadSources.NEUTRON) / 64D, (RadSources.FUSION + RadSources.NEUTRON) / 64D, (RadSources.FUSION + 2 * RadSources.NEUTRON) / 64D, (RadSources.FUSION + 2 * RadSources.NEUTRON) / 64D, RadSources.FUSION / 64D, RadSources.FUSION / 64D, RadSources.FUSION / 64D, RadSources.FUSION / 64D, RadSources.FUSION / 64D, (RadSources.FUSION + RadSources.NEUTRON) / 64D, (RadSources.FUSION + RadSources.NEUTRON) / 64D, (RadSources.FUSION + 2 * RadSources.NEUTRON) / 64D, (RadSources.FUSION + 2 * RadSources.NEUTRON) / 64D, (RadSources.FUSION + 2 * RadSources.NEUTRON) / 64D}, 0D, 1000D, ARRAY);
		
		heat_exchanger_min_size = sync(CATEGORY_HEAT_EXCHANGER, "heat_exchanger_min_size", 1, 1, 255);
		heat_exchanger_max_size = sync(CATEGORY_HEAT_EXCHANGER, "heat_exchanger_max_size", 24, 2, 255);
		heat_exchanger_conductivity = sync(CATEGORY_HEAT_EXCHANGER, "heat_exchanger_conductivity", new double[] {1D, 1.1D, 1.2D}, 0.01D, 15D, ARRAY);
		heat_exchanger_coolant_mult = sync(CATEGORY_HEAT_EXCHANGER, "heat_exchanger_coolant_mult", 5D, 0.001D, Integer.MAX_VALUE);
		heat_exchanger_alternate_exhaust_recipe = sync(CATEGORY_HEAT_EXCHANGER, "heat_exchanger_alternate_exhaust_recipe", false);
		
		turbine_min_size = sync(CATEGORY_TURBINE, "turbine_min_size", 1, 1, 255);
		turbine_max_size = sync(CATEGORY_TURBINE, "turbine_max_size", 24, 3, 255);
		turbine_blade_efficiency = sync(CATEGORY_TURBINE, "turbine_blade_efficiency", new double[] {1D, 1.1D, 1.2D}, 0.01D, 15D, ARRAY);
		turbine_blade_expansion = sync(CATEGORY_TURBINE, "turbine_blade_expansion", new double[] {1.4D, 1.6D, 1.8D}, 1D, 15D, ARRAY);
		turbine_stator_expansion = sync(CATEGORY_TURBINE, "turbine_stator_expansion", 0.75D, 0.01D, 1D);
		turbine_coil_conductivity = sync(CATEGORY_TURBINE, "turbine_coil_conductivity", new double[] {0.88D, 0.9D, 1D, 1.04D, 1.06D, 1.12D}, 0.01D, 15D, ARRAY);
		turbine_coil_rule = sync(CATEGORY_TURBINE, "turbine_coil_rule", new String[] {"one bearing || one connector", "one magnesium coils", "two magnesium coil", "one aluminum coil", "one beryllium coil", "one gold coil && one copper coil"}, ARRAY);
		turbine_connector_rule = sync(CATEGORY_TURBINE, "turbine_connector_rule", new String[] {"one of any coil"}, ARRAY);
		turbine_power_per_mb = sync(CATEGORY_TURBINE, "turbine_power_per_mb", new double[] {16D, 4D, 4D}, 0D, 255D, ARRAY);
		turbine_expansion_level = sync(CATEGORY_TURBINE, "turbine_expansion_level", new double[] {4D, 2D, 2D}, 1D, 255D, ARRAY);
		turbine_spin_up_multiplier = sync(CATEGORY_TURBINE, "turbine_spin_up_multiplier", new double[] {1D, 1D, 1D}, 0D, 255D, ARRAY);
		turbine_mb_per_blade = sync(CATEGORY_TURBINE, "turbine_mb_per_blade", 100, 1, 32767);
		turbine_throughput_leniency_params = sync(CATEGORY_TURBINE, "turbine_throughput_leniency_params", new double[] {0.5D, 0.75D}, 0D, 1D, ARRAY);
		turbine_tension_throughput_factor = sync(CATEGORY_TURBINE, "turbine_tension_throughput_factor", 2D, 1D, 255D);
		turbine_tension_leniency = sync(CATEGORY_TURBINE, "turbine_tension_leniency", 0.05D, 0D, 1D);
		turbine_power_bonus_multiplier = sync(CATEGORY_TURBINE, "turbine_power_bonus_multiplier", 1D, 0D, 255D);
		turbine_sound_volume = sync(CATEGORY_TURBINE, "turbine_sound_volume", 1D, 0D, 15D);
		turbine_particles = sync(CATEGORY_TURBINE, "turbine_particles", 0.025D, 0D, 1D);
		turbine_render_blade_width = sync(CATEGORY_TURBINE, "turbine_render_blade_width", NCMath.SQRT2, 0.01D, 4D);
		turbine_render_rotor_expansion = sync(CATEGORY_TURBINE, "turbine_render_rotor_expansion", 4D, 1D, 15D);
		turbine_render_rotor_speed = sync(CATEGORY_TURBINE, "turbine_render_rotor_speed", 1D, 0D, 15D);
		
		quantum_dedicated_server = sync(CATEGORY_QUANTUM, "quantum_dedicated_server", false);
		quantum_max_qubits_live = sync(CATEGORY_QUANTUM, "quantum_max_qubits_live", 7, 1, 14);
		quantum_max_qubits_code = sync(CATEGORY_QUANTUM, "quantum_max_qubits_code", 16, 1, 32);
		quantum_angle_precision = sync(CATEGORY_QUANTUM, "quantum_angle_precision", 16, 4, 1024);
		
		tool_mining_level = sync(CATEGORY_TOOL, "tool_mining_level", new int[] {2, 2, 3, 3, 3, 3, 4, 4}, 0, 15, ARRAY);
		tool_durability = sync(CATEGORY_TOOL, "tool_durability", new int[] {547, 547 * 5, 929, 929 * 5, 1245, 1245 * 5, 1928, 1928 * 5}, 1, 32767, ARRAY);
		tool_speed = sync(CATEGORY_TOOL, "tool_speed", new double[] {8D, 8D, 10D, 10D, 11D, 11D, 12D, 12D}, 1D, 255D, ARRAY);
		tool_attack_damage = sync(CATEGORY_TOOL, "tool_attack_damage", new double[] {2.5D, 2.5D, 3D, 3D, 3D, 3D, 3.5D, 3.5D}, 0D, 255D, ARRAY);
		tool_enchantability = sync(CATEGORY_TOOL, "tool_enchantability", new int[] {6, 6, 15, 15, 12, 12, 20, 20}, 1, 255, ARRAY);
		tool_handle_modifier = sync(CATEGORY_TOOL, "tool_handle_modifier", new double[] {0.85D, 1.1D, 1D, 0.75D}, 0.01D, 10D, ARRAY);
		
		armor_durability = sync(CATEGORY_ARMOR, "armor_durability", new int[] {22, 30, 34, 42, 0}, 1, 127, ARRAY);
		armor_toughness = sync(CATEGORY_ARMOR, "armor_toughness", new double[] {1D, 2D, 1D, 2D, 0D}, 0D, 8D, ARRAY);
		armor_enchantability = sync(CATEGORY_ARMOR, "armor_enchantability", new int[] {6, 15, 12, 20, 5}, 1, 255, ARRAY);
		armor_boron = sync(CATEGORY_ARMOR, "armor_boron", new int[] {2, 5, 7, 3}, 1, 25, ARRAY);
		armor_tough = sync(CATEGORY_ARMOR, "armor_tough", new int[] {3, 6, 7, 3}, 1, 25, ARRAY);
		armor_hard_carbon = sync(CATEGORY_ARMOR, "armor_hard_carbon", new int[] {3, 5, 7, 3}, 1, 25, ARRAY);
		armor_boron_nitride = sync(CATEGORY_ARMOR, "armor_boron_nitride", new int[] {3, 6, 8, 3}, 1, 25, ARRAY);
		armor_hazmat = sync(CATEGORY_ARMOR, "armor_hazmat", new int[] {3, 6, 7, 3}, 1, 25, ARRAY);
		
		entity_tracking_range = sync(CATEGORY_ENTITY, "entity_tracking_range", 64, 1, 255);
		
		radiation_enabled = sync(CATEGORY_RADIATION, "radiation_enabled", true);
		
		radiation_immune_players = sync(CATEGORY_RADIATION, "radiation_immune_players", new String[] {}, LIST);
		radiation_world_chunks_per_tick = sync(CATEGORY_RADIATION, "radiation_world_chunks_per_tick", 5, 1, 400);
		radiation_player_tick_rate = sync(CATEGORY_RADIATION, "radiation_player_tick_rate", 5, 1, 400);
		radiation_worlds = sync(CATEGORY_RADIATION, "radiation_worlds", new String[] {"4598_2.25"}, LIST);
		radiation_biomes = sync(CATEGORY_RADIATION, "radiation_biomes", new String[] {"nuclearcraft:nuclear_wasteland_0.25"}, LIST);
		radiation_structures = sync(CATEGORY_RADIATION, "radiation_structures", new String[] {}, LIST);
		radiation_world_limits = sync(CATEGORY_RADIATION, "radiation_world_limits", new String[] {}, LIST);
		radiation_biome_limits = sync(CATEGORY_RADIATION, "radiation_biome_limits", new String[] {}, LIST);
		radiation_from_biomes_dims_blacklist = sync(CATEGORY_RADIATION, "radiation_from_biomes_dims_blacklist", new int[] {144}, Integer.MIN_VALUE, Integer.MAX_VALUE, LIST);
		
		radiation_ores = sync(CATEGORY_RADIATION, "radiation_ores", new String[] {"depletedFuelIC2U_" + (RadSources.URANIUM_238 * 4D + RadSources.PLUTONIUM_239 / 9D), "depletedFuelIC2MOX_" + RadSources.PLUTONIUM_239 * 28D / 9D}, LIST);
		radiation_items = sync(CATEGORY_RADIATION, "radiation_items", new String[] {"ic2:nuclear:0_0.000000000048108553", "ic2:nuclear:1_" + RadSources.URANIUM_235, "ic2:nuclear:2_" + RadSources.URANIUM_238, "ic2:nuclear:3_" + RadSources.PLUTONIUM_239, "ic2:nuclear:4_0.000000833741517857143", "ic2:nuclear:5_" + RadSources.URANIUM_235 / 9D, "ic2:nuclear:6_" + RadSources.URANIUM_238 / 9D, "ic2:nuclear:7_" + RadSources.PLUTONIUM_239 / 9D, "ic2:nuclear:8_0.000000000048108553", "ic2:nuclear:9_0.000000833741517857143", "ic2:nuclear:10_" + RadSources.PLUTONIUM_238 * 3D, "ic2:nuclear:11_" + (RadSources.URANIUM_238 * 4D + RadSources.PLUTONIUM_239 / 9D), "ic2:nuclear:12_" + (RadSources.URANIUM_238 * 4D + RadSources.PLUTONIUM_239 / 9D) * 2D, "ic2:nuclear:13_" + (RadSources.URANIUM_238 * 4D + RadSources.PLUTONIUM_239 / 9D) * 4D, "ic2:nuclear:14_" + RadSources.PLUTONIUM_239 * 28D / 9D, "ic2:nuclear:15_" + RadSources.PLUTONIUM_239 * 2D * 28D / 9D, "ic2:nuclear:16_" + RadSources.PLUTONIUM_239 * 4D * 28D / 9D}, LIST);
		radiation_blocks = sync(CATEGORY_RADIATION, "radiation_blocks", new String[] {}, LIST);
		radiation_fluids = sync(CATEGORY_RADIATION, "radiation_fluids", new String[] {}, LIST);
		radiation_foods = sync(CATEGORY_RADIATION, "radiation_foods", new String[] {"minecraft:golden_apple:0_-20_0.1", "minecraft:golden_apple:1_-100_0.5", "minecraft:golden_carrot:0_-4_0", "minecraft:spider_eye:0_0_0.5", "minecraft:poisonous_potato:0_0_0.5", "minecraft:fish:3_0_2", "minecraft:rabbit_stew:0_0_0.1", "minecraft:chorus_fruit:0_0_-0.25", "minecraft:beetroot:0_0_0.25", "minecraft:beetroot_soup:0_0_1.5", "quark:golden_frog_leg:0_-4_0"}, LIST);
		radiation_ores_blacklist = sync(CATEGORY_RADIATION, "radiation_ores_blacklist", new String[] {}, LIST);
		radiation_items_blacklist = sync(CATEGORY_RADIATION, "radiation_items_blacklist", new String[] {}, LIST);
		radiation_blocks_blacklist = sync(CATEGORY_RADIATION, "radiation_blocks_blacklist", new String[] {}, LIST);
		radiation_fluids_blacklist = sync(CATEGORY_RADIATION, "radiation_fluids_blacklist", new String[] {}, LIST);
		
		max_player_rads = sync(CATEGORY_RADIATION, "max_player_rads", 1000D, 1D, 1000000000D);
		radiation_player_decay_rate = sync(CATEGORY_RADIATION, "radiation_player_decay_rate", 0.0000005D, 0D, 1D);
		max_entity_rads = sync(CATEGORY_RADIATION, "max_entity_rads", new String[] {}, LIST);
		radiation_entity_decay_rate = sync(CATEGORY_RADIATION, "radiation_entity_decay_rate", 0.001D, 0D, 1D);
		radiation_spread_rate = sync(CATEGORY_RADIATION, "radiation_spread_rate", 0.1D, 0D, 1D);
		radiation_spread_gradient = sync(CATEGORY_RADIATION, "radiation_spread_gradient", 0.5D, 1D, 1000000000D);
		radiation_decay_rate = sync(CATEGORY_RADIATION, "radiation_decay_rate", 0.001D, 0D, 1D);
		radiation_lowest_rate = sync(CATEGORY_RADIATION, "radiation_lowest_rate", 0.000000000000001D, 0D, 1D);
		radiation_chunk_limit = sync(CATEGORY_RADIATION, "radiation_chunk_limit", -1D, -1D, Double.MAX_VALUE);
		
		radiation_sound_volumes = sync(CATEGORY_RADIATION, "radiation_sound_volumes", new double[] {1D, 1D, 1D, 1D, 1D, 1D, 1D, 1D}, 0D, 15D, ARRAY);
		radiation_check_blocks = sync(CATEGORY_RADIATION, "radiation_check_blocks", true);
		radiation_block_effect_max_rate = sync(CATEGORY_RADIATION, "radiation_block_effect_max_rate", 0, 0, 15);
		radiation_rain_mult = sync(CATEGORY_RADIATION, "radiation_rain_mult", 1D, 0.000001D, 1000000D);
		radiation_swim_mult = sync(CATEGORY_RADIATION, "radiation_swim_mult", 2D, 0.000001D, 1000000D);
		
		radiation_feral_ghoul_attack = sync(CATEGORY_RADIATION, "radiation_feral_ghoul_attack", RadSources.CAESIUM_137, 0.000001D, 1000000D);
		
		radiation_radaway_amount = sync(CATEGORY_RADIATION, "radiation_radaway_amount", 300D, 0.001D, 1000000000D);
		radiation_radaway_slow_amount = sync(CATEGORY_RADIATION, "radiation_radaway_slow_amount", 300D, 0.001D, 1000000000D);
		radiation_radaway_rate = sync(CATEGORY_RADIATION, "radiation_radaway_rate", 5D, 0.001D, 1000000000D);
		radiation_radaway_slow_rate = sync(CATEGORY_RADIATION, "radiation_radaway_slow_rate", 0.025D, 0.00001D, 10000000D);
		radiation_poison_time = sync(CATEGORY_RADIATION, "radiation_poison_time", 60D, 1D, 1000000D);
		radiation_radaway_cooldown = sync(CATEGORY_RADIATION, "radiation_radaway_cooldown", 0D, 0D, 100000D);
		radiation_rad_x_amount = sync(CATEGORY_RADIATION, "radiation_rad_x_amount", 25D, 0.001D, 1000000000D);
		radiation_rad_x_lifetime = sync(CATEGORY_RADIATION, "radiation_rad_x_lifetime", 12000D, 20D, 1000000000D);
		radiation_rad_x_cooldown = sync(CATEGORY_RADIATION, "radiation_rad_x_cooldown", 0D, 0D, 100000D);
		radiation_shielding_level = sync(CATEGORY_RADIATION, "radiation_shielding_level", new double[] {0.01D, 0.1D, 1D}, 0.000000000000000001D, 1000D, ARRAY);
		radiation_tile_shielding = sync(CATEGORY_RADIATION, "radiation_tile_shielding", true);
		radiation_scrubber_fraction = sync(CATEGORY_RADIATION, "radiation_scrubber_fraction", 0.125D, 0.001D, 1D);
		radiation_scrubber_radius = sync(CATEGORY_RADIATION, "radiation_scrubber_radius", 4, 1, 10);
		radiation_scrubber_non_linear = sync(CATEGORY_RADIATION, "radiation_scrubber_non_linear", true);
		radiation_scrubber_param = sync(CATEGORY_RADIATION, "radiation_scrubber_param", new double[] {2.14280951676725D, 3D, 4D, 2D}, 1D, 15D, ARRAY);
		radiation_scrubber_time = sync(CATEGORY_RADIATION, "radiation_scrubber_time", new int[] {12000, 2400, 96000}, 1, Integer.MAX_VALUE, ARRAY);
		radiation_scrubber_power = sync(CATEGORY_RADIATION, "radiation_scrubber_power", new int[] {200, 40, 20}, 0, Integer.MAX_VALUE, ARRAY);
		radiation_scrubber_efficiency = sync(CATEGORY_RADIATION, "radiation_scrubber_efficiency", new double[] {1D, 5D, 0.25D}, 0D, 255D, ARRAY);
		radiation_geiger_block_redstone = sync(CATEGORY_RADIATION, "radiation_geiger_block_redstone", 3D, -127D, 127D);
		
		radiation_shielding_default_recipes = sync(CATEGORY_RADIATION, "radiation_shielding_default_recipes", true);
		radiation_shielding_item_blacklist = sync(CATEGORY_RADIATION, "radiation_shielding_item_blacklist", new String[] {"nuclearcraft:helm_hazmat", "nuclearcraft:chest_hazmat", "nuclearcraft:legs_hazmat", "nuclearcraft:boots_hazmat", "ic2:hazmat_helmet", "ic2:hazmat_chestplate", "ic2:hazmat_leggings", "ic2:rubber_boots", "extraplanets:tier1_space_suit_helmet", "extraplanets:tier1_space_suit_chest", "extraplanets:tier1_space_suit_jetpack_chest", "extraplanets:tier1_space_suit_leggings", "extraplanets:tier1_space_suit_boots", "extraplanets:tier1_space_suit_gravity_boots", "extraplanets:tier2_space_suit_helmet", "extraplanets:tier2_space_suit_chest", "extraplanets:tier2_space_suit_jetpack_chest", "extraplanets:tier2_space_suit_leggings", "extraplanets:tier2_space_suit_boots", "extraplanets:tier2_space_suit_gravity_boots", "extraplanets:tier3_space_suit_helmet", "extraplanets:tier3_space_suit_chest", "extraplanets:tier3_space_suit_jetpack_chest", "extraplanets:tier3_space_suit_leggings", "extraplanets:tier3_space_suit_boots", "extraplanets:tier3_space_suit_gravity_boots", "extraplanets:tier4_space_suit_helmet", "extraplanets:tier4_space_suit_chest", "extraplanets:tier4_space_suit_jetpack_chest", "extraplanets:tier4_space_suit_leggings", "extraplanets:tier4_space_suit_boots", "extraplanets:tier4_space_suit_gravity_boots"}, LIST);
		radiation_shielding_custom_stacks = sync(CATEGORY_RADIATION, "radiation_shielding_custom_stacks", new String[] {}, LIST);
		radiation_shielding_default_levels = sync(CATEGORY_RADIATION, "radiation_shielding_default_levels", new String[] {"nuclearcraft:helm_hazmat_2.0", "nuclearcraft:chest_hazmat_3.0", "nuclearcraft:legs_hazmat_2.0", "nuclearcraft:boots_hazmat_2.0", "ic2:hazmat_helmet_2.0", "ic2:hazmat_chestplate_3.0", "ic2:hazmat_leggings_2.0", "ic2:rubber_boots_2.0", "ic2:quantum_helmet_2.0", "ic2:quantum_chestplate_3.0", "ic2:quantum_leggings_2.0", "ic2:quantum_boots_2.0", "gravisuite:gravichestplate_3.0", "ic2:itemarmorquantumhelmet_2.0", "ic2:itemarmorquantumchestplate_3.0", "ic2:itemarmorquantumlegs_2.0", "ic2:itemarmorquantumboots_2.0", "gravisuit:gravisuit_3.0", "gravisuit:nucleargravisuit_3.0", "extraplanets:tier1_space_suit_helmet_1.0", "extraplanets:tier1_space_suit_chest_1.5", "extraplanets:tier1_space_suit_jetpack_chest_1.5", "extraplanets:tier1_space_suit_leggings_1.0", "extraplanets:tier1_space_suit_boots_1.0", "extraplanets:tier1_space_suit_gravity_boots_1.0", "extraplanets:tier2_space_suit_helmet_1.3", "extraplanets:tier2_space_suit_chest_1.95", "extraplanets:tier2_space_suit_jetpack_chest_1.95", "extraplanets:tier2_space_suit_leggings_1.3", "extraplanets:tier2_space_suit_boots_1.3", "extraplanets:tier2_space_suit_gravity_boots_1.3", "extraplanets:tier3_space_suit_helmet_1.6", "extraplanets:tier3_space_suit_chest_2.4", "extraplanets:tier3_space_suit_jetpack_chest_2.4", "extraplanets:tier3_space_suit_leggings_1.6", "extraplanets:tier3_space_suit_boots_1.6", "extraplanets:tier3_space_suit_gravity_boots_1.6", "extraplanets:tier4_space_suit_helmet_2.0", "extraplanets:tier4_space_suit_chest_3.0", "extraplanets:tier4_space_suit_jetpack_chest_3.0", "extraplanets:tier4_space_suit_leggings_2.0", "extraplanets:tier4_space_suit_boots_2.0", "extraplanets:tier4_space_suit_gravity_boots_2.0"}, LIST);
		
		radiation_hardcore_stacks = sync(CATEGORY_RADIATION, "radiation_hardcore_stacks", true);
		radiation_hardcore_containers = sync(CATEGORY_RADIATION, "radiation_hardcore_containers", 0D, 0D, 1D);
		radiation_dropped_items = sync(CATEGORY_RADIATION, "radiation_dropped_items", true);
		radiation_death_persist = sync(CATEGORY_RADIATION, "radiation_death_persist", true);
		radiation_death_persist_fraction = sync(CATEGORY_RADIATION, "radiation_death_persist_fraction", 0.75D, 0D, 1D);
		radiation_death_immunity_time = sync(CATEGORY_RADIATION, "radiation_death_immunity_time", 90D, 0D, 3600D);
		
		// TODO
		radiation_rads_text_color = sync(CATEGORY_RADIATION, "radiation_rads_text_color", new String[] {"0.0_f", "30.0_e", "50.0_6", "70.0_c", "90.0_4"}, LIST);
		radiation_rate_text_color = sync(CATEGORY_RADIATION, "radiation_rate_text_color", new String[] {"0.0_f", "0.000000001_e", "0.001_6", "0.1_c", "1.0_4"}, LIST);
		radiation_positive_food_rads_text_color = sync(CATEGORY_RADIATION, "radiation_positive_food_rads_text_color", new String[] {"0.0_f", "0.1_e", "1.0_6", "10.0_c", "100.0_4"}, LIST);
		radiation_negative_food_rads_text_color = sync(CATEGORY_RADIATION, "radiation_negative_food_rads_text_color", new String[] {"0.0_b", "-10.0_9", "-100.0_d"}, LIST);
		radiation_positive_food_resistance_text_color = sync(CATEGORY_RADIATION, "radiation_positive_food_resistance_text_color", new String[] {"0.0_f"}, LIST);
		radiation_negative_food_resistance_text_color = sync(CATEGORY_RADIATION, "radiation_negative_food_resistance_text_color", new String[] {"0.0_7"}, LIST);
		
		radiation_player_debuff_lists = sync(CATEGORY_RADIATION, "radiation_player_debuff_lists", new String[] {"40.0_minecraft:weakness@1", "55.0_minecraft:weakness@1,minecraft:mining_fatigue@1", "70.0_minecraft:weakness@2,minecraft:mining_fatigue@1,minecraft:hunger@1", "80.0_minecraft:weakness@2,minecraft:mining_fatigue@2,minecraft:hunger@1,minecraft:poison@1", "90.0_minecraft:weakness@3,minecraft:mining_fatigue@3,minecraft:hunger@2,minecraft:poison@1,minecraft:wither@1"}, LIST);
		radiation_passive_debuff_lists = sync(CATEGORY_RADIATION, "radiation_passive_debuff_lists", new String[] {"40.0_minecraft:weakness@1", "55.0_minecraft:weakness@1,minecraft:mining_fatigue@1", "70.0_minecraft:weakness@2,minecraft:mining_fatigue@1,minecraft:hunger@1", "80.0_minecraft:weakness@2,minecraft:mining_fatigue@2,minecraft:hunger@1,minecraft:poison@1", "90.0_minecraft:weakness@3,minecraft:mining_fatigue@3,minecraft:hunger@2,minecraft:poison@1,minecraft:wither@1"}, LIST);
		radiation_mob_buff_lists = sync(CATEGORY_RADIATION, "radiation_mob_buff_lists", new String[] {"40.0_minecraft:speed@1", "55.0_minecraft:speed@1,minecraft:strength@1", "70.0_minecraft:speed@1,minecraft:strength@1,minecraft:resistance@1", "80.0_minecraft:speed@1,minecraft:strength@1,minecraft:resistance@1,minecraft:absorption@1", "90.0_minecraft:speed@1,minecraft:strength@1,minecraft:resistance@1,minecraft:absorption@1,minecraft:regeneration@1"}, LIST);
		radiation_player_rads_fatal = sync(CATEGORY_RADIATION, "radiation_player_rads_fatal", true);
		radiation_passive_rads_fatal = sync(CATEGORY_RADIATION, "radiation_passive_rads_fatal", true);
		radiation_mob_rads_fatal = sync(CATEGORY_RADIATION, "radiation_mob_rads_fatal", true);
		
		radiation_horse_armor = sync(CATEGORY_RADIATION, "radiation_horse_armor", false);
		
		radiation_hud_size = sync(CATEGORY_RADIATION, "radiation_hud_size", 1D, 0.1D, 10D);
		radiation_hud_position = sync(CATEGORY_RADIATION, "radiation_hud_position", 225D, 0D, 360D);
		radiation_hud_position_cartesian = sync(CATEGORY_RADIATION, "radiation_hud_position_cartesian", new double[] {}, 0D, 1D, LIST);
		radiation_hud_text_outline = sync(CATEGORY_RADIATION, "radiation_hud_text_outline", false);
		radiation_require_counter = sync(CATEGORY_RADIATION, "radiation_require_counter", true);
		radiation_chunk_boundaries = sync(CATEGORY_RADIATION, "radiation_chunk_boundaries", true);
		radiation_unit_prefixes = sync(CATEGORY_RADIATION, "radiation_unit_prefixes", 0, 0, 15);
		
		radiation_badge_durability = sync(CATEGORY_RADIATION, "radiation_badge_durability", 250D, 0.000000000000000001D, 1000D);
		radiation_badge_info_rate = sync(CATEGORY_RADIATION, "radiation_badge_info_rate", 0.1D, 0.000000000000000001D, 1D);
		
		register_processor = sync(CATEGORY_REGISTRATION, "register_processor", new boolean[] {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true}, ARRAY);
		register_passive = sync(CATEGORY_REGISTRATION, "register_passive", new boolean[] {true, true, true}, ARRAY);
		register_battery = sync(CATEGORY_REGISTRATION, "register_battery", new boolean[] {true, true}, ARRAY);
		register_quantum = sync(CATEGORY_REGISTRATION, "register_quantum", true);
		register_tool = sync(CATEGORY_REGISTRATION, "register_tool", new boolean[] {true, true, true, true}, ARRAY);
		register_tic_tool = sync(CATEGORY_REGISTRATION, "register_tic_tool", new boolean[] {true, true, true, true, true, true, true, true}, ARRAY);
		register_armor = sync(CATEGORY_REGISTRATION, "register_armor", new boolean[] {true, true, true, true}, ARRAY);
		register_conarm_armor = sync(CATEGORY_REGISTRATION, "register_conarm_armor", new boolean[] {true, true, true, true, true, true, true, true}, ARRAY);
		register_entity = sync(CATEGORY_REGISTRATION, "register_entity", new boolean[] {true}, ARRAY);
		register_fluid_blocks = sync(CATEGORY_REGISTRATION, "register_fluid_blocks", false);
		register_cofh_fluids = sync(CATEGORY_REGISTRATION, "register_cofh_fluids", false);
		register_projecte_emc = sync(CATEGORY_REGISTRATION, "register_projecte_emc", true);
		
		give_guidebook = sync(CATEGORY_MISC, "give_guidebook", true);
		single_creative_tab = sync(CATEGORY_MISC, "single_creative_tab", false);
		ctrl_info = sync(CATEGORY_MISC, "ctrl_info", false);
		jei_chance_items_include_null = sync(CATEGORY_MISC, "jei_chance_items_include_null", false);
		rare_drops = sync(CATEGORY_MISC, "rare_drops", false);
		dungeon_loot = sync(CATEGORY_MISC, "dungeon_loot", false);
		corium_solidification = sync(CATEGORY_MISC, "corium_solidification", new int[] {0, 1, 2, -6, -100, 4598, -9999, -11325}, Integer.MIN_VALUE, Integer.MAX_VALUE, LIST);
		corium_solidification_list_type = sync(CATEGORY_MISC, "corium_solidification_list_type", false);
		ore_dict_raw_material_recipes = sync(CATEGORY_MISC, "ore_dict_raw_material_recipes", false);
		ore_dict_priority_bool = sync(CATEGORY_MISC, "ore_dict_priority_bool", true);
		ore_dict_priority = sync(CATEGORY_MISC, "ore_dict_priority", new String[] {"minecraft", "thermalfoundation", "techreborn", "nuclearcraft", "immersiveengineering", "mekanism", "ic2", "appliedenergistics2", "refinedstorage", "actuallyadditions", "libvulpes", "advancedrocketry", "thaumcraft", "biomesoplenty"}, LIST);
		hwyla_enabled = sync(CATEGORY_MISC, "hwyla_enabled", true);
		
		setCategoryPropertyOrders(config);
		
		BasicRecipeHandler.initGTCEIntegration();
		
		radiation_enabled_public = radiation_enabled;
		radiation_horse_armor_public = radiation_horse_armor;
		
		if (config.hasChanged()) {
			config.save();
		}
	}
	
	protected static void outputInfo() {
		File file = new File(Loader.instance().getConfigDir(), "nuclearcraft.info");
		file.delete();
		Configuration info = new Configuration(file);
		
		List<String> fissionPlacement = new ArrayList<>();
		for (Object2ObjectMap.Entry<String, String> entry : FissionPlacement.RULE_MAP_RAW.object2ObjectEntrySet()) {
			fissionPlacement.add(entry.getKey() + " -> " + entry.getValue());
		}
		
		Property propertyFissionPlacement = info.get(CATEGORY_OUTPUT, "fission_placement", fissionPlacement.toArray(new String[fissionPlacement.size()]));
		propertyFissionPlacement.setLanguageKey("gui.nc.config.fission_placement");
		
		List<String> turbinePlacement = new ArrayList<>();
		for (Object2ObjectMap.Entry<String, String> entry : TurbinePlacement.RULE_MAP_RAW.object2ObjectEntrySet()) {
			turbinePlacement.add(entry.getKey() + " -> " + entry.getValue());
		}
		
		Property propertyTurbinePlacement = info.get(CATEGORY_OUTPUT, "turbine_placement", turbinePlacement.toArray(new String[turbinePlacement.size()]));
		propertyTurbinePlacement.setLanguageKey("gui.nc.config.turbine_placement");
		
		if (info.hasChanged()) {
			info.save();
		}
	}
	
	public static int sync(String category, String name, int defaultValue) {
		Property property = config.get(category, name, defaultValue, Lang.localise("gui.nc.config." + name + ".comment"));
		property.setLanguageKey("gui.nc.config." + name);
		PROPERTY_ORDER_MAP.get(category).add(property.getName());
		int value = property.getInt();
		property.set(value);
		return value;
	}
	
	public static int sync(String category, String name, int defaultValue, int minValue, int maxValue) {
		Property property = config.get(category, name, defaultValue, Lang.localise("gui.nc.config." + category + "." + name + ".comment"), minValue, maxValue);
		property.setLanguageKey("gui.nc.config." + category + "." + name);
		PROPERTY_ORDER_MAP.get(category).add(property.getName());
		int value = property.getInt();
		property.set(value);
		return value;
	}
	
	public static boolean sync(String category, String name, boolean defaultValue) {
		Property property = config.get(category, name, defaultValue, Lang.localise("gui.nc.config." + category + "." + name + ".comment"));
		property.setLanguageKey("gui.nc.config." + category + "." + name);
		PROPERTY_ORDER_MAP.get(category).add(property.getName());
		boolean value = property.getBoolean();
		property.set(value);
		return value;
	}
	
	public static double sync(String category, String name, double defaultValue) {
		Property property = config.get(category, name, defaultValue, Lang.localise("gui.nc.config." + category + "." + name + ".comment"));
		property.setLanguageKey("gui.nc.config." + category + "." + name);
		PROPERTY_ORDER_MAP.get(category).add(property.getName());
		double value = property.getDouble();
		property.set(value);
		return value;
	}
	
	public static double sync(String category, String name, double defaultValue, double minValue, double maxValue) {
		Property property = config.get(category, name, defaultValue, Lang.localise("gui.nc.config." + category + "." + name + ".comment"), minValue, maxValue);
		property.setLanguageKey("gui.nc.config." + category + "." + name);
		PROPERTY_ORDER_MAP.get(category).add(property.getName());
		double value = property.getDouble();
		property.set(value);
		return value;
	}
	
	public static String sync(String category, String name, String defaultValue) {
		Property property = config.get(category, name, defaultValue, Lang.localise("gui.nc.config." + category + "." + name + ".comment"));
		property.setLanguageKey("gui.nc.config." + category + "." + name);
		PROPERTY_ORDER_MAP.get(category).add(property.getName());
		String value = property.getString();
		property.set(value);
		return value;
	}
	
	public static int[] sync(String category, String name, int[] defaultValue, boolean fixedArray) {
		Property property = config.get(category, name, defaultValue, Lang.localise("gui.nc.config." + category + "." + name + ".comment"));
		property.setLanguageKey("gui.nc.config." + category + "." + name);
		PROPERTY_ORDER_MAP.get(category).add(property.getName());
		int[] value = fixedArray ? readIntegerArray(property) : property.getIntList();
		property.set(value);
		return value;
	}
	
	public static int[] sync(String category, String name, int[] defaultValue, int minValue, int maxValue, boolean fixedArray) {
		Property property = config.get(category, name, defaultValue, Lang.localise("gui.nc.config." + category + "." + name + ".comment"), minValue, maxValue);
		property.setLanguageKey("gui.nc.config." + category + "." + name);
		PROPERTY_ORDER_MAP.get(category).add(property.getName());
		int[] value = fixedArray ? readIntegerArray(property) : property.getIntList();
		property.set(value);
		return value;
	}
	
	public static boolean[] sync(String category, String name, boolean[] defaultValue, boolean fixedArray) {
		Property property = config.get(category, name, defaultValue, Lang.localise("gui.nc.config." + category + "." + name + ".comment"));
		property.setLanguageKey("gui.nc.config." + category + "." + name);
		PROPERTY_ORDER_MAP.get(category).add(property.getName());
		boolean[] value = fixedArray ? readBooleanArray(property) : property.getBooleanList();
		property.set(value);
		return value;
	}
	
	public static double[] sync(String category, String name, double[] defaultValue, boolean fixedArray) {
		Property property = config.get(category, name, defaultValue, Lang.localise("gui.nc.config." + category + "." + name + ".comment"));
		property.setLanguageKey("gui.nc.config." + category + "." + name);
		PROPERTY_ORDER_MAP.get(category).add(property.getName());
		double[] value = fixedArray ? readDoubleArray(property) : property.getDoubleList();
		property.set(value);
		return value;
	}
	
	public static double[] sync(String category, String name, double[] defaultValue, double minValue, double maxValue, boolean fixedArray) {
		Property property = config.get(category, name, defaultValue, Lang.localise("gui.nc.config." + category + "." + name + ".comment"), minValue, maxValue);
		property.setLanguageKey("gui.nc.config." + category + "." + name);
		PROPERTY_ORDER_MAP.get(category).add(property.getName());
		double[] value = fixedArray ? readDoubleArray(property) : property.getDoubleList();
		property.set(value);
		return value;
	}
	
	public static String[] sync(String category, String name, String[] defaultValue, boolean fixedArray) {
		Property property = config.get(category, name, defaultValue, Lang.localise("gui.nc.config." + category + "." + name + ".comment"));
		property.setLanguageKey("gui.nc.config." + category + "." + name);
		PROPERTY_ORDER_MAP.get(category).add(property.getName());
		String[] value = fixedArray ? readStringArray(property) : property.getStringList();
		property.set(value);
		return value;
	}
	
	public static int[] readIntegerArray(Property property) {
		int[] currentList = property.getIntList();
		int currentLength = currentList.length;
		int defaultLength = property.getDefaults().length;
		
		if (currentLength == defaultLength) {
			return currentList;
		}
		int[] newList = new int[defaultLength];
		if (currentLength > defaultLength) {
			for (int i = 0; i < defaultLength; ++i) {
				newList[i] = currentList[i];
			}
		}
		else {
			for (int i = 0; i < currentLength; ++i) {
				newList[i] = currentList[i];
			}
			property.setToDefault();
			int[] defaultList = property.getIntList();
			for (int i = currentLength; i < defaultLength; ++i) {
				newList[i] = defaultList[i];
			}
		}
		return newList;
	}
	
	public static boolean[] readBooleanArray(Property property) {
		boolean[] currentList = property.getBooleanList();
		int currentLength = currentList.length;
		int defaultLength = property.getDefaults().length;
		
		if (currentLength == defaultLength) {
			return currentList;
		}
		boolean[] newList = new boolean[defaultLength];
		if (currentLength > defaultLength) {
			for (int i = 0; i < defaultLength; ++i) {
				newList[i] = currentList[i];
			}
		}
		else {
			for (int i = 0; i < currentLength; ++i) {
				newList[i] = currentList[i];
			}
			property.setToDefault();
			boolean[] defaultList = property.getBooleanList();
			for (int i = currentLength; i < defaultLength; ++i) {
				newList[i] = defaultList[i];
			}
		}
		return newList;
	}
	
	public static double[] readDoubleArray(Property property) {
		double[] currentList = property.getDoubleList();
		int currentLength = currentList.length;
		int defaultLength = property.getDefaults().length;
		
		if (currentLength == defaultLength) {
			return currentList;
		}
		double[] newList = new double[defaultLength];
		if (currentLength > defaultLength) {
			for (int i = 0; i < defaultLength; ++i) {
				newList[i] = currentList[i];
			}
		}
		else {
			for (int i = 0; i < currentLength; ++i) {
				newList[i] = currentList[i];
			}
			property.setToDefault();
			double[] defaultList = property.getDoubleList();
			for (int i = currentLength; i < defaultLength; ++i) {
				newList[i] = defaultList[i];
			}
		}
		return newList;
	}
	
	public static String[] readStringArray(Property property) {
		String[] currentList = property.getStringList();
		int currentLength = currentList.length;
		int defaultLength = property.getDefaults().length;
		
		if (currentLength == defaultLength) {
			return currentList;
		}
		String[] newList = new String[defaultLength];
		if (currentLength > defaultLength) {
			for (int i = 0; i < defaultLength; ++i) {
				newList[i] = currentList[i];
			}
		}
		else {
			for (int i = 0; i < currentLength; ++i) {
				newList[i] = currentList[i];
			}
			property.setToDefault();
			String[] defaultList = property.getStringList();
			for (int i = currentLength; i < defaultLength; ++i) {
				newList[i] = defaultList[i];
			}
		}
		return newList;
	}
	
	public static void setCategoryPropertyOrders(Configuration config) {
		for (Entry<String, List<String>> entry : PROPERTY_ORDER_MAP.entrySet()) {
			config.setCategoryPropertyOrder(entry.getKey(), entry.getValue());
		}
	}
	
	protected static class ServerConfigEventHandler {
		
		protected ServerConfigEventHandler() {}
		
		@SubscribeEvent
		public void configOnWorldLoad(PlayerLoggedInEvent event) {
			if (event.player instanceof EntityPlayerMP) {
				PacketHandler.instance.sendTo(getConfigUpdatePacket(), (EntityPlayerMP) event.player);
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
	
	protected static class ClientConfigEventHandler {
		
		protected ClientConfigEventHandler() {}
		
		@SubscribeEvent(priority = EventPriority.LOWEST)
		public void onEvent(OnConfigChangedEvent event) {
			if (event.getModID().equals(Global.MOD_ID)) {
				syncConfig(false);
			}
		}
	}
}
