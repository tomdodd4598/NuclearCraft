package nc.tile.internal.inventory;

import nc.tile.inventory.ITileInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;

public class InventoryTileWrapper<T extends TileEntity & ITileInventory> implements ISidedInventory {
	
	public final T tile;
	
	public InventoryTileWrapper(T tile) {
		this.tile = tile;
	}
	
	// IInventory
	
	@Override
	public boolean hasCustomName() {
		return tile.hasCustomName();
	}
	
	@Override
	public int getSizeInventory() {
		return tile.getSizeInventory();
	}
	
	@Override
	public boolean isEmpty() {
		return tile.isEmpty();
	}
	
	@Override
	public ItemStack getStackInSlot(int slot) {
		return tile.getStackInSlot(slot);
	}
	
	@Override
	public ItemStack decrStackSize(int slot, int count) {
		return tile.decrStackSize(slot, count);
	}
	
	@Override
	public ItemStack removeStackFromSlot(int slot) {
		return tile.removeStackFromSlot(slot);
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		tile.setInventorySlotContents(slot, stack);
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return tile.isItemValidForSlot(slot, stack);
	}
	
	@Override
	public int getInventoryStackLimit() {
		return tile.getInventoryStackLimit();
	}
	
	@Override
	public void clear() {
		tile.clear();
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return tile.isUsableByPlayer(player);
	}
	
	@Override
	public void openInventory(EntityPlayer player) {
		tile.openInventory(player);
	}
	
	@Override
	public void closeInventory(EntityPlayer player) {
		tile.closeInventory(player);
	}
	
	@Override
	public int getField(int id) {
		return tile.getField(id);
	}
	
	@Override
	public void setField(int id, int value) {
		tile.setField(id, value);
	}
	
	@Override
	public int getFieldCount() {
		return tile.getFieldCount();
	}
	
	@Override
	public void markDirty() {
		tile.markDirty();
	}

	@Override
	public String getName() {
		return tile.getName();
	}

	@Override
	public ITextComponent getDisplayName() {
		return tile.getDisplayName();
	}
	
	// ISidedInventory
	
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return tile.getSlotsForFace(side);
	}
	
	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
		return tile.canInsertItem(slot, stack, side);
	}
	
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
		return tile.canExtractItem(slot, stack, side);
	}
}
