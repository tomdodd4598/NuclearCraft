package nc.tile.hx;

import javax.annotation.Nonnull;

import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.hx.*;
import nc.tile.internal.fluid.TankSorption;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TileHeatExchangerTube extends TileHeatExchangerPart {
	
	protected @Nonnull HeatExchangerTubeSetting[] tubeSettings = new HeatExchangerTubeSetting[] {HeatExchangerTubeSetting.DISABLED, HeatExchangerTubeSetting.DISABLED, HeatExchangerTubeSetting.DISABLED, HeatExchangerTubeSetting.DISABLED, HeatExchangerTubeSetting.DISABLED, HeatExchangerTubeSetting.DISABLED};
	
	public EnumFacing flowDir = null;
	
	public final double conductivity;
	
	public static class Copper extends TileHeatExchangerTube {
		
		public Copper() {
			super(HeatExchangerTubeType.COPPER);
		}
	}
	
	public static class HardCarbon extends TileHeatExchangerTube {
		
		public HardCarbon() {
			super(HeatExchangerTubeType.HARD_CARBON);
		}
	}
	
	public static class Thermoconducting extends TileHeatExchangerTube {
		
		public Thermoconducting() {
			super(HeatExchangerTubeType.THERMOCONDUCTING);
		}
	}
	
	protected TileHeatExchangerTube(HeatExchangerTubeType tubeType) {
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
	
	public boolean isContraflow(TileHeatExchangerTube tube) {
		if (flowDir == null || tube.flowDir == null) {
			return (flowDir == null) == (tube.flowDir == null);
		}
		return flowDir.getIndex() != tube.flowDir.getIndex();
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
		updateFlowDir();
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
	
	public void updateFlowDir() {
		for (EnumFacing side : EnumFacing.VALUES) {
			HeatExchangerTubeSetting thisSetting = getTubeSetting(side);
			if (thisSetting == HeatExchangerTubeSetting.DISABLED) {
				continue;
			}
			
			TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
			
			if (tile instanceof TileHeatExchangerVent && thisSetting == HeatExchangerTubeSetting.PRODUCT_OUT) {
				flowDir = side;
				return;
			}
			else if (tile instanceof TileHeatExchangerTube tube) {
                HeatExchangerTubeSetting tubeSetting = tube.getTubeSetting(side.getOpposite());
				
				if (thisSetting == HeatExchangerTubeSetting.INPUT_SPREAD && tubeSetting == HeatExchangerTubeSetting.DEFAULT || thisSetting == HeatExchangerTubeSetting.PRODUCT_OUT && (tubeSetting == HeatExchangerTubeSetting.DEFAULT || tubeSetting == HeatExchangerTubeSetting.INPUT_SPREAD)) {
					flowDir = side;
					return;
				}
			}
		}
		
		flowDir = null;
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
		nbt.setInteger("flowDir", flowDir == null ? -1 : flowDir.getIndex());
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readTubeSettings(nbt);
		flowDir = nbt.getInteger("flowDir") == -1 ? null : EnumFacing.VALUES[nbt.getInteger("flowDir")];
	}
}
