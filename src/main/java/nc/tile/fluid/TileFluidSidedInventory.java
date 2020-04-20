package nc.tile.fluid;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.ints.IntList;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.inventory.InventoryConnection;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public abstract class TileFluidSidedInventory extends TileFluidInventory {
	
	public TileFluidSidedInventory(String name, int size, @Nonnull InventoryConnection[] inventoryConnections, int capacity, List<String> allowedFluidsList, @Nonnull FluidConnection[] fluidConnections) {
		super(name, size, inventoryConnections, capacity, allowedFluidsList, fluidConnections);
	}
	
	public TileFluidSidedInventory(String name, int size, @Nonnull InventoryConnection[] inventoryConnections, @Nonnull IntList capacity, List<List<String>> allowedFluidsLists, @Nonnull FluidConnection[] fluidConnections) {
		super(name, size, inventoryConnections, capacity, allowedFluidsLists, fluidConnections);
	}
	
	// Capability
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (!getInventoryStacks().isEmpty() && hasInventorySideCapability(side)) {
				return (T) getItemHandlerCapability(side);
			}
			return null;
		}
		return super.getCapability(capability, side);
	}
}
