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
import nc.recipe.ingredient.IFluidIngredient;
import nc.tile.IGui;
import nc.tile.energy.ITileEnergy;
import nc.tile.energyFluid.TileEnergyFluidSidedInventory;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.TankSorption;
import nc.util.ArrayHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidStack;

public class TileFluidProcessor extends TileEnergyFluidSidedInventory implements IFluidProcessor, IGui<ProcessorUpdatePacket>, IUpgradable {
	
	public final int[] slots;
	
	public final int defaultProcessTime, defaultProcessPower;
	public double baseProcessTime, baseProcessPower, baseProcessRadiation;
	public final int fluidInputSize, fluidOutputSize;
	
	public double time;
	public boolean isProcessing, canProcessInputs;
	
	public final boolean shouldLoseProgress, hasUpgrades;
	public final int guiID;
	
	public final NCRecipes.Type recipeType;
	protected ProcessorRecipe recipe;
	
	protected Set<EntityPlayer> playersToUpdate;
	
	public TileFluidProcessor(String name, int fluidInSize, int fluidOutSize, @Nonnull List<Integer> fluidCapacity, @Nonnull List<TankSorption> tankSorptions, List<List<String>> allowedFluids, int time, int power, boolean shouldLoseProgress, @Nonnull NCRecipes.Type recipeType) {
		this(name, fluidInSize, fluidOutSize, fluidCapacity, tankSorptions, allowedFluids, time, power, shouldLoseProgress, false, recipeType, 1);
	}
	
	public TileFluidProcessor(String name, int fluidInSize, int fluidOutSize, @Nonnull List<Integer> fluidCapacity, @Nonnull List<TankSorption> tankSorptions, List<List<String>> allowedFluids, int time, int power, boolean shouldLoseProgress, @Nonnull NCRecipes.Type recipeType, int guiID) {
		this(name, fluidInSize, fluidOutSize, fluidCapacity, tankSorptions, allowedFluids, time, power, shouldLoseProgress, true, recipeType, guiID);
	}
	
	public TileFluidProcessor(String name, int fluidInSize, int fluidOutSize, @Nonnull List<Integer> fluidCapacity, @Nonnull List<TankSorption> tankSorptions, List<List<String>> allowedFluids, int time, int power, boolean shouldLoseProgress, boolean upgrades, @Nonnull NCRecipes.Type recipeType, int guiID) {
		super(name, upgrades ? 2 : 0, IProcessor.getBaseCapacity(recipeType), power != 0 ? ITileEnergy.energyConnectionAll(EnergyConnection.IN) : ITileEnergy.energyConnectionAll(EnergyConnection.NON), fluidCapacity, fluidCapacity, tankSorptions, allowedFluids, ITileFluid.fluidConnectionAll(FluidConnection.BOTH));
		fluidInputSize = fluidInSize;
		fluidOutputSize = fluidOutSize;
		
		defaultProcessTime = time;
		defaultProcessPower = power;
		baseProcessTime = time;
		baseProcessPower = power;
		
		this.shouldLoseProgress = shouldLoseProgress;
		hasUpgrades = upgrades;
		this.guiID = guiID;
		setTanksShared(fluidInSize > 1);
		
		this.recipeType = recipeType;
		
		slots = ArrayHelper.increasingArray(hasUpgrades ? 2 : 0);
		
		playersToUpdate = new HashSet<EntityPlayer>();
	}
	
	public static List<Integer> defaultTankCapacities(int capacity, int inSize, int outSize) {
		List<Integer> tankCapacities = new ArrayList<Integer>();
		for (int i = 0; i < inSize + outSize; i++) tankCapacities.add(capacity);
		return tankCapacities;
	}
	
	public static List<TankSorption> defaultTankSorptions(int inSize, int outSize) {
		List<TankSorption> tankSorptions = new ArrayList<TankSorption>();
		for (int i = 0; i < inSize; i++) tankSorptions.add(TankSorption.IN);
		for (int i = 0; i < outSize; i++) tankSorptions.add(TankSorption.OUT);
		return tankSorptions;
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
				if (time > 0 && !getIsRedstonePowered()) loseProgress();
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
		if (recipe == null || !recipe.matchingInputs(new ArrayList<ItemStack>(), getFluidInputs())) {
			recipe = getRecipeHandler().getRecipeFromInputs(new ArrayList<ItemStack>(), getFluidInputs());
		}
	}
	
	@Override
	public void refreshActivity() {
		canProcessInputs = canProcessInputs(false);
	}
	
	@Override
	public void refreshActivityOnProduction() {
		canProcessInputs = canProcessInputs(true);
	}
	
	// Processor Stats
	
	public double getProcessTime() {
		return Math.max(1, baseProcessTime/getSpeedMultiplier());
	}
	
	public int getProcessPower() {
		return Math.min(Integer.MAX_VALUE, (int) (baseProcessPower*getPowerMultiplier()));
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
		getEnergyStorage().setStorageCapacity((int)MathHelper.clamp(NCConfig.machine_update_rate*defaultProcessPower*getPowerMultiplier(), IProcessor.getBaseCapacity(recipeType), Integer.MAX_VALUE));
		getEnergyStorage().setMaxTransfer((int)MathHelper.clamp(NCConfig.machine_update_rate*defaultProcessPower*getPowerMultiplier(), IProcessor.getBaseCapacity(recipeType), Integer.MAX_VALUE));
	}
	
	private int getMaxEnergyModified() { // Needed for Galacticraft
		return ModCheck.galacticraftLoaded() ? getMaxEnergyStored() - 16 : getMaxEnergyStored();
	}
	
	// Processing
	
	public boolean isProcessing() {
		return readyToProcess() && !getIsRedstonePowered();
	}
	
	public boolean readyToProcess() {
		return canProcessInputs && hasSufficientEnergy();
	}
	
	public boolean canProcessInputs(boolean justProduced) {
		if (!setRecipeStats()) return false;
		else if (!justProduced && time >= baseProcessTime) return true;
		return canProduceProducts();
	}
	
	public boolean hasSufficientEnergy() {
		return (time <= 0 && ((getProcessEnergy() >= getMaxEnergyModified() && getEnergyStored() >= getMaxEnergyModified()) || getProcessEnergy() <= getEnergyStored())) || (time > 0 && getEnergyStored() >= getProcessPower());
	}
	
	public boolean canProduceProducts() {
		for (int j = 0; j < fluidOutputSize; j++) {
			IFluidIngredient fluidProduct = getFluidProducts().get(j);
			if (fluidProduct.getMaxStackSize() <= 0) continue;
			if (fluidProduct.getStack() == null) return false;
			else if (!getTanks().get(j + fluidInputSize).isEmpty()) {
				if (!getTanks().get(j + fluidInputSize).getFluid().isFluidEqual(fluidProduct.getStack())) {
					return false;
				} else if (!getVoidExcessFluidOutputs() && getTanks().get(j + fluidInputSize).getFluidAmount() + fluidProduct.getMaxStackSize() > getTanks().get(j + fluidInputSize).getCapacity()) {
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
		if (!setRecipeStats()) {
			time = 0;
			if (getEmptyUnusableTankInputs()) for (int i = 0; i < fluidInputSize; i++) getTanks().get(i).setFluid(null);
		}
		else time = MathHelper.clamp(time - oldProcessTime, 0D, baseProcessTime);
		refreshActivityOnProduction();
		if (!canProcessInputs) time = 0;
	}
	
	public void produceProducts() {
		if (recipe == null) return;
		List<Integer> fluidInputOrder = getFluidInputOrder();
		if (fluidInputOrder == AbstractRecipeHandler.INVALID) return;
		
		for (int i = 0; i < fluidInputSize; i++) {
			int fluidIngredientStackSize = getFluidIngredients().get(fluidInputOrder.get(i)).getMaxStackSize();
			if (fluidIngredientStackSize > 0) getTanks().get(i).changeFluidAmount(-fluidIngredientStackSize);
			if (getTanks().get(i).getFluidAmount() <= 0) getTanks().get(i).setFluidStored(null);
		}
		for (int j = 0; j < fluidOutputSize; j++) {
			IFluidIngredient fluidProduct = getFluidProducts().get(j);
			if (fluidProduct.getMaxStackSize() <= 0) continue;
			if (getTanks().get(j + fluidInputSize).isEmpty()) {
				getTanks().get(j + fluidInputSize).setFluidStored(fluidProduct.getNextStack());
			} else if (getTanks().get(j + fluidInputSize).getFluid().isFluidEqual(fluidProduct.getStack())) {
				getTanks().get(j + fluidInputSize).changeFluidAmount(fluidProduct.getNextStackSize());
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
	public List<Tank> getFluidInputs() {
		return getTanks().subList(0, fluidInputSize);
	}
	
	@Override
	public List<IFluidIngredient> getFluidIngredients() {
		return recipe.fluidIngredients();
	}
	
	@Override
	public List<IFluidIngredient> getFluidProducts() {
		return recipe.fluidProducts();
	}
	
	@Override
	public List<Integer> getFluidInputOrder() {
		List<Integer> fluidInputOrder = new ArrayList<Integer>();
		List<IFluidIngredient> fluidIngredients = recipe.fluidIngredients();
		for (int i = 0; i < fluidInputSize; i++) {
			int position = -1;
			for (int j = 0; j < fluidIngredients.size(); j++) {
				if (fluidIngredients.get(j).matches(getFluidInputs().get(i), IngredientSorption.INPUT)) {
					position = j;
					break;
				}
			}
			if (position == -1) return AbstractRecipeHandler.INVALID;
			fluidInputOrder.add(position);
		}
		return fluidInputOrder;
	}
	
	// Upgrades
	
	@Override
	public boolean hasUpgrades() {
		return hasUpgrades;
	}
	
	@Override
	public int getSpeedUpgradeSlot() {
		return 0;
	}
	
	@Override
	public int getEnergyUpgradeSlot() {
		return 1;
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
			if (slot == getSpeedUpgradeSlot() || slot == getEnergyUpgradeSlot()) {
				refreshUpgrades();
			}
		}
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		super.setInventorySlotContents(slot, stack);
		if (!world.isRemote) {
			if (slot == getSpeedUpgradeSlot() || slot == getEnergyUpgradeSlot()) {
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
		return false;
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
		return false;
	}
	
	// Fluids
	
	@Override
	public boolean isNextToFill(FluidStack resource, int tankNumber) {
		if (tankNumber >= fluidInputSize) return false;
		if (!getTanksShared()) return true;
		
		for (int i = 0; i < fluidInputSize; i++) {
			if (tankNumber != i && getTanks().get(i).canFill() && getTanks().get(i).getFluid() != null) {
				if (getTanks().get(i).getFluid().isFluidEqual(resource)) return false;
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
