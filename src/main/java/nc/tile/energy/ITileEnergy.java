package nc.tile.energy;

import nc.tile.energy.storage.Storage;
import nc.tile.energy.storage.EnumStorage.EnergyConnection;

public interface ITileEnergy {
	public Storage getStorage();
	public EnergyConnection getEnergyConnection();
}
