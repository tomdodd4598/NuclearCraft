package nc.tile.generator;

import java.util.ArrayList;

import nc.ModCheck;
import nc.config.NCConfig;
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
import nc.tile.internal.energy.EnergyConnection;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public abstract class TileItemGenerator extends TileEnergySidedInventory implements IInterfaceable, IBufferable, IGui {

	public final int[] slots;
	
	public final int inputSize;
	public final int outputSize;
	public final int otherSlotsSize;
	
	public int time;
	public boolean isGenerating;
	public boolean hasConsumed;
	
	public int tickCount;
	
	public final NCRecipes.Type recipeType;
	
	public TileItemGenerator(String name, int inSize, int outSize, int otherSize, int capacity, NCRecipes.Type recipeType) {
		super(name, 2*inSize + outSize + otherSize, capacity, energyConnectionAll(EnergyConnection.OUT));
		inputSize = inSize;
		outputSize = outSize;
		otherSlotsSize = otherSize;
		
		this.recipeType = recipeType;
		
		int[] slots = new int[inSize + outSize];
		for (int i = 0; i < slots.length; i++) slots[i] = i;
		this.slots = slots;
	}
	
	public BaseRecipeHandler getRecipeHandler() {
		return recipeType.getRecipeHandler();
	}
	
	@Override
	public void onAdded() {
		super.onAdded();
		if (!world.isRemote) {
			isGenerating = isGenerating();
			hasConsumed = hasConsumed();
		}
	}
	
	@Override
	public void update() {
		super.update();
		updateGenerator();
	}
	
	public void updateGenerator() {
		boolean wasGenerating = isGenerating;
		isGenerating = canProcess() && isPowered();
		boolean shouldUpdate = false;
		if(!world.isRemote) {
			tick();
			if (time == 0) consume();
			if (isGenerating) process();
			if (wasGenerating != isGenerating) {
				shouldUpdate = true;
				updateBlockType();
			}
			pushEnergy();
		}
		if (shouldUpdate) markDirty();
	}
	
	public boolean isGenerating() {
		return canProcess() && isPowered();
	}
	
	public boolean canProcess() {
		return canProcessStacks();
	}
	
	public boolean isPowered() {
		return world.isBlockPowered(pos);
	}
	
	public void process() {
		time += getRateMultiplier();
		getEnergyStorage().changeEnergyStored(getProcessPower());
		if (time >= getProcessTime()) completeProcess();
	}
	
	public void completeProcess() {
		time = 0;
		produceProducts();
	}
	
	public void updateBlockType() {
		if (ModCheck.ic2Loaded()) removeTileFromENet();
		setState(isGenerating);
		world.notifyNeighborsOfStateChange(pos, blockType, true);
		if (ModCheck.ic2Loaded()) addTileToENet();
	}
	
	// Processing
	
	public abstract int getRateMultiplier();
		
	public abstract void setRateMultiplier(int value);
		
	public abstract int getProcessTime();
		
	public abstract void setProcessTime(int value);
		
	public abstract int getProcessPower();
		
	public abstract void setProcessPower(int value);
	
	public boolean hasConsumed() {
		if (world.isRemote) return hasConsumed;
		for (int i = 0; i < inputSize; i++) {
			if (!inventoryStacks.get(i + inputSize + outputSize + otherSlotsSize).isEmpty()) {
				return true;
			}
		}
		return false;
	}
		
	public boolean canProcessStacks() {
		for (int i = 0; i < inputSize; i++) {
			if (inventoryStacks.get(i).isEmpty() && !hasConsumed) {
				return false;
			}
		}
		if (time >= getProcessTime()) {
			return true;
		}
		Object[] output = hasConsumed ? outputs() : outputs();
		if (output == null || output.length != outputSize) {
			return false;
		}
		for(int j = 0; j < outputSize; j++) {
			if (output[j] == ItemStack.EMPTY || output[j] == null) {
				return false;
			} else {
				if (!inventoryStacks.get(j + inputSize).isEmpty()) {
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
		
	public void consume() {
		IRecipe recipe = getRecipe(false);
		Object[] outputs = outputs();
		int[] inputOrder = inputOrder();
		if (outputs == null || inputOrder == RecipeMethods.INVALID) return;
		if (!hasConsumed) {
			for (int i = 0; i < inputSize; i++) {
				if (!inventoryStacks.get(i + inputSize + outputSize + otherSlotsSize).isEmpty()) {
					inventoryStacks.set(i + inputSize + outputSize + otherSlotsSize, ItemStack.EMPTY);
				}
			}
			for (int i = 0; i < inputSize; i++) {
				if (getRecipeHandler() != null) {
					inventoryStacks.set(i + inputSize + outputSize + otherSlotsSize, new ItemStack(inventoryStacks.get(i).getItem(), recipe.inputs().get(inputOrder[i]).getStackSize(), inventoryStacks.get(i).getMetadata()));
					inventoryStacks.get(i).shrink(recipe.inputs().get(inputOrder[i]).getStackSize());
				} else {
					inventoryStacks.set(i + inputSize + outputSize + otherSlotsSize, new ItemStack(inventoryStacks.get(i).getItem(), 1, inventoryStacks.get(i).getMetadata()));
					inventoryStacks.get(i).shrink(1);
				}
				if (inventoryStacks.get(i).getCount() <= 0) {
					inventoryStacks.set(i, ItemStack.EMPTY);
				}
			}
			hasConsumed = true;
		}
	}
	
	public void produceProducts() {
		if (hasConsumed) {
			Object[] outputs = outputs();
			for (int j = 0; j < outputSize; j++) {
				ItemStack outputStack = (ItemStack) outputs[j];
				if (inventoryStacks.get(j + inputSize).isEmpty()) {
					inventoryStacks.set(j + inputSize, outputStack);
				} else if (inventoryStacks.get(j + inputSize).isItemEqual(outputStack)) {
					inventoryStacks.get(j + inputSize).grow(outputStack.getCount());
				}
			}
			for (int i = inputSize + outputSize + otherSlotsSize; i < 2*inputSize + outputSize + otherSlotsSize; i++) {
				inventoryStacks.set(i, ItemStack.EMPTY);
			}
			hasConsumed = false;
		}
	}
		
	public IRecipe getRecipe(boolean consumed) {
		return getRecipeHandler().getRecipeFromInputs(consumed ? consumedInputs() : inputs());
	}
	
	public Object[] inputs() {
		Object[] input = new Object[inputSize];
		for (int i = 0; i < inputSize; i++) {
			input[i] = inventoryStacks.get(i);
		}
		return input;
	}
	
	public ArrayList<ItemStack> inputItemStacksExcludingSlot(int slot) {
		ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
		for (int i = 0; i < inputSize; i++) {
			if (i != slot) stacks.add(inventoryStacks.get(i));
		}
		return stacks;
	}
	
	public Object[] consumedInputs() {
		Object[] input = new Object[inputSize];
		for (int i = 0; i < inputSize; i++) {
			input[i] = inventoryStacks.get(i + inputSize + outputSize + otherSlotsSize);
		}
		return input;
	}
	
	public int[] inputOrder() {
		int[] inputOrder = new int[inputSize];
		IRecipe recipe = getRecipe(false);
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
		IRecipe recipe = getRecipe(hasConsumed);
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
		else if (slot >= inputSize && slot < inputSize + outputSize) return false;
		return NCConfig.smart_processor_input ? getRecipeHandler().isValidInput(stack, inventoryStacks.get(slot), inputItemStacksExcludingSlot(slot)) : getRecipeHandler().isValidInput(stack);
	}
	
	// SidedInventory
	
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return slots;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing direction) {
		return isItemValidForSlot(slot, stack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction) {
		return slot >= inputSize && slot < inputSize + outputSize;
	}
		
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setInteger("time", time);
		nbt.setBoolean("isGenerating", isGenerating);
		nbt.setBoolean("hasConsumed", hasConsumed);
		return nbt;
	}
		
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		time = nbt.getInteger("time");
		isGenerating = nbt.getBoolean("isGenerating");
		hasConsumed = nbt.getBoolean("hasConsumed");
	}
	
	// Inventory Fields

	@Override
	public int getFieldCount() {
		return 2;
	}

	@Override
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

	@Override
	public void setField(int id, int value) {
		switch (id) {
		case 0:
			time = value;
			break;
		case 1:
			getEnergyStorage().setEnergyStored(value);
		}
	}
}
