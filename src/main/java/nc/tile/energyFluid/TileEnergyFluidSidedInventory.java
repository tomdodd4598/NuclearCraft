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
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, EnergyConnection energyConnection, int fluidCapacity, FluidConnection fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, capacity, capacity, energyConnection, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, EnergyConnection energyConnection, int[] fluidCapacity, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, capacity, capacity, energyConnection, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, EnergyConnection energyConnection, int fluidCapacity, int maxFluidTransfer, FluidConnection fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, capacity, capacity, energyConnection, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, EnergyConnection energyConnection, int[] fluidCapacity, int[] maxFluidTransfer, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, capacity, capacity, energyConnection, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, EnergyConnection energyConnection, int fluidCapacity, int maxFluidReceive, int maxFluidExtract, FluidConnection fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, capacity, capacity, energyConnection, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, EnergyConnection energyConnection, int[] fluidCapacity, int[] maxFluidReceive, int[] maxFluidExtract, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, capacity, capacity, energyConnection, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection energyConnection, int fluidCapacity, FluidConnection fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, maxTransfer, maxTransfer, energyConnection, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection energyConnection, int[] fluidCapacity, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, maxTransfer, maxTransfer, energyConnection, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection energyConnection, int fluidCapacity, int maxFluidTransfer, FluidConnection fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, maxTransfer, maxTransfer, energyConnection, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection energyConnection, int[] fluidCapacity, int[] maxFluidTransfer, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, maxTransfer, maxTransfer, energyConnection, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection energyConnection, int fluidCapacity, int maxFluidReceive, int maxFluidExtract, FluidConnection fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, maxTransfer, maxTransfer, energyConnection, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection energyConnection, int[] fluidCapacity, int[] maxFluidReceive, int[] maxFluidExtract, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, maxTransfer, maxTransfer, energyConnection, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection energyConnection, int fluidCapacity, FluidConnection fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, maxReceive, maxExtract, energyConnection, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection energyConnection, int[] fluidCapacity, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, maxReceive, maxExtract, energyConnection, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection energyConnection, int fluidCapacity, int maxFluidTransfer, FluidConnection fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, maxReceive, maxExtract, energyConnection, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection energyConnection, int[] fluidCapacity, int[] maxFluidTransfer, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, maxReceive, maxExtract, energyConnection, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection energyConnection, int fluidCapacity, int maxFluidReceive, int maxFluidExtract, FluidConnection fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, maxReceive, maxExtract, energyConnection, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnection, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection energyConnection, int[] fluidCapacity, int[] maxFluidReceive, int[] maxFluidExtract, FluidConnection[] fluidConnection, String[]... allowedFluids) {
		super(name, size, capacity, maxReceive, maxExtract, energyConnection, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnection, allowedFluids);
	}
	
	// SidedInventory
	
	@Override
	public abstract int[] getSlotsForFace(EnumFacing side);

	@Override
	public abstract boolean canInsertItem(int slot, ItemStack stack, EnumFacing direction);

	@Override
	public abstract boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction);
	
	// Capability
	
	net.minecraftforge.items.IItemHandler handlerTop = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.UP);
	net.minecraftforge.items.IItemHandler handlerBottom = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.DOWN);
	net.minecraftforge.items.IItemHandler handlerSide = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.WEST);
	
	@Override
	public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing) {
		if (CapabilityEnergy.ENERGY == capability && energyConnection.canConnect()) {
			return (T) storage;
		}
		if (energyConnection != null && ModCheck.teslaLoaded() && energyConnection.canConnect()) {
			if ((capability == TeslaCapabilities.CAPABILITY_CONSUMER && energyConnection.canReceive()) || (capability == TeslaCapabilities.CAPABILITY_PRODUCER && energyConnection.canExtract()) || capability == TeslaCapabilities.CAPABILITY_HOLDER)
				return (T) storage;
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this);
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
