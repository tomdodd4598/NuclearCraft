package nc.tile.generator;

import nc.config.NCConfig;
import nc.tile.dummy.IInterfaceable;
import nc.tile.energy.TileEnergy;
import nc.tile.internal.EnumEnergyStorage.EnergyConnection;
import nc.util.EnergyHelper;

public abstract class TilePassiveGenerator extends TileEnergy implements IInterfaceable {
	
	public final int power;
	
	public TilePassiveGenerator(int capacity) {
		this(capacity, capacity);
	}

	public TilePassiveGenerator(int capacity, int maxTransfer) {
		super(2*capacity*NCConfig.generator_rf_per_eu, maxTransfer*NCConfig.generator_rf_per_eu, EnergyConnection.OUT);
		power = capacity;
	}
	
	@Override
	public void update() {
		super.update();
		if(!world.isRemote) {
			storage.changeEnergyStored(getGenerated());
			pushEnergy();
			spreadEnergy();
		}
	}
	
	public abstract int getGenerated();
	
	@Override
	public int getSourceTier() {
		return EnergyHelper.getEUSourceTier(power);
	}
	
	@Override
	public int getSinkTier() {
		return 4;
	}
}
