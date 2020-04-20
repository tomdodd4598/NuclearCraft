package nc.tile.energyFluid;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.ints.IntList;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.inventory.InventoryConnection;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public abstract class TileEnergyFluidSidedInventory extends TileEnergyFluidInventory {
	
	public TileEnergyFluidSidedInventory(String name, int size, @Nonnull InventoryConnection[] inventoryConnections, int capacity, @Nonnull EnergyConnection[] energyConnections, int fluidCapacity, List<String> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		super(name, size, inventoryConnections, capacity, capacity, energyConnections, fluidCapacity, allowedFluids, fluidConnections);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, @Nonnull InventoryConnection[] inventoryConnections, int capacity, @Nonnull EnergyConnection[] energyConnections, @Nonnull IntList fluidCapacity, List<List<String>> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		super(name, size, inventoryConnections, capacity, capacity, energyConnections, fluidCapacity, allowedFluids, fluidConnections);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, @Nonnull InventoryConnection[] inventoryConnections, int capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections, int fluidCapacity, List<String> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		super(name, size, inventoryConnections, capacity, maxTransfer, energyConnections, fluidCapacity, allowedFluids, fluidConnections);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, @Nonnull InventoryConnection[] inventoryConnections, int capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections, @Nonnull IntList fluidCapacity, List<List<String>> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		super(name, size, inventoryConnections, capacity, maxTransfer, energyConnections, fluidCapacity, allowedFluids, fluidConnections);
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
