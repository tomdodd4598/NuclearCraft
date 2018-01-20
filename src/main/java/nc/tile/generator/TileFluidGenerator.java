package nc.tile.generator;

import java.util.ArrayList;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;
import nc.recipe.IIngredient;
import nc.recipe.IRecipe;
import nc.recipe.RecipeMethods;
import nc.recipe.SorptionType;
import nc.tile.IGui;
import nc.tile.dummy.IInterfaceable;
import nc.tile.energy.storage.EnumStorage.EnergyConnection;
import nc.tile.energyFluid.TileEnergyFluidSidedInventory;
import nc.tile.fluid.tank.EnumTank.FluidConnection;
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
	
	public final BaseRecipeHandler recipes;
	
	public TileFluidGenerator(String name, int fluidInSize, int fluidOutSize, int otherSize, int[] fluidCapacity, FluidConnection[] fluidConnection, String[][] allowedFluids, int capacity, BaseRecipeHandler recipes) {
		super(name, otherSize, capacity, EnergyConnection.OUT, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnection, allowedFluids);
		fluidInputSize = fluidInSize;
		fluidOutputSize = fluidOutSize;
		otherSlotsSize = otherSize;
		this.recipes = recipes;
		areTanksShared = fluidInSize > 1;
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
		for (int i = 0; i < fluidInputSize; i++) {
			if (tanks[i + fluidInputSize + fluidOutputSize].getFluid() != null) {
				return true;
			}
		}
		return false;
	}
		
	public boolean canProcessStacks() {
		for (int i = 0; i < fluidInputSize; i++) {
			if (tanks[i].getFluidAmount() <= 0 && !hasConsumed) {
				return false;
			}
		}
		if (time >= getProcessTime()) {
			return true;
		}
		Object[] output = hasConsumed ? outputs() : outputs();
		if (output == null || output.length != fluidOutputSize) {
			return false;
		}
		for(int j = 0; j < fluidOutputSize; j++) {
			if (output[j] == null) {
				return false;
			} else {
				if (tanks[j + fluidInputSize].getFluid() != null) {
					if (!tanks[j + fluidInputSize].getFluid().isFluidEqual((FluidStack)output[j])) {
						return false;
					} else if (tanks[j + fluidInputSize].getFluidAmount() + ((FluidStack)output[j]).amount > tanks[j + fluidInputSize].getCapacity()) {
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
			for (int i = 0; i < fluidInputSize; i++) {
				if (tanks[i + fluidInputSize + fluidOutputSize].getFluid() != null) {
					tanks[i + fluidInputSize + fluidOutputSize].setFluid(null);
				}
			}
			for (int i = 0; i < fluidInputSize; i++) {
				if (recipes != null) {
					tanks[i + fluidInputSize + fluidOutputSize].changeFluidStored(tanks[i].getFluid().getFluid(), recipe.inputs().get(inputOrder[i]).getStackSize());
					tanks[i].changeFluidStored(-recipe.inputs().get(inputOrder[i]).getStackSize());
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
			for (int j = 0; j < fluidOutputSize; j++) {
				FluidStack outputStack = (FluidStack) outputs[j];
				if (tanks[j + fluidInputSize].getFluid() == null) {
					tanks[j + fluidInputSize].setFluidStored(outputStack);
				} else if (tanks[j + fluidInputSize].getFluid().isFluidEqual(outputStack)) {
					tanks[j + fluidInputSize].changeFluidStored(outputStack.amount);
				}
			}
			for (int i = fluidInputSize + fluidOutputSize; i < 2*fluidInputSize + fluidOutputSize; i++) {
				tanks[i].setFluid(null);
			}
			hasConsumed = false;
		}
	}
		
	public IRecipe getRecipe(boolean consumed) {
		return recipes.getRecipeFromInputs(consumed ? consumedInputs() : inputs());
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
	
	public int[] inputOrder() {
		int[] inputOrder = new int[fluidInputSize];
		IRecipe recipe = getRecipe(false);
		if (recipe == null) return new int[] {};
		ArrayList<IIngredient> recipeIngredients = recipe.inputs();
		for (int i = 0; i < fluidInputSize; i++) {
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
		Object[] output = new Object[fluidOutputSize];
		IRecipe recipe = getRecipe(hasConsumed);
		if (recipe == null) return null;
		ArrayList<IIngredient> outputs = recipe.outputs();
		for (int i = 0; i < fluidOutputSize; i++) {
			Object out = RecipeMethods.getIngredientFromList(outputs, i);
			if (out == null) return null;
			else output[i] = out;
		}
		return output;
	}
	
	// Inventory
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return false;
	}
	
	// SidedInventory
	
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[] {};
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing direction) {
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction) {
		return false;
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
