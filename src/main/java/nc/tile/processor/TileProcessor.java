package nc.tile.processor;

import java.util.ArrayList;
import java.util.Arrays;

import nc.config.NCConfig;
import nc.recipe.NCRecipes;
import nc.util.RecipeHelper;

public class TileProcessor {
	
	public static class Manufactory extends TileItemProcessor {

		public Manufactory() {
			super("manufactory", 1, 1, NCConfig.processor_time[0], NCConfig.processor_power[0], false, NCRecipes.Type.MANUFACTORY, 1);
		}
	}
	
	public static class IsotopeSeparator extends TileItemProcessor {

		public IsotopeSeparator() {
			super("isotope_separator", 1, 2, NCConfig.processor_time[1], NCConfig.processor_power[1], false, NCRecipes.Type.ISOTOPE_SEPARATOR, 2);
		}
	}
	
	public static class DecayHastener extends TileItemProcessor {

		public DecayHastener() {
			super("decay_hastener", 1, 1, NCConfig.processor_time[2], NCConfig.processor_power[2], false, NCRecipes.Type.DECAY_HASTENER, 3);
		}
	}
	
	public static class FuelReprocessor extends TileItemProcessor {

		public FuelReprocessor() {
			super("fuel_reprocessor", 1, 4, NCConfig.processor_time[3], NCConfig.processor_power[3], false, NCRecipes.Type.FUEL_REPROCESSOR, 4);
		}
	}
	
	public static class AlloyFurnace extends TileItemProcessor {

		public AlloyFurnace() {
			super("alloy_furnace", 2, 1, NCConfig.processor_time[4], NCConfig.processor_power[4], true, NCRecipes.Type.ALLOY_FURNACE, 5);
		}
	}
	
	public static class Infuser extends TileItemFluidProcessor {

		public Infuser() {
			super("infuser", 1, 1, 1, 0, defaultTankCapacities(16000, 1, 0), defaultFluidConnections(1, 0), RecipeHelper.validFluids(NCRecipes.Type.INFUSER), NCConfig.processor_time[5], NCConfig.processor_power[5], true, NCRecipes.Type.INFUSER, 6);
		}
	}
	
	public static class Melter extends TileItemFluidProcessor {

		public Melter() {
			super("melter", 1, 0, 0, 1, defaultTankCapacities(16000, 0, 1), defaultFluidConnections(0, 1), RecipeHelper.validFluids(NCRecipes.Type.MELTER), NCConfig.processor_time[6], NCConfig.processor_power[6], true, NCRecipes.Type.MELTER, 7);
		}
	}
	
	public static class Supercooler extends TileFluidProcessor {

		public Supercooler() {
			super("supercooler", 1, 1, defaultTankCapacities(16000, 1, 1), defaultFluidConnections(1, 1), RecipeHelper.validFluids(NCRecipes.Type.SUPERCOOLER), NCConfig.processor_time[7], NCConfig.processor_power[7], true, NCRecipes.Type.SUPERCOOLER, 8);
		}
	}
	
	public static class Electrolyser extends TileFluidProcessor {

		public Electrolyser() {
			super("electrolyser", 1, 4, defaultTankCapacities(16000, 1, 4), defaultFluidConnections(1, 4), RecipeHelper.validFluids(NCRecipes.Type.ELECTROLYSER), NCConfig.processor_time[8], NCConfig.processor_power[8], true, NCRecipes.Type.ELECTROLYSER, 9);
		}
	}
	
	public static class Irradiator extends TileFluidProcessor {

		public Irradiator() {
			super("irradiator", 2, 2, defaultTankCapacities(16000, 2, 2), defaultFluidConnections(2, 2), Arrays.asList(RecipeHelper.validFluids(NCRecipes.Type.IRRADIATOR, Arrays.asList("neutron")).get(0), Arrays.asList("neutron"), new ArrayList<String>(), new ArrayList<String>()), NCConfig.processor_time[9], NCConfig.processor_power[9], false, NCRecipes.Type.IRRADIATOR, 10);
		}
	}
	
	public static class IngotFormer extends TileItemFluidProcessor {

		public IngotFormer() {
			super("ingot_former", 0, 1, 1, 0, defaultTankCapacities(16000, 1, 0), defaultFluidConnections(1, 0), RecipeHelper.validFluids(NCRecipes.Type.INGOT_FORMER), NCConfig.processor_time[10], NCConfig.processor_power[10], false, NCRecipes.Type.INGOT_FORMER, 11);
		}
	}
	
	public static class Pressurizer extends TileItemProcessor {

		public Pressurizer() {
			super("pressurizer", 1, 1, NCConfig.processor_time[11], NCConfig.processor_power[11], true, NCRecipes.Type.PRESSURIZER, 12);
		}
	}
	
	public static class ChemicalReactor extends TileFluidProcessor {

		public ChemicalReactor() {
			super("chemical_reactor", 2, 2, defaultTankCapacities(16000, 2, 2), defaultFluidConnections(2, 2), RecipeHelper.validFluids(NCRecipes.Type.CHEMICAL_REACTOR), NCConfig.processor_time[12], NCConfig.processor_power[12], true, NCRecipes.Type.CHEMICAL_REACTOR, 13);
		}
	}
	
	public static class SaltMixer extends TileFluidProcessor {

		public SaltMixer() {
			super("salt_mixer", 2, 1, defaultTankCapacities(16000, 2, 1), defaultFluidConnections(2, 1), RecipeHelper.validFluids(NCRecipes.Type.SALT_MIXER), NCConfig.processor_time[13], NCConfig.processor_power[13], false, NCRecipes.Type.SALT_MIXER, 14);
		}
	}
	
	public static class Crystallizer extends TileItemFluidProcessor {

		public Crystallizer() {
			super("crystallizer", 0, 1, 1, 0, defaultTankCapacities(16000, 1, 0), defaultFluidConnections(1, 0), RecipeHelper.validFluids(NCRecipes.Type.CRYSTALLIZER), NCConfig.processor_time[14], NCConfig.processor_power[14], true, NCRecipes.Type.CRYSTALLIZER, 15);
		}
	}
	
	public static class Dissolver extends TileItemFluidProcessor {

		public Dissolver() {
			super("dissolver", 1, 1, 0, 1, defaultTankCapacities(16000, 1, 1), defaultFluidConnections(1, 1), RecipeHelper.validFluids(NCRecipes.Type.DISSOLVER), NCConfig.processor_time[15], NCConfig.processor_power[15], true, NCRecipes.Type.DISSOLVER, 16);
		}
	}
	
	public static class Extractor extends TileItemFluidProcessor {

		public Extractor() {
			super("extractor", 1, 0, 1, 1, defaultTankCapacities(16000, 0, 1), defaultFluidConnections(0, 1), RecipeHelper.validFluids(NCRecipes.Type.EXTRACTOR), NCConfig.processor_time[16], NCConfig.processor_power[16], false, NCRecipes.Type.EXTRACTOR, 17);
		}
	}
	
	public static class Centrifuge extends TileFluidProcessor {

		public Centrifuge() {
			super("centrifuge", 1, 4, defaultTankCapacities(16000, 1, 4), defaultFluidConnections(1, 4), RecipeHelper.validFluids(NCRecipes.Type.CENTRIFUGE), NCConfig.processor_time[17], NCConfig.processor_power[17], false, NCRecipes.Type.CENTRIFUGE, 18);
		}
	}
	
	public static class RockCrusher extends TileItemProcessor {

		public RockCrusher() {
			super("rock_crusher", 1, 3, NCConfig.processor_time[18], NCConfig.processor_power[18], false, NCRecipes.Type.ROCK_CRUSHER, 19);
		}
	}
}
