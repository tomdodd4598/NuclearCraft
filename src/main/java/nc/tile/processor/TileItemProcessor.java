package nc.tile.processor;

import java.util.ArrayList;

import nc.config.NCConfig;
import nc.init.NCItems;
import nc.recipe.BaseRecipeHandler;
import nc.recipe.IIngredient;
import nc.recipe.IRecipe;
import nc.recipe.NCRecipes;
import nc.recipe.RecipeMethods;
import nc.recipe.SorptionType;
import nc.tile.IGui;
import nc.tile.dummy.IInterfaceable;
import nc.tile.energy.TileEnergySidedInventory;
import nc.tile.energyFluid.IBufferable;
import nc.tile.internal.EnumEnergyStorage.EnergyConnection;
import nc.util.NCMathHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

public abstract class TileItemProcessor extends TileEnergySidedInventory implements IInterfaceable, IBufferable, IGui {
	
	public final int defaultProcessTime;
	public int baseProcessTime;
	public final int baseProcessPower;
	public final int inputSize, outputSize;
	
	public int time;
	public boolean isProcessing;
	
	public final boolean hasUpgrades;
	public final int upgradeMeta;
	
	public int tickCount;
	
	public final NCRecipes.Type recipeType;
	
	public TileItemProcessor(String name, int inSize, int outSize, int time, int power, NCRecipes.Type recipeType) {
		this(name, inSize, outSize, time, power, false, recipeType, 1);
	}
	
	public TileItemProcessor(String name, int inSize, int outSize, int time, int power, NCRecipes.Type recipeType, int upgradeMeta) {
		this(name, inSize, outSize, time, power, true, recipeType, upgradeMeta);
	}
	
	public TileItemProcessor(String name, int inSize, int outSize, int time, int power, boolean upgrades, NCRecipes.Type recipeType, int upgradeMeta) {
		super(name, inSize + outSize + (upgrades ? 2 : 0), 32000, power != 0 ? EnergyConnection.IN : EnergyConnection.NON);
		inputSize = inSize;
		outputSize = outSize;
		defaultProcessTime = time;
		baseProcessTime = time;
		baseProcessPower = power;
		hasUpgrades = upgrades;
		this.upgradeMeta = upgradeMeta;
		
		this.recipeType = recipeType;
		
		int[] topSlots1 = new int[inSize];
		for (int i = 0; i < topSlots1.length; i++) topSlots1[i] = i;
		topSlots = topSlots1;
		
		int[] sideSlots1 = new int[inSize + outSize];
		for (int i = 0; i < sideSlots1.length; i++) sideSlots1[i] = i;
		sideSlots = sideSlots1;
		
		int[] bottomSlots1 = new int[outSize];
		for (int i = inSize; i < inSize + bottomSlots1.length; i++) bottomSlots1[i - inSize] = i;
		bottomSlots = bottomSlots1;
	}
	
	public BaseRecipeHandler getRecipeHandler() {
		return recipeType.getRecipeHandler();
	}
	
	@Override
	public void onAdded() {
		super.onAdded();
		baseProcessTime = defaultProcessTime;
		if (!world.isRemote) isProcessing = !isPowered() && canProcess();
	}
	
	@Override
	public void update() {
		super.update();
		updateProcessor();
	}
	
	public void updateProcessor() {
		boolean wasProcessing = isProcessing;
		isProcessing = canProcess() && !isPowered();
		setCapacityFromSpeed();
		boolean shouldUpdate = false;
		if (!world.isRemote) {
			tick();
			if (isProcessing) process();
			else if (!isPowered()) loseProgress();
			if (wasProcessing != isProcessing) {
				shouldUpdate = true;
				updateBlockType();
			}
		}
		if (shouldUpdate) markDirty();
	}
	
	public void tick() {
		if (tickCount > NCConfig.processor_update_rate) tickCount = 0; else tickCount++;
	}
	
	public boolean shouldCheck() {
		return tickCount > NCConfig.processor_update_rate;
	}
	
	public boolean canProcess() {
		return canProcessStacks();
	}
	
	public boolean isPowered() {
		return world.isBlockPowered(pos);
	}
	
	public void process() {
		time += getSpeedMultiplier();
		storage.changeEnergyStored(-getProcessPower());
		if (time >= baseProcessTime) completeProcess();
	}
	
	public void completeProcess() {
		time = 0;
		produceProducts();
	}
	
	public void loseProgress() {
		time = MathHelper.clamp(time - (int) (1.5D*getSpeedMultiplier()), 0, baseProcessTime);
	}
	
	public void updateBlockType() {
		removeTileFromENet();
		setState(isProcessing);
		world.notifyNeighborsOfStateChange(pos, blockType, true);
		addTileToENet();
	}
	
	// IC2 Tiers
	
	@Override
	public int getSourceTier() {
		return 1;
	}
		
	@Override
	public int getSinkTier() {
		return 4;
	}
	
	// Processing
	
	public int getSpeedCount() {
		if (!hasUpgrades) return 1;
		ItemStack speedStack = inventoryStacks.get(inputSize + outputSize);
		if (speedStack == ItemStack.EMPTY) return 1;
		return speedStack.getCount() + 1;
	}
	
	public double getSpeedMultiplier() {
		return getSpeedCount() > 1 ? NCConfig.speed_upgrade_multipliers[0]*(NCMathHelper.simplexNumber(getSpeedCount(), NCConfig.speed_upgrade_power_laws[0]) - 1) + 1 : 1;
	}
	
	public double getPowerMultiplier() {
		return getSpeedCount() > 1 ? NCConfig.speed_upgrade_multipliers[1]*(NCMathHelper.simplexNumber(getSpeedCount(), NCConfig.speed_upgrade_power_laws[1]) - 1) + 1 : 1;
	}
	
	public int getProcessTime() {
		return Math.max(1, (int) ((double)baseProcessTime/getSpeedMultiplier()));
	}
	
	public int getProcessPower() {
		return Math.min(Integer.MAX_VALUE, (int) ((double)baseProcessPower*getPowerMultiplier()));
	}
	
	public int getProcessEnergy() {
		return getProcessTime()*getProcessPower();
	}
	
	public void setCapacityFromSpeed() {
		storage.setStorageCapacity(MathHelper.clamp(2*getProcessPower(), 32000, Integer.MAX_VALUE));
		storage.setMaxTransfer(MathHelper.clamp(2*getProcessPower(), 32000, Integer.MAX_VALUE));
	}
	
	public boolean canProcessStacks() {
		for (int i = 0; i < inputSize; i++) {
			if (inventoryStacks.get(i).isEmpty()) {
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
		Object[] outputs = outputs();
		if (outputs == null || outputs.length != outputSize) {
			return false;
		}
		for(int j = 0; j < outputSize; j++) {
			if (outputs[j] == ItemStack.EMPTY || outputs[j] == null) {
				return false;
			} else {
				if (!inventoryStacks.get(j + inputSize).isEmpty()) {
					if (!inventoryStacks.get(j + inputSize).isItemEqual((ItemStack) outputs[j])) {
						return false;
					} else if (inventoryStacks.get(j + inputSize).getCount() + ((ItemStack) outputs[j]).getCount() > inventoryStacks.get(j + inputSize).getMaxStackSize()) {
						return false;
					}
				}
			}
		}
		Object[] inputs = inputs();
		if (getRecipeHandler().getRecipeFromInputs(inputs).extras().get(0) instanceof Integer) baseProcessTime = (int) getRecipeHandler().getRecipeFromInputs(inputs).extras().get(0);
		return true;
	}
	
	public void produceProducts() {
		IRecipe recipe = getRecipe();
		Object[] outputs = outputs();
		int[] inputOrder = inputOrder();
		if (outputs == null || inputOrder == RecipeMethods.INVALID) return;
		for (int j = 0; j < outputSize; j++) {
			ItemStack outputStack = (ItemStack) outputs[j];
			if (inventoryStacks.get(j + inputSize).isEmpty()) {
				inventoryStacks.set(j + inputSize, outputStack);
			} else if (inventoryStacks.get(j + inputSize).isItemEqual(outputStack)) {
				inventoryStacks.get(j + inputSize).grow(outputStack.getCount());
			}
		}
		for (int i = 0; i < inputSize; i++) {
			if (getRecipeHandler() != null) {
				inventoryStacks.get(i).shrink(recipe.inputs().get(inputOrder[i]).getStackSize());
			} else {
				inventoryStacks.get(i).shrink(1);
			}
			if (inventoryStacks.get(i).getCount() <= 0) {
				inventoryStacks.set(i, ItemStack.EMPTY);
			}
		}
	}
	
	public IRecipe getRecipe() {
		return getRecipeHandler().getRecipeFromInputs(inputs());
	}
	
	public Object[] inputs() {
		Object[] input = new Object[inputSize];
		for (int i = 0; i < inputSize; i++) {
			input[i] = inventoryStacks.get(i);
		}
		return input;
	}
	
	public int[] inputOrder() {
		int[] inputOrder = new int[inputSize];
		IRecipe recipe = getRecipe();
		if (recipe == null) return new int[] {};
		ArrayList<IIngredient> recipeIngredients = recipe.inputs();
		for (int i = 0; i < inputSize; i++) {
			inputOrder[i] = -1;
			for (int j = 0; j < recipeIngredients.size(); j++) {
				if (recipeIngredients.get(j).matches(inputs()[i], SorptionType.INPUT)) {
					inputOrder[i] = j;
					break;
				}
			}
			if (inputOrder[i] == -1) return RecipeMethods.INVALID;
		}
		return inputOrder;
	}
	
	public Object[] outputs() {
		Object[] output = new Object[outputSize];
		IRecipe recipe = getRecipe();
		if (recipe == null) return null;
		ArrayList<IIngredient> outputs = recipe.outputs();
		for (int i = 0; i < outputSize; i++) {
			Object out = RecipeMethods.getIngredientFromList(outputs, i);
			if (out == null) return null;
			else output[i] = out;
		}
		return output;
	}
	
	// Inventory
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (stack == ItemStack.EMPTY) return false;
		if (hasUpgrades) {
			if (stack.getItem() == NCItems.upgrade) {
				if (slot == inputSize + outputSize) return stack.getMetadata() == 0;
				else if (slot == inputSize + outputSize + 1) return stack.getMetadata() == upgradeMeta;
			}
		}
		if (slot >= inputSize) return false;
		return NCConfig.smart_processor_input ? getRecipeHandler().isValidInput(stack, inputs()) : getRecipeHandler().isValidInput(stack);
	}
	
	// SidedInventory
	
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return side == EnumFacing.DOWN ? bottomSlots : (side == EnumFacing.UP ? topSlots : sideSlots);
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing direction) {
		return isItemValidForSlot(slot, stack) && direction != EnumFacing.DOWN;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction) {
		return direction != EnumFacing.UP && slot >= inputSize && slot < inputSize + outputSize;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setInteger("time", time);
		nbt.setBoolean("isProcessing", isProcessing);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		time = nbt.getInteger("time");
		isProcessing = nbt.getBoolean("isProcessing");
	}
	
	// Inventory Fields

	@Override
	public int getFieldCount() {
		return 3;
	}

	@Override
	public int getField(int id) {
		switch (id) {
		case 0:
			return time;
		case 1:
			return getEnergyStored();
		case 2:
			return baseProcessTime;
		default:
			return 0;
		}
	}

	@Override
	public void setField(int id, int value) {
		switch (id) {
		case 0:
			time = value;
			break;
		case 1:
			storage.setEnergyStored(value);
			break;
		case 2:
			baseProcessTime = value;
		}
	}
}
