package nc.item.energy;

import nc.tile.internal.energy.*;
import nc.util.NBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IChargableItem {
	
	static NBTTagCompound getEnergyStorageNBT(ItemStack stack) {
		if (!(stack.getItem() instanceof IChargableItem item)) {
			return null;
		}
		
		NBTTagCompound nbt = NBTHelper.getStackNBT(stack);
        if (!nbt.hasKey("energyStorage")) {
			new EnergyStorage(item.getMaxEnergyStored(stack), item.getMaxTransfer(stack), 0L).writeToNBT(nbt, "energyStorage");
		}
		
		return nbt.getCompoundTag("energyStorage");
	}
	
	default long getEnergyStored(ItemStack stack) {
		NBTTagCompound nbt = getEnergyStorageNBT(stack);
		return nbt == null ? 0L : nbt.getLong("energy");
	}
	
	default void setEnergyStored(ItemStack stack, long energy) {
		NBTTagCompound nbt = getEnergyStorageNBT(stack);
		if (nbt != null && nbt.hasKey("energy")) {
			nbt.setLong("energy", energy);
		}
	}
	
	long getMaxEnergyStored(ItemStack stack);
	
	int getMaxTransfer(ItemStack stack);
	
	boolean canReceive(ItemStack stack);
	
	boolean canExtract(ItemStack stack);
	
	EnergyConnection getEnergyConnection(ItemStack stack);
	
	int getEnergyTier(ItemStack stack);
}
