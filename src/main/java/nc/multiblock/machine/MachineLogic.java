package nc.multiblock.machine;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.*;
import nc.ModCheck;
import nc.capability.radiation.source.*;
import nc.config.NCConfig;
import nc.handler.SoundHandler;
import nc.multiblock.*;
import nc.network.multiblock.*;
import nc.recipe.*;
import nc.recipe.ingredient.*;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.energy.EnergyStorage;
import nc.tile.internal.fluid.*;
import nc.tile.internal.fluid.Tank.TankInfo;
import nc.tile.internal.inventory.*;
import nc.tile.inventory.ITileInventory;
import nc.tile.machine.*;
import nc.tile.multiblock.TilePartAbstract.SyncReason;
import nc.util.*;
import net.minecraft.client.audio.ISound;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.*;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.*;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class MachineLogic extends MultiblockLogic<Machine, MachineLogic, IMachinePart> implements IPacketMultiblockLogic<Machine, MachineLogic, IMachinePart, MachineUpdatePacket> {
	
	public final IRadiationSource radiation = new RadiationSource(0D);
	
	public @Nonnull EnergyStorage energyStorage = new EnergyStorage(1);
	
	public @Nonnull List<Tank> reservoirTanks = Lists.newArrayList(new Tank(1, null));
	
	public BasicRecipeHandler recipeHandler;
	
	public int itemInputSize, itemOutputSize, fluidInputSize, fluidOutputSize;
	
	public @Nonnull InventoryStackList inventoryStacks = new InventoryStackList(new ArrayList<>());
	public @Nonnull List<Tank> tanks = Collections.emptyList();
	
	public @Nonnull List<InventoryConnection[]> inventoryConnections = Collections.emptyList();
	public @Nonnull List<FluidConnection[]> fluidConnections = Collections.emptyList();
	
	public boolean consumesInputs;
	
	public @Nonnull NonNullList<ItemStack> consumedStacks = NonNullList.withSize(0, ItemStack.EMPTY);
	public @Nonnull List<Tank> consumedTanks = Collections.emptyList();
	
	public double baseProcessTime, baseProcessPower, baseProcessRadiation;
	
	public double time, resetTime;
	public boolean isProcessing, canProcessInputs, hasConsumed;
	public int productionCount;
	
	public double baseSpeedMultiplier, basePowerMultiplier;
	
	public RecipeInfo<BasicRecipe> recipeInfo = null;
	
	public RecipeUnitInfo recipeUnitInfo = RecipeUnitInfo.DEFAULT;
	
	protected double prevSpeedMultiplier = 0D;
	
	public MachineLogic(Machine machine) {
		super(machine);
		constructorInit();
	}
	
	public MachineLogic(MachineLogic oldLogic) {
		super(oldLogic);
		
		String id = getID();
		if (id.isEmpty() || id.equals(oldLogic.getID())) {
			energyStorage = oldLogic.energyStorage;
			
			reservoirTanks = oldLogic.reservoirTanks;
			
			recipeHandler = oldLogic.recipeHandler;
			
			itemInputSize = oldLogic.itemInputSize;
			itemOutputSize = oldLogic.itemOutputSize;
			fluidInputSize = oldLogic.fluidInputSize;
			fluidOutputSize = oldLogic.fluidOutputSize;
			
			inventoryStacks = oldLogic.inventoryStacks;
			tanks = oldLogic.tanks;
			
			inventoryConnections = oldLogic.inventoryConnections;
			fluidConnections = oldLogic.fluidConnections;
			
			consumesInputs = oldLogic.consumesInputs;
			
			consumedStacks = oldLogic.consumedStacks;
			consumedTanks = oldLogic.consumedTanks;
			
			baseProcessTime = oldLogic.baseProcessTime;
			baseProcessPower = oldLogic.baseProcessPower;
			baseProcessRadiation = oldLogic.baseProcessRadiation;
			
			time = oldLogic.time;
			resetTime = oldLogic.resetTime;
			isProcessing = oldLogic.isProcessing;
			if (isProcessing) {
				multiblock.refreshSounds = true;
			}
			canProcessInputs = oldLogic.canProcessInputs;
			hasConsumed = oldLogic.hasConsumed;
			productionCount = oldLogic.productionCount;
			
			baseSpeedMultiplier = oldLogic.baseSpeedMultiplier;
			basePowerMultiplier = oldLogic.basePowerMultiplier;
			
			recipeInfo = oldLogic.recipeInfo;
			
			recipeUnitInfo = oldLogic.recipeUnitInfo;
			
			prevSpeedMultiplier = oldLogic.prevSpeedMultiplier;
		}
		else {
			constructorInit();
		}
	}
	
	@Override
	public String getID() {
		return "";
	}
	
	protected void constructorInit() {
		int volume = multiblock.getInteriorVolume();
		energyStorage = new EnergyStorage(volume * energyDensity());
		
		int tankCapacity = volume * tankDensity();
		
		List<Set<String>> reservoirValidFluids = getReservoirValidFluids();
		for (int i = 0, len = reservoirTanks.size(); i < len; ++i) {
			reservoirTanks.set(i, new Tank(tankCapacity, reservoirValidFluids == null || reservoirValidFluids.size() <= i ? null : reservoirValidFluids.get(i)));
		}
		
		recipeHandler = getRecipeHandler();
		
		itemInputSize = recipeHandler == null ? 0 : recipeHandler.itemInputSize;
		itemOutputSize = recipeHandler == null ? 0 : recipeHandler.itemOutputSize;
		fluidInputSize = recipeHandler == null ? 0 : recipeHandler.fluidInputSize;
		fluidOutputSize = recipeHandler == null ? 0 : recipeHandler.fluidOutputSize;
		
		inventoryStacks = InventoryStackList.withSize(inventorySize());
		
		tanks = new ArrayList<>();
		List<Set<String>> validFluids = recipeHandler == null ? null : recipeHandler.validFluids;
		for (int i = 0, len = tankCount(); i < len; ++i) {
			tanks.add(new Tank(tankCapacity, validFluids == null || validFluids.size() <= i ? null : validFluids.get(i)));
		}
		
		List<ItemSorption> itemSorptions = new ArrayList<>();
		for (int i = 0; i < itemInputSize; ++i) {
			itemSorptions.add(ItemSorption.IN);
		}
		for (int i = 0; i < itemOutputSize; ++i) {
			itemSorptions.add(ItemSorption.OUT);
		}
		
		inventoryConnections = StreamHelper.map(itemSorptions, x -> ITileInventory.inventoryConnectionAll(Collections.singletonList(x)));
		
		List<TankSorption> tankSorptions = new ArrayList<>();
		for (int i = 0; i < fluidInputSize; ++i) {
			tankSorptions.add(TankSorption.IN);
		}
		for (int i = 0; i < fluidOutputSize; ++i) {
			tankSorptions.add(TankSorption.OUT);
		}
		
		fluidConnections = StreamHelper.map(tankSorptions, x -> ITileFluid.fluidConnectionAll(Collections.singletonList(x)));
		
		baseProcessTime = defaultProcessTime();
		baseProcessPower = defaultProcessPower();
		
		consumesInputs = getConsumesInputs();
		
		consumedStacks = NonNullList.withSize(consumesInputs ? itemInputSize : 0, ItemStack.EMPTY);
		consumedTanks = new ArrayList<>();
		if (consumesInputs) {
			for (int i = 0; i < fluidInputSize; ++i) {
				consumedTanks.add(new Tank(tankCapacity, new ObjectOpenHashSet<>()));
			}
		}
	}
	
	public List<Set<String>> getReservoirValidFluids() {
		return null;
	}
	
	public BasicRecipeHandler getRecipeHandler() {
		return null;
	}
	
	public boolean getConsumesInputs() {
		return false;
	}
	
	public double defaultProcessTime() {
		return 1D;
	}
	
	public double defaultProcessPower() {
		return 0D;
	}
	
	public long energyDensity() {
		return (long) (Math.ceil(defaultProcessTime()) * Math.ceil(defaultProcessPower()));
	}
	
	public int inventorySize() {
		return itemInputSize + itemOutputSize;
	}
	
	public int combinedInventorySize() {
		return 36 + inventorySize();
	}
	
	public int tankCount() {
		return fluidInputSize + fluidOutputSize;
	}
	
	public int tankDensity() {
		return 1000;
	}
	
	public boolean losesProgress() {
		return false;
	}
	
	public boolean isGenerator() {
		return false;
	}
	
	// Multiblock Size Limits
	
	@Override
	public int getMinimumInteriorLength() {
		return NCConfig.machine_min_size;
	}
	
	@Override
	public int getMaximumInteriorLength() {
		return NCConfig.machine_max_size;
	}
	
	// Multiblock Methods
	
	@Override
	public void onMachineAssembled() {
		onMachineFormed();
	}
	
	@Override
	public void onMachineRestored() {
		onMachineFormed();
	}
	
	protected void onMachineFormed() {
		for (IMachineController<?> contr : getParts(IMachineController.class)) {
			multiblock.controller = contr;
			break;
		}
		
		if (!getWorld().isRemote) {
			setupMachine();
			refreshAll();
			setIsMachineOn(isProcessing);
			
			for (TileMachineProcessPort port : getParts(TileMachineProcessPort.class)) {
				port.setItemFluidData();
			}
		}
	}
	
	protected void setupMachine() {
		int volume = multiblock.getInteriorVolume();
		long energyCapacity = volume * energyDensity();
		energyStorage.setStorageCapacity(energyCapacity);
		energyStorage.setMaxTransfer(energyCapacity);
		
		int tankCapacity = volume * tankDensity();
		Consumer<Tank> setupTank = x -> {
			x.setTankCapacity(tankCapacity);
			x.clampTankAmount();
		};
		reservoirTanks.forEach(setupTank);
		tanks.forEach(setupTank);
		consumedTanks.forEach(setupTank);
	}
	
	@Override
	public void onMachinePaused() {
		onMachineBroken();
	}
	
	@Override
	public void onMachineDisassembled() {
		onMachineBroken();
	}
	
	public void onMachineBroken() {
		setIsMachineOn(false);
	}
	
	@Override
	public boolean isMachineWhole() {
		if (containsBlacklistedPart()) {
			return false;
		}
		
		for (IMachineController<?> controller : getParts(IMachineController.class)) {
			controller.setIsRenderer(false);
		}
		for (IMachineController<?> controller : getParts(IMachineController.class)) {
			controller.setIsRenderer(true);
			break;
		}
		
		return true;
	}
	
	@Override
	public List<Pair<Class<? extends IMachinePart>, String>> getPartBlacklist() {
		return Collections.emptyList();
	}
	
	@Override
	public void onAssimilate(Machine assimilated) {
		MachineLogic other = assimilated == null ? null : assimilated.getLogic();
		if (other != null) {
			energyStorage.mergeEnergyStorage(other.energyStorage);
			
			if (getID().equals(other.getID())) {
				for (int i = 0, len = reservoirTanks.size(); i < len; ++i) {
					reservoirTanks.get(i).mergeTank(other.reservoirTanks.get(i));
				}
				for (int i = 0, len = tanks.size(); i < len; ++i) {
					tanks.get(i).mergeTank(other.tanks.get(i));
				}
			}
		}
	}
	
	@Override
	public void onAssimilated(Machine assimilator) {}
	
	// Server
	
	@Override
	public boolean onUpdateServer() {
		boolean wasProcessing = isProcessing;
		isProcessing = isProcessing();
		
		if (isProcessing) {
			process();
		}
		else {
			radiation.setRadiationLevel(0D);
			if (time > 0D) {
				if (losesProgress() && !isHalted()) {
					loseProgress();
				}
				else if (!canProcessInputs) {
					time = resetTime = 0D;
				}
			}
		}
		
		if (wasProcessing == isProcessing) {
			multiblock.sendMultiblockUpdatePacketToListeners();
			multiblock.sendRenderPacketToAll();
			return false;
		}
		else {
			setIsMachineOn(isProcessing);
			return true;
		}
	}
	
	public void setActivity(boolean isMachineOn) {
		multiblock.controller.setActivity(isMachineOn);
	}
	
	public void setIsMachineOn(boolean isMachineOn) {
		boolean oldIsMachineOn = multiblock.isMachineOn;
		multiblock.isMachineOn = isMachineOn;
		if (multiblock.isMachineOn != oldIsMachineOn) {
			if (multiblock.controller != null) {
				setActivity(multiblock.isMachineOn);
				multiblock.sendMultiblockUpdatePacketToAll();
			}
		}
	}
	
	protected void loseProgress() {
		double newTime = MathHelper.clamp(time - 1.5D * Math.max(baseSpeedMultiplier, getSpeedMultiplier()), 0D, baseProcessTime);
		time = newTime;
		if (newTime < resetTime) {
			resetTime = newTime;
		}
	}
	
	protected boolean setRecipeStats() {
		setRecipeStats(recipeInfo == null ? null : recipeInfo.recipe);
		if (recipeInfo == null) {
			if (productionCount > 0) {
				recipeUnitInfo = recipeUnitInfo.withRateMultiplier(recipeUnitInfo.rateMultiplier / (1D + 1D / productionCount));
			}
			else {
				recipeUnitInfo = RecipeUnitInfo.DEFAULT;
			}
		}
		else {
			recipeUnitInfo = recipeInfo.getRecipeUnitInfo(1D);
		}
		return recipeInfo != null;
	}
	
	protected void setRecipeStats(@Nullable BasicRecipe recipe) {
		if (recipe == null) {
			baseProcessTime = defaultProcessTime();
			baseProcessPower = defaultProcessPower();
		}
		else {
			baseProcessTime = recipe.getBaseProcessTime(defaultProcessTime());
			baseProcessPower = recipe.getBaseProcessPower(defaultProcessPower());
		}
	}
	
	public List<ItemStack> getItemInputs(boolean consumed) {
		return consumed ? consumedStacks : inventoryStacks.subList(0, itemInputSize);
	}
	
	public List<Tank> getFluidInputs(boolean consumed) {
		return consumed ? consumedTanks : tanks.subList(0, fluidInputSize);
	}
	
	public List<ItemStack> getItemOutputs() {
		return itemOutputSize == 0 ? Collections.emptyList() : inventoryStacks.subList(itemInputSize, itemInputSize + itemOutputSize);
	}
	
	public List<Tank> getFluidOutputs() {
		return fluidOutputSize == 0 ? Collections.emptyList() : tanks.subList(fluidInputSize, fluidInputSize + fluidOutputSize);
	}
	
	protected List<IItemIngredient> getItemIngredients() {
		return recipeInfo.recipe.getItemIngredients();
	}
	
	protected List<IFluidIngredient> getFluidIngredients() {
		return recipeInfo.recipe.getFluidIngredients();
	}
	
	protected List<IItemIngredient> getItemProducts() {
		return recipeInfo.recipe.getItemProducts();
	}
	
	protected List<IFluidIngredient> getFluidProducts() {
		return recipeInfo.recipe.getFluidProducts();
	}
	
	protected long getEnergyCapacity() {
		return multiblock.getInteriorVolume() * energyDensity();
	}
	
	protected double getSpeedMultiplier() {
		return baseSpeedMultiplier;
	}
	
	protected double getPowerMultiplier() {
		return basePowerMultiplier;
	}
	
	public double getProcessTimeFP() {
		return baseProcessTime / getSpeedMultiplier();
	}
	
	public long getProcessTime() {
		double processTimeFP = getProcessTimeFP();
		return processTimeFP >= Long.MAX_VALUE ? Long.MAX_VALUE : Math.max(1L, (long) Math.ceil(processTimeFP));
	}
	
	public double getProcessPowerFP() {
		return baseProcessPower * getPowerMultiplier();
	}
	
	public long getProcessPower() {
		double processPowerFP = getProcessPowerFP();
		return processPowerFP >= Long.MAX_VALUE ? Long.MAX_VALUE : (long) Math.ceil(processPowerFP);
	}
	
	public double getProcessEnergyFP() {
		return getProcessTimeFP() * getProcessPowerFP();
	}
	
	public long getProcessEnergy() {
		double processEnergyFP = getProcessEnergyFP();
		return processEnergyFP >= Long.MAX_VALUE ? Long.MAX_VALUE : (long) Math.ceil(processEnergyFP);
	}
	
	protected boolean isProcessing() {
		return readyToProcess() && !isHalted();
	}
	
	protected boolean isHalted() {
		return multiblock.fullHalt || Stream.concat(Stream.of(multiblock.controller), getParts(TileMachineRedstonePort.class).stream()).anyMatch(x -> x != null && x.checkIsRedstonePowered(getWorld(), x.getTilePos()));
	}
	
	protected boolean readyToProcess() {
		return canProcessInputs && (!consumesInputs || hasConsumed) && (isGenerator() || hasSufficientEnergy());
	}
	
	// Needed for Galacticraft
	protected long getMaxEnergyModified() {
		return ModCheck.galacticraftLoaded() ? Math.max(0L, energyStorage.getMaxEnergyStoredLong() - 16L) : energyStorage.getMaxEnergyStoredLong();
	}
	
	public boolean hasSufficientEnergy() {
		if (time <= resetTime) {
			long processEnergy = getProcessEnergy(), maxEnergy = getMaxEnergyModified();
			if (processEnergy >= maxEnergy) {
				return energyStorage.getEnergyStoredLong() >= maxEnergy;
			}
			else {
				return energyStorage.getEnergyStoredLong() >= processEnergy;
			}
		}
		else {
			return energyStorage.getEnergyStoredLong() >= getProcessPower();
		}
	}
	
	protected boolean canProcessInputs() {
		boolean validRecipe = setRecipeStats();
		if (hasConsumed && !validRecipe) {
			List<ItemStack> itemInputs = getItemInputs(true);
			for (int i = 0; i < itemInputSize; ++i) {
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
	
	protected void process() {
		double speedMultiplier = getSpeedMultiplier();
		energyStorage.changeEnergyStored(isGenerator() ? getProcessPower() : -getProcessPower());
		radiation.setRadiationLevel(baseProcessRadiation * speedMultiplier);
		
		time += speedMultiplier;
		while (time >= baseProcessTime) {
			finishProcess();
		}
		productionCount = 0;
	}
	
	protected void finishProcess() {
		++productionCount;
		double oldProcessTime = baseProcessTime;
		produceProducts();
		refreshRecipe();
		time = resetTime = Math.max(0D, time - oldProcessTime);
		refreshActivityOnProduction();
		if (!canProcessInputs) {
			time = resetTime = 0D;
		}
	}
	
	protected boolean hasConsumed() {
		if (!consumesInputs) {
			return false;
		}
		
		if (getWorld().isRemote) {
			return hasConsumed;
		}
		
		for (ItemStack stack : consumedStacks) {
			if (!stack.isEmpty()) {
				return true;
			}
		}
		for (Tank tank : consumedTanks) {
			if (!tank.isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean canProduceProducts() {
		for (int i = 0; i < itemOutputSize; ++i) {
			int slot = itemInputSize + i;
			
			IItemIngredient product = getItemProducts().get(i);
			int productMaxStackSize = product.getMaxStackSize(0);
			ItemStack productStack = product.getStack();
			
			if (productMaxStackSize <= 0) {
				continue;
			}
			if (productStack == null || productStack.isEmpty()) {
				return false;
			}
			else {
				ItemStack stack = inventoryStacks.get(slot);
				if (!stack.isEmpty()) {
					if (!stack.isItemEqual(productStack)) {
						return false;
					}
					else if (stack.getCount() + productMaxStackSize > getItemProductCapacity(slot, stack)) {
						return false;
					}
				}
			}
		}
		
		for (int i = 0; i < fluidOutputSize; ++i) {
			int tankIndex = fluidInputSize + i;
			
			IFluidIngredient product = getFluidProducts().get(i);
			int productMaxStackSize = product.getMaxStackSize(0);
			FluidStack productStack = product.getStack();
			
			if (productMaxStackSize <= 0) {
				continue;
			}
			if (productStack == null) {
				return false;
			}
			else {
				Tank tank = tanks.get(tankIndex);
				if (!tank.isEmpty()) {
					if (!tank.getFluid().isFluidEqual(productStack)) {
						return false;
					}
					else if (tank.getFluidAmount() + productMaxStackSize > getFluidProductCapacity(tank, productStack)) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	protected int getItemProductCapacity(int slot, ItemStack stack) {
		return stack.getMaxStackSize();
	}
	
	protected int getFluidProductCapacity(Tank tank, FluidStack stack) {
		return tank.getCapacity();
	}
	
	protected void consumeInputs() {
		if (hasConsumed || recipeInfo == null) {
			return;
		}
		
		IntList itemInputOrder = recipeInfo.getItemInputOrder();
		if (itemInputOrder == AbstractRecipeHandler.INVALID) {
			return;
		}
		
		IntList fluidInputOrder = recipeInfo.getFluidInputOrder();
		if (fluidInputOrder == AbstractRecipeHandler.INVALID) {
			return;
		}
		
		if (consumesInputs) {
			for (int i = 0; i < itemInputSize; ++i) {
				consumedStacks.set(i, ItemStack.EMPTY);
			}
			for (Tank tank : consumedTanks) {
				tank.setFluidStored(null);
			}
		}
		
		for (int i = 0; i < itemInputSize; ++i) {
			int itemIngredientStackSize = getItemIngredients().get(itemInputOrder.get(i)).getMaxStackSize(recipeInfo.getItemIngredientNumbers().get(i));
			ItemStack stack = inventoryStacks.get(i);
			
			if (itemIngredientStackSize > 0) {
				if (consumesInputs) {
					consumedStacks.set(i, new ItemStack(stack.getItem(), itemIngredientStackSize, StackHelper.getMetadata(stack)));
				}
				stack.shrink(itemIngredientStackSize);
			}
			
			if (stack.getCount() <= 0) {
				inventoryStacks.set(i, ItemStack.EMPTY);
			}
		}
		
		for (int i = 0; i < fluidInputSize; ++i) {
			Tank tank = tanks.get(i);
			int fluidIngredientStackSize = getFluidIngredients().get(fluidInputOrder.get(i)).getMaxStackSize(recipeInfo.getFluidIngredientNumbers().get(i));
			if (fluidIngredientStackSize > 0) {
				if (consumesInputs) {
					consumedTanks.get(i).setFluidStored(new FluidStack(tank.getFluid(), fluidIngredientStackSize));
				}
				tank.changeFluidAmount(-fluidIngredientStackSize);
			}
			if (tank.getFluidAmount() <= 0) {
				tank.setFluidStored(null);
			}
		}
		
		if (consumesInputs) {
			hasConsumed = true;
		}
	}
	
	protected void produceProducts() {
		if (consumesInputs) {
			for (int i = 0; i < itemInputSize; ++i) {
				consumedStacks.set(i, ItemStack.EMPTY);
			}
			for (Tank tank : consumedTanks) {
				tank.setFluidStored(null);
			}
		}
		
		if ((consumesInputs && !hasConsumed) || recipeInfo == null) {
			return;
		}
		
		if (!consumesInputs) {
			consumeInputs();
		}
		
		for (int i = 0; i < itemOutputSize; ++i) {
			int slot = itemInputSize + i;
			
			IItemIngredient product = getItemProducts().get(i);
			
			if (product.getMaxStackSize(0) <= 0) {
				continue;
			}
			
			ItemStack currentStack = inventoryStacks.get(slot);
			ItemStack nextStack = product.getNextStack(0);
			
			if (currentStack.isEmpty()) {
				inventoryStacks.set(slot, nextStack);
			}
			else if (currentStack.isItemEqual(product.getStack())) {
				int count = Math.min(getInventoryStackLimit(), currentStack.getCount() + nextStack.getCount());
				currentStack.setCount(count);
			}
		}
		
		for (int i = 0; i < fluidOutputSize; ++i) {
			int tankIndex = fluidInputSize + i;
			
			IFluidIngredient product = getFluidProducts().get(i);
			
			if (product.getMaxStackSize(0) <= 0) {
				continue;
			}
			
			Tank tank = tanks.get(tankIndex);
			FluidStack nextStack = product.getNextStack(0);
			
			if (tank.isEmpty()) {
				tank.setFluidStored(nextStack);
			}
			else if (tank.getFluid().isFluidEqual(product.getStack())) {
				tank.changeFluidAmount(nextStack.amount);
			}
		}
		
		if (consumesInputs) {
			hasConsumed = false;
		}
	}
	
	public void refreshAll() {
		refreshDirty();
		isProcessing = isProcessing();
		hasConsumed = hasConsumed();
	}
	
	public void refreshDirty() {
		refreshRecipe();
		refreshActivity();
	}
	
	public void refreshRecipe() {
		recipeInfo = recipeHandler == null ? null : recipeHandler.getRecipeInfoFromInputs(getItemInputs(hasConsumed), getFluidInputs(hasConsumed));
		if (consumesInputs) {
			consumeInputs();
		}
	}
	
	public void refreshActivity() {
		canProcessInputs = canProcessInputs();
	}
	
	protected void refreshActivityOnProduction() {
		canProcessInputs = canProcessInputs();
	}
	
	protected int getInventoryStackLimit() {
		return 64;
	}
	
	// Component Logic
	
	public @Nonnull EnergyStorage getPowerPortEnergyStorage(EnergyStorage backupStorage) {
		return energyStorage;
	}
	
	public int getPowerPortEUSinkTier() {
		return 10;
	}
	
	public int getPowerPortEUSourceTier() {
		return isGenerator() ? EnergyHelper.getEUTier(getProcessPower()) : 1;
	}
	
	public @Nonnull List<Tank> getReservoirPortTanks(List<Tank> backupTanks) {
		return reservoirTanks;
	}
	
	public @Nonnull NonNullList<ItemStack> getProcessPortInventoryStacks(NonNullList<ItemStack> backupStacks, int slot) {
		return multiblock.isAssembled() && slot >= 0 ? inventoryStacks.subList(slot, slot + 1) : backupStacks;
	}
	
	public @Nonnull InventoryConnection[] getProcessPortInventoryConnections(InventoryConnection[] backupInventoryConnections, int slot) {
		return multiblock.isAssembled() && slot >= 0 ? inventoryConnections.get(slot) : backupInventoryConnections;
	}
	
	public @Nonnull List<Tank> getProcessPortTanks(List<Tank> backupTanks, int tankIndex) {
		return multiblock.isAssembled() && tankIndex >= 0 ? tanks.subList(tankIndex, tankIndex + 1) : backupTanks;
	}
	
	public @Nonnull FluidConnection[] getProcessPortFluidConnections(FluidConnection[] backupFluidConnections, int tankIndex) {
		return multiblock.isAssembled() && tankIndex >= 0 ? fluidConnections.get(tankIndex) : backupFluidConnections;
	}
	
	// Client
	
	@Override
	public void onUpdateClient() {}
	
	protected Object2ObjectMap<BlockPos, ISound> getSoundMap() {
		if (multiblock.soundMap == null) {
			multiblock.soundMap = new Object2ObjectOpenHashMap<>();
		}
		return multiblock.soundMap;
	}
	
	@SideOnly(Side.CLIENT)
	protected void clearSounds() {
		getSoundMap().forEach((k, v) -> SoundHandler.stopBlockSound(k));
		getSoundMap().clear();
	}
	
	// NBT
	
	@Override
	public void writeToLogicTag(NBTTagCompound logicTag, SyncReason syncReason) {
		logicTag.setDouble("radiationLevel", radiation.getRadiationLevel());
		
		energyStorage.writeToNBT(logicTag, "energyStorage");
		writeTanks(reservoirTanks, logicTag, "reservoirTanks");
		
		writeStacks(inventoryStacks, logicTag);
		writeTanks(tanks, logicTag, "tanks");
		
		logicTag.setDouble("time", time);
		logicTag.setDouble("resetTime", resetTime);
		logicTag.setBoolean("isProcessing", isProcessing);
		logicTag.setBoolean("canProcessInputs", canProcessInputs);
		logicTag.setBoolean("hasConsumed", hasConsumed);
		logicTag.setInteger("productionCount", productionCount);
		
		logicTag.setDouble("baseSpeedMultiplier", baseSpeedMultiplier);
		logicTag.setDouble("basePowerMultiplier", basePowerMultiplier);
		
		recipeUnitInfo.writeToNBT(logicTag, "recipeUnitInfo");
	}
	
	@Override
	public void readFromLogicTag(NBTTagCompound logicTag, SyncReason syncReason) {
		radiation.setRadiationLevel(logicTag.getDouble("radiationLevel"));
		
		energyStorage.readFromNBT(logicTag, "energyStorage");
		readTanks(reservoirTanks, logicTag, "reservoirTanks");
		
		readStacks(inventoryStacks, logicTag);
		readTanks(tanks, logicTag, "tanks");
		
		time = logicTag.getDouble("time");
		resetTime = logicTag.getDouble("resetTime");
		isProcessing = logicTag.getBoolean("isProcessing");
		canProcessInputs = logicTag.getBoolean("canProcessInputs");
		hasConsumed = logicTag.getBoolean("hasConsumed");
		productionCount = logicTag.getInteger("productionCount");
		
		baseSpeedMultiplier = logicTag.getDouble("baseSpeedMultiplier");
		basePowerMultiplier = logicTag.getDouble("basePowerMultiplier");
		
		recipeUnitInfo = RecipeUnitInfo.readFromNBT(logicTag, "recipeUnitInfo");
	}
	
	// Packets
	
	@Override
	public MachineUpdatePacket getMultiblockUpdatePacket() {
		return null;
	}
	
	@Override
	public void onMultiblockUpdatePacket(MachineUpdatePacket message) {
		multiblock.isMachineOn = message.isMachineOn;
		isProcessing = message.isProcessing;
		time = message.time;
		baseProcessTime = message.baseProcessTime;
		baseProcessPower = message.baseProcessPower;
		TankInfo.readInfoList(message.tankInfos, tanks);
		baseSpeedMultiplier = message.baseSpeedMultiplier;
		basePowerMultiplier = message.basePowerMultiplier;
		recipeUnitInfo = message.recipeUnitInfo;
	}
	
	public MachineRenderPacket getRenderPacket() {
		return null;
	}
	
	public void onRenderPacket(MachineRenderPacket message) {
		multiblock.isMachineOn = message.isMachineOn;
	}
	
	// Clear Material
	
	@Override
	public void clearAllMaterial() {
		Collections.fill(inventoryStacks, ItemStack.EMPTY);
		for (Tank tank : tanks) {
			tank.setFluidStored(null);
		}
		
		Collections.fill(consumedStacks, ItemStack.EMPTY);
		for (Tank tank : consumedTanks) {
			tank.setFluidStored(null);
		}
		
		for (Tank reservoirTank : reservoirTanks) {
			reservoirTank.setFluidStored(null);
		}
	}
}
