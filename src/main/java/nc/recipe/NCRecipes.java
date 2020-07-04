package nc.recipe;

import java.util.List;

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
	
	public static ProcessorRecipeHandler manufactory;
	public static ProcessorRecipeHandler separator;
	public static ProcessorRecipeHandler decay_hastener;
	public static ProcessorRecipeHandler fuel_reprocessor;
	public static ProcessorRecipeHandler alloy_furnace;
	public static ProcessorRecipeHandler infuser;
	public static ProcessorRecipeHandler melter;
	public static ProcessorRecipeHandler supercooler;
	public static ProcessorRecipeHandler electrolyzer;
	public static ProcessorRecipeHandler assembler;
	public static ProcessorRecipeHandler ingot_former;
	public static ProcessorRecipeHandler pressurizer;
	public static ProcessorRecipeHandler chemical_reactor;
	public static ProcessorRecipeHandler salt_mixer;
	public static ProcessorRecipeHandler crystallizer;
	public static ProcessorRecipeHandler enricher;
	public static ProcessorRecipeHandler extractor;
	public static ProcessorRecipeHandler centrifuge;
	public static ProcessorRecipeHandler rock_crusher;
	public static ProcessorRecipeHandler collector;
	public static ProcessorRecipeHandler decay_generator;
	public static ProcessorRecipeHandler fission_moderator;
	public static ProcessorRecipeHandler fission_reflector;
	public static ProcessorRecipeHandler fission_irradiator;
	public static ProcessorRecipeHandler pebble_fission;
	public static ProcessorRecipeHandler solid_fission;
	public static ProcessorRecipeHandler fission_heating;
	public static ProcessorRecipeHandler salt_fission;
	public static ProcessorRecipeHandler fusion;
	public static ProcessorRecipeHandler coolant_heater;
	public static ProcessorRecipeHandler fission_emergency_cooling;
	public static ProcessorRecipeHandler heat_exchanger;
	public static ProcessorRecipeHandler condenser;
	public static ProcessorRecipeHandler turbine;
	public static ProcessorRecipeHandler radiation_scrubber;
	public static ProcessorRecipeHandler radiation_block_mutation;
	public static ProcessorRecipeHandler radiation_block_purification;
	
	@SubscribeEvent(priority = EventPriority.LOW)
	public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		if (initialized) {
			return;
		}
		
		RadSources.init();
		
		manufactory = new ManufactoryRecipes();
		separator = new SeparatorRecipes();
		decay_hastener = new DecayHastenerRecipes();
		fuel_reprocessor = new FuelReprocessorRecipes();
		alloy_furnace = new AlloyFurnaceRecipes();
		infuser = new InfuserRecipes();
		melter = new MelterRecipes();
		supercooler = new SupercoolerRecipes();
		electrolyzer = new ElectrolyzerRecipes();
		assembler = new AssemblerRecipes();
		ingot_former = new IngotFormerRecipes();
		pressurizer = new PressurizerRecipes();
		chemical_reactor = new ChemicalReactorRecipes();
		salt_mixer = new SaltMixerRecipes();
		crystallizer = new CrystallizerRecipes();
		enricher = new EnricherRecipes();
		extractor = new ExtractorRecipes();
		centrifuge = new CentrifugeRecipes();
		rock_crusher = new RockCrusherRecipes();
		collector = new CollectorRecipes();
		decay_generator = new DecayGeneratorRecipes();
		fission_moderator = new FissionModeratorRecipes();
		fission_reflector = new FissionReflectorRecipes();
		fission_irradiator = new FissionIrradiatorRecipes();
		pebble_fission = new PebbleFissionRecipes();
		solid_fission = new SolidFissionRecipes();
		fission_heating = new FissionHeatingRecipes();
		salt_fission = new SaltFissionRecipes();
		fusion = new FusionRecipes();
		coolant_heater = new CoolantHeaterRecipes();
		fission_emergency_cooling = new FissionEmergencyCoolingRecipes();
		heat_exchanger = new HeatExchangerRecipes();
		condenser = new CondenserRecipes();
		turbine = new TurbineRecipes();
		radiation_scrubber = new RadiationScrubberRecipes();
		radiation_block_mutation = new RadiationBlockMutation();
		radiation_block_purification = new RadiationBlockPurification();
		
		CraftingRecipeHandler.registerCraftingRecipes();
		FurnaceRecipeHandler.registerFurnaceRecipes();
		GameRegistry.registerFuelHandler(new FurnaceFuelHandler());
		
		initialized = true;
	}
	
	public static List<List<String>> infuser_valid_fluids;
	public static List<List<String>> melter_valid_fluids;
	public static List<List<String>> supercooler_valid_fluids;
	public static List<List<String>> electrolyzer_valid_fluids;
	public static List<List<String>> assembler_valid_fluids;
	public static List<List<String>> ingot_former_valid_fluids;
	public static List<List<String>> chemical_reactor_valid_fluids;
	public static List<List<String>> salt_mixer_valid_fluids;
	public static List<List<String>> crystallizer_valid_fluids;
	public static List<List<String>> enricher_valid_fluids;
	public static List<List<String>> extractor_valid_fluids;
	public static List<List<String>> centrifuge_valid_fluids;
	public static List<List<String>> fission_heating_valid_fluids;
	public static List<List<String>> salt_fission_valid_fluids;
	public static List<List<String>> fusion_valid_fluids;
	public static List<List<String>> coolant_heater_valid_fluids;
	public static List<List<String>> fission_emergency_cooling_valid_fluids;
	public static List<List<String>> heat_exchanger_valid_fluids;
	public static List<List<String>> condenser_valid_fluids;
	public static List<List<String>> turbine_valid_fluids;
	public static List<List<String>> radiation_scrubber_valid_fluids;
	
	public static void init() {
		infuser_valid_fluids = RecipeHelper.validFluids(infuser);
		melter_valid_fluids = RecipeHelper.validFluids(melter);
		supercooler_valid_fluids = RecipeHelper.validFluids(supercooler);
		electrolyzer_valid_fluids = RecipeHelper.validFluids(electrolyzer);
		assembler_valid_fluids = RecipeHelper.validFluids(assembler);
		ingot_former_valid_fluids = RecipeHelper.validFluids(ingot_former);
		chemical_reactor_valid_fluids = RecipeHelper.validFluids(chemical_reactor);
		salt_mixer_valid_fluids = RecipeHelper.validFluids(salt_mixer);
		crystallizer_valid_fluids = RecipeHelper.validFluids(crystallizer);
		enricher_valid_fluids = RecipeHelper.validFluids(enricher);
		extractor_valid_fluids = RecipeHelper.validFluids(extractor);
		centrifuge_valid_fluids = RecipeHelper.validFluids(centrifuge);
		fission_heating_valid_fluids = RecipeHelper.validFluids(fission_heating);
		salt_fission_valid_fluids = RecipeHelper.validFluids(salt_fission);
		fusion_valid_fluids = RecipeHelper.validFluids(fusion);
		coolant_heater_valid_fluids = RecipeHelper.validFluids(coolant_heater);
		fission_emergency_cooling_valid_fluids = RecipeHelper.validFluids(fission_emergency_cooling);
		heat_exchanger_valid_fluids = RecipeHelper.validFluids(heat_exchanger);
		condenser_valid_fluids = RecipeHelper.validFluids(condenser);
		turbine_valid_fluids = RecipeHelper.validFluids(turbine);
		radiation_scrubber_valid_fluids = RecipeHelper.validFluids(radiation_scrubber);
	}
	
	public static void refreshRecipeCaches() {
		manufactory.refreshCache();
		separator.refreshCache();
		decay_hastener.refreshCache();
		fuel_reprocessor.refreshCache();
		alloy_furnace.refreshCache();
		infuser.refreshCache();
		melter.refreshCache();
		supercooler.refreshCache();
		electrolyzer.refreshCache();
		assembler.refreshCache();
		ingot_former.refreshCache();
		pressurizer.refreshCache();
		chemical_reactor.refreshCache();
		salt_mixer.refreshCache();
		crystallizer.refreshCache();
		enricher.refreshCache();
		extractor.refreshCache();
		centrifuge.refreshCache();
		rock_crusher.refreshCache();
		collector.refreshCache();
		decay_generator.refreshCache();
		fission_moderator.refreshCache();
		fission_reflector.refreshCache();
		fission_irradiator.refreshCache();
		pebble_fission.refreshCache();
		solid_fission.refreshCache();
		fission_heating.refreshCache();
		salt_fission.refreshCache();
		fusion.refreshCache();
		coolant_heater.refreshCache();
		fission_emergency_cooling.refreshCache();
		heat_exchanger.refreshCache();
		condenser.refreshCache();
		turbine.refreshCache();
		radiation_scrubber.refreshCache();
		radiation_block_mutation.refreshCache();
		radiation_block_purification.refreshCache();
	}
}
