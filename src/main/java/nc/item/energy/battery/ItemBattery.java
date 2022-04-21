package nc.item.energy.battery;

import nc.item.energy.ItemEnergy;
import nc.tile.internal.energy.EnergyConnection;

public class ItemBattery extends ItemEnergy {
	
	public ItemBattery(long capacity, int maxTransfer, int energyTier, String... tooltip) {
		super(capacity, maxTransfer, energyTier, EnergyConnection.BOTH, tooltip);
	}
	
	public ItemBattery(IBatteryItemType type, String... tooltip) {
		super(type.getCapacity(), type.getMaxTransfer(), type.getEnergyTier(), EnergyConnection.BOTH, tooltip);
	}
}
