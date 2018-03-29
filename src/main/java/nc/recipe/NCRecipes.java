package nc.recipe;

import nc.recipe.generator.FissionRecipes;
import nc.recipe.generator.FusionRecipes;
import nc.recipe.processor.AlloyFurnaceRecipes;
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
import nc.recipe.processor.SaltMixerRecipes;
import nc.recipe.processor.SupercoolerRecipes;

public class NCRecipes {
	
	public static boolean initialized = false;
	
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
	private static FissionRecipes fission;
	private static FusionRecipes fusion;
	
	public static void init() {
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
		fission = new FissionRecipes();
		fusion = new FusionRecipes();
		
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
		FISSION,
		FUSION;
		
		public BaseRecipeHandler getRecipeHandler() {
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
			case FISSION:
				return fission;
			case FUSION:
				return fusion;
			default:
				return manufactory;
			}
		}
	}
}
