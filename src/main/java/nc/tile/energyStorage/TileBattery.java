package nc.tile.energyStorage;

import nc.energy.EnumStorage.EnergyConnection;
import nc.tile.energy.TileEnergy;

public abstract class TileBattery extends TileEnergy {
	
	public TileBattery(int capacity) {
		this(capacity, capacity);
	}

	public TileBattery(int capacity, int maxTransfer) {
		super(capacity, maxTransfer, EnergyConnection.BOTH);
	}
	
	public void update() {
		super.update();
		if(!world.isRemote) {
			pushEnergy();
		}
	}
}
