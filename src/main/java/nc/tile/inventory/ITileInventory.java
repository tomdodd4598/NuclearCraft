package nc.tile.inventory;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import nc.tile.ITile;
import nc.tile.internal.inventory.InventoryConnection;
import nc.tile.internal.inventory.InventoryTileWrapper;
import nc.tile.internal.inventory.ItemOutputSetting;
import nc.tile.internal.inventory.ItemSorption;
import nc.util.BlockHelper;
import nc.util.NCInventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public interface ITileInventory extends ITile {
	
	// Inventory
	
	public @Nonnull NonNullList<ItemStack> getInventoryStacks();
	
	// IInventory
	
	public default boolean hasCustomName() {
		return false;
	}
	
	public default int getSizeInventory() {
		return getInventoryStacks().size();
	}
	
	public default boolean isEmpty() {
		for (ItemStack itemstack : getInventoryStacks()) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	public default ItemStack getStackInSlot(int slot) {
		return getInventoryStacks().get(slot);
	}
	
	public default ItemStack decrStackSize(int slot, int count) {
		return ItemStackHelper.getAndSplit(getInventoryStacks(), slot, count);
	}
	
	public default ItemStack removeStackFromSlot(int slot) {
		return ItemStackHelper.getAndRemove(getInventoryStacks(), slot);
	}
	
	public default void setInventorySlotContents(int slot, ItemStack stack) {
		ItemStack itemstack = getInventoryStacks().get(slot);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && nc.util.ItemStackHelper.areItemStackTagsEqual(stack, itemstack);
		
		if (stack.getCount() > getInventoryStackLimit()) {
			stack.setCount(getInventoryStackLimit());
		}
		
		getInventoryStacks().set(slot, stack);
		
		if (!flag) {
			markTileDirty();
		}
	}
	
	public default boolean isItemValidForSlot(int slot, ItemStack stack) {
		return true;
	}
	
	public default int getInventoryStackLimit() {
		return 64;
	}
	
	public default void clear() {
		getInventoryStacks().clear();
	}
	
	public default boolean isUsableByPlayer(EntityPlayer player) {
		return getTileWorld().getTileEntity(getTilePos()) != this ? false : player.getDistanceSq(getTilePos().getX() + 0.5D, getTilePos().getY() + 0.5D, getTilePos().getZ() + 0.5D) <= 64D;
	}
	
	public default void openInventory(EntityPlayer player) {}
	
	public default void closeInventory(EntityPlayer player) {}
	
	public default int getField(int id) {
		return 0;
	}
	
	public default void setField(int id, int value) {}
	
	public default int getFieldCount() {
		return 0;
	}
	
	public String getName();
	
	public ITextComponent getDisplayName();
	
	// ISidedInventory
	
	public default int[] getSlotsForFace(EnumFacing side) {
		return getInventoryConnection(side).getSlotsForFace();
	}
	
	public default boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
		return getItemSorption(side, slot).canReceive();
	}
	
	public default boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
		return getItemSorption(side, slot).canExtract();
	}
	
	// Inventory Connections
	
	public @Nonnull InventoryConnection[] getInventoryConnections();
	
	public void setInventoryConnections(@Nonnull InventoryConnection[] connections);
	
	public default @Nonnull InventoryConnection getInventoryConnection(@Nonnull EnumFacing side) {
		return getInventoryConnections()[side.getIndex()];
	}
	
	/*public default void setInventoryConnection(@Nonnull EnumFacing side, @Nonnull InventoryConnection connection) {
		getInventoryConnections()[side.getIndex()] = connection.copy();
	}*/
	
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
		markDirtyAndNotify();
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
	
	// Inventory Wrapper
	
	public @Nonnull InventoryTileWrapper getInventory();
	
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
		return writeInventoryConnectionsNormalized(nbt, EnumFacing.WEST);
	}

	public default NBTTagCompound writeInventoryConnectionsNormalized(NBTTagCompound nbt, EnumFacing facing) {
		getInventoryConnection(BlockHelper.bottom(facing)).writeToNBT(nbt, EnumFacing.DOWN);
		getInventoryConnection(BlockHelper.top(facing)).writeToNBT(nbt, EnumFacing.UP);
		getInventoryConnection(BlockHelper.left(facing)).writeToNBT(nbt, EnumFacing.NORTH);
		getInventoryConnection(BlockHelper.right(facing)).writeToNBT(nbt, EnumFacing.SOUTH);
		getInventoryConnection(BlockHelper.front(facing)).writeToNBT(nbt, EnumFacing.WEST);
		getInventoryConnection(BlockHelper.back(facing)).writeToNBT(nbt, EnumFacing.EAST);
		return nbt;
	}
	
	public default void readInventoryConnections(NBTTagCompound nbt) {
		readInventoryConnectionsNormalized(nbt, EnumFacing.WEST);
	}

	public default void readInventoryConnectionsNormalized(NBTTagCompound nbt, EnumFacing facing) {
		if (!hasConfigurableInventoryConnections()) {
			return;
		}
		getInventoryConnection(BlockHelper.bottom(facing)).readFromNBT(nbt, EnumFacing.DOWN);
		getInventoryConnection(BlockHelper.top(facing)).readFromNBT(nbt, EnumFacing.UP);
		getInventoryConnection(BlockHelper.left(facing)).readFromNBT(nbt, EnumFacing.NORTH);
		getInventoryConnection(BlockHelper.right(facing)).readFromNBT(nbt, EnumFacing.SOUTH);
		getInventoryConnection(BlockHelper.front(facing)).readFromNBT(nbt, EnumFacing.WEST);
		getInventoryConnection(BlockHelper.back(facing)).readFromNBT(nbt, EnumFacing.EAST);
	}
	
	public default NBTTagCompound writeSlotSettings(NBTTagCompound nbt) {
		for (int i = 0; i < getSizeInventory(); i++) {
			nbt.setInteger("itemOutputSetting" + i, getItemOutputSetting(i).ordinal());
		}
		return nbt;
	}
	
	public default void readSlotSettings(NBTTagCompound nbt) {
		for (int i = 0; i < getSizeInventory(); i++) {
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
	
	public default IItemHandler getItemHandlerCapability(@Nullable EnumFacing side) {
		return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(side == null ? new InvWrapper(getInventory()) : new SidedInvWrapper(getInventory(), side));
	}
}
