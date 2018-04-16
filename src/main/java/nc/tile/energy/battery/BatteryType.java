package nc.tile.energy.battery;

import nc.config.NCConfig;

public enum BatteryType {
	VOLTAIC_PILE_BASIC(0, 1),
	LITHIUM_ION_BATTERY_BASIC(1, 3);
	
	private int id;
	private int energyTier;
	
	private BatteryType(int id, int energyTier) {
		this.id = id;
		this.energyTier = energyTier;
	}
	
	public int getCapacity() {
		return NCConfig.battery_capacity[id];
	}
	
	public int getMaxTransfer() {
		return NCConfig.battery_capacity[id] / 100;
	}
	
	public int getEnergyTier() {
		return energyTier;
	}
}
