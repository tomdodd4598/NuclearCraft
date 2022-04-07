package nc.multiblock.fission.tile;

import static nc.config.NCConfig.smart_processor_input;
import static nc.util.PosHelper.DEFAULT_NON;

import java.util.*;

import javax.annotation.*;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.*;
import nc.Global;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.*;
import nc.multiblock.fission.tile.port.*;
import nc.network.multiblock.FissionIrradiatorUpdatePacket;
import nc.recipe.*;
import nc.recipe.ingredient.IItemIngredient;
import nc.tile.ITileGui;
import nc.tile.generator.IItemGenerator;
import nc.tile.internal.inventory.*;
import nc.tile.inventory.*;
import nc.util.*;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.*;

public class TileFissionIrradiator extends TileFissionPart implements ITileFilteredInventory, ITileGui<FissionIrradiatorUpdatePacket>, IItemGenerator, IFissionHeatingComponent, IFissionFluxSink, IFissionPortTarget<TileFissionIrradiatorPort, TileFissionIrradiator> {
	
	protected final @Nonnull String inventoryName = Global.MOD_ID + ".container.fission_irradiator";
	
	protected final @Nonnull NonNullList<ItemStack> inventoryStacks = NonNullList.withSize(2, ItemStack.EMPTY);
	protected final @Nonnull NonNullList<ItemStack> filterStacks = NonNullList.withSize(2, ItemStack.EMPTY);
	protected final @Nonnull NonNullList<ItemStack> consumedStacks = NonNullList.withSize(1, ItemStack.EMPTY);
	
	protected @Nonnull InventoryConnection[] inventoryConnections = ITileInventory.inventoryConnectionAll(Lists.newArrayList(ItemSorption.NON, ItemSorption.NON));
	
	protected final int itemInputSize = 1, itemOutputSize = 1;
	
	public double baseProcessTime = 1D, baseProcessHeatPerFlux = 0D, baseProcessEfficiency = 0D, baseProcessRadiation = 0D;
	public int minFluxPerTick = 0, maxFluxPerTick = -1;
	
	public double time;
	public boolean isProcessing, hasConsumed, canProcessInputs;
	
	protected RecipeInfo<BasicRecipe> recipeInfo;
	
	protected Set<EntityPlayer> updatePacketListeners;
	
	protected FissionCluster cluster = null;
	protected long heat = 0L;
	
	public long clusterHeatStored, clusterHeatCapacity;
	
	protected long flux = 0;
	
	protected BlockPos masterPortPos = DEFAULT_NON;
	protected TileFissionIrradiatorPort masterPort = null;
	
	// protected int irradiatorCount;
	
	public TileFissionIrradiator() {
		super(CuboidalPartPositionType.INTERIOR);
		
		updatePacketListeners = new ObjectOpenHashSet<>();
	}
	
	@Override
	public void onMachineAssembled(FissionReactor controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
	}
	
	// IFissionComponent
	
	@Override
	public @Nullable FissionCluster getCluster() {
		return cluster;
	}
	
	@Override
	public void setClusterInternal(@Nullable FissionCluster cluster) {
		this.cluster = cluster;
	}
	
	@Override
	public boolean isValidHeatConductor(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
		return isProcessing;
	}
	
	@Override
	public boolean isFunctional() {
		return isProcessing;
	}
	
	@Override
	public void resetStats() {
		flux = 0;
		refreshRecipe();
		refreshActivity();
		refreshIsProcessing(true);
	}
	
	@Override
	public boolean isClusterRoot() {
		return true;
	}
	
	@Override
	public void clusterSearch(Integer id, final Object2IntMap<IFissionComponent> clusterSearchCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
		refreshRecipe();
		refreshActivity();
		refreshIsProcessing(false);
		
		IFissionFluxSink.super.clusterSearch(id, clusterSearchCache, componentFailCache, assumedValidCache);
	}
	
	@Override
	public void refreshIsProcessing(boolean checkCluster) {
		isProcessing = isProcessing(checkCluster);
		hasConsumed = hasConsumed();
	}
	
	@Override
	public void onClusterMeltdown(Iterator<IFissionComponent> componentIterator) {
		
	}
	
	@Override
	public boolean isAcceptingFlux(EnumFacing side) {
		return canProcessInputs;
	}
	
	@Override
	public boolean isNullifyingSources(EnumFacing side) {
		return canProcessInputs;
	}
	
	@Override
	public double moderatorLineEfficiencyFactor() {
		return baseProcessEfficiency;
	}
	
	@Override
	public long getFlux() {
		return flux;
	}
	
	@Override
	public void addFlux(long addedFlux) {
		flux += addedFlux;
	}
	
	@Override
	public long getRawHeating() {
		return (long) Math.min(Long.MAX_VALUE, Math.floor(flux * baseProcessHeatPerFlux));
	}
	
	@Override
	public long getRawHeatingIgnoreCoolingPenalty() {
		return 0L;
	}
	
	@Override
	public double getEffectiveHeating() {
		return flux * baseProcessHeatPerFlux * baseProcessEfficiency;
	}
	
	@Override
	public double getEffectiveHeatingIgnoreCoolingPenalty() {
		return 0D;
	}
	
	@Override
	public long getHeatStored() {
		return heat;
	}
	
	@Override
	public void setHeatStored(long heat) {
		this.heat = heat;
	}
	
	// IFissionPortTarget
	
	@Override
	public BlockPos getMasterPortPos() {
		return masterPortPos;
	}
	
	@Override
	public void setMasterPortPos(BlockPos pos) {
		masterPortPos = pos;
	}
	
	@Override
	public void clearMasterPort() {
		masterPort = null;
		masterPortPos = DEFAULT_NON;
	}
	
	@Override
	public void refreshMasterPort() {
		masterPort = getMultiblock() == null ? null : getMultiblock().getPartMap(TileFissionIrradiatorPort.class).get(masterPortPos.toLong());
		if (masterPort == null) {
			masterPortPos = DEFAULT_NON;
		}
	}
	
	@Override
	public boolean onPortRefresh() {
		refreshRecipe();
		refreshActivity();
		refreshIsProcessing(true);
		
		return isMultiblockAssembled() && getMultiblock().isReactorOn && !isProcessing && isProcessing(false);
	}
	
	// Processing
	
	@Override
	public void onLoad() {
		super.onLoad();
		if (!world.isRemote) {
			refreshMasterPort();
			refreshRecipe();
			refreshActivity();
			refreshIsProcessing(true);
		}
	}
	
	@Override
	public void update() {
		if (!world.isRemote) {
			boolean wasProcessing = isProcessing;
			isProcessing = isProcessing(true);
			boolean shouldRefresh = isMultiblockAssembled() && getMultiblock().isReactorOn && !isProcessing && isProcessing(false);
			boolean shouldUpdate = wasProcessing != isProcessing;
			
			if (isProcessing) {
				process();
			}
			else {
				getRadiationSource().setRadiationLevel(0D);
			}
			
			if (shouldRefresh) {
				getMultiblock().refreshFlag = true;
			}
			
			sendTileUpdatePacketToListeners();
			if (shouldUpdate) {
				markDirty();
			}
		}
	}
	
	@Override
	public void refreshRecipe() {
		recipeInfo = NCRecipes.fission_irradiator.getRecipeInfoFromInputs(getItemInputs(hasConsumed), new ArrayList<>());
		consumeInputs();
	}
	
	@Override
	public void refreshActivity() {
		boolean wasReady = readyToProcess(false);
		canProcessInputs = canProcessInputs();
		if (getMultiblock() != null && !wasReady && readyToProcess(false)) {
			getMultiblock().refreshFlag = true;
		}
	}
	
	@Override
	public void refreshActivityOnProduction() {
		canProcessInputs = canProcessInputs();
	}
	
	// Processor Stats
	
	public double getSpeedMultiplier() {
		if ((minFluxPerTick >= 0 && flux < minFluxPerTick) || (maxFluxPerTick >= 0 && flux > maxFluxPerTick)) {
			return 0;
		}
		return flux;
	}
	
	public boolean setRecipeStats() {
		if (recipeInfo == null) {
			baseProcessTime = 1D;
			baseProcessHeatPerFlux = 0D;
			baseProcessEfficiency = 0D;
			minFluxPerTick = 0;
			maxFluxPerTick = -1;
			baseProcessRadiation = 0D;
			return false;
		}
		BasicRecipe recipe = recipeInfo.getRecipe();
		baseProcessTime = recipe.getIrradiatorFluxRequired();
		baseProcessHeatPerFlux = recipe.getIrradiatorHeatPerFlux();
		baseProcessEfficiency = recipe.getIrradiatorProcessEfficiency();
		minFluxPerTick = recipe.getIrradiatorMinFluxPerTick();
		maxFluxPerTick = recipe.getIrradiatorMaxFluxPerTick();
		baseProcessRadiation = recipe.getIrradiatorBaseProcessRadiation();
		return true;
	}
	
	// Processing
	
	public boolean isProcessing(boolean checkCluster) {
		return readyToProcess(checkCluster) && flux > 0;
	}
	
	public boolean readyToProcess(boolean checkCluster) {
		return canProcessInputs && hasConsumed && isMultiblockAssembled() && !(checkCluster && cluster == null);
	}
	
	public boolean hasConsumed() {
		if (world.isRemote) {
			return hasConsumed;
		}
		for (int i = 0; i < itemInputSize; ++i) {
			if (!consumedStacks.get(i).isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean canProcessInputs() {
		boolean validRecipe = setRecipeStats(), canProcess = validRecipe && canProduceProducts();
		if (hasConsumed && !validRecipe) {
			for (int i = 0; i < itemInputSize; ++i) {
				getItemInputs(true).set(i, ItemStack.EMPTY);
			}
			hasConsumed = false;
		}
		if (!canProcess) {
			time = MathHelper.clamp(time, 0D, baseProcessTime - 1D);
		}
		return canProcess;
	}
	
	public boolean canProduceProducts() {
		for (int j = 0; j < itemOutputSize; ++j) {
			IItemIngredient itemProduct = getItemProducts().get(j);
			if (itemProduct.getMaxStackSize(0) <= 0) {
				continue;
			}
			if (itemProduct.getStack() == null || itemProduct.getStack().isEmpty()) {
				return false;
			}
			else if (!getInventoryStacks().get(j + itemInputSize).isEmpty()) {
				if (!getInventoryStacks().get(j + itemInputSize).isItemEqual(itemProduct.getStack())) {
					return false;
				}
				else if (getInventoryStacks().get(j + itemInputSize).getCount() + itemProduct.getMaxStackSize(0) > getInventoryStacks().get(j + itemInputSize).getMaxStackSize()) {
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
		
		for (int i = 0; i < itemInputSize; ++i) {
			if (!consumedStacks.get(i).isEmpty()) {
				consumedStacks.set(i, ItemStack.EMPTY);
			}
		}
		for (int i = 0; i < itemInputSize; ++i) {
			int maxStackSize = getItemIngredients().get(itemInputOrder.get(i)).getMaxStackSize(recipeInfo.getItemIngredientNumbers().get(i));
			if (maxStackSize > 0) {
				consumedStacks.set(i, new ItemStack(getInventoryStacks().get(i).getItem(), maxStackSize, StackHelper.getMetadata(getInventoryStacks().get(i))));
				getInventoryStacks().get(i).shrink(maxStackSize);
			}
			if (getInventoryStacks().get(i).getCount() <= 0) {
				getInventoryStacks().set(i, ItemStack.EMPTY);
			}
		}
		hasConsumed = true;
	}
	
	public void process() {
		time += getSpeedMultiplier();
		getRadiationSource().setRadiationLevel(baseProcessRadiation * getSpeedMultiplier() / RecipeStats.getFissionMaxModeratorLineFlux());
		while (time >= baseProcessTime) {
			finishProcess();
		}
	}
	
	public void finishProcess() {
		double oldProcessTime = baseProcessTime, oldProcessHeat = baseProcessHeatPerFlux, oldProcessEfficiency = baseProcessEfficiency;
		produceProducts();
		refreshRecipe();
		time = Math.max(0D, time - oldProcessTime);
		refreshActivityOnProduction();
		if (!canProcessInputs) {
			time = 0;
		}
		
		if (getMultiblock() != null) {
			if (canProcessInputs) {
				if (oldProcessHeat != baseProcessHeatPerFlux || oldProcessEfficiency != baseProcessEfficiency) {
					getMultiblock().addClusterToRefresh(cluster);
				}
			}
			else {
				getMultiblock().refreshFlag = true;
			}
		}
	}
	
	public void produceProducts() {
		for (int i = 0; i < itemInputSize; ++i) {
			consumedStacks.set(i, ItemStack.EMPTY);
		}
		
		if (!hasConsumed || recipeInfo == null) {
			return;
		}
		
		for (int j = 0; j < itemOutputSize; ++j) {
			IItemIngredient itemProduct = getItemProducts().get(j);
			if (itemProduct.getNextStackSize(0) <= 0) {
				continue;
			}
			if (getInventoryStacks().get(j + itemInputSize).isEmpty()) {
				getInventoryStacks().set(j + itemInputSize, itemProduct.getNextStack(0));
			}
			else if (getInventoryStacks().get(j + itemInputSize).isItemEqual(itemProduct.getStack())) {
				int count = Math.min(getInventoryStackLimit(), getInventoryStacks().get(j + itemInputSize).getCount() + itemProduct.getNextStackSize(0));
				getInventoryStacks().get(j + itemInputSize).setCount(count);
			}
		}
		hasConsumed = false;
	}
	
	// IProcessor
	
	@Override
	public int getItemInputSize() {
		return itemInputSize;
	}
	
	@Override
	public int getItemOutputSize() {
		return itemOutputSize;
	}
	
	@Override
	public int getOtherSlotsSize() {
		return 0;
	}
	
	@Override
	public List<ItemStack> getItemInputs(boolean consumed) {
		return consumed ? consumedStacks : getInventoryStacks().subList(0, itemInputSize);
	}
	
	@Override
	public List<IItemIngredient> getItemIngredients() {
		return recipeInfo.getRecipe().getItemIngredients();
	}
	
	@Override
	public List<IItemIngredient> getItemProducts() {
		return recipeInfo.getRecipe().getItemProducts();
	}
	
	// ITileInventory
	
	@Override
	public String getName() {
		return inventoryName;
	}
	
	@Override
	public @Nonnull NonNullList<ItemStack> getInventoryStacks() {
		return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getInventoryStacks() : inventoryStacks;
	}
	
	@Override
	public @Nonnull NonNullList<ItemStack> getInventoryStacksInternal() {
		return inventoryStacks;
	}
	
	public @Nonnull NonNullList<ItemStack> getConsumedStacksInternal() {
		return consumedStacks;
	}
	
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack stack = ITileFilteredInventory.super.decrStackSize(slot, amount);
		if (!world.isRemote) {
			if (slot < itemInputSize) {
				refreshRecipe();
				refreshActivity();
			}
			else if (slot < itemInputSize + itemOutputSize) {
				refreshActivity();
			}
		}
		return stack;
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		ITileFilteredInventory.super.setInventorySlotContents(slot, stack);
		if (!world.isRemote) {
			if (slot < itemInputSize) {
				refreshRecipe();
				refreshActivity();
			}
			else if (slot < itemInputSize + itemOutputSize) {
				refreshActivity();
			}
		}
	}
	
	@Override
	public void markDirty() {
		refreshRecipe();
		refreshActivity();
		super.markDirty();
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (stack.isEmpty() || slot >= itemInputSize) {
			return false;
		}
		ItemStack filter = getFilterStacks().get(slot);
		if (!filter.isEmpty() && !stack.isItemEqual(filter)) {
			return false;
		}
		return isItemValidForSlotInternal(slot, stack);
	}
	
	@Override
	public boolean isItemValidForSlotInternal(int slot, ItemStack stack) {
		if (stack.isEmpty() || slot >= itemInputSize) {
			return false;
		}
		return smart_processor_input ? NCRecipes.fission_irradiator.isValidItemInput(slot, stack, recipeInfo, getItemInputs(false), inputItemStacksExcludingSlot(slot)) : NCRecipes.fission_irradiator.isValidItemInput(slot, stack);
	}
	
	public List<ItemStack> inputItemStacksExcludingSlot(int slot) {
		List<ItemStack> inputItemsExcludingSlot = new ArrayList<>(getItemInputs(false));
		inputItemsExcludingSlot.remove(slot);
		return inputItemsExcludingSlot;
	}
	
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
		return ITileFilteredInventory.super.canInsertItem(slot, stack, side) && isItemValidForSlot(slot, stack);
	}
	
	@Override
	public boolean hasConfigurableInventoryConnections() {
		return false;
	}
	
	@Override
	public void clearAllSlots() {
		ITileFilteredInventory.super.clearAllSlots();
		for (int i = 0; i < consumedStacks.size(); ++i) {
			consumedStacks.set(i, ItemStack.EMPTY);
		}
		
		hasConsumed = false;
		refreshRecipe();
		refreshActivity();
		refreshIsProcessing(true);
	}
	
	@Override
	public @Nonnull InventoryConnection[] getInventoryConnections() {
		return inventoryConnections;
	}
	
	@Override
	public void setInventoryConnections(@Nonnull InventoryConnection[] connections) {
		inventoryConnections = connections;
	}
	
	@Override
	public ItemOutputSetting getItemOutputSetting(int slot) {
		return ItemOutputSetting.DEFAULT;
	}
	
	@Override
	public void setItemOutputSetting(int slot, ItemOutputSetting setting) {}
	
	// ITileFilteredInventory
	
	@Override
	public @Nonnull NonNullList<ItemStack> getFilterStacks() {
		return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getFilterStacks() : filterStacks;
	}
	
	@Override
	public boolean canModifyFilter(int slot) {
		return getMultiblock() != null ? !getMultiblock().isAssembled() : true;
	}
	
	@Override
	public void onFilterChanged(int slot) {
		markDirty();
	}
	
	@Override
	public Object getFilterKey() {
		return getFilterStacks().get(0).isEmpty() ? 0 : RecipeItemHelper.pack(getFilterStacks().get(0));
	}
	
	// ITileGui
	
	@Override
	public int getGuiID() {
		return 200;
	}
	
	@Override
	public Set<EntityPlayer> getTileUpdatePacketListeners() {
		return updatePacketListeners;
	}
	
	@Override
	public FissionIrradiatorUpdatePacket getTileUpdatePacket() {
		return new FissionIrradiatorUpdatePacket(pos, masterPortPos, getFilterStacks(), cluster, isProcessing, time, baseProcessTime);
	}
	
	@Override
	public void onTileUpdatePacket(FissionIrradiatorUpdatePacket message) {
		masterPortPos = message.masterPortPos;
		if (DEFAULT_NON.equals(masterPortPos) ^ masterPort == null) {
			refreshMasterPort();
		}
		getFilterStacks().set(0, message.filterStack);
		clusterHeatStored = message.clusterHeatStored;
		clusterHeatCapacity = message.clusterHeatCapacity;
		isProcessing = message.isProcessing;
		time = message.time;
		baseProcessTime = message.baseProcessTime;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		writeInventory(nbt);
		writeInventoryConnections(nbt);
		
		nbt.setDouble("baseProcessTime", baseProcessTime);
		nbt.setDouble("baseProcessHeatPerFlux", baseProcessHeatPerFlux);
		nbt.setDouble("baseProcessEfficiency", baseProcessEfficiency);
		
		nbt.setDouble("time", time);
		nbt.setBoolean("isProcessing", isProcessing);
		nbt.setBoolean("hasConsumed", hasConsumed);
		nbt.setBoolean("canProcessInputs", canProcessInputs);
		
		nbt.setLong("flux", flux);
		nbt.setLong("clusterHeat", heat);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readInventory(nbt);
		readInventoryConnections(nbt);
		
		baseProcessTime = nbt.getDouble("baseProcessTime");
		baseProcessHeatPerFlux = nbt.getDouble("baseProcessHeatPerFlux");
		baseProcessEfficiency = nbt.getDouble("baseProcessEfficiency");
		
		time = nbt.getDouble("time");
		isProcessing = nbt.getBoolean("isProcessing");
		hasConsumed = nbt.getBoolean("hasConsumed");
		canProcessInputs = nbt.getBoolean("canProcessInputs");
		
		flux = nbt.getLong("flux");
		heat = nbt.getLong("clusterHeat");
	}
	
	@Override
	public NBTTagCompound writeInventory(NBTTagCompound nbt) {
		NBTHelper.writeAllItems(nbt, inventoryStacks, filterStacks, consumedStacks);
		return nbt;
	}
	
	@Override
	public void readInventory(NBTTagCompound nbt) {
		NBTHelper.readAllItems(nbt, inventoryStacks, filterStacks, consumedStacks);
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return !getInventoryStacks().isEmpty() && hasInventorySideCapability(side);
		}
		return super.hasCapability(capability, side);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (!getInventoryStacks().isEmpty() && hasInventorySideCapability(side)) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(getItemHandler(side));
			}
			return null;
		}
		return super.getCapability(capability, side);
	}
	
	@Override
	public IItemHandler getItemHandler(@Nullable EnumFacing side) {
		// ITileInventory tile = !DEFAULT_NON.equals(masterPortPos) ? masterPort : this;
		// return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new PortItemHandler(tile, side));
		return ITileFilteredInventory.super.getItemHandler(side);
	}
}
