package nc.tile.energyFluid;

import nc.ModCheck;
import nc.energy.EnumStorage.EnergyConnection;
import nc.fluid.EnumTank.FluidConnection;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public abstract class TileEnergyFluidSidedInventory extends TileEnergyFluidInventory implements ISidedInventory {
	
	public int[] topSlots;
	public int[] sideSlots;
	public int[] bottomSlots;
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, EnergyConnection connection, int fluidCapacity, FluidConnection fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, capacity, capacity, connection, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, EnergyConnection connection, int[] fluidCapacity, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, capacity, capacity, connection, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, EnergyConnection connection, int fluidCapacity, int maxFluidTransfer, FluidConnection fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, capacity, capacity, connection, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, EnergyConnection connection, int[] fluidCapacity, int[] maxFluidTransfer, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, capacity, capacity, connection, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, EnergyConnection connection, int fluidCapacity, int maxFluidReceive, int maxFluidExtract, FluidConnection fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, capacity, capacity, connection, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, EnergyConnection connection, int[] fluidCapacity, int[] maxFluidReceive, int[] maxFluidExtract, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, capacity, capacity, connection, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection connection, int fluidCapacity, FluidConnection fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, maxTransfer, maxTransfer, connection, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection connection, int[] fluidCapacity, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, maxTransfer, maxTransfer, connection, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection connection, int fluidCapacity, int maxFluidTransfer, FluidConnection fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, maxTransfer, maxTransfer, connection, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection connection, int[] fluidCapacity, int[] maxFluidTransfer, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, maxTransfer, maxTransfer, connection, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection connection, int fluidCapacity, int maxFluidReceive, int maxFluidExtract, FluidConnection fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, maxTransfer, maxTransfer, connection, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection connection, int[] fluidCapacity, int[] maxFluidReceive, int[] maxFluidExtract, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, maxTransfer, maxTransfer, connection, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection connection, int fluidCapacity, FluidConnection fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, maxReceive, maxExtract, connection, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection connection, int[] fluidCapacity, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, maxReceive, maxExtract, connection, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection connection, int fluidCapacity, int maxFluidTransfer, FluidConnection fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, maxReceive, maxExtract, connection, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection connection, int[] fluidCapacity, int[] maxFluidTransfer, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, maxReceive, maxExtract, connection, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection connection, int fluidCapacity, int maxFluidReceive, int maxFluidExtract, FluidConnection fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, maxReceive, maxExtract, connection, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection connection, int[] fluidCapacity, int[] maxFluidReceive, int[] maxFluidExtract, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, maxReceive, maxExtract, connection, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnection, allowedFluids);
	}
	
	// SidedInventory
	
	public abstract int[] getSlotsForFace(EnumFacing side);

	public abstract boolean canInsertItem(int slot, ItemStack stack, EnumFacing direction);

	public abstract boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction);
	
	// Capability
	
	net.minecraftforge.items.IItemHandler handlerTop = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.UP);
	net.minecraftforge.items.IItemHandler handlerBottom = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.DOWN);
	net.minecraftforge.items.IItemHandler handlerSide = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.WEST);
	
	@SuppressWarnings("unchecked")
	public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing) {
		if (CapabilityEnergy.ENERGY == capability) {
			return (T) storage;
		}
		if (connection != null && ModCheck.teslaLoaded && connection.canConnect()) {
			if ((capability == TeslaCapabilities.CAPABILITY_CONSUMER && connection.canReceive()) || (capability == TeslaCapabilities.CAPABILITY_PRODUCER && connection.canExtract()) || capability == TeslaCapabilities.CAPABILITY_HOLDER)
				return (T) storage;
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (T) tanks;
		}
		if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (facing == EnumFacing.DOWN) {
				return (T) handlerBottom;
			} else if (facing == EnumFacing.UP) {
				return (T) handlerTop;
			} else {
				return (T) handlerSide;
			}
		}
		return super.getCapability(capability, facing);
	}
}
