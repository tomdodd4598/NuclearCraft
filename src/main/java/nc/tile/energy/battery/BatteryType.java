package nc.tile.energy.battery;

import nc.config.NCConfig;

public enum BatteryType {
	VOLTAIC_PILE_BASIC(0, 1),
	VOLTAIC_PILE_ADVANCED(1, 2),
	VOLTAIC_PILE_DU(2, 3),
	VOLTAIC_PILE_ELITE(3, 4),
	LITHIUM_ION_BATTERY_BASIC(4, 3),
	LITHIUM_ION_BATTERY_ADVANCED(5, 4),
	LITHIUM_ION_BATTERY_DU(6, 5),
	LITHIUM_ION_BATTERY_ELITE(7, 6);
	
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
		return NCConfig.battery_capacity[id] / 20;
	}
	
	public int getEnergyTier() {
		return energyTier;
	}
}
