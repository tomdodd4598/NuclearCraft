package nc.tile.processor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import nc.ModCheck;
import nc.config.NCConfig;
import nc.init.NCItems;
import nc.network.tile.ProcessorUpdatePacket;
import nc.recipe.AbstractRecipeHandler;
import nc.recipe.IngredientSorption;
import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.recipe.ingredient.IItemIngredient;
import nc.tile.IGui;
import nc.tile.energy.ITileEnergy;
import nc.tile.energy.TileEnergySidedInventory;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.Tank;
import nc.util.ArrayHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

public class TileItemProcessor extends TileEnergySidedInventory implements IItemProcessor, IGui<ProcessorUpdatePacket>, IUpgradable {
	
	public final int[] slots;
	
	public final int defaultProcessTime, defaultProcessPower;
	public double baseProcessTime, baseProcessPower, baseProcessRadiation;
	public final int itemInputSize, itemOutputSize;
	
	public double time;
	public boolean isProcessing, canProcessInputs;
	
	public final boolean shouldLoseProgress, hasUpgrades;
	public final int guiID;
	
	public final NCRecipes.Type recipeType;
	protected ProcessorRecipe recipe;
	
	protected Set<EntityPlayer> playersToUpdate;
	
	public TileItemProcessor(String name, int itemInSize, int itemOutSize, int time, int power, boolean shouldLoseProgress, @Nonnull NCRecipes.Type recipeType) {
		this(name, itemInSize, itemOutSize, time, power, shouldLoseProgress, false, recipeType, 1);
	}
	
	public TileItemProcessor(String name, int itemInSize, int itemOutSize, int time, int power, boolean shouldLoseProgress, @Nonnull NCRecipes.Type recipeType, int guiID) {
		this(name, itemInSize, itemOutSize, time, power, shouldLoseProgress, true, recipeType, guiID);
	}
	
	public TileItemProcessor(String name, int itemInSize, int itemOutSize, int time, int power, boolean shouldLoseProgress, boolean upgrades, @Nonnull NCRecipes.Type recipeType, int guiID) {
		super(name, itemInSize + itemOutSize + (upgrades ? 2 : 0), 32000, power != 0 ? ITileEnergy.energyConnectionAll(EnergyConnection.IN) : ITileEnergy.energyConnectionAll(EnergyConnection.NON));
		itemInputSize = itemInSize;
		itemOutputSize = itemOutSize;
		
		defaultProcessTime = time;
		defaultProcessPower = power;
		baseProcessTime = time;
		baseProcessPower = power;
		
		this.shouldLoseProgress = shouldLoseProgress;
		hasUpgrades = upgrades;
		this.guiID = guiID;
		
		this.recipeType = recipeType;
		
		slots = ArrayHelper.increasingArray(itemInSize + itemOutSize + (hasUpgrades ? 2 : 0));
		
		playersToUpdate = new HashSet<EntityPlayer>();
	}
	
	// Ticking
	
	@Override
	public void onAdded() {
		super.onAdded();
		if (!world.isRemote) {
			refreshRecipe();
			refreshActivity();
			refreshUpgrades();
			isProcessing = isProcessing();
		}
	}
	
	@Override
	public void update() {
		super.update();
		updateProcessor();
	}
	
	public void updateProcessor() {
		if (!world.isRemote) {
			boolean wasProcessing = isProcessing;
			isProcessing = isProcessing();
			boolean shouldUpdate = false;
			if (isProcessing) process();
			else {
				getRadiationSource().setRadiationLevel(0D);
				if (time > 0 && !isRedstonePowered()) loseProgress();
			}
			if (wasProcessing != isProcessing) {
				shouldUpdate = true;
				updateBlockType();
				sendUpdateToAllPlayers();
			}
			sendUpdateToListeningPlayers();
			if (shouldUpdate) markDirty();
		}
	}
	
	public void updateBlockType() {
		if (ModCheck.ic2Loaded()) removeTileFromENet();
		setState(isProcessing);
		world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
		if (ModCheck.ic2Loaded()) addTileToENet();
	}
	
	@Override
	public void refreshRecipe() {
		if (recipe == null || !recipe.matchingInputs(getItemInputs(), new ArrayList<Tank>())) {
			recipe = getRecipeHandler().getRecipeFromInputs(getItemInputs(), new ArrayList<Tank>());
		}
	}
	
	@Override
	public void refreshActivity() {
		canProcessInputs = canProcessInputs();
	}
	
	// Processor Stats
	
	public double getProcessTime() {
		return Math.max(1, baseProcessTime/getSpeedMultiplier());
	}
	
	public int getProcessPower() {
		return Math.min(Integer.MAX_VALUE, (int) ((double)baseProcessPower*getPowerMultiplier()));
	}
	
	public double getProcessEnergy() {
		return getProcessTime()*getProcessPower();
	}
	
	public boolean setRecipeStats() {
		if (recipe == null) {
			baseProcessTime = defaultProcessTime;
			baseProcessPower = defaultProcessPower;
			baseProcessRadiation = 0D;
			return false;
		}
		baseProcessTime = recipe.getProcessTime(defaultProcessTime);
		baseProcessPower = recipe.getProcessPower(defaultProcessPower);
		baseProcessRadiation = recipe.getProcessRadiation();
		return true;
	}
	
	public void setCapacityFromSpeed() {
		getEnergyStorage().setStorageCapacity(MathHelper.clamp(NCConfig.machine_update_rate*getProcessPower(), 32000, Integer.MAX_VALUE));
		getEnergyStorage().setMaxTransfer(MathHelper.clamp(NCConfig.machine_update_rate*getProcessPower(), 32000, Integer.MAX_VALUE));
	}
	
	private int getMaxEnergyModified() { // Needed for Galacticraft
		return ModCheck.galacticraftLoaded() ? getMaxEnergyStored() - 16 : getMaxEnergyStored();
	}
	
	// Processing
	
	public boolean isProcessing() {
		return readyToProcess() && !isRedstonePowered();
	}
	
	public boolean readyToProcess() {
		return canProcessInputs;
	}
	
	public boolean canProcessInputs() {
		if (!setRecipeStats()) return false;
		else if (time >= baseProcessTime) return true;
		else if (!hasSufficientEnergy()) return false;
		return canProduceProducts();
	}
	
	public boolean hasSufficientEnergy() {
		return (time <= 0 && (getProcessEnergy() >= getMaxEnergyModified() || getProcessEnergy() <= getEnergyStored())) || (time > 0 && getEnergyStored() >= getProcessPower());
	}
	
	public boolean canProduceProducts() {
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
	
	public void process() {
		time += getSpeedMultiplier();
		getEnergyStorage().changeEnergyStored(-getProcessPower());
		getRadiationSource().setRadiationLevel(baseProcessRadiation*getSpeedMultiplier());
		if (time >= baseProcessTime) finishProcess();
	}
	
	public void finishProcess() {
		double oldProcessTime = baseProcessTime;
		produceProducts();
		refreshRecipe();
		if (!setRecipeStats()) time = 0;
		else time = MathHelper.clamp(time - oldProcessTime, 0D, baseProcessTime);
		refreshActivity();
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
	
	public void loseProgress() {
		time = MathHelper.clamp(time - 1.5D*getSpeedMultiplier(), 0D, baseProcessTime);
	}
	
	// IProcessor
	
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
	
	// Upgrades
	
	@Override
	public boolean hasUpgrades() {
		return hasUpgrades;
	}
	
	@Override
	public int getSpeedUpgradeSlot() {
		return itemInputSize + itemOutputSize;
	}
	
	@Override
	public int getEnergyUpgradeSlot() {
		return itemInputSize + itemOutputSize + 1;
	}
	
	@Override
	public int getSpeedCount() {
		return hasUpgrades ? inventoryStacks.get(getSpeedUpgradeSlot()).getCount() + 1 : 1;
	}
	
	@Override
	public int getEnergyCount() {
		return hasUpgrades ? Math.min(getSpeedCount(), inventoryStacks.get(getEnergyUpgradeSlot()).getCount() + 1) : 1;
	}
	
	@Override
	public void refreshUpgrades() {
		setCapacityFromSpeed();
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
	
	// IInventory
	
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack stack = super.decrStackSize(slot, amount);
		if (!world.isRemote) {
			if (slot < itemInputSize) {
				refreshRecipe();
				refreshActivity();
			}
			else if (slot < itemInputSize + itemOutputSize) {
				refreshActivity();
			}
			else if (slot == getSpeedUpgradeSlot() || slot == getEnergyUpgradeSlot()) {
				refreshUpgrades();
			}
		}
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		super.setInventorySlotContents(slot, stack);
		if (!world.isRemote) {
			if (slot < itemInputSize) {
				refreshRecipe();
				refreshActivity();
			}
			else if (slot < itemInputSize + itemOutputSize) {
				refreshActivity();
			}
			else if (slot == getSpeedUpgradeSlot() || slot == getEnergyUpgradeSlot()) {
				refreshUpgrades();
			}
		}
	}

	@Override
	public void markDirty() {
		refreshRecipe();
		refreshActivity();
		refreshUpgrades();
		super.markDirty();
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (stack == ItemStack.EMPTY) return false;
		if (hasUpgrades) {
			if (stack.getItem() == NCItems.upgrade) {
				if (slot == getSpeedUpgradeSlot()) return stack.getMetadata() == 0;
				else if (slot == getEnergyUpgradeSlot()) return stack.getMetadata() == 1;
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
	
	// ISidedInventory
	
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
	
	// IGui
	
	@Override
	public int getGuiID() {
		return guiID;
	}
	
	@Override
	public Set<EntityPlayer> getPlayersToUpdate() {
		return playersToUpdate;
	}
	
	@Override
	public ProcessorUpdatePacket getGuiUpdatePacket() {
		return new ProcessorUpdatePacket(pos, time, getEnergyStored(), baseProcessTime, baseProcessPower);
	}
	
	@Override
	public void onGuiPacket(ProcessorUpdatePacket message) {
		time = message.time;
		getEnergyStorage().setEnergyStored(message.energyStored);
		baseProcessTime = message.baseProcessTime;
		baseProcessPower = message.baseProcessPower;
	}
}
