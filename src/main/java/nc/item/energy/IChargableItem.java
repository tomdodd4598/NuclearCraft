package nc.item.energy;

import nc.tile.internal.energy.EnergyConnection;
import net.minecraft.item.ItemStack;

public interface IChargableItem {
	
	public int getEnergyStored(ItemStack stack);
	
	public void setEnergyStored(ItemStack stack, int amount);
	
	public int getMaxEnergyStored(ItemStack stack);
	
	public int getMaxTransfer(ItemStack stack);
	
	public boolean canReceive(ItemStack stack);
	
	public boolean canExtract(ItemStack stack);
	
	public EnergyConnection getEnergyConnection(ItemStack stack);
	
	public int getEnergyTier(ItemStack stack);
}
