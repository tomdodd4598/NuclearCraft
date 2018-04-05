package nc.tile.energy;

import nc.tile.dummy.IInterfaceable;
import nc.tile.internal.EnergyStorage;
import nc.tile.internal.EnumEnergyStorage.EnergyConnection;

public abstract class TileBatteryAbstract extends TileEnergy implements IBattery, IInterfaceable, IEnergySpread {
	
	public TileBatteryAbstract(int capacity) {
		this(capacity, capacity);
	}

	public TileBatteryAbstract(int capacity, int maxTransfer) {
		super(capacity, maxTransfer, EnergyConnection.IN);
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
	public EnergyStorage getBatteryStorage() {
		return storage;
	}
}
