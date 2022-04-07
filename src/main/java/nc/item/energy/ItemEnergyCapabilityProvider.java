package nc.item.energy;

import static nc.config.NCConfig.enable_gtce_eu;

import javax.annotation.*;

import gregtech.api.capability.GregtechCapabilities;
import nc.ModCheck;
import nc.tile.internal.energy.*;
import nc.util.NCMath;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.energy.CapabilityEnergy;

public class ItemEnergyCapabilityProvider implements ICapabilityProvider {
	
	private ItemStack stack;
	private EnergyStorage storage;
	private ItemEnergyWrapper wrapper;
	private ItemEnergyWrapperGT wrapperGT;
	private final int energyTier;
	
	public ItemEnergyCapabilityProvider(ItemStack stack, long capacity, int maxTransfer, long energy, EnergyConnection energyConnection, int energyTier) {
		this.stack = stack;
		storage = new EnergyStorage(capacity, maxTransfer, energy) {
			
			@Override
			public long getEnergyStoredLong() {
				NBTTagCompound nbt = IChargableItem.getEnergyStorageNBT(stack);
				if (nbt == null) {
					return 0L;
				}
				return Math.min(nbt.getLong("energy"), nbt.getLong("capacity"));
			}
			
			@Override
			public long getMaxEnergyStoredLong() {
				NBTTagCompound nbt = IChargableItem.getEnergyStorageNBT(stack);
				if (nbt == null) {
					return 0L;
				}
				return nbt.getLong("capacity");
			}
			
			@Override
			public int getMaxTransfer() {
				return maxTransfer;
			}
			
			@Override
			public void setEnergyStored(long newEnergy) {
				NBTTagCompound nbt = IChargableItem.getEnergyStorageNBT(stack);
				if (nbt != null && nbt.hasKey("energy")) {
					nbt.setLong("energy", newEnergy);
				}
			}
			
			@Override
			public void changeEnergyStored(long changeEnergy) {
				NBTTagCompound nbt = IChargableItem.getEnergyStorageNBT(stack);
				if (nbt != null && nbt.hasKey("energy")) {
					nbt.setLong("energy", NCMath.clamp(nbt.getLong("energy") + changeEnergy / stack.getCount(), 0, getMaxEnergyStored()));
				}
			}
			
			@Override
			public void setStorageCapacity(long newCapacity) {
				NBTTagCompound nbt = IChargableItem.getEnergyStorageNBT(stack);
				if (nbt != null && nbt.hasKey("capacity")) {
					nbt.setLong("capacity", newCapacity);
				}
				
			}
			
			@Override
			public void setMaxTransfer(int newMaxTransfer) {}
			
			@Override
			public boolean canReceive() {
				return energyConnection.canReceive();
			}
			
			@Override
			public boolean canExtract() {
				return energyConnection.canExtract();
			}
			
			@Override
			public int receiveEnergy(int maxReceive, boolean simulate) {
				if (!canReceive()) {
					return 0;
				}
				NBTTagCompound nbt = IChargableItem.getEnergyStorageNBT(stack);
				if (nbt == null || !nbt.hasKey("energy")) {
					return 0;
				}
				int energyReceived = Math.min(getMaxEnergyStored() - getEnergyStored(), Math.min(getMaxTransfer(), maxReceive));
				if (!simulate) {
					nbt.setLong("energy", getEnergyStored() + energyReceived / stack.getCount());
				}
				return energyReceived;
			}
			
			@Override
			public int extractEnergy(int maxExtract, boolean simulate) {
				if (!canExtract()) {
					return 0;
				}
				NBTTagCompound nbt = IChargableItem.getEnergyStorageNBT(stack);
				if (nbt == null || !nbt.hasKey("energy")) {
					return 0;
				}
				int energyExtracted = Math.min(getEnergyStored(), Math.min(getMaxTransfer(), maxExtract));
				if (!simulate) {
					nbt.setLong("energy", getEnergyStored() - energyExtracted / stack.getCount());
				}
				return energyExtracted;
			}
		};
		this.energyTier = energyTier;
	}
	
	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			if (wrapper == null) {
				wrapper = new ItemEnergyWrapper(storage);
			}
			return true;
		}
		if (ModCheck.gregtechLoaded() && enable_gtce_eu && capability == GregtechCapabilities.CAPABILITY_ELECTRIC_ITEM) {
			if (wrapperGT == null) {
				wrapperGT = new ItemEnergyWrapperGT(stack, storage, energyTier);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			if (wrapper == null) {
				wrapper = new ItemEnergyWrapper(storage);
			}
			return CapabilityEnergy.ENERGY.cast(wrapper);
		}
		if (ModCheck.gregtechLoaded() && enable_gtce_eu && capability == GregtechCapabilities.CAPABILITY_ELECTRIC_ITEM) {
			if (wrapperGT == null) {
				wrapperGT = new ItemEnergyWrapperGT(stack, storage, energyTier);
			}
			return GregtechCapabilities.CAPABILITY_ELECTRIC_ITEM.cast(wrapperGT);
		}
		return null;
	}
}
