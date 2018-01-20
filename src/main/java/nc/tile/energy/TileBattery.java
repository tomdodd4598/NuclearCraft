package nc.tile.energy;

import nc.tile.dummy.IInterfaceable;
import nc.tile.energy.storage.Storage;
import nc.tile.energy.storage.EnumStorage.EnergyConnection;

public abstract class TileBattery extends TileEnergy implements IBattery, IInterfaceable, IEnergySpread {
	
	public TileBattery(int capacity) {
		this(capacity, capacity);
	}

	public TileBattery(int capacity, int maxTransfer) {
		super(capacity, maxTransfer, EnergyConnection.BOTH);
	}
	
	@Override
	public void update() {
		super.update();
		if(!world.isRemote) {
			pushEnergy();
			spreadEnergy();
		}
	}

	@Override
	public Storage getBatteryStorage() {
		return storage;
	}
}
