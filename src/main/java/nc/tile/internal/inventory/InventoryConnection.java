package nc.tile.internal.inventory;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class InventoryConnection {
	
	private @Nonnull List<ItemSorption> sorptionList;
	private final @Nonnull List<ItemSorption> defaultSorptions;
	
	public InventoryConnection(@Nonnull List<ItemSorption> sorptionList) {
		this.sorptionList = new ArrayList<ItemSorption>(sorptionList);
		defaultSorptions = new ArrayList<ItemSorption>(sorptionList);
	}
	
	private InventoryConnection(@Nonnull InventoryConnection connection) {
		sorptionList = new ArrayList<ItemSorption>(connection.sorptionList);
		defaultSorptions = new ArrayList<ItemSorption>(connection.defaultSorptions);
	}
	
	private InventoryConnection copy() {
		return new InventoryConnection(this);
	}
	
	public static InventoryConnection[] cloneArray(@Nonnull InventoryConnection[] connections) {
		InventoryConnection[] clone = new InventoryConnection[6];
		for (int i = 0; i < 6; i++) {
			clone[i] = connections[i].copy();
		}
		return clone;
	}
	
	public ItemSorption getItemSorption(int slot) {
		return sorptionList.get(slot);
	}
	
	public void setItemSorption(int slot, ItemSorption sorption) {
		sorptionList.set(slot, sorption);
	}
	
	public ItemSorption getDefaultItemSorption(int slot) {
		return defaultSorptions.get(slot);
	}
	
	public boolean canConnect() {
		for (ItemSorption sorption : sorptionList) {
			if (sorption.canConnect()) {
				return true;
			}
		}
		return false;
	}
	
	public int[] getSlotsForFace() {
		IntList slotList = new IntArrayList();
		for (int i = 0; i < sorptionList.size(); i++) {
			if (getItemSorption(i).canConnect()) {
				slotList.add(i);
			}
		}
		return slotList.stream().mapToInt(i -> i).toArray();
	}
	
	public void toggleItemSorption(int slot, ItemSorption.Type type, boolean reverse) {
		setItemSorption(slot, getItemSorption(slot).next(type, reverse));
	}
	
	public final NBTTagCompound writeToNBT(NBTTagCompound nbt, @Nonnull EnumFacing side) {
		NBTTagCompound connectionTag = new NBTTagCompound();
		for (int i = 0; i < sorptionList.size(); i++) {
			connectionTag.setInteger("sorption" + i, getItemSorption(i).ordinal());
		}
		nbt.setTag("inventoryConnection" + side.getIndex(), connectionTag);
		return nbt;

	}
	
	public final InventoryConnection readFromNBT(NBTTagCompound nbt, @Nonnull EnumFacing side) {
		if (nbt.hasKey("inventoryConnection" + side.getIndex())) {
			NBTTagCompound connectionTag = nbt.getCompoundTag("inventoryConnection" + side.getIndex());
			for (int i = 0; i < sorptionList.size(); i++) {
				if (connectionTag.hasKey("sorption" + i)) {
					setItemSorption(i, ItemSorption.values()[connectionTag.getInteger("sorption" + i)]);
				}
			}
			
		}
		return this;
	}
}
