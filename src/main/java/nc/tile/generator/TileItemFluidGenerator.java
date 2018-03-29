package nc.tile.generator;

import java.util.ArrayList;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;
import nc.recipe.IIngredient;
import nc.recipe.IRecipe;
import nc.recipe.NCRecipes;
import nc.recipe.RecipeMethods;
import nc.recipe.SorptionType;
import nc.tile.IGui;
import nc.tile.dummy.IInterfaceable;
import nc.tile.energy.storage.EnumEnergyStorage.EnergyConnection;
import nc.tile.energyFluid.IBufferable;
import nc.tile.energyFluid.TileEnergyFluidSidedInventory;
import nc.tile.fluid.tank.EnumTank.FluidConnection;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;

public abstract class TileItemFluidGenerator extends TileEnergyFluidSidedInventory implements IInterfaceable, IBufferable, IGui {

	public final int itemInputSize;
	public final int fluidInputSize;
	public final int itemOutputSize;
	public final int fluidOutputSize;
	public final int otherSlotsSize;
	
	public int time;
	public boolean isGenerating;
	public boolean hasConsumed;
	
	public int tickCount;
	
	public final NCRecipes.Type recipeType;
	
	public TileItemFluidGenerator(String name, int itemInSize, int fluidInSize, int itemOutSize, int fluidOutSize, int otherSize, int[] fluidCapacity, FluidConnection[] fluidConnection, String[][] allowedFluids, int capacity, NCRecipes.Type recipeType) {
		super(name, 2*itemInSize + itemOutSize + otherSize, capacity, EnergyConnection.OUT, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnection, allowedFluids);
		itemInputSize = itemInSize;
		fluidInputSize = fluidInSize;
		itemOutputSize = itemOutSize;
		fluidOutputSize = fluidOutSize;
		otherSlotsSize = otherSize;
		areTanksShared = fluidInSize > 1;
		
		this.recipeType = recipeType;
		
		int[] topSlots1 = new int[itemInSize];
		for (int i = 0; i < topSlots1.length; i++) topSlots1[i] = i;
		topSlots = topSlots1;
		
		int[] sideSlots1 = new int[itemInSize + itemOutSize];
		for (int i = 0; i < sideSlots1.length; i++) sideSlots1[i] = i;
		sideSlots = sideSlots1;
		
		int[] bottomSlots1 = new int[itemOutSize];
		for (int i = itemInSize; i < itemInSize + bottomSlots1.length; i++) bottomSlots1[i - itemInSize] = i;
		bottomSlots = bottomSlots1;
	}
	
	public static FluidConnection[] fluidConnections(int inSize, int outSize) {
		FluidConnection[] fluidConnections = new FluidConnection[2*inSize + outSize];
		for (int i = 0; i < inSize; i++) fluidConnections[i] = FluidConnection.IN;
		for (int i = inSize; i < inSize + outSize; i++) fluidConnections[i] = FluidConnection.OUT;
		for (int i = inSize + outSize; i < 2*inSize + outSize; i++) fluidConnections[i] = FluidConnection.NON;
		return fluidConnections;
	}
	
	public static int[] tankCapacities(int capacity, int inSize, int outSize) {
		int[] tankCapacities = new int[2*inSize + outSize];
		for (int i = 0; i < 2*inSize + outSize; i++) tankCapacities[i] = capacity;
		return tankCapacities;
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
	
	public void tick() {
		if (tickCount > NCConfig.generator_update_rate) tickCount = 0; else tickCount++;
	}
	
	public boolean shouldCheck() {
		return tickCount > NCConfig.generator_update_rate;
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
		storage.changeEnergyStored(getProcessPower());
		if (time >= getProcessTime()) completeProcess();
	}
	
	public void completeProcess() {
		time = 0;
		produceProducts();
	}
	
	public void updateBlockType() {
		removeTileFromENet();
		setState(isGenerating);
		world.notifyNeighborsOfStateChange(pos, blockType, true);
		addTileToENet();
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
		for (int i = 0; i < itemInputSize; i++) {
			if (!inventoryStacks.get(i + itemInputSize + itemOutputSize + otherSlotsSize).isEmpty()) {
				return true;
			}
		}
		for (int i = 0; i < fluidInputSize; i++) {
			if (tanks[i + fluidInputSize + fluidOutputSize].getFluid() != null) {
				return true;
			}
		}
		return false;
	}
		
	public boolean canProcessStacks() {
		for (int i = 0; i < itemInputSize; i++) {
			if (inventoryStacks.get(i).isEmpty() && !hasConsumed) {
				return false;
			}
		}
		for (int i = 0; i < fluidInputSize; i++) {
			if (tanks[i].getFluidAmount() <= 0 && !hasConsumed) {
				return false;
			}
		}
		if (time >= getProcessTime()) {
			return true;
		}
		Object[] output = outputs();
		if (output == null || output.length != itemOutputSize + fluidInputSize) {
			return false;
		}
		for(int j = 0; j < itemOutputSize; j++) {
			if (output[j] == ItemStack.EMPTY || output[j] == null) {
				return false;
			} else {
				if (!inventoryStacks.get(j + itemInputSize).isEmpty()) {
					if (!inventoryStacks.get(j + itemInputSize).isItemEqual((ItemStack)output[j])) {
						return false;
					} else if (inventoryStacks.get(j + itemInputSize).getCount() + ((ItemStack)output[j]).getCount() > inventoryStacks.get(j + itemInputSize).getMaxStackSize()) {
						return false;
					}
				}
			}
		}
		for(int j = 0; j < fluidOutputSize; j++) {
			if (output[getRecipeHandler().outputSizeItem + j] == null) {
				return false;
			} else {
				if (tanks[j + fluidInputSize].getFluid() != null) {
					if (!tanks[j + fluidInputSize].getFluid().isFluidEqual((FluidStack)output[getRecipeHandler().outputSizeItem + j])) {
						return false;
					} else if (tanks[j + fluidInputSize].getFluidAmount() + ((FluidStack)output[getRecipeHandler().outputSizeItem + j]).amount > tanks[j + fluidInputSize].getCapacity()) {
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
		int[] itemInputOrder = itemInputOrder();
		int[] fluidInputOrder = fluidInputOrder();
		if (outputs == null || itemInputOrder == RecipeMethods.INVALID || fluidInputOrder == RecipeMethods.INVALID) return;
		if (!hasConsumed) {
			for (int i = 0; i < itemInputSize; i++) {
				if (!inventoryStacks.get(i + itemInputSize + itemOutputSize + otherSlotsSize).isEmpty()) {
					inventoryStacks.set(i + itemInputSize + itemOutputSize + otherSlotsSize, ItemStack.EMPTY);
				}
			}
			for (int i = 0; i < fluidInputSize; i++) {
				if (tanks[i + fluidInputSize + fluidOutputSize].getFluid() != null) {
					tanks[i + fluidInputSize + fluidOutputSize].setFluid(null);
				}
			}
			for (int i = 0; i < itemInputSize; i++) {
				if (getRecipeHandler() != null) {
					inventoryStacks.set(i + itemInputSize + itemOutputSize + otherSlotsSize, new ItemStack(inventoryStacks.get(i).getItem(), recipe.inputs().get(itemInputOrder[i]).getStackSize(), inventoryStacks.get(i).getMetadata()));
					inventoryStacks.get(i).shrink(recipe.inputs().get(itemInputOrder[i]).getStackSize());
				} else {
					inventoryStacks.set(i + itemInputSize + itemOutputSize + otherSlotsSize, new ItemStack(inventoryStacks.get(i).getItem(), 1, inventoryStacks.get(i).getMetadata()));
					inventoryStacks.get(i).shrink(1);
				}
				if (inventoryStacks.get(i).getCount() <= 0) {
					inventoryStacks.set(i, ItemStack.EMPTY);
				}
			}
			for (int i = 0; i < fluidInputSize; i++) {
				if (getRecipeHandler() != null) {
					tanks[i + fluidInputSize + fluidOutputSize].changeFluidStored(tanks[i].getFluid().getFluid(), recipe.inputs().get(fluidInputOrder[i] + itemInputSize).getStackSize());
					tanks[i].changeFluidStored(-recipe.inputs().get(fluidInputOrder[i] + itemInputSize).getStackSize());
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
		
	public void produceProducts() {
		if (hasConsumed) {
			Object[] outputs = outputs();
			for (int j = 0; j < itemOutputSize; j++) {
				ItemStack outputStack = (ItemStack) outputs[j];
				if (inventoryStacks.get(j + itemInputSize).isEmpty()) {
					inventoryStacks.set(j + itemInputSize, outputStack);
				} else if (inventoryStacks.get(j + itemInputSize).isItemEqual(outputStack)) {
					inventoryStacks.get(j + itemInputSize).grow(outputStack.getCount());
				}
			}
			for (int j = 0; j < fluidOutputSize; j++) {
				FluidStack outputStack = (FluidStack) outputs[j + itemOutputSize];
				if (tanks[j + fluidInputSize].getFluid() == null) {
					tanks[j + fluidInputSize].setFluidStored(outputStack);
				} else if (tanks[j + fluidInputSize].getFluid().isFluidEqual(outputStack)) {
					tanks[j + fluidInputSize].changeFluidStored(outputStack.amount);
				}
			}
			for (int i = itemInputSize + itemOutputSize + otherSlotsSize; i < 2*itemInputSize + itemOutputSize + otherSlotsSize; i++) {
				inventoryStacks.set(i, ItemStack.EMPTY);
			}
			for (int i = fluidInputSize + fluidOutputSize; i < 2*fluidInputSize + fluidOutputSize; i++) {
				tanks[i].setFluid(null);
			}
			hasConsumed = false;
		}
	}
		
	public IRecipe getRecipe(boolean consumed) {
		return getRecipeHandler().getRecipeFromInputs(consumed ? consumedInputs() : inputs());
	}
	
	public Object[] inputs() {
		Object[] input = new Object[itemInputSize + fluidInputSize];
		for (int i = 0; i < itemInputSize; i++) {
			input[i] = inventoryStacks.get(i);
		}
		for (int i = itemInputSize; i < fluidInputSize + itemInputSize; i++) {
			input[i] = tanks[i - itemInputSize].getFluid();
		}
		return input;
	}
	
	public Object[] consumedInputs() {
		Object[] input = new Object[itemInputSize + fluidInputSize];
		for (int i = 0; i < itemInputSize; i++) {
			input[i] = inventoryStacks.get(i + itemInputSize + itemOutputSize + otherSlotsSize);
		}
		for (int i = itemInputSize; i < fluidInputSize; i++) {
			input[i] = tanks[i + fluidInputSize + fluidOutputSize].getFluid();
		}
		return input;
	}
	
	public int[] itemInputOrder() {
		int[] inputOrder = new int[itemInputSize];
		IRecipe recipe = getRecipe(false);
		if (recipe == null) return new int[] {};
		ArrayList<IIngredient> recipeIngredients = recipe.inputs();
		for (int i = 0; i < itemInputSize; i++) {
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
	
	public int[] fluidInputOrder() {
		int[] inputOrder = new int[fluidInputSize];
		IRecipe recipe = getRecipe(false);
		if (recipe == null) return new int[] {};
		ArrayList<IIngredient> recipeIngredients = recipe.inputs();
		for (int i = 0; i < fluidInputSize; i++) {
			inputOrder[i] = -1;
			for (int j = 0; j < recipeIngredients.size(); j++) {
				if (recipeIngredients.get(j).matches(inputs()[i + itemInputSize], SorptionType.INPUT)) {
					inputOrder[i] = j - itemInputSize;
					break;
				}
			}
			if (inputOrder[i] == -1) return RecipeMethods.INVALID;
		}
		return inputOrder;
	}
	
	public Object[] outputs() {
		Object[] output = new Object[itemOutputSize + fluidOutputSize];
		IRecipe recipe = getRecipe(hasConsumed);
		if (recipe == null) return null;
		ArrayList<IIngredient> outputs = recipe.outputs();
		for (int i = 0; i < itemOutputSize + fluidOutputSize; i++) {
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
		else if (slot >= itemInputSize && slot < itemInputSize + itemOutputSize) return false;
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
		return direction != EnumFacing.UP && slot >= itemInputSize && slot < itemInputSize + itemOutputSize;
	}
	
	// Fluids
	
	@Override
	public boolean canFill(FluidStack resource, int tankNumber) {
		if (tankNumber >= fluidInputSize) return false;
		if (!areTanksShared) return true;
		
		for (int i = 0; i < fluidInputSize; i++) {
			if (tankNumber != i && fluidConnections[i].canFill() && tanks[i].getFluid() != null) {
				if (tanks[i].getFluid().isFluidEqual(resource)) return false;
			}
		}
		return true;
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
			storage.setEnergyStored(value);
		}
	}
}
