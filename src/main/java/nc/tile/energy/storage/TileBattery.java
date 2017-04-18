package nc.tile.energy.storage;

import nc.energy.EnumStorage.Connection;
import nc.tile.energy.TileEnergy;

public abstract class TileBattery extends TileEnergy {
	
	public TileBattery(int capacity) {
		this(capacity, capacity);
	}

	public TileBattery(int capacity, int maxTransfer) {
		super(capacity, maxTransfer, Connection.BOTH);
	}
	
	public void update() {
		super.update();
		if(!world.isRemote) {
			pushEnergy();
		}
	}
}
