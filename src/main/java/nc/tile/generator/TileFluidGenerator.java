package nc.tile.generator;

import ic2.api.energy.EnergyNet;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.energy.EnumStorage.EnergyConnection;
import nc.fluid.EnumTank.FluidConnection;
import nc.handler.ProcessorRecipeHandler;
import nc.tile.IGui;
import nc.tile.dummy.IInterfaceable;
import nc.tile.energyFluid.TileEnergyFluidSidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;

public abstract class TileFluidGenerator extends TileEnergyFluidSidedInventory implements IInterfaceable, IGui {

	public final int fluidInputSize;
	public final int fluidOutputSize;
	public final int otherSlotsSize;
	
	public int time;
	public boolean isGenerating;
	public boolean hasConsumed;
	
	public int tickCount;
	
	public final ProcessorRecipeHandler recipes;
	
	public TileFluidGenerator(String name, int fluidInSize, int fluidOutSize, int otherSize, int[] fluidCapacity, FluidConnection[] fluidConnection, String[][] allowedFluids, int capacity, ProcessorRecipeHandler recipes) {
		super(name, otherSize, capacity, EnergyConnection.OUT, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnection, allowedFluids);
		fluidInputSize = fluidInSize;
		fluidOutputSize = fluidOutSize;
		otherSlotsSize = otherSize;
		this.recipes = recipes;
	}
	
	public void update() {
		super.update();
		updateGenerator();
	}
	
	public void updateGenerator() {
		boolean flag = isGenerating;
		boolean flag1 = false;
		if(!worldObj.isRemote) {
			if (time == 0) {
				consume();
			}
			if (canProcess() && isPowered()) {
				isGenerating = true;
				time += getRateMultiplier();
				storage.changeEnergyStored(getProcessPower());
				if (time >= getProcessTime()) {
					time = 0;
					output();
				}
			} else {
				isGenerating = false;
			}
			if (flag != isGenerating) {
				flag1 = true;
				if (isEnergyTileSet && ModCheck.ic2Loaded()) {
					/*MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));*/ EnergyNet.instance.removeTile(this);
					isEnergyTileSet = false;
				}
				setBlockState();
				//invalidate();
			}
			pushEnergy();
		} else {
			isGenerating = canProcess() && isPowered();
		}
		
		if (flag1) {
			markDirty();
		}
	}
	
	public abstract void setBlockState();
	
	public void tick() {
		if (tickCount > NCConfig.generator_update_rate) {
			tickCount = 0;
		} else {
			tickCount++;
		}
	}
	
	public boolean shouldCheck() {
		return tickCount > NCConfig.generator_update_rate;
	}
	
	public void onAdded() {
		super.onAdded();
		if (!worldObj.isRemote) isGenerating = isGenerating();
		if (!worldObj.isRemote) hasConsumed = hasConsumed();
	}
	
	public boolean isGenerating() {
		if (worldObj.isRemote) return isGenerating;
		return isPowered() && time > 0;
	}
	
	public boolean isPowered() {
		return worldObj.isBlockPowered(pos);
	}
	
	public boolean hasConsumed() {
		if (worldObj.isRemote) return hasConsumed;
		for (int i = 0; i < fluidInputSize; i++) {
			if (tanks[i + fluidInputSize + fluidOutputSize].getFluid() != null) {
				return true;
			}
		}
		return false;
	}
	
	public boolean canProcess() {
		return canProcessStacks();
	}
	
	// Processing
	
	public abstract int getRateMultiplier();
		
	public abstract void setRateMultiplier(int value);
		
	public abstract int getProcessTime();
		
	public abstract void setProcessTime(int value);
		
	public abstract int getProcessPower();
		
	public abstract void setProcessPower(int value);
		
	public boolean canProcessStacks() {
		for (int i = 0; i < fluidInputSize; i++) {
			if (tanks[i].getFluidAmount() <= 0 && !hasConsumed) {
				return false;
			}
		}
		if (time >= getProcessTime()) {
			return true;
		}
		Object[] output = hasConsumed ? getOutput(consumedInputs()) : getOutput(inputs());
		int[] inputOrder = recipes.getInputOrder(inputs(), recipes.getInput(output));
		if (output == null || output.length != fluidOutputSize || inputOrder == ProcessorRecipeHandler.INVALID_ORDER) {
			return false;
		}
		for(int j = 0; j < fluidOutputSize; j++) {
			if (output[recipes.itemOutputSize + j] == null) {
				return false;
			} else {
				if (tanks[j + fluidInputSize].getFluid() != null) {
					if (!tanks[j + fluidInputSize].getFluid().isFluidEqual((FluidStack)output[recipes.itemOutputSize + j])) {
						return false;
					} else if (tanks[j + fluidInputSize].getFluidAmount() + ((FluidStack)output[recipes.itemOutputSize + j]).amount > tanks[j + fluidInputSize].getCapacity()) {
						return false;
					}
				}
			}
		}
		return true;
	}
		
	public void consume() {
		if (!hasConsumed) {
			for (int i = 0; i < fluidInputSize; i++) {
				if (tanks[i + fluidInputSize + fluidOutputSize].getFluid() != null) {
					tanks[i + fluidInputSize + fluidOutputSize].setFluid(null);
				}
			}
			Object[] output = getOutput(inputs());
			int[] fluidInputOrder = recipes.getFluidInputOrder(inputs(), recipes.getInput(output));
			if (output[0] == null || fluidInputOrder == ProcessorRecipeHandler.INVALID_ORDER) return;
			for (int i = 0; i < fluidInputSize; i++) {
				if (recipes != null) {
					tanks[i + fluidInputSize + fluidOutputSize].changeFluidStored(tanks[i].getFluid().getFluid(), recipes.getInputSize(fluidInputOrder[i], output));
					tanks[i].changeFluidStored(-recipes.getInputSize(fluidInputOrder[i], output));
				} else {
					tanks[i + fluidInputSize + fluidOutputSize].changeFluidStored(tanks[i].getFluid().getFluid(), 1000);
					tanks[i].changeFluidStored(-1000);
				}
				if (tanks[i].getFluidAmount() <= 0) {
					tanks[i].setFluidStored(null);
				}
			}
			hasConsumed = true;
		}
	}
		
	public void output() {
		if (hasConsumed) {
			Object[] output = getOutput(consumedInputs());
			for (int j = 0; j < fluidOutputSize; j++) {
				if (output[j] != null) {
					if (tanks[j + fluidInputSize].getFluid() == null) {
						FluidStack outputStack = ((FluidStack)output[j]).copy();
						tanks[j + fluidInputSize].setFluidStored(outputStack);
					} else if (tanks[j + fluidInputSize].getFluid().isFluidEqual((FluidStack)output[j])) {
						tanks[j + fluidInputSize].changeFluidStored(((FluidStack)output[j]).amount);
					}
				}
			}
			for (int i = fluidInputSize + fluidOutputSize; i < 2*fluidInputSize + fluidOutputSize; i++) {
				tanks[i].setFluid(null);
			}
			hasConsumed = false;
		}
	}
		
	public Object[] inputs() {
		Object[] input = new Object[fluidInputSize];
		for (int i = 0; i < fluidInputSize; i++) {
			input[i] = tanks[i].getFluid();
		}
		return input;
	}
		
	public Object[] consumedInputs() {
		Object[] input = new Object[fluidInputSize];
		for (int i = 0; i < fluidInputSize; i++) {
			input[i] = tanks[i + fluidInputSize + fluidOutputSize].getFluid();
		}
		return input;
	}
		
	public Object[] getOutput(Object... stacks) {
		return recipes.getOutput(stacks);
	}
	
	// Inventory
	
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return false;
	}
	
	public boolean isItemValid(ItemStack stack) {
		Object[] inputSets = recipes.getRecipes().keySet().toArray();
		for (int i = 0; i < inputSets.length; i++) {
			Object[] inputSet = (Object[])(inputSets[i]);
			for (int j = 0; j < inputSet.length; j++) {
				if (inputSet[j] instanceof ItemStack) {
					if (ItemStack.areItemsEqual((ItemStack)(inputSet[j]), stack)) return true;
				} else if (inputSet[j] instanceof ItemStack[]) {
					ItemStack[] stacks = (ItemStack[])(inputSet[j]);
					for (int k = 0; k < stacks.length; k++) {
						if (ItemStack.areItemsEqual(stacks[k], stack)) return true;
					}
				}
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
			if (tankNumber != i && tanks[i].getFluid() != null) if (tanks[i].getFluid().isFluidEqual(resource)) return false;
		}
		return true;
	}
	
	// NBT
	
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setInteger("time", time);
		nbt.setBoolean("isGenerating", isGenerating);
		nbt.setBoolean("hasConsumed", hasConsumed);
		return nbt;
	}
		
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		time = nbt.getInteger("time");
		isGenerating = nbt.getBoolean("isGenerating");
		hasConsumed = nbt.getBoolean("hasConsumed");
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
