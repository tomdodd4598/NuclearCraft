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
import nc.tile.energy.TileEnergySidedInventory;
import nc.tile.energyFluid.IBufferable;
import nc.tile.internal.energy.EnergyConnection;
import nc.util.ArrayHelper;
import nc.util.NCMathHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

public abstract class TileItemProcessor extends TileEnergySidedInventory implements IInterfaceable, IBufferable, IGui {
	
	public final int[] slots;
	
	public final int defaultProcessTime;
	public int baseProcessTime;
	public final int baseProcessPower;
	public final int itemInputSize, itemOutputSize;
	
	public int time;
	public boolean isProcessing, canProcessStacks;
	
	public final boolean hasUpgrades;
	public final int upgradeMeta;
	
	public final NCRecipes.Type recipeType;
	
	public TileItemProcessor(String name, int itemInSize, int itemOutSize, int time, int power, NCRecipes.Type recipeType) {
		this(name, itemInSize, itemOutSize, time, power, false, recipeType, 1);
	}
	
	public TileItemProcessor(String name, int itemInSize, int itemOutSize, int time, int power, NCRecipes.Type recipeType, int upgradeMeta) {
		this(name, itemInSize, itemOutSize, time, power, true, recipeType, upgradeMeta);
	}
	
	public TileItemProcessor(String name, int itemInSize, int itemOutSize, int time, int power, boolean upgrades, NCRecipes.Type recipeType, int upgradeMeta) {
		super(name, itemInSize + itemOutSize + (upgrades ? 2 : 0), 32000, power != 0 ? energyConnectionAll(EnergyConnection.IN) : energyConnectionAll(EnergyConnection.NON));
		itemInputSize = itemInSize;
		itemOutputSize = itemOutSize;
		defaultProcessTime = time;
		baseProcessTime = time;
		baseProcessPower = power;
		hasUpgrades = upgrades;
		this.upgradeMeta = upgradeMeta;
		
		this.recipeType = recipeType;
		
		slots = ArrayHelper.increasingArray(itemInSize + itemOutSize);
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
		if (!world.isRemote) isProcessing = !isPowered() && canProcess();
	}
	
	@Override
	public void update() {
		super.update();
		updateProcessor();
	}
	
	public void updateProcessor() {
		canProcessStacks = canProcessStacks();
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
	
	public boolean canProcess() {
		return canProcessStacks;
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
		ItemStack speedStack = inventoryStacks.get(itemInputSize + itemOutputSize);
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
		for (int i = 0; i < itemInputSize; i++) {
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
		if (outputs == null || outputs.length != itemOutputSize) {
			return false;
		}
		for(int j = 0; j < itemOutputSize; j++) {
			if (outputs[j] == ItemStack.EMPTY || outputs[j] == null) {
				return false;
			} else {
				if (!inventoryStacks.get(j + itemInputSize).isEmpty()) {
					if (!inventoryStacks.get(j + itemInputSize).isItemEqual((ItemStack) outputs[j])) {
						return false;
					} else if (inventoryStacks.get(j + itemInputSize).getCount() + ((ItemStack) outputs[j]).getCount() > inventoryStacks.get(j + itemInputSize).getMaxStackSize()) {
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
		for (int j = 0; j < itemOutputSize; j++) {
			ItemStack outputStack = (ItemStack) outputs[j];
			if (inventoryStacks.get(j + itemInputSize).isEmpty()) {
				inventoryStacks.set(j + itemInputSize, outputStack);
			} else if (inventoryStacks.get(j + itemInputSize).isItemEqual(outputStack)) {
				inventoryStacks.get(j + itemInputSize).grow(outputStack.getCount());
			}
		}
		for (int i = 0; i < itemInputSize; i++) {
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
		Object[] input = new Object[itemInputSize];
		for (int i = 0; i < itemInputSize; i++) {
			input[i] = inventoryStacks.get(i);
		}
		return input;
	}
	
	public ArrayList<ItemStack> inputItemStacksExcludingSlot(int slot) {
		ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
		for (int i = 0; i < itemInputSize; i++) {
			if (i != slot) stacks.add(inventoryStacks.get(i));
		}
		return stacks;
	}
	
	public int[] inputOrder() {
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
			if (inputOrder[i] == -1) return RecipeMethods.INVALID;
		}
		return inputOrder;
	}
	
	public Object[] outputs() {
		Object[] output = new Object[itemOutputSize];
		IRecipe recipe = getRecipe();
		if (recipe == null) return null;
		ArrayList<IIngredient> outputs = recipe.outputs();
		for (int i = 0; i < itemOutputSize; i++) {
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
				if (slot == itemInputSize + itemOutputSize) return stack.getMetadata() == 0;
				else if (slot == itemInputSize + itemOutputSize + 1) return stack.getMetadata() == upgradeMeta;
			}
		}
		if (slot >= itemInputSize) return false;
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
		return slot >= itemInputSize && slot < itemInputSize + itemOutputSize;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setInteger("time", time);
		nbt.setBoolean("isProcessing", isProcessing);
		nbt.setBoolean("canProcessStacks", canProcessStacks);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		time = nbt.getInteger("time");
		isProcessing = nbt.getBoolean("isProcessing");
		canProcessStacks = nbt.getBoolean("canProcessStacks");
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
