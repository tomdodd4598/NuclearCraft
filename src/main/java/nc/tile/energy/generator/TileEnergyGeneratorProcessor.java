package nc.tile.energy.generator;

import ic2.api.energy.event.EnergyTileUnloadEvent;
import nc.ModCheck;
import nc.energy.EnumStorage.Connection;
import nc.handlers.ProcessorRecipeHandler;
import nc.tile.energy.TileEnergySidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;

public abstract class TileEnergyGeneratorProcessor extends TileEnergySidedInventory {

	public final int inputSize;
	public final int outputSize;
	public final int otherSlotsSize;
	
	public int time;
	public boolean isGenerating;
	public boolean hasConsumed;
	
	public final ProcessorRecipeHandler recipes;
	
	public TileEnergyGeneratorProcessor(String name, int inSize, int outSize, int otherSize, int capacity, ProcessorRecipeHandler recipes) {
		super(name, 2*inSize + outSize + otherSize, capacity, Connection.OUT);
		inputSize = inSize;
		outputSize = outSize;
		otherSlotsSize = otherSize;
		this.recipes = recipes;
		
		int[] topSlots1 = new int[inSize];
		for (int i = 0; i < topSlots1.length; i++) {
			topSlots1[i] = i;
		}
		topSlots = topSlots1;
		
		int[] sideSlots1 = new int[inSize + outSize];
		for (int i = 0; i < sideSlots1.length; i++) {
			sideSlots1[i] = i;
		}
		sideSlots = sideSlots1;
		
		int[] bottomSlots1 = new int[outSize];
		for (int i = inSize; i < inSize + bottomSlots1.length; i++) {
			bottomSlots1[i - inSize] = i;
		}
		bottomSlots = bottomSlots1;
	}
	
	public void update() {
		boolean flag = isGenerating;
		boolean flag1 = false;
		super.update();
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
				setBlockState();
				//invalidate();
				if (isEnergyTileSet && ModCheck.ic2Loaded()) {
					MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
					isEnergyTileSet = false;
				}
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
		for (int i = 0; i < inputSize; i++) {
			if (inventoryStacks[i + inputSize + outputSize + otherSlotsSize] != null) {
				return true;
			}
		}
		return false;
	}
	
	// Processing
	
	public abstract int getRateMultiplier();
		
	public abstract void setRateMultiplier(int value);
		
	public abstract int getProcessTime();
		
	public abstract void setProcessTime(int value);
		
	public abstract int getProcessPower();
		
	public abstract void setProcessPower(int value);
		
	public boolean canProcess() {
		for (int i = 0; i < inputSize; i++) {
			if (inventoryStacks[i] == null && !hasConsumed) {
				return false;
			}
		}
		if (time >= getProcessTime()) {
			return true;
		}
		ItemStack[] output = hasConsumed ? getOutput(consumedInputs()) : getOutput(inputs());
		if (output == null || output.length != outputSize) {
			return false;
		}
		for(int j = 0; j < outputSize; j++) {
			if (output[j] == null) {
				return false;
			} else {
				if (inventoryStacks[j + inputSize] != null) {
					if (!inventoryStacks[j + inputSize].isItemEqual(output[j])) {
						return false;
					} else if (inventoryStacks[j + inputSize].stackSize + output[j].stackSize > inventoryStacks[j + inputSize].getMaxStackSize()) {
						return false;
					}
				}
			}
		}
		return true;
	}
		
	public void consume() {
		if (!hasConsumed) {
			for (int i = 0; i < inputSize; i++) {
				if (inventoryStacks[i + inputSize + outputSize + otherSlotsSize] != null) {
					inventoryStacks[i + inputSize + outputSize + otherSlotsSize] = null;
				}
			}
			ItemStack[] output = getOutput(inputs());
			if (output[0] == null) return;
			for (int i = 0; i < inputSize; i++) {
				if (recipes != null) {
					inventoryStacks[i + inputSize + outputSize + otherSlotsSize] = new ItemStack(inventoryStacks[i].getItem(), recipes.getInputSize(i, output), inventoryStacks[i].getMetadata());
					inventoryStacks[i].stackSize = inventoryStacks[i].stackSize - recipes.getInputSize(i, output);
				} else {
					inventoryStacks[i + inputSize + outputSize + otherSlotsSize] = new ItemStack(inventoryStacks[i].getItem(), 1, inventoryStacks[i].getMetadata());
					inventoryStacks[i].stackSize = inventoryStacks[i].stackSize - 1;
				}
				if (inventoryStacks[i].stackSize <= 0) {
					inventoryStacks[i] = null;
				}
			}
			hasConsumed = true;
		}
	}
		
	public void output() {
		if (hasConsumed) {
			ItemStack[] output = getOutput(consumedInputs());
			for (int j = 0; j < outputSize; j++) {
				if (output[j] != null) {
					if (inventoryStacks[j + inputSize] == null) {
						ItemStack outputStack = output[j].copy();
						inventoryStacks[j + inputSize] = outputStack;
					} else if (inventoryStacks[j + inputSize].isItemEqual(output[j])) {
						inventoryStacks[j + inputSize].stackSize = inventoryStacks[j + inputSize].stackSize + output[j].stackSize;
					}
				}
			}
			for (int i = inputSize + outputSize + otherSlotsSize; i < 2*inputSize + outputSize + otherSlotsSize; i++) {
				/*if (recipes != null) {
					inventoryStacks.get(i).setCount(inventoryStacks.get(i).getCount() - recipes.getInputSize(i, output));
				} else {
					inventoryStacks.get(i).setCount(inventoryStacks.get(i).getCount() - 1);
				}
				if (inventoryStacks.get(i).getCount() <= 0) {
					inventoryStacks.set(i, null);
				}*/
				inventoryStacks[i] = null;
			}
			hasConsumed = false;
		}
	}
		
	public ItemStack[] inputs() {
		ItemStack[] input = new ItemStack[inputSize];
		for (int i = 0; i < inputSize; i++) {
			input[i] = inventoryStacks[i];
		}
		return input;
	}
		
	public ItemStack[] consumedInputs() {
		ItemStack[] input = new ItemStack[inputSize];
		for (int i = 0; i < inputSize; i++) {
			input[i] = inventoryStacks[i + inputSize + outputSize + otherSlotsSize];
		}
		return input;
	}
		
	public ItemStack[] getOutput(ItemStack... itemstacks) {
		return recipes.getOutput(itemstacks);
	}
	
	// Inventory
	
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (stack == null) return false;
		else if (slot >= inputSize && slot < inputSize + outputSize) return false;
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
