package nc.tile.energy.processor;

import ic2.api.energy.event.EnergyTileUnloadEvent;
import nc.ModCheck;
import nc.energy.EnumStorage.Connection;
import nc.handler.ProcessorRecipeHandler;
import nc.init.NCItems;
import nc.tile.energy.TileEnergySidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;

public abstract class TileEnergyProcessor extends TileEnergySidedInventory {
	
	public final int baseProcessTime;
	public final int baseProcessPower;
	public final int inputSize;
	public final int outputSize;
	
	public int time;
	public boolean isProcessing;
	
	public final boolean hasUpgrades;
	public final int upgradeMeta;
	
	public final ProcessorRecipeHandler recipes;
	
	public TileEnergyProcessor(String name, int inSize, int outSize, int time, int power, ProcessorRecipeHandler recipes) {
		this(name, inSize, outSize, time, power, false, recipes, 1);
	}
	
	public TileEnergyProcessor(String name, int inSize, int outSize, int time, int power, ProcessorRecipeHandler recipes, int upgradeMeta) {
		this(name, inSize, outSize, time, power, true, recipes, upgradeMeta);
	}
	
	public TileEnergyProcessor(String name, int inSize, int outSize, int time, int power, boolean upgrades, ProcessorRecipeHandler recipes, int upgradeMeta) {
		super(name, inSize + outSize + (upgrades ? 2 : 0), 32000, Connection.IN);
		inputSize = inSize;
		outputSize = outSize;
		baseProcessTime = time;
		baseProcessPower = power;
		hasUpgrades = upgrades;
		this.recipes = recipes;
		this.upgradeMeta = upgradeMeta;
		
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
		super.update();
		updateProcessor();
	}
	
	public void updateProcessor() {
		boolean flag = isProcessing;
		boolean flag1 = false;
		if(!world.isRemote) {
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
				if (time != 0 && !isPowered()) time = MathHelper.clamp(time - 2*getSpeedMultiplier(), 0, baseProcessTime);
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
		if (!world.isRemote) isProcessing = isProcessing();
	}
	
	public boolean isProcessing() {
		if (world.isRemote) return isProcessing;
		return !isPowered() && canProcess();
	}
	
	public boolean isPowered() {
		return world.isBlockPowered(pos);
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
		ItemStack speedStack = inventoryStacks.get(inputSize + outputSize);
		if (speedStack == ItemStack.EMPTY) return 1;
		return speedStack.getCount() + 1;
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
		for (int i = 0; i < inputSize; i++) {
			if (inventoryStacks.get(i) == ItemStack.EMPTY) {
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
		if (output == null || output.length != outputSize) {
			return false;
		}
		for(int j = 0; j < outputSize; j++) {
			if (output[j] == ItemStack.EMPTY || output[j] == null) {
				return false;
			} else {
				if (inventoryStacks.get(j + inputSize) != ItemStack.EMPTY) {
					if (!inventoryStacks.get(j + inputSize).isItemEqual((ItemStack)output[j])) {
						return false;
					} else if (inventoryStacks.get(j + inputSize).getCount() + ((ItemStack)output[j]).getCount() > inventoryStacks.get(j + inputSize).getMaxStackSize()) {
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
		for (int j = 0; j < outputSize; j++) {
			if (output[j] != ItemStack.EMPTY) {
				if (inventoryStacks.get(j + inputSize) == ItemStack.EMPTY) {
					ItemStack outputStack = ((ItemStack)output[j]).copy();
					inventoryStacks.set(j + inputSize, outputStack);
				} else if (inventoryStacks.get(j + inputSize).isItemEqual((ItemStack)output[j])) {
					inventoryStacks.get(j + inputSize).setCount(inventoryStacks.get(j + inputSize).getCount() + ((ItemStack)output[j]).getCount());
				}
			}
		}
		for (int i = 0; i < inputSize; i++) {
			if (recipes != null) {
				inventoryStacks.get(i).setCount(inventoryStacks.get(i).getCount() - recipes.getInputSize(inputOrder[i], output));
			} else {
				inventoryStacks.get(i).setCount(inventoryStacks.get(i).getCount() - 1);
			}
			if (inventoryStacks.get(i).getCount() <= 0) {
				inventoryStacks.set(i, ItemStack.EMPTY);
			}
		}
	}
	
	public Object[] inputs() {
		Object[] input = new Object[inputSize];
		for (int i = 0; i < inputSize; i++) {
			input[i] = inventoryStacks.get(i);
		}
		return input;
	}
	
	public Object[] getOutput(Object... stacks) {
		return recipes.getOutput(stacks);
	}
	
	// Inventory
	
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (stack == ItemStack.EMPTY) return false;
		else if (hasUpgrades) {
			if (stack.getItem() == NCItems.upgrade) {
				if (slot == inputSize + outputSize) return stack.getMetadata() == 0;
				else if (slot == inputSize + outputSize + 1) return stack.getMetadata() == upgradeMeta;
			}
		}
		else if (slot >= inputSize) return false;
		return true;
	}
	
	/*public boolean isOxidiser() {
		return inventoryName == Global.MOD_ID + ".container.oxidiser";
	}*/
	
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
