package nc.item.fission;

import net.minecraft.item.ItemStack;

public interface IFissionableItem {
	
	public double getBaseTime(ItemStack stack);
	
	public double getBasePower(ItemStack stack);
	
	public double getBaseHeat(ItemStack stack);
	
	public String getFuelName(ItemStack stack);
	
}
