package nc.tile.processor;

import java.util.*;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.network.tile.ProcessorUpdatePacket;
import nc.recipe.*;
import nc.recipe.ingredient.*;
import nc.tile.energyFluid.TileEnergyFluidSidedInventory;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.*;
import nc.tile.internal.inventory.InventoryConnection;
import nc.util.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;

public class TileProcessor<TILE extends TileProcessor<TILE, INFO>, INFO extends ProcessorContainerInfo<TILE, INFO>> extends TileEnergyFluidSidedInventory implements IProcessor<TILE, INFO> {
	
	protected final INFO containerInfo;
	
	public double baseProcessTime, baseProcessPower, baseProcessRadiation;
	
	protected final @Nonnull NonNullList<ItemStack> consumedStacks;
	protected final @Nonnull List<Tank> consumedTanks = new ArrayList<>();
	
	public double time, resetTime;
	public boolean isProcessing, canProcessInputs, hasConsumed;
	
	protected final BasicRecipeHandler recipeHandler;
	protected RecipeInfo<BasicRecipe> recipeInfo = null;
	
	protected final Set<EntityPlayer> updatePacketListeners = new ObjectOpenHashSet<>();
	
	protected TileProcessor(String name, INFO containerInfo, @Nonnull InventoryConnection[] inventoryConnections, long capacity, @Nonnull EnergyConnection[] energyConnections, @Nonnull IntList fluidCapacity, List<List<String>> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		super(name, containerInfo.getInventorySize(), inventoryConnections, capacity, energyConnections, fluidCapacity, allowedFluids, fluidConnections);
		this.containerInfo = containerInfo;
		
		this.baseProcessTime = containerInfo.defaultProcessTime;
		this.baseProcessPower = containerInfo.defaultProcessPower;
		
		setInputTanksSeparated(containerInfo.fluidInputSize > 1);
		
		if (containerInfo.consumesInputs) {
			consumedStacks = NonNullList.withSize(containerInfo.itemInputSize, ItemStack.EMPTY);
			List<Tank> tanks = getTanks();
			for (int i = 0; i < containerInfo.fluidInputSize; ++i) {
				consumedTanks.add(new Tank(tanks.get(i).getCapacity(), new ArrayList<>()));
			}
		}
		else {
			consumedStacks = NonNullList.withSize(0, ItemStack.EMPTY);
		}
		
		recipeHandler = containerInfo.getRecipeHandler();
	}
	
	@Override
	public INFO getContainerInfo() {
		return containerInfo;
	}
	
	@Override
	public boolean getHasConsumed() {
		return hasConsumed;
	}
	
	@Override
	public void setHasConsumed(boolean hasConsumed) {
		this.hasConsumed = hasConsumed;
	}
	
	// Ticking
	
	@Override
	public void onLoad() {
		super.onLoad();
		if (!world.isRemote) {
			refreshAll();
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
				if (time > 0) {
					if (containerInfo.losesProgress && !isHaltedByRedstone()) {
						loseProgress();
					}
					else if (!canProcessInputs) {
						time = resetTime = 0;
					}
				}
			}
			if (wasProcessing != isProcessing) {
				shouldUpdate = true;
				setActivity(isProcessing);
				sendTileUpdatePacketToAll();
			}
			sendTileUpdatePacketToListeners();
			if (shouldUpdate) {
				markDirty();
			}
		}
	}
	
	@Override
	public void refreshRecipe() {
		recipeInfo = recipeHandler.getRecipeInfoFromInputs(getItemInputs(hasConsumed), getFluidInputs(hasConsumed));
		if (containerInfo.consumesInputs) {
			consumeInputs();
		}
	}
	
	@Override
	public void refreshActivity() {
		canProcessInputs = canProcessInputs();
	}
	
	public void refreshActivityOnProduction() {
		canProcessInputs = canProcessInputs();
	}
	
	public void refreshEnergyCapacity() {}
	
	public void refreshAll() {
		refreshRecipe();
		refreshActivity();
		refreshEnergyCapacity();
		isProcessing = isProcessing();
		hasConsumed = hasConsumed();
	}
	
	// Processor Stats
	
	public int getProcessTime() {
		return Math.max(1, NCMath.toInt(Math.round(Math.ceil(baseProcessTime / getSpeedMultiplier()))));
	}
	
	public int getProcessPower() {
		return NCMath.toInt(Math.ceil(baseProcessPower * getPowerMultiplier()));
	}
	
	public int getProcessEnergy() {
		return getProcessTime() * getProcessPower();
	}
	
	public boolean setRecipeStats() {
		if (recipeInfo == null) {
			baseProcessTime = containerInfo.defaultProcessTime;
			baseProcessPower = containerInfo.defaultProcessPower;
			baseProcessRadiation = 0D;
			return false;
		}
		BasicRecipe recipe = recipeInfo.getRecipe();
		baseProcessTime = recipe.getBaseProcessTime(containerInfo.defaultProcessTime);
		baseProcessPower = recipe.getBaseProcessPower(containerInfo.defaultProcessPower);
		baseProcessRadiation = recipe.getBaseProcessRadiation();
		return true;
	}
	
	// Needed for Galacticraft
	protected int getMaxEnergyModified() {
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
		return canProcessInputs && (!containerInfo.consumesInputs || hasConsumed) && hasSufficientEnergy();
	}
	
	public boolean canProcessInputs() {
		boolean validRecipe = setRecipeStats();
		if (hasConsumed && !validRecipe) {
			List<ItemStack> itemInputs = getItemInputs(true);
			for (int i = 0; i < containerInfo.itemInputSize; ++i) {
				itemInputs.set(i, ItemStack.EMPTY);
			}
			for (Tank tank : getFluidInputs(true)) {
				tank.setFluidStored(null);
			}
			hasConsumed = false;
		}
		
		boolean canProcess = validRecipe && canProduceProducts();
		if (!canProcess) {
			time = MathHelper.clamp(time, 0D, baseProcessTime - 1D);
		}
		return canProcess;
	}
	
	public boolean hasSufficientEnergy() {
		return time <= resetTime && (getProcessEnergy() >= getMaxEnergyModified() && getEnergyStored() >= getMaxEnergyModified() || getProcessEnergy() <= getEnergyStored()) || time > resetTime && getEnergyStored() >= getProcessPower();
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
			List<Tank> tanks = getTanks();
			for (int i = 0; i < containerInfo.fluidInputSize; ++i) {
				if (getVoidUnusableFluidInput(i)) {
					tanks.get(i).setFluidStored(null);
				}
			}
		}
	}
	
	public void loseProgress() {
		time = MathHelper.clamp(time - 1.5D * getSpeedMultiplier(), 0D, baseProcessTime);
		if (time < resetTime) {
			resetTime = time;
		}
	}
	
	public double getSpeedMultiplier() {
		return 1D;
	}
	
	public double getPowerMultiplier() {
		return 1D;
	}
	
	// IProcessor
	
	@Override
	public List<ItemStack> getItemInputs(boolean consumed) {
		return consumed ? consumedStacks : getInventoryStacks().subList(0, containerInfo.itemInputSize);
	}
	
	@Override
	public List<Tank> getFluidInputs(boolean consumed) {
		return consumed ? consumedTanks : getTanks().subList(0, containerInfo.fluidInputSize);
	}
	
	@Override
	public @Nonnull NonNullList<ItemStack> getConsumedStacks() {
		return consumedStacks;
	}
	
	@Override
	public @Nonnull List<Tank> getConsumedTanks() {
		return consumedTanks;
	}
	
	@Override
	public RecipeInfo<BasicRecipe> getRecipeInfo() {
		return recipeInfo;
	}
	
	@Override
	public List<IItemIngredient> getItemIngredients() {
		return recipeInfo.getRecipe().getItemIngredients();
	}
	
	@Override
	public List<IFluidIngredient> getFluidIngredients() {
		return recipeInfo.getRecipe().getFluidIngredients();
	}
	
	@Override
	public List<IItemIngredient> getItemProducts() {
		return recipeInfo.getRecipe().getItemProducts();
	}
	
	@Override
	public List<IFluidIngredient> getFluidProducts() {
		return recipeInfo.getRecipe().getFluidProducts();
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
			if (slot < containerInfo.itemInputSize) {
				refreshRecipe();
				refreshActivity();
			}
			else if (slot < containerInfo.itemInputSize + containerInfo.itemOutputSize) {
				refreshActivity();
			}
		}
		return stack;
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		super.setInventorySlotContents(slot, stack);
		if (!world.isRemote) {
			if (slot < containerInfo.itemInputSize) {
				refreshRecipe();
				refreshActivity();
			}
			else if (slot < containerInfo.itemInputSize + containerInfo.itemOutputSize) {
				refreshActivity();
			}
		}
	}
	
	@Override
	public void markDirty() {
		refreshRecipe();
		refreshActivity();
		refreshEnergyCapacity();
		super.markDirty();
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		}
		if (slot >= containerInfo.itemInputSize && slot < containerInfo.itemInputSize + containerInfo.itemOutputSize) {
			return false;
		}
		
		if (NCConfig.smart_processor_input) {
			return recipeHandler.isValidItemInput(slot, stack, recipeInfo, getItemInputs(false), inputItemStacksExcludingSlot(slot));
		}
		else {
			return recipeHandler.isValidItemInput(slot, stack);
		}
	}
	
	public List<ItemStack> inputItemStacksExcludingSlot(int slot) {
		List<ItemStack> inputItemsExcludingSlot = new ArrayList<>(getItemInputs(false));
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
	
	@Override
	public void clearAllSlots() {
		IProcessor.super.clearAllSlots();
		for (int i = 0; i < consumedStacks.size(); ++i) {
			consumedStacks.set(i, ItemStack.EMPTY);
		}
		refreshAll();
	}
	
	@Override
	public void clearAllTanks() {
		IProcessor.super.clearAllTanks();
		for (Tank tank : consumedTanks) {
			tank.setFluidStored(null);
		}
		refreshAll();
	}
	
	@Override
	public boolean hasConfigurableFluidConnections() {
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
		nbt.setBoolean("hasConsumed", hasConsumed);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		time = nbt.getDouble("time");
		resetTime = nbt.getDouble("resetTime");
		isProcessing = nbt.getBoolean("isProcessing");
		canProcessInputs = nbt.getBoolean("canProcessInputs");
		hasConsumed = nbt.getBoolean("hasConsumed");
	}
	
	@Override
	public NBTTagCompound writeInventory(NBTTagCompound nbt) {
		NBTHelper.writeAllItems(nbt, getInventoryStacks(), consumedStacks);
		return nbt;
	}
	
	@Override
	public void readInventory(NBTTagCompound nbt) {
		if (nbt.hasKey("hasConsumed")) {
			NBTHelper.readAllItems(nbt, getInventoryStacks(), consumedStacks);
		}
		else {
			super.readInventory(nbt);
		}
	}
	
	@Override
	public NBTTagCompound writeTanks(NBTTagCompound nbt) {
		super.writeTanks(nbt);
		for (int i = 0; i < consumedTanks.size(); ++i) {
			consumedTanks.get(i).writeToNBT(nbt, "consumedTanks" + i);
		}
		return nbt;
	}
	
	@Override
	public void readTanks(NBTTagCompound nbt) {
		super.readTanks(nbt);
		for (int i = 0; i < consumedTanks.size(); ++i) {
			consumedTanks.get(i).readFromNBT(nbt, "consumedTanks" + i);
		}
	}
	
	// IGui
	
	@Override
	public Set<EntityPlayer> getTileUpdatePacketListeners() {
		return updatePacketListeners;
	}
	
	@Override
	public ProcessorUpdatePacket getTileUpdatePacket() {
		return new ProcessorUpdatePacket(pos, isProcessing, time, getEnergyStored(), baseProcessTime, baseProcessPower, getTanks());
	}
	
	@Override
	public void onTileUpdatePacket(ProcessorUpdatePacket message) {
		isProcessing = message.isProcessing;
		time = message.time;
		getEnergyStorage().setEnergyStored(message.energyStored);
		baseProcessTime = message.baseProcessTime;
		baseProcessPower = message.baseProcessPower;
		
		List<Tank> tanks = getTanks();
		for (int i = 0; i < tanks.size(); ++i) {
			tanks.get(i).readInfo(message.tanksInfo.get(i));
		}
	}
}
