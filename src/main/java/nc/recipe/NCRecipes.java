package nc.recipe;

import nc.recipe.generator.FissionRecipes;
import nc.recipe.generator.FusionRecipes;
import nc.recipe.processor.AlloyFurnaceRecipes;
import nc.recipe.processor.ChemicalReactorRecipes;
import nc.recipe.processor.CrystallizerRecipes;
import nc.recipe.processor.DecayHastenerRecipes;
import nc.recipe.processor.DissolverRecipes;
import nc.recipe.processor.ElectrolyserRecipes;
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

public final class NCRecipes {
	public static final ManufactoryRecipes MANUFACTORY_RECIPES = new ManufactoryRecipes();
	public static final IsotopeSeparatorRecipes ISOTOPE_SEPARATOR_RECIPES = new IsotopeSeparatorRecipes();
	public static final DecayHastenerRecipes DECAY_HASTENER_RECIPES = new DecayHastenerRecipes();
	public static final FuelReprocessorRecipes FUEL_REPROCESSOR_RECIPES = new FuelReprocessorRecipes();
	public static final AlloyFurnaceRecipes ALLOY_FURNACE_RECIPES = new AlloyFurnaceRecipes();
	public static final InfuserRecipes INFUSER_RECIPES = new InfuserRecipes();
	public static final MelterRecipes MELTER_RECIPES = new MelterRecipes();
	public static final SupercoolerRecipes SUPERCOOLER_RECIPES = new SupercoolerRecipes();
	public static final ElectrolyserRecipes ELECTROLYSER_RECIPES = new ElectrolyserRecipes();
	public static final IrradiatorRecipes IRRADIATOR_RECIPES = new IrradiatorRecipes();
	public static final IngotFormerRecipes INGOT_FORMER_RECIPES = new IngotFormerRecipes();
	public static final PressurizerRecipes PRESSURIZER_RECIPES = new PressurizerRecipes();
	public static final ChemicalReactorRecipes CHEMICAL_REACTOR_RECIPES = new ChemicalReactorRecipes();
	public static final SaltMixerRecipes SALT_MIXER_RECIPES = new SaltMixerRecipes();
	public static final CrystallizerRecipes CRYSTALLIZER_RECIPES = new CrystallizerRecipes();
	public static final DissolverRecipes DISSOLVER_RECIPES = new DissolverRecipes();
	public static final FissionRecipes FISSION_RECIPES = new FissionRecipes();
	public static final FusionRecipes FUSION_RECIPES = new FusionRecipes();
}
