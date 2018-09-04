package nc.item.energy;

import gregtech.api.capability.GregtechCapabilities;
import nc.ModCheck;
import nc.tile.internal.energy.EnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
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
		this(stack, nbt.getInteger("energy"), nbt.getInteger("capacity"), nbt.getInteger("maxReceive"), nbt.getInteger("maxExtract"), energyTier);
	}
	
	public ItemEnergyCapabilityProvider(ItemStack stack, int energy, int capacity, int maxReceive, int maxExtract, int energyTier) {
		this.stack = stack;
		storage = new EnergyStorage(capacity, maxReceive, maxExtract, energy) {
			
			@Override
			public int getEnergyStored() {
				if (stack.hasTagCompound()) return stack.getTagCompound().getInteger("energy");
				return 0;
			}
			
			@Override
			public int getMaxEnergyStored() {
				if (stack.hasTagCompound()) return stack.getTagCompound().getInteger("capacity");
				return 0;
			}
			
			@Override
			public void setEnergyStored(int energy) {
				if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
				stack.getTagCompound().setInteger("energy", energy);
			}
			
			@Override
			public void changeEnergyStored(int energy) {
				if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
				stack.getTagCompound().setInteger("energy", MathHelper.clamp(stack.getTagCompound().getInteger("energy") + energy, 0, getMaxEnergyStored()));
			}
			
			@Override
			public void setStorageCapacity(int capacity) {
				if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
				stack.getTagCompound().setInteger("capacity", capacity);
			}
			
			@Override
			public int getMaxReceive() {
				if (stack.hasTagCompound()) return stack.getTagCompound().getInteger("maxReceive");
				return 0;
			}
			
			@Override
			public int getMaxExtract() {
				if (stack.hasTagCompound()) return stack.getTagCompound().getInteger("maxExtract");
				return 0;
			}
			
			@Override
			public boolean canReceive() {
				if (stack.hasTagCompound()) return stack.getTagCompound().getInteger("maxReceive") > 0;
				return super.canReceive();
			}
			
			@Override
			public boolean canExtract() {
				if (stack.hasTagCompound()) return stack.getTagCompound().getInteger("maxExtract") > 0;
				return super.canExtract();
			}
			
			@Override
			public void setMaxReceive(int maxReceive) {
				if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
				stack.getTagCompound().setInteger("maxReceive", maxReceive);
			}
			
			@Override
			public void setMaxExtract(int maxExtract) {
				if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
				stack.getTagCompound().setInteger("maxExtract", maxExtract);
			}
			
			@Override
			public int getMaxTransfer() {
				if (stack.hasTagCompound()) return Math.max(stack.getTagCompound().getInteger("maxReceive"), stack.getTagCompound().getInteger("maxExtract"));
				return 0;
			}
			
			@Override
			public void setMaxTransfer(int transfer) {
				if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
				stack.getTagCompound().setInteger("maxExtract", transfer);
				stack.getTagCompound().setInteger("maxReceive", transfer);
			}
			
			@Override
			public int receiveEnergy(int maxReceive, boolean simulate) {
				if (!canReceive()) return 0;
				int energyReceived = Math.min(getMaxEnergyStored() - getEnergyStored(), Math.min(getMaxReceive(), maxReceive));
				if (!simulate) stack.getTagCompound().setInteger("energy", getEnergyStored() + energyReceived);
				return energyReceived;
			}
			
			@Override
			public int extractEnergy(int maxExtract, boolean simulate) {
				if (!canExtract()) return 0;
				int energyExtracted = Math.min(getEnergyStored(), Math.min(getMaxExtract(), maxExtract));
				if (!simulate) stack.getTagCompound().setInteger("energy", getEnergyStored() - energyExtracted);
				return energyExtracted;
			}
		};
		this.energyTier = energyTier;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return this.getCapability(capability, facing) != null;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			if(wrapper == null) wrapper = new ItemEnergyWrapper(storage);
			return (T) wrapper;
		}
		if (ModCheck.gregtechLoaded()) if (capability == GregtechCapabilities.CAPABILITY_ELECTRIC_ITEM) {
			if(wrapperGT == null) wrapperGT = new ItemEnergyWrapperGT(stack, storage, energyTier);
			return (T) wrapperGT;
		}
		return null;
	}
}
