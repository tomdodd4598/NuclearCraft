package nc.tile.hx;

import javax.annotation.Nonnull;

import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.hx.*;
import nc.tile.internal.fluid.TankSorption;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.IFluidBlock;

public class TileCondenserTube extends TileHeatExchangerPart {
	
	protected @Nonnull HeatExchangerTubeSetting[] tubeSettings = new HeatExchangerTubeSetting[] {HeatExchangerTubeSetting.DISABLED, HeatExchangerTubeSetting.DISABLED, HeatExchangerTubeSetting.DISABLED, HeatExchangerTubeSetting.DISABLED, HeatExchangerTubeSetting.DISABLED, HeatExchangerTubeSetting.DISABLED};
	
	public int[] adjacentTemperatures = new int[] {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
	
	public final double conductivity;
	
	public static class Copper extends TileCondenserTube {
		
		public Copper() {
			super(HeatExchangerTubeType.COPPER);
		}
	}
	
	public static class HardCarbon extends TileCondenserTube {
		
		public HardCarbon() {
			super(HeatExchangerTubeType.HARD_CARBON);
		}
	}
	
	public static class Thermoconducting extends TileCondenserTube {
		
		public Thermoconducting() {
			super(HeatExchangerTubeType.THERMOCONDUCTING);
		}
	}
	
	protected TileCondenserTube(HeatExchangerTubeType tubeType) {
		super(CuboidalPartPositionType.INTERIOR);
		conductivity = tubeType.getConductivity();
	}
	
	@Override
	public void onMachineAssembled(HeatExchanger controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
	}
	
	public void updateAdjacentTemperatures() {
		for (EnumFacing dir : EnumFacing.VALUES) {
			Block block = world.getBlockState(pos.offset(dir)).getBlock();
			if (block == Blocks.WATER || block == Blocks.FLOWING_WATER) {
				adjacentTemperatures[dir.getIndex()] = 300;
			}
			else if (block == Blocks.LAVA || block == Blocks.FLOWING_LAVA) {
				adjacentTemperatures[dir.getIndex()] = 1300;
			}
			else if (block instanceof IFluidBlock && ((IFluidBlock) block).getFluid() != null) {
				adjacentTemperatures[dir.getIndex()] = Math.max(1, ((IFluidBlock) block).getFluid().getTemperature());
			}
			else {
				adjacentTemperatures[dir.getIndex()] = Integer.MAX_VALUE;
			}
		}
	}
	
	public @Nonnull HeatExchangerTubeSetting[] getTubeSettings() {
		return tubeSettings;
	}
	
	public void setTubeSettings(@Nonnull HeatExchangerTubeSetting[] settings) {
		tubeSettings = settings;
	}
	
	public HeatExchangerTubeSetting getTubeSetting(@Nonnull EnumFacing side) {
		return tubeSettings[side.getIndex()];
	}
	
	public void setTubeSetting(@Nonnull EnumFacing side, @Nonnull HeatExchangerTubeSetting setting) {
		tubeSettings[side.getIndex()] = setting;
	}
	
	public void toggleTubeSetting(@Nonnull EnumFacing side) {
		setTubeSetting(side, getTubeSetting(side).next());
		refreshFluidConnections(side);
		markDirtyAndNotify(true);
	}
	
	public void refreshFluidConnections(@Nonnull EnumFacing side) {
		switch (getTubeSetting(side)) {
			case DISABLED:
				// setTankSorption(side, 0, TankSorption.NON);
				// setTankSorption(side, 1, TankSorption.NON);
				break;
			case DEFAULT:
				// setTankSorption(side, 0, TankSorption.IN);
				// setTankSorption(side, 1, TankSorption.NON);
				break;
			case PRODUCT_OUT:
				// setTankSorption(side, 0, TankSorption.NON);
				// setTankSorption(side, 1, TankSorption.OUT);
				break;
			case INPUT_SPREAD:
				// setTankSorption(side, 0, TankSorption.OUT);
				// setTankSorption(side, 1, TankSorption.NON);
				break;
			default:
				// setTankSorption(side, 0, TankSorption.NON);
				// setTankSorption(side, 1, TankSorption.NON);
				break;
		}
	}
	
	// NBT
	
	public NBTTagCompound writeTubeSettings(NBTTagCompound nbt) {
		NBTTagCompound settingsTag = new NBTTagCompound();
		for (EnumFacing side : EnumFacing.VALUES) {
			settingsTag.setInteger("setting" + side.getIndex(), getTubeSetting(side).ordinal());
		}
		nbt.setTag("tubeSettings", settingsTag);
		return nbt;
	}
	
	public void readTubeSettings(NBTTagCompound nbt) {
		if (nbt.hasKey("fluidConnections0")) {
			for (EnumFacing side : EnumFacing.VALUES) {
				TankSorption sorption = TankSorption.values()[nbt.getInteger("fluidConnections" + side.getIndex())];
				switch (sorption) {
					case NON:
						// setTankSorption(side, 0, TankSorption.NON);
						// setTankSorption(side, 1, TankSorption.NON);
						setTubeSetting(side, HeatExchangerTubeSetting.DISABLED);
						break;
					case BOTH:
						// setTankSorption(side, 0, TankSorption.IN);
						// setTankSorption(side, 1, TankSorption.NON);
						setTubeSetting(side, HeatExchangerTubeSetting.DEFAULT);
						break;
					case IN:
						// setTankSorption(side, 0, TankSorption.NON);
						// setTankSorption(side, 1, TankSorption.OUT);
						setTubeSetting(side, HeatExchangerTubeSetting.PRODUCT_OUT);
						break;
					case OUT:
						// setTankSorption(side, 0, TankSorption.OUT);
						// setTankSorption(side, 1, TankSorption.NON);
						setTubeSetting(side, HeatExchangerTubeSetting.INPUT_SPREAD);
						break;
					default:
						// setTankSorption(side, 0, TankSorption.NON);
						// setTankSorption(side, 1, TankSorption.NON);
						setTubeSetting(side, HeatExchangerTubeSetting.DISABLED);
						break;
				}
			}
		}
		else {
			NBTTagCompound settingsTag = nbt.getCompoundTag("tubeSettings");
			for (EnumFacing side : EnumFacing.VALUES) {
				setTubeSetting(side, HeatExchangerTubeSetting.values()[settingsTag.getInteger("setting" + side.getIndex())]);
				refreshFluidConnections(side);
			}
		}
	}
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		writeTubeSettings(nbt);
		for (EnumFacing side : EnumFacing.VALUES) {
			nbt.setInteger("adjacentTemperature" + side.getIndex(), adjacentTemperatures[side.getIndex()]);
		}
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readTubeSettings(nbt);
		for (EnumFacing side : EnumFacing.VALUES) {
			adjacentTemperatures[side.getIndex()] = nbt.getInteger("adjacentTemperature" + side.getIndex());
		}
	}
}
