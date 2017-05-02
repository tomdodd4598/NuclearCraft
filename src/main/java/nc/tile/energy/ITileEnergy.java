package nc.tile.energy;

import nc.energy.EnumStorage.EnergyConnection;
import nc.energy.Storage;

public interface ITileEnergy {
	public Storage getStorage();
	public EnergyConnection getEnergyConnection();
}
