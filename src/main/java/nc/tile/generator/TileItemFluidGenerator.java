package nc.tile.generator;

import java.util.ArrayList;
import java.util.List;

import nc.ModCheck;
import nc.config.NCConfig;
import nc.recipe.IFluidIngredient;
import nc.recipe.IItemIngredient;
import nc.recipe.IRecipeHandler;
import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.recipe.SorptionType;
import nc.tile.IGui;
import nc.tile.dummy.IInterfaceable;
import nc.tile.energyFluid.IBufferable;
import nc.tile.energyFluid.TileEnergyFluidSidedInventory;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.fluid.Tank;
import nc.util.ArrayHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidStack;

public abstract class TileItemFluidGenerator extends TileEnergyFluidSidedInventory implements IItemFluidGenerator, IInterfaceable, IBufferable, IGui {

	public final int[] slots;
	
	public final int defaultProcessTime, defaultProcessPower;
	public double baseProcessTime = 1, baseProcessPower = 0;
	public double processPower = 0;
	public double speedMultiplier = 1;
	public final int itemInputSize, fluidInputSize, itemOutputSize, fluidOutputSize, otherSlotsSize;
	
	public double time;
	public boolean isProcessing, hasConsumed, canProcessInputs;
	
	public final NCRecipes.Type recipeType;
	protected ProcessorRecipe recipe;
	
	public TileItemFluidGenerator(String name, int itemInSize, int fluidInSize, int itemOutSize, int fluidOutSize, int otherSize, List<Integer> fluidCapacity, List<FluidConnection> fluidConnection, List<List<String>> allowedFluids, int capacity, NCRecipes.Type recipeType) {
		super(name, 2*itemInSize + itemOutSize + otherSize, capacity, energyConnectionAll(EnergyConnection.OUT), fluidCapacity, fluidCapacity, fluidCapacity, fluidConnection, allowedFluids);
		itemInputSize = itemInSize;
		fluidInputSize = fluidInSize;
		itemOutputSize = itemOutSize;
		fluidOutputSize = fluidOutSize;
		
		otherSlotsSize = otherSize;
		areTanksShared = fluidInSize > 1;
		
		defaultProcessTime = 1;
		defaultProcessPower = 0;
		
		this.recipeType = recipeType;
		
		slots = ArrayHelper.increasingArray(itemInSize + itemOutSize);
		
		for (int i = 0; i < tanks.size(); i++) {
			if (i < fluidInputSize) tanks.get(i).setStrictlyInput(true);
			else if (i < fluidInputSize + fluidOutputSize) tanks.get(i).setStrictlyOutput(true);
			else {
				tanks.get(i).setStrictlyInput(true);
				tanks.get(i).setStrictlyOutput(true);
			}
		}
	}
	
	public static List<Integer> defaultTankCapacities(int capacity, int inSize, int outSize) {
		List<Integer> tankCapacities = new ArrayList<Integer>();
		for (int i = 0; i < 2*inSize + outSize; i++) tankCapacities.add(capacity);
		return tankCapacities;
	}
	
	public static List<FluidConnection> defaultFluidConnections(int inSize, int outSize) {
		List<FluidConnection> fluidConnections = new ArrayList<FluidConnection>();
		for (int i = 0; i < inSize; i++) fluidConnections.add(FluidConnection.IN);
		for (int i = 0; i < outSize; i++) fluidConnections.add(FluidConnection.OUT);
		for (int i = 0; i < inSize; i++) fluidConnections.add(FluidConnection.NON);
		return fluidConnections;
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
		recipe = getRecipeHandler().getRecipeFromInputs(getItemInputs(hasConsumed), getFluidInputs(hasConsumed));
		canProcessInputs = canProcessInputs();
		boolean wasProcessing = isProcessing;
		isProcessing = isProcessing();
		boolean shouldUpdate = false;
		if(!world.isRemote) {
			tickTile();
			if (isProcessing) process();
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
		if (time >= baseProcessTime) {
			produceProducts();
			recipe = getRecipeHandler().getRecipeFromInputs(getItemInputs(hasConsumed), getFluidInputs(hasConsumed));
			setRecipeStats();
			if (recipe == null) time = 0; else time = MathHelper.clamp(time - baseProcessTime, 0D, baseProcessTime);
			if (emptyUnusableTankInputs) {
				for (int i = 0; i < fluidInputSize; i++) tanks.get(i).setFluid(null);
			}
		}
	}
	
	public void updateBlockType() {
		if (ModCheck.ic2Loaded()) removeTileFromENet();
		setState(isProcessing);
		world.notifyNeighborsOfStateChange(pos, blockType, true);
		if (ModCheck.ic2Loaded()) addTileToENet();
	}
	
	// Processing
	
	public boolean hasConsumed() {
		if (world.isRemote) return hasConsumed;
		for (int i = 0; i < itemInputSize; i++) {
			if (!inventoryStacks.get(i + itemInputSize + itemOutputSize).isEmpty()) return true;
		}
		for (int i = 0; i < fluidInputSize; i++) {
			if (!tanks.get(i + fluidInputSize + fluidOutputSize).isEmpty()) return true;
		}
		return false;
	}
		
	public boolean canProcessInputs() {
		if (recipe == null) return false;
		else if (time >= baseProcessTime) return true;
		
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
		for(int j = 0; j < fluidOutputSize; j++) {
			IFluidIngredient fluidProduct = getFluidProducts().get(j);
			if (fluidProduct.getMaxStackSize() <= 0) continue;
			if (fluidProduct.getStack() == null) return false;
			else if (!tanks.get(j + fluidInputSize).isEmpty()) {
				if (!tanks.get(j + fluidInputSize).getFluid().isFluidEqual(fluidProduct.getStack())) {
					return false;
				} else if (!voidExcessFluidOutputs && tanks.get(j + fluidInputSize).getFluidAmount() + fluidProduct.getMaxStackSize() > tanks.get(j + fluidInputSize).getCapacity()) {
					return false;
				}
			}
		}
		setRecipeStats();
		return true;
	}
	
	public abstract void setRecipeStats();
		
	public void consumeInputs() {
		if (hasConsumed || recipe == null) return;
		List<Integer> itemInputOrder = getItemInputOrder();
		List<Integer> fluidInputOrder = getFluidInputOrder();
		if (itemInputOrder == IRecipeHandler.INVALID || fluidInputOrder == IRecipeHandler.INVALID) return;
		
		for (int i = 0; i < itemInputSize; i++) {
			if (!inventoryStacks.get(i + itemInputSize + itemOutputSize).isEmpty()) {
				inventoryStacks.set(i + itemInputSize + itemOutputSize, ItemStack.EMPTY);
			}
		}
		for (int i = 0; i < fluidInputSize; i++) {
			if (!tanks.get(i + fluidInputSize + fluidOutputSize).isEmpty()) {
				tanks.get(i + fluidInputSize + fluidOutputSize).setFluid(null);
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
		for (int i = 0; i < fluidInputSize; i++) {
			IFluidIngredient fluidIngredient = getFluidIngredients().get(fluidInputOrder.get(i));
			if (fluidIngredient.getMaxStackSize() > 0) {
				tanks.get(i + fluidInputSize + fluidOutputSize).setFluidStored(new FluidStack(tanks.get(i).getFluid(), fluidIngredient.getMaxStackSize()));
				tanks.get(i).changeFluidStored(-fluidIngredient.getMaxStackSize());
			}
			if (tanks.get(i).isEmpty()) tanks.get(i).setFluid(null);
		}
		hasConsumed = true;
	}
		
	public void produceProducts() {
		for (int i = itemInputSize + itemOutputSize; i < 2*itemInputSize + itemOutputSize; i++) inventoryStacks.set(i, ItemStack.EMPTY);
		for (int i = fluidInputSize + fluidOutputSize; i < 2*fluidInputSize + fluidOutputSize; i++) tanks.get(i).setFluid(null);
		
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
		for (int j = 0; j < fluidOutputSize; j++) {
			IFluidIngredient fluidProduct = getFluidProducts().get(j);
			if (fluidProduct.getNextStackSize() <= 0) continue;
			if (tanks.get(j + fluidInputSize).isEmpty()) {
				tanks.get(j + fluidInputSize).setFluidStored(fluidProduct.getNextStack());
			} else if (tanks.get(j + fluidInputSize).getFluid().isFluidEqual(fluidProduct.getStack())) {
				tanks.get(j + fluidInputSize).changeFluidStored(fluidProduct.getNextStackSize());
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
	public List<Tank> getFluidInputs(boolean consumed) {
		return consumed ? tanks.subList(fluidInputSize + fluidOutputSize, 2*fluidInputSize + fluidOutputSize) : tanks.subList(0, fluidInputSize);
	}
	
	@Override
	public List<IItemIngredient> getItemIngredients() {
		return recipe.itemIngredients();
	}
	
	@Override
	public List<IFluidIngredient> getFluidIngredients() {
		return recipe.fluidIngredients();
	}
	
	@Override
	public List<IItemIngredient> getItemProducts() {
		return recipe.itemProducts();
	}
	
	@Override
	public List<IFluidIngredient> getFluidProducts() {
		return recipe.fluidProducts();
	}
	
	@Override
	public List<Integer> getItemInputOrder() {
		List<Integer> itemInputOrder = new ArrayList<Integer>();
		List<IItemIngredient> itemIngredients = recipe.itemIngredients();
		for (int i = 0; i < itemInputSize; i++) {
			int position = -1;
			for (int j = 0; j < itemIngredients.size(); j++) {
				if (itemIngredients.get(j).matches(getItemInputs(false).get(i), SorptionType.INPUT)) {
					position = j;
					break;
				}
			}
			if (position == -1) return IRecipeHandler.INVALID;
			itemInputOrder.add(position);
		}
		return itemInputOrder;
	}
	
	@Override
	public List<Integer> getFluidInputOrder() {
		List<Integer> fluidInputOrder = new ArrayList<Integer>();
		List<IFluidIngredient> fluidIngredients = recipe.fluidIngredients();
		for (int i = 0; i < fluidInputSize; i++) {
			int position = -1;
			for (int j = 0; j < fluidIngredients.size(); j++) {
				if (fluidIngredients.get(j).matches(getFluidInputs(false).get(i), SorptionType.INPUT)) {
					position = j;
					break;
				}
			}
			if (position == -1) return IRecipeHandler.INVALID;
			fluidInputOrder.add(position);
		}
		return fluidInputOrder;
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
	
	// Fluids
	
	@Override
	public boolean canFill(FluidStack resource, int tankNumber) {
		if (tankNumber >= fluidInputSize) return false;
		if (!areTanksShared) return true;
		
		for (int i = 0; i < fluidInputSize; i++) {
			if (tankNumber != i && fluidConnections.get(i).canFill() && tanks.get(i).getFluid() != null) {
				if (tanks.get(i).getFluid().isFluidEqual(resource)) return false;
			}
		}
		return true;
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
