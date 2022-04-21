package nc.tile.processor;

import static nc.config.NCConfig.*;

import java.util.*;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import nc.ModCheck;
import nc.init.NCItems;
import nc.network.tile.ProcessorUpdatePacket;
import nc.recipe.*;
import nc.recipe.ingredient.IFluidIngredient;
import nc.tile.energy.ITileEnergy;
import nc.tile.energyFluid.TileEnergyFluidSidedInventory;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.*;
import nc.tile.internal.inventory.ItemSorption;
import nc.tile.inventory.ITileInventory;
import nc.util.StackHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

public class TileFluidProcessor extends TileEnergyFluidSidedInventory implements IFluidProcessor, ITileSideConfigGui<ProcessorUpdatePacket>, IUpgradable {
	
	public final double defaultProcessTime, defaultProcessPower;
	public double baseProcessTime, baseProcessPower, baseProcessRadiation;
	protected final int fluidInputSize, fluidOutputSize;
	
	public double time, resetTime;
	public boolean isProcessing, canProcessInputs;
	
	public final boolean shouldLoseProgress, hasUpgrades;
	public final int processorID, sideConfigXOffset, sideConfigYOffset;
	
	public final BasicRecipeHandler recipeHandler;
	protected RecipeInfo<BasicRecipe> recipeInfo;
	
	protected Set<EntityPlayer> updatePacketListeners;
	
	public TileFluidProcessor(String name, int fluidInSize, int fluidOutSize, @Nonnull List<ItemSorption> itemSorptions, @Nonnull IntList fluidCapacity, @Nonnull List<TankSorption> tankSorptions, List<List<String>> allowedFluids, double time, double power, boolean shouldLoseProgress, @Nonnull BasicRecipeHandler recipeHandler, int processorID, int sideConfigXOffset, int sideConfigYOffset) {
		this(name, fluidInSize, fluidOutSize, itemSorptions, fluidCapacity, tankSorptions, allowedFluids, time, power, shouldLoseProgress, true, recipeHandler, processorID, sideConfigXOffset, sideConfigYOffset);
	}
	
	public TileFluidProcessor(String name, int fluidInSize, int fluidOutSize, @Nonnull List<ItemSorption> itemSorptions, @Nonnull IntList fluidCapacity, @Nonnull List<TankSorption> tankSorptions, List<List<String>> allowedFluids, double time, double power, boolean shouldLoseProgress, boolean upgrades, @Nonnull BasicRecipeHandler recipeHandler, int processorID, int sideConfigXOffset, int sideConfigYOffset) {
		super(name, upgrades ? 2 : 0, ITileInventory.inventoryConnectionAll(itemSorptions), IProcessor.getCapacity(processorID, 1D, 1D), power != 0 ? ITileEnergy.energyConnectionAll(EnergyConnection.IN) : ITileEnergy.energyConnectionAll(EnergyConnection.NON), fluidCapacity, allowedFluids, ITileFluid.fluidConnectionAll(tankSorptions));
		fluidInputSize = fluidInSize;
		fluidOutputSize = fluidOutSize;
		
		defaultProcessTime = processor_time_multiplier * time;
		defaultProcessPower = processor_power_multiplier * power;
		baseProcessTime = processor_time_multiplier * time;
		baseProcessPower = processor_power_multiplier * power;
		
		this.shouldLoseProgress = shouldLoseProgress;
		hasUpgrades = upgrades;
		this.processorID = processorID;
		this.sideConfigXOffset = sideConfigXOffset;
		this.sideConfigYOffset = sideConfigYOffset;
		setInputTanksSeparated(fluidInSize > 1);
		
		this.recipeHandler = recipeHandler;
		
		updatePacketListeners = new ObjectOpenHashSet<>();
	}
	
	public static List<ItemSorption> defaultItemSorptions(boolean upgrades) {
		List<ItemSorption> itemSorptions = new ArrayList<>();
		if (upgrades) {
			itemSorptions.add(ItemSorption.IN);
			itemSorptions.add(ItemSorption.IN);
		}
		return itemSorptions;
	}
	
	public static IntList defaultTankCapacities(int capacity, int inSize, int outSize) {
		IntList tankCapacities = new IntArrayList();
		for (int i = 0; i < inSize + outSize; ++i) {
			tankCapacities.add(capacity);
		}
		return tankCapacities;
	}
	
	public static List<TankSorption> defaultTankSorptions(int inSize, int outSize) {
		List<TankSorption> tankSorptions = new ArrayList<>();
		for (int i = 0; i < inSize; ++i) {
			tankSorptions.add(TankSorption.IN);
		}
		for (int i = 0; i < outSize; ++i) {
			tankSorptions.add(TankSorption.OUT);
		}
		return tankSorptions;
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
		recipeInfo = recipeHandler.getRecipeInfoFromInputs(new ArrayList<>(), getFluidInputs());
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
		BasicRecipe recipe = recipeInfo.getRecipe();
		baseProcessTime = recipe.getBaseProcessTime(defaultProcessTime);
		baseProcessPower = recipe.getBaseProcessPower(defaultProcessPower);
		baseProcessRadiation = recipe.getBaseProcessRadiation();
		return true;
	}
	
	public void setCapacityFromSpeed() {
		int capacity = IProcessor.getCapacity(processorID, getSpeedMultiplier(), getPowerMultiplier());
		getEnergyStorage().setStorageCapacity(capacity);
		getEnergyStorage().setMaxTransfer(capacity);
	}
	
	// Needed for Galacticraft
	private int getMaxEnergyModified() {
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
		for (int j = 0; j < fluidOutputSize; ++j) {
			if (getTankOutputSetting(j + fluidInputSize) == TankOutputSetting.VOID) {
				clearTank(j + fluidInputSize);
				continue;
			}
			IFluidIngredient fluidProduct = getFluidProducts().get(j);
			if (fluidProduct.getMaxStackSize(0) <= 0) {
				continue;
			}
			if (fluidProduct.getStack() == null) {
				return false;
			}
			else if (!getTanks().get(j + fluidInputSize).isEmpty()) {
				if (!getTanks().get(j + fluidInputSize).getFluid().isFluidEqual(fluidProduct.getStack())) {
					return false;
				}
				else if (getTankOutputSetting(j + fluidInputSize) == TankOutputSetting.DEFAULT && getTanks().get(j + fluidInputSize).getFluidAmount() + fluidProduct.getMaxStackSize(0) > getTanks().get(j + fluidInputSize).getCapacity()) {
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
			for (int i = 0; i < fluidInputSize; ++i) {
				if (getVoidUnusableFluidInput(i)) {
					getTanks().get(i).setFluid(null);
				}
			}
		}
	}
	
	public void produceProducts() {
		if (recipeInfo == null) {
			return;
		}
		IntList fluidInputOrder = recipeInfo.getFluidInputOrder();
		if (fluidInputOrder == AbstractRecipeHandler.INVALID) {
			return;
		}
		
		for (int i = 0; i < fluidInputSize; ++i) {
			Tank tank = getTanks().get(i);
			int fluidIngredientStackSize = getFluidIngredients().get(fluidInputOrder.get(i)).getMaxStackSize(recipeInfo.getFluidIngredientNumbers().get(i));
			if (fluidIngredientStackSize > 0) {
				tank.changeFluidAmount(-fluidIngredientStackSize);
			}
			if (tank.getFluidAmount() <= 0) {
				tank.setFluidStored(null);
			}
		}
		for (int j = 0; j < fluidOutputSize; ++j) {
			if (getTankOutputSetting(j + fluidInputSize) == TankOutputSetting.VOID) {
				clearTank(j + fluidInputSize);
				continue;
			}
			IFluidIngredient fluidProduct = getFluidProducts().get(j);
			if (fluidProduct.getMaxStackSize(0) <= 0) {
				continue;
			}
			if (getTanks().get(j + fluidInputSize).isEmpty()) {
				getTanks().get(j + fluidInputSize).setFluidStored(fluidProduct.getNextStack(0));
			}
			else if (getTanks().get(j + fluidInputSize).getFluid().isFluidEqual(fluidProduct.getStack())) {
				getTanks().get(j + fluidInputSize).changeFluidAmount(fluidProduct.getNextStackSize(0));
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
	public int getFluidInputSize() {
		return fluidInputSize;
	}
	
	@Override
	public int getFluidOutputputSize() {
		return fluidOutputSize;
	}
	
	@Override
	public List<Tank> getFluidInputs() {
		return getTanks().subList(0, fluidInputSize);
	}
	
	@Override
	public List<IFluidIngredient> getFluidIngredients() {
		return recipeInfo.getRecipe().getFluidIngredients();
	}
	
	@Override
	public List<IFluidIngredient> getFluidProducts() {
		return recipeInfo.getRecipe().getFluidProducts();
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
		return false;
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
		for (int i = 0; i < getTanks().size(); ++i) {
			getTanks().get(i).readInfo(message.tanksInfo.get(i));
		}
	}
	
	@Override
	public int getSideConfigXOffset() {
		return sideConfigXOffset;
	}
	
	@Override
	public int getSideConfigYOffset() {
		return sideConfigYOffset;
	}
}
