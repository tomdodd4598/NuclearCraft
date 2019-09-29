package nc.recipe;

import java.util.List;

import nc.radiation.RadBlockEffects;
import nc.radiation.RadSources;
import nc.recipe.generator.DecayGeneratorRecipes;
import nc.recipe.generator.FusionRecipes;
import nc.recipe.multiblock.CondenserRecipes;
import nc.recipe.multiblock.CoolantHeaterRecipes;
import nc.recipe.multiblock.FissionModeratorRecipes;
import nc.recipe.multiblock.SolidFissionRecipes;
import nc.recipe.multiblock.FissionReflectorRecipes;
import nc.recipe.multiblock.HeatExchangerRecipes;
import nc.recipe.multiblock.SaltFissionRecipes;
import nc.recipe.multiblock.TurbineRecipes;
import nc.recipe.other.CollectorRecipes;
import nc.recipe.processor.AlloyFurnaceRecipes;
import nc.recipe.processor.CentrifugeRecipes;
import nc.recipe.processor.ChemicalReactorRecipes;
import nc.recipe.processor.CrystallizerRecipes;
import nc.recipe.processor.DecayHastenerRecipes;
import nc.recipe.processor.DissolverRecipes;
import nc.recipe.processor.ElectrolyserRecipes;
import nc.recipe.processor.ExtractorRecipes;
import nc.recipe.processor.FuelReprocessorRecipes;
import nc.recipe.processor.InfuserRecipes;
import nc.recipe.processor.IngotFormerRecipes;
import nc.recipe.processor.IrradiatorRecipes;
import nc.recipe.processor.IsotopeSeparatorRecipes;
import nc.recipe.processor.ManufactoryRecipes;
import nc.recipe.processor.MelterRecipes;
import nc.recipe.processor.PressurizerRecipes;
import nc.recipe.processor.RockCrusherRecipes;
import nc.recipe.processor.SaltMixerRecipes;
import nc.recipe.processor.SupercoolerRecipes;
import nc.recipe.vanilla.CraftingRecipeHandler;
import nc.recipe.vanilla.FurnaceFuelHandler;
import nc.recipe.vanilla.FurnaceRecipeHandler;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class NCRecipes {
	
	private static boolean initialized = false;
	
	public static ManufactoryRecipes manufactory;
	public static IsotopeSeparatorRecipes isotope_separator;
	public static DecayHastenerRecipes decay_hastener;
	public static FuelReprocessorRecipes fuel_reprocessor;
	public static AlloyFurnaceRecipes alloy_furnace;
	public static InfuserRecipes infuser;
	public static MelterRecipes melter;
	public static SupercoolerRecipes supercooler;
	public static ElectrolyserRecipes electrolyser;
	public static IrradiatorRecipes irradiator;
	public static IngotFormerRecipes ingot_former;
	public static PressurizerRecipes pressurizer;
	public static ChemicalReactorRecipes chemical_reactor;
	public static SaltMixerRecipes salt_mixer;
	public static CrystallizerRecipes crystallizer;
	public static DissolverRecipes dissolver;
	public static ExtractorRecipes extractor;
	public static CentrifugeRecipes centrifuge;
	public static RockCrusherRecipes rock_crusher;
	public static CollectorRecipes collector;
	public static DecayGeneratorRecipes decay_generator;
	public static SolidFissionRecipes solid_fission;
	public static FusionRecipes fusion;
	public static FissionModeratorRecipes fission_moderator;
	public static FissionReflectorRecipes fission_reflector;
	public static SaltFissionRecipes salt_fission;
	public static CoolantHeaterRecipes coolant_heater;
	public static HeatExchangerRecipes heat_exchanger;
	public static TurbineRecipes turbine;
	public static CondenserRecipes condenser;
	public static RadBlockEffects radiation_block_mutations;
	
	@SubscribeEvent(priority = EventPriority.LOW)
	public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		if (initialized) return;
		
		manufactory = new ManufactoryRecipes();
		isotope_separator = new IsotopeSeparatorRecipes();
		decay_hastener = new DecayHastenerRecipes();
		fuel_reprocessor = new FuelReprocessorRecipes();
		alloy_furnace = new AlloyFurnaceRecipes();
		infuser = new InfuserRecipes();
		melter = new MelterRecipes();
		supercooler = new SupercoolerRecipes();
		electrolyser = new ElectrolyserRecipes();
		irradiator = new IrradiatorRecipes();
		ingot_former = new IngotFormerRecipes();
		pressurizer = new PressurizerRecipes();
		chemical_reactor = new ChemicalReactorRecipes();
		salt_mixer = new SaltMixerRecipes();
		crystallizer = new CrystallizerRecipes();
		dissolver = new DissolverRecipes();
		extractor = new ExtractorRecipes();
		centrifuge = new CentrifugeRecipes();
		rock_crusher = new RockCrusherRecipes();
		collector = new CollectorRecipes();
		decay_generator = new DecayGeneratorRecipes();
		solid_fission = new SolidFissionRecipes();
		fusion = new FusionRecipes();
		fission_moderator = new FissionModeratorRecipes();
		fission_reflector = new FissionReflectorRecipes();
		salt_fission = new SaltFissionRecipes();
		coolant_heater = new CoolantHeaterRecipes();
		heat_exchanger = new HeatExchangerRecipes();
		turbine = new TurbineRecipes();
		condenser = new CondenserRecipes();
		radiation_block_mutations = new RadBlockEffects();
		
		CraftingRecipeHandler.registerCraftingRecipes();
		FurnaceRecipeHandler.registerFurnaceRecipes();
		GameRegistry.registerFuelHandler(new FurnaceFuelHandler());
		
		RadSources.init();
		
		initialized = true;
	}
	
	public static List<List<String>> infuser_valid_fluids;
	public static List<List<String>> melter_valid_fluids;
	public static List<List<String>> supercooler_valid_fluids;
	public static List<List<String>> electrolyser_valid_fluids;
	public static List<List<String>> irradiator_valid_fluids;
	public static List<List<String>> ingot_former_valid_fluids;
	public static List<List<String>> chemical_reactor_valid_fluids;
	public static List<List<String>> salt_mixer_valid_fluids;
	public static List<List<String>> crystallizer_valid_fluids;
	public static List<List<String>> dissolver_valid_fluids;
	public static List<List<String>> extractor_valid_fluids;
	public static List<List<String>> centrifuge_valid_fluids;
	public static List<List<String>> fusion_valid_fluids;
	public static List<List<String>> salt_fission_valid_fluids;
	public static List<List<String>> coolant_heater_valid_fluids;
	public static List<List<String>> heat_exchanger_valid_fluids;
	public static List<List<String>> turbine_valid_fluids;
	public static List<List<String>> condenser_valid_fluids;
	
	public static void init() {
		infuser_valid_fluids = RecipeHelper.validFluids(infuser);
		melter_valid_fluids = RecipeHelper.validFluids(melter);
		supercooler_valid_fluids = RecipeHelper.validFluids(supercooler);
		electrolyser_valid_fluids = RecipeHelper.validFluids(electrolyser);
		irradiator_valid_fluids = RecipeHelper.validFluids(irradiator);
		ingot_former_valid_fluids = RecipeHelper.validFluids(ingot_former);
		chemical_reactor_valid_fluids = RecipeHelper.validFluids(chemical_reactor);
		salt_mixer_valid_fluids = RecipeHelper.validFluids(salt_mixer);
		crystallizer_valid_fluids = RecipeHelper.validFluids(crystallizer);
		dissolver_valid_fluids = RecipeHelper.validFluids(dissolver);
		extractor_valid_fluids = RecipeHelper.validFluids(extractor);
		centrifuge_valid_fluids = RecipeHelper.validFluids(centrifuge);
		fusion_valid_fluids = RecipeHelper.validFluids(fusion);
		salt_fission_valid_fluids = RecipeHelper.validFluids(salt_fission);
		coolant_heater_valid_fluids = RecipeHelper.validFluids(coolant_heater);
		heat_exchanger_valid_fluids = RecipeHelper.validFluids(heat_exchanger);
		turbine_valid_fluids = RecipeHelper.validFluids(turbine);
		condenser_valid_fluids = RecipeHelper.validFluids(condenser);
	}
	
	public static void refreshRecipeCaches() {
		manufactory.refreshCache();
		isotope_separator.refreshCache();
		decay_hastener.refreshCache();
		fuel_reprocessor.refreshCache();
		alloy_furnace.refreshCache();
		infuser.refreshCache();
		melter.refreshCache();
		supercooler.refreshCache();
		electrolyser.refreshCache();
		irradiator.refreshCache();
		ingot_former.refreshCache();
		pressurizer.refreshCache();
		chemical_reactor.refreshCache();
		salt_mixer.refreshCache();
		crystallizer.refreshCache();
		dissolver.refreshCache();
		extractor.refreshCache();
		centrifuge.refreshCache();
		rock_crusher.refreshCache();
		collector.refreshCache();
		decay_generator.refreshCache();
		solid_fission.refreshCache();
		fusion.refreshCache();
		fission_moderator.refreshCache();
		fission_reflector.refreshCache();
		salt_fission.refreshCache();
		coolant_heater.refreshCache();
		heat_exchanger.refreshCache();
		turbine.refreshCache();
		condenser.refreshCache();
		radiation_block_mutations.refreshCache();
	}
}
