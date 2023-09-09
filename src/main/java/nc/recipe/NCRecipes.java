package nc.recipe;

import java.util.*;

import it.unimi.dsi.fastutil.objects.*;
import nc.ModCheck;
import nc.integration.crafttweaker.CTRegistration;
import nc.integration.crafttweaker.CTRegistration.RegistrationInfo;
import nc.integration.tconstruct.TConstructExtras;
import nc.radiation.RadBlockEffects.*;
import nc.radiation.RadSources;
import nc.recipe.generator.*;
import nc.recipe.multiblock.*;
import nc.recipe.other.*;
import nc.recipe.processor.*;
import nc.recipe.vanilla.*;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class NCRecipes {
	
	private static boolean initialized = false;
	
	public static final Object2ObjectMap<String, BasicRecipeHandler> RECIPE_HANDLER_MAP = new Object2ObjectOpenHashMap<>();
	
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
	
	public static List<List<String>> getValidFluids(String name) {
		return getHandler(name).validFluids;
	}
	
	@SubscribeEvent(priority = EventPriority.LOW)
	public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		if (initialized) {
			return;
		}
		
		RadSources.init();
		
		RECIPE_HANDLER_MAP.put("manufactory", new ManufactoryRecipes());
		RECIPE_HANDLER_MAP.put("separator", new SeparatorRecipes());
		RECIPE_HANDLER_MAP.put("decay_hastener", new DecayHastenerRecipes());
		RECIPE_HANDLER_MAP.put("fuel_reprocessor", new FuelReprocessorRecipes());
		RECIPE_HANDLER_MAP.put("alloy_furnace", new AlloyFurnaceRecipes());
		RECIPE_HANDLER_MAP.put("infuser", new InfuserRecipes());
		RECIPE_HANDLER_MAP.put("melter", new MelterRecipes());
		RECIPE_HANDLER_MAP.put("supercooler", new SupercoolerRecipes());
		RECIPE_HANDLER_MAP.put("electrolyzer", new ElectrolyzerRecipes());
		RECIPE_HANDLER_MAP.put("assembler", new AssemblerRecipes());
		RECIPE_HANDLER_MAP.put("ingot_former", new IngotFormerRecipes());
		RECIPE_HANDLER_MAP.put("pressurizer", new PressurizerRecipes());
		RECIPE_HANDLER_MAP.put("chemical_reactor", new ChemicalReactorRecipes());
		RECIPE_HANDLER_MAP.put("salt_mixer", new SaltMixerRecipes());
		RECIPE_HANDLER_MAP.put("crystallizer", new CrystallizerRecipes());
		RECIPE_HANDLER_MAP.put("enricher", new EnricherRecipes());
		RECIPE_HANDLER_MAP.put("extractor", new ExtractorRecipes());
		RECIPE_HANDLER_MAP.put("centrifuge", new CentrifugeRecipes());
		RECIPE_HANDLER_MAP.put("rock_crusher", new RockCrusherRecipes());
		RECIPE_HANDLER_MAP.put("collector", new CollectorRecipes());
		RECIPE_HANDLER_MAP.put("decay_generator", new DecayGeneratorRecipes());
		RECIPE_HANDLER_MAP.put("fission_moderator", new FissionModeratorRecipes());
		RECIPE_HANDLER_MAP.put("fission_reflector", new FissionReflectorRecipes());
		RECIPE_HANDLER_MAP.put("fission_irradiator", new FissionIrradiatorRecipes());
		RECIPE_HANDLER_MAP.put("pebble_fission", new PebbleFissionRecipes());
		RECIPE_HANDLER_MAP.put("solid_fission", new SolidFissionRecipes());
		RECIPE_HANDLER_MAP.put("fission_heating", new FissionHeatingRecipes());
		RECIPE_HANDLER_MAP.put("salt_fission", new SaltFissionRecipes());
		RECIPE_HANDLER_MAP.put("fusion", new FusionRecipes());
		RECIPE_HANDLER_MAP.put("coolant_heater", new CoolantHeaterRecipes());
		RECIPE_HANDLER_MAP.put("fission_emergency_cooling", new FissionEmergencyCoolingRecipes());
		RECIPE_HANDLER_MAP.put("heat_exchanger", new HeatExchangerRecipes());
		RECIPE_HANDLER_MAP.put("condenser", new CondenserRecipes());
		RECIPE_HANDLER_MAP.put("turbine", new TurbineRecipes());
		RECIPE_HANDLER_MAP.put("radiation_scrubber", new RadiationScrubberRecipes());
		RECIPE_HANDLER_MAP.put("radiation_block_mutation", new RadiationBlockMutation());
		RECIPE_HANDLER_MAP.put("radiation_block_purification", new RadiationBlockPurification());
		
		CraftingRecipeHandler.registerCraftingRecipes();
		FurnaceRecipeHandler.registerFurnaceRecipes();
		GameRegistry.registerFuelHandler(new FurnaceFuelHandler());
		
		for (RegistrationInfo info : CTRegistration.INFO_LIST) {
			info.recipeInit();
		}
		
		initialized = true;
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
	public static CollectorRecipes collector;
	public static DecayGeneratorRecipes decay_generator;
	public static FissionModeratorRecipes fission_moderator;
	public static FissionReflectorRecipes fission_reflector;
	public static FissionIrradiatorRecipes fission_irradiator;
	public static PebbleFissionRecipes pebble_fission;
	public static SolidFissionRecipes solid_fission;
	public static FissionHeatingRecipes fission_heating;
	public static SaltFissionRecipes salt_fission;
	public static FusionRecipes fusion;
	public static CoolantHeaterRecipes coolant_heater;
	public static FissionEmergencyCoolingRecipes fission_emergency_cooling;
	public static HeatExchangerRecipes heat_exchanger;
	public static CondenserRecipes condenser;
	public static TurbineRecipes turbine;
	public static RadiationScrubberRecipes radiation_scrubber;
	public static RadiationBlockMutation radiation_block_mutation;
	public static RadiationBlockPurification radiation_block_purification;
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void registerShortcuts(RegistryEvent.Register<IRecipe> event) {
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
		collector = getHandler("collector");
		decay_generator = getHandler("decay_generator");
		fission_moderator = getHandler("fission_moderator");
		fission_reflector = getHandler("fission_reflector");
		fission_irradiator = getHandler("fission_irradiator");
		pebble_fission = getHandler("pebble_fission");
		solid_fission = getHandler("solid_fission");
		fission_heating = getHandler("fission_heating");
		salt_fission = getHandler("salt_fission");
		fusion = getHandler("fusion");
		coolant_heater = getHandler("coolant_heater");
		fission_emergency_cooling = getHandler("fission_emergency_cooling");
		heat_exchanger = getHandler("heat_exchanger");
		condenser = getHandler("condenser");
		turbine = getHandler("turbine");
		radiation_scrubber = getHandler("radiation_scrubber");
		radiation_block_mutation = getHandler("radiation_block_mutation");
		radiation_block_purification = getHandler("radiation_block_purification");
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void registerIntegrationRecipes(RegistryEvent.Register<IRecipe> event) {
		if (ModCheck.tinkersLoaded()) {
			TConstructExtras.init();
		}
		
		for (BasicRecipeHandler handler : processor_recipe_handlers) {
			handler.addGTCERecipes();
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
