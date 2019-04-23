package nc.tile.internal.energy;

import gregtech.api.capability.IEnergyContainer;
import nc.config.NCConfig;
import nc.tile.energy.ITileEnergy;
import nc.util.EnergyHelper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "gregtech.api.capability.IEnergyContainer", modid = "gregtech")
public class EnergyTileWrapperGT implements IEnergyContainer {
	
	public final ITileEnergy tile;
	public final EnumFacing side;
	
	public EnergyTileWrapperGT(ITileEnergy tile, EnumFacing side) {
		this.tile = tile;
		this.side = side;
	}

	@Override
	@Optional.Method(modid = "gregtech")
	public long acceptEnergyFromNetwork(EnumFacing side, long voltage, long amperage) {
		if (tile.getEnergyStored() >= tile.getMaxEnergyStored() || !tile.canReceiveEnergy(side)) return 0L;
		long amperesAccepted = Math.min(1L + MathHelper.floor((double)(tile.getMaxEnergyStored() - tile.getEnergyStored())/(double)NCConfig.rf_per_eu)/voltage, Math.min(amperage, getInputAmperage()));
		tile.getEnergyStorage().changeEnergyStored((int)Math.min(voltage*amperesAccepted*NCConfig.rf_per_eu, Integer.MAX_VALUE));
		return amperesAccepted;
	}

	@Override
	@Optional.Method(modid = "gregtech")
	public boolean inputsEnergy(EnumFacing side) {
		return tile.canReceiveEnergy(side);
	}
	
	@Override
	@Optional.Method(modid = "gregtech")
	public boolean outputsEnergy(EnumFacing side) {
		return tile.canExtractEnergy(side);
	}

	@Override
	public long changeEnergy(long differenceAmount) {
		int amount = (int)Math.min(differenceAmount, Integer.MAX_VALUE);
		int energyReceived = tile.getEnergyStorage().receiveEnergy(NCConfig.rf_per_eu*amount, true);
		tile.receiveEnergy(energyReceived, side, false);
		return amount - energyReceived/NCConfig.rf_per_eu;
	}

	@Override
	@Optional.Method(modid = "gregtech")
	public long getEnergyStored() {
		return tile.getEnergyStored()/NCConfig.rf_per_eu;
	}

	@Override
	@Optional.Method(modid = "gregtech")
	public long getEnergyCapacity() {
		return tile.getMaxEnergyStored()/NCConfig.rf_per_eu;
	}

	@Override
	@Optional.Method(modid = "gregtech")
	public long getInputAmperage() {
		return tile.getEnergyStored() < tile.getMaxEnergyStored() ? 1L : 0L;
	}
	
	@Override
	@Optional.Method(modid = "gregtech")
	public long getOutputAmperage() {
		return tile.getEnergyStored()/NCConfig.rf_per_eu < 1 ? 0L : 1L;
	}

	@Override
	@Optional.Method(modid = "gregtech")
	public long getInputVoltage() {
		return EnergyHelper.getMaxEUFromTier(tile.getEUSinkTier());
	}
	
	@Override
	@Optional.Method(modid = "gregtech")
	public long getOutputVoltage() {
		return EnergyHelper.getMaxEUFromTier(tile.getEUSourceTier());
	}
}
