package nc.tile.passive;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import gregtech.api.capability.GregtechCapabilities;
import nc.ModCheck;
import nc.capability.radiation.source.IRadiationSource;
import nc.config.NCConfig;
import nc.recipe.IngredientSorption;
import nc.recipe.ingredient.FluidIngredient;
import nc.recipe.ingredient.IFluidIngredient;
import nc.recipe.ingredient.IItemIngredient;
import nc.recipe.ingredient.ItemIngredient;
import nc.tile.energy.ITileEnergy;
import nc.tile.energyFluid.TileEnergyFluidSidedInventory;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.TankSorption;
import nc.tile.internal.inventory.ItemSorption;
import nc.tile.inventory.ITileInventory;
import nc.util.EnergyHelper;
import nc.util.GasHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

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
		super(name, 1, itemRate > 0D ? ITileInventory.inventoryConnectionAll(ItemSorption.OUT) : (itemRate < 0D ? ITileInventory.inventoryConnectionAll(ItemSorption.IN) : ITileInventory.inventoryConnectionAll(ItemSorption.NON)), energyRate == 0D ? 1 : MathHelper.ceil(Math.max(NCConfig.rf_per_eu, NCConfig.machine_update_rate)*Math.abs(energyRate)), energyRate == 0D ? 0 : MathHelper.ceil(Math.max(NCConfig.rf_per_eu, NCConfig.machine_update_rate)*Math.abs(energyRate)), energyRate > 0D ? ITileEnergy.energyConnectionAll(EnergyConnection.OUT) : (energyRate < 0D ? ITileEnergy.energyConnectionAll(EnergyConnection.IN) : ITileEnergy.energyConnectionAll(EnergyConnection.NON)), fluidRate == 0D ? 1 : MathHelper.ceil(6*NCConfig.machine_update_rate*Math.abs(fluidRate)), Lists.newArrayList(fluidType.getStack().getFluid().getName()), fluidRate > 0D ? ITileFluid.fluidConnectionAll(TankSorption.OUT) : (fluidRate < 0D ? ITileFluid.fluidConnectionAll(TankSorption.IN) : ITileFluid.fluidConnectionAll(TankSorption.NON)));
		this.energyRate = energyRate;
		this.itemRate = itemRate;
		this.itemType = itemType;
		this.fluidRate = fluidRate;
		this.fluidType = fluidType;
	}
	
	@Override
	public void update() {
		boolean wasActive = isActive;
		boolean shouldUpdate = false;
		super.update();
		if(!world.isRemote) {
			energyBool = changeEnergy(false);
			itemBool = changeStack(false);
			fluidBool = changeFluid(false);
			isActive = isRunning(energyBool, itemBool, fluidBool);
			if (wasActive != isActive) {
				shouldUpdate = true;
				updateBlockType();
			}
			if (tickCount == 0) {
				if (NCConfig.passive_push) {
					if (itemRate > 0D) pushStacks();
					if (fluidRate > 0D) pushFluid();
				}
				if (energyRate > 0D) pushEnergy();
			}
			
			tickCount();
			
			if (shouldUpdate) markDirty();
		}
	}
	
	public void tickCount() {
		tickCount++; tickCount %= NCConfig.machine_update_rate;
	}
	
	public void updateBlockType() {
		if (ModCheck.ic2Loaded()) removeTileFromENet();
		setState(isActive, this);
		world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
		if (ModCheck.ic2Loaded()) addTileToENet();
	}
	
	protected boolean changeEnergy(boolean simulateChange) {
		if (energyRate == 0) {
			return simulateChange;
		}
		
		energyBuffer += energyRate;
		int energyChange = (int) energyBuffer;
		energyBuffer -= energyChange;
		
		if (energyRate > 0 && getEnergyStorage().getEnergyStored() >= getEnergyStorage().getMaxEnergyStored()) {
			return false;
		}
		if (energyRate < 0 && getEnergyStorage().getEnergyStored() < Math.abs(energyChange)) {
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
		int itemChange = (int) itemBuffer;
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
			return true;
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
			return true;
		}
	}
	
	protected boolean changeFluid(boolean simulateChange) {
		if (fluidRate == 0) {
			return simulateChange;
		}
		
		fluidBuffer += fluidRate;
		int fluidChange = (int) fluidBuffer;
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
			} else {
				if (fluidRate >= 0) {
					return stack;
				}
				else {
					return stack && fluid;
				}
			}
		} else {
			if (itemRate >= 0) {
				if (fluidRate >= 0) {
					return energy;
				}
				else {
					return energy && fluid;
				}
			} else {
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
		return itemRate == 0 ? 1 : MathHelper.ceil(6*NCConfig.machine_update_rate*Math.abs(itemRate));
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
	public int getEUSourceTier() {
		return EnergyHelper.getEUTier(energyRate);
	}

	@Override
	public int getEUSinkTier() {
		return 10;
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
			return NCConfig.enable_gtce_eu && energyRate != 0 && hasEnergySideCapability(side);
		}
		else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return fluidRate != 0 && hasFluidSideCapability(side);
		}
		else if (ModCheck.mekanismLoaded() && capability == GasHelper.GAS_HANDLER_CAPABILITY) {
			return NCConfig.enable_mek_gas && fluidRate != 0 && hasFluidSideCapability(side);
		}
		else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return itemRate != 0;
		}
		return hasCapabilityDefault(capability, side);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (capability == IRadiationSource.CAPABILITY_RADIATION_SOURCE) {
			return (T) getRadiationSource();
		}
		else if (capability == CapabilityEnergy.ENERGY) {
			if (energyRate != 0 && hasEnergySideCapability(side)) {
				return (T) getEnergySide(nonNullSide(side));
			}
			return null;
		}
		else if (ModCheck.gregtechLoaded() && capability == GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER) {
			if (NCConfig.enable_gtce_eu && energyRate != 0 && hasEnergySideCapability(side)) {
				return (T) getEnergySideGT(nonNullSide(side));
			}
			return null;
		}
		else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			if (fluidRate != 0 && hasFluidSideCapability(side)) {
				return (T) getFluidSide(nonNullSide(side));
			}
			return null;
		}
		else if (ModCheck.mekanismLoaded() && capability == GasHelper.GAS_HANDLER_CAPABILITY) {
			if (NCConfig.enable_mek_gas && fluidRate != 0 && hasFluidSideCapability(side)) {
				return (T) getGasWrapper();
			}
			return null;
		}
		else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (itemRate != 0) {
				return (T) getItemHandlerCapability(null);
			}
			return null;
		}
		return getCapabilityDefault(capability, side);
	}
}
