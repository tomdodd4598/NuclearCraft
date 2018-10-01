package nc.tile.energyFluid;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.fluid.TankSorption;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public abstract class TileEnergyFluidSidedInventory extends TileEnergyFluidInventory implements ISidedInventory {
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, @Nonnull EnergyConnection[] energyConnections, int fluidCapacity, @Nonnull TankSorption tankSorption, List<String> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		super(name, size, capacity, capacity, energyConnections, fluidCapacity, fluidCapacity, tankSorption, allowedFluids, fluidConnections);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, @Nonnull EnergyConnection[] energyConnections, @Nonnull List<Integer> fluidCapacity, @Nonnull List<TankSorption> tankSorptions, List<List<String>> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		super(name, size, capacity, capacity, energyConnections, fluidCapacity, fluidCapacity, tankSorptions, allowedFluids, fluidConnections);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, @Nonnull EnergyConnection[] energyConnections, int fluidCapacity, int maxFluidTransfer, @Nonnull TankSorption tankSorption, List<String> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		super(name, size, capacity, capacity, energyConnections, fluidCapacity, maxFluidTransfer, tankSorption, allowedFluids, fluidConnections);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, @Nonnull EnergyConnection[] energyConnections, @Nonnull List<Integer> fluidCapacity, @Nonnull List<Integer> maxFluidTransfer, @Nonnull List<TankSorption> tankSorptions, List<List<String>> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		super(name, size, capacity, capacity, energyConnections, fluidCapacity, maxFluidTransfer, tankSorptions, allowedFluids, fluidConnections);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections, int fluidCapacity, @Nonnull TankSorption tankSorption, List<String> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		super(name, size, capacity, maxTransfer, energyConnections, fluidCapacity, fluidCapacity, tankSorption, allowedFluids, fluidConnections);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections, @Nonnull List<Integer> fluidCapacity, @Nonnull List<TankSorption> tankSorptions, List<List<String>> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		super(name, size, capacity, maxTransfer, energyConnections, fluidCapacity, fluidCapacity, tankSorptions, allowedFluids, fluidConnections);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections, int fluidCapacity, int maxFluidTransfer, @Nonnull TankSorption tankSorption, List<String> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		super(name, size, capacity, maxTransfer, energyConnections, fluidCapacity, maxFluidTransfer, tankSorption, allowedFluids, fluidConnections);
	}
	
	public TileEnergyFluidSidedInventory(String name, int size, int capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections, @Nonnull List<Integer> fluidCapacity, @Nonnull List<Integer> maxFluidTransfer, @Nonnull List<TankSorption> tankSorptions, List<List<String>> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		super(name, size, capacity, maxTransfer, energyConnections, fluidCapacity, maxFluidTransfer, tankSorptions, allowedFluids, fluidConnections);
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
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (facing == EnumFacing.DOWN) return (T) handlerBottom;
			else if (facing == EnumFacing.UP) return (T) handlerTop;
			else return (T) handlerSide;
		}
		return super.getCapability(capability, facing);
	}
}
