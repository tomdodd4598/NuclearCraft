package nc.tile.processor;

import java.util.*;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import li.cil.oc.api.network.SimpleComponent;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.handler.TileInfoHandler;
import nc.network.tile.ProcessorUpdatePacket;
import nc.recipe.*;
import nc.recipe.ingredient.*;
import nc.tile.energy.ITileEnergy;
import nc.tile.energyFluid.TileEnergyFluidSidedInventory;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.*;
import nc.tile.internal.inventory.InventoryConnection;
import nc.tile.inventory.ITileInventory;
import nc.tile.processor.info.ProcessorContainerInfo;
import nc.util.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers")
public abstract class TileProcessor<TILE extends TileProcessor<TILE, INFO>, INFO extends ProcessorContainerInfo<TILE, INFO>> extends TileEnergyFluidSidedInventory implements IProcessor<TILE, INFO>, SimpleComponent {
	
	protected final INFO info;
	
	public double baseProcessTime, baseProcessPower, baseProcessRadiation;
	
	protected final @Nonnull NonNullList<ItemStack> consumedStacks;
	protected final @Nonnull List<Tank> consumedTanks;
	
	public double time, resetTime;
	public boolean isProcessing, canProcessInputs, hasConsumed;
	
	protected final BasicRecipeHandler recipeHandler;
	protected RecipeInfo<BasicRecipe> recipeInfo = null;
	
	protected final Set<EntityPlayer> updatePacketListeners = new ObjectOpenHashSet<>();
	
	protected TileProcessor(String name) {
		this(name, TileInfoHandler.getContainerProcessorInfo(name));
	}
	
	private TileProcessor(String name, INFO info) {
		this(name, info, ITileInventory.inventoryConnectionAll(info.defaultItemSorptions()), IProcessor.energyCapacity(info, 1D, 1D), ITileEnergy.energyConnectionAll(info.defaultEnergyConnection()), info.defaultTankCapacities(), NCRecipes.getValidFluids(name), ITileFluid.fluidConnectionAll(info.defaultTankSorptions()));
	}
	
	private TileProcessor(String name, INFO info, @Nonnull InventoryConnection[] inventoryConnections, long capacity, @Nonnull EnergyConnection[] energyConnections, @Nonnull IntList fluidCapacity, List<List<String>> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
		super(name, info.getInventorySize(), inventoryConnections, capacity, energyConnections, fluidCapacity, allowedFluids, fluidConnections);
		this.info = info;
		
		baseProcessTime = info.defaultProcessTime;
		baseProcessPower = info.defaultProcessPower;
		
		setInputTanksSeparated(info.fluidInputSize > 1);
		
		consumedStacks = info.getConsumedStacks();
		consumedTanks = info.getConsumedTanks();
		
		recipeHandler = info.getRecipeHandler();
	}
	
	@Override
	public INFO getContainerInfo() {
		return info;
	}
	
	@Override
	public BasicRecipeHandler getRecipeHandler() {
		return recipeHandler;
	}
	
	@Override
	public double getCurrentTime() {
		return time;
	}
	
	@Override
	public boolean getIsProcessing() {
		return isProcessing;
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
					if (info.losesProgress && !isHaltedByRedstone()) {
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
	public void refreshAll() {
		refreshRecipe();
		refreshActivity();
		refreshEnergyCapacity();
		isProcessing = isProcessing();
		hasConsumed = hasConsumed();
	}
	
	@Override
	public void refreshRecipe() {
		recipeInfo = recipeHandler.getRecipeInfoFromInputs(getItemInputs(hasConsumed), getFluidInputs(hasConsumed));
		if (info.consumesInputs) {
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
	
	public void refreshEnergyCapacity() {}
	
	// Processor Stats
	
	@Override
	public double getBaseProcessTime() {
		return baseProcessTime;
	}
	
	@Override
	public double getBaseProcessPower() {
		return baseProcessPower;
	}
	
	@Override
	public boolean setRecipeStats() {
		if (recipeInfo == null) {
			baseProcessTime = info.defaultProcessTime;
			baseProcessPower = info.defaultProcessPower;
			baseProcessRadiation = 0D;
			return false;
		}
		BasicRecipe recipe = recipeInfo.recipe;
		baseProcessTime = recipe.getBaseProcessTime(info.defaultProcessTime);
		baseProcessPower = recipe.getBaseProcessPower(info.defaultProcessPower);
		baseProcessRadiation = recipe.getBaseProcessRadiation();
		return true;
	}
	
	// Processing
	
	@Override
	public boolean isProcessing() {
		return readyToProcess() && !isHaltedByRedstone();
	}
	
	@Override
	public boolean isHaltedByRedstone() {
		return getRedstoneControl() && getIsRedstonePowered();
	}
	
	@Override
	public boolean readyToProcess() {
		return canProcessInputs && (!info.consumesInputs || hasConsumed) && hasSufficientEnergy();
	}
	
	@Override
	public boolean canProcessInputs() {
		boolean validRecipe = setRecipeStats();
		if (hasConsumed && !validRecipe) {
			List<ItemStack> itemInputs = getItemInputs(true);
			for (int i = 0; i < info.itemInputSize; ++i) {
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
	
	// Needed for Galacticraft
	protected int getMaxEnergyModified() {
		return ModCheck.galacticraftLoaded() ? Math.max(0, getMaxEnergyStored() - 16) : getMaxEnergyStored();
	}
	
	public boolean hasSufficientEnergy() {
		return time <= resetTime && (getProcessEnergy() >= getMaxEnergyModified() && getEnergyStored() >= getMaxEnergyModified() || getProcessEnergy() <= getEnergyStored()) || time > resetTime && getEnergyStored() >= getProcessPower();
	}
	
	@Override
	public void process() {
		time += getSpeedMultiplier();
		getEnergyStorage().changeEnergyStored(-getProcessPower());
		getRadiationSource().setRadiationLevel(baseProcessRadiation * getSpeedMultiplier());
		while (time >= baseProcessTime) {
			finishProcess();
		}
	}
	
	@Override
	public void finishProcess() {
		double oldProcessTime = baseProcessTime;
		produceProducts();
		refreshRecipe();
		time = resetTime = Math.max(0D, time - oldProcessTime);
		refreshActivityOnProduction();
		if (!canProcessInputs) {
			time = resetTime = 0;
			List<Tank> tanks = getTanks();
			for (int i = 0; i < info.fluidInputSize; ++i) {
				if (getVoidUnusableFluidInput(i)) {
					tanks.get(i).setFluidStored(null);
				}
			}
		}
	}
	
	@Override
	public void loseProgress() {
		time = MathHelper.clamp(time - 1.5D * getSpeedMultiplier(), 0D, baseProcessTime);
		if (time < resetTime) {
			resetTime = time;
		}
	}
	
	// IProcessor
	
	@Override
	public List<ItemStack> getItemInputs(boolean consumed) {
		return consumed ? consumedStacks : getInventoryStacks().subList(0, info.itemInputSize);
	}
	
	@Override
	public List<Tank> getFluidInputs(boolean consumed) {
		return consumed ? consumedTanks : getTanks().subList(0, info.fluidInputSize);
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
		return recipeInfo.recipe.getItemIngredients();
	}
	
	@Override
	public List<IFluidIngredient> getFluidIngredients() {
		return recipeInfo.recipe.getFluidIngredients();
	}
	
	@Override
	public List<IItemIngredient> getItemProducts() {
		return recipeInfo.recipe.getItemProducts();
	}
	
	@Override
	public List<IFluidIngredient> getFluidProducts() {
		return recipeInfo.recipe.getFluidProducts();
	}
	
	@Override
	public double getSpeedMultiplier() {
		return 1D;
	}
	
	@Override
	public double getPowerMultiplier() {
		return 1D;
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
	public void setInventorySlotContents(int slot, ItemStack stack) {
		super.setInventorySlotContents(slot, stack);
		if (!world.isRemote) {
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
		if (slot >= info.itemInputSize && slot < info.itemInputSize + info.itemOutputSize) {
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
	
	// OpenComputers
	
	@Override
	@Optional.Method(modid = "opencomputers")
	public String getComponentName() {
		return info.ocComponentName;
	}
}
