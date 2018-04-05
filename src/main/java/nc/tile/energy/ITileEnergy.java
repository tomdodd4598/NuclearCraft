package nc.tile.energy;

import nc.tile.internal.EnergyStorage;
import nc.tile.internal.EnumEnergyStorage.EnergyConnection;

public interface ITileEnergy {
	public EnergyStorage getStorage();
	public EnergyConnection getEnergyConnection();
}
