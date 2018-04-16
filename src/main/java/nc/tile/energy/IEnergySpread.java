package nc.tile.energy;

import nc.tile.internal.energy.EnergyStorage;

public interface IEnergySpread {
	
	public void spreadEnergy();
	
	public EnergyStorage getEnergyStorage();
}
