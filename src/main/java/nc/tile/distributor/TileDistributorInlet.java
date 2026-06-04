package nc.tile.distributor;

import gregtech.api.capability.GregtechCapabilities;
import nc.*;
import nc.multiblock.distributor.Distributor;
import nc.tile.energy.ITileEnergy;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.energy.*;
import nc.tile.internal.fluid.*;
import nc.tile.internal.inventory.*;
import nc.tile.inventory.ITileInventory;
import nc.util.CapabilityHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.*;

import javax.annotation.*;
import java.util.*;

import static nc.config.NCConfig.*;

public class TileDistributorInlet extends TileDistributorPart implements ITileInventory, ITileFluid, ITileEnergy {
	
	protected final @Nonnull EnergyStorage backupStorage = new EnergyStorage(0L);
	protected final @Nonnull NonNullList<ItemStack> backupInventory = NonNullList.withSize(0, ItemStack.EMPTY);
	protected final @Nonnull List<Tank> backupTanks = Collections.emptyList();
	
	protected final @Nonnull EnergyConnection[] energyConnections = ITileEnergy.energyConnectionAll(EnergyConnection.IN);
	protected final @Nonnull InventoryConnection[] inventoryConnections = ITileInventory.inventoryConnectionAll(ItemSorption.IN);
	protected @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(TankSorption.IN);
	
	protected final @Nonnull EnergyTileWrapper[] energySides = ITileEnergy.getDefaultEnergySides(this);
	protected final @Nonnull EnergyTileWrapperGT[] energySidesGT = ITileEnergy.getDefaultEnergySidesGT(this);
	protected final @Nonnull FluidTileWrapper[] fluidSides = ITileFluid.getDefaultFluidSides(this);
	protected final @Nonnull GasTileWrapper gasWrapper = new GasTileWrapper(this);
	
	protected boolean ic2reg = false;
	
	@Override
	public void onMachineAssembled(Distributor multiblock) {
		doStandardNullControllerResponse(multiblock);
	}
	
	@Override
	public void onMachineBroken() {}
	
	// Inventory
	
	@Override
	public String getName() {
		return Global.MOD_ID + ".container.distributor_inlet";
	}
	
	@Override
	public @Nonnull NonNullList<ItemStack> getInventoryStacks() {
		Distributor multiblock = getMultiblock();
		return multiblock == null ? backupInventory : multiblock.inventoryStacks;
	}
	
	@Override
	public int getInventoryStackLimit() {
		Distributor multiblock = getMultiblock();
		return multiblock == null ? Distributor.BASE_ITEM_STACK_LIMIT : multiblock.itemStackLimit;
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (slot != 0 || stack.isEmpty()) {
			return false;
		}
		
		ItemStack slotStack = getStackInSlot(slot);
		return slotStack.isEmpty() || ItemHandlerHelper.canItemStacksStack(slotStack, stack);
	}
	
	@Override
	public @Nonnull InventoryConnection[] getInventoryConnections() {
		return inventoryConnections;
	}
	
	@Override
	public void setInventoryConnections(@Nonnull InventoryConnection[] connections) {
		System.arraycopy(connections, 0, inventoryConnections, 0, Math.min(connections.length, inventoryConnections.length));
	}
	
	@Override
	public ItemOutputSetting getItemOutputSetting(int slot) {
		return ItemOutputSetting.DEFAULT;
	}
	
	@Override
	public void setItemOutputSetting(int slot, ItemOutputSetting setting) {}
	
	@Override
	public IItemHandler getItemHandler(@Nullable EnumFacing side) {
		return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new RawItemHandler<>(this, side));
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		ITileInventory.super.setInventorySlotContents(slot, stack);
		Distributor multiblock = getMultiblock();
		if (multiblock != null) {
			multiblock.storageUpdated = true;
		}
	}
	
	// Fluids
	
	@Override
	public @Nonnull List<Tank> getTanks() {
		Distributor multiblock = getMultiblock();
		return multiblock == null ? backupTanks : Collections.singletonList(multiblock.tank);
	}
	
	@Override
	public @Nonnull FluidConnection[] getFluidConnections() {
		return fluidConnections;
	}
	
	@Override
	public void setFluidConnections(@Nonnull FluidConnection[] connections) {
		fluidConnections = connections;
	}
	
	@Override
	public @Nonnull FluidTileWrapper[] getFluidSides() {
		return fluidSides;
	}
	
	@Override
	public @Nonnull GasTileWrapper getGasWrapper() {
		return gasWrapper;
	}
	
	@Override
	public void onWrapperFill(int fillAmount, boolean doFill) {
		ITileFluid.super.onWrapperFill(fillAmount, doFill);
		if (doFill && fillAmount > 0) {
			Distributor multiblock = getMultiblock();
			if (multiblock != null) {
				multiblock.storageUpdated = true;
			}
		}
	}
	
	@Override
	public boolean getInputTanksSeparated() {
		return false;
	}
	
	@Override
	public void setInputTanksSeparated(boolean separated) {}
	
	@Override
	public boolean getVoidUnusableFluidInput(int tankNumber) {
		return false;
	}
	
	@Override
	public void setVoidUnusableFluidInput(int tankNumber, boolean voidUnusableFluidInput) {}
	
	@Override
	public TankOutputSetting getTankOutputSetting(int tankNumber) {
		return TankOutputSetting.DEFAULT;
	}
	
	@Override
	public void setTankOutputSetting(int tankNumber, TankOutputSetting setting) {}
	
	// Energy
	
	@Override
	public EnergyStorage getEnergyStorage() {
		Distributor multiblock = getMultiblock();
		return multiblock == null ? backupStorage : multiblock.energyStorage;
	}
	
	@Override
	public EnergyConnection[] getEnergyConnections() {
		return energyConnections;
	}
	
	@Override
	public @Nonnull EnergyTileWrapper[] getEnergySides() {
		return energySides;
	}
	
	@Override
	public @Nonnull EnergyTileWrapperGT[] getEnergySidesGT() {
		return energySidesGT;
	}
	
	@Override
	public int receiveEnergy(int maxReceive, EnumFacing side, boolean simulate) {
		int received = ITileEnergy.super.receiveEnergy(maxReceive, side, simulate);
		if (!simulate && received > 0) {
			Distributor multiblock = getMultiblock();
			if (multiblock != null) {
				multiblock.storageUpdated = true;
			}
		}
		return received;
	}
	
	@Override
	public boolean getIC2Reg() {
		return ic2reg;
	}
	
	@Override
	public void setIC2Reg(boolean ic2reg) {
		this.ic2reg = ic2reg;
	}
	
	@Override
	public int getSinkTier() {
		return !isMultiblockAssembled() ? 10 : getMultiblock().getPowerPortEUSinkTier();
	}
	
	@Override
	public int getSourceTier() {
		return 1;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		if (getMultiblock() == null) {
			writeInventory(nbt);
			writeTanks(nbt);
			writeEnergy(nbt);
		}
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readInventory(nbt);
		readTanks(nbt);
		readEnergy(nbt);
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return hasInventorySideCapability(side);
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || (ModCheck.mekanismLoaded() && enable_mek_gas && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY)) {
			return hasFluidSideCapability(side);
		}
		if (capability == CapabilityEnergy.ENERGY || (ModCheck.gregtechLoaded() && enable_gtce_eu && capability == GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER)) {
			return hasEnergySideCapability(side);
		}
		return super.hasCapability(capability, side);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (hasInventorySideCapability(side)) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(getItemHandler(nonNullSide(side)));
			}
			return null;
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			if (hasFluidSideCapability(side)) {
				return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(getFluidSide(nonNullSide(side)));
			}
			return null;
		}
		if (ModCheck.mekanismLoaded() && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY) {
			if (enable_mek_gas && hasFluidSideCapability(side)) {
				return CapabilityHelper.GAS_HANDLER_CAPABILITY.cast(getGasWrapper());
			}
			return null;
		}
		if (capability == CapabilityEnergy.ENERGY) {
			if (hasEnergySideCapability(side)) {
				return CapabilityEnergy.ENERGY.cast(getEnergySide(nonNullSide(side)));
			}
			return null;
		}
		if (ModCheck.gregtechLoaded() && capability == GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER) {
			if (enable_gtce_eu && hasEnergySideCapability(side)) {
				return GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER.cast(getEnergySideGT(nonNullSide(side)));
			}
			return null;
		}
		return super.getCapability(capability, side);
	}
}
