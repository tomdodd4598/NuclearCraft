package nc.tile.generator;

import java.util.ArrayList;

import nc.config.NCConfig;
import nc.energy.EnumStorage.EnergyConnection;
import nc.fluid.EnumTank.FluidConnection;
import nc.recipe.BaseRecipeHandler;
import nc.recipe.IIngredient;
import nc.recipe.IRecipe;
import nc.recipe.RecipeMethods;
import nc.recipe.SorptionType;
import nc.tile.IGui;
import nc.tile.dummy.IInterfaceable;
import nc.tile.energyFluid.TileEnergyFluidSidedInventory;
import nc.util.NCUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public abstract class TileItemFluidGenerator extends TileEnergyFluidSidedInventory implements IInterfaceable, IGui {

	public final int itemInputSize;
	public final int fluidInputSize;
	public final int itemOutputSize;
	public final int fluidOutputSize;
	public final int otherSlotsSize;
	
	public int time;
	public boolean isGenerating;
	public boolean hasConsumed;
	
	public int tickCount;
	
	public final BaseRecipeHandler recipes;
	
	public TileItemFluidGenerator(String name, int itemInSize, int fluidInSize, int itemOutSize, int fluidOutSize, int otherSize, int[] fluidCapacity, FluidConnection[] fluidConnection, String[][] allowedFluids, int capacity, BaseRecipeHandler recipes) {
		super(name, 2*itemInSize + itemOutSize + otherSize, capacity, EnergyConnection.OUT, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnection, allowedFluids);
		itemInputSize = itemInSize;
		fluidInputSize = fluidInSize;
		itemOutputSize = itemOutSize;
		fluidOutputSize = fluidOutSize;
		otherSlotsSize = otherSize;
		this.recipes = recipes;
		areTanksShared = fluidInSize > 1;
		
		int[] topSlots1 = new int[itemInSize];
		for (int i = 0; i < topSlots1.length; i++) {
			topSlots1[i] = i;
		}
		topSlots = topSlots1;
		
		int[] sideSlots1 = new int[itemInSize + itemOutSize];
		for (int i = 0; i < sideSlots1.length; i++) {
			sideSlots1[i] = i;
		}
		sideSlots = sideSlots1;
		
		int[] bottomSlots1 = new int[itemOutSize];
		for (int i = itemInSize; i < itemInSize + bottomSlots1.length; i++) {
			bottomSlots1[i - itemInSize] = i;
		}
		bottomSlots = bottomSlots1;
	}
	
	public static String[][] validFluids(BaseRecipeHandler recipes, String... exceptions) {
		int fluidInputSize = recipes.inputSizeFluid;
		int fluidOutputSize = recipes.outputSizeFluid;
		ArrayList<Fluid> fluidList = new ArrayList<Fluid>(FluidRegistry.getRegisteredFluids().values());
		ArrayList<FluidStack> fluidStackList = new ArrayList<FluidStack>();
		for (Fluid fluid : fluidList) {
			fluidStackList.add(new FluidStack(fluid, 1000));
		}
		ArrayList<String> exceptionsList = new ArrayList<String>();
		if (exceptions != null) for (int i = 0; i < exceptions.length; i++) {
			exceptionsList.add(exceptions[i]);
		}
		ArrayList<String> fluidNameList = new ArrayList<String>();
		for (FluidStack fluidStack : fluidStackList) {
			String fluidName = fluidStack.getFluid().getName();
			if (recipes.isValidManualInput(fluidStack) && !exceptionsList.contains(fluidName)) fluidNameList.add(fluidName);
		}
		String[] allowedFluidArray = new String[fluidNameList.size()];
		for (int i = 0; i < fluidNameList.size(); i++) {
			allowedFluidArray[i] = fluidNameList.get(i);
		}
		
		String[][] allowedFluidArrays = new String[fluidInputSize + fluidOutputSize][];
		for (int i = 0; i < fluidInputSize; i++) {
			allowedFluidArrays[i] = allowedFluidArray;
		}
		for (int i = fluidInputSize; i < fluidInputSize + fluidOutputSize; i++) {
			allowedFluidArrays[i] = new String[] {};
		}
		return allowedFluidArrays;
	}
	
	public static FluidConnection[] fluidConnections(int inSize, int outSize) {
		FluidConnection[] fluidConnections = new FluidConnection[2*inSize + outSize];
		for (int i = 0; i < inSize; i++) {
			fluidConnections[i] = FluidConnection.IN;
		}
		for (int i = inSize; i < inSize + outSize; i++) {
			fluidConnections[i] = FluidConnection.OUT;
		}
		for (int i = inSize + outSize; i < 2*inSize + outSize; i++) {
			fluidConnections[i] = FluidConnection.NON;
		}
		return fluidConnections;
	}
	
	public static int[] tankCapacities(int capacity, int inSize, int outSize) {
		int[] tankCapacities = new int[2*inSize + outSize];
		for (int i = 0; i < 2*inSize + outSize; i++) {
			tankCapacities[i] = capacity;
		}
		return tankCapacities;
	}
	
	public void update() {
		super.update();
		updateGenerator();
	}
	
	public void updateGenerator() {
		boolean flag = isGenerating;
		boolean flag1 = false;
		if(!world.isRemote) {
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
				if (NCConfig.update_block_type) setBlockState();
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
		if (!world.isRemote) isGenerating = isGenerating();
		if (!world.isRemote) hasConsumed = hasConsumed();
	}
	
	public boolean isGenerating() {
		if (world.isRemote) return isGenerating;
		return isPowered() && time > 0;
	}
	
	public boolean isPowered() {
		return world.isBlockPowered(pos);
	}
	
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
			if (output[recipes.outputSizeItem + j] == null) {
				return false;
			} else {
				if (tanks[j + fluidInputSize].getFluid() != null) {
					if (!tanks[j + fluidInputSize].getFluid().isFluidEqual((FluidStack)output[recipes.outputSizeItem + j])) {
						return false;
					} else if (tanks[j + fluidInputSize].getFluidAmount() + ((FluidStack)output[recipes.outputSizeItem + j]).amount > tanks[j + fluidInputSize].getCapacity()) {
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
		if (outputs == null || itemInputOrder == NCUtil.INVALID || fluidInputOrder == NCUtil.INVALID) return;
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
				if (recipes != null) {
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
				if (recipes != null) {
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
		
	public void output() {
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
		return recipes.getRecipeFromInputs(consumed ? consumedInputs() : inputs());
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
			if (inputOrder[i] == -1) return NCUtil.INVALID;
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
			if (inputOrder[i] == -1) return NCUtil.INVALID;
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
	
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (stack == ItemStack.EMPTY) return false;
		else if (slot >= itemInputSize && slot < itemInputSize + itemOutputSize) return false;
		return recipes.isValidManualInput(stack);
	}
	
	// SidedInventory
	
	public int[] getSlotsForFace(EnumFacing side) {
		return side == EnumFacing.DOWN ? bottomSlots : (side == EnumFacing.UP ? topSlots : sideSlots);
	}

	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing direction) {
		return isItemValidForSlot(slot, stack) && direction != EnumFacing.DOWN;
	}

	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction) {
		return direction != EnumFacing.UP && slot >= itemInputSize && slot < itemInputSize + itemOutputSize;
	}
	
	// Fluids
	
	public boolean canFill(FluidStack resource, int tankNumber) {
		if (tankNumber >= fluidInputSize) return false;
		if (!areTanksShared) return true;
		
		for (int i = 0; i < fluidInputSize; i++) {
			if (tankNumber != i && fluidConnection[i].canFill() && tanks[i].getFluid() != null) {
				if (tanks[i].getFluid().isFluidEqual(resource)) return false;
			}
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
