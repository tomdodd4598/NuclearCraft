package nc.item.energy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import gregtech.api.capability.GregtechCapabilities;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.tile.internal.energy.EnergyStorage;
import nc.util.NCMath;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;

public class ItemEnergyCapabilityProvider implements ICapabilityProvider {
	
	private ItemStack stack;
	private EnergyStorage storage;
	private ItemEnergyWrapper wrapper;
	private ItemEnergyWrapperGT wrapperGT;
	private final int energyTier;
	
	public ItemEnergyCapabilityProvider(ItemStack stack, NBTTagCompound nbt, int energyTier) {
		this(stack, nbt.getLong("energy"), nbt.getLong("capacity"), nbt.getInteger("maxTransfer"), energyTier);
	}
	
	public ItemEnergyCapabilityProvider(ItemStack stack, long energy, long capacity, int maxTransfer, int energyTier) {
		this.stack = stack;
		storage = new EnergyStorage(capacity, maxTransfer, energy) {
			
			@Override
			public int getEnergyStored() {
				if (stack.hasTagCompound()) return (int) Math.min(Integer.MAX_VALUE, stack.getTagCompound().getLong("energy"));
				return 0;
			}
			
			@Override
			public int getMaxEnergyStored() {
				if (stack.hasTagCompound()) return (int) Math.min(Integer.MAX_VALUE, stack.getTagCompound().getLong("capacity"));
				return 0;
			}
			
			@Override
			public void setEnergyStored(long energy) {
				if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
				stack.getTagCompound().setLong("energy", energy);
			}
			
			@Override
			public void changeEnergyStored(long energy) {
				if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
				stack.getTagCompound().setLong("energy", NCMath.clamp(stack.getTagCompound().getLong("energy") + energy/stack.getCount(), 0, getMaxEnergyStored()));
			}
			
			@Override
			public void setStorageCapacity(long capacity) {
				if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
				stack.getTagCompound().setLong("capacity", capacity);
			}
			
			@Override
			public boolean canReceive() {
				if (stack.hasTagCompound()) return stack.getTagCompound().getInteger("maxTransfer") > 0;
				return super.canReceive();
			}
			
			@Override
			public boolean canExtract() {
				if (stack.hasTagCompound()) return stack.getTagCompound().getInteger("maxTransfer") > 0;
				return super.canExtract();
			}
			
			@Override
			public int getMaxTransfer() {
				if (stack.hasTagCompound()) return stack.getTagCompound().getInteger("maxTransfer");
				return 0;
			}
			
			@Override
			public void setMaxTransfer(int maxTransfer) {
				if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
				stack.getTagCompound().setInteger("maxTransfer", maxTransfer);
			}
			
			@Override
			public int receiveEnergy(int maxReceive, boolean simulate) {
				if (!canReceive()) return 0;
				int energyReceived = Math.min(getMaxEnergyStored() - getEnergyStored(), Math.min(getMaxTransfer(), maxReceive));
				if (!simulate) stack.getTagCompound().setLong("energy", getEnergyStored() + energyReceived/stack.getCount());
				return energyReceived;
			}
			
			@Override
			public int extractEnergy(int maxExtract, boolean simulate) {
				if (!canExtract()) return 0;
				int energyExtracted = Math.min(getEnergyStored(), Math.min(getMaxTransfer(), maxExtract));
				if (!simulate) stack.getTagCompound().setLong("energy", getEnergyStored() - energyExtracted/stack.getCount());
				return energyExtracted;
			}
		};
		this.energyTier = energyTier;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return this.getCapability(capability, facing) != null;
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			if (wrapper == null) wrapper = new ItemEnergyWrapper(storage);
			return (T) wrapper;
		}
		if (ModCheck.gregtechLoaded() && NCConfig.enable_gtce_eu && capability == GregtechCapabilities.CAPABILITY_ELECTRIC_ITEM) {
			if (wrapperGT == null) wrapperGT = new ItemEnergyWrapperGT(stack, storage, energyTier);
			return (T) wrapperGT;
		}
		return null;
	}
}
