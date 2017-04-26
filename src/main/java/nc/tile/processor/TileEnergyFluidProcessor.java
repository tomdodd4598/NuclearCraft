package nc.tile.processor;

import ic2.api.energy.event.EnergyTileUnloadEvent;
import nc.ModCheck;
import nc.energy.EnumStorage.EnergyConnection;
import nc.fluid.EnumTank.FluidConnection;
import nc.handler.ProcessorRecipeHandler;
import nc.init.NCItems;
import nc.tile.energyFluid.TileEnergyFluidSidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;

public abstract class TileEnergyFluidProcessor extends TileEnergyFluidSidedInventory {
	
	public final int baseProcessTime;
	public final int baseProcessPower;
	public final int fluidInputSize;
	public final int fluidOutputSize;
	
	public int time;
	public boolean isProcessing;
	
	public final boolean hasUpgrades;
	public final int upgradeMeta;
	
	public final ProcessorRecipeHandler recipes;
	
	public TileEnergyFluidProcessor(String name, int fluidInSize, int fluidOutSize, int[] fluidCapacity, FluidConnection[] fluidConnection, String[][] allowedFluids, int time, int power, ProcessorRecipeHandler recipes) {
		this(name, fluidInSize, fluidOutSize, fluidCapacity, fluidConnection, allowedFluids, time, power, false, recipes, 1);
	}
	
	public TileEnergyFluidProcessor(String name, int fluidInSize, int fluidOutSize, int[] fluidCapacity, FluidConnection[] fluidConnection, String[][] allowedFluids, int time, int power, ProcessorRecipeHandler recipes, int upgradeMeta) {
		this(name, fluidInSize, fluidOutSize, fluidCapacity, fluidConnection, allowedFluids, time, power, true, recipes, upgradeMeta);
	}
	
	public TileEnergyFluidProcessor(String name, int fluidInSize, int fluidOutSize, int[] fluidCapacity, FluidConnection[] fluidConnection, String[][] allowedFluids, int time, int power, boolean upgrades, ProcessorRecipeHandler recipes, int upgradeMeta) {
		super(name, upgrades ? 2 : 0, 32000, EnergyConnection.IN, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnection, allowedFluids);
		fluidInputSize = fluidInSize;
		fluidOutputSize = fluidOutSize;
		baseProcessTime = time;
		baseProcessPower = power;
		hasUpgrades = upgrades;
		this.recipes = recipes;
		this.upgradeMeta = upgradeMeta;
	}
	
	public void update() {
		super.update();
		updateProcessor();
	}
	
	public void updateProcessor() {
		boolean flag = isProcessing;
		boolean flag1 = false;
		if(!worldObj.isRemote) {
			if (canProcess() && !isPowered()) {
				isProcessing = true;
				time += getSpeedMultiplier();
				storage.changeEnergyStored(-getProcessPower());
				if (time >= baseProcessTime) {
					time = 0;
					process();
				}
			} else {
				isProcessing = false;
				if (time != 0 && !isPowered()) time = MathHelper.clamp_int(time - 2*getSpeedMultiplier(), 0, baseProcessTime);
			}
			if (flag != isProcessing) {
				flag1 = true;
				setBlockState();
				//invalidate();
				if (isEnergyTileSet && ModCheck.ic2Loaded()) {
					MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
					isEnergyTileSet = false;
				}
			}
		} else {
			isProcessing = canProcess() && !isPowered();
		}
		
		if (flag1) {
			markDirty();
		}
	}
	
	public abstract void setBlockState();
	
	public void onAdded() {
		super.onAdded();
		if (!worldObj.isRemote) isProcessing = isProcessing();
	}
	
	public boolean isProcessing() {
		if (worldObj.isRemote) return isProcessing;
		return !isPowered() && canProcess();
	}
	
	public boolean isPowered() {
		return worldObj.isBlockPowered(pos);
	}
	
	public boolean canProcess() {
		return canProcessStacks();
	}
	
	// IC2 Tiers
	
	public int getSourceTier() {
		return 1;
	}
	
	public int getSinkTier() {
		return 2;
	}
	
	// Processing
	
	public int getSpeedMultiplier() {
		if (!hasUpgrades) return 1;
		ItemStack speedStack = inventoryStacks[0];
		if (speedStack == null) return 1;
		return speedStack.stackSize + 1;
	}
	
	public int getProcessTime() {
		return Math.max(1, baseProcessTime/getSpeedMultiplier());
	}
	
	public int getProcessPower() {
		return baseProcessPower*getSpeedMultiplier()*(getSpeedMultiplier() + 1) / 2;
	}
	
	public int getProcessEnergy() {
		return getProcessTime()*getProcessPower();
	}
	
	public boolean canProcessStacks() {
		for (int i = 0; i < fluidInputSize; i++) {
			if (tanks[i].getFluidAmount() <= 0) {
				return false;
			}
		}
		if (time >= baseProcessTime) {
			return true;
		}
		if (getProcessEnergy() > getMaxEnergyStored() && time <= 0 && getEnergyStored() < getMaxEnergyStored() /*- getProcessPower()*/) {
			return false;
		}
		if (getProcessEnergy() <= getMaxEnergyStored() && time <= 0 && getProcessEnergy() > getEnergyStored()) {
			return false;
		}
		if (getEnergyStored() < getProcessPower()) {
			return false;
		}
		Object[] output = getOutput(inputs());
		if (output == null || output.length != fluidOutputSize) {
			return false;
		}
		for(int j = 0; j < fluidOutputSize; j++) {
			if (output[recipes.itemInputSize + j] == null) {
				return false;
			} else {
				if (tanks[j + fluidInputSize] != null) {
					if (!tanks[j + fluidInputSize].getFluid().isFluidEqual((FluidStack) output[j])) {
						return false;
					} else if (tanks[j + fluidInputSize].getFluidAmount() + ((FluidStack) output[j]).amount > tanks[j + fluidInputSize].getCapacity()) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public void process() {
		Object[] output = getOutput(inputs());
		int[] inputOrder = recipes.getInputOrder(inputs(), recipes.getInput(output));
		for (int j = 0; j < fluidOutputSize; j++) {
			if (output[j] != null) {
				if (tanks[j + fluidInputSize] == null) {
					FluidStack outputStack = ((FluidStack) output[j]).copy();
					tanks[j + fluidInputSize].setFluidStored(outputStack);
				} else if (tanks[j + fluidInputSize].getFluid().isFluidEqual((FluidStack) output[j])) {
					tanks[j + fluidInputSize].changeFluidStored(((FluidStack) output[j]).amount);
				}
			}
		}
		for (int i = 0; i < fluidInputSize; i++) {
			if (recipes != null) {
				tanks[i].changeFluidStored(-recipes.getInputSize(inputOrder[i], output));
			} else {
				tanks[i].changeFluidStored(-1000);
			}
			if (tanks[i].getFluidAmount() <= 0) {
				tanks[i].setFluidStored(null);
			}
		}
	}
	
	public Object[] inputs() {
		Object[] input = new Object[fluidInputSize];
		for (int i = 0; i < fluidInputSize; i++) {
			input[i] = tanks[i].getFluid();
		}
		return input;
	}
	
	public Object[] getOutput(Object... stacks) {
		return recipes.getOutput(stacks);
	}
	
	// Inventory
	
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (stack == null) return false;
		else if (hasUpgrades) {
			if (stack.getItem() == NCItems.upgrade) {
				if (slot == 0) return stack.getMetadata() == 0;
				else if (slot == 1) return stack.getMetadata() == upgradeMeta;
			}
		}
		return false;
	}
	
	// SidedInventory
	
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[] {};
	}

	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing direction) {
		return false;
	}

	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction) {
		return false;
	}
	
	// Fluids
	
	public boolean canFill(FluidStack resource, int tankNumber) {
		if (tankNumber >= fluidInputSize) return false;
		for (int i = 0; i < fluidInputSize; i++) {
			if (tankNumber != i && tanks[i].getFluid().isFluidEqual(resource)) return false;
		}
		return true;
	}
	
	// NBT
	
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setInteger("time", time);
		nbt.setBoolean("isProcessing", isProcessing);
		return nbt;
	}
	
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		time = nbt.getInteger("time");
		isProcessing = nbt.getBoolean("isProcessing");
	}
	
	// Inventory Fields

	public int getFieldCount() {
		return 2;
	}

	public int getField(int id) {
		switch (id) {
		case 0:
			return time;
		case 1:
			return getEnergyStored();
		default:
			return 0;
		}
	}

	public void setField(int id, int value) {
		switch (id) {
		case 0:
			time = value;
			break;
		case 1:
			storage.setEnergyStored(value);
		}
	}
}
