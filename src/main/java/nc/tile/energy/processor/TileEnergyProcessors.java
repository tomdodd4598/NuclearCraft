package nc.tile.energy.processor;

import nc.block.tile.energy.processor.BlockAlloyFurnace;
import nc.block.tile.energy.processor.BlockDecayHastener;
import nc.block.tile.energy.processor.BlockFuelReprocessor;
import nc.block.tile.energy.processor.BlockIsotopeSeparator;
import nc.block.tile.energy.processor.BlockManufactory;
import nc.config.NCConfig;
import nc.crafting.processor.AlloyFurnaceRecipes;
import nc.crafting.processor.DecayHastenerRecipes;
import nc.crafting.processor.FuelReprocessorRecipes;
import nc.crafting.processor.IsotopeSeparatorRecipes;
import nc.crafting.processor.ManufactoryRecipes;

public class TileEnergyProcessors {
	
	public static class TileManufactory extends TileEnergyProcessor {

		public TileManufactory() {
			super("manufactory", 1, 1, NCConfig.processor_time[0], NCConfig.processor_power[0], ManufactoryRecipes.instance(), 1);
		}
		
		public void setBlockState() {
			BlockManufactory.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileIsotopeSeparator extends TileEnergyProcessor {

		public TileIsotopeSeparator() {
			super("isotope_separator", 1, 2, NCConfig.processor_time[1], NCConfig.processor_power[1], IsotopeSeparatorRecipes.instance(), 2);
		}
		
		public void setBlockState() {
			BlockIsotopeSeparator.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileDecayHastener extends TileEnergyProcessor {

		public TileDecayHastener() {
			super("decay_hastener", 1, 1, NCConfig.processor_time[2], NCConfig.processor_power[2], DecayHastenerRecipes.instance(), 3);
		}
		
		public void setBlockState() {
			BlockDecayHastener.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileFuelReprocessor extends TileEnergyProcessor {

		public TileFuelReprocessor() {
			super("fuel_reprocessor", 1, 4, NCConfig.processor_time[3], NCConfig.processor_power[3], FuelReprocessorRecipes.instance(), 4);
		}
		
		public void setBlockState() {
			BlockFuelReprocessor.setState(isProcessing, world, pos);
		}
	}
	
	public static class TileAlloyFurnace extends TileEnergyProcessor {

		public TileAlloyFurnace() {
			super("alloy_furnace", 2, 1, NCConfig.processor_time[4], NCConfig.processor_power[4], AlloyFurnaceRecipes.instance(), 5);
		}
		
		public void setBlockState() {
			BlockAlloyFurnace.setState(isProcessing, world, pos);
		}
	}
	
	/*public static class TileOxidiser extends TileEnergyProcessor {

		public TileOxidiser() {
			super("oxidiser", 1, 1, NCConfig.processor_time[5], NCConfig.processor_power[5], OxidiserRecipes.instance(), 6);
		}
		
		public void setBlockState() {
			BlockOxidiser.setState(isProcessing, world, pos);
		}
	}*/
}
