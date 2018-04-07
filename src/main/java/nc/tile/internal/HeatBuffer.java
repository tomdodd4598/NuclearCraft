package nc.tile.internal;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class HeatBuffer implements INBTSerializable<NBTTagCompound> {
	
	public long heatStored, heatCapacity;

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
		return Math.min(heatStored, Long.MAX_VALUE);
	}
	
	public long getHeatCapacity() {
		return Math.min(heatCapacity, Long.MAX_VALUE);
	}
	
	public void changeHeatStored(long energy) {
		heatStored += energy;
		if (heatStored > heatCapacity) heatStored = heatCapacity;
		else if (heatStored < 0) heatStored = 0;
	}
	
	public void setHeatStored(long energy) {
		heatStored = energy;
		if (heatStored > heatCapacity) heatStored = heatCapacity;
		else if (heatStored < 0) heatStored = 0;
	}
	
	public void setHeatCapacity(long newCapacity) {
		if(newCapacity == heatCapacity || newCapacity <= 0) return;
		heatCapacity = newCapacity;
		if(newCapacity < heatStored) setHeatStored(newCapacity);
    }
	
	public void mergeHeatBuffers(HeatBuffer other) {
		setHeatStored(heatStored + other.heatStored);
		setHeatCapacity(heatCapacity + other.heatCapacity);
	}
	
	// NBT
	
	@Override
	public NBTTagCompound serializeNBT() {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		readAll(nbt);
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		if (heatStored < 0) heatStored = 0;
		nbt.setLong("Heat", heatStored);
		return nbt;
	}
	
	public final NBTTagCompound writeAll(NBTTagCompound nbt) {
		NBTTagCompound energyTag = new NBTTagCompound();
		writeToNBT(energyTag);
		nbt.setTag("heatStorage", energyTag);
		return nbt;

	}
	
	public HeatBuffer readFromNBT(NBTTagCompound nbt) {
		heatStored = nbt.getLong("Heat");
		if (heatStored > heatCapacity) heatStored = heatCapacity;
		return this;
	}
	
	public final void readAll(NBTTagCompound nbt) {
		if (nbt.hasKey("heatStorage")) readFromNBT(nbt.getCompoundTag("heatStorage"));
	}
}
