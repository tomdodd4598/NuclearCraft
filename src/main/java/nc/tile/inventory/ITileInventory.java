package nc.tile.inventory;

import java.util.List;

import javax.annotation.*;

import com.google.common.collect.Lists;

import nc.multiblock.tile.port.ITilePort;
import nc.tile.ITile;
import nc.tile.internal.inventory.*;
import nc.tile.processor.IProcessor;
import nc.util.NCInventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.items.*;

public interface ITileInventory extends ITile, ISidedInventory {
	
	// Inventory
	
	public @Nonnull NonNullList<ItemStack> getInventoryStacks();
	
	// Inventory Logic
	
	public default void clearSlot(int slot) {
		getInventoryStacks().set(slot, ItemStack.EMPTY);
	}
	
	public default void clearAllSlots() {
		for (int i = 0; i < getInventoryStacks().size(); ++i) {
			clearSlot(i);
		}
	}
	
	// IInventory
	
	@Override
	public default boolean hasCustomName() {
		return false;
	}
	
	@Override
	public default int getSizeInventory() {
		return getInventoryStacks().size();
	}
	
	@Override
	public default boolean isEmpty() {
		for (ItemStack itemstack : getInventoryStacks()) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public default ItemStack getStackInSlot(int slot) {
		return getInventoryStacks().get(slot);
	}
	
	@Override
	public default ItemStack decrStackSize(int slot, int count) {
		return ItemStackHelper.getAndSplit(getInventoryStacks(), slot, count);
	}
	
	@Override
	public default ItemStack removeStackFromSlot(int slot) {
		return ItemStackHelper.getAndRemove(getInventoryStacks(), slot);
	}
	
	@Override
	public default void setInventorySlotContents(int slot, ItemStack stack) {
		ItemStack itemstack = getInventoryStacks().get(slot);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && nc.util.StackHelper.areItemStackTagsEqual(stack, itemstack);
		
		if (stack.getCount() > getInventoryStackLimit()) {
			stack.setCount(getInventoryStackLimit());
		}
		
		getInventoryStacks().set(slot, stack);
		
		if (!flag) {
			markTileDirty();
		}
	}
	
	@Override
	public default boolean isItemValidForSlot(int slot, ItemStack stack) {
		return true;
	}
	
	@Override
	public default int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public default void clear() {
		getInventoryStacks().clear();
	}
	
	@Override
	public default void openInventory(EntityPlayer player) {}
	
	@Override
	public default void closeInventory(EntityPlayer player) {}
	
	@Override
	public default boolean isUsableByPlayer(EntityPlayer player) {
		return player.getDistanceSq(getTilePos().getX() + 0.5D, getTilePos().getY() + 0.5D, getTilePos().getZ() + 0.5D) <= 64D;
	}
	
	@Override
	public default int getField(int id) {
		return 0;
	}
	
	@Override
	public default void setField(int id, int value) {}
	
	@Override
	public default int getFieldCount() {
		return 0;
	}
	
	@Override
	public String getName();
	
	// ISidedInventory
	
	@Override
	public default int[] getSlotsForFace(EnumFacing side) {
		return getInventoryConnection(side).getSlotsForFace();
	}
	
	@Override
	public default boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
		return getItemSorption(side, slot).canReceive();
	}
	
	@Override
	public default boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
		return getItemSorption(side, slot).canExtract();
	}
	
	// Inventory Connections
	
	public @Nonnull InventoryConnection[] getInventoryConnections();
	
	public void setInventoryConnections(@Nonnull InventoryConnection[] connections);
	
	public default @Nonnull InventoryConnection getInventoryConnection(@Nonnull EnumFacing side) {
		return getInventoryConnections()[side.getIndex()];
	}
	
	public default @Nonnull ItemSorption getItemSorption(@Nonnull EnumFacing side, int slotNumber) {
		return getInventoryConnections()[side.getIndex()].getItemSorption(slotNumber);
	}
	
	public default void setItemSorption(@Nonnull EnumFacing side, int slotNumber, @Nonnull ItemSorption sorption) {
		getInventoryConnections()[side.getIndex()].setItemSorption(slotNumber, sorption);
	}
	
	public default void toggleItemSorption(@Nonnull EnumFacing side, int slotNumber, ItemSorption.Type type, boolean reverse) {
		if (!hasConfigurableInventoryConnections()) {
			return;
		}
		getInventoryConnection(side).toggleItemSorption(slotNumber, type, reverse);
		markDirtyAndNotify(true);
	}
	
	public default boolean canConnectInventory(@Nonnull EnumFacing side) {
		return getInventoryConnection(side).canConnect();
	}
	
	public static InventoryConnection[] inventoryConnectionAll(@Nonnull List<ItemSorption> sorptionList) {
		InventoryConnection[] array = new InventoryConnection[6];
		for (int i = 0; i < 6; ++i) {
			array[i] = new InventoryConnection(sorptionList);
		}
		return array;
	}
	
	public static InventoryConnection[] inventoryConnectionAll(ItemSorption sorption) {
		return inventoryConnectionAll(Lists.newArrayList(sorption));
	}
	
	public default boolean hasConfigurableInventoryConnections() {
		return false;
	}
	
	// Item Distribution
	
	public default void pushStacks() {
		for (EnumFacing side : EnumFacing.VALUES) {
			pushStacksToSide(side);
		}
	}
	
	public default void pushStacksToSide(@Nonnull EnumFacing side) {
		if (!getInventoryConnection(side).canConnect()) {
			return;
		}
		
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		if (tile == null) {
			return;
		}
		
		/*if (ModCheck.mekanismLoaded() && tile.hasCapability(CapabilityHelper.LOGISTICAL_TRANSPORTER_CAPABILITY, side.getOpposite())) {
			ILogisticalTransporter lt = tile.getCapability(CapabilityHelper.LOGISTICAL_TRANSPORTER_CAPABILITY, side.getOpposite());
		}*/
		
		if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side.getOpposite())) {
			IItemHandler adjInv = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side.getOpposite());
			if (adjInv == null || adjInv.getSlots() < 1) {
				return;
			}
			
			boolean pushed = false;
			
			for (int i = 0; i < getInventoryStacks().size(); ++i) {
				if (!getItemSorption(side, i).canExtract() || getInventoryStacks().get(i).isEmpty()) {
					continue;
				}
				
				ItemStack initialStack = getInventoryStacks().get(i).copy();
				ItemStack remaining = NCInventoryHelper.addStackToInventory(adjInv, initialStack);
				
				if (remaining.getCount() >= initialStack.getCount()) {
					continue;
				}
				
				pushed = true;
				
				getInventoryStacks().get(i).shrink(initialStack.getCount() - remaining.getCount());
				
				if (getInventoryStacks().get(i).getCount() <= 0) {
					getInventoryStacks().set(i, ItemStack.EMPTY);
				}
			}
			
			if (pushed) {
				if (this instanceof IProcessor) {
					((IProcessor<?, ?>) this).refreshActivity();
				}
				if (this instanceof ITilePort) {
					((ITilePort<?, ?, ?, ?, ?>) this).setRefreshTargetsFlag(true);
				}
			}
		}
	}
	
	// NBT
	
	public default NBTTagCompound writeInventory(NBTTagCompound nbt) {
		ItemStackHelper.saveAllItems(nbt, getInventoryStacks());
		return nbt;
	}
	
	public default void readInventory(NBTTagCompound nbt) {
		ItemStackHelper.loadAllItems(nbt, getInventoryStacks());
	}
	
	public default NBTTagCompound writeInventoryConnections(NBTTagCompound nbt) {
		for (EnumFacing side : EnumFacing.VALUES) {
			getInventoryConnection(side).writeToNBT(nbt, side);
		}
		return nbt;
	}
	
	public default void readInventoryConnections(NBTTagCompound nbt) {
		if (!hasConfigurableInventoryConnections()) {
			return;
		}
		for (EnumFacing side : EnumFacing.VALUES) {
			getInventoryConnection(side).readFromNBT(nbt, side);
		}
	}
	
	public default NBTTagCompound writeSlotSettings(NBTTagCompound nbt) {
		for (int i = 0; i < getSizeInventory(); ++i) {
			nbt.setInteger("itemOutputSetting" + i, getItemOutputSetting(i).ordinal());
		}
		return nbt;
	}
	
	public default void readSlotSettings(NBTTagCompound nbt) {
		for (int i = 0; i < getSizeInventory(); ++i) {
			setItemOutputSetting(i, ItemOutputSetting.values()[nbt.getInteger("itemOutputSetting" + i)]);
		}
	}
	
	// Item Functions
	
	public ItemOutputSetting getItemOutputSetting(int slot);
	
	public void setItemOutputSetting(int slot, ItemOutputSetting setting);
	
	// Capabilities
	
	public default boolean hasInventorySideCapability(@Nullable EnumFacing side) {
		return side == null || getInventoryConnection(side).canConnect();
	}
	
	public default IItemHandler getItemHandler(@Nullable EnumFacing side) {
		return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new ItemHandler<>(this, side));
	}
}
