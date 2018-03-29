package nc.tile.passive;

import javax.annotation.Nullable;

import nc.ModCheck;
import nc.config.NCConfig;
import nc.tile.energy.storage.EnumEnergyStorage.EnergyConnection;
import nc.tile.energyFluid.TileEnergyFluidSidedInventory;
import nc.tile.fluid.tank.EnumTank.FluidConnection;
import nc.util.StackHelper;
import net.darkhax.tesla.capability.TeslaCapabilities;
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

public abstract class TilePassiveAbstract extends TileEnergyFluidSidedInventory implements /*IInterfaceable,*/ ITilePassive {
	
	public int tickCount;
	public final int updateRate;
	public boolean isRunning;
	public boolean energyBool;
	public boolean stackBool;
	public boolean fluidBool;
	
	public final int energyChange;
	public static ItemStack stackChange;
	public final int itemChange;
	public final int fluidChange;
	public final FluidStack fluidStackChange;
	public final Fluid fluidType;
	
	public TilePassiveAbstract(String name, int energyChange, int changeRate) {
		this(name, new ItemStack(Items.BEEF), 0, energyChange, FluidRegistry.LAVA, 0, changeRate);
	}
	
	public TilePassiveAbstract(String name, ItemStack stack, int itemChange, int changeRate) {
		this(name, stack, itemChange, 0, FluidRegistry.LAVA, 0, changeRate);
	}
	
	public TilePassiveAbstract(String name, Fluid fluid, int fluidChange, int changeRate) {
		this(name, new ItemStack(Items.BEEF), 0, 0, fluid, fluidChange, changeRate);
	}
	
	public TilePassiveAbstract(String name, Fluid fluid, int fluidChange, int changeRate, String[] fluidTypes) {
		this(name, new ItemStack(Items.BEEF), 0, 0, fluid, fluidChange, changeRate, fluidTypes);
	}
	
	public TilePassiveAbstract(String name, ItemStack stack, int itemChange, int energyChange, int changeRate) {
		this(name, stack, itemChange, energyChange, FluidRegistry.LAVA, 0, changeRate);
	}
	
	public TilePassiveAbstract(String name, int energyChange, Fluid fluid, int fluidChange, int changeRate) {
		this(name, new ItemStack(Items.BEEF), 0, energyChange, fluid, fluidChange, changeRate);
	}
	
	public TilePassiveAbstract(String name, ItemStack stack, int itemChange, Fluid fluid, int fluidChange, int changeRate) {
		this(name, stack, itemChange, 0, fluid, fluidChange, changeRate);
	}
	
	public TilePassiveAbstract(String name, ItemStack stack, int itemChange, int energyChange, Fluid fluid, int fluidChange, int changeRate) {
		this(name, stack, itemChange, energyChange, fluid, fluidChange, changeRate, new String[] {fluid.getName()});
	}
	
	public TilePassiveAbstract(String name, ItemStack stack, int itemChange, int energyChange, Fluid fluid, int fluidChange, int changeRate, String[] fluidTypes) {
		super(name, 1, energyChange == 0 ? 1 : 2*MathHelper.abs(energyChange)*changeRate*NCConfig.generator_rf_per_eu, energyChange == 0 ? 0 : MathHelper.abs(energyChange)*NCConfig.generator_rf_per_eu, energyChange > 0 ? EnergyConnection.OUT : (energyChange < 0 ? EnergyConnection.IN : EnergyConnection.NON), new int[] {fluidChange == 0 ? 1 : 2*MathHelper.abs(fluidChange)*changeRate}, new FluidConnection[] {fluidChange > 0 ? FluidConnection.OUT : (fluidChange < 0 ? FluidConnection.IN : FluidConnection.NON)}, fluidTypes);
		this.energyChange = energyChange*changeRate;
		this.itemChange = itemChange*changeRate;
		stackChange = StackHelper.changeStackSize(stack, MathHelper.abs(itemChange)*changeRate);
		this.fluidChange = fluidChange*changeRate;
		fluidStackChange = new FluidStack(fluid, MathHelper.abs(fluidChange)*changeRate);
		fluidType = fluid;
		updateRate = changeRate*20;
	}
	
	@Override
	public void update() {
		boolean flag = isRunning;
		boolean flag1 = false;
		super.update();
		if(!world.isRemote) {
			if (shouldUpdate()) {
				energyBool = changeEnergy(false);
				stackBool = changeStack(false);
				fluidBool = changeFluid(false);
			}
			isRunning = isRunning(energyBool, stackBool, fluidBool);
			if (flag != isRunning) {
				flag1 = true;
				removeTileFromENet();
				setState(isRunning);
				world.notifyNeighborsOfStateChange(pos, blockType, true);
				addTileToENet();
			}
			if (itemChange > 0) pushStacks();
			if (energyChange > 0) pushEnergy();
			if (fluidChange > 0) pushFluid();
		}
		if (flag1) {
			markDirty();
		}
	}
	
	public boolean shouldUpdate() {
		if (tickCount > updateRate) tickCount = 0; else tickCount++;
		return tickCount > updateRate;
	}
	
	public boolean changeEnergy(boolean b) {
		if (energyChange == 0) return b;
		if (storage.getEnergyStored() >= storage.getMaxEnergyStored() && energyChange > 0) return false;
		if (storage.getEnergyStored() < MathHelper.abs(energyChange) && energyChange < 0) return false;
		if (!b) {
			if (changeStack(true) && changeFluid(true)) storage.changeEnergyStored(energyChange);
		}
		if (energyChange < 0) return storage.getEnergyStored() > -energyChange;
		else return true;
	}
	
	public boolean changeStack(boolean b) {
		if (itemChange == 0) return b;
		if (!ItemStack.areItemsEqual(inventoryStacks.get(0), stackChange) && !inventoryStacks.get(0).isEmpty() && !b) inventoryStacks.set(0, ItemStack.EMPTY);
		if (itemChange > 0) {
			if (!inventoryStacks.get(0).isEmpty()) if (inventoryStacks.get(0).getCount() + itemChange > getInventoryStackLimit()) return false;
			if (inventoryStacks.get(0).isEmpty() && !b) {
				if (changeEnergy(true) && changeFluid(true)) newStack();
			}
			else if (!b) {
				if (changeEnergy(true) && changeFluid(true)) inventoryStacks.get(0).grow(itemChange);
			}
			return true;
		} else {
			if (inventoryStacks.get(0).isEmpty() || inventoryStacks.get(0).getCount() < MathHelper.abs(itemChange)) return false;
			else if (inventoryStacks.get(0).getCount() > MathHelper.abs(itemChange) && !b) {
				if (changeEnergy(true) && changeFluid(true)) inventoryStacks.get(0).grow(itemChange);
			}
			else if (inventoryStacks.get(0).getCount() == MathHelper.abs(itemChange) && !b) {
				if (changeEnergy(true) && changeFluid(true)) inventoryStacks.set(0, ItemStack.EMPTY);
			}
			return true;
		}
	}
	
	public void newStack() {
		inventoryStacks.set(0, stackChange);
	}
	
	public boolean changeFluid(boolean b) {
		if (fluidChange == 0) return b;
		if (tanks[0].getFluidAmount() >= tanks[0].getCapacity() && fluidChange > 0) return false;
		if (tanks[0].getFluidAmount() < MathHelper.abs(fluidChange) && fluidChange < 0) return false;
		if (!b) {
			if (changeEnergy(true) && changeStack(true)) {
				if (fluidChange > 0) {
					if (fluidStackChange != null) tanks[0].changeFluidStored(fluidType, fluidChange);
				}
				else tanks[0].changeFluidStored(fluidChange);
			}
		}
		return true;
	}
	
	public boolean isRunning(boolean energy, boolean stack, boolean fluid) {
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
		return itemChange == 0 ? 1 : 2*MathHelper.abs(itemChange);
	}
	
	// Sided Inventory

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[] {0};
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing direction) {
		return itemChange < 0 && ItemStack.areItemsEqual(stack, stackChange);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction) {
		return itemChange > 0;
	}
	
	// IC2 EU

	@Override
	public int getSourceTier() {
		return 2;
	}

	@Override
	public int getSinkTier() {
		return 4;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setBoolean("isRunning", isRunning);
		nbt.setBoolean("energyBool", energyBool);
		nbt.setBoolean("stackBool", stackBool);
		nbt.setBoolean("fluidBool", fluidBool);
		return nbt;
	}
		
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		isRunning = nbt.getBoolean("isRunning");
		energyBool = nbt.getBoolean("energyBool");
		stackBool = nbt.getBoolean("stackBool");
		fluidBool = nbt.getBoolean("fluidBool");
	}
	
	// Capability
	
	IItemHandler itemHandler = new InvWrapper(this);
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (energyChange != 0) if (CapabilityEnergy.ENERGY == capability && energyConnection.canConnect()) {
			return true;
		}
		if (energyChange != 0) if (energyConnection != null && ModCheck.teslaLoaded() && energyConnection.canConnect()) {
			if ((capability == TeslaCapabilities.CAPABILITY_CONSUMER && energyConnection.canReceive()) || (capability == TeslaCapabilities.CAPABILITY_PRODUCER && energyConnection.canExtract()) || capability == TeslaCapabilities.CAPABILITY_HOLDER)
				return true;
		}
		if (fluidChange != 0) {
			if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return true;
			//else if (capability == Capabilities.GAS_HANDLER_CAPABILITY) return true;
			//else if (capability == Capabilities.TUBE_CONNECTION_CAPABILITY) return true;
		}
		if (itemChange != 0) if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		return false;
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (energyChange != 0) if (CapabilityEnergy.ENERGY == capability && energyConnection.canConnect()) {
			return (T) storage;
		}
		if (energyChange != 0) if (energyConnection != null && ModCheck.teslaLoaded() && energyConnection.canConnect()) {
			if ((capability == TeslaCapabilities.CAPABILITY_CONSUMER && energyConnection.canReceive()) || (capability == TeslaCapabilities.CAPABILITY_PRODUCER && energyConnection.canExtract()) || capability == TeslaCapabilities.CAPABILITY_HOLDER)
				return (T) storage;
		}
		if (fluidChange != 0) {
			if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this);
			//if (capability == Capabilities.GAS_HANDLER_CAPABILITY) return Capabilities.GAS_HANDLER_CAPABILITY.cast(this);
			//if (capability == Capabilities.TUBE_CONNECTION_CAPABILITY) return Capabilities.TUBE_CONNECTION_CAPABILITY.cast(this);
		}
		if (itemChange != 0) if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) itemHandler;
		}
		return super.getCapability(capability, facing);
	}
}
