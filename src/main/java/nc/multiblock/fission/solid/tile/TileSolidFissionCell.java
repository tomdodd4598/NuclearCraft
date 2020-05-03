package nc.multiblock.fission.solid.tile;

import static nc.util.BlockPosHelper.DEFAULT_NON;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import nc.Global;
import nc.capability.radiation.source.IRadiationSource;
import nc.config.NCConfig;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.FissionCluster;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.solid.SolidFissionCellSetting;
import nc.multiblock.fission.tile.IFissionComponent;
import nc.multiblock.fission.tile.IFissionFluxSink;
import nc.multiblock.fission.tile.IFissionFuelComponent;
import nc.multiblock.fission.tile.TileFissionPart;
import nc.multiblock.fission.tile.port.IFissionPortTarget;
import nc.multiblock.fission.tile.port.TileFissionCellPort;
import nc.multiblock.network.SolidFissionCellUpdatePacket;
import nc.radiation.RadiationHelper;
import nc.recipe.AbstractRecipeHandler;
import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipe;
import nc.recipe.RecipeInfo;
import nc.recipe.ingredient.IItemIngredient;
import nc.tile.ITileGui;
import nc.tile.generator.IItemGenerator;
import nc.tile.internal.inventory.InventoryConnection;
import nc.tile.internal.inventory.InventoryTileWrapper;
import nc.tile.internal.inventory.ItemOutputSetting;
import nc.tile.internal.inventory.ItemSorption;
import nc.tile.inventory.ITileFilteredInventory;
import nc.tile.inventory.ITileInventory;
import nc.util.ItemStackHelper;
import nc.util.NBTHelper;
import nc.util.RegistryHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileSolidFissionCell extends TileFissionPart implements ITileFilteredInventory, ITileGui<SolidFissionCellUpdatePacket>, IItemGenerator, IFissionFuelComponent, IFissionPortTarget<TileFissionCellPort, TileSolidFissionCell> {
	
	protected final @Nonnull String inventoryName = Global.MOD_ID + ".container.fission_cell";
	
	protected final @Nonnull NonNullList<ItemStack> inventoryStacks = NonNullList.withSize(2, ItemStack.EMPTY);
	protected final @Nonnull NonNullList<ItemStack> filterStacks = NonNullList.withSize(2, ItemStack.EMPTY);
	protected final @Nonnull NonNullList<ItemStack> consumedStacks = NonNullList.withSize(1, ItemStack.EMPTY);
	
	protected @Nonnull InventoryConnection[] inventoryConnections = ITileInventory.inventoryConnectionAll(Lists.newArrayList(ItemSorption.NON, ItemSorption.NON));
	
	protected @Nonnull InventoryTileWrapper invWrapper;
	
	protected @Nonnull SolidFissionCellSetting[] cellSettings = new SolidFissionCellSetting[] {SolidFissionCellSetting.DISABLED, SolidFissionCellSetting.DISABLED, SolidFissionCellSetting.DISABLED, SolidFissionCellSetting.DISABLED, SolidFissionCellSetting.DISABLED, SolidFissionCellSetting.DISABLED};
	
	protected final int itemInputSize = 1, itemOutputSize = 1, otherSlotsSize = 0;
	
	public double baseProcessTime = 1D, baseProcessEfficiency = 0D, baseProcessRadiation = 0D;
	protected int baseProcessHeat = 0, baseProcessCriticality = 1;
	protected boolean selfPriming = false;
	
	public double time;
	public boolean isProcessing, hasConsumed, canProcessInputs;
	
	protected RecipeInfo<ProcessorRecipe> recipeInfo;
	
	protected Set<EntityPlayer> playersToUpdate;
	
	protected FissionCluster cluster = null;
	protected long heat = 0L;
	
	public long clusterHeatStored, clusterHeatCapacity;
	
	protected boolean primed = false, fluxSearched = false;
	protected int flux = 0, heatMult = 0;
	protected double undercoolingLifetimeFactor = 1D;
	protected Double sourceEfficiency = null;
	protected int[] moderatorLineFluxes = new int[] {0, 0, 0, 0, 0, 0};
	protected Double[] moderatorLineEfficiencies = new Double[] {null, null, null, null, null, null};
	protected IFissionFluxSink[] adjacentFluxSinks = new IFissionFluxSink[] {null, null, null, null, null, null};
	protected final LongSet[] passiveModeratorCaches = new LongSet[] {new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet()};
	protected final Long[] activeModeratorCache = new Long[] {null, null, null, null, null, null};
	protected final ModeratorLine[] moderatorLineCaches = new ModeratorLine[] {null, null, null, null, null, null};
	protected final LongSet[] passiveReflectorModeratorCaches = new LongSet[] {new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet()};
	protected final Long[] activeReflectorModeratorCache = new Long[] {null, null, null, null, null, null};
	protected final LongSet activeReflectorCache = new LongOpenHashSet();
	
	protected BlockPos masterPortPos = DEFAULT_NON;
	protected TileFissionCellPort masterPort = null;
	
	//protected int cellCount;
	
	public TileSolidFissionCell() {
		super(CuboidalPartPositionType.INTERIOR);
		invWrapper = new InventoryTileWrapper(this);
		
		playersToUpdate = new ObjectOpenHashSet<>();
	}
	
	@Override
	public void onMachineAssembled(FissionReactor controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		//if (getWorld().isRemote) return;
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
		//if (getWorld().isRemote) return;
		//getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()), 2);
	}
	
	// IFissionFuelComponent
	
	@Override
	public @Nullable FissionCluster getCluster() {
		return cluster;
	}
	
	@Override
	public void setClusterInternal(@Nullable FissionCluster cluster) {
		this.cluster = cluster;
	}
	
	@Override
	public boolean isValidHeatConductor() {
		return isProcessing;
	}
	
	@Override
	public boolean isFunctional() {
		return isProcessing;
	}
	
	@Override
	public void resetStats() {
		/*primed =*/ fluxSearched = false;
		flux = heatMult = 0;
		undercoolingLifetimeFactor = 1D;
		sourceEfficiency = null;
		for (EnumFacing dir : EnumFacing.VALUES) {
			moderatorLineFluxes[dir.getIndex()] = 0;
			moderatorLineEfficiencies[dir.getIndex()] = null;
			adjacentFluxSinks[dir.getIndex()] = null;
			passiveModeratorCaches[dir.getIndex()].clear();
			activeModeratorCache[dir.getIndex()] = null;
			moderatorLineCaches[dir.getIndex()] = null;
			passiveReflectorModeratorCaches[dir.getIndex()].clear();
			activeReflectorModeratorCache[dir.getIndex()] = null;
		}
		activeReflectorCache.clear();
		
		refreshRecipe();
		refreshActivity();
		refreshIsProcessing(true);
	}
	
	@Override
	public boolean isClusterRoot() {
		return true;
	}
	
	@Override
	public void clusterSearch(Integer id, final Object2IntMap<IFissionComponent> clusterSearchCache) {
		refreshRecipe();
		refreshActivity();
		refreshIsProcessing(false);
		
		IFissionFuelComponent.super.clusterSearch(id, clusterSearchCache);
	}
	
	@Override
	public void tryPriming(FissionReactor sourceReactor) {
		if (getMultiblock() != sourceReactor) return;
		
		if (canProcessInputs) primed = true;
	}
	
	@Override
	public boolean isPrimed() {
		return primed;
	}
	
	@Override
	public void unprime() {
		primed = false;
	}
	
	@Override
	public boolean isAcceptingFlux(EnumFacing side) {
		return canProcessInputs;
	}
	
	@Override
	public void refreshIsProcessing(boolean checkCluster) {
		isProcessing = isProcessing(checkCluster);
		hasConsumed = hasConsumed();
	}
	
	@Override
	public boolean isFluxSearched() {
		return fluxSearched;
	}
	
	@Override
	public void setFluxSearched(boolean fluxSearched) {
		this.fluxSearched = fluxSearched;
	}
	
	@Override
	public void incrementHeatMultiplier() {
		heatMult++;
	}
	
	@Override
	public double getSourceEfficiency() {
		return sourceEfficiency == null ? 1D : sourceEfficiency.doubleValue();
	}
	
	@Override
	public void setSourceEfficiency(double sourceEfficiency, boolean maximize) {
		this.sourceEfficiency = (this.sourceEfficiency != null && maximize) ? Math.max(this.sourceEfficiency, sourceEfficiency) : sourceEfficiency;
	}
	
	@Override
	public void addFlux(int flux) {
		this.flux += flux;
	}
	
	@Override
	public int[] getModeratorLineFluxes() {
		return moderatorLineFluxes;
	}
	
	@Override
	public Double[] getModeratorLineEfficiencies() {
		return moderatorLineEfficiencies;
	}
	
	@Override
	public IFissionFluxSink[] getAdjacentFluxSinks() {
		return adjacentFluxSinks;
	}
	
	@Override
	public LongSet[] getPassiveModeratorCaches() {
		return passiveModeratorCaches;
	}
	
	@Override
	public Long[] getActiveModeratorCache() {
		return activeModeratorCache;
	}
	
	@Override
	public ModeratorLine[] getModeratorLineCaches() {
		return moderatorLineCaches;
	}
	
	@Override
	public LongSet[] getPassiveReflectorModeratorCaches() {
		return passiveReflectorModeratorCaches;
	}
	
	@Override
	public Long[] getActiveReflectorModeratorCache() {
		return activeReflectorModeratorCache;
	}
	
	@Override
	public LongSet getActiveReflectorCache() {
		return activeReflectorCache;
	}
	
	@Override
	public long getRawHeating() {
		return baseProcessHeat*heatMult;
	}
	
	@Override
	public double getEffectiveHeating() {
		return baseProcessHeat*getEfficiency();
	}
	
	@Override
	public long getHeatMultiplier() {
		return heatMult;
	}
	
	@Override
	public double getFluxEfficiencyFactor() {
		return (1D + Math.exp(-2D*baseProcessCriticality))/(1D + Math.exp(2D*(flux - 2D*baseProcessCriticality)));
	}
	
	@Override
	public double getEfficiency() {
		return heatMult*baseProcessEfficiency*getSourceEfficiency()*getModeratorEfficiencyFactor()*getFluxEfficiencyFactor();
	}
	
	@Override
	public void setUndercoolingLifetimeFactor(double undercoolingLifetimeFactor) {
		this.undercoolingLifetimeFactor = undercoolingLifetimeFactor;
	}
	
	@Override
	public boolean isSelfPriming() {
		return selfPriming;
	}
	
	@Override
	public void onClusterMeltdown() {
		IRadiationSource chunkSource = RadiationHelper.getRadiationSource(world.getChunk(pos));
		if (chunkSource != null) {
			RadiationHelper.addToSourceRadiation(chunkSource, 8D*baseProcessRadiation*getSpeedMultiplier()*NCConfig.fission_meltdown_radiation_multiplier);
		}
		
		IBlockState corium = RegistryHelper.getBlock(Global.MOD_ID + ":fluid_corium").getDefaultState();
		world.removeTileEntity(pos);
		world.setBlockState(pos, corium);
		
		if (getMultiblock() != null) {
			for (EnumFacing dir : EnumFacing.VALUES) {
				BlockPos offPos = pos.offset(dir);
				if (getMultiblock().rand.nextDouble() < 0.75D) {
					world.removeTileEntity(offPos);
					world.setBlockState(offPos, corium);
				}
			}
		}
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
		masterPort = getMultiblock() == null ? null : getMultiblock().getPartMap(TileFissionCellPort.class).get(masterPortPos.toLong());
		if (masterPort == null) masterPortPos = DEFAULT_NON;
	}
	
	@Override
	public boolean onPortRefresh() {
		refreshRecipe();
		refreshActivity();
		refreshIsProcessing(isFunctional());
		
		return isFunctional() ^ readyToProcess(false);
	}
	
	// Processing
	
	@Override
	public void onAdded() {
		super.onAdded();
		if (!world.isRemote) {
			refreshMasterPort();
			refreshRecipe();
			refreshActivity();
			refreshIsProcessing(true);
		}
	}
	
	// Ticking
	
	@Override
	public void update() {
		super.update();
		updateCell();
	}
	
	public void updateCell() {
		if (!world.isRemote) {
			boolean wasProcessing = isProcessing;
			isProcessing = isProcessing(true);
			boolean shouldRefresh =  isMultiblockAssembled() && getMultiblock().isReactorOn && !isProcessing && isProcessing(false);
			boolean shouldUpdate = wasProcessing != isProcessing;
			
			if (isProcessing) process();
			else getRadiationSource().setRadiationLevel(0D);
			
			//tickCell();
			//if (cellCount == 0) pushStacks();
			
			if (shouldRefresh) {
				getMultiblock().refreshFlag = true;
			}
			
			sendUpdateToListeningPlayers();
			if (shouldUpdate) markDirty();
		}
	}
	
	/*public void tickCell() {
		cellCount++; cellCount %= NCConfig.machine_update_rate / 2;
	}*/
	
	@Override
	public void refreshRecipe() {
		recipeInfo = NCRecipes.solid_fission.getRecipeInfoFromInputs(getItemInputs(hasConsumed), new ArrayList<>());
		consumeInputs();
	}
	
	@Override
	public void refreshActivity() {
		boolean wasReady = readyToProcess(false);
		canProcessInputs = canProcessInputs();
		if (getMultiblock() != null && !wasReady && readyToProcess(false) && selfPriming) {
			getMultiblock().refreshFlag = true;
		}
	}
	
	@Override
	public void refreshActivityOnProduction() {
		canProcessInputs = canProcessInputs();
	}
	
	// Processor Stats
	
	public double getSpeedMultiplier() {
		return 1D/(NCConfig.fission_fuel_time_multiplier*undercoolingLifetimeFactor);
	}
	
	public boolean setRecipeStats() {
		if (recipeInfo == null) {
			baseProcessTime = 1D;
			baseProcessHeat = 0;
			baseProcessEfficiency = 0D;
			baseProcessCriticality = 1;
			selfPriming = false;
			baseProcessRadiation = 0D;
			return false;
		}
		baseProcessTime = recipeInfo.getRecipe().getFissionFuelTime();
		baseProcessHeat = recipeInfo.getRecipe().getFissionFuelHeat();
		baseProcessEfficiency = recipeInfo.getRecipe().getFissionFuelEfficiency();
		baseProcessCriticality = recipeInfo.getRecipe().getFissionFuelCriticality();
		selfPriming = recipeInfo.getRecipe().getFissionFuelSelfPriming();
		baseProcessRadiation = recipeInfo.getRecipe().getFissionFuelRadiation();
		return true;
	}
	
	// Processing
	
	public boolean isProcessing(boolean checkCluster) {
		return readyToProcess(checkCluster) && flux >= baseProcessCriticality;
	}
	
	public boolean readyToProcess(boolean checkCluster) {
		return canProcessInputs && hasConsumed && isMultiblockAssembled() && !(checkCluster && cluster == null);
	}
	
	public boolean hasConsumed() {
		if (world.isRemote) return hasConsumed;
		for (int i = 0; i < itemInputSize; i++) {
			if (!consumedStacks.get(i).isEmpty()) return true;
		}
		return false;
	}
		
	public boolean canProcessInputs() {
		boolean validRecipe = setRecipeStats(), canProcess = validRecipe && canProduceProducts();
		if (hasConsumed && !validRecipe) {
			for (int i = 0; i < itemInputSize; i++) getItemInputs(true).set(i, ItemStack.EMPTY);
			hasConsumed = false;
		}
		if (!canProcess) {
			time = MathHelper.clamp(time, 0D, baseProcessTime - 1D);
		}
		return canProcess;
	}
	
	public boolean canProduceProducts() {
		for(int j = 0; j < itemOutputSize; j++) {
			IItemIngredient itemProduct = getItemProducts().get(j);
			if (itemProduct.getMaxStackSize(0) <= 0) continue;
			if (itemProduct.getStack() == null || itemProduct.getStack().isEmpty()) return false;
			else if (!getInventoryStacks().get(j + itemInputSize).isEmpty()) {
				if (!getInventoryStacks().get(j + itemInputSize).isItemEqual(itemProduct.getStack())) {
					return false;
				} else if (getInventoryStacks().get(j + itemInputSize).getCount() + itemProduct.getMaxStackSize(0) > getInventoryStacks().get(j + itemInputSize).getMaxStackSize()) {
					return false;
				}
			}
		}
		return true;
	}
		
	public void consumeInputs() {
		if (hasConsumed || recipeInfo == null) return;
		IntList itemInputOrder = recipeInfo.getItemInputOrder();
		if (itemInputOrder == AbstractRecipeHandler.INVALID) return;
		
		for (int i = 0; i < itemInputSize; i++) {
			if (!consumedStacks.get(i).isEmpty()) {
				consumedStacks.set(i, ItemStack.EMPTY);
			}
		}
		for (int i = 0; i < itemInputSize; i++) {
			int maxStackSize = getItemIngredients().get(itemInputOrder.get(i)).getMaxStackSize(recipeInfo.getItemIngredientNumbers().get(i));
			if (maxStackSize > 0) {
				consumedStacks.set(i, new ItemStack(getInventoryStacks().get(i).getItem(), maxStackSize, ItemStackHelper.getMetadata(getInventoryStacks().get(i))));
				getInventoryStacks().get(i).shrink(maxStackSize);
			}
			if (getInventoryStacks().get(i).getCount() <= 0) getInventoryStacks().set(i, ItemStack.EMPTY);
		}
		hasConsumed = true;
	}
	
	public void process() {
		time += getSpeedMultiplier();
		getRadiationSource().setRadiationLevel(baseProcessRadiation*getSpeedMultiplier());
		while (time >= baseProcessTime) finishProcess();
	}
	
	public void finishProcess() {
		double oldProcessTime = baseProcessTime, oldProcessEfficiency = baseProcessEfficiency;
		int oldProcessHeat = baseProcessHeat, oldProcessCriticality = baseProcessCriticality;
		produceProducts();
		refreshRecipe();
		time = Math.max(0D, time - oldProcessTime);
		refreshActivityOnProduction();
		if (!canProcessInputs) time = 0;
		
		if (getMultiblock() != null) {
			if (canProcessInputs) {
				if (oldProcessHeat != baseProcessHeat || oldProcessEfficiency != baseProcessEfficiency || oldProcessCriticality != baseProcessCriticality) {
					if (flux < baseProcessCriticality) {
						getMultiblock().refreshFlag = true;
					}
					else {
						getMultiblock().refreshCluster(cluster);
					}
				}
			}
			else {
				getMultiblock().refreshFlag = true;
			}
		}
	}
		
	public void produceProducts() {
		for (int i = 0; i < itemInputSize; i++) consumedStacks.set(i, ItemStack.EMPTY);
		
		if (!hasConsumed || recipeInfo == null) return;
		
		for (int j = 0; j < itemOutputSize; j++) {
			IItemIngredient itemProduct = getItemProducts().get(j);
			if (itemProduct.getNextStackSize(0) <= 0) continue;
			if (getInventoryStacks().get(j + itemInputSize).isEmpty()) {
				getInventoryStacks().set(j + itemInputSize, itemProduct.getNextStack(0));
			} else if (getInventoryStacks().get(j + itemInputSize).isItemEqual(itemProduct.getStack())) {
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
		if (stack.isEmpty() || slot >= itemInputSize) return false;
		ItemStack filter = getFilterStacks().get(slot);
		if (!filter.isEmpty() && !stack.isItemEqual(filter)) {
			return false;
		}
		return isItemValidForSlotInternal(slot, stack);
	}
	
	@Override
	public boolean isItemValidForSlotInternal(int slot, ItemStack stack) {
		if (stack.isEmpty() || slot >= itemInputSize) return false;
		return NCConfig.smart_processor_input ? NCRecipes.solid_fission.isValidItemInput(stack, getInventoryStacks().get(slot), inputItemStacksExcludingSlot(slot)) : NCRecipes.solid_fission.isValidItemInput(stack);
	}
	
	public List<ItemStack> inputItemStacksExcludingSlot(int slot) {
		List<ItemStack> inputItemsExcludingSlot = new ArrayList<ItemStack>(getItemInputs(false));
		inputItemsExcludingSlot.remove(slot);
		return inputItemsExcludingSlot;
	}
	
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
		return ITileFilteredInventory.super.canInsertItem(slot, stack, side) && isItemValidForSlot(slot, stack);
	}
	
	@Override
	public boolean hasConfigurableInventoryConnections() {
		return true;
	}
	
	@Override
	public void clearAllSlots() {
		ITileFilteredInventory.super.clearAllSlots();
		for (int i = 0; i < consumedStacks.size(); i++) consumedStacks.set(i, ItemStack.EMPTY);
		
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
	public @Nonnull InventoryTileWrapper getInventory() {
		return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getInventory() : invWrapper;
	}
	
	@Override
	public ItemOutputSetting getItemOutputSetting(int slot) {
		return ItemOutputSetting.DEFAULT;
	}
	
	@Override
	public void setItemOutputSetting(int slot, ItemOutputSetting setting) {}
	
	public @Nonnull SolidFissionCellSetting[] getCellSettings() {
		return cellSettings;
	}
	
	public void setCellSettings(@Nonnull SolidFissionCellSetting[] settings) {
		cellSettings = settings;
	}
	
	public SolidFissionCellSetting getCellSetting(@Nonnull EnumFacing side) {
		return cellSettings[side.getIndex()];
	}
	
	public void setCellSetting(@Nonnull EnumFacing side, @Nonnull SolidFissionCellSetting setting) {
		cellSettings[side.getIndex()] = setting;
	}
	
	public void toggleCellSetting(@Nonnull EnumFacing side) {
		setCellSetting(side, getCellSetting(side).next());
		refreshInventoryConnections(side);
		markDirtyAndNotify();
	}
	
	public void refreshInventoryConnections(@Nonnull EnumFacing side) {
		switch (getCellSetting(side)) {
		case DISABLED:
			setItemSorption(side, 0, ItemSorption.NON);
			setItemSorption(side, 1, ItemSorption.NON);
			break;
		case DEFAULT:
			setItemSorption(side, 0, ItemSorption.IN);
			setItemSorption(side, 1, ItemSorption.NON);
			break;
		case DEPLETED_OUT:
			setItemSorption(side, 0, ItemSorption.NON);
			setItemSorption(side, 1, ItemSorption.OUT);
			break;
		case FUEL_SPREAD:
			setItemSorption(side, 0, ItemSorption.OUT);
			setItemSorption(side, 1, ItemSorption.NON);
			break;
		default:
			setItemSorption(side, 0, ItemSorption.NON);
			setItemSorption(side, 1, ItemSorption.NON);
			break;
		}
	}
	
	//TODO
	@Override
	public void pushStacksToSide(@Nonnull EnumFacing side) {
		
	}
	
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
		/*if (!canModifyFilter(slot)) {
			getMultiblock().getLogic().refreshPorts();
		}*/
		getInventory().markDirty();
	}
	
	@Override
	public int getFilterID() {
		return getFilterStacks().get(0).isEmpty() ? 0 : RecipeItemHelper.pack(getFilterStacks().get(0));
	}
	
	// ITileGui
	
	@Override
	public int getGuiID() {
		return 201;
	}
	
	@Override
	public Set<EntityPlayer> getPlayersToUpdate() {
		return playersToUpdate;
	}
	
	@Override
	public SolidFissionCellUpdatePacket getGuiUpdatePacket() {
		return new SolidFissionCellUpdatePacket(pos, masterPortPos, getFilterStacks(), cluster, isProcessing, time, baseProcessTime);
	}
	
	@Override
	public void onGuiPacket(SolidFissionCellUpdatePacket message) {
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
	
	public NBTTagCompound writeCellSettings(NBTTagCompound nbt) {
		NBTTagCompound settingsTag = new NBTTagCompound();
		for (EnumFacing side : EnumFacing.VALUES) {
			settingsTag.setInteger("setting" + side.getIndex(), getCellSetting(side).ordinal());
		}
		nbt.setTag("cellSettings", settingsTag);
		return nbt;
	}
	
	public void readCellSettings(NBTTagCompound nbt) {
		NBTTagCompound settingsTag = nbt.getCompoundTag("cellSettings");
		for (EnumFacing side : EnumFacing.VALUES) {
			setCellSetting(side, SolidFissionCellSetting.values()[settingsTag.getInteger("setting" + side.getIndex())]);
			refreshInventoryConnections(side);
		}
	}
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		writeInventory(nbt);
		writeInventoryConnections(nbt);
		writeCellSettings(nbt);
		
		nbt.setDouble("baseProcessTime", baseProcessTime);
		nbt.setInteger("baseProcessHeat", baseProcessHeat);
		nbt.setDouble("baseProcessEfficiency", baseProcessEfficiency);
		nbt.setInteger("baseProcessCriticality", baseProcessCriticality);
		nbt.setBoolean("selfPriming", selfPriming);
		
		nbt.setDouble("time", time);
		nbt.setBoolean("isProcessing", isProcessing);
		nbt.setBoolean("hasConsumed", hasConsumed);
		nbt.setBoolean("canProcessInputs", canProcessInputs);
		
		nbt.setInteger("flux", flux);
		nbt.setLong("clusterHeat", heat);
		
		//nbt.setLong("masterPortPos", masterPortPos.toLong());
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readInventory(nbt);
		readInventoryConnections(nbt);
		readCellSettings(nbt);
		
		baseProcessTime = nbt.getDouble("baseProcessTime");
		baseProcessHeat = nbt.getInteger("baseProcessHeat");
		baseProcessEfficiency = nbt.getDouble("baseProcessEfficiency");
		baseProcessCriticality = nbt.getInteger("baseProcessCriticality");
		selfPriming = nbt.getBoolean("selfPriming");
		
		time = nbt.getDouble("time");
		isProcessing = nbt.getBoolean("isProcessing");
		hasConsumed = nbt.getBoolean("hasConsumed");
		canProcessInputs = nbt.getBoolean("canProcessInputs");
		
		flux = nbt.getInteger("flux");
		heat = nbt.getLong("clusterHeat");
		
		//masterPortPos = BlockPos.fromLong(nbt.getLong("masterPortPos"));
	}
	
	@Override
	public NBTTagCompound writeInventory(NBTTagCompound nbt) {
		NBTHelper.saveAllItems(nbt, inventoryStacks, filterStacks, consumedStacks);
		return nbt;
	}
	
	@Override
	public void readInventory(NBTTagCompound nbt) {
		NBTHelper.loadAllItems(nbt, inventoryStacks, filterStacks, consumedStacks);
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
				return (T) getItemHandlerCapability(side);
			}
			return null;
		}
		return super.getCapability(capability, side);
	}
}
