package nc.tile.internal.energy;

import static nc.config.NCConfig.rf_per_eu;

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
	public final int getEnergyStored() {
		return NCMath.toInt(getEnergyStoredLong());
	}
	
	@Override
	public final int getMaxEnergyStored() {
		return NCMath.toInt(getMaxEnergyStoredLong());
	}
	
	public int getMaxTransfer() {
		return maxTransfer;
	}
	
	public long getEnergyStoredLong() {
		return Math.min(energyStored, energyCapacity);
	}
	
	public long getMaxEnergyStoredLong() {
		return energyCapacity;
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
		if (energyReceived <= 0) {
			return 0;
		}
		
		if (!simulated) {
			changeEnergyStored(energyReceived);
		}
		return energyReceived;
	}
	
	@Override
	public int extractEnergy(int extract, boolean simulated) {
		int energyExtracted = Math.min(NCMath.toInt(energyStored), Math.min(maxTransfer, extract));
		if (energyExtracted <= 0) {
			return 0;
		}
		
		if (!simulated) {
			changeEnergyStored(-energyExtracted);
		}
		return energyExtracted;
	}
	
	public void changeEnergyStored(long energy) {
		energyStored = NCMath.clamp(energyStored + energy, 0, energyCapacity);
	}
	
	/** Ignores energy capacity! */
	public void setEnergyStored(long energy) {
		energyStored = Math.max(0, energy);
	}
	
	/** Ignores energy stored! */
	public void setStorageCapacity(long newCapacity) {
		energyCapacity = Math.max(newCapacity, rf_per_eu);
		// cullEnergyStored();
	}
	
	public void setMaxTransfer(int newMaxTransfer) {
		maxTransfer = Math.max(newMaxTransfer, rf_per_eu);
	}
	
	/** Use to remove excess stored energy */
	public void cullEnergyStored() {
		if (energyStored > energyCapacity) {
			setEnergyStored(energyCapacity);
		}
	}
	
	public boolean isFull() {
		return energyStored >= energyCapacity;
	}
	
	public boolean isEmpty() {
		return energyStored == 0;
	}
	
	public void mergeEnergyStorage(EnergyStorage other) {
		setStorageCapacity(getMaxEnergyStoredLong() + other.getMaxEnergyStoredLong());
		setEnergyStored(getEnergyStoredLong() + other.getEnergyStoredLong());
		other.setEnergyStored(0);
	}
	
	// NBT
	
	public final NBTTagCompound writeToNBT(NBTTagCompound nbt, String name) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setLong("energy", energyStored);
		tag.setLong("capacity", energyCapacity);
		nbt.setTag(name, tag);
		return nbt;
		
	}
	
	public final EnergyStorage readFromNBT(NBTTagCompound nbt, String name) {
		if (nbt.hasKey(name, 10)) {
			NBTTagCompound tag = nbt.getCompoundTag(name);
			energyStored = tag.getLong("energy");
			if (tag.hasKey("capacity", 99)) {
				energyCapacity = tag.getLong("capacity");
			}
		}
		return this;
	}
}
