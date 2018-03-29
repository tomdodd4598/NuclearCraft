package nc.tile.processor;

import nc.config.NCConfig;
import nc.recipe.NCRecipes;
import nc.recipe.RecipeMethods;

public class TileProcessor {
	
	public static class Manufactory extends TileItemProcessor {

		public Manufactory() {
			super("manufactory", 1, 1, NCConfig.processor_time[0], NCConfig.processor_power[0], NCRecipes.Type.MANUFACTORY, 1);
		}
	}
	
	public static class IsotopeSeparator extends TileItemProcessor {

		public IsotopeSeparator() {
			super("isotope_separator", 1, 2, NCConfig.processor_time[1], NCConfig.processor_power[1], NCRecipes.Type.ISOTOPE_SEPARATOR, 2);
		}
	}
	
	public static class DecayHastener extends TileItemProcessor {

		public DecayHastener() {
			super("decay_hastener", 1, 1, NCConfig.processor_time[2], NCConfig.processor_power[2], NCRecipes.Type.DECAY_HASTENER, 3);
		}
	}
	
	public static class FuelReprocessor extends TileItemProcessor {

		public FuelReprocessor() {
			super("fuel_reprocessor", 1, 4, NCConfig.processor_time[3], NCConfig.processor_power[3], NCRecipes.Type.FUEL_REPROCESSOR, 4);
		}
	}
	
	public static class AlloyFurnace extends TileItemProcessor {

		public AlloyFurnace() {
			super("alloy_furnace", 2, 1, NCConfig.processor_time[4], NCConfig.processor_power[4], NCRecipes.Type.ALLOY_FURNACE, 5);
		}
	}
	
	public static class Infuser extends TileItemFluidProcessor {

		public Infuser() {
			super("infuser", 1, 1, 1, 0, tankCapacities(16000, 1, 0), fluidConnections(1, 0), RecipeMethods.validFluids(NCRecipes.Type.INFUSER), NCConfig.processor_time[5], NCConfig.processor_power[5], NCRecipes.Type.INFUSER, 6);
		}
	}
	
	public static class Melter extends TileItemFluidProcessor {

		public Melter() {
			super("melter", 1, 0, 0, 1, tankCapacities(16000, 0, 1), fluidConnections(0, 1), RecipeMethods.validFluids(NCRecipes.Type.MELTER), NCConfig.processor_time[6], NCConfig.processor_power[6], NCRecipes.Type.MELTER, 7);
		}
	}
	
	public static class Supercooler extends TileFluidProcessor {

		public Supercooler() {
			super("supercooler", 1, 1, tankCapacities(16000, 1, 1), fluidConnections(1, 1), RecipeMethods.validFluids(NCRecipes.Type.SUPERCOOLER), NCConfig.processor_time[7], NCConfig.processor_power[7], NCRecipes.Type.SUPERCOOLER, 8);
		}
	}
	
	public static class Electrolyser extends TileFluidProcessor {

		public Electrolyser() {
			super("electrolyser", 1, 4, tankCapacities(16000, 1, 4), fluidConnections(1, 4), RecipeMethods.validFluids(NCRecipes.Type.ELECTROLYSER), NCConfig.processor_time[8], NCConfig.processor_power[8], NCRecipes.Type.ELECTROLYSER, 9);
		}
	}
	
	public static class Irradiator extends TileFluidProcessor {

		public Irradiator() {
			super("irradiator", 2, 2, tankCapacities(16000, 2, 2), fluidConnections(2, 2), new String[][] {RecipeMethods.validFluids(NCRecipes.Type.IRRADIATOR, "neutron")[0], {"neutron"}, {}, {}}, NCConfig.processor_time[9], NCConfig.processor_power[9], NCRecipes.Type.IRRADIATOR, 10);
		}
	}
	
	public static class IngotFormer extends TileItemFluidProcessor {

		public IngotFormer() {
			super("ingot_former", 0, 1, 1, 0, tankCapacities(16000, 1, 0), fluidConnections(1, 0), RecipeMethods.validFluids(NCRecipes.Type.INGOT_FORMER), NCConfig.processor_time[10], NCConfig.processor_power[10], NCRecipes.Type.INGOT_FORMER, 11);
		}
	}
	
	public static class Pressurizer extends TileItemProcessor {

		public Pressurizer() {
			super("pressurizer", 1, 1, NCConfig.processor_time[11], NCConfig.processor_power[11], NCRecipes.Type.PRESSURIZER, 12);
		}
	}
	
	public static class ChemicalReactor extends TileFluidProcessor {

		public ChemicalReactor() {
			super("chemical_reactor", 2, 2, tankCapacities(16000, 2, 2), fluidConnections(2, 2), RecipeMethods.validFluids(NCRecipes.Type.CHEMICAL_REACTOR), NCConfig.processor_time[12], NCConfig.processor_power[12], NCRecipes.Type.CHEMICAL_REACTOR, 13);
		}
	}
	
	public static class SaltMixer extends TileFluidProcessor {

		public SaltMixer() {
			super("salt_mixer", 2, 1, tankCapacities(16000, 2, 1), fluidConnections(2, 1), RecipeMethods.validFluids(NCRecipes.Type.SALT_MIXER), NCConfig.processor_time[13], NCConfig.processor_power[13], NCRecipes.Type.SALT_MIXER, 14);
		}
	}
	
	public static class Crystallizer extends TileItemFluidProcessor {

		public Crystallizer() {
			super("crystallizer", 0, 1, 1, 0, tankCapacities(16000, 1, 0), fluidConnections(1, 0), RecipeMethods.validFluids(NCRecipes.Type.CRYSTALLIZER), NCConfig.processor_time[14], NCConfig.processor_power[14], NCRecipes.Type.CRYSTALLIZER, 15);
		}
	}
	
	public static class Dissolver extends TileItemFluidProcessor {

		public Dissolver() {
			super("dissolver", 1, 1, 0, 1, tankCapacities(16000, 1, 1), fluidConnections(1, 1), RecipeMethods.validFluids(NCRecipes.Type.DISSOLVER), NCConfig.processor_time[15], NCConfig.processor_power[15], NCRecipes.Type.DISSOLVER, 16);
		}
	}
	
	public static class Extractor extends TileItemFluidProcessor {

		public Extractor() {
			super("extractor", 1, 0, 1, 1, tankCapacities(16000, 0, 1), fluidConnections(0, 1), RecipeMethods.validFluids(NCRecipes.Type.EXTRACTOR), NCConfig.processor_time[16], NCConfig.processor_power[16], NCRecipes.Type.EXTRACTOR, 17);
		}
	}
}
