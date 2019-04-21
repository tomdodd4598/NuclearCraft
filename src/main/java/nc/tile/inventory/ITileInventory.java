package nc.tile.inventory;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import nc.tile.ITile;
import nc.tile.internal.inventory.InventoryConnection;
import nc.tile.internal.inventory.ItemSorption;
import nc.util.NCInventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public interface ITileInventory extends ITile, ISidedInventory {
	
	// Inventory
	
	public @Nonnull NonNullList<ItemStack> getInventoryStacks();
	
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
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && nc.util.ItemStackHelper.areItemStackTagsEqual(stack, itemstack);
		
		if (stack.getCount() > getInventoryStackLimit()) {
			stack.setCount(getInventoryStackLimit());
		}
		
		getInventoryStacks().set(slot, stack);
		
		if (!flag) {
			markDirty();
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
	public default boolean isUsableByPlayer(EntityPlayer player) {
		return getTileWorld().getTileEntity(getTilePos()) != this ? false : player.getDistanceSq(getTilePos().getX() + 0.5D, getTilePos().getY() + 0.5D, getTilePos().getZ() + 0.5D) <= 64D;
	}
	
	@Override
	public default void openInventory(EntityPlayer player) {
		
	}
	
	@Override
	public default void closeInventory(EntityPlayer player) {
		
	}
	
	@Override
	public default int getField(int id) {
		return 0;
	}
	
	@Override
	public default void setField(int id, int value) {
		
	}
	
	@Override
	public default int getFieldCount() {
		return 0;
	}
	
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
	
	public default void setInventoryConnection(@Nonnull EnumFacing side, @Nonnull InventoryConnection connection) {
		getInventoryConnections()[side.getIndex()] = connection.copy();
	}
	
	public default @Nonnull ItemSorption getItemSorption(@Nonnull EnumFacing side, int slotNumber) {
		return getInventoryConnections()[side.getIndex()].getItemSorption(slotNumber);
	}
	
	public default void setItemSorption(@Nonnull EnumFacing side, int slotNumber, @Nonnull ItemSorption sorption) {
		getInventoryConnections()[side.getIndex()].setItemSorption(slotNumber, sorption);
	}
	
	public default void toggleItemSorption(@Nonnull EnumFacing side, int slotNumber) {
		if (!hasConfigurableInventoryConnections()) {
			return;
		}
		getInventoryConnection(side).toggleItemSorption(slotNumber);
		markAndRefresh();
	}
	
	public default boolean canConnectInventory(@Nonnull EnumFacing side) {
		return getInventoryConnection(side).canConnect();
	}
	
	public static InventoryConnection[] inventoryConnectionAll(@Nonnull List<ItemSorption> sorptionList) {
		InventoryConnection[] array = new InventoryConnection[6];
		for (int i = 0; i < 6; i++) {
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
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		IItemHandler adjInv = tile == null ? null : tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side.getOpposite());
		if (adjInv == null || adjInv.getSlots() < 1) return;
		
		for (int i = 0; i < getInventoryStacks().size(); i++) {
			if (getInventoryStacks().get(i).isEmpty()) continue;
			
			ItemStack initialStack = getInventoryStacks().get(i).copy();
			ItemStack inserted = NCInventoryHelper.addStackToInventory(adjInv, initialStack);
			
			if (inserted.getCount() >= initialStack.getCount()) continue;
			
			getInventoryStacks().get(i).shrink(initialStack.getCount() - inserted.getCount());
			
			if (getInventoryStacks().get(i).getCount() <= 0) {
				getInventoryStacks().set(i, ItemStack.EMPTY);
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
	
	// Capabilities
	
	public default boolean hasInventorySideCapability(@Nullable EnumFacing side) {
		return side == null || getInventoryConnection(side).canConnect();
	}
}
