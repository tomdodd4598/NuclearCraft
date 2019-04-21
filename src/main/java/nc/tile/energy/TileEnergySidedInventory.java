package nc.tile.energy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.inventory.InventoryConnection;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public abstract class TileEnergySidedInventory extends TileEnergyInventory {
	
	public TileEnergySidedInventory(String name, int size, @Nonnull InventoryConnection[] inventoryConnections, int capacity, @Nonnull EnergyConnection[] energyConnections) {
		super(name, size, inventoryConnections, capacity, energyConnections);
	}
			
	public TileEnergySidedInventory(String name, int size, @Nonnull InventoryConnection[] inventoryConnections, int capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections) {
		super(name, size, inventoryConnections, capacity, maxTransfer, energyConnections);
	}
	
	// Capability
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (!getInventoryStacks().isEmpty() && hasInventorySideCapability(side) && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new SidedInvWrapper(this, nonNullSide(side)));
		}
		return super.getCapability(capability, side);
	}
}
