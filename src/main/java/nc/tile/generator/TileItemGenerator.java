package nc.tile.generator;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import nc.ModCheck;
import nc.config.NCConfig;
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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

public abstract class TileItemGenerator extends TileEnergySidedInventory implements IItemGenerator, IInterfaceable, IBufferable, IGui {

	public final int[] slots;
	
	public final int defaultProcessTime, defaultProcessPower;
	public double baseProcessTime = 1, baseProcessPower = 0, baseProcessRadiation = 0;
	public double processPower = 0;
	public double speedMultiplier = 1;
	public final int itemInputSize, itemOutputSize, otherSlotsSize;
	
	public double time;
	public boolean isProcessing, hasConsumed, canProcessInputs;
	
	public final NCRecipes.Type recipeType;
	protected ProcessorRecipe recipe;
	
	public TileItemGenerator(String name, int itemInSize, int itemOutSize, int otherSize, int capacity, @Nonnull NCRecipes.Type recipeType) {
		super(name, 2*itemInSize + itemOutSize + otherSize, capacity, ITileEnergy.energyConnectionAll(EnergyConnection.OUT));
		itemInputSize = itemInSize;
		itemOutputSize = itemOutSize;
		
		otherSlotsSize = otherSize;
		
		defaultProcessTime = 1;
		defaultProcessPower = 0;
		
		this.recipeType = recipeType;
		
		slots = ArrayHelper.increasingArray(itemInSize + itemOutSize);
	}
	
	@Override
	public void onAdded() {
		super.onAdded();
		if (!world.isRemote) {
			isProcessing = isProcessing();
			hasConsumed = hasConsumed();
		}
	}
	
	@Override
	public void update() {
		super.update();
		updateProcessor();
	}
	
	public void updateProcessor() {
		recipe = getRecipeHandler().getRecipeFromInputs(getItemInputs(hasConsumed), new ArrayList<Tank>());
		canProcessInputs = canProcessInputs();
		boolean wasProcessing = isProcessing;
		isProcessing = isProcessing();
		boolean shouldUpdate = false;
		if(!world.isRemote) {
			tickTile();
			if (isProcessing) process();
			else getRadiationSource().setRadiationLevel(0D);
			consumeInputs();
			if (wasProcessing != isProcessing) {
				shouldUpdate = true;
				updateBlockType();
			}
			pushEnergy();
		}
		if (shouldUpdate) markDirty();
	}
	
	public boolean isProcessing() {
		return readyToProcess() && isRedstonePowered();
	}
	
	public boolean readyToProcess() {
		return canProcessInputs && hasConsumed;
	}
	
	public void process() {
		time += speedMultiplier;
		getEnergyStorage().changeEnergyStored((int) processPower);
		getRadiationSource().setRadiationLevel(baseProcessRadiation*speedMultiplier);
		if (time >= baseProcessTime) {
			double oldProcessTime = baseProcessTime;
			produceProducts();
			recipe = getRecipeHandler().getRecipeFromInputs(getItemInputs(hasConsumed), new ArrayList<Tank>());
			setRecipeStats();
			if (recipe == null) time = 0;
			else time = MathHelper.clamp(time - oldProcessTime, 0D, baseProcessTime);
		}
	}
	
	public void updateBlockType() {
		if (ModCheck.ic2Loaded()) removeTileFromENet();
		setState(isProcessing);
		world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
		if (ModCheck.ic2Loaded()) addTileToENet();
	}
	
	// Processing
	
	public boolean hasConsumed() {
		if (world.isRemote) return hasConsumed;
		for (int i = 0; i < itemInputSize; i++) {
			if (!inventoryStacks.get(i + itemInputSize + itemOutputSize).isEmpty()) return true;
		}
		return false;
	}
		
	public boolean canProcessInputs() {
		if (recipe == null) {
			if (hasConsumed) {
				for (int i = 0; i < itemInputSize; i++) getItemInputs(true).set(i, ItemStack.EMPTY);
				hasConsumed = false;
			}
			return false;
		}
		setRecipeStats();
		if (time >= baseProcessTime) return true;
		
		for(int j = 0; j < itemOutputSize; j++) {
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
	
	public abstract void setRecipeStats();
		
	public void consumeInputs() {
		if (hasConsumed || recipe == null) return;
		List<Integer> itemInputOrder = getItemInputOrder();
		if (itemInputOrder == AbstractRecipeHandler.INVALID) return;
		
		for (int i = 0; i < itemInputSize; i++) {
			if (!inventoryStacks.get(i + itemInputSize + itemOutputSize).isEmpty()) {
				inventoryStacks.set(i + itemInputSize + itemOutputSize, ItemStack.EMPTY);
			}
		}
		for (int i = 0; i < itemInputSize; i++) {
			IItemIngredient itemIngredient = getItemIngredients().get(itemInputOrder.get(i));
			if (itemIngredient.getMaxStackSize() > 0) {
				inventoryStacks.set(i + itemInputSize + itemOutputSize, new ItemStack(inventoryStacks.get(i).getItem(), itemIngredient.getMaxStackSize(), inventoryStacks.get(i).getMetadata()));
				inventoryStacks.get(i).shrink(itemIngredient.getMaxStackSize());
			}
			if (inventoryStacks.get(i).getCount() <= 0) inventoryStacks.set(i, ItemStack.EMPTY);
		}
		hasConsumed = true;
	}
	
	public void produceProducts() {
		for (int i = itemInputSize + itemOutputSize; i < 2*itemInputSize + itemOutputSize; i++) inventoryStacks.set(i, ItemStack.EMPTY);
		
		if (!hasConsumed || recipe == null) return;
		
		for (int j = 0; j < itemOutputSize; j++) {
			IItemIngredient itemProduct = getItemProducts().get(j);
			if (itemProduct.getNextStackSize() <= 0) continue;
			if (inventoryStacks.get(j + itemInputSize).isEmpty()) {
				inventoryStacks.set(j + itemInputSize, itemProduct.getNextStack());
			} else if (inventoryStacks.get(j + itemInputSize).isItemEqual(itemProduct.getStack())) {
				inventoryStacks.get(j + itemInputSize).grow(itemProduct.getNextStackSize());
			}
		}
		hasConsumed = false;
	}
	
	@Override
	public ProcessorRecipeHandler getRecipeHandler() {
		return recipeType.getRecipeHandler();
	}
	
	@Override
	public ProcessorRecipe getRecipe() {
		return recipe;
	}
	
	@Override
	public List<ItemStack> getItemInputs(boolean consumed) {
		return consumed ? inventoryStacks.subList(itemInputSize + itemOutputSize, 2*itemInputSize + itemOutputSize) : inventoryStacks.subList(0, itemInputSize);
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
				if (itemIngredients.get(j).matches(getItemInputs(false).get(i), IngredientSorption.INPUT)) {
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
		else if (slot >= itemInputSize && slot < itemInputSize + itemOutputSize) return false;
		return NCConfig.smart_processor_input ? getRecipeHandler().isValidItemInput(stack, inventoryStacks.get(slot), inputItemStacksExcludingSlot(slot)) : getRecipeHandler().isValidItemInput(stack);
	}
	
	public List<ItemStack> inputItemStacksExcludingSlot(int slot) {
		List<ItemStack> inputItemsExcludingSlot = new ArrayList<ItemStack>(getItemInputs(false));
		inputItemsExcludingSlot.remove(slot);
		return inputItemsExcludingSlot;
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
		nbt.setBoolean("hasConsumed", hasConsumed);
		nbt.setBoolean("canProcessInputs", canProcessInputs);
		return nbt;
	}
		
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		time = nbt.getDouble("time");
		isProcessing = nbt.getBoolean("isProcessing");
		hasConsumed = nbt.getBoolean("hasConsumed");
		canProcessInputs = nbt.getBoolean("canProcessInputs");
	}
	
	// Inventory Fields

	@Override
	public int getFieldCount() {
		return 2;
	}

	@Override
	public int getField(int id) {
		switch (id) {
		case 0:
			return (int) time;
		case 1:
			return getEnergyStored();
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
		}
	}
}
