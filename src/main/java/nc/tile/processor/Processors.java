package nc.tile.processor;

import nc.block.tile.processor.BlockAlloyFurnace;
import nc.block.tile.processor.BlockChemicalReactor;
import nc.block.tile.processor.BlockCrystallizer;
import nc.block.tile.processor.BlockDecayHastener;
import nc.block.tile.processor.BlockDissolver;
import nc.block.tile.processor.BlockElectrolyser;
import nc.block.tile.processor.BlockFuelReprocessor;
import nc.block.tile.processor.BlockInfuser;
import nc.block.tile.processor.BlockIngotFormer;
import nc.block.tile.processor.BlockIrradiator;
import nc.block.tile.processor.BlockIsotopeSeparator;
import nc.block.tile.processor.BlockManufactory;
import nc.block.tile.processor.BlockMelter;
import nc.block.tile.processor.BlockPressurizer;
import nc.block.tile.processor.BlockSaltMixer;
import nc.block.tile.processor.BlockSupercooler;
import nc.config.NCConfig;
import nc.recipe.NCRecipes;

public class Processors {
	
	public static class TileManufactory extends TileEnergyItemProcessor {

		public TileManufactory() {
			super("manufactory", 1, 1, NCConfig.processor_time[0], NCConfig.processor_power[0], NCRecipes.MANUFACTORY_RECIPES, 1);
		}
		
		public void setBlockState() {
			BlockManufactory.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileIsotopeSeparator extends TileEnergyItemProcessor {

		public TileIsotopeSeparator() {
			super("isotope_separator", 1, 2, NCConfig.processor_time[1], NCConfig.processor_power[1], NCRecipes.ISOTOPE_SEPARATOR_RECIPES, 2);
		}
		
		public void setBlockState() {
			BlockIsotopeSeparator.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileDecayHastener extends TileEnergyItemProcessor {

		public TileDecayHastener() {
			super("decay_hastener", 1, 1, NCConfig.processor_time[2], NCConfig.processor_power[2], NCRecipes.DECAY_HASTENER_RECIPES, 3);
		}
		
		public void setBlockState() {
			BlockDecayHastener.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileFuelReprocessor extends TileEnergyItemProcessor {

		public TileFuelReprocessor() {
			super("fuel_reprocessor", 1, 4, NCConfig.processor_time[3], NCConfig.processor_power[3], NCRecipes.FUEL_REPROCESSOR_RECIPES, 4);
		}
		
		public void setBlockState() {
			BlockFuelReprocessor.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileAlloyFurnace extends TileEnergyItemProcessor {

		public TileAlloyFurnace() {
			super("alloy_furnace", 2, 1, NCConfig.processor_time[4], NCConfig.processor_power[4], NCRecipes.ALLOY_FURNACE_RECIPES, 5);
		}
		
		public void setBlockState() {
			BlockAlloyFurnace.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileInfuser extends TileEnergyItemFluidProcessor {

		public TileInfuser() {
			super("infuser", 1, 1, 1, 0, tankCapacities(16000, 1, 0), fluidConnections(1, 0), validFluids(NCRecipes.INFUSER_RECIPES), NCConfig.processor_time[5], NCConfig.processor_power[5], NCRecipes.INFUSER_RECIPES, 6);
		}
		
		public void setBlockState() {
			BlockInfuser.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileMelter extends TileEnergyItemFluidProcessor {

		public TileMelter() {
			super("melter", 1, 0, 0, 1, tankCapacities(16000, 0, 1), fluidConnections(0, 1), validFluids(NCRecipes.MELTER_RECIPES), NCConfig.processor_time[6], NCConfig.processor_power[6], NCRecipes.MELTER_RECIPES, 7);
		}
		
		public void setBlockState() {
			BlockMelter.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileSupercooler extends TileEnergyFluidProcessor {

		public TileSupercooler() {
			super("supercooler", 1, 1, tankCapacities(16000, 1, 1), fluidConnections(1, 1), validFluids(NCRecipes.SUPERCOOLER_RECIPES), NCConfig.processor_time[7], NCConfig.processor_power[7], NCRecipes.SUPERCOOLER_RECIPES, 8);
		}
		
		public void setBlockState() {
			BlockSupercooler.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileElectrolyser extends TileEnergyFluidProcessor {

		public TileElectrolyser() {
			super("electrolyser", 1, 4, tankCapacities(16000, 1, 4), fluidConnections(1, 4), validFluids(NCRecipes.ELECTROLYSER_RECIPES), NCConfig.processor_time[8], NCConfig.processor_power[8], NCRecipes.ELECTROLYSER_RECIPES, 9);
		}
		
		public void setBlockState() {
			BlockElectrolyser.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileIrradiator extends TileEnergyFluidProcessor {

		public TileIrradiator() {
			super("irradiator", 2, 2, tankCapacities(16000, 2, 2), fluidConnections(2, 2), new String[][] {validFluids(NCRecipes.IRRADIATOR_RECIPES, "neutron")[0], {"neutron"}, {}, {}}, NCConfig.processor_time[9], NCConfig.processor_power[9], NCRecipes.IRRADIATOR_RECIPES, 10);
		}
		
		public void setBlockState() {
			BlockIrradiator.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileIngotFormer extends TileEnergyItemFluidProcessor {

		public TileIngotFormer() {
			super("ingot_former", 0, 1, 1, 0, tankCapacities(16000, 1, 0), fluidConnections(1, 0), validFluids(NCRecipes.INGOT_FORMER_RECIPES), NCConfig.processor_time[10], NCConfig.processor_power[10], NCRecipes.INGOT_FORMER_RECIPES, 11);
		}
		
		public void setBlockState() {
			BlockIngotFormer.setState(isProcessing, world, pos);
		}
	}
	
	public static class TilePressurizer extends TileEnergyItemProcessor {

		public TilePressurizer() {
			super("pressurizer", 1, 1, NCConfig.processor_time[11], NCConfig.processor_power[11], NCRecipes.PRESSURIZER_RECIPES, 12);
		}
		
		public void setBlockState() {
			BlockPressurizer.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileChemicalReactor extends TileEnergyFluidProcessor {

		public TileChemicalReactor() {
			super("chemical_reactor", 2, 2, tankCapacities(16000, 2, 2), fluidConnections(2, 2), validFluids(NCRecipes.CHEMICAL_REACTOR_RECIPES), NCConfig.processor_time[12], NCConfig.processor_power[12], NCRecipes.CHEMICAL_REACTOR_RECIPES, 13);
		}
		
		public void setBlockState() {
			BlockChemicalReactor.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileSaltMixer extends TileEnergyFluidProcessor {

		public TileSaltMixer() {
			super("salt_mixer", 2, 1, tankCapacities(16000, 2, 1), fluidConnections(2, 1), validFluids(NCRecipes.SALT_MIXER_RECIPES), NCConfig.processor_time[13], NCConfig.processor_power[13], NCRecipes.SALT_MIXER_RECIPES, 14);
		}
		
		public void setBlockState() {
			BlockSaltMixer.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileCrystallizer extends TileEnergyItemFluidProcessor {

		public TileCrystallizer() {
			super("crystallizer", 0, 1, 1, 0, tankCapacities(16000, 1, 0), fluidConnections(1, 0), validFluids(NCRecipes.CRYSTALLIZER_RECIPES), NCConfig.processor_time[14], NCConfig.processor_power[14], NCRecipes.CRYSTALLIZER_RECIPES, 15);
		}
		
		public void setBlockState() {
			BlockCrystallizer.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileDissolver extends TileEnergyItemFluidProcessor {

		public TileDissolver() {
			super("dissolver", 1, 1, 0, 1, tankCapacities(16000, 1, 1), fluidConnections(1, 1), validFluids(NCRecipes.DISSOLVER_RECIPES), NCConfig.processor_time[15], NCConfig.processor_power[15], NCRecipes.DISSOLVER_RECIPES, 16);
		}
		
		public void setBlockState() {
			BlockDissolver.setState(isProcessing, world, pos);
		}
	}
}
