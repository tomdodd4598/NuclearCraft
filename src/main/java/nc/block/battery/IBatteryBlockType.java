package nc.block.battery;

import net.minecraft.tileentity.TileEntity;

public interface IBatteryBlockType {

	long getCapacity();

	int getMaxTransfer();
	
	int getEnergyTier();
	
	TileEntity getTile();
}
