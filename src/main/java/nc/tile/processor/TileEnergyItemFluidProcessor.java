package nc.tile.processor;

import java.util.ArrayList;

import nc.config.NCConfig;
import nc.energy.EnumStorage.EnergyConnection;
import nc.fluid.EnumTank.FluidConnection;
import nc.init.NCItems;
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
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public abstract class TileEnergyItemFluidProcessor extends TileEnergyFluidSidedInventory implements IInterfaceable, IGui {
	
	public final int defaultProcessTime;
	public int baseProcessTime;
	public final int baseProcessPower;
	public final int itemInputSize;
	public final int fluidInputSize;
	public final int itemOutputSize;
	public final int fluidOutputSize;
	
	public int time;
	public boolean isProcessing;
	
	public final boolean hasUpgrades;
	public final int upgradeMeta;
	
	public int tickCount;
	
	public final BaseRecipeHandler recipes;
	
	public TileEnergyItemFluidProcessor(String name, int itemInSize, int fluidInSize, int itemOutSize, int fluidOutSize, int[] fluidCapacity, FluidConnection[] fluidConnection, String[][] allowedFluids, int time, int power, BaseRecipeHandler recipes) {
		this(name, itemInSize, fluidInSize, itemOutSize, fluidOutSize, fluidCapacity, fluidConnection, allowedFluids, time, power, false, recipes, 1);
	}
	
	public TileEnergyItemFluidProcessor(String name, int itemInSize, int fluidInSize, int itemOutSize, int fluidOutSize, int[] fluidCapacity, FluidConnection[] fluidConnection, String[][] allowedFluids, int time, int power, BaseRecipeHandler recipes, int upgradeMeta) {
		this(name, itemInSize, fluidInSize, itemOutSize, fluidOutSize, fluidCapacity, fluidConnection, allowedFluids, time, power, true, recipes, upgradeMeta);
	}
	
	public TileEnergyItemFluidProcessor(String name, int itemInSize, int fluidInSize, int itemOutSize, int fluidOutSize, int[] fluidCapacity, FluidConnection[] fluidConnection, String[][] allowedFluids, int time, int power, boolean upgrades, BaseRecipeHandler recipes, int upgradeMeta) {
		super(name, itemInSize + itemOutSize + (upgrades ? 2 : 0), 32000, power != 0 ? EnergyConnection.IN : EnergyConnection.NON, fluidCapacity, fluidCapacity, fluidCapacity, fluidConnection, allowedFluids);
		itemInputSize = itemInSize;
		fluidInputSize = fluidInSize;
		itemOutputSize = itemOutSize;
		fluidOutputSize = fluidOutSize;
		defaultProcessTime = time;
		baseProcessTime = time;
		baseProcessPower = power;
		hasUpgrades = upgrades;
		this.recipes = recipes;
		this.upgradeMeta = upgradeMeta;
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
		FluidConnection[] fluidConnections = new FluidConnection[inSize + outSize];
		for (int i = 0; i < inSize; i++) {
			fluidConnections[i] = FluidConnection.IN;
		}
		for (int i = inSize; i < inSize + outSize; i++) {
			fluidConnections[i] = FluidConnection.OUT;
		}
		return fluidConnections;
	}
	
	public static int[] tankCapacities(int capacity, int inSize, int outSize) {
		int[] tankCapacities = new int[inSize + outSize];
		for (int i = 0; i < inSize + outSize; i++) {
			tankCapacities[i] = capacity;
		}
		return tankCapacities;
	}
	
	public void update() {
		super.update();
		updateProcessor();
	}
	
	public void updateProcessor() {
		boolean flag = isProcessing;
		boolean flag1 = false;
		if(!worldObj.isRemote) {
			tick();
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
				if (NCConfig.update_block_type) setBlockState();
			}
		} else {
			isProcessing = canProcess() && !isPowered();
		}
		
		if (flag1) {
			markDirty();
		}
	}
	
	public abstract void setBlockState();
	
	public void tick() {
		if (tickCount > NCConfig.processor_update_rate) {
			tickCount = 0;
		} else {
			tickCount++;
		}
	}
	
	public boolean shouldCheck() {
		return tickCount > NCConfig.processor_update_rate;
	}
	
	public void onAdded() {
		super.onAdded();
		baseProcessTime = defaultProcessTime;
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
		ItemStack speedStack = inventoryStacks[itemInputSize + itemOutputSize];
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
		for (int i = 0; i < itemInputSize; i++) {
			if (inventoryStacks[i] == null) {
				return false;
			}
		}
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
		Object[] outputs = outputs();
		if (outputs == null || outputs.length != itemOutputSize + fluidOutputSize) {
			return false;
		}
		for(int j = 0; j < itemOutputSize; j++) {
			if (outputs[j] == null) {
				return false;
			} else {
				if (inventoryStacks[j + itemInputSize] != null) {
					if (!inventoryStacks[j + itemInputSize].isItemEqual((ItemStack) outputs[j])) {
						return false;
					} else if (inventoryStacks[j + itemInputSize].stackSize + ((ItemStack) outputs[j]).stackSize > inventoryStacks[j + itemInputSize].getMaxStackSize()) {
						return false;
					}
				}
			}
		}
		for(int j = 0; j < fluidOutputSize; j++) {
			if (outputs[recipes.outputSizeItem + j] == null) {
				return false;
			} else {
				if (tanks[j + fluidInputSize].getFluid() != null) {
					if (!tanks[j + fluidInputSize].getFluid().isFluidEqual((FluidStack) outputs[recipes.outputSizeItem + j])) {
						return false;
					} else if (tanks[j + fluidInputSize].getFluidAmount() + ((FluidStack) outputs[recipes.outputSizeItem + j]).amount > tanks[j + fluidInputSize].getCapacity()) {
						return false;
					}
				}
			}
		}
		Object[] inputs = inputs();
		if (recipes.getRecipeFromInputs(inputs).extras().get(0) instanceof Integer) baseProcessTime = (int) recipes.getRecipeFromInputs(inputs).extras().get(0);
		return true;
	}
	
	public void process() {
		IRecipe recipe = getRecipe();
		Object[] outputs = outputs();
		int[] itemInputOrder = itemInputOrder();
		int[] fluidInputOrder = fluidInputOrder();
		if (outputs == null || itemInputOrder == NCUtil.INVALID || fluidInputOrder == NCUtil.INVALID) return;
		for (int j = 0; j < itemOutputSize; j++) {
			ItemStack outputStack = (ItemStack) outputs[j];
			if (inventoryStacks[j + itemInputSize] == null) {
				inventoryStacks[j + itemInputSize] = outputStack;
			} else if (inventoryStacks[j + itemInputSize].isItemEqual(outputStack)) {
				inventoryStacks[j + itemInputSize].stackSize += outputStack.stackSize;
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
		for (int i = 0; i < itemInputSize; i++) {
			if (recipes != null) {
				inventoryStacks[i].stackSize -= recipe.inputs().get(itemInputOrder[i]).getStackSize();
			} else {
				inventoryStacks[i].stackSize --;
			}
			if (inventoryStacks[i].stackSize <= 0) {
				inventoryStacks[i] = null;
			}
		}
		for (int i = 0; i < fluidInputSize; i++) {
			if (recipes != null) {
				tanks[i].changeFluidStored(-recipe.inputs().get(fluidInputOrder[i] + itemInputSize).getStackSize());
			} else {
				tanks[i].changeFluidStored(-1000);
			}
			if (tanks[i].getFluidAmount() <= 0) {
				tanks[i].setFluidStored(null);
			}
		}
	}
	
	public IRecipe getRecipe() {
		return recipes.getRecipeFromInputs(inputs());
	}
	
	public Object[] inputs() {
		Object[] input = new Object[itemInputSize + fluidInputSize];
		for (int i = 0; i < itemInputSize; i++) {
			input[i] = inventoryStacks[i];
		}
		for (int i = itemInputSize; i < fluidInputSize + itemInputSize; i++) {
			input[i] = tanks[i - itemInputSize].getFluid();
		}
		return input;
	}
	
	public int[] itemInputOrder() {
		int[] inputOrder = new int[itemInputSize];
		IRecipe recipe = getRecipe();
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
		IRecipe recipe = getRecipe();
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
		IRecipe recipe = getRecipe();
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
		if (stack == null) return false;
		if (hasUpgrades) {
			if (stack.getItem() == NCItems.upgrade) {
				if (slot == itemInputSize + itemOutputSize) return stack.getMetadata() == 0;
				else if (slot == itemInputSize + itemOutputSize + 1) return stack.getMetadata() == upgradeMeta;
			}
		}
		if (slot >= itemInputSize) return false;
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
		return 3;
	}

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
