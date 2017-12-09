package nc.energy;


import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList({ @Optional.Interface(iface = "net.darkhax.tesla.api.ITeslaConsumer", modid = "tesla"), @Optional.Interface(iface = "net.darkhax.tesla.api.ITeslaHolder", modid = "tesla"), @Optional.Interface(iface = "net.darkhax.tesla.api.ITeslaProducer", modid = "tesla") })
public class Storage implements IEnergyStorage, ITeslaConsumer, ITeslaProducer, ITeslaHolder, INBTSerializable<NBTTagCompound> {

	public long maxReceive;
	public long maxExtract;
	
	public long energyStored;
	public long energyCapacity;
	
	public Storage(int capacity) {
		this(capacity, capacity, capacity);
	}

	public Storage(int capacity, int maxTransfer) {
		this(capacity, maxTransfer, maxTransfer);
	}

	public Storage(int capacity, int maxReceive, int maxExtract) {
		energyCapacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
	}
	
	// Tesla Energy

	public long getStoredPower() {
		return getEnergyStored();
	}

	public long getCapacity() {
		return getMaxEnergyStored();
	}

	public long takePower(long power, boolean simulated) {
		long energyExtracted = Math.min(energyStored, Math.min(maxExtract, Math.min(Integer.MAX_VALUE, power)));
		if (!simulated) energyStored -= energyExtracted;
		return energyExtracted;
	}

	public long givePower(long power, boolean simulated) {
		long energyReceived = Math.min(energyCapacity - energyStored, Math.min(maxReceive, power));
		if (!simulated) energyStored += energyReceived;
		return energyReceived;
	}
	
	// Redstone Flux
	
	public int getEnergyStored() {
		return (int) Math.min(energyStored, Integer.MAX_VALUE);
	}

	public int getMaxEnergyStored() {
		return (int) Math.min(energyCapacity, Integer.MAX_VALUE);
	}

	public boolean canExtract() {
		return true;
	}

	public boolean canReceive() {
		return true;
	}

	public int receiveEnergy(int maxReceive, boolean simulated) {
		long energyReceived = Math.min(energyCapacity - energyStored, Math.min(this.maxReceive, maxReceive));
		if (!simulated) energyStored += energyReceived;
		return (int) energyReceived;
	}

	public int extractEnergy(int maxExtract, boolean simulated) {
		long energyExtracted = Math.min(energyStored, Math.min(this.maxExtract, maxExtract));
		if (!simulated) energyStored -= energyExtracted;
		return (int) energyExtracted;
	}
	
	public void changeEnergyStored(int energy) {
		energyStored += energy;
		if (energyStored > energyCapacity) energyStored = energyCapacity;
		else if (energyStored < 0) energyStored = 0;
	}
	
	public void setEnergyStored(int energy) {
		energyStored = energy;
		if (energyStored > energyCapacity) energyStored = energyCapacity;
		else if (energyStored < 0) energyStored = 0;
	}
	
	public void setStorageCapacity(int newCapacity) {
		if(newCapacity == energyCapacity || newCapacity <= 0) return;
		if(newCapacity < energyCapacity) setEnergyStored(newCapacity);
		energyCapacity = newCapacity;
    }
	
	// NBT
	
	public NBTTagCompound serializeNBT() {
		return writeToNBT(new NBTTagCompound());
	}

	public void deserializeNBT(NBTTagCompound nbt) {
		readAll(nbt);
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		if (energyStored < 0) energyStored = 0;
		nbt.setLong("Energy", energyStored);
		return nbt;
	}
	
	public final NBTTagCompound writeAll(NBTTagCompound nbt) {
		NBTTagCompound energyTag = new NBTTagCompound();
		writeToNBT(energyTag);
		nbt.setTag("energyStorage", energyTag);
		return nbt;

	}
	
	public Storage readFromNBT(NBTTagCompound nbt) {
		energyStored = nbt.getLong("Energy");
		if (energyStored > energyCapacity) energyStored = energyCapacity;
		return this;
	}
	
	public final void readAll(NBTTagCompound nbt) {
		if (nbt.hasKey("energyStorage")) readFromNBT(nbt.getCompoundTag("energyStorage"));
	}
}
