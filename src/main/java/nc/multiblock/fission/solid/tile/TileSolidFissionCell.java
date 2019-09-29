package nc.multiblock.fission.solid.tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import nc.Global;
import nc.capability.radiation.source.IRadiationSource;
import nc.config.NCConfig;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.FissionCluster;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.solid.SolidFissionCellSetting;
import nc.multiblock.fission.tile.IFissionFuelComponent;
import nc.multiblock.fission.tile.TileFissionPartBase;
import nc.radiation.RadiationHelper;
import nc.recipe.AbstractRecipeHandler;
import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.recipe.RecipeInfo;
import nc.recipe.ingredient.IItemIngredient;
import nc.tile.generator.IItemGenerator;
import nc.tile.internal.inventory.InventoryConnection;
import nc.tile.internal.inventory.InventoryTileWrapper;
import nc.tile.internal.inventory.ItemOutputSetting;
import nc.tile.internal.inventory.ItemSorption;
import nc.tile.inventory.ITileInventory;
import nc.util.ItemStackHelper;
import nc.util.RegistryHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileSolidFissionCell extends TileFissionPartBase implements IItemGenerator, ITileInventory, IFissionFuelComponent {
	
	protected final @Nonnull String inventoryName = Global.MOD_ID + ".container.fission_cell";
	
	protected final @Nonnull NonNullList<ItemStack> inventoryStacks = NonNullList.withSize(2, ItemStack.EMPTY);
	protected final @Nonnull NonNullList<ItemStack> consumedStacks = NonNullList.withSize(1, ItemStack.EMPTY);
	
	protected @Nonnull InventoryConnection[] inventoryConnections = ITileInventory.inventoryConnectionAll(Arrays.asList(ItemSorption.NON, ItemSorption.NON));
	
	protected @Nonnull InventoryTileWrapper invWrapper;
	
	protected @Nonnull SolidFissionCellSetting[] cellSettings = new SolidFissionCellSetting[] {SolidFissionCellSetting.DISABLED, SolidFissionCellSetting.DISABLED, SolidFissionCellSetting.DISABLED, SolidFissionCellSetting.DISABLED, SolidFissionCellSetting.DISABLED, SolidFissionCellSetting.DISABLED};
	
	protected final int itemInputSize = 1, itemOutputSize = 1;
	
	protected double baseProcessTime = 1D, baseProcessEfficiency = 0D, baseProcessRadiation = 0D;
	protected int baseProcessHeat = 0, baseProcessCriticality = 1;
	
	protected double time;
	protected boolean isProcessing, hasConsumed, canProcessInputs;
	
	protected static final ProcessorRecipeHandler RECIPE_HANDLER = NCRecipes.solid_fission;
	protected RecipeInfo<ProcessorRecipe> recipeInfo;
	
	protected FissionCluster cluster = null;
	protected boolean primed = false, fluxSearched = false;
	protected int flux = 0, heatMult = 0;
	protected double undercoolingLifetimeFactor = 1D;
	protected Double sourceEfficiency = null;
	protected Double[] moderatorLineEfficiencies = new Double[] {null, null, null, null, null, null};
	
	protected int cellCount;
	
	public TileSolidFissionCell() {
		super(CuboidalPartPositionType.INTERIOR);
		invWrapper = new InventoryTileWrapper(this);
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
	public void setCluster(@Nullable FissionCluster cluster) {
		if (cluster == null && this.cluster != null) {
			this.cluster.getComponentMap().remove(pos.toLong());
		}
		else if (cluster != null) {
			cluster.getComponentMap().put(pos.toLong(), this);
		}
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
		primed = fluxSearched = false;
		flux = heatMult = 0;
		undercoolingLifetimeFactor = 1D;
		sourceEfficiency = null;
		moderatorLineEfficiencies[0] = moderatorLineEfficiencies[1] = moderatorLineEfficiencies[2] = moderatorLineEfficiencies[3] = moderatorLineEfficiencies[4] = moderatorLineEfficiencies[5] = null;
		
		refreshRecipe();
		refreshActivity();
		refreshIsProcessing(true);
	}
	
	@Override
	public void clusterSearch(Integer id) {
		refreshRecipe();
		refreshActivity();
		refreshIsProcessing(false);
		
		IFissionFuelComponent.super.clusterSearch(id);
	}
	
	@Override
	public void tryPriming(FissionReactor sourceReactor) {
		if (getMultiblock() != sourceReactor) return;
		
		if (canProcessInputs) primed = true;
	}
	
	@Override
	public void unprime() {
		primed = false;
	}
	
	@Override
	public boolean isPrimed() {
		return primed;
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
	public void setSourceEfficiency(double sourceEfficiency, boolean maximize) {
		this.sourceEfficiency = (this.sourceEfficiency != null && maximize) ? Math.max(this.sourceEfficiency, sourceEfficiency) : sourceEfficiency;
	}
	
	@Override
	public void resetFlux() {
		flux = 0;
	}
	
	@Override
	public void addFlux(int flux) {
		this.flux += flux;
	}
	
	@Override
	public Double[] getModeratorLineEfficiencies() {
		return moderatorLineEfficiencies;
	}
	
	@Override
	public long getHeating() {
		return baseProcessHeat*heatMult;
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
		return heatMult*baseProcessEfficiency*(sourceEfficiency == null ? 1D : sourceEfficiency)*getModeratorEfficiencyFactor()*getFluxEfficiencyFactor();
	}
	
	@Override
	public void setUndercoolingLifetimeFactor(double undercoolingLifetimeFactor) {
		this.undercoolingLifetimeFactor = undercoolingLifetimeFactor;
	}
	
	@Override
	public void doMeltdown() {
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
	
	// Processing
	
	@Override
	public void onAdded() {
		super.onAdded();
		if (!world.isRemote) {
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
		if(!world.isRemote) {
			boolean wasProcessing = isProcessing;
			isProcessing = isProcessing(true);
			boolean shouldUpdate = false;
			tickCell();
			if (isProcessing) process();
			else getRadiationSource().setRadiationLevel(0D);
			if (wasProcessing != isProcessing) {
				shouldUpdate = true;
			}
			if (cellCount == 0) {
				pushStacks();
			}
			if (shouldUpdate) {
				if (getMultiblock() != null) {
					getMultiblock().refreshFlag = true;
				}
				markDirty();
			}
		}
	}
	
	public void tickCell() {
		cellCount++; cellCount %= NCConfig.machine_update_rate / 2;
	}
	
	@Override
	public void refreshRecipe() {
		recipeInfo = RECIPE_HANDLER.getRecipeInfoFromInputs(getItemInputs(hasConsumed), new ArrayList<>());
		consumeInputs();
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
	
	public double getSpeedMultiplier() {
		return 1D/(NCConfig.fission_fuel_time_multiplier*undercoolingLifetimeFactor);
	}
	
	public boolean setRecipeStats() {
		if (recipeInfo == null) {
			baseProcessTime = 1D;
			baseProcessHeat = 0;
			baseProcessEfficiency = 0D;
			baseProcessCriticality = 1;
			baseProcessRadiation = 0D;
			return false;
		}
		baseProcessTime = recipeInfo.getRecipe().getFissionFuelTime();
		baseProcessHeat = recipeInfo.getRecipe().getFissionFuelHeat();
		baseProcessEfficiency = recipeInfo.getRecipe().getFissionFuelEfficiency();
		baseProcessCriticality = recipeInfo.getRecipe().getFissionFuelCriticality();
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
		
	public boolean canProcessInputs(boolean justProduced) {
		if (!setRecipeStats()) {
			if (hasConsumed) {
				for (int i = 0; i < itemInputSize; i++) getItemInputs(true).set(i, ItemStack.EMPTY);
				hasConsumed = false;
			}
			return false;
		}
		if (!justProduced && time >= baseProcessTime) return true;
		return canProduceProducts();
	}
	
	public boolean canProduceProducts() {
		for(int j = 0; j < itemOutputSize; j++) {
			IItemIngredient itemProduct = getItemProducts().get(j);
			if (itemProduct.getMaxStackSize(0) <= 0) continue;
			if (itemProduct.getStack() == null || itemProduct.getStack() == ItemStack.EMPTY) return false;
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
		List<Integer> itemInputOrder = recipeInfo.getItemInputOrder();
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
		if (time >= baseProcessTime) finishProcess();
	}
	
	public void finishProcess() {
		double oldProcessTime = baseProcessTime, oldProcessEfficiency = baseProcessEfficiency;
		int oldProcessHeat = baseProcessHeat, oldProcessCriticality = baseProcessCriticality;
		produceProducts();
		refreshRecipe();
		if (!setRecipeStats()) time = 0;
		else time = MathHelper.clamp(time - oldProcessTime, 0D, baseProcessTime);
		refreshActivityOnProduction();
		if (!canProcessInputs) time = 0;
		
		if (getMultiblock() != null) {
			if (canProcessInputs) {
				if (oldProcessHeat != baseProcessHeat || oldProcessEfficiency != baseProcessEfficiency || oldProcessCriticality != baseProcessCriticality) {
					if (flux < baseProcessCriticality) {
						getMultiblock().refreshFlag = true;
					}
					else {
						getMultiblock().refreshCluster(cluster.getId());
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
	public List<ItemStack> getItemInputs(boolean consumed) {
		return consumed ? consumedStacks : getInventoryStacks().subList(0, itemInputSize);
	}
	
	@Override
	public List<IItemIngredient> getItemIngredients() {
		return recipeInfo.getRecipe().itemIngredients();
	}
	
	@Override
	public List<IItemIngredient> getItemProducts() {
		return recipeInfo.getRecipe().itemProducts();
	}
	
	// ITileInventory
	
	@Override
	public String getName() {
		return inventoryName;
	}
	
	@Override
	public @Nonnull NonNullList<ItemStack> getInventoryStacks() {
		return inventoryStacks;
	}
	
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack stack = ITileInventory.super.decrStackSize(slot, amount);
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
		ITileInventory.super.setInventorySlotContents(slot, stack);
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
		if (stack == ItemStack.EMPTY || slot >= itemInputSize) return false;
		return NCConfig.smart_processor_input ? RECIPE_HANDLER.isValidItemInput(stack, getInventoryStacks().get(slot), inputItemStacksExcludingSlot(slot)) : RECIPE_HANDLER.isValidItemInput(stack);
	}
	
	public List<ItemStack> inputItemStacksExcludingSlot(int slot) {
		List<ItemStack> inputItemsExcludingSlot = new ArrayList<ItemStack>(getItemInputs(false));
		inputItemsExcludingSlot.remove(slot);
		return inputItemsExcludingSlot;
	}
	
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
		return ITileInventory.super.canInsertItem(slot, stack, side) && isItemValidForSlot(slot, stack);
	}
	
	@Override
	public boolean hasConfigurableInventoryConnections() {
		return true;
	}
	
	@Override
	public void clearAllSlots() {
		ITileInventory.super.clearAllSlots();
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
		return invWrapper;
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
		
		nbt.setDouble("time", time);
		nbt.setBoolean("isProcessing", isProcessing);
		nbt.setBoolean("hasConsumed", hasConsumed);
		nbt.setBoolean("canProcessInputs", canProcessInputs);
		
		nbt.setInteger("flux", flux);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readInventory(nbt);
		readInventoryConnections(nbt);
		readCellSettings(nbt);
		
		baseProcessTime = nbt.getDouble("baseProcessTime");
		
		time = nbt.getDouble("time");
		isProcessing = nbt.getBoolean("isProcessing");
		hasConsumed = nbt.getBoolean("hasConsumed");
		canProcessInputs = nbt.getBoolean("canProcessInputs");
		
		flux = nbt.getInteger("flux");
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
