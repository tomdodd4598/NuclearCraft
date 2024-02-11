package nc.tile.processor;

import java.util.*;

import javax.annotation.*;

import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import nc.ModCheck;
import nc.handler.TileInfoHandler;
import nc.network.tile.ProcessorUpdatePacket;
import nc.recipe.*;
import nc.tile.energy.ITileEnergy;
import nc.tile.energyFluid.TileEnergyFluidSidedInventory;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.*;
import nc.tile.internal.inventory.InventoryConnection;
import nc.tile.inventory.ITileInventory;
import nc.tile.processor.info.ProcessorContainerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

public abstract class TileProcessor<TILE extends TileProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorContainerInfo<TILE, PACKET, INFO>> extends TileEnergyFluidSidedInventory implements IProcessor<TILE, PACKET, INFO> {
	
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
		this(name, TileInfoHandler.<TILE, PACKET, INFO>getProcessorContainerInfo(name));
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
			onTick();
		}
	}
	
	@Override
	public void refreshDirty() {
		IProcessor.super.refreshDirty();
		refreshEnergyCapacity();
	}
	
	public void refreshEnergyCapacity() {}
	
	// Processor Stats
	
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
		return baseProcessPower;
	}
	
	@Override
	public void setBaseProcessPower(double baseProcessPower) {
		this.baseProcessPower = baseProcessPower;
	}
	
	@Override
	public void setRecipeStats(@Nullable BasicRecipe recipe) {
		IProcessor.super.setRecipeStats(recipe);
		baseProcessRadiation = recipe == null ? 0D : recipe.getBaseProcessRadiation();
	}
	
	// Processing
	
	@Override
	public boolean isProcessing() {
		return IProcessor.super.isProcessing() && !isHalted();
	}
	
	@Override
	public boolean isHalted() {
		return getRedstoneControl() && getIsRedstonePowered();
	}
	
	@Override
	public boolean readyToProcess() {
		return IProcessor.super.readyToProcess() && hasSufficientEnergy();
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
		getEnergyStorage().changeEnergyStored(-getProcessPower());
		getRadiationSource().setRadiationLevel(baseProcessRadiation * getSpeedMultiplier());
		IProcessor.super.process();
	}
	
	// IProcessor
	
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
	public void setRecipeInfo(RecipeInfo<BasicRecipe> recipeInfo) {
		this.recipeInfo = recipeInfo;
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
	public void markDirty() {
		refreshDirty();
		super.markDirty();
	}
	
	@Override
	public boolean hasConfigurableInventoryConnections() {
		return true;
	}
	
	// ITileFluid
	
	@Override
	public boolean hasConfigurableFluidConnections() {
		return true;
	}
	
	// IGui
	
	@Override
	public Set<EntityPlayer> getTileUpdatePacketListeners() {
		return updatePacketListeners;
	}
	
	@Override
	public void onTileUpdatePacket(PACKET message) {
		IProcessor.super.onTileUpdatePacket(message);
		getEnergyStorage().setEnergyStored(message.energyStored);
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		writeProcessorNBT(nbt);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readProcessorNBT(nbt);
	}
}
