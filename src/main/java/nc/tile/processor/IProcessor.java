package nc.tile.processor;

import it.unimi.dsi.fastutil.ints.IntList;
import nc.config.NCConfig;
import nc.network.tile.processor.ProcessorUpdatePacket;
import nc.recipe.*;
import nc.recipe.ingredient.*;
import nc.tile.ITileGui;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.*;
import nc.tile.internal.fluid.Tank.TankInfo;
import nc.tile.internal.inventory.ItemOutputSetting;
import nc.tile.inventory.ITileInventory;
import nc.tile.processor.info.ProcessorContainerInfo;
import nc.util.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.*;
import java.util.*;

public interface IProcessor<TILE extends TileEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorContainerInfo<TILE, PACKET, INFO>> extends ITickable, ITileInventory, ITileFluid, ITileGui<TILE, PACKET, INFO> {
	
	BasicRecipeHandler getRecipeHandler();
	
	RecipeInfo<BasicRecipe> getRecipeInfo();
	
	void setRecipeInfo(RecipeInfo<BasicRecipe> recipeInfo);
	
	default boolean setRecipeStats() {
		RecipeInfo<BasicRecipe> recipeInfo = getRecipeInfo();
		setRecipeStats(recipeInfo == null ? null : recipeInfo.recipe);
		return recipeInfo != null;
	}
	
	default void setRecipeStats(@Nullable BasicRecipe recipe) {
		INFO info = getContainerInfo();
		if (recipe == null) {
			setBaseProcessTime(info.defaultProcessTime);
			setBaseProcessPower(info.defaultProcessPower);
		}
		else {
			setBaseProcessTime(recipe.getBaseProcessTime(info.defaultProcessTime));
			setBaseProcessPower(recipe.getBaseProcessPower(info.defaultProcessPower));
		}
	}
	
	@Nonnull
	NonNullList<ItemStack> getConsumedStacks();
	
	@Nonnull
	List<Tank> getConsumedTanks();
	
	default List<ItemStack> getItemInputs(boolean consumed) {
		return consumed ? getConsumedStacks() : getInventoryStacks().subList(0, getContainerInfo().itemInputSize);
	}
	
	default List<Tank> getFluidInputs(boolean consumed) {
		return consumed ? getConsumedTanks() : getTanks().subList(0, getContainerInfo().fluidInputSize);
	}
	
	default List<ItemStack> getItemOutputs() {
		int[] slots = getContainerInfo().itemOutputSlots;
		return slots.length == 0 ? Collections.emptyList() : getInventoryStacks().subList(slots[0], slots[0] + slots.length);
	}
	
	default List<Tank> getFluidOutputs() {
		int[] tanks = getContainerInfo().fluidOutputTanks;
		return tanks.length == 0 ? Collections.emptyList() : getTanks().subList(tanks[0], tanks[0] + tanks.length);
	}
	
	default List<IItemIngredient> getItemIngredients() {
		return getRecipeInfo().recipe.getItemIngredients();
	}
	
	default List<IFluidIngredient> getFluidIngredients() {
		return getRecipeInfo().recipe.getFluidIngredients();
	}
	
	default List<IItemIngredient> getItemProducts() {
		return getRecipeInfo().recipe.getItemProducts();
	}
	
	default List<IFluidIngredient> getFluidProducts() {
		return getRecipeInfo().recipe.getFluidProducts();
	}
	
	default long getEnergyCapacity() {
		return getContainerInfo().getEnergyCapacity(getSpeedMultiplier(), getPowerMultiplier());
	}
	
	double getBaseProcessTime();
	
	void setBaseProcessTime(double baseProcessTime);
	
	double getBaseProcessPower();
	
	void setBaseProcessPower(double baseProcessPower);
	
	double getCurrentTime();
	
	void setCurrentTime(double time);
	
	double getResetTime();
	
	void setResetTime(double resetTime);
	
	boolean getIsProcessing();
	
	void setIsProcessing(boolean isProcessing);
	
	boolean getCanProcessInputs();
	
	void setCanProcessInputs(boolean canProcessInputs);
	
	boolean getHasConsumed();
	
	void setHasConsumed(boolean hasConsumed);
	
	double getSpeedMultiplier();
	
	double getPowerMultiplier();
	
	default long getProcessTime() {
		return Math.max(1, (long) Math.ceil(getBaseProcessTime() / getSpeedMultiplier()));
	}
	
	default long getProcessPower() {
		return (long) Math.ceil(getBaseProcessPower() * getPowerMultiplier());
	}
	
	default long getProcessEnergy() {
		return (long) (Math.max(1D, Math.ceil(getBaseProcessTime() / getSpeedMultiplier())) * Math.ceil(getBaseProcessPower() * getPowerMultiplier()));
	}
	
	default boolean isProcessing() {
		return readyToProcess() && !isHalted();
	}
	
	default boolean isHalted() {
		return false;
	}
	
	default boolean readyToProcess() {
		return getCanProcessInputs() && (!getContainerInfo().consumesInputs || getHasConsumed());
	}
	
	default boolean canProcessInputs() {
		boolean validRecipe = setRecipeStats();
		if (getHasConsumed() && !validRecipe) {
			int itemInputSize = getContainerInfo().itemInputSize;
			List<ItemStack> itemInputs = getItemInputs(true);
			for (int i = 0; i < itemInputSize; ++i) {
				itemInputs.set(i, ItemStack.EMPTY);
			}
			for (Tank tank : getFluidInputs(true)) {
				tank.setFluidStored(null);
			}
			setHasConsumed(false);
		}
		
		boolean canProcess = validRecipe && canProduceProducts();
		if (!canProcess) {
			setCurrentTime(MathHelper.clamp(getCurrentTime(), 0D, getBaseProcessTime() - 1D));
		}
		return canProcess;
	}
	
	default void process() {
		setCurrentTime(getCurrentTime() + getSpeedMultiplier());
		while (getCurrentTime() >= getBaseProcessTime()) {
			finishProcess();
		}
	}
	
	default void finishProcess() {
		double oldProcessTime = getBaseProcessTime();
		produceProducts();
		refreshRecipe();
		double newTime = Math.max(0D, getCurrentTime() - oldProcessTime);
		setCurrentTime(newTime);
		setResetTime(newTime);
		refreshActivityOnProduction();
		if (!getCanProcessInputs()) {
			setCurrentTime(0D);
			setResetTime(0D);
			int fluidInputSize = getContainerInfo().fluidInputSize;
			List<Tank> tanks = getTanks();
			for (int i = 0; i < fluidInputSize; ++i) {
				if (getVoidUnusableFluidInput(i)) {
					tanks.get(i).setFluidStored(null);
				}
			}
		}
	}
	
	default boolean hasConsumed() {
		if (!getContainerInfo().consumesInputs) {
			return false;
		}
		
		if (getTileWorld().isRemote) {
			return getHasConsumed();
		}
		
		for (ItemStack stack : getConsumedStacks()) {
			if (!stack.isEmpty()) {
				return true;
			}
		}
		for (Tank tank : getConsumedTanks()) {
			if (!tank.isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	default boolean canProduceProducts() {
		INFO info = getContainerInfo();
		int itemOutputSize = info.itemOutputSize;
		
		List<ItemStack> stacks = getInventoryStacks();
		for (int i = 0; i < itemOutputSize; ++i) {
			int slot = info.itemOutputSlots[i];
			ItemOutputSetting outputSetting = getItemOutputSetting(slot);
			
			if (outputSetting == ItemOutputSetting.VOID) {
				stacks.set(slot, ItemStack.EMPTY);
				continue;
			}
			
			IItemIngredient product = getItemProducts().get(i);
			int productMaxStackSize = product.getMaxStackSize(0);
			ItemStack productStack = product.getStack();
			
			if (productMaxStackSize <= 0) {
				continue;
			}
			if (productStack == null || productStack.isEmpty()) {
				return false;
			}
			else {
				ItemStack stack = stacks.get(slot);
				if (!stack.isEmpty()) {
					if (!stack.isItemEqual(productStack)) {
						return false;
					}
					else if (outputSetting == ItemOutputSetting.DEFAULT && stack.getCount() + productMaxStackSize > getItemProductCapacity(slot, stack)) {
						return false;
					}
				}
			}
		}
		
		int fluidOutputSize = info.fluidOutputSize;
		
		List<Tank> tanks = getTanks();
		for (int i = 0; i < fluidOutputSize; ++i) {
			int tankIndex = info.fluidOutputTanks[i];
			TankOutputSetting outputSetting = getTankOutputSetting(tankIndex);
			
			if (outputSetting == TankOutputSetting.VOID) {
				clearTank(tankIndex);
				continue;
			}
			
			IFluidIngredient product = getFluidProducts().get(i);
			int productMaxStackSize = product.getMaxStackSize(0);
			FluidStack productStack = product.getStack();
			
			if (productMaxStackSize <= 0) {
				continue;
			}
			if (productStack == null) {
				return false;
			}
			else {
				Tank tank = tanks.get(tankIndex);
				if (!tank.isEmpty()) {
					if (!tank.getFluid().isFluidEqual(productStack)) {
						return false;
					}
					else if (outputSetting == TankOutputSetting.DEFAULT && tank.getFluidAmount() + productMaxStackSize > getFluidProductCapacity(tank, productStack)) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	default int getItemProductCapacity(int slot, ItemStack stack) {
		return stack.getMaxStackSize();
	}
	
	default int getFluidProductCapacity(Tank tank, FluidStack stack) {
		return tank.getCapacity();
	}
	
	default void consumeInputs() {
		RecipeInfo<BasicRecipe> recipeInfo;
		if (getHasConsumed() || (recipeInfo = getRecipeInfo()) == null) {
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
		
		INFO info = getContainerInfo();
		boolean consumesInputs = info.consumesInputs;
		int itemInputSize = info.itemInputSize;
		int fluidInputSize = info.fluidInputSize;
		
		NonNullList<ItemStack> consumedStacks = getConsumedStacks();
		List<Tank> consumedTanks = getConsumedTanks();
		
		if (consumesInputs) {
			for (int i = 0; i < itemInputSize; ++i) {
				consumedStacks.set(i, ItemStack.EMPTY);
			}
			for (Tank tank : consumedTanks) {
				tank.setFluidStored(null);
			}
		}
		
		List<ItemStack> stacks = getInventoryStacks();
		for (int i = 0; i < itemInputSize; ++i) {
			int slot = info.itemInputSlots[i];
			int itemIngredientStackSize = getItemIngredients().get(itemInputOrder.get(i)).getMaxStackSize(recipeInfo.getItemIngredientNumbers().get(i));
			ItemStack stack = stacks.get(slot);
			
			if (itemIngredientStackSize > 0) {
				if (consumesInputs) {
					consumedStacks.set(i, new ItemStack(stack.getItem(), itemIngredientStackSize, StackHelper.getMetadata(stack)));
				}
				stack.shrink(itemIngredientStackSize);
			}
			
			if (stack.getCount() <= 0) {
				stacks.set(slot, ItemStack.EMPTY);
			}
		}
		
		List<Tank> tanks = getTanks();
		for (int i = 0; i < fluidInputSize; ++i) {
			Tank tank = tanks.get(i);
			int fluidIngredientStackSize = getFluidIngredients().get(fluidInputOrder.get(i)).getMaxStackSize(recipeInfo.getFluidIngredientNumbers().get(i));
			if (fluidIngredientStackSize > 0) {
				if (consumesInputs) {
					consumedTanks.get(i).setFluidStored(new FluidStack(tank.getFluid(), fluidIngredientStackSize));
				}
				tank.changeFluidAmount(-fluidIngredientStackSize);
			}
			if (tank.getFluidAmount() <= 0) {
				tank.setFluidStored(null);
			}
		}
		
		if (consumesInputs) {
			setHasConsumed(true);
		}
	}
	
	default void produceProducts() {
		INFO info = getContainerInfo();
		boolean consumesInputs = info.consumesInputs;
		int itemInputSize = info.itemInputSize;
		int fluidInputSize = info.fluidInputSize;
		
		if (consumesInputs) {
			NonNullList<ItemStack> consumedStacks = getConsumedStacks();
			for (int i = 0; i < itemInputSize; ++i) {
				consumedStacks.set(i, ItemStack.EMPTY);
			}
			for (Tank tank : getConsumedTanks()) {
				tank.setFluidStored(null);
			}
		}
		
		if ((consumesInputs && !getHasConsumed()) || getRecipeInfo() == null) {
			return;
		}
		
		if (!consumesInputs) {
			consumeInputs();
		}
		
		int itemOutputSize = info.itemOutputSize;
		
		List<ItemStack> stacks = getInventoryStacks();
		for (int i = 0; i < itemOutputSize; ++i) {
			int slot = info.itemOutputSlots[i];
			if (getItemOutputSetting(slot) == ItemOutputSetting.VOID) {
				stacks.set(slot, ItemStack.EMPTY);
				continue;
			}
			
			IItemIngredient product = getItemProducts().get(i);
			
			if (product.getMaxStackSize(0) <= 0) {
				continue;
			}
			
			ItemStack currentStack = stacks.get(slot);
			ItemStack nextStack = product.getNextStack(0);
			
			if (currentStack.isEmpty()) {
				stacks.set(slot, nextStack);
			}
			else if (currentStack.isItemEqual(product.getStack())) {
				int count = Math.min(getInventoryStackLimit(), currentStack.getCount() + nextStack.getCount());
				currentStack.setCount(count);
			}
		}
		
		int fluidOutputSize = info.fluidOutputSize;
		
		List<Tank> tanks = getTanks();
		for (int i = 0; i < fluidOutputSize; ++i) {
			int tankIndex = info.fluidOutputTanks[i];
			if (getTankOutputSetting(tankIndex) == TankOutputSetting.VOID) {
				clearTank(tankIndex);
				continue;
			}
			
			IFluidIngredient product = getFluidProducts().get(i);
			
			if (product.getMaxStackSize(0) <= 0) {
				continue;
			}
			
			Tank tank = tanks.get(tankIndex);
			FluidStack nextStack = product.getNextStack(0);
			
			if (tank.isEmpty()) {
				tank.setFluidStored(nextStack);
			}
			else if (tank.getFluid().isFluidEqual(product.getStack())) {
				tank.changeFluidAmount(nextStack.amount);
			}
		}
		
		if (consumesInputs) {
			setHasConsumed(false);
		}
		
		autoPush();
	}
	
	// ITickable
	
	default void onTick() {
		boolean wasProcessing = getIsProcessing();
		setIsProcessing(isProcessing());
		boolean shouldUpdate = false;
		
		if (getIsProcessing()) {
			process();
		}
		else {
			getRadiationSource().setRadiationLevel(0D);
			if (getCurrentTime() > 0D) {
				if (getContainerInfo().losesProgress && !isHalted()) {
					loseProgress();
				}
				else if (!getCanProcessInputs()) {
					setCurrentTime(0D);
					setResetTime(0D);
				}
			}
			
			if (!wasProcessing) {
				shouldUpdate |= autoPush();
			}
		}
		
		boolean isProcessing = getIsProcessing();
		if (wasProcessing == isProcessing) {
			sendTileUpdatePacketToListeners();
		}
		else {
			shouldUpdate = true;
			setActivity(isProcessing);
			sendTileUpdatePacketToAll();
		}
		
		if (shouldUpdate) {
			markDirty();
		}
	}
	
	default void loseProgress() {
		double newTime = MathHelper.clamp(getCurrentTime() - 1.5D * getSpeedMultiplier(), 0D, getBaseProcessTime());
		setCurrentTime(newTime);
		if (newTime < getResetTime()) {
			setResetTime(newTime);
		}
	}
	
	default boolean autoPush() {
		HandlerPair[] adjacentHandlers = getAdjacentHandlers();
		if (adjacentHandlers == null) {
			return false;
		}
		
		List<EnumFacing> dirs = new ArrayList<>();
		for (int i = 0; i < 6; ++i) {
			if (adjacentHandlers[i] != null) {
				dirs.add(EnumFacing.VALUES[i]);
			}
		}
		
		return autoPushInternal(adjacentHandlers, getInventoryStacks(), getTanks(), dirs, dirs.size(), (int) getTileWorld().getTotalWorldTime());
	}
	
	default boolean autoPushInternal(HandlerPair[] adjacentHandlers, NonNullList<ItemStack> stacks, List<Tank> tanks, List<EnumFacing> dirs, int dirCount, int indexOffset) {
		INFO info = getContainerInfo();
		boolean pushed = false;
		
		for (int i = 0; i < info.itemInputSize; ++i) {
			pushed |= tryPushSlot(adjacentHandlers, stacks, info.itemInputSlots[i], dirs, dirCount, indexOffset);
		}
		for (int i = 0; i < info.fluidInputSize; ++i) {
			pushed |= tryPushTank(adjacentHandlers, tanks, info.fluidInputTanks[i], dirs, dirCount, indexOffset);
		}
		for (int i = 0; i < info.itemOutputSize; ++i) {
			pushed |= tryPushSlot(adjacentHandlers, stacks, info.itemOutputSlots[i], dirs, dirCount, indexOffset);
		}
		for (int i = 0; i < info.fluidOutputSize; ++i) {
			pushed |= tryPushTank(adjacentHandlers, tanks, info.fluidOutputTanks[i], dirs, dirCount, indexOffset);
		}
		
		return pushed;
	}
	
	default void onBlockNeighborChanged(IBlockState state, World world, BlockPos pos, BlockPos fromPos) {
		ITileGui.super.onBlockNeighborChanged(state, world, pos, fromPos);
		updateAdjacentHandlers();
	}
	
	class HandlerPair {
		
		public final IItemHandler itemHandler;
		public final IFluidHandler fluidHandler;
		
		public HandlerPair(IItemHandler itemHandler, IFluidHandler fluidHandler) {
			this.itemHandler = itemHandler;
			this.fluidHandler = fluidHandler;
		}
		
	}
	
	default @Nullable HandlerPair[] getAdjacentHandlers() {
		return null;
	}
	
	default void updateAdjacentHandlers() {}
	
	default void refreshAll() {
		refreshDirty();
		setIsProcessing(isProcessing());
		setHasConsumed(hasConsumed());
	}
	
	default void refreshDirty() {
		refreshRecipe();
		refreshActivity();
	}
	
	default void refreshRecipe() {
		boolean hasConsumed = getHasConsumed();
		setRecipeInfo(getRecipeHandler().getRecipeInfoFromInputs(getItemInputs(hasConsumed), getFluidInputs(hasConsumed)));
		if (getContainerInfo().consumesInputs) {
			consumeInputs();
		}
	}
	
	default void refreshActivity() {
		setCanProcessInputs(canProcessInputs());
	}
	
	default void refreshActivityOnProduction() {
		setCanProcessInputs(canProcessInputs());
	}
	
	// ITileInventory
	
	@Override
	default ItemStack decrStackSize(int slot, int amount) {
		ItemStack stack = ITileInventory.super.decrStackSize(slot, amount);
		if (!getTileWorld().isRemote) {
			INFO info = getContainerInfo();
			if (slot < info.itemInputSize) {
				refreshRecipe();
				refreshActivity();
			}
			else if (slot < info.itemInputSize + info.itemOutputSize) {
				refreshActivity();
			}
		}
		return stack;
	}
	
	@Override
	default void setInventorySlotContents(int slot, ItemStack stack) {
		ITileInventory.super.setInventorySlotContents(slot, stack);
		if (!getTileWorld().isRemote) {
			INFO info = getContainerInfo();
			if (slot < info.itemInputSize) {
				refreshRecipe();
				refreshActivity();
			}
			else if (slot < info.itemInputSize + info.itemOutputSize) {
				refreshActivity();
			}
		}
	}
	
	@Override
	default boolean isItemValidForSlot(int slot, ItemStack stack) {
		INFO info = getContainerInfo();
		if (stack.isEmpty() || (slot >= info.itemInputSize && slot < info.itemInputSize + info.itemOutputSize)) {
			return false;
		}
		
		if (NCConfig.smart_processor_input) {
			return getRecipeHandler().isValidItemInput(stack, slot, getItemInputs(false), getFluidInputs(false), getRecipeInfo());
		}
		else {
			return getRecipeHandler().isValidItemInput(stack, slot);
		}
	}
	
	@Override
	default boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
		return ITileInventory.super.canInsertItem(slot, stack, side) && isItemValidForSlot(slot, stack);
	}
	
	@Override
	default void clearAllSlots() {
		ITileInventory.super.clearAllSlots();
		@Nonnull NonNullList<ItemStack> consumedStacks = getConsumedStacks();
		Collections.fill(consumedStacks, ItemStack.EMPTY);
		refreshAll();
	}
	
	@Override
	default NBTTagCompound writeInventory(NBTTagCompound nbt) {
		NBTHelper.writeAllItems(nbt, getInventoryStacks(), getConsumedStacks());
		return nbt;
	}
	
	@Override
	default void readInventory(NBTTagCompound nbt) {
		if (nbt.hasKey("hasConsumed")) {
			NBTHelper.readAllItems(nbt, getInventoryStacks(), getConsumedStacks());
		}
		else {
			ITileInventory.super.readInventory(nbt);
		}
	}
	
	// ITileFluid
	
	@Override
	default boolean isFluidValidForTank(int tankNumber, FluidStack stack) {
		INFO info = getContainerInfo();
		if (stack == null || stack.amount <= 0 || (tankNumber >= info.fluidInputSize && tankNumber < info.fluidInputSize + info.fluidOutputSize)) {
			return false;
		}
		
		if (NCConfig.smart_processor_input) {
			return getRecipeHandler().isValidFluidInput(stack, tankNumber, getFluidInputs(false), getItemInputs(false), getRecipeInfo());
		}
		else {
			return getRecipeHandler().isValidFluidInput(stack, tankNumber);
		}
	}
	
	@Override
	default void clearAllTanks() {
		ITileFluid.super.clearAllTanks();
		for (Tank tank : getConsumedTanks()) {
			tank.setFluidStored(null);
		}
		refreshAll();
	}
	
	@Override
	default NBTTagCompound writeTanks(NBTTagCompound nbt) {
		ITileFluid.super.writeTanks(nbt);
		@Nonnull List<Tank> consumedTanks = getConsumedTanks();
		for (int i = 0; i < consumedTanks.size(); ++i) {
			consumedTanks.get(i).writeToNBT(nbt, "consumedTanks" + i);
		}
		return nbt;
	}
	
	@Override
	default void readTanks(NBTTagCompound nbt) {
		ITileFluid.super.readTanks(nbt);
		@Nonnull List<Tank> consumedTanks = getConsumedTanks();
		for (int i = 0; i < consumedTanks.size(); ++i) {
			consumedTanks.get(i).readFromNBT(nbt, "consumedTanks" + i);
		}
	}
	
	// IGui
	
	@Override
	default void onTileUpdatePacket(PACKET message) {
		setIsProcessing(message.isProcessing);
		setCurrentTime(message.time);
		setBaseProcessTime(message.baseProcessTime);
		TankInfo.readInfoList(message.tankInfos, getTanks());
	}
	
	// NBT
	
	default NBTTagCompound writeProcessorNBT(NBTTagCompound nbt) {
		nbt.setDouble("time", getCurrentTime());
		nbt.setDouble("resetTime", getResetTime());
		nbt.setBoolean("isProcessing", getIsProcessing());
		nbt.setBoolean("canProcessInputs", getCanProcessInputs());
		nbt.setBoolean("hasConsumed", getHasConsumed());
		return nbt;
	}
	
	default void readProcessorNBT(NBTTagCompound nbt) {
		setCurrentTime(nbt.getDouble("time"));
		setResetTime(nbt.getDouble("resetTime"));
		setIsProcessing(nbt.getBoolean("isProcessing"));
		setCanProcessInputs(nbt.getBoolean("canProcessInputs"));
		setHasConsumed(nbt.getBoolean("hasConsumed"));
	}
}
