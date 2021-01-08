package nc.multiblock.fission.solid.tile;

import static nc.config.NCConfig.*;
import static nc.util.PosHelper.DEFAULT_NON;

import java.util.*;

import javax.annotation.*;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.objects.*;
import nc.Global;
import nc.capability.radiation.source.IRadiationSource;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.*;
import nc.multiblock.fission.tile.*;
import nc.multiblock.fission.tile.port.*;
import nc.multiblock.network.SolidFissionCellUpdatePacket;
import nc.radiation.RadiationHelper;
import nc.recipe.*;
import nc.recipe.ingredient.IItemIngredient;
import nc.tile.ITileGui;
import nc.tile.generator.IItemGenerator;
import nc.tile.internal.inventory.*;
import nc.tile.inventory.*;
import nc.util.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.items.*;

public class TileSolidFissionCell extends TileFissionPart implements ITileFilteredInventory, ITileGui<SolidFissionCellUpdatePacket>, IItemGenerator, IFissionFuelComponent, IFissionPortTarget<TileFissionCellPort, TileSolidFissionCell> {
	
	protected final @Nonnull String inventoryName = Global.MOD_ID + ".container.fission_cell";
	
	protected final @Nonnull NonNullList<ItemStack> inventoryStacks = NonNullList.withSize(2, ItemStack.EMPTY);
	protected final @Nonnull NonNullList<ItemStack> filterStacks = NonNullList.withSize(2, ItemStack.EMPTY);
	protected final @Nonnull NonNullList<ItemStack> consumedStacks = NonNullList.withSize(1, ItemStack.EMPTY);
	
	protected @Nonnull InventoryConnection[] inventoryConnections = ITileInventory.inventoryConnectionAll(Lists.newArrayList(ItemSorption.NON, ItemSorption.NON));
	
	protected final int itemInputSize = 1, itemOutputSize = 1, otherSlotsSize = 0;
	
	public double baseProcessTime = 1D, baseProcessEfficiency = 0D, baseProcessRadiation = 0D;
	public int baseProcessHeat = 0, baseProcessCriticality = 1;
	protected boolean selfPriming = false;
	
	public double time;
	public boolean isProcessing, hasConsumed, canProcessInputs;
	
	protected RecipeInfo<BasicRecipe> recipeInfo;
	
	protected Set<EntityPlayer> playersToUpdate;
	
	protected FissionCluster cluster = null;
	protected long heat = 0L;
	
	public long clusterHeatStored, clusterHeatCapacity;
	
	protected boolean primed = false, fluxSearched = false;
	protected int flux = 0;
	
	public int heatMult = 0;
	protected double undercoolingLifetimeFactor = 1D;
	protected Double sourceEfficiency = null;
	protected int[] moderatorLineFluxes = new int[] {0, 0, 0, 0, 0, 0};
	protected Double[] moderatorLineEfficiencies = new Double[] {null, null, null, null, null, null};
	protected IFissionFluxSink[] adjacentFluxSinks = new IFissionFluxSink[] {null, null, null, null, null, null};
	protected final LongSet[] passiveModeratorCaches = new LongSet[] {new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet()};
	protected final LongSet[] activeModeratorCaches = new LongSet[] {new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet()};
	protected final ModeratorLine[] moderatorLineCaches = new ModeratorLine[] {null, null, null, null, null, null};
	protected final LongSet[] passiveReflectorModeratorCaches = new LongSet[] {new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet()};
	protected final LongSet[] activeReflectorModeratorCaches = new LongSet[] {new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet()};
	protected final LongSet activeReflectorCache = new LongOpenHashSet();
	
	protected BlockPos masterPortPos = DEFAULT_NON;
	protected TileFissionCellPort masterPort = null;
	
	// protected int cellCount;
	
	public TileSolidFissionCell() {
		super(CuboidalPartPositionType.INTERIOR);
		
		playersToUpdate = new ObjectOpenHashSet<>();
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
	public boolean isValidHeatConductor(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
		return isProcessing;
	}
	
	@Override
	public boolean isFunctional() {
		return isProcessing;
	}
	
	@Override
	public void resetStats() {
		/* primed = */ fluxSearched = false;
		flux = heatMult = 0;
		undercoolingLifetimeFactor = 1D;
		// sourceEfficiency = null;
		for (EnumFacing dir : EnumFacing.VALUES) {
			moderatorLineFluxes[dir.getIndex()] = 0;
			moderatorLineEfficiencies[dir.getIndex()] = null;
			adjacentFluxSinks[dir.getIndex()] = null;
			passiveModeratorCaches[dir.getIndex()].clear();
			activeModeratorCaches[dir.getIndex()].clear();
			moderatorLineCaches[dir.getIndex()] = null;
			passiveReflectorModeratorCaches[dir.getIndex()].clear();
			activeReflectorModeratorCaches[dir.getIndex()].clear();
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
	public void clusterSearch(Integer id, final Object2IntMap<IFissionComponent> clusterSearchCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
		refreshRecipe();
		refreshActivity();
		refreshIsProcessing(false);
		
		IFissionFuelComponent.super.clusterSearch(id, clusterSearchCache, componentFailCache, assumedValidCache);
	}
	
	@Override
	public void tryPriming(FissionReactor sourceReactor, boolean fromSource) {
		if (getMultiblock() != sourceReactor) {
			return;
		}
		
		if (canProcessInputs) {
			primed = true;
		}
	}
	
	@Override
	public boolean isPrimed() {
		return primed;
	}
	
	@Override
	public void addToPrimedCache(final ObjectSet<IFissionFuelComponent> primedCache) {
		primedCache.add(this);
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
		this.sourceEfficiency = this.sourceEfficiency != null && maximize ? Math.max(this.sourceEfficiency, sourceEfficiency) : sourceEfficiency;
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
	public LongSet[] getActiveModeratorCaches() {
		return activeModeratorCaches;
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
	public LongSet[] getActiveReflectorModeratorCaches() {
		return activeReflectorModeratorCaches;
	}
	
	@Override
	public LongSet getActiveReflectorCache() {
		return activeReflectorCache;
	}
	
	@Override
	public long getRawHeating() {
		return baseProcessHeat * heatMult;
	}
	
	@Override
	public double getEffectiveHeating() {
		return baseProcessHeat * getEfficiency();
	}
	
	@Override
	public long getHeatMultiplier() {
		return heatMult;
	}
	
	@Override
	public double getFluxEfficiencyFactor() {
		return (1D + Math.exp(-2D * baseProcessCriticality)) / (1D + Math.exp(2D * (flux - 2D * baseProcessCriticality)));
	}
	
	@Override
	public double getEfficiency() {
		return heatMult * baseProcessEfficiency * getSourceEfficiency() * getModeratorEfficiencyFactor() * getFluxEfficiencyFactor();
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
	public void onClusterMeltdown(Iterator<IFissionComponent> componentIterator) {
		IRadiationSource chunkSource = RadiationHelper.getRadiationSource(world.getChunk(pos));
		if (chunkSource != null) {
			RadiationHelper.addToSourceRadiation(chunkSource, 8D * baseProcessRadiation * getSpeedMultiplier() * fission_meltdown_radiation_multiplier);
		}
		
		componentIterator.remove();
		world.removeTileEntity(pos);
		
		IBlockState corium = FluidRegistry.getFluid("corium").getBlock().getDefaultState();
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
	
	// Ticking
	
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
			
			sendUpdateToListeningPlayers();
			if (shouldUpdate) {
				markDirty();
			}
		}
	}
	
	@Override
	public void refreshRecipe() {
		recipeInfo = NCRecipes.solid_fission.getRecipeInfoFromInputs(getItemInputs(hasConsumed), new ArrayList<>());
		consumeInputs();
	}
	
	@Override
	public void refreshActivity() {
		boolean wasReady = readyToProcess(false);
		canProcessInputs = canProcessInputs();
		if (/* selfPriming && */ getMultiblock() != null && !wasReady && readyToProcess(false)) {
			getMultiblock().refreshFlag = true;
		}
	}
	
	@Override
	public void refreshActivityOnProduction() {
		canProcessInputs = canProcessInputs();
	}
	
	// Processor Stats
	
	public double getSpeedMultiplier() {
		return 1D / (fission_fuel_time_multiplier * undercoolingLifetimeFactor);
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
		if (world.isRemote) {
			return hasConsumed;
		}
		for (int i = 0; i < itemInputSize; i++) {
			if (!consumedStacks.get(i).isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean canProcessInputs() {
		boolean validRecipe = setRecipeStats(), canProcess = validRecipe && canProduceProducts();
		if (hasConsumed && !validRecipe) {
			for (int i = 0; i < itemInputSize; i++) {
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
		for (int j = 0; j < itemOutputSize; j++) {
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
		
		for (int i = 0; i < itemInputSize; i++) {
			if (!consumedStacks.get(i).isEmpty()) {
				consumedStacks.set(i, ItemStack.EMPTY);
			}
		}
		for (int i = 0; i < itemInputSize; i++) {
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
		getRadiationSource().setRadiationLevel(baseProcessRadiation * getSpeedMultiplier());
		while (time >= baseProcessTime) {
			finishProcess();
		}
	}
	
	public void finishProcess() {
		double oldProcessTime = baseProcessTime, oldProcessEfficiency = baseProcessEfficiency;
		int oldProcessHeat = baseProcessHeat, oldProcessCriticality = baseProcessCriticality;
		produceProducts();
		refreshRecipe();
		time = Math.max(0D, time - oldProcessTime);
		refreshActivityOnProduction();
		if (!canProcessInputs) {
			time = 0;
		}
		
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
				sourceEfficiency = null;
				getMultiblock().refreshFlag = true;
			}
		}
	}
	
	public void produceProducts() {
		for (int i = 0; i < itemInputSize; i++) {
			consumedStacks.set(i, ItemStack.EMPTY);
		}
		
		if (!hasConsumed || recipeInfo == null) {
			return;
		}
		
		for (int j = 0; j < itemOutputSize; j++) {
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
		return smart_processor_input ? NCRecipes.solid_fission.isValidItemInput(stack, getInventoryStacks().get(slot), inputItemStacksExcludingSlot(slot)) : NCRecipes.solid_fission.isValidItemInput(stack);
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
		return true;
	}
	
	@Override
	public void clearAllSlots() {
		ITileFilteredInventory.super.clearAllSlots();
		for (int i = 0; i < consumedStacks.size(); i++) {
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
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		writeInventory(nbt);
		writeInventoryConnections(nbt);
		
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
		
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readInventory(nbt);
		readInventoryConnections(nbt);
		
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
				return (T) getItemHandler(side);
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
