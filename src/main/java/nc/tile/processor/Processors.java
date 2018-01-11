package nc.tile.processor;

import nc.config.NCConfig;
import nc.recipe.NCRecipes;
import nc.recipe.RecipeMethods;

public class Processors {
	
	public static class TileManufactory extends TileItemProcessor {

		public TileManufactory() {
			super("manufactory", 1, 1, NCConfig.processor_time[0], NCConfig.processor_power[0], NCRecipes.MANUFACTORY_RECIPES, 1);
		}
	}
	
	public static class TileIsotopeSeparator extends TileItemProcessor {

		public TileIsotopeSeparator() {
			super("isotope_separator", 1, 2, NCConfig.processor_time[1], NCConfig.processor_power[1], NCRecipes.ISOTOPE_SEPARATOR_RECIPES, 2);
		}
	}
	
	public static class TileDecayHastener extends TileItemProcessor {

		public TileDecayHastener() {
			super("decay_hastener", 1, 1, NCConfig.processor_time[2], NCConfig.processor_power[2], NCRecipes.DECAY_HASTENER_RECIPES, 3);
		}
	}
	
	public static class TileFuelReprocessor extends TileItemProcessor {

		public TileFuelReprocessor() {
			super("fuel_reprocessor", 1, 4, NCConfig.processor_time[3], NCConfig.processor_power[3], NCRecipes.FUEL_REPROCESSOR_RECIPES, 4);
		}
	}
	
	public static class TileAlloyFurnace extends TileItemProcessor {

		public TileAlloyFurnace() {
			super("alloy_furnace", 2, 1, NCConfig.processor_time[4], NCConfig.processor_power[4], NCRecipes.ALLOY_FURNACE_RECIPES, 5);
		}
	}
	
	public static class TileInfuser extends TileItemFluidProcessor {

		public TileInfuser() {
			super("infuser", 1, 1, 1, 0, tankCapacities(16000, 1, 0), fluidConnections(1, 0), RecipeMethods.validFluids(NCRecipes.INFUSER_RECIPES), NCConfig.processor_time[5], NCConfig.processor_power[5], NCRecipes.INFUSER_RECIPES, 6);
		}
	}
	
	public static class TileMelter extends TileItemFluidProcessor {

		public TileMelter() {
			super("melter", 1, 0, 0, 1, tankCapacities(16000, 0, 1), fluidConnections(0, 1), RecipeMethods.validFluids(NCRecipes.MELTER_RECIPES), NCConfig.processor_time[6], NCConfig.processor_power[6], NCRecipes.MELTER_RECIPES, 7);
		}
	}
	
	public static class TileSupercooler extends TileFluidProcessor {

		public TileSupercooler() {
			super("supercooler", 1, 1, tankCapacities(16000, 1, 1), fluidConnections(1, 1), RecipeMethods.validFluids(NCRecipes.SUPERCOOLER_RECIPES), NCConfig.processor_time[7], NCConfig.processor_power[7], NCRecipes.SUPERCOOLER_RECIPES, 8);
		}
	}
	
	public static class TileElectrolyser extends TileFluidProcessor {

		public TileElectrolyser() {
			super("electrolyser", 1, 4, tankCapacities(16000, 1, 4), fluidConnections(1, 4), RecipeMethods.validFluids(NCRecipes.ELECTROLYSER_RECIPES), NCConfig.processor_time[8], NCConfig.processor_power[8], NCRecipes.ELECTROLYSER_RECIPES, 9);
		}
	}
	
	public static class TileIrradiator extends TileFluidProcessor {

		public TileIrradiator() {
			super("irradiator", 2, 2, tankCapacities(16000, 2, 2), fluidConnections(2, 2), new String[][] {RecipeMethods.validFluids(NCRecipes.IRRADIATOR_RECIPES, "neutron")[0], {"neutron"}, {}, {}}, NCConfig.processor_time[9], NCConfig.processor_power[9], NCRecipes.IRRADIATOR_RECIPES, 10);
		}
	}
	
	public static class TileIngotFormer extends TileItemFluidProcessor {

		public TileIngotFormer() {
			super("ingot_former", 0, 1, 1, 0, tankCapacities(16000, 1, 0), fluidConnections(1, 0), RecipeMethods.validFluids(NCRecipes.INGOT_FORMER_RECIPES), NCConfig.processor_time[10], NCConfig.processor_power[10], NCRecipes.INGOT_FORMER_RECIPES, 11);
		}
	}
	
	public static class TilePressurizer extends TileItemProcessor {

		public TilePressurizer() {
			super("pressurizer", 1, 1, NCConfig.processor_time[11], NCConfig.processor_power[11], NCRecipes.PRESSURIZER_RECIPES, 12);
		}
	}
	
	public static class TileChemicalReactor extends TileFluidProcessor {

		public TileChemicalReactor() {
			super("chemical_reactor", 2, 2, tankCapacities(16000, 2, 2), fluidConnections(2, 2), RecipeMethods.validFluids(NCRecipes.CHEMICAL_REACTOR_RECIPES), NCConfig.processor_time[12], NCConfig.processor_power[12], NCRecipes.CHEMICAL_REACTOR_RECIPES, 13);
		}
	}
	
	public static class TileSaltMixer extends TileFluidProcessor {

		public TileSaltMixer() {
			super("salt_mixer", 2, 1, tankCapacities(16000, 2, 1), fluidConnections(2, 1), RecipeMethods.validFluids(NCRecipes.SALT_MIXER_RECIPES), NCConfig.processor_time[13], NCConfig.processor_power[13], NCRecipes.SALT_MIXER_RECIPES, 14);
		}
	}
	
	public static class TileCrystallizer extends TileItemFluidProcessor {

		public TileCrystallizer() {
			super("crystallizer", 0, 1, 1, 0, tankCapacities(16000, 1, 0), fluidConnections(1, 0), RecipeMethods.validFluids(NCRecipes.CRYSTALLIZER_RECIPES), NCConfig.processor_time[14], NCConfig.processor_power[14], NCRecipes.CRYSTALLIZER_RECIPES, 15);
		}
	}
	
	public static class TileDissolver extends TileItemFluidProcessor {

		public TileDissolver() {
			super("dissolver", 1, 1, 0, 1, tankCapacities(16000, 1, 1), fluidConnections(1, 1), RecipeMethods.validFluids(NCRecipes.DISSOLVER_RECIPES), NCConfig.processor_time[15], NCConfig.processor_power[15], NCRecipes.DISSOLVER_RECIPES, 16);
		}
	}
}
