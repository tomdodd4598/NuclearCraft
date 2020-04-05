package nc.item.energy;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import gregtech.api.capability.IElectricItem;
import nc.config.NCConfig;
import nc.tile.internal.energy.EnergyStorage;
import nc.util.EnergyHelper;
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
		if ((storage.canReceive()) && energyTier >= chargerTier) {
			if (!ignoreTransferLimit) amount = Math.min(amount, EnergyHelper.getMaxEUFromTier(energyTier));
			int charged = (int) Math.min(Math.min(amount, getMaxCharge() - getCharge()), Integer.MAX_VALUE);
			if (!simulate) changeCharge(charged);
			return charged;
		}
		return 0;
	}
	
	@Override
	@Optional.Method(modid = "gregtech")
	public long discharge(long amount, int dischargerTier, boolean ignoreTransferLimit, boolean externally, boolean simulate) {
		if ((storage.canExtract() || !externally) && dischargerTier >= energyTier) {
			if (!ignoreTransferLimit) amount = Math.min(amount, EnergyHelper.getMaxEUFromTier(energyTier));
			int discharged = (int) Math.min(Math.min(amount, getCharge()), Integer.MAX_VALUE);
			if (!simulate) changeCharge(-discharged);
			return discharged;
		}
		return 0;
	}
	
	@Override
	public long getCharge() {
		return storage.getEnergyStored()/NCConfig.rf_per_eu;
	}
	
	public void changeCharge(int change) {
		storage.changeEnergyStored(change*NCConfig.rf_per_eu);
		listeners.forEach(listener -> listener.accept(stack, (long) change));
	}
	
	@Override
	@Optional.Method(modid = "gregtech")
	public long getMaxCharge() {
		return storage.getMaxEnergyStored()/NCConfig.rf_per_eu;
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
