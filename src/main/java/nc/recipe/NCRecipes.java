package nc.recipe;

import java.util.List;

import nc.recipe.generator.DecayGeneratorRecipes;
import nc.recipe.generator.FissionRecipes;
import nc.recipe.generator.FusionRecipes;
import nc.recipe.multiblock.CondenserRecipes;
import nc.recipe.multiblock.CoolantHeaterRecipes;
import nc.recipe.multiblock.HeatExchangerRecipes;
import nc.recipe.multiblock.SaltFissionRecipes;
import nc.recipe.multiblock.TurbineRecipes;
import nc.recipe.other.ActiveCoolerRecipes;
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
import nc.util.RecipeHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class NCRecipes {
	
	private static boolean initialized = false;
	
	private static ManufactoryRecipes manufactory;
	private static IsotopeSeparatorRecipes isotope_separator;
	private static DecayHastenerRecipes decay_hastener;
	private static FuelReprocessorRecipes fuel_reprocessor;
	private static AlloyFurnaceRecipes alloy_furnace;
	private static InfuserRecipes infuser;
	private static MelterRecipes melter;
	private static SupercoolerRecipes supercooler;
	private static ElectrolyserRecipes electrolyser;
	private static IrradiatorRecipes irradiator;
	private static IngotFormerRecipes ingot_former;
	private static PressurizerRecipes pressurizer;
	private static ChemicalReactorRecipes chemical_reactor;
	private static SaltMixerRecipes salt_mixer;
	private static CrystallizerRecipes crystallizer;
	private static DissolverRecipes dissolver;
	private static ExtractorRecipes extractor;
	private static CentrifugeRecipes centrifuge;
	private static RockCrusherRecipes rock_crusher;
	private static CollectorRecipes collector;
	private static ActiveCoolerRecipes active_cooler;
	private static DecayGeneratorRecipes decay_generator;
	private static FissionRecipes fission;
	private static FusionRecipes fusion;
	private static SaltFissionRecipes salt_fission;
	private static CoolantHeaterRecipes coolant_heater;
	private static HeatExchangerRecipes heat_exchanger;
	private static TurbineRecipes turbine;
	private static CondenserRecipes condenser;
	
	@SubscribeEvent(priority = EventPriority.LOW)
    public void registerRecipes(RegistryEvent.Register<net.minecraft.item.crafting.IRecipe> event) {
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
		active_cooler = new ActiveCoolerRecipes();
		decay_generator = new DecayGeneratorRecipes();
		fission = new FissionRecipes();
		fusion = new FusionRecipes();
		salt_fission = new SaltFissionRecipes();
		coolant_heater = new CoolantHeaterRecipes();
		heat_exchanger = new HeatExchangerRecipes();
		turbine = new TurbineRecipes();
		condenser = new CondenserRecipes();
		
		CraftingRecipeHandler.registerCraftingRecipes();
		FurnaceRecipeHandler.registerFurnaceRecipes();
		GameRegistry.registerFuelHandler(new FurnaceFuelHandler());
		
		initialized = true;
	}
	
	public static enum Type {
		MANUFACTORY,
		ISOTOPE_SEPARATOR,
		DECAY_HASTENER,
		FUEL_REPROCESSOR,
		ALLOY_FURNACE,
		INFUSER,
		MELTER,
		SUPERCOOLER,
		ELECTROLYSER,
		IRRADIATOR,
		INGOT_FORMER,
		PRESSURIZER,
		CHEMICAL_REACTOR,
		SALT_MIXER,
		CRYSTALLIZER,
		DISSOLVER,
		EXTRACTOR,
		CENTRIFUGE,
		ROCK_CRUSHER,
		COLLECTOR,
		ACTIVE_COOLER,
		DECAY_GENERATOR,
		FISSION,
		FUSION,
		SALT_FISSION,
		COOLANT_HEATER,
		HEAT_EXCHANGER,
		TURBINE,
		CONDENSER;
		
		public ProcessorRecipeHandler getRecipeHandler() {
			switch (this) {
			case MANUFACTORY:
				return manufactory;
			case ISOTOPE_SEPARATOR:
				return isotope_separator;
			case DECAY_HASTENER:
				return decay_hastener;
			case FUEL_REPROCESSOR:
				return fuel_reprocessor;
			case ALLOY_FURNACE:
				return alloy_furnace;
			case INFUSER:
				return infuser;
			case MELTER:
				return melter;
			case SUPERCOOLER:
				return supercooler;
			case ELECTROLYSER:
				return electrolyser;
			case IRRADIATOR:
				return irradiator;
			case INGOT_FORMER:
				return ingot_former;
			case PRESSURIZER:
				return pressurizer;
			case CHEMICAL_REACTOR:
				return chemical_reactor;
			case SALT_MIXER:
				return salt_mixer;
			case CRYSTALLIZER:
				return crystallizer;
			case DISSOLVER:
				return dissolver;
			case EXTRACTOR:
				return extractor;
			case CENTRIFUGE:
				return centrifuge;
			case ROCK_CRUSHER:
				return rock_crusher;
			case COLLECTOR:
				return collector;
			case ACTIVE_COOLER:
				return active_cooler;
			case DECAY_GENERATOR:
				return decay_generator;
			case FISSION:
				return fission;
			case FUSION:
				return fusion;
			case SALT_FISSION:
				return salt_fission;
			case COOLANT_HEATER:
				return coolant_heater;
			case HEAT_EXCHANGER:
				return heat_exchanger;
			case TURBINE:
				return turbine;
			case CONDENSER:
				return condenser;
			default:
				return manufactory;
			}
		}
		
		public String getRecipeName() {
			return getRecipeHandler().getRecipeName();
		}
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
	public static List<List<String>> turbine_valid_fluids;
	
	public static void init() {
		infuser_valid_fluids = RecipeHelper.validFluids(NCRecipes.Type.INFUSER);
		melter_valid_fluids = RecipeHelper.validFluids(NCRecipes.Type.MELTER);
		supercooler_valid_fluids = RecipeHelper.validFluids(NCRecipes.Type.SUPERCOOLER);
		electrolyser_valid_fluids = RecipeHelper.validFluids(NCRecipes.Type.ELECTROLYSER);
		irradiator_valid_fluids = RecipeHelper.validFluids(NCRecipes.Type.IRRADIATOR);
		ingot_former_valid_fluids = RecipeHelper.validFluids(NCRecipes.Type.INGOT_FORMER);
		chemical_reactor_valid_fluids = RecipeHelper.validFluids(NCRecipes.Type.CHEMICAL_REACTOR);
		salt_mixer_valid_fluids = RecipeHelper.validFluids(NCRecipes.Type.SALT_MIXER);
		crystallizer_valid_fluids = RecipeHelper.validFluids(NCRecipes.Type.CRYSTALLIZER);
		dissolver_valid_fluids = RecipeHelper.validFluids(NCRecipes.Type.DISSOLVER);
		extractor_valid_fluids = RecipeHelper.validFluids(NCRecipes.Type.EXTRACTOR);
		centrifuge_valid_fluids = RecipeHelper.validFluids(NCRecipes.Type.CENTRIFUGE);
		fusion_valid_fluids = RecipeHelper.validFluids(NCRecipes.Type.FUSION);
		salt_fission_valid_fluids = RecipeHelper.validFluids(NCRecipes.Type.SALT_FISSION);
		coolant_heater_valid_fluids = RecipeHelper.validFluids(NCRecipes.Type.COOLANT_HEATER);
		turbine_valid_fluids = RecipeHelper.validFluids(NCRecipes.Type.TURBINE);
	}
}
