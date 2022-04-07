package nc.multiblock.battery;

import net.minecraft.tileentity.TileEntity;

public interface IBatteryBlockType {
	
	public long getCapacity();
	
	public int getMaxTransfer();
	
	public int getEnergyTier();
	
	public TileEntity getTile();
}
