package nc.item.energy;

import nc.tile.internal.energy.EnergyConnection;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IChargableItem {
	
	public static NBTTagCompound getEnergyStorageNBT(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null || !nbt.hasKey("energyStorage")) {
			return null;
		}
		return nbt.getCompoundTag("energyStorage");
	}
	
	public default long getEnergyStored(ItemStack stack) {
		NBTTagCompound nbt = getEnergyStorageNBT(stack);
		if (nbt == null) {
			return 0L;
		}
		return nbt.getLong("energy");
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
