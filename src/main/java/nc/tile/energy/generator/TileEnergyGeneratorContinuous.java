package nc.tile.energy.generator;

import nc.energy.EnumStorage.Connection;
import nc.tile.energy.TileEnergy;

public abstract class TileEnergyGeneratorContinuous extends TileEnergy {
	
	public TileEnergyGeneratorContinuous(int capacity) {
		this(capacity, capacity);
	}

	public TileEnergyGeneratorContinuous(int capacity, int maxTransfer) {
		super(capacity, maxTransfer, Connection.OUT);
	}
	
	public void update() {
		super.update();
		if(!world.isRemote) {
			storage.changeEnergyStored(getGenerated());
			pushEnergy();
		}
	}
	
	public abstract int getGenerated();
}
