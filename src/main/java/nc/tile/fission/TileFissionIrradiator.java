package nc.tile.fission;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.*;
import nc.Global;
import nc.handler.TileInfoHandler;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.*;
import nc.network.tile.multiblock.FissionIrradiatorUpdatePacket;
import nc.recipe.*;
import nc.tile.fission.port.*;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.*;
import nc.tile.internal.inventory.*;
import nc.tile.inventory.*;
import nc.tile.processor.IBasicProcessor;
import nc.tile.processor.info.ProcessorContainerInfoImpl;
import nc.util.*;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.*;

import javax.annotation.*;
import java.util.*;
import java.util.stream.IntStream;

import static nc.util.PosHelper.DEFAULT_NON;

public class TileFissionIrradiator extends TileFissionPart implements IBasicProcessor<TileFissionIrradiator, FissionIrradiatorUpdatePacket>, ITileFilteredInventory, IFissionHeatingComponent, IFissionFluxSink, IFissionPortTarget<TileFissionIrradiatorPort, TileFissionIrradiator> {
	
	protected final ProcessorContainerInfoImpl.BasicProcessorContainerInfo<TileFissionIrradiator, FissionIrradiatorUpdatePacket> info;
	
	protected final @Nonnull String inventoryName;
	
	protected final @Nonnull NonNullList<ItemStack> inventoryStacks;
	
	protected final @Nonnull NonNullList<ItemStack> filterStacks;
	
	protected @Nonnull InventoryConnection[] inventoryConnections;
	
	protected final @Nonnull List<Tank> tanks;
	
	protected @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(Collections.emptyList());
	
	protected @Nonnull FluidTileWrapper[] fluidSides = ITileFluid.getDefaultFluidSides(this);
	protected @Nonnull GasTileWrapper gasWrapper = new GasTileWrapper(this);
	
	public double baseProcessTime = 1D, baseProcessHeatPerFlux = 0D, baseProcessEfficiency = 0D, baseProcessRadiation = 0D;
	public long minFluxPerTick = 0, maxFluxPerTick = -1;
	
	public double time, resetTime;
	public boolean isProcessing, canProcessInputs;
	public boolean isRunningSimulated;
	
	protected RecipeInfo<BasicRecipe> recipeInfo = null;
	
	protected final Set<EntityPlayer> updatePacketListeners = new ObjectOpenHashSet<>();
	
	protected FissionCluster cluster = null;
	protected long heat = 0L;
	
	public long clusterHeatStored, clusterHeatCapacity;
	
	protected long flux = 0;
	
	protected BlockPos masterPortPos = DEFAULT_NON;
	protected TileFissionIrradiatorPort masterPort = null;
	
	public TileFissionIrradiator() {
		super(CuboidalPartPositionType.INTERIOR);
		info = TileInfoHandler.getProcessorContainerInfo("fission_irradiator");
		
		inventoryName = Global.MOD_ID + ".container." + info.name;
		
		inventoryStacks = info.getInventoryStacks();
		
		filterStacks = info.getInventoryStacks();
		
		inventoryConnections = ITileInventory.inventoryConnectionAll(info.nonItemSorptions());
		
		tanks = Collections.emptyList();
	}
	
	@Override
	public void onMachineAssembled(FissionReactor multiblock) {
		doStandardNullControllerResponse(multiblock);
		super.onMachineAssembled(multiblock);
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
	public boolean isValidHeatConductor(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
		return isRunning(simulate);
	}
	
	@Override
	public boolean isFunctional(boolean simulate) {
		return isRunning(simulate);
	}
	
	@Override
	public void resetStats() {
		flux = 0;
		refreshAll();
	}
	
	@Override
	public boolean isClusterRoot() {
		return true;
	}
	
	@Override
	public void clusterSearch(Integer id, final Object2IntMap<IFissionComponent> clusterSearchCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
		refreshDirty();
		refreshIsProcessing(false, simulate);
		
		IFissionFluxSink.super.clusterSearch(id, clusterSearchCache, componentFailCache, assumedValidCache, simulate);
	}
	
	@Override
	public void refreshIsProcessing(boolean checkCluster, boolean simulate) {
		if (simulate) {
			isProcessing = false;
			isRunningSimulated = isProcessing(checkCluster, simulate);
		}
		else {
			isProcessing = isProcessing(checkCluster, simulate);
			isRunningSimulated = false;
		}
	}
	
	@Override
	public void onClusterMeltdown(Iterator<IFissionComponent> componentIterator) {
	
	}
	
	@Override
	public boolean isAcceptingFlux(EnumFacing side, boolean simulate) {
		return canProcessInputs;
	}
	
	@Override
	public boolean isNullifyingSources(EnumFacing side, boolean simulate) {
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
	public long getRawHeating(boolean simulate) {
		return (long) Math.min(Long.MAX_VALUE, Math.floor(flux * baseProcessHeatPerFlux));
	}
	
	@Override
	public long getRawHeatingIgnoreCoolingPenalty(boolean simulate) {
		return 0L;
	}
	
	@Override
	public double getEffectiveHeating(boolean simulate) {
		return flux * baseProcessHeatPerFlux * baseProcessEfficiency;
	}
	
	@Override
	public double getEffectiveHeatingIgnoreCoolingPenalty(boolean simulate) {
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
		FissionReactor multiblock = getMultiblock();
		masterPort = multiblock == null ? null : multiblock.getPartMap(TileFissionIrradiatorPort.class).get(masterPortPos.toLong());
		if (masterPort == null) {
			masterPortPos = DEFAULT_NON;
		}
	}
	
	@Override
	public void onPortRefresh() {
		refreshAll();
	}
	
	// Ticking
	
	@Override
	public void onLoad() {
		super.onLoad();
		if (!world.isRemote) {
			refreshMasterPort();
			refreshAll();
		}
	}
	
	@Override
	public void update() {
		if (!world.isRemote) {
			FissionReactor reactor = getMultiblock();
			boolean shouldRefresh = reactor != null && reactor.isReactorOn && cluster == null && isProcessing(false, false);
			
			if (onTick()) {
				markDirty();
			}
			
			if (shouldRefresh) {
				getMultiblock().refreshFlag = true;
			}
		}
	}
	
	@Override
	public void refreshAll() {
		refreshDirty();
		refreshIsProcessing(true, false);
	}
	
	@Override
	public void refreshActivity() {
		boolean wasReady = readyToProcess(false);
		canProcessInputs = canProcessInputs();
		FissionReactor multiblock = getMultiblock();
		if (multiblock != null && !wasReady && readyToProcess(false)) {
			multiblock.refreshFlag = true;
		}
	}
	
	public boolean isRunning(boolean simulate) {
		return simulate ? isRunningSimulated : isProcessing;
	}
	
	// IProcessor
	
	@Override
	public ProcessorContainerInfoImpl.BasicProcessorContainerInfo<TileFissionIrradiator, FissionIrradiatorUpdatePacket> getContainerInfo() {
		return info;
	}
	
	@Override
	public BasicRecipeHandler getRecipeHandler() {
		return NCRecipes.fission_irradiator;
	}
	
	@Override
	public RecipeInfo<BasicRecipe> getRecipeInfo() {
		return recipeInfo;
	}
	
	@Override
	public void setRecipeInfo(RecipeInfo<BasicRecipe> recipeInfo) {
		this.recipeInfo = recipeInfo;
	}
	
	@Override
	public void setRecipeStats(@Nullable BasicRecipe recipe) {
		baseProcessTime = recipe == null ? 1D : recipe.getIrradiatorFluxRequired();
		baseProcessHeatPerFlux = recipe == null ? 0D : recipe.getIrradiatorHeatPerFlux();
		baseProcessEfficiency = recipe == null ? 0D : recipe.getIrradiatorProcessEfficiency();
		minFluxPerTick = recipe == null ? 0L : recipe.getIrradiatorMinFluxPerTick();
		maxFluxPerTick = recipe == null ? -1L : recipe.getIrradiatorMaxFluxPerTick();
		baseProcessRadiation = recipe == null ? 0D : recipe.getIrradiatorBaseProcessRadiation();
	}
	
	@Override
	public @Nonnull NonNullList<ItemStack> getConsumedStacks() {
		return getInventoryStacks();
	}
	
	@Override
	public @Nonnull List<Tank> getConsumedTanks() {
		return getTanks();
	}
	
	@Override
	public double getBaseProcessTime() {
		return baseProcessTime;
	}
	
	@Override
	public void setBaseProcessTime(double baseProcessTime) {
		this.baseProcessTime = baseProcessTime;
	}
	
	@Override
	public double getBaseProcessPower() {
		return 0D;
	}
	
	@Override
	public void setBaseProcessPower(double baseProcessPower) {}
	
	@Override
	public double getCurrentTime() {
		return time;
	}
	
	@Override
	public void setCurrentTime(double time) {
		this.time = time;
	}
	
	@Override
	public double getResetTime() {
		return resetTime;
	}
	
	@Override
	public void setResetTime(double resetTime) {
		this.resetTime = resetTime;
	}
	
	@Override
	public boolean getIsProcessing() {
		return isProcessing;
	}
	
	@Override
	public void setIsProcessing(boolean isProcessing) {
		this.isProcessing = isProcessing;
	}
	
	@Override
	public boolean getCanProcessInputs() {
		return canProcessInputs;
	}
	
	@Override
	public void setCanProcessInputs(boolean canProcessInputs) {
		this.canProcessInputs = canProcessInputs;
	}
	
	@Override
	public boolean getHasConsumed() {
		return false;
	}
	
	@Override
	public void setHasConsumed(boolean hasConsumed) {
	
	}
	
	@Override
	public double getSpeedMultiplier() {
		if ((minFluxPerTick >= 0L && flux < minFluxPerTick) || (maxFluxPerTick >= 0L && flux > maxFluxPerTick)) {
			return 0;
		}
		return flux;
	}
	
	@Override
	public double getPowerMultiplier() {
		return 0D;
	}
	
	@Override
	public boolean isProcessing() {
		return !isSimulation() && isProcessing(true, false);
	}
	
	public boolean isProcessing(boolean checkCluster, boolean simulate) {
		return readyToProcess(checkCluster) && flux > 0;
	}
	
	@Override
	public boolean readyToProcess() {
		return readyToProcess(true);
	}
	
	public boolean readyToProcess(boolean checkCluster) {
		return canProcessInputs && isMultiblockAssembled() && (!checkCluster || cluster != null);
	}
	
	@Override
	public void process() {
		getRadiationSource().setRadiationLevel(baseProcessRadiation * getSpeedMultiplier() / RecipeStats.getFissionMaxModeratorLineFlux());
		IBasicProcessor.super.process();
	}
	
	@Override
	public void finishProcess() {
		double oldProcessTime = baseProcessTime, oldProcessHeat = baseProcessHeatPerFlux, oldProcessEfficiency = baseProcessEfficiency;
		produceProducts();
		refreshRecipe();
		time = Math.max(0D, time - oldProcessTime);
		refreshActivityOnProduction();
		if (!canProcessInputs) {
			time = 0;
		}
		
		FissionReactor multiblock = getMultiblock();
		if (multiblock != null) {
			if (canProcessInputs) {
				if (oldProcessHeat != baseProcessHeatPerFlux || oldProcessEfficiency != baseProcessEfficiency) {
					multiblock.addClusterToRefresh(cluster);
				}
			}
			else {
				multiblock.refreshFlag = true;
			}
		}
	}
	
	@Override
	public int getItemProductCapacity(int slot, ItemStack stack) {
		return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getInventoryStackLimit() : IBasicProcessor.super.getItemProductCapacity(slot, stack);
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
	
	@Override
	public void markDirty() {
		refreshDirty();
		super.markDirty();
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (stack.isEmpty() || (slot >= info.itemInputSize && slot < info.itemInputSize + info.itemOutputSize)) {
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
		return IBasicProcessor.super.isItemValidForSlot(slot, stack);
	}
	
	@Override
	public int getInventoryStackLimit() {
		return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getInventoryStackLimit() : IBasicProcessor.super.getInventoryStackLimit();
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
		return !isMultiblockAssembled();
	}
	
	@Override
	public void onFilterChanged(int slot) {
		markDirty();
	}
	
	@Override
	public Object getFilterKey() {
		return getFilterStacks().get(0).isEmpty() ? 0 : RecipeItemHelper.pack(getFilterStacks().get(0));
	}
	
	// ITileFluid
	
	@Override
	public @Nonnull List<Tank> getTanks() {
		return tanks;
	}
	
	@Override
	public @Nonnull FluidConnection[] getFluidConnections() {
		return fluidConnections;
	}
	
	@Override
	public void setFluidConnections(@Nonnull FluidConnection[] connections) {
		fluidConnections = connections;
	}
	
	@Override
	public @Nonnull FluidTileWrapper[] getFluidSides() {
		return fluidSides;
	}
	
	@Override
	public @Nonnull GasTileWrapper getGasWrapper() {
		return gasWrapper;
	}
	
	@Override
	public boolean getInputTanksSeparated() {
		return false;
	}
	
	@Override
	public void setInputTanksSeparated(boolean separated) {}
	
	@Override
	public boolean getVoidUnusableFluidInput(int tankNumber) {
		return false;
	}
	
	@Override
	public void setVoidUnusableFluidInput(int tankNumber, boolean voidUnusableFluidInput) {}
	
	@Override
	public TankOutputSetting getTankOutputSetting(int tankNumber) {
		return TankOutputSetting.DEFAULT;
	}
	
	@Override
	public void setTankOutputSetting(int tankNumber, TankOutputSetting setting) {}
	
	@Override
	public boolean hasConfigurableFluidConnections() {
		return false;
	}
	
	// ITileGui
	
	@Override
	public Set<EntityPlayer> getTileUpdatePacketListeners() {
		return updatePacketListeners;
	}
	
	@Override
	public FissionIrradiatorUpdatePacket getTileUpdatePacket() {
		return new FissionIrradiatorUpdatePacket(pos, isProcessing, time, baseProcessTime, getTanks(), masterPortPos, getFilterStacks(), cluster);
	}
	
	@Override
	public void onTileUpdatePacket(FissionIrradiatorUpdatePacket message) {
		IBasicProcessor.super.onTileUpdatePacket(message);
		if (DEFAULT_NON.equals(masterPortPos = message.masterPortPos) ^ masterPort == null) {
			refreshMasterPort();
		}
		IntStream.range(0, filterStacks.size()).forEach(x -> filterStacks.set(x, message.filterStacks.get(x)));
		clusterHeatStored = message.clusterHeatStored;
		clusterHeatCapacity = message.clusterHeatCapacity;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		writeInventory(nbt);
		writeInventoryConnections(nbt);
		
		writeProcessorNBT(nbt);
		
		nbt.setDouble("baseProcessTime", baseProcessTime);
		nbt.setDouble("baseProcessHeatPerFlux", baseProcessHeatPerFlux);
		nbt.setDouble("baseProcessEfficiency", baseProcessEfficiency);
		
		nbt.setLong("minFluxPerTick", minFluxPerTick);
		nbt.setLong("maxFluxPerTick", maxFluxPerTick);
		
		nbt.setLong("flux", flux);
		nbt.setLong("clusterHeat", heat);
		
		nbt.setBoolean("isRunningSimulated", isRunningSimulated);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readInventory(nbt);
		readInventoryConnections(nbt);
		
		readProcessorNBT(nbt);
		
		baseProcessTime = nbt.getDouble("baseProcessTime");
		baseProcessHeatPerFlux = nbt.getDouble("baseProcessHeatPerFlux");
		baseProcessEfficiency = nbt.getDouble("baseProcessEfficiency");
		
		minFluxPerTick = nbt.getLong("minFluxPerTick");
		maxFluxPerTick = nbt.getLong("maxFluxPerTick");
		
		flux = nbt.getLong("flux");
		heat = nbt.getLong("clusterHeat");
		
		isRunningSimulated = nbt.getBoolean("isRunningSimulated");
	}
	
	@Override
	public NBTTagCompound writeInventory(NBTTagCompound nbt) {
		NBTHelper.writeAllItems(nbt, inventoryStacks, filterStacks);
		return nbt;
	}
	
	@Override
	public void readInventory(NBTTagCompound nbt) {
		NBTHelper.readAllItems(nbt, inventoryStacks, filterStacks);
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
		return ITileFilteredInventory.super.getItemHandler(side);
	}
	
	// OpenComputers
	
	@Override
	public String getOCKey() {
		return "irradiator";
	}
	
	@Override
	public Object getOCInfo() {
		Object2ObjectMap<String, Object> entry = new Object2ObjectLinkedOpenHashMap<>();
		List<ItemStack> stacks = getInventoryStacks();
		entry.put("input", OCHelper.stackInfo(stacks.get(0)));
		entry.put("output", OCHelper.stackInfo(stacks.get(1)));
		entry.put("effective_heating", getEffectiveHeating(false));
		entry.put("is_processing", getIsProcessing());
		entry.put("current_time", getCurrentTime());
		entry.put("base_process_time", getBaseProcessTime());
		entry.put("base_process_heat_per_flux", baseProcessHeatPerFlux);
		entry.put("base_process_efficiency", baseProcessEfficiency);
		entry.put("flux", getFlux());
		return entry;
	}
}
