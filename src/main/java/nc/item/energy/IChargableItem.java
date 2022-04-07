package nc.item.energy;

import nc.tile.internal.energy.*;
import nc.util.NBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IChargableItem {
	
	public static NBTTagCompound getEnergyStorageNBT(ItemStack stack) {
		if (!(stack.getItem() instanceof IChargableItem)) {
			return null;
		}
		
		NBTTagCompound nbt = NBTHelper.getStackNBT(stack);
		IChargableItem item = (IChargableItem) stack.getItem();
		if (!nbt.hasKey("energyStorage")) {
			new EnergyStorage(item.getMaxEnergyStored(stack), item.getMaxTransfer(stack), 0L).writeToNBT(nbt, "energyStorage");
		}
		
		return nbt.getCompoundTag("energyStorage");
	}
	
	public default long getEnergyStored(ItemStack stack) {
		NBTTagCompound nbt = getEnergyStorageNBT(stack);
		return nbt == null ? 0L : nbt.getLong("energy");
	}
	
	public default void setEnergyStored(ItemStack stack, long energy) {
		NBTTagCompound nbt = getEnergyStorageNBT(stack);
		if (nbt != null && nbt.hasKey("energy")) {
			nbt.setLong("energy", energy);
		}
	}
	
	public long getMaxEnergyStored(ItemStack stack);
	
	public int getMaxTransfer(ItemStack stack);
	
	public boolean canReceive(ItemStack stack);
	
	public boolean canExtract(ItemStack stack);
	
	public EnergyConnection getEnergyConnection(ItemStack stack);
	
	public int getEnergyTier(ItemStack stack);
}
