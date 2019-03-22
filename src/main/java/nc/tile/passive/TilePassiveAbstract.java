package nc.tile.passive;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import gregtech.api.capability.GregtechCapabilities;
import nc.ModCheck;
import nc.capability.radiation.source.IRadiationSource;
import nc.config.NCConfig;
import nc.recipe.IngredientSorption;
import nc.recipe.ingredient.IItemIngredient;
import nc.recipe.ingredient.ItemIngredient;
import nc.tile.energy.ITileEnergy;
import nc.tile.energyFluid.TileEnergyFluidSidedInventory;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.fluid.TankSorption;
import nc.util.GasHelper;
import nc.util.ItemStackHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public abstract class TilePassiveAbstract extends TileEnergyFluidSidedInventory implements ITilePassive {
	
	protected int tickCount;
	protected int pushCount;
	
	public final int updateRate;
	public final int pushRate;
	public boolean isActive;
	public boolean energyBool;
	public boolean stackBool;
	public boolean fluidBool;
	
	public final int energyChange;
	public IItemIngredient stackChange;
	public final int itemChange;
	public final int fluidChange;
	public final FluidStack fluidStackChange;
	public final Fluid fluidType;
	
	public TilePassiveAbstract(String name, int energyChange, int changeRate) {
		this(name, new ItemIngredient(new ItemStack(Items.BEEF)), 0, energyChange, FluidRegistry.LAVA, 0, changeRate);
	}
	
	public TilePassiveAbstract(String name, IItemIngredient item, int itemChange, int changeRate) {
		this(name, item, itemChange, 0, FluidRegistry.LAVA, 0, changeRate);
	}
	
	public TilePassiveAbstract(String name, Fluid fluid, int fluidChange, int changeRate) {
		this(name, new ItemIngredient(new ItemStack(Items.BEEF)), 0, 0, fluid, fluidChange, changeRate);
	}
	
	public TilePassiveAbstract(String name, Fluid fluid, int fluidChange, int changeRate, List<String> fluidTypes) {
		this(name, new ItemIngredient(new ItemStack(Items.BEEF)), 0, 0, fluid, fluidChange, changeRate, fluidTypes);
	}
	
	public TilePassiveAbstract(String name, IItemIngredient item, int itemChange, int energyChange, int changeRate) {
		this(name, item, itemChange, energyChange, FluidRegistry.LAVA, 0, changeRate);
	}
	
	public TilePassiveAbstract(String name, int energyChange, Fluid fluid, int fluidChange, int changeRate) {
		this(name, new ItemIngredient(new ItemStack(Items.BEEF)), 0, energyChange, fluid, fluidChange, changeRate);
	}
	
	public TilePassiveAbstract(String name, IItemIngredient item, int itemChange, Fluid fluid, int fluidChange, int changeRate) {
		this(name, item, itemChange, 0, fluid, fluidChange, changeRate);
	}
	
	public TilePassiveAbstract(String name, IItemIngredient item, int itemChange, int energyChange, Fluid fluid, int fluidChange, int changeRate) {
		this(name, item, itemChange, energyChange, fluid, fluidChange, changeRate, Lists.newArrayList(fluid.getName()));
	}
	
	public TilePassiveAbstract(String name, IItemIngredient item, int itemChange, int energyChange, Fluid fluid, int fluidChange, int changeRate, List<String> fluidTypes) {
		super(name, 1, energyChange == 0 ? 1 : NCConfig.rf_per_eu*MathHelper.abs(energyChange)*changeRate, energyChange == 0 ? 0 : NCConfig.rf_per_eu*MathHelper.abs(energyChange), energyChange > 0 ? ITileEnergy.energyConnectionAll(EnergyConnection.OUT) : (energyChange < 0 ? ITileEnergy.energyConnectionAll(EnergyConnection.IN) : ITileEnergy.energyConnectionAll(EnergyConnection.NON)), fluidChange == 0 ? 1 : 6*MathHelper.abs(fluidChange)*changeRate, fluidChange > 0 ? TankSorption.OUT : (fluidChange < 0 ? TankSorption.IN : TankSorption.NON), fluidTypes, fluidChange > 0 ? ITileFluid.fluidConnectionAll(FluidConnection.OUT) : (fluidChange < 0 ? ITileFluid.fluidConnectionAll(FluidConnection.IN) : ITileFluid.fluidConnectionAll(FluidConnection.NON)));
		this.energyChange = energyChange*changeRate;
		this.itemChange = itemChange*changeRate;
		stackChange = new ItemIngredient(ItemStackHelper.changeStackSize(item.getStack(), MathHelper.abs(itemChange)*changeRate));
		this.fluidChange = fluidChange*changeRate;
		fluidStackChange = new FluidStack(fluid, MathHelper.abs(fluidChange)*changeRate);
		fluidType = fluid;
		updateRate = changeRate*20;
		pushRate = Math.min(changeRate*20, NCConfig.machine_update_rate);
	}
	
	@Override
	public void update() {
		boolean wasActive = isActive;
		boolean shouldUpdate = false;
		super.update();
		if(!world.isRemote) {
			if (tickCount == 0) {
				energyBool = changeEnergy(false);
				stackBool = changeStack(false);
				fluidBool = changeFluid(false);
			}
			isActive = isRunning(energyBool, stackBool, fluidBool);
			if (wasActive != isActive) {
				shouldUpdate = true;
				updateBlockType();
			}
			if (pushCount == 0) {
				if (itemChange > 0) pushStacks(this);
				if (energyChange > 0) pushEnergy();
				if (fluidChange > 0) pushFluid();
			}
			
			tickCount();
			tickPush();
			
			if (shouldUpdate) markDirty();
		}
	}
	
	public void tickCount() {
		tickCount++; tickCount %= updateRate;
	}
	
	public void tickPush() {
		pushCount++; pushCount %= pushRate;
	}
	
	public void updateBlockType() {
		if (ModCheck.ic2Loaded()) removeTileFromENet();
		setState(isActive);
		world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
		if (ModCheck.ic2Loaded()) addTileToENet();
	}
	
	protected boolean changeEnergy(boolean simulateChange) {
		if (energyChange == 0) return simulateChange;
		if (getEnergyStorage().getEnergyStored() >= getEnergyStorage().getMaxEnergyStored() && energyChange > 0) return false;
		if (getEnergyStorage().getEnergyStored() < MathHelper.abs(energyChange) && energyChange < 0) return false;
		if (!simulateChange) {
			if (changeStack(true) && changeFluid(true)) getEnergyStorage().changeEnergyStored(energyChange);
		}
		return true;
	}
	
	protected boolean changeStack(boolean simulateChange) {
		if (itemChange == 0) return simulateChange;
		if (!stackChange.matches(inventoryStacks.get(0), IngredientSorption.NEUTRAL) && !inventoryStacks.get(0).isEmpty() && !simulateChange) inventoryStacks.set(0, ItemStack.EMPTY);
		if (itemChange > 0) {
			if (!inventoryStacks.get(0).isEmpty()) if (inventoryStacks.get(0).getCount() + itemChange > getInventoryStackLimit()) return false;
			if (inventoryStacks.get(0).isEmpty() && !simulateChange) {
				if (changeEnergy(true) && changeFluid(true)) setNewStack();
			}
			else if (!simulateChange) {
				if (changeEnergy(true) && changeFluid(true)) inventoryStacks.get(0).grow(itemChange);
			}
			return true;
		} else {
			if (inventoryStacks.get(0).isEmpty() || inventoryStacks.get(0).getCount() < MathHelper.abs(itemChange)) return false;
			else if (inventoryStacks.get(0).getCount() > MathHelper.abs(itemChange) && !simulateChange) {
				if (changeEnergy(true) && changeFluid(true)) inventoryStacks.get(0).grow(itemChange);
			}
			else if (inventoryStacks.get(0).getCount() == MathHelper.abs(itemChange) && !simulateChange) {
				if (changeEnergy(true) && changeFluid(true)) inventoryStacks.set(0, ItemStack.EMPTY);
			}
			return true;
		}
	}
	
	protected void setNewStack() {
		inventoryStacks.set(0, stackChange.getStack());
	}
	
	protected boolean changeFluid(boolean simulateChange) {
		if (fluidChange == 0) return simulateChange;
		if (getTanks().get(0).getFluidAmount() >= getTanks().get(0).getCapacity() && fluidChange > 0) return false;
		if (getTanks().get(0).getFluidAmount() < MathHelper.abs(fluidChange) && fluidChange < 0) return false;
		if (!simulateChange) {
			if (changeEnergy(true) && changeStack(true)) {
				if (fluidChange > 0) {
					if (fluidStackChange != null) getTanks().get(0).changeFluidStored(fluidType, fluidChange);
				}
				else getTanks().get(0).changeFluidAmount(fluidChange);
			}
		}
		return true;
	}
	
	protected boolean isRunning(boolean energy, boolean stack, boolean fluid) {
		if (energyChange == 0 && itemChange == 0 && fluidChange == 0) return true;
		if (energyChange >= 0) {
			if (itemChange >= 0) {
				if (fluidChange >= 0) return energy || stack || fluid;
				else return fluid;
			} else {
				if (fluidChange >= 0) return stack;
				else return stack && fluid;
			}
		} else {
			if (itemChange >= 0) {
				if (fluidChange >= 0) return energy;
				else return energy && fluid;
			} else {
				if (fluidChange >= 0) return energy && stack;
				else return energy && stack && fluid;
			}
		}
	}
	
	@Override
	public int getEnergyChange() {
		return energyChange;
	}
	
	@Override
	public int getItemChange() {
		return itemChange;
	}
	
	@Override
	public int getFluidChange() {
		return fluidChange;
	}
	
	@Override
	public boolean canPushEnergyTo() {
		return energyChange < 0;
	}
	
	@Override
	public boolean canPushItemsTo() {
		return itemChange < 0;
	}
	
	@Override
	public boolean canPushFluidsTo() {
		return fluidChange < 0;
	}
	
	// Inventory
	
	@Override
	public int getInventoryStackLimit() {
		return itemChange == 0 ? 1 : 6*MathHelper.abs(itemChange);
	}
	
	// Sided Inventory

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[] {0};
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing direction) {
		return itemChange < 0 && stackChange.matches(stack, IngredientSorption.NEUTRAL);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction) {
		return itemChange > 0;
	}
	
	// IC2 EU

	@Override
	public int getEUSourceTier() {
		return 2;
	}

	@Override
	public int getEUSinkTier() {
		return 4;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setBoolean("isRunning", isActive);
		nbt.setBoolean("energyBool", energyBool);
		nbt.setBoolean("stackBool", stackBool);
		nbt.setBoolean("fluidBool", fluidBool);
		return nbt;
	}
		
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		isActive = nbt.getBoolean("isRunning");
		energyBool = nbt.getBoolean("energyBool");
		stackBool = nbt.getBoolean("stackBool");
		fluidBool = nbt.getBoolean("fluidBool");
	}
	
	// Capability
	
	IItemHandler itemHandler = new InvWrapper(this);
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		if (energyChange != 0 && hasEnergySideCapability(side)) {
			side = nonNullSide(side);
			if (capability == CapabilityEnergy.ENERGY) return getEnergySide(side) != null;
			if (ModCheck.gregtechLoaded() && NCConfig.enable_gtce_eu) if (capability == GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER) return getEnergySideGT(side) != null;
		}
		if (fluidChange != 0 && hasFluidSideCapability(side)) {
			side = nonNullSide(side);
			if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return true;
			if (ModCheck.mekanismLoaded()) if (GasHelper.isGasCapability(capability)) return getGasWrapper() != null;
		}
		if (itemChange != 0) {
			if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
		}
		if (capability == IRadiationSource.CAPABILITY_RADIATION_SOURCE) return true;
		
		return hasCapabilityDefault(capability, side);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (energyChange != 0 && hasEnergySideCapability(side)) {
			side = nonNullSide(side);
			if (capability == CapabilityEnergy.ENERGY) return (T) getEnergySide(side);
			if (ModCheck.gregtechLoaded() && NCConfig.enable_gtce_eu) if (capability == GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER) return (T) getEnergySideGT(side);
		}
		if (fluidChange != 0 && hasFluidSideCapability(side)) {
			side = nonNullSide(side);
			if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return (T) getFluidSide(side);
			if (ModCheck.mekanismLoaded()) if (GasHelper.isGasCapability(capability)) return (T) getGasWrapper();
		}
		if (itemChange != 0) {
			if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T) itemHandler;
		}
		if (capability == IRadiationSource.CAPABILITY_RADIATION_SOURCE) return (T) getRadiationSource();
		
		return getCapabilityDefault(capability, side);
	}
}
