package nc.tile.passive;

import gregtech.api.capability.GregtechCapabilities;
import nc.ModCheck;
import nc.capability.radiation.source.IRadiationSource;
import nc.recipe.IngredientSorption;
import nc.recipe.ingredient.*;
import nc.tile.energy.ITileEnergy;
import nc.tile.energyFluid.TileEnergyFluidSidedInventory;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.TankSorption;
import nc.tile.internal.inventory.ItemSorption;
import nc.tile.inventory.ITileInventory;
import nc.util.*;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.Collections;

import static nc.config.NCConfig.*;

public abstract class TilePassiveAbstract extends TileEnergyFluidSidedInventory implements ITilePassive {
	
	protected int tickCount;
	
	public boolean isActive;
	public boolean energyBool, itemBool, fluidBool;
	
	public final double energyRate, itemRate, fluidRate;
	public double energyBuffer, itemBuffer, fluidBuffer;
	
	public final IItemIngredient itemType;
	public final IFluidIngredient fluidType;
	
	private static final IItemIngredient ITEM_BACKUP = new ItemIngredient(new ItemStack(Items.STICK));
	private static final IFluidIngredient FLUID_BACKUP = new FluidIngredient(new FluidStack(FluidRegistry.WATER, 1));
	
	public TilePassiveAbstract(String name, double energyRate) {
		this(name, ITEM_BACKUP, 0, energyRate, FLUID_BACKUP, 0);
	}
	
	public TilePassiveAbstract(String name, IItemIngredient itemType, double itemRate) {
		this(name, itemType, itemRate, 0, FLUID_BACKUP, 0);
	}
	
	public TilePassiveAbstract(String name, IFluidIngredient fluidType, double fluidRate) {
		this(name, ITEM_BACKUP, 0, 0, fluidType, fluidRate);
	}
	
	public TilePassiveAbstract(String name, IItemIngredient itemType, double itemRate, double energyRate) {
		this(name, itemType, itemRate, energyRate, FLUID_BACKUP, 0);
	}
	
	public TilePassiveAbstract(String name, double energyRate, IFluidIngredient fluidType, double fluidRate) {
		this(name, ITEM_BACKUP, 0, energyRate, fluidType, fluidRate);
	}
	
	public TilePassiveAbstract(String name, IItemIngredient itemType, double itemRate, IFluidIngredient fluidType, double fluidRate) {
		this(name, itemType, itemRate, 0, fluidType, fluidRate);
	}
	
	protected TilePassiveAbstract(String name, IItemIngredient itemType, double itemRate, double energyRate, IFluidIngredient fluidType, double fluidRate) {
		super(name, 1, itemRate > 0D ? ITileInventory.inventoryConnectionAll(ItemSorption.OUT) : itemRate < 0D ? ITileInventory.inventoryConnectionAll(ItemSorption.IN) : ITileInventory.inventoryConnectionAll(ItemSorption.NON), energyRate == 0D ? 1 : MathHelper.ceil(Math.max(rf_per_eu, machine_update_rate) * Math.abs(energyRate)), energyRate == 0D ? 0 : MathHelper.ceil(Math.max(rf_per_eu, machine_update_rate) * Math.abs(energyRate)), energyRate > 0D ? ITileEnergy.energyConnectionAll(EnergyConnection.OUT) : energyRate < 0D ? ITileEnergy.energyConnectionAll(EnergyConnection.IN) : ITileEnergy.energyConnectionAll(EnergyConnection.NON), fluidRate == 0D ? 1 : MathHelper.ceil(6 * machine_update_rate * Math.abs(fluidRate)), Collections.singleton(fluidType.getStack().getFluid().getName()), fluidRate > 0D ? ITileFluid.fluidConnectionAll(TankSorption.OUT) : fluidRate < 0D ? ITileFluid.fluidConnectionAll(TankSorption.IN) : ITileFluid.fluidConnectionAll(TankSorption.NON));
		this.energyRate = energyRate;
		this.itemRate = itemRate;
		this.itemType = itemType;
		this.fluidRate = fluidRate;
		this.fluidType = fluidType;
	}
	
	@Override
	public void update() {
		if (!world.isRemote) {
			boolean wasActive = isActive, shouldUpdate = false;
			energyBool = changeEnergy(false);
			itemBool = changeStack(false);
			fluidBool = changeFluid(false);
			isActive = isRunning(energyBool, itemBool, fluidBool);
			if (wasActive != isActive) {
				shouldUpdate = true;
				setActivity(isActive);
			}
			if (tickCount == 0) {
				if (passive_push) {
					if (itemRate > 0D) {
						pushStacks();
					}
					if (fluidRate > 0D) {
						pushFluid();
					}
				}
				if (energyRate > 0D) {
					pushEnergy();
				}
			}
			
			tickCount();
			
			if (shouldUpdate) {
				markDirty();
			}
		}
	}
	
	public void tickCount() {
		++tickCount;
		tickCount %= machine_update_rate;
	}
	
	protected boolean changeEnergy(boolean simulateChange) {
		if (energyRate == 0) {
			return simulateChange;
		}
		
		energyBuffer += energyRate;
		long energyChange = (long) energyBuffer;
		energyBuffer -= energyChange;
		
		if (energyRate > 0 && getEnergyStorage().getEnergyStoredLong() >= getEnergyStorage().getMaxEnergyStoredLong()) {
			return false;
		}
		if (energyRate < 0 && getEnergyStorage().getEnergyStoredLong() < Math.abs(energyChange)) {
			return false;
		}
		
		if (!simulateChange) {
			if (energyChange != 0 && changeStack(true) && changeFluid(true)) {
				getEnergyStorage().changeEnergyStored(energyChange);
			}
		}
		return true;
	}
	
	protected boolean changeStack(boolean simulateChange) {
		if (itemRate == 0) {
			return simulateChange;
		}
		
		itemBuffer += itemRate;
		int itemChange = NCMath.toInt(itemBuffer);
		itemBuffer -= itemChange;
		
		if (!simulateChange && !itemType.match(getInventoryStacks().get(0), IngredientSorption.NEUTRAL).matches()) {
			getInventoryStacks().set(0, ItemStack.EMPTY);
		}
		
		if (itemRate > 0) {
			if (getInventoryStacks().get(0).getCount() + itemChange > getInventoryStackLimit()) {
				return false;
			}
			if (!simulateChange && changeEnergy(true) && changeFluid(true)) {
				if (getInventoryStacks().get(0).isEmpty()) {
					getInventoryStacks().set(0, itemType.getStack());
				}
				else {
					getInventoryStacks().get(0).grow(itemChange);
				}
			}
		}
		else {
			if (getInventoryStacks().get(0).getCount() < Math.abs(itemChange)) {
				return false;
			}
			if (!simulateChange && changeEnergy(true) && changeFluid(true)) {
				if (getInventoryStacks().get(0).getCount() > Math.abs(itemChange)) {
					getInventoryStacks().get(0).grow(itemChange);
				}
				else if (getInventoryStacks().get(0).getCount() == Math.abs(itemChange)) {
					getInventoryStacks().set(0, ItemStack.EMPTY);
				}
			}
		}
		return true;
	}
	
	protected boolean changeFluid(boolean simulateChange) {
		if (fluidRate == 0) {
			return simulateChange;
		}
		
		fluidBuffer += fluidRate;
		int fluidChange = NCMath.toInt(fluidBuffer);
		fluidBuffer -= fluidChange;
		
		if (fluidRate > 0 && getTanks().get(0).getFluidAmount() >= getTanks().get(0).getCapacity()) {
			return false;
		}
		if (fluidRate < 0 && getTanks().get(0).getFluidAmount() < Math.abs(fluidChange)) {
			return false;
		}
		
		if (!simulateChange) {
			if (changeEnergy(true) && changeStack(true)) {
				if (fluidRate > 0) {
					getTanks().get(0).changeFluidStored(fluidType.getStack().getFluid(), fluidChange);
				}
				else {
					getTanks().get(0).changeFluidAmount(fluidChange);
				}
			}
		}
		return true;
	}
	
	protected boolean isRunning(boolean energy, boolean stack, boolean fluid) {
		if (energyRate == 0 && itemRate == 0 && fluidRate == 0) {
			return true;
		}
		if (energyRate >= 0) {
			if (itemRate >= 0) {
				if (fluidRate >= 0) {
					return energy || stack || fluid;
				}
				else {
					return fluid;
				}
			}
			else {
				if (fluidRate >= 0) {
					return stack;
				}
				else {
					return stack && fluid;
				}
			}
		}
		else {
			if (itemRate >= 0) {
				if (fluidRate >= 0) {
					return energy;
				}
				else {
					return energy && fluid;
				}
			}
			else {
				if (fluidRate >= 0) {
					return energy && stack;
				}
				else {
					return energy && stack && fluid;
				}
			}
		}
	}
	
	@Override
	public double getEnergyRate() {
		return energyRate;
	}
	
	@Override
	public double getItemRate() {
		return itemRate;
	}
	
	@Override
	public double getFluidRate() {
		return fluidRate;
	}
	
	@Override
	public boolean canPushEnergyTo() {
		return energyRate < 0;
	}
	
	@Override
	public boolean canPushItemsTo() {
		return itemRate < 0;
	}
	
	@Override
	public boolean canPushFluidsTo() {
		return fluidRate < 0;
	}
	
	// ITileInventory
	
	@Override
	public int getInventoryStackLimit() {
		return itemRate == 0 ? 1 : MathHelper.ceil(6 * machine_update_rate * Math.abs(itemRate));
	}
	
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[] {0};
	}
	
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
		return itemRate < 0 && super.canInsertItem(slot, stack, side) && itemType.match(stack, IngredientSorption.NEUTRAL).matches();
	}
	
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
		return itemRate > 0 && super.canExtractItem(slot, stack, side);
	}
	
	// IC2 EU
	
	@Override
	public int getSinkTier() {
		return 10;
	}
	
	@Override
	public int getSourceTier() {
		return EnergyHelper.getEUTier(energyRate);
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setBoolean("isRunning", isActive);
		nbt.setBoolean("energyBool", energyBool);
		nbt.setBoolean("itemBool", itemBool);
		nbt.setBoolean("fluidBool", fluidBool);
		nbt.setDouble("energyBuffer", energyBuffer);
		nbt.setDouble("itemBuffer", itemBuffer);
		nbt.setDouble("fluidBuffer", fluidBuffer);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		isActive = nbt.getBoolean("isRunning");
		energyBool = nbt.getBoolean("energyBool");
		itemBool = nbt.getBoolean("itemBool");
		fluidBool = nbt.getBoolean("fluidBool");
		energyBuffer = nbt.getDouble("energyBuffer");
		itemBuffer = nbt.getDouble("itemBuffer");
		fluidBuffer = nbt.getDouble("fluidBuffer");
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		if (capability == IRadiationSource.CAPABILITY_RADIATION_SOURCE) {
			return getRadiationSource() != null;
		}
		else if (capability == CapabilityEnergy.ENERGY) {
			return energyRate != 0 && hasEnergySideCapability(side);
		}
		else if (ModCheck.gregtechLoaded() && capability == GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER) {
			return enable_gtce_eu && energyRate != 0 && hasEnergySideCapability(side);
		}
		else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return fluidRate != 0 && hasFluidSideCapability(side);
		}
		else if (ModCheck.mekanismLoaded() && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY) {
			return enable_mek_gas && fluidRate != 0 && hasFluidSideCapability(side);
		}
		else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return itemRate != 0;
		}
		return hasCapabilityDefault(capability, side);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (capability == IRadiationSource.CAPABILITY_RADIATION_SOURCE) {
			return IRadiationSource.CAPABILITY_RADIATION_SOURCE.cast(getRadiationSource());
		}
		else if (capability == CapabilityEnergy.ENERGY) {
			if (energyRate != 0 && hasEnergySideCapability(side)) {
				return CapabilityEnergy.ENERGY.cast(getEnergySide(nonNullSide(side)));
			}
			return null;
		}
		else if (ModCheck.gregtechLoaded() && capability == GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER) {
			if (enable_gtce_eu && energyRate != 0 && hasEnergySideCapability(side)) {
				return GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER.cast(getEnergySideGT(nonNullSide(side)));
			}
			return null;
		}
		else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			if (fluidRate != 0 && hasFluidSideCapability(side)) {
				return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(getFluidSide(nonNullSide(side)));
			}
			return null;
		}
		else if (ModCheck.mekanismLoaded() && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY) {
			if (enable_mek_gas && fluidRate != 0 && hasFluidSideCapability(side)) {
				return CapabilityHelper.GAS_HANDLER_CAPABILITY.cast(getGasWrapper());
			}
			return null;
		}
		else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (itemRate != 0) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(getItemHandler(null));
			}
			return null;
		}
		return getCapabilityDefault(capability, side);
	}
}
