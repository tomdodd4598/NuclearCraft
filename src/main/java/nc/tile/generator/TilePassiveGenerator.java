package nc.tile.generator;

import nc.config.NCConfig;
import nc.energy.EnumStorage.EnergyConnection;
import nc.tile.energy.TileEnergy;

public abstract class TilePassiveGenerator extends TileEnergy {
	
	public final int power;
	
	public TilePassiveGenerator(int capacity) {
		this(capacity, capacity);
	}

	public TilePassiveGenerator(int capacity, int maxTransfer) {
		super(2*capacity*NCConfig.generator_rf_per_eu, maxTransfer*NCConfig.generator_rf_per_eu, EnergyConnection.OUT);
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
