package nc.tile.processor;

import static nc.config.NCConfig.smart_processor_input;

import java.util.*;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import nc.ModCheck;
import nc.init.NCItems;
import nc.network.tile.ProcessorUpdatePacket;
import nc.recipe.*;
import nc.recipe.ingredient.IItemIngredient;
import nc.tile.ITileGui;
import nc.tile.energy.*;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.inventory.*;
import nc.tile.inventory.ITileInventory;
import nc.util.StackHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

public class TileItemProcessor extends TileEnergySidedInventory implements IItemProcessor, ITileGui<ProcessorUpdatePacket>, IUpgradable {
	
	public final int defaultProcessTime, defaultProcessPower;
	public double baseProcessTime, baseProcessPower, baseProcessRadiation;
	protected final int itemInputSize, itemOutputSize;
	
	public double time, resetTime;
	public boolean isProcessing, canProcessInputs;
	
	public final boolean shouldLoseProgress, hasUpgrades;
	public final int processorID, sideConfigYOffset;
	
	public final BasicRecipeHandler recipeHandler;
	protected RecipeInfo<BasicRecipe> recipeInfo;
	
	protected Set<EntityPlayer> playersToUpdate;
	
	public TileItemProcessor(String name, int itemInSize, int itemOutSize, @Nonnull List<ItemSorption> itemSorptions, int time, int power, boolean shouldLoseProgress, @Nonnull BasicRecipeHandler recipeHandler, int processorID, int sideConfigYOffset) {
		this(name, itemInSize, itemOutSize, itemSorptions, time, power, shouldLoseProgress, true, recipeHandler, processorID, sideConfigYOffset);
	}
	
	public TileItemProcessor(String name, int itemInSize, int itemOutSize, @Nonnull List<ItemSorption> itemSorptions, int time, int power, boolean shouldLoseProgress, boolean upgrades, @Nonnull BasicRecipeHandler recipeHandler, int processorID, int sideConfigYOffset) {
		super(name, itemInSize + itemOutSize + (upgrades ? 2 : 0), ITileInventory.inventoryConnectionAll(itemSorptions), IProcessor.getCapacity(processorID, 1D, 1D), power != 0 ? ITileEnergy.energyConnectionAll(EnergyConnection.IN) : ITileEnergy.energyConnectionAll(EnergyConnection.NON));
		itemInputSize = itemInSize;
		itemOutputSize = itemOutSize;
		
		defaultProcessTime = time;
		defaultProcessPower = power;
		baseProcessTime = time;
		baseProcessPower = power;
		
		this.shouldLoseProgress = shouldLoseProgress;
		hasUpgrades = upgrades;
		this.processorID = processorID;
		this.sideConfigYOffset = sideConfigYOffset;
		
		this.recipeHandler = recipeHandler;
		
		playersToUpdate = new ObjectOpenHashSet<>();
	}
	
	public static List<ItemSorption> defaultItemSorptions(int inSize, int outSize, boolean upgrades) {
		List<ItemSorption> itemSorptions = new ArrayList<>();
		for (int i = 0; i < inSize; i++) {
			itemSorptions.add(ItemSorption.IN);
		}
		for (int i = 0; i < outSize; i++) {
			itemSorptions.add(ItemSorption.OUT);
		}
		if (upgrades) {
			itemSorptions.add(ItemSorption.IN);
			itemSorptions.add(ItemSorption.IN);
		}
		return itemSorptions;
	}
	
	// Ticking
	
	@Override
	public void onLoad() {
		super.onLoad();
		if (!world.isRemote) {
			refreshRecipe();
			refreshActivity();
			refreshUpgrades();
			isProcessing = isProcessing();
		}
	}
	
	@Override
	public void update() {
		if (!world.isRemote) {
			boolean wasProcessing = isProcessing;
			isProcessing = isProcessing();
			boolean shouldUpdate = false;
			if (isProcessing) {
				process();
			}
			else {
				getRadiationSource().setRadiationLevel(0D);
				if (time > 0 && !isHaltedByRedstone() && (shouldLoseProgress || !canProcessInputs)) {
					loseProgress();
				}
			}
			if (wasProcessing != isProcessing) {
				shouldUpdate = true;
				setActivity(isProcessing);
				sendUpdateToAllPlayers();
			}
			sendUpdateToListeningPlayers();
			if (shouldUpdate) {
				markDirty();
			}
		}
	}
	
	@Override
	public void refreshRecipe() {
		recipeInfo = recipeHandler.getRecipeInfoFromInputs(getItemInputs(), new ArrayList<>());
	}
	
	@Override
	public void refreshActivity() {
		canProcessInputs = canProcessInputs();
	}
	
	@Override
	public void refreshActivityOnProduction() {
		canProcessInputs = canProcessInputs();
	}
	
	// Processor Stats
	
	public int getProcessTime() {
		return Math.max(1, (int) Math.round(Math.ceil(baseProcessTime / getSpeedMultiplier())));
	}
	
	public int getProcessPower() {
		return Math.min(Integer.MAX_VALUE, (int) (baseProcessPower * getPowerMultiplier()));
	}
	
	public int getProcessEnergy() {
		return getProcessTime() * getProcessPower();
	}
	
	public boolean setRecipeStats() {
		if (recipeInfo == null) {
			baseProcessTime = defaultProcessTime;
			baseProcessPower = defaultProcessPower;
			baseProcessRadiation = 0D;
			return false;
		}
		baseProcessTime = recipeInfo.getRecipe().getBaseProcessTime(defaultProcessTime);
		baseProcessPower = recipeInfo.getRecipe().getBaseProcessPower(defaultProcessPower);
		baseProcessRadiation = recipeInfo.getRecipe().getBaseProcessRadiation();
		return true;
	}
	
	public void setCapacityFromSpeed() {
		int capacity = IProcessor.getCapacity(processorID, getSpeedMultiplier(), getPowerMultiplier());
		getEnergyStorage().setStorageCapacity(capacity);
		getEnergyStorage().setMaxTransfer(capacity);
	}
	
	private int getMaxEnergyModified() { // Needed for Galacticraft
		return ModCheck.galacticraftLoaded() ? Math.max(0, getMaxEnergyStored() - 16) : getMaxEnergyStored();
	}
	
	// Processing
	
	public boolean isProcessing() {
		return readyToProcess() && !isHaltedByRedstone();
	}
	
	public boolean isHaltedByRedstone() {
		return getRedstoneControl() && getIsRedstonePowered();
	}
	
	public boolean readyToProcess() {
		return canProcessInputs && hasSufficientEnergy();
	}
	
	public boolean canProcessInputs() {
		boolean validRecipe = setRecipeStats(), canProcess = validRecipe && canProduceProducts();
		if (!canProcess) {
			time = MathHelper.clamp(time, 0D, baseProcessTime - 1D);
		}
		return canProcess;
	}
	
	public boolean hasSufficientEnergy() {
		return time <= resetTime && (getProcessEnergy() >= getMaxEnergyModified() && getEnergyStored() >= getMaxEnergyModified() || getProcessEnergy() <= getEnergyStored()) || time > resetTime && getEnergyStored() >= getProcessPower();
	}
	
	public boolean canProduceProducts() {
		for (int j = 0; j < itemOutputSize; j++) {
			if (getItemOutputSetting(j + itemInputSize) == ItemOutputSetting.VOID) {
				getInventoryStacks().set(j + itemInputSize, ItemStack.EMPTY);
				continue;
			}
			IItemIngredient itemProduct = getItemProducts().get(j);
			if (itemProduct.getMaxStackSize(0) <= 0) {
				continue;
			}
			if (itemProduct.getStack() == null || itemProduct.getStack().isEmpty()) {
				return false;
			}
			else if (!getInventoryStacks().get(j + itemInputSize).isEmpty()) {
				if (!getInventoryStacks().get(j + itemInputSize).isItemEqual(itemProduct.getStack())) {
					return false;
				}
				else if (getItemOutputSetting(j + itemInputSize) == ItemOutputSetting.DEFAULT && getInventoryStacks().get(j + itemInputSize).getCount() + itemProduct.getMaxStackSize(0) > getInventoryStacks().get(j + itemInputSize).getMaxStackSize()) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void process() {
		time += getSpeedMultiplier();
		getEnergyStorage().changeEnergyStored(-getProcessPower());
		getRadiationSource().setRadiationLevel(baseProcessRadiation * getSpeedMultiplier());
		while (time >= baseProcessTime) {
			finishProcess();
		}
	}
	
	public void finishProcess() {
		double oldProcessTime = baseProcessTime;
		produceProducts();
		refreshRecipe();
		time = resetTime = Math.max(0D, time - oldProcessTime);
		refreshActivityOnProduction();
		if (!canProcessInputs) {
			time = resetTime = 0;
		}
	}
	
	public void produceProducts() {
		if (recipeInfo == null) {
			return;
		}
		IntList itemInputOrder = recipeInfo.getItemInputOrder();
		if (itemInputOrder == AbstractRecipeHandler.INVALID) {
			return;
		}
		
		for (int i = 0; i < itemInputSize; i++) {
			int itemIngredientStackSize = getItemIngredients().get(itemInputOrder.get(i)).getMaxStackSize(recipeInfo.getItemIngredientNumbers().get(i));
			if (itemIngredientStackSize > 0) {
				getInventoryStacks().get(i).shrink(itemIngredientStackSize);
			}
			if (getInventoryStacks().get(i).getCount() <= 0) {
				getInventoryStacks().set(i, ItemStack.EMPTY);
			}
		}
		for (int j = 0; j < itemOutputSize; j++) {
			if (getItemOutputSetting(j + itemInputSize) == ItemOutputSetting.VOID) {
				getInventoryStacks().set(j + itemInputSize, ItemStack.EMPTY);
				continue;
			}
			IItemIngredient itemProduct = getItemProducts().get(j);
			if (itemProduct.getMaxStackSize(0) <= 0) {
				continue;
			}
			if (getInventoryStacks().get(j + itemInputSize).isEmpty()) {
				getInventoryStacks().set(j + itemInputSize, itemProduct.getNextStack(0));
			}
			else if (getInventoryStacks().get(j + itemInputSize).isItemEqual(itemProduct.getStack())) {
				int count = Math.min(getInventoryStackLimit(), getInventoryStacks().get(j + itemInputSize).getCount() + itemProduct.getNextStackSize(0));
				getInventoryStacks().get(j + itemInputSize).setCount(count);
			}
		}
	}
	
	public void loseProgress() {
		time = MathHelper.clamp(time - 1.5D * getSpeedMultiplier(), 0D, baseProcessTime);
		if (time < resetTime) {
			resetTime = time;
		}
	}
	
	// IProcessor
	
	@Override
	public int getItemInputSize() {
		return itemInputSize;
	}
	
	@Override
	public int getItemOutputSize() {
		return itemOutputSize;
	}
	
	@Override
	public List<ItemStack> getItemInputs() {
		return getInventoryStacks().subList(0, itemInputSize);
	}
	
	@Override
	public List<IItemIngredient> getItemIngredients() {
		return recipeInfo.getRecipe().getItemIngredients();
	}
	
	@Override
	public List<IItemIngredient> getItemProducts() {
		return recipeInfo.getRecipe().getItemProducts();
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
		return hasUpgrades ? getInventoryStacks().get(getSpeedUpgradeSlot()).getCount() + 1 : 1;
	}
	
	@Override
	public int getEnergyCount() {
		return hasUpgrades ? Math.min(getSpeedCount(), getInventoryStacks().get(getEnergyUpgradeSlot()).getCount() + 1) : 1;
	}
	
	@Override
	public void refreshUpgrades() {
		setCapacityFromSpeed();
	}
	
	// IC2 Tiers
	
	@Override
	public int getSinkTier() {
		return 10;
	}
	
	@Override
	public int getSourceTier() {
		return 1;
	}
	
	// ITileInventory
	
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
		if (stack.isEmpty()) {
			return false;
		}
		if (hasUpgrades) {
			if (stack.getItem() == NCItems.upgrade) {
				if (slot == getSpeedUpgradeSlot()) {
					return StackHelper.getMetadata(stack) == 0;
				}
				else if (slot == getEnergyUpgradeSlot()) {
					return StackHelper.getMetadata(stack) == 1;
				}
			}
		}
		if (slot >= itemInputSize) {
			return false;
		}
		return smart_processor_input ? recipeHandler.isValidItemInput(stack, getInventoryStacks().get(slot), inputItemStacksExcludingSlot(slot)) : recipeHandler.isValidItemInput(stack);
	}
	
	public List<ItemStack> inputItemStacksExcludingSlot(int slot) {
		List<ItemStack> inputItemsExcludingSlot = new ArrayList<>(getItemInputs());
		inputItemsExcludingSlot.remove(slot);
		return inputItemsExcludingSlot;
	}
	
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
		return super.canInsertItem(slot, stack, side) && isItemValidForSlot(slot, stack);
	}
	
	@Override
	public boolean hasConfigurableInventoryConnections() {
		return true;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setDouble("time", time);
		nbt.setDouble("resetTime", resetTime);
		nbt.setBoolean("isProcessing", isProcessing);
		nbt.setBoolean("canProcessInputs", canProcessInputs);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		time = nbt.getDouble("time");
		resetTime = nbt.getDouble("resetTime");
		isProcessing = nbt.getBoolean("isProcessing");
		canProcessInputs = nbt.getBoolean("canProcessInputs");
		if (nbt.hasKey("redstoneControl")) {
			setRedstoneControl(nbt.getBoolean("redstoneControl"));
		}
		else {
			setRedstoneControl(true);
		}
	}
	
	// IGui
	
	@Override
	public int getGuiID() {
		return processorID;
	}
	
	@Override
	public Set<EntityPlayer> getPlayersToUpdate() {
		return playersToUpdate;
	}
	
	@Override
	public ProcessorUpdatePacket getGuiUpdatePacket() {
		return new ProcessorUpdatePacket(pos, isProcessing, time, getEnergyStored(), baseProcessTime, baseProcessPower, new ArrayList<>());
	}
	
	@Override
	public void onGuiPacket(ProcessorUpdatePacket message) {
		isProcessing = message.isProcessing;
		time = message.time;
		getEnergyStorage().setEnergyStored(message.energyStored);
		baseProcessTime = message.baseProcessTime;
		baseProcessPower = message.baseProcessPower;
	}
	
	@Override
	public int getSideConfigYOffset() {
		return sideConfigYOffset;
	}
}
