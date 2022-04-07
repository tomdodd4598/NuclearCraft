package nc.tile.energyFluid;

import java.util.*;

import javax.annotation.*;

import it.unimi.dsi.fastutil.ints.IntList;
import nc.Global;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.inventory.*;
import nc.tile.inventory.ITileInventory;
import nc.util.NCMath;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public abstract class TileEnergyFluidInventory extends TileEnergyFluid implements ITileInventory {
	
	private @Nonnull final String inventoryName;
	
	private @Nonnull final NonNullList<ItemStack> inventoryStacks;
	
	private @Nonnull InventoryConnection[] inventoryConnections;
	
	private @Nonnull final List<ItemOutputSetting> itemOutputSettings;
	
	public TileEnergyFluidInventory(String name, int size, @Nonnull InventoryConnection[] inventoryConnections, long capacity, @Nonnull EnergyConnection[] energyConnections, int fluidCapacity, List<String> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		this(name, size, inventoryConnections, capacity, NCMath.toInt(capacity), energyConnections, fluidCapacity, allowedFluids, fluidConnections);
	}
	
	public TileEnergyFluidInventory(String name, int size, @Nonnull InventoryConnection[] inventoryConnections, long capacity, @Nonnull EnergyConnection[] energyConnections, @Nonnull IntList fluidCapacity, List<List<String>> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		this(name, size, inventoryConnections, capacity, NCMath.toInt(capacity), energyConnections, fluidCapacity, allowedFluids, fluidConnections);
	}
	
	public TileEnergyFluidInventory(String name, int size, @Nonnull InventoryConnection[] inventoryConnections, long capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections, int fluidCapacity, List<String> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		super(capacity, maxTransfer, energyConnections, fluidCapacity, allowedFluids, fluidConnections);
		inventoryName = Global.MOD_ID + ".container." + name;
		inventoryStacks = NonNullList.withSize(size, ItemStack.EMPTY);
		this.inventoryConnections = inventoryConnections;
		itemOutputSettings = new ArrayList<>();
		for (int i = 0; i < size; ++i) {
			itemOutputSettings.add(ItemOutputSetting.DEFAULT);
		}
	}
	
	public TileEnergyFluidInventory(String name, int size, @Nonnull InventoryConnection[] inventoryConnections, long capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections, @Nonnull IntList fluidCapacity, List<List<String>> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		super(capacity, maxTransfer, energyConnections, fluidCapacity, allowedFluids, fluidConnections);
		inventoryName = Global.MOD_ID + ".container." + name;
		inventoryStacks = NonNullList.withSize(size, ItemStack.EMPTY);
		this.inventoryConnections = inventoryConnections;
		itemOutputSettings = new ArrayList<>();
		for (int i = 0; i < size; ++i) {
			itemOutputSettings.add(ItemOutputSetting.DEFAULT);
		}
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
	
	@Override
	public ItemOutputSetting getItemOutputSetting(int slot) {
		return itemOutputSettings.get(slot);
	}
	
	@Override
	public void setItemOutputSetting(int slot, ItemOutputSetting setting) {
		itemOutputSettings.set(slot, setting);
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		writeInventory(nbt);
		writeInventoryConnections(nbt);
		writeSlotSettings(nbt);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readInventory(nbt);
		readInventoryConnections(nbt);
		readSlotSettings(nbt);
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return !getInventoryStacks().isEmpty() && hasInventorySideCapability(side);
		}
		return super.hasCapability(capability, side);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (!getInventoryStacks().isEmpty() && hasInventorySideCapability(side)) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(getItemHandler(null));
			}
			return null;
		}
		return super.getCapability(capability, side);
	}
}
