package nc.item.energy.battery;

public interface IBatteryItemType {
	
	long getCapacity();
	
	int getMaxTransfer();
	
	int getEnergyTier();
}
