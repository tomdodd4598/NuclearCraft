package nc.item.energy;

import nc.tile.energy.battery.BatteryType;
import nc.tile.internal.energy.EnergyConnection;

public class ItemBattery extends ItemEnergy {
	
	public ItemBattery(String name, int capacity, int maxTransfer, int energyTier, String... tooltip) {
		super(name, capacity, maxTransfer, energyTier, EnergyConnection.BOTH, tooltip);
	}
	
	public ItemBattery(String name, BatteryType type, String... tooltip) {
		super(name, type.getCapacity()/4, type.getMaxTransfer()/4, type.getEnergyTier(), EnergyConnection.BOTH, tooltip);
	}
}
