package nc.tile.internal.heat;

import nc.util.NCMath;
import net.minecraft.nbt.NBTTagCompound;

public class HeatBuffer {
	
	private long heatStored, heatCapacity;

	public HeatBuffer(long capacity) {
		heatCapacity = capacity;
	}
	
	public long removeHeat(long heat, boolean simulated) {
		long heatRemoved = Math.min(heatStored, heat);
		if (!simulated) heatStored -= heatRemoved;
		return heatRemoved;
	}
	
	public long addHeat(long heat, boolean simulated) {
		long heatAdded = Math.min(heatCapacity - heatStored, heat);
		if (!simulated) heatStored += heatAdded;
		return heatAdded;
	}
	
	public long getHeatStored() {
		return heatStored;
	}
	
	public long getHeatCapacity() {
		return heatCapacity;
	}
	
	public void changeHeatStored(long heat) {
		heatStored = NCMath.clamp(heatStored + heat, 0, heatCapacity);
	}
	
	/** Ignores heat capacity! */
	public void setHeatStored(long heat) {
		heatStored = Math.max(0, heat);
	}
	
	/** Ignores heat stored! */
	public void setHeatCapacity(long newCapacity) {
		heatCapacity = Math.max(0, newCapacity);
		//if (newCapacity < heatStored) setHeatStored(newCapacity);
	}
	
	public boolean isFull() {
		return heatStored >= heatCapacity;
	}
	
	public boolean isEmpty() {
		return heatStored == 0;
	}
	
	public void mergeHeatBuffers(HeatBuffer other) {
		setHeatStored(heatStored + other.heatStored);
		setHeatCapacity(heatCapacity + other.heatCapacity);
	}
	
	// NBT
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt, String name) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setLong("heatStored", heatStored);
		tag.setLong("heatCapacity", heatCapacity);
		nbt.setTag(name, tag);
		return nbt;
	}
	
	public HeatBuffer readFromNBT(NBTTagCompound nbt, String name) {
		if (nbt.hasKey(name, 10)) {
			NBTTagCompound tag = nbt.getCompoundTag(name);
			heatStored = tag.getLong("heatStored");
			heatCapacity = tag.getLong("heatCapacity");
		}
		return this;
	}
}
