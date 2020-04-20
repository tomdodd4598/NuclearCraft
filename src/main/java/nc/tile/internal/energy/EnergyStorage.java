package nc.tile.internal.energy;

import nc.config.NCConfig;
import nc.util.NCMath;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyStorage implements IEnergyStorage {
	
	private long energyStored, energyCapacity;
	private int maxTransfer;
	
	public EnergyStorage(long capacity) {
		this(capacity, NCMath.toInt(capacity));
	}
	
	public EnergyStorage(long capacity, int maxTransfer) {
		setStorageCapacity(capacity);
		setMaxTransfer(maxTransfer);
	}
	
	public EnergyStorage(long capacity, int maxTransfer, long energy) {
		this(capacity, maxTransfer);
		setEnergyStored(energy);
	}
	
	// Forge Energy
	
	@Override
	public int getEnergyStored() {
		return NCMath.toInt(energyStored);
	}
	
	@Override
	public int getMaxEnergyStored() {
		return NCMath.toInt(energyCapacity);
	}
	
	public int getMaxTransfer() {
		return maxTransfer;
	}
	
	@Override
	public boolean canReceive() {
		return true;
	}
	
	@Override
	public boolean canExtract() {
		return true;
	}
	
	@Override
	public int receiveEnergy(int receive, boolean simulated) {
		int energyReceived = Math.min(NCMath.toInt(energyCapacity - energyStored), Math.min(maxTransfer, receive));
		if (!simulated) changeEnergyStored(energyReceived);
		return energyReceived;
	}
	
	@Override
	public int extractEnergy(int extract, boolean simulated) {
		int energyExtracted = Math.min(NCMath.toInt(energyStored), Math.min(maxTransfer, extract));
		if (!simulated) changeEnergyStored(-energyExtracted);
		return energyExtracted;
	}
	
	public void changeEnergyStored(long energy) {
		energyStored += energy;
		if (energyStored > energyCapacity) energyStored = energyCapacity;
		else if (energyStored < 0) energyStored = 0;
	}
	
	public void setEnergyStored(long energy) {
		energyStored = energy;
		if (energyStored > energyCapacity) energyStored = energyCapacity;
		else if (energyStored < 0) energyStored = 0;
	}
	
	public void setStorageCapacity(long newCapacity) {
		if (newCapacity == energyCapacity || newCapacity <= 0) return;
		energyCapacity = Math.max(newCapacity, NCConfig.rf_per_eu);
		if (newCapacity < energyStored) setEnergyStored(newCapacity);
	}
	
	public void mergeEnergyStorage(EnergyStorage other) {
		setStorageCapacity(getMaxEnergyStored() + other.getMaxEnergyStored());
		setEnergyStored(getEnergyStored() + other.getEnergyStored());
	}
	
	public void setMaxTransfer(int newMaxTransfer) {
		if (newMaxTransfer < 0) return;
		if (newMaxTransfer != maxTransfer) maxTransfer = Math.max(newMaxTransfer, NCConfig.rf_per_eu);
	}
	
	// NBT
	
	public final NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		NBTTagCompound storageTag = new NBTTagCompound();
		if (energyStored < 0) {
			energyStored = 0;
		}
		storageTag.setLong("energy", energyStored);
		nbt.setTag("energyStorage", storageTag);
		return nbt;

	}
	
	public final EnergyStorage readFromNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("energyStorage")) {
			NBTTagCompound storageTag = nbt.getCompoundTag("energyStorage");
			energyStored = storageTag.getLong("energy");
			if (energyStored > energyCapacity) {
				energyStored = energyCapacity;
			}
		}
		return this;
	}
}
