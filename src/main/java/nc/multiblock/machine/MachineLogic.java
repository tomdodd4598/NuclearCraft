package nc.multiblock.machine;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.*;
import nc.*;
import nc.config.NCConfig;
import nc.handler.SoundHandler;
import nc.multiblock.*;
import nc.network.multiblock.*;
import nc.recipe.*;
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
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.*;
import net.minecraftforge.items.*;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class MachineLogic extends MultiblockLogic<Machine, MachineLogic, IMachinePart> implements IPacketMultiblockLogic<Machine, MachineLogic, IMachinePart, MachineUpdatePacket> {
	
	public MachineLogic(Machine machine) {
		super(machine);
		constructorInit();
	}
	
	public MachineLogic(MachineLogic oldLogic) {
		super(oldLogic);
		
		String id = getID();
		if (!id.isEmpty() && !id.equals(oldLogic.getID())) {
			constructorInit();
		}
		
		if (multiblock.processor.isProcessing) {
			multiblock.refreshSounds = true;
		}
	}
	
	@Override
	public String getID() {
		return "";
	}
	
	protected void constructorInit() {
		int volume = multiblock.getExteriorVolume();
		long energyCapacity = volume * energyDensity();
		int tankCapacity = volume * tankDensity();
		
		multiblock.energyStorage.setStorageCapacity(energyCapacity);
		multiblock.energyStorage.setMaxTransfer(energyCapacity);
		
		InventoryStackList prevReservoirInventoryStacks = multiblock.reservoirInventoryStacks;
		
		multiblock.reservoirInventoryStacks = InventoryStackList.withSize(reservoirInventorySize());
		
		for (int i = 0, len = Math.min(prevReservoirInventoryStacks.size(), multiblock.reservoirInventoryStacks.size()); i < len; ++i) {
			multiblock.reservoirInventoryStacks.set(i, prevReservoirInventoryStacks.get(i));
		}
		
		int nextReservoirTankCount = reservoirTankCount(), prevReservoirTankCount = multiblock.reservoirTanks.size();
		
		List<Set<String>> reservoirValidFluids = getReservoirValidFluids();
		IntFunction<Set<String>> getReservoirValidFluids = x -> reservoirValidFluids != null && x < reservoirValidFluids.size() ? reservoirValidFluids.get(x) : null;
		
		multiblock.reservoirTanks = IntStream.range(0, nextReservoirTankCount).mapToObj(x -> {
			if (x < prevReservoirTankCount) {
				Tank tank = multiblock.reservoirTanks.get(x);
				tank.setTankCapacity(tankCapacity);
				tank.setAllowedFluids(getReservoirValidFluids.apply(x));
				return tank;
			}
			else {
				return new Tank(tankCapacity, getReservoirValidFluids.apply(x));
			}
		}).collect(Collectors.toList());
		
		multiblock.recipeHandler = getRecipeHandler();
		
		if (multiblock.recipeHandler == null) {
			multiblock.itemInputSize = multiblock.itemOutputSize = multiblock.fluidInputSize = multiblock.fluidOutputSize = 0;
		}
		else {
			multiblock.itemInputSize = multiblock.recipeHandler.itemInputSize;
			multiblock.itemOutputSize = multiblock.recipeHandler.itemOutputSize;
			multiblock.fluidInputSize = multiblock.recipeHandler.fluidInputSize;
			multiblock.fluidOutputSize = multiblock.recipeHandler.fluidOutputSize;
		}
		
		InventoryStackList prevInventoryStacks = multiblock.inventoryStacks;
		
		multiblock.inventoryStacks = InventoryStackList.withSize(inventorySize());
		
		for (int i = 0, len = Math.min(prevInventoryStacks.size(), multiblock.inventoryStacks.size()); i < len; ++i) {
			multiblock.inventoryStacks.set(i, prevInventoryStacks.get(i));
		}
		
		int nextTankCount = tankCount(), prevTankCount = multiblock.tanks.size();
		
		List<Set<String>> validFluids = multiblock.recipeHandler == null ? null : multiblock.recipeHandler.validFluids;
		IntFunction<Set<String>> getValidFluids = x -> validFluids != null && x < validFluids.size() ? validFluids.get(x) : null;
		
		multiblock.tanks = IntStream.range(0, nextTankCount).mapToObj(x -> {
			if (x < prevTankCount) {
				Tank tank = multiblock.tanks.get(x);
				tank.setTankCapacity(tankCapacity);
				tank.setAllowedFluids(getValidFluids.apply(x));
				return tank;
			}
			else {
				return new Tank(tankCapacity, getValidFluids.apply(x));
			}
		}).collect(Collectors.toList());
		
		List<ItemSorption> itemSorptions = new ArrayList<>();
		for (int i = 0; i < multiblock.itemInputSize; ++i) {
			itemSorptions.add(ItemSorption.IN);
		}
		for (int i = 0; i < multiblock.itemOutputSize; ++i) {
			itemSorptions.add(ItemSorption.OUT);
		}
		
		multiblock.inventoryConnections = StreamHelper.map(itemSorptions, x -> ITileInventory.inventoryConnectionAll(Collections.singletonList(x)));
		
		List<TankSorption> tankSorptions = new ArrayList<>();
		for (int i = 0; i < multiblock.fluidInputSize; ++i) {
			tankSorptions.add(TankSorption.IN);
		}
		for (int i = 0; i < multiblock.fluidOutputSize; ++i) {
			tankSorptions.add(TankSorption.OUT);
		}
		
		multiblock.fluidConnections = StreamHelper.map(tankSorptions, x -> ITileFluid.fluidConnectionAll(Collections.singletonList(x)));
		
		multiblock.processor.baseProcessTime = defaultProcessTime();
		
		multiblock.baseProcessPower = defaultProcessPower();
		
		boolean consumesInputs = getConsumesInputs();
		int consumedTankCapacity = multiblock.processor.getInputTankCapacity();
		
		NonNullList<ItemStack> prevConsumedStacks = multiblock.processor.consumedStacks;
		
		multiblock.processor.consumedStacks = NonNullList.withSize(consumesInputs ? multiblock.itemInputSize : 0, ItemStack.EMPTY);
		
		for (int i = 0, len = Math.min(prevConsumedStacks == null ? 0 : prevConsumedStacks.size(), multiblock.processor.consumedStacks.size()); i < len; ++i) {
			multiblock.processor.consumedStacks.set(i, prevConsumedStacks.get(i));
		}
		
		int prevConsumedTankCount = multiblock.processor.consumedTanks == null ? 0 : multiblock.processor.consumedTanks.size();
		
		multiblock.processor.consumedTanks = IntStream.range(0, consumesInputs ? multiblock.fluidInputSize : 0).mapToObj(x -> {
			if (x < prevConsumedTankCount) {
				return multiblock.processor.consumedTanks.get(x);
			}
			else {
				return new Tank(consumedTankCapacity, new ObjectOpenHashSet<>());
			}
		}).collect(Collectors.toList());
	}
	
	public int reservoirInventorySize() {
		return 0;
	}
	
	public int reservoirTankCount() {
		return 0;
	}
	
	public boolean usesReservoirPorts() {
		return reservoirInventorySize() > 0 || reservoirTankCount() > 0;
	}
	
	public List<Set<String>> getReservoirValidFluids() {
		return null;
	}
	
	public BasicRecipeHandler getRecipeHandler() {
		return null;
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
		return multiblock.itemInputSize + multiblock.itemOutputSize;
	}
	
	public int combinedInventorySize() {
		return 36 + inventorySize();
	}
	
	public int tankCount() {
		return multiblock.fluidInputSize + multiblock.fluidOutputSize;
	}
	
	public int tankDensity() {
		return 1000;
	}
	
	public boolean getConsumesInputs() {
		return false;
	}
	
	public boolean getLosesProgress() {
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
			multiblock.processor.refreshAll();
			setIsMachineOn(multiblock.processor.isProcessing);
			
			for (TileMachineProcessPort port : getParts(TileMachineProcessPort.class)) {
				port.setItemFluidData();
			}
		}
	}
	
	protected void setupMachine() {
		int volume = multiblock.getExteriorVolume();
		long energyCapacity = volume * energyDensity();
		int tankCapacity = volume * tankDensity();
		
		multiblock.energyStorage.setStorageCapacity(energyCapacity);
		multiblock.energyStorage.setMaxTransfer(energyCapacity);
		
		Consumer<Tank> setupTank = x -> {
			x.setTankCapacity(tankCapacity);
			x.clampTankAmount();
		};
		multiblock.reservoirTanks.forEach(setupTank);
		multiblock.tanks.forEach(setupTank);
		multiblock.processor.consumedTanks.forEach(setupTank);
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
		
		if (!usesReservoirPorts()) {
			Long2ObjectMap<TileMachineReservoirPort> reservoirPortMap = getPartMap(TileMachineReservoirPort.class);
			if (!reservoirPortMap.isEmpty()) {
				multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.machine.invalid_reservoir_port", reservoirPortMap.keySet());
				return false;
			}
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
			multiblock.energyStorage.mergeEnergyStorage(assimilated.energyStorage);
			
			if (getID().equals(other.getID())) {
				mergeInventoryStacks(multiblock.reservoirInventoryStacks, assimilated.reservoirInventoryStacks);
				mergeTanks(multiblock.reservoirTanks, assimilated.reservoirTanks);
				mergeInventoryStacks(multiblock.inventoryStacks, assimilated.inventoryStacks);
				mergeTanks(multiblock.tanks, assimilated.tanks);
			}
		}
	}
	
	protected static void mergeInventoryStacks(NonNullList<ItemStack> stacks, NonNullList<ItemStack> assimilatedStacks) {
		for (ItemStack assimilatedStack : assimilatedStacks) {
			if (assimilatedStack.isEmpty()) {
				continue;
			}
			
			ItemStack remaining = assimilatedStack.copy();
			for (ItemStack stack : stacks) {
				if (!remaining.isEmpty() && ItemHandlerHelper.canItemStacksStack(stack, remaining)) {
					int transfer = Math.min(remaining.getCount(), stack.getMaxStackSize() - stack.getCount());
					if (transfer > 0) {
						stack.grow(transfer);
						remaining.shrink(transfer);
					}
				}
			}
			
			for (int i = 0; i < stacks.size() && !remaining.isEmpty(); ++i) {
				if (stacks.get(i).isEmpty()) {
					int transfer = Math.min(remaining.getCount(), remaining.getMaxStackSize());
					ItemStack stack = remaining.copy();
					stack.setCount(transfer);
					stacks.set(i, stack);
					remaining.shrink(transfer);
				}
			}
		}
	}
	
	protected static void mergeTanks(List<Tank> tanks, List<Tank> assimilatedTanks) {
		for (int i = 0, len = Math.min(tanks.size(), assimilatedTanks.size()); i < len; ++i) {
			tanks.get(i).mergeTank(assimilatedTanks.get(i));
		}
	}
	
	@Override
	public void onAssimilated(Machine assimilator) {}
	
	// Server
	
	@Override
	public boolean onUpdateServer() {
		if (multiblock.machineActivityCooldown > 0) {
			--multiblock.machineActivityCooldown;
		}
		
		boolean shouldUpdate = multiblock.processor.onTick();
		if (multiblock.machineActivityCooldown <= 0 && multiblock.machineActivityDirty) {
			shouldUpdate |= setIsMachineOnInternal(multiblock.isMachineOnQueued);
		}
		
		if (multiblock.controller != null) {
			multiblock.sendRenderPacketToAll();
		}
		
		return shouldUpdate;
	}
	
	public void setActivity(boolean isMachineOn) {
		multiblock.controller.setActivity(isMachineOn);
	}
	
	public void setIsMachineOn(boolean isMachineOn) {
		if (multiblock.machineActivityCooldown > 0) {
			multiblock.isMachineOnQueued = isMachineOn;
			multiblock.machineActivityDirty = isMachineOn != multiblock.isMachineOn;
			return;
		}
		
		setIsMachineOnInternal(isMachineOn);
	}
	
	protected boolean setIsMachineOnInternal(boolean isMachineOn) {
		boolean oldIsMachineOn = multiblock.isMachineOn;
		multiblock.isMachineOn = isMachineOn;
		multiblock.machineActivityDirty = false;
		if (multiblock.isMachineOn != oldIsMachineOn) {
			multiblock.machineActivityCooldown = NCConfig.machine_update_rate;
			if (multiblock.controller != null) {
				setActivity(multiblock.isMachineOn);
				multiblock.sendMultiblockUpdatePacketToAll();
			}
		}
		return multiblock.isMachineOn != oldIsMachineOn;
	}
	
	protected void setRecipeStats(@Nullable BasicRecipe recipe) {
		if (recipe == null) {
			multiblock.processor.baseProcessTime = defaultProcessTime();
			multiblock.baseProcessPower = defaultProcessPower();
		}
		else {
			multiblock.processor.baseProcessTime = recipe.getBaseProcessTime(defaultProcessTime());
			multiblock.baseProcessPower = recipe.getBaseProcessPower(defaultProcessPower());
		}
	}
	
	protected long getEnergyCapacity() {
		return multiblock.getExteriorVolume() * energyDensity();
	}
	
	protected double getSpeedMultiplier() {
		return multiblock.baseSpeedMultiplier;
	}
	
	protected double getPowerMultiplier() {
		return multiblock.basePowerMultiplier;
	}
	
	public double getProcessTimeFP() {
		return multiblock.processor.baseProcessTime / getSpeedMultiplier();
	}
	
	public long getProcessTime() {
		double processTimeFP = getProcessTimeFP();
		return processTimeFP >= Long.MAX_VALUE ? Long.MAX_VALUE : Math.max(1L, (long) Math.ceil(processTimeFP));
	}
	
	public double getProcessPowerFP() {
		return multiblock.baseProcessPower * getPowerMultiplier();
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
	
	public boolean isHalted() {
		return multiblock.fullHalt || Stream.concat(Stream.of(multiblock.controller), getParts(TileMachineRedstonePort.class).stream()).anyMatch(x -> x != null && x.getIsRedstonePowered());
	}
	
	// Needed for Galacticraft
	protected long getMaxEnergyModified() {
		return ModCheck.galacticraftLoaded() ? Math.max(0L, multiblock.energyStorage.getMaxEnergyStoredLong() - 16L) : multiblock.energyStorage.getMaxEnergyStoredLong();
	}
	
	protected boolean hasSufficientEnergy() {
		if (multiblock.processor.time <= multiblock.processor.resetTime) {
			long processEnergy = getProcessEnergy(), maxEnergy = getMaxEnergyModified();
			if (processEnergy >= maxEnergy) {
				return multiblock.energyStorage.getEnergyStoredLong() >= maxEnergy;
			}
			else {
				return multiblock.energyStorage.getEnergyStoredLong() >= processEnergy;
			}
		}
		else {
			return multiblock.energyStorage.getEnergyStoredLong() >= getProcessPower();
		}
	}
	
	protected boolean readyToProcess() {
		return multiblock.processor.readyToProcessBase() && (isGenerator() || hasSufficientEnergy());
	}
	
	public void refreshRecipe() {
		multiblock.processor.refreshRecipeBase();
	}
	
	public void refreshActivity() {
		multiblock.processor.refreshActivityBase();
	}
	
	// Component Logic
	
	public @Nonnull EnergyStorage getPowerPortEnergyStorage(EnergyStorage backupStorage) {
		return multiblock.energyStorage;
	}
	
	public int getPowerPortEUSinkTier() {
		return 10;
	}
	
	public int getPowerPortEUSourceTier() {
		return isGenerator() ? EnergyHelper.getEUTier(getProcessPower()) : 1;
	}
	
	public @Nonnull NonNullList<ItemStack> getReservoirPortInventoryStacks(TileMachineReservoirPort port) {
		return multiblock.reservoirInventoryStacks;
	}
	
	public @Nonnull InventoryConnection[] getReservoirPortInventoryConnections(TileMachineReservoirPort port) {
		return port.backupInventoryConnections;
	}
	
	public @Nonnull ItemStack getReservoirPortStackInSlot(TileMachineReservoirPort port, int slot) {
		NonNullList<ItemStack> stacks = port.getInventoryStacks();
		return slot >= 0 && slot < stacks.size() ? stacks.get(slot) : ItemStack.EMPTY;
	}
	
	public @Nonnull ItemStack decrReservoirPortStackSize(TileMachineReservoirPort port, int slot, int amount) {
		NonNullList<ItemStack> stacks = port.getInventoryStacks();
		if (slot < 0 || slot >= stacks.size() || amount <= 0) {
			return ItemStack.EMPTY;
		}
		
		ItemStack stack = ItemStackHelper.getAndSplit(stacks, slot, amount);
		if (!stack.isEmpty()) {
			onReservoirPortInventoryChanged(port, slot);
		}
		return stack;
	}
	
	public @Nonnull ItemStack removeReservoirPortStackFromSlot(TileMachineReservoirPort port, int slot) {
		NonNullList<ItemStack> stacks = port.getInventoryStacks();
		if (slot < 0 || slot >= stacks.size()) {
			return ItemStack.EMPTY;
		}
		
		ItemStack stack = ItemStackHelper.getAndRemove(stacks, slot);
		if (!stack.isEmpty()) {
			onReservoirPortInventoryChanged(port, slot);
		}
		return stack;
	}
	
	public void setReservoirPortInventorySlotContents(TileMachineReservoirPort port, int slot, ItemStack stack) {
		NonNullList<ItemStack> stacks = port.getInventoryStacks();
		if (slot < 0 || slot >= stacks.size()) {
			return;
		}
		
		ItemStack stackInSlot = stacks.get(slot);
		boolean sameStack = !stack.isEmpty() && stack.isItemEqual(stackInSlot) && StackHelper.areItemStackTagsEqual(stack, stackInSlot);
		
		if (stack.getCount() > port.getInventoryStackLimit()) {
			stack.setCount(port.getInventoryStackLimit());
		}
		
		stacks.set(slot, stack);
		
		if (!sameStack) {
			port.markTileDirty();
		}
		onReservoirPortInventoryChanged(port, slot);
	}
	
	public void clearReservoirPortInventory(TileMachineReservoirPort port) {
		Collections.fill(port.getInventoryStacks(), ItemStack.EMPTY);
		onReservoirPortInventoryCleared(port);
	}
	
	public boolean hasReservoirPortItemCapability(TileMachineReservoirPort port, @Nullable EnumFacing side) {
		return !port.getInventoryStacks().isEmpty() && port.hasInventorySideCapability(side);
	}
	
	public @Nonnull IItemHandler getReservoirPortItemHandler(TileMachineReservoirPort port, @Nullable EnumFacing side) {
		return port.getItemHandler(side);
	}
	
	public void pushReservoirPortItemToSide(TileMachineReservoirPort port, @Nonnull EnumFacing side) {
		NonNullList<ItemStack> stacks = port.getInventoryStacks();
		if (!stacks.isEmpty()) {
			port.pushStacksToSide(side);
		}
	}
	
	public boolean isReservoirPortItemValid(TileMachineReservoirPort port, int slot, ItemStack stack) {
		NonNullList<ItemStack> stacks = port.getInventoryStacks();
		return slot >= 0 && slot < stacks.size() && !stack.isEmpty();
	}
	
	public void onReservoirPortInventoryChanged(TileMachineReservoirPort port, int slot) {
		refreshRecipe();
		refreshActivity();
	}
	
	public void onReservoirPortInventoryCleared(TileMachineReservoirPort port) {
		refreshRecipe();
		refreshActivity();
	}
	
	public @Nonnull List<Tank> getReservoirPortTanks(TileMachineReservoirPort port) {
		return multiblock.reservoirTanks;
	}
	
	public @Nonnull FluidConnection[] getReservoirPortFluidConnections(TileMachineReservoirPort port) {
		return port.backupFluidConnections;
	}
	
	public boolean hasReservoirPortFluidCapability(TileMachineReservoirPort port, @Nullable EnumFacing side) {
		return !port.getTanks().isEmpty() && port.hasFluidSideCapability(side);
	}
	
	public @Nonnull IFluidHandler getReservoirPortFluidHandler(TileMachineReservoirPort port, @Nonnull EnumFacing side) {
		return port.getFluidSide(side);
	}
	
	public void pushReservoirPortFluidToSide(TileMachineReservoirPort port, @Nonnull EnumFacing side) {
		List<Tank> tanks = port.getTanks();
		if (!tanks.isEmpty() && !tanks.get(0).isEmpty() && port.getTankSorption(side, 0).canDrain()) {
			port.pushFluidToSide(side);
		}
	}
	
	public boolean isReservoirPortFluidValid(TileMachineReservoirPort port, int tankNumber, FluidStack stack) {
		List<Tank> tanks = port.getTanks();
		return tankNumber >= 0 && tankNumber < tanks.size() && tanks.get(tankNumber).canFillFluidType(stack);
	}
	
	public void onReservoirPortTanksCleared(TileMachineReservoirPort port) {}
	
	public @Nonnull NonNullList<ItemStack> getProcessPortInventoryStacks(NonNullList<ItemStack> backupStacks, int slot) {
		return multiblock.isAssembled() && slot >= 0 ? multiblock.inventoryStacks.subList(slot, slot + 1) : backupStacks;
	}
	
	public @Nonnull InventoryConnection[] getProcessPortInventoryConnections(InventoryConnection[] backupInventoryConnections, int slot) {
		return multiblock.isAssembled() && slot >= 0 ? multiblock.inventoryConnections.get(slot) : backupInventoryConnections;
	}
	
	public @Nonnull List<Tank> getProcessPortTanks(List<Tank> backupTanks, int tankIndex) {
		return multiblock.isAssembled() && tankIndex >= 0 ? multiblock.tanks.subList(tankIndex, tankIndex + 1) : backupTanks;
	}
	
	public @Nonnull FluidConnection[] getProcessPortFluidConnections(FluidConnection[] backupFluidConnections, int tankIndex) {
		return multiblock.isAssembled() && tankIndex >= 0 ? multiblock.fluidConnections.get(tankIndex) : backupFluidConnections;
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
		logicTag.setBoolean("isMachineOn", multiblock.isMachineOn);
		logicTag.setBoolean("fullHalt", multiblock.fullHalt);
		
		logicTag.setDouble("radiationLevel", multiblock.radiation.getRadiationLevel());
		
		multiblock.energyStorage.writeToNBT(logicTag, "energyStorage");
		NBTTagCompound reservoirInventoryTag = new NBTTagCompound();
		writeStacks(multiblock.reservoirInventoryStacks, reservoirInventoryTag);
		logicTag.setTag("reservoirInventoryStacks", reservoirInventoryTag);
		writeTanks(multiblock.reservoirTanks, logicTag, "reservoirTanks");
		
		writeStacks(multiblock.inventoryStacks, logicTag);
		writeTanks(multiblock.tanks, logicTag, "tanks");
		
		multiblock.processor.writeToNBT(logicTag, "processor");
		logicTag.setInteger("productionCount", multiblock.productionCount);
		
		logicTag.setDouble("baseSpeedMultiplier", multiblock.baseSpeedMultiplier);
		logicTag.setDouble("basePowerMultiplier", multiblock.basePowerMultiplier);
		
		multiblock.recipeUnitInfo.writeToNBT(logicTag, "recipeUnitInfo");
		
		logicTag.setBoolean("readyToProcess", multiblock.readyToProcess);
	}
	
	@Override
	public void readFromLogicTag(NBTTagCompound logicTag, SyncReason syncReason) {
		multiblock.isMachineOn = logicTag.getBoolean("isMachineOn");
		multiblock.fullHalt = logicTag.getBoolean("fullHalt");
		
		multiblock.radiation.setRadiationLevel(logicTag.getDouble("radiationLevel"));
		
		multiblock.energyStorage.readFromNBT(logicTag, "energyStorage");
		readStacks(multiblock.reservoirInventoryStacks, logicTag.getCompoundTag("reservoirInventoryStacks"));
		readTanks(multiblock.reservoirTanks, logicTag, "reservoirTanks");
		
		readStacks(multiblock.inventoryStacks, logicTag);
		readTanks(multiblock.tanks, logicTag, "tanks");
		
		multiblock.processor.readFromNBT(logicTag, "processor");
		multiblock.productionCount = logicTag.getInteger("productionCount");
		
		multiblock.baseSpeedMultiplier = logicTag.getDouble("baseSpeedMultiplier");
		multiblock.basePowerMultiplier = logicTag.getDouble("basePowerMultiplier");
		
		multiblock.recipeUnitInfo = RecipeUnitInfo.readFromNBT(logicTag, "recipeUnitInfo");
		
		multiblock.readyToProcess = logicTag.getBoolean("readyToProcess");
	}
	
	// Packets
	
	@Override
	public MachineUpdatePacket getMultiblockUpdatePacket() {
		return null;
	}
	
	@Override
	public void onMultiblockUpdatePacket(MachineUpdatePacket message) {
		multiblock.isMachineOn = message.isMachineOn;
		multiblock.processor.isProcessing = message.isProcessing;
		multiblock.processor.time = message.time;
		multiblock.processor.baseProcessTime = message.baseProcessTime;
		multiblock.baseProcessPower = message.baseProcessPower;
		TankInfo.readInfoList(message.tankInfos, multiblock.tanks);
		multiblock.baseSpeedMultiplier = message.baseSpeedMultiplier;
		multiblock.basePowerMultiplier = message.basePowerMultiplier;
		multiblock.recipeUnitInfo = message.recipeUnitInfo;
		multiblock.readyToProcess = message.readyToProcess;
	}
	
	public MachineRenderPacket getRenderPacket() {
		return null;
	}
	
	public void onRenderPacket(MachineRenderPacket message) {
		multiblock.isMachineOn = message.isMachineOn;
	}
	
	// Clear Material
	
	@Override
	public void clearAllMaterial() {}
}
