package nc.tile.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.init.NCItems;
import nc.network.tile.ProcessorUpdatePacket;
import nc.recipe.AbstractRecipeHandler;
import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.recipe.RecipeInfo;
import nc.recipe.ingredient.IItemIngredient;
import nc.tile.IGui;
import nc.tile.energy.ITileEnergy;
import nc.tile.energy.TileEnergySidedInventory;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.inventory.InventoryConnection;
import nc.tile.internal.inventory.ItemOutputSetting;
import nc.tile.internal.inventory.ItemSorption;
import nc.tile.inventory.ITileInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class TileItemProcessor extends TileEnergySidedInventory implements IItemProcessor, IGui<ProcessorUpdatePacket>, IUpgradable {
	
	public final int defaultProcessTime, defaultProcessPower;
	public double baseProcessTime, baseProcessPower, baseProcessRadiation;
	public final int itemInputSize, itemOutputSize;
	
	public double time, resetTime;
	public boolean isProcessing, canProcessInputs;
	
	public final boolean shouldLoseProgress, hasUpgrades;
	public final int processorID, sideConfigYOffset;

	private int pushCooldown;

	public final ProcessorRecipeHandler recipeHandler;
	protected RecipeInfo<ProcessorRecipe> recipeInfo;
	
	protected Set<EntityPlayer> playersToUpdate;
	
	public TileItemProcessor(String name, int itemInSize, int itemOutSize, @Nonnull List<ItemSorption> itemSorptions, int time, int power, boolean shouldLoseProgress, @Nonnull ProcessorRecipeHandler recipeHandler, int processorID, int sideConfigYOffset) {
		this(name, itemInSize, itemOutSize, itemSorptions, time, power, shouldLoseProgress, true, recipeHandler, processorID, sideConfigYOffset);
	}
	
	public TileItemProcessor(String name, int itemInSize, int itemOutSize, @Nonnull List<ItemSorption> itemSorptions, int time, int power, boolean shouldLoseProgress, boolean upgrades, @Nonnull ProcessorRecipeHandler recipeHandler, int processorID, int sideConfigYOffset) {
		super(name, itemInSize + itemOutSize + (upgrades ? 2 : 0), ITileInventory.inventoryConnectionAll(itemSorptions), IProcessor.getCapacity(recipeHandler, time, 1D, power, 1D), power != 0 ? ITileEnergy.energyConnectionAll(EnergyConnection.IN) : ITileEnergy.energyConnectionAll(EnergyConnection.NON));
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
		
		playersToUpdate = new ObjectOpenHashSet<EntityPlayer>();
	}
	
	public static List<ItemSorption> defaultItemSorptions(int inSize, int outSize, boolean upgrades) {
		List<ItemSorption> itemSorptions = new ArrayList<ItemSorption>();
		for (int i = 0; i < inSize; i++) itemSorptions.add(ItemSorption.IN);
		for (int i = 0; i < outSize; i++) itemSorptions.add(ItemSorption.OUT);
		if (upgrades) {
			itemSorptions.add(ItemSorption.IN);
			itemSorptions.add(ItemSorption.IN);
		}
		return itemSorptions;
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
				if (time > 0 && (!isHaltedByRedstone() || !readyToProcess())) loseProgress();
				if (wasProcessing) {
					pushCooldown = 50;
				}
				if (pushCooldown <= 0) {
					for (int i = 0; i < itemOutputSize; i++) {
						shouldUpdate |= pushItemProducts(i + itemInputSize);
					}
					pushCooldown = 50;
				}
				pushCooldown--;
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
		setState(isProcessing, this);
		world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
		if (ModCheck.ic2Loaded()) addTileToENet();
	}
	
	@Override
	public void refreshRecipe() {
		recipeInfo = recipeHandler.getRecipeInfoFromInputs(getItemInputs(), new ArrayList<Tank>());
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
		return Math.max(1, (int) Math.round(Math.ceil(baseProcessTime/getSpeedMultiplier())));
	}
	
	public int getProcessPower() {
		return Math.min(Integer.MAX_VALUE, (int) (baseProcessPower*getPowerMultiplier()));
	}
	
	public int getProcessEnergy() {
		return getProcessTime()*getProcessPower();
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
		getEnergyStorage().setStorageCapacity(IProcessor.getCapacity(recipeHandler, defaultProcessTime, getSpeedMultiplier(), defaultProcessPower, getPowerMultiplier()));
		getEnergyStorage().setMaxTransfer(IProcessor.getCapacity(recipeHandler, defaultProcessTime, getSpeedMultiplier(), defaultProcessPower, getPowerMultiplier()));
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
		return (time <= resetTime && ((getProcessEnergy() >= getMaxEnergyModified() && getEnergyStored() >= getMaxEnergyModified()) || getProcessEnergy() <= getEnergyStored())) || (time > resetTime && getEnergyStored() >= getProcessPower());
	}
	
	public boolean canProduceProducts() {
		for (int j = 0; j < itemOutputSize; j++) {
			if (getItemOutputSetting(j + itemInputSize) == ItemOutputSetting.VOID) {
				getInventoryStacks().set(j + itemInputSize, ItemStack.EMPTY);
				continue;
			}
			IItemIngredient itemProduct = getItemProducts().get(j);
			if (itemProduct.getMaxStackSize(0) <= 0) continue;
			if (itemProduct.getStack() == null || itemProduct.getStack() == ItemStack.EMPTY) return false;
			else if (!getInventoryStacks().get(j + itemInputSize).isEmpty()) {
				if (!getInventoryStacks().get(j + itemInputSize).isItemEqual(itemProduct.getStack())) {
					return false;
				} else if (getItemOutputSetting(j + itemInputSize) == ItemOutputSetting.DEFAULT && getInventoryStacks().get(j + itemInputSize).getCount() + itemProduct.getMaxStackSize(0) > getInventoryStacks().get(j + itemInputSize).getMaxStackSize()) {
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
		if (!setRecipeStats()) time = resetTime = 0;
		else time = resetTime = MathHelper.clamp(time - oldProcessTime, 0D, baseProcessTime);
		refreshActivityOnProduction();
		if (!canProcessInputs) time = resetTime = 0;
	}
	
	public void produceProducts() {
		if (recipeInfo == null) return;
		List<Integer> itemInputOrder = recipeInfo.getItemInputOrder();
		if (itemInputOrder == AbstractRecipeHandler.INVALID) return;
		
		for (int i = 0; i < itemInputSize; i++) {
			int itemIngredientStackSize = getItemIngredients().get(itemInputOrder.get(i)).getMaxStackSize(recipeInfo.getItemIngredientNumbers().get(i));
			if (itemIngredientStackSize > 0) getInventoryStacks().get(i).shrink(itemIngredientStackSize);
			if (getInventoryStacks().get(i).getCount() <= 0) getInventoryStacks().set(i, ItemStack.EMPTY);
		}
		for (int j = 0; j < itemOutputSize; j++) {
			if (getItemOutputSetting(j + itemInputSize) == ItemOutputSetting.VOID) {
				getInventoryStacks().set(j + itemInputSize, ItemStack.EMPTY);
				continue;
			}
			IItemIngredient itemProduct = getItemProducts().get(j);
			if (itemProduct.getMaxStackSize(0) <= 0) continue;
			if (getInventoryStacks().get(j + itemInputSize).isEmpty()) {
				getInventoryStacks().set(j + itemInputSize, itemProduct.getNextStack(0));
			} else if (getInventoryStacks().get(j + itemInputSize).isItemEqual(itemProduct.getStack())) {
				int count = Math.min(getInventoryStackLimit(), getInventoryStacks().get(j + itemInputSize).getCount() + itemProduct.getNextStackSize(0));
				getInventoryStacks().get(j + itemInputSize).setCount(count);
			}
			pushItemProducts(j+itemInputSize);
		}
	}

	private boolean pushItemProducts(int slot) {
		ItemStack stackInSlot = getInventoryStacks().get(slot);
		if (stackInSlot.isEmpty()) return false;
		InventoryConnection[] connections = getInventoryConnections();
		boolean hasDoneWork = false;
		for (EnumFacing side : EnumFacing.VALUES) {
			if (connections[side.ordinal()].getItemSorption(slot) == ItemSorption.PUSH)	{
				TileEntity tile = world.getTileEntity(pos.offset(side));
				if (tile == null) continue;
				IItemHandler inventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side.getOpposite());
				if (inventory == null) continue;
				ItemStack remainder = ItemHandlerHelper.insertItemStacked(inventory, stackInSlot, false);
				hasDoneWork |= remainder.getCount() != stackInSlot.getCount();
				stackInSlot = remainder;
				if (stackInSlot.isEmpty()) break;
			}
		}
		getInventoryStacks().set(slot, stackInSlot);
		return hasDoneWork;
	}
	
	public void loseProgress() {
		time = MathHelper.clamp(time - 1.5D*getSpeedMultiplier(), 0D, baseProcessTime);
		if (time < resetTime) resetTime = time;
	}
	
	// IProcessor
	
	@Override
	public List<ItemStack> getItemInputs() {
		return getInventoryStacks().subList(0, itemInputSize);
	}
	
	@Override
	public List<IItemIngredient> getItemIngredients() {
		return recipeInfo.getRecipe().itemIngredients();
	}
	
	@Override
	public List<IItemIngredient> getItemProducts() {
		return recipeInfo.getRecipe().itemProducts();
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
	public int getEUSourceTier() {
		return 1;
	}
		
	@Override
	public int getEUSinkTier() {
		return 10;
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
		if (stack == ItemStack.EMPTY) return false;
		if (hasUpgrades) {
			if (stack.getItem() == NCItems.upgrade) {
				if (slot == getSpeedUpgradeSlot()) return stack.getMetadata() == 0;
				else if (slot == getEnergyUpgradeSlot()) return stack.getMetadata() == 1;
			}
		}
		if (slot >= itemInputSize) return false;
		return NCConfig.smart_processor_input ? recipeHandler.isValidItemInput(stack, getInventoryStacks().get(slot), inputItemStacksExcludingSlot(slot)) : recipeHandler.isValidItemInput(stack);
	}
	
	public List<ItemStack> inputItemStacksExcludingSlot(int slot) {
		List<ItemStack> inputItemsExcludingSlot = new ArrayList<ItemStack>(getItemInputs());
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
		nbt.setInteger("pushCooldown", pushCooldown);
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
		} else setRedstoneControl(true);
		pushCooldown = nbt.getInteger("pushCooldown");
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
		return new ProcessorUpdatePacket(pos, time, getEnergyStored(), baseProcessTime, baseProcessPower, new ArrayList<Tank>());
	}
	
	@Override
	public void onGuiPacket(ProcessorUpdatePacket message) {
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
