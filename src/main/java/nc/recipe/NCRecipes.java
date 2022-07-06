package nc.recipe;

import java.util.List;

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
	
	public static BasicRecipeHandler getHandler(String name) {
		return RECIPE_HANDLER_MAP.get(name);
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
		for (BasicRecipeHandler handler : RECIPE_HANDLER_MAP.values()) {
			handler.init();
		}
	}
	
	public static void refreshRecipeCaches() {
		for (BasicRecipeHandler handler : RECIPE_HANDLER_MAP.values()) {
			handler.refreshCache();
		}
	}
}
