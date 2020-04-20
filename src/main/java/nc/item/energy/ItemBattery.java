package nc.item.energy;

import nc.multiblock.battery.BatteryType;
import nc.tile.internal.energy.EnergyConnection;

public class ItemBattery extends ItemEnergy {
	
	public ItemBattery(int capacity, int maxTransfer, int energyTier, String... tooltip) {
		super(capacity, maxTransfer, energyTier, EnergyConnection.BOTH, tooltip);
	}
	
	public ItemBattery(BatteryType type, String... tooltip) {
		super(type.getCapacity()/4, type.getMaxTransfer()/4, type.getEnergyTier(), EnergyConnection.BOTH, tooltip);
	}
}
