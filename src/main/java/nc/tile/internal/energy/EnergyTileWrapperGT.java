package nc.tile.internal.energy;

import static nc.config.NCConfig.rf_per_eu;

import gregtech.api.capability.IEnergyContainer;
import nc.tile.energy.ITileEnergy;
import nc.util.*;
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
	public long acceptEnergyFromNetwork(EnumFacing sideIn, long voltage, long amperage) {
		if (tile.getEnergyStored() >= tile.getMaxEnergyStored() || !tile.canReceiveEnergy(sideIn)) {
			return 0L;
		}
		long amperesAccepted = Math.min(1L + MathHelper.floor((tile.getMaxEnergyStored() - tile.getEnergyStored()) / (double) rf_per_eu) / voltage, Math.min(amperage, getInputAmperage()));
		tile.getEnergyStorage().changeEnergyStored(NCMath.toInt(voltage * amperesAccepted * rf_per_eu));
		return amperesAccepted;
	}
	
	@Override
	@Optional.Method(modid = "gregtech")
	public boolean inputsEnergy(EnumFacing sideIn) {
		return tile.canReceiveEnergy(sideIn);
	}
	
	@Override
	@Optional.Method(modid = "gregtech")
	public boolean outputsEnergy(EnumFacing sideIn) {
		return tile.canExtractEnergy(sideIn);
	}
	
	@Override
	public long changeEnergy(long differenceAmount) {
		int amount = NCMath.toInt(differenceAmount);
		int energyReceived = tile.getEnergyStorage().receiveEnergy(rf_per_eu * amount, true);
		tile.receiveEnergy(energyReceived, side, false);
		return amount - energyReceived / rf_per_eu;
	}
	
	@Override
	@Optional.Method(modid = "gregtech")
	public long getEnergyStored() {
		return tile.getEnergyStored() / rf_per_eu;
	}
	
	@Override
	@Optional.Method(modid = "gregtech")
	public long getEnergyCapacity() {
		return tile.getMaxEnergyStored() / rf_per_eu;
	}
	
	@Override
	@Optional.Method(modid = "gregtech")
	public long getInputAmperage() {
		return tile.getEnergyStored() < tile.getMaxEnergyStored() ? 1L : 0L;
	}
	
	@Override
	@Optional.Method(modid = "gregtech")
	public long getOutputAmperage() {
		return tile.getEnergyStored() / rf_per_eu < 1 ? 0L : 1L;
	}
	
	@Override
	@Optional.Method(modid = "gregtech")
	public long getInputVoltage() {
		return EnergyHelper.getMaxEUFromTier(tile.getSinkTier());
	}
	
	@Override
	@Optional.Method(modid = "gregtech")
	public long getOutputVoltage() {
		return EnergyHelper.getMaxEUFromTier(tile.getSourceTier());
	}
}
