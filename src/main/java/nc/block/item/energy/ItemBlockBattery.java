package nc.block.item.energy;

import nc.multiblock.battery.BatteryType;
import nc.tile.internal.energy.EnergyConnection;
import net.minecraft.block.Block;

public class ItemBlockBattery extends ItemBlockEnergy {
	
	public ItemBlockBattery(Block block, BatteryType type, String... tooltip) {
		super(block, type.getCapacity(), type.getMaxTransfer(), type.getEnergyTier(), EnergyConnection.BOTH, tooltip);
	}
}
