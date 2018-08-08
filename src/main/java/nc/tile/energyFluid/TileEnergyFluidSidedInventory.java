package nc.tile.energyFluid;

import java.util.List;

import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.FluidConnection;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public abstract class TileEnergyFluidSidedInventory extends TileEnergyFluidInventory implements ISidedInventory {
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, EnergyConnection[] energyConnections, int fluidCapacity, FluidConnection fluidConnections, List<String> allowedFluids) {
		super(name, size, capacity, capacity, capacity, energyConnections, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, EnergyConnection[] energyConnections, List<Integer> fluidCapacity, List<FluidConnection> fluidConnections, List<List<String>> allowedFluids) {
		super(name, size, capacity, capacity, capacity, energyConnections, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, EnergyConnection[] energyConnections, int fluidCapacity, int maxFluidTransfer, FluidConnection fluidConnections, List<String> allowedFluids) {
		super(name, size, capacity, capacity, capacity, energyConnections, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, EnergyConnection[] energyConnections, List<Integer> fluidCapacity, List<Integer> maxFluidTransfer, List<FluidConnection> fluidConnections, List<List<String>> allowedFluids) {
		super(name, size, capacity, capacity, capacity, energyConnections, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, EnergyConnection[] energyConnections, int fluidCapacity, int maxFluidReceive, int maxFluidExtract, FluidConnection fluidConnections, List<String> allowedFluids) {
		super(name, size, capacity, capacity, capacity, energyConnections, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, EnergyConnection[] energyConnections, List<Integer> fluidCapacity, List<Integer> maxFluidReceive, List<Integer> maxFluidExtract, List<FluidConnection> fluidConnections, List<List<String>> allowedFluids) {
		super(name, size, capacity, capacity, capacity, energyConnections, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection[] energyConnections, int fluidCapacity, FluidConnection fluidConnections, List<String> allowedFluids) {
		super(name, size, capacity, maxTransfer, maxTransfer, energyConnections, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection[] energyConnections, List<Integer> fluidCapacity, List<FluidConnection> fluidConnections, List<List<String>> allowedFluids) {
		super(name, size, capacity, maxTransfer, maxTransfer, energyConnections, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection[] energyConnections, int fluidCapacity, int maxFluidTransfer, FluidConnection fluidConnections, List<String> allowedFluids) {
		super(name, size, capacity, maxTransfer, maxTransfer, energyConnections, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection[] energyConnections, List<Integer> fluidCapacity, List<Integer> maxFluidTransfer, List<FluidConnection> fluidConnections, List<List<String>> allowedFluids) {
		super(name, size, capacity, maxTransfer, maxTransfer, energyConnections, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection[] energyConnections, int fluidCapacity, int maxFluidReceive, int maxFluidExtract, FluidConnection fluidConnections, List<String> allowedFluids) {
		super(name, size, capacity, maxTransfer, maxTransfer, energyConnections, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxTransfer, EnergyConnection[] energyConnections, List<Integer> fluidCapacity, List<Integer> maxFluidReceive, List<Integer> maxFluidExtract, List<FluidConnection> fluidConnections, List<List<String>> allowedFluids) {
		super(name, size, capacity, maxTransfer, maxTransfer, energyConnections, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection[] energyConnections, int fluidCapacity, FluidConnection fluidConnections, List<String> allowedFluids) {
		super(name, size, capacity, maxReceive, maxExtract, energyConnections, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection[] energyConnections, List<Integer> fluidCapacity, List<FluidConnection> fluidConnections, List<List<String>> allowedFluids) {
		super(name, size, capacity, maxReceive, maxExtract, energyConnections, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection[] energyConnections, int fluidCapacity, int maxFluidTransfer, FluidConnection fluidConnections, List<String> allowedFluids) {
		super(name, size, capacity, maxReceive, maxExtract, energyConnections, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection[] energyConnections, List<Integer> fluidCapacity, List<Integer> maxFluidTransfer, List<FluidConnection> fluidConnections, List<List<String>> allowedFluids) {
		super(name, size, capacity, maxReceive, maxExtract, energyConnections, fluidCapacity, maxFluidTransfer, maxFluidTransfer, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection[] energyConnections, int fluidCapacity, int maxFluidReceive, int maxFluidExtract, FluidConnection fluidConnections, List<String> allowedFluids) {
		super(name, size, capacity, maxReceive, maxExtract, energyConnections, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnections, allowedFluids);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxReceive, int maxExtract, EnergyConnection[] energyConnections, List<Integer> fluidCapacity, List<Integer> maxFluidReceive, List<Integer> maxFluidExtract, List<FluidConnection> fluidConnections, List<List<String>> allowedFluids) {
		super(name, size, capacity, maxReceive, maxExtract, energyConnections, fluidCapacity, maxFluidReceive, maxFluidExtract, fluidConnections, allowedFluids);
	}
	
	// SidedInventory
	
	@Override
	public abstract int[] getSlotsForFace(EnumFacing side);

	@Override
	public abstract boolean canInsertItem(int slot, ItemStack stack, EnumFacing direction);

	@Override
	public abstract boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction);
	
	// Capability
	
	IItemHandler handlerTop = new SidedInvWrapper(this, EnumFacing.UP);
	IItemHandler handlerBottom = new SidedInvWrapper(this, EnumFacing.DOWN);
	IItemHandler handlerSide = new SidedInvWrapper(this, EnumFacing.WEST);
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (facing == EnumFacing.DOWN) return (T) handlerBottom;
			else if (facing == EnumFacing.UP) return (T) handlerTop;
			else return (T) handlerSide;
		}
		return super.getCapability(capability, facing);
	}
}
