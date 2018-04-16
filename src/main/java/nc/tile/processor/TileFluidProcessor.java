package nc.tile.processor;

import java.util.ArrayList;

import nc.ModCheck;
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
import nc.tile.energyFluid.IBufferable;
import nc.tile.energyFluid.TileEnergyFluidSidedInventory;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.FluidConnection;
import nc.util.NCMathHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidStack;

public abstract class TileFluidProcessor extends TileEnergyFluidSidedInventory implements IInterfaceable, IBufferable, IGui {
	
	public final int defaultProcessTime;
	public int baseProcessTime;
	public final int baseProcessPower;
	public final int fluidInputSize, fluidOutputSize;
	
	public int time;
	public boolean isProcessing;
	
	public final boolean hasUpgrades;
	public final int upgradeMeta;
	
	public int tickCount;
	
	public final NCRecipes.Type recipeType;
	
	public TileFluidProcessor(String name, int fluidInSize, int fluidOutSize, int[] fluidCapacity, FluidConnection[] fluidConnection, String[][] allowedFluids, int time, int power, NCRecipes.Type recipeType) {
		this(name, fluidInSize, fluidOutSize, fluidCapacity, fluidConnection, allowedFluids, time, power, false, recipeType, 1);
	}
	
	public TileFluidProcessor(String name, int fluidInSize, int fluidOutSize, int[] fluidCapacity, FluidConnection[] fluidConnection, String[][] allowedFluids, int time, int power, NCRecipes.Type recipeType, int upgradeMeta) {
		this(name, fluidInSize, fluidOutSize, fluidCapacity, fluidConnection, allowedFluids, time, power, true, recipeType, upgradeMeta);
	}
	
	public TileFluidProcessor(String name, int fluidInSize, int fluidOutSize, int[] fluidCapacity, FluidConnection[] fluidConnection, String[][] allowedFluids, int time, int power, boolean upgrades, NCRecipes.Type recipeType, int upgradeMeta) {
		super(name, upgrades ? 2 : 0, 32000, power != 0 ? energyConnectionAll(EnergyConnection.IN) : energyConnectionAll(EnergyConnection.NON), fluidCapacity, fluidCapacity, fluidCapacity, fluidConnection, allowedFluids);
		fluidInputSize = fluidInSize;
		fluidOutputSize = fluidOutSize;
		defaultProcessTime = time;
		baseProcessTime = time;
		baseProcessPower = power;
		hasUpgrades = upgrades;
		this.upgradeMeta = upgradeMeta;
		areTanksShared = fluidInSize > 1;
		
		this.recipeType = recipeType;
		
		for (int i = 0; i < tanks.length; i++) {
			if (i < fluidInputSize) tanks[i].setStrictlyInput(true);
			else tanks[i].setStrictlyOutput(true);
		}
	}
	
	public static FluidConnection[] fluidConnections(int inSize, int outSize) {
		FluidConnection[] fluidConnections = new FluidConnection[inSize + outSize];
		for (int i = 0; i < inSize; i++) fluidConnections[i] = FluidConnection.IN;
		for (int i = inSize; i < inSize + outSize; i++) fluidConnections[i] = FluidConnection.OUT;
		return fluidConnections;
	}
	
	public static int[] tankCapacities(int capacity, int inSize, int outSize) {
		int[] tankCapacities = new int[inSize + outSize];
		for (int i = 0; i < inSize + outSize; i++) tankCapacities[i] = capacity;
		return tankCapacities;
	}
	
	public BaseRecipeHandler getRecipeHandler() {
		return recipeType.getRecipeHandler();
	}
	
	@Override
	public int getGuiID() {
		return upgradeMeta;
	}
	
	@Override
	public void onAdded() {
		super.onAdded();
		baseProcessTime = defaultProcessTime;
		if (!world.isRemote) isProcessing = canProcess() && !isPowered();
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
		getEnergyStorage().changeEnergyStored(-getProcessPower());
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
		if (ModCheck.ic2Loaded()) removeTileFromENet();
		setState(isProcessing);
		world.notifyNeighborsOfStateChange(pos, blockType, true);
		if (ModCheck.ic2Loaded()) addTileToENet();
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
		ItemStack speedStack = inventoryStacks.get(0);
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
		getEnergyStorage().setStorageCapacity(MathHelper.clamp(2*getProcessPower(), 32000, Integer.MAX_VALUE));
		getEnergyStorage().setMaxTransfer(MathHelper.clamp(2*getProcessPower(), 32000, Integer.MAX_VALUE));
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
		Object[] outputs = outputs();
		if (outputs == null || outputs.length != fluidOutputSize) {
			return false;
		}
		for(int j = 0; j < fluidOutputSize; j++) {
			if (outputs[j] == null) {
				return false;
			} else {
				if (tanks[j + fluidInputSize].getFluid() != null) {
					if (!tanks[j + fluidInputSize].getFluid().isFluidEqual((FluidStack) outputs[j])) {
						return false;
					} else if (tanks[j + fluidInputSize].getFluidAmount() + ((FluidStack) outputs[j]).amount > tanks[j + fluidInputSize].getCapacity()) {
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
		for (int j = 0; j < fluidOutputSize; j++) {
			FluidStack outputStack = (FluidStack) outputs[j];
			if (tanks[j + fluidInputSize].getFluid() == null) {
				tanks[j + fluidInputSize].setFluidStored(outputStack);
			} else if (tanks[j + fluidInputSize].getFluid().isFluidEqual(outputStack)) {
				tanks[j + fluidInputSize].changeFluidStored(outputStack.amount);
			}
		}
		for (int i = 0; i < fluidInputSize; i++) {
			if (getRecipeHandler() != null) {
				tanks[i].changeFluidStored(-recipe.inputs().get(inputOrder[i]).getStackSize());
			} else {
				tanks[i].changeFluidStored(-1000);
			}
			if (tanks[i].getFluidAmount() <= 0) {
				tanks[i].setFluidStored(null);
			}
		}
	}
	
	public IRecipe getRecipe() {
		return getRecipeHandler().getRecipeFromInputs(inputs());
	}
	
	public Object[] inputs() {
		Object[] input = new Object[fluidInputSize];
		for (int i = 0; i < fluidInputSize; i++) {
			input[i] = tanks[i].getFluid();
		}
		return input;
	}
	
	public int[] inputOrder() {
		int[] inputOrder = new int[fluidInputSize];
		IRecipe recipe = getRecipe();
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
		IRecipe recipe = getRecipe();
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
		if (stack == ItemStack.EMPTY) return false;
		if (hasUpgrades) {
			if (stack.getItem() == NCItems.upgrade) {
				if (slot == 0) return stack.getMetadata() == 0;
				else if (slot == 1) return stack.getMetadata() == upgradeMeta;
			}
		}
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
			getEnergyStorage().setEnergyStored(value);
			break;
		case 2:
			baseProcessTime = value;
		}
	}
}
