package nc.item.energy.battery;

import nc.config.NCConfig;

public enum BatteryItemType implements IBatteryItemType {
	
	LITHIUM_ION_CELL(0);
	
	private int id;
	
	private BatteryItemType(int id) {
		this.id = id;
	}
	
	@Override
	public long getCapacity() {
		return NCConfig.battery_item_capacity[id];
	}
	
	@Override
	public int getMaxTransfer() {
		return NCConfig.battery_item_max_transfer[id];
	}
	
	@Override
	public int getEnergyTier() {
		return NCConfig.battery_item_energy_tier[id];
	}
}
