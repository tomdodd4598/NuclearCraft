package nc.tile.energy;

import javax.annotation.*;

import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.inventory.InventoryConnection;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public abstract class TileEnergySidedInventory extends TileEnergyInventory {
	
	public TileEnergySidedInventory(String name, int size, @Nonnull InventoryConnection[] inventoryConnections, long capacity, @Nonnull EnergyConnection[] energyConnections) {
		super(name, size, inventoryConnections, capacity, energyConnections);
	}
	
	public TileEnergySidedInventory(String name, int size, @Nonnull InventoryConnection[] inventoryConnections, long capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections) {
		super(name, size, inventoryConnections, capacity, maxTransfer, energyConnections);
	}
	
	// Capability
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (!getInventoryStacks().isEmpty() && hasInventorySideCapability(side)) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(getItemHandler(side));
			}
			return null;
		}
		return super.getCapability(capability, side);
	}
}
