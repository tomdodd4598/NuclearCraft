package nc.tile.energy;

import nc.tile.ITile;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.energy.EnergyStorage;
import nc.tile.internal.energy.EnergyTileWrapper;
import nc.tile.internal.energy.EnergyTileWrapperGT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public interface ITileEnergy extends ITile {
	
	public EnergyStorage getEnergyStorage();
	
	public EnergyConnection getEnergyConnection(EnumFacing side);
	
	public void setEnergyConnection(EnergyConnection connection, EnumFacing side);
	
	public void toggleEnergyConnection(EnumFacing side);
	
	public default int getEnergyStored() {
		return getEnergyStorage().getEnergyStored();
	}
	
	public default int getMaxEnergyStored() {
		return getEnergyStorage().getMaxEnergyStored();
	}
	
	public default int extractEnergy(int maxExtract, EnumFacing side, boolean simulate) {
		return canExtractEnergy(side) ? getEnergyStorage().extractEnergy(maxExtract, simulate) : 0;
	}
	
	public default int receiveEnergy(int maxReceive, EnumFacing side, boolean simulate) {
		return canReceiveEnergy(side) ? getEnergyStorage().receiveEnergy(maxReceive, simulate) : 0;
	}
	
	public default boolean canConnectEnergy(EnumFacing side) {
		return getEnergyConnection(side).canConnect();
	}
	
	public default boolean canExtractEnergy(EnumFacing side) {
		return getEnergyConnection(side).canExtract();
	}
	
	public default boolean canReceiveEnergy(EnumFacing side) {
		return getEnergyConnection(side).canReceive();
	}
	
	public abstract int getEUSourceTier();
	
	public abstract int getEUSinkTier();
	
	public EnergyTileWrapper getEnergySide(EnumFacing side);
	
	public EnergyTileWrapperGT getEnergySideGT(EnumFacing side);
	
	public NBTTagCompound writeEnergy(NBTTagCompound nbt);
	
	public void readEnergy(NBTTagCompound nbt);
	
	public NBTTagCompound writeEnergyConnections(NBTTagCompound nbt);
	
	public void readEnergyConnections(NBTTagCompound nbt);
}
