package nc.multiblock.machine;

import nc.network.multiblock.*;
import nc.recipe.*;
import nc.recipe.ingredient.*;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.Tank.TankInfo;
import nc.tile.machine.*;
import nc.tile.multiblock.TilePartAbstract.SyncReason;
import nc.util.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.*;
import net.minecraftforge.items.*;

import javax.annotation.*;
import java.util.*;

public class DecayPoolLogic extends MachineLogic {
	
	protected static final long CONTAINER_BASE_STORAGE_CAPACITY = 144L;
	protected static final long ITEM_STORAGE_CAPACITY = 144L;
	
	protected final List<SourceRecipeGroup> sourceRecipeGroups = new ArrayList<>();
	protected final List<ItemIngredientGroup> itemIngredientGroups = new ArrayList<>();
	protected final List<FluidIngredientGroup> fluidIngredientGroups = new ArrayList<>();
	protected final List<ItemProductGroup> itemProductGroups = new ArrayList<>();
	protected final List<FluidProductGroup> fluidProductGroups = new ArrayList<>();
	
	public double totalDecayRate = 0D;
	
	protected final NonNullList<ItemStack> itemInputView = InventoryStackList.withSize(1);
	protected final NonNullList<ItemStack> itemOutputView = InventoryStackList.withSize(1);
	
	public DecayPoolLogic(Machine machine) {
		super(machine);
	}
	
	public DecayPoolLogic(MachineLogic oldLogic) {
		super(oldLogic);
		if (oldLogic instanceof DecayPoolLogic oldDecayPoolLogic) {
			for (SourceRecipeGroup group : oldDecayPoolLogic.sourceRecipeGroups) {
				sourceRecipeGroups.add(group.copy());
			}
			for (ItemIngredientGroup group : oldDecayPoolLogic.itemIngredientGroups) {
				itemIngredientGroups.add(group.copy());
			}
			for (FluidIngredientGroup group : oldDecayPoolLogic.fluidIngredientGroups) {
				fluidIngredientGroups.add(group.copy());
			}
			for (ItemProductGroup group : oldDecayPoolLogic.itemProductGroups) {
				itemProductGroups.add(group.copy());
			}
			for (FluidProductGroup group : oldDecayPoolLogic.fluidProductGroups) {
				fluidProductGroups.add(group.copy());
			}
			refreshContainerHeat();
		}
	}
	
	@Override
	public String getID() {
		return "decay_pool";
	}
	
	@Override
	public BasicRecipeHandler getRecipeHandler() {
		return NCRecipes.multiblock_decay_pool;
	}
	
	@Override
	public boolean usesReservoirPorts() {
		return true;
	}
	
	@Override
	public @Nonnull List<Tank> getReservoirPortTanks(TileMachineReservoirPort port) {
		return Collections.emptyList();
	}
	
	@Override
	public double defaultProcessTime() {
		return 64D;
	}
	
	@Override
	public double defaultProcessPower() {
		return 0D;
	}
	
	@Override
	public boolean isMachineWhole() {
		if (!super.isMachineWhole()) {
			return false;
		}
		
		multiblock.basePowerMultiplier = 0D;
		
		refreshContainerHeat();
		
		return true;
	}
	
	public long getStorageCapacity() {
		return getPartCount(TileDecayPoolContainer.class) * CONTAINER_BASE_STORAGE_CAPACITY;
	}
	
	public long getIngredientStorageUsed() {
		long storage = 0L;
		for (ItemIngredientGroup group : itemIngredientGroups) {
			storage += group.amount * ITEM_STORAGE_CAPACITY;
		}
		for (FluidIngredientGroup group : fluidIngredientGroups) {
			storage += group.amount;
		}
		return storage;
	}
	
	public long getProductStorageUsed() {
		long storage = 0L;
		for (ItemProductGroup group : itemProductGroups) {
			storage += group.amount * ITEM_STORAGE_CAPACITY;
		}
		for (FluidProductGroup group : fluidProductGroups) {
			storage += group.amount;
		}
		return storage;
	}
	
	@Override
	public @Nonnull NonNullList<ItemStack> getReservoirPortInventoryStacks(TileMachineReservoirPort port) {
		if (!multiblock.isAssembled()) {
			return port.backupStacks;
		}
		if (port.getItemSorption(EnumFacing.DOWN, 0).canReceive()) {
			itemInputView.set(0, ItemStack.EMPTY);
			return itemInputView;
		}
		itemOutputView.set(0, nextItemProduct());
		return itemOutputView;
	}
	
	@Override
	public @Nonnull ItemStack getReservoirPortStackInSlot(TileMachineReservoirPort port, int slot) {
		return slot == 0 && !port.getItemSorption(EnumFacing.DOWN, 0).canReceive() ? nextItemProduct() : ItemStack.EMPTY;
	}
	
	@Override
	public @Nonnull ItemStack decrReservoirPortStackSize(TileMachineReservoirPort port, int slot, int amount) {
		if (slot != 0 || amount <= 0 || port.getItemSorption(EnumFacing.DOWN, 0).canReceive()) {
			return ItemStack.EMPTY;
		}
		
		ItemStack stack = extractItemProduct(amount, false);
		if (!stack.isEmpty()) {
			port.markDirty();
		}
		return stack;
	}
	
	@Override
	public @Nonnull ItemStack removeReservoirPortStackFromSlot(TileMachineReservoirPort port, int slot) {
		return decrReservoirPortStackSize(port, slot, 64);
	}
	
	@Override
	public void setReservoirPortInventorySlotContents(TileMachineReservoirPort port, int slot, ItemStack stack) {
		if (slot != 0 || stack.isEmpty() || !port.getItemSorption(EnumFacing.DOWN, 0).canReceive()) {
			return;
		}
		
		long inserted = insertItemIngredient(stack, true);
		if (inserted >= stack.getCount() && insertItemIngredient(stack, false) > 0L) {
			port.markDirty();
		}
	}
	
	@Override
	public void clearReservoirPortInventory(TileMachineReservoirPort port) {
		boolean changed = !itemIngredientGroups.isEmpty();
		if (changed) {
			itemIngredientGroups.clear();
			sourceRecipeGroups.removeIf(group -> !hasSourceRecipeIngredient(group));
		}
		if (!itemProductGroups.isEmpty()) {
			itemProductGroups.clear();
			changed = true;
		}
		if (changed) {
			port.markDirty();
			refreshContainerHeat();
			refreshActivity();
		}
	}
	
	@Override
	public @Nonnull IItemHandler getReservoirPortItemHandler(TileMachineReservoirPort port, @Nullable EnumFacing side) {
		return new IItemHandlerModifiable() {
			
			@Override
			public int getSlots() {
				return 1;
			}
			
			@Override
			public @Nonnull ItemStack getStackInSlot(int slot) {
				return slot == 0 && !port.getItemSorption(EnumFacing.DOWN, 0).canReceive() ? nextItemProduct() : ItemStack.EMPTY;
			}
			
			@Override
			public @Nonnull ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
				if (slot != 0 || stack.isEmpty() || !port.getItemSorption(side == null ? EnumFacing.DOWN : side, 0).canReceive()) {
					return stack;
				}
				
				long inserted = insertItemIngredient(stack, simulate);
				if (inserted <= 0L) {
					return stack;
				}
				if (!simulate) {
					port.markDirty();
				}
				if (inserted >= stack.getCount()) {
					return ItemStack.EMPTY;
				}
				
				ItemStack remainder = stack.copy();
				remainder.shrink((int) inserted);
				return remainder;
			}
			
			@Override
			public @Nonnull ItemStack extractItem(int slot, int amount, boolean simulate) {
				if (slot != 0 || amount <= 0 || !port.getItemSorption(side == null ? EnumFacing.DOWN : side, 0).canExtract()) {
					return ItemStack.EMPTY;
				}
				ItemStack extracted = extractItemProduct(amount, simulate);
				if (!simulate && !extracted.isEmpty()) {
					port.markDirty();
				}
				return extracted;
			}
			
			@Override
			public int getSlotLimit(int slot) {
				return 64;
			}
			
			@Override
			public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
				if (slot == 0 && !stack.isEmpty() && port.getItemSorption(side == null ? EnumFacing.DOWN : side, 0).canReceive()) {
					if (insertItemIngredient(stack, false) > 0L) {
						port.markDirty();
					}
				}
			}
		};
	}
	
	@Override
	public void pushReservoirPortItemToSide(TileMachineReservoirPort port, @Nonnull EnumFacing side) {
		if (!port.getItemSorption(side, 0).canExtract()) {
			return;
		}
		
		TileEntity tile = port.getTileWorld().getTileEntity(port.getTilePos().offset(side));
		if (tile == null) {
			return;
		}
		
		IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side.getOpposite());
		if (handler == null || handler.getSlots() < 1) {
			return;
		}
		
		ItemStack stack = nextItemProduct();
		if (stack.isEmpty()) {
			return;
		}
		
		ItemStack remaining = NCInventoryHelper.addStackToInventory(handler, stack.copy());
		int moved = stack.getCount() - remaining.getCount();
		if (moved <= 0) {
			return;
		}
		
		extractItemProduct(moved, false);
		port.markDirty();
	}
	
	@Override
	public boolean isReservoirPortItemValid(TileMachineReservoirPort port, int slot, ItemStack stack) {
		long availableStorage = getStorageCapacity() - getIngredientStorageUsed();
		return multiblock.isAssembled() && port.getItemSorption(EnumFacing.DOWN, 0).canReceive() && slot == 0 && getSourceRecipeInfo(stack) != null && availableStorage / ITEM_STORAGE_CAPACITY >= stack.getCount();
	}
	
	@Override
	public void onReservoirPortInventoryChanged(TileMachineReservoirPort port, int slot) {
		refreshActivity();
	}
	
	@Override
	public void onReservoirPortInventoryCleared(TileMachineReservoirPort port) {
		refreshActivity();
	}
	
	@Override
	public @Nonnull IFluidHandler getReservoirPortFluidHandler(TileMachineReservoirPort port, @Nonnull EnumFacing side) {
		return new IFluidHandler() {
			
			@Override
			public IFluidTankProperties[] getTankProperties() {
				if (port.getTankSorption(EnumFacing.DOWN, 0).canFill()) {
					return new IFluidTankProperties[] {new FluidTankProperties(null, (int) Math.min(Integer.MAX_VALUE, getStorageCapacity()), true, false)};
				}
				
				FluidStack stack = peekFluidProduct(Integer.MAX_VALUE);
				return new IFluidTankProperties[] {new FluidTankProperties(stack, (int) Math.min(Integer.MAX_VALUE, getStorageCapacity()), false, true)};
			}
			
			@Override
			public int fill(FluidStack resource, boolean doFill) {
				if (resource == null || resource.amount <= 0 || !port.getTankSorption(side, 0).canFill()) {
					return 0;
				}
				int filled = (int) Math.min(Integer.MAX_VALUE, insertFluidIngredient(resource, !doFill));
				if (doFill && filled > 0) {
					port.markDirty();
				}
				return filled;
			}
			
			@Override
			public @Nullable FluidStack drain(FluidStack resource, boolean doDrain) {
				if (resource == null || resource.amount <= 0 || !port.getTankSorption(side, 0).canDrain()) {
					return null;
				}
				FluidStack drained = extractFluidProduct(resource, !doDrain);
				if (doDrain && drained != null && drained.amount > 0) {
					port.markDirty();
				}
				return drained;
			}
			
			@Override
			public @Nullable FluidStack drain(int maxDrain, boolean doDrain) {
				if (maxDrain <= 0 || !port.getTankSorption(side, 0).canDrain()) {
					return null;
				}
				FluidStack stack = peekFluidProduct(maxDrain);
				FluidStack drained = stack == null ? null : extractFluidProduct(stack, !doDrain);
				if (doDrain && drained != null && drained.amount > 0) {
					port.markDirty();
				}
				return drained;
			}
		};
	}
	
	@Override
	public boolean hasReservoirPortFluidCapability(TileMachineReservoirPort port, @Nullable EnumFacing side) {
		return port.hasFluidSideCapability(side);
	}
	
	@Override
	public void pushReservoirPortFluidToSide(TileMachineReservoirPort port, @Nonnull EnumFacing side) {
		if (!port.getTankSorption(side, 0).canDrain()) {
			return;
		}
		
		TileEntity tile = port.getTileWorld().getTileEntity(port.getTilePos().offset(side));
		if (tile == null) {
			return;
		}
		
		IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
		if (handler == null) {
			return;
		}
		
		FluidStack stack = peekFluidProduct(Integer.MAX_VALUE);
		if (stack == null) {
			return;
		}
		
		int moved = handler.fill(stack.copy(), true);
		if (moved <= 0) {
			return;
		}
		
		stack.amount = moved;
		extractFluidProduct(stack, false);
		port.markDirty();
	}
	
	@Override
	public boolean isReservoirPortFluidValid(TileMachineReservoirPort port, int tankNumber, FluidStack stack) {
		return multiblock.isAssembled() && port.getTankSorption(EnumFacing.DOWN, 0).canFill() && tankNumber == 0 && getSourceRecipeInfo(stack) != null && getStorageCapacity() > getIngredientStorageUsed();
	}
	
	@Override
	public void onReservoirPortTanksCleared(TileMachineReservoirPort port) {
		refreshActivity();
	}
	
	@Override
	public void clearAllMaterial() {
		sourceRecipeGroups.clear();
		itemIngredientGroups.clear();
		fluidIngredientGroups.clear();
		itemProductGroups.clear();
		fluidProductGroups.clear();
		refreshActivity();
	}
	
	public void refreshContainerHeat() {
		multiblock.baseSpeedMultiplier = 0D;
		totalDecayRate = 0D;
		for (SourceRecipeGroup group : sourceRecipeGroups) {
			long recipeProductionCount = getAvailableSourceRecipeProductionCount(group);
			multiblock.baseSpeedMultiplier += recipeProductionCount * group.heatPerTick;
			totalDecayRate += group.meanLifetime <= 0D ? recipeProductionCount : recipeProductionCount / group.meanLifetime;
		}
	}
	
	@Override
	public void onAssimilate(Machine assimilated) {
		super.onAssimilate(assimilated);
		
		if (assimilated != null && assimilated.getLogic() instanceof DecayPoolLogic logic) {
			for (SourceRecipeGroup group : logic.sourceRecipeGroups) {
				SourceRecipeGroup copy = group.copy();
				SourceRecipeGroup existing = copy.recipe == null ? null : getSourceRecipeGroup(copy.recipe);
				if (existing == null) {
					for (SourceRecipeGroup recipeGroup : sourceRecipeGroups) {
						if (itemStacksEqual(recipeGroup.itemIngredient, copy.itemIngredient) || fluidStacksEqual(recipeGroup.fluidIngredient, copy.fluidIngredient)) {
							existing = recipeGroup;
							break;
						}
					}
				}
				if (existing == null) {
					sourceRecipeGroups.add(copy);
				}
				else {
					existing.assimilateIngredientKeys(copy);
					existing.decayRemainder = Math.min(1D - NCMath.EPSILON, existing.decayRemainder + copy.decayRemainder);
				}
			}
			for (ItemIngredientGroup group : logic.itemIngredientGroups) {
				ItemIngredientGroup copy = group.copy();
				ItemIngredientGroup existing = null;
				for (ItemIngredientGroup ingredientGroup : itemIngredientGroups) {
					if (itemStacksEqual(ingredientGroup.stack, copy.stack)) {
						existing = ingredientGroup;
						break;
					}
				}
				if (existing == null) {
					itemIngredientGroups.add(copy);
				}
				else {
					existing.amount += copy.amount;
				}
			}
			for (FluidIngredientGroup group : logic.fluidIngredientGroups) {
				FluidIngredientGroup copy = group.copy();
				FluidIngredientGroup existing = null;
				for (FluidIngredientGroup ingredientGroup : fluidIngredientGroups) {
					if (fluidStacksEqual(ingredientGroup.stack, copy.stack)) {
						existing = ingredientGroup;
						break;
					}
				}
				if (existing == null) {
					fluidIngredientGroups.add(copy);
				}
				else {
					existing.amount += copy.amount;
				}
			}
			for (ItemProductGroup group : logic.itemProductGroups) {
				addItemProduct(group.stack, group.amount);
			}
			for (FluidProductGroup group : logic.fluidProductGroups) {
				addFluidProduct(group.stack, group.amount);
			}
			refreshContainerHeat();
		}
	}
	
	@Override
	public boolean onUpdateServer() {
		boolean decayed = updateSourceRecipes();
		boolean shouldUpdate = super.onUpdateServer();
		return shouldUpdate || decayed;
	}
	
	protected boolean updateSourceRecipes() {
		boolean storageChanged = itemIngredientGroups.removeIf(group -> group.amount <= 0L);
		storageChanged |= fluidIngredientGroups.removeIf(group -> group.amount <= 0L);
		boolean progressChanged = false;
		
		Iterator<SourceRecipeGroup> iterator = sourceRecipeGroups.iterator();
		while (iterator.hasNext()) {
			SourceRecipeGroup group = iterator.next();
			if (!hasSourceRecipeIngredient(group)) {
				iterator.remove();
				progressChanged = true;
				continue;
			}
			
			long recipeProductionCount = getAvailableSourceRecipeProductionCount(group);
			if (recipeProductionCount <= 0L) {
				continue;
			}
			
			double oldRemainder = group.decayRemainder;
			double expectedDecays = group.meanLifetime <= 0D ? recipeProductionCount : (double) recipeProductionCount / group.meanLifetime + group.decayRemainder;
			long naturalDecays = (long) expectedDecays;
			if (naturalDecays <= 0L) {
				if (group.decayRemainder != expectedDecays) {
					group.decayRemainder = expectedDecays;
					progressChanged = true;
				}
				continue;
			}
			
			long decays = Math.min(naturalDecays, recipeProductionCount);
			long availableStorage = Math.max(0L, getStorageCapacity() - getProductStorageUsed());
			if (getProductStorageRequired(group, decays) > availableStorage) {
				long low = 0L, high = decays;
				while (low < high) {
					long mid = (low + high + 1L) >>> 1;
					if (getProductStorageRequired(group, mid) <= availableStorage) {
						low = mid;
					}
					else {
						high = mid - 1L;
					}
				}
				decays = low;
			}
			group.decayRemainder = expectedDecays - decays;
			
			if (decays == 0L) {
				if (oldRemainder != group.decayRemainder) {
					progressChanged = true;
				}
				continue;
			}
			
			if (group.hasItemIngredient) {
				long remaining = decays;
				Iterator<ItemIngredientGroup> itemIterator = itemIngredientGroups.iterator();
				while (remaining > 0L && itemIterator.hasNext()) {
					ItemIngredientGroup ingredientGroup = itemIterator.next();
					if (ingredientGroup.recipe != group.recipe || ingredientGroup.ingredientSize <= 0) {
						continue;
					}
					
					long groupCompletions = Math.min(remaining, ingredientGroup.amount / ingredientGroup.ingredientSize);
					if (groupCompletions <= 0L) {
						continue;
					}
					
					ingredientGroup.amount -= groupCompletions * ingredientGroup.ingredientSize;
					remaining -= groupCompletions;
					if (ingredientGroup.amount <= 0L) {
						itemIterator.remove();
					}
				}
			}
			if (group.hasFluidIngredient) {
				long remaining = decays;
				Iterator<FluidIngredientGroup> fluidIterator = fluidIngredientGroups.iterator();
				while (remaining > 0L && fluidIterator.hasNext()) {
					FluidIngredientGroup ingredientGroup = fluidIterator.next();
					if (ingredientGroup.recipe != group.recipe || ingredientGroup.ingredientSize <= 0) {
						continue;
					}
					
					long groupCompletions = Math.min(remaining, ingredientGroup.amount / ingredientGroup.ingredientSize);
					if (groupCompletions <= 0L) {
						continue;
					}
					
					ingredientGroup.amount -= groupCompletions * ingredientGroup.ingredientSize;
					remaining -= groupCompletions;
					if (ingredientGroup.amount <= 0L) {
						fluidIterator.remove();
					}
				}
			}
			
			if (!group.itemOutput.isEmpty() && group.itemOutputSize > 0) {
				addItemProduct(group.itemOutput, decays * group.itemOutputSize);
			}
			if (group.fluidOutput != null && group.fluidOutputSize > 0) {
				addFluidProduct(group.fluidOutput, decays * group.fluidOutputSize);
			}
			
			storageChanged = true;
		}
		
		boolean removedEmptyItems = itemIngredientGroups.removeIf(group -> group.amount <= 0L);
		boolean removedEmptyFluids = fluidIngredientGroups.removeIf(group -> group.amount <= 0L);
		boolean removedUnusedSourceRecipes = sourceRecipeGroups.removeIf(group -> !hasSourceRecipeIngredient(group));
		if (removedEmptyItems || removedEmptyFluids || removedUnusedSourceRecipes) {
			storageChanged = true;
		}
		if (storageChanged) {
			refreshActivity();
		}
		return storageChanged || progressChanged;
	}
	
	protected long getProductStorageRequired(SourceRecipeGroup group, long decays) {
		long storage = 0L;
		if (!group.itemOutput.isEmpty() && group.itemOutputSize > 0) {
			storage += decays * group.itemOutputSize * ITEM_STORAGE_CAPACITY;
		}
		if (group.fluidOutput != null && group.fluidOutputSize > 0) {
			storage += decays * group.fluidOutputSize;
		}
		return storage;
	}
	
	protected boolean hasSourceRecipeIngredient(SourceRecipeGroup group) {
		if (group.recipe == null) {
			return false;
		}
		for (ItemIngredientGroup ingredientGroup : itemIngredientGroups) {
			if (ingredientGroup.recipe == group.recipe && ingredientGroup.amount > 0L) {
				return true;
			}
		}
		for (FluidIngredientGroup ingredientGroup : fluidIngredientGroups) {
			if (ingredientGroup.recipe == group.recipe && ingredientGroup.amount > 0L) {
				return true;
			}
		}
		return false;
	}
	
	protected long getAvailableSourceRecipeProductionCount(SourceRecipeGroup group) {
		if (group.recipe == null) {
			return 0L;
		}
		
		long completions = Long.MAX_VALUE;
		if (group.hasItemIngredient) {
			long itemCompletions = 0L;
			for (ItemIngredientGroup ingredientGroup : itemIngredientGroups) {
				if (ingredientGroup.recipe == group.recipe && ingredientGroup.ingredientSize > 0) {
					itemCompletions += ingredientGroup.amount / ingredientGroup.ingredientSize;
				}
			}
			completions = Math.min(completions, itemCompletions);
		}
		if (group.hasFluidIngredient) {
			long fluidCompletions = 0L;
			for (FluidIngredientGroup ingredientGroup : fluidIngredientGroups) {
				if (ingredientGroup.recipe == group.recipe && ingredientGroup.ingredientSize > 0) {
					fluidCompletions += ingredientGroup.amount / ingredientGroup.ingredientSize;
				}
			}
			completions = Math.min(completions, fluidCompletions);
		}
		return completions == Long.MAX_VALUE ? 0L : completions;
	}
	
	protected void addSourceRecipeInfo(SourceRecipeInfo info) {
		SourceRecipeGroup group = getSourceRecipeGroup(info.recipe);
		if (group == null) {
			group = new SourceRecipeGroup(info);
			sourceRecipeGroups.add(group);
		}
		else {
			group.assimilateSourceInfo(info);
		}
	}
	
	protected @Nullable SourceRecipeGroup getSourceRecipeGroup(@Nullable BasicRecipe recipe) {
		if (recipe == null) {
			return null;
		}
		for (SourceRecipeGroup group : sourceRecipeGroups) {
			if (group.recipe == recipe) {
				return group;
			}
		}
		return null;
	}
	
	public long insertItemIngredient(ItemStack stack, boolean simulate) {
		if (!multiblock.isAssembled()) {
			return 0L;
		}
		
		SourceRecipeInfo info = getSourceRecipeInfo(stack);
		if (info == null) {
			return 0L;
		}
		
		ItemIngredientGroup group = null;
		for (ItemIngredientGroup ingredientGroup : itemIngredientGroups) {
			if (itemStacksEqual(ingredientGroup.stack, stack)) {
				group = ingredientGroup;
				break;
			}
		}
		long availableStorage = getStorageCapacity() - getIngredientStorageUsed();
		long inserted = availableStorage <= 0L ? 0L : Math.min(stack.getCount(), availableStorage / ITEM_STORAGE_CAPACITY);
		if (!simulate && inserted > 0L) {
			if (group == null) {
				group = new ItemIngredientGroup(normalizedItemStack(stack));
				itemIngredientGroups.add(group);
			}
			group.setRecipeInfo(info);
			group.amount += inserted;
			addSourceRecipeInfo(info);
			refreshActivity();
		}
		return inserted;
	}
	
	public long insertFluidIngredient(FluidStack stack, boolean simulate) {
		if (!multiblock.isAssembled()) {
			return 0L;
		}
		
		SourceRecipeInfo info = getSourceRecipeInfo(stack);
		if (info == null) {
			return 0L;
		}
		
		FluidIngredientGroup group = null;
		for (FluidIngredientGroup ingredientGroup : fluidIngredientGroups) {
			if (fluidStacksEqual(ingredientGroup.stack, stack)) {
				group = ingredientGroup;
				break;
			}
		}
		long availableStorage = getStorageCapacity() - getIngredientStorageUsed();
		long inserted = availableStorage <= 0L ? 0L : Math.min(stack.amount, availableStorage);
		if (!simulate && inserted > 0L) {
			if (group == null) {
				group = new FluidIngredientGroup(normalizedFluidStack(stack));
				fluidIngredientGroups.add(group);
			}
			group.setRecipeInfo(info);
			group.amount += inserted;
			addSourceRecipeInfo(info);
			refreshActivity();
		}
		return inserted;
	}
	
	protected @Nullable SourceRecipeInfo getSourceRecipeInfo(ItemStack stack) {
		if (stack.isEmpty()) {
			return null;
		}
		
		for (BasicRecipe recipe : NCRecipes.decay_pool_heat_source.getRecipeList()) {
			IItemIngredient itemIngredient = recipe.getItemIngredients().get(0);
			if (itemIngredient.isEmpty()) {
				continue;
			}
			
			IngredientMatchResult matchResult = itemIngredient.match(stack, IngredientSorption.NEUTRAL);
			if (matchResult.matches()) {
				return new SourceRecipeInfo(recipe, normalizedItemStack(stack), null, itemIngredient.getMaxStackSize(matchResult.getIngredientNumber()));
			}
		}
		return null;
	}
	
	protected @Nullable SourceRecipeInfo getSourceRecipeInfo(@Nullable FluidStack stack) {
		if (stack == null || stack.amount <= 0) {
			return null;
		}
		
		for (BasicRecipe recipe : NCRecipes.decay_pool_heat_source.getRecipeList()) {
			IFluidIngredient fluidIngredient = recipe.getFluidIngredients().get(0);
			if (fluidIngredient.isEmpty()) {
				continue;
			}
			
			IngredientMatchResult matchResult = fluidIngredient.match(stack, IngredientSorption.NEUTRAL);
			if (matchResult.matches()) {
				return new SourceRecipeInfo(recipe, ItemStack.EMPTY, normalizedFluidStack(stack), fluidIngredient.getMaxStackSize(matchResult.getIngredientNumber()));
			}
		}
		return null;
	}
	
	protected void addItemProduct(ItemStack stack, long amount) {
		if (stack.isEmpty() || amount <= 0L) {
			return;
		}
		
		ItemProductGroup group = null;
		for (ItemProductGroup productGroup : itemProductGroups) {
			if (itemStacksEqual(productGroup.stack, stack)) {
				group = productGroup;
				break;
			}
		}
		if (group == null) {
			itemProductGroups.add(new ItemProductGroup(normalizedItemStack(stack), amount));
		}
		else {
			group.amount += amount;
		}
	}
	
	protected void addFluidProduct(FluidStack stack, long amount) {
		if (stack == null || amount <= 0L) {
			return;
		}
		
		FluidProductGroup group = null;
		for (FluidProductGroup productGroup : fluidProductGroups) {
			if (fluidStacksEqual(productGroup.stack, stack)) {
				group = productGroup;
				break;
			}
		}
		if (group == null) {
			fluidProductGroups.add(new FluidProductGroup(normalizedFluidStack(stack), amount));
		}
		else {
			group.amount += amount;
		}
	}
	
	protected ItemStack nextItemProduct() {
		for (ItemProductGroup group : itemProductGroups) {
			if (group.amount > 0L) {
				ItemStack stack = group.stack.copy();
				stack.setCount((int) Math.min(group.amount, stack.getMaxStackSize()));
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}
	
	protected ItemStack extractItemProduct(int amount, boolean simulate) {
		if (amount <= 0) {
			return ItemStack.EMPTY;
		}
		
		Iterator<ItemProductGroup> iterator = itemProductGroups.iterator();
		while (iterator.hasNext()) {
			ItemProductGroup group = iterator.next();
			if (group.amount <= 0L) {
				iterator.remove();
				continue;
			}
			
			ItemStack stack = group.stack.copy();
			int extracted = (int) Math.min(Math.min(group.amount, amount), stack.getMaxStackSize());
			stack.setCount(extracted);
			if (!simulate) {
				group.amount -= extracted;
				if (group.amount <= 0L) {
					iterator.remove();
				}
				refreshActivity();
			}
			return stack;
		}
		return ItemStack.EMPTY;
	}
	
	protected @Nullable FluidStack peekFluidProduct(int amount) {
		if (amount <= 0) {
			return null;
		}
		
		for (FluidProductGroup group : fluidProductGroups) {
			if (group.amount > 0L) {
				FluidStack stack = group.stack.copy();
				stack.amount = (int) Math.min(group.amount, amount);
				return stack;
			}
		}
		return null;
	}
	
	protected @Nullable FluidStack extractFluidProduct(@Nullable FluidStack resource, boolean simulate) {
		if (resource == null || resource.amount <= 0) {
			return null;
		}
		
		Iterator<FluidProductGroup> iterator = fluidProductGroups.iterator();
		while (iterator.hasNext()) {
			FluidProductGroup group = iterator.next();
			if (group.amount <= 0L) {
				iterator.remove();
				continue;
			}
			if (!fluidStacksEqual(group.stack, resource)) {
				continue;
			}
			
			FluidStack stack = group.stack.copy();
			stack.amount = (int) Math.min(group.amount, resource.amount);
			if (!simulate) {
				group.amount -= stack.amount;
				if (group.amount <= 0L) {
					iterator.remove();
				}
				refreshActivity();
			}
			return stack;
		}
		return null;
	}
	
	@Override
	protected void setRecipeStats(@Nullable BasicRecipe recipe) {
		if (recipe == null) {
			multiblock.processor.baseProcessTime = defaultProcessTime();
		}
		else {
			RecipeInfo<BasicRecipe> recipeInfo = multiblock.processor.recipeInfo;
			int ingredientSize = recipeInfo == null ? recipe.getFluidIngredients().get(0).getMaxStackSize(0) : recipe.getFluidIngredients().get(0).getMaxStackSize(recipeInfo.getFluidIngredientNumbers().get(0));
			multiblock.processor.baseProcessTime = (double) recipe.getDecayPoolHeatPerInputMB() * ingredientSize;
		}
		multiblock.baseProcessPower = 0D;
	}
	
	@Override
	protected double getSpeedMultiplier() {
		return multiblock.baseSpeedMultiplier;
	}
	
	@Override
	protected double getPowerMultiplier() {
		return 0D;
	}
	
	@Override
	protected boolean readyToProcess() {
		return multiblock.baseSpeedMultiplier > 0D && multiblock.processor.baseProcessTime > 0D && super.readyToProcess();
	}
	
	@Override
	public void refreshActivity() {
		refreshContainerHeat();
		super.refreshActivity();
	}
	
	@Override
	public void writeToLogicTag(NBTTagCompound logicTag, SyncReason syncReason) {
		super.writeToLogicTag(logicTag, syncReason);
		
		NBTTagList sourceRecipeList = new NBTTagList();
		for (SourceRecipeGroup group : sourceRecipeGroups) {
			sourceRecipeList.appendTag(group.writeToNBT());
		}
		logicTag.setTag("sourceRecipeGroups", sourceRecipeList);
		
		NBTTagList itemIngredientList = new NBTTagList();
		for (ItemIngredientGroup group : itemIngredientGroups) {
			itemIngredientList.appendTag(group.writeToNBT());
		}
		logicTag.setTag("itemIngredientGroups", itemIngredientList);
		
		NBTTagList fluidIngredientList = new NBTTagList();
		for (FluidIngredientGroup group : fluidIngredientGroups) {
			fluidIngredientList.appendTag(group.writeToNBT());
		}
		logicTag.setTag("fluidIngredientGroups", fluidIngredientList);
		
		NBTTagList itemProductList = new NBTTagList();
		for (ItemProductGroup group : itemProductGroups) {
			itemProductList.appendTag(group.writeToNBT());
		}
		logicTag.setTag("itemProductGroups", itemProductList);
		
		NBTTagList fluidProductList = new NBTTagList();
		for (FluidProductGroup group : fluidProductGroups) {
			fluidProductList.appendTag(group.writeToNBT());
		}
		logicTag.setTag("fluidProductGroups", fluidProductList);
	}
	
	@Override
	public void readFromLogicTag(NBTTagCompound logicTag, SyncReason syncReason) {
		super.readFromLogicTag(logicTag, syncReason);
		
		sourceRecipeGroups.clear();
		NBTTagList sourceRecipeList = logicTag.getTagList("sourceRecipeGroups", 10);
		for (int i = 0; i < sourceRecipeList.tagCount(); ++i) {
			SourceRecipeGroup group = SourceRecipeGroup.readFromNBT(sourceRecipeList.getCompoundTagAt(i));
			if (group != null) {
				sourceRecipeGroups.add(group);
			}
		}
		
		itemIngredientGroups.clear();
		NBTTagList itemIngredientList = logicTag.getTagList("itemIngredientGroups", 10);
		for (int i = 0; i < itemIngredientList.tagCount(); ++i) {
			ItemIngredientGroup group = ItemIngredientGroup.readFromNBT(itemIngredientList.getCompoundTagAt(i));
			if (group != null) {
				itemIngredientGroups.add(group);
			}
		}
		
		fluidIngredientGroups.clear();
		NBTTagList fluidIngredientList = logicTag.getTagList("fluidIngredientGroups", 10);
		for (int i = 0; i < fluidIngredientList.tagCount(); ++i) {
			FluidIngredientGroup group = FluidIngredientGroup.readFromNBT(fluidIngredientList.getCompoundTagAt(i));
			if (group != null) {
				fluidIngredientGroups.add(group);
			}
		}
		
		itemProductGroups.clear();
		NBTTagList itemProductList = logicTag.getTagList("itemProductGroups", 10);
		for (int i = 0; i < itemProductList.tagCount(); ++i) {
			ItemProductGroup group = ItemProductGroup.readFromNBT(itemProductList.getCompoundTagAt(i));
			if (group != null) {
				itemProductGroups.add(group);
			}
		}
		
		fluidProductGroups.clear();
		NBTTagList fluidProductList = logicTag.getTagList("fluidProductGroups", 10);
		for (int i = 0; i < fluidProductList.tagCount(); ++i) {
			FluidProductGroup group = FluidProductGroup.readFromNBT(fluidProductList.getCompoundTagAt(i));
			if (group != null) {
				fluidProductGroups.add(group);
			}
		}
		
		for (SourceRecipeGroup group : sourceRecipeGroups) {
			SourceRecipeInfo itemInfo = group.itemIngredient.isEmpty() ? null : getSourceRecipeInfo(group.itemIngredient);
			if (itemInfo != null) {
				group.setRecipe(itemInfo.recipe);
			}
			else {
				SourceRecipeInfo fluidInfo = getSourceRecipeInfo(group.fluidIngredient);
				group.setRecipe(fluidInfo == null ? null : fluidInfo.recipe);
			}
		}
		for (ItemIngredientGroup group : itemIngredientGroups) {
			SourceRecipeInfo info = getSourceRecipeInfo(group.stack);
			group.setRecipeInfo(info);
			if (info != null) {
				addSourceRecipeInfo(info);
			}
		}
		for (FluidIngredientGroup group : fluidIngredientGroups) {
			SourceRecipeInfo info = getSourceRecipeInfo(group.stack);
			group.setRecipeInfo(info);
			if (info != null) {
				addSourceRecipeInfo(info);
			}
		}
		sourceRecipeGroups.removeIf(group -> !hasSourceRecipeIngredient(group));
		refreshContainerHeat();
	}
	
	@Override
	public MachineUpdatePacket getMultiblockUpdatePacket() {
		return new DecayPoolUpdatePacket(multiblock.controller.getTilePos(), multiblock.isMachineOn, multiblock.processor.isProcessing, multiblock.processor.time, multiblock.processor.baseProcessTime, multiblock.baseProcessPower, multiblock.tanks, multiblock.baseSpeedMultiplier, multiblock.basePowerMultiplier, multiblock.recipeUnitInfo, multiblock.readyToProcess, totalDecayRate);
	}
	
	@Override
	public void onMultiblockUpdatePacket(MachineUpdatePacket message) {
		super.onMultiblockUpdatePacket(message);
		if (message instanceof DecayPoolUpdatePacket packet) {
			totalDecayRate = packet.totalDecayRate;
		}
	}
	
	@Override
	public DecayPoolRenderPacket getRenderPacket() {
		return new DecayPoolRenderPacket(multiblock.controller.getTilePos(), multiblock.isMachineOn, multiblock.processor.isProcessing, multiblock.tanks);
	}
	
	@Override
	public void onRenderPacket(MachineRenderPacket message) {
		super.onRenderPacket(message);
		if (message instanceof DecayPoolRenderPacket packet) {
			boolean wasProcessing = multiblock.processor.isProcessing;
			multiblock.processor.isProcessing = packet.isProcessing;
			if (wasProcessing != multiblock.processor.isProcessing) {
				multiblock.refreshSounds = true;
			}
			TankInfo.readInfoList(packet.tankInfos, multiblock.tanks);
		}
	}
	
	protected static class SourceRecipeGroup {
		
		protected @Nullable BasicRecipe recipe;
		protected ItemStack itemIngredient = ItemStack.EMPTY, itemOutput = ItemStack.EMPTY;
		protected @Nullable FluidStack fluidIngredient, fluidOutput;
		protected double decayRemainder, meanLifetime, heatPerTick;
		protected boolean hasItemIngredient, hasFluidIngredient;
		protected int itemOutputSize, fluidOutputSize;
		
		protected SourceRecipeGroup() {}
		
		protected SourceRecipeGroup(SourceRecipeInfo info) {
			assimilateSourceInfo(info);
		}
		
		protected SourceRecipeGroup copy() {
			SourceRecipeGroup copy = new SourceRecipeGroup();
			copy.recipe = recipe;
			copy.itemIngredient = itemIngredient.copy();
			copy.fluidIngredient = fluidIngredient == null ? null : fluidIngredient.copy();
			copy.itemOutput = itemOutput.copy();
			copy.fluidOutput = fluidOutput == null ? null : fluidOutput.copy();
			copy.decayRemainder = decayRemainder;
			copy.meanLifetime = meanLifetime;
			copy.heatPerTick = heatPerTick;
			copy.hasItemIngredient = hasItemIngredient;
			copy.hasFluidIngredient = hasFluidIngredient;
			copy.itemOutputSize = itemOutputSize;
			copy.fluidOutputSize = fluidOutputSize;
			return copy;
		}
		
		protected void assimilateSourceInfo(SourceRecipeInfo info) {
			if (!info.itemIngredient.isEmpty()) {
				itemIngredient = info.itemIngredient.copy();
			}
			if (info.fluidIngredient != null) {
				fluidIngredient = info.fluidIngredient.copy();
			}
			setRecipe(info.recipe);
		}
		
		protected void assimilateIngredientKeys(SourceRecipeGroup group) {
			if (itemIngredient.isEmpty() && !group.itemIngredient.isEmpty()) {
				itemIngredient = group.itemIngredient.copy();
			}
			if (fluidIngredient == null && group.fluidIngredient != null) {
				fluidIngredient = group.fluidIngredient.copy();
			}
		}
		
		protected void setRecipe(@Nullable BasicRecipe recipe) {
			this.recipe = recipe;
			if (recipe == null) {
				meanLifetime = Double.POSITIVE_INFINITY;
				heatPerTick = 0D;
				hasItemIngredient = hasFluidIngredient = false;
				itemOutput = ItemStack.EMPTY;
				fluidOutput = null;
				itemOutputSize = fluidOutputSize = 0;
				return;
			}
			
			IItemIngredient itemIngredient = recipe.getItemIngredients().get(0);
			IFluidIngredient fluidIngredient = recipe.getFluidIngredients().get(0);
			hasItemIngredient = !itemIngredient.isEmpty();
			hasFluidIngredient = !fluidIngredient.isEmpty();
			
			meanLifetime = recipe.getDecayPoolContainerLifetime();
			heatPerTick = recipe.getDecayPoolContainerHeat();
			
			IItemIngredient itemProduct = recipe.getItemProducts().get(0);
			itemOutput = itemProduct.isEmpty() ? ItemStack.EMPTY : normalizedItemStack(itemProduct.getNextStack(0));
			itemOutputSize = itemProduct.isEmpty() ? 0 : itemProduct.getMaxStackSize(0);
			
			IFluidIngredient fluidProduct = recipe.getFluidProducts().get(0);
			fluidOutput = fluidProduct.isEmpty() ? null : normalizedFluidStack(fluidProduct.getNextStack(0));
			fluidOutputSize = fluidProduct.isEmpty() ? 0 : fluidProduct.getMaxStackSize(0);
		}
		
		protected NBTTagCompound writeToNBT() {
			NBTTagCompound tag = new NBTTagCompound();
			if (!itemIngredient.isEmpty()) {
				tag.setTag("itemIngredient", itemIngredient.writeToNBT(new NBTTagCompound()));
			}
			if (fluidIngredient != null) {
				tag.setTag("fluidIngredient", fluidIngredient.writeToNBT(new NBTTagCompound()));
			}
			tag.setDouble("decayRemainder", decayRemainder);
			return tag;
		}
		
		protected static @Nullable SourceRecipeGroup readFromNBT(NBTTagCompound tag) {
			ItemStack itemIngredient = tag.hasKey("itemIngredient", 10) ? new ItemStack(tag.getCompoundTag("itemIngredient")) : ItemStack.EMPTY;
			FluidStack fluidIngredient = tag.hasKey("fluidIngredient", 10) ? FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("fluidIngredient")) : null;
			if (itemIngredient.isEmpty() && fluidIngredient == null) {
				return null;
			}
			
			SourceRecipeGroup group = new SourceRecipeGroup();
			group.itemIngredient = normalizedItemStack(itemIngredient);
			group.fluidIngredient = normalizedFluidStack(fluidIngredient);
			group.decayRemainder = tag.getDouble("decayRemainder");
			return group;
		}
	}
	
	protected static class SourceRecipeInfo {
		
		protected final BasicRecipe recipe;
		protected final ItemStack itemIngredient;
		protected final @Nullable FluidStack fluidIngredient;
		protected final int ingredientSize;
		
		protected SourceRecipeInfo(BasicRecipe recipe, ItemStack itemIngredient, @Nullable FluidStack fluidIngredient, int ingredientSize) {
			this.recipe = recipe;
			this.itemIngredient = itemIngredient;
			this.fluidIngredient = fluidIngredient;
			this.ingredientSize = ingredientSize;
		}
	}
	
	protected static class ItemIngredientGroup {
		
		protected final ItemStack stack;
		protected long amount;
		protected @Nullable BasicRecipe recipe;
		protected int ingredientSize = 1;
		
		protected ItemIngredientGroup(ItemStack stack) {
			this.stack = stack;
		}
		
		protected ItemIngredientGroup copy() {
			ItemIngredientGroup copy = new ItemIngredientGroup(stack.copy());
			copy.amount = amount;
			copy.recipe = recipe;
			copy.ingredientSize = ingredientSize;
			return copy;
		}
		
		protected void setRecipeInfo(@Nullable SourceRecipeInfo info) {
			if (info == null) {
				recipe = null;
				ingredientSize = 1;
				return;
			}
			recipe = info.recipe;
			ingredientSize = info.ingredientSize;
		}
		
		protected NBTTagCompound writeToNBT() {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setTag("stack", stack.writeToNBT(new NBTTagCompound()));
			tag.setLong("amount", amount);
			return tag;
		}
		
		protected static @Nullable ItemIngredientGroup readFromNBT(NBTTagCompound tag) {
			ItemStack stack = tag.hasKey("stack", 10) ? new ItemStack(tag.getCompoundTag("stack")) : ItemStack.EMPTY;
			long amount = Math.max(0L, tag.getLong("amount"));
			if (stack.isEmpty() || amount == 0L) {
				return null;
			}
			
			ItemIngredientGroup group = new ItemIngredientGroup(normalizedItemStack(stack));
			group.amount = amount;
			return group;
		}
	}
	
	protected static class FluidIngredientGroup {
		
		protected final FluidStack stack;
		protected long amount;
		protected @Nullable BasicRecipe recipe;
		protected int ingredientSize = 1;
		
		protected FluidIngredientGroup(FluidStack stack) {
			this.stack = stack;
		}
		
		protected FluidIngredientGroup copy() {
			FluidIngredientGroup copy = new FluidIngredientGroup(stack.copy());
			copy.amount = amount;
			copy.recipe = recipe;
			copy.ingredientSize = ingredientSize;
			return copy;
		}
		
		protected void setRecipeInfo(@Nullable SourceRecipeInfo info) {
			if (info == null) {
				recipe = null;
				ingredientSize = 1;
				return;
			}
			recipe = info.recipe;
			ingredientSize = info.ingredientSize;
		}
		
		protected NBTTagCompound writeToNBT() {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setTag("stack", stack.writeToNBT(new NBTTagCompound()));
			tag.setLong("amount", amount);
			return tag;
		}
		
		protected static @Nullable FluidIngredientGroup readFromNBT(NBTTagCompound tag) {
			FluidStack stack = tag.hasKey("stack", 10) ? FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("stack")) : null;
			long amount = Math.max(0L, tag.getLong("amount"));
			if (stack == null || amount == 0L) {
				return null;
			}
			
			FluidIngredientGroup group = new FluidIngredientGroup(normalizedFluidStack(stack));
			group.amount = amount;
			return group;
		}
	}
	
	protected static class ItemProductGroup {
		
		protected final ItemStack stack;
		protected long amount;
		
		protected ItemProductGroup(ItemStack stack, long amount) {
			this.stack = stack;
			this.amount = amount;
		}
		
		protected ItemProductGroup copy() {
			return new ItemProductGroup(stack.copy(), amount);
		}
		
		protected NBTTagCompound writeToNBT() {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setTag("stack", stack.writeToNBT(new NBTTagCompound()));
			tag.setLong("amount", amount);
			return tag;
		}
		
		protected static @Nullable ItemProductGroup readFromNBT(NBTTagCompound tag) {
			ItemStack stack = tag.hasKey("stack", 10) ? new ItemStack(tag.getCompoundTag("stack")) : ItemStack.EMPTY;
			long amount = Math.max(0L, tag.getLong("amount"));
			return stack.isEmpty() || amount == 0L ? null : new ItemProductGroup(stack, amount);
		}
	}
	
	protected static class FluidProductGroup {
		
		protected final FluidStack stack;
		protected long amount;
		
		protected FluidProductGroup(FluidStack stack, long amount) {
			this.stack = stack;
			this.amount = amount;
		}
		
		protected FluidProductGroup copy() {
			return new FluidProductGroup(stack.copy(), amount);
		}
		
		protected NBTTagCompound writeToNBT() {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setTag("stack", stack.writeToNBT(new NBTTagCompound()));
			tag.setLong("amount", amount);
			return tag;
		}
		
		protected static @Nullable FluidProductGroup readFromNBT(NBTTagCompound tag) {
			FluidStack stack = tag.hasKey("stack", 10) ? FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("stack")) : null;
			long amount = Math.max(0L, tag.getLong("amount"));
			return stack == null || amount == 0L ? null : new FluidProductGroup(stack, amount);
		}
	}
	
	protected static ItemStack normalizedItemStack(ItemStack stack) {
		if (stack.isEmpty()) {
			return ItemStack.EMPTY;
		}
		ItemStack copy = stack.copy();
		copy.setCount(1);
		return copy;
	}
	
	protected static @Nullable FluidStack normalizedFluidStack(@Nullable FluidStack stack) {
		if (stack == null || stack.amount <= 0) {
			return null;
		}
		FluidStack copy = stack.copy();
		copy.amount = 1;
		return copy;
	}
	
	protected static boolean itemStacksEqual(ItemStack a, ItemStack b) {
		return !a.isEmpty() && !b.isEmpty() && a.isItemEqual(b) && StackHelper.areItemStackTagsEqual(a, b);
	}
	
	protected static boolean fluidStacksEqual(@Nullable FluidStack a, @Nullable FluidStack b) {
		return a != null && b != null && a.isFluidEqual(b) && FluidStack.areFluidStackTagsEqual(a, b);
	}
}
