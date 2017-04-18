package nc.tile.energy.generator;

import nc.config.NCConfig;
import nc.energy.EnumStorage.Connection;
import nc.tile.energy.TileEnergy;

public abstract class TileEnergyGeneratorContinuous extends TileEnergy {
	
	public final int power;
	
	public TileEnergyGeneratorContinuous(int capacity) {
		this(capacity, capacity);
	}

	public TileEnergyGeneratorContinuous(int capacity, int maxTransfer) {
		super(capacity*NCConfig.generator_rf_per_eu, maxTransfer*NCConfig.generator_rf_per_eu, Connection.OUT);
		power = capacity;
	}
	
	public void update() {
		super.update();
		if(!world.isRemote) {
			storage.changeEnergyStored(getGenerated());
			pushEnergy();
		}
	}
	
	public abstract int getGenerated();
	
	public int getSourceTier() {
		double euPerTick = (double) power / (double) NCConfig.generator_rf_per_eu;
		return euPerTick < 32.0D ? 1 : (euPerTick < 128.0D ? 2 : (euPerTick < 512.0D ? 3 : 4));
	}
	
	public int getSinkTier() {
		return 4;
	}
}
