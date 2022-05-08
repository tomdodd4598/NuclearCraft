package nc.tile.processor;

import java.util.*;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.handler.TileInfo;
import nc.network.tile.ProcessorUpdatePacket;
import nc.recipe.*;
import nc.recipe.ingredient.*;
import nc.tile.ITileGui;
import nc.tile.energy.ITileEnergy;
import nc.tile.energyFluid.TileEnergyFluidSidedInventory;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.*;
import nc.tile.internal.inventory.ItemOutputSetting;
import nc.tile.inventory.ITileInventory;
import nc.util.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidStack;

public class TileAbstractProcessor<INFO extends ProcessorContainerInfo<?>> extends TileEnergyFluidSidedInventory implements IProcessor<INFO>, ITileGui<ProcessorUpdatePacket, INFO> {
	
	protected final INFO containerInfo;
	
	public final double defaultProcessTime, defaultProcessPower;
	public double baseProcessTime, baseProcessPower, baseProcessRadiation;
	
	protected final @Nonnull NonNullList<ItemStack> consumedStacks;
	protected final @Nonnull List<Tank> consumedTanks = new ArrayList<>();
	
	public double time, resetTime;
	public boolean isProcessing, canProcessInputs, hasConsumed;
	
	public final int sideConfigXOffset, sideConfigYOffset;
	
	protected final BasicRecipeHandler recipeHandler;
	protected RecipeInfo<BasicRecipe> recipeInfo = null;
	
	protected final Set<EntityPlayer> updatePacketListeners = new ObjectOpenHashSet<>();
	
	@SuppressWarnings("unchecked")
	public TileAbstractProcessor(String name, List<List<String>> allowedFluids, double baseProcessTime, double baseProcessPower) {
		this(name, (INFO) TileInfo.getContainerInfoProcessorInfo(name), allowedFluids, baseProcessTime, baseProcessPower);
	}
	
	protected TileAbstractProcessor(String name, INFO containerInfo, List<List<String>> allowedFluids, double baseProcessTime, double baseProcessPower) {
		super(name, containerInfo.getInventorySize(), ITileInventory.inventoryConnectionAll(IProcessor.defaultItemSorptions(containerInfo)), IProcessor.energyCapacity(containerInfo, 1D, 1D), ITileEnergy.energyConnectionAll(baseProcessPower == 0 ? EnergyConnection.NON : EnergyConnection.IN), IProcessor.defaultTankCapacities(containerInfo), allowedFluids, ITileFluid.fluidConnectionAll(IProcessor.defaultTankSorptions(containerInfo)));
		this.containerInfo = containerInfo;
		
		defaultProcessTime = baseProcessTime;
		defaultProcessPower = baseProcessPower;
		this.baseProcessTime = baseProcessTime;
		this.baseProcessPower = baseProcessPower;
		
		sideConfigXOffset = sideConfigXOffsetIn;
		sideConfigYOffset = sideConfigYOffsetIn;
		
		setInputTanksSeparated(getFluidInputSize() > 1);
		
		if (getConsumesInputs()) {
			consumedStacks = NonNullList.withSize(getItemInputSize(), ItemStack.EMPTY);
			List<Tank> tanks = getTanks();
			for (int i = 0; i < getFluidInputSize(); ++i) {
				consumedTanks.add(new Tank(tanks.get(i).getCapacity(), new ArrayList<>()));
			}
		}
		else {
			consumedStacks = NonNullList.withSize(0, ItemStack.EMPTY);
		}
		
		this.recipeHandler = NCRecipes.RECIPE_HANDLER_MAP.get(name);
	}
	
	@Override
	public INFO getContainerInfo() {
		return containerInfo;
	}
	
	@Override
	public int getItemInputSize() {
		return containerInfo.itemInputSize;
	}
	
	@Override
	public int getFluidInputSize() {
		return containerInfo.fluidInputSize;
	}
	
	@Override
	public int getItemOutputSize() {
		return containerInfo.itemOutputSize;
	}
	
	@Override
	public int getFluidOutputSize() {
		return containerInfo.fluidOutputSize;
	}
	
	@Override
	public boolean getConsumesInputs() {
		return containerInfo.consumesInputs;
	}
	
	@Override
	public boolean getLosesProgress() {
		return containerInfo.losesProgress;
	}
	
	@Override
	public int getSideConfigXOffset() {
		return sideConfigXOffset;
	}
	
	@Override
	public int getSideConfigYOffset() {
		return sideConfigYOffset;
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
					if (getLosesProgress() && !isHaltedByRedstone()) {
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
		if (getConsumesInputs()) {
			consumeInputs();
		}
	}
	
	@Override
	public void refreshActivity() {
		canProcessInputs = canProcessInputs();
	}
	
	@Override
	public void refreshActivityOnProduction() {
		canProcessInputs = canProcessInputs();
	}
	
	@Override
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
		BasicRecipe recipe = recipeInfo.getRecipe();
		baseProcessTime = recipe.getBaseProcessTime(defaultProcessTime);
		baseProcessPower = recipe.getBaseProcessPower(defaultProcessPower);
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
		return canProcessInputs && (!getConsumesInputs() || hasConsumed) && hasSufficientEnergy();
	}
	
	public boolean hasConsumed() {
		if (!getConsumesInputs()) {
			return false;
		}
		
		if (world.isRemote) {
			return hasConsumed;
		}
		for (ItemStack stack : consumedStacks) {
			if (!stack.isEmpty()) {
				return true;
			}
		}
		for (Tank tank : consumedTanks) {
			if (!tank.isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean canProcessInputs() {
		boolean validRecipe = setRecipeStats();
		if (hasConsumed && !validRecipe) {
			for (int i = 0; i < getItemInputSize(); ++i) {
				getItemInputs(true).set(i, ItemStack.EMPTY);
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
	
	public boolean canProduceProducts() {
		List<ItemStack> stacks = getInventoryStacks();
		for (int j = 0; j < getItemOutputSize(); ++j) {
			if (getItemOutputSetting(j + getItemInputSize()) == ItemOutputSetting.VOID) {
				stacks.set(j + getItemInputSize(), ItemStack.EMPTY);
				continue;
			}
			IItemIngredient itemProduct = getItemProducts().get(j);
			if (itemProduct.getMaxStackSize(0) <= 0) {
				continue;
			}
			if (itemProduct.getStack() == null || itemProduct.getStack().isEmpty()) {
				return false;
			}
			else if (!stacks.get(j + getItemInputSize()).isEmpty()) {
				if (!stacks.get(j + getItemInputSize()).isItemEqual(itemProduct.getStack())) {
					return false;
				}
				else if (getItemOutputSetting(j + getItemInputSize()) == ItemOutputSetting.DEFAULT && stacks.get(j + getItemInputSize()).getCount() + itemProduct.getMaxStackSize(0) > stacks.get(j + getItemInputSize()).getMaxStackSize()) {
					return false;
				}
			}
		}
		
		List<Tank> tanks = getTanks();
		for (int j = 0; j < getFluidOutputSize(); ++j) {
			if (getTankOutputSetting(j + getFluidInputSize()) == TankOutputSetting.VOID) {
				clearTank(j + getFluidInputSize());
				continue;
			}
			IFluidIngredient fluidProduct = getFluidProducts().get(j);
			if (fluidProduct.getMaxStackSize(0) <= 0) {
				continue;
			}
			if (fluidProduct.getStack() == null) {
				return false;
			}
			else if (!tanks.get(j + getFluidInputSize()).isEmpty()) {
				if (!tanks.get(j + getFluidInputSize()).getFluid().isFluidEqual(fluidProduct.getStack())) {
					return false;
				}
				else if (getTankOutputSetting(j + getFluidInputSize()) == TankOutputSetting.DEFAULT && tanks.get(j + getFluidInputSize()).getFluidAmount() + fluidProduct.getMaxStackSize(0) > tanks.get(j + getFluidInputSize()).getCapacity()) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void consumeInputs() {
		if (hasConsumed || recipeInfo == null) {
			return;
		}
		
		IntList itemInputOrder = recipeInfo.getItemInputOrder();
		if (itemInputOrder == AbstractRecipeHandler.INVALID) {
			return;
		}
		
		IntList fluidInputOrder = recipeInfo.getFluidInputOrder();
		if (fluidInputOrder == AbstractRecipeHandler.INVALID) {
			return;
		}
		
		if (getConsumesInputs()) {
			for (int i = 0; i < getItemInputSize(); ++i) {
				if (!consumedStacks.get(i).isEmpty()) {
					consumedStacks.set(i, ItemStack.EMPTY);
				}
			}
			for (Tank tank : consumedTanks) {
				if (!tank.isEmpty()) {
					tank.setFluidStored(null);
				}
			}
		}
		
		List<ItemStack> stacks = getInventoryStacks();
		for (int i = 0; i < getItemInputSize(); ++i) {
			int itemIngredientStackSize = getItemIngredients().get(itemInputOrder.get(i)).getMaxStackSize(recipeInfo.getItemIngredientNumbers().get(i));
			if (itemIngredientStackSize > 0) {
				if (getConsumesInputs()) {
					consumedStacks.set(i, new ItemStack(stacks.get(i).getItem(), itemIngredientStackSize, StackHelper.getMetadata(stacks.get(i))));
				}
				stacks.get(i).shrink(itemIngredientStackSize);
			}
			if (stacks.get(i).getCount() <= 0) {
				stacks.set(i, ItemStack.EMPTY);
			}
		}
		
		List<Tank> tanks = getTanks();
		for (int i = 0; i < getFluidInputSize(); ++i) {
			Tank tank = tanks.get(i);
			int fluidIngredientStackSize = getFluidIngredients().get(fluidInputOrder.get(i)).getMaxStackSize(recipeInfo.getFluidIngredientNumbers().get(i));
			if (fluidIngredientStackSize > 0) {
				if (getConsumesInputs()) {
					consumedTanks.get(i).setFluidStored(new FluidStack(tank.getFluid(), fluidIngredientStackSize));
				}
				tank.changeFluidAmount(-fluidIngredientStackSize);
			}
			if (tank.getFluidAmount() <= 0) {
				tank.setFluidStored(null);
			}
		}
		
		if (getConsumesInputs()) {
			hasConsumed = true;
		}
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
			for (int i = 0; i < getFluidInputSize(); ++i) {
				if (getVoidUnusableFluidInput(i)) {
					tanks.get(i).setFluidStored(null);
				}
			}
		}
	}
	
	public void produceProducts() {
		if (getConsumesInputs()) {
			for (int i = 0; i < getItemInputSize(); ++i) {
				consumedStacks.set(i, ItemStack.EMPTY);
			}
			for (int i = 0; i < getFluidInputSize(); ++i) {
				consumedTanks.get(i).setFluidStored(null);
			}
		}
		
		if ((getConsumesInputs() && !hasConsumed) || recipeInfo == null) {
			return;
		}
		
		if (!getConsumesInputs()) {
			consumeInputs();
		}
		
		List<ItemStack> stacks = getInventoryStacks();
		for (int j = 0; j < getItemOutputSize(); ++j) {
			if (getItemOutputSetting(j + getItemInputSize()) == ItemOutputSetting.VOID) {
				stacks.set(j + getItemInputSize(), ItemStack.EMPTY);
				continue;
			}
			IItemIngredient itemProduct = getItemProducts().get(j);
			ItemStack nextStack = itemProduct.getNextStack(0);
			if (itemProduct.getMaxStackSize(0) <= 0) {
				continue;
			}
			if (stacks.get(j + getItemInputSize()).isEmpty()) {
				stacks.set(j + getItemInputSize(), nextStack);
			}
			else if (stacks.get(j + getItemInputSize()).isItemEqual(itemProduct.getStack())) {
				int count = Math.min(getInventoryStackLimit(), stacks.get(j + getItemInputSize()).getCount() + nextStack.getCount());
				stacks.get(j + getItemInputSize()).setCount(count);
			}
		}
		
		List<Tank> tanks = getTanks();
		for (int j = 0; j < getFluidOutputSize(); ++j) {
			if (getTankOutputSetting(j + getFluidInputSize()) == TankOutputSetting.VOID) {
				clearTank(j + getFluidInputSize());
				continue;
			}
			IFluidIngredient fluidProduct = getFluidProducts().get(j);
			FluidStack nextStack = fluidProduct.getNextStack(0);
			if (fluidProduct.getMaxStackSize(0) <= 0) {
				continue;
			}
			if (tanks.get(j + getFluidInputSize()).isEmpty()) {
				tanks.get(j + getFluidInputSize()).setFluidStored(nextStack);
			}
			else if (tanks.get(j + getFluidInputSize()).getFluid().isFluidEqual(fluidProduct.getStack())) {
				tanks.get(j + getFluidInputSize()).changeFluidAmount(nextStack.amount);
			}
		}
		
		if (getConsumesInputs()) {
			hasConsumed = false;
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
		return consumed ? consumedStacks : getInventoryStacks().subList(0, getItemInputSize());
	}
	
	@Override
	public List<Tank> getFluidInputs(boolean consumed) {
		return consumed ? consumedTanks : getTanks().subList(0, getFluidInputSize());
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
			if (slot < getItemInputSize()) {
				refreshRecipe();
				refreshActivity();
			}
			else if (slot < getItemInputSize() + getItemOutputSize()) {
				refreshActivity();
			}
		}
		return stack;
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		super.setInventorySlotContents(slot, stack);
		if (!world.isRemote) {
			if (slot < getItemInputSize()) {
				refreshRecipe();
				refreshActivity();
			}
			else if (slot < getItemInputSize() + getItemOutputSize()) {
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
		if (slot >= getItemInputSize() && slot < getItemInputSize() + getItemOutputSize()) {
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
