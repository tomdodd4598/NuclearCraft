package nc.tile.processor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import nc.ModCheck;
import nc.config.NCConfig;
import nc.init.NCItems;
import nc.recipe.AbstractRecipeHandler;
import nc.recipe.IngredientSorption;
import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.recipe.ingredient.IItemIngredient;
import nc.tile.IGui;
import nc.tile.dummy.IInterfaceable;
import nc.tile.energy.ITileEnergy;
import nc.tile.energy.TileEnergySidedInventory;
import nc.tile.energyFluid.IBufferable;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.Tank;
import nc.util.ArrayHelper;
import nc.util.NCMath;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

public class TileItemProcessor extends TileEnergySidedInventory implements IItemProcessor, IInterfaceable, IBufferable, IGui, IUpgradable {
	
	public final int[] slots;
	
	public final int defaultProcessTime, defaultProcessPower;
	public double baseProcessTime, baseProcessPower, baseProcessRadiation;
	public final int itemInputSize, itemOutputSize;
	
	public double time;
	public boolean isProcessing, canProcessInputs;
	
	public final boolean shouldLoseProgress, hasUpgrades;
	public final int upgradeMeta;
	
	public final NCRecipes.Type recipeType;
	protected ProcessorRecipe recipe;
	
	public TileItemProcessor(String name, int itemInSize, int itemOutSize, int time, int power, boolean shouldLoseProgress, @Nonnull NCRecipes.Type recipeType) {
		this(name, itemInSize, itemOutSize, time, power, shouldLoseProgress, false, recipeType, 1);
	}
	
	public TileItemProcessor(String name, int itemInSize, int itemOutSize, int time, int power, boolean shouldLoseProgress, @Nonnull NCRecipes.Type recipeType, int upgradeMeta) {
		this(name, itemInSize, itemOutSize, time, power, shouldLoseProgress, true, recipeType, upgradeMeta);
	}
	
	public TileItemProcessor(String name, int itemInSize, int itemOutSize, int time, int power, boolean shouldLoseProgress, boolean upgrades, @Nonnull NCRecipes.Type recipeType, int upgradeMeta) {
		super(name, itemInSize + itemOutSize + (upgrades ? 2 : 0), 32000, power != 0 ? ITileEnergy.energyConnectionAll(EnergyConnection.IN) : ITileEnergy.energyConnectionAll(EnergyConnection.NON));
		itemInputSize = itemInSize;
		itemOutputSize = itemOutSize;
		
		defaultProcessTime = time;
		defaultProcessPower = power;
		baseProcessTime = time;
		baseProcessPower = power;
		
		this.shouldLoseProgress = shouldLoseProgress;
		hasUpgrades = upgrades;
		this.upgradeMeta = upgradeMeta;
		
		this.recipeType = recipeType;
		
		slots = ArrayHelper.increasingArray(itemInSize + itemOutSize + (hasUpgrades ? 2 : 0));
	}
	
	@Override
	public int getGuiID() {
		return upgradeMeta;
	}
	
	@Override
	public void onAdded() {
		super.onAdded();
		if (!world.isRemote) isProcessing = isProcessing();
	}
	
	@Override
	public void update() {
		super.update();
		updateProcessor();
	}
	
	public void updateProcessor() {
		recipe = getRecipeHandler().getRecipeFromInputs(getItemInputs(), new ArrayList<Tank>());
		canProcessInputs = canProcessInputs();
		boolean wasProcessing = isProcessing;
		isProcessing = isProcessing();
		setCapacityFromSpeed();
		boolean shouldUpdate = false;
		if (!world.isRemote) {
			tickTile();
			if (isProcessing) process();
			else {
				getRadiationSource().setRadiationLevel(0D);
				if (!isRedstonePowered()) loseProgress();
			}
			if (wasProcessing != isProcessing) {
				shouldUpdate = true;
				updateBlockType();
			}
		}
		if (shouldUpdate) markDirty();
	}
	
	public boolean isProcessing() {
		return readyToProcess() && !isRedstonePowered();
	}
	
	public boolean readyToProcess() {
		return canProcessInputs;
	}
	
	public void process() {
		time += getSpeedMultiplier();
		getEnergyStorage().changeEnergyStored(-getProcessPower());
		getRadiationSource().setRadiationLevel(baseProcessRadiation*getSpeedMultiplier());
		if (time >= baseProcessTime) {
			double oldProcessTime = baseProcessTime;
			produceProducts();
			recipe = getRecipeHandler().getRecipeFromInputs(getItemInputs(), new ArrayList<Tank>());
			setRecipeStats();
			if (recipe == null) time = 0;
			else time = MathHelper.clamp(time - oldProcessTime, 0D, baseProcessTime);
		}
	}
	
	public void loseProgress() {
		time = MathHelper.clamp(time - 1.5D*getSpeedMultiplier(), 0D, baseProcessTime);
	}
	
	public void updateBlockType() {
		if (ModCheck.ic2Loaded()) removeTileFromENet();
		setState(isProcessing);
		world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
		if (ModCheck.ic2Loaded()) addTileToENet();
	}
	
	// IC2 Tiers
	
	@Override
	public int getEUSourceTier() {
		return 1;
	}
		
	@Override
	public int getEUSinkTier() {
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
		return getSpeedCount() > 1 ? NCConfig.speed_upgrade_multipliers[0]*(NCMath.simplexNumber(getSpeedCount(), NCConfig.speed_upgrade_power_laws[0]) - 1) + 1 : 1;
	}
	
	public double getPowerMultiplier() {
		return getSpeedCount() > 1 ? NCConfig.speed_upgrade_multipliers[1]*(NCMath.simplexNumber(getSpeedCount(), NCConfig.speed_upgrade_power_laws[1]) - 1) + 1 : 1;
	}
	
	public double getProcessTime() {
		return Math.max(1, baseProcessTime/getSpeedMultiplier());
	}
	
	public int getProcessPower() {
		return Math.min(Integer.MAX_VALUE, (int) ((double)baseProcessPower*getPowerMultiplier()));
	}
	
	public double getProcessEnergy() {
		return getProcessTime()*getProcessPower();
	}
	
	public void setCapacityFromSpeed() {
		getEnergyStorage().setStorageCapacity(MathHelper.clamp(NCConfig.machine_update_rate*getProcessPower(), 32000, Integer.MAX_VALUE));
		getEnergyStorage().setMaxTransfer(MathHelper.clamp(NCConfig.machine_update_rate*getProcessPower(), 32000, Integer.MAX_VALUE));
	}
	
	// Needed for Galacticraft
	private int getMaxEnergyModified() {
		return ModCheck.galacticraftLoaded() ? getMaxEnergyStored() - 20 : getMaxEnergyStored();
	}
	
	public boolean canProcessInputs() {
		if (recipe == null) return false;
		setRecipeStats();
		if (time >= baseProcessTime) return true;
		
		else if ((time <= 0 && (getProcessEnergy() <= getMaxEnergyModified() || getEnergyStored() < getMaxEnergyModified()) && (getProcessEnergy() > getMaxEnergyModified() || getProcessEnergy() > getEnergyStored())) || getEnergyStored() < getProcessPower()) return false;
		
		for (int j = 0; j < itemOutputSize; j++) {
			IItemIngredient itemProduct = getItemProducts().get(j);
			if (itemProduct.getMaxStackSize() <= 0) continue;
			if (itemProduct.getStack() == null || itemProduct.getStack() == ItemStack.EMPTY) return false;
			else if (!inventoryStacks.get(j + itemInputSize).isEmpty()) {
				if (!inventoryStacks.get(j + itemInputSize).isItemEqual(itemProduct.getStack())) {
					return false;
				} else if (inventoryStacks.get(j + itemInputSize).getCount() + itemProduct.getMaxStackSize() > inventoryStacks.get(j + itemInputSize).getMaxStackSize()) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void setRecipeStats() {
		if (recipe == null) {
			setDefaultRecipeStats();
			return;
		}
		
		baseProcessTime = recipe.getProcessTime(defaultProcessTime);
		baseProcessPower = recipe.getProcessPower(defaultProcessPower);
		baseProcessRadiation = recipe.getProcessRadiation();
	}
	
	public void setDefaultRecipeStats() {
		baseProcessTime = defaultProcessTime;
		baseProcessPower = defaultProcessPower;
		baseProcessRadiation = 0D;
	}
	
	public void produceProducts() {
		if (recipe == null) return;
		List<Integer> itemInputOrder = getItemInputOrder();
		if (itemInputOrder == AbstractRecipeHandler.INVALID) return;
		
		for (int i = 0; i < itemInputSize; i++) {
			int itemIngredientStackSize = getItemIngredients().get(itemInputOrder.get(i)).getMaxStackSize();
			if (itemIngredientStackSize > 0) inventoryStacks.get(i).shrink(itemIngredientStackSize);
			if (inventoryStacks.get(i).getCount() <= 0) inventoryStacks.set(i, ItemStack.EMPTY);
		}
		for (int j = 0; j < itemOutputSize; j++) {
			IItemIngredient itemProduct = getItemProducts().get(j);
			if (itemProduct.getMaxStackSize() <= 0) continue;
			if (inventoryStacks.get(j + itemInputSize).isEmpty()) {
				inventoryStacks.set(j + itemInputSize, itemProduct.getNextStack());
			} else if (inventoryStacks.get(j + itemInputSize).isItemEqual(itemProduct.getStack())) {
				inventoryStacks.get(j + itemInputSize).grow(itemProduct.getNextStackSize());
			}
		}
	}
	
	// IItemProcessor
	
	@Override
	public ProcessorRecipeHandler getRecipeHandler() {
		return recipeType.getRecipeHandler();
	}
	
	@Override
	public ProcessorRecipe getRecipe() {
		return recipe;
	}
	
	@Override
	public List<ItemStack> getItemInputs() {
		return inventoryStacks.subList(0, itemInputSize);
	}
	
	@Override
	public List<IItemIngredient> getItemIngredients() {
		return recipe.itemIngredients();
	}
	
	@Override
	public List<IItemIngredient> getItemProducts() {
		return recipe.itemProducts();
	}
	
	@Override
	public List<Integer> getItemInputOrder() {
		List<Integer> itemInputOrder = new ArrayList<Integer>();
		List<IItemIngredient> itemIngredients = recipe.itemIngredients();
		for (int i = 0; i < itemInputSize; i++) {
			int position = -1;
			for (int j = 0; j < itemIngredients.size(); j++) {
				if (itemIngredients.get(j).matches(getItemInputs().get(i), IngredientSorption.INPUT)) {
					position = j;
					break;
				}
			}
			if (position == -1) return AbstractRecipeHandler.INVALID;
			itemInputOrder.add(position);
		}
		return itemInputOrder;
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
		return NCConfig.smart_processor_input ? getRecipeHandler().isValidItemInput(stack, inventoryStacks.get(slot), inputItemStacksExcludingSlot(slot)) : getRecipeHandler().isValidItemInput(stack);
	}
	
	public List<ItemStack> inputItemStacksExcludingSlot(int slot) {
		List<ItemStack> inputItemsExcludingSlot = new ArrayList<ItemStack>(getItemInputs());
		inputItemsExcludingSlot.remove(slot);
		return inputItemsExcludingSlot;
	}
	
	@Override
	public boolean hasUpgrades() {
		return hasUpgrades;
	}
	
	@Override
	public int getSpeedUpgradeSlot() {
		return itemInputSize + itemOutputSize;
	}
	
	@Override
	public int getUpgradeMeta() {
		return upgradeMeta;
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
		nbt.setDouble("time", time);
		nbt.setBoolean("isProcessing", isProcessing);
		nbt.setBoolean("canProcessInputs", canProcessInputs);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		time = nbt.getDouble("time");
		isProcessing = nbt.getBoolean("isProcessing");
		canProcessInputs = nbt.getBoolean("canProcessInputs");
	}
	
	// Inventory Fields

	@Override
	public int getFieldCount() {
		return 4;
	}

	@Override
	public int getField(int id) {
		switch (id) {
		case 0:
			return (int) time;
		case 1:
			return getEnergyStored();
		case 2:
			return (int) baseProcessTime;
		case 3:
			return (int) baseProcessPower;
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
			break;
		case 3:
			baseProcessPower = value;
		}
	}
}
