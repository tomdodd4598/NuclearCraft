package nc.tile.processor;

import nc.block.tile.processor.BlockAlloyFurnace;
import nc.block.tile.processor.BlockDecayHastener;
import nc.block.tile.processor.BlockElectrolyser;
import nc.block.tile.processor.BlockFuelReprocessor;
import nc.block.tile.processor.BlockInfuser;
import nc.block.tile.processor.BlockIrradiator;
import nc.block.tile.processor.BlockIsotopeSeparator;
import nc.block.tile.processor.BlockManufactory;
import nc.block.tile.processor.BlockMelter;
import nc.block.tile.processor.BlockSupercooler;
import nc.config.NCConfig;
import nc.crafting.processor.AlloyFurnaceRecipes;
import nc.crafting.processor.DecayHastenerRecipes;
import nc.crafting.processor.ElectrolyserRecipes;
import nc.crafting.processor.FuelReprocessorRecipes;
import nc.crafting.processor.InfuserRecipes;
import nc.crafting.processor.IrradiatorRecipes;
import nc.crafting.processor.IsotopeSeparatorRecipes;
import nc.crafting.processor.ManufactoryRecipes;
import nc.crafting.processor.MelterRecipes;
import nc.crafting.processor.SupercoolerRecipes;
import nc.fluid.EnumTank.FluidConnection;

public class Processors {
	
	public static class TileManufactory extends TileEnergyItemProcessor {

		public TileManufactory() {
			super("Manufactory", 1, 1, NCConfig.processor_time[0], NCConfig.processor_power[0], ManufactoryRecipes.instance(), 1);
		}
		
		public void setBlockState() {
			BlockManufactory.setState(isProcessing, worldObj, pos);
		}
	}
	
	public static class TileIsotopeSeparator extends TileEnergyItemProcessor {

		public TileIsotopeSeparator() {
			super("Isotope Separator", 1, 2, NCConfig.processor_time[1], NCConfig.processor_power[1], IsotopeSeparatorRecipes.instance(), 2);
		}
		
		public void setBlockState() {
			BlockIsotopeSeparator.setState(isProcessing, worldObj, pos);
		}
	}
	
	public static class TileDecayHastener extends TileEnergyItemProcessor {

		public TileDecayHastener() {
			super("Decay Hastener", 1, 1, NCConfig.processor_time[2], NCConfig.processor_power[2], DecayHastenerRecipes.instance(), 3);
		}
		
		public void setBlockState() {
			BlockDecayHastener.setState(isProcessing, worldObj, pos);
		}
	}
	
	public static class TileFuelReprocessor extends TileEnergyItemProcessor {

		public TileFuelReprocessor() {
			super("Fuel Reprocessor", 1, 4, NCConfig.processor_time[3], NCConfig.processor_power[3], FuelReprocessorRecipes.instance(), 4);
		}
		
		public void setBlockState() {
			BlockFuelReprocessor.setState(isProcessing, worldObj, pos);
		}
	}
	
	public static class TileAlloyFurnace extends TileEnergyItemProcessor {

		public TileAlloyFurnace() {
			super("Alloy Furnace", 2, 1, NCConfig.processor_time[4], NCConfig.processor_power[4], AlloyFurnaceRecipes.instance(), 5);
		}
		
		public void setBlockState() {
			BlockAlloyFurnace.setState(isProcessing, worldObj, pos);
		}
	}
	
	public static class TileInfuser extends TileEnergyItemFluidProcessor {

		public TileInfuser() {
			super("Infuser", 1, 1, 1, 0, new int[] {16000}, new FluidConnection[] {FluidConnection.IN}, new String[][] {{"oxygen", "liquidhelium"}}, NCConfig.processor_time[5], NCConfig.processor_power[5], InfuserRecipes.instance(), 6);
		}
		
		public void setBlockState() {
			BlockInfuser.setState(isProcessing, worldObj, pos);
		}
	}
	
	public static class TileMelter extends TileEnergyItemFluidProcessor {

		public TileMelter() {
			super("Melter", 1, 0, 0, 1, new int[] {16000}, new FluidConnection[] {FluidConnection.OUT}, new String[][] {{}}, NCConfig.processor_time[6], NCConfig.processor_power[6], MelterRecipes.instance(), 7);
		}
		
		public void setBlockState() {
			BlockMelter.setState(isProcessing, worldObj, pos);
		}
	}
	
	public static class TileSupercooler extends TileEnergyFluidProcessor {

		public TileSupercooler() {
			super("Supercooler", 1, 1, new int[] {16000, 16000}, new FluidConnection[] {FluidConnection.IN, FluidConnection.OUT}, new String[][] {{"helium", "water"}, {}}, NCConfig.processor_time[7], NCConfig.processor_power[7], SupercoolerRecipes.instance(), 8);
		}
		
		public void setBlockState() {
			BlockSupercooler.setState(isProcessing, worldObj, pos);
		}
	}
	
	public static class TileElectrolyser extends TileEnergyFluidProcessor {

		public TileElectrolyser() {
			super("Electrolyser", 1, 4, new int[] {16000, 16000, 16000, 16000, 16000}, new FluidConnection[] {FluidConnection.IN, FluidConnection.OUT, FluidConnection.OUT, FluidConnection.OUT, FluidConnection.OUT}, new String[][] {{"water", "heavywater"}, {}, {}, {}, {}}, NCConfig.processor_time[8], NCConfig.processor_power[8], ElectrolyserRecipes.instance(), 9);
		}
		
		public void setBlockState() {
			BlockElectrolyser.setState(isProcessing, worldObj, pos);
		}
	}
	
	public static class TileIrradiator extends TileEnergyFluidProcessor {

		public TileIrradiator() {
			super("Irradiator", 2, 2, new int[] {16000, 16000, 16000, 16000}, new FluidConnection[] {FluidConnection.IN, FluidConnection.IN, FluidConnection.OUT, FluidConnection.OUT}, new String[][] {{"lithium6", "boron10", "neutron"}, {"lithium6", "boron10", "neutron"}, {}, {}}, NCConfig.processor_time[9], NCConfig.processor_power[9], IrradiatorRecipes.instance(), 10);
		}
		
		public void setBlockState() {
			BlockIrradiator.setState(isProcessing, worldObj, pos);
		}
	}
}
