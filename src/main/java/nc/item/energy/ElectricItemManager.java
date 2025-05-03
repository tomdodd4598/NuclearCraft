package nc.item.energy;

import ic2.api.item.IElectricItemManager;
import nc.util.NCMath;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import static nc.config.NCConfig.rf_per_eu;

public class ElectricItemManager implements IElectricItemManager {
	
	public IChargableItem item;
	
	public static ElectricItemManager getElectricItemManager(IChargableItem item) {
		ElectricItemManager manager = new ElectricItemManager();
		manager.item = item;
		
		return manager;
	}
	
	@Override
	public double charge(ItemStack stack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate) {
		if (item.canReceive(stack)) {
			double energyToStore = Math.min(Math.min(amount * rf_per_eu, Math.pow(10, 1 + item.getEnergyTier(stack)) * rf_per_eu), item.getMaxEnergyStored(stack) - item.getEnergyStored(stack));
			
			if (!simulate) {
				item.setEnergyStored(stack, item.getEnergyStored(stack) + NCMath.toInt(energyToStore / stack.getCount()));
			}
			
			return NCMath.toInt(Math.round(energyToStore / rf_per_eu));
		}
		return 0;
	}
	
	@Override
	public double discharge(ItemStack stack, double amount, int tier, boolean ignoreTransferLimit, boolean externally, boolean simulate) {
		if (item.canExtract(stack)) {
			double energyToGive = Math.min(Math.min(amount * rf_per_eu, Math.pow(10, 1 + item.getEnergyTier(stack)) * rf_per_eu), item.getEnergyStored(stack));
			
			if (!simulate) {
				item.setEnergyStored(stack, item.getEnergyStored(stack) - NCMath.toInt(energyToGive / stack.getCount()));
			}
			
			return Math.round(energyToGive / rf_per_eu);
		}
		return 0;
	}
	
	@Override
	public double getCharge(ItemStack stack) {
		return (double) item.getEnergyStored(stack) / (double) rf_per_eu;
	}
	
	@Override
	public double getMaxCharge(ItemStack stack) {
		return (double) item.getMaxEnergyStored(stack) / (double) rf_per_eu;
	}
	
	@Override
	public boolean canUse(ItemStack stack, double amount) {
		return getCharge(stack) >= amount;
	}
	
	@Override
	public boolean use(ItemStack stack, double amount, EntityLivingBase entity) {
		return false;
	}
	
	@Override
	public void chargeFromArmor(ItemStack stack, EntityLivingBase entity) {
	
	}
	
	@Override
	public String getToolTip(ItemStack stack) {
		return null;
	}
	
	@Override
	public int getTier(ItemStack stack) {
		return item.getEnergyTier(stack);
	}
}
