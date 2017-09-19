package nc.tile.processor;

import nc.block.tile.processor.BlockAlloyFurnace;
import nc.block.tile.processor.BlockChemicalReactor;
import nc.block.tile.processor.BlockCrystallizer;
import nc.block.tile.processor.BlockDecayHastener;
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
import nc.recipe.processor.AlloyFurnaceRecipes;
import nc.recipe.processor.ChemicalReactorRecipes;
import nc.recipe.processor.CrystallizerRecipes;
import nc.recipe.processor.DecayHastenerRecipes;
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

public class Processors {
	
	public static class TileManufactory extends TileEnergyItemProcessor {

		public TileManufactory() {
			super("manufactory", 1, 1, NCConfig.processor_time[0], NCConfig.processor_power[0], ManufactoryRecipes.instance(), 1);
		}
		
		public void setBlockState() {
			BlockManufactory.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileIsotopeSeparator extends TileEnergyItemProcessor {

		public TileIsotopeSeparator() {
			super("isotope_separator", 1, 2, NCConfig.processor_time[1], NCConfig.processor_power[1], IsotopeSeparatorRecipes.instance(), 2);
		}
		
		public void setBlockState() {
			BlockIsotopeSeparator.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileDecayHastener extends TileEnergyItemProcessor {

		public TileDecayHastener() {
			super("decay_hastener", 1, 1, NCConfig.processor_time[2], NCConfig.processor_power[2], DecayHastenerRecipes.instance(), 3);
		}
		
		public void setBlockState() {
			BlockDecayHastener.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileFuelReprocessor extends TileEnergyItemProcessor {

		public TileFuelReprocessor() {
			super("fuel_reprocessor", 1, 4, NCConfig.processor_time[3], NCConfig.processor_power[3], FuelReprocessorRecipes.instance(), 4);
		}
		
		public void setBlockState() {
			BlockFuelReprocessor.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileAlloyFurnace extends TileEnergyItemProcessor {

		public TileAlloyFurnace() {
			super("alloy_furnace", 2, 1, NCConfig.processor_time[4], NCConfig.processor_power[4], AlloyFurnaceRecipes.instance(), 5);
		}
		
		public void setBlockState() {
			BlockAlloyFurnace.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileInfuser extends TileEnergyItemFluidProcessor {

		public TileInfuser() {
			super("infuser", 1, 1, 1, 0, tankCapacities(16000, 1, 0), fluidConnections(1, 0), validFluids(InfuserRecipes.instance()), NCConfig.processor_time[5], NCConfig.processor_power[5], InfuserRecipes.instance(), 6);
		}
		
		public void setBlockState() {
			BlockInfuser.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileMelter extends TileEnergyItemFluidProcessor {

		public TileMelter() {
			super("melter", 1, 0, 0, 1, tankCapacities(16000, 0, 1), fluidConnections(0, 1), validFluids(MelterRecipes.instance()), NCConfig.processor_time[6], NCConfig.processor_power[6], MelterRecipes.instance(), 7);
		}
		
		public void setBlockState() {
			BlockMelter.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileSupercooler extends TileEnergyFluidProcessor {

		public TileSupercooler() {
			super("supercooler", 1, 1, tankCapacities(16000, 1, 1), fluidConnections(1, 1), validFluids(SupercoolerRecipes.instance()), NCConfig.processor_time[7], NCConfig.processor_power[7], SupercoolerRecipes.instance(), 8);
		}
		
		public void setBlockState() {
			BlockSupercooler.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileElectrolyser extends TileEnergyFluidProcessor {

		public TileElectrolyser() {
			super("electrolyser", 1, 4, tankCapacities(16000, 1, 4), fluidConnections(1, 4), validFluids(ElectrolyserRecipes.instance()), NCConfig.processor_time[8], NCConfig.processor_power[8], ElectrolyserRecipes.instance(), 9);
		}
		
		public void setBlockState() {
			BlockElectrolyser.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileIrradiator extends TileEnergyFluidProcessor {

		public TileIrradiator() {
			super("irradiator", 2, 2, tankCapacities(16000, 2, 2), fluidConnections(2, 2), new String[][] {validFluids(IrradiatorRecipes.instance(), "neutron")[0], {"neutron"}, {}, {}}, NCConfig.processor_time[9], NCConfig.processor_power[9], IrradiatorRecipes.instance(), 10);
		}
		
		public void setBlockState() {
			BlockIrradiator.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileIngotFormer extends TileEnergyItemFluidProcessor {

		public TileIngotFormer() {
			super("ingot_former", 0, 1, 1, 0, tankCapacities(16000, 1, 0), fluidConnections(1, 0), validFluids(IngotFormerRecipes.instance()), NCConfig.processor_time[10], NCConfig.processor_power[10], IngotFormerRecipes.instance(), 11);
		}
		
		public void setBlockState() {
			BlockIngotFormer.setState(isProcessing, world, pos);
		}
	}
	
	public static class TilePressurizer extends TileEnergyItemProcessor {

		public TilePressurizer() {
			super("pressurizer", 1, 1, NCConfig.processor_time[11], NCConfig.processor_power[11], PressurizerRecipes.instance(), 12);
		}
		
		public void setBlockState() {
			BlockPressurizer.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileChemicalReactor extends TileEnergyFluidProcessor {

		public TileChemicalReactor() {
			super("chemical_reactor", 2, 2, tankCapacities(16000, 2, 2), fluidConnections(2, 2), validFluids(ChemicalReactorRecipes.instance()), NCConfig.processor_time[12], NCConfig.processor_power[12], ChemicalReactorRecipes.instance(), 13);
		}
		
		public void setBlockState() {
			BlockChemicalReactor.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileSaltMixer extends TileEnergyFluidProcessor {

		public TileSaltMixer() {
			super("salt_mixer", 2, 1, tankCapacities(16000, 2, 1), fluidConnections(2, 1), validFluids(SaltMixerRecipes.instance()), NCConfig.processor_time[13], NCConfig.processor_power[13], SaltMixerRecipes.instance(), 14);
		}
		
		public void setBlockState() {
			BlockSaltMixer.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileCrystallizer extends TileEnergyItemFluidProcessor {

		public TileCrystallizer() {
			super("crystallizer", 0, 1, 1, 0, tankCapacities(16000, 1, 0), fluidConnections(1, 0), validFluids(CrystallizerRecipes.instance()), NCConfig.processor_time[14], NCConfig.processor_power[14], CrystallizerRecipes.instance(), 15);
		}
		
		public void setBlockState() {
			BlockCrystallizer.setState(isProcessing, world, pos);
		}
	}
}
