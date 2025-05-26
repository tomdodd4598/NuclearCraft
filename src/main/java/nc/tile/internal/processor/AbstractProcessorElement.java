package nc.tile.internal.processor;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import nc.recipe.*;
import nc.recipe.ingredient.*;
import nc.tile.internal.fluid.*;
import nc.tile.internal.inventory.ItemOutputSetting;
import nc.util.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.*;
import java.util.*;

public abstract class AbstractProcessorElement {
	
	public double time, resetTime;
	public boolean isProcessing, canProcessInputs, hasConsumed;
	
	public double baseProcessTime;
	
	public RecipeInfo<BasicRecipe> recipeInfo = null;
	
	public @Nonnull NonNullList<ItemStack> consumedStacks = null;
	public @Nonnull List<Tank> consumedTanks = null;
	
	public AbstractProcessorElement() {
		this(true);
	}
	
	public AbstractProcessorElement(boolean baseInit) {
		if (baseInit) {
			baseConstructorInit();
		}
	}
	
	public void baseConstructorInit() {
		boolean consumesInputs = getConsumesInputs();
		int inputTankCapacity = getInputTankCapacity();
		
		consumedStacks = NonNullList.withSize(consumesInputs ? getItemInputSize() : 0, ItemStack.EMPTY);
		consumedTanks = new ArrayList<>();
		if (consumesInputs) {
			for (int i = 0, fluidInputSize = getFluidInputSize(); i < fluidInputSize; ++i) {
				consumedTanks.add(new Tank(inputTankCapacity, new ObjectOpenHashSet<>()));
			}
		}
	}
	
	public abstract World getWorld();
	
	public abstract BasicRecipeHandler getRecipeHandler();
	
	public boolean setRecipeStats() {
		setRecipeStats(recipeInfo == null ? null : recipeInfo.recipe);
		return recipeInfo != null;
	}
	
	public abstract void setRecipeStats(@Nullable BasicRecipe recipe);
	
	public int getInventoryStackLimit() {
		return 64;
	}
	
	public int getInputTankCapacity() {
		return 16000;
	}
	
	public abstract @Nonnull NonNullList<ItemStack> getInventoryStacks();
	
	public abstract @Nonnull List<Tank> getTanks();
	
	public abstract boolean getConsumesInputs();
	
	public abstract boolean getLosesProgress();
	
	public abstract int getItemInputSize();
	
	public abstract int getFluidInputSize();
	
	public abstract int getItemOutputSize();
	
	public abstract int getFluidOutputSize();
	
	public abstract int getItemInputSlot(int index);
	
	public abstract int getFluidInputTank(int index);
	
	public abstract int getItemOutputSlot(int index);
	
	public abstract int getFluidOutputTank(int index);
	
	public List<ItemStack> getItemInputs(boolean consumed) {
		return consumed ? consumedStacks : getInventoryStacks().subList(0, getItemInputSize());
	}
	
	public List<Tank> getFluidInputs(boolean consumed) {
		return consumed ? consumedTanks : getTanks().subList(0, getFluidInputSize());
	}
	
	public List<ItemStack> getItemOutputs() {
		int itemOutputSize = getItemOutputSize();
		if (itemOutputSize == 0) {
			return Collections.emptyList();
		}
		else {
			int itemOutputStart = getItemOutputSlot(0);
			return getInventoryStacks().subList(itemOutputStart, itemOutputStart + itemOutputSize);
		}
	}
	
	public List<Tank> getFluidOutputs() {
		int fluidOutputSize = getFluidOutputSize();
		if (fluidOutputSize == 0) {
			return Collections.emptyList();
		}
		else {
			int fluidOutputStart = getFluidOutputTank(0);
			return getTanks().subList(fluidOutputStart, fluidOutputStart + fluidOutputSize);
		}
	}
	
	public List<IItemIngredient> getItemIngredients() {
		return recipeInfo.recipe.getItemIngredients();
	}
	
	public List<IFluidIngredient> getFluidIngredients() {
		return recipeInfo.recipe.getFluidIngredients();
	}
	
	public List<IItemIngredient> getItemProducts() {
		return recipeInfo.recipe.getItemProducts();
	}
	
	public List<IFluidIngredient> getFluidProducts() {
		return recipeInfo.recipe.getFluidProducts();
	}
	
	public abstract double getSpeedMultiplier();
	
	public boolean isProcessing() {
		return readyToProcess() && !isHalted();
	}
	
	public abstract boolean isHalted();
	
	public boolean readyToProcessBase() {
		return canProcessInputs && (!getConsumesInputs() || hasConsumed);
	}
	
	public boolean readyToProcess() {
		return readyToProcessBase();
	}
	
	public boolean canProcessInputs() {
		boolean validRecipe = setRecipeStats();
		if (hasConsumed && !validRecipe) {
			int itemInputSize = getItemInputSize();
			List<ItemStack> itemInputs = getItemInputs(true);
			for (int i = 0; i < itemInputSize; ++i) {
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
	
	public void process() {
		time += getSpeedMultiplier();
		while (time >= baseProcessTime) {
			finishProcess();
		}
	}
	
	public void finishProcess() {
		double oldProcessTime = baseProcessTime;
		produceProducts();
		refreshRecipe();
		double newTime = Math.max(0D, time - oldProcessTime);
		time = resetTime = newTime;
		refreshActivityOnProduction();
		if (!canProcessInputs) {
			time = resetTime = 0D;
			int fluidInputSize = getFluidInputSize();
			List<Tank> tanks = getTanks();
			for (int i = 0; i < fluidInputSize; ++i) {
				if (getVoidUnusableFluidInput(i)) {
					tanks.get(i).setFluidStored(null);
				}
			}
		}
	}
	
	public boolean hasConsumed() {
		if (!getConsumesInputs()) {
			return false;
		}
		
		if (getWorld().isRemote) {
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
	
	public boolean canProduceProducts() {
		int itemOutputSize = getItemOutputSize();
		
		List<ItemStack> stacks = getInventoryStacks();
		for (int i = 0; i < itemOutputSize; ++i) {
			int slot = getItemOutputSlot(i);
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
		
		int fluidOutputSize = getFluidOutputSize();
		
		List<Tank> tanks = getTanks();
		for (int i = 0; i < fluidOutputSize; ++i) {
			int tankIndex = getFluidOutputTank(i);
			Tank tank = tanks.get(tankIndex);
			TankOutputSetting outputSetting = getTankOutputSetting(tankIndex);
			
			if (outputSetting == TankOutputSetting.VOID) {
				tank.setFluidStored(null);
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
	
	public int getItemProductCapacity(int slot, ItemStack stack) {
		return stack.getMaxStackSize();
	}
	
	public int getFluidProductCapacity(Tank tank, FluidStack stack) {
		return tank.getCapacity();
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
		
		boolean consumesInputs = getConsumesInputs();
		int itemInputSize = getItemInputSize();
		int fluidInputSize = getFluidInputSize();
		
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
			int slot = getItemInputSlot(i);
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
			Tank tank = tanks.get(getFluidInputTank(i));
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
			hasConsumed = true;
		}
	}
	
	public void produceProducts() {
		boolean consumesInputs = getConsumesInputs();
		int itemInputSize = getItemInputSize();
		int fluidInputSize = getFluidInputSize();
		
		if (consumesInputs) {
			for (int i = 0; i < itemInputSize; ++i) {
				consumedStacks.set(i, ItemStack.EMPTY);
			}
			for (Tank tank : consumedTanks) {
				tank.setFluidStored(null);
			}
		}
		
		if ((consumesInputs && !hasConsumed) || recipeInfo == null) {
			return;
		}
		
		if (!consumesInputs) {
			consumeInputs();
		}
		
		int itemOutputSize = getItemOutputSize();
		
		List<ItemStack> stacks = getInventoryStacks();
		for (int i = 0; i < itemOutputSize; ++i) {
			int slot = getItemOutputSlot(i);
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
		
		int fluidOutputSize = getFluidOutputSize();
		
		List<Tank> tanks = getTanks();
		for (int i = 0; i < fluidOutputSize; ++i) {
			int tankIndex = getFluidOutputTank(i);
			Tank tank = tanks.get(tankIndex);
			
			if (getTankOutputSetting(tankIndex) == TankOutputSetting.VOID) {
				tank.setFluidStored(null);
				continue;
			}
			
			IFluidIngredient product = getFluidProducts().get(i);
			
			if (product.getMaxStackSize(0) <= 0) {
				continue;
			}
			
			FluidStack nextStack = product.getNextStack(0);
			
			if (tank.isEmpty()) {
				tank.setFluidStored(nextStack);
			}
			else if (tank.getFluid().isFluidEqual(product.getStack())) {
				tank.changeFluidAmount(nextStack.amount);
			}
		}
		
		if (consumesInputs) {
			hasConsumed = false;
		}
	}
	
	public boolean onTick() {
		boolean wasProcessing = isProcessing;
		isProcessing = isProcessing();
		boolean shouldUpdate = false;
		
		if (isProcessing) {
			process();
		}
		else {
			shouldUpdate = onIdle(wasProcessing);
		}
		
		if (wasProcessing == isProcessing) {
			onResumeProcessingState();
		}
		else {
			shouldUpdate = true;
			onChangeProcessingState();
		}
		
		return shouldUpdate;
	}
	
	public boolean onIdle(boolean wasProcessing) {
		if (time > 0D) {
			if (getLosesProgress() && !isHalted()) {
				loseProgress();
			}
			else if (!canProcessInputs) {
				time = resetTime = 0D;
			}
		}
		return false;
	}
	
	public void loseProgress() {
		double newTime = MathHelper.clamp(time - 1.5D * getSpeedMultiplier(), 0D, baseProcessTime);
		time = newTime;
		if (newTime < resetTime) {
			resetTime = newTime;
		}
	}
	
	public abstract void onResumeProcessingState();
	
	public abstract void onChangeProcessingState();
	
	public void refreshAll() {
		refreshRecipe();
		refreshActivity();
		
		isProcessing = isProcessing();
		hasConsumed = hasConsumed();
	}
	
	public final void refreshRecipeBase() {
		BasicRecipeHandler recipeHandler = getRecipeHandler();
		recipeInfo = recipeHandler == null ? null : recipeHandler.getRecipeInfoFromInputs(getItemInputs(hasConsumed), getFluidInputs(hasConsumed));
		if (getConsumesInputs()) {
			consumeInputs();
		}
	}
	
	public void refreshRecipe() {
		refreshRecipeBase();
	}
	
	public final void refreshActivityBase() {
		canProcessInputs = canProcessInputs();
	}
	
	public void refreshActivity() {
		refreshActivityBase();
	}
	
	public void refreshActivityOnProduction() {
		canProcessInputs = canProcessInputs();
	}
	
	public boolean getVoidUnusableFluidInput(int tankNumber) {
		return false;
	}
	
	public ItemOutputSetting getItemOutputSetting(int slot) {
		return ItemOutputSetting.DEFAULT;
	}
	
	public TankOutputSetting getTankOutputSetting(int tankNumber) {
		return TankOutputSetting.DEFAULT;
	}
	
	// NBT
	
	public void writeToNBT(NBTTagCompound nbt, String name) {
		NBTTagCompound tag = new NBTTagCompound();
		
		tag.setDouble("time", time);
		tag.setDouble("resetTime", resetTime);
		tag.setBoolean("isProcessing", isProcessing);
		tag.setBoolean("canProcessInputs", canProcessInputs);
		tag.setBoolean("hasConsumed", hasConsumed);
		
		NBTHelper.writeAllItems(tag, "consumedStacks", consumedStacks);
		for (int i = 0; i < consumedTanks.size(); ++i) {
			consumedTanks.get(i).writeToNBT(tag, "consumedTanks" + i);
		}
		
		nbt.setTag(name, tag);
	}
	
	public void readFromNBT(NBTTagCompound nbt, String name) {
		if (nbt.hasKey(name, 10)) {
			NBTTagCompound tag = nbt.getCompoundTag(name);
			
			time = tag.getDouble("time");
			resetTime = tag.getDouble("resetTime");
			isProcessing = tag.getBoolean("isProcessing");
			canProcessInputs = tag.getBoolean("canProcessInputs");
			hasConsumed = tag.getBoolean("hasConsumed");
			
			NBTHelper.readAllItems(tag, "consumedStacks", consumedStacks);
			for (int i = 0; i < consumedTanks.size(); ++i) {
				consumedTanks.get(i).readFromNBT(tag, "consumedTanks" + i);
			}
		}
	}
}
