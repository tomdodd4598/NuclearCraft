package nc.tile.energy;

import nc.tile.energy.storage.EnergyStorage;
import nc.tile.energy.storage.EnumEnergyStorage.EnergyConnection;

public interface ITileEnergy {
	public EnergyStorage getStorage();
	public EnergyConnection getEnergyConnection();
}
