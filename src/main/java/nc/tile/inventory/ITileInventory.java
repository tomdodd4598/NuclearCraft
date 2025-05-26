package nc.tile.inventory;

import com.google.common.collect.Lists;
import nc.tile.ITile;
import nc.tile.hx.IHeatExchangerPart;
import nc.tile.internal.inventory.*;
import nc.tile.machine.IMachinePart;
import nc.tile.multiblock.port.ITilePort;
import nc.tile.processor.IProcessor;
import nc.tile.processor.IProcessor.HandlerPair;
import nc.util.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.items.*;

import javax.annotation.*;
import java.util.*;

public interface ITileInventory extends ITile, ISidedInventory {
	
	// Inventory
	
	@Nonnull NonNullList<ItemStack> getInventoryStacks();
	
	default void clearAllSlots() {
		@Nonnull NonNullList<ItemStack> stacks = getInventoryStacks();
		Collections.fill(stacks, ItemStack.EMPTY);
	}
	
	// IInventory
	
	@Override
	default boolean hasCustomName() {
		return false;
	}
	
	@Override
	default int getSizeInventory() {
		return getInventoryStacks().size();
	}
	
	@Override
	default boolean isEmpty() {
		for (ItemStack stack : getInventoryStacks()) {
			if (!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	default ItemStack getStackInSlot(int slot) {
		return getInventoryStacks().get(slot);
	}
	
	@Override
	default ItemStack decrStackSize(int slot, int count) {
		return ItemStackHelper.getAndSplit(getInventoryStacks(), slot, count);
	}
	
	@Override
	default ItemStack removeStackFromSlot(int slot) {
		return ItemStackHelper.getAndRemove(getInventoryStacks(), slot);
	}
	
	@Override
	default void setInventorySlotContents(int slot, ItemStack stack) {
		@Nonnull NonNullList<ItemStack> stacks = getInventoryStacks();
		ItemStack itemstack = stacks.get(slot);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && StackHelper.areItemStackTagsEqual(stack, itemstack);
		
		if (stack.getCount() > getInventoryStackLimit()) {
			stack.setCount(getInventoryStackLimit());
		}
		
		stacks.set(slot, stack);
		
		if (!flag) {
			markTileDirty();
		}
	}
	
	@Override
	default boolean isItemValidForSlot(int slot, ItemStack stack) {
		return true;
	}
	
	@Override
	default int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	default void clear() {
		getInventoryStacks().clear();
	}
	
	@Override
	default void openInventory(EntityPlayer player) {}
	
	@Override
	default void closeInventory(EntityPlayer player) {}
	
	@Override
	default boolean isUsableByPlayer(EntityPlayer player) {
		return ITile.super.isUsableByPlayer(player);
	}
	
	@Override
	default int getField(int id) {
		return 0;
	}
	
	@Override
	default void setField(int id, int value) {}
	
	@Override
	default int getFieldCount() {
		return 0;
	}
	
	@Override
	String getName();
	
	// ISidedInventory
	
	@Override
	default int[] getSlotsForFace(EnumFacing side) {
		return getInventoryConnection(side).getSlotsForFace();
	}
	
	@Override
	default boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
		return getItemSorption(side, slot).canReceive();
	}
	
	@Override
	default boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
		return getItemSorption(side, slot).canExtract();
	}
	
	// Inventory Connections
	
	@Nonnull
	InventoryConnection[] getInventoryConnections();
	
	void setInventoryConnections(@Nonnull InventoryConnection[] connections);
	
	default @Nonnull InventoryConnection getInventoryConnection(@Nonnull EnumFacing side) {
		return getInventoryConnections()[side.getIndex()];
	}
	
	default @Nonnull ItemSorption getItemSorption(@Nonnull EnumFacing side, int slotNumber) {
		return getInventoryConnections()[side.getIndex()].getItemSorption(slotNumber);
	}
	
	default void setItemSorption(@Nonnull EnumFacing side, int slotNumber, @Nonnull ItemSorption sorption) {
		getInventoryConnections()[side.getIndex()].setItemSorption(slotNumber, sorption);
	}
	
	default void toggleItemSorption(@Nonnull EnumFacing side, int slotNumber, ItemSorption.Type type, boolean reverse) {
		if (!hasConfigurableInventoryConnections()) {
			return;
		}
		getInventoryConnection(side).toggleItemSorption(slotNumber, type, reverse);
		markDirtyAndNotify(true);
	}
	
	default boolean canConnectInventory(@Nonnull EnumFacing side) {
		return getInventoryConnection(side).canConnect();
	}
	
	static InventoryConnection[] inventoryConnectionAll(@Nonnull List<ItemSorption> sorptionList) {
		InventoryConnection[] array = new InventoryConnection[6];
		for (int i = 0; i < 6; ++i) {
			array[i] = new InventoryConnection(sorptionList);
		}
		return array;
	}
	
	static InventoryConnection[] inventoryConnectionAll(ItemSorption sorption) {
		return inventoryConnectionAll(Lists.newArrayList(sorption));
	}
	
	default boolean hasConfigurableInventoryConnections() {
		return false;
	}
	
	// Item Distribution
	
	default void pushStacks() {
		for (EnumFacing side : EnumFacing.VALUES) {
			pushStacksToSide(side);
		}
	}
	
	default void pushStacksToSide(@Nonnull EnumFacing side) {
		if (!getInventoryConnection(side).canConnect()) {
			return;
		}
		
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		if (tile == null) {
			return;
		}
		
		EnumFacing oppositeSide = side.getOpposite();
		if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, oppositeSide)) {
			IItemHandler adjInv = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, oppositeSide);
			if (adjInv == null || adjInv.getSlots() < 1) {
				return;
			}
			
			boolean pushed = false;
			
			@Nonnull NonNullList<ItemStack> stacks = getInventoryStacks();
			int stackCount = stacks.size();
			for (int i = 0; i < stackCount; ++i) {
				pushed |= pushSlotToHandler(adjInv, stacks, side, i);
			}
			
			if (pushed) {
				if (this instanceof IProcessor<?, ?, ?> processor) {
					processor.refreshActivity();
				}
				else if (this instanceof IMachinePart part) {
					part.refreshMachineActivity();
				}
				else if (this instanceof IHeatExchangerPart part) {
					part.refreshHeatExchangerActivity();
				}
				
				if (this instanceof ITilePort<?, ?, ?, ?, ?> port) {
					port.setRefreshTargetsFlag(true);
				}
			}
		}
	}
	
	default boolean pushSlotToHandler(IItemHandler handler, NonNullList<ItemStack> stacks, @Nonnull EnumFacing side, int slot) {
		ItemStack stack;
		if (!getItemSorption(side, slot).canExtract() || (stack = stacks.get(slot)).isEmpty()) {
			return false;
		}
		
		ItemStack initialStack = stack.copy();
		ItemStack remaining = NCInventoryHelper.addStackToInventory(handler, initialStack);
		
		if (remaining.getCount() >= initialStack.getCount()) {
			return false;
		}
		
		stack.shrink(initialStack.getCount() - remaining.getCount());
		
		if (stack.getCount() <= 0) {
			stacks.set(slot, ItemStack.EMPTY);
		}
		
		return true;
	}
	
	default boolean tryPushSlot(HandlerPair[] adjacentHandlers, NonNullList<ItemStack> stacks, int slot, List<EnumFacing> dirs, int dirCount, int indexOffset) {
		if (!stacks.get(slot).isEmpty()) {
			for (int i = 0; i < dirCount; ++i) {
				EnumFacing dir = dirs.get((i + indexOffset) % dirCount);
				if (getItemSorption(dir, slot).equals(ItemSorption.AUTO_OUT)) {
					IItemHandler handler = adjacentHandlers[dir.getIndex()].itemHandler;
					if (handler != null && pushSlotToHandler(handler, stacks, dir, slot)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	// NBT
	
	default NBTTagCompound writeInventory(NBTTagCompound nbt) {
		ItemStackHelper.saveAllItems(nbt, getInventoryStacks());
		return nbt;
	}
	
	default void readInventory(NBTTagCompound nbt) {
		ItemStackHelper.loadAllItems(nbt, getInventoryStacks());
	}
	
	default NBTTagCompound writeInventoryConnections(NBTTagCompound nbt) {
		return writeInventoryConnectionsDirectional(nbt, null);
	}
	
	default NBTTagCompound writeInventoryConnectionsDirectional(NBTTagCompound nbt, @Nullable EnumFacing facing) {
		for (int i = 0; i < 6; ++i) {
			getInventoryConnection(BlockHelper.DIR_FROM_FACING[i].apply(facing)).writeToNBT(nbt, EnumFacing.byIndex(i));
		}
		return nbt;
	}
	
	default void readInventoryConnections(NBTTagCompound nbt) {
		readInventoryConnectionsDirectional(nbt, null);
	}
	
	default void readInventoryConnectionsDirectional(NBTTagCompound nbt, @Nullable EnumFacing facing) {
		if (!hasConfigurableInventoryConnections()) {
			return;
		}
		for (int i = 0; i < 6; ++i) {
			getInventoryConnection(BlockHelper.DIR_FROM_FACING[i].apply(facing)).readFromNBT(nbt, EnumFacing.byIndex(i));
		}
	}
	
	default NBTTagCompound writeSlotSettings(NBTTagCompound nbt) {
		for (int i = 0; i < getSizeInventory(); ++i) {
			nbt.setInteger("itemOutputSetting" + i, getItemOutputSetting(i).ordinal());
		}
		return nbt;
	}
	
	default void readSlotSettings(NBTTagCompound nbt) {
		for (int i = 0; i < getSizeInventory(); ++i) {
			setItemOutputSetting(i, ItemOutputSetting.values()[nbt.getInteger("itemOutputSetting" + i)]);
		}
	}
	
	// Item Functions
	
	ItemOutputSetting getItemOutputSetting(int slot);
	
	void setItemOutputSetting(int slot, ItemOutputSetting setting);
	
	// Capabilities
	
	default boolean hasInventorySideCapability(@Nullable EnumFacing side) {
		return side == null || getInventoryConnection(side).canConnect();
	}
	
	default IItemHandler getItemHandler(@Nullable EnumFacing side) {
		return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new ItemHandler<>(this, side));
	}
}
