package nc.tile.inventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import nc.tile.internal.inventory.InventoryConnection;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public abstract class TileSidedInventory extends TileInventory {
		
	public TileSidedInventory(String name, int size, @Nonnull InventoryConnection[] inventoryConnections) {
		super(name, size, inventoryConnections);
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
