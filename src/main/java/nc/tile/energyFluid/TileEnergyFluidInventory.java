package nc.tile.energyFluid;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import nc.Global;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.inventory.InventoryConnection;
import nc.tile.inventory.ITileInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public abstract class TileEnergyFluidInventory extends TileEnergyFluid implements ITileInventory {
	
	private @Nonnull String inventoryName;
	
	private @Nonnull NonNullList<ItemStack> inventoryStacks;
	
	private @Nonnull InventoryConnection[] inventoryConnections;
	
	public TileEnergyFluidInventory(String name, int size, @Nonnull InventoryConnection[] inventoryConnections, int capacity, @Nonnull EnergyConnection[] energyConnections, int fluidCapacity, List<String> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		this(name, size, inventoryConnections, capacity, capacity, energyConnections, fluidCapacity, fluidCapacity, allowedFluids, fluidConnections);
	}
	
	public TileEnergyFluidInventory(String name, int size, @Nonnull InventoryConnection[] inventoryConnections, int capacity, @Nonnull EnergyConnection[] energyConnections, @Nonnull List<Integer> fluidCapacity, List<List<String>> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		this(name, size, inventoryConnections, capacity, capacity, energyConnections, fluidCapacity, fluidCapacity, allowedFluids, fluidConnections);
	}
	
	public TileEnergyFluidInventory(String name, int size, @Nonnull InventoryConnection[] inventoryConnections, int capacity, @Nonnull EnergyConnection[] energyConnections, int fluidCapacity, int maxFluidTransfer, List<String> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		this(name, size, inventoryConnections, capacity, capacity, energyConnections, fluidCapacity, maxFluidTransfer, allowedFluids, fluidConnections);
	}
	
	public TileEnergyFluidInventory(String name, int size, @Nonnull InventoryConnection[] inventoryConnections, int capacity, @Nonnull EnergyConnection[] energyConnections, @Nonnull List<Integer> fluidCapacity, @Nonnull List<Integer> maxFluidTransfer, List<List<String>> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		this(name, size, inventoryConnections, capacity, capacity, energyConnections, fluidCapacity, maxFluidTransfer, allowedFluids, fluidConnections);
	}
	
	public TileEnergyFluidInventory(String name, int size, @Nonnull InventoryConnection[] inventoryConnections, int capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections, int fluidCapacity, List<String> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		this(name, size, inventoryConnections, capacity, maxTransfer, energyConnections, fluidCapacity, fluidCapacity, allowedFluids, fluidConnections);
	}
	
	public TileEnergyFluidInventory(String name, int size, @Nonnull InventoryConnection[] inventoryConnections, int capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections, @Nonnull List<Integer> fluidCapacity, List<List<String>> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		this(name, size, inventoryConnections, capacity, maxTransfer, energyConnections, fluidCapacity, fluidCapacity, allowedFluids, fluidConnections);
	}
	
	public TileEnergyFluidInventory(String name, int size, @Nonnull InventoryConnection[] inventoryConnections, int capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections, int fluidCapacity, int maxFluidTransfer, List<String> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		super(capacity, maxTransfer, energyConnections, fluidCapacity, maxFluidTransfer, allowedFluids, fluidConnections);
		inventoryName = Global.MOD_ID + ".container." + name;
		inventoryStacks = NonNullList.<ItemStack>withSize(size, ItemStack.EMPTY);
		this.inventoryConnections = inventoryConnections;
	}
	
	public TileEnergyFluidInventory(String name, int size, @Nonnull InventoryConnection[] inventoryConnections, int capacity, int maxTransfer, EnergyConnection[] energyConnections, @Nonnull List<Integer> fluidCapacity, @Nonnull List<Integer> maxFluidTransfer, List<List<String>> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		super(capacity, maxTransfer, energyConnections, fluidCapacity, maxFluidTransfer, allowedFluids, fluidConnections);
		inventoryName = Global.MOD_ID + ".container." + name;
		inventoryStacks = NonNullList.<ItemStack>withSize(size, ItemStack.EMPTY);
		this.inventoryConnections = inventoryConnections;
	}
	
	// Inventory
	
	@Override
	public String getName() {
		return inventoryName;
	}
	
	@Override
	public @Nonnull NonNullList<ItemStack> getInventoryStacks() {
		return inventoryStacks;
	}
	
	@Override
	public @Nonnull InventoryConnection[] getInventoryConnections() {
		return inventoryConnections;
	}
	
	@Override
	public void setInventoryConnections(@Nonnull InventoryConnection[] connections) {
		inventoryConnections = connections;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		writeInventory(nbt);
		writeInventoryConnections(nbt);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readInventory(nbt);
		readInventoryConnections(nbt);
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		if (!getInventoryStacks().isEmpty() && hasInventorySideCapability(side) && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, side);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (!getInventoryStacks().isEmpty() && hasInventorySideCapability(side) && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new InvWrapper(this));
		}
		return super.getCapability(capability, side);
	}
}
