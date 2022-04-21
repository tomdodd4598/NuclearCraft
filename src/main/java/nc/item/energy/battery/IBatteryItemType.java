package nc.item.energy.battery;

public interface IBatteryItemType {
	
	public long getCapacity();
	
	public int getMaxTransfer();
	
	public int getEnergyTier();
}
