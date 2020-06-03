package nc.tile.internal.energy;

import net.minecraftforge.energy.IEnergyStorage;

public class EnergyStorageVoid implements IEnergyStorage {
	
	public EnergyStorageVoid() {}
	
	@Override
	public int getEnergyStored() {
		return 0;
	}
	
	@Override
	public int getMaxEnergyStored() {
		return Integer.MAX_VALUE;
	}
	
	@Override
	public boolean canReceive() {
		return true;
	}
	
	@Override
	public boolean canExtract() {
		return false;
	}
	
	@Override
	public int receiveEnergy(int receive, boolean simulated) {
		return receive;
	}
	
	@Override
	public int extractEnergy(int extract, boolean simulated) {
		return 0;
	}
}
