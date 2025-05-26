package nc.recipe;

import it.unimi.dsi.fastutil.objects.*;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.integration.crafttweaker.CTRegistration;
import nc.integration.crafttweaker.CTRegistration.RegistrationInfo;
import nc.integration.tconstruct.TConstructExtras;
import nc.radiation.RadBlockEffects.*;
import nc.radiation.RadSources;
import nc.recipe.generator.DecayGeneratorRecipes;
import nc.recipe.multiblock.*;
import nc.recipe.other.CollectorRecipes;
import nc.recipe.processor.*;
import nc.recipe.radiation.RadiationScrubberRecipes;
import nc.recipe.vanilla.*;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.*;

import static nc.config.NCConfig.gtce_recipe_integration;

public class NCRecipes {
	
	private static boolean initialized = false;
	
	private static final Object2ObjectMap<String, BasicRecipeHandler> RECIPE_HANDLER_MAP = new Object2ObjectOpenHashMap<>();
	
	private static final Object2BooleanMap<String> GTCE_INTEGRATION = new Object2BooleanOpenHashMap<>();
	
	public static final String[] CT_RECIPE_HANDLER_NAME_ARRAY = {
			"manufactory",
			"separator",
			"decay_hastener",
			"fuel_reprocessor",
			"alloy_furnace",
			"infuser",
			"melter",
			"supercooler",
			"electrolyzer",
			"assembler",
			"ingot_former",
			"pressurizer",
			"chemical_reactor",
			"salt_mixer",
			"crystallizer",
			"enricher",
			"extractor",
			"centrifuge",
			"rock_crusher",
			"electric_furnace",
			"decay_generator",
			"machine_diaphragm",
			"machine_sieve_assembly",
			"multiblock_electrolyzer",
			"electrolyzer_cathode",
			"electrolyzer_anode",
			"multiblock_distiller",
			"fission_moderator",
			"fission_reflector",
			"fission_irradiator",
			"pebble_fission",
			"solid_fission",
			"fission_heating",
			"salt_fission",
			"fission_emergency_cooling",
			"heat_exchanger",
			"turbine",
			"radiation_scrubber",
			"radiation_block_mutation",
			"radiation_block_purification",
	};
	
	public static void putHandler(BasicRecipeHandler handler) {
		RECIPE_HANDLER_MAP.put(handler.getName(), handler);
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends BasicRecipeHandler> T getHandler(String name) {
		return (T) RECIPE_HANDLER_MAP.get(name);
	}
	
	public static Collection<BasicRecipeHandler> getHandlers() {
		return RECIPE_HANDLER_MAP.values();
	}
	
	public static List<BasicRecipe> getRecipeList(String name) {
		return getHandler(name).recipeList;
	}
	
	public static List<Set<String>> getValidFluids(String name) {
		return getHandler(name).validFluids;
	}
	
	public static boolean hasGTCEIntegration(String name) {
		return GTCE_INTEGRATION.getBoolean(name);
	}
	
	@SubscribeEvent(priority = EventPriority.LOW)
	public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		if (initialized) {
			return;
		}
		initialized = true;
		
		RadSources.init();
		
		putHandler(new ManufactoryRecipes());
		putHandler(new SeparatorRecipes());
		putHandler(new DecayHastenerRecipes());
		putHandler(new FuelReprocessorRecipes());
		putHandler(new AlloyFurnaceRecipes());
		putHandler(new InfuserRecipes());
		putHandler(new MelterRecipes());
		putHandler(new SupercoolerRecipes());
		putHandler(new ElectrolyzerRecipes());
		putHandler(new AssemblerRecipes());
		putHandler(new IngotFormerRecipes());
		putHandler(new PressurizerRecipes());
		putHandler(new ChemicalReactorRecipes());
		putHandler(new SaltMixerRecipes());
		putHandler(new CrystallizerRecipes());
		putHandler(new EnricherRecipes());
		putHandler(new ExtractorRecipes());
		putHandler(new CentrifugeRecipes());
		putHandler(new RockCrusherRecipes());
		putHandler(new ElectricFurnaceRecipes());
		putHandler(new CollectorRecipes());
		putHandler(new DecayGeneratorRecipes());
		putHandler(new MachineDiaphragmRecipes());
		putHandler(new MachineSieveAssemblyRecipes());
		putHandler(new MultiblockElectrolyzerRecipes());
		putHandler(new ElectrolyzerCathodeRecipes());
		putHandler(new ElectrolyzerAnodeRecipes());
		putHandler(new MultiblockDistillerRecipes());
		putHandler(new MultiblockInfiltratorRecipes());
		putHandler(new InfiltratorPressureFluidRecipes());
		putHandler(new FissionModeratorRecipes());
		putHandler(new FissionReflectorRecipes());
		putHandler(new FissionIrradiatorRecipes());
		putHandler(new PebbleFissionRecipes());
		putHandler(new SolidFissionRecipes());
		putHandler(new FissionHeatingRecipes());
		putHandler(new SaltFissionRecipes());
		putHandler(new CoolantHeaterRecipes());
		putHandler(new FissionEmergencyCoolingRecipes());
		putHandler(new HeatExchangerRecipes());
		putHandler(new TurbineRecipes());
		putHandler(new RadiationScrubberRecipes());
		putHandler(new RadiationBlockMutation());
		putHandler(new RadiationBlockPurification());
		
		registerShortcuts();
		
		CraftingRecipeHandler.registerCraftingRecipes();
		FurnaceRecipeHandler.registerFurnaceRecipes();
		GameRegistry.registerFuelHandler(new FurnaceFuelHandler());
		
		for (RegistrationInfo info : CTRegistration.INFO_LIST) {
			info.recipeInit();
		}
	}
	
	public static ManufactoryRecipes manufactory;
	public static SeparatorRecipes separator;
	public static DecayHastenerRecipes decay_hastener;
	public static FuelReprocessorRecipes fuel_reprocessor;
	public static AlloyFurnaceRecipes alloy_furnace;
	public static InfuserRecipes infuser;
	public static MelterRecipes melter;
	public static SupercoolerRecipes supercooler;
	public static ElectrolyzerRecipes electrolyzer;
	public static AssemblerRecipes assembler;
	public static IngotFormerRecipes ingot_former;
	public static PressurizerRecipes pressurizer;
	public static ChemicalReactorRecipes chemical_reactor;
	public static SaltMixerRecipes salt_mixer;
	public static CrystallizerRecipes crystallizer;
	public static EnricherRecipes enricher;
	public static ExtractorRecipes extractor;
	public static CentrifugeRecipes centrifuge;
	public static RockCrusherRecipes rock_crusher;
	public static ElectricFurnaceRecipes electric_furnace;
	public static CollectorRecipes collector;
	public static DecayGeneratorRecipes decay_generator;
	public static MachineDiaphragmRecipes machine_diaphragm;
	public static MachineSieveAssemblyRecipes machine_sieve_assembly;
	public static MultiblockElectrolyzerRecipes multiblock_electrolyzer;
	public static ElectrolyzerCathodeRecipes electrolyzer_cathode;
	public static ElectrolyzerAnodeRecipes electrolyzer_anode;
	public static MultiblockDistillerRecipes multiblock_distiller;
	public static MultiblockInfiltratorRecipes multiblock_infiltrator;
	public static InfiltratorPressureFluidRecipes infiltrator_pressure_fluid;
	public static FissionModeratorRecipes fission_moderator;
	public static FissionReflectorRecipes fission_reflector;
	public static FissionIrradiatorRecipes fission_irradiator;
	public static PebbleFissionRecipes pebble_fission;
	public static SolidFissionRecipes solid_fission;
	public static FissionHeatingRecipes fission_heating;
	public static SaltFissionRecipes salt_fission;
	public static CoolantHeaterRecipes coolant_heater;
	public static FissionEmergencyCoolingRecipes fission_emergency_cooling;
	public static HeatExchangerRecipes heat_exchanger;
	public static TurbineRecipes turbine;
	public static RadiationScrubberRecipes radiation_scrubber;
	public static RadiationBlockMutation radiation_block_mutation;
	public static RadiationBlockPurification radiation_block_purification;
	
	public void registerShortcuts() {
		manufactory = getHandler("manufactory");
		separator = getHandler("separator");
		decay_hastener = getHandler("decay_hastener");
		fuel_reprocessor = getHandler("fuel_reprocessor");
		alloy_furnace = getHandler("alloy_furnace");
		infuser = getHandler("infuser");
		melter = getHandler("melter");
		supercooler = getHandler("supercooler");
		electrolyzer = getHandler("electrolyzer");
		assembler = getHandler("assembler");
		ingot_former = getHandler("ingot_former");
		pressurizer = getHandler("pressurizer");
		chemical_reactor = getHandler("chemical_reactor");
		salt_mixer = getHandler("salt_mixer");
		crystallizer = getHandler("crystallizer");
		enricher = getHandler("enricher");
		extractor = getHandler("extractor");
		centrifuge = getHandler("centrifuge");
		rock_crusher = getHandler("rock_crusher");
		electric_furnace = getHandler("electric_furnace");
		collector = getHandler("collector");
		decay_generator = getHandler("decay_generator");
		machine_diaphragm = getHandler("machine_diaphragm");
		machine_sieve_assembly = getHandler("machine_sieve_assembly");
		multiblock_electrolyzer = getHandler("multiblock_electrolyzer");
		electrolyzer_cathode = getHandler("electrolyzer_cathode");
		electrolyzer_anode = getHandler("electrolyzer_anode");
		multiblock_distiller = getHandler("multiblock_distiller");
		multiblock_infiltrator = getHandler("multiblock_infiltrator");
		infiltrator_pressure_fluid = getHandler("infiltrator_pressure_fluid");
		fission_moderator = getHandler("fission_moderator");
		fission_reflector = getHandler("fission_reflector");
		fission_irradiator = getHandler("fission_irradiator");
		pebble_fission = getHandler("pebble_fission");
		solid_fission = getHandler("solid_fission");
		fission_heating = getHandler("fission_heating");
		salt_fission = getHandler("salt_fission");
		coolant_heater = getHandler("coolant_heater");
		fission_emergency_cooling = getHandler("fission_emergency_cooling");
		heat_exchanger = getHandler("heat_exchanger");
		turbine = getHandler("turbine");
		radiation_scrubber = getHandler("radiation_scrubber");
		radiation_block_mutation = getHandler("radiation_block_mutation");
		radiation_block_purification = getHandler("radiation_block_purification");
	}
	
	public static void initGTCEIntegration() {
		for (int i = 0, len = gtce_recipe_integration.length; i < len; ++i) {
			GTCE_INTEGRATION.put(CT_RECIPE_HANDLER_NAME_ARRAY[i], gtce_recipe_integration[i]);
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void registerIntegrationRecipes(RegistryEvent.Register<IRecipe> event) {
		if (ModCheck.tinkersLoaded() && NCConfig.register_tic_recipes) {
			TConstructExtras.init();
		}
		
		if (ModCheck.gregtechLoaded() && NCConfig.gtce_recipe_integration_global) {
			for (BasicRecipeHandler handler : getHandlers()) {
				handler.addGTCERecipes();
			}
		}
	}
	
	public static void init() {
		for (BasicRecipeHandler handler : getHandlers()) {
			handler.init();
		}
	}
	
	public static void postInit() {
		for (BasicRecipeHandler handler : getHandlers()) {
			handler.postInit();
		}
	}
	
	public static void refreshRecipeCaches() {
		for (BasicRecipeHandler handler : getHandlers()) {
			handler.refreshCache();
		}
	}
}
