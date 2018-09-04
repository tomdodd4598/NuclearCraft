package nc.item.energy;

import nc.tile.internal.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class ItemEnergyWrapper implements IEnergyStorage {
	
	protected EnergyStorage storage;
	
	public ItemEnergyWrapper(EnergyStorage storage) {
		this.storage = storage;
	}
	
	@Override
	public int getEnergyStored() {
		return storage.getEnergyStored();
	}
	
	@Override
	public int getMaxEnergyStored() {
		return storage.getMaxEnergyStored();
	}
	
	@Override
	public boolean canReceive() {
		return storage.canReceive();
	}
	
	@Override
	public boolean canExtract() {
		return storage.canExtract();
	}
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return storage.receiveEnergy(maxReceive, simulate);
	}
	
	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return storage.extractEnergy(maxExtract, simulate);
	}
}
