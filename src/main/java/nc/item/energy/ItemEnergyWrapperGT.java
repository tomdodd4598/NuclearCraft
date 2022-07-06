package nc.item.energy;

import static nc.config.NCConfig.rf_per_eu;

import java.util.*;
import java.util.function.BiConsumer;

import gregtech.api.capability.IElectricItem;
import nc.tile.internal.energy.EnergyStorage;
import nc.util.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "gregtech.api.capability.IElectricItem", modid = "gregtech")
public class ItemEnergyWrapperGT implements IElectricItem {
	
	protected ItemStack stack;
	protected EnergyStorage storage;
	protected int energyTier;
	
	public List<BiConsumer<ItemStack, Long>> listeners = new ArrayList<>();
	
	public ItemEnergyWrapperGT(ItemStack stack, EnergyStorage storage, int energyTier) {
		this.stack = stack;
		this.storage = storage;
		this.energyTier = energyTier;
	}
	
	@Override
	@Optional.Method(modid = "gregtech")
	public boolean canProvideChargeExternally() {
		return true;
	}
	
	@Override
	@Optional.Method(modid = "gregtech")
	public void addChargeListener(BiConsumer<ItemStack, Long> chargeListener) {
		listeners.add(chargeListener);
	}
	
	@Override
	@Optional.Method(modid = "gregtech")
	public long charge(long amount, int chargerTier, boolean ignoreTransferLimit, boolean simulate) {
		if (storage.canReceive() && energyTier >= chargerTier) {
			if (!ignoreTransferLimit) {
				amount = Math.min(amount, EnergyHelper.getMaxEUFromTier(energyTier));
			}
			int charged = NCMath.toInt(Math.min(amount, getMaxCharge() - getCharge()));
			if (!simulate) {
				changeCharge(charged);
			}
			return charged;
		}
		return 0;
	}
	
	@Override
	@Optional.Method(modid = "gregtech")
	public long discharge(long amount, int dischargerTier, boolean ignoreTransferLimit, boolean externally, boolean simulate) {
		if ((storage.canExtract() || !externally) && dischargerTier >= energyTier) {
			if (!ignoreTransferLimit) {
				amount = Math.min(amount, EnergyHelper.getMaxEUFromTier(energyTier));
			}
			int discharged = NCMath.toInt(Math.min(amount, getCharge()));
			if (!simulate) {
				changeCharge(-discharged);
			}
			return discharged;
		}
		return 0;
	}
	
	@Override
	public long getCharge() {
		return storage.getEnergyStored() / rf_per_eu;
	}
	
	public void changeCharge(long change) {
		storage.changeEnergyStored(change * rf_per_eu);
		listeners.forEach(listener -> listener.accept(stack, change));
	}
	
	@Override
	@Optional.Method(modid = "gregtech")
	public long getMaxCharge() {
		return storage.getMaxEnergyStored() / rf_per_eu;
	}
	
	@Override
	@Optional.Method(modid = "gregtech")
	public long getTransferLimit() {
		return EnergyHelper.getMaxEUFromTier(1 + energyTier);
	}
	
	@Override
	@Optional.Method(modid = "gregtech")
	public boolean canUse(long amount) {
		return getCharge() >= amount;
	}
	
	@Override
	@Optional.Method(modid = "gregtech")
	public int getTier() {
		return energyTier;
	}
}
